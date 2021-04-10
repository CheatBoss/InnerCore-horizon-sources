package org.spongycastle.crypto.io;

import org.spongycastle.crypto.modes.*;
import org.spongycastle.crypto.*;
import java.io.*;

public class CipherOutputStream extends FilterOutputStream
{
    private AEADBlockCipher aeadBlockCipher;
    private byte[] buf;
    private BufferedBlockCipher bufferedBlockCipher;
    private final byte[] oneByte;
    private StreamCipher streamCipher;
    
    public CipherOutputStream(final OutputStream outputStream, final BufferedBlockCipher bufferedBlockCipher) {
        super(outputStream);
        this.oneByte = new byte[1];
        this.bufferedBlockCipher = bufferedBlockCipher;
    }
    
    public CipherOutputStream(final OutputStream outputStream, final StreamCipher streamCipher) {
        super(outputStream);
        this.oneByte = new byte[1];
        this.streamCipher = streamCipher;
    }
    
    public CipherOutputStream(final OutputStream outputStream, final AEADBlockCipher aeadBlockCipher) {
        super(outputStream);
        this.oneByte = new byte[1];
        this.aeadBlockCipher = aeadBlockCipher;
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
    
    @Override
    public void close() throws IOException {
        this.ensureCapacity(0, true);
        IOException ex;
        try {
            Label_0105: {
                int n;
                OutputStream outputStream;
                byte[] array;
                if (this.bufferedBlockCipher != null) {
                    n = this.bufferedBlockCipher.doFinal(this.buf, 0);
                    if (n == 0) {
                        break Label_0105;
                    }
                    outputStream = this.out;
                    array = this.buf;
                }
                else if (this.aeadBlockCipher != null) {
                    n = this.aeadBlockCipher.doFinal(this.buf, 0);
                    if (n == 0) {
                        break Label_0105;
                    }
                    outputStream = this.out;
                    array = this.buf;
                }
                else {
                    if (this.streamCipher != null) {
                        this.streamCipher.reset();
                    }
                    break Label_0105;
                }
                outputStream.write(array, 0, n);
            }
            ex = null;
        }
        catch (Exception ex2) {
            ex = new CipherIOException("Error closing stream: ", ex2);
        }
        catch (InvalidCipherTextException ex3) {
            ex = new InvalidCipherTextIOException("Error finalising cipher data", ex3);
        }
        try {
            this.flush();
            this.out.close();
        }
        catch (IOException ex4) {
            if (ex == null) {
                ex = ex4;
            }
        }
        if (ex == null) {
            return;
        }
        throw ex;
    }
    
    @Override
    public void flush() throws IOException {
        this.out.flush();
    }
    
    @Override
    public void write(final int n) throws IOException {
        final byte[] oneByte = this.oneByte;
        final byte b = (byte)n;
        oneByte[0] = b;
        if (this.streamCipher != null) {
            this.out.write(this.streamCipher.returnByte(b));
            return;
        }
        this.write(oneByte, 0, 1);
    }
    
    @Override
    public void write(final byte[] array) throws IOException {
        this.write(array, 0, array.length);
    }
    
    @Override
    public void write(final byte[] array, int n, final int n2) throws IOException {
        this.ensureCapacity(n2, false);
        final BufferedBlockCipher bufferedBlockCipher = this.bufferedBlockCipher;
        if (bufferedBlockCipher != null) {
            n = bufferedBlockCipher.processBytes(array, n, n2, this.buf, 0);
            if (n == 0) {
                return;
            }
        }
        else {
            final AEADBlockCipher aeadBlockCipher = this.aeadBlockCipher;
            if (aeadBlockCipher == null) {
                this.streamCipher.processBytes(array, n, n2, this.buf, 0);
                this.out.write(this.buf, 0, n2);
                return;
            }
            n = aeadBlockCipher.processBytes(array, n, n2, this.buf, 0);
            if (n == 0) {
                return;
            }
        }
        this.out.write(this.buf, 0, n);
    }
}
