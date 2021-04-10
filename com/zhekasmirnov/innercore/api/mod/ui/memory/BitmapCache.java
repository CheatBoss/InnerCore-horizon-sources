package com.zhekasmirnov.innercore.api.mod.ui.memory;

import com.zhekasmirnov.innercore.utils.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import android.graphics.*;
import java.nio.*;
import com.zhekasmirnov.innercore.api.log.*;
import java.util.*;
import java.io.*;
import java.nio.channels.*;

public class BitmapCache
{
    public static final String CACHE_DIR;
    private static final int DEFAULT_MAX_STACK_POS = 64;
    private static boolean isGCRunning;
    private static ArrayList<BitmapWrap> registeredWraps;
    private static int useId;
    
    static {
        final StringBuilder sb = new StringBuilder();
        sb.append(FileTools.DIR_WORK);
        sb.append("cache/bmp/");
        FileTools.mkdirs(CACHE_DIR = sb.toString());
        try {
            final File[] listFiles = new File(BitmapCache.CACHE_DIR).listFiles();
            for (int length = listFiles.length, i = 0; i < length; ++i) {
                listFiles[i].delete();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        BitmapCache.useId = 0;
        BitmapCache.registeredWraps = new ArrayList<BitmapWrap>();
        BitmapCache.isGCRunning = false;
    }
    
    public static void asyncGC() {
        if (!BitmapCache.isGCRunning) {
            BitmapCache.isGCRunning = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final long currentTimeMillis = System.currentTimeMillis();
                    BitmapCache.storeOldWraps(64);
                    final StringBuilder sb = new StringBuilder();
                    sb.append("async GC took ");
                    sb.append(System.currentTimeMillis() - currentTimeMillis);
                    sb.append(" ms");
                    Logger.info("UI", sb.toString());
                    BitmapCache.isGCRunning = false;
                }
            }).start();
        }
    }
    
    static File getCacheFile(final String s) {
        return new File(BitmapCache.CACHE_DIR, s);
    }
    
    static int getStackPos(final int n) {
        return BitmapCache.useId - n;
    }
    
    static int getUseId() {
        final int useId = BitmapCache.useId;
        BitmapCache.useId = useId + 1;
        return useId;
    }
    
    public static void immediateGC() {
        final long currentTimeMillis = System.currentTimeMillis();
        storeOldWraps(64);
        final StringBuilder sb = new StringBuilder();
        sb.append("immediate GC took ");
        sb.append(System.currentTimeMillis() - currentTimeMillis);
        sb.append(" ms");
        Logger.info("UI", sb.toString());
    }
    
    public static void init() {
    }
    
    static void readFromFile(final File file, final Bitmap bitmap) throws IOException {
        try {
            final RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            final byte[] array = new byte[(int)randomAccessFile.length()];
            randomAccessFile.readFully(array);
            final ByteBuffer allocateDirect = ByteBuffer.allocateDirect(array.length);
            allocateDirect.put(array).rewind();
            bitmap.copyPixelsFromBuffer((Buffer)allocateDirect);
        }
        catch (NullPointerException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("failed to read bitmap: ");
            sb.append(ex);
            throw new IOException(sb.toString());
        }
    }
    
    static void registerWrap(final BitmapWrap bitmapWrap) {
        BitmapCache.registeredWraps.add(bitmapWrap);
    }
    
    static void storeOldWraps(final int n) {
        // monitorenter(BitmapCache.class)
        try {
            try {
                for (final BitmapWrap bitmapWrap : new ArrayList<BitmapWrap>(BitmapCache.registeredWraps)) {
                    if (bitmapWrap != null && bitmapWrap.getStackPos() > n) {
                        bitmapWrap.storeIfNeeded();
                    }
                }
            }
            finally {}
        }
        catch (ConcurrentModificationException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("GC failed and will be restarted: ");
            sb.append(ex);
            ICLog.i("UI", sb.toString());
            storeOldWraps(n);
        }
        // monitorexit(BitmapCache.class)
        return;
    }
    // monitorexit(BitmapCache.class)
    
    public static Bitmap testCaching(final Bitmap bitmap) {
        final File cacheFile = getCacheFile(bitmap.toString());
        try {
            writeToFile(cacheFile, bitmap);
            final Bitmap bitmap2 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
            try {
                readFromFile(cacheFile, bitmap2);
                return bitmap2;
            }
            catch (IOException ex) {
                ex.printStackTrace();
                return bitmap;
            }
        }
        catch (IOException ex2) {
            ex2.printStackTrace();
            return bitmap;
        }
    }
    
    static void unregisterWrap(final BitmapWrap bitmapWrap) {
        BitmapCache.registeredWraps.remove(bitmapWrap);
    }
    
    static void writeToFile(final File file, final Bitmap bitmap) throws IOException {
        try {
            final ByteBuffer allocateDirect = ByteBuffer.allocateDirect(bitmap.getRowBytes() * bitmap.getHeight());
            bitmap.copyPixelsToBuffer((Buffer)allocateDirect);
            final FileChannel channel = new FileOutputStream(file, false).getChannel();
            allocateDirect.rewind();
            channel.write(allocateDirect);
            channel.close();
        }
        catch (NullPointerException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("failed to write bitmap: ");
            sb.append(ex);
            throw new IOException(sb.toString());
        }
    }
}
