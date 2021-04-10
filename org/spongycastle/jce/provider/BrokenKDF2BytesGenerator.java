package org.spongycastle.jce.provider;

import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class BrokenKDF2BytesGenerator implements DerivationFunction
{
    private Digest digest;
    private byte[] iv;
    private byte[] shared;
    
    public BrokenKDF2BytesGenerator(final Digest digest) {
        this.digest = digest;
    }
    
    @Override
    public int generateBytes(final byte[] array, int i, final int n) throws DataLengthException, IllegalArgumentException {
        if (array.length - n >= i) {
            final long n2 = n * 8L;
            if (n2 > this.digest.getDigestSize() * 8L * 2147483648L) {
                new IllegalArgumentException("Output length to large");
            }
            final int n3 = (int)(n2 / this.digest.getDigestSize());
            final int digestSize = this.digest.getDigestSize();
            final byte[] array2 = new byte[digestSize];
            final int n4 = 1;
            int n5 = i;
            Digest digest;
            byte[] shared;
            Digest digest2;
            byte[] iv;
            int n6;
            for (i = n4; i <= n3; ++i) {
                digest = this.digest;
                shared = this.shared;
                digest.update(shared, 0, shared.length);
                this.digest.update((byte)(i & 0xFF));
                this.digest.update((byte)(i >> 8 & 0xFF));
                this.digest.update((byte)(i >> 16 & 0xFF));
                this.digest.update((byte)(i >> 24 & 0xFF));
                digest2 = this.digest;
                iv = this.iv;
                digest2.update(iv, 0, iv.length);
                this.digest.doFinal(array2, 0);
                n6 = n - n5;
                if (n6 > digestSize) {
                    System.arraycopy(array2, 0, array, n5, digestSize);
                    n5 += digestSize;
                }
                else {
                    System.arraycopy(array2, 0, array, n5, n6);
                }
            }
            this.digest.reset();
            return n;
        }
        throw new OutputLengthException("output buffer too small");
    }
    
    public Digest getDigest() {
        return this.digest;
    }
    
    @Override
    public void init(final DerivationParameters derivationParameters) {
        if (derivationParameters instanceof KDFParameters) {
            final KDFParameters kdfParameters = (KDFParameters)derivationParameters;
            this.shared = kdfParameters.getSharedSecret();
            this.iv = kdfParameters.getIV();
            return;
        }
        throw new IllegalArgumentException("KDF parameters required for generator");
    }
}
