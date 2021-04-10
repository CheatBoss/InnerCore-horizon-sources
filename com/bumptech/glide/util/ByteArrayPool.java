package com.bumptech.glide.util;

import java.util.*;
import android.util.*;

public final class ByteArrayPool
{
    private static final ByteArrayPool BYTE_ARRAY_POOL;
    private static final int MAX_BYTE_ARRAY_COUNT = 32;
    private static final int MAX_SIZE = 2146304;
    private static final String TAG = "ByteArrayPool";
    private static final int TEMP_BYTES_SIZE = 65536;
    private final Queue<byte[]> tempQueue;
    
    static {
        BYTE_ARRAY_POOL = new ByteArrayPool();
    }
    
    private ByteArrayPool() {
        this.tempQueue = Util.createQueue(0);
    }
    
    public static ByteArrayPool get() {
        return ByteArrayPool.BYTE_ARRAY_POOL;
    }
    
    public void clear() {
        synchronized (this.tempQueue) {
            this.tempQueue.clear();
        }
    }
    
    public byte[] getBytes() {
        Object tempQueue = this.tempQueue;
        synchronized (tempQueue) {
            final byte[] array = this.tempQueue.poll();
            // monitorexit(tempQueue)
            tempQueue = array;
            if (array == null) {
                final byte[] array2 = (byte[])(tempQueue = new byte[65536]);
                if (Log.isLoggable("ByteArrayPool", 3)) {
                    Log.d("ByteArrayPool", "Created temp bytes");
                    tempQueue = array2;
                }
            }
            return (byte[])tempQueue;
        }
    }
    
    public boolean releaseBytes(final byte[] array) {
        if (array.length != 65536) {
            return false;
        }
        boolean b = false;
        synchronized (this.tempQueue) {
            if (this.tempQueue.size() < 32) {
                b = true;
                this.tempQueue.offer(array);
            }
            return b;
        }
    }
}
