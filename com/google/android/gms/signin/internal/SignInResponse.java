package com.google.android.gms.signin.internal;

import com.google.android.gms.common.internal.*;
import com.google.android.gms.common.*;
import android.app.*;
import com.google.android.gms.common.internal.safeparcel.*;
import android.os.*;

public class SignInResponse extends AbstractSafeParcelable
{
    public static final Parcelable$Creator<SignInResponse> CREATOR;
    private final ResolveAccountResponse zzadu;
    private final int zzal;
    private final ConnectionResult zzeu;
    
    static {
        CREATOR = (Parcelable$Creator)new SignInResponseCreator();
    }
    
    public SignInResponse(final int n) {
        this(new ConnectionResult(n, null), null);
    }
    
    SignInResponse(final int zzal, final ConnectionResult zzeu, final ResolveAccountResponse zzadu) {
        this.zzal = zzal;
        this.zzeu = zzeu;
        this.zzadu = zzadu;
    }
    
    public SignInResponse(final ConnectionResult connectionResult, final ResolveAccountResponse resolveAccountResponse) {
        this(1, connectionResult, resolveAccountResponse);
    }
    
    public ConnectionResult getConnectionResult() {
        return this.zzeu;
    }
    
    public ResolveAccountResponse getResolveAccountResponse() {
        return this.zzadu;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        final int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zzal);
        SafeParcelWriter.writeParcelable(parcel, 2, (Parcelable)this.getConnectionResult(), n, false);
        SafeParcelWriter.writeParcelable(parcel, 3, (Parcelable)this.getResolveAccountResponse(), n, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
