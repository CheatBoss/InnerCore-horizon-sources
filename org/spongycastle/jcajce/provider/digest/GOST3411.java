package org.spongycastle.jcajce.provider.digest;

import org.spongycastle.crypto.digests.*;
import org.spongycastle.crypto.macs.*;
import org.spongycastle.crypto.*;
import org.spongycastle.jcajce.provider.config.*;
import org.spongycastle.asn1.cryptopro.*;
import org.spongycastle.asn1.rosstandart.*;
import org.spongycastle.jcajce.provider.symmetric.util.*;
import org.spongycastle.asn1.*;

public class GOST3411
{
    private GOST3411() {
    }
    
    public static class Digest extends BCMessageDigest implements Cloneable
    {
        public Digest() {
            super(new GOST3411Digest());
        }
        
        @Override
        public Object clone() throws CloneNotSupportedException {
            final Digest digest = (Digest)super.clone();
            digest.digest = new GOST3411Digest((GOST3411Digest)this.digest);
            return digest;
        }
    }
    
    public static class Digest2012_256 extends BCMessageDigest implements Cloneable
    {
        public Digest2012_256() {
            super(new GOST3411_2012_256Digest());
        }
        
        @Override
        public Object clone() throws CloneNotSupportedException {
            final Digest2012_256 digest2012_256 = (Digest2012_256)super.clone();
            digest2012_256.digest = new GOST3411_2012_256Digest((GOST3411_2012_256Digest)this.digest);
            return digest2012_256;
        }
    }
    
    public static class Digest2012_512 extends BCMessageDigest implements Cloneable
    {
        public Digest2012_512() {
            super(new GOST3411_2012_512Digest());
        }
        
        @Override
        public Object clone() throws CloneNotSupportedException {
            final Digest2012_512 digest2012_512 = (Digest2012_512)super.clone();
            digest2012_512.digest = new GOST3411_2012_512Digest((GOST3411_2012_512Digest)this.digest);
            return digest2012_512;
        }
    }
    
    public static class HashMac extends BaseMac
    {
        public HashMac() {
            super(new HMac(new GOST3411Digest()));
        }
    }
    
    public static class HashMac2012_256 extends BaseMac
    {
        public HashMac2012_256() {
            super(new HMac(new GOST3411_2012_256Digest()));
        }
    }
    
    public static class HashMac2012_512 extends BaseMac
    {
        public HashMac2012_512() {
            super(new HMac(new GOST3411_2012_512Digest()));
        }
    }
    
    public static class KeyGenerator extends BaseKeyGenerator
    {
        public KeyGenerator() {
            super("HMACGOST3411", 256, new CipherKeyGenerator());
        }
    }
    
    public static class KeyGenerator2012_256 extends BaseKeyGenerator
    {
        public KeyGenerator2012_256() {
            super("HMACGOST3411", 256, new CipherKeyGenerator());
        }
    }
    
    public static class KeyGenerator2012_512 extends BaseKeyGenerator
    {
        public KeyGenerator2012_512() {
            super("HMACGOST3411", 512, new CipherKeyGenerator());
        }
    }
    
    public static class Mappings extends DigestAlgorithmProvider
    {
        private static final String PREFIX;
        
        static {
            PREFIX = GOST3411.class.getName();
        }
        
