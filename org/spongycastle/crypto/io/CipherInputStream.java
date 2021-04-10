package org.spongycastle.crypto.io;

import org.spongycastle.crypto.modes.*;
import java.io.*;
import org.spongycastle.crypto.*;
import org.spongycastle.util.*;

public class CipherInputStream extends FilterInputStream
{
    private static final int INPUT_BUF_SIZE = 2048;
    private AEADBlockCipher aeadBlockCipher;
    private byte[] buf;
    private int bufOff;
    private BufferedBlockCipher bufferedBlockCipher;
    private boolean finalized;
    private byte[] inBuf;
    private byte[] markBuf;
    private int markBufOff;
    private long markPosition;
    private int maxBuf;
    private SkippingCipher skippingCipher;
    private StreamCipher streamCipher;
    
    public CipherInputStream(final InputStream inputStream, final BufferedBlockCipher bufferedBlockCipher) {
        this(inputStream, bufferedBlockCipher, 2048);
    }
    
    public CipherInputStream(final InputStream inputStream, final BufferedBlockCipher bufferedBlockCipher, final int n) {
        super(inputStream);
        this.bufferedBlockCipher = bufferedBlockCipher;
        this.inBuf = new byte[n];
        SkippingCipher skippingCipher;
        if (bufferedBlockCipher instanceof SkippingCipher) {
            skippingCipher = (SkippingCipher)bufferedBlockCipher;
        }
        else {
            skippingCipher = null;
        }
        this.skippingCipher = skippingCipher;
    }
    
    public CipherInputStream(final InputStream inputStream, final StreamCipher streamCipher) {
        this(inputStream, streamCipher, 2048);
    }
    
    public CipherInputStream(final InputStream inputStream, final StreamCipher streamCipher, final int n) {
        super(inputStream);
        this.streamCipher = streamCipher;
        this.inBuf = new byte[n];
        SkippingCipher skippingCipher;
        if (streamCipher instanceof SkippingCipher) {
            skippingCipher = (SkippingCipher)streamCipher;
        }
        else {
            skippingCipher = null;
        }
        this.skippingCipher = skippingCipher;
    }
    
    public CipherInputStream(final InputStream inputStream, final AEADBlockCipher aeadBlockCipher) {
        this(inputStream, aeadBlockCipher, 2048);
    }
    
    public CipherInputStream(final InputStream inputStream, final AEADBlockCipher aeadBlockCipher, final int n) {
        super(inputStream);
        this.aeadBlockCipher = aeadBlockCipher;
        this.inBuf = new byte[n];
        SkippingCipher skippingCipher;
        if (aeadBlockCipher instanceof SkippingCipher) {
            skippingCipher = (SkippingCipher)aeadBlockCipher;
        }
        else {
            skippingCipher = null;
        }
        this.skippingCipher = skippingCipher;
    }
    
    private void ensureCapacity(final int n, final boolean b) {
        int n2;
        if (b) {
            final BufferedBlockCipher bufferedBlockCipher = this.bufferedBlockCipher;
            if (bufferedBlockCipher != null) {
                n2 = bufferedBlockCipher.getOutputSize(n);
            }
            else {
                final AEADBlockCipher aeadBlockCipher = this.aeadBlockCipher;
                n2 = n;
                if (aeadBlockCipher != null) {
                    n2 = aeadBlockCipher.getOutputSize(n);
                }
            }
        }
        else {
            final BufferedBlockCipher bufferedBlockCipher2 = this.bufferedBlockCipher;
            if (bufferedBlockCipher2 != null) {
                n2 = bufferedBlockCipher2.getUpdateOutputSize(n);
            }
            else {
                final AEADBlockCipher aeadBlockCipher2 = this.aeadBlockCipher;
                n2 = n;
                if (aeadBlockCipher2 != null) {
                    n2 = aeadBlockCipher2.getUpdateOutputSize(n);
                }
            }
        }
        final byte[] buf = this.buf;
        if (buf == null || buf.length < n2) {
            this.buf = new byte[n2];
        }
    }
    
