package com.google.android.gms.signin.internal;

import com.google.android.gms.common.internal.*;
import com.google.android.gms.common.internal.safeparcel.*;
import android.os.*;

public class SignInRequest extends AbstractSafeParcelable
{
    public static final Parcelable$Creator<SignInRequest> CREATOR;
    private final ResolveAccountRequest zzadt;
    private final int zzal;
    
    static {
        CREATOR = (Parcelable$Creator)new SignInRequestCreator();
    }
    
    SignInRequest(final int zzal, final ResolveAccountRequest zzadt) {
        this.zzal = zzal;
        this.zzadt = zzadt;
    }
    
    public SignInRequest(final ResolveAccountRequest resolveAccountRequest) {
        this(1, resolveAccountRequest);
    }
    
    public ResolveAccountRequest getResolveAccountRequest() {
        return this.zzadt;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        final int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zzal);
        SafeParcelWriter.writeParcelable(parcel, 2, (Parcelable)this.getResolveAccountRequest(), n, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
