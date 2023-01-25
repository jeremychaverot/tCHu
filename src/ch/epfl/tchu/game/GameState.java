package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.*;

/**
 * @author Jérémy Chaverot (315858)
 */
public final class GameState extends PublicGameState {

    private final Deck<Ticket> tickets;
    private final Map<PlayerId, PlayerState> playerState;
    private final CardState cardState;
    private final List<PlayerId> allPlayers;

    /**
     * To get a initial game state of tCHu.
     * @param tickets (SortedBag<Ticket>) : the deck of all tickets.
     * @param rng     (Random) : instance from Random used to shuffle or generate random number.
     * @return initialGameState (GameState) : a new game state for the start of the game, where each player
     * has Constants.INITIAL_CARDS_COUNT cards, and the left cards constitute the deck.
     */
    public static GameState initial(List<PlayerId> allPlayers, SortedBag<Ticket> tickets, Random rng) {
        var numberOfPlayers = allPlayers.size();
        Map<PlayerId, PlayerState> newMap = new HashMap<>();
        Deck<Card> randomCards = Deck.of(Constants.ALL_CARDS, rng);
        Deck<Ticket> randomTickets = Deck.of(tickets, rng);
        int randomPlayer = rng.nextInt(numberOfPlayers);

        for (PlayerId p : allPlayers) {
            newMap.put(p, PlayerState.initial(randomCards.topCards(Constants.INITIAL_CARDS_COUNT)));
            randomCards = randomCards.withoutTopCards(Constants.INITIAL_CARDS_COUNT);
        }

        while ((randomTickets.size() - (Constants.INITIAL_TICKETS_COUNT * numberOfPlayers)) % Constants.IN_GAME_TICKETS_COUNT != 0)
            randomTickets = randomTickets.withoutTopCard();

        return new GameState(randomTickets, CardState.of(randomCards), allPlayers.get(randomPlayer), newMap, null);
    }

    /**
     * Private constructor.
     * @param tickets         (SortedBag<Ticket>) : the sorted set of all tickets.
     * @param cardState       (CardState) : state of the cards car/locomotive.
     * @param currentPlayerId (PlayerId) : current player identity.
     * @param playerState     (Map<PlayerId, PlayerState>) : state of the players.
     * @param lastPlayer      (PlayerId) : last player identity.
     */
    private GameState(Deck<Ticket> tickets, CardState cardState, PlayerId currentPlayerId, Map<PlayerId,
            PlayerState> playerState, PlayerId lastPlayer) {
        super(tickets.size(), cardState, currentPlayerId, Map.copyOf(playerState), lastPlayer);

        this.tickets = Objects.requireNonNull(tickets);
        this.cardState = Objects.requireNonNull(cardState);
        this.playerState = Map.copyOf(playerState);
        this.allPlayers = new ArrayList<>(playerState.keySet());
    }

    /**
     * To get the 'count' first ticket(s) at the top of the deck of tickets.
     * @param count (int) : the number of tickets wanted.
     * @return topTickets (SortedBag<Ticket>) : the sorted set of 'count' tickets.
     * @throws IllegalArgumentException if count is not a number between 0 and the deck of tickets size (both inclusive).
     */
    public SortedBag<Ticket> topTickets(int count) {
        Preconditions.checkArgument((count >= 0) && (count <= tickets.size()));

        return tickets.topCards(count);
    }

    /**
     * To get a new game state, identical to the previous one, but to which we removed the 'count' top tickets.
     * @param count (int) : the number of tickets at the top of the deck we want to remove.
     * @return gameState (GameState) : a similar game state without the 'count' top tickets.
     * @throws IllegalArgumentException if count is not a number between 0 and the deck of tickets size (both inclusive).
     */
    public GameState withoutTopTickets(int count) {
        Preconditions.checkArgument((count >= 0) && (count <= tickets.size()));

        return new GameState(tickets.withoutTopCards(count), cardState, currentPlayerId(), playerState, lastPlayer());
    }

