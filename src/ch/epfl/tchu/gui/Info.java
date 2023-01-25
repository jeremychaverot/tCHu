package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Trail;

import java.util.List;

/**
 * The class Info goal is to build the information messages of the game that will be sent to the players.
 * @author Cristian Safta (324694)
 * @author Jérémy Chaverot (315858)
 */
public final class Info {

    /**
     * Returns the name of the card in french.
     * @param card (Card) : the card.
     * @param count (int) : the number of cards.
     * @return cardName (String) : the name of the card in french.
     * @throws IllegalArgumentException if card is null.
     */
    public static String cardName(Card card, int count) {
        if (card == Card.LOCOMOTIVE) {
            return StringsFr.LOCOMOTIVE_CARD + StringsFr.plural(count);
        }
        switch (card.color()) {
            case BLUE:
                return StringsFr.BLUE_CARD + StringsFr.plural(count);
            case BLACK:
                return StringsFr.BLACK_CARD + StringsFr.plural(count);
            case GREEN:
                return StringsFr.GREEN_CARD + StringsFr.plural(count);
            case ORANGE:
                return StringsFr.ORANGE_CARD + StringsFr.plural(count);
            case RED:
                return StringsFr.RED_CARD + StringsFr.plural(count);
            case VIOLET:
                return StringsFr.VIOLET_CARD + StringsFr.plural(count);
            case WHITE:
                return StringsFr.WHITE_CARD + StringsFr.plural(count);
            case YELLOW:
                return StringsFr.YELLOW_CARD + StringsFr.plural(count);
            default: throw new Error();
        }
    }

    /**
     * Returns the message that players terminated round with x amount of points.
     * @param playerNames (List<String>) : the name of the players.
     * @param points (int) : the points the players got.
     * @return draw (String) : the message that players terminated round with x amount of points.
     */
    public static String draw(List<String> playerNames, int points) {
        String names = "";

        for (int i = 0; i < playerNames.size(); i++) {
            names = names.concat(playerNames.get(i));
            if (i < playerNames.size() - 2)
                names = names.concat(", ");
            if (i == (playerNames.size() - 2))
                names = names.concat(StringsFr.AND_SEPARATOR);
        }

        return String.format(StringsFr.DRAW, names, points);
    }

    /**
     * Returns the string containing a more friendly-readable version a set of cards.
     * @param cards (SortedBag<Card>) : set of cards we want to obtain the string of.
     * @return cardsToString (String) : the string containing the more friendly-readable version a set of cards.
     */
    public static String cardsToString(SortedBag<Card> cards) {
        List<Card> cardList = cards.toList();
        List<Card> cardSublist = cardList.subList(0, cardList.size() - cards.countOf(cards.get(cards.size() - 1)));
        String s = "";
        Card prevCard = null;

        if (cardSublist.isEmpty())
            s = cards.countOf(cardList.get(0)) + " " + cardName(cardList.get(0), cards.countOf(cardList.get(0)));
        else {
            for (Card c : cardSublist) {
                if (prevCard == null)
                    s = cards.countOf(c)+ " " + cardName(c, cards.countOf(c));
                else if (c != prevCard)
                    s = s.concat(", " + cards.countOf(c) + " " + cardName(c, cards.countOf(c)));
                prevCard = c;
            }
            s = s.concat(StringsFr.AND_SEPARATOR + cards.countOf(cardList.get(cardList.size() - 1))+ " "
                    + cardName(cardList.get(cardList.size() - 1), cards.countOf(cardList.get(cardList.size() - 1))));
        }

        return s;
    }

    private final String playerName;

