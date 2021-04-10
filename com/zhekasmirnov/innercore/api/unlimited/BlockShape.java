package com.zhekasmirnov.innercore.api.unlimited;

import com.zhekasmirnov.innercore.api.*;
import com.zhekasmirnov.innercore.api.mod.ui.icon.*;

public class BlockShape
{
    public float x1;
    public float x2;
    public float y1;
    public float y2;
    public float z1;
    public float z2;
    
    public BlockShape() {
        this.set(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public BlockShape(final float n, final float n2, final float n3, final float n4, final float n5, final float n6) {
        this.set(n, n2, n3, n4, n5, n6);
    }
    
    public void set(final float x1, final float y1, final float z1, final float x2, final float y2, final float z2) {
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
    }
    
    public void setToBlock(final int n, int i) {
        NativeBlock.setShape(n, i, this.x1, this.y1, this.z1, this.x2, this.y2, this.z2);
        if (i == -1) {
            for (i = 0; i < 16; ++i) {
                ItemModels.updateBlockShape(n, i, this);
            }
        }
        else {
            ItemModels.updateBlockShape(n, i, this);
        }
    }
}
