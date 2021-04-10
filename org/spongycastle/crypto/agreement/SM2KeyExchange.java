package org.spongycastle.crypto.agreement;

import org.spongycastle.crypto.digests.*;
import org.spongycastle.math.ec.*;
import java.math.*;
import org.spongycastle.crypto.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.params.*;

public class SM2KeyExchange
{
    private int curveLength;
    private final Digest digest;
    private ECDomainParameters ecParams;
    private ECPrivateKeyParameters ephemeralKey;
    private ECPoint ephemeralPubPoint;
    private boolean initiator;
    private ECPrivateKeyParameters staticKey;
    private ECPoint staticPubPoint;
    private byte[] userID;
    private int w;
    
    public SM2KeyExchange() {
        this(new SM3Digest());
    }
    
    public SM2KeyExchange(final Digest digest) {
        this.digest = digest;
    }
    
    private byte[] S1(final Digest digest, final ECPoint ecPoint, final byte[] array) {
        final byte[] array2 = new byte[digest.getDigestSize()];
        digest.update((byte)2);
        this.addFieldElement(digest, ecPoint.getAffineYCoord());
        digest.update(array, 0, array.length);
        digest.doFinal(array2, 0);
        return array2;
    }
    
    private byte[] S2(final Digest digest, final ECPoint ecPoint, final byte[] array) {
        final byte[] array2 = new byte[digest.getDigestSize()];
        digest.update((byte)3);
        this.addFieldElement(digest, ecPoint.getAffineYCoord());
        digest.update(array, 0, array.length);
        digest.doFinal(array2, 0);
        return array2;
    }
    
    private void addFieldElement(final Digest digest, final ECFieldElement ecFieldElement) {
        final byte[] unsignedByteArray = BigIntegers.asUnsignedByteArray(this.curveLength, ecFieldElement.toBigInteger());
        digest.update(unsignedByteArray, 0, unsignedByteArray.length);
    }
    
    private void addUserID(final Digest digest, final byte[] array) {
        final int n = array.length * 8;
        digest.update((byte)(n >> 8 & 0xFF));
        digest.update((byte)(n & 0xFF));
        digest.update(array, 0, array.length);
    }
    
    private byte[] calculateInnerHash(final Digest digest, final ECPoint ecPoint, final byte[] array, final byte[] array2, final ECPoint ecPoint2, final ECPoint ecPoint3) {
        this.addFieldElement(digest, ecPoint.getAffineXCoord());
        digest.update(array, 0, array.length);
        digest.update(array2, 0, array2.length);
        this.addFieldElement(digest, ecPoint2.getAffineXCoord());
        this.addFieldElement(digest, ecPoint2.getAffineYCoord());
        this.addFieldElement(digest, ecPoint3.getAffineXCoord());
        this.addFieldElement(digest, ecPoint3.getAffineYCoord());
        final byte[] array3 = new byte[digest.getDigestSize()];
        digest.doFinal(array3, 0);
        return array3;
    }
    
    private ECPoint calculateU(final SM2KeyExchangePublicParameters sm2KeyExchangePublicParameters) {
        return sm2KeyExchangePublicParameters.getStaticPublicKey().getQ().add(sm2KeyExchangePublicParameters.getEphemeralPublicKey().getQ().multiply(this.reduce(sm2KeyExchangePublicParameters.getEphemeralPublicKey().getQ().getAffineXCoord().toBigInteger())).normalize()).normalize().multiply(this.ecParams.getH().multiply(this.staticKey.getD().add(this.reduce(this.ephemeralPubPoint.getAffineXCoord().toBigInteger()).multiply(this.ephemeralKey.getD())).mod(this.ecParams.getN()))).normalize();
    }
    
    private byte[] getZ(final Digest digest, byte[] array, final ECPoint ecPoint) {
        this.addUserID(digest, array);
        this.addFieldElement(digest, this.ecParams.getCurve().getA());
        this.addFieldElement(digest, this.ecParams.getCurve().getB());
        this.addFieldElement(digest, this.ecParams.getG().getAffineXCoord());
        this.addFieldElement(digest, this.ecParams.getG().getAffineYCoord());
        this.addFieldElement(digest, ecPoint.getAffineXCoord());
        this.addFieldElement(digest, ecPoint.getAffineYCoord());
        array = new byte[digest.getDigestSize()];
        digest.doFinal(array, 0);
        return array;
    }
    
    private byte[] kdf(final ECPoint ecPoint, final byte[] array, final byte[] array2, final int n) {
        final int n2 = this.digest.getDigestSize() * 8;
        final int digestSize = this.digest.getDigestSize();
        final byte[] array3 = new byte[digestSize];
        final int n3 = (n + 7) / 8;
        final byte[] array4 = new byte[n3];
        int i = 1;
        int n4 = 1;
        int n5 = 0;
        while (i <= (n + n2 - 1) / n2) {
            this.addFieldElement(this.digest, ecPoint.getAffineXCoord());
            this.addFieldElement(this.digest, ecPoint.getAffineYCoord());
            this.digest.update(array, 0, array.length);
            this.digest.update(array2, 0, array2.length);
            this.digest.update((byte)(n4 >> 24));
            this.digest.update((byte)(n4 >> 16));
            this.digest.update((byte)(n4 >> 8));
            this.digest.update((byte)n4);
            this.digest.doFinal(array3, 0);
            final int n6 = n5 + digestSize;
            if (n6 < n3) {
                System.arraycopy(array3, 0, array4, n5, digestSize);
            }
            else {
                System.arraycopy(array3, 0, array4, n5, n3 - n5);
            }
            ++n4;
            ++i;
            n5 = n6;
        }
        return array4;
    }
    
