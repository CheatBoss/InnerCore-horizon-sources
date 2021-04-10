package com.google.android.gms.common.internal;

import com.google.android.gms.common.*;
import android.os.*;
import com.google.android.gms.common.internal.safeparcel.*;

public class ConnectionInfo extends AbstractSafeParcelable
{
    public static final Parcelable$Creator<ConnectionInfo> CREATOR;
    private Bundle zzsf;
    private Feature[] zzsg;
    
    static {
        CREATOR = (Parcelable$Creator)new ConnectionInfoCreator();
    }
    
    public ConnectionInfo() {
    }
    
    ConnectionInfo(final Bundle zzsf, final Feature[] zzsg) {
        this.zzsf = zzsf;
        this.zzsg = zzsg;
    }
    
    public Feature[] getAvailableFeatures() {
        return this.zzsg;
    }
    
    public Bundle getResolutionBundle() {
        return this.zzsf;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        final int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeBundle(parcel, 1, this.zzsf, false);
        SafeParcelWriter.writeTypedArray(parcel, 2, this.zzsg, n, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
