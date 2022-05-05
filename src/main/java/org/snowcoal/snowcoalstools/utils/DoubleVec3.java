package org.snowcoal.snowcoalstools.utils;

public class DoubleVec3 {
    private double x, y, z;

    /**
     * DoubleVec3
     *
     * instantiates a R3 vector of doubles
     */
    public DoubleVec3(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * getX, getY, getZ
     *
     * get the X, Y, and Z values
     */
    public double getX(){ return x;}
    public double getY(){ return y;}
    public double getZ(){ return z;}

    /**
     * normalize
     *
     * normalizes the vector
     */
    public void normalize(){
        double magnitude = Math.sqrt(Math.pow(x, 2.0) + Math.pow(y, 2.0) + Math.pow(z, 2.0));

        x /= magnitude;
        y /= magnitude;
        z /= magnitude;
    }
}
