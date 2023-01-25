/*
package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class SerdesTest {

    @Test
    void integerSerdeWorksWithTrivialValues() {
        int a = 2021;
        String b = Serdes.integerSerde.serialize(a);
        int c = Serdes.integerSerde.deserialize(b);
        assertEquals(a, c);
        assertEquals(b, "2021");
        assertEquals(1942,Serdes.integerSerde.deserialize(Serdes.integerSerde.serialize(1942)));
        assertEquals(0,Serdes.integerSerde.deserialize(Serdes.integerSerde.serialize(0)));
        assertEquals(-1,Serdes.integerSerde.deserialize(Serdes.integerSerde.serialize(-1)));
        assertEquals(-32455,Serdes.integerSerde.deserialize(Serdes.integerSerde.serialize(-32455)));
    }
    
    @Test 
    void stringSerdeWorksWithTrivialValues(){
        assertEquals("Q2hhcmxlcw==", Serdes.stringSerde.serialize("Charles"));
        assertEquals("Hello there!", Serdes.stringSerde.deserialize(Serdes.stringSerde.serialize("Hello there!")));
        assertEquals("Never gonna give you up", Serdes.stringSerde.deserialize(Serdes.stringSerde.serialize("Never gonna give you up")));
        assertEquals("afsdlkjjlkafsdafsd", Serdes.stringSerde.deserialize(Serdes.stringSerde.serialize("afsdlkjjlkafsdafsd")));
        assertEquals("?.,", Serdes.stringSerde.deserialize(Serdes.stringSerde.serialize("?.,")));
    }
    
    @Test
    void playerIdSerdeWorks() {
        assertEquals("0", Serdes.playerIdSerde.serialize(PlayerId.PLAYER_1));
        assertEquals("1", Serdes.playerIdSerde.serialize(PlayerId.PLAYER_2));
    }
    
    @Test
    void stringListSerdeWorksWithTrivialValues() {
        List<String> s = List.of("hi ","I ","am","John");
        assertEquals(s,Serdes.stringListSerde.deserialize(Serdes.stringListSerde.serialize(s)));
    }
    
    @Test
    void cardSortedBagSerdeWorksWithTrivialValue() {
        SortedBag.Builder<Card> cards = new SortedBag.Builder<>();
        cards.add(Card.BLUE);
        cards.add(Card.LOCOMOTIVE);
        SortedBag cards1 = cards.build();
        assertEquals(cards1, Serdes.cardSortedBagSerde.deserialize(Serdes.cardSortedBagSerde.serialize(cards1)));
    }
    
    @Test
    void publicCardStateSerdeWorks() {
        List<Card> faceUpCards = List.of(Card.BLACK,Card.BLACK,Card.BLUE,Card.LOCOMOTIVE,Card.WHITE);
        PublicCardState state1 = new PublicCardState(faceUpCards, 10, 7);
        assertEquals(state1.deckSize(), Serdes.publicCardStateSerde.deserialize(Serdes.publicCardStateSerde.serialize(state1)).deckSize());
        assertEquals(state1.discardsSize(), Serdes.publicCardStateSerde.deserialize(Serdes.publicCardStateSerde.serialize(state1)).discardsSize());
        assertEquals(state1.faceUpCard(1), Serdes.publicCardStateSerde.deserialize(Serdes.publicCardStateSerde.serialize(state1)).faceUpCard(1));
    }

    @Test
    void serdesWorksWithStatementExample() {
        List<Card> fu = List.of(Card.RED, Card.WHITE, Card.BLUE, Card.BLACK, Card.RED);
        PublicCardState cs = new PublicCardState(fu, 30, 31);
        List<Route> rs1 = ChMap.routes().subList(0, 2);
        Map<PlayerId, PublicPlayerState> ps = Map.of(
                PlayerId.PLAYER_1, new PublicPlayerState(10, 11, rs1),
                PlayerId.PLAYER_2, new PublicPlayerState(20, 21, List.of()));
        PublicGameState gs =
                new PublicGameState(40, cs, PlayerId.PLAYER_2, ps, null);
        String l = Serdes.publicGameStateSerde.serialize(gs);
        assertEquals("40:6,7,2,0,6;30;31:1:10;11;0,1:20;21;:", l);
    }

    @Test
    void serdesWorksWithTrivialExample() {
        SortedBag.Builder<Card> fu = new SortedBag.Builder<>();
        fu.add(Card.ALL.get(0));
        fu.add(Card.ALL.get(1));
        fu.add(Card.ALL.get(2));
        fu.add(Card.ALL.get(3));
        PlayerState p = new PlayerState(SortedBag.of(), fu.build(), List.of());

        assertEquals(";0,1,2,3;", Serdes.playerStateSerde.serialize(p));
    }

    @Test
    void serdesThrowsExceptionWhenNullArgument() {
        assertThrows(NullPointerException.class, () -> {
            Serdes.stringListSerde.serialize(null);
        });
        assertThrows(NullPointerException.class, () -> {
            Serdes.stringListSerde.deserialize(null);
        });
    }

    @Test
    void serdeThrowsExceptionWhenArgumentDontBelongToEnumValues() {
        assertThrows(NumberFormatException.class, () -> {
            System.out.println(Serdes.cardSerde.deserialize(Color.BLUE.name()));
        });
    }

    @Test
    void serdeListOfWorksWithEmptyArgument() {
        assertEquals(List.of(), Serdes.stringListSerde.deserialize(""));
        assertEquals("", Serdes.stringListSerde.serialize(List.of()));
    }

}
*/
