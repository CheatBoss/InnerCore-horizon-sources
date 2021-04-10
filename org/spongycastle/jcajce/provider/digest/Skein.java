package org.spongycastle.jcajce.provider.digest;

import org.spongycastle.crypto.digests.*;
import org.spongycastle.jcajce.provider.symmetric.util.*;
import org.spongycastle.crypto.*;
import org.spongycastle.jcajce.provider.config.*;
import org.spongycastle.crypto.macs.*;

public class Skein
{
    private Skein() {
    }
    
    public static class DigestSkein1024 extends BCMessageDigest implements Cloneable
    {
        public DigestSkein1024(final int n) {
            super(new SkeinDigest(1024, n));
        }
        
        @Override
        public Object clone() throws CloneNotSupportedException {
            final BCMessageDigest bcMessageDigest = (BCMessageDigest)super.clone();
            bcMessageDigest.digest = new SkeinDigest((SkeinDigest)this.digest);
            return bcMessageDigest;
        }
    }
    
    public static class DigestSkein256 extends BCMessageDigest implements Cloneable
    {
        public DigestSkein256(final int n) {
            super(new SkeinDigest(256, n));
        }
        
        @Override
        public Object clone() throws CloneNotSupportedException {
            final BCMessageDigest bcMessageDigest = (BCMessageDigest)super.clone();
            bcMessageDigest.digest = new SkeinDigest((SkeinDigest)this.digest);
            return bcMessageDigest;
        }
    }
    
    public static class DigestSkein512 extends BCMessageDigest implements Cloneable
    {
        public DigestSkein512(final int n) {
            super(new SkeinDigest(512, n));
        }
        
        @Override
        public Object clone() throws CloneNotSupportedException {
            final BCMessageDigest bcMessageDigest = (BCMessageDigest)super.clone();
            bcMessageDigest.digest = new SkeinDigest((SkeinDigest)this.digest);
            return bcMessageDigest;
        }
    }
    
    public static class Digest_1024_1024 extends DigestSkein1024
    {
        public Digest_1024_1024() {
            super(1024);
        }
    }
    
    public static class Digest_1024_384 extends DigestSkein1024
    {
        public Digest_1024_384() {
            super(384);
        }
    }
    
    public static class Digest_1024_512 extends DigestSkein1024
    {
        public Digest_1024_512() {
            super(512);
        }
    }
    
    public static class Digest_256_128 extends DigestSkein256
    {
        public Digest_256_128() {
            super(128);
        }
    }
    
    public static class Digest_256_160 extends DigestSkein256
    {
        public Digest_256_160() {
            super(160);
        }
    }
    
    public static class Digest_256_224 extends DigestSkein256
    {
        public Digest_256_224() {
            super(224);
        }
    }
    
    public static class Digest_256_256 extends DigestSkein256
    {
        public Digest_256_256() {
            super(256);
        }
    }
    
    public static class Digest_512_128 extends DigestSkein512
    {
        public Digest_512_128() {
            super(128);
        }
    }
    
    public static class Digest_512_160 extends DigestSkein512
    {
        public Digest_512_160() {
            super(160);
        }
    }
    
    public static class Digest_512_224 extends DigestSkein512
    {
        public Digest_512_224() {
            super(224);
        }
    }
    
    public static class Digest_512_256 extends DigestSkein512
    {
        public Digest_512_256() {
            super(256);
        }
    }
    
    public static class Digest_512_384 extends DigestSkein512
    {
        public Digest_512_384() {
            super(384);
        }
    }
    
    public static class Digest_512_512 extends DigestSkein512
    {
        public Digest_512_512() {
            super(512);
        }
    }
    
    public static class HMacKeyGenerator_1024_1024 extends BaseKeyGenerator
    {
        public HMacKeyGenerator_1024_1024() {
            super("HMACSkein-1024-1024", 1024, new CipherKeyGenerator());
        }
    }
    
    public static class HMacKeyGenerator_1024_384 extends BaseKeyGenerator
    {
        public HMacKeyGenerator_1024_384() {
            super("HMACSkein-1024-384", 384, new CipherKeyGenerator());
        }
    }
    
    public static class HMacKeyGenerator_1024_512 extends BaseKeyGenerator
    {
        public HMacKeyGenerator_1024_512() {
            super("HMACSkein-1024-512", 512, new CipherKeyGenerator());
        }
    }
    
