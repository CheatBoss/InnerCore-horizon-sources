package org.spongycastle.jcajce.provider.symmetric;

import org.spongycastle.jcajce.provider.config.*;
import org.spongycastle.asn1.gnu.*;
import org.spongycastle.asn1.*;
import org.spongycastle.jcajce.provider.symmetric.util.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.generators.*;
import org.spongycastle.crypto.modes.*;
import org.spongycastle.crypto.macs.*;
import org.spongycastle.crypto.engines.*;

public final class Serpent
{
    private Serpent() {
    }
    
    public static class AlgParams extends IvAlgorithmParameters
    {
        @Override
        protected String engineToString() {
            return "Serpent IV";
        }
    }
    
    public static class CBC extends BaseBlockCipher
    {
        public CBC() {
            super(new CBCBlockCipher(new SerpentEngine()), 128);
        }
    }
    
    public static class CFB extends BaseBlockCipher
    {
        public CFB() {
            super(new BufferedBlockCipher(new CFBBlockCipher(new SerpentEngine(), 128)), 128);
        }
    }
    
    public static class ECB extends BaseBlockCipher
    {
        public ECB() {
            super(new BlockCipherProvider() {
                @Override
                public BlockCipher get() {
                    return new SerpentEngine();
                }
            });
        }
    }
    
    public static class KeyGen extends BaseKeyGenerator
    {
        public KeyGen() {
            super("Serpent", 192, new CipherKeyGenerator());
        }
    }
    
    public static class Mappings extends SymmetricAlgorithmProvider
    {
        private static final String PREFIX;
        
        static {
            PREFIX = Serpent.class.getName();
        }
        
