package com.google.android.gms.internal.measurement;

import java.util.*;
import java.util.function.*;

final class zzvz<K> implements Iterator<Map.Entry<K, Object>>
{
    private Iterator<Map.Entry<K, Object>> zzcac;
    
    public zzvz(final Iterator<Map.Entry<K, Object>> zzcac) {
        this.zzcac = zzcac;
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
        return this.zzcac.hasNext();
    }
    
    @Override
    public final void remove() {
        this.zzcac.remove();
    }
}
