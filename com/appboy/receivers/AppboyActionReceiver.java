package com.appboy.receivers;

import com.appboy.support.*;
import android.content.*;
import java.util.*;
import com.google.android.gms.location.*;
import bo.app.*;
import android.location.*;
import android.util.*;
import com.appboy.*;

public class AppboyActionReceiver extends BroadcastReceiver
{
    private static final String a;
    
    static {
        a = AppboyLogger.getAppboyLogTag(AppboyActionReceiver.class);
    }
    
    public void onReceive(final Context context, final Intent intent) {
        if (intent == null) {
            AppboyLogger.w(AppboyActionReceiver.a, "AppboyActionReceiver received null intent. Doing nothing.");
            return;
        }
        new Thread(new a(context.getApplicationContext(), intent, this.goAsync())).start();
    }
    
    static class a implements Runnable
    {
        private final String a;
        private final Context b;
        private final BroadcastReceiver$PendingResult c;
        private final Intent d;
        
        a(final Context b, final Intent d, final BroadcastReceiver$PendingResult c) {
            this.b = b;
            this.d = d;
            this.a = d.getAction();
            this.c = c;
        }
        
        static boolean a(final Context context, final GeofencingEvent geofencingEvent) {
            if (geofencingEvent.hasError()) {
                final int errorCode = geofencingEvent.getErrorCode();
                final String a = AppboyActionReceiver.a;
                final StringBuilder sb = new StringBuilder();
                sb.append("AppboyLocation Services error: ");
                sb.append(errorCode);
                AppboyLogger.e(a, sb.toString());
                return false;
            }
            final int geofenceTransition = geofencingEvent.getGeofenceTransition();
            final List triggeringGeofences = geofencingEvent.getTriggeringGeofences();
            if (geofenceTransition != 0) {
                final Iterator<Geofence> iterator = triggeringGeofences.iterator();
                while (iterator.hasNext()) {
                    AppboyInternal.recordGeofenceTransition(context, iterator.next().getRequestId(), x.a);
                }
                return true;
            }
            if (2 == geofenceTransition) {
                final Iterator<Geofence> iterator2 = triggeringGeofences.iterator();
                while (iterator2.hasNext()) {
                    AppboyInternal.recordGeofenceTransition(context, iterator2.next().getRequestId(), x.b);
                }
                return true;
            }
            final String a2 = AppboyActionReceiver.a;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Unsupported transition type received: ");
            sb2.append(geofenceTransition);
            AppboyLogger.w(a2, sb2.toString());
            return false;
        }
        
        static boolean a(final Context context, final LocationResult locationResult) {
            try {
                final Location lastLocation = locationResult.getLastLocation();
                AppboyInternal.requestGeofenceRefresh(context, new ch(lastLocation.getLatitude(), lastLocation.getLongitude(), lastLocation.getAltitude(), (double)lastLocation.getAccuracy()));
                return true;
            }
            catch (Exception ex) {
                AppboyLogger.e(AppboyActionReceiver.a, "Exception while processing location result", ex);
                return false;
            }
        }
        
        boolean a() {
            if (this.a == null) {
                Log.d(AppboyActionReceiver.a, "Received intent with null action. Doing nothing.");
                return false;
            }
            final String a = AppboyActionReceiver.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Received intent with action ");
            sb.append(this.a);
            Log.d(a, sb.toString());
            if (this.a.equals("com.appboy.action.receiver.DATA_SYNC")) {
                AppboyLogger.d(AppboyActionReceiver.a, "Requesting immediate data flush from AppboyActionReceiver.", false);
                Appboy.getInstance(this.b).requestImmediateDataFlush();
                return true;
            }
            if (this.a.equals("com.appboy.action.receiver.APPBOY_GEOFENCE_LOCATION_UPDATE")) {
                if (LocationResult.hasResult(this.d)) {
                    final String a2 = AppboyActionReceiver.a;
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("AppboyActionReceiver received intent with location result: ");
                    sb2.append(this.a);
                    AppboyLogger.d(a2, sb2.toString());
                    return a(this.b, LocationResult.extractResult(this.d));
                }
                final String a3 = AppboyActionReceiver.a;
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("AppboyActionReceiver received intent without location result: ");
                sb3.append(this.a);
                AppboyLogger.w(a3, sb3.toString());
                return false;
            }
            else {
                if (this.a.equals("com.appboy.action.receiver.APPBOY_GEOFENCE_UPDATE")) {
                    final String a4 = AppboyActionReceiver.a;
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("AppboyActionReceiver received intent with geofence transition: ");
                    sb4.append(this.a);
                    AppboyLogger.d(a4, sb4.toString());
                    return a(this.b, GeofencingEvent.fromIntent(this.d));
                }
                final String a5 = AppboyActionReceiver.a;
                final StringBuilder sb5 = new StringBuilder();
                sb5.append("Unknown intent received in AppboyActionReceiver with action: ");
                sb5.append(this.a);
                AppboyLogger.w(a5, sb5.toString());
                return false;
            }
        }
        
        @Override
        public void run() {
            try {
                this.a();
            }
            catch (Exception ex) {
                final String a = AppboyActionReceiver.a;
                final StringBuilder sb = new StringBuilder();
                sb.append("Caught exception while performing the AppboyActionReceiver work. Action: ");
                sb.append(this.a);
                sb.append(" Intent: ");
                sb.append(this.d);
                AppboyLogger.e(a, sb.toString(), ex);
            }
            this.c.finish();
        }
    }
}
