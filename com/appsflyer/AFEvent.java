package com.appsflyer;

import java.lang.ref.*;
import android.content.*;
import java.util.*;
import android.net.*;

public abstract class AFEvent
{
    WeakReference<Context> \u0131;
    public String \u0196;
    public Intent \u01c3;
    boolean \u0237;
    public int \u0268;
    AppsFlyerTrackingRequestListener \u0269;
    private Map<String, Object> \u026a;
    String \u0279;
    private final boolean \u027e;
    private byte[] \u027f;
    Map<String, Object> \u0399;
    Context \u03b9;
    String \u0406;
    String \u0456;
    String \u04c0;
    private String \u04cf;
    
    public AFEvent() {
        this(null, null, null);
    }
    
    public AFEvent(final String \u04cf, final Boolean b, final Context \u03b9) {
        this.\u04c0 = \u04cf;
        this.\u027e = (b == null || b);
        this.\u03b9 = \u03b9;
    }
    
    protected String addChannel(final String s) {
        final String configuredChannel = AppsFlyerLibCore.getInstance().getConfiguredChannel(this.context());
        String string = s;
        if (configuredChannel != null) {
            string = Uri.parse(s).buildUpon().appendQueryParameter("channel", configuredChannel).build().toString();
        }
        return string;
    }
    
    public Context context() {
        final Context \u03b9 = this.\u03b9;
        if (\u03b9 != null) {
            return \u03b9;
        }
        final WeakReference<Context> \u0131 = this.\u0131;
        if (\u0131 != null) {
            return \u0131.get();
        }
        return null;
    }
    
    protected AFEvent context(final Context \u03b9) {
        this.\u03b9 = \u03b9;
        return this;
    }
    
    public Intent intent() {
        return this.\u01c3;
    }
    
    public boolean isEncrypt() {
        return this.\u027e;
    }
    
    public AFEvent key(final String \u04cf) {
        this.\u04cf = \u04cf;
        return this;
    }
    
    public String key() {
        return this.\u04cf;
    }
    
    public AFEvent params(final Map<String, ?> \u026a) {
        this.\u026a = (Map<String, Object>)\u026a;
        return this;
    }
    
    public Map<String, Object> params() {
        return this.\u026a;
    }
    
    public AFEvent post(final byte[] \u027f) {
        this.\u027f = \u027f;
        return this;
    }
    
    public AFEvent urlString(final String \u0456) {
        this.\u0456 = \u0456;
        return this;
    }
    
    public String urlString() {
        return this.\u0456;
    }
    
    public AFEvent weakContext() {
        this.\u0131 = new WeakReference<Context>(this.\u03b9);
        this.\u03b9 = null;
        return this;
    }
    
    final AFEvent \u01c3() {
        if (this.context() != null) {
            this.\u03b9 = this.context().getApplicationContext();
        }
        return this;
    }
    
    final boolean \u0399() {
        return this.\u0237;
    }
    
    final byte[] \u03b9() {
        return this.\u027f;
    }
}
