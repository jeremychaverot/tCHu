package ch.epfl.tchu.net;

import ch.epfl.tchu.game.Player;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * Allows to create the remote proxy for the chat.
 * @author Jérémy Chaverot (315858)
 */
public class RemoteChatProxy implements ChatSystem {

    private final Player player;
    private final List<Socket> sockets;

    /**
     * Public constructor to build a remote chat proxy.
     */
    public RemoteChatProxy(Player player) {
        this.player = player;
        sockets = new ArrayList<>();
    }

    /**
     * To send a message to all the clients, i.e. all the players.
     * @param message (String) : the message we want to send.
     */
    private void sendAll(String message) {
        player.receiveMessage(Serdes.stringSerde.deserialize(message));

        sockets.forEach(s -> {
            try {
                var writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), US_ASCII));
                writer.write(message);
                writer.write('\n');
                writer.flush();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }

    /**
     * To add bind a new client the the chat proxy.
     * @param socket (Socket) : the socket the chat client is on.
     */
    public void addClient(Socket socket) {
        sockets.add(socket);
    }

    /**
     * To start the connection between the proxy and the given client, and so the proxy can receive the messages.
     * When a message is received, it is sent to all the players.
     */
    public void startReading(Socket socket) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), US_ASCII));
            String message;
            while ((message = reader.readLine()) != null)
                sendAll(message);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * When the proxy player wants to send a message to the client players.
     * @param message (String) : the message to be send.
     */
    @Override
    public void send(String message) {
        sendAll(Serdes.stringSerde.serialize(message));
    }

}
