package org.spongycastle.crypto.digests;

import org.spongycastle.crypto.*;

public class SHAKEDigest extends KeccakDigest implements Xof
{
    public SHAKEDigest() {
        this(128);
    }
    
    public SHAKEDigest(final int n) {
        super(checkBitLength(n));
    }
    
    public SHAKEDigest(final SHAKEDigest shakeDigest) {
        super(shakeDigest);
    }
    
    private static int checkBitLength(final int n) {
        if (n == 128) {
            return n;
        }
        if (n == 256) {
            return n;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("'bitLength' ");
        sb.append(n);
        sb.append(" not supported for SHAKE");
        throw new IllegalArgumentException(sb.toString());
    }
    
    @Override
    public int doFinal(final byte[] array, final int n) {
        return this.doFinal(array, n, this.getDigestSize());
    }
    
    @Override
    protected int doFinal(final byte[] array, final int n, final byte b, final int n2) {
        return this.doFinal(array, n, this.getDigestSize(), b, n2);
    }
    
    @Override
    public int doFinal(final byte[] array, int doOutput, final int n) {
        doOutput = this.doOutput(array, doOutput, n);
        this.reset();
        return doOutput;
    }
    
    protected int doFinal(final byte[] array, final int n, final int n2, final byte b, int n3) {
        if (n3 >= 0 && n3 <= 7) {
            final int n4 = (b & (1 << n3) - 1) | 15 << n3;
            final int n5 = n3 + 4;
            n3 = n4;
            int n6;
            if ((n6 = n5) >= 8) {
                this.absorb(new byte[] { (byte)n4 }, 0, 1);
                n6 = n5 - 8;
                n3 = n4 >>> 8;
            }
            if (n6 > 0) {
                this.absorbBits(n3, n6);
            }
            this.squeeze(array, n, n2 * 8L);
            this.reset();
            return n2;
        }
        throw new IllegalArgumentException("'partialBits' must be in the range [0,7]");
    }
    
    @Override
    public int doOutput(final byte[] array, final int n, final int n2) {
        if (!this.squeezing) {
            this.absorbBits(15, 4);
        }
        this.squeeze(array, n, n2 * 8L);
        return n2;
    }
    
    @Override
    public String getAlgorithmName() {
        final StringBuilder sb = new StringBuilder();
        sb.append("SHAKE");
        sb.append(this.fixedOutputLength);
        return sb.toString();
    }
}
