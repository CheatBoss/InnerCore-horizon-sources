package com.google.android.gms.common.api.internal;

import android.content.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.*;
import android.util.*;
import com.google.android.gms.tasks.*;
import com.google.android.gms.common.util.*;
import com.google.android.gms.common.*;
import android.support.v4.util.*;
import android.app.*;
import java.util.*;
import android.os.*;
import com.google.android.gms.common.internal.*;
import com.google.android.gms.common.api.*;

public class GoogleApiManager implements Handler$Callback
{
    private static final Object lock;
    public static final Status zzjj;
    private static final Status zzjk;
    private static GoogleApiManager zzjo;
    private final Handler handler;
    private long zzjl;
    private long zzjm;
    private long zzjn;
    private final Context zzjp;
    private final GoogleApiAvailability zzjq;
    private final GoogleApiAvailabilityCache zzjr;
    private final AtomicInteger zzjs;
    private final AtomicInteger zzjt;
    private final Map<zzh<?>, zza<?>> zzju;
    private zzad zzjv;
    private final Set<zzh<?>> zzjw;
    private final Set<zzh<?>> zzjx;
    
    static {
        zzjj = new Status(4, "Sign-out occurred while this API call was in progress.");
        zzjk = new Status(4, "The user must be signed in to make this API call.");
        lock = new Object();
    }
    
    private GoogleApiManager(final Context zzjp, final Looper looper, final GoogleApiAvailability zzjq) {
        this.zzjl = 5000L;
        this.zzjm = 120000L;
        this.zzjn = 10000L;
        this.zzjs = new AtomicInteger(1);
        this.zzjt = new AtomicInteger(0);
        this.zzju = new ConcurrentHashMap<zzh<?>, zza<?>>(5, 0.75f, 1);
        this.zzjv = null;
        this.zzjw = (Set<zzh<?>>)new ArraySet();
        this.zzjx = (Set<zzh<?>>)new ArraySet();
        this.zzjp = zzjp;
        this.handler = new Handler(looper, (Handler$Callback)this);
        this.zzjq = zzjq;
        this.zzjr = new GoogleApiAvailabilityCache(zzjq);
        final Handler handler = this.handler;
        handler.sendMessage(handler.obtainMessage(6));
    }
    
    public static GoogleApiManager zzb(final Context context) {
        synchronized (GoogleApiManager.lock) {
            if (GoogleApiManager.zzjo == null) {
                final HandlerThread handlerThread = new HandlerThread("GoogleApiHandler", 9);
                handlerThread.start();
                GoogleApiManager.zzjo = new GoogleApiManager(context.getApplicationContext(), handlerThread.getLooper(), GoogleApiAvailability.getInstance());
            }
            return GoogleApiManager.zzjo;
        }
    }
    
    private final void zzb(final GoogleApi<?> googleApi) {
        final zzh<O> zzm = googleApi.zzm();
        zza<?> zza;
        if ((zza = this.zzju.get(zzm)) == null) {
            zza = new zza<Object>(googleApi);
            this.zzju.put(zzm, zza);
        }
        if (zza.requiresSignIn()) {
            this.zzjx.add(zzm);
        }
        zza.connect();
    }
    