        @Override
        public void configure(final ConfigurableProvider configurableProvider) {
            final StringBuilder sb = new StringBuilder();
            sb.append(Mappings.PREFIX);
            sb.append("$ECB");
            configurableProvider.addAlgorithm("Cipher.Serpent", sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(Mappings.PREFIX);
            sb2.append("$KeyGen");
            configurableProvider.addAlgorithm("KeyGenerator.Serpent", sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(Mappings.PREFIX);
            sb3.append("$AlgParams");
            configurableProvider.addAlgorithm("AlgorithmParameters.Serpent", sb3.toString());
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(Mappings.PREFIX);
            sb4.append("$TECB");
            configurableProvider.addAlgorithm("Cipher.Tnepres", sb4.toString());
            final StringBuilder sb5 = new StringBuilder();
            sb5.append(Mappings.PREFIX);
            sb5.append("$TKeyGen");
            configurableProvider.addAlgorithm("KeyGenerator.Tnepres", sb5.toString());
            final StringBuilder sb6 = new StringBuilder();
            sb6.append(Mappings.PREFIX);
            sb6.append("$TAlgParams");
            configurableProvider.addAlgorithm("AlgorithmParameters.Tnepres", sb6.toString());
            final ASN1ObjectIdentifier serpent_128_ECB = GNUObjectIdentifiers.Serpent_128_ECB;
            final StringBuilder sb7 = new StringBuilder();
            sb7.append(Mappings.PREFIX);
            sb7.append("$ECB");
            configurableProvider.addAlgorithm("Cipher", serpent_128_ECB, sb7.toString());
            final ASN1ObjectIdentifier serpent_192_ECB = GNUObjectIdentifiers.Serpent_192_ECB;
            final StringBuilder sb8 = new StringBuilder();
            sb8.append(Mappings.PREFIX);
            sb8.append("$ECB");
            configurableProvider.addAlgorithm("Cipher", serpent_192_ECB, sb8.toString());
            final ASN1ObjectIdentifier serpent_256_ECB = GNUObjectIdentifiers.Serpent_256_ECB;
            final StringBuilder sb9 = new StringBuilder();
            sb9.append(Mappings.PREFIX);
            sb9.append("$ECB");
            configurableProvider.addAlgorithm("Cipher", serpent_256_ECB, sb9.toString());
            final ASN1ObjectIdentifier serpent_128_CBC = GNUObjectIdentifiers.Serpent_128_CBC;
            final StringBuilder sb10 = new StringBuilder();
            sb10.append(Mappings.PREFIX);
            sb10.append("$CBC");
            configurableProvider.addAlgorithm("Cipher", serpent_128_CBC, sb10.toString());
            final ASN1ObjectIdentifier serpent_192_CBC = GNUObjectIdentifiers.Serpent_192_CBC;
            final StringBuilder sb11 = new StringBuilder();
            sb11.append(Mappings.PREFIX);
            sb11.append("$CBC");
            configurableProvider.addAlgorithm("Cipher", serpent_192_CBC, sb11.toString());
            final ASN1ObjectIdentifier serpent_256_CBC = GNUObjectIdentifiers.Serpent_256_CBC;
            final StringBuilder sb12 = new StringBuilder();
            sb12.append(Mappings.PREFIX);
            sb12.append("$CBC");
            configurableProvider.addAlgorithm("Cipher", serpent_256_CBC, sb12.toString());
            final ASN1ObjectIdentifier serpent_128_CFB = GNUObjectIdentifiers.Serpent_128_CFB;
            final StringBuilder sb13 = new StringBuilder();
            sb13.append(Mappings.PREFIX);
            sb13.append("$CFB");
            configurableProvider.addAlgorithm("Cipher", serpent_128_CFB, sb13.toString());
            final ASN1ObjectIdentifier serpent_192_CFB = GNUObjectIdentifiers.Serpent_192_CFB;
            final StringBuilder sb14 = new StringBuilder();
            sb14.append(Mappings.PREFIX);
            sb14.append("$CFB");
            configurableProvider.addAlgorithm("Cipher", serpent_192_CFB, sb14.toString());
            final ASN1ObjectIdentifier serpent_256_CFB = GNUObjectIdentifiers.Serpent_256_CFB;
            final StringBuilder sb15 = new StringBuilder();
            sb15.append(Mappings.PREFIX);
            sb15.append("$CFB");
            configurableProvider.addAlgorithm("Cipher", serpent_256_CFB, sb15.toString());
            final ASN1ObjectIdentifier serpent_128_OFB = GNUObjectIdentifiers.Serpent_128_OFB;
            final StringBuilder sb16 = new StringBuilder();
            sb16.append(Mappings.PREFIX);
            sb16.append("$OFB");
            configurableProvider.addAlgorithm("Cipher", serpent_128_OFB, sb16.toString());
            final ASN1ObjectIdentifier serpent_192_OFB = GNUObjectIdentifiers.Serpent_192_OFB;
            final StringBuilder sb17 = new StringBuilder();
            sb17.append(Mappings.PREFIX);
            sb17.append("$OFB");
            configurableProvider.addAlgorithm("Cipher", serpent_192_OFB, sb17.toString());
            final ASN1ObjectIdentifier serpent_256_OFB = GNUObjectIdentifiers.Serpent_256_OFB;
            final StringBuilder sb18 = new StringBuilder();
            sb18.append(Mappings.PREFIX);
            sb18.append("$OFB");
            configurableProvider.addAlgorithm("Cipher", serpent_256_OFB, sb18.toString());
            final StringBuilder sb19 = new StringBuilder();
            sb19.append(Mappings.PREFIX);
            sb19.append("$SerpentGMAC");
            final String string = sb19.toString();
            final StringBuilder sb20 = new StringBuilder();
            sb20.append(Mappings.PREFIX);
            sb20.append("$KeyGen");
            this.addGMacAlgorithm(configurableProvider, "SERPENT", string, sb20.toString());
            final StringBuilder sb21 = new StringBuilder();
            sb21.append(Mappings.PREFIX);
            sb21.append("$TSerpentGMAC");
            final String string2 = sb21.toString();
            final StringBuilder sb22 = new StringBuilder();
            sb22.append(Mappings.PREFIX);
            sb22.append("$TKeyGen");
            this.addGMacAlgorithm(configurableProvider, "TNEPRES", string2, sb22.toString());
            final StringBuilder sb23 = new StringBuilder();
            sb23.append(Mappings.PREFIX);
            sb23.append("$Poly1305");
            final String string3 = sb23.toString();
            final StringBuilder sb24 = new StringBuilder();
            sb24.append(Mappings.PREFIX);
            sb24.append("$Poly1305KeyGen");
            this.addPoly1305Algorithm(configurableProvider, "SERPENT", string3, sb24.toString());
        }
    }
    
    public static class OFB extends BaseBlockCipher
    {
        public OFB() {
            super(new BufferedBlockCipher(new OFBBlockCipher(new SerpentEngine(), 128)), 128);
        }
    }
    
    public static class Poly1305 extends BaseMac
    {
        public Poly1305() {
            super(new org.spongycastle.crypto.macs.Poly1305(new TwofishEngine()));
        }
    }
    
    public static class Poly1305KeyGen extends BaseKeyGenerator
    {
        public Poly1305KeyGen() {
            super("Poly1305-Serpent", 256, new Poly1305KeyGenerator());
        }
    }
    
    public static class SerpentGMAC extends BaseMac
    {
        public SerpentGMAC() {
            super(new GMac(new GCMBlockCipher(new SerpentEngine())));
        }
    }
    
    public static class TAlgParams extends IvAlgorithmParameters
    {
        @Override
        protected String engineToString() {
            return "Tnepres IV";
        }
    }
    
    public static class TECB extends BaseBlockCipher
    {
        public TECB() {
            super(new BlockCipherProvider() {
                @Override
                public BlockCipher get() {
                    return new TnepresEngine();
                }
            });
        }
    }
    
    public static class TKeyGen extends BaseKeyGenerator
    {
        public TKeyGen() {
            super("Tnepres", 192, new CipherKeyGenerator());
        }
    }
    
    public static class TSerpentGMAC extends BaseMac
    {
        public TSerpentGMAC() {
            super(new GMac(new GCMBlockCipher(new TnepresEngine())));
        }
    }
}