    /**
     * To get a view of the card at the top of the deck.
     * @return card (Card) : the top deck card.
     * @throws IllegalArgumentException if the deck is empty.
     */
    public Card topCard() {
        Preconditions.checkArgument(!cardState.isDeckEmpty());

        return cardState.topDeckCard();
    }

    /**
     * To get the same game state without the top deck card.
     * @return gameState (GameState) : a game state where we took the top deck card off.
     * @throws IllegalArgumentException if the deck is empty.
     */
    public GameState withoutTopCard() {
        Preconditions.checkArgument(!cardState.isDeckEmpty());

        return new GameState(tickets, cardState.withoutTopDeckCard(), currentPlayerId(), playerState, lastPlayer());
    }

    /**
     * To get the same game state, but with cards added to the discards.
     * @param discardedCards (Sorted<Card>) : the cards we want to add to the discards.
     * @return gameState (gameState) : the same game state, but we added cards to the discards.
     */
    public GameState withMoreDiscardedCards(SortedBag<Card> discardedCards) {
        return new GameState(tickets, cardState.withMoreDiscardedCards(discardedCards), currentPlayerId(), playerState,
                lastPlayer());
    }

    /**
     * From a game state where the deck is empty, we recreate the deck from the discards mixed.
     * @param rng (Random) : random number generator.
     * @return gameState (GameState) : the same game state if the deck in not empty, the same game state with
     * deck recreated from discards otherwise.
     */
    public GameState withCardsDeckRecreatedIfNeeded(Random rng) {
        if (cardState.isDeckEmpty()) {
            return new GameState(tickets, cardState.withDeckRecreatedFromDiscards(rng), currentPlayerId(), playerState,
                    lastPlayer());
        } else return this;
    }

    /**
     * To add new tickets to a player.
     * @param playerId      (PlayerId) : the player we want to add tickets to.
     * @param chosenTickets (SortedBag<Ticket>) : the tickets the player choose and that are to be added.
     * @return gameState (GameState) : the same game state but with tickets added to a player.
     * @throws IllegalArgumentException if the player possesses already at least one ticket.
     */
    public GameState withInitiallyChosenTickets(PlayerId playerId, SortedBag<Ticket> chosenTickets) {
        Preconditions.checkArgument(!(playerState.get(playerId).ticketCount() > 0));

        Map<PlayerId, PlayerState> newMap = new EnumMap<>(playerState);
        newMap.put(playerId, newMap.get(playerId).withAddedTickets(chosenTickets));

        return new GameState(tickets, cardState, currentPlayerId(), newMap, lastPlayer());
    }

    /**
     * To add the chosen tickets to the current player, and remove them from the deck of tickets.
     * @param drawnTickets  (SortedBag<Ticket>) : the tickets the current player drew.
     * @param chosenTickets (SortedBag<Ticket>) : the tickets the current player choose.
     * @return gameState (GameState) : the same game state, but with the chosen tickets added to the current player,
     * and removed then from the deck of tickets.
     * @throws IllegalArgumentException if the drawn tickets does not contain the chosen ones.
     */
    public GameState withChosenAdditionalTickets(SortedBag<Ticket> drawnTickets, SortedBag<Ticket> chosenTickets) {
        Preconditions.checkArgument(drawnTickets.contains(chosenTickets));

        Deck<Ticket> newTickets = tickets.withoutTopCards(drawnTickets.size());
        Map<PlayerId, PlayerState> newMap = new EnumMap<>(playerState);
        newMap.put(currentPlayerId(), newMap.get(currentPlayerId()).withAddedTickets(chosenTickets));

        return new GameState(newTickets, cardState, currentPlayerId(), newMap, lastPlayer());
    }

