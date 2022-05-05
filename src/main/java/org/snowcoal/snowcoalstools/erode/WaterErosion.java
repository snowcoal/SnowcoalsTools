package org.snowcoal.snowcoalstools.erode;

import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.entity.Player;
import org.snowcoal.snowcoalstools.SnowcoalsTools;

public class WaterErosion extends HeightMapErosion {

    public WaterErosion(Region sel, Player player, SnowcoalsTools instance, int time, int intensity) {
        super(sel, player, instance, time, intensity);
    }

    @Override
    void doErosion() {

    }
}
