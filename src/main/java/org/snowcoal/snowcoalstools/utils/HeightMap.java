package org.snowcoal.snowcoalstools.utils;

import com.sk89q.worldedit.math.convolution.HeightMapFilter;

public class HeightMap {
    private double[][] heightMap;
    private double scale;
    private int width;
    private int length;
    private int smallWidth;
    private int smallLength;
    private int minHeight;
    private int maxHeight;

    private int edgePadding = 2;

    /**
     * HeightMap
     *
     * instantiates an empty heightmap
     *
     * width - width of heightmap
     * length - length of heightmap
     * minHeight - the minimum value the heightmap can have
     * maxHeight - the maximum value the heightmap can have
     * scale - scales the heightmap
     */
    public HeightMap(int width, int length, int minHeight, int maxHeight, double scale){
        // add padding border around the outside
        this.length = length + 2*edgePadding;
        this.width = width + 2*edgePadding;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.scale = scale;
        this.smallLength = length;
        this.smallWidth = width;

        heightMap = new double[this.length][this.width];

        // fill with initial height
        for(int i = 0; i < this.length; i++){
            for(int j = 0; j < this.width; j++) {
                heightMap[i][j] = minHeight;
            }
        }
    }

    /**
     * setHeight
     *
     * sets a single point of the height map
     *
     * x - x value of pt
     * x - z value of pt
     * val - value to set it to
     */
    public void setHeight(int x, int z, double val){
        if(checkPoint(x, z)) {
            heightMap[z+edgePadding][x+edgePadding] = val;
        }
    }

    /**
     * changeHeight
     *
     * adds to the current height of the heightmap
     *
     * x - x value of pt
     * x - z value of pt
     * val - value to set it to
     *
     * returns false if more height could not be added
     */
    public boolean addHeight(int x, int z, double val){
        double newHeight = heightMap[z+edgePadding][x+edgePadding] + val;
        // keep height constrained
        if(newHeight < minHeight - 2 || newHeight > maxHeight + 2){
            return false;
        }
        heightMap[z+edgePadding][x+edgePadding] = newHeight;

        return true;
    }

    /**
     * getHeight
     *
     * gets a height at some point
     *
     * x - x value of pt
     * x - z value of pt
     */
    public double getHeight(int x, int z){
        return(heightMap[z+edgePadding][x+edgePadding]);
    }

    /**
     * getWidth
     *
     * gets the width not including padding
     */
    public int getWidth(){
        return this.smallWidth;
    }

    /**
     * getLength
     *
     * gets the length not including padding
     */
    public int getLength(){
        return this.smallLength;
    }

    /**
     * getSurfaceNormal
     *
     * samples the surface normal vector from a x z point
     *
     * x - x value of pt
     * x - z value of pt
     */
    public DoubleVec3 getSurfaceNormal(int x, int z){
        double[] s = new double[9];
        int count = 0;

        // sample points around point
        for(int i = -1; i <= 1; i++){
            for(int j = -1; j <= 1; j++){
                //s[count] = heightMap[z+1+i][x+1+j];
                s[count] = getHeight(x+j, z+i);
                count ++;
            }
        }

        // apply Sobel equations
        double normalX = scale * -(s[8]-s[6]+2.0*(s[5]-s[3])+s[2]-s[0]);
        double normalZ = scale * (s[0]-s[6]+2.0*(s[1]-s[7])+s[2]-s[8]);
        double normalY = 1.0;

        // normalize the vector
        DoubleVec3 normal = new DoubleVec3(normalX, normalY, normalZ);
        normal.normalize();
        return normal;
    }

    /**
     * getAveragedNormal
     *
     * samples the surface normals from a 5 point area and gets a weighted "average"
     *
     * x - x value of center pt
     * x - z value of center pt
     *
     * weight - weighs the center point more
     */
    public DoubleVec3 getAveragedNormal(int x, int z, double centerWeight){
        DoubleVec3 center = getSurfaceNormal(x, z);
        center.scalarMultiply(centerWeight);

        DoubleVec3 left = getSurfaceNormal(x - 1, z);
        DoubleVec3 right = getSurfaceNormal(x + 1, z);
        DoubleVec3 top = getSurfaceNormal(x, z - 1);
        DoubleVec3 bottom = getSurfaceNormal(x, z + 1);

        // add all together
        DoubleVec3 output = left.add(right).add(top).add(bottom).add(center);

        // normalize again
        output.normalize();

        return output;
    }

    /**
     * applyFilter
     *
     * applies a FAWE gauassian filter to the heightmap
     *
     * filter - the filter
     * iterations - number of times to filter
     */
    public void applyFilter(HeightMapFilter filter, int iterations){
        float[] newData = new float[width*length];

        int count = 0;
        // put all data into flat array
        for(int i = 0; i < this.length; i++){
            for(int j = 0; j < this.width; j++) {
                newData[count] = (float) heightMap[i][j];
                count ++;
            }
        }

        // apply filter
        for (int n = 0; n < iterations; n++) {
            newData = filter.filter(newData, width, length, 0.5F);
        }

        count = 0;
        // unpack flat array
        for(int i = 0; i < this.length; i++){
            for(int j = 0; j < this.width; j++) {
                heightMap[i][j] = (double) newData[count];
                count ++;
            }
        }
    }

    /**
     * checkPoint
     *
     * checks if a input point to the height map is in bounds
     */
    private boolean checkPoint(int x, int z){
        return(!(x < 0 || z < 0 || x >= smallWidth || z >= smallLength));
    }
}
