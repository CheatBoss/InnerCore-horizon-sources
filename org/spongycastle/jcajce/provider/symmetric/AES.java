package org.spongycastle.jcajce.provider.symmetric;

import java.util.*;
import javax.crypto.spec.*;
import java.security.*;
import org.spongycastle.asn1.cms.*;
import java.io.*;
import org.spongycastle.jcajce.spec.*;
import java.security.spec.*;
import org.spongycastle.asn1.*;
import org.spongycastle.jcajce.provider.config.*;
import org.spongycastle.asn1.nist.*;
import org.spongycastle.asn1.bc.*;
import org.spongycastle.crypto.modes.*;
import org.spongycastle.crypto.macs.*;
import org.spongycastle.crypto.generators.*;
import org.spongycastle.jcajce.provider.symmetric.util.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.engines.*;

public final class AES
{
    private static final Map<String, String> generalAesAttributes;
    
    static {
        (generalAesAttributes = new HashMap<String, String>()).put("SupportedKeyClasses", "javax.crypto.SecretKey");
        AES.generalAesAttributes.put("SupportedKeyFormats", "RAW");
    }
    
    private AES() {
    }
    
    public static class AESCCMMAC extends BaseMac
    {
        public AESCCMMAC() {
            super(new CCMMac());
        }
        
        private static class CCMMac implements Mac
        {
            private final CCMBlockCipher ccm;
            private int macLength;
            
            private CCMMac() {
                this.ccm = new CCMBlockCipher(new AESEngine());
                this.macLength = 8;
            }
            
            @Override
            public int doFinal(final byte[] array, int doFinal) throws DataLengthException, IllegalStateException {
                try {
                    doFinal = this.ccm.doFinal(array, 0);
                    return doFinal;
                }
                catch (InvalidCipherTextException ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("exception on doFinal(): ");
                    sb.append(ex.toString());
                    throw new IllegalStateException(sb.toString());
                }
            }
            
            @Override
            public String getAlgorithmName() {
                final StringBuilder sb = new StringBuilder();
                sb.append(this.ccm.getAlgorithmName());
                sb.append("Mac");
                return sb.toString();
            }
            
            @Override
            public int getMacSize() {
                return this.macLength;
            }
            
            @Override
            public void init(final CipherParameters cipherParameters) throws IllegalArgumentException {
                this.ccm.init(true, cipherParameters);
                this.macLength = this.ccm.getMac().length;
            }
            
            @Override
            public void reset() {
                this.ccm.reset();
            }
            
            @Override
            public void update(final byte b) throws IllegalStateException {
                this.ccm.processAADByte(b);
            }
            
            @Override
            public void update(final byte[] array, final int n, final int n2) throws DataLengthException, IllegalStateException {
                this.ccm.processAADBytes(array, n, n2);
            }
        }
    }
    
    public static class AESCMAC extends BaseMac
    {
        public AESCMAC() {
            super(new CMac(new AESEngine()));
        }
    }
    
