package org.snowcoal.snowcoalstools.smooth;

import com.fastasyncworldedit.core.math.MutableBlockVector3;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockStateHolder;
import com.sk89q.worldedit.world.block.BlockTypes;
import org.snowcoal.snowcoalstools.utils.NearestBlockStateFinder;

import org.snowcoal.snowcoalstools.SnowcoalsTools;

import java.lang.Math;

public class Dim3Smooth {
    private Region sel;
    private Player player;
    private SnowcoalsTools instance;
    private int k;
    private final double c = 1/Math.pow(2.0*Math.PI, 3.0/2.0);
    private double[][][] kernel;
    private double cutoff = 0.5;
    private int changedBlocks = 0;
    private int numIterations = 1;

    public Dim3Smooth(Region sel, Player player, SnowcoalsTools instance, int k, int numIterations, double cutoff) {
        this.sel = sel;
        this.player = player;
        this.instance = instance;
        // k must be ODD and >= 3!!!
        this.k = k;
        this.cutoff = cutoff;
        this.numIterations = numIterations;

        smooth();
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
    private void genKernel(){
        kernel = new double[k][k][k];

        int center = k/2;
        double sum = 0.0;

        // first pass, find sum
        for (int x = 0; x < k; x++){
            for (int y = 0; y < k; y++){
                for (int z = 0; z < k; z++){
                    double g = gauss((double)(center - x), (double)(center - y), (double)(center - z));
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
     * smooth
     *
     * base method that does the smoothing
     *
     */
    public void smooth() {
        BlockVector3 min = this.sel.getMinimumPoint();
        BlockVector3 max = this.sel.getMaximumPoint();

        // set up new editsession for edit
        EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder()
                .world(this.player.getWorld())
                .actor((Actor)this.player)
                .allowedRegionsEverywhere()
                .limitUnlimited()
                .compile()
                .build();

        // padding for outside kernel (if 3x3x3, = 1)
        int pad = k / 2;
        // generate kernel
        genKernel();

        // find bounds for changed area
        MutableBlockVector3 outputMin = new MutableBlockVector3(min.getX() + pad, min.getY() + pad, min.getZ() + pad);
        MutableBlockVector3 outputMax = new MutableBlockVector3(max.getX() - pad, max.getY() - pad, max.getZ() - pad);

        // copy editsession
        Extent srcExtent = editSession;
        Extent dstExtent = createCopyClipboard(editSession, min, max);

        // smooth some number of times
        for(int i = 0; i < numIterations; i++) {


            // loop through all blocks in output (padding around outside)
            for (int x = outputMin.getX(); x <= outputMax.getX(); x++) {
                for (int y = outputMin.getY(); y <= outputMax.getY(); y++) {
                    for (int z = outputMin.getZ(); z <= outputMax.getZ(); z++) {
                        double sum = 0.0;

                        // loop through kernel
                        for (int kx = -pad; kx <= pad; kx++) {
                            for (int ky = -pad; ky <= pad; ky++) {
                                for (int kz = -pad; kz <= pad; kz++) {
                                    // double blockVal = editSession.getBlock(x + kx, y + ky, z + kz).isAir() ? 0.0 : 1.0;
                                    double blockVal = srcExtent.getBlock(x + kx, y + ky, z + kz).isAir() ? 0.0 : 1.0;
                                    sum += blockVal * kernel[kx + pad][ky + pad][kz + pad];
                                }
                            }
                        }
                        // decide whether to set it to air or not
                        if (sum < cutoff) {
                            dstExtent.setBlock(x, y, z, (BlockStateHolder) new BlockState(BlockTypes.AIR, 0, 0));
                        } else {
                            // find nearest BlockState (to make output look similar to input)
                            NearestBlockStateFinder finder = new NearestBlockStateFinder(this.sel, editSession, 100);
                            BlockState closest = finder.findNearestBlockState(x, y, z);

                            // if null default to air
                            if (closest == null) {
                                dstExtent.setBlock(x, y, z, (BlockStateHolder) new BlockState(BlockTypes.AIR, 0, 0));
                            } else {
                                dstExtent.setBlock(x, y, z, (BlockStateHolder) closest);

                                // only update counter on last iteration
                                if(i == numIterations-1) {
                                    changedBlocks++;
                                }
                            }
                        }
                    }
                }
            }

            // copy src
            srcExtent = dstExtent;
            // dont do this on last iteration
            if(i < numIterations - 1) {
                dstExtent = createCopyClipboard(dstExtent, min, max);
            }
        }

        // create clipboard holder
        ClipboardHolder pasteClipboardHolder = new ClipboardHolder((BlockArrayClipboard)dstExtent);

        // paste final clipboard into world
        Operation operation = pasteClipboardHolder
                .createPaste(editSession)
                .to(min)
                .copyEntities(false)
                .copyBiomes(false)
                .ignoreAirBlocks(false)
                .build();

        // perform operation
        Operations.complete(operation);

        LocalSession localSession = WorldEdit.getInstance().getSessionManager().get(player);
        localSession.remember(editSession);
        editSession.close();
    }

    /**
     * createCopyClipboard
     *
     * copies an Extent (editsession, clipboard, etc) into a new clipboard
     *
     */
    private BlockArrayClipboard createCopyClipboard(Extent source, BlockVector3 min, BlockVector3 max){
        CuboidRegion CopyRegion = new CuboidRegion(player.getWorld(), min, max);
        BlockArrayClipboard copyClipboard = new BlockArrayClipboard(CopyRegion);
        copyClipboard.setOrigin(min);

        // copy editsession into paste clipboard
        for (int x = min.getX(); x <= max.getX(); x++) {
            for (int y = min.getY(); y <= max.getY(); y++) {
                for (int z = min.getZ(); z <= max.getZ(); z++) {
                    BlockState blockState = source.getBlock(x,y,z);
                    copyClipboard.setBlock(x, y, z, (BlockStateHolder) blockState);
                }
            }
        }

        return copyClipboard;
    }

    /**
     * getChangedBlocks
     *
     * gets the amount of blocks that were changed by the edit
     *
     */
    public int getChangedBlocks(){
        return this.changedBlocks;
    }
}
