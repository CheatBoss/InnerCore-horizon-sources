package com.google.firebase;

import com.google.firebase.events.*;
import android.support.v4.util.*;
import java.util.concurrent.*;
import com.google.firebase.internal.*;
import com.google.firebase.components.*;
import com.google.android.gms.common.util.*;
import android.app.*;
import com.google.android.gms.common.api.internal.*;
import java.util.*;
import android.util.*;
import java.lang.reflect.*;
import android.content.pm.*;
import android.support.v4.content.*;
import com.google.android.gms.common.internal.*;
import android.os.*;
import java.util.concurrent.atomic.*;
import android.content.*;

public class FirebaseApp
{
    static final Map<String, FirebaseApp> zza;
    private static final List<String> zzb;
    private static final List<String> zzc;
    private static final List<String> zzd;
    private static final List<String> zze;
    private static final Set<String> zzf;
    private static final Object zzg;
    private static final Executor zzh;
    private final Context zzi;
    private final String zzj;
    private final FirebaseOptions zzk;
    private final zzd zzl;
    private final SharedPreferences zzm;
    private final Publisher zzn;
    private final AtomicBoolean zzo;
    private final AtomicBoolean zzp;
    private final AtomicBoolean zzq;
    private final List<Object> zzr;
    private final List<BackgroundStateChangeListener> zzs;
    private final List<Object> zzt;
    private IdTokenListenersCountChangedListener zzv;
    
    static {
        zzb = Arrays.asList("com.google.firebase.auth.FirebaseAuth", "com.google.firebase.iid.FirebaseInstanceId");
        zzc = Collections.singletonList("com.google.firebase.crash.FirebaseCrash");
        zzd = Arrays.asList("com.google.android.gms.measurement.AppMeasurement");
        zze = Arrays.asList(new String[0]);
        zzf = Collections.emptySet();
        zzg = new Object();
        zzh = new zza((byte)0);
        zza = new ArrayMap<String, FirebaseApp>();
    }
    
    protected FirebaseApp(final Context context, final String s, final FirebaseOptions firebaseOptions) {
        this.zzo = new AtomicBoolean(false);
        this.zzp = new AtomicBoolean();
        this.zzr = new CopyOnWriteArrayList<Object>();
        this.zzs = new CopyOnWriteArrayList<BackgroundStateChangeListener>();
        this.zzt = new CopyOnWriteArrayList<Object>();
        this.zzi = Preconditions.checkNotNull(context);
        this.zzj = Preconditions.checkNotEmpty(s);
        this.zzk = Preconditions.checkNotNull(firebaseOptions);
        this.zzv = (IdTokenListenersCountChangedListener)new com.google.firebase.internal.zza();
        this.zzm = context.getSharedPreferences("com.google.firebase.common.prefs", 0);
        this.zzq = new AtomicBoolean(this.zzb());
        final zzd zzl = new zzd(FirebaseApp.zzh, com.google.firebase.components.zzc.zza(context).zza(), (Component<?>[])new Component[] { Component.of(context, Context.class, (Class<? super Context>[])new Class[0]), Component.of(this, FirebaseApp.class, (Class<? super FirebaseApp>[])new Class[0]), Component.of(firebaseOptions, FirebaseOptions.class, (Class<? super FirebaseOptions>[])new Class[0]) });
        this.zzl = zzl;
        this.zzn = (Publisher)zzl.get(Publisher.class);
    }
    
    public static FirebaseApp getInstance() {
        synchronized (FirebaseApp.zzg) {
            final FirebaseApp firebaseApp = FirebaseApp.zza.get("[DEFAULT]");
            if (firebaseApp != null) {
                return firebaseApp;
            }
            final StringBuilder sb = new StringBuilder("Default FirebaseApp is not initialized in this process ");
            sb.append(ProcessUtils.getMyProcessName());
            sb.append(". Make sure to call FirebaseApp.initializeApp(Context) first.");
            throw new IllegalStateException(sb.toString());
        }
    }
    
