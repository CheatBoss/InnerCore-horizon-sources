package com.google.android.gms.internal.measurement;

import java.util.*;
import java.util.function.*;

final class zzxu implements Iterator<Map.Entry<Object, Object>>
{
    private int pos;
    private Iterator<Map.Entry<Object, Object>> zzccg;
    private final /* synthetic */ zzxm zzcch;
    private boolean zzccl;
    
    private zzxu(final zzxm zzcch) {
        this.zzcch = zzcch;
        this.pos = -1;
    }
    
    private final Iterator<Map.Entry<Object, Object>> zzyb() {
        if (this.zzccg == null) {
            this.zzccg = this.zzcch.zzccc.entrySet().iterator();
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
        return this.pos + 1 < this.zzcch.zzccb.size() || (!this.zzcch.zzccc.isEmpty() && this.zzyb().hasNext());
    }
    
    @Override
    public final void remove() {
        if (!this.zzccl) {
            throw new IllegalStateException("remove() was called before next()");
        }
        this.zzccl = false;
        this.zzcch.zzxz();
        if (this.pos < this.zzcch.zzccb.size()) {
            this.zzcch.zzbv(this.pos--);
            return;
        }
        this.zzyb().remove();
    }
}
