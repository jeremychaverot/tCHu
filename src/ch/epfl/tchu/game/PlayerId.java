package ch.epfl.tchu.game;

import java.util.List;

/**
 * @author Jérémy Chaverot (315858)
 */
public enum PlayerId {

    PLAYER_1,
    PLAYER_2,
    PLAYER_3,
    PLAYER_4,
    PLAYER_5,
    PLAYER_6,
    PLAYER_7,
    PLAYER_8;

    /**
     * The list of all players (List<PlayerId>).
     */
    public static final List<PlayerId> ALL = List.of(PlayerId.values());

    /**
     * The number of players (int).
     */
    public static final int COUNT = ALL.size();

    /**
     * To get the player playing next the one we apply the method to.
     * @return player (PlayerId) : the next player.
     */
    public PlayerId next() {
        return ALL.get((ALL.indexOf(this) + 1) % COUNT);
    }

    /**
     * To get the player playing next the one we apply the method to.
     * @return player (PlayerId) : the next player.
     */
    public PlayerId next(List<PlayerId> players) {
        return players.get((players.indexOf(this) + 1) % players.size());
    }

}
