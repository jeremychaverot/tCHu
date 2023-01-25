package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.net.ChatSystem;
import javafx.application.Platform;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * The goal of this class is to adapt an instance of GraphicalPlayer in a value of type Player, using also threads.
 * @author Jérémy Chaverot (315858)
 */
public final class GraphicalPlayerAdapter implements Player {

    private final BlockingQueue<SortedBag<Ticket>> ticketsBlockingQueue;
    private final BlockingQueue<SortedBag<Card>> cardsBlockingQueue;
    private final BlockingQueue<Integer> integerBlockingQueue;
    private final BlockingQueue<TurnKind> turnKindBlockingQueue;
    private final BlockingQueue<Route> routeBlockingQueue;
    private final BlockingQueue<GraphicalPlayer> graphicalPlayerBlockingQueue;
    private ChatSystem chatSystem;
    private GraphicalPlayer graphicalPlayer;

    /**
     * Public constructor to create an instance of GraphicalPlayerAdapter.
     * Initialize the blocking queues.
     */
    public GraphicalPlayerAdapter() {
        ticketsBlockingQueue = new ArrayBlockingQueue<>(1);
        cardsBlockingQueue = new ArrayBlockingQueue<>(1);
        integerBlockingQueue = new ArrayBlockingQueue<>(1);
        turnKindBlockingQueue = new ArrayBlockingQueue<>(1);
        routeBlockingQueue = new ArrayBlockingQueue<>(1);
        graphicalPlayerBlockingQueue = new ArrayBlockingQueue<>(1);
    }

    /**
     * To set the chat system that will be given to the graphical player (either a proxy or a client).
     * @param chatSystem (ChatSystem) : the chat that will be given to the player.
     */
    public void setChatSystem(ChatSystem chatSystem) {
        this.chatSystem = chatSystem;
    }

