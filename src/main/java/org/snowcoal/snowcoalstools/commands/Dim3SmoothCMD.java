package org.snowcoal.snowcoalstools.commands;

import org.snowcoal.snowcoalstools.MessageSender;
import org.snowcoal.snowcoalstools.SnowcoalsTools;
import org.snowcoal.snowcoalstools.smooth.Dim3Smooth;

public class Dim3SmoothCMD extends SelectionCMD{

    public Dim3SmoothCMD(SnowcoalsTools cg, MessageSender ms) {
        super(cg, ms);
    }

    @Override
    boolean runCommand() {
        // check args
        if (args.length > 3 || args.length == 0) {
            Incorrect();
            return true;
        }
        String arg0 = args[0];
        String arg1;
        String arg2;
        int k = 0;
        int numIterations = 1;
        double cutoff = 0.5;

        // check for bad args
        try{
            k = Integer.parseInt(args[0]);
        }
        catch(NumberFormatException e){
            Incorrect();
            return true;
        }
        if(k < 3 || k % 2 == 0){
            Incorrect();
            return true;
        }

        // if 2nd arg
        if(args.length >= 2){
            arg1 = args[1];
            try{
                numIterations = Integer.parseInt(args[1]);
            }
            catch(NumberFormatException e){
                Incorrect();
                return true;
            }
        }

        // if 3rd arg
        if(args.length == 3){
            arg2 = args[2];
            try{
                cutoff = Double.parseDouble(args[2]);
            }
            catch(NumberFormatException e){
                Incorrect();
                return true;
            }
        }

        Dim3Smooth dim3Smooth = null;
        // attempt to run command
        try {
            dim3Smooth = new Dim3Smooth(sel, player, instance, k, numIterations, cutoff);
        } catch(Exception e){
            this.msgSender.sendMessage(sender, error);
            this.instance.printException(e);
            return true;
        }

        // success message
        this.msgSender.sendMessage(sender, logo + dim3Smooth.getChangedBlocks() + " Blocks were changed.");
        return true;
    }

    private void Incorrect(){
        msgSender.sendMessage(sender, incorrectUsage);
        msgSender.sendMessage(sender, incorrectUsage2);
        msgSender.sendMessage(sender, incorrectUsage3);
        msgSender.sendMessage(sender, incorrectUsage4);
        msgSender.sendMessage(sender, incorrectUsage5);
    }

    private final String incorrectUsage = "&eIncorrect command usage. Correct: ./dim3smooth <filterSize> <numIterations> <cutoff>";
    private final String incorrectUsage2 = "&efilterSize must be an odd number >= 3 (larger = more blur)";
    private final String incorrectUsage3 = "&enumIterations is optional and is how many times to run the smoothing (default = 1)";
    private final String incorrectUsage4 = "&ecutoff is optional and determines when to remove blocks (default = 0.5)";
    private final String incorrectUsage5 = "&eSetting smaller will make objects larger and caves smaller, and setting larger will make objects smaller and caves larger";
}
