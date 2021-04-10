package com.appboy;

import android.content.*;
import bo.app.*;

public class AppboyInternal
{
    public static void addSerializedContentCardToStorage(final Context context, final String s, final String s2) {
        Appboy.getInstance(context).a(s, s2);
    }
    
    public static void recordGeofenceTransition(final Context context, final String s, final x x) {
        Appboy.getInstance(context).a(s, x);
    }
    
    public static void requestGeofenceRefresh(final Context context, final cb cb) {
        Appboy.getInstance(context).a(cb);
    }
    
    public static void requestGeofenceRefresh(final Context context, final boolean b) {
        Appboy.getInstance(context).a(b);
    }
    
    public static void requestGeofencesInitialization(final Context context) {
        Appboy.getInstance(context).a();
    }
}
