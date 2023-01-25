package ch.epfl.tchu.game;

import java.util.List;

/**
 * @author Jérémy Chaverot (315858)
 */
public enum Card {

    BLACK(Color.BLACK),
    VIOLET(Color.VIOLET),
    BLUE(Color.BLUE),
    GREEN(Color.GREEN),
    YELLOW(Color.YELLOW),
    ORANGE(Color.ORANGE),
    RED(Color.RED),
    WHITE(Color.WHITE),
    LOCOMOTIVE(null);

    /**
     * The list of all possible cards (List<Card>).
     */
    public static final List<Card> ALL = List.of(Card.values());
    
    /**
     * The number of possible cards (int).
     */
    public static final int COUNT = ALL.size();

    /**
     * The list of all possible cars, same as the list of cards without locomotive (List<Card>).
     */
    public static final List<Card> CARS = List.of(Card.BLACK, Card.VIOLET, Card.BLUE, Card.GREEN, Card.YELLOW,
            Card.ORANGE, Card.RED, Card.WHITE);

    private final Color color;

    /**
     * To get the color of a card.
     * @param color (Color) : the color of the card.
     * @return the card with this color (Card).
     */
    public static Card of(Color color) {
        int index = Color.ALL.indexOf(color);

        return CARS.get(index);
    }

    Card(Color color) { this.color = color; }

    /**
     * To get the color associated to the card (this).
     * @return the color associated (Color).
     */
    public Color color() { return color; }

}
