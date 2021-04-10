package org.spongycastle.pqc.crypto.ntru;

import org.spongycastle.pqc.math.ntru.polynomial.*;
import java.nio.*;
import org.spongycastle.crypto.*;

public class NTRUSigner
{
    private Digest hashAlg;
    private NTRUSigningParameters params;
    private NTRUSigningPrivateKeyParameters signingKeyPair;
    private NTRUSigningPublicKeyParameters verificationKey;
    
    public NTRUSigner(final NTRUSigningParameters params) {
        this.params = params;
    }
    
    private IntegerPolynomial sign(IntegerPolynomial integerPolynomial, final NTRUSigningPrivateKeyParameters ntruSigningPrivateKeyParameters) {
        final int n = this.params.N;
        final int q = this.params.q;
        int i = this.params.B;
        final NTRUSigningPublicKeyParameters publicKey = ntruSigningPrivateKeyParameters.getPublicKey();
        final IntegerPolynomial integerPolynomial2 = new IntegerPolynomial(n);
        while (i >= 1) {
            final Polynomial f = ntruSigningPrivateKeyParameters.getBasis(i).f;
            final Polynomial fPrime = ntruSigningPrivateKeyParameters.getBasis(i).fPrime;
            final IntegerPolynomial mult = f.mult(integerPolynomial);
            mult.div(q);
            final IntegerPolynomial mult2 = fPrime.mult(mult);
            integerPolynomial = fPrime.mult(integerPolynomial);
            integerPolynomial.div(q);
            mult2.sub(f.mult(integerPolynomial));
            integerPolynomial2.add(mult2);
            final IntegerPolynomial integerPolynomial3 = (IntegerPolynomial)ntruSigningPrivateKeyParameters.getBasis(i).h.clone();
            if (i > 1) {
                integerPolynomial = ntruSigningPrivateKeyParameters.getBasis(i - 1).h;
            }
            else {
                integerPolynomial = publicKey.h;
            }
            integerPolynomial3.sub(integerPolynomial);
            integerPolynomial = mult2.mult(integerPolynomial3, q);
            --i;
        }
        final Polynomial f2 = ntruSigningPrivateKeyParameters.getBasis(0).f;
        final Polynomial fPrime2 = ntruSigningPrivateKeyParameters.getBasis(0).fPrime;
        final IntegerPolynomial mult3 = f2.mult(integerPolynomial);
        mult3.div(q);
        final IntegerPolynomial mult4 = fPrime2.mult(mult3);
        integerPolynomial = fPrime2.mult(integerPolynomial);
        integerPolynomial.div(q);
        mult4.sub(f2.mult(integerPolynomial));
        integerPolynomial2.add(mult4);
        integerPolynomial2.modPositive(q);
        return integerPolynomial2;
    }
    
    private byte[] signHash(byte[] binary, final NTRUSigningPrivateKeyParameters ntruSigningPrivateKeyParameters) {
        final NTRUSigningPublicKeyParameters publicKey = ntruSigningPrivateKeyParameters.getPublicKey();
        int n = 0;
        IntegerPolynomial msgRep;
        IntegerPolynomial sign;
        int n2;
        do {
            n2 = n + 1;
            if (n2 > this.params.signFailTolerance) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Signing failed: too many retries (max=");
                sb.append(this.params.signFailTolerance);
                sb.append(")");
                throw new IllegalStateException(sb.toString());
            }
            msgRep = this.createMsgRep(binary, n2);
            sign = this.sign(msgRep, ntruSigningPrivateKeyParameters);
            n = n2;
        } while (!this.verify(msgRep, sign, publicKey.h));
        binary = sign.toBinary(this.params.q);
        final ByteBuffer allocate = ByteBuffer.allocate(binary.length + 4);
        allocate.put(binary);
        allocate.putInt(n2);
        return allocate.array();
    }
    
