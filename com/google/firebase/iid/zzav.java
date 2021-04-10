package com.google.firebase.iid;

import android.support.v4.util.*;
import android.support.v4.content.*;
import android.util.*;
import java.io.*;
import android.content.*;
import java.util.*;

final class zzav
{
    private final SharedPreferences zzcz;
    private final zzx zzda;
    private final Map<String, zzy> zzdb;
    private final Context zzv;
    
    public zzav(final Context context) {
        this(context, new zzx());
    }
    
    private zzav(final Context zzv, final zzx zzda) {
        this.zzdb = new ArrayMap<String, zzy>();
        this.zzv = zzv;
        this.zzcz = zzv.getSharedPreferences("com.google.android.gms.appid", 0);
        this.zzda = zzda;
        final File file = new File(ContextCompat.getNoBackupFilesDir(this.zzv), "com.google.android.gms.appid-no-backup");
        if (!file.exists()) {
            try {
                if (file.createNewFile() && !this.isEmpty()) {
                    Log.i("FirebaseInstanceId", "App restored, clearing state");
                    this.zzak();
                    FirebaseInstanceId.getInstance().zzl();
                }
            }
            catch (IOException ex) {
                if (Log.isLoggable("FirebaseInstanceId", 3)) {
                    final String value = String.valueOf(ex.getMessage());
                    String concat;
                    if (value.length() != 0) {
                        concat = "Error creating file in no backup dir: ".concat(value);
                    }
                    else {
                        concat = new String("Error creating file in no backup dir: ");
                    }
                    Log.d("FirebaseInstanceId", concat);
                }
            }
        }
    }
    
    private final boolean isEmpty() {
        synchronized (this) {
            return this.zzcz.getAll().isEmpty();
        }
    }
    
    private static String zza(final String s, final String s2, final String s3) {
        final StringBuilder sb = new StringBuilder(String.valueOf(s).length() + 4 + String.valueOf(s2).length() + String.valueOf(s3).length());
        sb.append(s);
        sb.append("|T|");
        sb.append(s2);
        sb.append("|");
        sb.append(s3);
        return sb.toString();
    }
    
    static String zzd(final String s, final String s2) {
        final StringBuilder sb = new StringBuilder(String.valueOf(s).length() + 3 + String.valueOf(s2).length());
        sb.append(s);
        sb.append("|S|");
        sb.append(s2);
        return sb.toString();
    }
    
    public final void zza(final String s, final String s2, final String s3, String zza, final String s4) {
        synchronized (this) {
            zza = zzaw.zza(zza, s4, System.currentTimeMillis());
            if (zza == null) {
                return;
            }
            final SharedPreferences$Editor edit = this.zzcz.edit();
            edit.putString(zza(s, s2, s3), zza);
            edit.commit();
        }
    }
    
    public final String zzaj() {
        synchronized (this) {
            return this.zzcz.getString("topic_operaion_queue", "");
        }
    }
    
    public final void zzak() {
        synchronized (this) {
            this.zzdb.clear();
            zzx.zza(this.zzv);
            this.zzcz.edit().clear().commit();
        }
    }
    
    public final zzaw zzb(final String s, final String s2, final String s3) {
        synchronized (this) {
            return zzaw.zzi(this.zzcz.getString(zza(s, s2, s3), (String)null));
        }
    }
    
    public final void zzf(final String s) {
        synchronized (this) {
            this.zzcz.edit().putString("topic_operaion_queue", s).apply();
        }
    }
    
    public final zzy zzg(final String s) {
        synchronized (this) {
            final zzy zzy = this.zzdb.get(s);
            if (zzy != null) {
                return zzy;
            }
            zzy zzy2;
            try {
                zzy2 = this.zzda.zzb(this.zzv, s);
            }
            catch (zzz zzz) {
                Log.w("FirebaseInstanceId", "Stored data is corrupt, generating new identity");
                FirebaseInstanceId.getInstance().zzl();
                zzy2 = this.zzda.zzc(this.zzv, s);
            }
            this.zzdb.put(s, zzy2);
            return zzy2;
        }
    }
    
    public final void zzh(String concat) {
        synchronized (this) {
            concat = String.valueOf(concat).concat("|T|");
            final SharedPreferences$Editor edit = this.zzcz.edit();
            for (final String s : this.zzcz.getAll().keySet()) {
                if (s.startsWith(concat)) {
                    edit.remove(s);
                }
            }
            edit.commit();
        }
    }
}
