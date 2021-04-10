package org.spongycastle.crypto.digests;

public class SHA3Digest extends KeccakDigest
{
    public SHA3Digest() {
        this(256);
    }
    
    public SHA3Digest(final int n) {
        super(checkBitLength(n));
    }
    
    public SHA3Digest(final SHA3Digest sha3Digest) {
        super(sha3Digest);
    }
    
    private static int checkBitLength(final int n) {
        if (n == 224 || n == 256 || n == 384) {
            return n;
        }
        if (n == 512) {
            return n;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("'bitLength' ");
        sb.append(n);
        sb.append(" not supported for SHA-3");
        throw new IllegalArgumentException(sb.toString());
    }
    
    @Override
    public int doFinal(final byte[] array, final int n) {
        this.absorbBits(2, 2);
        return super.doFinal(array, n);
    }
    
    @Override
    protected int doFinal(final byte[] array, final int n, final byte b, int n2) {
        if (n2 >= 0 && n2 <= 7) {
            final int n3 = (b & (1 << n2) - 1) | 2 << n2;
            final int n4 = n2 + 2;
            n2 = n3;
            int n5;
            if ((n5 = n4) >= 8) {
                this.absorb(new byte[] { (byte)n3 }, 0, 1);
                n5 = n4 - 8;
                n2 = n3 >>> 8;
            }
            return super.doFinal(array, n, (byte)n2, n5);
        }
        throw new IllegalArgumentException("'partialBits' must be in the range [0,7]");
    }
    
    @Override
    public String getAlgorithmName() {
        final StringBuilder sb = new StringBuilder();
        sb.append("SHA3-");
        sb.append(this.fixedOutputLength);
        return sb.toString();
    }
}
