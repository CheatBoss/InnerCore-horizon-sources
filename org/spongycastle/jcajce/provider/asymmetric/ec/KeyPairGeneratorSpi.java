package org.spongycastle.jcajce.provider.asymmetric.ec;

import java.util.*;
import org.spongycastle.jcajce.provider.config.*;
import org.spongycastle.crypto.generators.*;
import org.spongycastle.util.*;
import org.spongycastle.jce.provider.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;
import java.math.*;
import org.spongycastle.math.ec.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.x9.*;
import org.spongycastle.crypto.params.*;
import java.security.spec.*;
import java.security.*;
import org.spongycastle.jce.spec.*;
import org.spongycastle.crypto.*;

public abstract class KeyPairGeneratorSpi extends KeyPairGenerator
{
    public KeyPairGeneratorSpi(final String s) {
        super(s);
    }
    
    public static class EC extends KeyPairGeneratorSpi
    {
        private static Hashtable ecParameters;
        String algorithm;
        int certainty;
        ProviderConfiguration configuration;
        Object ecParams;
        ECKeyPairGenerator engine;
        boolean initialised;
        ECKeyGenerationParameters param;
        SecureRandom random;
        int strength;
        
        static {
            (EC.ecParameters = new Hashtable()).put(Integers.valueOf(192), new ECGenParameterSpec("prime192v1"));
            EC.ecParameters.put(Integers.valueOf(239), new ECGenParameterSpec("prime239v1"));
            EC.ecParameters.put(Integers.valueOf(256), new ECGenParameterSpec("prime256v1"));
            EC.ecParameters.put(Integers.valueOf(224), new ECGenParameterSpec("P-224"));
            EC.ecParameters.put(Integers.valueOf(384), new ECGenParameterSpec("P-384"));
            EC.ecParameters.put(Integers.valueOf(521), new ECGenParameterSpec("P-521"));
        }
        
        public EC() {
            super("EC");
            this.engine = new ECKeyPairGenerator();
            this.ecParams = null;
            this.strength = 239;
            this.certainty = 50;
            this.random = new SecureRandom();
            this.initialised = false;
            this.algorithm = "EC";
            this.configuration = BouncyCastleProvider.CONFIGURATION;
        }
        
        public EC(final String algorithm, final ProviderConfiguration configuration) {
            super(algorithm);
            this.engine = new ECKeyPairGenerator();
            this.ecParams = null;
            this.strength = 239;
            this.certainty = 50;
            this.random = new SecureRandom();
            this.initialised = false;
            this.algorithm = algorithm;
            this.configuration = configuration;
        }
        
        protected ECKeyGenerationParameters createKeyGenParamsBC(final ECParameterSpec ecParameterSpec, final SecureRandom secureRandom) {
            return new ECKeyGenerationParameters(new ECDomainParameters(ecParameterSpec.getCurve(), ecParameterSpec.getG(), ecParameterSpec.getN(), ecParameterSpec.getH()), secureRandom);
        }
        
        protected ECKeyGenerationParameters createKeyGenParamsJCE(final java.security.spec.ECParameterSpec ecParameterSpec, final SecureRandom secureRandom) {
            final ECCurve convertCurve = EC5Util.convertCurve(ecParameterSpec.getCurve());
            return new ECKeyGenerationParameters(new ECDomainParameters(convertCurve, EC5Util.convertPoint(convertCurve, ecParameterSpec.getGenerator(), false), ecParameterSpec.getOrder(), BigInteger.valueOf(ecParameterSpec.getCofactor())), secureRandom);
        }
        
