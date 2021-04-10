package org.spongycastle.crypto.io;

import org.spongycastle.crypto.*;
import java.io.*;

public class DigestInputStream extends FilterInputStream
{
    protected Digest digest;
    
    public DigestInputStream(final InputStream inputStream, final Digest digest) {
        super(inputStream);
        this.digest = digest;
    }
    
    public Digest getDigest() {
        return this.digest;
    }
    
    @Override
    public int read() throws IOException {
        final int read = this.in.read();
        if (read >= 0) {
            this.digest.update((byte)read);
        }
        return read;
    }
    
    @Override
    public int read(final byte[] array, final int n, int read) throws IOException {
        read = this.in.read(array, n, read);
        if (read > 0) {
            this.digest.update(array, n, read);
        }
        return read;
    }
}
