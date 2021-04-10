package com.google.firebase.messaging;

import android.os.*;
import com.google.android.gms.common.internal.safeparcel.*;

public final class RemoteMessage extends AbstractSafeParcelable
{
    public static final Parcelable$Creator<RemoteMessage> CREATOR;
    Bundle zzdp;
    
    static {
        CREATOR = (Parcelable$Creator)new zzc();
    }
    
    public RemoteMessage(final Bundle zzdp) {
        this.zzdp = zzdp;
    }
    
    public final void writeToParcel(final Parcel parcel, int beginObjectHeader) {
        beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeBundle(parcel, 2, this.zzdp, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
