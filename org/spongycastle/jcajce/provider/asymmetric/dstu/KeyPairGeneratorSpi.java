package org.spongycastle.jcajce.provider.asymmetric.dstu;

import org.spongycastle.crypto.generators.*;
import java.security.spec.*;
import java.security.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;
import java.math.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.jce.provider.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.ua.*;
import org.spongycastle.jce.spec.*;
import org.spongycastle.crypto.*;
import org.spongycastle.math.ec.*;

public class KeyPairGeneratorSpi extends KeyPairGenerator
{
    String algorithm;
    Object ecParams;
    ECKeyPairGenerator engine;
    boolean initialised;
    ECKeyGenerationParameters param;
    SecureRandom random;
    
    public KeyPairGeneratorSpi() {
        super("DSTU4145");
        this.ecParams = null;
        this.engine = new DSTU4145KeyPairGenerator();
        this.algorithm = "DSTU4145";
        this.random = null;
        this.initialised = false;
    }
    
    @Override
    public KeyPair generateKeyPair() {
        if (!this.initialised) {
            throw new IllegalStateException("DSTU Key Pair Generator not initialised");
        }
        final AsymmetricCipherKeyPair generateKeyPair = this.engine.generateKeyPair();
        final ECPublicKeyParameters ecPublicKeyParameters = (ECPublicKeyParameters)generateKeyPair.getPublic();
        final ECPrivateKeyParameters ecPrivateKeyParameters = (ECPrivateKeyParameters)generateKeyPair.getPrivate();
        final Object ecParams = this.ecParams;
        if (ecParams instanceof ECParameterSpec) {
            final ECParameterSpec ecParameterSpec = (ECParameterSpec)ecParams;
            final BCDSTU4145PublicKey bcdstu4145PublicKey = new BCDSTU4145PublicKey(this.algorithm, ecPublicKeyParameters, ecParameterSpec);
            return new KeyPair(bcdstu4145PublicKey, new BCDSTU4145PrivateKey(this.algorithm, ecPrivateKeyParameters, bcdstu4145PublicKey, ecParameterSpec));
        }
        if (ecParams == null) {
            return new KeyPair(new BCDSTU4145PublicKey(this.algorithm, ecPublicKeyParameters), new BCDSTU4145PrivateKey(this.algorithm, ecPrivateKeyParameters));
        }
        final java.security.spec.ECParameterSpec ecParameterSpec2 = (java.security.spec.ECParameterSpec)ecParams;
        final BCDSTU4145PublicKey bcdstu4145PublicKey2 = new BCDSTU4145PublicKey(this.algorithm, ecPublicKeyParameters, ecParameterSpec2);
        return new KeyPair(bcdstu4145PublicKey2, new BCDSTU4145PrivateKey(this.algorithm, ecPrivateKeyParameters, bcdstu4145PublicKey2, ecParameterSpec2));
    }
    
    @Override
    public void initialize(final int n, final SecureRandom random) {
        this.random = random;
        final Object ecParams = this.ecParams;
        if (ecParams != null) {
            try {
                this.initialize((AlgorithmParameterSpec)ecParams, random);
                return;
            }
            catch (InvalidAlgorithmParameterException ex) {
                throw new InvalidParameterException("key size not configurable.");
            }
        }
        throw new InvalidParameterException("unknown key size.");
    }
    
    @Override
    public void initialize(final AlgorithmParameterSpec ecParams, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        Label_0067: {
            ECKeyGenerationParameters param2 = null;
            Label_0054: {
                if (!(ecParams instanceof ECParameterSpec)) {
                    ECKeyGenerationParameters param;
                    if (ecParams instanceof java.security.spec.ECParameterSpec) {
                        final java.security.spec.ECParameterSpec ecParameterSpec = (java.security.spec.ECParameterSpec)ecParams;
                        this.ecParams = ecParams;
                        final ECCurve convertCurve = EC5Util.convertCurve(ecParameterSpec.getCurve());
                        param = new ECKeyGenerationParameters(new ECDomainParameters(convertCurve, EC5Util.convertPoint(convertCurve, ecParameterSpec.getGenerator(), false), ecParameterSpec.getOrder(), BigInteger.valueOf(ecParameterSpec.getCofactor())), secureRandom);
                    }
                    else {
                        final boolean b = ecParams instanceof ECGenParameterSpec;
                        if (!b && !(ecParams instanceof ECNamedCurveGenParameterSpec)) {
                            if (ecParams == null && BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa() != null) {
                                final ECParameterSpec ecImplicitlyCa = BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa();
                                this.ecParams = ecParams;
                                param2 = new ECKeyGenerationParameters(new ECDomainParameters(ecImplicitlyCa.getCurve(), ecImplicitlyCa.getG(), ecImplicitlyCa.getN(), ecImplicitlyCa.getH()), secureRandom);
                                break Label_0054;
                            }
                            if (ecParams == null && BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa() == null) {
                                throw new InvalidAlgorithmParameterException("null parameter passed but no implicitCA set");
                            }
                            final StringBuilder sb = new StringBuilder();
                            sb.append("parameter object not a ECParameterSpec: ");
                            sb.append(ecParams.getClass().getName());
                            throw new InvalidAlgorithmParameterException(sb.toString());
                        }
                        else {
                            String s;
                            if (b) {
                                s = ((ECGenParameterSpec)ecParams).getName();
                            }
                            else {
                                s = ((ECNamedCurveGenParameterSpec)ecParams).getName();
                            }
                            final ECDomainParameters byOID = DSTU4145NamedCurves.getByOID(new ASN1ObjectIdentifier(s));
                            if (byOID == null) {
                                final StringBuilder sb2 = new StringBuilder();
                                sb2.append("unknown curve name: ");
                                sb2.append(s);
                                throw new InvalidAlgorithmParameterException(sb2.toString());
                            }
                            final ECNamedCurveSpec ecParams2 = new ECNamedCurveSpec(s, byOID.getCurve(), byOID.getG(), byOID.getN(), byOID.getH(), byOID.getSeed());
                            this.ecParams = ecParams2;
                            final ECNamedCurveSpec ecNamedCurveSpec = ecParams2;
                            final ECCurve convertCurve2 = EC5Util.convertCurve(ecNamedCurveSpec.getCurve());
                            param = new ECKeyGenerationParameters(new ECDomainParameters(convertCurve2, EC5Util.convertPoint(convertCurve2, ecNamedCurveSpec.getGenerator(), false), ecNamedCurveSpec.getOrder(), BigInteger.valueOf(ecNamedCurveSpec.getCofactor())), secureRandom);
                        }
                    }
                    this.param = param;
                    this.engine.init(param);
                    break Label_0067;
                }
                final ECParameterSpec ecParameterSpec2 = (ECParameterSpec)ecParams;
                this.ecParams = ecParams;
                param2 = new ECKeyGenerationParameters(new ECDomainParameters(ecParameterSpec2.getCurve(), ecParameterSpec2.getG(), ecParameterSpec2.getN(), ecParameterSpec2.getH()), secureRandom);
            }
            this.param = param2;
            this.engine.init(param2);
        }
        this.initialised = true;
    }
}
