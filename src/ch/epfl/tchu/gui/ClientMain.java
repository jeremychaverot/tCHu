package ch.epfl.tchu.gui;

import java.util.List;

import ch.epfl.tchu.net.RemoteChatClient;
import ch.epfl.tchu.net.RemotePlayerClient;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Class that represents the game client.
 * @author Cristian Safta (324694)
 * @author Jeremy Chaverot (315858)
 */
public final class ClientMain extends Application {

    /**
     * The method main, call the method start.
     * @param args(String[]) : launch arguments that are the host name and the port (localhost and 5108 by default),
     *                       and the host name and the port for the chat system (localhost and 5109 by default).
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        String hostName, chatHostName;
        int port, chatPort;
        List<String> arguments = getParameters().getRaw();

        switch (arguments.size()) {
            case 0:
                hostName = "localhost";
                port = 5108;
                chatHostName = "localhost";
                chatPort = 5109;
                break;
            case 1:
                hostName = arguments.get(0);
                port = 5108;
                chatHostName = "localhost";
                chatPort = 5109;
                break;
            case 2:
                hostName = arguments.get(0);
                port = Integer.parseInt(arguments.get(1));
                chatHostName = "localhost";
                chatPort = 5109;
                break;
            case 3:
                hostName = arguments.get(0);
                port = Integer.parseInt(arguments.get(1));
                chatHostName = arguments.get(2);
                chatPort = 5109;
                break;
            default:
                hostName = arguments.get(0);
                port = Integer.parseInt(arguments.get(1));
                chatHostName = arguments.get(2);
                chatPort = Integer.parseInt(arguments.get(3));
                break;
        }

        GraphicalPlayerAdapter playerAdapter = new GraphicalPlayerAdapter();
        RemoteChatClient chatClient = new RemoteChatClient(playerAdapter, chatHostName, chatPort);
        playerAdapter.setChatSystem(chatClient);
        RemotePlayerClient playerClient = new RemotePlayerClient(playerAdapter, hostName, port);

        new Thread(() -> chatClient.run()).start();
        Thread gameThread = new Thread(() -> playerClient.run());
        gameThread.setDaemon(true);
        gameThread.start();
        
    }

}
