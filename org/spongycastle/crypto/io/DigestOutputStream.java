package org.spongycastle.crypto.io;

import org.spongycastle.crypto.*;
import java.io.*;

public class DigestOutputStream extends OutputStream
{
    protected Digest digest;
    
    public DigestOutputStream(final Digest digest) {
        this.digest = digest;
    }
    
    public byte[] getDigest() {
        final byte[] array = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(array, 0);
        return array;
    }
    
    @Override
    public void write(final int n) throws IOException {
        this.digest.update((byte)n);
    }
    
    @Override
    public void write(final byte[] array, final int n, final int n2) throws IOException {
        this.digest.update(array, n, n2);
    }
}
