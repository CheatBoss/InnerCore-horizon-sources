package org.spongycastle.crypto.generators;

import org.spongycastle.util.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class BaseKDFBytesGenerator implements DigestDerivationFunction
{
    private int counterStart;
    private Digest digest;
    private byte[] iv;
    private byte[] shared;
    
    protected BaseKDFBytesGenerator(final int counterStart, final Digest digest) {
        this.counterStart = counterStart;
        this.digest = digest;
    }
    
    @Override
    public int generateBytes(final byte[] array, int n, int i) throws DataLengthException, IllegalArgumentException {
        if (array.length - i < n) {
            throw new OutputLengthException("output buffer too small");
        }
        final long n2 = i;
        final int digestSize = this.digest.getDigestSize();
        if (n2 <= 8589934591L) {
            final long n3 = digestSize;
            final int n4 = (int)((n2 + n3 - 1L) / n3);
            final byte[] array2 = new byte[this.digest.getDigestSize()];
            final byte[] array3 = new byte[4];
            Pack.intToBigEndian(this.counterStart, array3, 0);
            final int n5 = this.counterStart & 0xFFFFFF00;
            int n6 = n;
            final int n7 = 0;
            n = n5;
            int n8 = i;
            Digest digest;
            byte[] shared;
            byte[] iv;
            byte b;
            int n9;
            for (i = n7; i < n4; ++i, n = n9) {
                digest = this.digest;
                shared = this.shared;
                digest.update(shared, 0, shared.length);
                this.digest.update(array3, 0, 4);
                iv = this.iv;
                if (iv != null) {
                    this.digest.update(iv, 0, iv.length);
                }
                this.digest.doFinal(array2, 0);
                if (n8 > digestSize) {
                    System.arraycopy(array2, 0, array, n6, digestSize);
                    n6 += digestSize;
                    n8 -= digestSize;
                }
                else {
                    System.arraycopy(array2, 0, array, n6, n8);
                }
                b = (byte)(array3[3] + 1);
                array3[3] = b;
                n9 = n;
                if (b == 0) {
                    n9 = n + 256;
                    Pack.intToBigEndian(n9, array3, 0);
                }
            }
            this.digest.reset();
            return (int)n2;
        }
        throw new IllegalArgumentException("Output length too large");
    }
    
    @Override
    public Digest getDigest() {
        return this.digest;
    }
    
    @Override
    public void init(final DerivationParameters derivationParameters) {
        byte[] iv;
        if (derivationParameters instanceof KDFParameters) {
            final KDFParameters kdfParameters = (KDFParameters)derivationParameters;
            this.shared = kdfParameters.getSharedSecret();
            iv = kdfParameters.getIV();
        }
        else {
            if (!(derivationParameters instanceof ISO18033KDFParameters)) {
                throw new IllegalArgumentException("KDF parameters required for generator");
            }
            this.shared = ((ISO18033KDFParameters)derivationParameters).getSeed();
            iv = null;
        }
        this.iv = iv;
    }
}