    public boolean handleMessage(final Message message) {
        final int what = message.what;
        long zzjn = 300000L;
        switch (what) {
            default: {
                final int what2 = message.what;
                final StringBuilder sb = new StringBuilder(31);
                sb.append("Unknown message id: ");
                sb.append(what2);
                Log.w("GoogleApiManager", sb.toString());
                return false;
            }
            case 16: {
                final zzb zzb = (zzb)message.obj;
                if (this.zzju.containsKey(zzb.zzkn)) {
                    ((zza<Api.ApiOptions>)this.zzju.get(zzb.zzkn)).zzb(zzb);
                    return true;
                }
                break;
            }
            case 15: {
                final zzb zzb2 = (zzb)message.obj;
                if (this.zzju.containsKey(zzb2.zzkn)) {
                    ((zza<Api.ApiOptions>)this.zzju.get(zzb2.zzkn)).zza(zzb2);
                    return true;
                }
                break;
            }
            case 14: {
                final zzae zzae = (zzae)message.obj;
                final zzh<?> zzm = zzae.zzm();
                TaskCompletionSource<Boolean> taskCompletionSource;
                Boolean result;
                if (!this.zzju.containsKey(zzm)) {
                    taskCompletionSource = zzae.zzao();
                    result = false;
                }
                else {
                    final boolean zza = GoogleApiManager.zza.zza((zza<Api.ApiOptions>)this.zzju.get(zzm), false);
                    taskCompletionSource = zzae.zzao();
                    result = zza;
                }
                taskCompletionSource.setResult(result);
                return true;
            }
            case 12: {
                if (this.zzju.containsKey(message.obj)) {
                    this.zzju.get(message.obj).zzbs();
                    return true;
                }
                break;
            }
            case 11: {
                if (this.zzju.containsKey(message.obj)) {
                    this.zzju.get(message.obj).zzay();
                    return true;
                }
                break;
            }
            case 10: {
                final Iterator<zzh<?>> iterator = this.zzjx.iterator();
                while (iterator.hasNext()) {
                    this.zzju.remove(iterator.next()).zzbm();
                }
                this.zzjx.clear();
                return true;
            }
            case 9: {
                if (this.zzju.containsKey(message.obj)) {
                    this.zzju.get(message.obj).resume();
                    return true;
                }
                break;
            }
            case 7: {
                this.zzb((GoogleApi<?>)message.obj);
                return true;
            }
            case 6: {
                if (!PlatformVersion.isAtLeastIceCreamSandwich() || !(this.zzjp.getApplicationContext() instanceof Application)) {
                    break;
                }
                BackgroundDetector.initialize((Application)this.zzjp.getApplicationContext());
                BackgroundDetector.getInstance().addListener((BackgroundDetector.BackgroundStateChangeListener)new zzbh(this));
                if (!BackgroundDetector.getInstance().readCurrentStateIfPossible(true)) {
                    this.zzjn = 300000L;
                    return true;
                }
                break;
            }
            case 5: {
                final int arg1 = message.arg1;
                final ConnectionResult connectionResult = (ConnectionResult)message.obj;
                while (true) {
                    for (final zza<?> zza2 : this.zzju.values()) {
                        if (zza2.getInstanceId() == arg1) {
                            if (zza2 != null) {
                                final String errorString = this.zzjq.getErrorString(connectionResult.getErrorCode());
                                final String errorMessage = connectionResult.getErrorMessage();
                                final StringBuilder sb2 = new StringBuilder(String.valueOf(errorString).length() + 69 + String.valueOf(errorMessage).length());
                                sb2.append("Error resolution was canceled by the user, original error message: ");
                                sb2.append(errorString);
                                sb2.append(": ");
                                sb2.append(errorMessage);
                                zza2.zzc(new Status(17, sb2.toString()));
                                return true;
                            }
                            final StringBuilder sb3 = new StringBuilder(76);
                            sb3.append("Could not find API instance ");
                            sb3.append(arg1);
                            sb3.append(" while trying to fail enqueued calls.");
                            Log.wtf("GoogleApiManager", sb3.toString(), (Throwable)new Exception());
                            return true;
                        }
                    }
                    zza<?> zza2 = null;
                    continue;
                }
            }
            case 4:
            case 8:
            case 13: {
                final zzbu zzbu = (zzbu)message.obj;
                zza<?> zza3;
                if ((zza3 = this.zzju.get(zzbu.zzlr.zzm())) == null) {
                    this.zzb(zzbu.zzlr);
                    zza3 = this.zzju.get(zzbu.zzlr.zzm());
                }
                if (zza3.requiresSignIn() && this.zzjt.get() != zzbu.zzlq) {
                    zzbu.zzlp.zza(GoogleApiManager.zzjj);
                    zza3.zzbm();
                    return true;
                }
                zza3.zza(zzbu.zzlp);
                return true;
            }
            case 3: {
                for (final zza<?> zza4 : this.zzju.values()) {
                    zza4.zzbo();
                    zza4.connect();
                }
                break;
            }
            case 2: {
                final zzj zzj = (zzj)message.obj;
                for (final zzh<?> zzh : zzj.zzs()) {
                    final zza<?> zza5 = this.zzju.get(zzh);
                    if (zza5 == null) {
                        zzj.zza(zzh, new ConnectionResult(13), null);
                        return true;
                    }
                    if (zza5.isConnected()) {
                        zzj.zza(zzh, ConnectionResult.RESULT_SUCCESS, zza5.zzae().getEndpointPackageName());
                    }
                    else if (zza5.zzbp() != null) {
                        zzj.zza(zzh, zza5.zzbp(), null);
                    }
                    else {
                        zza5.zza(zzj);
                    }
                }
                break;
            }
            case 1: {
                if (message.obj) {
                    zzjn = 10000L;
                }
                this.zzjn = zzjn;
                this.handler.removeMessages(12);
                for (final zzh<?> zzh2 : this.zzju.keySet()) {
                    final Handler handler = this.handler;
                    handler.sendMessageDelayed(handler.obtainMessage(12, (Object)zzh2), this.zzjn);
                }
                break;
            }
        }
        return true;
    }
    
