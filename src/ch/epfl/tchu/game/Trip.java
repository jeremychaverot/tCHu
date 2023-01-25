package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import ch.epfl.tchu.Preconditions;

/** 
 * @author Cristian Safta (324694)
 */
public final class Trip {

    private final Station from;
    private final Station to;
    private final int points;

    /**
     * To create from a list of departure stations and a list of arrival stations a list of trip along with the points.
     * @param from (List) : a list with all Departure stations.
     * @param to (List) : a list with all Arrival stations.
     * @return all (List<Trip) : list containing all the possible trips from the departure stations to the arrival
     * stations along with their associated points.
     */
    public static List<Trip> all(List<Station> from, List<Station> to, int points) {
        Preconditions.checkArgument(from.size() != 0);
        Preconditions.checkArgument(to.size() != 0);

        List<Trip> trips = new ArrayList<>();
        for(Station s1 : from) {
            for(Station s2 : to) {
                trips.add(new Trip(s1, s2, points));
            }
        }

        return trips;
    }
    
    /**
     * Constructor.
     * @param from (Station) : departure station.
     * @param to (Station) : arrival station.
     * @param points (int) : the amount of points the trip is worth.
     * @throws IllegalArgumentException if points is smaller or equal to 0.
     */
    public Trip(Station from, Station to, int points) {
      Preconditions.checkArgument(points > 0);

      this.from = Objects.requireNonNull(from);
      this.to = Objects.requireNonNull(to);
      this.points = points;
    }

    /**
     * To get the departure station of the trip.
     * @return from (Station) : the arrival station
     */
    public Station from() { return from; }
    
    /**
     * To get the arrival station of the trip.
     * @return to (Station) : the arrival station
     */
    public Station to() { return to; }
    
    /**
     * To get the amount of points we earn when completing the trip.
     * @return points (int) : the amount of points
     */
    public int points() { return points; }
    
    /**
     * To get the amount of points given the connectivity.
     * @param connectivity (StationConnectivity).
     * @return points (int) : the amount of points.
     */
    public int points(StationConnectivity connectivity) {
        return connectivity.connected(to,from)
                ? points
                : -points;
    }

}
