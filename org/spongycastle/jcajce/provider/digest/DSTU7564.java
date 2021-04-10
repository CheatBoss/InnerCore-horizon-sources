package org.spongycastle.jcajce.provider.digest;

import org.spongycastle.crypto.digests.*;
import org.spongycastle.crypto.macs.*;
import org.spongycastle.jcajce.provider.symmetric.util.*;
import org.spongycastle.crypto.*;
import org.spongycastle.jcajce.provider.config.*;
import org.spongycastle.asn1.ua.*;
import org.spongycastle.asn1.*;

public class DSTU7564
{
    private DSTU7564() {
    }
    
    public static class Digest256 extends DigestDSTU7564
    {
        public Digest256() {
            super(256);
        }
    }
    
    public static class Digest384 extends DigestDSTU7564
    {
        public Digest384() {
            super(384);
        }
    }
    
    public static class Digest512 extends DigestDSTU7564
    {
        public Digest512() {
            super(512);
        }
    }
    
    public static class DigestDSTU7564 extends BCMessageDigest implements Cloneable
    {
        public DigestDSTU7564(final int n) {
            super(new DSTU7564Digest(n));
        }
        
        @Override
        public Object clone() throws CloneNotSupportedException {
            final BCMessageDigest bcMessageDigest = (BCMessageDigest)super.clone();
            bcMessageDigest.digest = new DSTU7564Digest((DSTU7564Digest)this.digest);
            return bcMessageDigest;
        }
    }
    
    public static class HashMac256 extends BaseMac
    {
        public HashMac256() {
            super(new DSTU7564Mac(256));
        }
    }
    
    public static class HashMac384 extends BaseMac
    {
        public HashMac384() {
            super(new DSTU7564Mac(384));
        }
    }
    
    public static class HashMac512 extends BaseMac
    {
        public HashMac512() {
            super(new DSTU7564Mac(512));
        }
    }
    
    public static class KeyGenerator256 extends BaseKeyGenerator
    {
        public KeyGenerator256() {
            super("HMACDSTU7564-256", 256, new CipherKeyGenerator());
        }
    }
    
    public static class KeyGenerator384 extends BaseKeyGenerator
    {
        public KeyGenerator384() {
            super("HMACDSTU7564-384", 384, new CipherKeyGenerator());
        }
    }
    
    public static class KeyGenerator512 extends BaseKeyGenerator
    {
        public KeyGenerator512() {
            super("HMACDSTU7564-512", 512, new CipherKeyGenerator());
        }
    }
    
    public static class Mappings extends DigestAlgorithmProvider
    {
        private static final String PREFIX;
        
        static {
            PREFIX = DSTU7564.class.getName();
        }
        
        @Override
        public void configure(final ConfigurableProvider configurableProvider) {
            final StringBuilder sb = new StringBuilder();
            sb.append(Mappings.PREFIX);
            sb.append("$Digest256");
            configurableProvider.addAlgorithm("MessageDigest.DSTU7564-256", sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(Mappings.PREFIX);
            sb2.append("$Digest384");
            configurableProvider.addAlgorithm("MessageDigest.DSTU7564-384", sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(Mappings.PREFIX);
            sb3.append("$Digest512");
            configurableProvider.addAlgorithm("MessageDigest.DSTU7564-512", sb3.toString());
            final ASN1ObjectIdentifier dstu7564digest_256 = UAObjectIdentifiers.dstu7564digest_256;
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(Mappings.PREFIX);
            sb4.append("$Digest256");
            configurableProvider.addAlgorithm("MessageDigest", dstu7564digest_256, sb4.toString());
            final ASN1ObjectIdentifier dstu7564digest_257 = UAObjectIdentifiers.dstu7564digest_384;
            final StringBuilder sb5 = new StringBuilder();
            sb5.append(Mappings.PREFIX);
            sb5.append("$Digest384");
            configurableProvider.addAlgorithm("MessageDigest", dstu7564digest_257, sb5.toString());
            final ASN1ObjectIdentifier dstu7564digest_258 = UAObjectIdentifiers.dstu7564digest_512;
            final StringBuilder sb6 = new StringBuilder();
            sb6.append(Mappings.PREFIX);
            sb6.append("$Digest512");
            configurableProvider.addAlgorithm("MessageDigest", dstu7564digest_258, sb6.toString());
            final StringBuilder sb7 = new StringBuilder();
            sb7.append(Mappings.PREFIX);
            sb7.append("$HashMac256");
            final String string = sb7.toString();
            final StringBuilder sb8 = new StringBuilder();
            sb8.append(Mappings.PREFIX);
            sb8.append("$KeyGenerator256");
            this.addHMACAlgorithm(configurableProvider, "DSTU7564-256", string, sb8.toString());
            final StringBuilder sb9 = new StringBuilder();
            sb9.append(Mappings.PREFIX);
            sb9.append("$HashMac384");
            final String string2 = sb9.toString();
            final StringBuilder sb10 = new StringBuilder();
            sb10.append(Mappings.PREFIX);
            sb10.append("$KeyGenerator384");
            this.addHMACAlgorithm(configurableProvider, "DSTU7564-384", string2, sb10.toString());
            final StringBuilder sb11 = new StringBuilder();
            sb11.append(Mappings.PREFIX);
            sb11.append("$HashMac512");
            final String string3 = sb11.toString();
            final StringBuilder sb12 = new StringBuilder();
            sb12.append(Mappings.PREFIX);
            sb12.append("$KeyGenerator512");
            this.addHMACAlgorithm(configurableProvider, "DSTU7564-512", string3, sb12.toString());
            this.addHMACAlias(configurableProvider, "DSTU7564-256", UAObjectIdentifiers.dstu7564mac_256);
            this.addHMACAlias(configurableProvider, "DSTU7564-384", UAObjectIdentifiers.dstu7564mac_384);
            this.addHMACAlias(configurableProvider, "DSTU7564-512", UAObjectIdentifiers.dstu7564mac_512);
        }
    }
}
