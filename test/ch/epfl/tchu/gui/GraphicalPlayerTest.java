/*
package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

    public final class GraphicalPlayerTest extends Application {
        public static void main(String[] args) {
            launch(args);
        }

        private void setState(GraphicalPlayer player) {
            PlayerState p1State = new PlayerState(SortedBag.of(ChMap.tickets().subList(0, 10)),
                    SortedBag.of(1, Card.WHITE, 3, Card.RED),
                    ChMap.routes().subList(0, 3));

            PublicPlayerState p2State =
                    new PublicPlayerState(0, 0, ChMap.routes().subList(3, 6));

            Map<PlayerId, PublicPlayerState> pubPlayerStates =
                    Map.of(PlayerId.PLAYER_1, p1State, PlayerId.PLAYER_2, p2State);
            PublicCardState cardState =
                    new PublicCardState(Card.ALL.subList(0, 5), 110 - 2 * 4 - 5, 0);
            PublicGameState publicGameState =
                    new PublicGameState(36, cardState, PlayerId.PLAYER_1, pubPlayerStates, null);
            player.setState(publicGameState, p1State);
        }

        @Override
        public void start(Stage primaryStage) {
            Map<PlayerId, String> playerNames =
                    Map.of(PlayerId.PLAYER_1, "Ada", PlayerId.PLAYER_2, "Charles");
            GraphicalPlayer p = new GraphicalPlayer(PlayerId.PLAYER_1, playerNames);
            setState(p);

            ActionHandlers.DrawTicketsHandler drawTicketsH =
                    () -> p.receiveInfo("\nJe tire des billets !\n");
            ActionHandlers.DrawCardHandler drawCardH =
                    s -> p.receiveInfo(String.format("\nJe tire une carte de %s !\n", s));
            ActionHandlers.ClaimRouteHandler claimRouteH =
                    (r, cs) -> {
                        String rn = r.station1() + " - " + r.station2();
                        p.receiveInfo(String.format("\nJe m'empare de %s avec %s\n", rn, cs));
                    };

            ActionHandlers.ChooseTicketsHandler chooseTicketsH =
                    t -> {
                        System.out.println("Le joueur a choisi les tickets : " + t);
                        p.receiveInfo("\nLe joueur a choisi les tickets : " + t + "\n" );
                    };

            ActionHandlers.ChooseCardsHandler chooseCardsH =
                    c -> {
                        System.out.println("Le joueur a choisi les cartes : " + c);
                        p.receiveInfo("\nLe joueur a choisi les cartes : " + c + "\n" );
            };

            List<SortedBag<Card>> list = List.of(
                    SortedBag.of(1, Card.VIOLET, 3, Card.RED),
                    SortedBag.of(2, Card.WHITE, 1, Card.BLUE),
                    SortedBag.of(6, Card.GREEN, 2, Card.BLACK),
                    SortedBag.of(1, Card.YELLOW, 3, Card.YELLOW),
                    SortedBag.of(1, Card.WHITE, 3, Card.RED)
            );

            p.startTurn(drawTicketsH, drawCardH, claimRouteH);

            p.chooseTickets(SortedBag.of(ChMap.tickets().subList(0, 5)), chooseTicketsH);

            p.chooseClaimCards(list, chooseCardsH);

            p.chooseAdditionalCards(list, chooseCardsH);
        }
    }

*/
