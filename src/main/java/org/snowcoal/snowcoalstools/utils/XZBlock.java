package org.snowcoal.snowcoalstools.utils;

/**
 * XZBlock
 *
 * hashable x/z position for a block
 *
 * Can be used as a map key to save/get some 3rd attribute from its x/z position
 * Has a very fast hash function
 */
public class XZBlock {
    public int posX;
    public int posZ;

    public XZBlock(int posX, int posZ) {
        this.posX = posX;
        this.posZ = posZ;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (getClass() != obj.getClass()) return false;
        final XZBlock other = (XZBlock) obj;
        if ((!(posX == other.posX)) || (!(posZ == other.posZ)))
            return false;
        return true;
    }

    // COLLISIONS WILL HAPPEN PAST X/Z +32767 -32768
    @Override
    public int hashCode() {
        // shift x left 16 times to make it 0xabcd0000
        int x = posX << 16;
        // clear high bits of z so its 0x00001234
        int z = posZ & 0x0000ffff;
        // ORing together yields 0xabcd1234
        return x | z;
    }
}
