package org.spongycastle.jcajce.provider.asymmetric;

import org.spongycastle.jcajce.provider.config.*;
import org.spongycastle.asn1.cryptopro.*;
import org.spongycastle.jcajce.provider.asymmetric.ecgost.*;
import org.spongycastle.jcajce.provider.util.*;
import org.spongycastle.asn1.rosstandart.*;

public class ECGOST
{
    private static final String PREFIX = "org.spongycastle.jcajce.provider.asymmetric.ecgost.";
    private static final String PREFIX_GOST_2012 = "org.spongycastle.jcajce.provider.asymmetric.ecgost12.";
    
    public static class Mappings extends AsymmetricAlgorithmProvider
    {
        @Override
        public void configure(final ConfigurableProvider configurableProvider) {
            configurableProvider.addAlgorithm("KeyFactory.ECGOST3410", "org.spongycastle.jcajce.provider.asymmetric.ecgost.KeyFactorySpi");
            configurableProvider.addAlgorithm("Alg.Alias.KeyFactory.GOST-3410-2001", "ECGOST3410");
            configurableProvider.addAlgorithm("Alg.Alias.KeyFactory.ECGOST-3410", "ECGOST3410");
            this.registerOid(configurableProvider, CryptoProObjectIdentifiers.gostR3410_2001, "ECGOST3410", new KeyFactorySpi());
            this.registerOid(configurableProvider, CryptoProObjectIdentifiers.gostR3410_2001DH, "ECGOST3410", new KeyFactorySpi());
            this.registerOidAlgorithmParameters(configurableProvider, CryptoProObjectIdentifiers.gostR3410_2001, "ECGOST3410");
            configurableProvider.addAlgorithm("KeyPairGenerator.ECGOST3410", "org.spongycastle.jcajce.provider.asymmetric.ecgost.KeyPairGeneratorSpi");
            configurableProvider.addAlgorithm("Alg.Alias.KeyPairGenerator.ECGOST-3410", "ECGOST3410");
            configurableProvider.addAlgorithm("Alg.Alias.KeyPairGenerator.GOST-3410-2001", "ECGOST3410");
            configurableProvider.addAlgorithm("Signature.ECGOST3410", "org.spongycastle.jcajce.provider.asymmetric.ecgost.SignatureSpi");
            configurableProvider.addAlgorithm("Alg.Alias.Signature.ECGOST-3410", "ECGOST3410");
            configurableProvider.addAlgorithm("Alg.Alias.Signature.GOST-3410-2001", "ECGOST3410");
            configurableProvider.addAlgorithm("KeyAgreement.ECGOST3410", "org.spongycastle.jcajce.provider.asymmetric.ecgost.KeyAgreementSpi$ECVKO");
            final StringBuilder sb = new StringBuilder();
            sb.append("Alg.Alias.KeyAgreement.");
            sb.append(CryptoProObjectIdentifiers.gostR3410_2001);
            configurableProvider.addAlgorithm(sb.toString(), "ECGOST3410");
            configurableProvider.addAlgorithm("Alg.Alias.KeyAgreement.GOST-3410-2001", "ECGOST3410");
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Alg.Alias.KeyAgreement.");
            sb2.append(CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_ESDH);
            configurableProvider.addAlgorithm(sb2.toString(), "ECGOST3410");
            configurableProvider.addAlgorithm("AlgorithmParameters.ECGOST3410", "org.spongycastle.jcajce.provider.asymmetric.ecgost.AlgorithmParametersSpi");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.GOST-3410-2001", "ECGOST3410");
            this.addSignatureAlgorithm(configurableProvider, "GOST3411", "ECGOST3410", "org.spongycastle.jcajce.provider.asymmetric.ecgost.SignatureSpi", CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_2001);
            configurableProvider.addAlgorithm("KeyFactory.ECGOST3410-2012", "org.spongycastle.jcajce.provider.asymmetric.ecgost12.KeyFactorySpi");
            configurableProvider.addAlgorithm("Alg.Alias.KeyFactory.GOST-3410-2012", "ECGOST3410-2012");
            configurableProvider.addAlgorithm("Alg.Alias.KeyFactory.ECGOST-3410-2012", "ECGOST3410-2012");
            this.registerOid(configurableProvider, RosstandartObjectIdentifiers.id_tc26_gost_3410_12_256, "ECGOST3410-2012", new org.spongycastle.jcajce.provider.asymmetric.ecgost12.KeyFactorySpi());
            this.registerOid(configurableProvider, RosstandartObjectIdentifiers.id_tc26_agreement_gost_3410_12_256, "ECGOST3410-2012", new org.spongycastle.jcajce.provider.asymmetric.ecgost12.KeyFactorySpi());
            this.registerOidAlgorithmParameters(configurableProvider, RosstandartObjectIdentifiers.id_tc26_gost_3410_12_256, "ECGOST3410-2012");
            this.registerOid(configurableProvider, RosstandartObjectIdentifiers.id_tc26_gost_3410_12_512, "ECGOST3410-2012", new org.spongycastle.jcajce.provider.asymmetric.ecgost12.KeyFactorySpi());
            this.registerOid(configurableProvider, RosstandartObjectIdentifiers.id_tc26_agreement_gost_3410_12_512, "ECGOST3410-2012", new org.spongycastle.jcajce.provider.asymmetric.ecgost12.KeyFactorySpi());
            this.registerOidAlgorithmParameters(configurableProvider, RosstandartObjectIdentifiers.id_tc26_gost_3410_12_512, "ECGOST3410-2012");
            configurableProvider.addAlgorithm("KeyPairGenerator.ECGOST3410-2012", "org.spongycastle.jcajce.provider.asymmetric.ecgost12.KeyPairGeneratorSpi");
            configurableProvider.addAlgorithm("Alg.Alias.KeyPairGenerator.ECGOST3410-2012", "ECGOST3410-2012");
            configurableProvider.addAlgorithm("Alg.Alias.KeyPairGenerator.GOST-3410-2012", "ECGOST3410-2012");
            configurableProvider.addAlgorithm("Signature.ECGOST3410-2012-256", "org.spongycastle.jcajce.provider.asymmetric.ecgost12.ECGOST2012SignatureSpi256");
            configurableProvider.addAlgorithm("Alg.Alias.Signature.ECGOST3410-2012-256", "ECGOST3410-2012-256");
            configurableProvider.addAlgorithm("Alg.Alias.Signature.GOST-3410-2012-256", "ECGOST3410-2012-256");
            this.addSignatureAlgorithm(configurableProvider, "GOST3411-2012-256", "ECGOST3410-2012-256", "org.spongycastle.jcajce.provider.asymmetric.ecgost12.ECGOST2012SignatureSpi256", RosstandartObjectIdentifiers.id_tc26_signwithdigest_gost_3410_12_256);
            configurableProvider.addAlgorithm("Signature.ECGOST3410-2012-512", "org.spongycastle.jcajce.provider.asymmetric.ecgost12.ECGOST2012SignatureSpi512");
            configurableProvider.addAlgorithm("Alg.Alias.Signature.ECGOST3410-2012-512", "ECGOST3410-2012-512");
            configurableProvider.addAlgorithm("Alg.Alias.Signature.GOST-3410-2012-512", "ECGOST3410-2012-512");
            this.addSignatureAlgorithm(configurableProvider, "GOST3411-2012-512", "ECGOST3410-2012-512", "org.spongycastle.jcajce.provider.asymmetric.ecgost12.ECGOST2012SignatureSpi512", RosstandartObjectIdentifiers.id_tc26_signwithdigest_gost_3410_12_512);
            configurableProvider.addAlgorithm("KeyAgreement.ECGOST3410-2012-256", "org.spongycastle.jcajce.provider.asymmetric.ecgost12.KeyAgreementSpi$ECVKO256");
            configurableProvider.addAlgorithm("KeyAgreement.ECGOST3410-2012-512", "org.spongycastle.jcajce.provider.asymmetric.ecgost12.KeyAgreementSpi$ECVKO512");
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Alg.Alias.KeyAgreement.");
            sb3.append(RosstandartObjectIdentifiers.id_tc26_agreement_gost_3410_12_256);
            configurableProvider.addAlgorithm(sb3.toString(), "ECGOST3410-2012-256");
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Alg.Alias.KeyAgreement.");
            sb4.append(RosstandartObjectIdentifiers.id_tc26_agreement_gost_3410_12_512);
            configurableProvider.addAlgorithm(sb4.toString(), "ECGOST3410-2012-512");
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("Alg.Alias.KeyAgreement.");
            sb5.append(RosstandartObjectIdentifiers.id_tc26_gost_3410_12_256);
            configurableProvider.addAlgorithm(sb5.toString(), "ECGOST3410-2012-256");
            final StringBuilder sb6 = new StringBuilder();
            sb6.append("Alg.Alias.KeyAgreement.");
            sb6.append(RosstandartObjectIdentifiers.id_tc26_gost_3410_12_512);
            configurableProvider.addAlgorithm(sb6.toString(), "ECGOST3410-2012-512");
        }
    }
}
