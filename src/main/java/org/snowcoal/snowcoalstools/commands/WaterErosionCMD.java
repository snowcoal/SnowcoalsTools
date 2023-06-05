package org.snowcoal.snowcoalstools.commands;

import org.snowcoal.snowcoalstools.MessageSender;
import org.snowcoal.snowcoalstools.SnowcoalsTools;
import org.snowcoal.snowcoalstools.erode.WaterErosion;

public class WaterErosionCMD extends SelectionCMD {
    public WaterErosionCMD(SnowcoalsTools cg, MessageSender ms) {
        super(cg, ms);
    }

    @Override
    boolean runCommand() {
        // attempt to run command
        WaterErosion waterErosion = null;
        try {
            waterErosion = new WaterErosion(sel, player, instance, 0, 0);
        } catch (Exception e) {
            msgSender.sendMessage(sender, error);
            instance.printException(e);
            return false;
        }

        // success message
        msgSender.sendMessage(sender, logo + waterErosion.getChangedBlocks() + " Blocks were changed.");

        return true;
    }
}
