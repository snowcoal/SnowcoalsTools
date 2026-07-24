package org.snowcoal.snowcoalstools.utils;

/**
 * XYZBlock
 *
 * hashable x/y/z position for a block
 *
 * Can be used as a map key to save/get some 3rd attribute from its x/y/z position
 * Has a very fast hash function
 */
public class XYZBlock {
    public int posX;
    public int posY;
    public int posZ;

    public XYZBlock(int posX, int posY, int posZ) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (getClass() != obj.getClass()) return false;
        final XYZBlock other = (XYZBlock) obj;
        if ((!(posX == other.posX)) || (!(posY == other.posY)) || (!(posZ == other.posZ)))
            return false;
        return true;
    }

    // COLLISIONS WILL HAPPEN PAST X/Z +512 -512 (10 bits per int)
    @Override
    public int hashCode() {
        // shift x left 20 times and clear highest 2 bits
        int x = (posX << 20) & 0x3ff00000;
        // shift y left 10 times and clear highest 12 bits and lowest 10 bits
        int y = (posY << 10) & 0x000ffc00;
        // clear highest 22 bits of z
        int z = posZ & 0x000003ff;
        // ORing together yields | 00 | 10 low bits x | 10 low bits y | 10 low bits z |
        return x | y | z;
    }
}
