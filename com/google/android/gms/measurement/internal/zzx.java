package com.google.android.gms.measurement.internal;

import android.content.*;
import com.google.android.gms.common.util.*;
import java.util.concurrent.*;
import java.util.*;
import android.content.pm.*;

public final class zzx extends zzcp
{
    private long zzahz;
    private String zzaia;
    private Boolean zzaib;
    
    zzx(final zzbt zzbt) {
        super(zzbt);
    }
    
    @Override
    protected final boolean zzgt() {
        final Calendar instance = Calendar.getInstance();
        this.zzahz = TimeUnit.MINUTES.convert(instance.get(15) + instance.get(16), TimeUnit.MILLISECONDS);
        final Locale default1 = Locale.getDefault();
        final String lowerCase = default1.getLanguage().toLowerCase(Locale.ENGLISH);
        final String lowerCase2 = default1.getCountry().toLowerCase(Locale.ENGLISH);
        final StringBuilder sb = new StringBuilder(String.valueOf(lowerCase).length() + 1 + String.valueOf(lowerCase2).length());
        sb.append(lowerCase);
        sb.append("-");
        sb.append(lowerCase2);
        this.zzaia = sb.toString();
        return false;
    }
    
    public final long zzis() {
        this.zzcl();
        return this.zzahz;
    }
    
    public final String zzit() {
        this.zzcl();
        return this.zzaia;
    }
    
    public final boolean zzl(final Context context) {
        if (this.zzaib == null) {
            this.zzgr();
            this.zzaib = false;
            try {
                final PackageManager packageManager = context.getPackageManager();
                if (packageManager != null) {
                    packageManager.getPackageInfo("com.google.android.gms", 128);
                    this.zzaib = true;
                }
            }
            catch (PackageManager$NameNotFoundException ex) {}
        }
        return this.zzaib;
    }
}
