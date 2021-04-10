package com.google.android.gms.measurement.internal;

import com.google.android.gms.internal.measurement.*;
import java.util.*;
import android.os.*;

public abstract class zzah extends zzr implements zzag
{
    public zzah() {
        super("com.google.android.gms.measurement.internal.IMeasurementService");
    }
    
    @Override
    protected final boolean dispatchTransaction(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
        Label_0473: {
            Object o = null;
            switch (n) {
                default: {
                    return false;
                }
                case 18: {
                    this.zzd(zzs.zza(parcel, zzh.CREATOR));
                    break Label_0473;
                }
                case 17: {
                    o = this.zze(parcel.readString(), parcel.readString(), parcel.readString());
                    break;
                }
                case 16: {
                    o = this.zza(parcel.readString(), parcel.readString(), zzs.zza(parcel, zzh.CREATOR));
                    break;
                }
                case 15: {
                    o = this.zza(parcel.readString(), parcel.readString(), parcel.readString(), zzs.zza(parcel));
                    break;
                }
                case 14: {
                    o = this.zza(parcel.readString(), parcel.readString(), zzs.zza(parcel), zzs.zza(parcel, zzh.CREATOR));
                    break;
                }
                case 13: {
                    this.zzb(zzs.zza(parcel, zzl.CREATOR));
                    break Label_0473;
                }
                case 12: {
                    this.zza(zzs.zza(parcel, zzl.CREATOR), zzs.zza(parcel, zzh.CREATOR));
                    break Label_0473;
                }
                case 11: {
                    final String zzc = this.zzc(zzs.zza(parcel, zzh.CREATOR));
                    parcel2.writeNoException();
                    parcel2.writeString(zzc);
                    return true;
                }
                case 10: {
                    this.zza(parcel.readLong(), parcel.readString(), parcel.readString(), parcel.readString());
                    break Label_0473;
                }
                case 9: {
                    final byte[] zza = this.zza(zzs.zza(parcel, zzad.CREATOR), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeByteArray(zza);
                    return true;
                }
                case 7: {
                    o = this.zza(zzs.zza(parcel, zzh.CREATOR), zzs.zza(parcel));
                    break;
                }
                case 6: {
                    this.zzb(zzs.zza(parcel, zzh.CREATOR));
                    break Label_0473;
                }
                case 5: {
                    this.zza(zzs.zza(parcel, zzad.CREATOR), parcel.readString(), parcel.readString());
                    break Label_0473;
                }
                case 4: {
                    this.zza(zzs.zza(parcel, zzh.CREATOR));
                    break Label_0473;
                }
                case 2: {
                    this.zza(zzs.zza(parcel, zzfh.CREATOR), zzs.zza(parcel, zzh.CREATOR));
                    break Label_0473;
                }
                case 1: {
                    this.zza(zzs.zza(parcel, zzad.CREATOR), zzs.zza(parcel, zzh.CREATOR));
                    break Label_0473;
                }
            }
            parcel2.writeNoException();
            parcel2.writeTypedList((List)o);
            return true;
        }
        parcel2.writeNoException();
        return true;
    }
}
