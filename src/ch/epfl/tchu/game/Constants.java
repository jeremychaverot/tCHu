package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;

import java.util.List;

public final class Constants {

    /**
     * Number of 'car' card of each color.
     */
    public static final int CAR_CARDS_COUNT = 12;

    /**
     * Number of 'locomotive' card.
     */
    public static final int LOCOMOTIVE_CARDS_COUNT = 14;

    /**
     * Total number of 'car'/'locomotive' cards.
     */
    public static final int TOTAL_CARDS_COUNT =
            LOCOMOTIVE_CARDS_COUNT + CAR_CARDS_COUNT * Color.COUNT;

    /**
     * Set of all the cards (110 in total).
     */
    public static final SortedBag<Card> ALL_CARDS = computeAllCards();

    private static SortedBag<Card> computeAllCards() {
        var cardsBuilder = new SortedBag.Builder<Card>();
        cardsBuilder.add(LOCOMOTIVE_CARDS_COUNT, Card.LOCOMOTIVE);
        for (Card card : Card.CARS)
            cardsBuilder.add(CAR_CARDS_COUNT, card);
        assert cardsBuilder.size() == TOTAL_CARDS_COUNT;
        return cardsBuilder.build();
    }

    /**
     * Fictitious slot number designating the deck of cards.
     */
    public static final int DECK_SLOT = -1;

    /**
     * List of all the slot numbers of the visible face up cards.
     */
    public static final List<Integer> FACE_UP_CARD_SLOTS = List.of(0, 1, 2, 3, 4);

    /**
     * Number of slot for the face up cards.
     */
    public static final int FACE_UP_CARDS_COUNT = FACE_UP_CARD_SLOTS.size();

    /**
     * Number of tickets given to each player at the beginning of the tCHu game.
     */
    public static final int INITIAL_TICKETS_COUNT = 3;

    /**
     * Number of cards given to each player at the beginning of the tCHu game.
     */
    public static final int INITIAL_CARDS_COUNT = 4;

    /**
     * Number of cars each player possesses at the beginning of the tCHu game.
     */
    public static final int INITIAL_CAR_COUNT = 40;

    /**
     * Number of tickets drawn at the same time during a game.
     */
    public static final int IN_GAME_TICKETS_COUNT = 3;

    /**
     * Number of cards drawn at the same time during a game.
     */
    public final static int IN_GAME_CARDS_COUNT = 2;

    /**
     * Maximal amount of tickets a player can throw away during a draw.
     */
    public static final int DISCARDABLE_TICKETS_COUNT = 2;

    /**
     * Number of cards to draw when claiming a tunnel.
     */
    public static final int ADDITIONAL_TUNNEL_CARDS = 3;

    /**
     * Number of points given when claiming a route of length 1 to 6.
     * (The element at index i corresponds to a length of route i. A invalid value
     * is placed at index 0, since routes of length 0 do not exist).
     */
    public static final List<Integer> ROUTE_CLAIM_POINTS =
            List.of(Integer.MIN_VALUE, 1, 2, 4, 7, 10, 15);

    /**
     * Minimal length of a route.
     */
    public static final int MIN_ROUTE_LENGTH = 1;

    /**
     * Maximal length of a route.
     */
    public static final int MAX_ROUTE_LENGTH = ROUTE_CLAIM_POINTS.size() - 1;

    /**
     * Amount of bonus points given the player(s) having the longest trail.
     */
    public static final int LONGEST_TRAIL_BONUS_POINTS = 10;

    /**
     * Empty private constructor because irrelevant.
     */
    private Constants() {}

}
