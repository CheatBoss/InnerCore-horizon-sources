package com.google.android.gms.common;

import javax.annotation.*;
import android.content.*;
import android.util.*;
import android.os.*;
import com.google.android.gms.dynamite.*;
import com.google.android.gms.common.internal.*;
import java.util.*;
import java.io.*;
import com.google.android.gms.dynamic.*;

@CheckReturnValue
final class GoogleCertificates
{
    private static volatile IGoogleCertificatesApi zzax;
    private static final Object zzay;
    private static Context zzaz;
    
    static {
        zzay = new Object();
    }
    
    static void init(final Context context) {
        synchronized (GoogleCertificates.class) {
            if (GoogleCertificates.zzaz == null) {
                if (context != null) {
                    GoogleCertificates.zzaz = context.getApplicationContext();
                }
            }
            else {
                Log.w("GoogleCertificates", "GoogleCertificates has been initialized already");
            }
        }
    }
    
    static zzg zza(final String s, final CertData certData, final boolean b) {
        String s2;
        try {
            zzc();
            Preconditions.checkNotNull(GoogleCertificates.zzaz);
            final GoogleCertificatesQuery googleCertificatesQuery = new GoogleCertificatesQuery(s, certData, b);
            try {
                if (GoogleCertificates.zzax.isGoogleOrPlatformSigned(googleCertificatesQuery, ObjectWrapper.wrap(GoogleCertificates.zzaz.getPackageManager()))) {
                    return zzg.zzg();
                }
                boolean b2 = true;
                if (b || !zza(s, certData, true).zzbl) {
                    b2 = false;
                }
                return zzg.zza(s, certData, b, b2);
            }
            catch (RemoteException ex) {
                Log.e("GoogleCertificates", "Failed to get Google certificates from remote", (Throwable)ex);
                s2 = "module call";
            }
        }
        catch (DynamiteModule.LoadingException ex) {
            s2 = "module init";
        }
        final RemoteException ex;
        return zzg.zza(s2, (Throwable)ex);
    }
    
    private static void zzc() throws DynamiteModule.LoadingException {
        if (GoogleCertificates.zzax != null) {
            return;
        }
        Preconditions.checkNotNull(GoogleCertificates.zzaz);
        synchronized (GoogleCertificates.zzay) {
            if (GoogleCertificates.zzax == null) {
                GoogleCertificates.zzax = IGoogleCertificatesApi.Stub.asInterface(DynamiteModule.load(GoogleCertificates.zzaz, DynamiteModule.PREFER_HIGHEST_OR_LOCAL_VERSION_NO_FORCE_STAGING, "com.google.android.gms.googlecertificates").instantiate("com.google.android.gms.common.GoogleCertificatesImpl"));
            }
        }
    }
    
    abstract static class CertData extends Stub
    {
        private int zzbc;
        
        protected CertData(final byte[] array) {
            Preconditions.checkArgument(array.length == 25);
            this.zzbc = Arrays.hashCode(array);
        }
        
        protected static byte[] zzd(final String s) {
            try {
                return s.getBytes("ISO-8859-1");
            }
            catch (UnsupportedEncodingException ex) {
                throw new AssertionError((Object)ex);
            }
        }
        
        public boolean equals(final Object o) {
            if (o != null) {
                if (!(o instanceof ICertData)) {
                    return false;
                }
                try {
                    final ICertData certData = (ICertData)o;
                    if (certData.getHashCode() != this.hashCode()) {
                        return false;
                    }
                    final IObjectWrapper bytesWrapped = certData.getBytesWrapped();
                    return bytesWrapped != null && Arrays.equals(this.getBytes(), (byte[])ObjectWrapper.unwrap(bytesWrapped));
                }
                catch (RemoteException ex) {
                    Log.e("GoogleCertificates", "Failed to get Google certificates from remote", (Throwable)ex);
                }
            }
            return false;
        }
        
        abstract byte[] getBytes();
        
        @Override
        public IObjectWrapper getBytesWrapped() {
            return ObjectWrapper.wrap(this.getBytes());
        }
        
        @Override
        public int getHashCode() {
            return this.hashCode();
        }
        
        public int hashCode() {
            return this.zzbc;
        }
    }
}
