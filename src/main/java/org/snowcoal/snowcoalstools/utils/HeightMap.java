package org.snowcoal.snowcoalstools.utils;

public class HeightMap {
    private Double[][] heightMap;
    private double scale;
    private int width;
    private int length;

    /**
     * HeightMap
     *
     * instantiates an empty heightmap
     *
     * width - width of heightmap
     * length - length of heightmap
     * scale - scales the heightmap
     */
    public HeightMap(int width, int length, double scale){
        // add padding border around the outside
        this.length = length + 2;
        this.width = width + 2;
        this.scale = scale;

        heightMap = new Double[this.length][this.width];

        // fill with zeros
        for(int i = 0; i <= this.length; i++){
            for(int j = 0; j <= this.width; j++) {
                heightMap[i][j] = 0.0;
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
            heightMap[z+1][x+1] = val;
        }
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
        return(heightMap[z][x]);
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
        if(!checkPoint(x, z)) return null;

        double[] s = new double[9];
        int count = 0;

        // sample points around point
        for(int i = -1; i <= 1; i++){
            for(int j = -1; j <= 1; j++){
                s[count] = heightMap[z+1+i][x+1+j];
                count ++;
            }
        }

        // apply Sobel equations
        double normalX = scale * -(s[8]-s[6]+2.0*(s[5]-s[3])+s[2]-s[0]);
        double normalZ = scale * -(s[0]-s[6]+2.0*(s[1]-s[7])+s[2]-s[8]);
        double normalY = 1.0;

        // normalize the vector
        DoubleVec3 normal = new DoubleVec3(normalX, normalY, normalZ);
        normal.normalize();

        return normal;
    }

    /**
     * checkPoint
     *
     * checks if a input point to the height map is in bounds
     */
    private boolean checkPoint(int x, int z){
        return(!(x < 0 || z < 0 || x >= width-2 || z >= length-2));
    }
}
