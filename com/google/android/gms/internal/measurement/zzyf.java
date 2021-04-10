package com.google.android.gms.internal.measurement;

import java.util.*;
import java.util.function.*;

final class zzyf implements ListIterator<String>
{
    private ListIterator<String> zzccr;
    private final /* synthetic */ int zzccs;
    private final /* synthetic */ zzye zzcct;
    
    zzyf(final zzye zzcct, final int zzccs) {
        this.zzcct = zzcct;
        this.zzccs = zzccs;
        this.zzccr = this.zzcct.zzccq.listIterator(this.zzccs);
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
        return this.zzccr.hasNext();
    }
    
    @Override
    public final boolean hasPrevious() {
        return this.zzccr.hasPrevious();
    }
    
    @Override
    public final int nextIndex() {
        return this.zzccr.nextIndex();
    }
    
    @Override
    public final int previousIndex() {
        return this.zzccr.previousIndex();
    }
    
    @Override
    public final void remove() {
        throw new UnsupportedOperationException();
    }
}
