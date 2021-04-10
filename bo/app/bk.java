package bo.app;

import com.appboy.configuration.*;
import android.content.*;
import com.appboy.services.*;
import android.location.*;
import com.appboy.support.*;
import android.app.*;

public final class bk implements bu
{
    private static final String a;
    private final Context b;
    private final String c;
    private final LocationManager d;
    private final br e;
    private final boolean f;
    private final boolean g;
    private boolean h;
    private long i;
    private float j;
    private String k;
    
    static {
        a = AppboyLogger.getAppboyLogTag(bk.class);
    }
    
    public bk(final Context b, final br e, final AppboyConfigurationProvider appboyConfigurationProvider, final dr dr) {
        this.h = false;
        this.i = 3600000L;
        this.j = 50.0f;
        this.b = b;
        this.c = b.getPackageName();
        this.e = e;
        this.d = (LocationManager)b.getSystemService("location");
        this.f = a(appboyConfigurationProvider);
        this.h = this.b(appboyConfigurationProvider, dr);
        this.g = this.f();
        this.a(appboyConfigurationProvider, dr);
        final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                if (intent == null) {
                    AppboyLogger.e(bk.a, "Location broadcast receiver received null intent.");
                    return;
                }
                final String action = intent.getAction();
                if (action.endsWith(".SINGLE_APPBOY_LOCATION_UPDATE")) {
                    bk.this.a(intent);
                    return;
                }
                if (action.endsWith(".REQUEST_INIT_APPBOY_LOCATION_SERVICE")) {
                    bk.this.c();
                }
            }
        };
        final StringBuilder sb = new StringBuilder();
        sb.append(this.c);
        sb.append(".SINGLE_APPBOY_LOCATION_UPDATE");
        final IntentFilter intentFilter = new IntentFilter(sb.toString());
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(this.c);
        sb2.append(".REQUEST_INIT_APPBOY_LOCATION_SERVICE");
        intentFilter.addAction(sb2.toString());
        this.b.registerReceiver((BroadcastReceiver)broadcastReceiver, intentFilter);
        if (!PermissionUtils.hasPermission(this.b, "android.permission.ACCESS_FINE_LOCATION")) {
            this.e();
        }
    }
    
    private void a(final Intent intent) {
        try {
            final String a = bk.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Single location update received from ");
            sb.append(intent.getStringExtra("origin"));
            sb.append(": ");
            sb.append(intent.getAction());
            AppboyLogger.i(a, sb.toString());
            final Location location = (Location)intent.getExtras().get("location");
            if (location != null) {
                this.a(new ch(location.getLatitude(), location.getLongitude(), location.getAltitude(), (double)location.getAccuracy()));
                return;
            }
            AppboyLogger.w(bk.a, "Failed to process location update. Received location was null.");
        }
        catch (Exception ex) {
            AppboyLogger.e(bk.a, "Failed to process location update.", ex);
        }
    }
    
    private void a(final AppboyConfigurationProvider appboyConfigurationProvider, final dr dr) {
        String s;
        StringBuilder sb;
        String s2;
        if (dr.h() >= 0L) {
            this.i = dr.h();
            s = bk.a;
            sb = new StringBuilder();
            s2 = "Time interval override set via server configuration for background location collection: ";
        }
        else if (appboyConfigurationProvider.getLocationUpdateTimeIntervalInMillis() > 300000L) {
            this.i = appboyConfigurationProvider.getLocationUpdateTimeIntervalInMillis();
            s = bk.a;
            sb = new StringBuilder();
            s2 = "Time interval override set via local configuration for background location collection: ";
        }
        else {
            this.i = 3600000L;
            s = bk.a;
            sb = new StringBuilder();
            s2 = "Time interval override set to default for background location collection: ";
        }
        sb.append(s2);
        sb.append(this.i / 1000L);
        sb.append("s.");
        AppboyLogger.i(s, sb.toString());
        String s3;
        StringBuilder sb2;
        String s4;
        if (dr.j() >= 0.0f) {
            this.j = dr.j();
            s3 = bk.a;
            sb2 = new StringBuilder();
            s4 = "Distance threshold override set via server configuration for background location collection: ";
        }
        else if (appboyConfigurationProvider.getLocationUpdateDistanceInMeters() > 50.0f) {
            this.j = appboyConfigurationProvider.getLocationUpdateDistanceInMeters();
            s3 = bk.a;
            sb2 = new StringBuilder();
            s4 = "Distance threshold override set via local configuration for background location collection: ";
        }
        else {
            this.j = 50.0f;
            s3 = bk.a;
            sb2 = new StringBuilder();
            s4 = "Distance threshold override set to default for background location collection: ";
        }
        sb2.append(s4);
        sb2.append(this.j);
        sb2.append("m.");
        AppboyLogger.i(s3, sb2.toString());
    }
    
    public static boolean a(final AppboyConfigurationProvider appboyConfigurationProvider) {
        if (appboyConfigurationProvider.isLocationCollectionEnabled()) {
            AppboyLogger.i(bk.a, "Location collection enabled via sdk configuration.");
            return true;
        }
        AppboyLogger.i(bk.a, "Location collection disabled via sdk configuration.");
        return false;
    }
    
    private boolean a(final String s) {
        if (!this.g) {
            final String a = bk.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Appboy Location service is not available. Did not send intent to service: ");
            sb.append(s);
            AppboyLogger.i(a, sb.toString());
            return false;
        }
        final Intent setClass = new Intent(s).setClass(this.b, (Class)AppboyLocationService.class);
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(this.c);
        sb2.append(".REQUEST_APPBOY_LOCATION_UPDATES");
        if (s.equals(sb2.toString())) {
            setClass.putExtra("distance", this.j);
            setClass.putExtra("time", this.i);
        }
        this.b.startService(setClass);
        return true;
    }
    
    private boolean b(final AppboyConfigurationProvider appboyConfigurationProvider, final dr dr) {
        String s = null;
        String s2 = null;
        Label_0038: {
            String s3;
            String s4;
            if (dr.c()) {
                if (!dr.d()) {
                    s = bk.a;
                    s2 = "Background location collection disabled via server configuration.";
                    break Label_0038;
                }
                s3 = bk.a;
                s4 = "Background location collection enabled via server configuration.";
            }
            else {
                if (!appboyConfigurationProvider.isBackgroundLocationCollectionEnabled()) {
                    s = bk.a;
                    s2 = "Background location collection disabled via sdk configuration.";
                    break Label_0038;
                }
                s3 = bk.a;
                s4 = "Background location collection enabled via sdk configuration.";
            }
            AppboyLogger.i(s3, s4);
            return true;
        }
        AppboyLogger.i(s, s2);
        return false;
    }
    
    private void e() {
        if (!this.g) {
            AppboyLogger.i(bk.a, "Did not attempt to stop service. Braze Location service is not available.");
            return;
        }
        AppboyLogger.i(bk.a, "Stopping Braze location service if currently running.");
        this.b.stopService(new Intent().setClass(this.b, (Class)AppboyLocationService.class));
    }
    
    private boolean f() {
        if (eg.a(this.b, AppboyLocationService.class)) {
            return true;
        }
        AppboyLogger.i(bk.a, "Appboy location service is not available. Declare <service android:name=\"com.appboy.services.AppboyLocationService\"/> in your AndroidManifest.xml to enable Braze location service.");
        return false;
    }
    
    private String g() {
        final String k = this.k;
        if (k != null) {
            return k;
        }
        final Criteria criteria = new Criteria();
        criteria.setAccuracy(2);
        criteria.setPowerRequirement(1);
        return this.k = this.d.getBestProvider(criteria, true);
    }
    
    @Override
    public void a() {
        this.e();
    }
    
    @Override
    public void a(final co co) {
        if (co == null) {
            AppboyLogger.w(bk.a, "Could not reset background location collection interval. Server config was null.");
            return;
        }
        if (co.h() >= 0L) {
            this.i = co.h();
            final String a = bk.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Time interval override reset via server configuration for background location collection: ");
            sb.append(this.i / 1000L);
            sb.append("s.");
            AppboyLogger.i(a, sb.toString());
        }
        if (co.i() >= 0.0f) {
            this.j = co.i();
            final String a2 = bk.a;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Distance threshold override reset via server configuration for background location collection: ");
            sb2.append(this.j);
            sb2.append("m.");
            AppboyLogger.i(a2, sb2.toString());
        }
        if (co.g()) {
            if (co.f()) {
                this.h = true;
                AppboyLogger.i(bk.a, "Background location collection enabled via server configuration. Requesting location updates.");
                this.c();
                return;
            }
            this.h = false;
            AppboyLogger.i(bk.a, "Background location collection disabled via server configuration. Stopping any active Braze location service.");
            this.e();
        }
    }
    
    @Override
    public boolean a(final cb cb) {
        try {
            this.e.a(cg.a(cb));
            return true;
        }
        catch (Exception ex) {
            AppboyLogger.w(bk.a, "Failed to log location recorded event.", ex);
            return false;
        }
    }
    
    @Override
    public boolean b() {
        if (!this.f) {
            AppboyLogger.i(bk.a, "Did not request single location update. Location collection is disabled.");
            return false;
        }
        if (!PermissionUtils.hasPermission(this.b, "android.permission.ACCESS_FINE_LOCATION") && !PermissionUtils.hasPermission(this.b, "android.permission.ACCESS_COARSE_LOCATION")) {
            AppboyLogger.i(bk.a, "Did not request single location update. Fine grained location permissions not found.");
            return false;
        }
        String g;
        if (PermissionUtils.hasPermission(this.b, "android.permission.ACCESS_FINE_LOCATION")) {
            g = "passive";
        }
        else {
            g = this.g();
        }
        if (StringUtils.isNullOrBlank(g)) {
            AppboyLogger.d(bk.a, "Could not request single location update. Android location provider not found.");
            return false;
        }
        try {
            AppboyLogger.d(bk.a, "Requesting single location update.");
            final StringBuilder sb = new StringBuilder();
            sb.append(this.c);
            sb.append(".SINGLE_APPBOY_LOCATION_UPDATE");
            final Intent intent = new Intent(sb.toString());
            intent.putExtra("origin", "Appboy location manager");
            this.d.requestSingleUpdate(g, PendingIntent.getBroadcast(this.b, 0, intent, 134217728));
            return true;
        }
        catch (Exception ex) {
            AppboyLogger.w(bk.a, "Failed to request single location update due to exception.", ex);
            return false;
        }
        catch (SecurityException ex2) {
            AppboyLogger.w(bk.a, "Failed to request single location update due to security exception from insufficient permissions.", ex2);
            return false;
        }
    }
    
    @Override
    public boolean c() {
        if (!this.f) {
            AppboyLogger.i(bk.a, "Did not request background location updates. Location collection is disabled.");
            return false;
        }
        if (!this.h) {
            AppboyLogger.i(bk.a, "Did not request background location updates. Background location collection is disabled.");
            return false;
        }
        if (!PermissionUtils.hasPermission(this.b, "android.permission.ACCESS_FINE_LOCATION")) {
            AppboyLogger.i(bk.a, "Did not request background location updates. Fine grained location permissions not found.");
            return false;
        }
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.c);
            sb.append(".REQUEST_REMOVE_APPBOY_LOCATION_UPDATES");
            this.a(sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(this.c);
            sb2.append(".REQUEST_APPBOY_LOCATION_UPDATES");
            return this.a(sb2.toString());
        }
        catch (Exception ex) {
            AppboyLogger.w(bk.a, "Could not request location updates due to exception.", ex);
            return false;
        }
    }
}
