package org.spongycastle.pqc.jcajce.provider;

import org.spongycastle.jcajce.provider.config.*;
import org.spongycastle.pqc.asn1.*;
import org.spongycastle.pqc.jcajce.provider.newhope.*;
import org.spongycastle.jcajce.provider.util.*;

public class NH
{
    private static final String PREFIX = "org.spongycastle.pqc.jcajce.provider.newhope.";
    
    public static class Mappings extends AsymmetricAlgorithmProvider
    {
        @Override
        public void configure(final ConfigurableProvider configurableProvider) {
            configurableProvider.addAlgorithm("KeyFactory.NH", "org.spongycastle.pqc.jcajce.provider.newhope.NHKeyFactorySpi");
            configurableProvider.addAlgorithm("KeyPairGenerator.NH", "org.spongycastle.pqc.jcajce.provider.newhope.NHKeyPairGeneratorSpi");
            configurableProvider.addAlgorithm("KeyAgreement.NH", "org.spongycastle.pqc.jcajce.provider.newhope.KeyAgreementSpi");
            this.registerOid(configurableProvider, PQCObjectIdentifiers.newHope, "NH", new NHKeyFactorySpi());
        }
    }
}
