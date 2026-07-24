package org.snowcoal.snowcoalstools.erode;

import com.sk89q.worldedit.math.convolution.GaussianKernel;
import com.sk89q.worldedit.math.convolution.HeightMapFilter;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.entity.Player;
import org.snowcoal.snowcoalstools.SnowcoalsTools;
import org.snowcoal.snowcoalstools.utils.DoubleVec3;

import java.util.Random;

public class WaterErosion extends HeightMapErosion{
    private Random random;

    // params
    private double radius = 0.1;
    private int maxIterations = 10000;
    private double stopTolerance = 0.00001;
    private double depositionRate = 0.3;
    private double erosionRate = 0.6;
    private double iterationScale = 1.0;
    private double surfaceFriction = 0.5;
    private double erosionSpeed = 0.5;

    public WaterErosion(Region sel, Player player, SnowcoalsTools instance, int time, int intensity) {
        super(sel, player, instance, time, intensity);
        super.populateHeightMap();
        doErosion();
        super.placeHeightMap();
        super.endOperation();
    }

    @Override
    void doErosion() {
        random = new Random();
        int balls = 10000;

        for(int i = 0; i < balls; i++){
            double px = random.nextDouble() * (super.width - 1);
            double pz = random.nextDouble() * (super.length - 1);

            erodePoint(px, pz);
        }

        // smooth terrain
        HeightMapFilter filter = new HeightMapFilter(new GaussianKernel(5, 1.0));
        applyHeightMapFilter(filter, 1);
    }

    private void erodePoint(double x, double z){
        double ox = (random.nextDouble()*2.0 - 1.0) * radius;
        double oz = (random.nextDouble()*2.0 - 1.0) * radius;

        double sediment = 0.0;
        double xPrev = x;
        double zPrev = z;
        double vx = 0.0;
        double vz = 0.0;

        for(int i = 0; i < maxIterations; i++){
            // get normal
            DoubleVec3 surfaceNormal = super.getHeightMapNormal(x + ox, z + oz);

            // stop if terrain is flat of if boundary reached
            if(Math.abs(surfaceNormal.getY() - 1.0) < stopTolerance || x >= super.width || x < 0 || z >= super.length || z < 0) break;
            // stop if the position is basically the same
            if(i != 0 && (Math.abs(x - xPrev) < stopTolerance || Math.abs(z - zPrev) < stopTolerance)) break;

            // calculate deposition and erosion amounts
            double deposit = sediment * depositionRate * surfaceNormal.getY();
            double erosion = erosionRate * (1.0 - surfaceNormal.getY()) * Math.min(1.0, i * iterationScale);

            // attempt to change height map, if it cant break
            if(!super.changeHeightMap(xPrev, zPrev, deposit - erosion)) break;

            // update values
            sediment += erosion - deposit;
            vx = surfaceFriction * vx + surfaceNormal.getX() * erosionSpeed;
            vz = surfaceFriction * vz + surfaceNormal.getZ() * erosionSpeed;
            xPrev = x;
            zPrev = z;
            x += vx;
            z += vz;
        }
    }
}
