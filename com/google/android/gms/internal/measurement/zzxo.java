package com.google.android.gms.internal.measurement;

import java.util.function.*;
import java.util.*;

final class zzxo implements Iterator<Map.Entry<Object, Object>>
{
    private int pos;
    private Iterator<Map.Entry<Object, Object>> zzccg;
    private final /* synthetic */ zzxm zzcch;
    
    private zzxo(final zzxm zzcch) {
        this.zzcch = zzcch;
        this.pos = this.zzcch.zzccb.size();
    }
    
    private final Iterator<Map.Entry<Object, Object>> zzyb() {
        if (this.zzccg == null) {
            this.zzccg = this.zzcch.zzcce.entrySet().iterator();
        }
        return this.zzccg;
    }
    
    @Override
    public void forEachRemaining(final Consumer<?> p0) {
        // 
        // This method could not be decompiled.
        // 
        // Could not show original bytecode, likely due to the same error.
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public final boolean hasNext() {
        final int pos = this.pos;
        return (pos > 0 && pos <= this.zzcch.zzccb.size()) || this.zzyb().hasNext();
    }
    
    @Override
    public final void remove() {
        throw new UnsupportedOperationException();
    }
}
