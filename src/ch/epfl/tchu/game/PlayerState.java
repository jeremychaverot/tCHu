package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

/**
 * @author Jérémy Chaverot (315858)
 */
public final class PlayerState extends PublicPlayerState {

    private final SortedBag<Ticket> tickets;
    private final SortedBag<Card> cards;

    /**
     * Static construction method.
     * @param initialCards (SortedBag<Card>) : the player's initial cards.
     * @return playerState (PlayerState) : state where the player's cards are the initial ones,
     * and the player has no tickets and no routes.
     * @throws IllegalArgumentException if the size initialCards is not equal to Constants.INITIAL_CARDS_COUNT.
     */
    public static PlayerState initial(SortedBag<Card> initialCards) {
        Preconditions.checkArgument(initialCards.size() == Constants.INITIAL_CARDS_COUNT);

        return new PlayerState(SortedBag.of(), initialCards, List.of(), null);
    }
    
    /**
     * Constructor.
     * @param tickets (SortedBag<Ticket>) : the player's tickets.
     * @param cards (SortedBag<Card>) : the player's cards.
     * @param routes (List<Route>) : the routes already possessed by the player.
     */
    public PlayerState(SortedBag<Ticket> tickets, SortedBag<Card> cards, List<Route> routes, Trail longestTrail) {
        super(tickets.size(), cards.size(), routes, longestTrail);

        this.tickets = tickets;
        this.cards = cards;
    }
    
    /**
     * To get the player's tickets.
     * @return tickets (SortedBag<Ticket>) : the player's tickets.
     */
    public SortedBag<Ticket> tickets() { return tickets; }
    
    /**
     * To add new tickets to the player.
     * @param newTickets (SortedBag<Ticket>) : the new tickets we want to add.
     * @return playerState (PlayerState) : the same player state but with tickets added to the old ones.
     */
    public PlayerState withAddedTickets(SortedBag<Ticket> newTickets) {
        return new PlayerState(tickets.union(newTickets), cards, routes(), null);
    }

    /**
     * To add a longest trail to the player. This method can be called once at the end of the game, or never.
     * @param longestTrail (Trail) : one of the longest trail of the game.
     * @return playerState (PlayerState) : the same player state but with a longest trail added.
     */
    public PlayerState withAddedLongestTrail(Trail longestTrail) {
        return new PlayerState(tickets, cards, routes(), longestTrail);
    }
    /**
     * To get the player's cards.
     * @return cards (SortedBag<Card>) : the player's cards.
     */
    public SortedBag<Card> cards() { return cards; }
    
    /**
     * To add a new card to the player.
     * @param card (Card) : the new card we want to add.
     * @return playerState (PlayerState) : the same player state but with a card added to the old ones.
     */
    public PlayerState withAddedCard(Card card) {
        return new PlayerState(tickets, cards.union(SortedBag.of(card)), routes(), null);
    }
    
    /** UNUSED METHOD.
     * To add new cards to the player.
     * @param additionalCards (SortedBag<Card>) : the new cards we want to add.
     * @return playerState (PlayerState) : the same player state but with new cards added to the old ones.
     */
    /*public PlayerState withAddedCards(SortedBag<Card> additionalCards) {
        return new PlayerState(tickets, cards.union(additionalCards), routes());
    }*/
    
    /**
     * States if the player can claim the route.
     * @param route (Route) : the route of interest.
     * @return boolean (Boolean) : true if the player can claim the route, false otherwise.
     */
    public boolean canClaimRoute(Route route) {
        return route.length() <= carCount()
                && possibleClaimCards(route).size() > 0;
    }
    
    /**
     * To get the possible claim cards of a certain route.
     * @param route (Route) : the route of interest.
     * @return possibleClaimCards (List<SortedBag<Card>>) : the list of all the sets (sorted) of cards the player can
     * use according to his cards to take possession of the route.
     * @throws IllegalArgumentException if the player doesn't have enough cars, i. e. less than
     *                                      the route length.
     */
    public List<SortedBag<Card>> possibleClaimCards(Route route) {
        Preconditions.checkArgument(route.length() <= carCount());

        var allPossibleClaimCards = route.possibleClaimCards();
        List<SortedBag<Card>> possibleClaimCards = new ArrayList<>();
            allPossibleClaimCards.forEach((c) -> {
                if (cards.contains(c))
                    possibleClaimCards.add(c);
            });

        return possibleClaimCards;
    }
    
