package com.microsoft.aad.adal;

import android.util.*;
import java.util.concurrent.*;
import java.util.*;

public final class Telemetry
{
    private static final Telemetry INSTANCE;
    private static final String TAG;
    private static boolean sAllowPii;
    private DefaultDispatcher mDispatcher;
    private final Map<Pair<String, String>, String> mEventTracking;
    
    static {
        TAG = Telemetry.class.getSimpleName();
        Telemetry.sAllowPii = false;
        INSTANCE = new Telemetry();
    }
    
    public Telemetry() {
        this.mDispatcher = null;
        this.mEventTracking = new ConcurrentHashMap<Pair<String, String>, String>();
    }
    
    public static boolean getAllowPii() {
        return Telemetry.sAllowPii;
    }
    
    public static Telemetry getInstance() {
        synchronized (Telemetry.class) {
            return Telemetry.INSTANCE;
        }
    }
    
    static String registerNewRequest() {
        return UUID.randomUUID().toString();
    }
    
    public static void setAllowPii(final boolean sAllowPii) {
        Telemetry.sAllowPii = sAllowPii;
    }
    
    void flush(final String s) {
        final DefaultDispatcher mDispatcher = this.mDispatcher;
        if (mDispatcher != null) {
            mDispatcher.flush(s);
        }
    }
    
    public void registerDispatcher(final IDispatcher dispatcher, final boolean b) {
        // monitorenter(this)
        Label_0023: {
            if (!b) {
                break Label_0023;
            }
            while (true) {
                try {
                    DefaultDispatcher mDispatcher = new AggregatedDispatcher(dispatcher);
                    while (true) {
                        this.mDispatcher = mDispatcher;
                        return;
                        throw;
                        mDispatcher = new DefaultDispatcher(dispatcher);
                        continue;
                    }
                }
                // monitorexit(this)
                // monitorexit(this)
                finally {
                    continue;
                }
                break;
            }
        }
    }
    
    void startEvent(final String s, final String s2) {
        if (this.mDispatcher == null) {
            return;
        }
        this.mEventTracking.put((Pair<String, String>)new Pair((Object)s, (Object)s2), Long.toString(System.currentTimeMillis()));
    }
    
    void stopEvent(final String s, final IEvents events, String s2) {
        if (this.mDispatcher == null) {
            return;
        }
        s2 = this.mEventTracking.remove(new Pair((Object)s, (Object)s2));
        if (StringExtensions.isNullOrBlank(s2)) {
            Logger.w(Telemetry.TAG, "Stop Event called without a corresponding start_event", "", null);
            return;
        }
        final long long1 = Long.parseLong(s2);
        final long currentTimeMillis = System.currentTimeMillis();
        final String string = Long.toString(currentTimeMillis);
        events.setProperty("Microsoft.ADAL.start_time", s2);
        events.setProperty("Microsoft.ADAL.stop_time", string);
        events.setProperty("Microsoft.ADAL.response_time", Long.toString(currentTimeMillis - long1));
        this.mDispatcher.receive(s, events);
    }
}
