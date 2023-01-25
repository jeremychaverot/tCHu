package ch.epfl.tchu.net;

public interface ChatSystem {

    /**
     * To send a message to all the other players.
     * @param message (String) : the message to be sent.
     */
    public abstract void send(String message);
}
