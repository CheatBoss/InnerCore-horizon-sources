package com.appsflyer;

import com.appsflyer.internal.*;
import android.os.*;
import android.hardware.*;
import android.content.*;
import java.util.concurrent.*;
import java.util.*;

public final class AFSensorManager
{
    public static volatile AFSensorManager sInstance;
    private static final BitSet \u0237;
    private static final Handler \u026a;
    final Handler \u0131;
    int \u0196;
    final Map<r, r> \u01c3;
    private final Runnable \u0268;
    final SensorManager \u0269;
    final Runnable \u0279;
    long \u027e;
    boolean \u0399;
    final Object \u03b9;
    final Runnable \u0406;
    final Runnable \u0456;
    boolean \u04c0;
    private final Map<r, Map<String, Object>> \u04cf;
    
    static {
        \u0237 = new BitSet(6);
        \u026a = new Handler(Looper.getMainLooper());
        AFSensorManager.\u0237.set(1);
        AFSensorManager.\u0237.set(2);
        AFSensorManager.\u0237.set(4);
    }
    
    private AFSensorManager(final SensorManager \u0269, final Handler \u0131) {
        this.\u03b9 = new Object();
        this.\u01c3 = new HashMap<r, r>(AFSensorManager.\u0237.size());
        this.\u04cf = new ConcurrentHashMap<r, Map<String, Object>>(AFSensorManager.\u0237.size());
        this.\u0279 = new Runnable() {
            @Override
            public final void run() {
                synchronized (AFSensorManager.this.\u03b9) {
                    final AFSensorManager \u01c3 = AFSensorManager.this;
                    try {
                        for (final Sensor sensor : \u01c3.\u0269.getSensorList(-1)) {
                            if (AFSensorManager.\u0269(sensor.getType())) {
                                final r r = new r(sensor.getType(), sensor.getName(), sensor.getVendor());
                                if (!\u01c3.\u01c3.containsKey(r)) {
                                    \u01c3.\u01c3.put(r, r);
                                }
                                \u01c3.\u0269.registerListener((SensorEventListener)\u01c3.\u01c3.get(r), sensor, 0);
                            }
                        }
                    }
                    finally {}
                    \u01c3.\u04c0 = true;
                    AFSensorManager.this.\u0131.postDelayed(AFSensorManager.this.\u0268, 100L);
                    AFSensorManager.this.\u0399 = true;
                }
            }
        };
        this.\u0456 = new Runnable() {
            @Override
            public final void run() {
                synchronized (AFSensorManager.this.\u03b9) {
                    AFSensorManager.this.\u0399();
                }
            }
        };
        this.\u0406 = new Runnable() {
            @Override
            public final void run() {
                synchronized (AFSensorManager.this.\u03b9) {
                    if (AFSensorManager.this.\u0399) {
                        AFSensorManager.this.\u0131.removeCallbacks(AFSensorManager.this.\u0279);
                        AFSensorManager.this.\u0131.removeCallbacks(AFSensorManager.this.\u0456);
                        AFSensorManager.this.\u0399();
                        AFSensorManager.this.\u0399 = false;
                    }
                }
            }
        };
        this.\u0196 = 1;
        this.\u027e = 0L;
        this.\u0268 = new Runnable() {
            @Override
            public final void run() {
                synchronized (AFSensorManager.this.\u03b9) {
                    if (AFSensorManager.this.\u0196 == 0) {
                        AFSensorManager.this.\u0196 = 1;
                    }
                    AFSensorManager.this.\u0131.postDelayed(AFSensorManager.this.\u0456, AFSensorManager.this.\u0196 * 500L);
                }
            }
        };
        this.\u0269 = \u0269;
        this.\u0131 = \u0131;
    }
    
    private static AFSensorManager \u01c3(final SensorManager sensorManager, final Handler handler) {
        if (AFSensorManager.sInstance == null) {
            synchronized (AFSensorManager.class) {
                if (AFSensorManager.sInstance == null) {
                    AFSensorManager.sInstance = new AFSensorManager(sensorManager, handler);
                }
            }
        }
        return AFSensorManager.sInstance;
    }
    
    static AFSensorManager \u0269(final Context context) {
        if (AFSensorManager.sInstance != null) {
            return AFSensorManager.sInstance;
        }
        return \u01c3((SensorManager)context.getApplicationContext().getSystemService("sensor"), AFSensorManager.\u026a);
    }
    
    static boolean \u0269(final int n) {
        return n >= 0 && AFSensorManager.\u0237.get(n);
    }
    
    final List<Map<String, Object>> \u0269() {
        final Iterator<r> iterator = this.\u01c3.values().iterator();
        while (iterator.hasNext()) {
            iterator.next().\u0131(this.\u04cf, true);
        }
        final Map<r, Map<String, Object>> \u04cf = this.\u04cf;
        if (\u04cf != null && !\u04cf.isEmpty()) {
            return new CopyOnWriteArrayList<Map<String, Object>>(this.\u04cf.values());
        }
        return new CopyOnWriteArrayList<Map<String, Object>>((Collection<? extends Map<String, Object>>)Collections.emptyList());
    }
    
    final void \u0399() {
        try {
            if (!this.\u01c3.isEmpty()) {
                for (final r r : this.\u01c3.values()) {
                    this.\u0269.unregisterListener((SensorEventListener)r);
                    r.\u0131(this.\u04cf, true);
                }
            }
        }
        finally {}
        this.\u0196 = 0;
        this.\u04c0 = false;
    }
    
    final List<Map<String, Object>> \u03b9() {
        synchronized (this.\u03b9) {
            if (!this.\u01c3.isEmpty() && this.\u04c0) {
                final Iterator<r> iterator = this.\u01c3.values().iterator();
                while (iterator.hasNext()) {
                    iterator.next().\u0131(this.\u04cf, false);
                }
            }
            if (this.\u04cf.isEmpty()) {
                return new CopyOnWriteArrayList<Map<String, Object>>((Collection<? extends Map<String, Object>>)Collections.emptyList());
            }
            return new CopyOnWriteArrayList<Map<String, Object>>(this.\u04cf.values());
        }
    }
}
