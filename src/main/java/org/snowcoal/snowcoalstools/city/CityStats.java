package org.snowcoal.snowcoalstools.city;

public class CityStats {
    public int numCells;
    public int numHouses;
    public long area;

    public CityStats(int numCells, int numHouses, long area){
        this.area = area;
        this.numCells = numCells;
        this.numHouses = numHouses;
    }
}
