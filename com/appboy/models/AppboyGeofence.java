package com.appboy.models;

import bo.app.*;
import org.json.*;
import com.google.android.gms.location.*;

public class AppboyGeofence implements IPutIntoJson<JSONObject>, Comparable<AppboyGeofence>
{
    final int a;
    final boolean b;
    final boolean c;
    final int d;
    double e;
    private final JSONObject f;
    private final String g;
    private final double h;
    private final double i;
    private final int j;
    private final int k;
    private final boolean l;
    private final boolean m;
    
    public AppboyGeofence(final JSONObject jsonObject) {
        this(jsonObject, jsonObject.getString("id"), jsonObject.getDouble("latitude"), jsonObject.getDouble("longitude"), jsonObject.getInt("radius"), jsonObject.getInt("cooldown_enter"), jsonObject.getInt("cooldown_exit"), jsonObject.getBoolean("analytics_enabled_enter"), jsonObject.getBoolean("analytics_enabled_exit"), jsonObject.optBoolean("enter_events", true), jsonObject.optBoolean("exit_events", true), jsonObject.optInt("notification_responsiveness", 30000));
    }
    
    AppboyGeofence(final JSONObject f, final String g, final double h, final double i, final int a, final int j, final int k, final boolean m, final boolean l, final boolean b, final boolean c, final int d) {
        this.e = -1.0;
        this.f = f;
        this.g = g;
        this.h = h;
        this.i = i;
        this.a = a;
        this.j = j;
        this.k = k;
        this.m = m;
        this.l = l;
        this.b = b;
        this.c = c;
        this.d = d;
    }
    
    @Override
    public int compareTo(final AppboyGeofence appboyGeofence) {
        final double e = this.e;
        int n = 1;
        if (e == -1.0) {
            return 1;
        }
        if (e < appboyGeofence.getDistanceFromGeofenceRefresh()) {
            n = -1;
        }
        return n;
    }
    
    public boolean equivalentServerData(final AppboyGeofence appboyGeofence) {
        try {
            ge.a(appboyGeofence.forJsonPut(), this.f, gg.b);
            return true;
        }
        catch (AssertionError | JSONException assertionError) {
            return false;
        }
    }
    
    @Override
    public JSONObject forJsonPut() {
        return this.f;
    }
    
    public boolean getAnalyticsEnabledEnter() {
        return this.m;
    }
    
    public boolean getAnalyticsEnabledExit() {
        return this.l;
    }
    
    public int getCooldownEnterSeconds() {
        return this.j;
    }
    
    public int getCooldownExitSeconds() {
        return this.k;
    }
    
    public double getDistanceFromGeofenceRefresh() {
        return this.e;
    }
    
    public String getId() {
        return this.g;
    }
    
    public double getLatitude() {
        return this.h;
    }
    
    public double getLongitude() {
        return this.i;
    }
    
    public double getRadiusMeters() {
        return this.a;
    }
    
    public void setDistanceFromGeofenceRefresh(final double e) {
        this.e = e;
    }
    
    public Geofence toGeofence() {
        throw new Runtime("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\r\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.copyTypes(TypeTransformer.java:311)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.fixTypes(TypeTransformer.java:226)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:207)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n");
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AppboyGeofence{");
        sb.append("id=");
        sb.append(this.g);
        sb.append(", latitude='");
        sb.append(this.h);
        sb.append(", longitude=");
        sb.append(this.i);
        sb.append(", radiusMeters=");
        sb.append(this.a);
        sb.append(", cooldownEnterSeconds=");
        sb.append(this.j);
        sb.append(", cooldownExitSeconds=");
        sb.append(this.k);
        sb.append(", analyticsEnabledEnter=");
        sb.append(this.m);
        sb.append(", analyticsEnabledExit=");
        sb.append(this.l);
        sb.append(", enterEvents=");
        sb.append(this.b);
        sb.append(", exitEvents=");
        sb.append(this.c);
        sb.append(", notificationResponsivenessMs=");
        sb.append(this.d);
        sb.append(", distanceFromGeofenceRefresh=");
        sb.append(this.e);
        sb.append('}');
        return sb.toString();
    }
}
