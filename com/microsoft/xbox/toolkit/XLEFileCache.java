package com.microsoft.xbox.toolkit;

import java.util.*;
import java.security.*;
import java.io.*;

public class XLEFileCache
{
    private static final String TAG;
    private boolean enabled;
    private final long expiredTimer;
    final int maxFileNumber;
    private int readAccessCnt;
    private int readSuccessfulCnt;
    int size;
    private int writeAccessCnt;
    
    static {
        TAG = XLEFileCache.class.getSimpleName();
    }
    
    XLEFileCache() {
        this.size = 0;
        this.enabled = true;
        this.readAccessCnt = 0;
        this.writeAccessCnt = 0;
        this.readSuccessfulCnt = 0;
        this.expiredTimer = Long.MAX_VALUE;
        this.maxFileNumber = 0;
        this.enabled = false;
    }
    
    XLEFileCache(final String s, final int n) {
        this(s, n, Long.MAX_VALUE);
    }
    
    XLEFileCache(final String s, final int maxFileNumber, final long expiredTimer) {
        this.size = 0;
        this.enabled = true;
        this.readAccessCnt = 0;
        this.writeAccessCnt = 0;
        this.readSuccessfulCnt = 0;
        this.maxFileNumber = maxFileNumber;
        this.expiredTimer = expiredTimer;
    }
    
    private void checkAndEnsureCapacity() {
        if (this.size >= this.maxFileNumber && this.enabled) {
            final File[] listFiles = XLEFileCacheManager.getCacheRootDir(this).listFiles();
            listFiles[new Random().nextInt(listFiles.length)].delete();
            this.size = listFiles.length - 1;
        }
    }
    
    private String getCachedItemFileName(final XLEFileCacheItemKey xleFileCacheItemKey) {
        return String.valueOf(xleFileCacheItemKey.getKeyString().hashCode());
    }
    
    private static int readInt(final InputStream inputStream) throws IOException {
        final int read = inputStream.read();
        final int read2 = inputStream.read();
        final int read3 = inputStream.read();
        final int read4 = inputStream.read();
        if ((read | read2 | read3 | read4) >= 0) {
            return (read << 24) + (read2 << 16) + (read3 << 8) + (read4 << 0);
        }
        throw new EOFException();
    }
    
    public boolean contains(final XLEFileCacheItemKey xleFileCacheItemKey) {
        synchronized (this) {
            return this.enabled && new File(XLEFileCacheManager.getCacheRootDir(this), this.getCachedItemFileName(xleFileCacheItemKey)).exists();
        }
    }
    
    public InputStream getInputStreamForRead(final XLEFileCacheItemKey xleFileCacheItemKey) {
        while (true) {
            while (true) {
                synchronized (this) {
                    if (!this.enabled) {
                        return null;
                    }
                    if (Thread.currentThread() != ThreadManager.UIThread) {
                        final boolean b = true;
                        XLEAssert.assertTrue(b);
                        ++this.readAccessCnt;
                        final File file = new File(XLEFileCacheManager.getCacheRootDir(this), this.getCachedItemFileName(xleFileCacheItemKey));
                        if (file.exists()) {
                            if (file.lastModified() < System.currentTimeMillis() - this.expiredTimer) {
                                file.delete();
                                --this.size;
                                return null;
                            }
                            try {
                                final InputStream contentInputStream = new CachedFileInputStreamItem(xleFileCacheItemKey, file).getContentInputStream();
                                ++this.readSuccessfulCnt;
                                return contentInputStream;
                            }
                            catch (IOException ex) {}
                        }
                        return null;
                    }
                }
                final boolean b = false;
                continue;
            }
        }
    }
    
    public int getItemsInCache() {
        return this.size;
    }
    
    public OutputStream getOuputStreamForSave(final XLEFileCacheItemKey xleFileCacheItemKey) throws IOException {
        while (true) {
            while (true) {
                synchronized (this) {
                    if (!this.enabled) {
                        return new OutputStream() {
                            @Override
                            public void write(final int n) throws IOException {
                            }
                        };
                    }
                    if (Thread.currentThread() != ThreadManager.UIThread) {
                        final boolean b = true;
                        XLEAssert.assertTrue(b);
                        ++this.writeAccessCnt;
                        this.checkAndEnsureCapacity();
                        final File file = new File(XLEFileCacheManager.getCacheRootDir(this), this.getCachedItemFileName(xleFileCacheItemKey));
                        if (file.exists()) {
                            file.delete();
                            --this.size;
                        }
                        if (file.createNewFile()) {
                            ++this.size;
                        }
                        return new CachedFileOutputStreamItem(xleFileCacheItemKey, file);
                    }
                }
                final boolean b = false;
                continue;
            }
        }
    }
    
