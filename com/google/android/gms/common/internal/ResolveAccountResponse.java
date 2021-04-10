package com.google.android.gms.common.internal;

import com.google.android.gms.common.*;
import com.google.android.gms.common.internal.safeparcel.*;
import android.os.*;

public class ResolveAccountResponse extends AbstractSafeParcelable
{
    public static final Parcelable$Creator<ResolveAccountResponse> CREATOR;
    private final int zzal;
    private ConnectionResult zzeu;
    private boolean zzhs;
    private IBinder zzqv;
    private boolean zzuv;
    
    static {
        CREATOR = (Parcelable$Creator)new ResolveAccountResponseCreator();
    }
    
    ResolveAccountResponse(final int zzal, final IBinder zzqv, final ConnectionResult zzeu, final boolean zzhs, final boolean zzuv) {
        this.zzal = zzal;
        this.zzqv = zzqv;
        this.zzeu = zzeu;
        this.zzhs = zzhs;
        this.zzuv = zzuv;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResolveAccountResponse)) {
            return false;
        }
        final ResolveAccountResponse resolveAccountResponse = (ResolveAccountResponse)o;
        return this.zzeu.equals(resolveAccountResponse.zzeu) && this.getAccountAccessor().equals(resolveAccountResponse.getAccountAccessor());
    }
    
    public IAccountAccessor getAccountAccessor() {
        return IAccountAccessor.Stub.asInterface(this.zzqv);
    }
    
    public ConnectionResult getConnectionResult() {
        return this.zzeu;
    }
    
    public boolean getSaveDefaultAccount() {
        return this.zzhs;
    }
    
    public boolean isFromCrossClientAuth() {
        return this.zzuv;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        final int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zzal);
        SafeParcelWriter.writeIBinder(parcel, 2, this.zzqv, false);
        SafeParcelWriter.writeParcelable(parcel, 3, (Parcelable)this.getConnectionResult(), n, false);
        SafeParcelWriter.writeBoolean(parcel, 4, this.getSaveDefaultAccount());
        SafeParcelWriter.writeBoolean(parcel, 5, this.isFromCrossClientAuth());
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