    public static class HMacKeyGenerator_256_128 extends BaseKeyGenerator
    {
        public HMacKeyGenerator_256_128() {
            super("HMACSkein-256-128", 128, new CipherKeyGenerator());
        }
    }
    
    public static class HMacKeyGenerator_256_160 extends BaseKeyGenerator
    {
        public HMacKeyGenerator_256_160() {
            super("HMACSkein-256-160", 160, new CipherKeyGenerator());
        }
    }
    
    public static class HMacKeyGenerator_256_224 extends BaseKeyGenerator
    {
        public HMacKeyGenerator_256_224() {
            super("HMACSkein-256-224", 224, new CipherKeyGenerator());
        }
    }
    
    public static class HMacKeyGenerator_256_256 extends BaseKeyGenerator
    {
        public HMacKeyGenerator_256_256() {
            super("HMACSkein-256-256", 256, new CipherKeyGenerator());
        }
    }
    
    public static class HMacKeyGenerator_512_128 extends BaseKeyGenerator
    {
        public HMacKeyGenerator_512_128() {
            super("HMACSkein-512-128", 128, new CipherKeyGenerator());
        }
    }
    
    public static class HMacKeyGenerator_512_160 extends BaseKeyGenerator
    {
        public HMacKeyGenerator_512_160() {
            super("HMACSkein-512-160", 160, new CipherKeyGenerator());
        }
    }
    
    public static class HMacKeyGenerator_512_224 extends BaseKeyGenerator
    {
        public HMacKeyGenerator_512_224() {
            super("HMACSkein-512-224", 224, new CipherKeyGenerator());
        }
    }
    
    public static class HMacKeyGenerator_512_256 extends BaseKeyGenerator
    {
        public HMacKeyGenerator_512_256() {
            super("HMACSkein-512-256", 256, new CipherKeyGenerator());
        }
    }
    
    public static class HMacKeyGenerator_512_384 extends BaseKeyGenerator
    {
        public HMacKeyGenerator_512_384() {
            super("HMACSkein-512-384", 384, new CipherKeyGenerator());
        }
    }
    
    public static class HMacKeyGenerator_512_512 extends BaseKeyGenerator
    {
        public HMacKeyGenerator_512_512() {
            super("HMACSkein-512-512", 512, new CipherKeyGenerator());
        }
    }
    
    public static class HashMac_1024_1024 extends BaseMac
    {
        public HashMac_1024_1024() {
            super(new HMac(new SkeinDigest(1024, 1024)));
        }
    }
    
    public static class HashMac_1024_384 extends BaseMac
    {
        public HashMac_1024_384() {
            super(new HMac(new SkeinDigest(1024, 384)));
        }
    }
    
    public static class HashMac_1024_512 extends BaseMac
    {
        public HashMac_1024_512() {
            super(new HMac(new SkeinDigest(1024, 512)));
        }
    }
    
    public static class HashMac_256_128 extends BaseMac
    {
        public HashMac_256_128() {
            super(new HMac(new SkeinDigest(256, 128)));
        }
    }
    
    public static class HashMac_256_160 extends BaseMac
    {
        public HashMac_256_160() {
            super(new HMac(new SkeinDigest(256, 160)));
        }
    }
    
    public static class HashMac_256_224 extends BaseMac
    {
        public HashMac_256_224() {
            super(new HMac(new SkeinDigest(256, 224)));
        }
    }
    
    public static class HashMac_256_256 extends BaseMac
    {
        public HashMac_256_256() {
            super(new HMac(new SkeinDigest(256, 256)));
        }
    }
    
    public static class HashMac_512_128 extends BaseMac
    {
        public HashMac_512_128() {
            super(new HMac(new SkeinDigest(512, 128)));
        }
    }
    
    public static class HashMac_512_160 extends BaseMac
    {
        public HashMac_512_160() {
            super(new HMac(new SkeinDigest(512, 160)));
        }
    }
    
    public static class HashMac_512_224 extends BaseMac
    {
        public HashMac_512_224() {
            super(new HMac(new SkeinDigest(512, 224)));
        }
    }
    
    public static class HashMac_512_256 extends BaseMac
    {
        public HashMac_512_256() {
            super(new HMac(new SkeinDigest(512, 256)));
        }
    }
    
