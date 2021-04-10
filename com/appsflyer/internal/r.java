package com.appsflyer.internal;

import java.util.concurrent.*;
import android.hardware.*;
import java.util.*;

public final class r implements SensorEventListener
{
    private final String \u0131;
    private long \u0196;
    private final int \u01c3;
    private final float[][] \u0269;
    private final long[] \u0399;
    private final String \u03b9;
    private final int \u0406;
    private double \u0456;
    
    public r(final int \u01c3, String \u03b9, final String s) {
        this.\u0269 = new float[2][];
        this.\u0399 = new long[2];
        this.\u01c3 = \u01c3;
        String \u0131 = \u03b9;
        if (\u03b9 == null) {
            \u0131 = "";
        }
        this.\u0131 = \u0131;
        if ((\u03b9 = s) == null) {
            \u03b9 = "";
        }
        this.\u03b9 = \u03b9;
        this.\u0406 = ((\u01c3 + 31) * 31 + this.\u0131.hashCode()) * 31 + \u03b9.hashCode();
    }
    
    private static double \u0131(final float[] array, final float[] array2) {
        final int min = Math.min(array.length, array2.length);
        double n = 0.0;
        for (int i = 0; i < min; ++i) {
            n += StrictMath.pow(array[i] - array2[i], 2.0);
        }
        return Math.sqrt(n);
    }
    
    private boolean \u01c3() {
        return this.\u0269[0] != null;
    }
    
    private static List<Float> \u0269(final float[] array) {
        final ArrayList<Float> list = new ArrayList<Float>(array.length);
        for (int length = array.length, i = 0; i < length; ++i) {
            list.add(array[i]);
        }
        return list;
    }
    
    private boolean \u0399(final int n, final String s, final String s2) {
        return this.\u01c3 == n && this.\u0131.equals(s) && this.\u03b9.equals(s2);
    }
    
    private static boolean \u0399(final Sensor sensor) {
        return sensor != null && sensor.getName() != null && sensor.getVendor() != null;
    }
    
    private Map<String, Object> \u03b9() {
        final ConcurrentHashMap<String, List<Float>> concurrentHashMap = (ConcurrentHashMap<String, List<Float>>)new ConcurrentHashMap<String, String>(7);
        concurrentHashMap.put("sT", (String)this.\u01c3);
        concurrentHashMap.put("sN", this.\u0131);
        concurrentHashMap.put("sV", this.\u03b9);
        final float[] array = this.\u0269[0];
        if (array != null) {
            concurrentHashMap.put("sVS", \u0269(array));
        }
        final float[] array2 = this.\u0269[1];
        if (array2 != null) {
            concurrentHashMap.put("sVE", \u0269(array2));
        }
        return (Map<String, Object>)concurrentHashMap;
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof r) {
            final r r = (r)o;
            return this.\u0399(r.\u01c3, r.\u0131, r.\u03b9);
        }
        return false;
    }
    
    @Override
    public final int hashCode() {
        return this.\u0406;
    }
    
    public final void onAccuracyChanged(final Sensor sensor, final int n) {
    }
    
    public final void onSensorChanged(final SensorEvent sensorEvent) {
        if (sensorEvent != null && sensorEvent.values != null && \u0399(sensorEvent.sensor)) {
            final int type = sensorEvent.sensor.getType();
            final String name = sensorEvent.sensor.getName();
            final String vendor = sensorEvent.sensor.getVendor();
            final long timestamp = sensorEvent.timestamp;
            final float[] values = sensorEvent.values;
            if (this.\u0399(type, name, vendor)) {
                final long currentTimeMillis = System.currentTimeMillis();
                final float[][] \u0269 = this.\u0269;
                final float[] array = \u0269[0];
                if (array == null) {
                    \u0269[0] = Arrays.copyOf(values, values.length);
                    this.\u0399[0] = currentTimeMillis;
                    return;
                }
                final float[] array2 = \u0269[1];
                if (array2 == null) {
                    final float[] copy = Arrays.copyOf(values, values.length);
                    this.\u0269[1] = copy;
                    this.\u0399[1] = currentTimeMillis;
                    this.\u0456 = \u0131(array, copy);
                    return;
                }
                if (50000000L <= timestamp - this.\u0196) {
                    this.\u0196 = timestamp;
                    if (Arrays.equals(array2, values)) {
                        this.\u0399[1] = currentTimeMillis;
                        return;
                    }
                    final double \u0131 = \u0131(array, values);
                    if (\u0131 > this.\u0456) {
                        this.\u0269[1] = Arrays.copyOf(values, values.length);
                        this.\u0399[1] = currentTimeMillis;
                        this.\u0456 = \u0131;
                    }
                }
            }
        }
    }
    
    public final void \u0131(final Map<r, Map<String, Object>> map, final boolean b) {
        if (this.\u01c3()) {
            map.put(this, this.\u03b9());
            if (b) {
                final int length = this.\u0269.length;
                final int n = 0;
                for (int i = 0; i < length; ++i) {
                    this.\u0269[i] = null;
                }
                for (int length2 = this.\u0399.length, j = n; j < length2; ++j) {
                    this.\u0399[j] = 0L;
                }
                this.\u0456 = 0.0;
                this.\u0196 = 0L;
            }
        }
        else if (!map.containsKey(this)) {
            map.put(this, this.\u03b9());
        }
    }
}
