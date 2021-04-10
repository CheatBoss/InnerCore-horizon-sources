package com.microsoft.xbox.toolkit;

public class TimeMonitor
{
    private final long NSTOMSEC;
    private long endTicks;
    private long startTicks;
    
    public TimeMonitor() {
        this.startTicks = 0L;
        this.endTicks = 0L;
        this.NSTOMSEC = 1000000L;
    }
    
    public long currentTime() {
        return (System.nanoTime() - this.startTicks) / 1000000L;
    }
    
    public long getElapsedMs() {
        final boolean isStarted = this.getIsStarted();
        long n = 0L;
        if (isStarted) {
            long n2 = this.endTicks;
            if (n2 == 0L) {
                n2 = System.nanoTime();
            }
            n = (n2 - this.startTicks) / 1000000L;
        }
        return n;
    }
    
    public boolean getIsEnded() {
        return this.endTicks != 0L;
    }
    
    public boolean getIsStarted() {
        return this.startTicks != 0L;
    }
    
    public void reset() {
        this.startTicks = 0L;
        this.endTicks = 0L;
    }
    
    public void saveCurrentTime() {
        if (this.getIsStarted()) {
            this.endTicks = System.nanoTime();
        }
    }
    
    public void start() {
        this.startTicks = System.nanoTime();
        this.endTicks = 0L;
    }
    
    public void stop() {
        if (this.startTicks != 0L && this.endTicks == 0L) {
            this.endTicks = System.nanoTime();
        }
    }
}
