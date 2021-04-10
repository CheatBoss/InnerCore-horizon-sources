package com.google.android.gms.common;

import javax.annotation.*;
import com.google.android.gms.common.internal.*;
import android.util.*;
import com.google.android.gms.dynamic.*;
import android.os.*;
import com.google.android.gms.common.internal.safeparcel.*;

public class GoogleCertificatesQuery extends AbstractSafeParcelable
{
    public static final Parcelable$Creator<GoogleCertificatesQuery> CREATOR;
    private final String zzbh;
    @Nullable
    private final GoogleCertificates.CertData zzbi;
    private final boolean zzbj;
    
    static {
        CREATOR = (Parcelable$Creator)new GoogleCertificatesQueryCreator();
    }
    
    GoogleCertificatesQuery(final String zzbh, @Nullable final IBinder binder, final boolean zzbj) {
        this.zzbh = zzbh;
        this.zzbi = zza(binder);
        this.zzbj = zzbj;
    }
    
    GoogleCertificatesQuery(final String zzbh, @Nullable final GoogleCertificates.CertData zzbi, final boolean zzbj) {
        this.zzbh = zzbh;
        this.zzbi = zzbi;
        this.zzbj = zzbj;
    }
    
    @Nullable
    private static GoogleCertificates.CertData zza(@Nullable final IBinder binder) {
        if (binder == null) {
            return null;
        }
        try {
            final IObjectWrapper bytesWrapped = ICertData.Stub.asInterface(binder).getBytesWrapped();
            byte[] array;
            if (bytesWrapped == null) {
                array = null;
            }
            else {
                array = ObjectWrapper.unwrap(bytesWrapped);
            }
            if (array != null) {
                return new zzb(array);
            }
            Log.e("GoogleCertificatesQuery", "Could not unwrap certificate");
            return null;
        }
        catch (RemoteException ex) {
            Log.e("GoogleCertificatesQuery", "Could not unwrap certificate", (Throwable)ex);
            return null;
        }
    }
    
    public boolean getAllowTestKeys() {
        return this.zzbj;
    }
    
    @Nullable
    public IBinder getCallingCertificateBinder() {
        final GoogleCertificates.CertData zzbi = this.zzbi;
        if (zzbi == null) {
            Log.w("GoogleCertificatesQuery", "certificate binder is null");
            return null;
        }
        return zzbi.asBinder();
    }
    
    public String getCallingPackage() {
        return this.zzbh;
    }
    
    public void writeToParcel(final Parcel parcel, int beginObjectHeader) {
        beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 1, this.getCallingPackage(), false);
        SafeParcelWriter.writeIBinder(parcel, 2, this.getCallingCertificateBinder(), false);
        SafeParcelWriter.writeBoolean(parcel, 3, this.getAllowTestKeys());
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
