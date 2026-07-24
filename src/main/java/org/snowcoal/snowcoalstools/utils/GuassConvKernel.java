package org.snowcoal.snowcoalstools.utils;

import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;

public class GuassConvKernel {
    private Extent srcExtent;
    private int k;
    private int pad;
    private final double c = 1/Math.pow(2.0*Math.PI, 3.0/2.0);
    private double[][][] kernel;


    public GuassConvKernel(Extent srcExtent, int k) {
        this.srcExtent = srcExtent;
        // k must be ODD and >= 3!!!
        this.k = k;
        this.pad = k/2;
        genKernel();
    }

    /**
     * gauss
     *
     * 3D Guassian distribution function
     *
     */
    private double gauss(double x, double y, double z){
        return c * Math.exp((-(Math.pow(x, 2.0) + Math.pow(y, 2.0) + Math.pow(z, 2.0)))/2.0);
    }

    /**
     * genKernel
     *
     * generates a base guassian kernel of size k (this could be precomputed but whatever)
     * output kernel is normalized, so distribution isnt 100% exact
     */
    private void genKernel() {
        kernel = new double[k][k][k];

        double sum = 0.0;

        // first pass, find sum
        for (int x = 0; x < k; x++){
            for (int y = 0; y < k; y++){
                for (int z = 0; z < k; z++){
                    double g = gauss((double)(pad - x), (double)(pad - y), (double)(pad - z));
                    kernel[x][y][z] = g;
                    sum += g;
                }
            }
        }

        double c = (1 - sum)/(double)(k*k*k);

        // second pass, add constant to each to normalize
        for (int x = 0; x < k; x++){
            for (int y = 0; y < k; y++){
                for (int z = 0; z < k; z++){
                    kernel[x][y][z] += c;
                }
            }
        }
    }

    /**
     * guassConvolve
     *
     * performs a single convolution at the given block with the kernel
     *
     */
    public double guassConvolve(int x, int y, int z) {
        double sum = 0;

        for (int kx = -pad; kx <= pad; kx++) {
            for (int ky = -pad; ky <= pad; ky++) {
                for (int kz = -pad; kz <= pad; kz++) {
                    double blockVal = srcExtent.getBlock(x + kx, y + ky, z + kz).isAir() ? 0.0 : 1.0;
                    sum += blockVal * kernel[kx + pad][ky + pad][kz + pad];
                }
            }
        }

        return sum;
    }

    /**
     * checkKernelBounds
     *
     * checks if an input is within the region AND within the kernel output
     *
     */
    public boolean checkKernelBounds(Region sel, int x, int y, int z){
        BlockVector3 min = sel.getMinimumPoint();
        BlockVector3 max = sel.getMaximumPoint();

        return !(x < min.getX() + pad || x > max.getX() - pad || y < min.getY() + pad || y > max.getY() - pad || z < min.getZ() + pad || z > max.getZ() - pad);
    }
}
