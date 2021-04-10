package org.spongycastle.jcajce.provider.asymmetric;

import java.util.*;
import org.spongycastle.jcajce.provider.util.*;
import org.spongycastle.jcajce.provider.config.*;
import org.spongycastle.asn1.gm.*;

public class GM
{
    private static final String PREFIX = "org.spongycastle.jcajce.provider.asymmetric.ec.";
    private static final Map<String, String> generalSm2Attributes;
    
    static {
        (generalSm2Attributes = new HashMap<String, String>()).put("SupportedKeyClasses", "java.security.interfaces.ECPublicKey|java.security.interfaces.ECPrivateKey");
        GM.generalSm2Attributes.put("SupportedKeyFormats", "PKCS#8|X.509");
    }
    
    public static class Mappings extends AsymmetricAlgorithmProvider
    {
        @Override
        public void configure(final ConfigurableProvider configurableProvider) {
            configurableProvider.addAlgorithm("Signature.SM3WITHSM2", "org.spongycastle.jcajce.provider.asymmetric.ec.GMSignatureSpi$sm3WithSM2");
            final StringBuilder sb = new StringBuilder();
            sb.append("Alg.Alias.Signature.");
            sb.append(GMObjectIdentifiers.sm2sign_with_sm3);
            configurableProvider.addAlgorithm(sb.toString(), "SM3WITHSM2");
        }
    }
}