    private boolean verify(final IntegerPolynomial integerPolynomial, final IntegerPolynomial integerPolynomial2, IntegerPolynomial mult) {
        final int q = this.params.q;
        final double normBoundSq = this.params.normBoundSq;
        final double betaSq = this.params.betaSq;
        mult = mult.mult(integerPolynomial2, q);
        mult.sub(integerPolynomial);
        final double n = (double)integerPolynomial2.centeredNormSq(q);
        final double n2 = (double)mult.centeredNormSq(q);
        Double.isNaN(n2);
        Double.isNaN(n);
        return (long)(n + betaSq * n2) <= normBoundSq;
    }
    
    private boolean verifyHash(final byte[] array, byte[] array2, final NTRUSigningPublicKeyParameters ntruSigningPublicKeyParameters) {
        final ByteBuffer wrap = ByteBuffer.wrap(array2);
        array2 = new byte[array2.length - 4];
        wrap.get(array2);
        return this.verify(this.createMsgRep(array, wrap.getInt()), IntegerPolynomial.fromBinary(array2, this.params.N, this.params.q), ntruSigningPublicKeyParameters.h);
    }
    
    protected IntegerPolynomial createMsgRep(final byte[] array, int i) {
        final int n = this.params.N;
        final int n2 = 31 - Integer.numberOfLeadingZeros(this.params.q);
        final int n3 = (n2 + 7) / 8;
        final IntegerPolynomial integerPolynomial = new IntegerPolynomial(n);
        final ByteBuffer allocate = ByteBuffer.allocate(array.length + 4);
        allocate.put(array);
        allocate.putInt(i);
        final NTRUSignerPrng ntruSignerPrng = new NTRUSignerPrng(allocate.array(), this.params.hashAlg);
        byte[] nextBytes;
        byte b;
        int n4;
        ByteBuffer allocate2;
        for (i = 0; i < n; ++i) {
            nextBytes = ntruSignerPrng.nextBytes(n3);
            b = nextBytes[nextBytes.length - 1];
            n4 = n3 * 8 - n2;
            nextBytes[nextBytes.length - 1] = (byte)(b >> n4 << n4);
            allocate2 = ByteBuffer.allocate(4);
            allocate2.put(nextBytes);
            allocate2.rewind();
            integerPolynomial.coeffs[i] = Integer.reverseBytes(allocate2.getInt());
        }
        return integerPolynomial;
    }
    
    public byte[] generateSignature() {
        final Digest hashAlg = this.hashAlg;
        if (hashAlg != null && this.signingKeyPair != null) {
            final byte[] array = new byte[hashAlg.getDigestSize()];
            this.hashAlg.doFinal(array, 0);
            return this.signHash(array, this.signingKeyPair);
        }
        throw new IllegalStateException("Call initSign first!");
    }
    
    public void init(final boolean b, final CipherParameters cipherParameters) {
        if (b) {
            this.signingKeyPair = (NTRUSigningPrivateKeyParameters)cipherParameters;
        }
        else {
            this.verificationKey = (NTRUSigningPublicKeyParameters)cipherParameters;
        }
        (this.hashAlg = this.params.hashAlg).reset();
    }
    
    public void update(final byte b) {
        final Digest hashAlg = this.hashAlg;
        if (hashAlg != null) {
            hashAlg.update(b);
            return;
        }
        throw new IllegalStateException("Call initSign or initVerify first!");
    }
    
    public void update(final byte[] array, final int n, final int n2) {
        final Digest hashAlg = this.hashAlg;
        if (hashAlg != null) {
            hashAlg.update(array, n, n2);
            return;
        }
        throw new IllegalStateException("Call initSign or initVerify first!");
    }
    
    public boolean verifySignature(final byte[] array) {
        final Digest hashAlg = this.hashAlg;
        if (hashAlg != null && this.verificationKey != null) {
            final byte[] array2 = new byte[hashAlg.getDigestSize()];
            this.hashAlg.doFinal(array2, 0);
            return this.verifyHash(array2, array, this.verificationKey);
        }
        throw new IllegalStateException("Call initVerify first!");
    }
}
