package com.google.android.gms.signin.internal;

import android.content.*;
import com.google.android.gms.common.api.*;
import com.google.android.gms.common.internal.safeparcel.*;
import android.os.*;

public class AuthAccountResult extends AbstractSafeParcelable implements Result
{
    public static final Parcelable$Creator<AuthAccountResult> CREATOR;
    private int zzadn;
    private Intent zzado;
    private final int zzal;
    
    static {
        CREATOR = (Parcelable$Creator)new AuthAccountResultCreator();
    }
    
    public AuthAccountResult() {
        this(0, null);
    }
    
    AuthAccountResult(final int zzal, final int zzadn, final Intent zzado) {
        this.zzal = zzal;
        this.zzadn = zzadn;
        this.zzado = zzado;
    }
    
    public AuthAccountResult(final int n, final Intent intent) {
        this(2, n, intent);
    }
    
    public int getConnectionResultCode() {
        return this.zzadn;
    }
    
    public Intent getRawAuthResolutionIntent() {
        return this.zzado;
    }
    
    @Override
    public Status getStatus() {
        if (this.zzadn == 0) {
            return Status.RESULT_SUCCESS;
        }
        return Status.RESULT_CANCELED;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        final int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zzal);
        SafeParcelWriter.writeInt(parcel, 2, this.getConnectionResultCode());
        SafeParcelWriter.writeParcelable(parcel, 3, (Parcelable)this.getRawAuthResolutionIntent(), n, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
