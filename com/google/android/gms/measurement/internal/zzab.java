package com.google.android.gms.measurement.internal;

import java.util.*;
import java.util.function.*;

final class zzab implements Iterator<String>
{
    private Iterator<String> zzain;
    private final /* synthetic */ zzaa zzaio;
    
    zzab(final zzaa zzaio) {
        this.zzaio = zzaio;
        this.zzain = this.zzaio.zzaim.keySet().iterator();
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
        return this.zzain.hasNext();
    }
    
    @Override
    public final void remove() {
        throw new UnsupportedOperationException("Remove not supported");
    }
}
