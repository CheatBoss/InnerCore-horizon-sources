package org.spongycastle.crypto.io;

import org.spongycastle.crypto.*;
import java.io.*;

public class SignerInputStream extends FilterInputStream
{
    protected Signer signer;
    
    public SignerInputStream(final InputStream inputStream, final Signer signer) {
        super(inputStream);
        this.signer = signer;
    }
    
    public Signer getSigner() {
        return this.signer;
    }
    
    @Override
    public int read() throws IOException {
        final int read = this.in.read();
        if (read >= 0) {
            this.signer.update((byte)read);
        }
        return read;
    }
    
    @Override
    public int read(final byte[] array, final int n, int read) throws IOException {
        read = this.in.read(array, n, read);
        if (read > 0) {
            this.signer.update(array, n, read);
        }
        return read;
    }
}
