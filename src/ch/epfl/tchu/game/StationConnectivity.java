package ch.epfl.tchu.game;

/**
 * @author Jérémy Chaverot (315858)
 */
public interface StationConnectivity {
    
    /**
     * To know if two station are connected.
     * @param s1 (Station) : the first station we're interested in.
     * @param s2 (Station) : the second station we're interested in.
     * @return connected (boolean) : true if the stations are connected, false otherwise.
     */
    boolean connected(Station s1, Station s2);

}
