package com.appboy;

import android.os.*;
import com.appboy.lrucache.*;
import android.net.*;
import com.appboy.configuration.*;
import android.util.*;
import java.util.concurrent.*;
import java.io.*;
import com.appboy.models.cards.*;
import java.util.*;
import android.app.*;
import com.appboy.models.*;
import com.appboy.models.outgoing.*;
import com.appboy.support.*;
import java.math.*;
import bo.app.*;
import android.content.*;
import org.json.*;
import com.appboy.events.*;

public class Appboy implements IAppboy, IAppboyUnitySupport
{
    private static final Object A;
    private static volatile IAppboyEndpointProvider B;
    private static volatile IAppboyNotificationFactory C;
    private static volatile boolean D;
    private static volatile boolean E;
    private static volatile boolean F;
    private static volatile dq G;
    private static final String l;
    private static final Set<String> m;
    private static final Set<String> n;
    private static final Set<String> o;
    private static volatile Appboy p;
    volatile ad a;
    volatile ej b;
    volatile dl c;
    volatile fy d;
    volatile bi e;
    volatile dr f;
    volatile bl g;
    volatile dh h;
    final AppboyConfigurationProvider i;
    final bv j;
    final az k;
    private final Context q;
    private final ac r;
    private final bx s;
    private volatile AppboyUser t;
    private volatile ThreadPoolExecutor u;
    private final l v;
    private final bg w;
    private final ay x;
    private IAppboyImageLoader y;
    private volatile boolean z;
    
    static {
        l = AppboyLogger.getAppboyLogTag(Appboy.class);
        m = new HashSet<String>(Arrays.asList("AED", "AFN", "ALL", "AMD", "ANG", "AOA", "ARS", "AUD", "AWG", "AZN", "BAM", "BBD", "BDT", "BGN", "BHD", "BIF", "BMD", "BND", "BOB", "BRL", "BSD", "BTC", "BTN", "BWP", "BYR", "BZD", "CAD", "CDF", "CHF", "CLF", "CLP", "CNY", "COP", "CRC", "CUC", "CUP", "CVE", "CZK", "DJF", "DKK", "DOP", "DZD", "EEK", "EGP", "ERN", "ETB", "EUR", "FJD", "FKP", "GBP", "GEL", "GGP", "GHS", "GIP", "GMD", "GNF", "GTQ", "GYD", "HKD", "HNL", "HRK", "HTG", "HUF", "IDR", "ILS", "IMP", "INR", "IQD", "IRR", "ISK", "JEP", "JMD", "JOD", "JPY", "KES", "KGS", "KHR", "KMF", "KPW", "KRW", "KWD", "KYD", "KZT", "LAK", "LBP", "LKR", "LRD", "LSL", "LTL", "LVL", "LYD", "MAD", "MDL", "MGA", "MKD", "MMK", "MNT", "MOP", "MRO", "MTL", "MUR", "MVR", "MWK", "MXN", "MYR", "MZN", "NAD", "NGN", "NIO", "NOK", "NPR", "NZD", "OMR", "PAB", "PEN", "PGK", "PHP", "PKR", "PLN", "PYG", "QAR", "RON", "RSD", "RUB", "RWF", "SAR", "SBD", "SCR", "SDG", "SEK", "SGD", "SHP", "SLL", "SOS", "SRD", "STD", "SVC", "SYP", "SZL", "THB", "TJS", "TMT", "TND", "TOP", "TRY", "TTD", "TWD", "TZS", "UAH", "UGX", "USD", "UYU", "UZS", "VEF", "VND", "VUV", "WST", "XAF", "XAG", "XAU", "XCD", "XDR", "XOF", "XPD", "XPF", "XPT", "YER", "ZAR", "ZMK", "ZMW", "ZWL"));
        n = new HashSet<String>(Collections.singletonList("calypso appcrawler"));
        o = new HashSet<String>(Arrays.asList("android.permission.ACCESS_NETWORK_STATE", "android.permission.INTERNET"));
        Appboy.p = null;
        A = new Object();
        Appboy.D = false;
        Appboy.E = false;
        Appboy.F = false;
    }
    