    private BigInteger reduce(final BigInteger bigInteger) {
        return bigInteger.and(BigInteger.valueOf(1L).shiftLeft(this.w).subtract(BigInteger.valueOf(1L))).setBit(this.w);
    }
    
    public byte[] calculateKey(final int n, final CipherParameters cipherParameters) {
        SM2KeyExchangePublicParameters sm2KeyExchangePublicParameters;
        byte[] id;
        if (cipherParameters instanceof ParametersWithID) {
            final ParametersWithID parametersWithID = (ParametersWithID)cipherParameters;
            sm2KeyExchangePublicParameters = (SM2KeyExchangePublicParameters)parametersWithID.getParameters();
            id = parametersWithID.getID();
        }
        else {
            sm2KeyExchangePublicParameters = (SM2KeyExchangePublicParameters)cipherParameters;
            id = new byte[0];
        }
        final byte[] z = this.getZ(this.digest, this.userID, this.staticPubPoint);
        final byte[] z2 = this.getZ(this.digest, id, sm2KeyExchangePublicParameters.getStaticPublicKey().getQ());
        final ECPoint calculateU = this.calculateU(sm2KeyExchangePublicParameters);
        if (this.initiator) {
            return this.kdf(calculateU, z, z2, n);
        }
        return this.kdf(calculateU, z2, z, n);
    }
    
    public byte[][] calculateKeyWithConfirmation(final int n, byte[] kdf, final CipherParameters cipherParameters) {
        SM2KeyExchangePublicParameters sm2KeyExchangePublicParameters;
        byte[] id;
        if (cipherParameters instanceof ParametersWithID) {
            final ParametersWithID parametersWithID = (ParametersWithID)cipherParameters;
            sm2KeyExchangePublicParameters = (SM2KeyExchangePublicParameters)parametersWithID.getParameters();
            id = parametersWithID.getID();
        }
        else {
            sm2KeyExchangePublicParameters = (SM2KeyExchangePublicParameters)cipherParameters;
            id = new byte[0];
        }
        if (this.initiator && kdf == null) {
            throw new IllegalArgumentException("if initiating, confirmationTag must be set");
        }
        final byte[] z = this.getZ(this.digest, this.userID, this.staticPubPoint);
        final byte[] z2 = this.getZ(this.digest, id, sm2KeyExchangePublicParameters.getStaticPublicKey().getQ());
        final ECPoint calculateU = this.calculateU(sm2KeyExchangePublicParameters);
        if (!this.initiator) {
            kdf = this.kdf(calculateU, z2, z, n);
            final byte[] calculateInnerHash = this.calculateInnerHash(this.digest, calculateU, z2, z, sm2KeyExchangePublicParameters.getEphemeralPublicKey().getQ(), this.ephemeralPubPoint);
            return new byte[][] { kdf, this.S1(this.digest, calculateU, calculateInnerHash), this.S2(this.digest, calculateU, calculateInnerHash) };
        }
        final byte[] kdf2 = this.kdf(calculateU, z, z2, n);
        final byte[] calculateInnerHash2 = this.calculateInnerHash(this.digest, calculateU, z, z2, this.ephemeralPubPoint, sm2KeyExchangePublicParameters.getEphemeralPublicKey().getQ());
        if (Arrays.constantTimeAreEqual(this.S1(this.digest, calculateU, calculateInnerHash2), kdf)) {
            return new byte[][] { kdf2, this.S2(this.digest, calculateU, calculateInnerHash2) };
        }
        throw new IllegalStateException("confirmation tag mismatch");
    }
    
    public int getFieldSize() {
        return (this.staticKey.getParameters().getCurve().getFieldSize() + 7) / 8;
    }
    
    public void init(final CipherParameters cipherParameters) {
        SM2KeyExchangePrivateParameters sm2KeyExchangePrivateParameters;
        byte[] id;
        if (cipherParameters instanceof ParametersWithID) {
            final ParametersWithID parametersWithID = (ParametersWithID)cipherParameters;
            sm2KeyExchangePrivateParameters = (SM2KeyExchangePrivateParameters)parametersWithID.getParameters();
            id = parametersWithID.getID();
        }
        else {
            sm2KeyExchangePrivateParameters = (SM2KeyExchangePrivateParameters)cipherParameters;
            id = new byte[0];
        }
        this.userID = id;
        this.initiator = sm2KeyExchangePrivateParameters.isInitiator();
        this.staticKey = sm2KeyExchangePrivateParameters.getStaticPrivateKey();
        this.ephemeralKey = sm2KeyExchangePrivateParameters.getEphemeralPrivateKey();
        this.ecParams = this.staticKey.getParameters();
        this.staticPubPoint = sm2KeyExchangePrivateParameters.getStaticPublicPoint();
        this.ephemeralPubPoint = sm2KeyExchangePrivateParameters.getEphemeralPublicPoint();
        this.curveLength = (this.ecParams.getCurve().getFieldSize() + 7) / 8;
        this.w = this.ecParams.getCurve().getFieldSize() / 2 - 1;
    }
}
