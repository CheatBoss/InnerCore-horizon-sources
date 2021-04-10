package org.spongycastle.jcajce.provider.symmetric;

import javax.crypto.spec.*;
import java.security.*;
import java.io.*;
import org.spongycastle.jcajce.spec.*;
import java.security.spec.*;
import org.spongycastle.asn1.cms.*;
import org.spongycastle.jcajce.provider.config.*;
import org.spongycastle.asn1.nsri.*;
import org.spongycastle.asn1.*;
import org.spongycastle.crypto.modes.*;
import org.spongycastle.crypto.macs.*;
import org.spongycastle.crypto.generators.*;
import org.spongycastle.jcajce.provider.symmetric.util.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.engines.*;

public final class ARIA
{
    private ARIA() {
    }
    
    public static class AlgParamGen extends BaseAlgorithmParameterGenerator
    {
        @Override
        protected AlgorithmParameters engineGenerateParameters() {
            final byte[] array = new byte[16];
            if (this.random == null) {
                this.random = new SecureRandom();
            }
            this.random.nextBytes(array);
            try {
                final AlgorithmParameters parametersInstance = this.createParametersInstance("ARIA");
                parametersInstance.init(new IvParameterSpec(array));
                return parametersInstance;
            }
            catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }
        
        @Override
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for ARIA parameter generation.");
        }
    }
    
    public static class AlgParams extends IvAlgorithmParameters
    {
        @Override
        protected String engineToString() {
            return "ARIA IV";
        }
    }
    
    public static class AlgParamsCCM extends BaseAlgorithmParameters
    {
        private CCMParameters ccmParams;
        
        @Override
        protected byte[] engineGetEncoded() throws IOException {
            return this.ccmParams.getEncoded();
        }
        
        @Override
        protected byte[] engineGetEncoded(final String s) throws IOException {
            if (this.isASN1FormatString(s)) {
                return this.ccmParams.getEncoded();
            }
            throw new IOException("unknown format specified");
        }
        
        @Override
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
            if (GcmSpecUtil.isGcmSpec(algorithmParameterSpec)) {
                this.ccmParams = CCMParameters.getInstance(GcmSpecUtil.extractGcmParameters(algorithmParameterSpec));
                return;
            }
            if (algorithmParameterSpec instanceof AEADParameterSpec) {
                final AEADParameterSpec aeadParameterSpec = (AEADParameterSpec)algorithmParameterSpec;
                this.ccmParams = new CCMParameters(aeadParameterSpec.getNonce(), aeadParameterSpec.getMacSizeInBits() / 8);
                return;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("AlgorithmParameterSpec class not recognized: ");
            sb.append(algorithmParameterSpec.getClass().getName());
            throw new InvalidParameterSpecException(sb.toString());
        }
        
        @Override
        protected void engineInit(final byte[] array) throws IOException {
            this.ccmParams = CCMParameters.getInstance(array);
        }
        
        @Override
        protected void engineInit(final byte[] array, final String s) throws IOException {
            if (this.isASN1FormatString(s)) {
                this.ccmParams = CCMParameters.getInstance(array);
                return;
            }
            throw new IOException("unknown format specified");
        }
        
        @Override
        protected String engineToString() {
            return "CCM";
        }
        
        @Override
        protected AlgorithmParameterSpec localEngineGetParameterSpec(final Class clazz) throws InvalidParameterSpecException {
            if (clazz != AlgorithmParameterSpec.class && !GcmSpecUtil.isGcmSpec(clazz)) {
                if (clazz == AEADParameterSpec.class) {
                    return new AEADParameterSpec(this.ccmParams.getNonce(), this.ccmParams.getIcvLen() * 8);
                }
                if (clazz == IvParameterSpec.class) {
                    return new IvParameterSpec(this.ccmParams.getNonce());
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("AlgorithmParameterSpec not recognized: ");
                sb.append(clazz.getName());
                throw new InvalidParameterSpecException(sb.toString());
            }
            else {
                if (GcmSpecUtil.gcmSpecExists()) {
                    return GcmSpecUtil.extractGcmSpec(this.ccmParams.toASN1Primitive());
                }
                return new AEADParameterSpec(this.ccmParams.getNonce(), this.ccmParams.getIcvLen() * 8);
            }
        }
    }
    
    public static class AlgParamsGCM extends BaseAlgorithmParameters
    {
        private GCMParameters gcmParams;
        
        @Override
        protected byte[] engineGetEncoded() throws IOException {
            return this.gcmParams.getEncoded();
        }
        
        @Override
        protected byte[] engineGetEncoded(final String s) throws IOException {
            if (this.isASN1FormatString(s)) {
                return this.gcmParams.getEncoded();
            }
            throw new IOException("unknown format specified");
        }
        
        @Override
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
            if (GcmSpecUtil.isGcmSpec(algorithmParameterSpec)) {
                this.gcmParams = GcmSpecUtil.extractGcmParameters(algorithmParameterSpec);
                return;
            }
            if (algorithmParameterSpec instanceof AEADParameterSpec) {
                final AEADParameterSpec aeadParameterSpec = (AEADParameterSpec)algorithmParameterSpec;
                this.gcmParams = new GCMParameters(aeadParameterSpec.getNonce(), aeadParameterSpec.getMacSizeInBits() / 8);
                return;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("AlgorithmParameterSpec class not recognized: ");
            sb.append(algorithmParameterSpec.getClass().getName());
            throw new InvalidParameterSpecException(sb.toString());
        }
        
        @Override
        protected void engineInit(final byte[] array) throws IOException {
            this.gcmParams = GCMParameters.getInstance(array);
        }
        
        @Override
        protected void engineInit(final byte[] array, final String s) throws IOException {
            if (this.isASN1FormatString(s)) {
                this.gcmParams = GCMParameters.getInstance(array);
                return;
            }
            throw new IOException("unknown format specified");
        }
        
        @Override
        protected String engineToString() {
            return "GCM";
        }
        
        @Override
        protected AlgorithmParameterSpec localEngineGetParameterSpec(final Class clazz) throws InvalidParameterSpecException {
            if (clazz != AlgorithmParameterSpec.class && !GcmSpecUtil.isGcmSpec(clazz)) {
                if (clazz == AEADParameterSpec.class) {
                    return new AEADParameterSpec(this.gcmParams.getNonce(), this.gcmParams.getIcvLen() * 8);
                }
                if (clazz == IvParameterSpec.class) {
                    return new IvParameterSpec(this.gcmParams.getNonce());
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("AlgorithmParameterSpec not recognized: ");
                sb.append(clazz.getName());
                throw new InvalidParameterSpecException(sb.toString());
            }
            else {
                if (GcmSpecUtil.gcmSpecExists()) {
                    return GcmSpecUtil.extractGcmSpec(this.gcmParams.toASN1Primitive());
                }
                return new AEADParameterSpec(this.gcmParams.getNonce(), this.gcmParams.getIcvLen() * 8);
            }
        }
    }
    
    public static class CBC extends BaseBlockCipher
    {
        public CBC() {
            super(new CBCBlockCipher(new ARIAEngine()), 128);
        }
    }
    
    public static class CFB extends BaseBlockCipher
    {
        public CFB() {
            super(new BufferedBlockCipher(new CFBBlockCipher(new ARIAEngine(), 128)), 128);
        }
    }
    
    public static class ECB extends BaseBlockCipher
    {
        public ECB() {
            super(new BlockCipherProvider() {
                @Override
                public BlockCipher get() {
                    return new ARIAEngine();
                }
            });
        }
    }
    
    public static class GMAC extends BaseMac
    {
        public GMAC() {
            super(new GMac(new GCMBlockCipher(new ARIAEngine())));
        }
    }
    
    public static class KeyGen extends BaseKeyGenerator
    {
        public KeyGen() {
            this(256);
        }
        
        public KeyGen(final int n) {
            super("ARIA", n, new CipherKeyGenerator());
        }
    }
    
    public static class KeyGen128 extends KeyGen
    {
        public KeyGen128() {
            super(128);
        }
    }
    
    public static class KeyGen192 extends KeyGen
    {
        public KeyGen192() {
            super(192);
        }
    }
    
    public static class KeyGen256 extends KeyGen
    {
        public KeyGen256() {
            super(256);
        }
    }
    
    public static class Mappings extends SymmetricAlgorithmProvider
    {
        private static final String PREFIX;
        
        static {
            PREFIX = ARIA.class.getName();
        }
        
        @Override
        public void configure(final ConfigurableProvider configurableProvider) {
            final StringBuilder sb = new StringBuilder();
            sb.append(Mappings.PREFIX);
            sb.append("$AlgParams");
            configurableProvider.addAlgorithm("AlgorithmParameters.ARIA", sb.toString());
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters", NSRIObjectIdentifiers.id_aria128_cbc, "ARIA");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters", NSRIObjectIdentifiers.id_aria192_cbc, "ARIA");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters", NSRIObjectIdentifiers.id_aria256_cbc, "ARIA");
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(Mappings.PREFIX);
            sb2.append("$AlgParamGen");
            configurableProvider.addAlgorithm("AlgorithmParameterGenerator.ARIA", sb2.toString());
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameterGenerator", NSRIObjectIdentifiers.id_aria128_cbc, "ARIA");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameterGenerator", NSRIObjectIdentifiers.id_aria192_cbc, "ARIA");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameterGenerator", NSRIObjectIdentifiers.id_aria256_cbc, "ARIA");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameterGenerator", NSRIObjectIdentifiers.id_aria128_ofb, "ARIA");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameterGenerator", NSRIObjectIdentifiers.id_aria192_ofb, "ARIA");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameterGenerator", NSRIObjectIdentifiers.id_aria256_ofb, "ARIA");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameterGenerator", NSRIObjectIdentifiers.id_aria128_cfb, "ARIA");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameterGenerator", NSRIObjectIdentifiers.id_aria192_cfb, "ARIA");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameterGenerator", NSRIObjectIdentifiers.id_aria256_cfb, "ARIA");
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(Mappings.PREFIX);
            sb3.append("$ECB");
            configurableProvider.addAlgorithm("Cipher.ARIA", sb3.toString());
            final ASN1ObjectIdentifier id_aria128_ecb = NSRIObjectIdentifiers.id_aria128_ecb;
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(Mappings.PREFIX);
            sb4.append("$ECB");
            configurableProvider.addAlgorithm("Cipher", id_aria128_ecb, sb4.toString());
            final ASN1ObjectIdentifier id_aria192_ecb = NSRIObjectIdentifiers.id_aria192_ecb;
            final StringBuilder sb5 = new StringBuilder();
            sb5.append(Mappings.PREFIX);
            sb5.append("$ECB");
            configurableProvider.addAlgorithm("Cipher", id_aria192_ecb, sb5.toString());
            final ASN1ObjectIdentifier id_aria256_ecb = NSRIObjectIdentifiers.id_aria256_ecb;
            final StringBuilder sb6 = new StringBuilder();
            sb6.append(Mappings.PREFIX);
            sb6.append("$ECB");
            configurableProvider.addAlgorithm("Cipher", id_aria256_ecb, sb6.toString());
            final ASN1ObjectIdentifier id_aria128_cbc = NSRIObjectIdentifiers.id_aria128_cbc;
            final StringBuilder sb7 = new StringBuilder();
            sb7.append(Mappings.PREFIX);
            sb7.append("$CBC");
            configurableProvider.addAlgorithm("Cipher", id_aria128_cbc, sb7.toString());
            final ASN1ObjectIdentifier id_aria192_cbc = NSRIObjectIdentifiers.id_aria192_cbc;
            final StringBuilder sb8 = new StringBuilder();
            sb8.append(Mappings.PREFIX);
            sb8.append("$CBC");
            configurableProvider.addAlgorithm("Cipher", id_aria192_cbc, sb8.toString());
            final ASN1ObjectIdentifier id_aria256_cbc = NSRIObjectIdentifiers.id_aria256_cbc;
            final StringBuilder sb9 = new StringBuilder();
            sb9.append(Mappings.PREFIX);
            sb9.append("$CBC");
            configurableProvider.addAlgorithm("Cipher", id_aria256_cbc, sb9.toString());
            final ASN1ObjectIdentifier id_aria128_cfb = NSRIObjectIdentifiers.id_aria128_cfb;
            final StringBuilder sb10 = new StringBuilder();
            sb10.append(Mappings.PREFIX);
            sb10.append("$CFB");
            configurableProvider.addAlgorithm("Cipher", id_aria128_cfb, sb10.toString());
            final ASN1ObjectIdentifier id_aria192_cfb = NSRIObjectIdentifiers.id_aria192_cfb;
            final StringBuilder sb11 = new StringBuilder();
            sb11.append(Mappings.PREFIX);
            sb11.append("$CFB");
            configurableProvider.addAlgorithm("Cipher", id_aria192_cfb, sb11.toString());
            final ASN1ObjectIdentifier id_aria256_cfb = NSRIObjectIdentifiers.id_aria256_cfb;
            final StringBuilder sb12 = new StringBuilder();
            sb12.append(Mappings.PREFIX);
            sb12.append("$CFB");
            configurableProvider.addAlgorithm("Cipher", id_aria256_cfb, sb12.toString());
            final ASN1ObjectIdentifier id_aria128_ofb = NSRIObjectIdentifiers.id_aria128_ofb;
            final StringBuilder sb13 = new StringBuilder();
            sb13.append(Mappings.PREFIX);
            sb13.append("$OFB");
            configurableProvider.addAlgorithm("Cipher", id_aria128_ofb, sb13.toString());
            final ASN1ObjectIdentifier id_aria192_ofb = NSRIObjectIdentifiers.id_aria192_ofb;
            final StringBuilder sb14 = new StringBuilder();
            sb14.append(Mappings.PREFIX);
            sb14.append("$OFB");
            configurableProvider.addAlgorithm("Cipher", id_aria192_ofb, sb14.toString());
            final ASN1ObjectIdentifier id_aria256_ofb = NSRIObjectIdentifiers.id_aria256_ofb;
            final StringBuilder sb15 = new StringBuilder();
            sb15.append(Mappings.PREFIX);
            sb15.append("$OFB");
            configurableProvider.addAlgorithm("Cipher", id_aria256_ofb, sb15.toString());
            final StringBuilder sb16 = new StringBuilder();
            sb16.append(Mappings.PREFIX);
            sb16.append("$RFC3211Wrap");
            configurableProvider.addAlgorithm("Cipher.ARIARFC3211WRAP", sb16.toString());
            final StringBuilder sb17 = new StringBuilder();
            sb17.append(Mappings.PREFIX);
            sb17.append("$Wrap");
            configurableProvider.addAlgorithm("Cipher.ARIAWRAP", sb17.toString());
            configurableProvider.addAlgorithm("Alg.Alias.Cipher", NSRIObjectIdentifiers.id_aria128_kw, "ARIAWRAP");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher", NSRIObjectIdentifiers.id_aria192_kw, "ARIAWRAP");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher", NSRIObjectIdentifiers.id_aria256_kw, "ARIAWRAP");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.ARIAKW", "ARIAWRAP");
            final StringBuilder sb18 = new StringBuilder();
            sb18.append(Mappings.PREFIX);
            sb18.append("$WrapPad");
            configurableProvider.addAlgorithm("Cipher.ARIAWRAPPAD", sb18.toString());
            configurableProvider.addAlgorithm("Alg.Alias.Cipher", NSRIObjectIdentifiers.id_aria128_kwp, "ARIAWRAPPAD");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher", NSRIObjectIdentifiers.id_aria192_kwp, "ARIAWRAPPAD");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher", NSRIObjectIdentifiers.id_aria256_kwp, "ARIAWRAPPAD");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.ARIAKWP", "ARIAWRAPPAD");
            final StringBuilder sb19 = new StringBuilder();
            sb19.append(Mappings.PREFIX);
            sb19.append("$KeyGen");
            configurableProvider.addAlgorithm("KeyGenerator.ARIA", sb19.toString());
            final ASN1ObjectIdentifier id_aria128_kw = NSRIObjectIdentifiers.id_aria128_kw;
            final StringBuilder sb20 = new StringBuilder();
            sb20.append(Mappings.PREFIX);
            sb20.append("$KeyGen128");
            configurableProvider.addAlgorithm("KeyGenerator", id_aria128_kw, sb20.toString());
            final ASN1ObjectIdentifier id_aria192_kw = NSRIObjectIdentifiers.id_aria192_kw;
            final StringBuilder sb21 = new StringBuilder();
            sb21.append(Mappings.PREFIX);
            sb21.append("$KeyGen192");
            configurableProvider.addAlgorithm("KeyGenerator", id_aria192_kw, sb21.toString());
            final ASN1ObjectIdentifier id_aria256_kw = NSRIObjectIdentifiers.id_aria256_kw;
            final StringBuilder sb22 = new StringBuilder();
            sb22.append(Mappings.PREFIX);
            sb22.append("$KeyGen256");
            configurableProvider.addAlgorithm("KeyGenerator", id_aria256_kw, sb22.toString());
            final ASN1ObjectIdentifier id_aria128_kwp = NSRIObjectIdentifiers.id_aria128_kwp;
            final StringBuilder sb23 = new StringBuilder();
            sb23.append(Mappings.PREFIX);
            sb23.append("$KeyGen128");
            configurableProvider.addAlgorithm("KeyGenerator", id_aria128_kwp, sb23.toString());
            final ASN1ObjectIdentifier id_aria192_kwp = NSRIObjectIdentifiers.id_aria192_kwp;
            final StringBuilder sb24 = new StringBuilder();
            sb24.append(Mappings.PREFIX);
            sb24.append("$KeyGen192");
            configurableProvider.addAlgorithm("KeyGenerator", id_aria192_kwp, sb24.toString());
            final ASN1ObjectIdentifier id_aria256_kwp = NSRIObjectIdentifiers.id_aria256_kwp;
            final StringBuilder sb25 = new StringBuilder();
            sb25.append(Mappings.PREFIX);
            sb25.append("$KeyGen256");
            configurableProvider.addAlgorithm("KeyGenerator", id_aria256_kwp, sb25.toString());
            final ASN1ObjectIdentifier id_aria128_ecb2 = NSRIObjectIdentifiers.id_aria128_ecb;
            final StringBuilder sb26 = new StringBuilder();
            sb26.append(Mappings.PREFIX);
            sb26.append("$KeyGen128");
            configurableProvider.addAlgorithm("KeyGenerator", id_aria128_ecb2, sb26.toString());
            final ASN1ObjectIdentifier id_aria192_ecb2 = NSRIObjectIdentifiers.id_aria192_ecb;
            final StringBuilder sb27 = new StringBuilder();
            sb27.append(Mappings.PREFIX);
            sb27.append("$KeyGen192");
            configurableProvider.addAlgorithm("KeyGenerator", id_aria192_ecb2, sb27.toString());
            final ASN1ObjectIdentifier id_aria256_ecb2 = NSRIObjectIdentifiers.id_aria256_ecb;
            final StringBuilder sb28 = new StringBuilder();
            sb28.append(Mappings.PREFIX);
            sb28.append("$KeyGen256");
            configurableProvider.addAlgorithm("KeyGenerator", id_aria256_ecb2, sb28.toString());
            final ASN1ObjectIdentifier id_aria128_cbc2 = NSRIObjectIdentifiers.id_aria128_cbc;
            final StringBuilder sb29 = new StringBuilder();
            sb29.append(Mappings.PREFIX);
            sb29.append("$KeyGen128");
            configurableProvider.addAlgorithm("KeyGenerator", id_aria128_cbc2, sb29.toString());
            final ASN1ObjectIdentifier id_aria192_cbc2 = NSRIObjectIdentifiers.id_aria192_cbc;
            final StringBuilder sb30 = new StringBuilder();
            sb30.append(Mappings.PREFIX);
            sb30.append("$KeyGen192");
            configurableProvider.addAlgorithm("KeyGenerator", id_aria192_cbc2, sb30.toString());
            final ASN1ObjectIdentifier id_aria256_cbc2 = NSRIObjectIdentifiers.id_aria256_cbc;
            final StringBuilder sb31 = new StringBuilder();
            sb31.append(Mappings.PREFIX);
            sb31.append("$KeyGen256");
            configurableProvider.addAlgorithm("KeyGenerator", id_aria256_cbc2, sb31.toString());
            final ASN1ObjectIdentifier id_aria128_cfb2 = NSRIObjectIdentifiers.id_aria128_cfb;
            final StringBuilder sb32 = new StringBuilder();
            sb32.append(Mappings.PREFIX);
            sb32.append("$KeyGen128");
            configurableProvider.addAlgorithm("KeyGenerator", id_aria128_cfb2, sb32.toString());
            final ASN1ObjectIdentifier id_aria192_cfb2 = NSRIObjectIdentifiers.id_aria192_cfb;
            final StringBuilder sb33 = new StringBuilder();
            sb33.append(Mappings.PREFIX);
            sb33.append("$KeyGen192");
            configurableProvider.addAlgorithm("KeyGenerator", id_aria192_cfb2, sb33.toString());
            final ASN1ObjectIdentifier id_aria256_cfb2 = NSRIObjectIdentifiers.id_aria256_cfb;
            final StringBuilder sb34 = new StringBuilder();
            sb34.append(Mappings.PREFIX);
            sb34.append("$KeyGen256");
            configurableProvider.addAlgorithm("KeyGenerator", id_aria256_cfb2, sb34.toString());
            final ASN1ObjectIdentifier id_aria128_ofb2 = NSRIObjectIdentifiers.id_aria128_ofb;
            final StringBuilder sb35 = new StringBuilder();
            sb35.append(Mappings.PREFIX);
            sb35.append("$KeyGen128");
            configurableProvider.addAlgorithm("KeyGenerator", id_aria128_ofb2, sb35.toString());
            final ASN1ObjectIdentifier id_aria192_ofb2 = NSRIObjectIdentifiers.id_aria192_ofb;
            final StringBuilder sb36 = new StringBuilder();
            sb36.append(Mappings.PREFIX);
            sb36.append("$KeyGen192");
            configurableProvider.addAlgorithm("KeyGenerator", id_aria192_ofb2, sb36.toString());
            final ASN1ObjectIdentifier id_aria256_ofb2 = NSRIObjectIdentifiers.id_aria256_ofb;
            final StringBuilder sb37 = new StringBuilder();
            sb37.append(Mappings.PREFIX);
            sb37.append("$KeyGen256");
            configurableProvider.addAlgorithm("KeyGenerator", id_aria256_ofb2, sb37.toString());
            final ASN1ObjectIdentifier id_aria128_ccm = NSRIObjectIdentifiers.id_aria128_ccm;
            final StringBuilder sb38 = new StringBuilder();
            sb38.append(Mappings.PREFIX);
            sb38.append("$KeyGen128");
            configurableProvider.addAlgorithm("KeyGenerator", id_aria128_ccm, sb38.toString());
            final ASN1ObjectIdentifier id_aria192_ccm = NSRIObjectIdentifiers.id_aria192_ccm;
            final StringBuilder sb39 = new StringBuilder();
            sb39.append(Mappings.PREFIX);
            sb39.append("$KeyGen192");
            configurableProvider.addAlgorithm("KeyGenerator", id_aria192_ccm, sb39.toString());
            final ASN1ObjectIdentifier id_aria256_ccm = NSRIObjectIdentifiers.id_aria256_ccm;
            final StringBuilder sb40 = new StringBuilder();
            sb40.append(Mappings.PREFIX);
            sb40.append("$KeyGen256");
            configurableProvider.addAlgorithm("KeyGenerator", id_aria256_ccm, sb40.toString());
            final ASN1ObjectIdentifier id_aria128_gcm = NSRIObjectIdentifiers.id_aria128_gcm;
            final StringBuilder sb41 = new StringBuilder();
            sb41.append(Mappings.PREFIX);
            sb41.append("$KeyGen128");
            configurableProvider.addAlgorithm("KeyGenerator", id_aria128_gcm, sb41.toString());
            final ASN1ObjectIdentifier id_aria192_gcm = NSRIObjectIdentifiers.id_aria192_gcm;
            final StringBuilder sb42 = new StringBuilder();
            sb42.append(Mappings.PREFIX);
            sb42.append("$KeyGen192");
            configurableProvider.addAlgorithm("KeyGenerator", id_aria192_gcm, sb42.toString());
            final ASN1ObjectIdentifier id_aria256_gcm = NSRIObjectIdentifiers.id_aria256_gcm;
            final StringBuilder sb43 = new StringBuilder();
            sb43.append(Mappings.PREFIX);
            sb43.append("$KeyGen256");
            configurableProvider.addAlgorithm("KeyGenerator", id_aria256_gcm, sb43.toString());
            final StringBuilder sb44 = new StringBuilder();
            sb44.append(Mappings.PREFIX);
            sb44.append("$AlgParamGenCCM");
            configurableProvider.addAlgorithm("AlgorithmParameterGenerator.ARIACCM", sb44.toString());
            final StringBuilder sb45 = new StringBuilder();
            sb45.append("Alg.Alias.AlgorithmParameterGenerator.");
            sb45.append(NSRIObjectIdentifiers.id_aria128_ccm);
            configurableProvider.addAlgorithm(sb45.toString(), "CCM");
            final StringBuilder sb46 = new StringBuilder();
            sb46.append("Alg.Alias.AlgorithmParameterGenerator.");
            sb46.append(NSRIObjectIdentifiers.id_aria192_ccm);
            configurableProvider.addAlgorithm(sb46.toString(), "CCM");
            final StringBuilder sb47 = new StringBuilder();
            sb47.append("Alg.Alias.AlgorithmParameterGenerator.");
            sb47.append(NSRIObjectIdentifiers.id_aria256_ccm);
            configurableProvider.addAlgorithm(sb47.toString(), "CCM");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher", NSRIObjectIdentifiers.id_aria128_ccm, "CCM");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher", NSRIObjectIdentifiers.id_aria192_ccm, "CCM");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher", NSRIObjectIdentifiers.id_aria256_ccm, "CCM");
            final StringBuilder sb48 = new StringBuilder();
            sb48.append(Mappings.PREFIX);
            sb48.append("$AlgParamGenGCM");
            configurableProvider.addAlgorithm("AlgorithmParameterGenerator.ARIAGCM", sb48.toString());
            final StringBuilder sb49 = new StringBuilder();
            sb49.append("Alg.Alias.AlgorithmParameterGenerator.");
            sb49.append(NSRIObjectIdentifiers.id_aria128_gcm);
            configurableProvider.addAlgorithm(sb49.toString(), "GCM");
            final StringBuilder sb50 = new StringBuilder();
            sb50.append("Alg.Alias.AlgorithmParameterGenerator.");
            sb50.append(NSRIObjectIdentifiers.id_aria192_gcm);
            configurableProvider.addAlgorithm(sb50.toString(), "GCM");
            final StringBuilder sb51 = new StringBuilder();
            sb51.append("Alg.Alias.AlgorithmParameterGenerator.");
            sb51.append(NSRIObjectIdentifiers.id_aria256_gcm);
            configurableProvider.addAlgorithm(sb51.toString(), "GCM");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher", NSRIObjectIdentifiers.id_aria128_gcm, "GCM");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher", NSRIObjectIdentifiers.id_aria192_gcm, "GCM");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher", NSRIObjectIdentifiers.id_aria256_gcm, "GCM");
            final StringBuilder sb52 = new StringBuilder();
            sb52.append(Mappings.PREFIX);
            sb52.append("$GMAC");
            final String string = sb52.toString();
            final StringBuilder sb53 = new StringBuilder();
            sb53.append(Mappings.PREFIX);
            sb53.append("$KeyGen");
            this.addGMacAlgorithm(configurableProvider, "ARIA", string, sb53.toString());
            final StringBuilder sb54 = new StringBuilder();
            sb54.append(Mappings.PREFIX);
            sb54.append("$Poly1305");
            final String string2 = sb54.toString();
            final StringBuilder sb55 = new StringBuilder();
            sb55.append(Mappings.PREFIX);
            sb55.append("$Poly1305KeyGen");
            this.addPoly1305Algorithm(configurableProvider, "ARIA", string2, sb55.toString());
        }
    }
    
    public static class OFB extends BaseBlockCipher
    {
        public OFB() {
            super(new BufferedBlockCipher(new OFBBlockCipher(new ARIAEngine(), 128)), 128);
        }
    }
    
    public static class Poly1305 extends BaseMac
    {
        public Poly1305() {
            super(new org.spongycastle.crypto.macs.Poly1305(new ARIAEngine()));
        }
    }
    
    public static class Poly1305KeyGen extends BaseKeyGenerator
    {
        public Poly1305KeyGen() {
            super("Poly1305-ARIA", 256, new Poly1305KeyGenerator());
        }
    }
    
    public static class RFC3211Wrap extends BaseWrapCipher
    {
        public RFC3211Wrap() {
            super(new RFC3211WrapEngine(new ARIAEngine()), 16);
        }
    }
    
    public static class Wrap extends BaseWrapCipher
    {
        public Wrap() {
            super(new ARIAWrapEngine());
        }
    }
    
    public static class WrapPad extends BaseWrapCipher
    {
        public WrapPad() {
            super(new ARIAWrapPadEngine());
        }
    }
}
