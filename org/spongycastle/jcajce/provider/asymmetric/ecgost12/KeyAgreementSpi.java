package org.spongycastle.jcajce.provider.asymmetric.ecgost12;

import org.spongycastle.asn1.x9.*;
import org.spongycastle.crypto.agreement.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;
import java.security.spec.*;
import org.spongycastle.jcajce.spec.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.jce.interfaces.*;
import java.security.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.digests.*;

public class KeyAgreementSpi extends BaseAgreementSpi
{
    private static final X9IntegerConverter converter;
    private ECVKOAgreement agreement;
    private String kaAlgorithm;
    private ECDomainParameters parameters;
    private byte[] result;
    
    static {
        converter = new X9IntegerConverter();
    }
    
    protected KeyAgreementSpi(final String kaAlgorithm, final ECVKOAgreement agreement, final DerivationFunction derivationFunction) {
        super(kaAlgorithm, derivationFunction);
        this.kaAlgorithm = kaAlgorithm;
        this.agreement = agreement;
    }
    
    static AsymmetricKeyParameter generatePublicKeyParameter(final PublicKey publicKey) throws InvalidKeyException {
        if (publicKey instanceof BCECGOST3410_2012PublicKey) {
            return ((BCECGOST3410_2012PublicKey)publicKey).engineGetKeyParameters();
        }
        return ECUtil.generatePublicKeyParameter(publicKey);
    }
    
    private static String getSimpleName(final Class clazz) {
        final String name = clazz.getName();
        return name.substring(name.lastIndexOf(46) + 1);
    }
    
    private void initFromKey(final Key key, final AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException {
        if (key instanceof PrivateKey) {
            final ECPrivateKeyParameters ecPrivateKeyParameters = (ECPrivateKeyParameters)ECUtil.generatePrivateKeyParameter((PrivateKey)key);
            this.parameters = ecPrivateKeyParameters.getParameters();
            byte[] userKeyingMaterial;
            if (algorithmParameterSpec instanceof UserKeyingMaterialSpec) {
                userKeyingMaterial = ((UserKeyingMaterialSpec)algorithmParameterSpec).getUserKeyingMaterial();
            }
            else {
                userKeyingMaterial = null;
            }
            this.ukmParameters = userKeyingMaterial;
            this.agreement.init(new ParametersWithUKM(ecPrivateKeyParameters, this.ukmParameters));
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(this.kaAlgorithm);
        sb.append(" key agreement requires ");
        sb.append(getSimpleName(ECPrivateKey.class));
        sb.append(" for initialisation");
        throw new InvalidKeyException(sb.toString());
    }
    
    @Override
    protected byte[] calcSecret() {
        return this.result;
    }
    
    @Override
    protected Key engineDoPhase(final Key key, final boolean b) throws InvalidKeyException, IllegalStateException {
        if (this.parameters == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.kaAlgorithm);
            sb.append(" not initialised.");
            throw new IllegalStateException(sb.toString());
        }
        if (b) {
            if (key instanceof PublicKey) {
                final AsymmetricKeyParameter generatePublicKeyParameter = generatePublicKeyParameter((PublicKey)key);
                try {
                    this.result = this.agreement.calculateAgreement(generatePublicKeyParameter);
                    return null;
                }
                catch (Exception ex) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("calculation failed: ");
                    sb2.append(ex.getMessage());
                    throw new InvalidKeyException(sb2.toString()) {
                        @Override
                        public Throwable getCause() {
                            return ex;
                        }
                    };
                }
            }
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(this.kaAlgorithm);
            sb3.append(" key agreement requires ");
            sb3.append(getSimpleName(ECPublicKey.class));
            sb3.append(" for doPhase");
            throw new InvalidKeyException(sb3.toString());
        }
        final StringBuilder sb4 = new StringBuilder();
        sb4.append(this.kaAlgorithm);
        sb4.append(" can only be between two parties.");
        throw new IllegalStateException(sb4.toString());
    }
    
    @Override
    protected void engineInit(final Key key, final SecureRandom secureRandom) throws InvalidKeyException {
        this.initFromKey(key, null);
    }
    
    @Override
    protected void engineInit(final Key key, final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (algorithmParameterSpec != null && !(algorithmParameterSpec instanceof UserKeyingMaterialSpec)) {
            throw new InvalidAlgorithmParameterException("No algorithm parameters supported");
        }
        this.initFromKey(key, algorithmParameterSpec);
    }
    
    public static class ECVKO256 extends KeyAgreementSpi
    {
        public ECVKO256() {
            super("ECGOST3410-2012-256", new ECVKOAgreement(new GOST3411_2012_256Digest()), null);
        }
    }
    
    public static class ECVKO512 extends KeyAgreementSpi
    {
        public ECVKO512() {
            super("ECGOST3410-2012-512", new ECVKOAgreement(new GOST3411_2012_512Digest()), null);
        }
    }
}
