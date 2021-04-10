package com.google.firebase.iid;

import java.util.*;
import java.nio.channels.*;
import android.content.*;
import com.google.android.gms.internal.firebase_messaging.*;
import android.support.v4.content.*;
import android.util.*;
import java.security.spec.*;
import java.security.*;
import android.text.*;
import java.io.*;

final class zzx
{
    private final zzy zza(Context context, String channel, final zzy zzy, final boolean b) {
        if (Log.isLoggable("FirebaseInstanceId", 3)) {
            Log.d("FirebaseInstanceId", "Writing key to properties file");
        }
        final Properties properties = new Properties();
        properties.setProperty("pub", zzy.zzu());
        properties.setProperty("pri", zzy.zzv());
        properties.setProperty("cre", String.valueOf(zzy.zzbp));
        final File zzf = zzf(context, channel);
        try {
            zzf.createNewFile();
            context = (Context)new RandomAccessFile(zzf, "rw");
            try {
                channel = (String)((RandomAccessFile)context).getChannel();
                try {
                    ((FileChannel)channel).lock();
                    if (b && ((FileChannel)channel).size() > 0L) {
                        try {
                            ((FileChannel)channel).position(0L);
                            final zzy zza = zza((FileChannel)channel);
                            if (channel != null) {
                                zza(null, (FileChannel)channel);
                            }
                            zza(null, (RandomAccessFile)context);
                            return zza;
                        }
                        catch (IOException | zzz ex2) {
                            final zzz zzz2;
                            final zzz zzz = zzz2;
                            if (Log.isLoggable("FirebaseInstanceId", 3)) {
                                final String value = String.valueOf(zzz);
                                final StringBuilder sb = new StringBuilder(String.valueOf(value).length() + 64);
                                sb.append("Tried reading key pair before writing new one, but failed with: ");
                                sb.append(value);
                                Log.d("FirebaseInstanceId", sb.toString());
                            }
                        }
                    }
                    ((FileChannel)channel).position(0L);
                    properties.store(Channels.newOutputStream((WritableByteChannel)channel), null);
                    if (channel != null) {
                        zza(null, (FileChannel)channel);
                    }
                    zza(null, (RandomAccessFile)context);
                    return zzy;
                }
                finally {
                    try {}
                    finally {
                        if (channel != null) {
                            zza((Throwable)zzy, (FileChannel)channel);
                        }
                    }
                }
            }
            finally {
                try {}
                finally {
                    zza((Throwable)channel, (RandomAccessFile)context);
                }
            }
        }
        catch (IOException ex) {
            final String value2 = String.valueOf(ex);
            final StringBuilder sb2 = new StringBuilder(String.valueOf(value2).length() + 21);
            sb2.append("Failed to write key: ");
            sb2.append(value2);
            Log.w("FirebaseInstanceId", sb2.toString());
            return null;
        }
    }
    
    private static zzy zza(final SharedPreferences sharedPreferences, final String s) throws zzz {
        final String zzd = zzav.zzd(s, "|P|");
        zzy zzy = null;
        final String string = sharedPreferences.getString(zzd, (String)null);
        final String string2 = sharedPreferences.getString(zzav.zzd(s, "|K|"), (String)null);
        if (string != null) {
            if (string2 == null) {
                return null;
            }
            zzy = new zzy(zzc(string, string2), zzb(sharedPreferences, s));
        }
        return zzy;
    }
    
    private final zzy zza(File file) throws zzz, IOException {
        file = (File)new FileInputStream(file);
        try {
            final FileChannel channel = ((FileInputStream)file).getChannel();
            try {
                channel.lock(0L, Long.MAX_VALUE, true);
                final zzy zza = zza(channel);
                if (channel != null) {
                    zza(null, channel);
                }
                zza(null, (FileInputStream)file);
                return zza;
            }
            finally {
                try {}
                finally {
                    if (channel != null) {
                        final Throwable t;
                        zza(t, channel);
                    }
                }
            }
        }
        finally {
            try {}
            finally {
                final Throwable t2;
                zza(t2, (FileInputStream)file);
            }
        }
    }
    
