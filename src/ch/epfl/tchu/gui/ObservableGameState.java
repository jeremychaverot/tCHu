package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

/**
 * ObservableGameState represents the observable game state of a tCHu game, which is to be given to the GUI.
 * @author Jérémy Chaverot (315858)
 */
public final class ObservableGameState {

    private final PlayerId ownId ;
    private final List<PlayerId> allPlayers ;

    // Properties of the public game state
    private final List<ObjectProperty<Card>> faceUpCards;
    private final IntegerProperty ticketsPercentage;
    private final IntegerProperty cardsPercentage;
    private final Map<Route, ObjectProperty<PlayerId>> routes;

    // Properties of the public player state of each player
    private final Map<PlayerId, IntegerProperty> ticketCount;
    private final Map<PlayerId, IntegerProperty> cardCount;
    private final Map<PlayerId, IntegerProperty> carCount;
    private final Map<PlayerId, IntegerProperty> points;
    private final Map<PlayerId, ObjectProperty<Trail>> longestTrail;

    // Properties of the private player state of the instance's possessor
    private final ObservableList<Ticket> tickets;
    private final Map<Card, IntegerProperty> cards;
    private final Map<Route, BooleanProperty> claimable;

    private PublicGameState publicGameState;
    private PlayerState playerState;

    /**
     * Public constructor.
     * @param ownId (PlayerId) : the player we want to create an instance of ObservableGameState for.
     * @param allPlayers (List<PlayerId>) : the list of all players.
     */
    public ObservableGameState(PlayerId ownId, List<PlayerId> allPlayers) {
        this.ownId = ownId;
        this.allPlayers = allPlayers;

        // Properties of the public game state
        faceUpCards = createFaceUpCards();
        ticketsPercentage = new SimpleIntegerProperty();
        cardsPercentage = new SimpleIntegerProperty();
        routes = createRoutes();
        longestTrail = createLongestTrail();

        // Properties of the public player state of each player
        ticketCount = new HashMap<>();
        cardCount = new HashMap<>();
        carCount = new HashMap<>();
        points = new HashMap<>();
            for (PlayerId p : allPlayers) {
                ticketCount.put(p, new SimpleIntegerProperty());
                cardCount.put(p, new SimpleIntegerProperty());
                carCount.put(p, new SimpleIntegerProperty());
                points.put(p, new SimpleIntegerProperty());
            }

        // Properties of the private player state of the instance's possessor
        tickets = FXCollections.observableArrayList();
        cards = createCards();
        claimable = createClaimable();

    }

    /**
     * To update the game state, and therefore all the properties.
     * @param newPublicGameState (PublicGameState) : the new public game state.
     * @param newPlayerState (PlayerState) : the new player state of the instance's possessor.
     */
    public void setState(PublicGameState newPublicGameState, PlayerState newPlayerState) {

        publicGameState = newPublicGameState;
        playerState = newPlayerState;

        // Properties of the public game state
        ticketsPercentage.set((int) Math.round(publicGameState.ticketsCount() * 100 / (double) UsaMap.tickets().size()));
        cardsPercentage.set((int) Math.round(publicGameState.cardState().deckSize() * 100 / (double) Constants.TOTAL_CARDS_COUNT));
        for (int slot : Constants.FACE_UP_CARD_SLOTS) {
            Card newCard = publicGameState.cardState().faceUpCard(slot);
            faceUpCards.get(slot).set(newCard);
        }
        for (Route r : publicGameState.claimedRoutes()) {
            var identity = PlayerId.PLAYER_1;
            while (!publicGameState.playerState(identity).routes().contains(r)) {
                identity = identity.next(allPlayers);
            }
            routes.get(r).set(identity);
        }

        // Properties of the public player state of each player
        for (PlayerId p : allPlayers) {
            ticketCount.get(p).set(publicGameState.playerState(p).ticketCount());
            cardCount.get(p).set(publicGameState.playerState(p).cardCount());
            carCount.get(p).set(publicGameState.playerState(p).carCount());
            points.get(p).set(publicGameState.playerState(p).claimPoints());
            longestTrail.get(p).set(publicGameState.playerState(p).longestTrail());
        }

        // Properties of the private player state of the instance's possessor
        tickets.setAll(playerState.tickets().toList());
        for (Card c : Card.ALL)
            cards.get(c).set(playerState.cards().countOf(c));
        claimable.forEach((r, p) -> claimable.get(r).set(false));
        if (publicGameState.currentPlayerId() == ownId) {
            Set<List<Station>> stationsPair = new HashSet<List<Station>>();
            if (allPlayers.size() == 2) {
                routes.forEach((r, p) -> {
                    if (p.get() != null)
                        stationsPair.add(r.stations());
                });
                UsaMap.routes().forEach(r -> {
                    if (newPlayerState.canClaimRoute(r))
                        if (!stationsPair.contains(r.stations()))
                            claimable.get(r).set(true);
                });
            } else {
                routes.forEach((r, p) -> {
                    if (p.get() == ownId)
                        stationsPair.add(r.stations());
                });
                UsaMap.routes().forEach(r -> {
                    if (routes.get(r).get() == null)
                        if (newPlayerState.canClaimRoute(r))
                            if (!stationsPair.contains(r.stations()))
                                claimable.get(r).set(true);
                });
            }
        }

    }


