package org.spongycastle.pqc.jcajce.provider;

import org.spongycastle.jcajce.provider.config.*;
import org.spongycastle.pqc.asn1.*;
import org.spongycastle.pqc.jcajce.provider.sphincs.*;
import org.spongycastle.jcajce.provider.util.*;

public class SPHINCS
{
    private static final String PREFIX = "org.spongycastle.pqc.jcajce.provider.sphincs.";
    
    public static class Mappings extends AsymmetricAlgorithmProvider
    {
        @Override
        public void configure(final ConfigurableProvider configurableProvider) {
            configurableProvider.addAlgorithm("KeyFactory.SPHINCS256", "org.spongycastle.pqc.jcajce.provider.sphincs.Sphincs256KeyFactorySpi");
            configurableProvider.addAlgorithm("KeyPairGenerator.SPHINCS256", "org.spongycastle.pqc.jcajce.provider.sphincs.Sphincs256KeyPairGeneratorSpi");
            this.addSignatureAlgorithm(configurableProvider, "SHA512", "SPHINCS256", "org.spongycastle.pqc.jcajce.provider.sphincs.SignatureSpi$withSha512", PQCObjectIdentifiers.sphincs256_with_SHA512);
            this.addSignatureAlgorithm(configurableProvider, "SHA3-512", "SPHINCS256", "org.spongycastle.pqc.jcajce.provider.sphincs.SignatureSpi$withSha3_512", PQCObjectIdentifiers.sphincs256_with_SHA3_512);
            this.registerOid(configurableProvider, PQCObjectIdentifiers.sphincs256, "SPHINCS256", new Sphincs256KeyFactorySpi());
            this.registerOidAlgorithmParameters(configurableProvider, PQCObjectIdentifiers.sphincs256, "SPHINCS256");
        }
    }
}
