package com.google.android.gms.common.internal;

import com.google.android.gms.common.api.*;
import android.accounts.*;
import com.google.android.gms.common.*;
import java.util.*;
import com.google.android.gms.common.internal.safeparcel.*;
import android.os.*;

public class GetServiceRequest extends AbstractSafeParcelable
{
    public static final Parcelable$Creator<GetServiceRequest> CREATOR;
    private final int version;
    private final int zzst;
    private int zzsu;
    private String zzsv;
    private IBinder zzsw;
    private Scope[] zzsx;
    private Bundle zzsy;
    private Account zzsz;
    private Feature[] zzta;
    private Feature[] zztb;
    private boolean zztc;
    
    static {
        CREATOR = (Parcelable$Creator)new GetServiceRequestCreator();
    }
    
    public GetServiceRequest(final int zzst) {
        this.version = 4;
        this.zzsu = GoogleApiAvailabilityLight.GOOGLE_PLAY_SERVICES_VERSION_CODE;
        this.zzst = zzst;
        this.zztc = true;
    }
    
    GetServiceRequest(final int version, final int zzst, final int zzsu, final String zzsv, final IBinder zzsw, final Scope[] zzsx, final Bundle zzsy, final Account zzsz, final Feature[] zzta, final Feature[] zztb, final boolean zztc) {
        this.version = version;
        this.zzst = zzst;
        this.zzsu = zzsu;
        if ("com.google.android.gms".equals(zzsv)) {
            this.zzsv = "com.google.android.gms";
        }
        else {
            this.zzsv = zzsv;
        }
        if (version < 2) {
            this.zzsz = zzb(zzsw);
        }
        else {
            this.zzsw = zzsw;
            this.zzsz = zzsz;
        }
        this.zzsx = zzsx;
        this.zzsy = zzsy;
        this.zzta = zzta;
        this.zztb = zztb;
        this.zztc = zztc;
    }
    
    private static Account zzb(final IBinder binder) {
        if (binder != null) {
            return AccountAccessor.getAccountBinderSafe(IAccountAccessor.Stub.asInterface(binder));
        }
        return null;
    }
    
    public GetServiceRequest setAuthenticatedAccount(final IAccountAccessor accountAccessor) {
        if (accountAccessor != null) {
            this.zzsw = accountAccessor.asBinder();
        }
        return this;
    }
    
    public GetServiceRequest setCallingPackage(final String zzsv) {
        this.zzsv = zzsv;
        return this;
    }
    
    public GetServiceRequest setClientApiFeatures(final Feature[] zztb) {
        this.zztb = zztb;
        return this;
    }
    
    public GetServiceRequest setClientRequestedAccount(final Account zzsz) {
        this.zzsz = zzsz;
        return this;
    }
    
    public GetServiceRequest setClientRequiredFeatures(final Feature[] zzta) {
        this.zzta = zzta;
        return this;
    }
    
    public GetServiceRequest setExtraArgs(final Bundle zzsy) {
        this.zzsy = zzsy;
        return this;
    }
    
    public GetServiceRequest setScopes(final Collection<Scope> collection) {
        this.zzsx = collection.toArray(new Scope[collection.size()]);
        return this;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        final int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.version);
        SafeParcelWriter.writeInt(parcel, 2, this.zzst);
        SafeParcelWriter.writeInt(parcel, 3, this.zzsu);
        SafeParcelWriter.writeString(parcel, 4, this.zzsv, false);
        SafeParcelWriter.writeIBinder(parcel, 5, this.zzsw, false);
        SafeParcelWriter.writeTypedArray(parcel, 6, this.zzsx, n, false);
        SafeParcelWriter.writeBundle(parcel, 7, this.zzsy, false);
        SafeParcelWriter.writeParcelable(parcel, 8, (Parcelable)this.zzsz, n, false);
        SafeParcelWriter.writeTypedArray(parcel, 10, this.zzta, n, false);
        SafeParcelWriter.writeTypedArray(parcel, 11, this.zztb, n, false);
        SafeParcelWriter.writeBoolean(parcel, 12, this.zztc);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
