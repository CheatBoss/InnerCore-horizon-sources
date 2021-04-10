package com.google.android.gms.common.api;

import com.google.android.gms.common.internal.*;
import android.os.*;
import com.google.android.gms.common.internal.safeparcel.*;

public final class Scope extends AbstractSafeParcelable implements ReflectedParcelable
{
    public static final Parcelable$Creator<Scope> CREATOR;
    private final int zzal;
    private final String zzdp;
    
    static {
        CREATOR = (Parcelable$Creator)new zzd();
    }
    
    Scope(final int zzal, final String zzdp) {
        Preconditions.checkNotEmpty(zzdp, "scopeUri must not be null or empty");
        this.zzal = zzal;
        this.zzdp = zzdp;
    }
    
    public Scope(final String s) {
        this(1, s);
    }
    
    @Override
    public final boolean equals(final Object o) {
        return this == o || (o instanceof Scope && this.zzdp.equals(((Scope)o).zzdp));
    }
    
    public final String getScopeUri() {
        return this.zzdp;
    }
    
    @Override
    public final int hashCode() {
        return this.zzdp.hashCode();
    }
    
    @Override
    public final String toString() {
        return this.zzdp;
    }
    
    public final void writeToParcel(final Parcel parcel, int beginObjectHeader) {
        beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zzal);
        SafeParcelWriter.writeString(parcel, 2, this.getScopeUri(), false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