    Appboy(final Context context) {
        this.z = false;
        final long nanoTime = System.nanoTime();
        AppboyLogger.d(Appboy.l, "Braze SDK Initializing");
        this.q = context.getApplicationContext();
        AppboyLogger.setTestUserDeviceLoggingManager(this.s = new bx());
        final String model = Build.MODEL;
        if (model != null && Appboy.n.contains(model.toLowerCase(Locale.US))) {
            final String l = Appboy.l;
            final StringBuilder sb = new StringBuilder();
            sb.append("Device build model matches a known crawler. Enabling mock network request mode. Device model: ");
            sb.append(model);
            AppboyLogger.i(l, sb.toString());
            enableMockAppboyNetworkRequestsAndDropEventsMode();
        }
        this.y = new AppboyLruImageLoader(this.q);
        final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(ei.a(), ei.b(), ei.c(), TimeUnit.SECONDS, ei.d(), new ThreadPoolExecutor.DiscardOldestPolicy());
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                AppboyLogger.checkForSystemLogLevelProperty();
            }
        });
        final AppboyConfigurationProvider i = new AppboyConfigurationProvider(this.q);
        this.i = i;
        if (!StringUtils.isNullOrBlank(i.getCustomEndpoint())) {
            this.a(this.i.getCustomEndpoint());
        }
        this.v = new l(this.q);
        this.w = new bg(this.q);
        this.r = new ac(threadPoolExecutor, Appboy.G);
        this.j = new bw(this.q, this.i);
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                if (Appboy.this.i.isFirebaseCloudMessagingRegistrationEnabled()) {
                    if (bp.a(Appboy.this.q, Appboy.this.i)) {
                        AppboyLogger.i(Appboy.l, "Firebase Cloud Messaging found. Setting up Firebase Cloud Messaging.");
                        new bp(context).a(Appboy.this.i.getFirebaseCloudMessagingSenderIdKey());
                    }
                    else {
                        AppboyLogger.e(Appboy.l, "Firebase Cloud Messaging requirements not met. Braze will not register for Firebase Cloud Messaging.");
                    }
                }
                else {
                    AppboyLogger.i(Appboy.l, "Automatic Firebase Cloud Messaging registration not enabled in configuration. Braze will not register for Firebase Cloud Messaging.");
                }
                Label_0219: {
                    if (Appboy.this.i.isGcmMessagingRegistrationEnabled()) {
                        String s;
                        String s2;
                        if (bq.a(Appboy.this.q, Appboy.this.i)) {
                            AppboyLogger.i(Appboy.l, "Google Cloud Messaging found. Setting up Google Cloud Messaging");
                            final bq bq = new bq(Appboy.this.q, Appboy.this.j);
                            final String gcmSenderId = Appboy.this.i.getGcmSenderId();
                            if (gcmSenderId != null) {
                                bq.a(gcmSenderId);
                                break Label_0219;
                            }
                            s = Appboy.l;
                            s2 = "GCM Sender Id not found, not registering with GCM Server";
                        }
                        else {
                            s = Appboy.l;
                            s2 = "GCM manifest requirements not met. Braze will not register for GCM.";
                        }
                        AppboyLogger.e(s, s2);
                    }
                    else {
                        AppboyLogger.i(Appboy.l, "Automatic GCM registration not enabled in configuration. Braze will not register for GCM.");
                    }
                }
                if (!Appboy.this.i.isAdmMessagingRegistrationEnabled()) {
                    AppboyLogger.i(Appboy.l, "Automatic ADM registration not enabled in configuration. Braze will not register for ADM.");
                    return;
                }
                if (bf.a(Appboy.this.q)) {
                    AppboyLogger.i(Appboy.l, "Amazon Device Messaging found. Setting up Amazon Device Messaging");
                    new bf(Appboy.this.q, Appboy.this.j).a();
                    return;
                }
                AppboyLogger.e(Appboy.l, "ADM manifest requirements not met. Braze will not register for ADM.");
            }
        });
        final ax ax = new ax("Appboy-User-Dependency-Thread");
        ax.a(this.x = new ay(this.r));
        (this.k = new az(ax)).submit(new Runnable() {
            @Override
            public void run() {
                AppboyLogger.v(Appboy.l, "Starting up a new user dependency manager");
                Appboy.this.a(new ej(Appboy.this.q, Appboy.this.v, Appboy.this.i, Appboy.this.r, Appboy.this.w, Appboy.this.j, Appboy.D, Appboy.E, Appboy.this.s));
            }
        });
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Appboy.this.g();
            }
        });
        final long nanoTime2 = System.nanoTime();
        final String j = Appboy.l;
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Appboy loaded in ");
        sb2.append(TimeUnit.MILLISECONDS.convert(nanoTime2 - nanoTime, TimeUnit.NANOSECONDS));
        sb2.append(" ms.");
        AppboyLogger.d(j, sb2.toString());
    }
    
    private static dq a(final Context context) {
        if (Appboy.G == null) {
            Appboy.G = new dq(context);
        }
        return Appboy.G;
    }
    
    private void a(final ej b) {
        this.b = b;
        this.g = b.d();
        this.f = b.a();
        this.d = b.l();
        this.e = b.m();
        this.h = b.n();
        this.t = new AppboyUser(b.g(), this.g, this.v.a(), b.j(), this.f);
        b.c().a(b.f());
        b.e().a();
        this.a = b.f();
        this.x.a(this.a);
        this.u = b.h();
        this.c = b.i();
        this.d = b.l();
        b.k().a(this.u, b.e());
        this.s.a(this.g);
        this.s.a(this.f.o());
    }
    
    private void a(final String s) {
        synchronized (Appboy.A) {
            setAppboyEndpointProvider(new IAppboyEndpointProvider() {
                @Override
                public Uri getApiEndpoint(final Uri uri) {
                    return uri.buildUpon().encodedAuthority(s).build();
                }
            });
        }
    }
    
    private void a(final Throwable t) {
        try {
            this.a.a(t, Throwable.class);
        }
        catch (Exception ex) {
            AppboyLogger.e(Appboy.l, "Failed to log throwable.", ex);
        }
    }
    
    private void b(final boolean b) {
        this.k.submit(new Runnable() {
            @Override
            public void run() {
                Appboy.this.g.a(b);
                Appboy.this.b.b().a(b);
                if (Appboy.this.y != null) {
                    final String b = Appboy.l;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Setting the image loader deny network downloads to ");
                    sb.append(b);
                    AppboyLogger.d(b, sb.toString());
                    Appboy.this.y.setOffline(b);
                }
            }
        });
    }
    
    public static void clearAppboyEndpointProvider() {
        synchronized (Appboy.A) {
            Appboy.B = null;
        }
    }
    
    public static boolean configure(final Context context, final AppboyConfig appboyConfig) {
        String s;
        String s2;
        if (Appboy.p == null) {
            if (!Appboy.F) {
                synchronized (Appboy.class) {
                    if (Appboy.p != null || Appboy.F) {
                        AppboyLogger.i(Appboy.l, "Appboy.configure() can only be called once during the lifetime of the singleton.");
                        return false;
                    }
                    final m m = new m(context.getApplicationContext());
                    if (appboyConfig != null) {
                        Appboy.F = true;
                        m.a(appboyConfig);
                        return true;
                    }
                    AppboyLogger.i(Appboy.l, "Appboy.configure() called with a null config; Clearing all configuration values.");
                    m.a();
                    return true;
                }
            }
            s = Appboy.l;
            s2 = "Appboy.configure() can only be called once during the lifetime of the singleton.";
        }
        else {
            s = Appboy.l;
            s2 = "Appboy.configure() must be called before the first call to Appboy.getInstance()";
        }
        AppboyLogger.w(s, s2);
        return false;
    }
    
    public static void disableSdk(final Context context) {
        a(context).a(true);
        Log.w(Appboy.l, "Stopping the SDK instance.");
        h();
        AppboyLogger.w(Appboy.l, "Disabling all network requests");
        setOutboundNetworkRequestsOffline(true);
    }
    
    public static boolean enableMockAppboyNetworkRequestsAndDropEventsMode() {
        if (Appboy.p == null) {
            synchronized (Appboy.class) {
                if (Appboy.p == null) {
                    if (Appboy.D) {
                        AppboyLogger.i(Appboy.l, "Appboy network requests already being mocked. Note that events dispatched in this mode are dropped.");
                        return true;
                    }
                    AppboyLogger.i(Appboy.l, "Appboy network requests will be mocked. Events dispatched in this mode will be dropped.");
                    return Appboy.D = true;
                }
            }
        }
        AppboyLogger.e(Appboy.l, "Attempt to enable mocking Braze network requests had no effect since getInstance() has already been called.");
        return false;
    }
    
    public static void enableSdk(final Context context) {
        Log.w(Appboy.l, "Setting SDK to enabled.");
        a(context).a(false);
        AppboyLogger.w(Appboy.l, "Enabling all network requests");
        setOutboundNetworkRequestsOffline(false);
    }
    
    private ContentCardsUpdatedEvent f() {
        if (i()) {
            return null;
        }
        try {
            return this.k.submit((Callable<ContentCardsUpdatedEvent>)new Callable<ContentCardsUpdatedEvent>() {
                public ContentCardsUpdatedEvent a() {
                    return Appboy.this.h.a();
                }
            }).get();
        }
        catch (Exception ex) {
            AppboyLogger.w(Appboy.l, "Failed to retrieve the cached ContentCardsUpdatedEvent.", ex);
            this.a(ex);
            return null;
        }
    }
    
    private void g() {
        final Iterator<String> iterator = Appboy.o.iterator();
        final boolean b = false;
        boolean b2 = true;
        while (iterator.hasNext()) {
            final String s = iterator.next();
            if (!PermissionUtils.hasPermission(this.q, s)) {
                final String l = Appboy.l;
                final StringBuilder sb = new StringBuilder();
                sb.append("The Braze SDK requires the permission ");
                sb.append(s);
                sb.append(". Check your app manifest.");
                AppboyLogger.e(l, sb.toString());
                b2 = false;
            }
        }
        if (this.i.getAppboyApiKey().toString().equals("")) {
            AppboyLogger.e(Appboy.l, "The Braze SDK requires a non-empty API key. Check your appboy.xml or AppboyConfig.");
            b2 = false;
        }
        if (this.i.isFirebaseCloudMessagingRegistrationEnabled() && this.i.isGcmMessagingRegistrationEnabled()) {
            AppboyLogger.e(Appboy.l, "Both Firebase Cloud Messaging and Google Cloud Messaging automatic push registration are enabled. It is recommended to only have one automatic push registration active.");
            b2 = b;
        }
        if (!b2) {
            AppboyLogger.e(Appboy.l, "The Braze SDK is not integrated correctly. Please visit https://www.braze.com/documentation/Android");
        }
    }
    
    public static Uri getAppboyApiEndpoint(final Uri uri) {
        synchronized (Appboy.A) {
            if (Appboy.B != null) {
                try {
                    final Uri apiEndpoint = Appboy.B.getApiEndpoint(uri);
                    if (apiEndpoint != null) {
                        return apiEndpoint;
                    }
                }
                catch (Exception ex) {
                    AppboyLogger.e(Appboy.l, "Caught exception trying to get a Braze API endpoint from the AppboyEndpointProvider. Using the original URI");
                }
            }
            return uri;
        }
    }
    
    public static IAppboyNotificationFactory getCustomAppboyNotificationFactory() {
        return Appboy.C;
    }
    
    public static Appboy getInstance(final Context context) {
        Label_0039: {
            if (Appboy.p != null && !Appboy.p.z) {
                break Label_0039;
            }
            synchronized (Appboy.class) {
                if (Appboy.p != null && !Appboy.p.z) {
                    return Appboy.p;
                }
                setOutboundNetworkRequestsOffline(a(context).a());
                return Appboy.p = new Appboy(context);
            }
        }
    }
    
    public static boolean getOutboundNetworkRequestsOffline() {
        return Appboy.E;
    }
    
    private static void h() {
        try {
            AppboyLogger.i(Appboy.l, "Shutting down all queued work on the Braze SDK");
            synchronized (Appboy.class) {
                if (Appboy.p != null) {
                    if (Appboy.p.k != null) {
                        AppboyLogger.d(Appboy.l, "Shutting down the user dependency executor");
                        Appboy.p.k.shutdownNow();
                    }
                    final ej b = Appboy.p.b;
                    if (b != null) {
                        if (b.b() != null) {
                            b.b().a(true);
                        }
                        if (b.k() != null) {
                            b.k().a();
                        }
                        if (b.m() != null) {
                            b.m().b();
                        }
                        if (b.j() != null) {
                            b.j().a();
                        }
                    }
                    Appboy.p.z = true;
                }
            }
        }
        catch (Exception ex) {
            AppboyLogger.w(Appboy.l, "Failed to shutdown queued work on the Braze SDK.", ex);
        }
    }
    
    private static boolean i() {
        if (Appboy.G == null) {
            AppboyLogger.d(Appboy.l, "SDK enablement provider was null. Returning SDK as enabled.");
            return false;
        }
        final boolean a = Appboy.G.a();
        if (a) {
            AppboyLogger.w(Appboy.l, "SDK is disabled. Not performing action on SDK.");
        }
        return a;
    }
    
    public static void setAppboyEndpointProvider(final IAppboyEndpointProvider b) {
        synchronized (Appboy.A) {
            Appboy.B = b;
        }
    }
    
    public static void setCustomAppboyNotificationFactory(final IAppboyNotificationFactory c) {
        AppboyLogger.d(Appboy.l, "Custom Braze notification factory set");
        Appboy.C = c;
    }
    
    public static void setOutboundNetworkRequestsOffline(final boolean e) {
        final String l = Appboy.l;
        final StringBuilder sb = new StringBuilder();
        sb.append("Appboy outbound network requests are now ");
        String s;
        if (e) {
            s = "disabled";
        }
        else {
            s = "enabled";
        }
        sb.append(s);
        AppboyLogger.i(l, sb.toString());
        synchronized (Appboy.class) {
            Appboy.E = e;
            if (Appboy.p != null) {
                Appboy.p.b(e);
            }
        }
    }
    
    public static void wipeData(final Context context) {
        h();
        try {
            fx.a(context);
            AppboyLruImageLoader.deleteStoredData(context);
        }
        catch (Exception ex) {
            AppboyLogger.w(Appboy.l, "Failed to delete data from the internal storage cache.", ex);
        }
        try {
            df.a(context);
        }
        catch (Exception ex2) {
            AppboyLogger.w(Appboy.l, "Failed to delete Braze database files for the Braze SDK.", ex2);
        }
        try {
            final File file = new File(context.getApplicationInfo().dataDir, "shared_prefs");
            if (file.exists() && file.isDirectory()) {
                final File[] listFiles = file.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(final File file, final String s) {
                        return s.startsWith("com.appboy");
                    }
                });
                for (int length = listFiles.length, i = 0; i < length; ++i) {
                    final File file2 = listFiles[i];
                    final String l = Appboy.l;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Deleting shared prefs file at: ");
                    sb.append(file2.getAbsolutePath());
                    AppboyLogger.v(l, sb.toString());
                    AppboyFileUtils.deleteFileOrDirectory(file2);
                }
            }
        }
        catch (Exception ex3) {
            AppboyLogger.w(Appboy.l, "Failed to delete shared preference data for the Braze SDK.", ex3);
        }
    }
    
    void a() {
        if (i()) {
            return;
        }
        this.k.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    if (Appboy.this.e != null) {
                        Appboy.this.e.a();
                        return;
                    }
                    AppboyLogger.d(Appboy.l, "Geofence manager was null. Not initializing geofences.");
                }
                catch (Exception ex) {
                    AppboyLogger.w(Appboy.l, "Failed to initialize geofences with the geofence manager.", ex);
                    Appboy.this.a(ex);
                }
            }
        });
    }
    
    void a(final cb cb) {
        if (i()) {
            return;
        }
        this.k.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    if (Appboy.this.e != null) {
                        Appboy.this.e.a(cb);
                        return;
                    }
                    AppboyLogger.d(Appboy.l, "Geofence manager was null. Not requesting geofence refresh.");
                }
                catch (Exception ex) {
                    AppboyLogger.w(Appboy.l, "Failed to request geofence refresh.", ex);
                    Appboy.this.a(ex);
                }
            }
        });
    }
    
    void a(final String s, final x x) {
        if (i()) {
            return;
        }
        this.k.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    if (Appboy.this.e != null) {
                        Appboy.this.e.b(s, x);
                        return;
                    }
                    AppboyLogger.d(Appboy.l, "Geofence manager was null. Not posting geofence report");
                }
                catch (Exception ex) {
                    AppboyLogger.w(Appboy.l, "Failed to post geofence report.", ex);
                    Appboy.this.a(ex);
                }
            }
        });
    }
    
    void a(final String s, final String s2) {
        if (i()) {
            return;
        }
        if (StringUtils.isNullOrBlank(s)) {
            final String l = Appboy.l;
            final StringBuilder sb = new StringBuilder();
            sb.append("Cannot add null or blank card json to storage. Returning. User id: ");
            sb.append(s2);
            sb.append(" Serialized json: ");
            sb.append(s);
            AppboyLogger.w(l, sb.toString());
            return;
        }
        this.k.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Appboy.this.h.a(new cn(s), s2);
                }
                catch (Exception ex) {
                    final String b = Appboy.l;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Failed to update ContentCard storage provider with single card update. User id: ");
                    sb.append(s2);
                    sb.append(" Serialized json: ");
                    sb.append(s);
                    AppboyLogger.e(b, sb.toString(), ex);
                }
            }
        });
    }
    
    void a(final boolean b) {
        if (i()) {
            return;
        }
        this.k.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    if (Appboy.this.e != null) {
                        Appboy.this.e.b(b);
                        return;
                    }
                    AppboyLogger.d(Appboy.l, "Geofence manager was null. Not requesting geofence refresh.");
                }
                catch (Exception ex) {
                    final String b = Appboy.l;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Failed to request geofence refresh with rate limit ignore: ");
                    sb.append(b);
                    AppboyLogger.w(b, sb.toString(), ex);
                    Appboy.this.a(ex);
                }
            }
        });
    }
    
    @Override
    public void changeUser(final String s) {
        if (i()) {
            return;
        }
        this.k.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    if (StringUtils.isNullOrEmpty(s)) {
                        AppboyLogger.e(Appboy.l, "ArgumentException: userId passed to changeUser was null or empty. The current user will remain the active user.");
                        return;
                    }
                    final String userId = Appboy.this.t.getUserId();
                    if (userId.equals(s)) {
                        final String b = Appboy.l;
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Received request to change current user ");
                        sb.append(s);
                        sb.append(" to the same user id. Doing nothing.");
                        AppboyLogger.i(b, sb.toString());
                        return;
                    }
                    if (userId.equals("")) {
                        final String b2 = Appboy.l;
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("Changing anonymous user to ");
                        sb2.append(s);
                        AppboyLogger.i(b2, sb2.toString());
                        Appboy.this.v.b(s);
                        Appboy.this.t.a(s);
                    }
                    else {
                        final String b3 = Appboy.l;
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("Changing current user ");
                        sb3.append(userId);
                        sb3.append(" to new user ");
                        sb3.append(s);
                        sb3.append(".");
                        AppboyLogger.i(b3, sb3.toString());
                        Appboy.this.r.a(new FeedUpdatedEvent(new ArrayList<Card>(), s, false, du.a()), FeedUpdatedEvent.class);
                    }
                    Appboy.this.g.c();
                    Appboy.this.v.a(s);
                    final ej b4 = Appboy.this.b;
                    Appboy.this.a(new ej(Appboy.this.q, Appboy.this.v, Appboy.this.i, Appboy.this.r, Appboy.this.w, Appboy.this.j, Appboy.D, Appboy.E, Appboy.this.s));
                    Appboy.this.b.g().d();
                    Appboy.this.g.a();
                    Appboy.this.g.a(new ck.a().a());
                    Appboy.this.requestContentCardsRefresh(false);
                    b4.o();
                }
                catch (Exception ex) {
                    final String b5 = Appboy.l;
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("Failed to set external id to: ");
                    sb4.append(s);
                    AppboyLogger.w(b5, sb4.toString(), ex);
                    Appboy.this.a(ex);
                }
            }
        });
    }
    
    @Override
    public void closeSession(final Activity activity) {
        if (i()) {
            return;
        }
        this.k.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    if (activity == null) {
                        AppboyLogger.w(Appboy.l, "Cannot close session with null activity.");
                        return;
                    }
                    final cd b = Appboy.this.g.b(activity);
                    if (b != null) {
                        final String b2 = Appboy.l;
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Closed session with ID: ");
                        sb.append(b.a());
                        AppboyLogger.i(b2, sb.toString());
                    }
                }
                catch (Exception ex) {
                    AppboyLogger.w(Appboy.l, "Failed to close session.", ex);
                    Appboy.this.a(ex);
                }
            }
        });
    }
    
    @Override
    public IInAppMessage deserializeInAppMessageString(final String s) {
        return eb.a(s, this.g);
    }
    
    @Override
    public IAppboyImageLoader getAppboyImageLoader() {
        if (this.y == null) {
            AppboyLogger.d(Appboy.l, "The Image Loader was null. Creating a new Image Loader and returning it.");
            this.y = new AppboyLruImageLoader(this.q);
        }
        return this.y;
    }
    
    @Override
    public String getAppboyPushMessageRegistrationId() {
        if (i()) {
            return "";
        }
        try {
            return this.j.a();
        }
        catch (Exception ex) {
            AppboyLogger.w(Appboy.l, "Failed to get the registration ID.", ex);
            this.a(ex);
            return null;
        }
    }
    
    @Override
    public int getContentCardCount() {
        if (i()) {
            return -1;
        }
        final ContentCardsUpdatedEvent f = this.f();
        if (f != null) {
            return f.getCardCount();
        }
        AppboyLogger.w(Appboy.l, "The ContentCardsUpdatedEvent was null. Returning the default value for the card count.");
        return -1;
    }
    
    @Override
    public int getContentCardUnviewedCount() {
        if (i()) {
            return -1;
        }
        final ContentCardsUpdatedEvent f = this.f();
        if (f != null) {
            return f.getUnviewedCardCount();
        }
        AppboyLogger.w(Appboy.l, "The ContentCardsUpdatedEvent was null. Returning the default value for the unviewed card count.");
        return -1;
    }
    
    @Override
    public long getContentCardsLastUpdatedInSecondsFromEpoch() {
        if (i()) {
            return -1L;
        }
        final ContentCardsUpdatedEvent f = this.f();
        if (f != null) {
            return f.getLastUpdatedInSecondsFromEpoch();
        }
        AppboyLogger.w(Appboy.l, "The ContentCardsUpdatedEvent was null. Returning the default value for the last update timestamp.");
        return -1L;
    }
    
    @Override
    public AppboyUser getCurrentUser() {
        try {
            return this.k.submit((Callable<AppboyUser>)new Callable<AppboyUser>() {
                public AppboyUser a() {
                    return Appboy.this.t;
                }
            }).get();
        }
        catch (Exception ex) {
            AppboyLogger.w(Appboy.l, "Failed to retrieve the current user.", ex);
            this.a(ex);
            return null;
        }
    }
    
    public String getDeviceId() {
        return this.w.a();
    }
    
    @Override
    public String getInstallTrackingId() {
        if (i()) {
            return "";
        }
        return this.w.a();
    }
    
    @Override
    public void logContentCardsDisplayed() {
        if (i()) {
            return;
        }
        this.k.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Appboy.this.g.a(cg.g());
                }
                catch (Exception ex) {
                    AppboyLogger.w(Appboy.l, "Failed to log that Content Cards was displayed.", ex);
                    Appboy.this.a(ex);
                }
            }
        });
    }
    
    @Override
    public void logCustomEvent(final String s) {
        this.logCustomEvent(s, null);
    }
    
    @Override
    public void logCustomEvent(final String s, final AppboyProperties appboyProperties) {
        if (i()) {
            return;
        }
        this.k.submit(new Runnable() {
            @Override
            public void run() {
                Object o = s;
                String b = null;
                try {
                    if (!ValidationUtils.isValidLogCustomEventInput((String)o, Appboy.this.f)) {
                        b = Appboy.l;
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Log custom event input ");
                        sb.append((String)o);
                        sb.append(" was invalid. Not logging custom event to Appboy.");
                        AppboyLogger.w(b, sb.toString());
                        return;
                    }
                    final String ensureAppboyFieldLength = ValidationUtils.ensureAppboyFieldLength((String)o);
                    try {
                        o = cg.a(ensureAppboyFieldLength, appboyProperties);
                        if (Appboy.this.g.a((ca)o)) {
                            Appboy.this.d.a(new fj(ensureAppboyFieldLength, appboyProperties, (ca)o));
                        }
                        return;
                    }
                    catch (Exception b) {
                        o = ensureAppboyFieldLength;
                    }
                }
                catch (Exception ex) {}
                final String b2 = Appboy.l;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Failed to log custom event: ");
                sb2.append((String)o);
                AppboyLogger.w(b2, sb2.toString(), (Throwable)b);
                Appboy.this.a((Throwable)b);
            }
        });
    }
    
    @Override
    public void logFeedCardClick(final String s) {
        if (i()) {
            return;
        }
        this.k.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    if (StringUtils.isNullOrBlank(s)) {
                        AppboyLogger.e(Appboy.l, "Card ID cannot be null or blank.");
                        return;
                    }
                    Appboy.this.g.a(cg.d(s));
                }
                catch (Exception ex) {
                    final String b = Appboy.l;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Failed to log feed card clicked. Card id: ");
                    sb.append(s);
                    AppboyLogger.w(b, sb.toString(), ex);
                    Appboy.this.a(ex);
                }
            }
        });
    }
    
    @Override
    public void logFeedCardImpression(final String s) {
        if (i()) {
            return;
        }
        this.k.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    if (StringUtils.isNullOrBlank(s)) {
                        AppboyLogger.e(Appboy.l, "Card ID cannot be null or blank.");
                        return;
                    }
                    Appboy.this.g.a(cg.c(s));
                    Appboy.this.c.b(s);
                }
                catch (Exception ex) {
                    final String b = Appboy.l;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Failed to log feed card impression. Card id: ");
                    sb.append(s);
                    AppboyLogger.w(b, sb.toString(), ex);
                    Appboy.this.a(ex);
                }
            }
        });
    }
    
    @Override
    public void logFeedDisplayed() {
        if (i()) {
            return;
        }
        this.k.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Appboy.this.g.a(cg.h());
                }
                catch (Exception ex) {
                    AppboyLogger.w(Appboy.l, "Failed to log that the feed was displayed.", ex);
                    Appboy.this.a(ex);
                }
            }
        });
    }
    
    @Override
    public void logFeedbackDisplayed() {
        if (i()) {
            return;
        }
        this.k.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Appboy.this.g.a(cg.i());
                }
                catch (Exception ex) {
                    AppboyLogger.w(Appboy.l, "Failed to log that feedback was displayed.", ex);
                    Appboy.this.a(ex);
                }
            }
        });
    }
    
    @Override
    public void logPurchase(final String s, final String s2, final BigDecimal bigDecimal) {
        this.logPurchase(s, s2, bigDecimal, 1);
    }
    
    @Override
    public void logPurchase(final String s, final String s2, final BigDecimal bigDecimal, final int n) {
        this.logPurchase(s, s2, bigDecimal, n, null);
    }
    
    @Override
    public void logPurchase(final String s, final String s2, final BigDecimal bigDecimal, final int n, final AppboyProperties appboyProperties) {
        if (i()) {
            return;
        }
        this.k.submit(new Runnable() {
            @Override
            public void run() {
                Object o = s;
                final String b = s2;
                Label_0058: {
                    if (b != null) {
                        break Label_0058;
                    }
                    while (true) {
                        try {
                            final String b2 = Appboy.l;
                            final StringBuilder sb = new StringBuilder();
                            sb.append("The currencyCode is null. Expected one of ");
                            sb.append(Appboy.m);
                            sb.append(". Not logging in-app purchase to Appboy.");
                            AppboyLogger.w(b2, sb.toString());
                            return;
                            final String ensureAppboyFieldLength;
                            Label_0105: {
                                ensureAppboyFieldLength = ValidationUtils.ensureAppboyFieldLength((String)o);
                            }
                            try {
                                final String upperCase;
                                o = cg.a(ensureAppboyFieldLength, upperCase, bigDecimal, n, appboyProperties);
                                if (Appboy.this.g.a((ca)o)) {
                                    Appboy.this.d.a(new fo(ensureAppboyFieldLength, appboyProperties, (ca)o));
                                }
                                return;
                            }
                            catch (Exception ex2) {
                                o = ensureAppboyFieldLength;
                                final Exception ex = ex2;
                            }
                            break Label_0171;
                            final String upperCase = b.trim().toUpperCase(Locale.US);
                            // iftrue(Label_0105:, ValidationUtils.isValidLogPurchaseInput((String)o, upperCase, this.c, this.d, this.f.f, (Set<String>)Appboy.e()))
                            AppboyLogger.w(Appboy.l, "Log purchase input was invalid. Not logging in-app purchase to Appboy.");
                            return;
                            final String b3 = Appboy.l;
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("Failed to log purchase event of ");
                            sb2.append((String)o);
                            Exception ex = null;
                            AppboyLogger.w(b3, sb2.toString(), ex);
                            Appboy.this.a(ex);
                        }
                        catch (Exception ex) {
                            continue;
                        }
                        break;
                    }
                }
            }
        });
    }
    
    @Override
    public void logPurchase(final String s, final String s2, final BigDecimal bigDecimal, final AppboyProperties appboyProperties) {
        this.logPurchase(s, s2, bigDecimal, 1, appboyProperties);
    }
    
    @Override
    public void logPushDeliveryEvent(final String s) {
        if (i()) {
            return;
        }
        this.k.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    if (StringUtils.isNullOrBlank(s)) {
                        AppboyLogger.w(Appboy.l, "Campaign ID cannot be null or blank for push delivery event.");
                        return;
                    }
                    Appboy.this.g.a(cg.j(s));
                }
                catch (Exception ex) {
                    AppboyLogger.w(Appboy.l, "Failed to log push delivery event.", ex);
                    Appboy.this.a(ex);
                }
            }
        });
    }
    
    @Override
    public void logPushNotificationActionClicked(final String s, final String s2) {
        if (i()) {
            return;
        }
        this.k.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    if (StringUtils.isNullOrBlank(s)) {
                        AppboyLogger.w(Appboy.l, "Campaign ID cannot be null or blank. Not logging push notification action clicked.");
                        return;
                    }
                    if (StringUtils.isNullOrBlank(s2)) {
                        AppboyLogger.w(Appboy.l, "Action ID cannot be null or blank");
                        return;
                    }
                    Appboy.this.g.a(cg.c(s, s2));
                }
                catch (Exception ex) {
                    AppboyLogger.w(Appboy.l, "Failed to log push notification action clicked.", ex);
                    Appboy.this.a(ex);
                }
            }
        });
    }
    
    @Override
    public void logPushNotificationOpened(final Intent intent) {
        if (i()) {
            return;
        }
        this.k.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    final String stringExtra = intent.getStringExtra("cid");
                    if (!StringUtils.isNullOrBlank(stringExtra)) {
                        final String b = Appboy.l;
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Logging push click to Appboy. Campaign Id: ");
                        sb.append(stringExtra);
                        AppboyLogger.i(b, sb.toString());
                        Appboy.this.logPushNotificationOpened(stringExtra);
                    }
                    else {
                        AppboyLogger.i(Appboy.l, "No campaign Id associated with this notification. Not logging push click to Appboy.");
                    }
                    if (intent.hasExtra("ab_push_fetch_test_triggers_key") && intent.getStringExtra("ab_push_fetch_test_triggers_key").equals("true")) {
                        AppboyLogger.i(Appboy.l, "Push contained key for fetching test triggers, fetching triggers.");
                        Appboy.this.g.a(new ck.a().b());
                    }
                }
                catch (Exception ex) {
                    AppboyLogger.w(Appboy.l, "Error logging push notification", ex);
                }
            }
        });
    }
    
    @Override
    public void logPushNotificationOpened(final String s) {
        if (i()) {
            return;
        }
        this.k.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    if (StringUtils.isNullOrBlank(s)) {
                        AppboyLogger.w(Appboy.l, "Campaign ID cannot be null or blank. Not logging push notification opened.");
                        return;
                    }
                    Appboy.this.g.a(cg.b(s));
                }
                catch (Exception ex) {
                    AppboyLogger.w(Appboy.l, "Failed to log opened push.", ex);
                    Appboy.this.a(ex);
                }
            }
        });
    }
    
    @Override
    public void logPushStoryPageClicked(final String s, final String s2) {
        if (i()) {
            return;
        }
        this.k.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!ValidationUtils.isValidPushStoryClickInput(s, s2)) {
                        AppboyLogger.w(Appboy.l, "Push story page click input was invalid. Not logging in-app purchase to Appboy.");
                        return;
                    }
                    Appboy.this.g.a(cg.b(s, s2));
                }
                catch (Exception ex) {
                    final String b = Appboy.l;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Failed to log push story page clicked for page id: ");
                    sb.append(s2);
                    sb.append(" cid: ");
                    sb.append(s);
                    AppboyLogger.w(b, sb.toString(), ex);
                    Appboy.this.a(ex);
                }
            }
        });
    }
    
    @Override
    public void openSession(final Activity activity) {
        if (i()) {
            return;
        }
        this.k.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    if (activity == null) {
                        AppboyLogger.w(Appboy.l, "Cannot open session with null activity.");
                        return;
                    }
                    Appboy.this.g.a(activity);
                }
                catch (Exception ex) {
                    AppboyLogger.e(Appboy.l, "Failed to open session.", ex);
                    Appboy.this.a(ex);
                }
            }
        });
    }
    
    @Override
    public void registerAppboyPushMessages(final String s) {
        if (i()) {
            return;
        }
        try {
            if (StringUtils.isNullOrBlank(s)) {
                AppboyLogger.w(Appboy.l, "Push registration ID must not be null or blank. Not registering for push messages from Appboy.");
                return;
            }
            final String l = Appboy.l;
            final StringBuilder sb = new StringBuilder();
            sb.append("Push token ");
            sb.append(s);
            sb.append(" registered and immediately being flushed.");
            AppboyLogger.i(l, sb.toString());
            this.j.a(s);
            this.requestImmediateDataFlush();
        }
        catch (Exception ex) {
            AppboyLogger.w(Appboy.l, "Failed to set the registration ID.", ex);
            this.a(ex);
        }
    }
    
    @Override
    public <T> void removeSingleSubscription(final IEventSubscriber<T> eventSubscriber, final Class<T> clazz) {
        try {
            this.r.c(eventSubscriber, clazz);
        }
        catch (Exception ex) {
            final String l = Appboy.l;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to remove ");
            sb.append(clazz.getName());
            sb.append(" subscriber.");
            AppboyLogger.w(l, sb.toString(), ex);
            this.a(ex);
        }
    }
    
    @Override
    public void requestContentCardsRefresh(final boolean b) {
        if (i()) {
            return;
        }
        this.k.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    if (b) {
                        Appboy.this.r.a(Appboy.this.h.a(), ContentCardsUpdatedEvent.class);
                        return;
                    }
                    Appboy.this.g.a(Appboy.this.h.b(), Appboy.this.h.c());
                }
                catch (Exception ex) {
                    final String b = Appboy.l;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Failed to request Content Cards refresh. Requesting from cache: ");
                    sb.append(b);
                    AppboyLogger.w(b, sb.toString(), ex);
                    Appboy.this.a(ex);
                }
            }
        });
    }
    
    @Override
    public void requestFeedRefresh() {
        if (i()) {
            return;
        }
        this.k.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Appboy.this.g.a(new ck.a().a());
                }
                catch (Exception ex) {
                    AppboyLogger.w(Appboy.l, "Failed to request refresh of feed.", ex);
                    Appboy.this.a(ex);
                }
            }
        });
    }
    
    @Override
    public void requestFeedRefreshFromCache() {
        if (i()) {
            return;
        }
        this.k.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Appboy.this.r.a(Appboy.this.c.a(), FeedUpdatedEvent.class);
                }
                catch (JSONException ex) {
                    AppboyLogger.w(Appboy.l, "Failed to retrieve and publish feed from offline cache.", (Throwable)ex);
                }
            }
        });
    }
    
    @Override
    public void requestImmediateDataFlush() {
        if (i()) {
            return;
        }
        this.k.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Appboy.this.g.d();
                }
                catch (Exception ex) {
                    AppboyLogger.w(Appboy.l, "Failed to request data flush.", ex);
                    Appboy.this.a(ex);
                }
            }
        });
    }
    
    @Override
    public void setAppboyImageLoader(final IAppboyImageLoader y) {
        if (this.y == null) {
            AppboyLogger.w(Appboy.l, "The Image Loader cannot be set to null. Doing nothing.");
            return;
        }
        this.y = y;
    }
    
    @Override
    public void submitFeedback(final String s, final String s2, final boolean b) {
        if (i()) {
            return;
        }
        this.k.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Appboy.this.g.a(s, s2, b);
                }
                catch (Exception ex) {
                    final String b = Appboy.l;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Failed to submit feedback: ");
                    sb.append(s2);
                    AppboyLogger.w(b, sb.toString(), ex);
                    Appboy.this.a(ex);
                }
            }
        });
    }
    
    @Override
    public void subscribeToContentCardsUpdates(final IEventSubscriber<ContentCardsUpdatedEvent> eventSubscriber) {
        try {
            this.r.a(eventSubscriber, ContentCardsUpdatedEvent.class);
        }
        catch (Exception ex) {
            AppboyLogger.w(Appboy.l, "Failed to add subscriber for Content Cards updates.", ex);
            this.a(ex);
        }
    }
    
    @Override
    public void subscribeToFeedUpdates(final IEventSubscriber<FeedUpdatedEvent> eventSubscriber) {
        try {
            this.r.a(eventSubscriber, FeedUpdatedEvent.class);
        }
        catch (Exception ex) {
            AppboyLogger.w(Appboy.l, "Failed to add subscriber for feed updates.", ex);
            this.a(ex);
        }
    }
    
    @Override
    public void subscribeToFeedbackRequestEvents(final IEventSubscriber<SubmitFeedbackSucceeded> eventSubscriber, final IEventSubscriber<SubmitFeedbackFailed> eventSubscriber2) {
        while (true) {
            if (eventSubscriber != null) {
                while (true) {
                    try {
                        this.r.a(eventSubscriber, SubmitFeedbackSucceeded.class);
                        if (eventSubscriber2 != null) {
                            this.r.a(eventSubscriber2, SubmitFeedbackFailed.class);
                            return;
                        }
                        return;
                        final Exception ex;
                        AppboyLogger.w(Appboy.l, "Failed to add subscribers for feedback request events.", ex);
                        this.a(ex);
                        return;
                    }
                    catch (Exception ex2) {}
                    final Exception ex2;
                    final Exception ex = ex2;
                    continue;
                }
            }
            continue;
        }
    }
    
    @Override
    public void subscribeToNewInAppMessages(final IEventSubscriber<InAppMessageEvent> eventSubscriber) {
        try {
            this.r.a(eventSubscriber, InAppMessageEvent.class);
        }
        catch (Exception ex) {
            AppboyLogger.w(Appboy.l, "Failed to add subscriber to new in-app messages.", ex);
            this.a(ex);
        }
    }
}
