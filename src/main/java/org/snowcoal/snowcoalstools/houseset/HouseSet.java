package org.snowcoal.snowcoalstools.houseset;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.*;
import java.lang.Integer;
import java.io.FileWriter;
import java.io.BufferedReader;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.util.Location;

public class HouseSet{
    // tree of input houses sorted by type, and then by width
    // inputting an type gets a list that contains houses with that same type, and
    // each list is sorted smallest to largest based on width
    private HashMap<Integer, LinkedList<House>> houseTree;
    // list of every house
    public ArrayList<House> houseSetList;
    // house ID counter
    private int ID = 0;

    // rand generator
    private Random random;

    public HouseSet() {
        random = new Random();
        houseTree = new HashMap<Integer, LinkedList<House>>();
        houseSetList = new ArrayList<House>();

        // setup map of empty lists
        for(int i = -2; i <= 4; i++){
            LinkedList<House> houseList = new LinkedList<House>();
            houseTree.put(i, houseList);
        }
    }

    /**
     * addHouse
     *
     * adds a house to the houseset.
     *
     * sel - cuboid selection of house
     * cpy_pt - players location when doing ./hs addhouse
     *
     * return - -1 if bad type, -2 if bad selection, 0 normally
     *
     */
    public boolean addHouse(CuboidRegion sel, Location cpy_pt, int houseType){
        int width = sel.getWidth();
        // only care about odd width if house is a straight one
        if(width % 2 == 0 && houseType == -2){
            return false;
        }
        House house = new House();
        BlockVector3 pos1 = sel.getMinimumPoint();
        BlockVector3 pos2 = sel.getMaximumPoint();

        // set copy point
        house.cpy_pt_x = cpy_pt.getBlockX();
        house.cpy_pt_y = cpy_pt.getBlockY();
        house.cpy_pt_z = cpy_pt.getBlockZ();
        // set pos1
        house.pos1_x = pos1.getX();
        house.pos1_y = pos1.getY();
        house.pos1_z = pos1.getZ();
        // set pos2
        house.pos2_x = pos2.getX();
        house.pos2_y = pos2.getY();
        house.pos2_z = pos2.getZ();

        house.ID = this.ID;
        house.type = houseType;
        if(houseType == -2){
            house.width = width;
        }
        else{
            house.width = -1;
        }

        placeHouseIntoTree(house, houseType);

        return true;
    }

    /**
     * placeHouseIntoTree
     *
     * places House object into tree and map
     *
     * house - house to be placed
     * houseType - type of house to be placed
     *
     */
    private void placeHouseIntoTree(House house, int houseType){
        // place into ID map
        //housesByID.add(this.ID, house);
        houseSetList.add(house);

        // get list that house belongs in
        LinkedList<House> houseList = houseTree.get(houseType);
        int it = 0;

        // if empty put it at end
        if(houseList.isEmpty()){
            houseList.add(house);
        }
        // otherwise add it into sorted position in list
        else{
            while(true){
                // if it gets to the end of the list, put it at the end
                if(it == houseList.size()){
                    houseList.add(house);
                    break;
                }
                House curHouse = houseList.get(it);
                // if the new houses width is less or equal to the currents, insert it here
                if(house.width <= curHouse.width) {
                    houseList.add(it, house);
                    break;
                }
                it++;
            }
        }

        // increment ID so next house is different
        ID++;
    }


    /**
     * pickRandHouseByWidth
     *
     * picks a random house with the given type that has a width that is in [minWidth, maxWidth]
     * if minWidth = maxWidth, it picks a random house with exactly that width
     * type - ID of type of house to be picked:
     * -1 = unassigned, -2 = house between corners, 0 = no neighbors, 1 = "i", 2 = "L", 3 = "T", 4 = "+"
     * minWidth - min width of house to be picked
     * maxWidth - max width of house to be picked
     *
     * return - pointer to the house that was picked, returns null if inputs bad or if no house found
     *
     */
    public House pickRandHouseByWidth(int typeID, int minWidth, int maxWidth){
        House retVal = null;
        // check input for range
        if(typeID < -2 || typeID > 4 || maxWidth < minWidth){
            System.out.println("ERROR: input into pickRandHouseByWidth is invalid");
            return retVal;
        }
        // get the list of houses with the type
        LinkedList<House> houseList = houseTree.get(typeID);
        if(houseList == null) {
            System.out.println("ERROR: Attempted to get house of type: " + typeID + ", but none exist.");
            return retVal;
        }

        List<House> houseVec = new LinkedList<House>();
        // get all houses from list that are within the correct range
        for(House house: houseList){
            // break loop if hit house that has a width larger than the max allowed
            if(house.width > maxWidth){
                break;
            }
            // otherwise add it to the vector if its width is larger or equal to min
            else if(house.width >= minWidth){
                houseVec.add(house);
            }
        }
        int size = houseVec.size();
        if(size == 0){
            System.out.println("ERROR: attempted to pick house of width "+ minWidth +" which is non-existent");
            return retVal;
        }

        // generate a random index from 0 - max size of vector
        int index = random.nextInt(size);
        // get ID of chosen house
        retVal = houseVec.get(index);

        return retVal;
    }


