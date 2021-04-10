package okhttp3.internal.http2;

import java.util.*;

public final class Settings
{
    private int set;
    private final int[] values;
    
    public Settings() {
        this.values = new int[10];
    }
    
    void clear() {
        this.set = 0;
        Arrays.fill(this.values, 0);
    }
    
    int get(final int n) {
        return this.values[n];
    }
    
    int getHeaderTableSize() {
        if ((this.set & 0x2) != 0x0) {
            return this.values[1];
        }
        return -1;
    }
    
    int getInitialWindowSize() {
        if ((this.set & 0x80) != 0x0) {
            return this.values[7];
        }
        return 65535;
    }
    
    int getMaxConcurrentStreams(int n) {
        if ((this.set & 0x10) != 0x0) {
            n = this.values[4];
        }
        return n;
    }
    
    int getMaxFrameSize(int n) {
        if ((this.set & 0x20) != 0x0) {
            n = this.values[5];
        }
        return n;
    }
    
    boolean isSet(final int n) {
        return (1 << n & this.set) != 0x0;
    }
    
    void merge(final Settings settings) {
        for (int i = 0; i < 10; ++i) {
            if (settings.isSet(i)) {
                this.set(i, settings.get(i));
            }
        }
    }
    
    Settings set(final int n, final int n2) {
        if (n >= 0) {
            final int[] values = this.values;
            if (n >= values.length) {
                return this;
            }
            this.set |= 1 << n;
            values[n] = n2;
        }
        return this;
    }
    
    int size() {
        return Integer.bitCount(this.set);
    }
}
