package com.appboy.services;

import com.appboy.support.*;
import android.location.*;
import android.app.*;
import android.content.*;
import android.os.*;
import com.appboy.*;

public class AppboyLocationService extends Service
{
    private static final String a;
    private LocationListener b;
    private LocationManager c;
    
    static {
        a = AppboyLogger.getAppboyLogTag(AppboyLocationService.class);
    }
    
    private void a(final Intent intent) {
        final String a = AppboyLocationService.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("Null intent action received in Braze location service: ");
        sb.append(intent.getDataString());
        AppboyLogger.w(a, sb.toString());
    }
    
    private void b() {
        final LocationListener b = this.b;
        if (b != null) {
            try {
                this.c.removeUpdates(b);
            }
            catch (SecurityException ex) {
                AppboyLogger.w(AppboyLocationService.a, "Could not remove background location updates. Security exception from insufficient permissions", ex);
            }
        }
    }
    
    private void b(final Intent intent) {
        final String a = AppboyLocationService.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("Requesting background location updates: ");
        sb.append(intent.getAction());
        AppboyLogger.d(a, sb.toString());
        if (this.c == null) {
            this.c = (LocationManager)this.getApplicationContext().getSystemService("location");
        }
        if (this.b == null) {
            this.b = this.c();
        }
        final float floatExtra = intent.getFloatExtra("distance", 50.0f);
        final long longExtra = intent.getLongExtra("time", 3600000L);
        final LocationListener b = this.b;
        if (b != null) {
            try {
                this.c.requestLocationUpdates("passive", longExtra, floatExtra, b);
                final String a2 = AppboyLocationService.a;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Collecting locations using passive provider with time interval ");
                sb2.append(longExtra / 1000L);
                sb2.append("s and update distance ");
                sb2.append(floatExtra);
                sb2.append("m.");
                AppboyLogger.i(a2, sb2.toString());
                return;
            }
            catch (SecurityException ex) {
                AppboyLogger.w(AppboyLocationService.a, "Could not request background location updates. Security exception from insufficient permissions", ex);
                return;
            }
        }
        AppboyLogger.w(AppboyLocationService.a, "Could not request background location updates. Braze location listener was null.");
    }
    
    private LocationListener c() {
        return (LocationListener)new LocationListener() {
            public void onLocationChanged(final Location location) {
                if (location != null) {
                    AppboyLogger.d(AppboyLocationService.a, "Requesting single location update.");
                    final StringBuilder sb = new StringBuilder();
                    sb.append(AppboyLocationService.this.getApplicationContext().getPackageName());
                    sb.append(".SINGLE_APPBOY_LOCATION_UPDATE");
                    final Intent intent = new Intent(sb.toString());
                    intent.putExtra("location", (Parcelable)location);
                    intent.putExtra("origin", "Appboy location service");
                    final PendingIntent broadcast = PendingIntent.getBroadcast(AppboyLocationService.this.getApplicationContext(), 0, intent, 134217728);
                    try {
                        AppboyLocationService.this.c.requestSingleUpdate("passive", broadcast);
                    }
                    catch (SecurityException ex) {
                        AppboyLogger.w(AppboyLocationService.a, "Could not request single location update. Security exception from insufficient permissions", ex);
                    }
                }
            }
            
            public void onProviderDisabled(final String s) {
                if (s != null && s.equals("passive")) {
                    AppboyLocationService.this.b();
                }
            }
            
            public void onProviderEnabled(final String s) {
            }
            
            public void onStatusChanged(final String s, final int n, final Bundle bundle) {
            }
        };
    }
    
    private void c(final Intent intent) {
        final String a = AppboyLocationService.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("Removing current location updates: ");
        sb.append(intent.getAction());
        AppboyLogger.d(a, sb.toString());
        this.b();
    }
    
    private void d(final Intent intent) {
        final String a = AppboyLocationService.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("Unknown intent received: ");
        sb.append(intent.getAction());
        AppboyLogger.w(a, sb.toString());
    }
    
    public static void requestInitialization(final Context context) {
        AppboyLogger.d(AppboyLocationService.a, "Location permissions were granted. Requesting initialization of location service and geofence initialization.");
        final StringBuilder sb = new StringBuilder();
        sb.append(context.getPackageName());
        sb.append(".REQUEST_INIT_APPBOY_LOCATION_SERVICE");
        context.sendBroadcast(new Intent(sb.toString()));
        AppboyInternal.requestGeofencesInitialization(context);
    }
    
    public IBinder onBind(final Intent intent) {
        return null;
    }
    
    public void onDestroy() {
        super.onDestroy();
        this.b();
    }
    
    public int onStartCommand(final Intent intent, final int n, final int n2) {
        if (intent == null) {
            AppboyLogger.i(AppboyLocationService.a, "Null intent received. Initializing Appboy.");
            Appboy.getInstance(this.getApplicationContext());
            return 1;
        }
        final String action = intent.getAction();
        if (action == null) {
            this.a(intent);
            return 1;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getPackageName());
        sb.append(".REQUEST_APPBOY_LOCATION_UPDATES");
        if (action.equals(sb.toString())) {
            this.b(intent);
            return 1;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(this.getPackageName());
        sb2.append(".REQUEST_REMOVE_APPBOY_LOCATION_UPDATES");
        if (action.contains(sb2.toString())) {
            this.c(intent);
            return 1;
        }
        this.d(intent);
        return 1;
    }
}
