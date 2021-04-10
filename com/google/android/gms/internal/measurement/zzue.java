package com.google.android.gms.internal.measurement;

import java.util.function.*;
import java.util.*;

final class zzue implements zzuj
{
    private final int limit;
    private int position;
    private final /* synthetic */ zzud zzbuc;
    
    zzue(final zzud zzbuc) {
        this.zzbuc = zzbuc;
        this.position = 0;
        this.limit = this.zzbuc.size();
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
        return this.position < this.limit;
    }
    
    @Override
    public final byte nextByte() {
        try {
            return this.zzbuc.zzal(this.position++);
        }
        catch (IndexOutOfBoundsException ex) {
            throw new NoSuchElementException(ex.getMessage());
        }
    }
    
    @Override
    public final void remove() {
        throw new UnsupportedOperationException();
    }
}