    /**
     * To get the same game state, except that the card from the face up ones of index
     * 'slot' will be added the to current player, and replaced by the card at the top of the deck.
     * @param slot (int) : index of the card in the face up cards the player will acquire and that will be replaced.
     * @return gameState (GameState) : the same game state, but where the current player gained a card from the face
     * up cards, and this card got replaced from the one at the top of the deck.
     * @throws IllegalArgumentException if the player cannot draw a card (according to the value
     *                                      of the boolean canDrawCards()).
     */
    public GameState withDrawnFaceUpCard(int slot) {

        Map<PlayerId, PlayerState> newMap = new EnumMap<>(playerState);
        newMap.put(currentPlayerId(), newMap.get(currentPlayerId()).withAddedCard(cardState.faceUpCard(slot)));

        return new GameState(tickets, cardState.withDrawnFaceUpCard(slot), currentPlayerId(), newMap, lastPlayer());
    }

    /**
     * To get the same game state, except that the card at the top of the deck was placed in the current player's hand.
     * @return gameState (GameState) : the same game state, but the current player gained blindly the card at
     * the top of the deck.
     * @throws IllegalArgumentException if the player cannot draw a card (according to the value
     *                                      of the boolean canDrawCards()).
     */
    public GameState withBlindlyDrawnCard() {

        Map<PlayerId, PlayerState> newMap = new EnumMap<>(playerState);
        newMap.put(currentPlayerId(), newMap.get(currentPlayerId()).withAddedCard(cardState.topDeckCard()));

        return new GameState(tickets, cardState.withoutTopDeckCard(), currentPlayerId(), newMap, lastPlayer());
    }

    /**
     * To get the same game state, except that the current player claimed a route, and the claimed cards were added to the discards.
     * @param route (Route) : the route the current player is claiming.
     * @param cards (SortedBag<Card>) : the cards used to claim the route.
     * @return gameState (GameState) : the same game state, but the current player claimed a route.
     */
    public GameState withClaimedRoute(Route route, SortedBag<Card> cards) {
        Map<PlayerId, PlayerState> newMap = new EnumMap<>(playerState);
        newMap.put(currentPlayerId(), newMap.get(currentPlayerId()).withClaimedRoute(route, cards));

        return new GameState(tickets, cardState.withMoreDiscardedCards(cards), currentPlayerId(), newMap, lastPlayer());
    }

    /**
     * To get the same game state, except that a player was given a longest trail. This method can be called once at
     * the end of the game, or never.
     * @param playerId (PlayerId) : the player that got the longest trail.
     * @param longestTrail (Trail) : one of the longest trail of the game.
     * @return gameState (GameState) : the same game state, but with an added longest trail.
     */
    public GameState withAddedLongestTrail(PlayerId playerId, Trail longestTrail) {
        Map<PlayerId, PlayerState> newMap = new EnumMap<>(playerState);
        newMap.put(playerId, newMap.get(playerId).withAddedLongestTrail(longestTrail));

        return new GameState(tickets, cardState, currentPlayerId(), newMap, lastPlayer());
    }

    /**
     * States if the last turn begins.
     * @return lastTurnBegins (Boolean) : true if it's the case, false otherwise.
     */
    public boolean lastTurnBegins() {
        return lastPlayer() == null
                && playerState.get(currentPlayerId()).carCount() <= 2;
    }

    /**
     * To finish the turn of the current player.
     * @return gameState (GameState) : the same game state, except that the current player is the player following the
     * current one. If lastTurnBegins() return true, the current actual player becomes the last player.
     */
    public GameState forNextTurn() {
        if (lastTurnBegins())
            return new GameState(tickets, cardState, currentPlayerId().next(allPlayers), playerState, currentPlayerId());

        else return new GameState(tickets, cardState, currentPlayerId().next(allPlayers), playerState, lastPlayer());
    }

    /**
     * To get the (complete) player state of the given player identity.
     * @param playerId (PlayerId) : the identity of the player of interest.
     * @return playerState (PlayerState) : the player state of the given player identity.
     */
    @Override
    public PlayerState playerState(PlayerId playerId) {
        return playerState.get(playerId);
    }

    /**
     * To get the (complete) player state of the current player.
     * @return playerState (PlayerState) : the player state of the current player.
     */
    @Override
    public PlayerState currentPlayerState() {
        return playerState.get(currentPlayerId());
    }

}