        @Override
        public void configure(final ConfigurableProvider configurableProvider) {
            final StringBuilder sb = new StringBuilder();
            sb.append(Mappings.PREFIX);
            sb.append("$Digest");
            configurableProvider.addAlgorithm("MessageDigest.GOST3411", sb.toString());
            configurableProvider.addAlgorithm("Alg.Alias.MessageDigest.GOST", "GOST3411");
            configurableProvider.addAlgorithm("Alg.Alias.MessageDigest.GOST-3411", "GOST3411");
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Alg.Alias.MessageDigest.");
            sb2.append(CryptoProObjectIdentifiers.gostR3411);
            configurableProvider.addAlgorithm(sb2.toString(), "GOST3411");
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(Mappings.PREFIX);
            sb3.append("$HashMac");
            final String string = sb3.toString();
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(Mappings.PREFIX);
            sb4.append("$KeyGenerator");
            this.addHMACAlgorithm(configurableProvider, "GOST3411", string, sb4.toString());
            this.addHMACAlias(configurableProvider, "GOST3411", CryptoProObjectIdentifiers.gostR3411);
            final StringBuilder sb5 = new StringBuilder();
            sb5.append(Mappings.PREFIX);
            sb5.append("$Digest2012_256");
            configurableProvider.addAlgorithm("MessageDigest.GOST3411-2012-256", sb5.toString());
            configurableProvider.addAlgorithm("Alg.Alias.MessageDigest.GOST-2012-256", "GOST3411-2012-256");
            configurableProvider.addAlgorithm("Alg.Alias.MessageDigest.GOST-3411-2012-256", "GOST3411-2012-256");
            final StringBuilder sb6 = new StringBuilder();
            sb6.append("Alg.Alias.MessageDigest.");
            sb6.append(RosstandartObjectIdentifiers.id_tc26_gost_3411_12_256);
            configurableProvider.addAlgorithm(sb6.toString(), "GOST3411-2012-256");
            final StringBuilder sb7 = new StringBuilder();
            sb7.append(Mappings.PREFIX);
            sb7.append("$HashMac2012_256");
            final String string2 = sb7.toString();
            final StringBuilder sb8 = new StringBuilder();
            sb8.append(Mappings.PREFIX);
            sb8.append("$KeyGenerator2012_256");
            this.addHMACAlgorithm(configurableProvider, "GOST3411-2012-256", string2, sb8.toString());
            this.addHMACAlias(configurableProvider, "GOST3411-2012-256", RosstandartObjectIdentifiers.id_tc26_hmac_gost_3411_12_256);
            final StringBuilder sb9 = new StringBuilder();
            sb9.append(Mappings.PREFIX);
            sb9.append("$Digest2012_512");
            configurableProvider.addAlgorithm("MessageDigest.GOST3411-2012-512", sb9.toString());
            configurableProvider.addAlgorithm("Alg.Alias.MessageDigest.GOST-2012-512", "GOST3411-2012-512");
            configurableProvider.addAlgorithm("Alg.Alias.MessageDigest.GOST-3411-2012-512", "GOST3411-2012-512");
            final StringBuilder sb10 = new StringBuilder();
            sb10.append("Alg.Alias.MessageDigest.");
            sb10.append(RosstandartObjectIdentifiers.id_tc26_gost_3411_12_512);
            configurableProvider.addAlgorithm(sb10.toString(), "GOST3411-2012-512");
            final StringBuilder sb11 = new StringBuilder();
            sb11.append(Mappings.PREFIX);
            sb11.append("$HashMac2012_512");
            final String string3 = sb11.toString();
            final StringBuilder sb12 = new StringBuilder();
            sb12.append(Mappings.PREFIX);
            sb12.append("$KeyGenerator2012_512");
            this.addHMACAlgorithm(configurableProvider, "GOST3411-2012-512", string3, sb12.toString());
            this.addHMACAlias(configurableProvider, "GOST3411-2012-512", RosstandartObjectIdentifiers.id_tc26_hmac_gost_3411_12_512);
            final StringBuilder sb13 = new StringBuilder();
            sb13.append(Mappings.PREFIX);
            sb13.append("$PBEWithMacKeyFactory");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBEWITHHMACGOST3411", sb13.toString());
            final StringBuilder sb14 = new StringBuilder();
            sb14.append("Alg.Alias.SecretKeyFactory.");
            sb14.append(CryptoProObjectIdentifiers.gostR3411);
            configurableProvider.addAlgorithm(sb14.toString(), "PBEWITHHMACGOST3411");
        }
    }
    
    public static class PBEWithMacKeyFactory extends PBESecretKeyFactory
    {
        public PBEWithMacKeyFactory() {
            super("PBEwithHmacGOST3411", null, false, 2, 6, 256, 0);
        }
    }
}
