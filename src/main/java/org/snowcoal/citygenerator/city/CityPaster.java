package org.snowcoal.citygenerator.city;

import com.fastasyncworldedit.core.math.MutableBlockVector3;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.world.World;
import org.snowcoal.citygenerator.houseset.House;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.function.mask.InverseSingleBlockTypeMask;
import com.sk89q.worldedit.world.block.BlockTypes;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.function.operation.Operation;
import org.snowcoal.citygenerator.houseset.HouseSet;

import java.util.Collections;
import java.util.HashMap;

public class CityPaster {
    private City city;

    public CityPaster(City city){
        this.city = city;
    }

    public boolean pasteCity(Player player) {
        int y_paste = player.getLocation().getBlockY();

        HouseSet houseSet = city.getHouseSet();

        // get world
        World world = player.getWorld();

        // make new editsession for copy
        EditSession copyEditSession = WorldEdit.getInstance().newEditSessionBuilder()
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
            ForwardExtentCopy copy = new ForwardExtentCopy(copyEditSession, region, cpy_pt, clipboard , cpy_pt);
            copy.setCopyingEntities(false);
            Operations.complete(copy);

            // add to map
            clipboards.put(house.ID, clipboard);
        }

        int count = 0;

        // make new editsession for paste
        EditSession pasteEditSession = WorldEdit.getInstance().newEditSessionBuilder()
                .world(world)
                .actor(player)
                .allowedRegionsEverywhere()
                .limitUnlimited()
                .compile().build();

        // loop through ordered list
        for (CityHouse cityHouse : city.houseList) {
            // refresh editsession every 200 pastes
            if(count == 200){
                count = 0;
                // close old editsession
                pasteEditSession.close();
                // make new editsession for paste
                pasteEditSession = WorldEdit.getInstance().newEditSessionBuilder()
                        .world(world)
                        .actor(player)
                        .allowedRegionsEverywhere()
                        .limitUnlimited()
                        .compile().build();

            }

            House srcHouse = cityHouse.house_ptr;
            int houseID = srcHouse.ID;

            // get clipboard copy
            BlockArrayClipboard clipboard = clipboards.get(houseID);

            // create paste location
            MutableBlockVector3 paste_pt = new MutableBlockVector3(cityHouse.pos_x, y_paste, cityHouse.pos_z);

            // set mask
            Mask mask = new InverseSingleBlockTypeMask(clipboard, BlockTypes.BEDROCK);

            // set up clipboard holder and rotate transform
            ClipboardHolder holder = new ClipboardHolder(clipboard);
            holder.setTransform(new AffineTransform().rotateY(cityHouse.rotation * -90));

            // build operation
            Operation operation = holder
                    .createPaste(pasteEditSession)
                    .to(paste_pt)
                    .copyEntities(false)
                    .copyBiomes(false)
                    .ignoreAirBlocks(true)
                    .maskSource(mask)
                    .build();

            // perform operation
            Operations.complete(operation);

        }

        // close editsessions
        copyEditSession.close();
        pasteEditSession.close();
        return true;
    }

}
