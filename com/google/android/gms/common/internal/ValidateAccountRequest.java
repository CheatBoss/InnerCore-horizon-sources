package com.google.android.gms.common.internal;

import android.os.*;
import com.google.android.gms.common.internal.safeparcel.*;

@Deprecated
public class ValidateAccountRequest extends AbstractSafeParcelable
{
    public static final Parcelable$Creator<ValidateAccountRequest> CREATOR;
    private final int zzal;
    
    static {
        CREATOR = (Parcelable$Creator)new ValidateAccountRequestCreator();
    }
    
    ValidateAccountRequest(final int zzal) {
        this.zzal = zzal;
    }
    
    public void writeToParcel(final Parcel parcel, int beginObjectHeader) {
        beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zzal);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
