package com.google.android.gms.common;

import javax.annotation.*;
import android.content.*;
import com.google.android.gms.common.internal.*;
import android.util.*;
import com.google.android.gms.common.wrappers.*;
import android.content.pm.*;

@CheckReturnValue
public class GoogleSignatureVerifier
{
    private static GoogleSignatureVerifier zzbv;
    private final Context mContext;
    
    private GoogleSignatureVerifier(final Context context) {
        this.mContext = context.getApplicationContext();
    }
    
    public static GoogleSignatureVerifier getInstance(final Context context) {
        Preconditions.checkNotNull(context);
        synchronized (GoogleSignatureVerifier.class) {
            if (GoogleSignatureVerifier.zzbv == null) {
                GoogleCertificates.init(context);
                GoogleSignatureVerifier.zzbv = new GoogleSignatureVerifier(context);
            }
            return GoogleSignatureVerifier.zzbv;
        }
    }
    
    private static GoogleCertificates.CertData zza(final PackageInfo packageInfo, final GoogleCertificates.CertData... array) {
        if (packageInfo.signatures == null) {
            return null;
        }
        if (packageInfo.signatures.length != 1) {
            Log.w("GoogleSignatureVerifier", "Package has more than one signature.");
            return null;
        }
        final Signature[] signatures = packageInfo.signatures;
        int i = 0;
        final zzb zzb = new zzb(signatures[0].toByteArray());
        while (i < array.length) {
            if (array[i].equals(zzb)) {
                return array[i];
            }
            ++i;
        }
        return null;
    }
    
    private final zzg zza(final PackageInfo packageInfo) {
        final boolean honorsDebugCertificates = GooglePlayServicesUtilLight.honorsDebugCertificates(this.mContext);
        String s;
        if (packageInfo == null) {
            s = "null pkg";
        }
        else if (packageInfo.signatures.length != 1) {
            s = "single cert required";
        }
        else {
            final zzb zzb = new zzb(packageInfo.signatures[0].toByteArray());
            final String packageName = packageInfo.packageName;
            final zzg zza = GoogleCertificates.zza(packageName, (GoogleCertificates.CertData)zzb, honorsDebugCertificates);
            if (!zza.zzbl || packageInfo.applicationInfo == null || (packageInfo.applicationInfo.flags & 0x2) == 0x0 || (honorsDebugCertificates && !GoogleCertificates.zza(packageName, (GoogleCertificates.CertData)zzb, false).zzbl)) {
                return zza;
            }
            s = "debuggable release cert app rejected";
        }
        return zzg.zze(s);
    }
    
    private final zzg zzb(int i) {
        final String[] packagesForUid = Wrappers.packageManager(this.mContext).getPackagesForUid(i);
        if (packagesForUid != null && packagesForUid.length != 0) {
            zzg zzf = null;
            int length;
            for (length = packagesForUid.length, i = 0; i < length; ++i) {
                zzf = this.zzf(packagesForUid[i]);
                if (zzf.zzbl) {
                    return zzf;
                }
            }
            return zzf;
        }
        return zzg.zze("no pkgs");
    }
    
    private final zzg zzf(String s) {
        try {
            return this.zza(Wrappers.packageManager(this.mContext).getPackageInfo(s, 64));
        }
        catch (PackageManager$NameNotFoundException ex) {
            s = String.valueOf(s);
            if (s.length() != 0) {
                s = "no pkg ".concat(s);
            }
            else {
                s = new String("no pkg ");
            }
            return zzg.zze(s);
        }
    }
    
    public boolean isGooglePublicSignedPackage(final PackageInfo packageInfo) {
        if (packageInfo == null) {
            return false;
        }
        if (this.isGooglePublicSignedPackage(packageInfo, false)) {
            return true;
        }
        if (this.isGooglePublicSignedPackage(packageInfo, true)) {
            if (GooglePlayServicesUtilLight.honorsDebugCertificates(this.mContext)) {
                return true;
            }
            Log.w("GoogleSignatureVerifier", "Test-keys aren't accepted on this build.");
        }
        return false;
    }
    
    public boolean isGooglePublicSignedPackage(final PackageInfo packageInfo, final boolean b) {
        if (packageInfo != null && packageInfo.signatures != null) {
            GoogleCertificates.CertData[] zzbg;
            if (b) {
                zzbg = zzd.zzbg;
            }
            else {
                zzbg = new GoogleCertificates.CertData[] { zzd.zzbg[0] };
            }
            if (zza(packageInfo, zzbg) != null) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isUidGoogleSigned(final int n) {
        final zzg zzb = this.zzb(n);
        zzb.zzi();
        return zzb.zzbl;
    }
}
