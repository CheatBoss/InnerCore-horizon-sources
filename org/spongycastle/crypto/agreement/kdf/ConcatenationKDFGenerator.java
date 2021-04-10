package org.spongycastle.crypto.agreement.kdf;

import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class ConcatenationKDFGenerator implements DerivationFunction
{
    private Digest digest;
    private int hLen;
    private byte[] otherInfo;
    private byte[] shared;
    
    public ConcatenationKDFGenerator(final Digest digest) {
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
            final int hLen = this.hLen;
            int n3 = 1;
            int n4 = 1;
            int n5;
            if (n2 > hLen) {
                n5 = 0;
                while (true) {
                    this.ItoOSP(n4, array3);
                    this.digest.update(array3, 0, 4);
                    final Digest digest = this.digest;
                    final byte[] shared = this.shared;
                    digest.update(shared, 0, shared.length);
                    final Digest digest2 = this.digest;
                    final byte[] otherInfo = this.otherInfo;
                    digest2.update(otherInfo, 0, otherInfo.length);
                    this.digest.doFinal(array2, 0);
                    System.arraycopy(array2, 0, array, n + n5, this.hLen);
                    final int hLen2 = this.hLen;
                    n5 += hLen2;
                    n3 = n4 + 1;
                    if (n4 >= n2 / hLen2) {
                        break;
                    }
                    n4 = n3;
                }
            }
            else {
                n5 = 0;
            }
            if (n5 < n2) {
                this.ItoOSP(n3, array3);
                this.digest.update(array3, 0, 4);
                final Digest digest3 = this.digest;
                final byte[] shared2 = this.shared;
                digest3.update(shared2, 0, shared2.length);
                final Digest digest4 = this.digest;
                final byte[] otherInfo2 = this.otherInfo;
                digest4.update(otherInfo2, 0, otherInfo2.length);
                this.digest.doFinal(array2, 0);
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
        if (derivationParameters instanceof KDFParameters) {
            final KDFParameters kdfParameters = (KDFParameters)derivationParameters;
            this.shared = kdfParameters.getSharedSecret();
            this.otherInfo = kdfParameters.getIV();
            return;
        }
        throw new IllegalArgumentException("KDF parameters required for generator");
    }
}
