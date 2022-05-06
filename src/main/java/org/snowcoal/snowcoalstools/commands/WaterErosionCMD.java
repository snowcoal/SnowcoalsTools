package org.snowcoal.snowcoalstools.commands;

import org.snowcoal.snowcoalstools.MessageSender;
import org.snowcoal.snowcoalstools.SnowcoalsTools;
import org.snowcoal.snowcoalstools.erode.WaterErodeThread;

public class WaterErosionCMD extends SelectionCMD {
    public WaterErosionCMD(SnowcoalsTools cg, MessageSender ms) {
        super(cg, ms);
    }

    @Override
    boolean runCommand(SenderData senderData) {
        WaterErodeThread waterErodeThread = new WaterErodeThread(senderData);
        waterErodeThread.start();

        return true;
    }
}
