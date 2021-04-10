package com.google.android.gms.ads.identifier;

import javax.annotation.*;
import com.google.android.gms.common.internal.*;
import java.io.*;
import com.google.android.gms.common.*;
import com.google.android.gms.common.stats.*;
import android.content.*;
import android.content.pm.*;
import com.google.android.gms.internal.ads_identifier.*;
import java.util.*;
import android.util.*;
import android.os.*;
import java.lang.ref.*;
import java.util.concurrent.*;

@ParametersAreNonnullByDefault
public class AdvertisingIdClient
{
    private final Context mContext;
    private BlockingServiceConnection zze;
    private zze zzf;
    private boolean zzg;
    private final Object zzh;
    private zza zzi;
    private final boolean zzj;
    private final long zzk;
    
    private AdvertisingIdClient(final Context context, final long zzk, final boolean b, final boolean zzj) {
        this.zzh = new Object();
        Preconditions.checkNotNull(context);
        Context applicationContext = context;
        if (b) {
            applicationContext = context.getApplicationContext();
            if (applicationContext == null) {
                applicationContext = context;
            }
        }
        this.mContext = applicationContext;
        this.zzg = false;
        this.zzk = zzk;
        this.zzj = zzj;
    }
    
    public static Info getAdvertisingIdInfo(Context context) throws IOException, IllegalStateException, GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException {
        final zzb zzb = new zzb(context);
        final boolean boolean1 = zzb.getBoolean("gads:ad_id_app_context:enabled", false);
        final float float1 = zzb.getFloat("gads:ad_id_app_context:ping_ratio", 0.0f);
        final String string = zzb.getString("gads:ad_id_use_shared_preference:experiment_id", "");
        context = (Context)new AdvertisingIdClient(context, -1L, boolean1, zzb.getBoolean("gads:ad_id_use_persistent_service:enabled", false));
        try {
            final long elapsedRealtime = SystemClock.elapsedRealtime();
            ((AdvertisingIdClient)context).zza(false);
            final Info info = ((AdvertisingIdClient)context).getInfo();
            ((AdvertisingIdClient)context).zza(info, boolean1, float1, SystemClock.elapsedRealtime() - elapsedRealtime, string, null);
            ((AdvertisingIdClient)context).finish();
            return info;
        }
        finally {
            try {
                final Throwable t;
                ((AdvertisingIdClient)context).zza(null, boolean1, float1, -1L, string, t);
            }
            finally {
                ((AdvertisingIdClient)context).finish();
            }
        }
    }
    
    public static void setShouldSkipGmsCoreVersionCheck(final boolean b) {
    }
    
    private static BlockingServiceConnection zza(final Context context, final boolean b) throws IOException, GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException {
        try {
            context.getPackageManager().getPackageInfo("com.android.vending", 0);
            final int googlePlayServicesAvailable = GoogleApiAvailabilityLight.getInstance().isGooglePlayServicesAvailable(context, 12451000);
            if (googlePlayServicesAvailable != 0 && googlePlayServicesAvailable != 2) {
                throw new IOException("Google Play services not available");
            }
            String s;
            if (b) {
                s = "com.google.android.gms.ads.identifier.service.PERSISTENT_START";
            }
            else {
                s = "com.google.android.gms.ads.identifier.service.START";
            }
            final BlockingServiceConnection blockingServiceConnection = new BlockingServiceConnection();
            final Intent intent = new Intent(s);
            intent.setPackage("com.google.android.gms");
            try {
                if (ConnectionTracker.getInstance().bindService(context, intent, (ServiceConnection)blockingServiceConnection, 1)) {
                    return blockingServiceConnection;
                }
                throw new IOException("Connection failure");
            }
            finally {
                final Throwable t;
                throw new IOException(t);
            }
        }
        catch (PackageManager$NameNotFoundException ex) {
            throw new GooglePlayServicesNotAvailableException(9);
        }
    }
    
    private static zze zza(final Context context, final BlockingServiceConnection blockingServiceConnection) throws IOException {
        try {
            return zzf.zza(blockingServiceConnection.getServiceWithTimeout(10000L, TimeUnit.MILLISECONDS));
        }
        catch (InterruptedException ex) {
            throw new IOException("Interrupted exception");
        }
        finally {
            final Throwable t;
            throw new IOException(t);
        }
    }
    
    private final void zza() {
        synchronized (this.zzh) {
            if (this.zzi != null) {
                this.zzi.zzo.countDown();
                try {
                    this.zzi.join();
                }
                catch (InterruptedException ex) {}
            }
            if (this.zzk > 0L) {
                this.zzi = new zza(this, this.zzk);
            }
        }
    }
    
    private final void zza(final boolean b) throws IOException, IllegalStateException, GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException {
        Preconditions.checkNotMainThread("Calling this from your main thread can lead to deadlock");
        synchronized (this) {
            if (this.zzg) {
                this.finish();
            }
            final BlockingServiceConnection zza = zza(this.mContext, this.zzj);
            this.zze = zza;
            this.zzf = zza(this.mContext, zza);
            this.zzg = true;
            if (b) {
                this.zza();
            }
        }
    }
    
