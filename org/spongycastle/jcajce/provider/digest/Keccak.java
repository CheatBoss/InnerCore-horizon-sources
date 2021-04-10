package org.spongycastle.jcajce.provider.digest;

import org.spongycastle.crypto.digests.*;
import org.spongycastle.crypto.macs.*;
import org.spongycastle.jcajce.provider.symmetric.util.*;
import org.spongycastle.crypto.*;
import org.spongycastle.jcajce.provider.config.*;

public class Keccak
{
    private Keccak() {
    }
    
    public static class Digest224 extends DigestKeccak
    {
        public Digest224() {
            super(224);
        }
    }
    
    public static class Digest256 extends DigestKeccak
    {
        public Digest256() {
            super(256);
        }
    }
    
    public static class Digest288 extends DigestKeccak
    {
        public Digest288() {
            super(288);
        }
    }
    
    public static class Digest384 extends DigestKeccak
    {
        public Digest384() {
            super(384);
        }
    }
    
    public static class Digest512 extends DigestKeccak
    {
        public Digest512() {
            super(512);
        }
    }
    
    public static class DigestKeccak extends BCMessageDigest implements Cloneable
    {
        public DigestKeccak(final int n) {
            super(new KeccakDigest(n));
        }
        
        @Override
        public Object clone() throws CloneNotSupportedException {
            final BCMessageDigest bcMessageDigest = (BCMessageDigest)super.clone();
            bcMessageDigest.digest = new KeccakDigest((KeccakDigest)this.digest);
            return bcMessageDigest;
        }
    }
    
    public static class HashMac224 extends BaseMac
    {
        public HashMac224() {
            super(new HMac(new KeccakDigest(224)));
        }
    }
    
    public static class HashMac256 extends BaseMac
    {
        public HashMac256() {
            super(new HMac(new KeccakDigest(256)));
        }
    }
    
    public static class HashMac288 extends BaseMac
    {
        public HashMac288() {
            super(new HMac(new KeccakDigest(288)));
        }
    }
    
    public static class HashMac384 extends BaseMac
    {
        public HashMac384() {
            super(new HMac(new KeccakDigest(384)));
        }
    }
    
    public static class HashMac512 extends BaseMac
    {
        public HashMac512() {
            super(new HMac(new KeccakDigest(512)));
        }
    }
    
    public static class KeyGenerator224 extends BaseKeyGenerator
    {
        public KeyGenerator224() {
            super("HMACKECCAK224", 224, new CipherKeyGenerator());
        }
    }
    
    public static class KeyGenerator256 extends BaseKeyGenerator
    {
        public KeyGenerator256() {
            super("HMACKECCAK256", 256, new CipherKeyGenerator());
        }
    }
    
    public static class KeyGenerator288 extends BaseKeyGenerator
    {
        public KeyGenerator288() {
            super("HMACKECCAK288", 288, new CipherKeyGenerator());
        }
    }
    
    public static class KeyGenerator384 extends BaseKeyGenerator
    {
        public KeyGenerator384() {
            super("HMACKECCAK384", 384, new CipherKeyGenerator());
        }
    }
    
    public static class KeyGenerator512 extends BaseKeyGenerator
    {
        public KeyGenerator512() {
            super("HMACKECCAK512", 512, new CipherKeyGenerator());
        }
    }
    
    public static class Mappings extends DigestAlgorithmProvider
    {
        private static final String PREFIX;
        
        static {
            PREFIX = Keccak.class.getName();
        }
        
        @Override
        public void configure(final ConfigurableProvider configurableProvider) {
            final StringBuilder sb = new StringBuilder();
            sb.append(Mappings.PREFIX);
            sb.append("$Digest224");
            configurableProvider.addAlgorithm("MessageDigest.KECCAK-224", sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(Mappings.PREFIX);
            sb2.append("$Digest288");
            configurableProvider.addAlgorithm("MessageDigest.KECCAK-288", sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(Mappings.PREFIX);
            sb3.append("$Digest256");
            configurableProvider.addAlgorithm("MessageDigest.KECCAK-256", sb3.toString());
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(Mappings.PREFIX);
            sb4.append("$Digest384");
            configurableProvider.addAlgorithm("MessageDigest.KECCAK-384", sb4.toString());
            final StringBuilder sb5 = new StringBuilder();
            sb5.append(Mappings.PREFIX);
            sb5.append("$Digest512");
            configurableProvider.addAlgorithm("MessageDigest.KECCAK-512", sb5.toString());
            final StringBuilder sb6 = new StringBuilder();
            sb6.append(Mappings.PREFIX);
            sb6.append("$HashMac224");
            final String string = sb6.toString();
            final StringBuilder sb7 = new StringBuilder();
            sb7.append(Mappings.PREFIX);
            sb7.append("$KeyGenerator224");
            this.addHMACAlgorithm(configurableProvider, "KECCAK224", string, sb7.toString());
            final StringBuilder sb8 = new StringBuilder();
            sb8.append(Mappings.PREFIX);
            sb8.append("$HashMac256");
            final String string2 = sb8.toString();
            final StringBuilder sb9 = new StringBuilder();
            sb9.append(Mappings.PREFIX);
            sb9.append("$KeyGenerator256");
            this.addHMACAlgorithm(configurableProvider, "KECCAK256", string2, sb9.toString());
            final StringBuilder sb10 = new StringBuilder();
            sb10.append(Mappings.PREFIX);
            sb10.append("$HashMac288");
            final String string3 = sb10.toString();
            final StringBuilder sb11 = new StringBuilder();
            sb11.append(Mappings.PREFIX);
            sb11.append("$KeyGenerator288");
            this.addHMACAlgorithm(configurableProvider, "KECCAK288", string3, sb11.toString());
            final StringBuilder sb12 = new StringBuilder();
            sb12.append(Mappings.PREFIX);
            sb12.append("$HashMac384");
            final String string4 = sb12.toString();
            final StringBuilder sb13 = new StringBuilder();
            sb13.append(Mappings.PREFIX);
            sb13.append("$KeyGenerator384");
            this.addHMACAlgorithm(configurableProvider, "KECCAK384", string4, sb13.toString());
            final StringBuilder sb14 = new StringBuilder();
            sb14.append(Mappings.PREFIX);
            sb14.append("$HashMac512");
            final String string5 = sb14.toString();
            final StringBuilder sb15 = new StringBuilder();
            sb15.append(Mappings.PREFIX);
            sb15.append("$KeyGenerator512");
            this.addHMACAlgorithm(configurableProvider, "KECCAK512", string5, sb15.toString());
        }
    }
}