    public static class HashMac_512_384 extends BaseMac
    {
        public HashMac_512_384() {
            super(new HMac(new SkeinDigest(512, 384)));
        }
    }
    
    public static class HashMac_512_512 extends BaseMac
    {
        public HashMac_512_512() {
            super(new HMac(new SkeinDigest(512, 512)));
        }
    }
    
    public static class Mappings extends DigestAlgorithmProvider
    {
        private static final String PREFIX;
        
        static {
            PREFIX = Skein.class.getName();
        }
        
        private void addSkeinMacAlgorithm(final ConfigurableProvider configurableProvider, final int n, final int n2) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Skein-MAC-");
            sb.append(n);
            sb.append("-");
            sb.append(n2);
            final String string = sb.toString();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(Mappings.PREFIX);
            sb2.append("$SkeinMac_");
            sb2.append(n);
            sb2.append("_");
            sb2.append(n2);
            final String string2 = sb2.toString();
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(Mappings.PREFIX);
            sb3.append("$SkeinMacKeyGenerator_");
            sb3.append(n);
            sb3.append("_");
            sb3.append(n2);
            final String string3 = sb3.toString();
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Mac.");
            sb4.append(string);
            configurableProvider.addAlgorithm(sb4.toString(), string2);
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("Alg.Alias.Mac.Skein-MAC");
            sb5.append(n);
            sb5.append("/");
            sb5.append(n2);
            configurableProvider.addAlgorithm(sb5.toString(), string);
            final StringBuilder sb6 = new StringBuilder();
            sb6.append("KeyGenerator.");
            sb6.append(string);
            configurableProvider.addAlgorithm(sb6.toString(), string3);
            final StringBuilder sb7 = new StringBuilder();
            sb7.append("Alg.Alias.KeyGenerator.Skein-MAC");
            sb7.append(n);
            sb7.append("/");
            sb7.append(n2);
            configurableProvider.addAlgorithm(sb7.toString(), string);
        }
        