    // Properties of the public game state

    /**
     * To get the left tickets percentage integer read only propriety.
     * @return percentage (ReadOnlyIntegerProperty) : percentage of left tickets out of all of them
     * (integer between 0 and 100, both inclusive).
     */
    public ReadOnlyIntegerProperty ticketsPercentage() {
        return ticketsPercentage;
    }

    /**
     * To get the left cards percentage integer read only propriety.
     * @return percentage (ReadOnlyIntegerProperty) : percentage of left cards out of all of them
     * (integer between 0 and 100, both inclusive).
     */
    public ReadOnlyIntegerProperty cardsPercentage() {
        return cardsPercentage;
    }

    /**
     * To get the card read only property of the face up card at the given slot read only propriety.
     * @param slot (int) : slot of interest.
     * @return card (ReadOnlyObjectProperty<Card>) : face up card at the given slot.
     */
    public ReadOnlyObjectProperty<Card> faceUpCard(int slot) {
        return faceUpCards.get(slot);
    }

    /**
     * To get the PlayerId read only propriety of the player that possesses the given route, null if it belongs to no one.
     * @param route (Route) : route of interest.
     * @return playerId (ReadOnlyObjectProperty<PlayerId>) : id of the player that possesses the given route, null if
     * it belongs to no one.
     */
    public ReadOnlyObjectProperty<PlayerId> routes(Route route) {
        return routes.get(route);
    }


    // Properties of the public player state of each player

    /**
     * To get the number of tickets read only integer property of the given playerId.
     * @param playerId (PlayerId) : player of interest.
     * @return ticketCount (ReadOnlyIntegerProperty) : the amount of tickets the player has.
     */
    public ReadOnlyIntegerProperty ticketCount(PlayerId playerId) {
        return ticketCount.get(playerId);
    }

    /**
     * To get the number of card read only integer property of the given playerId.
     * @param playerId (PlayerId) : player of interest.
     * @return cardCount (ReadOnlyIntegerProperty) : the amount of cards the player has.
     */
    public ReadOnlyIntegerProperty cardCount(PlayerId playerId) {
        return cardCount.get(playerId);
    }

    /**
     * To get the number of cars read only integer property of the given playerId.
     * @param playerId (PlayerId) : player of interest.
     * @return carCount (ReadOnlyIntegerProperty) : the amount of cars the player has.
     */
    public ReadOnlyIntegerProperty carCount(PlayerId playerId) {
        return carCount.get(playerId);
    }

    /**
     * To get the number of points read only integer property of the given playerId.
     * @param playerId (PlayerId) : player of interest.
     * @return points (ReadOnlyIntegerProperty) : the amount of points the player has.
     */
    public ReadOnlyIntegerProperty points(PlayerId playerId) {
        return points.get(playerId);
    }

