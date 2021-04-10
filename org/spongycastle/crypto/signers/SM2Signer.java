package org.spongycastle.crypto.signers;

import java.security.*;
import org.spongycastle.util.*;
import java.math.*;
import org.spongycastle.math.ec.*;
import org.spongycastle.crypto.digests.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class SM2Signer implements DSA, ECConstants
{
    private int curveLength;
    private ECKeyParameters ecKey;
    private ECDomainParameters ecParams;
    private final DSAKCalculator kCalculator;
    private ECPoint pubPoint;
    private SecureRandom random;
    private byte[] userID;
    
    public SM2Signer() {
        this.kCalculator = new RandomDSAKCalculator();
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
    
    private byte[] getZ(final Digest digest) {
        this.addUserID(digest, this.userID);
        this.addFieldElement(digest, this.ecParams.getCurve().getA());
        this.addFieldElement(digest, this.ecParams.getCurve().getB());
        this.addFieldElement(digest, this.ecParams.getG().getAffineXCoord());
        this.addFieldElement(digest, this.ecParams.getG().getAffineYCoord());
        this.addFieldElement(digest, this.pubPoint.getAffineXCoord());
        this.addFieldElement(digest, this.pubPoint.getAffineYCoord());
        final byte[] array = new byte[digest.getDigestSize()];
        digest.doFinal(array, 0);
        return array;
    }
    
    protected BigInteger calculateE(final byte[] array) {
        return new BigInteger(1, array);
    }
    
    protected ECMultiplier createBasePointMultiplier() {
        return new FixedPointCombMultiplier();
    }
    
    @Override
    public BigInteger[] generateSignature(final byte[] array) {
        final SM3Digest sm3Digest = new SM3Digest();
        final byte[] z = this.getZ(sm3Digest);
        sm3Digest.update(z, 0, z.length);
        sm3Digest.update(array, 0, array.length);
        final byte[] array2 = new byte[sm3Digest.getDigestSize()];
        sm3Digest.doFinal(array2, 0);
        final BigInteger n = this.ecParams.getN();
        final BigInteger calculateE = this.calculateE(array2);
        final BigInteger d = ((ECPrivateKeyParameters)this.ecKey).getD();
        final ECMultiplier basePointMultiplier = this.createBasePointMultiplier();
        BigInteger mod;
        BigInteger mod2;
        while (true) {
            final BigInteger nextK = this.kCalculator.nextK();
            mod = calculateE.add(basePointMultiplier.multiply(this.ecParams.getG(), nextK).normalize().getAffineXCoord().toBigInteger()).mod(n);
            if (!mod.equals(SM2Signer.ZERO) && !mod.add(nextK).equals(n)) {
                mod2 = d.add(SM2Signer.ONE).modInverse(n).multiply(nextK.subtract(mod.multiply(d)).mod(n)).mod(n);
                if (!mod2.equals(SM2Signer.ZERO)) {
                    break;
                }
                continue;
            }
        }
        return new BigInteger[] { mod, mod2 };
    }
    
    @Override
    public void init(final boolean b, CipherParameters parameters) {
        if (parameters instanceof ParametersWithID) {
            final ParametersWithID parametersWithID = (ParametersWithID)parameters;
            parameters = parametersWithID.getParameters();
            this.userID = parametersWithID.getID();
        }
        else {
            this.userID = new byte[0];
        }
        ECPoint pubPoint;
        if (b) {
            if (parameters instanceof ParametersWithRandom) {
                final ParametersWithRandom parametersWithRandom = (ParametersWithRandom)parameters;
                final ECKeyParameters ecKey = (ECKeyParameters)parametersWithRandom.getParameters();
                this.ecKey = ecKey;
                final ECDomainParameters parameters2 = ecKey.getParameters();
                this.ecParams = parameters2;
                this.kCalculator.init(parameters2.getN(), parametersWithRandom.getRandom());
            }
            else {
                final ECKeyParameters ecKey2 = (ECKeyParameters)parameters;
                this.ecKey = ecKey2;
                final ECDomainParameters parameters3 = ecKey2.getParameters();
                this.ecParams = parameters3;
                this.kCalculator.init(parameters3.getN(), new SecureRandom());
            }
            pubPoint = this.ecParams.getG().multiply(((ECPrivateKeyParameters)this.ecKey).getD()).normalize();
        }
        else {
            final ECKeyParameters ecKey3 = (ECKeyParameters)parameters;
            this.ecKey = ecKey3;
            this.ecParams = ecKey3.getParameters();
            pubPoint = ((ECPublicKeyParameters)this.ecKey).getQ();
        }
        this.pubPoint = pubPoint;
        this.curveLength = (this.ecParams.getCurve().getFieldSize() + 7) / 8;
    }
    
    @Override
    public boolean verifySignature(byte[] array, final BigInteger bigInteger, final BigInteger bigInteger2) {
        final BigInteger n = this.ecParams.getN();
        if (bigInteger.compareTo(SM2Signer.ONE) >= 0) {
            if (bigInteger.compareTo(n) >= 0) {
                return false;
            }
            if (bigInteger2.compareTo(SM2Signer.ONE) >= 0) {
                if (bigInteger2.compareTo(n) >= 0) {
                    return false;
                }
                final ECPoint q = ((ECPublicKeyParameters)this.ecKey).getQ();
                final SM3Digest sm3Digest = new SM3Digest();
                final byte[] z = this.getZ(sm3Digest);
                sm3Digest.update(z, 0, z.length);
                sm3Digest.update(array, 0, array.length);
                array = new byte[sm3Digest.getDigestSize()];
                sm3Digest.doFinal(array, 0);
                final BigInteger calculateE = this.calculateE(array);
                final BigInteger mod = bigInteger.add(bigInteger2).mod(n);
                return !mod.equals(SM2Signer.ZERO) && bigInteger.equals(calculateE.add(this.ecParams.getG().multiply(bigInteger2).add(q.multiply(mod)).normalize().getAffineXCoord().toBigInteger()).mod(n));
            }
        }
        return false;
    }
}
