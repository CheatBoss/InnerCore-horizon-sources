package com.google.android.gms.measurement.internal;

import com.google.android.gms.internal.measurement.*;
import java.util.*;
import android.os.*;

public final class zzai extends zzq implements zzag
{
    zzai(final IBinder binder) {
        super(binder, "com.google.android.gms.measurement.internal.IMeasurementService");
    }
    
    @Override
    public final List<zzfh> zza(final zzh zzh, final boolean b) throws RemoteException {
        final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
        zzs.zza(obtainAndWriteInterfaceToken, (Parcelable)zzh);
        zzs.writeBoolean(obtainAndWriteInterfaceToken, b);
        final Parcel transactAndReadException = this.transactAndReadException(7, obtainAndWriteInterfaceToken);
        final ArrayList typedArrayList = transactAndReadException.createTypedArrayList((Parcelable$Creator)zzfh.CREATOR);
        transactAndReadException.recycle();
        return (List<zzfh>)typedArrayList;
    }
    
    @Override
    public final List<zzl> zza(final String s, final String s2, final zzh zzh) throws RemoteException {
        final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(s);
        obtainAndWriteInterfaceToken.writeString(s2);
        zzs.zza(obtainAndWriteInterfaceToken, (Parcelable)zzh);
        final Parcel transactAndReadException = this.transactAndReadException(16, obtainAndWriteInterfaceToken);
        final ArrayList typedArrayList = transactAndReadException.createTypedArrayList((Parcelable$Creator)zzl.CREATOR);
        transactAndReadException.recycle();
        return (List<zzl>)typedArrayList;
    }
    
    @Override
    public final List<zzfh> zza(final String s, final String s2, final String s3, final boolean b) throws RemoteException {
        final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(s);
        obtainAndWriteInterfaceToken.writeString(s2);
        obtainAndWriteInterfaceToken.writeString(s3);
        zzs.writeBoolean(obtainAndWriteInterfaceToken, b);
        final Parcel transactAndReadException = this.transactAndReadException(15, obtainAndWriteInterfaceToken);
        final ArrayList typedArrayList = transactAndReadException.createTypedArrayList((Parcelable$Creator)zzfh.CREATOR);
        transactAndReadException.recycle();
        return (List<zzfh>)typedArrayList;
    }
    
    @Override
    public final List<zzfh> zza(final String s, final String s2, final boolean b, final zzh zzh) throws RemoteException {
        final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(s);
        obtainAndWriteInterfaceToken.writeString(s2);
        zzs.writeBoolean(obtainAndWriteInterfaceToken, b);
        zzs.zza(obtainAndWriteInterfaceToken, (Parcelable)zzh);
        final Parcel transactAndReadException = this.transactAndReadException(14, obtainAndWriteInterfaceToken);
        final ArrayList typedArrayList = transactAndReadException.createTypedArrayList((Parcelable$Creator)zzfh.CREATOR);
        transactAndReadException.recycle();
        return (List<zzfh>)typedArrayList;
    }
    
    @Override
    public final void zza(final long n, final String s, final String s2, final String s3) throws RemoteException {
        final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeLong(n);
        obtainAndWriteInterfaceToken.writeString(s);
        obtainAndWriteInterfaceToken.writeString(s2);
        obtainAndWriteInterfaceToken.writeString(s3);
        this.transactAndReadExceptionReturnVoid(10, obtainAndWriteInterfaceToken);
    }
    
    @Override
    public final void zza(final zzad zzad, final zzh zzh) throws RemoteException {
        final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
        zzs.zza(obtainAndWriteInterfaceToken, (Parcelable)zzad);
        zzs.zza(obtainAndWriteInterfaceToken, (Parcelable)zzh);
        this.transactAndReadExceptionReturnVoid(1, obtainAndWriteInterfaceToken);
    }
    
    @Override
    public final void zza(final zzad zzad, final String s, final String s2) throws RemoteException {
        final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
        zzs.zza(obtainAndWriteInterfaceToken, (Parcelable)zzad);
        obtainAndWriteInterfaceToken.writeString(s);
        obtainAndWriteInterfaceToken.writeString(s2);
        this.transactAndReadExceptionReturnVoid(5, obtainAndWriteInterfaceToken);
    }
    
    @Override
    public final void zza(final zzfh zzfh, final zzh zzh) throws RemoteException {
        final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
        zzs.zza(obtainAndWriteInterfaceToken, (Parcelable)zzfh);
        zzs.zza(obtainAndWriteInterfaceToken, (Parcelable)zzh);
        this.transactAndReadExceptionReturnVoid(2, obtainAndWriteInterfaceToken);
    }
    
    @Override
    public final void zza(final zzh zzh) throws RemoteException {
        final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
        zzs.zza(obtainAndWriteInterfaceToken, (Parcelable)zzh);
        this.transactAndReadExceptionReturnVoid(4, obtainAndWriteInterfaceToken);
    }
    
    @Override
    public final void zza(final zzl zzl, final zzh zzh) throws RemoteException {
        final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
        zzs.zza(obtainAndWriteInterfaceToken, (Parcelable)zzl);
        zzs.zza(obtainAndWriteInterfaceToken, (Parcelable)zzh);
        this.transactAndReadExceptionReturnVoid(12, obtainAndWriteInterfaceToken);
    }
    
    @Override
    public final byte[] zza(final zzad zzad, final String s) throws RemoteException {
        final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
        zzs.zza(obtainAndWriteInterfaceToken, (Parcelable)zzad);
        obtainAndWriteInterfaceToken.writeString(s);
        final Parcel transactAndReadException = this.transactAndReadException(9, obtainAndWriteInterfaceToken);
        final byte[] byteArray = transactAndReadException.createByteArray();
        transactAndReadException.recycle();
        return byteArray;
    }
    
    @Override
    public final void zzb(final zzh zzh) throws RemoteException {
        final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
        zzs.zza(obtainAndWriteInterfaceToken, (Parcelable)zzh);
        this.transactAndReadExceptionReturnVoid(6, obtainAndWriteInterfaceToken);
    }
    
    @Override
    public final void zzb(final zzl zzl) throws RemoteException {
        final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
        zzs.zza(obtainAndWriteInterfaceToken, (Parcelable)zzl);
        this.transactAndReadExceptionReturnVoid(13, obtainAndWriteInterfaceToken);
    }
    
    @Override
    public final String zzc(final zzh zzh) throws RemoteException {
        final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
        zzs.zza(obtainAndWriteInterfaceToken, (Parcelable)zzh);
        final Parcel transactAndReadException = this.transactAndReadException(11, obtainAndWriteInterfaceToken);
        final String string = transactAndReadException.readString();
        transactAndReadException.recycle();
        return string;
    }
    
    @Override
    public final void zzd(final zzh zzh) throws RemoteException {
        final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
        zzs.zza(obtainAndWriteInterfaceToken, (Parcelable)zzh);
        this.transactAndReadExceptionReturnVoid(18, obtainAndWriteInterfaceToken);
    }
    
    @Override
    public final List<zzl> zze(final String s, final String s2, final String s3) throws RemoteException {
        final Parcel obtainAndWriteInterfaceToken = this.obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(s);
        obtainAndWriteInterfaceToken.writeString(s2);
        obtainAndWriteInterfaceToken.writeString(s3);
        final Parcel transactAndReadException = this.transactAndReadException(17, obtainAndWriteInterfaceToken);
        final ArrayList typedArrayList = transactAndReadException.createTypedArrayList((Parcelable$Creator)zzl.CREATOR);
        transactAndReadException.recycle();
        return (List<zzl>)typedArrayList;
    }
}
