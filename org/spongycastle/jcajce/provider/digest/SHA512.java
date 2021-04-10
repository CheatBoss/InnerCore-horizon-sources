package org.spongycastle.jcajce.provider.digest;

import org.spongycastle.crypto.digests.*;
import org.spongycastle.jcajce.provider.symmetric.util.*;
import org.spongycastle.crypto.*;
import org.spongycastle.jcajce.provider.config.*;
import org.spongycastle.asn1.nist.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.crypto.macs.*;

public class SHA512
{
    private SHA512() {
    }
    
    public static class Digest extends BCMessageDigest implements Cloneable
    {
        public Digest() {
            super(new SHA512Digest());
        }
        
        @Override
        public Object clone() throws CloneNotSupportedException {
            final Digest digest = (Digest)super.clone();
            digest.digest = new SHA512Digest((SHA512Digest)this.digest);
            return digest;
        }
    }
    
    public static class DigestT extends BCMessageDigest implements Cloneable
    {
        public DigestT(final int n) {
            super(new SHA512tDigest(n));
        }
        
        @Override
        public Object clone() throws CloneNotSupportedException {
            final DigestT digestT = (DigestT)super.clone();
            digestT.digest = new SHA512tDigest((SHA512tDigest)this.digest);
            return digestT;
        }
    }
    
    public static class DigestT224 extends DigestT
    {
        public DigestT224() {
            super(224);
        }
    }
    
    public static class DigestT256 extends DigestT
    {
        public DigestT256() {
            super(256);
        }
    }
    
    public static class HashMac extends BaseMac
    {
        public HashMac() {
            super(new HMac(new SHA512Digest()));
        }
    }
    
    public static class HashMacT224 extends BaseMac
    {
        public HashMacT224() {
            super(new HMac(new SHA512tDigest(224)));
        }
    }
    
    public static class HashMacT256 extends BaseMac
    {
        public HashMacT256() {
            super(new HMac(new SHA512tDigest(256)));
        }
    }
    
    public static class KeyGenerator extends BaseKeyGenerator
    {
        public KeyGenerator() {
            super("HMACSHA512", 512, new CipherKeyGenerator());
        }
    }
    
    public static class KeyGeneratorT224 extends BaseKeyGenerator
    {
        public KeyGeneratorT224() {
            super("HMACSHA512/224", 224, new CipherKeyGenerator());
        }
    }
    
    public static class KeyGeneratorT256 extends BaseKeyGenerator
    {
        public KeyGeneratorT256() {
            super("HMACSHA512/256", 256, new CipherKeyGenerator());
        }
    }
    
    public static class Mappings extends DigestAlgorithmProvider
    {
        private static final String PREFIX;
        
        static {
            PREFIX = SHA512.class.getName();
        }
        
        @Override
        public void configure(final ConfigurableProvider configurableProvider) {
            final StringBuilder sb = new StringBuilder();
            sb.append(Mappings.PREFIX);
            sb.append("$Digest");
            configurableProvider.addAlgorithm("MessageDigest.SHA-512", sb.toString());
            configurableProvider.addAlgorithm("Alg.Alias.MessageDigest.SHA512", "SHA-512");
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Alg.Alias.MessageDigest.");
            sb2.append(NISTObjectIdentifiers.id_sha512);
            configurableProvider.addAlgorithm(sb2.toString(), "SHA-512");
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(Mappings.PREFIX);
            sb3.append("$DigestT224");
            configurableProvider.addAlgorithm("MessageDigest.SHA-512/224", sb3.toString());
            configurableProvider.addAlgorithm("Alg.Alias.MessageDigest.SHA512/224", "SHA-512/224");
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Alg.Alias.MessageDigest.");
            sb4.append(NISTObjectIdentifiers.id_sha512_224);
            configurableProvider.addAlgorithm(sb4.toString(), "SHA-512/224");
            final StringBuilder sb5 = new StringBuilder();
            sb5.append(Mappings.PREFIX);
            sb5.append("$DigestT256");
            configurableProvider.addAlgorithm("MessageDigest.SHA-512/256", sb5.toString());
            configurableProvider.addAlgorithm("Alg.Alias.MessageDigest.SHA512256", "SHA-512/256");
            final StringBuilder sb6 = new StringBuilder();
            sb6.append("Alg.Alias.MessageDigest.");
            sb6.append(NISTObjectIdentifiers.id_sha512_256);
            configurableProvider.addAlgorithm(sb6.toString(), "SHA-512/256");
            final StringBuilder sb7 = new StringBuilder();
            sb7.append(Mappings.PREFIX);
            sb7.append("$OldSHA512");
            configurableProvider.addAlgorithm("Mac.OLDHMACSHA512", sb7.toString());
            final StringBuilder sb8 = new StringBuilder();
            sb8.append(Mappings.PREFIX);
            sb8.append("$HashMac");
            configurableProvider.addAlgorithm("Mac.PBEWITHHMACSHA512", sb8.toString());
            final StringBuilder sb9 = new StringBuilder();
            sb9.append(Mappings.PREFIX);
            sb9.append("$HashMac");
            final String string = sb9.toString();
            final StringBuilder sb10 = new StringBuilder();
            sb10.append(Mappings.PREFIX);
            sb10.append("$KeyGenerator");
            this.addHMACAlgorithm(configurableProvider, "SHA512", string, sb10.toString());
            this.addHMACAlias(configurableProvider, "SHA512", PKCSObjectIdentifiers.id_hmacWithSHA512);
            final StringBuilder sb11 = new StringBuilder();
            sb11.append(Mappings.PREFIX);
            sb11.append("$HashMacT224");
            final String string2 = sb11.toString();
            final StringBuilder sb12 = new StringBuilder();
            sb12.append(Mappings.PREFIX);
            sb12.append("$KeyGeneratorT224");
            this.addHMACAlgorithm(configurableProvider, "SHA512/224", string2, sb12.toString());
            final StringBuilder sb13 = new StringBuilder();
            sb13.append(Mappings.PREFIX);
            sb13.append("$HashMacT256");
            final String string3 = sb13.toString();
            final StringBuilder sb14 = new StringBuilder();
            sb14.append(Mappings.PREFIX);
            sb14.append("$KeyGeneratorT256");
            this.addHMACAlgorithm(configurableProvider, "SHA512/256", string3, sb14.toString());
        }
    }
    
    public static class OldSHA512 extends BaseMac
    {
        public OldSHA512() {
            super(new OldHMac(new SHA512Digest()));
        }
    }
}
