package com.google.android.gms.signin.internal;

import com.google.android.gms.common.api.*;
import android.accounts.*;
import com.google.android.gms.common.internal.safeparcel.*;
import android.os.*;

public class RecordConsentRequest extends AbstractSafeParcelable
{
    public static final Parcelable$Creator<RecordConsentRequest> CREATOR;
    private final Scope[] zzadr;
    private final int zzal;
    private final Account zzs;
    private final String zzw;
    
    static {
        CREATOR = (Parcelable$Creator)new RecordConsentRequestCreator();
    }
    
    RecordConsentRequest(final int zzal, final Account zzs, final Scope[] zzadr, final String zzw) {
        this.zzal = zzal;
        this.zzs = zzs;
        this.zzadr = zzadr;
        this.zzw = zzw;
    }
    
    public Account getAccount() {
        return this.zzs;
    }
    
    public Scope[] getScopesToConsent() {
        return this.zzadr;
    }
    
    public String getServerClientId() {
        return this.zzw;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        final int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zzal);
        SafeParcelWriter.writeParcelable(parcel, 2, (Parcelable)this.getAccount(), n, false);
        SafeParcelWriter.writeTypedArray(parcel, 3, this.getScopesToConsent(), n, false);
        SafeParcelWriter.writeString(parcel, 4, this.getServerClientId(), false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
