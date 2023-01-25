package ch.epfl.tchu.net;

import java.io.*;
import java.net.Socket;

import ch.epfl.tchu.game.Player;

import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * Allows to create the remote client for the chat.
 * @author Jérémy Chaverot (315858)
 */
public final class RemoteChatClient implements ChatSystem {

    private final Player player;
    private final Socket socket;
    private final BufferedReader reader;
    private final BufferedWriter writer;


    /**
     * Public constructor.
     * @param player (Player) : player who is given remote access.
     * @param name (String) : name used to connect to the proxy.
     * @param port (int) : port number used to connect to the proxy.
     * @throws UncheckedIOException if an unchecked exception is found.
     */
    public RemoteChatClient(Player player, String name, int port) {
        this.player = player;
        try {
            socket = new Socket(name, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), US_ASCII));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), US_ASCII));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * When this method is run, allows to realize the exchanges for the chat with the proxy.
     * @throws UncheckedIOException if an unchecked exception is found.
     */
    public void run() {
        try {
            String message;
            while ((message = reader.readLine()) != null)
                player.receiveMessage(Serdes.stringSerde.deserialize(message));

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * To send a message to the remote chat proxy.
     * @param message (String) : the message to be send.
     */
    @Override
    public void send(String message) {
        try {
            writer.write(Serdes.stringSerde.serialize(message));
            writer.write('\n');
            writer.flush();

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
