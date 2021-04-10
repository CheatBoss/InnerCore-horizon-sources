package bo.app;

import com.appboy.support.*;
import android.app.*;
import android.os.*;
import com.appboy.models.*;
import android.content.*;
import java.util.*;
import com.google.android.gms.location.*;
import com.google.android.gms.tasks.*;
import com.google.android.gms.common.api.*;

public final class dx
{
    private static final String a;
    
    static {
        a = AppboyLogger.getAppboyLogTag(dx.class);
    }
    
    public static void a(final Context context) {
        AppboyLogger.d(dx.a, "Deleting registered geofence cache.");
        final SharedPreferences$Editor edit = context.getSharedPreferences("com.appboy.support.geofences", 0).edit();
        edit.clear();
        edit.apply();
    }
    
    public static void a(final Context context, final PendingIntent pendingIntent) {
        try {
            AppboyLogger.d(dx.a, "Requesting single location update from Google Play Services.");
            final LocationRequest create = LocationRequest.create();
            create.setPriority(100);
            create.setNumUpdates(1);
            if (Looper.myLooper() == null) {
                Looper.prepare();
            }
            LocationServices.getFusedLocationProviderClient(context).requestLocationUpdates(create, pendingIntent);
        }
        catch (Exception ex) {
            final String s = dx.a;
        }
        catch (SecurityException ex) {
            final String s = dx.a;
            goto Label_0054;
        }
    }
    
    public static void a(final Context context, final List<AppboyGeofence> list, final PendingIntent pendingIntent) {
        try {
            final SharedPreferences sharedPreferences = context.getSharedPreferences("com.appboy.support.geofences", 0);
            final List<AppboyGeofence> a = dw.a(sharedPreferences);
            if (list.isEmpty()) {
                final ArrayList<String> list2 = new ArrayList<String>();
                for (final AppboyGeofence appboyGeofence : a) {
                    list2.add(appboyGeofence.getId());
                    final String a2 = dx.a;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Obsolete geofence will be un-registered: ");
                    sb.append(appboyGeofence.getId());
                    AppboyLogger.d(a2, sb.toString());
                }
                if (!list2.isEmpty()) {
                    LocationServices.getGeofencingClient(context).removeGeofences((List)list2);
                    sharedPreferences.edit().clear().apply();
                    final String a3 = dx.a;
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("No new geofences to register. Cleared ");
                    sb2.append(a.size());
                    sb2.append(" previously registered geofences.");
                    AppboyLogger.d(a3, sb2.toString());
                    return;
                }
                AppboyLogger.d(dx.a, "No new geofences to register. No geofences are currently registered.");
            }
            else {
                final ArrayList<AppboyGeofence> list3 = new ArrayList<AppboyGeofence>();
                final HashSet<String> set = new HashSet<String>();
                for (final AppboyGeofence appboyGeofence2 : list) {
                    set.add(appboyGeofence2.getId());
                    boolean b = true;
                    for (final AppboyGeofence appboyGeofence3 : a) {
                        if (appboyGeofence2.getId().equals(appboyGeofence3.getId()) && appboyGeofence2.equivalentServerData(appboyGeofence3)) {
                            b = false;
                        }
                    }
                    if (b) {
                        final String a4 = dx.a;
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("New geofence will be registered: ");
                        sb3.append(appboyGeofence2.getId());
                        AppboyLogger.d(a4, sb3.toString());
                        list3.add(appboyGeofence2);
                    }
                }
                final ArrayList<String> list4 = new ArrayList<String>();
                for (final AppboyGeofence appboyGeofence4 : a) {
                    if (!set.contains(appboyGeofence4.getId())) {
                        list4.add(appboyGeofence4.getId());
                        final String a5 = dx.a;
                        final StringBuilder sb4 = new StringBuilder();
                        sb4.append("Obsolete geofence will be un-registered: ");
                        sb4.append(appboyGeofence4.getId());
                        AppboyLogger.d(a5, sb4.toString());
                    }
                }
                if (!list4.isEmpty()) {
                    final SharedPreferences$Editor edit = sharedPreferences.edit();
                    final Iterator<Object> iterator5 = list4.iterator();
                    while (iterator5.hasNext()) {
                        edit.remove((String)iterator5.next());
                    }
                    edit.apply();
                    final String a6 = dx.a;
                    final StringBuilder sb5 = new StringBuilder();
                    sb5.append("Un-registering ");
                    sb5.append(list4.size());
                    sb5.append(" obsolete geofences from Google Play Services.");
                    AppboyLogger.d(a6, sb5.toString());
                    LocationServices.getGeofencingClient(context).removeGeofences((List)list4);
                }
                else {
                    AppboyLogger.d(dx.a, "No obsolete geofences need to be unregistered from Google Play Services.");
                }
                if (!list3.isEmpty()) {
                    final ArrayList<Geofence> list5 = new ArrayList<Geofence>();
                    final SharedPreferences$Editor edit2 = sharedPreferences.edit();
                    for (final AppboyGeofence appboyGeofence5 : list3) {
                        list5.add(appboyGeofence5.toGeofence());
                        edit2.putString(appboyGeofence5.getId(), appboyGeofence5.forJsonPut().toString());
                    }
                    edit2.apply();
                    final String a7 = dx.a;
                    final StringBuilder sb6 = new StringBuilder();
                    sb6.append("Registering ");
                    sb6.append(list3.size());
                    sb6.append(" new geofences with Google Play Services.");
                    AppboyLogger.d(a7, sb6.toString());
                    b(context, list5, pendingIntent);
                    return;
                }
                AppboyLogger.d(dx.a, "No new geofences need to be registered with Google Play Services.");
            }
        }
        catch (Exception ex) {
            final String s = dx.a;
        }
        catch (SecurityException ex) {
            final String s = dx.a;
            goto Label_0827;
        }
    }
    
