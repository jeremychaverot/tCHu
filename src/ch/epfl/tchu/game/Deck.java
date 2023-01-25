package ch.epfl.tchu.game;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

/** 
 * @author Cristian Safta (324694)
 */
public final class Deck <C extends Comparable<C>> {

    private final List<C> cards;

    /**
     * Static method that takes a SortedBag of cards and returns a deck of shuffled cards of that type.
     * @param <C> : type of cards
     * @param cards (SortedBag) : a sorted bag of cards of type C
     * @param rng (Random): rng element used to shuffle the cards
     * @return deck (Deck) : a deck of shuffled cards
     */
    public static <C extends Comparable<C>> Deck<C> of(SortedBag<C> cards, Random rng) {
        List<C> l = cards.toList();
        Collections.shuffle(l, rng);
        return new Deck<>(l);
    }

    /**
     * Private Constructor.
     */
    private Deck(List<C> cards) {
        this.cards = List.copyOf(cards);
    }
    
    /**
     * Returns the size of the deck.
     * @return size (int) : the size of the deck
     */
    public int size() {
        return cards.size();
    }
    
    /**
     * Returns if the deck is empty or not.
     * @return isEmpty (boolean) : true if the deck is empty false if not.
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }
    
    /**
     * Returns the card at the top of the deck.
     * @return topCard (<C>) : the card at the top of the deck
     * @throws IllegalArgumentException if the deck is empty
     */
    public C topCard() {
        Preconditions.checkArgument(!isEmpty());

        return cards.get(0);
    }
    
    /**
     * Returns a new deck without the top card.
     * @return deck (Deck) : a new deck without the top card
     * @throws IllegalArgumentException if the deck is empty
     */
    public Deck<C> withoutTopCard() {
        Preconditions.checkArgument(!isEmpty());

        final List<C> cardsCopy = cards.subList(1, cards.size());

        return new Deck<>(cardsCopy);
    }
    
    /**
     * Returns a sortedBag of the top number of cards of the deck given by variable count.
     * @param count (int) : the number of cards that will be returned from the top of the deck
     * @return topCards (SortedBag) : a sortedBag of the top number of cards of the deck given by variable count
     * @throws IllegalArgumentException if count is less than 0 or more than the number of cards the deck has
     */
    public SortedBag<C> topCards(int count) {
        Preconditions.checkArgument(count >= 0 && count <= cards.size());

        SortedBag.Builder<C> topCards = new SortedBag.Builder<>();
            for (int i = 0 ; i < count ; i++) {
                topCards.add(cards.get(i));
            }

        return topCards.build();
    }
    
    /**
     * Returns a new deck without the top count cards.
     * @param count (int) : the number of cards from the top of the deck that will be left out
     * @return withoutTopCards (Deck) : a new deck without the top count cards
     * @throws IllegalArgumentException if count is less that 0 or more than the number of cards the deck has
     */
    public Deck<C> withoutTopCards(int count) {
        Preconditions.checkArgument(count >= 0 && count <= cards.size());

        final List<C> cardsCopy = cards.subList(count, cards.size());

        return new Deck<>(cardsCopy);
    }

}
