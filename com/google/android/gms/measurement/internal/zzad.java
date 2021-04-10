package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.*;
import com.google.android.gms.common.internal.safeparcel.*;
import android.os.*;

public final class zzad extends AbstractSafeParcelable
{
    public static final Parcelable$Creator<zzad> CREATOR;
    public final String name;
    public final String origin;
    public final zzaa zzaid;
    public final long zzaip;
    
    static {
        CREATOR = (Parcelable$Creator)new zzae();
    }
    
    zzad(final zzad zzad, final long zzaip) {
        Preconditions.checkNotNull(zzad);
        this.name = zzad.name;
        this.zzaid = zzad.zzaid;
        this.origin = zzad.origin;
        this.zzaip = zzaip;
    }
    
    public zzad(final String name, final zzaa zzaid, final String origin, final long zzaip) {
        this.name = name;
        this.zzaid = zzaid;
        this.origin = origin;
        this.zzaip = zzaip;
    }
    
    @Override
    public final String toString() {
        final String origin = this.origin;
        final String name = this.name;
        final String value = String.valueOf(this.zzaid);
        final StringBuilder sb = new StringBuilder(String.valueOf(origin).length() + 21 + String.valueOf(name).length() + String.valueOf(value).length());
        sb.append("origin=");
        sb.append(origin);
        sb.append(",name=");
        sb.append(name);
        sb.append(",params=");
        sb.append(value);
        return sb.toString();
    }
    
    public final void writeToParcel(final Parcel parcel, final int n) {
        final int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.name, false);
        SafeParcelWriter.writeParcelable(parcel, 3, (Parcelable)this.zzaid, n, false);
        SafeParcelWriter.writeString(parcel, 4, this.origin, false);
        SafeParcelWriter.writeLong(parcel, 5, this.zzaip);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
