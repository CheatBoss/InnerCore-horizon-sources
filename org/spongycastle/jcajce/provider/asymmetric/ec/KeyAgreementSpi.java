package org.spongycastle.jcajce.provider.asymmetric.ec;

import org.spongycastle.asn1.x9.*;
import java.math.*;
import java.security.spec.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;
import org.spongycastle.jcajce.spec.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.jce.interfaces.*;
import java.security.*;
import org.spongycastle.crypto.util.*;
import org.spongycastle.crypto.generators.*;
import org.spongycastle.crypto.agreement.*;
import org.spongycastle.crypto.agreement.kdf.*;

public class KeyAgreementSpi extends BaseAgreementSpi
{
    private static final X9IntegerConverter converter;
    private BasicAgreement agreement;
    private String kaAlgorithm;
    private MQVParameterSpec mqvParameters;
    private ECDomainParameters parameters;
    private BigInteger result;
    
    static {
        converter = new X9IntegerConverter();
    }
    
    protected KeyAgreementSpi(final String kaAlgorithm, final BasicAgreement agreement, final DerivationFunction derivationFunction) {
        super(kaAlgorithm, derivationFunction);
        this.kaAlgorithm = kaAlgorithm;
        this.agreement = agreement;
    }
    
    private static String getSimpleName(final Class clazz) {
        final String name = clazz.getName();
        return name.substring(name.lastIndexOf(46) + 1);
    }
    
    private void initFromKey(final Key key, final AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException {
        final boolean b = this.agreement instanceof ECMQVBasicAgreement;
        final ECPublicKeyParameters ecPublicKeyParameters = null;
        final byte[] array = null;
        final ECPublicKeyParameters ecPublicKeyParameters2 = null;
        CipherParameters cipherParameters;
        if (b) {
            this.mqvParameters = null;
            final boolean b2 = key instanceof MQVPrivateKey;
            if (!b2 && !(algorithmParameterSpec instanceof MQVParameterSpec)) {
                final StringBuilder sb = new StringBuilder();
                sb.append(this.kaAlgorithm);
                sb.append(" key agreement requires ");
                sb.append(getSimpleName(MQVParameterSpec.class));
                sb.append(" for initialisation");
                throw new InvalidKeyException(sb.toString());
            }
            ECPrivateKeyParameters ecPrivateKeyParameters2;
            ECPublicKeyParameters ecPublicKeyParameters3;
            ECPrivateKeyParameters ecPrivateKeyParameters3;
            if (b2) {
                final MQVPrivateKey mqvPrivateKey = (MQVPrivateKey)key;
                final ECPrivateKeyParameters ecPrivateKeyParameters = (ECPrivateKeyParameters)ECUtil.generatePrivateKeyParameter(mqvPrivateKey.getStaticPrivateKey());
                ecPrivateKeyParameters2 = (ECPrivateKeyParameters)ECUtil.generatePrivateKeyParameter(mqvPrivateKey.getEphemeralPrivateKey());
                ecPublicKeyParameters3 = ecPublicKeyParameters;
                ecPrivateKeyParameters3 = ecPrivateKeyParameters;
                if (mqvPrivateKey.getEphemeralPublicKey() != null) {
                    ecPublicKeyParameters3 = (ECPublicKeyParameters)ECUtils.generatePublicKeyParameter(mqvPrivateKey.getEphemeralPublicKey());
                    ecPrivateKeyParameters2 = ecPrivateKeyParameters2;
                    ecPrivateKeyParameters3 = ecPrivateKeyParameters;
                }
            }
            else {
                final MQVParameterSpec mqvParameters = (MQVParameterSpec)algorithmParameterSpec;
                ecPrivateKeyParameters3 = (ECPrivateKeyParameters)ECUtil.generatePrivateKeyParameter((PrivateKey)key);
                ecPrivateKeyParameters2 = (ECPrivateKeyParameters)ECUtil.generatePrivateKeyParameter(mqvParameters.getEphemeralPrivateKey());
                ecPublicKeyParameters3 = ecPublicKeyParameters2;
                if (mqvParameters.getEphemeralPublicKey() != null) {
                    ecPublicKeyParameters3 = (ECPublicKeyParameters)ECUtils.generatePublicKeyParameter(mqvParameters.getEphemeralPublicKey());
                }
                this.mqvParameters = mqvParameters;
                this.ukmParameters = mqvParameters.getUserKeyingMaterial();
            }
            cipherParameters = new MQVPrivateParameters(ecPrivateKeyParameters3, ecPrivateKeyParameters2, ecPublicKeyParameters3);
            this.parameters = ecPrivateKeyParameters3.getParameters();
        }
        else {
            if (!(key instanceof PrivateKey)) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(this.kaAlgorithm);
                sb2.append(" key agreement requires ");
                sb2.append(getSimpleName(ECPrivateKey.class));
                sb2.append(" for initialisation");
                throw new InvalidKeyException(sb2.toString());
            }
            final ECPrivateKeyParameters ecPrivateKeyParameters4 = (ECPrivateKeyParameters)ECUtil.generatePrivateKeyParameter((PrivateKey)key);
            this.parameters = ecPrivateKeyParameters4.getParameters();
            byte[] userKeyingMaterial = array;
            if (algorithmParameterSpec instanceof UserKeyingMaterialSpec) {
                userKeyingMaterial = ((UserKeyingMaterialSpec)algorithmParameterSpec).getUserKeyingMaterial();
            }
            this.ukmParameters = userKeyingMaterial;
            cipherParameters = ecPrivateKeyParameters4;
        }
        this.agreement.init(cipherParameters);
    }
    