    /**
     * Constructor.
     * @param playerName (String) : the name of the player the text messages will contain.
     */
    public Info(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Returns a message that indicated player will play first.
     * @return willPlayFirst (String) : a message that indicated player will play first.
     */
    public String willPlayFirst() {
        return String.format(StringsFr.WILL_PLAY_FIRST, playerName);
    }

    /**
     * Returns the message that player has kept count tickets.
     * @param count (int) : count the number of tickets kept.
     * @return keptTickets (String) : the message that player has kept count tickets.
     */
    public String keptTickets(int count) {
        return String.format(StringsFr.KEPT_N_TICKETS, playerName, count, StringsFr.plural(count));
    }

    /**
     * Returns a message that the player can play.
     * @return canPlay (String) : a message that the player can play.
     */
    public String canPlay() {
        return String.format(StringsFr.CAN_PLAY, playerName);
    }

    /**
     * Returns a message that player has drew count tickets.
     * @param count (int) : the number of tickets the player drew.
     * @return drewTickets (String) : a message that player has drew count tickets.
     */
    public String drewTickets(int count) {
        return String.format(StringsFr.DREW_TICKETS, playerName, count, StringsFr.plural(count));
    }

    /**
     * Returns a message that player has drew a blind card from the deck.
     * @return drewBlindCard (String) : a message that player has drew a blind card from the deck.
     */
    public String drewBlindCard() {
        return String.format(StringsFr.DREW_BLIND_CARD, playerName);
    }

    /**
     * Returns a message that player drew a visible card.
     * @param card (Card) : the card the player drew.
     * @return drewVisibleCard (String) : a message that player drew a visible card.
     */
    public String drewVisibleCard(Card card) {
        return String.format(StringsFr.DREW_VISIBLE_CARD, playerName, cardName(card,1));
    }

    /**
     * Returns a message that the player claimed a route.
     * @param route (Route) : the route the player claimed.
     * @param cards (SortedBag<Card>) : the cards the player used ot claim the route.
     * @return claimedRoute (String) : the message that the player claimed the route.
     */
    public String claimedRoute(Route route, SortedBag<Card> cards) {
        return String.format(StringsFr.CLAIMED_ROUTE, playerName, routeString(route), cardsToString(cards));
    }

    /**
     * Returns a message that indicated the player attempts to claim a tunnel using cards.
     * @param route (Route) : the tunnel that the player attempts to claim.
     * @param initialCards (SortedBag<Card>): the cards the player uses to claim the tunnel.
     * @return attemptsTunnelClaim (String) : message that indicates the player attempts to claim a tunnel using cards.
     */
    public String attemptsTunnelClaim(Route route, SortedBag<Card> initialCards) {
        return String.format(StringsFr.ATTEMPTS_TUNNEL_CLAIM, playerName, routeString(route), cardsToString(initialCards));
    }

    /**
     * Returns a message that indicated a player drew additional cards.
     * @param drawnCards (SortedBag<Card>) : the cards player drew.
     * @param additionalCost (int) : the additional cost associated to those cards.
     * @return drewAdditionalCards (String) : a message that indicated a player drew additional cards.
     */
    public String drewAdditionalCards(SortedBag<Card> drawnCards, int additionalCost) {
        String s1 = String.format(StringsFr.ADDITIONAL_CARDS_ARE, cardsToString(drawnCards));
        String s2 = additionalCost > 0
                ? String.format(StringsFr.SOME_ADDITIONAL_COST,additionalCost, StringsFr.plural(additionalCost))
                : StringsFr.NO_ADDITIONAL_COST;

        return s1.concat(s2);
    }

    /**
     * Returns a message that indicated that the player didnt claim a route.
     * @param route(Route) : the route the player did not claim.
     * @return didNotClaimRoute (String) : a message that indicated that the player didnt claim a route.
     */
    public String didNotClaimRoute(Route route) {
        return String.format(StringsFr.DID_NOT_CLAIM_ROUTE, playerName, routeString(route));
    }

    /**
     * Returns a message that indicated that the last turn begins.
     * @param carCount (int) : the number of cars the player has left.
     * @return lastTurnBegins (String) : a message that indicated that the last turn begins.
     */
    public String lastTurnBegins(int carCount) {
        return String.format(StringsFr.LAST_TURN_BEGINS, playerName, carCount, StringsFr.plural(carCount));
    }

    /**
     * Returns a message that indicates player gets 10 points bonus from having the longest trail.
     * @param longestTrail (Trail) : the longest trail.
     * @return longestTrailBonus (String) : a message that indicates player gets 10 points bonus
     * from having the longest trail.
     */
    public String getsLongestTrailBonus(Trail longestTrail) {
        return String.format(StringsFr.GETS_BONUS, playerName, trailString(longestTrail));
    }

    /**
     * Returns a message that indicated a player has won.
     * @param points (int) : the points of the winner.
     * @return won (String) : a message that indicated a player has won.
     */
    public String won(int points) {
        return String.format(StringsFr.WINS, playerName, points, StringsFr.plural(points));
    }

    /**
     * Returns a message that indicated the points of the player.
     * @param points (int) : the points of the player.
     * @return points (String) : a message that indicated the points the player has at the end of the game.
     */
    public String getPoints(int points) {
        return String.format(StringsFr.POINTS, playerName, points, StringsFr.plural(points));
    }

    private String routeString(Route route) {
        return route.station1() + StringsFr.EN_DASH_SEPARATOR+route.station2();
    }

    private String trailString(Trail trail){
        return trail.station1() + StringsFr.EN_DASH_SEPARATOR + trail.station2();
    }

}
