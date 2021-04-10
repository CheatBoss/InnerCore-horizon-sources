package net.lingala.zip4j.io;

import net.lingala.zip4j.unzip.*;
import net.lingala.zip4j.crypto.*;
import java.io.*;
import net.lingala.zip4j.exception.*;

public class PartInputStream extends BaseInputStream
{
    private byte[] aesBlockByte;
    private int aesBytesReturned;
    private long bytesRead;
    private int count;
    private IDecrypter decrypter;
    private boolean isAESEncryptedFile;
    private long length;
    private byte[] oneByteBuff;
    private RandomAccessFile raf;
    private UnzipEngine unzipEngine;
    
    public PartInputStream(final RandomAccessFile raf, final long n, final long length, final UnzipEngine unzipEngine) {
        boolean isAESEncryptedFile = true;
        this.oneByteBuff = new byte[1];
        this.aesBlockByte = new byte[16];
        this.aesBytesReturned = 0;
        this.isAESEncryptedFile = false;
        this.count = -1;
        this.raf = raf;
        this.unzipEngine = unzipEngine;
        this.decrypter = unzipEngine.getDecrypter();
        this.bytesRead = 0L;
        this.length = length;
        if (!unzipEngine.getFileHeader().isEncrypted() || unzipEngine.getFileHeader().getEncryptionMethod() != 99) {
            isAESEncryptedFile = false;
        }
        this.isAESEncryptedFile = isAESEncryptedFile;
    }
    
    private void checkAndReadAESMacBytes() throws IOException {
        if (this.isAESEncryptedFile && this.decrypter != null && this.decrypter instanceof AESDecrypter) {
            if (((AESDecrypter)this.decrypter).getStoredMac() != null) {
                return;
            }
            final byte[] storedMac = new byte[10];
            final int read = this.raf.read(storedMac);
            if (read != 10) {
                if (!this.unzipEngine.getZipModel().isSplitArchive()) {
                    throw new IOException("Error occured while reading stored AES authentication bytes");
                }
                this.raf.close();
                (this.raf = this.unzipEngine.startNextSplitFile()).read(storedMac, read, 10 - read);
            }
            ((AESDecrypter)this.unzipEngine.getDecrypter()).setStoredMac(storedMac);
        }
    }
    
    @Override
    public int available() {
        final long n = this.length - this.bytesRead;
        if (n > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int)n;
    }
    
    @Override
    public void close() throws IOException {
        this.raf.close();
    }
    
    @Override
    public UnzipEngine getUnzipEngine() {
        return this.unzipEngine;
    }
    
    @Override
    public int read() throws IOException {
        if (this.bytesRead >= this.length) {
            return -1;
        }
        if (this.isAESEncryptedFile) {
            if (this.aesBytesReturned == 0 || this.aesBytesReturned == 16) {
                if (this.read(this.aesBlockByte) == -1) {
                    return -1;
                }
                this.aesBytesReturned = 0;
            }
            return this.aesBlockByte[this.aesBytesReturned++] & 0xFF;
        }
        if (this.read(this.oneByteBuff, 0, 1) == -1) {
            return -1;
        }
        return this.oneByteBuff[0] & 0xFF;
    }
    
    @Override
    public int read(final byte[] array) throws IOException {
        return this.read(array, 0, array.length);
    }
    
    @Override
    public int read(final byte[] array, final int n, int read) throws IOException {
        int n2 = read;
        if (read > this.length - this.bytesRead) {
            read = (int)(this.length - this.bytesRead);
            if ((n2 = read) == 0) {
                this.checkAndReadAESMacBytes();
                return -1;
            }
        }
        read = n2;
        if (this.unzipEngine.getDecrypter() instanceof AESDecrypter) {
            read = n2;
            if (this.bytesRead + n2 < this.length) {
                read = n2;
                if (n2 % 16 != 0) {
                    read = n2 - n2 % 16;
                }
            }
        }
        synchronized (this.raf) {
            this.count = this.raf.read(array, n, read);
            if (this.count < read && this.unzipEngine.getZipModel().isSplitArchive()) {
                this.raf.close();
                this.raf = this.unzipEngine.startNextSplitFile();
                if (this.count < 0) {
                    this.count = 0;
                }
                read = this.raf.read(array, this.count, read - this.count);
                if (read > 0) {
                    this.count += read;
                }
            }
            // monitorexit(this.raf)
            if (this.count > 0) {
                if (this.decrypter != null) {
                    try {
                        this.decrypter.decryptData(array, n, this.count);
                    }
                    catch (ZipException ex) {
                        throw new IOException(ex.getMessage());
                    }
                }
                this.bytesRead += this.count;
            }
            if (this.bytesRead >= this.length) {
                this.checkAndReadAESMacBytes();
            }
            return this.count;
        }
    }
    
    @Override
    public void seek(final long n) throws IOException {
        this.raf.seek(n);
    }
    
    @Override
    public long skip(final long n) throws IOException {
        if (n < 0L) {
            throw new IllegalArgumentException();
        }
        long n2 = n;
        if (n > this.length - this.bytesRead) {
            n2 = this.length - this.bytesRead;
        }
        this.bytesRead += n2;
        return n2;
    }
}