    public final void zza(final ConnectionResult connectionResult, final int n) {
        if (!this.zzc(connectionResult, n)) {
            final Handler handler = this.handler;
            handler.sendMessage(handler.obtainMessage(5, n, 0, (Object)connectionResult));
        }
    }
    
    final boolean zzc(final ConnectionResult connectionResult, final int n) {
        return this.zzjq.showWrappedErrorNotification(this.zzjp, connectionResult, n);
    }
    
    public final void zzr() {
        final Handler handler = this.handler;
        handler.sendMessage(handler.obtainMessage(3));
    }
    
    public final class zza<O extends Api.ApiOptions> implements OnConnectionFailedListener, zzq
    {
        private final zzh<O> zzhc;
        private final Queue<com.google.android.gms.common.api.internal.zzb> zzjz;
        private final Api.Client zzka;
        private final Api.AnyClient zzkb;
        private final zzaa zzkc;
        private final Set<zzj> zzkd;
        private final Map<ListenerHolder.ListenerKey<?>, zzbv> zzke;
        private final int zzkf;
        private final zzby zzkg;
        private boolean zzkh;
        private final List<zzb> zzki;
        private ConnectionResult zzkj;
        
        public zza(final GoogleApi<O> googleApi) {
            this.zzjz = new LinkedList<com.google.android.gms.common.api.internal.zzb>();
            this.zzkd = new HashSet<zzj>();
            this.zzke = new HashMap<ListenerHolder.ListenerKey<?>, zzbv>();
            this.zzki = new ArrayList<zzb>();
            this.zzkj = null;
            final Api.Client zza = googleApi.zza(GoogleApiManager.this.handler.getLooper(), this);
            this.zzka = zza;
            Object client = zza;
            if (zza instanceof SimpleClientAdapter) {
                client = ((SimpleClientAdapter)zza).getClient();
            }
            this.zzkb = (Api.AnyClient)client;
            this.zzhc = googleApi.zzm();
            this.zzkc = new zzaa();
            this.zzkf = googleApi.getInstanceId();
            if (this.zzka.requiresSignIn()) {
                this.zzkg = googleApi.zza(GoogleApiManager.this.zzjp, GoogleApiManager.this.handler);
                return;
            }
            this.zzkg = null;
        }
        
        private final void zza(final zzb zzb) {
            if (!this.zzki.contains(zzb)) {
                return;
            }
            if (!this.zzkh) {
                if (!this.zzka.isConnected()) {
                    this.connect();
                    return;
                }
                this.zzbl();
            }
        }
        
        static /* synthetic */ boolean zza(final zza zza, final boolean b) {
            return zza.zzb(false);
        }
        
