package ch.epfl.tchu.game;

import java.util.Objects;
import java.util.ArrayList;
import java.util.List;
import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

/**
 * @author Jérémy Chaverot (315858)
 */
public final class Route {

    private final String id;
    private final Station station1;
    private final Station station2;
    private final int length;
    private final Level level;
    private final Color color;
    
    /**
     * Constructor.
     * @param id (String) : road identity.
     * @param station1 (Station) : first station of the road.
     * @param station2 (Station) : second station of the road.
     * @param length (int) : length of the road.
     * @param level (Level) : level of the road.
     * @param color (Color) : color of the road.
     * @throws IllegalArgumentException if station1 equals station2 or the length of the road is out of bound.
     * @throws NullPointerException if station 1 or station2 or level is null.
     */
    public Route(String id, Station station1, Station station2, int length, Level level, Color color) {
        Preconditions.checkArgument(!station1.equals(station2));
        Preconditions.checkArgument(Constants.MIN_ROUTE_LENGTH <= length
                && length <= Constants.MAX_ROUTE_LENGTH);

        this.id = Objects.requireNonNull(id);
        this.station1 = Objects.requireNonNull(station1);
        this.station2 = Objects.requireNonNull(station2);
        this.length = length;
        this.level = Objects.requireNonNull(level);
        this.color = color;
    }

    /**
     * The two possible types of road.
     */
    public enum Level {
        OVERGROUND,
        UNDERGROUND
    }

   /**
    * To get the identity of the road.
    * @return id (String) : the identity of the road.
    */
   public String id() { return id; }
   
   /**
    * To get the first station.
    * @return station1 (Station) : the first station.
    */
   public Station station1() { return station1; }

   /**
    * To get the second station.
    * @return station2 (Station) : the second station.
    */
   public Station station2() { return station2; }
   
   /**
    * To get the length of the road.
    * @return length (int) : the length of the road.
    */
   public int length() { return length; }
   
   /**
    * To get the level of the road.
    * @return level (Level) : the ground level of the road.
    */
   public Level level() { return level; }
    
   /**
    * @return the color of the road, or null if it is a neutral colored road.
    */
   public Color color() { return color; }
   
   /**
    * @return a list of the two stations.
    */
   public List<Station> stations() { return List.of(station1, station2); }
   
    /**
     * @param station (Station) : the station already known and of which we want to know the partner.
     * @return the opposite station (Station) : the station which is not the one given.
     * @throws IllegalArgumentException if the given station is neither the first nor the second of the road.
     */
   public Station stationOpposite(Station station) {
       if (station.equals(station1))
           return station2;
       else if (station.equals(station2))
              return station1;
            else throw new IllegalArgumentException();
   }
   
   /**
    * Returns a list of all the possible card to claim the road.
    * Sorted in ascending order of locomotive then in color.
    * @return list (List<SortedBag<Card>>) : list of the possible claim cards.
    */
   public List<SortedBag<Card>> possibleClaimCards() {
       List<SortedBag<Card>> list = new ArrayList<>();
      
       switch (level) {
           case OVERGROUND :
               if (color != null) {
                   list.add(SortedBag.of(length, Card.of(color)));
               } else {
                   for (Card c : Card.CARS) {
                       list.add(SortedBag.of(length, c));
                   }
               }
               break;

           case UNDERGROUND :
               if (color != null) {
                   for (int i = 0; i <= length; i++) {
                       list.add(SortedBag.of(length-i, Card.of(color), i, Card.LOCOMOTIVE));
                   }
               } else {
                   for (int i = 0; i < length; i++) {
                       for (Card c : Card.CARS) {
                           list.add(SortedBag.of(length-i, c , i, Card.LOCOMOTIVE));
                       }
                   }
                   list.add(SortedBag.of(length, Card.LOCOMOTIVE));
               }
               break;
       }

       return list;
   }


   /**
    * Returns the amount of additional cards needed when trying to claim an underground road.
    * @param claimCards (SortedBag<Card>) : initially played cards.
    * @param drawnCards (SortedBag<Card>) : the three cards drawn at the top of the deck.
    * @return amount (int) : the amount of additional cards needed.
    * @throws IllegalArgumentException if the level is not Level.UNDERGROUND,
    *                                  or if the size of drawnCards is not Constants.ADDITIONAL_TUNNEL_CARDS
    */
   public int additionalClaimCardsCount(SortedBag<Card> claimCards, SortedBag<Card> drawnCards) {
       Preconditions.checkArgument(level == Level.UNDERGROUND);
       Preconditions.checkArgument(drawnCards.size() == Constants.ADDITIONAL_TUNNEL_CARDS);

       int counter = 0;
           counter += drawnCards.countOf(Card.LOCOMOTIVE);
           for (Card c : Card.CARS) {
               if (claimCards.contains(c) && drawnCards.contains(c)) {
                   counter += drawnCards.countOf(c);
                   break;
               }
           }

       return counter;
   }
   
   /**
    * Returns the amount of points a player get when acquiring the road.
    * @return points (int) : the amount of points the player gets.
    */
   public int claimPoints() { return Constants.ROUTE_CLAIM_POINTS.get(length); }
    
}
