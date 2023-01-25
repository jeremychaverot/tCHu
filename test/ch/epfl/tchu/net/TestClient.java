/*
package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TestClient {

    public static void main(String[] args) {

        System.out.println("Starting client!");
        RemotePlayerClient playerClient =
                new RemotePlayerClient(new TestPlayer(), "localhost", 5108);

        playerClient.run();
        System.out.println("Client done!");
    }


    private final static class TestPlayer implements Player {

        private SortedBag<Ticket> allInitialTickets;
        private final Random rng = new Random();

        @Override
        public void initPlayers(PlayerId ownId, Map<PlayerId, String> names) {
            System.out.printf("ownId: %s\n", ownId);
            System.out.printf("playerNames: %s\n", names);
        }

        @Override
        public void receiveInfo(String info) {
            System.out.printf("Info received: %s\n", info);
        }

        @Override
        public void updateState(PublicGameState newState, PlayerState ownState) {

            Map<PlayerId, PublicPlayerState> p = new EnumMap<PlayerId, PublicPlayerState>(PlayerId.class);
            p.put(PlayerId.PLAYER_1, new PublicPlayerState(1, 1, ChMap.routes().subList(0, 10)));
            p.put(PlayerId.PLAYER_2, new PublicPlayerState(2, 2, ChMap.routes().subList(10, 20)));

            PublicGameState p1 = new PublicGameState(2, new PublicCardState(Card.ALL.subList(0, 5), 5, 5),
                    PlayerId.PLAYER_1, p, null);
            PlayerState p2 = new PlayerState(SortedBag.of(1, ChMap.tickets().get(0), 1, ChMap.tickets().get(1)),
                    SortedBag.of(1, Card.GREEN, 1, Card.BLACK),
                    List.of(ChMap.routes().get(0)));

            if (p1.ticketsCount() == newState.ticketsCount()
                    && p1.currentPlayerId() == newState.currentPlayerId()
                    && p1.claimedRoutes().equals(newState.claimedRoutes())
                    && p2.tickets().equals(ownState.tickets())
                    && p2.cards().equals(ownState.cards()))
                System.out.printf("updateState call done successfully\n");
        }

        @Override
        public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
            allInitialTickets = tickets;
            System.out.printf("tickets: %s\n", tickets);
        }

        @Override
        public SortedBag<Ticket> chooseInitialTickets() {
            return SortedBag.of(1, allInitialTickets.get(0), 1, allInitialTickets.get(4));
        }

        @Override
        public TurnKind nextTurn() {
            return TurnKind.ALL.get(rng.nextInt(TurnKind.ALL.size()));
        }

        @Override
        public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
            System.out.printf("Tickets to choose from: %s\n", options);
            return options.difference(SortedBag.of(options.get(1)));
        }

        @Override
        public int drawSlot() {
            if (rng.nextInt(2) == 0) return -1;
            return rng.nextInt(5);
        }

        @Override
        public Route claimedRoute() {
            return ChMap.routes().get(0);
        }

        @Override
        public SortedBag<Card> initialClaimCards() {
            return SortedBag.of(1, Card.ALL.get(rng.nextInt(Card.ALL.size())),
                    1, Card.ALL.get(rng.nextInt(Card.ALL.size())));
        }

        @Override
        public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
            System.out.printf("List of sorted bag of possible cards: %s\n", options);
            return options.get(0);
        }

    }

}
*/