        private final void zzb(final zzb zzb) {
            if (this.zzki.remove(zzb)) {
                GoogleApiManager.this.handler.removeMessages(15, (Object)zzb);
                GoogleApiManager.this.handler.removeMessages(16, (Object)zzb);
                final Feature zzd = zzb.zzdr;
                final ArrayList<zzf> list = new ArrayList<zzf>(this.zzjz.size());
                for (final com.google.android.gms.common.api.internal.zzb zzb2 : this.zzjz) {
                    if (zzb2 instanceof zzf) {
                        final Feature[] requiredFeatures = ((zzf)zzb2).getRequiredFeatures();
                        if (requiredFeatures == null || !ArrayUtils.contains(requiredFeatures, zzd)) {
                            continue;
                        }
                        list.add((zzf)zzb2);
                    }
                }
                final ArrayList<zzf> list2 = list;
                final int size = list2.size();
                int i = 0;
                while (i < size) {
                    final zzf<?> value = list2.get(i);
                    ++i;
                    final zzf<?> zzf = value;
                    this.zzjz.remove(zzf);
                    zzf.zza(new UnsupportedApiCallException(zzd));
                }
            }
        }
        
        private final boolean zzb(final com.google.android.gms.common.api.internal.zzb zzb) {
            if (!(zzb instanceof zzf)) {
                this.zzc(zzb);
                return true;
            }
            final zzf zzf = (zzf)zzb;
            final Feature[] requiredFeatures = zzf.getRequiredFeatures();
            if (requiredFeatures != null && requiredFeatures.length != 0) {
                Feature[] availableFeatures;
                if ((availableFeatures = this.zzka.getAvailableFeatures()) == null) {
                    availableFeatures = new Feature[0];
                }
                final ArrayMap arrayMap = new ArrayMap<Object, Object>(availableFeatures.length);
                for (int length = availableFeatures.length, i = 0; i < length; ++i) {
                    final Feature feature = availableFeatures[i];
                    arrayMap.put(feature.getName(), Long.valueOf(feature.getVersion()));
                }
                for (int length2 = requiredFeatures.length, j = 0; j < length2; ++j) {
                    final Feature feature2 = requiredFeatures[j];
                    if (!arrayMap.containsKey(feature2.getName()) || (long)arrayMap.get(feature2.getName()) < feature2.getVersion()) {
                        if (zzf.shouldAutoResolveMissingFeatures()) {
                            final zzb zzb2 = new zzb(this.zzhc, feature2, null);
                            final int index = this.zzki.indexOf(zzb2);
                            if (index >= 0) {
                                final zzb zzb3 = this.zzki.get(index);
                                GoogleApiManager.this.handler.removeMessages(15, (Object)zzb3);
                                GoogleApiManager.this.handler.sendMessageDelayed(Message.obtain(GoogleApiManager.this.handler, 15, (Object)zzb3), GoogleApiManager.this.zzjl);
                                return false;
                            }
                            this.zzki.add(zzb2);
                            GoogleApiManager.this.handler.sendMessageDelayed(Message.obtain(GoogleApiManager.this.handler, 15, (Object)zzb2), GoogleApiManager.this.zzjl);
                            GoogleApiManager.this.handler.sendMessageDelayed(Message.obtain(GoogleApiManager.this.handler, 16, (Object)zzb2), GoogleApiManager.this.zzjm);
                            final ConnectionResult connectionResult = new ConnectionResult(2, null);
                            if (!this.zzh(connectionResult)) {
                                GoogleApiManager.this.zzc(connectionResult, this.zzkf);
                                return false;
                            }
                        }
                        else {
                            zzf.zza(new UnsupportedApiCallException(feature2));
                        }
                        return false;
                    }
                    this.zzki.remove(new zzb(this.zzhc, feature2, null));
                }
                this.zzc(zzb);
                return true;
            }
            this.zzc(zzb);
            return true;
        }
        
        private final boolean zzb(final boolean b) {
            Preconditions.checkHandlerThread(GoogleApiManager.this.handler);
            if (!this.zzka.isConnected() || this.zzke.size() != 0) {
                return false;
            }
            if (this.zzkc.zzaj()) {
                if (b) {
                    this.zzbr();
                }
                return false;
            }
            this.zzka.disconnect();
            return true;
        }
        