    public static class AESGMAC extends BaseMac
    {
        public AESGMAC() {
            super(new GMac(new GCMBlockCipher(new AESEngine())));
        }
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
                final AlgorithmParameters parametersInstance = this.createParametersInstance("AES");
                parametersInstance.init(new IvParameterSpec(array));
                return parametersInstance;
            }
            catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }
        
        @Override
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for AES parameter generation.");
        }
    }
    
    public static class AlgParamGenCCM extends BaseAlgorithmParameterGenerator
    {
        @Override
        protected AlgorithmParameters engineGenerateParameters() {
            final byte[] array = new byte[12];
            if (this.random == null) {
                this.random = new SecureRandom();
            }
            this.random.nextBytes(array);
            try {
                final AlgorithmParameters parametersInstance = this.createParametersInstance("CCM");
                parametersInstance.init(new CCMParameters(array, 12).getEncoded());
                return parametersInstance;
            }
            catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }
        
        @Override
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for AES parameter generation.");
        }
    }
    
    public static class AlgParamGenGCM extends BaseAlgorithmParameterGenerator
    {
        @Override
        protected AlgorithmParameters engineGenerateParameters() {
            final byte[] array = new byte[12];
            if (this.random == null) {
                this.random = new SecureRandom();
            }
            this.random.nextBytes(array);
            try {
                final AlgorithmParameters parametersInstance = this.createParametersInstance("GCM");
                parametersInstance.init(new GCMParameters(array, 16).getEncoded());
                return parametersInstance;
            }
            catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }
        
        @Override
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for AES parameter generation.");
        }
    }
    
    public static class AlgParams extends IvAlgorithmParameters
    {
        @Override
        protected String engineToString() {
            return "AES IV";
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
            super(new CBCBlockCipher(new AESEngine()), 128);
        }
    }
    
    public static class CCM extends BaseBlockCipher
    {
        public CCM() {
            super(new CCMBlockCipher(new AESEngine()), false, 16);
        }
    }
    
    public static class CFB extends BaseBlockCipher
    {
        public CFB() {
            super(new BufferedBlockCipher(new CFBBlockCipher(new AESEngine(), 128)), 128);
        }
    }
    
    public static class ECB extends BaseBlockCipher
    {
        public ECB() {
            super(new BlockCipherProvider() {
                @Override
                public BlockCipher get() {
                    return new AESEngine();
                }
            });
        }
    }
    
    public static class GCM extends BaseBlockCipher
    {
        public GCM() {
            super(new GCMBlockCipher(new AESEngine()));
        }
    }
    
    public static class KeyFactory extends BaseSecretKeyFactory
    {
        public KeyFactory() {
            super("AES", null);
        }
    }
    
    public static class KeyGen extends BaseKeyGenerator
    {
        public KeyGen() {
            this(192);
        }
        
        public KeyGen(final int n) {
            super("AES", n, new CipherKeyGenerator());
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
        private static final String wrongAES128 = "2.16.840.1.101.3.4.2";
        private static final String wrongAES192 = "2.16.840.1.101.3.4.22";
        private static final String wrongAES256 = "2.16.840.1.101.3.4.42";
        
        static {
            PREFIX = AES.class.getName();
        }
        
        @Override
        public void configure(final ConfigurableProvider configurableProvider) {
            final StringBuilder sb = new StringBuilder();
            sb.append(Mappings.PREFIX);
            sb.append("$AlgParams");
            configurableProvider.addAlgorithm("AlgorithmParameters.AES", sb.toString());
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.2.16.840.1.101.3.4.2", "AES");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.2.16.840.1.101.3.4.22", "AES");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.2.16.840.1.101.3.4.42", "AES");
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Alg.Alias.AlgorithmParameters.");
            sb2.append(NISTObjectIdentifiers.id_aes128_CBC);
            configurableProvider.addAlgorithm(sb2.toString(), "AES");
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Alg.Alias.AlgorithmParameters.");
            sb3.append(NISTObjectIdentifiers.id_aes192_CBC);
            configurableProvider.addAlgorithm(sb3.toString(), "AES");
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Alg.Alias.AlgorithmParameters.");
            sb4.append(NISTObjectIdentifiers.id_aes256_CBC);
            configurableProvider.addAlgorithm(sb4.toString(), "AES");
            final StringBuilder sb5 = new StringBuilder();
            sb5.append(Mappings.PREFIX);
            sb5.append("$AlgParamsGCM");
            configurableProvider.addAlgorithm("AlgorithmParameters.GCM", sb5.toString());
            final StringBuilder sb6 = new StringBuilder();
            sb6.append("Alg.Alias.AlgorithmParameters.");
            sb6.append(NISTObjectIdentifiers.id_aes128_GCM);
            configurableProvider.addAlgorithm(sb6.toString(), "GCM");
            final StringBuilder sb7 = new StringBuilder();
            sb7.append("Alg.Alias.AlgorithmParameters.");
            sb7.append(NISTObjectIdentifiers.id_aes192_GCM);
            configurableProvider.addAlgorithm(sb7.toString(), "GCM");
            final StringBuilder sb8 = new StringBuilder();
            sb8.append("Alg.Alias.AlgorithmParameters.");
            sb8.append(NISTObjectIdentifiers.id_aes256_GCM);
            configurableProvider.addAlgorithm(sb8.toString(), "GCM");
            final StringBuilder sb9 = new StringBuilder();
            sb9.append(Mappings.PREFIX);
            sb9.append("$AlgParamsCCM");
            configurableProvider.addAlgorithm("AlgorithmParameters.CCM", sb9.toString());
            final StringBuilder sb10 = new StringBuilder();
            sb10.append("Alg.Alias.AlgorithmParameters.");
            sb10.append(NISTObjectIdentifiers.id_aes128_CCM);
            configurableProvider.addAlgorithm(sb10.toString(), "CCM");
            final StringBuilder sb11 = new StringBuilder();
            sb11.append("Alg.Alias.AlgorithmParameters.");
            sb11.append(NISTObjectIdentifiers.id_aes192_CCM);
            configurableProvider.addAlgorithm(sb11.toString(), "CCM");
            final StringBuilder sb12 = new StringBuilder();
            sb12.append("Alg.Alias.AlgorithmParameters.");
            sb12.append(NISTObjectIdentifiers.id_aes256_CCM);
            configurableProvider.addAlgorithm(sb12.toString(), "CCM");
            final StringBuilder sb13 = new StringBuilder();
            sb13.append(Mappings.PREFIX);
            sb13.append("$AlgParamGen");
            configurableProvider.addAlgorithm("AlgorithmParameterGenerator.AES", sb13.toString());
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameterGenerator.2.16.840.1.101.3.4.2", "AES");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameterGenerator.2.16.840.1.101.3.4.22", "AES");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameterGenerator.2.16.840.1.101.3.4.42", "AES");
            final StringBuilder sb14 = new StringBuilder();
            sb14.append("Alg.Alias.AlgorithmParameterGenerator.");
            sb14.append(NISTObjectIdentifiers.id_aes128_CBC);
            configurableProvider.addAlgorithm(sb14.toString(), "AES");
            final StringBuilder sb15 = new StringBuilder();
            sb15.append("Alg.Alias.AlgorithmParameterGenerator.");
            sb15.append(NISTObjectIdentifiers.id_aes192_CBC);
            configurableProvider.addAlgorithm(sb15.toString(), "AES");
            final StringBuilder sb16 = new StringBuilder();
            sb16.append("Alg.Alias.AlgorithmParameterGenerator.");
            sb16.append(NISTObjectIdentifiers.id_aes256_CBC);
            configurableProvider.addAlgorithm(sb16.toString(), "AES");
            configurableProvider.addAttributes("Cipher.AES", AES.generalAesAttributes);
            final StringBuilder sb17 = new StringBuilder();
            sb17.append(Mappings.PREFIX);
            sb17.append("$ECB");
            configurableProvider.addAlgorithm("Cipher.AES", sb17.toString());
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.2.16.840.1.101.3.4.2", "AES");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.2.16.840.1.101.3.4.22", "AES");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.2.16.840.1.101.3.4.42", "AES");
            final ASN1ObjectIdentifier id_aes128_ECB = NISTObjectIdentifiers.id_aes128_ECB;
            final StringBuilder sb18 = new StringBuilder();
            sb18.append(Mappings.PREFIX);
            sb18.append("$ECB");
            configurableProvider.addAlgorithm("Cipher", id_aes128_ECB, sb18.toString());
            final ASN1ObjectIdentifier id_aes192_ECB = NISTObjectIdentifiers.id_aes192_ECB;
            final StringBuilder sb19 = new StringBuilder();
            sb19.append(Mappings.PREFIX);
            sb19.append("$ECB");
            configurableProvider.addAlgorithm("Cipher", id_aes192_ECB, sb19.toString());
            final ASN1ObjectIdentifier id_aes256_ECB = NISTObjectIdentifiers.id_aes256_ECB;
            final StringBuilder sb20 = new StringBuilder();
            sb20.append(Mappings.PREFIX);
            sb20.append("$ECB");
            configurableProvider.addAlgorithm("Cipher", id_aes256_ECB, sb20.toString());
            final ASN1ObjectIdentifier id_aes128_CBC = NISTObjectIdentifiers.id_aes128_CBC;
            final StringBuilder sb21 = new StringBuilder();
            sb21.append(Mappings.PREFIX);
            sb21.append("$CBC");
            configurableProvider.addAlgorithm("Cipher", id_aes128_CBC, sb21.toString());
            final ASN1ObjectIdentifier id_aes192_CBC = NISTObjectIdentifiers.id_aes192_CBC;
            final StringBuilder sb22 = new StringBuilder();
            sb22.append(Mappings.PREFIX);
            sb22.append("$CBC");
            configurableProvider.addAlgorithm("Cipher", id_aes192_CBC, sb22.toString());
            final ASN1ObjectIdentifier id_aes256_CBC = NISTObjectIdentifiers.id_aes256_CBC;
            final StringBuilder sb23 = new StringBuilder();
            sb23.append(Mappings.PREFIX);
            sb23.append("$CBC");
            configurableProvider.addAlgorithm("Cipher", id_aes256_CBC, sb23.toString());
            final ASN1ObjectIdentifier id_aes128_OFB = NISTObjectIdentifiers.id_aes128_OFB;
            final StringBuilder sb24 = new StringBuilder();
            sb24.append(Mappings.PREFIX);
            sb24.append("$OFB");
            configurableProvider.addAlgorithm("Cipher", id_aes128_OFB, sb24.toString());
            final ASN1ObjectIdentifier id_aes192_OFB = NISTObjectIdentifiers.id_aes192_OFB;
            final StringBuilder sb25 = new StringBuilder();
            sb25.append(Mappings.PREFIX);
            sb25.append("$OFB");
            configurableProvider.addAlgorithm("Cipher", id_aes192_OFB, sb25.toString());
            final ASN1ObjectIdentifier id_aes256_OFB = NISTObjectIdentifiers.id_aes256_OFB;
            final StringBuilder sb26 = new StringBuilder();
            sb26.append(Mappings.PREFIX);
            sb26.append("$OFB");
            configurableProvider.addAlgorithm("Cipher", id_aes256_OFB, sb26.toString());
            final ASN1ObjectIdentifier id_aes128_CFB = NISTObjectIdentifiers.id_aes128_CFB;
            final StringBuilder sb27 = new StringBuilder();
            sb27.append(Mappings.PREFIX);
            sb27.append("$CFB");
            configurableProvider.addAlgorithm("Cipher", id_aes128_CFB, sb27.toString());
            final ASN1ObjectIdentifier id_aes192_CFB = NISTObjectIdentifiers.id_aes192_CFB;
            final StringBuilder sb28 = new StringBuilder();
            sb28.append(Mappings.PREFIX);
            sb28.append("$CFB");
            configurableProvider.addAlgorithm("Cipher", id_aes192_CFB, sb28.toString());
            final ASN1ObjectIdentifier id_aes256_CFB = NISTObjectIdentifiers.id_aes256_CFB;
            final StringBuilder sb29 = new StringBuilder();
            sb29.append(Mappings.PREFIX);
            sb29.append("$CFB");
            configurableProvider.addAlgorithm("Cipher", id_aes256_CFB, sb29.toString());
            configurableProvider.addAttributes("Cipher.AESWRAP", AES.generalAesAttributes);
            final StringBuilder sb30 = new StringBuilder();
            sb30.append(Mappings.PREFIX);
            sb30.append("$Wrap");
            configurableProvider.addAlgorithm("Cipher.AESWRAP", sb30.toString());
            configurableProvider.addAlgorithm("Alg.Alias.Cipher", NISTObjectIdentifiers.id_aes128_wrap, "AESWRAP");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher", NISTObjectIdentifiers.id_aes192_wrap, "AESWRAP");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher", NISTObjectIdentifiers.id_aes256_wrap, "AESWRAP");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.AESKW", "AESWRAP");
            configurableProvider.addAttributes("Cipher.AESWRAPPAD", AES.generalAesAttributes);
            final StringBuilder sb31 = new StringBuilder();
            sb31.append(Mappings.PREFIX);
            sb31.append("$WrapPad");
            configurableProvider.addAlgorithm("Cipher.AESWRAPPAD", sb31.toString());
            configurableProvider.addAlgorithm("Alg.Alias.Cipher", NISTObjectIdentifiers.id_aes128_wrap_pad, "AESWRAPPAD");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher", NISTObjectIdentifiers.id_aes192_wrap_pad, "AESWRAPPAD");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher", NISTObjectIdentifiers.id_aes256_wrap_pad, "AESWRAPPAD");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.AESKWP", "AESWRAPPAD");
            final StringBuilder sb32 = new StringBuilder();
            sb32.append(Mappings.PREFIX);
            sb32.append("$RFC3211Wrap");
            configurableProvider.addAlgorithm("Cipher.AESRFC3211WRAP", sb32.toString());
            final StringBuilder sb33 = new StringBuilder();
            sb33.append(Mappings.PREFIX);
            sb33.append("$RFC5649Wrap");
            configurableProvider.addAlgorithm("Cipher.AESRFC5649WRAP", sb33.toString());
            final StringBuilder sb34 = new StringBuilder();
            sb34.append(Mappings.PREFIX);
            sb34.append("$AlgParamGenCCM");
            configurableProvider.addAlgorithm("AlgorithmParameterGenerator.CCM", sb34.toString());
            final StringBuilder sb35 = new StringBuilder();
            sb35.append("Alg.Alias.AlgorithmParameterGenerator.");
            sb35.append(NISTObjectIdentifiers.id_aes128_CCM);
            configurableProvider.addAlgorithm(sb35.toString(), "CCM");
            final StringBuilder sb36 = new StringBuilder();
            sb36.append("Alg.Alias.AlgorithmParameterGenerator.");
            sb36.append(NISTObjectIdentifiers.id_aes192_CCM);
            configurableProvider.addAlgorithm(sb36.toString(), "CCM");
            final StringBuilder sb37 = new StringBuilder();
            sb37.append("Alg.Alias.AlgorithmParameterGenerator.");
            sb37.append(NISTObjectIdentifiers.id_aes256_CCM);
            configurableProvider.addAlgorithm(sb37.toString(), "CCM");
            configurableProvider.addAttributes("Cipher.CCM", AES.generalAesAttributes);
            final StringBuilder sb38 = new StringBuilder();
            sb38.append(Mappings.PREFIX);
            sb38.append("$CCM");
            configurableProvider.addAlgorithm("Cipher.CCM", sb38.toString());
            configurableProvider.addAlgorithm("Alg.Alias.Cipher", NISTObjectIdentifiers.id_aes128_CCM, "CCM");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher", NISTObjectIdentifiers.id_aes192_CCM, "CCM");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher", NISTObjectIdentifiers.id_aes256_CCM, "CCM");
            final StringBuilder sb39 = new StringBuilder();
            sb39.append(Mappings.PREFIX);
            sb39.append("$AlgParamGenGCM");
            configurableProvider.addAlgorithm("AlgorithmParameterGenerator.GCM", sb39.toString());
            final StringBuilder sb40 = new StringBuilder();
            sb40.append("Alg.Alias.AlgorithmParameterGenerator.");
            sb40.append(NISTObjectIdentifiers.id_aes128_GCM);
            configurableProvider.addAlgorithm(sb40.toString(), "GCM");
            final StringBuilder sb41 = new StringBuilder();
            sb41.append("Alg.Alias.AlgorithmParameterGenerator.");
            sb41.append(NISTObjectIdentifiers.id_aes192_GCM);
            configurableProvider.addAlgorithm(sb41.toString(), "GCM");
            final StringBuilder sb42 = new StringBuilder();
            sb42.append("Alg.Alias.AlgorithmParameterGenerator.");
            sb42.append(NISTObjectIdentifiers.id_aes256_GCM);
            configurableProvider.addAlgorithm(sb42.toString(), "GCM");
            configurableProvider.addAttributes("Cipher.GCM", AES.generalAesAttributes);
            final StringBuilder sb43 = new StringBuilder();
            sb43.append(Mappings.PREFIX);
            sb43.append("$GCM");
            configurableProvider.addAlgorithm("Cipher.GCM", sb43.toString());
            configurableProvider.addAlgorithm("Alg.Alias.Cipher", NISTObjectIdentifiers.id_aes128_GCM, "GCM");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher", NISTObjectIdentifiers.id_aes192_GCM, "GCM");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher", NISTObjectIdentifiers.id_aes256_GCM, "GCM");
            final StringBuilder sb44 = new StringBuilder();
            sb44.append(Mappings.PREFIX);
            sb44.append("$KeyGen");
            configurableProvider.addAlgorithm("KeyGenerator.AES", sb44.toString());
            final StringBuilder sb45 = new StringBuilder();
            sb45.append(Mappings.PREFIX);
            sb45.append("$KeyGen128");
            configurableProvider.addAlgorithm("KeyGenerator.2.16.840.1.101.3.4.2", sb45.toString());
            final StringBuilder sb46 = new StringBuilder();
            sb46.append(Mappings.PREFIX);
            sb46.append("$KeyGen192");
            configurableProvider.addAlgorithm("KeyGenerator.2.16.840.1.101.3.4.22", sb46.toString());
            final StringBuilder sb47 = new StringBuilder();
            sb47.append(Mappings.PREFIX);
            sb47.append("$KeyGen256");
            configurableProvider.addAlgorithm("KeyGenerator.2.16.840.1.101.3.4.42", sb47.toString());
            final ASN1ObjectIdentifier id_aes128_ECB2 = NISTObjectIdentifiers.id_aes128_ECB;
            final StringBuilder sb48 = new StringBuilder();
            sb48.append(Mappings.PREFIX);
            sb48.append("$KeyGen128");
            configurableProvider.addAlgorithm("KeyGenerator", id_aes128_ECB2, sb48.toString());
            final ASN1ObjectIdentifier id_aes128_CBC2 = NISTObjectIdentifiers.id_aes128_CBC;
            final StringBuilder sb49 = new StringBuilder();
            sb49.append(Mappings.PREFIX);
            sb49.append("$KeyGen128");
            configurableProvider.addAlgorithm("KeyGenerator", id_aes128_CBC2, sb49.toString());
            final ASN1ObjectIdentifier id_aes128_OFB2 = NISTObjectIdentifiers.id_aes128_OFB;
            final StringBuilder sb50 = new StringBuilder();
            sb50.append(Mappings.PREFIX);
            sb50.append("$KeyGen128");
            configurableProvider.addAlgorithm("KeyGenerator", id_aes128_OFB2, sb50.toString());
            final ASN1ObjectIdentifier id_aes128_CFB2 = NISTObjectIdentifiers.id_aes128_CFB;
            final StringBuilder sb51 = new StringBuilder();
            sb51.append(Mappings.PREFIX);
            sb51.append("$KeyGen128");
            configurableProvider.addAlgorithm("KeyGenerator", id_aes128_CFB2, sb51.toString());
            final ASN1ObjectIdentifier id_aes192_ECB2 = NISTObjectIdentifiers.id_aes192_ECB;
            final StringBuilder sb52 = new StringBuilder();
            sb52.append(Mappings.PREFIX);
            sb52.append("$KeyGen192");
            configurableProvider.addAlgorithm("KeyGenerator", id_aes192_ECB2, sb52.toString());
            final ASN1ObjectIdentifier id_aes192_CBC2 = NISTObjectIdentifiers.id_aes192_CBC;
            final StringBuilder sb53 = new StringBuilder();
            sb53.append(Mappings.PREFIX);
            sb53.append("$KeyGen192");
            configurableProvider.addAlgorithm("KeyGenerator", id_aes192_CBC2, sb53.toString());
            final ASN1ObjectIdentifier id_aes192_OFB2 = NISTObjectIdentifiers.id_aes192_OFB;
            final StringBuilder sb54 = new StringBuilder();
            sb54.append(Mappings.PREFIX);
            sb54.append("$KeyGen192");
            configurableProvider.addAlgorithm("KeyGenerator", id_aes192_OFB2, sb54.toString());
            final ASN1ObjectIdentifier id_aes192_CFB2 = NISTObjectIdentifiers.id_aes192_CFB;
            final StringBuilder sb55 = new StringBuilder();
            sb55.append(Mappings.PREFIX);
            sb55.append("$KeyGen192");
            configurableProvider.addAlgorithm("KeyGenerator", id_aes192_CFB2, sb55.toString());
            final ASN1ObjectIdentifier id_aes256_ECB2 = NISTObjectIdentifiers.id_aes256_ECB;
            final StringBuilder sb56 = new StringBuilder();
            sb56.append(Mappings.PREFIX);
            sb56.append("$KeyGen256");
            configurableProvider.addAlgorithm("KeyGenerator", id_aes256_ECB2, sb56.toString());
            final ASN1ObjectIdentifier id_aes256_CBC2 = NISTObjectIdentifiers.id_aes256_CBC;
            final StringBuilder sb57 = new StringBuilder();
            sb57.append(Mappings.PREFIX);
            sb57.append("$KeyGen256");
            configurableProvider.addAlgorithm("KeyGenerator", id_aes256_CBC2, sb57.toString());
            final ASN1ObjectIdentifier id_aes256_OFB2 = NISTObjectIdentifiers.id_aes256_OFB;
            final StringBuilder sb58 = new StringBuilder();
            sb58.append(Mappings.PREFIX);
            sb58.append("$KeyGen256");
            configurableProvider.addAlgorithm("KeyGenerator", id_aes256_OFB2, sb58.toString());
            final ASN1ObjectIdentifier id_aes256_CFB2 = NISTObjectIdentifiers.id_aes256_CFB;
            final StringBuilder sb59 = new StringBuilder();
            sb59.append(Mappings.PREFIX);
            sb59.append("$KeyGen256");
            configurableProvider.addAlgorithm("KeyGenerator", id_aes256_CFB2, sb59.toString());
            final StringBuilder sb60 = new StringBuilder();
            sb60.append(Mappings.PREFIX);
            sb60.append("$KeyGen");
            configurableProvider.addAlgorithm("KeyGenerator.AESWRAP", sb60.toString());
            final ASN1ObjectIdentifier id_aes128_wrap = NISTObjectIdentifiers.id_aes128_wrap;
            final StringBuilder sb61 = new StringBuilder();
            sb61.append(Mappings.PREFIX);
            sb61.append("$KeyGen128");
            configurableProvider.addAlgorithm("KeyGenerator", id_aes128_wrap, sb61.toString());
            final ASN1ObjectIdentifier id_aes192_wrap = NISTObjectIdentifiers.id_aes192_wrap;
            final StringBuilder sb62 = new StringBuilder();
            sb62.append(Mappings.PREFIX);
            sb62.append("$KeyGen192");
            configurableProvider.addAlgorithm("KeyGenerator", id_aes192_wrap, sb62.toString());
            final ASN1ObjectIdentifier id_aes256_wrap = NISTObjectIdentifiers.id_aes256_wrap;
            final StringBuilder sb63 = new StringBuilder();
            sb63.append(Mappings.PREFIX);
            sb63.append("$KeyGen256");
            configurableProvider.addAlgorithm("KeyGenerator", id_aes256_wrap, sb63.toString());
            final ASN1ObjectIdentifier id_aes128_GCM = NISTObjectIdentifiers.id_aes128_GCM;
            final StringBuilder sb64 = new StringBuilder();
            sb64.append(Mappings.PREFIX);
            sb64.append("$KeyGen128");
            configurableProvider.addAlgorithm("KeyGenerator", id_aes128_GCM, sb64.toString());
            final ASN1ObjectIdentifier id_aes192_GCM = NISTObjectIdentifiers.id_aes192_GCM;
            final StringBuilder sb65 = new StringBuilder();
            sb65.append(Mappings.PREFIX);
            sb65.append("$KeyGen192");
            configurableProvider.addAlgorithm("KeyGenerator", id_aes192_GCM, sb65.toString());
            final ASN1ObjectIdentifier id_aes256_GCM = NISTObjectIdentifiers.id_aes256_GCM;
            final StringBuilder sb66 = new StringBuilder();
            sb66.append(Mappings.PREFIX);
            sb66.append("$KeyGen256");
            configurableProvider.addAlgorithm("KeyGenerator", id_aes256_GCM, sb66.toString());
            final ASN1ObjectIdentifier id_aes128_CCM = NISTObjectIdentifiers.id_aes128_CCM;
            final StringBuilder sb67 = new StringBuilder();
            sb67.append(Mappings.PREFIX);
            sb67.append("$KeyGen128");
            configurableProvider.addAlgorithm("KeyGenerator", id_aes128_CCM, sb67.toString());
            final ASN1ObjectIdentifier id_aes192_CCM = NISTObjectIdentifiers.id_aes192_CCM;
            final StringBuilder sb68 = new StringBuilder();
            sb68.append(Mappings.PREFIX);
            sb68.append("$KeyGen192");
            configurableProvider.addAlgorithm("KeyGenerator", id_aes192_CCM, sb68.toString());
            final ASN1ObjectIdentifier id_aes256_CCM = NISTObjectIdentifiers.id_aes256_CCM;
            final StringBuilder sb69 = new StringBuilder();
            sb69.append(Mappings.PREFIX);
            sb69.append("$KeyGen256");
            configurableProvider.addAlgorithm("KeyGenerator", id_aes256_CCM, sb69.toString());
            final StringBuilder sb70 = new StringBuilder();
            sb70.append(Mappings.PREFIX);
            sb70.append("$KeyGen");
            configurableProvider.addAlgorithm("KeyGenerator.AESWRAPPAD", sb70.toString());
            final ASN1ObjectIdentifier id_aes128_wrap_pad = NISTObjectIdentifiers.id_aes128_wrap_pad;
            final StringBuilder sb71 = new StringBuilder();
            sb71.append(Mappings.PREFIX);
            sb71.append("$KeyGen128");
            configurableProvider.addAlgorithm("KeyGenerator", id_aes128_wrap_pad, sb71.toString());
            final ASN1ObjectIdentifier id_aes192_wrap_pad = NISTObjectIdentifiers.id_aes192_wrap_pad;
            final StringBuilder sb72 = new StringBuilder();
            sb72.append(Mappings.PREFIX);
            sb72.append("$KeyGen192");
            configurableProvider.addAlgorithm("KeyGenerator", id_aes192_wrap_pad, sb72.toString());
            final ASN1ObjectIdentifier id_aes256_wrap_pad = NISTObjectIdentifiers.id_aes256_wrap_pad;
            final StringBuilder sb73 = new StringBuilder();
            sb73.append(Mappings.PREFIX);
            sb73.append("$KeyGen256");
            configurableProvider.addAlgorithm("KeyGenerator", id_aes256_wrap_pad, sb73.toString());
            final StringBuilder sb74 = new StringBuilder();
            sb74.append(Mappings.PREFIX);
            sb74.append("$AESCMAC");
            configurableProvider.addAlgorithm("Mac.AESCMAC", sb74.toString());
            final StringBuilder sb75 = new StringBuilder();
            sb75.append(Mappings.PREFIX);
            sb75.append("$AESCCMMAC");
            configurableProvider.addAlgorithm("Mac.AESCCMMAC", sb75.toString());
            final StringBuilder sb76 = new StringBuilder();
            sb76.append("Alg.Alias.Mac.");
            sb76.append(NISTObjectIdentifiers.id_aes128_CCM.getId());
            configurableProvider.addAlgorithm(sb76.toString(), "AESCCMMAC");
            final StringBuilder sb77 = new StringBuilder();
            sb77.append("Alg.Alias.Mac.");
            sb77.append(NISTObjectIdentifiers.id_aes192_CCM.getId());
            configurableProvider.addAlgorithm(sb77.toString(), "AESCCMMAC");
            final StringBuilder sb78 = new StringBuilder();
            sb78.append("Alg.Alias.Mac.");
            sb78.append(NISTObjectIdentifiers.id_aes256_CCM.getId());
            configurableProvider.addAlgorithm(sb78.toString(), "AESCCMMAC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher", BCObjectIdentifiers.bc_pbe_sha1_pkcs12_aes128_cbc, "PBEWITHSHAAND128BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher", BCObjectIdentifiers.bc_pbe_sha1_pkcs12_aes192_cbc, "PBEWITHSHAAND192BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher", BCObjectIdentifiers.bc_pbe_sha1_pkcs12_aes256_cbc, "PBEWITHSHAAND256BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher", BCObjectIdentifiers.bc_pbe_sha256_pkcs12_aes128_cbc, "PBEWITHSHA256AND128BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher", BCObjectIdentifiers.bc_pbe_sha256_pkcs12_aes192_cbc, "PBEWITHSHA256AND192BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher", BCObjectIdentifiers.bc_pbe_sha256_pkcs12_aes256_cbc, "PBEWITHSHA256AND256BITAES-CBC-BC");
            final StringBuilder sb79 = new StringBuilder();
            sb79.append(Mappings.PREFIX);
            sb79.append("$PBEWithSHA1AESCBC128");
            configurableProvider.addAlgorithm("Cipher.PBEWITHSHAAND128BITAES-CBC-BC", sb79.toString());
            final StringBuilder sb80 = new StringBuilder();
            sb80.append(Mappings.PREFIX);
            sb80.append("$PBEWithSHA1AESCBC192");
            configurableProvider.addAlgorithm("Cipher.PBEWITHSHAAND192BITAES-CBC-BC", sb80.toString());
            final StringBuilder sb81 = new StringBuilder();
            sb81.append(Mappings.PREFIX);
            sb81.append("$PBEWithSHA1AESCBC256");
            configurableProvider.addAlgorithm("Cipher.PBEWITHSHAAND256BITAES-CBC-BC", sb81.toString());
            final StringBuilder sb82 = new StringBuilder();
            sb82.append(Mappings.PREFIX);
            sb82.append("$PBEWithSHA256AESCBC128");
            configurableProvider.addAlgorithm("Cipher.PBEWITHSHA256AND128BITAES-CBC-BC", sb82.toString());
            final StringBuilder sb83 = new StringBuilder();
            sb83.append(Mappings.PREFIX);
            sb83.append("$PBEWithSHA256AESCBC192");
            configurableProvider.addAlgorithm("Cipher.PBEWITHSHA256AND192BITAES-CBC-BC", sb83.toString());
            final StringBuilder sb84 = new StringBuilder();
            sb84.append(Mappings.PREFIX);
            sb84.append("$PBEWithSHA256AESCBC256");
            configurableProvider.addAlgorithm("Cipher.PBEWITHSHA256AND256BITAES-CBC-BC", sb84.toString());
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHSHA1AND128BITAES-CBC-BC", "PBEWITHSHAAND128BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHSHA1AND192BITAES-CBC-BC", "PBEWITHSHAAND192BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHSHA1AND256BITAES-CBC-BC", "PBEWITHSHAAND256BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHSHA-1AND128BITAES-CBC-BC", "PBEWITHSHAAND128BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHSHA-1AND192BITAES-CBC-BC", "PBEWITHSHAAND192BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHSHA-1AND256BITAES-CBC-BC", "PBEWITHSHAAND256BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHSHAAND128BITAES-BC", "PBEWITHSHAAND128BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHSHAAND192BITAES-BC", "PBEWITHSHAAND192BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHSHAAND256BITAES-BC", "PBEWITHSHAAND256BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHSHA1AND128BITAES-BC", "PBEWITHSHAAND128BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHSHA1AND192BITAES-BC", "PBEWITHSHAAND192BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHSHA1AND256BITAES-BC", "PBEWITHSHAAND256BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHSHA-1AND128BITAES-BC", "PBEWITHSHAAND128BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHSHA-1AND192BITAES-BC", "PBEWITHSHAAND192BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHSHA-1AND256BITAES-BC", "PBEWITHSHAAND256BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHSHA-256AND128BITAES-CBC-BC", "PBEWITHSHA256AND128BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHSHA-256AND192BITAES-CBC-BC", "PBEWITHSHA256AND192BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHSHA-256AND256BITAES-CBC-BC", "PBEWITHSHA256AND256BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHSHA256AND128BITAES-BC", "PBEWITHSHA256AND128BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHSHA256AND192BITAES-BC", "PBEWITHSHA256AND192BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHSHA256AND256BITAES-BC", "PBEWITHSHA256AND256BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHSHA-256AND128BITAES-BC", "PBEWITHSHA256AND128BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHSHA-256AND192BITAES-BC", "PBEWITHSHA256AND192BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHSHA-256AND256BITAES-BC", "PBEWITHSHA256AND256BITAES-CBC-BC");
            final StringBuilder sb85 = new StringBuilder();
            sb85.append(Mappings.PREFIX);
            sb85.append("$PBEWithAESCBC");
            configurableProvider.addAlgorithm("Cipher.PBEWITHMD5AND128BITAES-CBC-OPENSSL", sb85.toString());
            final StringBuilder sb86 = new StringBuilder();
            sb86.append(Mappings.PREFIX);
            sb86.append("$PBEWithAESCBC");
            configurableProvider.addAlgorithm("Cipher.PBEWITHMD5AND192BITAES-CBC-OPENSSL", sb86.toString());
            final StringBuilder sb87 = new StringBuilder();
            sb87.append(Mappings.PREFIX);
            sb87.append("$PBEWithAESCBC");
            configurableProvider.addAlgorithm("Cipher.PBEWITHMD5AND256BITAES-CBC-OPENSSL", sb87.toString());
            final StringBuilder sb88 = new StringBuilder();
            sb88.append(Mappings.PREFIX);
            sb88.append("$KeyFactory");
            configurableProvider.addAlgorithm("SecretKeyFactory.AES", sb88.toString());
            final ASN1ObjectIdentifier aes = NISTObjectIdentifiers.aes;
            final StringBuilder sb89 = new StringBuilder();
            sb89.append(Mappings.PREFIX);
            sb89.append("$KeyFactory");
            configurableProvider.addAlgorithm("SecretKeyFactory", aes, sb89.toString());
            final StringBuilder sb90 = new StringBuilder();
            sb90.append(Mappings.PREFIX);
            sb90.append("$PBEWithMD5And128BitAESCBCOpenSSL");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBEWITHMD5AND128BITAES-CBC-OPENSSL", sb90.toString());
            final StringBuilder sb91 = new StringBuilder();
            sb91.append(Mappings.PREFIX);
            sb91.append("$PBEWithMD5And192BitAESCBCOpenSSL");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBEWITHMD5AND192BITAES-CBC-OPENSSL", sb91.toString());
            final StringBuilder sb92 = new StringBuilder();
            sb92.append(Mappings.PREFIX);
            sb92.append("$PBEWithMD5And256BitAESCBCOpenSSL");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBEWITHMD5AND256BITAES-CBC-OPENSSL", sb92.toString());
            final StringBuilder sb93 = new StringBuilder();
            sb93.append(Mappings.PREFIX);
            sb93.append("$PBEWithSHAAnd128BitAESBC");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBEWITHSHAAND128BITAES-CBC-BC", sb93.toString());
            final StringBuilder sb94 = new StringBuilder();
            sb94.append(Mappings.PREFIX);
            sb94.append("$PBEWithSHAAnd192BitAESBC");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBEWITHSHAAND192BITAES-CBC-BC", sb94.toString());
            final StringBuilder sb95 = new StringBuilder();
            sb95.append(Mappings.PREFIX);
            sb95.append("$PBEWithSHAAnd256BitAESBC");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBEWITHSHAAND256BITAES-CBC-BC", sb95.toString());
            final StringBuilder sb96 = new StringBuilder();
            sb96.append(Mappings.PREFIX);
            sb96.append("$PBEWithSHA256And128BitAESBC");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBEWITHSHA256AND128BITAES-CBC-BC", sb96.toString());
            final StringBuilder sb97 = new StringBuilder();
            sb97.append(Mappings.PREFIX);
            sb97.append("$PBEWithSHA256And192BitAESBC");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBEWITHSHA256AND192BITAES-CBC-BC", sb97.toString());
            final StringBuilder sb98 = new StringBuilder();
            sb98.append(Mappings.PREFIX);
            sb98.append("$PBEWithSHA256And256BitAESBC");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBEWITHSHA256AND256BITAES-CBC-BC", sb98.toString());
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory.PBEWITHSHA1AND128BITAES-CBC-BC", "PBEWITHSHAAND128BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory.PBEWITHSHA1AND192BITAES-CBC-BC", "PBEWITHSHAAND192BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory.PBEWITHSHA1AND256BITAES-CBC-BC", "PBEWITHSHAAND256BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory.PBEWITHSHA-1AND128BITAES-CBC-BC", "PBEWITHSHAAND128BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory.PBEWITHSHA-1AND192BITAES-CBC-BC", "PBEWITHSHAAND192BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory.PBEWITHSHA-1AND256BITAES-CBC-BC", "PBEWITHSHAAND256BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory.PBEWITHSHA-256AND128BITAES-CBC-BC", "PBEWITHSHA256AND128BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory.PBEWITHSHA-256AND192BITAES-CBC-BC", "PBEWITHSHA256AND192BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory.PBEWITHSHA-256AND256BITAES-CBC-BC", "PBEWITHSHA256AND256BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory.PBEWITHSHA-256AND128BITAES-BC", "PBEWITHSHA256AND128BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory.PBEWITHSHA-256AND192BITAES-BC", "PBEWITHSHA256AND192BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory.PBEWITHSHA-256AND256BITAES-BC", "PBEWITHSHA256AND256BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory", BCObjectIdentifiers.bc_pbe_sha1_pkcs12_aes128_cbc, "PBEWITHSHAAND128BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory", BCObjectIdentifiers.bc_pbe_sha1_pkcs12_aes192_cbc, "PBEWITHSHAAND192BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory", BCObjectIdentifiers.bc_pbe_sha1_pkcs12_aes256_cbc, "PBEWITHSHAAND256BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory", BCObjectIdentifiers.bc_pbe_sha256_pkcs12_aes128_cbc, "PBEWITHSHA256AND128BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory", BCObjectIdentifiers.bc_pbe_sha256_pkcs12_aes192_cbc, "PBEWITHSHA256AND192BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory", BCObjectIdentifiers.bc_pbe_sha256_pkcs12_aes256_cbc, "PBEWITHSHA256AND256BITAES-CBC-BC");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHAAND128BITAES-CBC-BC", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHAAND192BITAES-CBC-BC", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHAAND256BITAES-CBC-BC", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHA256AND128BITAES-CBC-BC", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHA256AND192BITAES-CBC-BC", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHA256AND256BITAES-CBC-BC", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHA1AND128BITAES-CBC-BC", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHA1AND192BITAES-CBC-BC", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHA1AND256BITAES-CBC-BC", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHA-1AND128BITAES-CBC-BC", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHA-1AND192BITAES-CBC-BC", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHA-1AND256BITAES-CBC-BC", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHA-256AND128BITAES-CBC-BC", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHA-256AND192BITAES-CBC-BC", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHA-256AND256BITAES-CBC-BC", "PKCS12PBE");
            final StringBuilder sb99 = new StringBuilder();
            sb99.append("Alg.Alias.AlgorithmParameters.");
            sb99.append(BCObjectIdentifiers.bc_pbe_sha1_pkcs12_aes128_cbc.getId());
            configurableProvider.addAlgorithm(sb99.toString(), "PKCS12PBE");
            final StringBuilder sb100 = new StringBuilder();
            sb100.append("Alg.Alias.AlgorithmParameters.");
            sb100.append(BCObjectIdentifiers.bc_pbe_sha1_pkcs12_aes192_cbc.getId());
            configurableProvider.addAlgorithm(sb100.toString(), "PKCS12PBE");
            final StringBuilder sb101 = new StringBuilder();
            sb101.append("Alg.Alias.AlgorithmParameters.");
            sb101.append(BCObjectIdentifiers.bc_pbe_sha1_pkcs12_aes256_cbc.getId());
            configurableProvider.addAlgorithm(sb101.toString(), "PKCS12PBE");
            final StringBuilder sb102 = new StringBuilder();
            sb102.append("Alg.Alias.AlgorithmParameters.");
            sb102.append(BCObjectIdentifiers.bc_pbe_sha256_pkcs12_aes128_cbc.getId());
            configurableProvider.addAlgorithm(sb102.toString(), "PKCS12PBE");
            final StringBuilder sb103 = new StringBuilder();
            sb103.append("Alg.Alias.AlgorithmParameters.");
            sb103.append(BCObjectIdentifiers.bc_pbe_sha256_pkcs12_aes192_cbc.getId());
            configurableProvider.addAlgorithm(sb103.toString(), "PKCS12PBE");
            final StringBuilder sb104 = new StringBuilder();
            sb104.append("Alg.Alias.AlgorithmParameters.");
            sb104.append(BCObjectIdentifiers.bc_pbe_sha256_pkcs12_aes256_cbc.getId());
            configurableProvider.addAlgorithm(sb104.toString(), "PKCS12PBE");
            final StringBuilder sb105 = new StringBuilder();
            sb105.append(Mappings.PREFIX);
            sb105.append("$AESGMAC");
            final String string = sb105.toString();
            final StringBuilder sb106 = new StringBuilder();
            sb106.append(Mappings.PREFIX);
            sb106.append("$KeyGen128");
            this.addGMacAlgorithm(configurableProvider, "AES", string, sb106.toString());
            final StringBuilder sb107 = new StringBuilder();
            sb107.append(Mappings.PREFIX);
            sb107.append("$Poly1305");
            final String string2 = sb107.toString();
            final StringBuilder sb108 = new StringBuilder();
            sb108.append(Mappings.PREFIX);
            sb108.append("$Poly1305KeyGen");
            this.addPoly1305Algorithm(configurableProvider, "AES", string2, sb108.toString());
        }
    }
    
    public static class OFB extends BaseBlockCipher
    {
        public OFB() {
            super(new BufferedBlockCipher(new OFBBlockCipher(new AESEngine(), 128)), 128);
        }
    }
    
    public static class PBEWithAESCBC extends BaseBlockCipher
    {
        public PBEWithAESCBC() {
            super(new CBCBlockCipher(new AESEngine()));
        }
    }
    
    public static class PBEWithMD5And128BitAESCBCOpenSSL extends PBESecretKeyFactory
    {
        public PBEWithMD5And128BitAESCBCOpenSSL() {
            super("PBEWithMD5And128BitAES-CBC-OpenSSL", null, true, 3, 0, 128, 128);
        }
    }
    
    public static class PBEWithMD5And192BitAESCBCOpenSSL extends PBESecretKeyFactory
    {
        public PBEWithMD5And192BitAESCBCOpenSSL() {
            super("PBEWithMD5And192BitAES-CBC-OpenSSL", null, true, 3, 0, 192, 128);
        }
    }
    
    public static class PBEWithMD5And256BitAESCBCOpenSSL extends PBESecretKeyFactory
    {
        public PBEWithMD5And256BitAESCBCOpenSSL() {
            super("PBEWithMD5And256BitAES-CBC-OpenSSL", null, true, 3, 0, 256, 128);
        }
    }
    
    public static class PBEWithSHA1AESCBC128 extends BaseBlockCipher
    {
        public PBEWithSHA1AESCBC128() {
            super(new CBCBlockCipher(new AESEngine()), 2, 1, 128, 16);
        }
    }
    
    public static class PBEWithSHA1AESCBC192 extends BaseBlockCipher
    {
        public PBEWithSHA1AESCBC192() {
            super(new CBCBlockCipher(new AESEngine()), 2, 1, 192, 16);
        }
    }
    
    public static class PBEWithSHA1AESCBC256 extends BaseBlockCipher
    {
        public PBEWithSHA1AESCBC256() {
            super(new CBCBlockCipher(new AESEngine()), 2, 1, 256, 16);
        }
    }
    
    public static class PBEWithSHA256AESCBC128 extends BaseBlockCipher
    {
        public PBEWithSHA256AESCBC128() {
            super(new CBCBlockCipher(new AESEngine()), 2, 4, 128, 16);
        }
    }
    
    public static class PBEWithSHA256AESCBC192 extends BaseBlockCipher
    {
        public PBEWithSHA256AESCBC192() {
            super(new CBCBlockCipher(new AESEngine()), 2, 4, 192, 16);
        }
    }
    
    public static class PBEWithSHA256AESCBC256 extends BaseBlockCipher
    {
        public PBEWithSHA256AESCBC256() {
            super(new CBCBlockCipher(new AESEngine()), 2, 4, 256, 16);
        }
    }
    
    public static class PBEWithSHA256And128BitAESBC extends PBESecretKeyFactory
    {
        public PBEWithSHA256And128BitAESBC() {
            super("PBEWithSHA256And128BitAES-CBC-BC", null, true, 2, 4, 128, 128);
        }
    }
    
    public static class PBEWithSHA256And192BitAESBC extends PBESecretKeyFactory
    {
        public PBEWithSHA256And192BitAESBC() {
            super("PBEWithSHA256And192BitAES-CBC-BC", null, true, 2, 4, 192, 128);
        }
    }
    
    public static class PBEWithSHA256And256BitAESBC extends PBESecretKeyFactory
    {
        public PBEWithSHA256And256BitAESBC() {
            super("PBEWithSHA256And256BitAES-CBC-BC", null, true, 2, 4, 256, 128);
        }
    }
    
    public static class PBEWithSHAAnd128BitAESBC extends PBESecretKeyFactory
    {
        public PBEWithSHAAnd128BitAESBC() {
            super("PBEWithSHA1And128BitAES-CBC-BC", null, true, 2, 1, 128, 128);
        }
    }
    
    public static class PBEWithSHAAnd192BitAESBC extends PBESecretKeyFactory
    {
        public PBEWithSHAAnd192BitAESBC() {
            super("PBEWithSHA1And192BitAES-CBC-BC", null, true, 2, 1, 192, 128);
        }
    }
    
    public static class PBEWithSHAAnd256BitAESBC extends PBESecretKeyFactory
    {
        public PBEWithSHAAnd256BitAESBC() {
            super("PBEWithSHA1And256BitAES-CBC-BC", null, true, 2, 1, 256, 128);
        }
    }
    
    public static class Poly1305 extends BaseMac
    {
        public Poly1305() {
            super(new org.spongycastle.crypto.macs.Poly1305(new AESEngine()));
        }
    }
    
    public static class Poly1305KeyGen extends BaseKeyGenerator
    {
        public Poly1305KeyGen() {
            super("Poly1305-AES", 256, new Poly1305KeyGenerator());
        }
    }
    
    public static class RFC3211Wrap extends BaseWrapCipher
    {
        public RFC3211Wrap() {
            super(new RFC3211WrapEngine(new AESEngine()), 16);
        }
    }
    
    public static class RFC5649Wrap extends BaseWrapCipher
    {
        public RFC5649Wrap() {
            super(new RFC5649WrapEngine(new AESEngine()));
        }
    }
    
    public static class Wrap extends BaseWrapCipher
    {
        public Wrap() {
            super(new AESWrapEngine());
        }
    }
    
    public static class WrapPad extends BaseWrapCipher
    {
        public WrapPad() {
            super(new AESWrapPadEngine());
        }
    }
}
