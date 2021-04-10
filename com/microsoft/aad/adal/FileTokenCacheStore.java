package com.microsoft.aad.adal;

import android.content.*;
import java.io.*;
import java.util.*;

public class FileTokenCacheStore implements ITokenCacheStore
{
    private static final String TAG;
    private static final long serialVersionUID = -8252291336171327870L;
    private final Object mCacheLock;
    private final File mFile;
    private final MemoryTokenCacheStore mInMemoryCache;
    
    public FileTokenCacheStore(final Context context, final String s) {
        this.mCacheLock = new Object();
        if (context == null) {
            throw new IllegalArgumentException("context");
        }
        if (!StringExtensions.isNullOrBlank(s)) {
            final File dir = context.getDir(context.getPackageName(), 0);
            if (dir != null) {
                try {
                    final File mFile = new File(dir, s);
                    this.mFile = mFile;
                    if (!mFile.exists()) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append(FileTokenCacheStore.TAG);
                        sb.append(":FileTokenCacheStore");
                        Logger.v(sb.toString(), "There is not any previous cache file to load cache. ");
                        this.mInMemoryCache = new MemoryTokenCacheStore();
                        return;
                    }
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(FileTokenCacheStore.TAG);
                    sb2.append(":FileTokenCacheStore");
                    Logger.v(sb2.toString(), "There is previous cache file to load cache. ");
                    final FileInputStream fileInputStream = new FileInputStream(this.mFile);
                    final ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                    final Object object = objectInputStream.readObject();
                    fileInputStream.close();
                    objectInputStream.close();
                    if (object instanceof MemoryTokenCacheStore) {
                        this.mInMemoryCache = (MemoryTokenCacheStore)object;
                        return;
                    }
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append(FileTokenCacheStore.TAG);
                    sb3.append(":FileTokenCacheStore");
                    Logger.w(sb3.toString(), "Existing cache format is wrong. ", "", ADALError.DEVICE_FILE_CACHE_FORMAT_IS_WRONG);
                    this.mInMemoryCache = new MemoryTokenCacheStore();
                    return;
                }
                catch (IOException | ClassNotFoundException ex3) {
                    final ClassNotFoundException ex2;
                    final ClassNotFoundException ex = ex2;
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append(FileTokenCacheStore.TAG);
                    sb4.append(":FileTokenCacheStore");
                    Logger.e(sb4.toString(), "Exception during cache load. ", ExceptionExtensions.getExceptionMessage(ex), ADALError.DEVICE_FILE_CACHE_IS_NOT_LOADED_FROM_FILE);
                    throw new IllegalStateException(ex);
                }
            }
            throw new IllegalStateException("It could not access the Authorization cache directory");
        }
        throw new IllegalArgumentException("fileName");
    }
    
    private void writeToFile() {
        synchronized (this.mCacheLock) {
            if (this.mFile != null && this.mInMemoryCache != null) {
                try {
                    final FileOutputStream fileOutputStream = new FileOutputStream(this.mFile);
                    final ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                    objectOutputStream.writeObject(this.mInMemoryCache);
                    objectOutputStream.flush();
                    objectOutputStream.close();
                    fileOutputStream.close();
                }
                catch (IOException ex) {
                    Logger.e(FileTokenCacheStore.TAG, "Exception during cache flush", ExceptionExtensions.getExceptionMessage(ex), ADALError.DEVICE_FILE_CACHE_IS_NOT_WRITING_TO_FILE);
                }
            }
        }
    }
    
    @Override
    public boolean contains(final String s) {
        return this.mInMemoryCache.contains(s);
    }
    
    @Override
    public Iterator<TokenCacheItem> getAll() {
        return this.mInMemoryCache.getAll();
    }
    
    @Override
    public TokenCacheItem getItem(final String s) {
        return this.mInMemoryCache.getItem(s);
    }
    
    @Override
    public void removeAll() {
        this.mInMemoryCache.removeAll();
        this.writeToFile();
    }
    
    @Override
    public void removeItem(final String s) {
        this.mInMemoryCache.removeItem(s);
        this.writeToFile();
    }
    
    @Override
    public void setItem(final String s, final TokenCacheItem tokenCacheItem) {
        this.mInMemoryCache.setItem(s, tokenCacheItem);
        this.writeToFile();
    }
}
