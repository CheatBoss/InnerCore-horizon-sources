package org.spongycastle.jcajce.provider.symmetric;

import javax.crypto.spec.*;
import java.security.spec.*;
import java.security.*;
import org.spongycastle.crypto.macs.*;
import org.spongycastle.jcajce.provider.config.*;
import org.spongycastle.asn1.ua.*;
import org.spongycastle.asn1.*;
import org.spongycastle.crypto.modes.*;
import org.spongycastle.jcajce.provider.symmetric.util.*;
import org.spongycastle.crypto.engines.*;
import org.spongycastle.crypto.*;

public class DSTU7624
{
    private DSTU7624() {
    }
    
    public static class AlgParamGen extends BaseAlgorithmParameterGenerator
    {
        private final int ivLength;
        
        public AlgParamGen(final int n) {
            this.ivLength = n / 8;
        }
        
        @Override
        protected AlgorithmParameters engineGenerateParameters() {
            final byte[] array = new byte[this.ivLength];
            if (this.random == null) {
                this.random = new SecureRandom();
            }
            this.random.nextBytes(array);
            try {
                final AlgorithmParameters parametersInstance = this.createParametersInstance("DSTU7624");
                parametersInstance.init(new IvParameterSpec(array));
                return parametersInstance;
            }
            catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }
        
