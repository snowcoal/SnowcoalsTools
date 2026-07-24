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

    /**
     * scalarMultiply
     *
     * multiplies the vector by a scalar
     */
    public void scalarMultiply(double scalar){
        x *= scalar;
        y *= scalar;
        z *= scalar;
    }

    /**
     * add
     *
     * adds a vector to the current and returns a new vector
     */
    public DoubleVec3 add(DoubleVec3 a){
        return new DoubleVec3(x + a.x, y + a.y, z + a.z);
    }
}
