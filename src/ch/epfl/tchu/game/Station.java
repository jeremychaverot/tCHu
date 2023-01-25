package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

/** 
 * @author Cristian Safta (324694)
 */
public final class Station {
    
    private final int id;
    private final String name;
    
    /**
     * Constructor.
     * @param id (int) : station ID number.
     * @param name (String) : name of the station.
     * @throws IllegalArgumentException if id is strictly negative.
     */
    public Station(int id, String name) {
        Preconditions.checkArgument(id >= 0);

        this.id = id;
        this.name = name;
    }
    
    /**
     * To get the ID number of the station.
     * @return id (int) : the ID number.
     */
    public int id() { return id; }
    
    /**
     * To get the name of the station.
     * @return name (String) : the name of the station.
     */
    public String name() { return name; }
    
    @Override
    public String toString() { return name; }

}

