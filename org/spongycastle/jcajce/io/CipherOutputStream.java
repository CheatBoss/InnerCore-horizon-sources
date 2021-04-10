package org.spongycastle.jcajce.io;

import javax.crypto.*;
import java.io.*;
import org.spongycastle.crypto.io.*;
import java.security.*;

public class CipherOutputStream extends FilterOutputStream
{
    private final Cipher cipher;
    private final byte[] oneByte;
    
    public CipherOutputStream(final OutputStream outputStream, final Cipher cipher) {
        super(outputStream);
        this.oneByte = new byte[1];
        this.cipher = cipher;
    }
    
    @Override
    public void close() throws IOException {
        IOException ex;
        try {
            final byte[] doFinal = this.cipher.doFinal();
            if (doFinal != null) {
                this.out.write(doFinal);
            }
            ex = null;
        }
        catch (Exception ex2) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Error closing stream: ");
            sb.append(ex2);
            ex = new IOException(sb.toString());
        }
        catch (GeneralSecurityException ex3) {
            ex = new InvalidCipherTextIOException("Error during cipher finalisation", ex3);
        }
        IOException ex4;
        try {
            this.flush();
            this.out.close();
            ex4 = ex;
        }
        catch (IOException ex5) {
            ex4 = ex;
            if (ex == null) {
                ex4 = ex5;
            }
        }
        if (ex4 == null) {
            return;
        }
        throw ex4;
    }
    
    @Override
    public void flush() throws IOException {
        this.out.flush();
    }
    
    @Override
    public void write(final int n) throws IOException {
        final byte[] oneByte = this.oneByte;
        oneByte[0] = (byte)n;
        this.write(oneByte, 0, 1);
    }
    
    @Override
    public void write(byte[] update, final int n, final int n2) throws IOException {
        update = this.cipher.update(update, n, n2);
        if (update != null) {
            this.out.write(update);
        }
    }
}
