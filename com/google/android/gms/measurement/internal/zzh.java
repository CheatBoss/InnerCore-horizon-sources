package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.*;
import android.text.*;
import android.os.*;
import com.google.android.gms.common.internal.safeparcel.*;

public final class zzh extends AbstractSafeParcelable
{
    public static final Parcelable$Creator<zzh> CREATOR;
    public final String packageName;
    public final long zzadt;
    public final String zzafx;
    public final String zzafz;
    public final long zzagd;
    public final String zzage;
    public final long zzagf;
    public final boolean zzagg;
    public final long zzagh;
    public final boolean zzagi;
    public final boolean zzagj;
    public final String zzagk;
    public final String zzagv;
    public final boolean zzagw;
    public final long zzagx;
    public final int zzagy;
    public final boolean zzagz;
    public final String zzts;
    
    static {
        CREATOR = (Parcelable$Creator)new zzi();
    }
    
    zzh(final String packageName, String zzafx, final String zzts, final long zzagd, final String zzage, final long zzadt, final long zzagf, final String zzagv, final boolean zzagg, final boolean zzagw, final String zzafz, final long zzagh, final long zzagx, final int zzagy, final boolean zzagi, final boolean zzagj, final boolean zzagz, final String zzagk) {
        Preconditions.checkNotEmpty(packageName);
        this.packageName = packageName;
        if (TextUtils.isEmpty((CharSequence)zzafx)) {
            zzafx = null;
        }
        this.zzafx = zzafx;
        this.zzts = zzts;
        this.zzagd = zzagd;
        this.zzage = zzage;
        this.zzadt = zzadt;
        this.zzagf = zzagf;
        this.zzagv = zzagv;
        this.zzagg = zzagg;
        this.zzagw = zzagw;
        this.zzafz = zzafz;
        this.zzagh = zzagh;
        this.zzagx = zzagx;
        this.zzagy = zzagy;
        this.zzagi = zzagi;
        this.zzagj = zzagj;
        this.zzagz = zzagz;
        this.zzagk = zzagk;
    }
    
    zzh(final String packageName, final String zzafx, final String zzts, final String zzage, final long zzadt, final long zzagf, final String zzagv, final boolean zzagg, final boolean zzagw, final long zzagd, final String zzafz, final long zzagh, final long zzagx, final int zzagy, final boolean zzagi, final boolean zzagj, final boolean zzagz, final String zzagk) {
        this.packageName = packageName;
        this.zzafx = zzafx;
        this.zzts = zzts;
        this.zzagd = zzagd;
        this.zzage = zzage;
        this.zzadt = zzadt;
        this.zzagf = zzagf;
        this.zzagv = zzagv;
        this.zzagg = zzagg;
        this.zzagw = zzagw;
        this.zzafz = zzafz;
        this.zzagh = zzagh;
        this.zzagx = zzagx;
        this.zzagy = zzagy;
        this.zzagi = zzagi;
        this.zzagj = zzagj;
        this.zzagz = zzagz;
        this.zzagk = zzagk;
    }
    
    public final void writeToParcel(final Parcel parcel, int beginObjectHeader) {
        beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.packageName, false);
        SafeParcelWriter.writeString(parcel, 3, this.zzafx, false);
        SafeParcelWriter.writeString(parcel, 4, this.zzts, false);
        SafeParcelWriter.writeString(parcel, 5, this.zzage, false);
        SafeParcelWriter.writeLong(parcel, 6, this.zzadt);
        SafeParcelWriter.writeLong(parcel, 7, this.zzagf);
        SafeParcelWriter.writeString(parcel, 8, this.zzagv, false);
        SafeParcelWriter.writeBoolean(parcel, 9, this.zzagg);
        SafeParcelWriter.writeBoolean(parcel, 10, this.zzagw);
        SafeParcelWriter.writeLong(parcel, 11, this.zzagd);
        SafeParcelWriter.writeString(parcel, 12, this.zzafz, false);
        SafeParcelWriter.writeLong(parcel, 13, this.zzagh);
        SafeParcelWriter.writeLong(parcel, 14, this.zzagx);
        SafeParcelWriter.writeInt(parcel, 15, this.zzagy);
        SafeParcelWriter.writeBoolean(parcel, 16, this.zzagi);
        SafeParcelWriter.writeBoolean(parcel, 17, this.zzagj);
        SafeParcelWriter.writeBoolean(parcel, 18, this.zzagz);
        SafeParcelWriter.writeString(parcel, 19, this.zzagk, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