    /**
     * To get the longest trail read only object property of the given playerId.
     * @param playerId (PlayerId) : player of interest.
     * @return longestTrail (ReadOnlyObjectProperty<Trail>) : one of the longest trail of the game that the player is
     * the owner of, can be null.
     */
    public ReadOnlyObjectProperty<Trail> longestTrail(PlayerId playerId) {
        return allPlayers.contains(playerId) ? longestTrail.get(playerId) : new SimpleObjectProperty<>();
    }


    // Properties of the private player state of the instance's possessor

    /**
     * To get the observable list of tickets of the player that possesses the instance.
     * @return tickets (ObservableList<Ticket>) : the list of tickets the player has.
     */
    public ObservableList<Ticket> tickets() {
        return FXCollections.unmodifiableObservableList(tickets);
    }

    /**
     * To get the number of the given card read only integer property of the player that possesses the instance.
     * @param card (Card) : card of interest.
     * @return number (ReadOnlyIntegerProperty) : the amount of the given card the player has.
     */
    public ReadOnlyIntegerProperty cards(Card card) {
        return cards.get(card);
    }

    /**
     * To get the given route read only boolean property of the player that possesses the instance : is true if the player
     * can claim the route, false otherwise.
     * @param route (Route) : route of interest.
     * @return boolean (ReadOnlyBooleanProperty) : states if the player player can, or not claim the given route.
     */
    public ReadOnlyBooleanProperty claimable(Route route) {
        return claimable.get(route);
    }


    /**
     * States if the player can draw a ticket in the actual state.
     * @return canDrawTickets (boolean) : true if the player can draw a ticket, false otherwise.
     */
    public boolean canDrawTickets() {
        return publicGameState.canDrawTickets();
    }

    /**
     * States if the player can draw cards in the actual state.
     * A player can draw a card if the sum of the deck size and the discard size is more or equal to 5.
     * @return canDrawCards (boolean) : true if the player can draw cards, false otherwise.
     */
    public boolean canDrawCards() {
        return publicGameState.canDrawCards();
    }

    /**
     * To get the possible claim cards of a certain route for the actual player state.
     * @param route (Route) : the route of interest.
     * @return possibleClaimCards (List<SortedBag<Card>>) : the list of all the sets (sorted) of cards the player can
     * use according to his cards to take possession of the route.
     * @throws IllegalArgumentException if the player doesn't have enough cars, i. e. less than
     *                                      the route length.
     */
    public List<SortedBag<Card>> possibleClaimCards(Route route) {
        return playerState.possibleClaimCards(route);
    }


    // Private helper methods to initialize the properties

    private static Map<Route, ObjectProperty<PlayerId>> createRoutes() {
        Map<Route, ObjectProperty<PlayerId>> map = new HashMap<>();
            for (Route r : UsaMap.routes())
                map.put(r, new SimpleObjectProperty<>());

        return map;
    }

    private static List<ObjectProperty<Card>> createFaceUpCards() {
        List<ObjectProperty<Card>> list = new ArrayList<>();
        for (int i = 0; i < Constants.FACE_UP_CARDS_COUNT ; i++)
            list.add(new SimpleObjectProperty<>());

        return list;
    }

    private static Map<Card, IntegerProperty> createCards() {
        Map<Card, IntegerProperty> map = new HashMap<>();
        for (Card c : Card.ALL)
            map.put(c, new SimpleIntegerProperty());

        return map;
    }

    private static Map<Route, BooleanProperty> createClaimable() {
        Map<Route, BooleanProperty> map = new HashMap<>();
        for (Route r : UsaMap.routes())
            map.put(r, new SimpleBooleanProperty());

        return map;
    }

    private Map<PlayerId, ObjectProperty<Trail>> createLongestTrail() {
        Map<PlayerId, ObjectProperty<Trail>> map = new HashMap<>();
        for (PlayerId p : allPlayers)
            map.put(p, new SimpleObjectProperty<>());

        return map;
    }

}
