package com.google.android.gms.internal.measurement;

import java.util.*;
import java.util.function.*;

final class zzyg implements Iterator<String>
{
    private final /* synthetic */ zzye zzcct;
    private Iterator<String> zzccu;
    
    zzyg(final zzye zzcct) {
        this.zzcct = zzcct;
        this.zzccu = this.zzcct.zzccq.iterator();
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
        return this.zzccu.hasNext();
    }
    
    @Override
    public final void remove() {
        throw new UnsupportedOperationException();
    }
}
