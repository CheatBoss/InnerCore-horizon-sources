package org.spongycastle.crypto.digests;

import org.spongycastle.crypto.*;
import java.io.*;

public class NullDigest implements Digest
{
    private ByteArrayOutputStream bOut;
    
    public NullDigest() {
        this.bOut = new ByteArrayOutputStream();
    }
    
    @Override
    public int doFinal(final byte[] array, final int n) {
        final byte[] byteArray = this.bOut.toByteArray();
        System.arraycopy(byteArray, 0, array, n, byteArray.length);
        this.reset();
        return byteArray.length;
    }
    
    @Override
    public String getAlgorithmName() {
        return "NULL";
    }
    
    @Override
    public int getDigestSize() {
        return this.bOut.size();
    }
    
    @Override
    public void reset() {
        this.bOut.reset();
    }
    
    @Override
    public void update(final byte b) {
        this.bOut.write(b);
    }
    
    @Override
    public void update(final byte[] array, final int n, final int n2) {
        this.bOut.write(array, n, n2);
    }
}
