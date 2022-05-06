package org.snowcoal.snowcoalstools.erode;
import org.snowcoal.snowcoalstools.commands.SenderData;
import org.snowcoal.snowcoalstools.erode.WaterErosion;

public class WaterErodeThread implements Runnable{
    private SenderData senderData;
    private Thread thread;

    public WaterErodeThread(SenderData senderData){
        this.senderData = senderData;
    }

    @Override
    public void run() {
        // attempt to run command
        WaterErosion waterErosion = null;
        try {
            waterErosion = new WaterErosion(senderData.sel, senderData.player, senderData.instance, 0, 0);
        } catch(Exception e){
            senderData.msgSender.sendMessage(senderData.sender, error);
            senderData.instance.printException(e);
            return;
        }

        // success message
        senderData.msgSender.sendMessage(senderData.sender, logo + waterErosion.getChangedBlocks() + " Blocks were changed.");
    }

    public void start(){
        if(thread == null){
            thread = new Thread(this);
            thread.start();
        }
    }
    private final String logo = "(&3&lSNOWCOAL&r)&d ";
    private final String error = "&cERROR: An error occurred while attempting to run this command";
}
