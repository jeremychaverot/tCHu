package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** 
 * @author Cristian Safta (324694)
 */
public final class Trail {

    private final List<Route> routes;
    private final int length;
    private final Station station1;
    private final Station station2;

    /**
     * Given a list of routes calculates the longest trail that can be formed from those routes.
     * @param routes (List) : a list of routes.
     * @return longest (Trail) : longest trail that can be formed from the list of routes.
     */
    public static Trail longest(List<Route> routes) {
        if (routes.isEmpty())
            return new Trail(null,null,null,0);

        Trail longestTrail = new Trail(Collections.singletonList(routes.get(0)), routes.get(0).station1(),
                routes.get(0).station2());
        List<Trail> cs = new ArrayList<>();
        List<Trail> rs = new ArrayList<>();

        for (Route r : routes) {
            cs.add(new Trail(Collections.singletonList(r), r.station1(), r.station2()));
            cs.add(new Trail(Collections.singletonList(r), r.station2(), r.station1()));
            rs.add(new Trail(Collections.singletonList(r), r.station1(), r.station2()));
            rs.add(new Trail(Collections.singletonList(r), r.station2(), r.station1()));
        }

        while (!cs.isEmpty()) {
            List<Trail> cs2 = new ArrayList<>();

            for (Trail c : cs) {
                for (Trail trail : rs) {
                    if ((c.station2().id() == trail.station1().id()) && (!c.routes.contains(trail.routes.get(0)))) {
                        List<Route> routes1 = new ArrayList<>(c.routes);
                        routes1.add(trail.routes.get(0));
                        cs2.add(new Trail(routes1, c.station1, trail.station2()));
                    }
                }

                if (c.length() > longestTrail.length())
                    longestTrail = new Trail(c.routes, c.station1, c.station2);
            }

            cs = cs2;
        }

        return longestTrail;
    }

    /**
     * Public constructor to build a trail.
     * @param routes (List<Route>) : list of the routes the trail is composed of.
     * @param station1 (Station) : the first station of the trail.
     * @param station2 (Station) : the last station of the trail.
     */
    public Trail(List<Route> routes, Station station1, Station station2) {
        int lengthBuilder = 0 ;
            for(Route route: routes) {
                lengthBuilder += route.length();
            }

        this.length = lengthBuilder;
        this.routes = (routes == null)
                ? List.of()
                : List.copyOf(routes);
        this.station1 = station1;
        this.station2 = station2;
    }

    /**
     * Private constructor.
     */
    private Trail(List<Route> routes, Station station1, Station station2, int length) {
        this.routes = (routes == null)
                ? List.of()
                : List.copyOf(routes);
        this.station1 = station1;
        this.station2 = station2;
        this.length = length;
    }
    
    /**
     * To get the length of the trail.
     * @return length (int) : the length of the trail.
     */
    public int length() {
        return length;
    }
    
    /**
     * To get the first station of the trail.
     * @return station1 (Station) : the first station of the trail or null if the trail is of length 0.
     */
    public Station station1() {
        return length == 0
            ? null
            : station1;
    }
    
    /**
     * To get the last station of the trail.
     * @return the last station of the trail or null if the trail is of length 0.
     */
    public Station station2() {
        return length == 0
                ? null
                : station2;
    }

    /**
     * To get the list of routes of the trail.
     * @return list (List<Route>) : the list of routes of the trail or null if the trail is of length 0.
     */
    public List<Route> routes() {
        return length == 0
                ? null
                : routes;
    }
    
    @Override
    public String toString() {
        if (station1 == null && station2 == null && routes == null && length == 0)
            return "Empty trail";

        StringBuilder S = new StringBuilder(station1.name() + " - " + station2.name() + " (" + length + ")" + " / ");
            for (Route r : routes) {
                S.append(r.station1().name())
                        .append(" ")
                        .append(r.station2().name())
                        .append(" - ");
            }

      return S.toString();
    }

}
