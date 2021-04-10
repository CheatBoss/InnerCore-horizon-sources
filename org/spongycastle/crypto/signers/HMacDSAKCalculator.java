package org.spongycastle.crypto.signers;

import java.math.*;
import org.spongycastle.crypto.macs.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;
import java.security.*;

public class HMacDSAKCalculator implements DSAKCalculator
{
    private static final BigInteger ZERO;
    private final byte[] K;
    private final byte[] V;
    private final HMac hMac;
    private BigInteger n;
    
    static {
        ZERO = BigInteger.valueOf(0L);
    }
    
    public HMacDSAKCalculator(final Digest digest) {
        final HMac hMac = new HMac(digest);
        this.hMac = hMac;
        this.V = new byte[hMac.getMacSize()];
        this.K = new byte[this.hMac.getMacSize()];
    }
    
    private BigInteger bitsToInt(final byte[] array) {
        BigInteger shiftRight = new BigInteger(1, array);
        if (array.length * 8 > this.n.bitLength()) {
            shiftRight = shiftRight.shiftRight(array.length * 8 - this.n.bitLength());
        }
        return shiftRight;
    }
    
    @Override
    public void init(final BigInteger n, BigInteger bigInteger, final byte[] array) {
        this.n = n;
        Arrays.fill(this.V, (byte)1);
        Arrays.fill(this.K, (byte)0);
        final int n2 = (n.bitLength() + 7) / 8;
        final byte[] array2 = new byte[n2];
        final byte[] unsignedByteArray = BigIntegers.asUnsignedByteArray(bigInteger);
        System.arraycopy(unsignedByteArray, 0, array2, n2 - unsignedByteArray.length, unsignedByteArray.length);
        final int n3 = (n.bitLength() + 7) / 8;
        final byte[] array3 = new byte[n3];
        final BigInteger bigInteger2 = bigInteger = this.bitsToInt(array);
        if (bigInteger2.compareTo(n) >= 0) {
            bigInteger = bigInteger2.subtract(n);
        }
        final byte[] unsignedByteArray2 = BigIntegers.asUnsignedByteArray(bigInteger);
        System.arraycopy(unsignedByteArray2, 0, array3, n3 - unsignedByteArray2.length, unsignedByteArray2.length);
        this.hMac.init(new KeyParameter(this.K));
        final HMac hMac = this.hMac;
        final byte[] v = this.V;
        hMac.update(v, 0, v.length);
        this.hMac.update((byte)0);
        this.hMac.update(array2, 0, n2);
        this.hMac.update(array3, 0, n3);
        this.hMac.doFinal(this.K, 0);
        this.hMac.init(new KeyParameter(this.K));
        final HMac hMac2 = this.hMac;
        final byte[] v2 = this.V;
        hMac2.update(v2, 0, v2.length);
        this.hMac.doFinal(this.V, 0);
        final HMac hMac3 = this.hMac;
        final byte[] v3 = this.V;
        hMac3.update(v3, 0, v3.length);
        this.hMac.update((byte)1);
        this.hMac.update(array2, 0, n2);
        this.hMac.update(array3, 0, n3);
        this.hMac.doFinal(this.K, 0);
        this.hMac.init(new KeyParameter(this.K));
        final HMac hMac4 = this.hMac;
        final byte[] v4 = this.V;
        hMac4.update(v4, 0, v4.length);
        this.hMac.doFinal(this.V, 0);
    }
    
    @Override
    public void init(final BigInteger bigInteger, final SecureRandom secureRandom) {
        throw new IllegalStateException("Operation not supported");
    }
    
    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @Override
    public BigInteger nextK() {
        final int n = (this.n.bitLength() + 7) / 8;
        final byte[] array = new byte[n];
        BigInteger bitsToInt;
        while (true) {
            int min;
            for (int i = 0; i < n; i += min) {
                final HMac hMac = this.hMac;
                final byte[] v = this.V;
                hMac.update(v, 0, v.length);
                this.hMac.doFinal(this.V, 0);
                min = Math.min(n - i, this.V.length);
                System.arraycopy(this.V, 0, array, i, min);
            }
            bitsToInt = this.bitsToInt(array);
            if (bitsToInt.compareTo(HMacDSAKCalculator.ZERO) > 0 && bitsToInt.compareTo(this.n) < 0) {
                break;
            }
            final HMac hMac2 = this.hMac;
            final byte[] v2 = this.V;
            hMac2.update(v2, 0, v2.length);
            this.hMac.update((byte)0);
            this.hMac.doFinal(this.K, 0);
            this.hMac.init(new KeyParameter(this.K));
            final HMac hMac3 = this.hMac;
            final byte[] v3 = this.V;
            hMac3.update(v3, 0, v3.length);
            this.hMac.doFinal(this.V, 0);
        }
        return bitsToInt;
    }
}
