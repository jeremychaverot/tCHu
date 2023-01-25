package ch.epfl.tchu.net;

import java.util.List;

/**
 * Enum class that contains all the possible actions during the game.
 * @author Jérémy Chaverot (315858)
 */
public enum MessageId {

    INIT_PLAYERS,
    RECEIVE_INFO,
    UPDATE_STATE,
    SET_INITIAL_TICKETS,
    CHOOSE_INITIAL_TICKETS,
    NEXT_TURN,
    CHOOSE_TICKETS,
    DRAW_SLOT,
    ROUTE,
    CARDS,
    CHOOSE_ADDITIONAL_CARDS;

    /**
     * The list of all possible message id (List<MessageId>).
     */
    public static final List<MessageId> ALL = List.of(MessageId.values());

    /**
     * The number of possible message id (int).
     */
    public static final int COUNT = ALL.size();

}
