package com.google.android.gms.common;

import com.google.android.gms.common.util.*;

final class zzi extends zzg
{
    private final String packageName;
    private final GoogleCertificates.CertData zzbn;
    private final boolean zzbo;
    private final boolean zzbp;
    
    private zzi(final String packageName, final GoogleCertificates.CertData zzbn, final boolean zzbo, final boolean zzbp) {
        super(false, null, null);
        this.packageName = packageName;
        this.zzbn = zzbn;
        this.zzbo = zzbo;
        this.zzbp = zzbp;
    }
    
    @Override
    final String getErrorMessage() {
        String s;
        if (this.zzbp) {
            s = "debug cert rejected";
        }
        else {
            s = "not whitelisted";
        }
        final String packageName = this.packageName;
        final String bytesToStringLowercase = Hex.bytesToStringLowercase(AndroidUtilsLight.getMessageDigest("SHA-1").digest(this.zzbn.getBytes()));
        final boolean zzbo = this.zzbo;
        final StringBuilder sb = new StringBuilder(s.length() + 44 + String.valueOf(packageName).length() + String.valueOf(bytesToStringLowercase).length());
        sb.append(s);
        sb.append(": pkg=");
        sb.append(packageName);
        sb.append(", sha1=");
        sb.append(bytesToStringLowercase);
        sb.append(", atk=");
        sb.append(zzbo);
        sb.append(", ver=12451009.false");
        return sb.toString();
    }
}
