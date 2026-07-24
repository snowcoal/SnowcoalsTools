package org.snowcoal.snowcoalstools.city;

import com.fastasyncworldedit.core.math.MutableBlockVector3;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.world.World;
import org.snowcoal.snowcoalstools.houseset.House;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.function.mask.InverseSingleBlockTypeMask;
import com.sk89q.worldedit.world.block.BlockTypes;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.function.operation.Operation;
import org.snowcoal.snowcoalstools.houseset.HouseSet;

import java.util.Collections;
import java.util.HashMap;

public class CityPaster implements Runnable{
    private City city;
    private Thread thread;
    private Player player;

    public CityPaster(City city){
        this.city = city;
        this.player = city.player;
    }

    @Override
    public void run() {
        pasteCityThread();
        thread = null;
    }

    /**
     * pasteCity
     *
     * public method to paste city
     *
     */
    public void pasteCity(){
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    /**
     * pasteCityThread
     *
     * thread that pastes city into world
     *
     */
    private boolean pasteCityThread() {
        int y_paste = player.getLocation().getBlockY();

        HouseSet houseSet = city.getHouseSet();

        // get world
        World world = player.getWorld();

        // make new editsession for copy
        EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder()
                .world(world)
                .actor(player)
                .allowedRegionsEverywhere()
                .limitUnlimited()
                .compile().build();

        // sort the house list
        Collections.sort(city.houseList);

        // map from house ID to clipboard containing house copy
        HashMap<Integer, BlockArrayClipboard> clipboards = new HashMap<Integer, BlockArrayClipboard>();


        // loop though each house
        for(House house: houseSet.houseSetList){
            // make copy point vectors
            MutableBlockVector3 cpy_pt = new MutableBlockVector3(house.cpy_pt_x, 100, house.cpy_pt_z);
            MutableBlockVector3 pos1 = new MutableBlockVector3(house.pos1_x, house.pos1_y, house.pos1_z);
            MutableBlockVector3 pos2 = new MutableBlockVector3(house.pos2_x, house.pos2_y, house.pos2_z);

            // create new region, and clipboard
            CuboidRegion region = new CuboidRegion(world, pos1, pos2);
            BlockArrayClipboard clipboard = new BlockArrayClipboard(region);
            clipboard.setOrigin(cpy_pt);

            // do copy
            ForwardExtentCopy copy = new ForwardExtentCopy(editSession, region, cpy_pt, clipboard , cpy_pt);
            copy.setCopyingEntities(false);
            Operations.complete(copy);

            // add to map
            clipboards.put(house.ID, clipboard);
        }


        // create new region, and clipboard for paste
        MutableBlockVector3 corner1 = new MutableBlockVector3(city.start_x, -64, city.start_z);
        MutableBlockVector3 corner2 = new MutableBlockVector3(city.start_x+city.cityWidth, 320, city.start_z+city.cityLength);
        CuboidRegion pasteRegion = new CuboidRegion(world, corner1, corner2);
        BlockArrayClipboard pasteClipboard = new BlockArrayClipboard(pasteRegion);
        ClipboardHolder pasteClipboardHolder = new ClipboardHolder(pasteClipboard);
        pasteClipboard.setOrigin(corner1);

        // loop through ordered list
        for (CityHouse cityHouse : city.houseList) {

            House srcHouse = cityHouse.house_ptr;
            int houseID = srcHouse.ID;

            // get clipboard copy
            BlockArrayClipboard clipboard = clipboards.get(houseID);

            // create paste location
            // y_paste = world.getHighestTerrainBlock(cityHouse.pos_x, cityHouse.pos_z, 0, 100);
            MutableBlockVector3 paste_pt = new MutableBlockVector3(cityHouse.pos_x, y_paste, cityHouse.pos_z);

            // set mask
            Mask mask = new InverseSingleBlockTypeMask(clipboard, BlockTypes.BEDROCK);

            // set up clipboard holder and rotate transform
            ClipboardHolder holder = new ClipboardHolder(clipboard);
            holder.setTransform(new AffineTransform().rotateY(cityHouse.rotation * -90));

            // paste house into final clipboard
            Operation operation = holder
                    .createPaste(pasteClipboard)
                    .to(paste_pt)
                    .copyEntities(false)
                    .copyBiomes(false)
                    .ignoreAirBlocks(true)
                    .maskSource(mask)
                    .build();

            // perform operation
            Operations.complete(operation);
        }

        // paste final clipboard into world
        Operation operation = pasteClipboardHolder
                .createPaste(editSession)
                .to(corner1)
                .copyEntities(false)
                .copyBiomes(false)
                .ignoreAirBlocks(true)
                .build();

        // perform operation
        Operations.complete(operation);

        // close editsession
        LocalSession localSession = WorldEdit.getInstance().getSessionManager().get(player);
        localSession.remember(editSession);
        editSession.close();
        return true;
    }

}