    public static FirebaseApp initializeApp(final Context context) {
        synchronized (FirebaseApp.zzg) {
            if (FirebaseApp.zza.containsKey("[DEFAULT]")) {
                return getInstance();
            }
            final FirebaseOptions fromResource = FirebaseOptions.fromResource(context);
            if (fromResource == null) {
                return null;
            }
            return initializeApp(context, fromResource);
        }
    }
    
    public static FirebaseApp initializeApp(final Context context, final FirebaseOptions firebaseOptions) {
        return initializeApp(context, firebaseOptions, "[DEFAULT]");
    }
    
    public static FirebaseApp initializeApp(Context applicationContext, final FirebaseOptions firebaseOptions, String trim) {
        if (PlatformVersion.isAtLeastIceCreamSandwich() && applicationContext.getApplicationContext() instanceof Application) {
            BackgroundDetector.initialize((Application)applicationContext.getApplicationContext());
            BackgroundDetector.getInstance().addListener((BackgroundDetector.BackgroundStateChangeListener)new BackgroundDetector.BackgroundStateChangeListener() {
                @Override
                public final void onBackgroundStateChanged(final boolean b) {
                    FirebaseApp.onBackgroundStateChanged(b);
                }
            });
        }
        trim = trim.trim();
        if (applicationContext.getApplicationContext() != null) {
            applicationContext = applicationContext.getApplicationContext();
        }
        synchronized (FirebaseApp.zzg) {
            final boolean containsKey = FirebaseApp.zza.containsKey(trim);
            final StringBuilder sb = new StringBuilder("FirebaseApp name ");
            sb.append(trim);
            sb.append(" already exists!");
            Preconditions.checkState(containsKey ^ true, sb.toString());
            Preconditions.checkNotNull(applicationContext, "Application context cannot be null.");
            final FirebaseApp firebaseApp = new FirebaseApp(applicationContext, trim, firebaseOptions);
            FirebaseApp.zza.put(trim, firebaseApp);
            // monitorexit(FirebaseApp.zzg)
            firebaseApp.zze();
            return firebaseApp;
        }
    }
    
    public static void onBackgroundStateChanged(final boolean b) {
        synchronized (FirebaseApp.zzg) {
            for (final FirebaseApp firebaseApp : new ArrayList<FirebaseApp>(FirebaseApp.zza.values())) {
                if (firebaseApp.zzo.get()) {
                    firebaseApp.zza(b);
                }
            }
        }
    }
    
