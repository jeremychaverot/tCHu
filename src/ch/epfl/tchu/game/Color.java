package ch.epfl.tchu.game;

import java.util.List;

/** 
 * @author Cristian Safta (324694)
 */
public enum Color {

    BLACK,
    VIOLET,
    BLUE,
    GREEN,
    YELLOW,
    ORANGE,
    RED,
    WHITE;
   
    /**
     * A List of all the possible colors (List<Color>).
     */
    public static final List<Color> ALL = List.of(Color.values());
    
    /**
     * The number of possible colors (int).
     */
    public static final int COUNT = ALL.size();

}
