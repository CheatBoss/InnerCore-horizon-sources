package com.google.android.gms.signin.internal;

import java.util.*;
import com.google.android.gms.common.api.*;
import android.os.*;
import com.google.android.gms.common.internal.safeparcel.*;

public class CheckServerAuthResult extends AbstractSafeParcelable
{
    public static final Parcelable$Creator<CheckServerAuthResult> CREATOR;
    private final boolean zzadp;
    private final List<Scope> zzadq;
    private final int zzal;
    
    static {
        CREATOR = (Parcelable$Creator)new CheckServerAuthResultCreator();
    }
    
    CheckServerAuthResult(final int zzal, final boolean zzadp, final List<Scope> zzadq) {
        this.zzal = zzal;
        this.zzadp = zzadp;
        this.zzadq = zzadq;
    }
    
    public void writeToParcel(final Parcel parcel, int beginObjectHeader) {
        beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zzal);
        SafeParcelWriter.writeBoolean(parcel, 2, this.zzadp);
        SafeParcelWriter.writeTypedList(parcel, 3, this.zzadq, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