    private final boolean zza(final Info info, final boolean b, final float n, final long n2, final String s, final Throwable t) {
        if (Math.random() > n) {
            return false;
        }
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        final String s2 = "1";
        String s3;
        if (b) {
            s3 = "1";
        }
        else {
            s3 = "0";
        }
        hashMap.put("app_context", s3);
        if (info != null) {
            String s4;
            if (info.isLimitAdTrackingEnabled()) {
                s4 = s2;
            }
            else {
                s4 = "0";
            }
            hashMap.put("limit_ad_tracking", s4);
        }
        if (info != null && info.getId() != null) {
            hashMap.put("ad_id_size", Integer.toString(info.getId().length()));
        }
        if (t != null) {
            hashMap.put("error", t.getClass().getName());
        }
        if (s != null && !s.isEmpty()) {
            hashMap.put("experiment_id", s);
        }
        hashMap.put("tag", "AdvertisingIdClient");
        hashMap.put("time_spent", Long.toString(n2));
        new com.google.android.gms.ads.identifier.zza(this, hashMap).start();
        return true;
    }
    
    @Override
    protected void finalize() throws Throwable {
        this.finish();
        super.finalize();
    }
    
    public final void finish() {
        Preconditions.checkNotMainThread("Calling this from your main thread can lead to deadlock");
        synchronized (this) {
            if (this.mContext != null && this.zze != null) {
                try {
                    if (this.zzg) {
                        ConnectionTracker.getInstance().unbindService(this.mContext, (ServiceConnection)this.zze);
                    }
                }
                finally {
                    final Throwable t;
                    Log.i("AdvertisingIdClient", "AdvertisingIdClient unbindService failed.", t);
                }
                this.zzg = false;
                this.zzf = null;
                this.zze = null;
            }
        }
    }
    
    public Info getInfo() throws IOException {
        Preconditions.checkNotMainThread("Calling this from your main thread can lead to deadlock");
        synchronized (this) {
            Label_0095: {
                if (!this.zzg) {
                    final Object zzh = this.zzh;
                    synchronized (zzh) {
                        if (this.zzi != null && this.zzi.zzp) {
                            // monitorexit(zzh)
                            try {
                                this.zza(false);
                                if (this.zzg) {
                                    break Label_0095;
                                }
                                throw new IOException("AdvertisingIdClient cannot reconnect.");
                            }
                            catch (Exception zzh) {
                                throw new IOException("AdvertisingIdClient cannot reconnect.", (Throwable)zzh);
                            }
                        }
                        throw new IOException("AdvertisingIdClient is not connected.");
                    }
                }
            }
            Preconditions.checkNotNull(this.zze);
            Preconditions.checkNotNull(this.zzf);
            try {
                final Info info = new Info(this.zzf.getId(), this.zzf.zzb(true));
                // monitorexit(this)
                this.zza();
                return info;
            }
            catch (RemoteException ex) {
                Log.i("AdvertisingIdClient", "GMS remote exception ", (Throwable)ex);
                throw new IOException("Remote exception");
            }
        }
    }
    
    public static final class Info
    {
        private final String zzq;
        private final boolean zzr;
        
        public Info(final String zzq, final boolean zzr) {
            this.zzq = zzq;
            this.zzr = zzr;
        }
        
        public final String getId() {
            return this.zzq;
        }
        
        public final boolean isLimitAdTrackingEnabled() {
            return this.zzr;
        }
        
        @Override
        public final String toString() {
            final String zzq = this.zzq;
            final boolean zzr = this.zzr;
            final StringBuilder sb = new StringBuilder(String.valueOf(zzq).length() + 7);
            sb.append("{");
            sb.append(zzq);
            sb.append("}");
            sb.append(zzr);
            return sb.toString();
        }
    }
    
    static final class zza extends Thread
    {
        private WeakReference<AdvertisingIdClient> zzm;
        private long zzn;
        CountDownLatch zzo;
        boolean zzp;
        
        public zza(final AdvertisingIdClient advertisingIdClient, final long zzn) {
            this.zzm = new WeakReference<AdvertisingIdClient>(advertisingIdClient);
            this.zzn = zzn;
            this.zzo = new CountDownLatch(1);
            this.zzp = false;
            this.start();
        }
        
        private final void disconnect() {
            final AdvertisingIdClient advertisingIdClient = this.zzm.get();
            if (advertisingIdClient != null) {
                advertisingIdClient.finish();
                this.zzp = true;
            }
        }
        
        @Override
        public final void run() {
            try {
                if (!this.zzo.await(this.zzn, TimeUnit.MILLISECONDS)) {
                    this.disconnect();
                }
            }
            catch (InterruptedException ex) {
                this.disconnect();
            }
        }
    }
}