        @Override
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for DSTU7624 parameter generation.");
        }
    }
    
    public static class AlgParamGen128 extends AlgParamGen
    {
        AlgParamGen128() {
            super(128);
        }
    }
    
    public static class AlgParamGen256 extends AlgParamGen
    {
        AlgParamGen256() {
            super(256);
        }
    }
    
    public static class AlgParamGen512 extends AlgParamGen
    {
        AlgParamGen512() {
            super(512);
        }
    }
    
    public static class AlgParams extends IvAlgorithmParameters
    {
        @Override
        protected String engineToString() {
            return "DSTU7624 IV";
        }
    }
    
    public static class CBC128 extends BaseBlockCipher
    {
        public CBC128() {
            super(new CBCBlockCipher(new DSTU7624Engine(128)), 128);
        }
    }
    
    public static class CBC256 extends BaseBlockCipher
    {
        public CBC256() {
            super(new CBCBlockCipher(new DSTU7624Engine(256)), 256);
        }
    }
    
    public static class CBC512 extends BaseBlockCipher
    {
        public CBC512() {
            super(new CBCBlockCipher(new DSTU7624Engine(512)), 512);
        }
    }
    
    public static class CCM128 extends BaseBlockCipher
    {
        public CCM128() {
            super(new KCCMBlockCipher(new DSTU7624Engine(128)));
        }
    }
    
    public static class CCM256 extends BaseBlockCipher
    {
        public CCM256() {
            super(new KCCMBlockCipher(new DSTU7624Engine(256)));
        }
    }
    
    public static class CCM512 extends BaseBlockCipher
    {
        public CCM512() {
            super(new KCCMBlockCipher(new DSTU7624Engine(512)));
        }
    }
    
    public static class CFB128 extends BaseBlockCipher
    {
        public CFB128() {
            super(new BufferedBlockCipher(new CFBBlockCipher(new DSTU7624Engine(128), 128)), 128);
        }
    }
    
    public static class CFB256 extends BaseBlockCipher
    {
        public CFB256() {
            super(new BufferedBlockCipher(new CFBBlockCipher(new DSTU7624Engine(256), 256)), 256);
        }
    }
    
    public static class CFB512 extends BaseBlockCipher
    {
        public CFB512() {
            super(new BufferedBlockCipher(new CFBBlockCipher(new DSTU7624Engine(512), 512)), 512);
        }
    }
    
    public static class CTR128 extends BaseBlockCipher
    {
        public CTR128() {
            super(new BufferedBlockCipher(new KCTRBlockCipher(new DSTU7624Engine(128))), 128);
        }
    }
    
    public static class CTR256 extends BaseBlockCipher
    {
        public CTR256() {
            super(new BufferedBlockCipher(new KCTRBlockCipher(new DSTU7624Engine(256))), 256);
        }
    }
    
    public static class CTR512 extends BaseBlockCipher
    {
        public CTR512() {
            super(new BufferedBlockCipher(new KCTRBlockCipher(new DSTU7624Engine(512))), 512);
        }
    }
    
    public static class ECB extends BaseBlockCipher
    {
        public ECB() {
            super(new BlockCipherProvider() {
                @Override
                public BlockCipher get() {
                    return new DSTU7624Engine(128);
                }
            });
        }
    }
    
    public static class ECB128 extends BaseBlockCipher
    {
        public ECB128() {
            super(new DSTU7624Engine(128));
        }
    }
    
    public static class ECB256 extends BaseBlockCipher
    {
        public ECB256() {
            super(new DSTU7624Engine(256));
        }
    }
    
    public static class ECB512 extends BaseBlockCipher
    {
        public ECB512() {
            super(new DSTU7624Engine(512));
        }
    }
    
    public static class ECB_128 extends BaseBlockCipher
    {
        public ECB_128() {
            super(new DSTU7624Engine(128));
        }
    }
    
    public static class ECB_256 extends BaseBlockCipher
    {
        public ECB_256() {
            super(new DSTU7624Engine(256));
        }
    }
    
    public static class ECB_512 extends BaseBlockCipher
    {
        public ECB_512() {
            super(new DSTU7624Engine(512));
        }
    }
    
    public static class GCM128 extends BaseBlockCipher
    {
        public GCM128() {
            super(new KGCMBlockCipher(new DSTU7624Engine(128)));
        }
    }
    
    public static class GCM256 extends BaseBlockCipher
    {
        public GCM256() {
            super(new KGCMBlockCipher(new DSTU7624Engine(256)));
        }
    }
    
    public static class GCM512 extends BaseBlockCipher
    {
        public GCM512() {
            super(new KGCMBlockCipher(new DSTU7624Engine(512)));
        }
    }
    
    public static class GMAC extends BaseMac
    {
        public GMAC() {
            super(new KGMac(new KGCMBlockCipher(new DSTU7624Engine(128)), 128));
        }
    }
    
    public static class GMAC128 extends BaseMac
    {
        public GMAC128() {
            super(new KGMac(new KGCMBlockCipher(new DSTU7624Engine(128)), 128));
        }
    }
    
    public static class GMAC256 extends BaseMac
    {
        public GMAC256() {
            super(new KGMac(new KGCMBlockCipher(new DSTU7624Engine(256)), 256));
        }
    }
    
    public static class GMAC512 extends BaseMac
    {
        public GMAC512() {
            super(new KGMac(new KGCMBlockCipher(new DSTU7624Engine(512)), 512));
        }
    }
    
    public static class KeyGen extends BaseKeyGenerator
    {
        public KeyGen() {
            this(256);
        }
        
        public KeyGen(final int n) {
            super("DSTU7624", n, new CipherKeyGenerator());
        }
    }
    
    public static class KeyGen128 extends KeyGen
    {
        public KeyGen128() {
            super(128);
        }
    }
    
    public static class KeyGen256 extends KeyGen
    {
        public KeyGen256() {
            super(256);
        }
    }
    
    public static class KeyGen512 extends KeyGen
    {
        public KeyGen512() {
            super(512);
        }
    }
    
    public static class Mappings extends SymmetricAlgorithmProvider
    {
        private static final String PREFIX;
        
        static {
            PREFIX = DSTU7624.class.getName();
        }
        
        @Override
        public void configure(final ConfigurableProvider configurableProvider) {
            final StringBuilder sb = new StringBuilder();
            sb.append(Mappings.PREFIX);
            sb.append("$AlgParams128");
            configurableProvider.addAlgorithm("AlgorithmParameters.DSTU7624", sb.toString());
            final ASN1ObjectIdentifier dstu7624cbc_128 = UAObjectIdentifiers.dstu7624cbc_128;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(Mappings.PREFIX);
            sb2.append("$AlgParams");
            configurableProvider.addAlgorithm("AlgorithmParameters", dstu7624cbc_128, sb2.toString());
            final ASN1ObjectIdentifier dstu7624cbc_129 = UAObjectIdentifiers.dstu7624cbc_256;
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(Mappings.PREFIX);
            sb3.append("$AlgParams");
            configurableProvider.addAlgorithm("AlgorithmParameters", dstu7624cbc_129, sb3.toString());
            final ASN1ObjectIdentifier dstu7624cbc_130 = UAObjectIdentifiers.dstu7624cbc_512;
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(Mappings.PREFIX);
            sb4.append("$AlgParams");
            configurableProvider.addAlgorithm("AlgorithmParameters", dstu7624cbc_130, sb4.toString());
            final StringBuilder sb5 = new StringBuilder();
            sb5.append(Mappings.PREFIX);
            sb5.append("$AlgParamGen128");
            configurableProvider.addAlgorithm("AlgorithmParameterGenerator.DSTU7624", sb5.toString());
            final ASN1ObjectIdentifier dstu7624cbc_131 = UAObjectIdentifiers.dstu7624cbc_128;
            final StringBuilder sb6 = new StringBuilder();
            sb6.append(Mappings.PREFIX);
            sb6.append("$AlgParamGen128");
            configurableProvider.addAlgorithm("AlgorithmParameterGenerator", dstu7624cbc_131, sb6.toString());
            final ASN1ObjectIdentifier dstu7624cbc_132 = UAObjectIdentifiers.dstu7624cbc_256;
            final StringBuilder sb7 = new StringBuilder();
            sb7.append(Mappings.PREFIX);
            sb7.append("$AlgParamGen256");
            configurableProvider.addAlgorithm("AlgorithmParameterGenerator", dstu7624cbc_132, sb7.toString());
            final ASN1ObjectIdentifier dstu7624cbc_133 = UAObjectIdentifiers.dstu7624cbc_512;
            final StringBuilder sb8 = new StringBuilder();
            sb8.append(Mappings.PREFIX);
            sb8.append("$AlgParamGen512");
            configurableProvider.addAlgorithm("AlgorithmParameterGenerator", dstu7624cbc_133, sb8.toString());
            final StringBuilder sb9 = new StringBuilder();
            sb9.append(Mappings.PREFIX);
            sb9.append("$ECB_128");
            configurableProvider.addAlgorithm("Cipher.DSTU7624", sb9.toString());
            final StringBuilder sb10 = new StringBuilder();
            sb10.append(Mappings.PREFIX);
            sb10.append("$ECB_128");
            configurableProvider.addAlgorithm("Cipher.DSTU7624-128", sb10.toString());
            final StringBuilder sb11 = new StringBuilder();
            sb11.append(Mappings.PREFIX);
            sb11.append("$ECB_256");
            configurableProvider.addAlgorithm("Cipher.DSTU7624-256", sb11.toString());
            final StringBuilder sb12 = new StringBuilder();
            sb12.append(Mappings.PREFIX);
            sb12.append("$ECB_512");
            configurableProvider.addAlgorithm("Cipher.DSTU7624-512", sb12.toString());
            final ASN1ObjectIdentifier dstu7624ecb_128 = UAObjectIdentifiers.dstu7624ecb_128;
            final StringBuilder sb13 = new StringBuilder();
            sb13.append(Mappings.PREFIX);
            sb13.append("$ECB128");
            configurableProvider.addAlgorithm("Cipher", dstu7624ecb_128, sb13.toString());
            final ASN1ObjectIdentifier dstu7624ecb_129 = UAObjectIdentifiers.dstu7624ecb_256;
            final StringBuilder sb14 = new StringBuilder();
            sb14.append(Mappings.PREFIX);
            sb14.append("$ECB256");
            configurableProvider.addAlgorithm("Cipher", dstu7624ecb_129, sb14.toString());
            final ASN1ObjectIdentifier dstu7624ecb_130 = UAObjectIdentifiers.dstu7624ecb_512;
            final StringBuilder sb15 = new StringBuilder();
            sb15.append(Mappings.PREFIX);
            sb15.append("$ECB512");
            configurableProvider.addAlgorithm("Cipher", dstu7624ecb_130, sb15.toString());
            final ASN1ObjectIdentifier dstu7624cbc_134 = UAObjectIdentifiers.dstu7624cbc_128;
            final StringBuilder sb16 = new StringBuilder();
            sb16.append(Mappings.PREFIX);
            sb16.append("$CBC128");
            configurableProvider.addAlgorithm("Cipher", dstu7624cbc_134, sb16.toString());
            final ASN1ObjectIdentifier dstu7624cbc_135 = UAObjectIdentifiers.dstu7624cbc_256;
            final StringBuilder sb17 = new StringBuilder();
            sb17.append(Mappings.PREFIX);
            sb17.append("$CBC256");
            configurableProvider.addAlgorithm("Cipher", dstu7624cbc_135, sb17.toString());
            final ASN1ObjectIdentifier dstu7624cbc_136 = UAObjectIdentifiers.dstu7624cbc_512;
            final StringBuilder sb18 = new StringBuilder();
            sb18.append(Mappings.PREFIX);
            sb18.append("$CBC512");
            configurableProvider.addAlgorithm("Cipher", dstu7624cbc_136, sb18.toString());
            final ASN1ObjectIdentifier dstu7624ofb_128 = UAObjectIdentifiers.dstu7624ofb_128;
            final StringBuilder sb19 = new StringBuilder();
            sb19.append(Mappings.PREFIX);
            sb19.append("$OFB128");
            configurableProvider.addAlgorithm("Cipher", dstu7624ofb_128, sb19.toString());
            final ASN1ObjectIdentifier dstu7624ofb_129 = UAObjectIdentifiers.dstu7624ofb_256;
            final StringBuilder sb20 = new StringBuilder();
            sb20.append(Mappings.PREFIX);
            sb20.append("$OFB256");
            configurableProvider.addAlgorithm("Cipher", dstu7624ofb_129, sb20.toString());
            final ASN1ObjectIdentifier dstu7624ofb_130 = UAObjectIdentifiers.dstu7624ofb_512;
            final StringBuilder sb21 = new StringBuilder();
            sb21.append(Mappings.PREFIX);
            sb21.append("$OFB512");
            configurableProvider.addAlgorithm("Cipher", dstu7624ofb_130, sb21.toString());
            final ASN1ObjectIdentifier dstu7624cfb_128 = UAObjectIdentifiers.dstu7624cfb_128;
            final StringBuilder sb22 = new StringBuilder();
            sb22.append(Mappings.PREFIX);
            sb22.append("$CFB128");
            configurableProvider.addAlgorithm("Cipher", dstu7624cfb_128, sb22.toString());
            final ASN1ObjectIdentifier dstu7624cfb_129 = UAObjectIdentifiers.dstu7624cfb_256;
            final StringBuilder sb23 = new StringBuilder();
            sb23.append(Mappings.PREFIX);
            sb23.append("$CFB256");
            configurableProvider.addAlgorithm("Cipher", dstu7624cfb_129, sb23.toString());
            final ASN1ObjectIdentifier dstu7624cfb_130 = UAObjectIdentifiers.dstu7624cfb_512;
            final StringBuilder sb24 = new StringBuilder();
            sb24.append(Mappings.PREFIX);
            sb24.append("$CFB512");
            configurableProvider.addAlgorithm("Cipher", dstu7624cfb_130, sb24.toString());
            final ASN1ObjectIdentifier dstu7624ctr_128 = UAObjectIdentifiers.dstu7624ctr_128;
            final StringBuilder sb25 = new StringBuilder();
            sb25.append(Mappings.PREFIX);
            sb25.append("$CTR128");
            configurableProvider.addAlgorithm("Cipher", dstu7624ctr_128, sb25.toString());
            final ASN1ObjectIdentifier dstu7624ctr_129 = UAObjectIdentifiers.dstu7624ctr_256;
            final StringBuilder sb26 = new StringBuilder();
            sb26.append(Mappings.PREFIX);
            sb26.append("$CTR256");
            configurableProvider.addAlgorithm("Cipher", dstu7624ctr_129, sb26.toString());
            final ASN1ObjectIdentifier dstu7624ctr_130 = UAObjectIdentifiers.dstu7624ctr_512;
            final StringBuilder sb27 = new StringBuilder();
            sb27.append(Mappings.PREFIX);
            sb27.append("$CTR512");
            configurableProvider.addAlgorithm("Cipher", dstu7624ctr_130, sb27.toString());
            final ASN1ObjectIdentifier dstu7624ccm_128 = UAObjectIdentifiers.dstu7624ccm_128;
            final StringBuilder sb28 = new StringBuilder();
            sb28.append(Mappings.PREFIX);
            sb28.append("$CCM128");
            configurableProvider.addAlgorithm("Cipher", dstu7624ccm_128, sb28.toString());
            final ASN1ObjectIdentifier dstu7624ccm_129 = UAObjectIdentifiers.dstu7624ccm_256;
            final StringBuilder sb29 = new StringBuilder();
            sb29.append(Mappings.PREFIX);
            sb29.append("$CCM256");
            configurableProvider.addAlgorithm("Cipher", dstu7624ccm_129, sb29.toString());
            final ASN1ObjectIdentifier dstu7624ccm_130 = UAObjectIdentifiers.dstu7624ccm_512;
            final StringBuilder sb30 = new StringBuilder();
            sb30.append(Mappings.PREFIX);
            sb30.append("$CCM512");
            configurableProvider.addAlgorithm("Cipher", dstu7624ccm_130, sb30.toString());
            final StringBuilder sb31 = new StringBuilder();
            sb31.append(Mappings.PREFIX);
            sb31.append("$Wrap");
            configurableProvider.addAlgorithm("Cipher.DSTU7624KW", sb31.toString());
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.DSTU7624WRAP", "DSTU7624KW");
            final StringBuilder sb32 = new StringBuilder();
            sb32.append(Mappings.PREFIX);
            sb32.append("$Wrap128");
            configurableProvider.addAlgorithm("Cipher.DSTU7624-128KW", sb32.toString());
            final StringBuilder sb33 = new StringBuilder();
            sb33.append("Alg.Alias.Cipher.");
            sb33.append(UAObjectIdentifiers.dstu7624kw_128.getId());
            configurableProvider.addAlgorithm(sb33.toString(), "DSTU7624-128KW");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.DSTU7624-128WRAP", "DSTU7624-128KW");
            final StringBuilder sb34 = new StringBuilder();
            sb34.append(Mappings.PREFIX);
            sb34.append("$Wrap256");
            configurableProvider.addAlgorithm("Cipher.DSTU7624-256KW", sb34.toString());
            final StringBuilder sb35 = new StringBuilder();
            sb35.append("Alg.Alias.Cipher.");
            sb35.append(UAObjectIdentifiers.dstu7624kw_256.getId());
            configurableProvider.addAlgorithm(sb35.toString(), "DSTU7624-256KW");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.DSTU7624-256WRAP", "DSTU7624-256KW");
            final StringBuilder sb36 = new StringBuilder();
            sb36.append(Mappings.PREFIX);
            sb36.append("$Wrap512");
            configurableProvider.addAlgorithm("Cipher.DSTU7624-512KW", sb36.toString());
            final StringBuilder sb37 = new StringBuilder();
            sb37.append("Alg.Alias.Cipher.");
            sb37.append(UAObjectIdentifiers.dstu7624kw_512.getId());
            configurableProvider.addAlgorithm(sb37.toString(), "DSTU7624-512KW");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.DSTU7624-512WRAP", "DSTU7624-512KW");
            final StringBuilder sb38 = new StringBuilder();
            sb38.append(Mappings.PREFIX);
            sb38.append("$GMAC");
            configurableProvider.addAlgorithm("Mac.DSTU7624GMAC", sb38.toString());
            final StringBuilder sb39 = new StringBuilder();
            sb39.append(Mappings.PREFIX);
            sb39.append("$GMAC128");
            configurableProvider.addAlgorithm("Mac.DSTU7624-128GMAC", sb39.toString());
            final StringBuilder sb40 = new StringBuilder();
            sb40.append("Alg.Alias.Mac.");
            sb40.append(UAObjectIdentifiers.dstu7624gmac_128.getId());
            configurableProvider.addAlgorithm(sb40.toString(), "DSTU7624-128GMAC");
            final StringBuilder sb41 = new StringBuilder();
            sb41.append(Mappings.PREFIX);
            sb41.append("$GMAC256");
            configurableProvider.addAlgorithm("Mac.DSTU7624-256GMAC", sb41.toString());
            final StringBuilder sb42 = new StringBuilder();
            sb42.append("Alg.Alias.Mac.");
            sb42.append(UAObjectIdentifiers.dstu7624gmac_256.getId());
            configurableProvider.addAlgorithm(sb42.toString(), "DSTU7624-256GMAC");
            final StringBuilder sb43 = new StringBuilder();
            sb43.append(Mappings.PREFIX);
            sb43.append("$GMAC512");
            configurableProvider.addAlgorithm("Mac.DSTU7624-512GMAC", sb43.toString());
            final StringBuilder sb44 = new StringBuilder();
            sb44.append("Alg.Alias.Mac.");
            sb44.append(UAObjectIdentifiers.dstu7624gmac_512.getId());
            configurableProvider.addAlgorithm(sb44.toString(), "DSTU7624-512GMAC");
            final StringBuilder sb45 = new StringBuilder();
            sb45.append(Mappings.PREFIX);
            sb45.append("$KeyGen");
            configurableProvider.addAlgorithm("KeyGenerator.DSTU7624", sb45.toString());
            final ASN1ObjectIdentifier dstu7624kw_128 = UAObjectIdentifiers.dstu7624kw_128;
            final StringBuilder sb46 = new StringBuilder();
            sb46.append(Mappings.PREFIX);
            sb46.append("$KeyGen128");
            configurableProvider.addAlgorithm("KeyGenerator", dstu7624kw_128, sb46.toString());
            final ASN1ObjectIdentifier dstu7624kw_129 = UAObjectIdentifiers.dstu7624kw_256;
            final StringBuilder sb47 = new StringBuilder();
            sb47.append(Mappings.PREFIX);
            sb47.append("$KeyGen256");
            configurableProvider.addAlgorithm("KeyGenerator", dstu7624kw_129, sb47.toString());
            final ASN1ObjectIdentifier dstu7624kw_130 = UAObjectIdentifiers.dstu7624kw_512;
            final StringBuilder sb48 = new StringBuilder();
            sb48.append(Mappings.PREFIX);
            sb48.append("$KeyGen512");
            configurableProvider.addAlgorithm("KeyGenerator", dstu7624kw_130, sb48.toString());
            final ASN1ObjectIdentifier dstu7624ecb_131 = UAObjectIdentifiers.dstu7624ecb_128;
            final StringBuilder sb49 = new StringBuilder();
            sb49.append(Mappings.PREFIX);
            sb49.append("$KeyGen128");
            configurableProvider.addAlgorithm("KeyGenerator", dstu7624ecb_131, sb49.toString());
            final ASN1ObjectIdentifier dstu7624ecb_132 = UAObjectIdentifiers.dstu7624ecb_256;
            final StringBuilder sb50 = new StringBuilder();
            sb50.append(Mappings.PREFIX);
            sb50.append("$KeyGen256");
            configurableProvider.addAlgorithm("KeyGenerator", dstu7624ecb_132, sb50.toString());
            final ASN1ObjectIdentifier dstu7624ecb_133 = UAObjectIdentifiers.dstu7624ecb_512;
            final StringBuilder sb51 = new StringBuilder();
            sb51.append(Mappings.PREFIX);
            sb51.append("$KeyGen512");
            configurableProvider.addAlgorithm("KeyGenerator", dstu7624ecb_133, sb51.toString());
            final ASN1ObjectIdentifier dstu7624cbc_137 = UAObjectIdentifiers.dstu7624cbc_128;
            final StringBuilder sb52 = new StringBuilder();
            sb52.append(Mappings.PREFIX);
            sb52.append("$KeyGen128");
            configurableProvider.addAlgorithm("KeyGenerator", dstu7624cbc_137, sb52.toString());
            final ASN1ObjectIdentifier dstu7624cbc_138 = UAObjectIdentifiers.dstu7624cbc_256;
            final StringBuilder sb53 = new StringBuilder();
            sb53.append(Mappings.PREFIX);
            sb53.append("$KeyGen256");
            configurableProvider.addAlgorithm("KeyGenerator", dstu7624cbc_138, sb53.toString());
            final ASN1ObjectIdentifier dstu7624cbc_139 = UAObjectIdentifiers.dstu7624cbc_512;
            final StringBuilder sb54 = new StringBuilder();
            sb54.append(Mappings.PREFIX);
            sb54.append("$KeyGen512");
            configurableProvider.addAlgorithm("KeyGenerator", dstu7624cbc_139, sb54.toString());
            final ASN1ObjectIdentifier dstu7624ofb_131 = UAObjectIdentifiers.dstu7624ofb_128;
            final StringBuilder sb55 = new StringBuilder();
            sb55.append(Mappings.PREFIX);
            sb55.append("$KeyGen128");
            configurableProvider.addAlgorithm("KeyGenerator", dstu7624ofb_131, sb55.toString());
            final ASN1ObjectIdentifier dstu7624ofb_132 = UAObjectIdentifiers.dstu7624ofb_256;
            final StringBuilder sb56 = new StringBuilder();
            sb56.append(Mappings.PREFIX);
            sb56.append("$KeyGen256");
            configurableProvider.addAlgorithm("KeyGenerator", dstu7624ofb_132, sb56.toString());
            final ASN1ObjectIdentifier dstu7624ofb_133 = UAObjectIdentifiers.dstu7624ofb_512;
            final StringBuilder sb57 = new StringBuilder();
            sb57.append(Mappings.PREFIX);
            sb57.append("$KeyGen512");
            configurableProvider.addAlgorithm("KeyGenerator", dstu7624ofb_133, sb57.toString());
            final ASN1ObjectIdentifier dstu7624cfb_131 = UAObjectIdentifiers.dstu7624cfb_128;
            final StringBuilder sb58 = new StringBuilder();
            sb58.append(Mappings.PREFIX);
            sb58.append("$KeyGen128");
            configurableProvider.addAlgorithm("KeyGenerator", dstu7624cfb_131, sb58.toString());
            final ASN1ObjectIdentifier dstu7624cfb_132 = UAObjectIdentifiers.dstu7624cfb_256;
            final StringBuilder sb59 = new StringBuilder();
            sb59.append(Mappings.PREFIX);
            sb59.append("$KeyGen256");
            configurableProvider.addAlgorithm("KeyGenerator", dstu7624cfb_132, sb59.toString());
            final ASN1ObjectIdentifier dstu7624cfb_133 = UAObjectIdentifiers.dstu7624cfb_512;
            final StringBuilder sb60 = new StringBuilder();
            sb60.append(Mappings.PREFIX);
            sb60.append("$KeyGen512");
            configurableProvider.addAlgorithm("KeyGenerator", dstu7624cfb_133, sb60.toString());
            final ASN1ObjectIdentifier dstu7624ctr_131 = UAObjectIdentifiers.dstu7624ctr_128;
            final StringBuilder sb61 = new StringBuilder();
            sb61.append(Mappings.PREFIX);
            sb61.append("$KeyGen128");
            configurableProvider.addAlgorithm("KeyGenerator", dstu7624ctr_131, sb61.toString());
            final ASN1ObjectIdentifier dstu7624ctr_132 = UAObjectIdentifiers.dstu7624ctr_256;
            final StringBuilder sb62 = new StringBuilder();
            sb62.append(Mappings.PREFIX);
            sb62.append("$KeyGen256");
            configurableProvider.addAlgorithm("KeyGenerator", dstu7624ctr_132, sb62.toString());
            final ASN1ObjectIdentifier dstu7624ctr_133 = UAObjectIdentifiers.dstu7624ctr_512;
            final StringBuilder sb63 = new StringBuilder();
            sb63.append(Mappings.PREFIX);
            sb63.append("$KeyGen512");
            configurableProvider.addAlgorithm("KeyGenerator", dstu7624ctr_133, sb63.toString());
            final ASN1ObjectIdentifier dstu7624ccm_131 = UAObjectIdentifiers.dstu7624ccm_128;
            final StringBuilder sb64 = new StringBuilder();
            sb64.append(Mappings.PREFIX);
            sb64.append("$KeyGen128");
            configurableProvider.addAlgorithm("KeyGenerator", dstu7624ccm_131, sb64.toString());
            final ASN1ObjectIdentifier dstu7624ccm_132 = UAObjectIdentifiers.dstu7624ccm_256;
            final StringBuilder sb65 = new StringBuilder();
            sb65.append(Mappings.PREFIX);
            sb65.append("$KeyGen256");
            configurableProvider.addAlgorithm("KeyGenerator", dstu7624ccm_132, sb65.toString());
            final ASN1ObjectIdentifier dstu7624ccm_133 = UAObjectIdentifiers.dstu7624ccm_512;
            final StringBuilder sb66 = new StringBuilder();
            sb66.append(Mappings.PREFIX);
            sb66.append("$KeyGen512");
            configurableProvider.addAlgorithm("KeyGenerator", dstu7624ccm_133, sb66.toString());
            final ASN1ObjectIdentifier dstu7624gmac_128 = UAObjectIdentifiers.dstu7624gmac_128;
            final StringBuilder sb67 = new StringBuilder();
            sb67.append(Mappings.PREFIX);
            sb67.append("$KeyGen128");
            configurableProvider.addAlgorithm("KeyGenerator", dstu7624gmac_128, sb67.toString());
            final ASN1ObjectIdentifier dstu7624gmac_129 = UAObjectIdentifiers.dstu7624gmac_256;
            final StringBuilder sb68 = new StringBuilder();
            sb68.append(Mappings.PREFIX);
            sb68.append("$KeyGen256");
            configurableProvider.addAlgorithm("KeyGenerator", dstu7624gmac_129, sb68.toString());
            final ASN1ObjectIdentifier dstu7624gmac_130 = UAObjectIdentifiers.dstu7624gmac_512;
            final StringBuilder sb69 = new StringBuilder();
            sb69.append(Mappings.PREFIX);
            sb69.append("$KeyGen512");
            configurableProvider.addAlgorithm("KeyGenerator", dstu7624gmac_130, sb69.toString());
        }
    }
    
    public static class OFB128 extends BaseBlockCipher
    {
        public OFB128() {
            super(new BufferedBlockCipher(new OFBBlockCipher(new DSTU7624Engine(128), 128)), 128);
        }
    }
    
    public static class OFB256 extends BaseBlockCipher
    {
        public OFB256() {
            super(new BufferedBlockCipher(new OFBBlockCipher(new DSTU7624Engine(256), 256)), 256);
        }
    }
    
    public static class OFB512 extends BaseBlockCipher
    {
        public OFB512() {
            super(new BufferedBlockCipher(new OFBBlockCipher(new DSTU7624Engine(512), 512)), 512);
        }
    }
    
    public static class Wrap extends BaseWrapCipher
    {
        public Wrap() {
            super(new DSTU7624WrapEngine(128));
        }
    }
    
    public static class Wrap128 extends BaseWrapCipher
    {
        public Wrap128() {
            super(new DSTU7624WrapEngine(128));
        }
    }
    
    public static class Wrap256 extends BaseWrapCipher
    {
        public Wrap256() {
            super(new DSTU7624WrapEngine(256));
        }
    }
    
    public static class Wrap512 extends BaseWrapCipher
    {
        public Wrap512() {
            super(new DSTU7624WrapEngine(512));
        }
    }
}
