package org.spongycastle.crypto.agreement;

import java.math.*;
import org.spongycastle.math.ec.*;
import org.spongycastle.crypto.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.params.*;

public class ECMQVBasicAgreement implements BasicAgreement
{
    MQVPrivateParameters privParams;
    
    private ECPoint calculateMqvAgreement(final ECDomainParameters ecDomainParameters, final ECPrivateKeyParameters ecPrivateKeyParameters, final ECPrivateKeyParameters ecPrivateKeyParameters2, final ECPublicKeyParameters ecPublicKeyParameters, final ECPublicKeyParameters ecPublicKeyParameters2, final ECPublicKeyParameters ecPublicKeyParameters3) {
        final BigInteger n = ecDomainParameters.getN();
        final int n2 = (n.bitLength() + 1) / 2;
        final BigInteger shiftLeft = ECConstants.ONE.shiftLeft(n2);
        final ECCurve curve = ecDomainParameters.getCurve();
        final ECPoint[] array = { ECAlgorithms.importPoint(curve, ecPublicKeyParameters.getQ()), ECAlgorithms.importPoint(curve, ecPublicKeyParameters2.getQ()), ECAlgorithms.importPoint(curve, ecPublicKeyParameters3.getQ()) };
        curve.normalizeAll(array);
        final ECPoint ecPoint = array[0];
        final ECPoint ecPoint2 = array[1];
        final ECPoint ecPoint3 = array[2];
        final BigInteger mod = ecPrivateKeyParameters.getD().multiply(ecPoint.getAffineXCoord().toBigInteger().mod(shiftLeft).setBit(n2)).add(ecPrivateKeyParameters2.getD()).mod(n);
        final BigInteger setBit = ecPoint3.getAffineXCoord().toBigInteger().mod(shiftLeft).setBit(n2);
        final BigInteger mod2 = ecDomainParameters.getH().multiply(mod).mod(n);
        return ECAlgorithms.sumOfTwoMultiplies(ecPoint2, setBit.multiply(mod2).mod(n), ecPoint3, mod2);
    }
    
    @Override
    public BigInteger calculateAgreement(final CipherParameters cipherParameters) {
        if (Properties.isOverrideSet("org.spongycastle.ec.disable_mqv")) {
            throw new IllegalStateException("ECMQV explicitly disabled");
        }
        final MQVPublicParameters mqvPublicParameters = (MQVPublicParameters)cipherParameters;
        final ECPrivateKeyParameters staticPrivateKey = this.privParams.getStaticPrivateKey();
        final ECDomainParameters parameters = staticPrivateKey.getParameters();
        if (!parameters.equals(mqvPublicParameters.getStaticPublicKey().getParameters())) {
            throw new IllegalStateException("ECMQV public key components have wrong domain parameters");
        }
        final ECPoint normalize = this.calculateMqvAgreement(parameters, staticPrivateKey, this.privParams.getEphemeralPrivateKey(), this.privParams.getEphemeralPublicKey(), mqvPublicParameters.getStaticPublicKey(), mqvPublicParameters.getEphemeralPublicKey()).normalize();
        if (!normalize.isInfinity()) {
            return normalize.getAffineXCoord().toBigInteger();
        }
        throw new IllegalStateException("Infinity is not a valid agreement value for MQV");
    }
    
    @Override
    public int getFieldSize() {
        return (this.privParams.getStaticPrivateKey().getParameters().getCurve().getFieldSize() + 7) / 8;
    }
    
    @Override
    public void init(final CipherParameters cipherParameters) {
        this.privParams = (MQVPrivateParameters)cipherParameters;
    }
}
