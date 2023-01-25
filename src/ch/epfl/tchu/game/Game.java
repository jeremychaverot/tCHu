package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Player.TurnKind;
import ch.epfl.tchu.gui.Info;

import java.util.*;

/**
 * @author Jérémy Chaverot (315858)
 * @author Cristian Safta (324694)
 */
public final class Game {

    /**
     * Static public method that represents the tCHu game.
     * @param players     (Map<PlayerId, Player>) : the players engaged in the game.
     * @param playerNames (Map<PlayerId, String>) : the names of the player.
     * @param tickets     (SortedBag<Ticket>) : all the tickets available for the game.
     * @param rng         (Random) : instance from Random used to shuffle and generate random.
     * @throws IllegalArgumentException if players is not size PlayerId.COUNT,
     *                                  or if playerNames is not size PlayerId.COUNT.
     */
    public static void play(Map<PlayerId, Player> players, Map<PlayerId, String> playerNames,
                            SortedBag<Ticket> tickets, Random rng) {
        Preconditions.checkArgument(players.size() == playerNames.size());
        List<PlayerId> allPlayers = PlayerId.ALL.subList(0, players.size());

        //Before the game
        GameState gameState = GameState.initial(allPlayers, tickets, rng);
        Map<PlayerId, Info> playerInfos = new HashMap<>();
            for (PlayerId p : allPlayers)
                playerInfos.put(p, new Info(playerNames.get(p)));

        players.forEach((playerId, player) -> player.initPlayers(playerId, playerNames));
        receiveInfo(players, playerInfos.get(gameState.currentPlayerId()).willPlayFirst());

        for (Map.Entry<PlayerId, Player> p : players.entrySet()) {
            p.getValue().setInitialTicketChoice(gameState.topTickets(Constants.INITIAL_TICKETS_COUNT));
            gameState = gameState.withoutTopTickets(Constants.INITIAL_TICKETS_COUNT);
        }

        updateState(players, gameState);

        Map<PlayerId, SortedBag<Ticket>> playerInitialTickets = new HashMap<>();
        for (Map.Entry<PlayerId, Player> p : players.entrySet()) {
            var playerId = p.getKey();
            playerInitialTickets.put(playerId, p.getValue().chooseInitialTickets());
            gameState = gameState.withInitiallyChosenTickets(playerId, playerInitialTickets.get(playerId));
        }

        players.forEach((playerId, player) -> receiveInfo(players, playerInfos.get(playerId)
                .keptTickets(playerInitialTickets.get(playerId).size())));



        //During the game
        int remainingTurns = allPlayers.size();
        boolean lastTurnBegun = false;

        while (remainingTurns > 0) {
            PlayerId currentPlayerId = gameState.currentPlayerId();
            Player currentPlayer = players.get(currentPlayerId);
            Info currentPlayerInfo = playerInfos.get(currentPlayerId);

            receiveInfo(players, currentPlayerInfo.canPlay());
            updateState(players, gameState);
            TurnKind turn = currentPlayer.nextTurn();

            switch (turn) {

                case DRAW_TICKETS:
                    SortedBag<Ticket> drawnTickets = gameState.topTickets(Constants.IN_GAME_TICKETS_COUNT);
                    receiveInfo(players, currentPlayerInfo.drewTickets(Constants.IN_GAME_TICKETS_COUNT));
                    SortedBag<Ticket> keptTickets = currentPlayer.chooseTickets(drawnTickets);
                    gameState = gameState.withChosenAdditionalTickets(drawnTickets, keptTickets);
                    receiveInfo(players, currentPlayerInfo.keptTickets(keptTickets.size()));
                    break;

                case DRAW_CARDS:
                    for (int i = 0 ; i < Constants.IN_GAME_CARDS_COUNT ; i++) {
                        if (i != 0)
                            updateState(players, gameState);
                        gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
                        int slot = currentPlayer.drawSlot();
                        if (slot == Constants.DECK_SLOT) {
                            gameState = gameState.withBlindlyDrawnCard();
                            receiveInfo(players, currentPlayerInfo.drewBlindCard());
                        } else {
                            Card card = gameState.cardState().faceUpCard(slot);
                            gameState = gameState.withDrawnFaceUpCard(slot);
                            receiveInfo(players, currentPlayerInfo.drewVisibleCard(card));
                        }
                    }
                    break;

                case CLAIM_ROUTE:
                    Route claimedRoute = currentPlayer.claimedRoute();
                    SortedBag<Card> initialClaimCards = currentPlayer.initialClaimCards();

                    if (claimedRoute.level() == Route.Level.UNDERGROUND) {

                        receiveInfo(players, currentPlayerInfo.attemptsTunnelClaim(claimedRoute, initialClaimCards));

                        var drawCardsBuilder = new SortedBag.Builder<Card>();
                            for (int i = 0 ; i < Constants.ADDITIONAL_TUNNEL_CARDS ; i++) {
                                gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
                                Card topCard = gameState.topCard();
                                drawCardsBuilder.add(topCard);
                                gameState = gameState.withoutTopCard()
                                        .withMoreDiscardedCards(SortedBag.of(topCard));
                            }
                        SortedBag<Card> drawnCards = drawCardsBuilder.build();

                        int additionalClaimCardsCount = claimedRoute.additionalClaimCardsCount(initialClaimCards, drawnCards);

                        receiveInfo(players, currentPlayerInfo.drewAdditionalCards(drawnCards, additionalClaimCardsCount));

                        if (additionalClaimCardsCount == 0) {
                            gameState = gameState.withClaimedRoute(claimedRoute, initialClaimCards);
                            receiveInfo(players, currentPlayerInfo.claimedRoute(claimedRoute, initialClaimCards));
                        } else {
                            List<SortedBag<Card>> possibleAdditionalCards = gameState.currentPlayerState()
                                    .possibleAdditionalCards(additionalClaimCardsCount, initialClaimCards, drawnCards);
                            if (possibleAdditionalCards.size() == 0)
                                receiveInfo(players, currentPlayerInfo.didNotClaimRoute(claimedRoute));
                            else {
                                SortedBag<Card> chosenAdditionalCards = currentPlayer
                                        .chooseAdditionalCards(possibleAdditionalCards);
                                if (chosenAdditionalCards.size() == 0)
                                    receiveInfo(players, currentPlayerInfo.didNotClaimRoute(claimedRoute));
                                else {
                                    var cards = initialClaimCards.union(chosenAdditionalCards);
                                    gameState = gameState.withClaimedRoute(claimedRoute, cards);
                                    receiveInfo(players, currentPlayerInfo.claimedRoute(claimedRoute, cards));
                                }
                            }
                        }

                    } else {
                        gameState = gameState.withClaimedRoute(claimedRoute, initialClaimCards);
                        receiveInfo(players, currentPlayerInfo.claimedRoute(claimedRoute, initialClaimCards));
                    }
                    break;
            }

           if (lastTurnBegun)
               remainingTurns -= 1;

           if (gameState.lastTurnBegins() && !lastTurnBegun) {
               lastTurnBegun = true;
               receiveInfo(players, currentPlayerInfo.lastTurnBegins(gameState.playerState(currentPlayerId).carCount()));
           }

           if (remainingTurns > 0)
               gameState = gameState.forNextTurn();
        }



        //In the end
        Map<PlayerId, Trail> playerLongestTrails = new HashMap<>();
        Map<PlayerId, Integer> playerFinalPoints = new HashMap<>();
            for (PlayerId p : allPlayers) {
                playerLongestTrails.put(p, Trail.longest(gameState.playerState(p).routes()));
                playerFinalPoints.put(p, gameState.playerState(p).finalPoints());
            }

        Map<Map<PlayerId, Trail>, Integer> unSortedMapOfTrails = new HashMap<>();
            playerLongestTrails.forEach((id, trail) -> unSortedMapOfTrails.put(Map.of(id, trail), trail.length()));
        Map<Map<PlayerId, Trail>, Integer> sortedMapOfTrails = new LinkedHashMap<>();
        unSortedMapOfTrails.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedMapOfTrails.put(x.getKey(), x.getValue()));

