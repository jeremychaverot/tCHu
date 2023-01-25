package ch.epfl.tchu.game;

import java.util.List;
import java.util.TreeSet;
import ch.epfl.tchu.Preconditions;

/**
 * @author Cristian Safta (324694)
 * @author Jérémy Chaverot (315858)
 **/
public final class Ticket implements Comparable<Ticket> {
    
    private final List<Trip> trips;
    private final String text;
    
    /**
     * Primary constructor.
     * @param trips (List<Trip>) : list of trips that are to put in the ticket.
     * @throws IllegalArgumentException if the size of trip is 0,
     * or if each departure stations in trips are not the same.
     */
    public Ticket(List<Trip> trips) {
        Preconditions.checkArgument(trips.size() != 0);
        Preconditions.checkArgument(checkTripList(trips));
        
        this.trips = List.copyOf(trips);
        this.text = computeText(trips);
    }
    
    /**
     * Secondary constructor.
     * @param from (Station) : departure station.
     * @param to (Station) : arrival station.
     * @param points (int) : number of points the trip gives.
     */
    public Ticket(Station from, Station to, int points) {
        this(List.of(new Trip(from, to, points)));
    }
    
    /**
     * To get the text of the ticket.
     * @return text (String) : the text of the ticket.
     */
    public String text() {
        return text;
    }

    /**
     * Tells how much points the ticket is currently worth.
     * @param connectivity (StationConnectivity) : the connectivity is the one of the player owning the ticket.
     * @return points (int) : the ticket's amount of point.
     */
    public int points(StationConnectivity connectivity) {
        int max = 0;
        int min = Integer.MAX_VALUE;
        int points ;

        for (Trip t : trips) {
            if (t.points() < min)
                min = t.points();

            if (connectivity.connected(t.from(), t.to()) && t.points() > max)
                max = t.points();
        }

        points = (max != 0)
                ? max
                : -min;

        return points;
    }

    /**
     * Helper method used to compute the text of the ticket.
     * @return text (String) : the text of the ticket.
     */
    private static String computeText(List<Trip> trips) {
        TreeSet<String> s = new TreeSet<>();
        for(Trip t1 : trips) {
            s.add(t1.to().name() + " (" + t1.points() + ")");
        }

        if (trips.size() == 1)
            return trips.get(0).from().name() + " - " + trips.get(0).to().name() + " (" +trips.get(0).points() + ")";
        else {
            String s2 = String.join(", ", s);
            return trips.get(0).from().name() + " - {" + s2 + "}";
        }
    }

    /**
     * Method used to check if the names of each departure stations in the list of trips are the same.
     * Used in the constructor.
     * @param tripList (List<Trip>) : the list of trips that is being checked.
     * @return checker (boolean) : false if the names aren't the same, true if they are.
     */
    private boolean checkTripList(List<Trip> tripList) {
        String expectedName = tripList.get(0).from().name();
        for (Trip t1 : tripList) {
            if (!t1.from().name().equals(expectedName))
                return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return text();
    }

    @Override
    public int compareTo(Ticket that) {
        return text.compareTo(that.text());
    }

}