    private static void b(final Context context, final List<Geofence> list, final PendingIntent pendingIntent) {
        LocationServices.getGeofencingClient(context).addGeofences(new GeofencingRequest$Builder().addGeofences((List)list).setInitialTrigger(0).build(), pendingIntent).addOnSuccessListener((OnSuccessListener)new OnSuccessListener<Void>() {
            public void a(final Void void1) {
                AppboyLogger.d(dx.a, "Geofences successfully registered with Google Play Services.");
            }
        }).addOnFailureListener((OnFailureListener)new OnFailureListener() {
            @Override
            public void onFailure(final Exception ex) {
                if (!(ex instanceof ApiException)) {
                    AppboyLogger.e(dx.a, "Geofence exception encountered while adding geofences.", ex);
                    return;
                }
                final int statusCode = ((ApiException)ex).getStatusCode();
                if (statusCode == 0) {
                    AppboyLogger.d(dx.a, "Received Geofence registration success code in failure block with Google Play Services.");
                    return;
                }
                switch (statusCode) {
                    default: {
                        final String a = dx.a;
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Geofence pending result returned unknown status code: ");
                        sb.append(statusCode);
                        AppboyLogger.w(a, sb.toString());
                    }
                    case 1002: {
                        final String a2 = dx.a;
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("Geofences not registered with Google Play Services due to GEOFENCE_TOO_MANY_PENDING_INTENTS: ");
                        sb2.append(statusCode);
                        AppboyLogger.w(a2, sb2.toString());
                    }
                    case 1001: {
                        final String a3 = dx.a;
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("Geofences not registered with Google Play Services due to GEOFENCE_TOO_MANY_GEOFENCES: ");
                        sb3.append(statusCode);
                        AppboyLogger.w(a3, sb3.toString());
                    }
                    case 1000: {
                        final String a4 = dx.a;
                        final StringBuilder sb4 = new StringBuilder();
                        sb4.append("Geofences not registered with Google Play Services due to GEOFENCE_NOT_AVAILABLE: ");
                        sb4.append(statusCode);
                        AppboyLogger.w(a4, sb4.toString());
                    }
                }
            }
        });
    }
}