        List<Integer> listOfTrailLengths = new ArrayList<>(sortedMapOfTrails.values());
        var occurrences = Collections.frequency(listOfTrailLengths, listOfTrailLengths.get(0));
        Iterator<Map.Entry<Map<PlayerId, Trail>, Integer>> trailsIterator = sortedMapOfTrails.entrySet().iterator();
        for (int i = 0 ; i < occurrences ; i++) {
            var playerId = trailsIterator.next().getKey().keySet().iterator().next();
            var trail = playerLongestTrails.get(playerId);
            playerFinalPoints.put(playerId , playerFinalPoints.get(playerId) + Constants.LONGEST_TRAIL_BONUS_POINTS);
            gameState = gameState.withAddedLongestTrail(playerId, trail);
            receiveInfo(players, playerInfos.get(playerId).getsLongestTrailBonus(trail));
        }

        updateState(players, gameState);

        Map<PlayerId, Integer> sortedMapOfPlayerFinalPoints = new LinkedHashMap<>();
        playerFinalPoints.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedMapOfPlayerFinalPoints.put(x.getKey(), x.getValue()));

        List<Integer> listOfPoints = new ArrayList<>(sortedMapOfPlayerFinalPoints.values());
        var numberOfWinners = Collections.frequency(listOfPoints, listOfPoints.get(0));
        Iterator<Map.Entry<PlayerId, Integer>> pointsIterator = sortedMapOfPlayerFinalPoints.entrySet().iterator();
        if (numberOfWinners == 1) {
            var playerId = pointsIterator.next().getKey();
            receiveInfo(players, playerInfos.get(playerId).won(playerFinalPoints.get(playerId)));
        } else {
            List<String> namesOfWinner = new ArrayList<>();
            for (int i = 0 ; i < numberOfWinners ; i++)
                namesOfWinner.add(playerNames.get(pointsIterator.next().getKey()));
            receiveInfo(players, Info.draw(namesOfWinner, listOfPoints.get(0)));
        }
        String otherPlayersPoints = "";
            while (pointsIterator.hasNext()) {
                var otherId = pointsIterator.next().getKey();
                otherPlayersPoints =
                        otherPlayersPoints.concat(playerInfos.get(otherId).getPoints(sortedMapOfPlayerFinalPoints.get(otherId)));
            }
        if (!otherPlayersPoints.isEmpty())
            receiveInfo(players, otherPlayersPoints);

    }

    private static void receiveInfo(Map<PlayerId, Player> players, String info) {
        players.forEach((playerId, player) -> player.receiveInfo(info));
    }

    private static void updateState(Map<PlayerId, Player> players, GameState newState) {
        players.forEach((playerId, player) -> player.updateState(newState, newState.playerState(playerId)));
    }

    /**
     * Empty private constructor because irrelevant.
     */
    private Game() {}

}
