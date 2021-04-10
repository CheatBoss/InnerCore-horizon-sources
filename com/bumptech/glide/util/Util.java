package com.bumptech.glide.util;

import android.graphics.*;
import android.annotation.*;
import java.util.*;
import android.os.*;

public final class Util
{
    private static final char[] HEX_CHAR_ARRAY;
    private static final char[] SHA_1_CHARS;
    private static final char[] SHA_256_CHARS;
    
    static {
        HEX_CHAR_ARRAY = "0123456789abcdef".toCharArray();
        SHA_256_CHARS = new char[64];
        SHA_1_CHARS = new char[40];
    }
    
    private Util() {
    }
    
    public static void assertBackgroundThread() {
        if (!isOnBackgroundThread()) {
            throw new IllegalArgumentException("YOu must call this method on a background thread");
        }
    }
    
    public static void assertMainThread() {
        if (!isOnMainThread()) {
            throw new IllegalArgumentException("You must call this method on the main thread");
        }
    }
    
    private static String bytesToHex(final byte[] array, final char[] array2) {
        for (int i = 0; i < array.length; ++i) {
            final int n = array[i] & 0xFF;
            array2[i * 2] = Util.HEX_CHAR_ARRAY[n >>> 4];
            array2[i * 2 + 1] = Util.HEX_CHAR_ARRAY[n & 0xF];
        }
        return new String(array2);
    }
    
    public static <T> Queue<T> createQueue(final int n) {
        return new ArrayDeque<T>(n);
    }
    
    public static int getBitmapByteSize(final int n, final int n2, final Bitmap$Config bitmap$Config) {
        return n * n2 * getBytesPerPixel(bitmap$Config);
    }
    
    @TargetApi(19)
    public static int getBitmapByteSize(final Bitmap bitmap) {
        if (Build$VERSION.SDK_INT >= 19) {
            try {
                return bitmap.getAllocationByteCount();
            }
            catch (NullPointerException ex) {}
        }
        return bitmap.getHeight() * bitmap.getRowBytes();
    }
    
    private static int getBytesPerPixel(final Bitmap$Config bitmap$Config) {
        Bitmap$Config argb_8888 = bitmap$Config;
        if (bitmap$Config == null) {
            argb_8888 = Bitmap$Config.ARGB_8888;
        }
        switch (argb_8888) {
            default: {
                return 4;
            }
            case RGB_565:
            case ARGB_4444: {
                return 2;
            }
            case ALPHA_8: {
                return 1;
            }
        }
    }
    
    @Deprecated
    public static int getSize(final Bitmap bitmap) {
        return getBitmapByteSize(bitmap);
    }
    
    public static <T> List<T> getSnapshot(final Collection<T> collection) {
        final ArrayList<T> list = new ArrayList<T>(collection.size());
        final Iterator<T> iterator = collection.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }
    
    public static boolean isOnBackgroundThread() {
        return isOnMainThread() ^ true;
    }
    
    public static boolean isOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
    
    private static boolean isValidDimension(final int n) {
        return n > 0 || n == Integer.MIN_VALUE;
    }
    
    public static boolean isValidDimensions(final int n, final int n2) {
        return isValidDimension(n) && isValidDimension(n2);
    }
    
    public static String sha1BytesToHex(final byte[] array) {
        synchronized (Util.SHA_1_CHARS) {
            return bytesToHex(array, Util.SHA_1_CHARS);
        }
    }
    
    public static String sha256BytesToHex(final byte[] array) {
        synchronized (Util.SHA_256_CHARS) {
            return bytesToHex(array, Util.SHA_256_CHARS);
        }
    }
}
