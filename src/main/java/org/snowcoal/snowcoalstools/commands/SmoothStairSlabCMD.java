package org.snowcoal.snowcoalstools.commands;

import org.snowcoal.snowcoalstools.MessageSender;
import org.snowcoal.snowcoalstools.SnowcoalsTools;
import org.snowcoal.snowcoalstools.smooth.SmoothStairSlab;

public class SmoothStairSlabCMD extends SelectionCMD{

    public SmoothStairSlabCMD(SnowcoalsTools cg, MessageSender ms) {
        super(cg, ms);
    }

    @Override
    boolean runCommand() {
        SmoothStairSlab smoothStairSlab = null;
        // attempt to run command
        try {
            smoothStairSlab = new SmoothStairSlab(sel, player, instance);
        } catch(Exception e){
            this.msgSender.sendMessage(sender, error);
            this.instance.printException(e);
            return true;
        }

        // success message
        this.msgSender.sendMessage(sender, logo + smoothStairSlab.getChangedBlocks() + " Blocks were changed.");
        return true;
    }
}