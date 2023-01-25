package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

/**
 * @author Jérémy Chaverot (315858)
 */
public final class CardState extends PublicCardState {

    private final Deck<Card> deck;
    private final SortedBag<Card> discards;

    /**
     * To have a configuration where the Constants.FACE_UP_CARDS_COUNT top cards of the deck are the one face up,
     * the deck is made up of the left cards and the discard is empty.
     * @param deck (Deck<Card>) : the deck we want to make the configuration with.
     * @return cardState (CardState) : a state where the face up cards are the Constants.FACE_UP_CARDS_COUNT on the
     * the top of the deck, the deck is made up of the left cards and the discard is empty.
     * @throws IllegalArgumentException if the deck contains less than and the face up cards count.
     */
    public static CardState of(Deck<Card> deck) {
        Preconditions.checkArgument(deck.size() >= Constants.FACE_UP_CARDS_COUNT);

        List<Card> cards = new ArrayList<>();
        for (int i = 0 ; i < Constants.FACE_UP_CARDS_COUNT ; i++) {
            cards.add(deck.topCard());
            deck = deck.withoutTopCard();
        }

        return new CardState(cards, deck, SortedBag.of());
    }

    /**
     * Private constructor.
     */
    private CardState(List<Card> faceUpCards, Deck<Card> deck, SortedBag<Card> discards) {
        super(faceUpCards, deck.size(), discards.size());

        this.deck = deck;
        this.discards = discards;
    }
    
    /**
     * To get the same set of cards, except that the card from the face up ones of index 'slot' will be replaced by
     * the card at the top of the deck.
     * @param slot (int) : index of the face up cards we desire to replace.
     * @return the same set of cards with one face up card replaced.
     * @throws IllegalArgumentException if the deck is empty.
     * @throws IndexOutOfBoundsException if slot is not between 0 (inclusive) and Constants.FACE_UP_CARDS_COUNT (exclusive).
     */
    public CardState withDrawnFaceUpCard(int slot) {
        Preconditions.checkArgument(!isDeckEmpty());

        List<Card> cards = new ArrayList<>(faceUpCards());
        cards.set(Objects.checkIndex(slot, Constants.FACE_UP_CARDS_COUNT), deck.topCard());
        Deck<Card> newDeck = deck.withoutTopCard();

        return new CardState(cards, newDeck, discards);
    }
    
    /**
     * To get a view of the card at the top of the deck.
     * @return card (Card) : the top deck card.
     * @throws IllegalArgumentException if the deck is empty.
     */
    public Card topDeckCard() {
        Preconditions.checkArgument(!isDeckEmpty());

        return deck.topCard();
    }

    /**
     * To get the same set of cards without the top deck card.
     * @return cardState (CardState) : a card state where we took the top deck card off.
     * @throws IllegalArgumentException if the deck is empty.
     */
    public CardState withoutTopDeckCard() {
        Preconditions.checkArgument(!isDeckEmpty());

        Deck<Card> newDeck = deck.withoutTopCard();

        return new CardState(faceUpCards(), newDeck, discards);
    }
    
    /**
     * From a set of cards where the deck is empty, we recreate the deck from the discards mixed.
     * @param rng (Random) : random number generator.
     * @return cardState (CardState) : the same set of cards with a refilled deck.
     * @throws IllegalArgumentException if the deck is not empty.
     */
    public CardState withDeckRecreatedFromDiscards(Random rng) {
        Preconditions.checkArgument(isDeckEmpty());

        Deck<Card> newDeck = Deck.of(discards, rng);

        return new CardState(faceUpCards(), newDeck, SortedBag.of());
    }
    
    /**
     * To get the same set of cards, but with cards added to the discards. 
     * @param additionalDiscards (Sorted<Card>) : the cards we want to add to the discards.
     * @return cardState (CardState) : the same set of cards, but we added cards to the discards.
     */
    public CardState withMoreDiscardedCards(SortedBag<Card> additionalDiscards) {
        return new CardState(faceUpCards(), deck, discards.union(additionalDiscards));
    }

}
