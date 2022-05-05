package org.snowcoal.snowcoalstools.commands;

import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.regions.Region;
import org.snowcoal.snowcoalstools.MessageSender;
import org.snowcoal.snowcoalstools.SnowcoalsTools;
import org.snowcoal.snowcoalstools.erode.WaterErosion;

public class WaterErosionCMD extends ErosionCMD{
    public WaterErosionCMD(SnowcoalsTools cg, MessageSender ms) {
        super(cg, ms);
    }

    @Override
    void runCommand(Region sel, Player player, SnowcoalsTools instance) {

    }

    @Override
    int getChangedBlocks() {
        return 0;
    }
}