    private static <T> void zza(Class<T> sb, final T t, Iterable<String> s, final boolean b) {
        final Iterator<String> iterator = ((Iterable<String>)s).iterator();
        while (true) {
            if (!iterator.hasNext()) {
                return;
            }
            s = iterator.next();
            Label_0048: {
                if (!b) {
                    break Label_0048;
                }
            Label_0193_Outer:
                while (true) {
                    while (true) {
                        try {
                            if (!FirebaseApp.zze.contains(s)) {
                                break;
                            }
                            final Method method = Class.forName(s).getMethod("getInstance", (Class)sb);
                            final int modifiers = method.getModifiers();
                            if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers)) {
                                method.invoke(null, t);
                                break;
                            }
                            break;
                            // iftrue(Label_0245:, FirebaseApp.zzf.contains((Object)s))
                            while (true) {
                                final StringBuilder sb2 = new StringBuilder();
                                sb2.append(s);
                                sb2.append(" is not linked. Skipping initialization.");
                                Log.d("FirebaseApp", sb2.toString());
                                break;
                                continue Label_0193_Outer;
                            }
                            Label_0245: {
                                sb = new StringBuilder();
                            }
                            sb.append(s);
                            sb.append(" is missing, but is required. Check if it has been removed by Proguard.");
                            throw new IllegalStateException(sb.toString());
                        }
                        catch (IllegalAccessException ex) {}
                        catch (InvocationTargetException ex2) {}
                        catch (NoSuchMethodException ex3) {}
                        catch (ClassNotFoundException ex4) {}
                        continue;
                    }
                }
            }
        }
    }
    
    private void zza(final boolean b) {
        Log.d("FirebaseApp", "Notifying background state change listeners.");
        final Iterator<BackgroundStateChangeListener> iterator = this.zzs.iterator();
        while (iterator.hasNext()) {
            iterator.next().onBackgroundStateChanged(b);
        }
    }
    
    private boolean zzb() {
        if (this.zzm.contains("firebase_data_collection_default_enabled")) {
            return this.zzm.getBoolean("firebase_data_collection_default_enabled", true);
        }
        try {
            final PackageManager packageManager = this.zzi.getPackageManager();
            if (packageManager != null) {
                final ApplicationInfo applicationInfo = packageManager.getApplicationInfo(this.zzi.getPackageName(), 128);
                if (applicationInfo != null && applicationInfo.metaData != null && applicationInfo.metaData.containsKey("firebase_data_collection_default_enabled")) {
                    return applicationInfo.metaData.getBoolean("firebase_data_collection_default_enabled");
                }
            }
            return true;
        }
        catch (PackageManager$NameNotFoundException ex) {
            return true;
        }
    }
    
    private void zzc() {
        Preconditions.checkState(this.zzp.get() ^ true, "FirebaseApp was deleted");
    }
    
    private void zze() {
        final boolean deviceProtectedStorage = ContextCompat.isDeviceProtectedStorage(this.zzi);
        if (deviceProtectedStorage) {
            FirebaseApp.zzb.zza(this.zzi);
        }
        else {
            this.zzl.zza(this.isDefaultApp());
        }
        zza(FirebaseApp.class, this, FirebaseApp.zzb, deviceProtectedStorage);
        if (this.isDefaultApp()) {
            zza(FirebaseApp.class, this, FirebaseApp.zzc, deviceProtectedStorage);
            zza(Context.class, this.zzi, FirebaseApp.zzd, deviceProtectedStorage);
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof FirebaseApp && this.zzj.equals(((FirebaseApp)o).getName());
    }
    
    public <T> T get(final Class<T> clazz) {
        this.zzc();
        return (T)this.zzl.get(clazz);
    }
    
    public Context getApplicationContext() {
        this.zzc();
        return this.zzi;
    }
    
    public String getName() {
        this.zzc();
        return this.zzj;
    }
    
    public FirebaseOptions getOptions() {
        this.zzc();
        return this.zzk;
    }
    
    @Override
    public int hashCode() {
        return this.zzj.hashCode();
    }
    
    public boolean isDataCollectionDefaultEnabled() {
        this.zzc();
        return this.zzq.get();
    }
    
    public boolean isDefaultApp() {
        return "[DEFAULT]".equals(this.getName());
    }
    
    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("name", this.zzj).add("options", this.zzk).toString();
    }
    
    public interface BackgroundStateChangeListener
    {
        void onBackgroundStateChanged(final boolean p0);
    }
    
    public interface IdTokenListenersCountChangedListener
    {
    }
    
    static final class zza implements Executor
    {
        private static final Handler zza;
        
        static {
            zza = new Handler(Looper.getMainLooper());
        }
        
        private zza() {
        }
        
        @Override
        public final void execute(final Runnable runnable) {
            FirebaseApp.zza.zza.post(runnable);
        }
    }
    
    static final class zzb extends BroadcastReceiver
    {
        private static AtomicReference<zzb> zza;
        private final Context zzb;
        
        static {
            zzb.zza = new AtomicReference<zzb>();
        }
        
        private zzb(final Context zzb) {
            this.zzb = zzb;
        }
        
        static /* synthetic */ void zza(final Context context) {
            if (zzb.zza.get() == null) {
                final zzb zzb = new zzb(context);
                if (FirebaseApp.zzb.zza.compareAndSet(null, zzb)) {
                    context.registerReceiver((BroadcastReceiver)zzb, new IntentFilter("android.intent.action.USER_UNLOCKED"));
                }
            }
        }
        
        public final void onReceive(final Context context, final Intent intent) {
            synchronized (FirebaseApp.zzg) {
                final Iterator<FirebaseApp> iterator = FirebaseApp.zza.values().iterator();
                while (iterator.hasNext()) {
                    iterator.next().zze();
                }
                // monitorexit(FirebaseApp.zza())
                this.zzb.unregisterReceiver((BroadcastReceiver)this);
            }
        }
    }
}
