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
import org.snowcoal.snowcoalstools.utils.GuassConvKernel;
import org.snowcoal.snowcoalstools.utils.NearestBlockStateFinder;

import org.snowcoal.snowcoalstools.SnowcoalsTools;

public class Dim3Smooth {
    private Region sel;
    private Player player;
    private SnowcoalsTools instance;
    private int k;
    private double cutoff = 0.5;
    private int changedBlocks = 0;
    private int numIterations = 1;

    public Dim3Smooth(Region sel, Player player, SnowcoalsTools instance, int k, int numIterations, double cutoff) {
        this.sel = sel;
        this.player = player;
        this.instance = instance;

        this.k = k;
        this.cutoff = cutoff;
        this.numIterations = numIterations;

        smooth();
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

        // find bounds for changed area
        MutableBlockVector3 outputMin = new MutableBlockVector3(min.getX() + pad, min.getY() + pad, min.getZ() + pad);
        MutableBlockVector3 outputMax = new MutableBlockVector3(max.getX() - pad, max.getY() - pad, max.getZ() - pad);

        // copy editsession
        Extent srcExtent = editSession;
        Extent dstExtent = createCopyClipboard(editSession, min, max);

        // generate kernel
        GuassConvKernel guassConvKernel = new GuassConvKernel(srcExtent, k);

        // smooth some number of times
        for(int i = 0; i < numIterations; i++) {

            // loop through all blocks in output (padding around outside)
            for (int x = outputMin.getX(); x <= outputMax.getX(); x++) {
                for (int y = outputMin.getY(); y <= outputMax.getY(); y++) {
                    for (int z = outputMin.getZ(); z <= outputMax.getZ(); z++) {

                        // convolve at current block
                        double sum = guassConvKernel.guassConvolve(x, y, z);

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
