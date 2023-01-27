package org.snowcoal.snowcoalstools.commands;

import org.snowcoal.snowcoalstools.SnowcoalsTools;
import org.snowcoal.snowcoalstools.MessageSender;
import org.snowcoal.snowcoalstools.smooth.BlockScanner;

public class BlockScannerCMD  extends SelectionCMD {
    private SnowcoalsTools instance;
    private MessageSender msgSender = null;

    public BlockScannerCMD(SnowcoalsTools cg, MessageSender ms){ super(cg, ms); }

    @Override
    boolean runCommand(SenderData senderData) {
        BlockScanner blockScanner = null;
        // attempt to run command
        try {
            blockScanner = new BlockScanner(senderData.sel, senderData.player, senderData.instance);
            blockScanner.scanBlocks();
        } catch(Exception e){
            this.msgSender.sendMessage(senderData.sender, error);
            this.instance.printException(e);
            return true;
        }
        return false;
    }

    private final String logo = "(&3&lSNOWCOAL&r)&d ";
    private final String error = "&cERROR: An error occurred while attempting to run this command";

}
