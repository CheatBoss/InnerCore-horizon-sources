package com.google.android.gms.common.api;

import android.app.*;
import com.google.android.gms.common.internal.*;
import com.google.android.gms.common.internal.safeparcel.*;
import android.os.*;

public final class Status extends AbstractSafeParcelable implements Result, ReflectedParcelable
{
    public static final Parcelable$Creator<Status> CREATOR;
    public static final Status RESULT_CANCELED;
    public static final Status RESULT_DEAD_CLIENT;
    public static final Status RESULT_INTERNAL_ERROR;
    public static final Status RESULT_INTERRUPTED;
    public static final Status RESULT_SUCCESS;
    public static final Status RESULT_TIMEOUT;
    private static final Status zzdq;
    private final int zzal;
    private final int zzam;
    private final PendingIntent zzan;
    private final String zzao;
    
    static {
        RESULT_SUCCESS = new Status(0);
        RESULT_INTERRUPTED = new Status(14);
        RESULT_INTERNAL_ERROR = new Status(8);
        RESULT_TIMEOUT = new Status(15);
        RESULT_CANCELED = new Status(16);
        zzdq = new Status(17);
        RESULT_DEAD_CLIENT = new Status(18);
        CREATOR = (Parcelable$Creator)new zze();
    }
    
    public Status(final int n) {
        this(n, null);
    }
    
    Status(final int zzal, final int zzam, final String zzao, final PendingIntent zzan) {
        this.zzal = zzal;
        this.zzam = zzam;
        this.zzao = zzao;
        this.zzan = zzan;
    }
    
    public Status(final int n, final String s) {
        this(1, n, s, null);
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (!(o instanceof Status)) {
            return false;
        }
        final Status status = (Status)o;
        return this.zzal == status.zzal && this.zzam == status.zzam && Objects.equal(this.zzao, status.zzao) && Objects.equal(this.zzan, status.zzan);
    }
    
    @Override
    public final Status getStatus() {
        return this;
    }
    
    public final int getStatusCode() {
        return this.zzam;
    }
    
    public final String getStatusMessage() {
        return this.zzao;
    }
    
    @Override
    public final int hashCode() {
        return Objects.hashCode(this.zzal, this.zzam, this.zzao, this.zzan);
    }
    
    public final boolean isSuccess() {
        return this.zzam <= 0;
    }
    
    @Override
    public final String toString() {
        return Objects.toStringHelper(this).add("statusCode", this.zzp()).add("resolution", this.zzan).toString();
    }
    
    public final void writeToParcel(final Parcel parcel, final int n) {
        final int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.getStatusCode());
        SafeParcelWriter.writeString(parcel, 2, this.getStatusMessage(), false);
        SafeParcelWriter.writeParcelable(parcel, 3, (Parcelable)this.zzan, n, false);
        SafeParcelWriter.writeInt(parcel, 1000, this.zzal);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
    
    public final String zzp() {
        final String zzao = this.zzao;
        if (zzao != null) {
            return zzao;
        }
        return CommonStatusCodes.getStatusCodeString(this.zzam);
    }
}
