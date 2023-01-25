/*
package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class TestServer {

    public static void main(String[] args) throws IOException {

        System.out.println("Starting server!");
        try (ServerSocket serverSocket = new ServerSocket(5108);
             Socket socket = serverSocket.accept()) {
            Player playerProxy = new RemotePlayerProxy(socket);

            var playerNames = Map.of(PlayerId.PLAYER_1, "Ada", PlayerId.PLAYER_2, "Charles");
            playerProxy.initPlayers(PlayerId.PLAYER_1, playerNames);
            playerProxy.receiveInfo("This is a test!");

            Map<PlayerId, PublicPlayerState> p = new HashMap<>();
                p.put(PlayerId.PLAYER_1, new PublicPlayerState(1, 1, ChMap.routes().subList(0, 10)));
                p.put(PlayerId.PLAYER_2, new PublicPlayerState(2, 2, ChMap.routes().subList(10, 20)));
            playerProxy.updateState(new PublicGameState(2, new PublicCardState(Card.ALL.subList(0, 5), 5, 5),
                            PlayerId.PLAYER_1, p, null),
                    new PlayerState(SortedBag.of(1, ChMap.tickets().get(0), 1, ChMap.tickets().get(1)),
                            SortedBag.of(1, Card.GREEN, 1, Card.BLACK),
                            List.of(ChMap.routes().get(0))));

            var sortedBagBuilder = new SortedBag.Builder<Ticket>();
            Random rng = new Random();
            for (int i = 0; i < 5; i++) {
                sortedBagBuilder.add(ChMap.tickets().get(rng.nextInt(ChMap.tickets().size())));
            }
            playerProxy.setInitialTicketChoice(sortedBagBuilder.build());

            System.out.printf("Chosen initial tickets : %s\n", playerProxy.chooseInitialTickets());
            System.out.printf("Next turn kind : %s\n", playerProxy.nextTurn());
            System.out.printf("Draw slot : %s\n", playerProxy.drawSlot());
            System.out.printf("Route to claim : %s\n", playerProxy.claimedRoute().stations());
            System.out.printf("Initial cards : %s\n", playerProxy.initialClaimCards());
            System.out.printf("Chosen cards : %s\n", playerProxy.chooseAdditionalCards(
                    List.of(SortedBag.of(1, Card.BLUE, 1, Card.LOCOMOTIVE),
                            SortedBag.of(1, Card.RED, 1, Card.YELLOW),
                            SortedBag.of(1, Card.GREEN, 1, Card.BLACK))));

        }
        System.out.println("Server done!");
    }
}
*/