    private static zzy zza(final FileChannel fileChannel) throws zzz, IOException {
        final Properties properties = new Properties();
        properties.load(Channels.newInputStream(fileChannel));
        final String property = properties.getProperty("pub");
        final String property2 = properties.getProperty("pri");
        if (property != null && property2 != null) {
            final KeyPair zzc = zzc(property, property2);
            try {
                return new zzy(zzc, Long.parseLong(properties.getProperty("cre")));
            }
            catch (NumberFormatException ex) {
                throw new zzz(ex);
            }
        }
        throw new zzz("Invalid properties file");
    }
    
    static void zza(final Context context) {
        final File[] listFiles = zzb(context).listFiles();
        for (int length = listFiles.length, i = 0; i < length; ++i) {
            final File file = listFiles[i];
            if (file.getName().startsWith("com.google.InstanceId")) {
                file.delete();
            }
        }
    }
    
    private final void zza(Context sharedPreferences, final String s, final zzy zzy) {
        sharedPreferences = (Context)sharedPreferences.getSharedPreferences("com.google.android.gms.appid", 0);
        try {
            if (zzy.equals(zza((SharedPreferences)sharedPreferences, s))) {
                return;
            }
        }
        catch (zzz zzz) {}
        if (Log.isLoggable("FirebaseInstanceId", 3)) {
            Log.d("FirebaseInstanceId", "Writing key to shared preferences");
        }
        final SharedPreferences$Editor edit = ((SharedPreferences)sharedPreferences).edit();
        edit.putString(zzav.zzd(s, "|P|"), zzy.zzu());
        edit.putString(zzav.zzd(s, "|K|"), zzy.zzv());
        edit.putString(zzav.zzd(s, "cre"), String.valueOf(zzy.zzbp));
        edit.commit();
    }
    
    private static /* synthetic */ void zza(final Throwable t, final FileInputStream fileInputStream) {
        if (t != null) {
            try {
                fileInputStream.close();
                return;
            }
            finally {
                final Throwable t2;
                zza.zza(t, t2);
                return;
            }
        }
        fileInputStream.close();
    }
    
    private static /* synthetic */ void zza(final Throwable t, final RandomAccessFile randomAccessFile) {
        if (t != null) {
            try {
                randomAccessFile.close();
                return;
            }
            finally {
                final Throwable t2;
                zza.zza(t, t2);
                return;
            }
        }
        randomAccessFile.close();
    }
    
    private static /* synthetic */ void zza(final Throwable t, final FileChannel fileChannel) {
        if (t != null) {
            try {
                fileChannel.close();
                return;
            }
            finally {
                final Throwable t2;
                zza.zza(t, t2);
                return;
            }
        }
        fileChannel.close();
    }
    
    private static long zzb(final SharedPreferences sharedPreferences, final String s) {
        final String string = sharedPreferences.getString(zzav.zzd(s, "cre"), (String)null);
        if (string != null) {
            try {
                return Long.parseLong(string);
            }
            catch (NumberFormatException ex) {}
        }
        return 0L;
    }
    
    private static File zzb(final Context context) {
        final File noBackupFilesDir = ContextCompat.getNoBackupFilesDir(context);
        if (noBackupFilesDir != null && noBackupFilesDir.isDirectory()) {
            return noBackupFilesDir;
        }
        Log.w("FirebaseInstanceId", "noBackupFilesDir doesn't exist, using regular files directory instead");
        return context.getFilesDir();
    }
    
