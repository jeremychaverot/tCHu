package ch.epfl.tchu.game;

import java.util.List;
import java.util.Map;

import ch.epfl.tchu.SortedBag;

/** 
 * @author Cristian Safta (324694)
 */
public interface Player {

     enum TurnKind {

        DRAW_TICKETS,
        DRAW_CARDS,
        CLAIM_ROUTE;

        /**
         * A list of all possible types of turns (List<TurnKind>).
         */
        public static final List<TurnKind> ALL = List.of(TurnKind.values());

    }
    
    /**
     * Method being called at the beginning of a game to communicate to the player his id and the names of all the players
     * @param ownId (PlayerId) : the id of the player
     * @param playerNames (Map<PlayerId, String>) : a map that contains the ids and the name of all the players in the game
     */
    void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames);

    /**
     * Method that is called each time that some info needs to be communicated to the player
     * @param info (String) : the info that needs to be communicated to the player
     */
    void receiveInfo(String info);

    /**
     * Method that is called each time that some message needs to be communicated to the player
     * @param message (String) : the message that needs to be communicated to the player
     */
    void receiveMessage(String message);
    
    /**
     * Method that is called when the state of the game changes to communicate to the player the state of 
     * the game (only the components that he can view) and his own state
     * @param newState (PublicGameState) : the state of the game
     * @param ownState (PlayerState) : the state of the player
     */
    void updateState(PublicGameState newState, PlayerState ownState);
    
    /**
     * Method that is called at the beginning of a game to communicate to the player the 5 tickets that he got
     * @param tickets (SortedBag<Ticket>) :  a sorted bag containing the tickets that the player received
     */
    void setInitialTicketChoice(SortedBag<Ticket> tickets);

    /**
     * Method called at the beginning of a game that asks the player which of the tickets that were
     * distributed to him he wants to keep
     * @return chooseInitialTickets (SortedBag<Ticket>) : a sorted bag that contains the tickets the player wants to keep
     */
    SortedBag<Ticket> chooseInitialTickets();
    
    /**
     * Method being called when the turn of the player starts to find what kind of action he wants to do
     * @return nextTurn (TurnKind) : the type of action that the player wants to do during the round
     */
    TurnKind nextTurn();
    
    /**
     * Method being called when a player decides to draw additional tickets during a round to make him know which tickets
     * he drew and which ones he wants to keep
     * @param options (SortedBag<Ticket>) : the tickets he drew
     * @return chooseTickets (SortedBag<Ticket>) : the tickets he wants to keep
     */
    SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options);
    
    /**
     * Method called when the player has decided to draw car/locomotive card that indicated from where he wished to draw them
     * (0-4 for the face-up cards cards and -1 from the deck)
     * @return drawSlot (int) : the slot from which the player draws the card
     */
    int drawSlot();
    
    /**
     * Method called when the player attempts to seize a route in order to know which route it is
     * @return claimedRoute (Route) : the route the player attempts to seize
     */
    Route claimedRoute();

    /**
     * Method that is called when the player attempts to claim a route that indicated which cards he initially uses to do so
     * @return initialClaimCards (SortedBag<Card>) : the cards he uses to attempt to claim the route
     */
    SortedBag<Card> initialClaimCards();

    /**
     * Method which is called when the player has decided to try to seize a tunnel and additional cards are needed,
     * in order to know which card(s) he wishes to use for this, the possibilities being passed to him in argument;
     * if the returned multiset is empty, it means that the player does not wish (or cannot) choose one of these possibilities
     * @param options (List<SortedBag<Card>>) : a list containing the possible sets of cards the player
     *                                          can use to claim the tunnel
     * @return chooseAdditionalCards (SortedBag<Card>) : the cards the player chooses to use to claim the tunnel
     *                                                   (empty if he cannot do so)
     */
    SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options);

}
