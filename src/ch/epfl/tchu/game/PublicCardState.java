package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ch.epfl.tchu.Preconditions;

/**
 * @author Jérémy Chaverot (315858)
 */
public class PublicCardState {
    
    private final List<Card> faceUpCards;
    private final int deckSize;
    private final int discardsSize;
    
    /**
     * Constructor.
     * @param faceUpCards (List<Card>) : the five visible cards.
     * @param deckSize (int) : size of the deck.
     * @param discardsSize (int) : size of the discard.
     * @throws IllegalArgumentException if size of faceUpCards is not 5,
     *                                  or if deckSize is negative,
     *                                  or if discardsSize is negative.
     */
    public PublicCardState(List<Card> faceUpCards, int deckSize, int discardsSize) {
        Preconditions.checkArgument(faceUpCards.size() == Constants.FACE_UP_CARDS_COUNT);
        Preconditions.checkArgument(!(deckSize < 0));
        Preconditions.checkArgument(!(discardsSize < 0));

        this.faceUpCards = List.copyOf(faceUpCards);
        this.deckSize = deckSize;
        this.discardsSize = discardsSize;
    }
    
    /** UNUSED METHOD.
     * @return the amount of cards which does not belong to the players (int).
     */
    /*public int totalSize() {
        return deckSize + discardsSize + Constants.FACE_UP_CARDS_COUNT;
    }*/
    
    /**
     * @return the list of the faceUpCards (List<Card>).
     */
    public List<Card> faceUpCards() { return faceUpCards; }
    
    /**
     * To get a certain card from those face up.
     * @param slot (int) : the slot of interest.
     * @return card (Card) : the card of the given index from those which are face up.
     * @throws IndexOutOfBoundsException if slot is not between 0 (inclusive) and 
     *                                      Constants.FACE_UP_CARDS_COUNT (exclusive).
     */
    public Card faceUpCard(int slot) {
        return faceUpCards.get(Objects.checkIndex(slot, Constants.FACE_UP_CARDS_COUNT));
    }
    
    /**
     * To get the deck size.
     * @return the deck size (int).
     */
    public int deckSize() { return deckSize; }
    
   /**
    * States if the emptiness of the deck.
    * @return (boolean) : value that states if the deck is empty (true) or not (false).
    */
    public boolean isDeckEmpty() { return deckSize == 0; }
  
   /**
    * To get the discards size.
    * @return the discard size (int).
    */
    public int discardsSize() { return discardsSize; }
   
}