    public void save(final XLEFileCacheItemKey xleFileCacheItemKey, final InputStream inputStream) {
        synchronized (this) {
            try {
                final OutputStream ouputStreamForSave = this.getOuputStreamForSave(xleFileCacheItemKey);
                StreamUtil.CopyStream(ouputStreamForSave, inputStream);
                ouputStreamForSave.close();
            }
            catch (IOException ex) {}
        }
    }
    // monitorexit(this)
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Size=");
        sb.append(this.size);
        sb.append("\tRootDir=");
        sb.append(XLEFileCacheManager.getCacheRootDir(this));
        sb.append("\tMaxFileNumber=");
        sb.append(this.maxFileNumber);
        sb.append("\tExpiredTimerInSeconds=");
        sb.append(this.expiredTimer);
        sb.append("\tWriteAccessCnt=");
        sb.append(this.writeAccessCnt);
        sb.append("\tReadAccessCnt=");
        sb.append(this.readAccessCnt);
        sb.append("\tReadSuccessfulCnt=");
        sb.append(this.readSuccessfulCnt);
        return sb.toString();
    }
    
    private class CachedFileInputStreamItem
    {
        private byte[] computedMd5;
        private InputStream contentInputStream;
        private MessageDigest mDigest;
        private byte[] savedMd5;
        
        public CachedFileInputStreamItem(final XLEFileCacheItemKey xleFileCacheItemKey, final File file) throws IOException {
            this.mDigest = null;
            XLEFileCache.this = (XLEFileCache)new FileInputStream(file);
            try {
                final MessageDigest instance = MessageDigest.getInstance("MD5");
                this.mDigest = instance;
                final byte[] savedMd5 = new byte[instance.getDigestLength()];
                this.savedMd5 = savedMd5;
                if (((FileInputStream)XLEFileCache.this).read(savedMd5) != this.mDigest.getDigestLength()) {
                    ((FileInputStream)XLEFileCache.this).close();
                    throw new IOException("Ddigest lengh check failed!");
                }
                final int access$000 = readInt((InputStream)XLEFileCache.this);
                final byte[] array = new byte[access$000];
                if (access$000 != ((FileInputStream)XLEFileCache.this).read(array) || !xleFileCacheItemKey.getKeyString().equals(new String(array))) {
                    file.delete();
                    throw new IOException("File key check failed because keyLength != readKeyLength or !key.getKeyString().equals(new String(urlOrSomething))");
                }
                final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                StreamUtil.CopyStream(byteArrayOutputStream, (InputStream)XLEFileCache.this);
                ((FileInputStream)XLEFileCache.this).close();
                final byte[] byteArray = byteArrayOutputStream.toByteArray();
                this.mDigest.update(byteArray);
                this.computedMd5 = this.mDigest.digest();
                if (!this.isMd5Error()) {
                    this.contentInputStream = new ByteArrayInputStream(byteArray);
                    return;
                }
                file.delete();
                final StringBuilder sb = new StringBuilder();
                sb.append(((FileInputStream)XLEFileCache.this).getFD());
                sb.append("the saved md5 is not equal computed md5.ComputedMd5:");
                sb.append(this.computedMd5);
                sb.append("     SavedMd5:");
                sb.append(this.savedMd5);
                throw new IOException(sb.toString());
            }
            catch (OutOfMemoryError outOfMemoryError) {
                ((FileInputStream)XLEFileCache.this).close();
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("File digest failed! Out of memory: ");
                sb2.append(outOfMemoryError.getMessage());
                throw new IOException(sb2.toString());
            }
            catch (NoSuchAlgorithmException ex) {
                ((FileInputStream)XLEFileCache.this).close();
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("File digest failed! ");
                sb3.append(ex.getMessage());
                throw new IOException(sb3.toString());
            }
        }
        
        private boolean isMd5Error() {
            for (int i = 0; i < this.mDigest.getDigestLength(); ++i) {
                if (this.savedMd5[i] != this.computedMd5[i]) {
                    return true;
                }
            }
            return false;
        }
        
        public InputStream getContentInputStream() {
            return this.contentInputStream;
        }
    }
    
    private class CachedFileOutputStreamItem extends FileOutputStream
    {
        private File destFile;
        private MessageDigest mDigest;
        private boolean startDigest;
        private boolean writeMd5Finished;
        
        public CachedFileOutputStreamItem(final XLEFileCacheItemKey xleFileCacheItemKey, final File destFile) throws IOException {
            super(destFile);
            this.mDigest = null;
            this.startDigest = false;
            this.writeMd5Finished = false;
            this.destFile = destFile;
            try {
                final MessageDigest instance = MessageDigest.getInstance("MD5");
                this.mDigest = instance;
                this.write(new byte[instance.getDigestLength()]);
                final byte[] bytes = xleFileCacheItemKey.getKeyString().getBytes();
                this.writeInt(bytes.length);
                this.write(bytes);
                this.startDigest = true;
            }
            catch (NoSuchAlgorithmException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("File digest failed!");
                sb.append(ex.getMessage());
                throw new IOException(sb.toString());
            }
        }
        
        private final void writeInt(final int n) throws IOException {
            this.write(n >>> 24 & 0xFF);
            this.write(n >>> 16 & 0xFF);
            this.write(n >>> 8 & 0xFF);
            this.write(n >>> 0 & 0xFF);
        }
        
        @Override
        public void close() throws IOException {
            super.close();
            if (!this.writeMd5Finished) {
                this.writeMd5Finished = true;
                final RandomAccessFile randomAccessFile = new RandomAccessFile(this.destFile, "rw");
                final byte[] digest = this.mDigest.digest();
                randomAccessFile.seek(0L);
                randomAccessFile.write(digest);
                randomAccessFile.close();
            }
        }
        
        @Override
        public void write(final byte[] array, final int n, final int n2) throws IOException {
            super.write(array, n, n2);
            if (this.startDigest) {
                this.mDigest.update(array, n, n2);
            }
        }
    }
}
