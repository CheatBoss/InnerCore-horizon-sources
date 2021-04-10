package com.google.android.gms.common.internal;

import com.google.android.gms.common.api.*;
import android.accounts.*;
import com.google.android.gms.common.internal.safeparcel.*;
import android.os.*;

public class AuthAccountRequest extends AbstractSafeParcelable
{
    public static final Parcelable$Creator<AuthAccountRequest> CREATOR;
    private final int zzal;
    @Deprecated
    private final IBinder zzqv;
    private final Scope[] zzqw;
    private Integer zzqx;
    private Integer zzqy;
    private Account zzs;
    
    static {
        CREATOR = (Parcelable$Creator)new AuthAccountRequestCreator();
    }
    
    AuthAccountRequest(final int zzal, final IBinder zzqv, final Scope[] zzqw, final Integer zzqx, final Integer zzqy, final Account zzs) {
        this.zzal = zzal;
        this.zzqv = zzqv;
        this.zzqw = zzqw;
        this.zzqx = zzqx;
        this.zzqy = zzqy;
        this.zzs = zzs;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        final int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zzal);
        SafeParcelWriter.writeIBinder(parcel, 2, this.zzqv, false);
        SafeParcelWriter.writeTypedArray(parcel, 3, this.zzqw, n, false);
        SafeParcelWriter.writeIntegerObject(parcel, 4, this.zzqx, false);
        SafeParcelWriter.writeIntegerObject(parcel, 5, this.zzqy, false);
        SafeParcelWriter.writeParcelable(parcel, 6, (Parcelable)this.zzs, n, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
