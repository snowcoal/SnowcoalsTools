package org.snowcoal.snowcoalstools.commands;

import org.snowcoal.snowcoalstools.MessageSender;
import org.snowcoal.snowcoalstools.SnowcoalsTools;
import org.snowcoal.snowcoalstools.smooth.SmoothStairSlab;

public class SmoothStairSlabCMD extends SelectionCMD{

    public SmoothStairSlabCMD(SnowcoalsTools cg, MessageSender ms) {
        super(cg, ms);
    }

    @Override
    boolean runCommand(SenderData senderData) {
        SmoothStairSlab smoothStairSlab = null;
        // attempt to run command
        try {
            smoothStairSlab = new SmoothStairSlab(senderData.sel, senderData.player, senderData.instance);
        } catch(Exception e){
            this.msgSender.sendMessage(senderData.sender, error);
            this.instance.printException(e);
            return true;
        }

        // success message
        this.msgSender.sendMessage(senderData.sender, logo + smoothStairSlab.getChangedBlocks() + " Blocks were changed.");
        return true;
    }

    private final String logo = "(&3&lSNOWCOAL&r)&d ";
    private final String error = "&cERROR: An error occurred while attempting to run this command";
}