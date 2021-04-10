package com.google.firebase.iid;

import java.io.*;
import java.util.concurrent.*;
import android.util.*;
import android.os.*;
import com.google.android.gms.tasks.*;
import com.google.firebase.events.*;
import com.google.firebase.*;
import android.content.*;
import android.content.pm.*;

public class FirebaseInstanceId
{
    private static final long zzaf;
    private static zzav zzag;
    private static ScheduledThreadPoolExecutor zzah;
    private final Executor zzai;
    private final FirebaseApp zzaj;
    private final zzam zzak;
    private MessagingChannel zzal;
    private final zzap zzam;
    private final zzaz zzan;
    private boolean zzao;
    private final zza zzap;
    
    static {
        zzaf = TimeUnit.HOURS.toSeconds(8L);
    }
    
    FirebaseInstanceId(final FirebaseApp firebaseApp, final Subscriber subscriber) {
        this(firebaseApp, new zzam(firebaseApp.getApplicationContext()), zzi.zze(), zzi.zze(), subscriber);
    }
    
    private FirebaseInstanceId(final FirebaseApp zzaj, final zzam zzak, final Executor executor, final Executor zzai, final Subscriber subscriber) {
        this.zzam = new zzap();
        this.zzao = false;
        if (com.google.firebase.iid.zzam.zza(zzaj) != null) {
            synchronized (FirebaseInstanceId.class) {
                if (FirebaseInstanceId.zzag == null) {
                    FirebaseInstanceId.zzag = new zzav(zzaj.getApplicationContext());
                }
                // monitorexit(FirebaseInstanceId.class)
                this.zzaj = zzaj;
                this.zzak = zzak;
                if (this.zzal == null) {
                    final MessagingChannel messagingChannel = zzaj.get(MessagingChannel.class);
                    MessagingChannel zzal;
                    if (messagingChannel != null && messagingChannel.isAvailable()) {
                        zzal = messagingChannel;
                    }
                    else {
                        zzal = new zzq(zzaj, zzak, executor);
                    }
                    this.zzal = zzal;
                }
                this.zzal = this.zzal;
                this.zzai = zzai;
                this.zzan = new zzaz(FirebaseInstanceId.zzag);
                final zza zzap = new zza(subscriber);
                this.zzap = zzap;
                if (zzap.isEnabled()) {
                    this.zzf();
                }
                return;
            }
        }
        throw new IllegalStateException("FirebaseInstanceId failed to initialize, FirebaseApp is missing project ID");
    }
    
    public static FirebaseInstanceId getInstance() {
        return getInstance(FirebaseApp.getInstance());
    }
    
    public static FirebaseInstanceId getInstance(final FirebaseApp firebaseApp) {
        synchronized (FirebaseInstanceId.class) {
            return firebaseApp.get(FirebaseInstanceId.class);
        }
    }
    
    private final void startSync() {
        synchronized (this) {
            if (!this.zzao) {
                this.zza(0L);
            }
        }
    }
    
    private final Task<InstanceIdResult> zza(final String s, final String s2) {
        final String zzd = zzd(s2);
        final TaskCompletionSource<InstanceIdResult> taskCompletionSource = new TaskCompletionSource<InstanceIdResult>();
        this.zzai.execute(new zzm(this, s, s2, taskCompletionSource, zzd));
        return taskCompletionSource.getTask();
    }
    
    private final <T> T zza(final Task<T> task) throws IOException {
        try {
            return Tasks.await(task, 30000L, TimeUnit.MILLISECONDS);
        }
        catch (InterruptedException | TimeoutException ex2) {
            throw new IOException("SERVICE_NOT_AVAILABLE");
        }
        catch (ExecutionException ex) {
            final Throwable cause = ex.getCause();
            if (cause instanceof IOException) {
                if ("INSTANCE_ID_RESET".equals(cause.getMessage())) {
                    this.zzl();
                }
                throw (IOException)cause;
            }
            if (cause instanceof RuntimeException) {
                throw (RuntimeException)cause;
            }
            throw new IOException(ex);
        }
    }
    
    static void zza(final Runnable runnable, final long n) {
        synchronized (FirebaseInstanceId.class) {
            if (FirebaseInstanceId.zzah == null) {
                FirebaseInstanceId.zzah = new ScheduledThreadPoolExecutor(1);
            }
            FirebaseInstanceId.zzah.schedule(runnable, n, TimeUnit.SECONDS);
        }
    }
    
    private static zzaw zzb(final String s, final String s2) {
        return FirebaseInstanceId.zzag.zzb("", s, s2);
    }
    
    private static String zzd(final String s) {
        if (!s.isEmpty() && !s.equalsIgnoreCase("fcm") && !s.equalsIgnoreCase("gcm")) {
            return s;
        }
        return "*";
    }
    
    private final void zzf() {
        final zzaw zzi = this.zzi();
        if (!this.zzn() || zzi == null || zzi.zzj(this.zzak.zzac()) || this.zzan.zzap()) {
            this.startSync();
        }
    }
    
