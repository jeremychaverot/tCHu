package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.tchu.Preconditions;

 /**
  * @author Jérémy Chaverot (315858)
  */
public class PublicPlayerState {

    private final int ticketCount;
    private final int cardCount;
    private final List<Route> routes;
    private final int carCount;
    private final int claimPoints;
    private final Trail longestTrail;
    
    /**
     * Constructor.
     * @param ticketCount (int) : the amount of tickets.
     * @param cardCount (int) : the amount of cards.
     * @param routes (List<Route>) : the route the player possesses.
     * @param longestTrail (Trail) : one of the longest trail of the game that the player possesses (can be null).
     * @throws IllegalArgumentException if ticketCount is strictly smaller than 0,
     *                                  or if cardCount is strictly smaller than 0.
     */
    public PublicPlayerState(int ticketCount, int cardCount, List<Route> routes, Trail longestTrail) {
        Preconditions.checkArgument(ticketCount >= 0);
        Preconditions.checkArgument(cardCount >= 0);

        this.ticketCount = ticketCount;
        this.cardCount = cardCount;
        this.routes = List.copyOf(routes);
        this.longestTrail = longestTrail;


        var carCountBuilder = Constants.INITIAL_CAR_COUNT;
        var claimPointBuilder = 0;
            for (Route r : this.routes) {
                carCountBuilder -= r.length();
                claimPointBuilder += r.claimPoints();
            }

        carCount = carCountBuilder;
        claimPoints = claimPointBuilder;
    }
    
    /**
     * To get the amount of tickets.
     * @return ticketCount (int) : the amount of tickets.
     */
    public int ticketCount() {
        return ticketCount;
    }
    
    /**
     * To get the amount of cards.
     * @return cardCount (int) : the amount of cards.
     */
    public int cardCount() {
        return cardCount;
    }
    
    /**
     * To get the routes possessed by the player.
     * @return routes (List<Route>) : the routes of the player.
     */
    public List<Route> routes() { return new ArrayList<>(routes); }
    
    /**
     * To get the player's amount of remaining cars.
     * @return carCount (int) : the amount of remaining cars.
     */
    public int carCount() {
        return carCount;
    }
    
    /**
     * To get the total of points the routes of the player are worth.
     * @return claimPoints (int) : the amount of points of the player's routes.
     */
    public int claimPoints() {
        return claimPoints;
    }

     /**
      * To get one of the longest trail of the game that the player is the owner of.
      * @return longestTrail (Trail) : one of the longest trail of the game that the player is the owner of.
      */
     public Trail longestTrail() {
         return longestTrail;
     }

}