        private final void zzbj() {
            this.zzbo();
            this.zzi(ConnectionResult.RESULT_SUCCESS);
            this.zzbq();
            for (final zzbv zzbv : this.zzke.values()) {
                try {
                    zzbv.zzlt.registerListener(this.zzkb, new TaskCompletionSource<Void>());
                    continue;
                }
                catch (RemoteException ex) {
                    continue;
                }
                catch (DeadObjectException ex2) {
                    this.onConnectionSuspended(1);
                    this.zzka.disconnect();
                }
                break;
            }
            this.zzbl();
            this.zzbr();
        }
        
        private final void zzbk() {
            this.zzbo();
            this.zzkh = true;
            this.zzkc.zzal();
            GoogleApiManager.this.handler.sendMessageDelayed(Message.obtain(GoogleApiManager.this.handler, 9, (Object)this.zzhc), GoogleApiManager.this.zzjl);
            GoogleApiManager.this.handler.sendMessageDelayed(Message.obtain(GoogleApiManager.this.handler, 11, (Object)this.zzhc), GoogleApiManager.this.zzjm);
            GoogleApiManager.this.zzjr.flush();
        }
        
        private final void zzbl() {
            final ArrayList<Object> list = new ArrayList<Object>(this.zzjz);
            final int size = list.size();
            int i = 0;
            while (i < size) {
                final com.google.android.gms.common.api.internal.zzb value = list.get(i);
                final int n = i + 1;
                final com.google.android.gms.common.api.internal.zzb zzb = value;
                if (!this.zzka.isConnected()) {
                    break;
                }
                i = n;
                if (!this.zzb(zzb)) {
                    continue;
                }
                this.zzjz.remove(zzb);
                i = n;
            }
        }
        
        private final void zzbq() {
            if (this.zzkh) {
                GoogleApiManager.this.handler.removeMessages(11, (Object)this.zzhc);
                GoogleApiManager.this.handler.removeMessages(9, (Object)this.zzhc);
                this.zzkh = false;
            }
        }
        
        private final void zzbr() {
            GoogleApiManager.this.handler.removeMessages(12, (Object)this.zzhc);
            GoogleApiManager.this.handler.sendMessageDelayed(GoogleApiManager.this.handler.obtainMessage(12, (Object)this.zzhc), GoogleApiManager.this.zzjn);
        }
        
        private final void zzc(final com.google.android.gms.common.api.internal.zzb zzb) {
            zzb.zza(this.zzkc, this.requiresSignIn());
            try {
                zzb.zza((zza<?>)this);
            }
            catch (DeadObjectException ex) {
                this.onConnectionSuspended(1);
                this.zzka.disconnect();
            }
        }
        
        private final boolean zzh(final ConnectionResult connectionResult) {
            synchronized (GoogleApiManager.lock) {
                if (GoogleApiManager.this.zzjv != null && GoogleApiManager.this.zzjw.contains(this.zzhc)) {
                    GoogleApiManager.this.zzjv.zzb(connectionResult, this.zzkf);
                    return true;
                }
                return false;
            }
        }
        
        private final void zzi(final ConnectionResult connectionResult) {
            for (final zzj zzj : this.zzkd) {
                String endpointPackageName = null;
                if (Objects.equal(connectionResult, ConnectionResult.RESULT_SUCCESS)) {
                    endpointPackageName = this.zzka.getEndpointPackageName();
                }
                zzj.zza(this.zzhc, connectionResult, endpointPackageName);
            }
            this.zzkd.clear();
        }
        
        public final void connect() {
            Preconditions.checkHandlerThread(GoogleApiManager.this.handler);
            if (!this.zzka.isConnected()) {
                if (this.zzka.isConnecting()) {
                    return;
                }
                final int clientAvailability = GoogleApiManager.this.zzjr.getClientAvailability(GoogleApiManager.this.zzjp, this.zzka);
                if (clientAvailability != 0) {
                    this.onConnectionFailed(new ConnectionResult(clientAvailability, null));
                    return;
                }
                final zzc zzc = new zzc(this.zzka, this.zzhc);
                if (this.zzka.requiresSignIn()) {
                    this.zzkg.zza(zzc);
                }
                this.zzka.connect(zzc);
            }
        }
        
