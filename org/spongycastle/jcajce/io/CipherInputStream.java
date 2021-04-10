package org.spongycastle.jcajce.io;

import javax.crypto.*;
import org.spongycastle.crypto.io.*;
import java.security.*;
import java.io.*;

public class CipherInputStream extends FilterInputStream
{
    private byte[] buf;
    private int bufOff;
    private final Cipher cipher;
    private boolean finalized;
    private final byte[] inputBuffer;
    private int maxBuf;
    
    public CipherInputStream(final InputStream inputStream, final Cipher cipher) {
        super(inputStream);
        this.inputBuffer = new byte[512];
        this.finalized = false;
        this.cipher = cipher;
    }
    
    private byte[] finaliseCipher() throws InvalidCipherTextIOException {
        try {
            this.finalized = true;
            return this.cipher.doFinal();
        }
        catch (GeneralSecurityException ex) {
            throw new InvalidCipherTextIOException("Error finalising cipher", ex);
        }
    }
    
    private int nextChunk() throws IOException {
        if (this.finalized) {
            return -1;
        }
        this.bufOff = 0;
        this.maxBuf = 0;
        while (true) {
            final int maxBuf = this.maxBuf;
            if (maxBuf != 0) {
                return maxBuf;
            }
            final int read = this.in.read(this.inputBuffer);
            if (read == -1) {
                final byte[] finaliseCipher = this.finaliseCipher();
                if ((this.buf = finaliseCipher) == null) {
                    return -1;
                }
                if (finaliseCipher.length == 0) {
                    return -1;
                }
                return this.maxBuf = finaliseCipher.length;
            }
            else {
                final byte[] update = this.cipher.update(this.inputBuffer, 0, read);
                if ((this.buf = update) == null) {
                    continue;
                }
                this.maxBuf = update.length;
            }
        }
    }
    
    @Override
    public int available() throws IOException {
        return this.maxBuf - this.bufOff;
    }
    
    @Override
    public void close() throws IOException {
        try {
            this.in.close();
            if (!this.finalized) {
                this.finaliseCipher();
            }
            this.bufOff = 0;
            this.maxBuf = 0;
        }
        finally {
            if (!this.finalized) {
                this.finaliseCipher();
            }
        }
    }
    
    @Override
    public void mark(final int n) {
    }
    
    @Override
    public boolean markSupported() {
        return false;
    }
    
    @Override
    public int read() throws IOException {
        if (this.bufOff >= this.maxBuf && this.nextChunk() < 0) {
            return -1;
        }
        return this.buf[this.bufOff++] & 0xFF;
    }
    
    @Override
    public int read(final byte[] array, final int n, int min) throws IOException {
        if (this.bufOff >= this.maxBuf && this.nextChunk() < 0) {
            return -1;
        }
        min = Math.min(min, this.available());
        System.arraycopy(this.buf, this.bufOff, array, n, min);
        this.bufOff += min;
        return min;
    }
    
    @Override
    public void reset() throws IOException {
    }
    
    @Override
    public long skip(final long n) throws IOException {
        if (n <= 0L) {
            return 0L;
        }
        final int n2 = (int)Math.min(n, this.available());
        this.bufOff += n2;
        return n2;
    }
}