    /**
     * To get the list of the possible additional cards the player needs to play, given the cards played and the cards
     * drawn, according to the cards he has left.
     * @param additionalCardsCount (int) : the amount of cards the player is forced to play.
     * @param initialCards (SortedBag<Card>) : the cards already played by the player.
     * @param drawnCards (SortedBag<Card>) : the cards drawn at the top of the deck.
     * @return possibleAdditionalCards (List<SortedBag<Card>>) : the list of all possible cards the player can use to 
     * take in possession the tunnel.
     * @throws IllegalArgumentException if additionalCardsCount is not between 1 and Constants.ADDITIONAL_TUNNEL_CARDS
     *                                      (both inclusive),
     *                                  or if initialCards is empty,
     *                                  or if initialCards contains strictly more than two different type of cards,
     *                                  or if drawnCards size is not equal to Constants.ADDITIONAL_TUNNEL_CARDS.
     */
    public List<SortedBag<Card>> possibleAdditionalCards(int additionalCardsCount, SortedBag<Card> initialCards,
                                                         SortedBag<Card> drawnCards) {
        Preconditions.checkArgument(additionalCardsCount >= 1
                && additionalCardsCount <= Constants.ADDITIONAL_TUNNEL_CARDS);
        Preconditions.checkArgument(!initialCards.isEmpty());
        Preconditions.checkArgument(initialCards.toSet().size() <= 2);
        Preconditions.checkArgument(drawnCards.size() == Constants.ADDITIONAL_TUNNEL_CARDS);
        
        var newCards = cards.difference(initialCards);
        var onlyUsableCardsBuilder = new SortedBag.Builder<Card>();
            for (Card c : Card.CARS) {
                if (initialCards.contains(c)) {
                    onlyUsableCardsBuilder.add(Math.min(newCards.countOf(c), additionalCardsCount), c);
                    break;
                }
            }
            onlyUsableCardsBuilder.add(newCards.countOf(Card.LOCOMOTIVE), Card.LOCOMOTIVE);

        var onlyUsableCards = onlyUsableCardsBuilder.build();

        if (onlyUsableCards.size() < additionalCardsCount)
            return List.of();

        var subSetsOfGivenSize = onlyUsableCards.subsetsOfSize(additionalCardsCount);
        List<SortedBag<Card>> possibleAdditionalCards = new ArrayList<>();
        subSetsOfGivenSize.forEach(possibleAdditionalCards::add);
        possibleAdditionalCards.sort(Comparator.comparingInt(cs -> cs.countOf(Card.LOCOMOTIVE)));
        
        return possibleAdditionalCards;
    }

    /**
     * To get the same player state, but where the player took in possession the given route by use of
     * the given cards.
     * @param route (Route) : the route the player took hold of.
     * @param claimCards (SortedBag<Card>) : the cards the player took the route hold of with.
     * @return playerState (PlayerState) : the same player state with a new route in possession, and less cards.
     */
    public PlayerState withClaimedRoute(Route route, SortedBag<Card> claimCards) {
        List<Route> newRoutes = routes();
        newRoutes.add(route);

        return new PlayerState(tickets, cards.difference(claimCards), newRoutes, null);
    }
    
    /**
     * To get the amount of points, eventually negative, obtained by the player thanks to his tickets.
     * @return ticketPoints (int) : the amount of points obtained by the player.
     */
    public int ticketPoints() {
        int points = 0;
        int maxId = 0;
            for (Route route : routes()) {
                int a = route.station1().id();
                int b = route.station2().id();
                if ((a > maxId) || (b > maxId)) {
                    maxId = Math.max(a, b);
                }
            }
        
        StationPartition.Builder partitionInBuilding = new StationPartition.Builder(maxId + 1);
        for (Route route : routes())
            partitionInBuilding = partitionInBuilding.connect(route.station1(), route.station2());
        StationPartition partition = partitionInBuilding.build();
        
        for (Ticket t : tickets)
            points += t.points(partition);
        
        return points;
    }
    
    /**
     * To get the amount of points in total obtained by the player at the end of the game, i. e. the sum
     * of his ticket points and the construction of the routes points.
     * @return finalPoints (int) : The amount of points in total obtained by the player at the end of the game.
     */
    public int finalPoints() { return ticketPoints() + claimPoints(); }
    
}
