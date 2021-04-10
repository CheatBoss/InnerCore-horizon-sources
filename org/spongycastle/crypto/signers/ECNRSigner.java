package org.spongycastle.crypto.signers;

import java.security.*;
import java.math.*;
import org.spongycastle.crypto.generators.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.math.ec.*;

public class ECNRSigner implements DSA
{
    private boolean forSigning;
    private ECKeyParameters key;
    private SecureRandom random;
    
    @Override
    public BigInteger[] generateSignature(final byte[] array) {
        if (!this.forSigning) {
            throw new IllegalStateException("not initialised for signing");
        }
        final BigInteger n = ((ECPrivateKeyParameters)this.key).getParameters().getN();
        final int bitLength = n.bitLength();
        final BigInteger bigInteger = new BigInteger(1, array);
        final int bitLength2 = bigInteger.bitLength();
        final ECPrivateKeyParameters ecPrivateKeyParameters = (ECPrivateKeyParameters)this.key;
        if (bitLength2 <= bitLength) {
            BigInteger mod;
            AsymmetricCipherKeyPair generateKeyPair;
            do {
                final ECKeyPairGenerator ecKeyPairGenerator = new ECKeyPairGenerator();
                ecKeyPairGenerator.init(new ECKeyGenerationParameters(ecPrivateKeyParameters.getParameters(), this.random));
                generateKeyPair = ecKeyPairGenerator.generateKeyPair();
                mod = ((ECPublicKeyParameters)generateKeyPair.getPublic()).getQ().getAffineXCoord().toBigInteger().add(bigInteger).mod(n);
            } while (mod.equals(ECConstants.ZERO));
            return new BigInteger[] { mod, ((ECPrivateKeyParameters)generateKeyPair.getPrivate()).getD().subtract(mod.multiply(ecPrivateKeyParameters.getD())).mod(n) };
        }
        throw new DataLengthException("input too large for ECNR key.");
    }
    
    @Override
    public void init(final boolean forSigning, final CipherParameters cipherParameters) {
        this.forSigning = forSigning;
        if (!forSigning) {
            this.key = (ECPublicKeyParameters)cipherParameters;
            return;
        }
        if (cipherParameters instanceof ParametersWithRandom) {
            final ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
            this.random = parametersWithRandom.getRandom();
            this.key = (ECPrivateKeyParameters)parametersWithRandom.getParameters();
            return;
        }
        this.random = new SecureRandom();
        this.key = (ECPrivateKeyParameters)cipherParameters;
    }
    
    @Override
    public boolean verifySignature(final byte[] array, final BigInteger bigInteger, final BigInteger bigInteger2) {
        if (this.forSigning) {
            throw new IllegalStateException("not initialised for verifying");
        }
        final ECPublicKeyParameters ecPublicKeyParameters = (ECPublicKeyParameters)this.key;
        final BigInteger n = ecPublicKeyParameters.getParameters().getN();
        final int bitLength = n.bitLength();
        final BigInteger bigInteger3 = new BigInteger(1, array);
        if (bigInteger3.bitLength() <= bitLength) {
            if (bigInteger.compareTo(ECConstants.ONE) >= 0) {
                if (bigInteger.compareTo(n) >= 0) {
                    return false;
                }
                if (bigInteger2.compareTo(ECConstants.ZERO) >= 0) {
                    if (bigInteger2.compareTo(n) >= 0) {
                        return false;
                    }
                    final ECPoint normalize = ECAlgorithms.sumOfTwoMultiplies(ecPublicKeyParameters.getParameters().getG(), bigInteger2, ecPublicKeyParameters.getQ(), bigInteger).normalize();
                    return !normalize.isInfinity() && bigInteger.subtract(normalize.getAffineXCoord().toBigInteger()).mod(n).equals(bigInteger3);
                }
            }
            return false;
        }
        throw new DataLengthException("input too large for ECNR key.");
    }
}