    protected byte[] bigIntToBytes(final BigInteger bigInteger) {
        final X9IntegerConverter converter = KeyAgreementSpi.converter;
        return converter.integerToBytes(bigInteger, converter.getByteLength(this.parameters.getCurve()));
    }
    
    @Override
    protected byte[] calcSecret() {
        return this.bigIntToBytes(this.result);
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
            Label_0173: {
                CipherParameters generatePublicKeyParameter;
                if (this.agreement instanceof ECMQVBasicAgreement) {
                    if (!(key instanceof MQVPublicKey)) {
                        generatePublicKeyParameter = new MQVPublicParameters((ECPublicKeyParameters)ECUtils.generatePublicKeyParameter((PublicKey)key), (ECPublicKeyParameters)ECUtils.generatePublicKeyParameter(this.mqvParameters.getOtherPartyEphemeralKey()));
                    }
                    else {
                        final MQVPublicKey mqvPublicKey = (MQVPublicKey)key;
                        generatePublicKeyParameter = new MQVPublicParameters((ECPublicKeyParameters)ECUtils.generatePublicKeyParameter(mqvPublicKey.getStaticKey()), (ECPublicKeyParameters)ECUtils.generatePublicKeyParameter(mqvPublicKey.getEphemeralKey()));
                    }
                }
                else {
                    if (!(key instanceof PublicKey)) {
                        break Label_0173;
                    }
                    generatePublicKeyParameter = ECUtils.generatePublicKeyParameter((PublicKey)key);
                }
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
        if (algorithmParameterSpec != null && !(algorithmParameterSpec instanceof MQVParameterSpec) && !(algorithmParameterSpec instanceof UserKeyingMaterialSpec)) {
            throw new InvalidAlgorithmParameterException("No algorithm parameters supported");
        }
        this.initFromKey(key, algorithmParameterSpec);
    }
    
    public static class CDHwithSHA1KDFAndSharedInfo extends KeyAgreementSpi
    {
        public CDHwithSHA1KDFAndSharedInfo() {
            super("ECCDHwithSHA1KDF", new ECDHCBasicAgreement(), new KDF2BytesGenerator(DigestFactory.createSHA1()));
        }
    }
    
    public static class CDHwithSHA224KDFAndSharedInfo extends KeyAgreementSpi
    {
        public CDHwithSHA224KDFAndSharedInfo() {
            super("ECCDHwithSHA224KDF", new ECDHCBasicAgreement(), new KDF2BytesGenerator(DigestFactory.createSHA224()));
        }
    }
    
    public static class CDHwithSHA256KDFAndSharedInfo extends KeyAgreementSpi
    {
        public CDHwithSHA256KDFAndSharedInfo() {
            super("ECCDHwithSHA256KDF", new ECDHCBasicAgreement(), new KDF2BytesGenerator(DigestFactory.createSHA256()));
        }
    }
    
    public static class CDHwithSHA384KDFAndSharedInfo extends KeyAgreementSpi
    {
        public CDHwithSHA384KDFAndSharedInfo() {
            super("ECCDHwithSHA384KDF", new ECDHCBasicAgreement(), new KDF2BytesGenerator(DigestFactory.createSHA384()));
        }
    }
    
    public static class CDHwithSHA512KDFAndSharedInfo extends KeyAgreementSpi
    {
        public CDHwithSHA512KDFAndSharedInfo() {
            super("ECCDHwithSHA512KDF", new ECDHCBasicAgreement(), new KDF2BytesGenerator(DigestFactory.createSHA512()));
        }
    }
    
    public static class DH extends KeyAgreementSpi
    {
        public DH() {
            super("ECDH", new ECDHBasicAgreement(), null);
        }
    }
    
    public static class DHC extends KeyAgreementSpi
    {
        public DHC() {
            super("ECDHC", new ECDHCBasicAgreement(), null);
        }
    }
    
    public static class DHwithSHA1CKDF extends KeyAgreementSpi
    {
        public DHwithSHA1CKDF() {
            super("ECDHwithSHA1CKDF", new ECDHCBasicAgreement(), new ConcatenationKDFGenerator(DigestFactory.createSHA1()));
        }
    }
    
    public static class DHwithSHA1KDF extends KeyAgreementSpi
    {
        public DHwithSHA1KDF() {
            super("ECDHwithSHA1KDF", new ECDHBasicAgreement(), new KDF2BytesGenerator(DigestFactory.createSHA1()));
        }
    }
    
    public static class DHwithSHA1KDFAndSharedInfo extends KeyAgreementSpi
    {
        public DHwithSHA1KDFAndSharedInfo() {
            super("ECDHwithSHA1KDF", new ECDHBasicAgreement(), new KDF2BytesGenerator(DigestFactory.createSHA1()));
        }
    }
    
    public static class DHwithSHA224KDFAndSharedInfo extends KeyAgreementSpi
    {
        public DHwithSHA224KDFAndSharedInfo() {
            super("ECDHwithSHA224KDF", new ECDHBasicAgreement(), new KDF2BytesGenerator(DigestFactory.createSHA224()));
        }
    }
    
    public static class DHwithSHA256CKDF extends KeyAgreementSpi
    {
        public DHwithSHA256CKDF() {
            super("ECDHwithSHA256CKDF", new ECDHCBasicAgreement(), new ConcatenationKDFGenerator(DigestFactory.createSHA256()));
        }
    }
    
    public static class DHwithSHA256KDFAndSharedInfo extends KeyAgreementSpi
    {
        public DHwithSHA256KDFAndSharedInfo() {
            super("ECDHwithSHA256KDF", new ECDHBasicAgreement(), new KDF2BytesGenerator(DigestFactory.createSHA256()));
        }
    }
    
    public static class DHwithSHA384CKDF extends KeyAgreementSpi
    {
        public DHwithSHA384CKDF() {
            super("ECDHwithSHA384CKDF", new ECDHCBasicAgreement(), new ConcatenationKDFGenerator(DigestFactory.createSHA384()));
        }
    }
    
    public static class DHwithSHA384KDFAndSharedInfo extends KeyAgreementSpi
    {
        public DHwithSHA384KDFAndSharedInfo() {
            super("ECDHwithSHA384KDF", new ECDHBasicAgreement(), new KDF2BytesGenerator(DigestFactory.createSHA384()));
        }
    }
    
    public static class DHwithSHA512CKDF extends KeyAgreementSpi
    {
        public DHwithSHA512CKDF() {
            super("ECDHwithSHA512CKDF", new ECDHCBasicAgreement(), new ConcatenationKDFGenerator(DigestFactory.createSHA512()));
        }
    }
    
    public static class DHwithSHA512KDFAndSharedInfo extends KeyAgreementSpi
    {
        public DHwithSHA512KDFAndSharedInfo() {
            super("ECDHwithSHA512KDF", new ECDHBasicAgreement(), new KDF2BytesGenerator(DigestFactory.createSHA512()));
        }
    }
    
    public static class MQV extends KeyAgreementSpi
    {
        public MQV() {
            super("ECMQV", new ECMQVBasicAgreement(), null);
        }
    }
    
    public static class MQVwithSHA1CKDF extends KeyAgreementSpi
    {
        public MQVwithSHA1CKDF() {
            super("ECMQVwithSHA1CKDF", new ECMQVBasicAgreement(), new ConcatenationKDFGenerator(DigestFactory.createSHA1()));
        }
    }
    
    public static class MQVwithSHA1KDFAndSharedInfo extends KeyAgreementSpi
    {
        public MQVwithSHA1KDFAndSharedInfo() {
            super("ECMQVwithSHA1KDF", new ECMQVBasicAgreement(), new KDF2BytesGenerator(DigestFactory.createSHA1()));
        }
    }
    
    public static class MQVwithSHA224CKDF extends KeyAgreementSpi
    {
        public MQVwithSHA224CKDF() {
            super("ECMQVwithSHA224CKDF", new ECMQVBasicAgreement(), new ConcatenationKDFGenerator(DigestFactory.createSHA224()));
        }
    }
    
    public static class MQVwithSHA224KDFAndSharedInfo extends KeyAgreementSpi
    {
        public MQVwithSHA224KDFAndSharedInfo() {
            super("ECMQVwithSHA224KDF", new ECMQVBasicAgreement(), new KDF2BytesGenerator(DigestFactory.createSHA224()));
        }
    }
    
    public static class MQVwithSHA256CKDF extends KeyAgreementSpi
    {
        public MQVwithSHA256CKDF() {
            super("ECMQVwithSHA256CKDF", new ECMQVBasicAgreement(), new ConcatenationKDFGenerator(DigestFactory.createSHA256()));
        }
    }
    
    public static class MQVwithSHA256KDFAndSharedInfo extends KeyAgreementSpi
    {
        public MQVwithSHA256KDFAndSharedInfo() {
            super("ECMQVwithSHA256KDF", new ECMQVBasicAgreement(), new KDF2BytesGenerator(DigestFactory.createSHA256()));
        }
    }
    
    public static class MQVwithSHA384CKDF extends KeyAgreementSpi
    {
        public MQVwithSHA384CKDF() {
            super("ECMQVwithSHA384CKDF", new ECMQVBasicAgreement(), new ConcatenationKDFGenerator(DigestFactory.createSHA384()));
        }
    }
    
    public static class MQVwithSHA384KDFAndSharedInfo extends KeyAgreementSpi
    {
        public MQVwithSHA384KDFAndSharedInfo() {
            super("ECMQVwithSHA384KDF", new ECMQVBasicAgreement(), new KDF2BytesGenerator(DigestFactory.createSHA384()));
        }
    }
    
    public static class MQVwithSHA512CKDF extends KeyAgreementSpi
    {
        public MQVwithSHA512CKDF() {
            super("ECMQVwithSHA512CKDF", new ECMQVBasicAgreement(), new ConcatenationKDFGenerator(DigestFactory.createSHA512()));
        }
    }
    
    public static class MQVwithSHA512KDFAndSharedInfo extends KeyAgreementSpi
    {
        public MQVwithSHA512KDFAndSharedInfo() {
            super("ECMQVwithSHA512KDF", new ECMQVBasicAgreement(), new KDF2BytesGenerator(DigestFactory.createSHA512()));
        }
    }
}