    /**
     * Method being called at the beginning of a game to communicate to the player his id and the names of all the players.
     * @param ownId       (PlayerId) : the id of the player.
     * @param playerNames (Map<PlayerId, String>) : a map that contains the ids and the name of all the players in the game.
     */
    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        Platform.runLater(() -> {
            Sounds.playBackgroundMusic();
            put(graphicalPlayerBlockingQueue, new GraphicalPlayer(ownId, playerNames));
            graphicalPlayer = take(graphicalPlayerBlockingQueue);
            graphicalPlayer.setChatSystem(chatSystem);
        });
    }

    /**
     * Method that is called each time that some info needs to be communicated to the player.
     * @param info (String) : the info that needs to be communicated to the player.
     */
    @Override
    public void receiveInfo(String info) {
        Platform.runLater(() -> graphicalPlayer.receiveInfo(info));
    }

    /**
     * Method that is called each time that some message needs to be communicated to the player.
     * @param message (String) : the message that needs to be communicated to the player.
     */
    @Override
    public void receiveMessage(String message) {
         graphicalPlayer.receiveMessage(message);
    }

    /**
     * Method that is called when the state of the game changes to communicate to the player the state of
     * the game (only the components that he can view) and his own state.
     * @param newState (PublicGameState) : the state of the game.
     * @param ownState (PlayerState) : the state of the player.
     */
    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {
        Platform.runLater(() -> graphicalPlayer.setState(newState, ownState));
    }

    /**
     * Method that is called at the beginning of a game to communicate to the player the 5 tickets that he got.
     * @param tickets (SortedBag<Ticket>) : a sorted bag containing the tickets that the player received.
     */
    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
        assert ticketsBlockingQueue.isEmpty();

        Platform.runLater(() -> graphicalPlayer.chooseTickets(tickets,
                t -> put(ticketsBlockingQueue, t)));
    }

    /**
     * Method called at the beginning of a game that asks the player which of the tickets that were
     * distributed to him he wants to keep.
     * @return chooseInitialTickets (SortedBag<Ticket>) : a sorted bag that contains the tickets the player wants to keep.
     */
    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        return take(ticketsBlockingQueue);
    }

    /**
     * Method being called when the turn of the player starts to find what kind of action he wants to do.
     * @return nextTurn (TurnKind) : the type of action that the player wants to do during the round.
     */
    @Override
    public TurnKind nextTurn() {
        assert turnKindBlockingQueue.isEmpty();
        assert integerBlockingQueue.isEmpty();
        assert routeBlockingQueue.isEmpty();
        assert cardsBlockingQueue.isEmpty();

        Platform.runLater(() -> graphicalPlayer.startTurn(
                () -> put(turnKindBlockingQueue, TurnKind.DRAW_TICKETS),

                slot -> {
                    put(integerBlockingQueue, slot);
                    put(turnKindBlockingQueue, TurnKind.DRAW_CARDS);
                },

                (route, initialClaimCards) -> {
                    put(routeBlockingQueue, route);
                    put(cardsBlockingQueue, initialClaimCards);
                    put(turnKindBlockingQueue, TurnKind.CLAIM_ROUTE);
                }
        ));

        return take(turnKindBlockingQueue);
    }

    /**
     * Method being called when a player decides to draw additional tickets during a round to make him know which tickets
     * he drew and which ones he wants to keep.
     * @param options (SortedBag<Ticket>) : the tickets he drew.
     * @return chooseTickets (SortedBag<Ticket>) : the tickets he wants to keep.
     */
    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        assert ticketsBlockingQueue.isEmpty();

        Platform.runLater(() -> graphicalPlayer.chooseTickets(options, t -> put(ticketsBlockingQueue, t)));

        return take(ticketsBlockingQueue);
    }

    /**
     * Method called when the player has decided to draw car/locomotive card that indicated from where he wished to draw them
     * (0-4 for the face-up cards cards and -1 from the deck).
     * @return drawSlot (int) : the slot from which the player draws the card.
     */
    @Override
    public int drawSlot() {
        if (!integerBlockingQueue.isEmpty())
            return integerBlockingQueue.remove();

        Platform.runLater(() -> graphicalPlayer.drawCard(c -> put(integerBlockingQueue, c)));

        return take(integerBlockingQueue);
    }

    /**
     * Method called when the player attempts to seize a route in order to know which route it is.
     * @return claimedRoute (Route) : the route the player attempts to seize.
     */
    @Override
    public Route claimedRoute() {
        return routeBlockingQueue.remove();
    }

    /**
     * Method that is called when the player attempts to claim a route that indicated which cards he initially uses to do so.
     * @return initialClaimCards (SortedBag<Card>) : the cards he uses to attempt to claim the route.
     */
    @Override
    public SortedBag<Card> initialClaimCards() {
        return cardsBlockingQueue.remove();
    }

    /**
     * Method which is called when the player has decided to try to seize a tunnel and additional cards are needed,
     * in order to know which card(s) he wishes to use for this, the possibilities being passed to him in argument;
     * if the returned multiset is empty, it means that the player does not wish (or cannot) choose one of these possibilities.
     * @param options (List<SortedBag<Card>>) : a list containing the possible sets of cards the player
     *                can use to claim the tunnel.
     * @return chooseAdditionalCards (SortedBag<Card>) : the cards the player chooses to use to claim the tunnel
     *         (empty if he cannot do so).
     */
    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        assert cardsBlockingQueue.isEmpty();

        Platform.runLater(() -> graphicalPlayer.chooseAdditionalCards(options, c -> put(cardsBlockingQueue, c)));

        return take(cardsBlockingQueue);
    }


    // Helper private static method to allow modularization.

    private static <E> void put(BlockingQueue<E> queue, E element) {
        try {
            queue.put(element);
        } catch (InterruptedException e) {
            throw new Error();
        }
    }

    private static <E> E take(BlockingQueue<E> queue) {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            throw new Error();
        }
    }

}
