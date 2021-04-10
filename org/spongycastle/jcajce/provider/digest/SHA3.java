package org.spongycastle.jcajce.provider.digest;

import org.spongycastle.crypto.digests.*;
import org.spongycastle.crypto.macs.*;
import org.spongycastle.jcajce.provider.symmetric.util.*;
import org.spongycastle.crypto.*;
import org.spongycastle.jcajce.provider.config.*;
import org.spongycastle.asn1.nist.*;
import org.spongycastle.asn1.*;

public class SHA3
{
    private SHA3() {
    }
    
    public static class Digest224 extends DigestSHA3
    {
        public Digest224() {
            super(224);
        }
    }
    
    public static class Digest256 extends DigestSHA3
    {
        public Digest256() {
            super(256);
        }
    }
    
    public static class Digest384 extends DigestSHA3
    {
        public Digest384() {
            super(384);
        }
    }
    
    public static class Digest512 extends DigestSHA3
    {
        public Digest512() {
            super(512);
        }
    }
    
    public static class DigestSHA3 extends BCMessageDigest implements Cloneable
    {
        public DigestSHA3(final int n) {
            super(new SHA3Digest(n));
        }
        
        @Override
        public Object clone() throws CloneNotSupportedException {
            final BCMessageDigest bcMessageDigest = (BCMessageDigest)super.clone();
            bcMessageDigest.digest = new SHA3Digest((SHA3Digest)this.digest);
            return bcMessageDigest;
        }
    }
    
    public static class HashMac224 extends HashMacSHA3
    {
        public HashMac224() {
            super(224);
        }
    }
    
    public static class HashMac256 extends HashMacSHA3
    {
        public HashMac256() {
            super(256);
        }
    }
    
    public static class HashMac384 extends HashMacSHA3
    {
        public HashMac384() {
            super(384);
        }
    }
    
    public static class HashMac512 extends HashMacSHA3
    {
        public HashMac512() {
            super(512);
        }
    }
    
    public static class HashMacSHA3 extends BaseMac
    {
        public HashMacSHA3(final int n) {
            super(new HMac(new SHA3Digest(n)));
        }
    }
    
    public static class KeyGenerator224 extends KeyGeneratorSHA3
    {
        public KeyGenerator224() {
            super(224);
        }
    }
    
    public static class KeyGenerator256 extends KeyGeneratorSHA3
    {
        public KeyGenerator256() {
            super(256);
        }
    }
    
    public static class KeyGenerator384 extends KeyGeneratorSHA3
    {
        public KeyGenerator384() {
            super(384);
        }
    }
    
    public static class KeyGenerator512 extends KeyGeneratorSHA3
    {
        public KeyGenerator512() {
            super(512);
        }
    }
    
    public static class KeyGeneratorSHA3 extends BaseKeyGenerator
    {
        public KeyGeneratorSHA3(final int n) {
            final StringBuilder sb = new StringBuilder();
            sb.append("HMACSHA3-");
            sb.append(n);
            super(sb.toString(), n, new CipherKeyGenerator());
        }
    }
    
    public static class Mappings extends DigestAlgorithmProvider
    {
        private static final String PREFIX;
        
        static {
            PREFIX = SHA3.class.getName();
        }
        
        @Override
        public void configure(final ConfigurableProvider configurableProvider) {
            final StringBuilder sb = new StringBuilder();
            sb.append(Mappings.PREFIX);
            sb.append("$Digest224");
            configurableProvider.addAlgorithm("MessageDigest.SHA3-224", sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(Mappings.PREFIX);
            sb2.append("$Digest256");
            configurableProvider.addAlgorithm("MessageDigest.SHA3-256", sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(Mappings.PREFIX);
            sb3.append("$Digest384");
            configurableProvider.addAlgorithm("MessageDigest.SHA3-384", sb3.toString());
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(Mappings.PREFIX);
            sb4.append("$Digest512");
            configurableProvider.addAlgorithm("MessageDigest.SHA3-512", sb4.toString());
            final ASN1ObjectIdentifier id_sha3_224 = NISTObjectIdentifiers.id_sha3_224;
            final StringBuilder sb5 = new StringBuilder();
            sb5.append(Mappings.PREFIX);
            sb5.append("$Digest224");
            configurableProvider.addAlgorithm("MessageDigest", id_sha3_224, sb5.toString());
            final ASN1ObjectIdentifier id_sha3_225 = NISTObjectIdentifiers.id_sha3_256;
            final StringBuilder sb6 = new StringBuilder();
            sb6.append(Mappings.PREFIX);
            sb6.append("$Digest256");
            configurableProvider.addAlgorithm("MessageDigest", id_sha3_225, sb6.toString());
            final ASN1ObjectIdentifier id_sha3_226 = NISTObjectIdentifiers.id_sha3_384;
            final StringBuilder sb7 = new StringBuilder();
            sb7.append(Mappings.PREFIX);
            sb7.append("$Digest384");
            configurableProvider.addAlgorithm("MessageDigest", id_sha3_226, sb7.toString());
            final ASN1ObjectIdentifier id_sha3_227 = NISTObjectIdentifiers.id_sha3_512;
            final StringBuilder sb8 = new StringBuilder();
            sb8.append(Mappings.PREFIX);
            sb8.append("$Digest512");
            configurableProvider.addAlgorithm("MessageDigest", id_sha3_227, sb8.toString());
            final StringBuilder sb9 = new StringBuilder();
            sb9.append(Mappings.PREFIX);
            sb9.append("$HashMac224");
            final String string = sb9.toString();
            final StringBuilder sb10 = new StringBuilder();
            sb10.append(Mappings.PREFIX);
            sb10.append("$KeyGenerator224");
            this.addHMACAlgorithm(configurableProvider, "SHA3-224", string, sb10.toString());
            this.addHMACAlias(configurableProvider, "SHA3-224", NISTObjectIdentifiers.id_hmacWithSHA3_224);
            final StringBuilder sb11 = new StringBuilder();
            sb11.append(Mappings.PREFIX);
            sb11.append("$HashMac256");
            final String string2 = sb11.toString();
            final StringBuilder sb12 = new StringBuilder();
            sb12.append(Mappings.PREFIX);
            sb12.append("$KeyGenerator256");
            this.addHMACAlgorithm(configurableProvider, "SHA3-256", string2, sb12.toString());
            this.addHMACAlias(configurableProvider, "SHA3-256", NISTObjectIdentifiers.id_hmacWithSHA3_256);
            final StringBuilder sb13 = new StringBuilder();
            sb13.append(Mappings.PREFIX);
            sb13.append("$HashMac384");
            final String string3 = sb13.toString();
            final StringBuilder sb14 = new StringBuilder();
            sb14.append(Mappings.PREFIX);
            sb14.append("$KeyGenerator384");
            this.addHMACAlgorithm(configurableProvider, "SHA3-384", string3, sb14.toString());
            this.addHMACAlias(configurableProvider, "SHA3-384", NISTObjectIdentifiers.id_hmacWithSHA3_384);
            final StringBuilder sb15 = new StringBuilder();
            sb15.append(Mappings.PREFIX);
            sb15.append("$HashMac512");
            final String string4 = sb15.toString();
            final StringBuilder sb16 = new StringBuilder();
            sb16.append(Mappings.PREFIX);
            sb16.append("$KeyGenerator512");
            this.addHMACAlgorithm(configurableProvider, "SHA3-512", string4, sb16.toString());
            this.addHMACAlias(configurableProvider, "SHA3-512", NISTObjectIdentifiers.id_hmacWithSHA3_512);
        }
    }
}
