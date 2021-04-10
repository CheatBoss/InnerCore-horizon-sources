package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.*;
import android.os.*;
import com.google.android.gms.common.internal.safeparcel.*;

public final class zzfh extends AbstractSafeParcelable
{
    public static final Parcelable$Creator<zzfh> CREATOR;
    public final String name;
    public final String origin;
    private final int versionCode;
    private final String zzamp;
    public final long zzaue;
    private final Long zzauf;
    private final Float zzaug;
    private final Double zzauh;
    
    static {
        CREATOR = (Parcelable$Creator)new zzfi();
    }
    
    zzfh(final int versionCode, final String name, final long zzaue, final Long zzauf, final Float n, final String zzamp, final String origin, final Double zzauh) {
        this.versionCode = versionCode;
        this.name = name;
        this.zzaue = zzaue;
        this.zzauf = zzauf;
        Double value = null;
        this.zzaug = null;
        if (versionCode == 1) {
            if (n != null) {
                value = (double)n;
            }
            this.zzauh = value;
        }
        else {
            this.zzauh = zzauh;
        }
        this.zzamp = zzamp;
        this.origin = origin;
    }
    
    zzfh(final zzfj zzfj) {
        this(zzfj.name, zzfj.zzaue, zzfj.value, zzfj.origin);
    }
    
    zzfh(final String name, final long zzaue, final Object o, final String origin) {
        Preconditions.checkNotEmpty(name);
        this.versionCode = 2;
        this.name = name;
        this.zzaue = zzaue;
        this.origin = origin;
        if (o == null) {
            this.zzauf = null;
            this.zzaug = null;
            this.zzauh = null;
            this.zzamp = null;
            return;
        }
        if (o instanceof Long) {
            this.zzauf = (Long)o;
            this.zzaug = null;
            this.zzauh = null;
            this.zzamp = null;
            return;
        }
        if (o instanceof String) {
            this.zzauf = null;
            this.zzaug = null;
            this.zzauh = null;
            this.zzamp = (String)o;
            return;
        }
        if (o instanceof Double) {
            this.zzauf = null;
            this.zzaug = null;
            this.zzauh = (Double)o;
            this.zzamp = null;
            return;
        }
        throw new IllegalArgumentException("User attribute given of un-supported type");
    }
    
    public final Object getValue() {
        final Long zzauf = this.zzauf;
        if (zzauf != null) {
            return zzauf;
        }
        final Double zzauh = this.zzauh;
        if (zzauh != null) {
            return zzauh;
        }
        final String zzamp = this.zzamp;
        if (zzamp != null) {
            return zzamp;
        }
        return null;
    }
    
    public final void writeToParcel(final Parcel parcel, int beginObjectHeader) {
        beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.versionCode);
        SafeParcelWriter.writeString(parcel, 2, this.name, false);
        SafeParcelWriter.writeLong(parcel, 3, this.zzaue);
        SafeParcelWriter.writeLongObject(parcel, 4, this.zzauf, false);
        SafeParcelWriter.writeFloatObject(parcel, 5, null, false);
        SafeParcelWriter.writeString(parcel, 6, this.zzamp, false);
        SafeParcelWriter.writeString(parcel, 7, this.origin, false);
        SafeParcelWriter.writeDoubleObject(parcel, 8, this.zzauh, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
