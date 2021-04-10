package com.zhekasmirnov.innercore.api.runtime;

import java.util.*;

public class TPSMeter
{
    private static HashMap<String, TPSMeter> tpsMeterByName;
    private int frame;
    private long lastMeasuredFrame;
    private long lastMeasuredTime;
    private final int maxFramesPerMeasure;
    private final int maxTimePerMeasure;
    private float tps;
    
    static {
        TPSMeter.tpsMeterByName = new HashMap<String, TPSMeter>();
    }
    
    public TPSMeter(final int maxFramesPerMeasure, final int maxTimePerMeasure) {
        this.lastMeasuredTime = -1L;
        this.lastMeasuredFrame = 0L;
        this.frame = 0;
        this.tps = 0.0f;
        this.maxFramesPerMeasure = maxFramesPerMeasure;
        this.maxTimePerMeasure = maxTimePerMeasure;
    }
    
    public TPSMeter(final String s, final int n, final int n2) {
        this(n, n2);
        TPSMeter.tpsMeterByName.put(s, this);
    }
    
    public static TPSMeter getByName(final String s) {
        return TPSMeter.tpsMeterByName.get(s);
    }
    
    public float getTps() {
        return Math.round(this.tps * 10.0f) / 10.0f;
    }
    
    public void onTick() {
        final long currentTimeMillis = System.currentTimeMillis();
        if (this.frame++ % this.maxFramesPerMeasure == 0 || currentTimeMillis - this.lastMeasuredTime > this.maxTimePerMeasure) {
            this.tps = (this.frame - this.lastMeasuredFrame) * 1000L / (float)(currentTimeMillis - this.lastMeasuredTime);
            this.lastMeasuredFrame = this.frame;
            this.lastMeasuredTime = currentTimeMillis;
        }
    }
}
