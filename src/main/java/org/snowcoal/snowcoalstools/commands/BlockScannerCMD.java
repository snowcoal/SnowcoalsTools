package org.snowcoal.snowcoalstools.commands;

import org.snowcoal.snowcoalstools.SnowcoalsTools;
import org.snowcoal.snowcoalstools.MessageSender;
import org.snowcoal.snowcoalstools.smooth.BlockScanner;

public class BlockScannerCMD  extends SelectionCMD {

    public BlockScannerCMD(SnowcoalsTools cg, MessageSender ms){ super(cg, ms); }

    @Override
    boolean runCommand() {
        BlockScanner blockScanner = null;
        // attempt to run command
        try {
            blockScanner = new BlockScanner(sel, player, instance);
            blockScanner.scanBlocks();
        } catch(Exception e){
            this.msgSender.sendMessage(sender, error);
            this.instance.printException(e);
            return true;
        }
        return false;
    }

}
