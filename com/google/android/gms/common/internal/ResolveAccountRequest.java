package com.google.android.gms.common.internal;

import android.accounts.*;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.internal.safeparcel.*;
import android.os.*;

public class ResolveAccountRequest extends AbstractSafeParcelable
{
    public static final Parcelable$Creator<ResolveAccountRequest> CREATOR;
    private final int zzal;
    private final Account zzs;
    private final int zzut;
    private final GoogleSignInAccount zzuu;
    
    static {
        CREATOR = (Parcelable$Creator)new ResolveAccountRequestCreator();
    }
    
    ResolveAccountRequest(final int zzal, final Account zzs, final int zzut, final GoogleSignInAccount zzuu) {
        this.zzal = zzal;
        this.zzs = zzs;
        this.zzut = zzut;
        this.zzuu = zzuu;
    }
    
    public ResolveAccountRequest(final Account account, final int n, final GoogleSignInAccount googleSignInAccount) {
        this(2, account, n, googleSignInAccount);
    }
    
    public Account getAccount() {
        return this.zzs;
    }
    
    public int getSessionId() {
        return this.zzut;
    }
    
    public GoogleSignInAccount getSignInAccountHint() {
        return this.zzuu;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        final int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zzal);
        SafeParcelWriter.writeParcelable(parcel, 2, (Parcelable)this.getAccount(), n, false);
        SafeParcelWriter.writeInt(parcel, 3, this.getSessionId());
        SafeParcelWriter.writeParcelable(parcel, 4, (Parcelable)this.getSignInAccountHint(), n, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