    private void finaliseCipher() throws IOException {
        try {
            this.ensureCapacity(0, this.finalized = true);
            if (this.bufferedBlockCipher != null) {
                this.maxBuf = this.bufferedBlockCipher.doFinal(this.buf, 0);
                return;
            }
            if (this.aeadBlockCipher != null) {
                this.maxBuf = this.aeadBlockCipher.doFinal(this.buf, 0);
                return;
            }
            this.maxBuf = 0;
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Error finalising cipher ");
            sb.append(ex);
            throw new IOException(sb.toString());
        }
        catch (InvalidCipherTextException ex2) {
            throw new InvalidCipherTextIOException("Error finalising cipher", ex2);
        }
    }
    
    private int nextChunk() throws IOException {
        if (this.finalized) {
            return -1;
        }
        this.bufOff = 0;
        this.maxBuf = 0;
        int maxBuf;
        while (true) {
            maxBuf = this.maxBuf;
            if (maxBuf != 0) {
                break;
            }
            int maxBuf2 = this.in.read(this.inBuf);
            if (maxBuf2 != -1) {
                try {
                    this.ensureCapacity(maxBuf2, false);
                    if (this.bufferedBlockCipher != null) {
                        maxBuf2 = this.bufferedBlockCipher.processBytes(this.inBuf, 0, maxBuf2, this.buf, 0);
                    }
                    else if (this.aeadBlockCipher != null) {
                        maxBuf2 = this.aeadBlockCipher.processBytes(this.inBuf, 0, maxBuf2, this.buf, 0);
                    }
                    else {
                        this.streamCipher.processBytes(this.inBuf, 0, maxBuf2, this.buf, 0);
                    }
                    this.maxBuf = maxBuf2;
                    continue;
                }
                catch (Exception ex) {
                    throw new CipherIOException("Error processing stream ", ex);
                }
                break;
            }
            this.finaliseCipher();
            final int maxBuf3 = this.maxBuf;
            if (maxBuf3 == 0) {
                return -1;
            }
            return maxBuf3;
        }
        return maxBuf;
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
            this.markBufOff = 0;
            this.markPosition = 0L;
            final byte[] markBuf = this.markBuf;
            if (markBuf != null) {
                Arrays.fill(markBuf, (byte)0);
                this.markBuf = null;
            }
            final byte[] buf = this.buf;
            if (buf != null) {
                Arrays.fill(buf, (byte)0);
                this.buf = null;
            }
            Arrays.fill(this.inBuf, (byte)0);
        }
        finally {
            if (!this.finalized) {
                this.finaliseCipher();
            }
        }
    }
    
    @Override
    public void mark(final int n) {
        this.in.mark(n);
        final SkippingCipher skippingCipher = this.skippingCipher;
        if (skippingCipher != null) {
            this.markPosition = skippingCipher.getPosition();
        }
        final byte[] buf = this.buf;
        if (buf != null) {
            System.arraycopy(buf, 0, this.markBuf = new byte[buf.length], 0, buf.length);
        }
        this.markBufOff = this.bufOff;
    }
    
    @Override
    public boolean markSupported() {
        return this.skippingCipher != null && this.in.markSupported();
    }
    
    @Override
    public int read() throws IOException {
        if (this.bufOff >= this.maxBuf && this.nextChunk() < 0) {
            return -1;
        }
        return this.buf[this.bufOff++] & 0xFF;
    }
    
    @Override
    public int read(final byte[] array) throws IOException {
        return this.read(array, 0, array.length);
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
        if (this.skippingCipher != null) {
            this.in.reset();
            this.skippingCipher.seekTo(this.markPosition);
            final byte[] markBuf = this.markBuf;
            if (markBuf != null) {
                this.buf = markBuf;
            }
            this.bufOff = this.markBufOff;
            return;
        }
        throw new IOException("cipher must implement SkippingCipher to be used with reset()");
    }
    
    @Override
    public long skip(long skip) throws IOException {
        if (skip <= 0L) {
            return 0L;
        }
        if (this.skippingCipher == null) {
            final int n = (int)Math.min(skip, this.available());
            this.bufOff += n;
            return n;
        }
        final long n2 = this.available();
        if (skip <= n2) {
            this.bufOff += (int)skip;
            return skip;
        }
        this.bufOff = this.maxBuf;
        skip = this.in.skip(skip - n2);
        if (skip == this.skippingCipher.skip(skip)) {
            return skip + n2;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Unable to skip cipher ");
        sb.append(skip);
        sb.append(" bytes.");
        throw new IOException(sb.toString());
    }
}