        protected ECNamedCurveSpec createNamedCurveSpec(final String s) throws InvalidAlgorithmParameterException {
            X9ECParameters x9ECParameters;
            if ((x9ECParameters = ECUtils.getDomainParametersFromName(s)) == null) {
                try {
                    if ((x9ECParameters = ECNamedCurveTable.getByOID(new ASN1ObjectIdentifier(s))) == null) {
                        x9ECParameters = this.configuration.getAdditionalECParameters().get(new ASN1ObjectIdentifier(s));
                        if (x9ECParameters == null) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("unknown curve OID: ");
                            sb.append(s);
                            throw new InvalidAlgorithmParameterException(sb.toString());
                        }
                    }
                }
                catch (IllegalArgumentException ex) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("unknown curve name: ");
                    sb2.append(s);
                    throw new InvalidAlgorithmParameterException(sb2.toString());
                }
            }
            return new ECNamedCurveSpec(s, x9ECParameters.getCurve(), x9ECParameters.getG(), x9ECParameters.getN(), x9ECParameters.getH(), null);
        }
        
        @Override
        public KeyPair generateKeyPair() {
            if (!this.initialised) {
                this.initialize(this.strength, new SecureRandom());
            }
            final AsymmetricCipherKeyPair generateKeyPair = this.engine.generateKeyPair();
            final ECPublicKeyParameters ecPublicKeyParameters = (ECPublicKeyParameters)generateKeyPair.getPublic();
            final ECPrivateKeyParameters ecPrivateKeyParameters = (ECPrivateKeyParameters)generateKeyPair.getPrivate();
            final Object ecParams = this.ecParams;
            if (ecParams instanceof ECParameterSpec) {
                final ECParameterSpec ecParameterSpec = (ECParameterSpec)ecParams;
                final BCECPublicKey bcecPublicKey = new BCECPublicKey(this.algorithm, ecPublicKeyParameters, ecParameterSpec, this.configuration);
                return new KeyPair(bcecPublicKey, new BCECPrivateKey(this.algorithm, ecPrivateKeyParameters, bcecPublicKey, ecParameterSpec, this.configuration));
            }
            if (ecParams == null) {
                return new KeyPair(new BCECPublicKey(this.algorithm, ecPublicKeyParameters, this.configuration), new BCECPrivateKey(this.algorithm, ecPrivateKeyParameters, this.configuration));
            }
            final java.security.spec.ECParameterSpec ecParameterSpec2 = (java.security.spec.ECParameterSpec)ecParams;
            final BCECPublicKey bcecPublicKey2 = new BCECPublicKey(this.algorithm, ecPublicKeyParameters, ecParameterSpec2, this.configuration);
            return new KeyPair(bcecPublicKey2, new BCECPrivateKey(this.algorithm, ecPrivateKeyParameters, bcecPublicKey2, ecParameterSpec2, this.configuration));
        }
        
        @Override
        public void initialize(final int strength, final SecureRandom random) {
            this.strength = strength;
            this.random = random;
            final ECGenParameterSpec ecGenParameterSpec = EC.ecParameters.get(Integers.valueOf(strength));
            if (ecGenParameterSpec != null) {
                try {
                    this.initialize(ecGenParameterSpec, random);
                    return;
                }
                catch (InvalidAlgorithmParameterException ex) {
                    throw new InvalidParameterException("key size not configurable.");
                }
            }
            throw new InvalidParameterException("unknown key size.");
        }
        
        @Override
        public void initialize(final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            Label_0136: {
                ECKeyGenerationParameters param = null;
                Label_0030: {
                    ECParameterSpec ecImplicitlyCa;
                    if (algorithmParameterSpec == null) {
                        ecImplicitlyCa = this.configuration.getEcImplicitlyCa();
                        if (ecImplicitlyCa == null) {
                            throw new InvalidAlgorithmParameterException("null parameter passed but no implicitCA set");
                        }
                        this.ecParams = null;
                    }
                    else if (algorithmParameterSpec instanceof ECParameterSpec) {
                        this.ecParams = algorithmParameterSpec;
                        ecImplicitlyCa = (ECParameterSpec)algorithmParameterSpec;
                    }
                    else {
                        if (algorithmParameterSpec instanceof java.security.spec.ECParameterSpec) {
                            this.ecParams = algorithmParameterSpec;
                            param = this.createKeyGenParamsJCE((java.security.spec.ECParameterSpec)algorithmParameterSpec, secureRandom);
                            break Label_0030;
                        }
                        String s;
                        if (algorithmParameterSpec instanceof ECGenParameterSpec) {
                            s = ((ECGenParameterSpec)algorithmParameterSpec).getName();
                        }
                        else {
                            if (!(algorithmParameterSpec instanceof ECNamedCurveGenParameterSpec)) {
                                throw new InvalidAlgorithmParameterException("parameter object not a ECParameterSpec");
                            }
                            s = ((ECNamedCurveGenParameterSpec)algorithmParameterSpec).getName();
                        }
                        this.initializeNamedCurve(s, secureRandom);
                        break Label_0136;
                    }
                    param = this.createKeyGenParamsBC(ecImplicitlyCa, secureRandom);
                }
                this.param = param;
            }
            this.engine.init(this.param);
            this.initialised = true;
        }
        
        protected void initializeNamedCurve(final String s, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            final ECNamedCurveSpec namedCurveSpec = this.createNamedCurveSpec(s);
            this.ecParams = namedCurveSpec;
            this.param = this.createKeyGenParamsJCE(namedCurveSpec, secureRandom);
        }
    }
    
    public static class ECDH extends EC
    {
        public ECDH() {
            super("ECDH", BouncyCastleProvider.CONFIGURATION);
        }
    }
    
    public static class ECDHC extends EC
    {
        public ECDHC() {
            super("ECDHC", BouncyCastleProvider.CONFIGURATION);
        }
    }
    
    public static class ECDSA extends EC
    {
        public ECDSA() {
            super("ECDSA", BouncyCastleProvider.CONFIGURATION);
        }
    }
    
    public static class ECMQV extends EC
    {
        public ECMQV() {
            super("ECMQV", BouncyCastleProvider.CONFIGURATION);
        }
    }
}
