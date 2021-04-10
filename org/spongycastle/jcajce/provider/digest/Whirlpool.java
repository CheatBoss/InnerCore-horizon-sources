package org.spongycastle.jcajce.provider.digest;

import org.spongycastle.crypto.digests.*;
import org.spongycastle.crypto.macs.*;
import org.spongycastle.jcajce.provider.symmetric.util.*;
import org.spongycastle.crypto.*;
import org.spongycastle.jcajce.provider.config.*;

public class Whirlpool
{
    private Whirlpool() {
    }
    
    public static class Digest extends BCMessageDigest implements Cloneable
    {
        public Digest() {
            super(new WhirlpoolDigest());
        }
        
        @Override
        public Object clone() throws CloneNotSupportedException {
            final Digest digest = (Digest)super.clone();
            digest.digest = new WhirlpoolDigest((WhirlpoolDigest)this.digest);
            return digest;
        }
    }
    
    public static class HashMac extends BaseMac
    {
        public HashMac() {
            super(new HMac(new WhirlpoolDigest()));
        }
    }
    
    public static class KeyGenerator extends BaseKeyGenerator
    {
        public KeyGenerator() {
            super("HMACWHIRLPOOL", 512, new CipherKeyGenerator());
        }
    }
    
    public static class Mappings extends DigestAlgorithmProvider
    {
        private static final String PREFIX;
        
        static {
            PREFIX = Whirlpool.class.getName();
        }
        
        @Override
        public void configure(final ConfigurableProvider configurableProvider) {
            final StringBuilder sb = new StringBuilder();
            sb.append(Mappings.PREFIX);
            sb.append("$Digest");
            configurableProvider.addAlgorithm("MessageDigest.WHIRLPOOL", sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(Mappings.PREFIX);
            sb2.append("$HashMac");
            final String string = sb2.toString();
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(Mappings.PREFIX);
            sb3.append("$KeyGenerator");
            this.addHMACAlgorithm(configurableProvider, "WHIRLPOOL", string, sb3.toString());
        }
    }
}
