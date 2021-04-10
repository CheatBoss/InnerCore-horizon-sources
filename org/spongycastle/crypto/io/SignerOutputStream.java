package org.spongycastle.crypto.io;

import org.spongycastle.crypto.*;
import java.io.*;

public class SignerOutputStream extends OutputStream
{
    protected Signer signer;
    
    public SignerOutputStream(final Signer signer) {
        this.signer = signer;
    }
    
    public Signer getSigner() {
        return this.signer;
    }
    
    @Override
    public void write(final int n) throws IOException {
        this.signer.update((byte)n);
    }
    
    @Override
    public void write(final byte[] array, final int n, final int n2) throws IOException {
        this.signer.update(array, n, n2);
    }
}
