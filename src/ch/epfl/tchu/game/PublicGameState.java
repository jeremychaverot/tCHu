package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.*;

/**
 * @author Jérémy Chaverot (315858)
 */
public class PublicGameState {

    private final int ticketsCount;
    private final PublicCardState cardState;
    private final PlayerId currentPlayerId;
    private final Map<PlayerId, PublicPlayerState> playerState;
    private final PlayerId lastPlayer;

    /**
     * Constructor.
     * @param ticketsCount    (int) : size of the deck of tickets.
     * @param cardState       (PublicCardState) : public state of the cards car/locomotive.
     * @param currentPlayerId (PlayerId) : current player.
     * @param playerState     (Map<PlayerId, PublicPlayerState>) : public state of the players.
     * @param lastPlayer      (PlayerId) : identity of the last player.
     * @throws IllegalArgumentException if the ticket deck size is strictly negative
     *                                  or if playerState does not contains exactly PlayerId.COUNT pairs key/value.
     * @throws NullPointerException     if cardState is null or if currentPlayerId is null.
     */
    public PublicGameState(int ticketsCount, PublicCardState cardState, PlayerId currentPlayerId, Map<PlayerId,
            PublicPlayerState> playerState, PlayerId lastPlayer) {
        Preconditions.checkArgument(ticketsCount >= 0);

        this.ticketsCount = ticketsCount;
        this.cardState = Objects.requireNonNull(cardState);
        this.currentPlayerId = Objects.requireNonNull(currentPlayerId);
        this.playerState = Map.copyOf(playerState);
        this.lastPlayer = lastPlayer;
    }

    /**
     * To get the size of the deck of tickets.
     * @return ticketsCount (int) : size of the deck of tickets.
     */
    public int ticketsCount() {
        return ticketsCount;
    }

    /**
     * States if the player can draw a ticket.
     * @return canDrawTickets (boolean) : true if the player can draw a ticket, false otherwise.
     */
    public boolean canDrawTickets() {
        return ticketsCount > 0;
    }

    /**
     * The get the public part of the state of cards car/locomotive.
     * @return cardState (PublicCardState) : public state of the cards car/locomotive.
     */
    public PublicCardState cardState() {
        return cardState;
    }

    /**
     * States if the player can draw cards.
     * A player can draw a card if the sum of the deck size and the discard size is more or equal to 5.
     * @return canDrawCards (boolean) : true if the player can draw cards, false otherwise.
     */
    public boolean canDrawCards() { return (cardState.deckSize() + cardState.discardsSize()) >= 5; }

    /**
     * To get the current player id.
     * @return currentPlayerId (PlayerId) : the current player id.
     */
    public PlayerId currentPlayerId() { return currentPlayerId; }

    /**
     * To get the public player state of the given player identity.
     * @param playerId (PlayerId) : the identity of the player of interest.
     * @return playerState (PublicPlayerState) : the public player state of the given player identity.
     */
    public PublicPlayerState playerState(PlayerId playerId) { return playerState.get(playerId); }

    /**
     * To get the public player state of the current player.
     * @return playerState (PublicPlayerState) : the public player state of the current player.
     */
    public PublicPlayerState currentPlayerState() {
        return playerState.get(currentPlayerId);
    }

    /**
     * To get the totality of the routes possessed by the two players.
     * @return claimedRoutes (List<Route>) : the list of the routes possessed by the players.
     */
    public List<Route> claimedRoutes() {
        List<Route> claimedRoutes = new ArrayList<>();
        playerState.forEach((k, v) -> claimedRoutes.addAll(v.routes()));

        return claimedRoutes;
    }

    /**
     * To get the identity of the last player.
     * @return lastPlayer (PlayerId) : identity of the last player (can be null).
     */
    public PlayerId lastPlayer() { return lastPlayer; }

    /**
     * To get the number of players playing the tCHu game.
     * @return number (int) : the number of players.
     */
    public int numberOfPlayers() { return playerState.size(); }

}