        public final int getInstanceId() {
            return this.zzkf;
        }
        
        final boolean isConnected() {
            return this.zzka.isConnected();
        }
        
        @Override
        public final void onConnected(final Bundle bundle) {
            if (Looper.myLooper() == GoogleApiManager.this.handler.getLooper()) {
                this.zzbj();
                return;
            }
            GoogleApiManager.this.handler.post((Runnable)new zzbi(this));
        }
        
        @Override
        public final void onConnectionFailed(final ConnectionResult zzkj) {
            Preconditions.checkHandlerThread(GoogleApiManager.this.handler);
            final zzby zzkg = this.zzkg;
            if (zzkg != null) {
                zzkg.zzbz();
            }
            this.zzbo();
            GoogleApiManager.this.zzjr.flush();
            this.zzi(zzkj);
            if (zzkj.getErrorCode() == 4) {
                this.zzc(GoogleApiManager.zzjk);
                return;
            }
            if (this.zzjz.isEmpty()) {
                this.zzkj = zzkj;
                return;
            }
            if (this.zzh(zzkj)) {
                return;
            }
            if (!GoogleApiManager.this.zzc(zzkj, this.zzkf)) {
                if (zzkj.getErrorCode() == 18) {
                    this.zzkh = true;
                }
                if (this.zzkh) {
                    GoogleApiManager.this.handler.sendMessageDelayed(Message.obtain(GoogleApiManager.this.handler, 9, (Object)this.zzhc), GoogleApiManager.this.zzjl);
                    return;
                }
                final String zzq = this.zzhc.zzq();
                final StringBuilder sb = new StringBuilder(String.valueOf(zzq).length() + 38);
                sb.append("API: ");
                sb.append(zzq);
                sb.append(" is not available on this device.");
                this.zzc(new Status(17, sb.toString()));
            }
        }
        
        @Override
        public final void onConnectionSuspended(final int n) {
            if (Looper.myLooper() == GoogleApiManager.this.handler.getLooper()) {
                this.zzbk();
                return;
            }
            GoogleApiManager.this.handler.post((Runnable)new zzbj(this));
        }
        
        public final boolean requiresSignIn() {
            return this.zzka.requiresSignIn();
        }
        
        public final void resume() {
            Preconditions.checkHandlerThread(GoogleApiManager.this.handler);
            if (this.zzkh) {
                this.connect();
            }
        }
        
        public final void zza(final com.google.android.gms.common.api.internal.zzb zzb) {
            Preconditions.checkHandlerThread(GoogleApiManager.this.handler);
            if (this.zzka.isConnected()) {
                if (this.zzb(zzb)) {
                    this.zzbr();
                    return;
                }
                this.zzjz.add(zzb);
            }
            else {
                this.zzjz.add(zzb);
                final ConnectionResult zzkj = this.zzkj;
                if (zzkj != null && zzkj.hasResolution()) {
                    this.onConnectionFailed(this.zzkj);
                    return;
                }
                this.connect();
            }
        }
        
        public final void zza(final zzj zzj) {
            Preconditions.checkHandlerThread(GoogleApiManager.this.handler);
            this.zzkd.add(zzj);
        }
        
        public final Api.Client zzae() {
            return this.zzka;
        }
        
        public final void zzay() {
            Preconditions.checkHandlerThread(GoogleApiManager.this.handler);
            if (this.zzkh) {
                this.zzbq();
                Status status;
                if (GoogleApiManager.this.zzjq.isGooglePlayServicesAvailable(GoogleApiManager.this.zzjp) == 18) {
                    status = new Status(8, "Connection timed out while waiting for Google Play services update to complete.");
                }
                else {
                    status = new Status(8, "API failed to connect while resuming due to an unknown error.");
                }
                this.zzc(status);
                this.zzka.disconnect();
            }
        }
        
