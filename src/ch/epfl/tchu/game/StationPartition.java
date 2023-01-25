package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.Arrays;

/**
 * @author Cristian Safta (324694)
 */
public final class StationPartition implements StationConnectivity {

    private final int[] stationDestination;

    private StationPartition(int[] integers) {
        this.stationDestination = integers.clone();
    }

    /**
     * To know if two stations are connected.
     * @param s1 (Station) : the first station we're interested in.
     * @param s2 (Station) : the second station we're interested in.
     * @return boolean (Boolean) : true if the stations are connected, false otherwise.
     */
    @Override
    public boolean connected(Station s1, Station s2) {
        if (s1.id() < stationDestination.length && s2.id() < stationDestination.length)
            return stationDestination[s1.id()] == stationDestination[s2.id()];
        else return s1.id() == s2.id();
    }


    public static final class Builder {

        private final int stationCount;
        private final int[] stationPointers;

        /**
         * Constructor.
         * @param stationCount (int) : the number of stations.
         * @throws IllegalArgumentException if stationCount is strictly negative.
         */
        public Builder(int stationCount) {
            Preconditions.checkArgument(stationCount >= 0);

            this.stationCount = stationCount;
            stationPointers = new int[stationCount];
                for (int i = 0 ; i < stationCount ; i++) {
                    stationPointers[i] = i;
                }
        }

        /**
         * Connects the two stations.
         * @param s1 (Station) : the first station
         * @param s2 (Station) : the second station
         * @return this (StationPartition.Builder) : the instance previously initialized.
         */
        public Builder connect(Station s1, Station s2) {
            stationPointers[findDestination(s1.id())] = findDestination(s2.id());

            return this;
        }

        /**
         * Builds the flattened StationPartition.
         * @return stationPartition (StationPartition) : the built station partition.
         */
        public StationPartition build() {
            for (int i = 0 ; i < stationCount ; i++) {
                stationPointers[i] = findDestination(i);
            }

            return new StationPartition(stationPointers);
        }

        //Helper method used to find the representative of the subset that includes station
        private int findDestination(int station) {
            while (stationPointers[station] != station)
                station = stationPointers[station];

            return station;
        }

    }

}