    private static KeyPair zzc(final String s, String value) throws zzz {
        try {
            final byte[] decode = Base64.decode(s, 8);
            final byte[] decode2 = Base64.decode(value, 8);
            try {
                final KeyFactory instance = KeyFactory.getInstance("RSA");
                return new KeyPair(instance.generatePublic(new X509EncodedKeySpec(decode)), instance.generatePrivate(new PKCS8EncodedKeySpec(decode2)));
            }
            catch (InvalidKeySpecException | NoSuchAlgorithmException ex4) {
                final NoSuchAlgorithmException ex2;
                final NoSuchAlgorithmException ex = ex2;
                value = String.valueOf(ex);
                final StringBuilder sb = new StringBuilder(String.valueOf(value).length() + 19);
                sb.append("Invalid key stored ");
                sb.append(value);
                Log.w("FirebaseInstanceId", sb.toString());
                throw new zzz(ex);
            }
        }
        catch (IllegalArgumentException ex3) {
            throw new zzz(ex3);
        }
    }
    
    private final zzy zzd(final Context context, final String s) throws zzz {
        zzy zze = null;
        try {
            zze = this.zze(context, s);
            if (zze != null) {
                this.zza(context, s, zze);
                return zze;
            }
            zze = null;
        }
        catch (zzz zzz) {}
        try {
            final zzy zza = zza(context.getSharedPreferences("com.google.android.gms.appid", 0), s);
            if (zza != null) {
                this.zza(context, s, zza, false);
                return zza;
            }
        }
        catch (zzz zzz2) {}
        if (zze == null) {
            return null;
        }
        throw zze;
    }
    
    private final zzy zze(Context zzf, String s) throws zzz {
        zzf = (Context)zzf(zzf, s);
        if (!((File)zzf).exists()) {
            return null;
        }
        try {
            return this.zza((File)zzf);
        }
        catch (zzz | IOException zzz) {
            final IOException ex2;
            final IOException ex = ex2;
            if (Log.isLoggable("FirebaseInstanceId", 3)) {
                s = String.valueOf(ex);
                final StringBuilder sb = new StringBuilder(String.valueOf(s).length() + 40);
                sb.append("Failed to read key from file, retrying: ");
                sb.append(s);
                Log.d("FirebaseInstanceId", sb.toString());
            }
            try {
                return this.zza((File)zzf);
            }
            catch (IOException ex3) {
                s = String.valueOf(ex3);
                final StringBuilder sb2 = new StringBuilder(String.valueOf(s).length() + 45);
                sb2.append("IID file exists, but failed to read from it: ");
                sb2.append(s);
                Log.w("FirebaseInstanceId", sb2.toString());
                throw new zzz(ex3);
            }
        }
    }
    
    private static File zzf(final Context context, String s) {
        Label_0072: {
            if (TextUtils.isEmpty((CharSequence)s)) {
                s = "com.google.InstanceId.properties";
                break Label_0072;
            }
            try {
                s = Base64.encodeToString(s.getBytes("UTF-8"), 11);
                final StringBuilder sb = new StringBuilder(String.valueOf(s).length() + 33);
                sb.append("com.google.InstanceId_");
                sb.append(s);
                sb.append(".properties");
                s = sb.toString();
                return new File(zzb(context), s);
            }
            catch (UnsupportedEncodingException ex) {
                throw new AssertionError((Object)ex);
            }
        }
    }
    
    final zzy zzb(final Context context, final String s) throws zzz {
        final zzy zzd = this.zzd(context, s);
        if (zzd != null) {
            return zzd;
        }
        return this.zzc(context, s);
    }
    
    final zzy zzc(final Context context, final String s) {
        final zzy zzy = new zzy(com.google.firebase.iid.zza.zzb(), System.currentTimeMillis());
        final zzy zza = this.zza(context, s, zzy, true);
        if (zza != null && !zza.equals(zzy)) {
            if (Log.isLoggable("FirebaseInstanceId", 3)) {
                Log.d("FirebaseInstanceId", "Loaded key after generating new one, using loaded one");
            }
            return zza;
        }
        if (Log.isLoggable("FirebaseInstanceId", 3)) {
            Log.d("FirebaseInstanceId", "Generated new key");
        }
        this.zza(context, s, zzy);
        return zzy;
    }
}
