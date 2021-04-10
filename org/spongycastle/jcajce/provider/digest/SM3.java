package org.spongycastle.jcajce.provider.digest;

import org.spongycastle.crypto.digests.*;
import org.spongycastle.crypto.*;
import org.spongycastle.jcajce.provider.config.*;

public class SM3
{
    private SM3() {
    }
    
    public static class Digest extends BCMessageDigest implements Cloneable
    {
        public Digest() {
            super(new SM3Digest());
        }
        
        @Override
        public Object clone() throws CloneNotSupportedException {
            final Digest digest = (Digest)super.clone();
            digest.digest = new SM3Digest((SM3Digest)this.digest);
            return digest;
        }
    }
    
    public static class Mappings extends DigestAlgorithmProvider
    {
        private static final String PREFIX;
        
        static {
            PREFIX = SM3.class.getName();
        }
        
        @Override
        public void configure(final ConfigurableProvider configurableProvider) {
            final StringBuilder sb = new StringBuilder();
            sb.append(Mappings.PREFIX);
            sb.append("$Digest");
            configurableProvider.addAlgorithm("MessageDigest.SM3", sb.toString());
            configurableProvider.addAlgorithm("Alg.Alias.MessageDigest.SM3", "SM3");
            configurableProvider.addAlgorithm("Alg.Alias.MessageDigest.1.2.156.197.1.401", "SM3");
        }
    }
}