        @Override
        public void configure(final ConfigurableProvider configurableProvider) {
            final StringBuilder sb = new StringBuilder();
            sb.append(Mappings.PREFIX);
            sb.append("$Digest_256_128");
            configurableProvider.addAlgorithm("MessageDigest.Skein-256-128", sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(Mappings.PREFIX);
            sb2.append("$Digest_256_160");
            configurableProvider.addAlgorithm("MessageDigest.Skein-256-160", sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(Mappings.PREFIX);
            sb3.append("$Digest_256_224");
            configurableProvider.addAlgorithm("MessageDigest.Skein-256-224", sb3.toString());
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(Mappings.PREFIX);
            sb4.append("$Digest_256_256");
            configurableProvider.addAlgorithm("MessageDigest.Skein-256-256", sb4.toString());
            final StringBuilder sb5 = new StringBuilder();
            sb5.append(Mappings.PREFIX);
            sb5.append("$Digest_512_128");
            configurableProvider.addAlgorithm("MessageDigest.Skein-512-128", sb5.toString());
            final StringBuilder sb6 = new StringBuilder();
            sb6.append(Mappings.PREFIX);
            sb6.append("$Digest_512_160");
            configurableProvider.addAlgorithm("MessageDigest.Skein-512-160", sb6.toString());
            final StringBuilder sb7 = new StringBuilder();
            sb7.append(Mappings.PREFIX);
            sb7.append("$Digest_512_224");
            configurableProvider.addAlgorithm("MessageDigest.Skein-512-224", sb7.toString());
            final StringBuilder sb8 = new StringBuilder();
            sb8.append(Mappings.PREFIX);
            sb8.append("$Digest_512_256");
            configurableProvider.addAlgorithm("MessageDigest.Skein-512-256", sb8.toString());
            final StringBuilder sb9 = new StringBuilder();
            sb9.append(Mappings.PREFIX);
            sb9.append("$Digest_512_384");
            configurableProvider.addAlgorithm("MessageDigest.Skein-512-384", sb9.toString());
            final StringBuilder sb10 = new StringBuilder();
            sb10.append(Mappings.PREFIX);
            sb10.append("$Digest_512_512");
            configurableProvider.addAlgorithm("MessageDigest.Skein-512-512", sb10.toString());
            final StringBuilder sb11 = new StringBuilder();
            sb11.append(Mappings.PREFIX);
            sb11.append("$Digest_1024_384");
            configurableProvider.addAlgorithm("MessageDigest.Skein-1024-384", sb11.toString());
            final StringBuilder sb12 = new StringBuilder();
            sb12.append(Mappings.PREFIX);
            sb12.append("$Digest_1024_512");
            configurableProvider.addAlgorithm("MessageDigest.Skein-1024-512", sb12.toString());
            final StringBuilder sb13 = new StringBuilder();
            sb13.append(Mappings.PREFIX);
            sb13.append("$Digest_1024_1024");
            configurableProvider.addAlgorithm("MessageDigest.Skein-1024-1024", sb13.toString());
            final StringBuilder sb14 = new StringBuilder();
            sb14.append(Mappings.PREFIX);
            sb14.append("$HashMac_256_128");
            final String string = sb14.toString();
            final StringBuilder sb15 = new StringBuilder();
            sb15.append(Mappings.PREFIX);
            sb15.append("$HMacKeyGenerator_256_128");
            this.addHMACAlgorithm(configurableProvider, "Skein-256-128", string, sb15.toString());
            final StringBuilder sb16 = new StringBuilder();
            sb16.append(Mappings.PREFIX);
            sb16.append("$HashMac_256_160");
            final String string2 = sb16.toString();
            final StringBuilder sb17 = new StringBuilder();
            sb17.append(Mappings.PREFIX);
            sb17.append("$HMacKeyGenerator_256_160");
            this.addHMACAlgorithm(configurableProvider, "Skein-256-160", string2, sb17.toString());
            final StringBuilder sb18 = new StringBuilder();
            sb18.append(Mappings.PREFIX);
            sb18.append("$HashMac_256_224");
            final String string3 = sb18.toString();
            final StringBuilder sb19 = new StringBuilder();
            sb19.append(Mappings.PREFIX);
            sb19.append("$HMacKeyGenerator_256_224");
            this.addHMACAlgorithm(configurableProvider, "Skein-256-224", string3, sb19.toString());
            final StringBuilder sb20 = new StringBuilder();
            sb20.append(Mappings.PREFIX);
            sb20.append("$HashMac_256_256");
            final String string4 = sb20.toString();
            final StringBuilder sb21 = new StringBuilder();
            sb21.append(Mappings.PREFIX);
            sb21.append("$HMacKeyGenerator_256_256");
            this.addHMACAlgorithm(configurableProvider, "Skein-256-256", string4, sb21.toString());
            final StringBuilder sb22 = new StringBuilder();
            sb22.append(Mappings.PREFIX);
            sb22.append("$HashMac_512_128");
            final String string5 = sb22.toString();
            final StringBuilder sb23 = new StringBuilder();
            sb23.append(Mappings.PREFIX);
            sb23.append("$HMacKeyGenerator_512_128");
            this.addHMACAlgorithm(configurableProvider, "Skein-512-128", string5, sb23.toString());
            final StringBuilder sb24 = new StringBuilder();
            sb24.append(Mappings.PREFIX);
            sb24.append("$HashMac_512_160");
            final String string6 = sb24.toString();
            final StringBuilder sb25 = new StringBuilder();
            sb25.append(Mappings.PREFIX);
            sb25.append("$HMacKeyGenerator_512_160");
            this.addHMACAlgorithm(configurableProvider, "Skein-512-160", string6, sb25.toString());
            final StringBuilder sb26 = new StringBuilder();
            sb26.append(Mappings.PREFIX);
            sb26.append("$HashMac_512_224");
            final String string7 = sb26.toString();
            final StringBuilder sb27 = new StringBuilder();
            sb27.append(Mappings.PREFIX);
            sb27.append("$HMacKeyGenerator_512_224");
            this.addHMACAlgorithm(configurableProvider, "Skein-512-224", string7, sb27.toString());
            final StringBuilder sb28 = new StringBuilder();
            sb28.append(Mappings.PREFIX);
            sb28.append("$HashMac_512_256");
            final String string8 = sb28.toString();
            final StringBuilder sb29 = new StringBuilder();
            sb29.append(Mappings.PREFIX);
            sb29.append("$HMacKeyGenerator_512_256");
            this.addHMACAlgorithm(configurableProvider, "Skein-512-256", string8, sb29.toString());
            final StringBuilder sb30 = new StringBuilder();
            sb30.append(Mappings.PREFIX);
            sb30.append("$HashMac_512_384");
            final String string9 = sb30.toString();
            final StringBuilder sb31 = new StringBuilder();
            sb31.append(Mappings.PREFIX);
            sb31.append("$HMacKeyGenerator_512_384");
            this.addHMACAlgorithm(configurableProvider, "Skein-512-384", string9, sb31.toString());
            final StringBuilder sb32 = new StringBuilder();
            sb32.append(Mappings.PREFIX);
            sb32.append("$HashMac_512_512");
            final String string10 = sb32.toString();
            final StringBuilder sb33 = new StringBuilder();
            sb33.append(Mappings.PREFIX);
            sb33.append("$HMacKeyGenerator_512_512");
            this.addHMACAlgorithm(configurableProvider, "Skein-512-512", string10, sb33.toString());
            final StringBuilder sb34 = new StringBuilder();
            sb34.append(Mappings.PREFIX);
            sb34.append("$HashMac_1024_384");
            final String string11 = sb34.toString();
            final StringBuilder sb35 = new StringBuilder();
            sb35.append(Mappings.PREFIX);
            sb35.append("$HMacKeyGenerator_1024_384");
            this.addHMACAlgorithm(configurableProvider, "Skein-1024-384", string11, sb35.toString());
            final StringBuilder sb36 = new StringBuilder();
            sb36.append(Mappings.PREFIX);
            sb36.append("$HashMac_1024_512");
            final String string12 = sb36.toString();
            final StringBuilder sb37 = new StringBuilder();
            sb37.append(Mappings.PREFIX);
            sb37.append("$HMacKeyGenerator_1024_512");
            this.addHMACAlgorithm(configurableProvider, "Skein-1024-512", string12, sb37.toString());
            final StringBuilder sb38 = new StringBuilder();
            sb38.append(Mappings.PREFIX);
            sb38.append("$HashMac_1024_1024");
            final String string13 = sb38.toString();
            final StringBuilder sb39 = new StringBuilder();
            sb39.append(Mappings.PREFIX);
            sb39.append("$HMacKeyGenerator_1024_1024");
            this.addHMACAlgorithm(configurableProvider, "Skein-1024-1024", string13, sb39.toString());
            this.addSkeinMacAlgorithm(configurableProvider, 256, 128);
            this.addSkeinMacAlgorithm(configurableProvider, 256, 160);
            this.addSkeinMacAlgorithm(configurableProvider, 256, 224);
            this.addSkeinMacAlgorithm(configurableProvider, 256, 256);
            this.addSkeinMacAlgorithm(configurableProvider, 512, 128);
            this.addSkeinMacAlgorithm(configurableProvider, 512, 160);
            this.addSkeinMacAlgorithm(configurableProvider, 512, 224);
            this.addSkeinMacAlgorithm(configurableProvider, 512, 256);
            this.addSkeinMacAlgorithm(configurableProvider, 512, 384);
            this.addSkeinMacAlgorithm(configurableProvider, 512, 512);
            this.addSkeinMacAlgorithm(configurableProvider, 1024, 384);
            this.addSkeinMacAlgorithm(configurableProvider, 1024, 512);
            this.addSkeinMacAlgorithm(configurableProvider, 1024, 1024);
        }
    }
    
    public static class SkeinMacKeyGenerator_1024_1024 extends BaseKeyGenerator
    {
        public SkeinMacKeyGenerator_1024_1024() {
            super("Skein-MAC-1024-1024", 1024, new CipherKeyGenerator());
        }
    }
    
    public static class SkeinMacKeyGenerator_1024_384 extends BaseKeyGenerator
    {
        public SkeinMacKeyGenerator_1024_384() {
            super("Skein-MAC-1024-384", 384, new CipherKeyGenerator());
        }
    }
    
    public static class SkeinMacKeyGenerator_1024_512 extends BaseKeyGenerator
    {
        public SkeinMacKeyGenerator_1024_512() {
            super("Skein-MAC-1024-512", 512, new CipherKeyGenerator());
        }
    }
    
    public static class SkeinMacKeyGenerator_256_128 extends BaseKeyGenerator
    {
        public SkeinMacKeyGenerator_256_128() {
            super("Skein-MAC-256-128", 128, new CipherKeyGenerator());
        }
    }
    
    public static class SkeinMacKeyGenerator_256_160 extends BaseKeyGenerator
    {
        public SkeinMacKeyGenerator_256_160() {
            super("Skein-MAC-256-160", 160, new CipherKeyGenerator());
        }
    }
    
    public static class SkeinMacKeyGenerator_256_224 extends BaseKeyGenerator
    {
        public SkeinMacKeyGenerator_256_224() {
            super("Skein-MAC-256-224", 224, new CipherKeyGenerator());
        }
    }
    
    public static class SkeinMacKeyGenerator_256_256 extends BaseKeyGenerator
    {
        public SkeinMacKeyGenerator_256_256() {
            super("Skein-MAC-256-256", 256, new CipherKeyGenerator());
        }
    }
    
    public static class SkeinMacKeyGenerator_512_128 extends BaseKeyGenerator
    {
        public SkeinMacKeyGenerator_512_128() {
            super("Skein-MAC-512-128", 128, new CipherKeyGenerator());
        }
    }
    
    public static class SkeinMacKeyGenerator_512_160 extends BaseKeyGenerator
    {
        public SkeinMacKeyGenerator_512_160() {
            super("Skein-MAC-512-160", 160, new CipherKeyGenerator());
        }
    }
    
    public static class SkeinMacKeyGenerator_512_224 extends BaseKeyGenerator
    {
        public SkeinMacKeyGenerator_512_224() {
            super("Skein-MAC-512-224", 224, new CipherKeyGenerator());
        }
    }
    
    public static class SkeinMacKeyGenerator_512_256 extends BaseKeyGenerator
    {
        public SkeinMacKeyGenerator_512_256() {
            super("Skein-MAC-512-256", 256, new CipherKeyGenerator());
        }
    }
    
    public static class SkeinMacKeyGenerator_512_384 extends BaseKeyGenerator
    {
        public SkeinMacKeyGenerator_512_384() {
            super("Skein-MAC-512-384", 384, new CipherKeyGenerator());
        }
    }
    
    public static class SkeinMacKeyGenerator_512_512 extends BaseKeyGenerator
    {
        public SkeinMacKeyGenerator_512_512() {
            super("Skein-MAC-512-512", 512, new CipherKeyGenerator());
        }
    }
    
    public static class SkeinMac_1024_1024 extends BaseMac
    {
        public SkeinMac_1024_1024() {
            super(new SkeinMac(1024, 1024));
        }
    }
    
    public static class SkeinMac_1024_384 extends BaseMac
    {
        public SkeinMac_1024_384() {
            super(new SkeinMac(1024, 384));
        }
    }
    
    public static class SkeinMac_1024_512 extends BaseMac
    {
        public SkeinMac_1024_512() {
            super(new SkeinMac(1024, 512));
        }
    }
    
    public static class SkeinMac_256_128 extends BaseMac
    {
        public SkeinMac_256_128() {
            super(new SkeinMac(256, 128));
        }
    }
    
    public static class SkeinMac_256_160 extends BaseMac
    {
        public SkeinMac_256_160() {
            super(new SkeinMac(256, 160));
        }
    }
    
    public static class SkeinMac_256_224 extends BaseMac
    {
        public SkeinMac_256_224() {
            super(new SkeinMac(256, 224));
        }
    }
    
    public static class SkeinMac_256_256 extends BaseMac
    {
        public SkeinMac_256_256() {
            super(new SkeinMac(256, 256));
        }
    }
    
    public static class SkeinMac_512_128 extends BaseMac
    {
        public SkeinMac_512_128() {
            super(new SkeinMac(512, 128));
        }
    }
    
    public static class SkeinMac_512_160 extends BaseMac
    {
        public SkeinMac_512_160() {
            super(new SkeinMac(512, 160));
        }
    }
    
    public static class SkeinMac_512_224 extends BaseMac
    {
        public SkeinMac_512_224() {
            super(new SkeinMac(512, 224));
        }
    }
    
    public static class SkeinMac_512_256 extends BaseMac
    {
        public SkeinMac_512_256() {
            super(new SkeinMac(512, 256));
        }
    }
    
    public static class SkeinMac_512_384 extends BaseMac
    {
        public SkeinMac_512_384() {
            super(new SkeinMac(512, 384));
        }
    }
    
    public static class SkeinMac_512_512 extends BaseMac
    {
        public SkeinMac_512_512() {
            super(new SkeinMac(512, 512));
        }
    }
}
