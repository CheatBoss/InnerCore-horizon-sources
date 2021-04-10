package org.spongycastle.jcajce.provider.digest;

import org.spongycastle.crypto.digests.*;
import org.spongycastle.crypto.*;
import org.spongycastle.jcajce.provider.config.*;
import org.spongycastle.asn1.misc.*;

public class Blake2b
{
    private Blake2b() {
    }
    
    public static class Blake2b160 extends BCMessageDigest implements Cloneable
    {
        public Blake2b160() {
            super(new Blake2bDigest(160));
        }
        
        @Override
        public Object clone() throws CloneNotSupportedException {
            final Blake2b160 blake2b160 = (Blake2b160)super.clone();
            blake2b160.digest = new Blake2bDigest((Blake2bDigest)this.digest);
            return blake2b160;
        }
    }
    
    public static class Blake2b256 extends BCMessageDigest implements Cloneable
    {
        public Blake2b256() {
            super(new Blake2bDigest(256));
        }
        
        @Override
        public Object clone() throws CloneNotSupportedException {
            final Blake2b256 blake2b256 = (Blake2b256)super.clone();
            blake2b256.digest = new Blake2bDigest((Blake2bDigest)this.digest);
            return blake2b256;
        }
    }
    
    public static class Blake2b384 extends BCMessageDigest implements Cloneable
    {
        public Blake2b384() {
            super(new Blake2bDigest(384));
        }
        
        @Override
        public Object clone() throws CloneNotSupportedException {
            final Blake2b384 blake2b384 = (Blake2b384)super.clone();
            blake2b384.digest = new Blake2bDigest((Blake2bDigest)this.digest);
            return blake2b384;
        }
    }
    
    public static class Blake2b512 extends BCMessageDigest implements Cloneable
    {
        public Blake2b512() {
            super(new Blake2bDigest(512));
        }
        
        @Override
        public Object clone() throws CloneNotSupportedException {
            final Blake2b512 blake2b512 = (Blake2b512)super.clone();
            blake2b512.digest = new Blake2bDigest((Blake2bDigest)this.digest);
            return blake2b512;
        }
    }
    
    public static class Mappings extends DigestAlgorithmProvider
    {
        private static final String PREFIX;
        
        static {
            PREFIX = Blake2b.class.getName();
        }
        
        @Override
        public void configure(final ConfigurableProvider configurableProvider) {
            final StringBuilder sb = new StringBuilder();
            sb.append(Mappings.PREFIX);
            sb.append("$Blake2b512");
            configurableProvider.addAlgorithm("MessageDigest.BLAKE2B-512", sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Alg.Alias.MessageDigest.");
            sb2.append(MiscObjectIdentifiers.id_blake2b512);
            configurableProvider.addAlgorithm(sb2.toString(), "BLAKE2B-512");
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(Mappings.PREFIX);
            sb3.append("$Blake2b384");
            configurableProvider.addAlgorithm("MessageDigest.BLAKE2B-384", sb3.toString());
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Alg.Alias.MessageDigest.");
            sb4.append(MiscObjectIdentifiers.id_blake2b384);
            configurableProvider.addAlgorithm(sb4.toString(), "BLAKE2B-384");
            final StringBuilder sb5 = new StringBuilder();
            sb5.append(Mappings.PREFIX);
            sb5.append("$Blake2b256");
            configurableProvider.addAlgorithm("MessageDigest.BLAKE2B-256", sb5.toString());
            final StringBuilder sb6 = new StringBuilder();
            sb6.append("Alg.Alias.MessageDigest.");
            sb6.append(MiscObjectIdentifiers.id_blake2b256);
            configurableProvider.addAlgorithm(sb6.toString(), "BLAKE2B-256");
            final StringBuilder sb7 = new StringBuilder();
            sb7.append(Mappings.PREFIX);
            sb7.append("$Blake2b160");
            configurableProvider.addAlgorithm("MessageDigest.BLAKE2B-160", sb7.toString());
            final StringBuilder sb8 = new StringBuilder();
            sb8.append("Alg.Alias.MessageDigest.");
            sb8.append(MiscObjectIdentifiers.id_blake2b160);
            configurableProvider.addAlgorithm(sb8.toString(), "BLAKE2B-160");
        }
    }
}
