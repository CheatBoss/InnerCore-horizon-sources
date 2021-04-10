package com.google.android.gms.common;

import android.app.*;
import com.google.android.gms.common.internal.*;
import com.google.android.gms.common.internal.safeparcel.*;
import android.os.*;

public final class ConnectionResult extends AbstractSafeParcelable
{
    public static final Parcelable$Creator<ConnectionResult> CREATOR;
    public static final ConnectionResult RESULT_SUCCESS;
    private final int zzal;
    private final int zzam;
    private final PendingIntent zzan;
    private final String zzao;
    
    static {
        RESULT_SUCCESS = new ConnectionResult(0);
        CREATOR = (Parcelable$Creator)new ConnectionResultCreator();
    }
    
    public ConnectionResult(final int n) {
        this(n, null, null);
    }
    
    ConnectionResult(final int zzal, final int zzam, final PendingIntent zzan, final String zzao) {
        this.zzal = zzal;
        this.zzam = zzam;
        this.zzan = zzan;
        this.zzao = zzao;
    }
    
    public ConnectionResult(final int n, final PendingIntent pendingIntent) {
        this(n, pendingIntent, null);
    }
    
    public ConnectionResult(final int n, final PendingIntent pendingIntent, final String s) {
        this(1, n, pendingIntent, s);
    }
    
    static String zza(final int n) {
        if (n == 99) {
            return "UNFINISHED";
        }
        if (n == 1500) {
            return "DRIVE_EXTERNAL_STORAGE_REQUIRED";
        }
        switch (n) {
            default: {
                switch (n) {
                    default: {
                        final StringBuilder sb = new StringBuilder(31);
                        sb.append("UNKNOWN_ERROR_CODE(");
                        sb.append(n);
                        sb.append(")");
                        return sb.toString();
                    }
                    case 21: {
                        return "API_VERSION_UPDATE_REQUIRED";
                    }
                    case 20: {
                        return "RESTRICTED_PROFILE";
                    }
                    case 19: {
                        return "SERVICE_MISSING_PERMISSION";
                    }
                    case 18: {
                        return "SERVICE_UPDATING";
                    }
                    case 17: {
                        return "SIGN_IN_FAILED";
                    }
                    case 16: {
                        return "API_UNAVAILABLE";
                    }
                    case 15: {
                        return "INTERRUPTED";
                    }
                    case 14: {
                        return "TIMEOUT";
                    }
                    case 13: {
                        return "CANCELED";
                    }
                }
                break;
            }
            case 11: {
                return "LICENSE_CHECK_FAILED";
            }
            case 10: {
                return "DEVELOPER_ERROR";
            }
            case 9: {
                return "SERVICE_INVALID";
            }
            case 8: {
                return "INTERNAL_ERROR";
            }
            case 7: {
                return "NETWORK_ERROR";
            }
            case 6: {
                return "RESOLUTION_REQUIRED";
            }
            case 5: {
                return "INVALID_ACCOUNT";
            }
            case 4: {
                return "SIGN_IN_REQUIRED";
            }
            case 3: {
                return "SERVICE_DISABLED";
            }
            case 2: {
                return "SERVICE_VERSION_UPDATE_REQUIRED";
            }
            case 1: {
                return "SERVICE_MISSING";
            }
            case 0: {
                return "SUCCESS";
            }
            case -1: {
                return "UNKNOWN";
            }
        }
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ConnectionResult)) {
            return false;
        }
        final ConnectionResult connectionResult = (ConnectionResult)o;
        return this.zzam == connectionResult.zzam && Objects.equal(this.zzan, connectionResult.zzan) && Objects.equal(this.zzao, connectionResult.zzao);
    }
    
    public final int getErrorCode() {
        return this.zzam;
    }
    
    public final String getErrorMessage() {
        return this.zzao;
    }
    
    public final PendingIntent getResolution() {
        return this.zzan;
    }
    
    public final boolean hasResolution() {
        return this.zzam != 0 && this.zzan != null;
    }
    
    @Override
    public final int hashCode() {
        return Objects.hashCode(this.zzam, this.zzan, this.zzao);
    }
    
    public final boolean isSuccess() {
        return this.zzam == 0;
    }
    
    @Override
    public final String toString() {
        return Objects.toStringHelper(this).add("statusCode", zza(this.zzam)).add("resolution", this.zzan).add("message", this.zzao).toString();
    }
    
    public final void writeToParcel(final Parcel parcel, final int n) {
        final int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zzal);
        SafeParcelWriter.writeInt(parcel, 2, this.getErrorCode());
        SafeParcelWriter.writeParcelable(parcel, 3, (Parcelable)this.getResolution(), n, false);
        SafeParcelWriter.writeString(parcel, 4, this.getErrorMessage(), false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
