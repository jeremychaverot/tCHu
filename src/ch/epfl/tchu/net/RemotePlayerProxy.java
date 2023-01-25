package ch.epfl.tchu.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static java.nio.charset.StandardCharsets.US_ASCII;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Player;
import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.game.PlayerState;
import ch.epfl.tchu.game.PublicGameState;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Ticket;

/**
 * RemotePlayerProxy creates a proxy of a remote player, which is to be connected to the client.
 * @author Cristian Safta (324694)
 */
public final class RemotePlayerProxy implements Player {

    private final BufferedWriter w;
    private final BufferedReader r;

    /**
     * Public constructor.
     * @param socket (Socket) : the socket of the client.
     * @throws UncheckedIOException if an unchecked exception is found.
     */
    public RemotePlayerProxy(Socket socket) {
        try {
            w = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), US_ASCII));
            r = new BufferedReader(new InputStreamReader(socket.getInputStream(), US_ASCII));

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Method being called at the beginning of a game to communicate to the player his id and the names of all the players
     * @param ownId (PlayerId) : the id of the player
     * @param playerNames (Map<PlayerId, String>) : a map that contains the ids and the name of all the players in the game
     */
    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        List<String> names = new ArrayList<>();
            for (int i = 0 ; i < playerNames.size() ; i++)
                names.add(playerNames.get(PlayerId.ALL.get(i)));

        String message = Serdes.playerIdSerde.serialize(ownId) + " " + Serdes.stringListSerde.serialize(names);
        sendMessage(MessageId.INIT_PLAYERS, message);
    }

    /**
     * Method that is called each time that some info needs to be communicated to the player
     * @param info (String) : the info that needs to be communicated to the player
     */
    @Override
    public void receiveInfo(String info) {
        String message = Serdes.stringSerde.serialize(info);
        sendMessage(MessageId.RECEIVE_INFO, message);
    }

    /**
     * Method that is called each time that some message needs to be communicated to the player
     * @param message (String) : the message that needs to be communicated to the player
     */
    @Override
    public void receiveMessage(String message) { } // Useless here.

    /**
     * Method that is called when the state of the game changes to communicate to the player the state of
     * the game (only the components that he can view) and his own state
     * @param newState (PublicGameState) : the state of the game
     * @param ownState (PlayerState) : the state of the player
     */
    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {
        String message = Serdes.publicGameStateSerde.serialize(newState) + " " + Serdes.playerStateSerde.serialize(ownState);
        sendMessage(MessageId.UPDATE_STATE, message);
    }

    /**
     * Method that is called at the beginning of a game to communicate to the player the 5 tickets that he got
     * @param tickets (SortedBag<Ticket>) :  a sorted bag containing the tickets that the player received
     */
    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
        String message = Serdes.ticketSortedBagSerde.serialize(tickets);
        sendMessage(MessageId.SET_INITIAL_TICKETS, message);
    }

    /**
     * Method called at the beginning of a game that asks the player which of the tickets that were
     * distributed to him he wants to keep
     * @return chooseInitialTickets (SortedBag<Ticket>) : a sorted bag that contains the tickets the player wants to keep
     */
    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        sendMessage(MessageId.CHOOSE_INITIAL_TICKETS);
        return Serdes.ticketSortedBagSerde.deserialize(receiveMessage());
    }

    /**
     * Method being called when the turn of the player starts to find what kind of action he wants to do
     * @return nextTurn (TurnKind) : the type of action that the player wants to do during the round
     */
    @Override
    public TurnKind nextTurn() {
        sendMessage(MessageId.NEXT_TURN);
        return Serdes.turnKindSerde.deserialize(receiveMessage());
    }

    /**
     * Method being called when a player decides to draw additional tickets during a round to make him know which tickets
     * he drew and which ones he wants to keep
     * @param options (SortedBag<Ticket>) : the tickets he drew
     * @return chooseTickets (SortedBag<Ticket>) : the tickets he wants to keep
     */
    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        String message = Serdes.ticketSortedBagSerde.serialize(options);
        sendMessage(MessageId.CHOOSE_TICKETS, message);
        return Serdes.ticketSortedBagSerde.deserialize(receiveMessage());
    }

    /**
     * Method called when the player has decided to draw car/locomotive card that indicated from where he wished to draw them
     * (0-4 for the face-up cards cards and -1 from the deck)
     * @return drawSlot (int) : the slot from which the player draws the card
     */
    @Override
    public int drawSlot() {
        sendMessage(MessageId.DRAW_SLOT);
        return Serdes.integerSerde.deserialize(receiveMessage());
    }

    /**
     * Method called when the player attempts to seize a route in order to know which route it is
     * @return claimedRoute (Route) : the route the player attempts to seize
     */
    @Override
    public Route claimedRoute() {
        sendMessage(MessageId.ROUTE);
        return Serdes.routeSerde.deserialize(receiveMessage());
    }

    /**
     * Method that is called when the player attempts to claim a route that indicated which cards he initially uses to do so
     * @return initialClaimCards (SortedBag<Card>) : the cards he uses to attempt to claim the route
     */
    @Override
    public SortedBag<Card> initialClaimCards() {
        sendMessage(MessageId.CARDS);
        return Serdes.cardSortedBagSerde.deserialize(receiveMessage());
    }

    /**
     * Method which is called when the player has decided to try to seize a tunnel and additional cards are needed,
     * in order to know which card(s) he wishes to use for this, the possibilities being passed to him in argument;
     * if the returned multiset is empty, it means that the player does not wish (or cannot) choose one of these possibilities
     * @param options (List<SortedBag<Card>>) : a list containing the possible sets of cards the player
     *                                          can use to claim the tunnel
     * @return chooseAdditionalCards (SortedBag<Card>) : the cards the player chooses to use to claim the tunnel
     *                                                   (empty if he cannot do so)
     */
    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        String message = Serdes.listOfSortedBagOfCardsSerde.serialize(options);
        sendMessage(MessageId.CHOOSE_ADDITIONAL_CARDS, message);
        return Serdes.cardSortedBagSerde.deserialize(receiveMessage());
    }


    //Helper methods used to send a message to the client
    private void sendMessage(MessageId messageId, String serializedMessage) {
        try {
            w.write(messageId.name() + " " + serializedMessage);
            w.write('\n');
            w.flush();

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void sendMessage(MessageId messageId) {
        try {
            w.write(messageId.name());
            w.write('\n');
            w.flush();

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    //Helper method used to read a message from the client
    private String receiveMessage() {
        try {
            return r.readLine();

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}

