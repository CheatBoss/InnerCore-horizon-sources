package com.google.android.gms.measurement.internal;

import java.util.function.*;
import java.util.*;
import android.os.*;
import com.google.android.gms.common.internal.safeparcel.*;

public final class zzaa extends AbstractSafeParcelable implements Iterable<String>
{
    public static final Parcelable$Creator<zzaa> CREATOR;
    private final Bundle zzaim;
    
    static {
        CREATOR = (Parcelable$Creator)new zzac();
    }
    
    zzaa(final Bundle zzaim) {
        this.zzaim = zzaim;
    }
    
    @Override
    public void forEach(final Consumer<?> p0) {
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
    
    final Object get(final String s) {
        return this.zzaim.get(s);
    }
    
    final Long getLong(final String s) {
        return this.zzaim.getLong(s);
    }
    
    final String getString(final String s) {
        return this.zzaim.getString(s);
    }
    
    @Override
    public final Iterator<String> iterator() {
        return new zzab(this);
    }
    
    public final int size() {
        return this.zzaim.size();
    }
    
    @Override
    public Spliterator<Object> spliterator() {
        return Iterable-CC.$default$spliterator();
    }
    
    @Override
    public final String toString() {
        return this.zzaim.toString();
    }
    
    public final void writeToParcel(final Parcel parcel, int beginObjectHeader) {
        beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeBundle(parcel, 2, this.zziv(), false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
    
    final Double zzbq(final String s) {
        return this.zzaim.getDouble(s);
    }
    
    public final Bundle zziv() {
        return new Bundle(this.zzaim);
    }
}