    private static String zzh() {
        return zzam.zza(FirebaseInstanceId.zzag.zzg("").getKeyPair());
    }
    
    static boolean zzk() {
        return Log.isLoggable("FirebaseInstanceId", 3) || (Build$VERSION.SDK_INT == 23 && Log.isLoggable("FirebaseInstanceId", 3));
    }
    
    public String getToken(final String s, final String s2) throws IOException {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            return this.zza(this.zza(s, s2)).getToken();
        }
        throw new IOException("MAIN_THREAD");
    }
    
    final void zza(final long n) {
        synchronized (this) {
            zza(new zzax(this, this.zzak, this.zzan, Math.min(Math.max(30L, n << 1), FirebaseInstanceId.zzaf)), n);
            this.zzao = true;
        }
    }
    
    final void zza(final boolean zzao) {
        synchronized (this) {
            this.zzao = zzao;
        }
    }
    
    final void zzb(final String s) throws IOException {
        final zzaw zzi = this.zzi();
        if (zzi != null && !zzi.zzj(this.zzak.zzac())) {
            this.zza(this.zzal.subscribeToTopic(zzh(), zzi.zzbn, s));
            return;
        }
        throw new IOException("token not available");
    }
    
    final void zzc(final String s) throws IOException {
        final zzaw zzi = this.zzi();
        if (zzi != null && !zzi.zzj(this.zzak.zzac())) {
            this.zza(this.zzal.unsubscribeFromTopic(zzh(), zzi.zzbn, s));
            return;
        }
        throw new IOException("token not available");
    }
    
    final FirebaseApp zzg() {
        return this.zzaj;
    }
    
    final zzaw zzi() {
        return zzb(com.google.firebase.iid.zzam.zza(this.zzaj), "*");
    }
    
    final String zzj() throws IOException {
        return this.getToken(com.google.firebase.iid.zzam.zza(this.zzaj), "*");
    }
    
    final void zzl() {
        synchronized (this) {
            FirebaseInstanceId.zzag.zzak();
            if (this.zzap.isEnabled()) {
                this.startSync();
            }
        }
    }
    
    final boolean zzm() {
        return this.zzal.isAvailable();
    }
    
    final boolean zzn() {
        return this.zzal.isChannelBuilt();
    }
    
    final void zzo() throws IOException {
        this.zza(this.zzal.buildChannel(zzh(), zzaw.zza(this.zzi())));
    }
    
    final void zzp() {
        FirebaseInstanceId.zzag.zzh("");
        this.startSync();
    }
    
    private final class zza
    {
        private final boolean zzaw;
        private final Subscriber zzax;
        private EventHandler<DataCollectionDefaultChange> zzay;
        private Boolean zzaz;
        
        zza(final Subscriber zzax) {
            this.zzax = zzax;
            this.zzaw = this.zzt();
            final Boolean zzs = this.zzs();
            this.zzaz = zzs;
            if (zzs == null && this.zzaw) {
                zzax.subscribe(DataCollectionDefaultChange.class, this.zzay = (EventHandler<DataCollectionDefaultChange>)new zzp(this));
            }
        }
        
        private final Boolean zzs() {
            final Context applicationContext = FirebaseInstanceId.this.zzaj.getApplicationContext();
            final SharedPreferences sharedPreferences = applicationContext.getSharedPreferences("com.google.firebase.messaging", 0);
            if (sharedPreferences.contains("auto_init")) {
                return sharedPreferences.getBoolean("auto_init", false);
            }
            try {
                final PackageManager packageManager = applicationContext.getPackageManager();
                if (packageManager != null) {
                    final ApplicationInfo applicationInfo = packageManager.getApplicationInfo(applicationContext.getPackageName(), 128);
                    if (applicationInfo != null && applicationInfo.metaData != null && applicationInfo.metaData.containsKey("firebase_messaging_auto_init_enabled")) {
                        return applicationInfo.metaData.getBoolean("firebase_messaging_auto_init_enabled");
                    }
                }
            }
            catch (PackageManager$NameNotFoundException ex) {}
            return null;
        }
        
        private final boolean zzt() {
            try {
                Class.forName("com.google.firebase.messaging.FirebaseMessaging");
                return true;
            }
            catch (ClassNotFoundException ex) {
                final Context applicationContext = FirebaseInstanceId.this.zzaj.getApplicationContext();
                final Intent intent = new Intent("com.google.firebase.MESSAGING_EVENT");
                intent.setPackage(applicationContext.getPackageName());
                final ResolveInfo resolveService = applicationContext.getPackageManager().resolveService(intent, 0);
                return resolveService != null && resolveService.serviceInfo != null;
            }
        }
        
        final boolean isEnabled() {
            synchronized (this) {
                if (this.zzaz != null) {
                    return this.zzaz;
                }
                return this.zzaw && FirebaseInstanceId.this.zzaj.isDataCollectionDefaultEnabled();
            }
        }
    }
}