        public final void zzbm() {
            Preconditions.checkHandlerThread(GoogleApiManager.this.handler);
            this.zzc(GoogleApiManager.zzjj);
            this.zzkc.zzak();
            final ListenerHolder.ListenerKey[] array = this.zzke.keySet().toArray((ListenerHolder.ListenerKey[])new ListenerHolder.ListenerKey[this.zzke.size()]);
            for (int length = array.length, i = 0; i < length; ++i) {
                this.zza(new zzg(array[i], new TaskCompletionSource<Boolean>()));
            }
            this.zzi(new ConnectionResult(4));
            if (this.zzka.isConnected()) {
                this.zzka.onUserSignOut(new zzbl(this));
            }
        }
        
        public final Map<ListenerHolder.ListenerKey<?>, zzbv> zzbn() {
            return this.zzke;
        }
        
        public final void zzbo() {
            Preconditions.checkHandlerThread(GoogleApiManager.this.handler);
            this.zzkj = null;
        }
        
        public final ConnectionResult zzbp() {
            Preconditions.checkHandlerThread(GoogleApiManager.this.handler);
            return this.zzkj;
        }
        
        public final boolean zzbs() {
            return this.zzb(true);
        }
        
        public final void zzc(final Status status) {
            Preconditions.checkHandlerThread(GoogleApiManager.this.handler);
            final Iterator<com.google.android.gms.common.api.internal.zzb> iterator = this.zzjz.iterator();
            while (iterator.hasNext()) {
                iterator.next().zza(status);
            }
            this.zzjz.clear();
        }
        
        public final void zzg(final ConnectionResult connectionResult) {
            Preconditions.checkHandlerThread(GoogleApiManager.this.handler);
            this.zzka.disconnect();
            this.onConnectionFailed(connectionResult);
        }
    }
    
    private static final class zzb
    {
        private final Feature zzdr;
        private final zzh<?> zzkn;
        
        private zzb(final zzh<?> zzkn, final Feature zzdr) {
            this.zzkn = zzkn;
            this.zzdr = zzdr;
        }
        
        @Override
        public final boolean equals(final Object o) {
            if (o != null && o instanceof zzb) {
                final zzb zzb = (zzb)o;
                if (Objects.equal(this.zzkn, zzb.zzkn) && Objects.equal(this.zzdr, zzb.zzdr)) {
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public final int hashCode() {
            return Objects.hashCode(this.zzkn, this.zzdr);
        }
        
        @Override
        public final String toString() {
            return Objects.toStringHelper(this).add("key", this.zzkn).add("feature", this.zzdr).toString();
        }
    }
    
    private final class zzc implements zzcb, ConnectionProgressReportCallbacks
    {
        private final zzh<?> zzhc;
        private final Api.Client zzka;
        private IAccountAccessor zzko;
        private Set<Scope> zzkp;
        private boolean zzkq;
        
        public zzc(final Api.Client zzka, final zzh<?> zzhc) {
            this.zzko = null;
            this.zzkp = null;
            this.zzkq = false;
            this.zzka = zzka;
            this.zzhc = zzhc;
        }
        
        private final void zzbu() {
            if (this.zzkq) {
                final IAccountAccessor zzko = this.zzko;
                if (zzko != null) {
                    this.zzka.getRemoteService(zzko, this.zzkp);
                }
            }
        }
        
        @Override
        public final void onReportServiceBinding(final ConnectionResult connectionResult) {
            GoogleApiManager.this.handler.post((Runnable)new zzbn(this, connectionResult));
        }
        
        @Override
        public final void zza(final IAccountAccessor zzko, final Set<Scope> zzkp) {
            if (zzko != null && zzkp != null) {
                this.zzko = zzko;
                this.zzkp = zzkp;
                this.zzbu();
                return;
            }
            Log.wtf("GoogleApiManager", "Received null response from onSignInSuccess", (Throwable)new Exception());
            this.zzg(new ConnectionResult(4));
        }
        
        @Override
        public final void zzg(final ConnectionResult connectionResult) {
            GoogleApiManager.this.zzju.get(this.zzhc).zzg(connectionResult);
        }
    }
}
