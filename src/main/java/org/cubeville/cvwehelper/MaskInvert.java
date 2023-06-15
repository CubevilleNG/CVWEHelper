package org.cubeville.cvwehelper;

import com.sk89q.worldedit.function.mask.AbstractMask;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.mask.Mask2D;
import com.sk89q.worldedit.math.BlockVector3;

public class MaskInvert extends AbstractMask
{
    private Mask mask;
    
    MaskInvert(Mask mask) {
        this.mask = mask;
    }

    @Override
    public boolean test(BlockVector3 vector) {
        return !mask.test(vector);
    }

    @Override
    public Mask2D toMask2D() { // What is a 2d mask even?
        return null;
    }
}
