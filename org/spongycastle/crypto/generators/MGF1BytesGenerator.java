package org.spongycastle.crypto.generators;

import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class MGF1BytesGenerator implements DerivationFunction
{
    private Digest digest;
    private int hLen;
    private byte[] seed;
    
    public MGF1BytesGenerator(final Digest digest) {
        this.digest = digest;
        this.hLen = digest.getDigestSize();
    }
    
    private void ItoOSP(final int n, final byte[] array) {
        array[0] = (byte)(n >>> 24);
        array[1] = (byte)(n >>> 16);
        array[2] = (byte)(n >>> 8);
        array[3] = (byte)(n >>> 0);
    }
    
    @Override
    public int generateBytes(final byte[] array, final int n, final int n2) throws DataLengthException, IllegalArgumentException {
        if (array.length - n2 >= n) {
            final byte[] array2 = new byte[this.hLen];
            final byte[] array3 = new byte[4];
            this.digest.reset();
            int n4;
            if (n2 > this.hLen) {
                int n3 = 0;
                do {
                    this.ItoOSP(n3, array3);
                    final Digest digest = this.digest;
                    final byte[] seed = this.seed;
                    digest.update(seed, 0, seed.length);
                    this.digest.update(array3, 0, 4);
                    this.digest.doFinal(array2, 0);
                    final int hLen = this.hLen;
                    System.arraycopy(array2, 0, array, n3 * hLen + n, hLen);
                    n4 = n3 + 1;
                } while ((n3 = n4) < n2 / this.hLen);
            }
            else {
                n4 = 0;
            }
            if (this.hLen * n4 < n2) {
                this.ItoOSP(n4, array3);
                final Digest digest2 = this.digest;
                final byte[] seed2 = this.seed;
                digest2.update(seed2, 0, seed2.length);
                this.digest.update(array3, 0, 4);
                this.digest.doFinal(array2, 0);
                final int n5 = n4 * this.hLen;
                System.arraycopy(array2, 0, array, n + n5, n2 - n5);
            }
            return n2;
        }
        throw new OutputLengthException("output buffer too small");
    }
    
    public Digest getDigest() {
        return this.digest;
    }
    
    @Override
    public void init(final DerivationParameters derivationParameters) {
        if (derivationParameters instanceof MGFParameters) {
            this.seed = ((MGFParameters)derivationParameters).getSeed();
            return;
        }
        throw new IllegalArgumentException("MGF parameters required for MGF1Generator");
    }
}
