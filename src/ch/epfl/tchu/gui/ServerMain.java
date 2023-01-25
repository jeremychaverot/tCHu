package ch.epfl.tchu.gui;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.net.RemoteChatProxy;
import ch.epfl.tchu.net.RemotePlayerProxy;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Class representing the server of the game.
 * @author Cristian Safta (324694)
 * @author Jeremy Chaverot (315858)
 */
public final class ServerMain extends Application {

    /**
     * The method main, call the method start.
     * @param args(String[]) : launch arguments that are the names of the players in the right order
     *                       (2 players and Ada, Charles by default).
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        ServerSocket socketGame, socketChat;
        List<String> defaultNames = List.of("Ada", "Charles", "Alice", "Bob", "Emma", "Romain", "Joseph", "Camille");
        List<String> arguments = getParameters().getRaw();
        List<Socket> sockets = new ArrayList<>();
        List<RemotePlayerProxy> remotePlayers = new ArrayList<>();

        int numberOfPlayers = 2;
            if (!arguments.isEmpty()) {
                numberOfPlayers = Objects.checkIndex(Integer.parseInt(arguments.get(0)), PlayerId.COUNT + 1);
                if (arguments.size() > numberOfPlayers + 1) // In case that there are more given names than the number of players.
                    arguments = arguments.subList(0, numberOfPlayers + 1);
            }

        try {
            socketGame = new ServerSocket(5108);
            socketChat = new ServerSocket(5109);
                for (int i = 0 ; i < numberOfPlayers - 1 ; i++) {
                    sockets.add(socketGame.accept());
                    remotePlayers.add(new RemotePlayerProxy(sockets.get(i)));
                }

            Map<PlayerId, String> playerNames = new HashMap<>();
                if (!arguments.isEmpty()) {
                    for (int i = 0 ; i < arguments.size() - 1 ; i++)
                        playerNames.put(PlayerId.ALL.get(i), arguments.get(i+1));
                    for (int i = arguments.size() - 1 ; i < numberOfPlayers ; i++)
                        playerNames.put(PlayerId.ALL.get(i), defaultNames.get(i));
                } else for (int i = 0 ; i < numberOfPlayers ; i++) {
                    playerNames.put(PlayerId.ALL.get(i), defaultNames.get(i));
                }

            Map<PlayerId, Player> players = new HashMap<>();
            GraphicalPlayerAdapter player1 = new GraphicalPlayerAdapter();
                players.put(PlayerId.PLAYER_1, player1);
                for (int i = 1 ; i < numberOfPlayers; i++)
                    players.put(PlayerId.ALL.get(i), remotePlayers.get(i-1));

            RemoteChatProxy chatProxy = new RemoteChatProxy(player1);
            player1.setChatSystem(chatProxy);

            new Thread(() -> {
                while (true) {
                    try {
                        Socket socket = socketChat.accept();
                        new Thread(() -> {
                            chatProxy.addClient(socket);
                            chatProxy.startReading(socket);
                        }).start();
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                }
            }).start();

            Thread gameThread = new Thread(() -> Game.play(players, playerNames, SortedBag.of(UsaMap.tickets()), new Random()));
            gameThread.setDaemon(true);
            gameThread.start();

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
