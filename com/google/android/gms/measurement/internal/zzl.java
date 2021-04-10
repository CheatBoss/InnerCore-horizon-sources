package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.*;
import com.google.android.gms.common.internal.safeparcel.*;
import android.os.*;

public final class zzl extends AbstractSafeParcelable
{
    public static final Parcelable$Creator<zzl> CREATOR;
    public boolean active;
    public long creationTimestamp;
    public String origin;
    public String packageName;
    public long timeToLive;
    public String triggerEventName;
    public long triggerTimeout;
    public zzfh zzahb;
    public zzad zzahc;
    public zzad zzahd;
    public zzad zzahe;
    
    static {
        CREATOR = (Parcelable$Creator)new zzm();
    }
    
    zzl(final zzl zzl) {
        Preconditions.checkNotNull(zzl);
        this.packageName = zzl.packageName;
        this.origin = zzl.origin;
        this.zzahb = zzl.zzahb;
        this.creationTimestamp = zzl.creationTimestamp;
        this.active = zzl.active;
        this.triggerEventName = zzl.triggerEventName;
        this.zzahc = zzl.zzahc;
        this.triggerTimeout = zzl.triggerTimeout;
        this.zzahd = zzl.zzahd;
        this.timeToLive = zzl.timeToLive;
        this.zzahe = zzl.zzahe;
    }
    
    zzl(final String packageName, final String origin, final zzfh zzahb, final long creationTimestamp, final boolean active, final String triggerEventName, final zzad zzahc, final long triggerTimeout, final zzad zzahd, final long timeToLive, final zzad zzahe) {
        this.packageName = packageName;
        this.origin = origin;
        this.zzahb = zzahb;
        this.creationTimestamp = creationTimestamp;
        this.active = active;
        this.triggerEventName = triggerEventName;
        this.zzahc = zzahc;
        this.triggerTimeout = triggerTimeout;
        this.zzahd = zzahd;
        this.timeToLive = timeToLive;
        this.zzahe = zzahe;
    }
    
    public final void writeToParcel(final Parcel parcel, final int n) {
        final int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.packageName, false);
        SafeParcelWriter.writeString(parcel, 3, this.origin, false);
        SafeParcelWriter.writeParcelable(parcel, 4, (Parcelable)this.zzahb, n, false);
        SafeParcelWriter.writeLong(parcel, 5, this.creationTimestamp);
        SafeParcelWriter.writeBoolean(parcel, 6, this.active);
        SafeParcelWriter.writeString(parcel, 7, this.triggerEventName, false);
        SafeParcelWriter.writeParcelable(parcel, 8, (Parcelable)this.zzahc, n, false);
        SafeParcelWriter.writeLong(parcel, 9, this.triggerTimeout);
        SafeParcelWriter.writeParcelable(parcel, 10, (Parcelable)this.zzahd, n, false);
        SafeParcelWriter.writeLong(parcel, 11, this.timeToLive);
        SafeParcelWriter.writeParcelable(parcel, 12, (Parcelable)this.zzahe, n, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
