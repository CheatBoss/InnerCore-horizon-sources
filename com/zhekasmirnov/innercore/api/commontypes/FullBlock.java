package com.zhekasmirnov.innercore.api.commontypes;

import org.mozilla.javascript.*;
import com.zhekasmirnov.apparatus.mcpe.*;

public class FullBlock extends ScriptableObject
{
    public int data;
    public int id;
    
    public FullBlock(final int n) {
        this.id = (0xFFFF & n);
        if (n >> 24 == 1) {
            this.id = -this.id;
        }
        this.put("id", (Scriptable)this, (Object)this.id);
        this.data = (n >> 16 & 0xFF);
        this.put("data", (Scriptable)this, (Object)this.data);
    }
    
    public FullBlock(final int id, final int data) {
        this.id = id;
        this.put("id", (Scriptable)this, (Object)this.id);
        this.data = data;
        this.put("data", (Scriptable)this, (Object)this.data);
    }
    
    public FullBlock(final long n, final int n2, final int n3, final int n4) {
        this(NativeBlockSource.getDefaultForActor(n), n2, n3, n4);
    }
    
    public FullBlock(final NativeBlockSource nativeBlockSource, final int n, final int n2, final int n3) {
        if (nativeBlockSource != null) {
            this.id = nativeBlockSource.getBlockId(n, n2, n3);
            this.data = nativeBlockSource.getBlockData(n, n2, n3);
        }
        else {
            this.data = 0;
            this.id = 0;
        }
        this.put("id", (Scriptable)this, (Object)this.id);
        this.put("data", (Scriptable)this, (Object)this.data);
    }
    
    public String getClassName() {
        return "FullBlock";
    }
}