    /**
     * isEmpty
     *
     * used for checking if house set is empty
     *
     */
    public boolean isEmpty(){
        if(ID == 0) return true;
        return false;
    }


    /**
     * printTree
     *
     * used for testing tree
     */
    public void printTree(boolean printAllData){
        for(int t = -2; t <= 4; t++){
            List<House> houseList = houseTree.get(t);
            System.out.println("Size of list for type " +t+ ": " +houseList.size());
            // print width of each house
            for(House house: houseList){
                if(!printAllData) {
                    System.out.println(house.ID + " " + house.type + " " + house.width);
                }
                else{
                    String s = house.ID + " "
                            + house.type + " "
                            + house.width + " "
                            + house.cpy_pt_x + " "
                            + house.cpy_pt_y + " "
                            + house.cpy_pt_z + " "
                            + house.pos1_x + " "
                            + house.pos1_y + " "
                            + house.pos1_z + " "
                            + house.pos2_x + " "
                            + house.pos2_y + " "
                            + house.pos2_z;

                    System.out.println(s);

                    int volume = Math.abs(house.pos1_x - house.pos2_x)*Math.abs(house.pos1_y - house.pos2_y)*Math.abs(house.pos1_z - house.pos2_z);
                    System.out.println("volume :" + volume);
                }
            }
        }
    }


    /**
     * writeToCSV
     *
     * writes HouseSet object to csv file
     *
     * returns true on success and false on failure
     *
     */
    public boolean writeToCSV(String filepath){
        try{
            FileWriter fw = new FileWriter(filepath);
            for(int t = -2; t <= 4; t++){
                List<House> houseList = houseTree.get(t);
                for(House house: houseList){
                    String line = house.ID + ","
                            + house.type + ","
                            + house.width + ","
                            + house.cpy_pt_x + ","
                            + house.cpy_pt_y + ","
                            + house.cpy_pt_z + ","
                            + house.pos1_x + ","
                            + house.pos1_y + ","
                            + house.pos1_z + ","
                            + house.pos2_x + ","
                            + house.pos2_y + ","
                            + house.pos2_z + "\n";

                    fw.write(line);
                }
            }
            fw.close();
        }
        catch(IOException e){
            return false;
        }
        return true;
    }


    /**
     * readFromCSV
     *
     * loads HouseSet object from csv file
     *
     * returns true on success and false on failure
     *
     */
    public boolean readFromCSV(String filepath){
        clear();

        try{
            BufferedReader br = Files.newBufferedReader(FileSystems.getDefault().getPath(filepath));

            String delim = ",";
            String line;
            while((line = br.readLine()) != null){
                String[] row = line.split(delim);

                House house = new House();

                try{
                    house.ID = Integer.parseInt(row[0]);
                    house.type = Integer.parseInt(row[1]);
                    house.width = Integer.parseInt(row[2]);
                    // set copy point
                    house.cpy_pt_x = Integer.parseInt(row[3]);
                    house.cpy_pt_y = Integer.parseInt(row[4]);
                    house.cpy_pt_z = Integer.parseInt(row[5]);
                    // set pos1
                    house.pos1_x = Integer.parseInt(row[6]);
                    house.pos1_y = Integer.parseInt(row[7]);
                    house.pos1_z = Integer.parseInt(row[8]);
                    // set pos2
                    house.pos2_x = Integer.parseInt(row[9]);
                    house.pos2_y = Integer.parseInt(row[10]);
                    house.pos2_z = Integer.parseInt(row[11]);
                }
                catch(NumberFormatException | ArrayIndexOutOfBoundsException e){
                    return false;
                }

                // add house to tree
                placeHouseIntoTree(house, house.type);
            }
            br.close();
        }
        catch(IOException e){
            return false;
        }

        return true;
    }

    /**
     * clear
     *
     * clears the current houseSet selection
     *
     */
    public void clear(){
        // clear all lists in Tree (but dont clear outer map)
        for(int t = -2; t <= 4; t++){
            List<House> houseList = houseTree.get(t);
            houseList.clear();
        }
        // clear houses by ID map
        houseSetList.clear();
        // reset ID
        ID = 0;
    }


}
