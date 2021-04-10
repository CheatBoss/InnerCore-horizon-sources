package org.spongycastle.jcajce.provider.symmetric;

import javax.crypto.spec.*;
import java.security.*;
import org.spongycastle.util.*;
import java.io.*;
import java.security.spec.*;
import org.spongycastle.crypto.modes.*;
import org.spongycastle.crypto.macs.*;
import org.spongycastle.jcajce.provider.util.*;
import org.spongycastle.jcajce.provider.config.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.asn1.*;
import org.spongycastle.jcajce.provider.symmetric.util.*;
import org.spongycastle.crypto.engines.*;
import org.spongycastle.crypto.*;

public final class RC2
{
    private RC2() {
    }
    
    public static class AlgParamGen extends BaseAlgorithmParameterGenerator
    {
        RC2ParameterSpec spec;
        
        public AlgParamGen() {
            this.spec = null;
        }
        
        @Override
        protected AlgorithmParameters engineGenerateParameters() {
            if (this.spec == null) {
                final byte[] array = new byte[8];
                if (this.random == null) {
                    this.random = new SecureRandom();
                }
                this.random.nextBytes(array);
                try {
                    final AlgorithmParameters parametersInstance = this.createParametersInstance("RC2");
                    parametersInstance.init(new IvParameterSpec(array));
                    return parametersInstance;
                }
                catch (Exception ex) {
                    throw new RuntimeException(ex.getMessage());
                }
            }
            try {
                final AlgorithmParameters parametersInstance2 = this.createParametersInstance("RC2");
                parametersInstance2.init(this.spec);
                return parametersInstance2;
            }
            catch (Exception ex2) {
                throw new RuntimeException(ex2.getMessage());
            }
        }
        
        @Override
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            if (algorithmParameterSpec instanceof RC2ParameterSpec) {
                this.spec = (RC2ParameterSpec)algorithmParameterSpec;
                return;
            }
            throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for RC2 parameter generation.");
        }
    }
    
    public static class AlgParams extends BaseAlgorithmParameters
    {
        private static final short[] ekb;
        private static final short[] table;
        private byte[] iv;
        private int parameterVersion;
        
        static {
            table = new short[] { 189, 86, 234, 242, 162, 241, 172, 42, 176, 147, 209, 156, 27, 51, 253, 208, 48, 4, 182, 220, 125, 223, 50, 75, 247, 203, 69, 155, 49, 187, 33, 90, 65, 159, 225, 217, 74, 77, 158, 218, 160, 104, 44, 195, 39, 95, 128, 54, 62, 238, 251, 149, 26, 254, 206, 168, 52, 169, 19, 240, 166, 63, 216, 12, 120, 36, 175, 35, 82, 193, 103, 23, 245, 102, 144, 231, 232, 7, 184, 96, 72, 230, 30, 83, 243, 146, 164, 114, 140, 8, 21, 110, 134, 0, 132, 250, 244, 127, 138, 66, 25, 246, 219, 205, 20, 141, 80, 18, 186, 60, 6, 78, 236, 179, 53, 17, 161, 136, 142, 43, 148, 153, 183, 113, 116, 211, 228, 191, 58, 222, 150, 14, 188, 10, 237, 119, 252, 55, 107, 3, 121, 137, 98, 198, 215, 192, 210, 124, 106, 139, 34, 163, 91, 5, 93, 2, 117, 213, 97, 227, 24, 143, 85, 81, 173, 31, 11, 94, 133, 229, 194, 87, 99, 202, 61, 108, 180, 197, 204, 112, 178, 145, 89, 13, 71, 32, 200, 79, 88, 224, 1, 226, 22, 56, 196, 111, 59, 15, 101, 70, 190, 126, 45, 123, 130, 249, 64, 181, 29, 115, 248, 235, 38, 199, 135, 151, 37, 84, 177, 40, 170, 152, 157, 165, 100, 109, 122, 212, 16, 129, 68, 239, 73, 214, 174, 46, 221, 118, 92, 47, 167, 28, 201, 9, 105, 154, 131, 207, 41, 57, 185, 233, 76, 255, 67, 171 };
            ekb = new short[] { 93, 190, 155, 139, 17, 153, 110, 77, 89, 243, 133, 166, 63, 183, 131, 197, 228, 115, 107, 58, 104, 90, 192, 71, 160, 100, 52, 12, 241, 208, 82, 165, 185, 30, 150, 67, 65, 216, 212, 44, 219, 248, 7, 119, 42, 202, 235, 239, 16, 28, 22, 13, 56, 114, 47, 137, 193, 249, 128, 196, 109, 174, 48, 61, 206, 32, 99, 254, 230, 26, 199, 184, 80, 232, 36, 23, 252, 37, 111, 187, 106, 163, 68, 83, 217, 162, 1, 171, 188, 182, 31, 152, 238, 154, 167, 45, 79, 158, 142, 172, 224, 198, 73, 70, 41, 244, 148, 138, 175, 225, 91, 195, 179, 123, 87, 209, 124, 156, 237, 135, 64, 140, 226, 203, 147, 20, 201, 97, 46, 229, 204, 246, 94, 168, 92, 214, 117, 141, 98, 149, 88, 105, 118, 161, 74, 181, 85, 9, 120, 51, 130, 215, 221, 121, 245, 27, 11, 222, 38, 33, 40, 116, 4, 151, 86, 223, 60, 240, 55, 57, 220, 255, 6, 164, 234, 66, 8, 218, 180, 113, 176, 207, 18, 122, 78, 250, 108, 29, 132, 0, 200, 127, 145, 69, 170, 43, 194, 177, 143, 213, 186, 242, 173, 25, 178, 103, 54, 247, 15, 10, 146, 125, 227, 157, 233, 144, 62, 35, 39, 102, 19, 236, 129, 21, 189, 34, 191, 159, 126, 169, 81, 75, 76, 251, 2, 211, 112, 134, 49, 231, 59, 5, 3, 84, 96, 72, 101, 24, 210, 205, 95, 50, 136, 14, 53, 253 };
        }
        
        public AlgParams() {
            this.parameterVersion = 58;
        }
        
        @Override
        protected byte[] engineGetEncoded() {
            return Arrays.clone(this.iv);
        }
        
        @Override
        protected byte[] engineGetEncoded(final String s) throws IOException {
            if (this.isASN1FormatString(s)) {
                RC2CBCParameter rc2CBCParameter;
                if (this.parameterVersion == -1) {
                    rc2CBCParameter = new RC2CBCParameter(this.engineGetEncoded());
                }
                else {
                    rc2CBCParameter = new RC2CBCParameter(this.parameterVersion, this.engineGetEncoded());
                }
                return rc2CBCParameter.getEncoded();
            }
            if (s.equals("RAW")) {
                return this.engineGetEncoded();
            }
            return null;
        }
        
        @Override
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
            byte[] iv;
            if (algorithmParameterSpec instanceof IvParameterSpec) {
                iv = ((IvParameterSpec)algorithmParameterSpec).getIV();
            }
            else {
                if (!(algorithmParameterSpec instanceof RC2ParameterSpec)) {
                    throw new InvalidParameterSpecException("IvParameterSpec or RC2ParameterSpec required to initialise a RC2 parameters algorithm parameters object");
                }
                final RC2ParameterSpec rc2ParameterSpec = (RC2ParameterSpec)algorithmParameterSpec;
                final int effectiveKeyBits = rc2ParameterSpec.getEffectiveKeyBits();
                if (effectiveKeyBits != -1) {
                    int parameterVersion;
                    if ((parameterVersion = effectiveKeyBits) < 256) {
                        parameterVersion = AlgParams.table[effectiveKeyBits];
                    }
                    this.parameterVersion = parameterVersion;
                }
                iv = rc2ParameterSpec.getIV();
            }
            this.iv = iv;
        }
        
        @Override
        protected void engineInit(final byte[] array) throws IOException {
            this.iv = Arrays.clone(array);
        }
        
        @Override
        protected void engineInit(final byte[] array, final String s) throws IOException {
            if (this.isASN1FormatString(s)) {
                final RC2CBCParameter instance = RC2CBCParameter.getInstance(ASN1Primitive.fromByteArray(array));
                if (instance.getRC2ParameterVersion() != null) {
                    this.parameterVersion = instance.getRC2ParameterVersion().intValue();
                }
                this.iv = instance.getIV();
                return;
            }
            if (s.equals("RAW")) {
                this.engineInit(array);
                return;
            }
            throw new IOException("Unknown parameters format in IV parameters object");
        }
        
        @Override
        protected String engineToString() {
            return "RC2 Parameters";
        }
        
        @Override
        protected AlgorithmParameterSpec localEngineGetParameterSpec(final Class clazz) throws InvalidParameterSpecException {
            if (clazz == RC2ParameterSpec.class || clazz == AlgorithmParameterSpec.class) {
                final int parameterVersion = this.parameterVersion;
                if (parameterVersion != -1) {
                    if (parameterVersion < 256) {
                        return new RC2ParameterSpec(AlgParams.ekb[this.parameterVersion], this.iv);
                    }
                    return new RC2ParameterSpec(this.parameterVersion, this.iv);
                }
            }
            if (clazz != IvParameterSpec.class && clazz != AlgorithmParameterSpec.class) {
                throw new InvalidParameterSpecException("unknown parameter spec passed to RC2 parameters object.");
            }
            return new IvParameterSpec(this.iv);
        }
    }
    
    public static class CBC extends BaseBlockCipher
    {
        public CBC() {
            super(new CBCBlockCipher(new RC2Engine()), 64);
        }
    }
    
    public static class CBCMAC extends BaseMac
    {
        public CBCMAC() {
            super(new CBCBlockCipherMac(new RC2Engine()));
        }
    }
    
    public static class CFB8MAC extends BaseMac
    {
        public CFB8MAC() {
            super(new CFBBlockCipherMac(new RC2Engine()));
        }
    }
    
    public static class ECB extends BaseBlockCipher
    {
        public ECB() {
            super(new RC2Engine());
        }
    }
    
    public static class KeyGenerator extends BaseKeyGenerator
    {
        public KeyGenerator() {
            super("RC2", 128, new CipherKeyGenerator());
        }
    }
    
    public static class Mappings extends AlgorithmProvider
    {
        private static final String PREFIX;
        
        static {
            PREFIX = RC2.class.getName();
        }
        
        @Override
        public void configure(final ConfigurableProvider configurableProvider) {
            final StringBuilder sb = new StringBuilder();
            sb.append(Mappings.PREFIX);
            sb.append("$AlgParamGen");
            configurableProvider.addAlgorithm("AlgorithmParameterGenerator.RC2", sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(Mappings.PREFIX);
            sb2.append("$AlgParamGen");
            configurableProvider.addAlgorithm("AlgorithmParameterGenerator.1.2.840.113549.3.2", sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(Mappings.PREFIX);
            sb3.append("$KeyGenerator");
            configurableProvider.addAlgorithm("KeyGenerator.RC2", sb3.toString());
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(Mappings.PREFIX);
            sb4.append("$KeyGenerator");
            configurableProvider.addAlgorithm("KeyGenerator.1.2.840.113549.3.2", sb4.toString());
            final StringBuilder sb5 = new StringBuilder();
            sb5.append(Mappings.PREFIX);
            sb5.append("$AlgParams");
            configurableProvider.addAlgorithm("AlgorithmParameters.RC2", sb5.toString());
            final StringBuilder sb6 = new StringBuilder();
            sb6.append(Mappings.PREFIX);
            sb6.append("$AlgParams");
            configurableProvider.addAlgorithm("AlgorithmParameters.1.2.840.113549.3.2", sb6.toString());
            final StringBuilder sb7 = new StringBuilder();
            sb7.append(Mappings.PREFIX);
            sb7.append("$ECB");
            configurableProvider.addAlgorithm("Cipher.RC2", sb7.toString());
            final StringBuilder sb8 = new StringBuilder();
            sb8.append(Mappings.PREFIX);
            sb8.append("$Wrap");
            configurableProvider.addAlgorithm("Cipher.RC2WRAP", sb8.toString());
            configurableProvider.addAlgorithm("Alg.Alias.Cipher", PKCSObjectIdentifiers.id_alg_CMSRC2wrap, "RC2WRAP");
            final ASN1ObjectIdentifier rc2_CBC = PKCSObjectIdentifiers.RC2_CBC;
            final StringBuilder sb9 = new StringBuilder();
            sb9.append(Mappings.PREFIX);
            sb9.append("$CBC");
            configurableProvider.addAlgorithm("Cipher", rc2_CBC, sb9.toString());
            final StringBuilder sb10 = new StringBuilder();
            sb10.append(Mappings.PREFIX);
            sb10.append("$CBCMAC");
            configurableProvider.addAlgorithm("Mac.RC2MAC", sb10.toString());
            configurableProvider.addAlgorithm("Alg.Alias.Mac.RC2", "RC2MAC");
            final StringBuilder sb11 = new StringBuilder();
            sb11.append(Mappings.PREFIX);
            sb11.append("$CFB8MAC");
            configurableProvider.addAlgorithm("Mac.RC2MAC/CFB8", sb11.toString());
            configurableProvider.addAlgorithm("Alg.Alias.Mac.RC2/CFB8", "RC2MAC/CFB8");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory.PBEWITHMD2ANDRC2-CBC", "PBEWITHMD2ANDRC2");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory.PBEWITHMD5ANDRC2-CBC", "PBEWITHMD5ANDRC2");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory.PBEWITHSHA1ANDRC2-CBC", "PBEWITHSHA1ANDRC2");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory", PKCSObjectIdentifiers.pbeWithMD2AndRC2_CBC, "PBEWITHMD2ANDRC2");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory", PKCSObjectIdentifiers.pbeWithMD5AndRC2_CBC, "PBEWITHMD5ANDRC2");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory", PKCSObjectIdentifiers.pbeWithSHA1AndRC2_CBC, "PBEWITHSHA1ANDRC2");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory.1.2.840.113549.1.12.1.5", "PBEWITHSHAAND128BITRC2-CBC");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory.1.2.840.113549.1.12.1.6", "PBEWITHSHAAND40BITRC2-CBC");
            final StringBuilder sb12 = new StringBuilder();
            sb12.append(Mappings.PREFIX);
            sb12.append("$PBEWithMD2KeyFactory");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBEWITHMD2ANDRC2", sb12.toString());
            final StringBuilder sb13 = new StringBuilder();
            sb13.append(Mappings.PREFIX);
            sb13.append("$PBEWithMD5KeyFactory");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBEWITHMD5ANDRC2", sb13.toString());
            final StringBuilder sb14 = new StringBuilder();
            sb14.append(Mappings.PREFIX);
            sb14.append("$PBEWithSHA1KeyFactory");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBEWITHSHA1ANDRC2", sb14.toString());
            final StringBuilder sb15 = new StringBuilder();
            sb15.append(Mappings.PREFIX);
            sb15.append("$PBEWithSHAAnd128BitKeyFactory");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBEWITHSHAAND128BITRC2-CBC", sb15.toString());
            final StringBuilder sb16 = new StringBuilder();
            sb16.append(Mappings.PREFIX);
            sb16.append("$PBEWithSHAAnd40BitKeyFactory");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBEWITHSHAAND40BITRC2-CBC", sb16.toString());
            configurableProvider.addAlgorithm("Alg.Alias.Cipher", PKCSObjectIdentifiers.pbeWithMD2AndRC2_CBC, "PBEWITHMD2ANDRC2");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher", PKCSObjectIdentifiers.pbeWithMD5AndRC2_CBC, "PBEWITHMD5ANDRC2");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher", PKCSObjectIdentifiers.pbeWithSHA1AndRC2_CBC, "PBEWITHSHA1ANDRC2");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.1.2.840.113549.1.12.1.5", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.1.2.840.113549.1.12.1.6", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWithSHAAnd3KeyTripleDES", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher", PKCSObjectIdentifiers.pbeWithSHAAnd128BitRC2_CBC, "PBEWITHSHAAND128BITRC2-CBC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher", PKCSObjectIdentifiers.pbeWithSHAAnd40BitRC2_CBC, "PBEWITHSHAAND40BITRC2-CBC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHSHA1AND128BITRC2-CBC", "PBEWITHSHAAND128BITRC2-CBC");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHSHA1AND40BITRC2-CBC", "PBEWITHSHAAND40BITRC2-CBC");
            final StringBuilder sb17 = new StringBuilder();
            sb17.append(Mappings.PREFIX);
            sb17.append("$PBEWithSHA1AndRC2");
            configurableProvider.addAlgorithm("Cipher.PBEWITHSHA1ANDRC2", sb17.toString());
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHSHAANDRC2-CBC", "PBEWITHSHA1ANDRC2");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHSHA1ANDRC2-CBC", "PBEWITHSHA1ANDRC2");
            final StringBuilder sb18 = new StringBuilder();
            sb18.append(Mappings.PREFIX);
            sb18.append("$PBEWithSHAAnd128BitRC2");
            configurableProvider.addAlgorithm("Cipher.PBEWITHSHAAND128BITRC2-CBC", sb18.toString());
            final StringBuilder sb19 = new StringBuilder();
            sb19.append(Mappings.PREFIX);
            sb19.append("$PBEWithSHAAnd40BitRC2");
            configurableProvider.addAlgorithm("Cipher.PBEWITHSHAAND40BITRC2-CBC", sb19.toString());
            final StringBuilder sb20 = new StringBuilder();
            sb20.append(Mappings.PREFIX);
            sb20.append("$PBEWithMD5AndRC2");
            configurableProvider.addAlgorithm("Cipher.PBEWITHMD5ANDRC2", sb20.toString());
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHMD5ANDRC2-CBC", "PBEWITHMD5ANDRC2");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHA1ANDRC2", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHAANDRC2", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHA1ANDRC2-CBC", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHAAND40BITRC2-CBC", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHAAND128BITRC2-CBC", "PKCS12PBE");
        }
    }
    
    public static class PBEWithMD2KeyFactory extends PBESecretKeyFactory
    {
        public PBEWithMD2KeyFactory() {
            super("PBEwithMD2andRC2", PKCSObjectIdentifiers.pbeWithMD2AndRC2_CBC, true, 0, 5, 64, 64);
        }
    }
    
    public static class PBEWithMD5AndRC2 extends BaseBlockCipher
    {
        public PBEWithMD5AndRC2() {
            super(new CBCBlockCipher(new RC2Engine()), 0, 0, 64, 8);
        }
    }
    
    public static class PBEWithMD5KeyFactory extends PBESecretKeyFactory
    {
        public PBEWithMD5KeyFactory() {
            super("PBEwithMD5andRC2", PKCSObjectIdentifiers.pbeWithMD5AndRC2_CBC, true, 0, 0, 64, 64);
        }
    }
    
    public static class PBEWithSHA1AndRC2 extends BaseBlockCipher
    {
        public PBEWithSHA1AndRC2() {
            super(new CBCBlockCipher(new RC2Engine()), 0, 1, 64, 8);
        }
    }
    
    public static class PBEWithSHA1KeyFactory extends PBESecretKeyFactory
    {
        public PBEWithSHA1KeyFactory() {
            super("PBEwithSHA1andRC2", PKCSObjectIdentifiers.pbeWithSHA1AndRC2_CBC, true, 0, 1, 64, 64);
        }
    }
    
    public static class PBEWithSHAAnd128BitKeyFactory extends PBESecretKeyFactory
    {
        public PBEWithSHAAnd128BitKeyFactory() {
            super("PBEwithSHAand128BitRC2-CBC", PKCSObjectIdentifiers.pbeWithSHAAnd128BitRC2_CBC, true, 2, 1, 128, 64);
        }
    }
    
    public static class PBEWithSHAAnd128BitRC2 extends BaseBlockCipher
    {
        public PBEWithSHAAnd128BitRC2() {
            super(new CBCBlockCipher(new RC2Engine()), 2, 1, 128, 8);
        }
    }
    
    public static class PBEWithSHAAnd40BitKeyFactory extends PBESecretKeyFactory
    {
        public PBEWithSHAAnd40BitKeyFactory() {
            super("PBEwithSHAand40BitRC2-CBC", PKCSObjectIdentifiers.pbeWithSHAAnd40BitRC2_CBC, true, 2, 1, 40, 64);
        }
    }
    
    public static class PBEWithSHAAnd40BitRC2 extends BaseBlockCipher
    {
        public PBEWithSHAAnd40BitRC2() {
            super(new CBCBlockCipher(new RC2Engine()), 2, 1, 40, 8);
        }
    }
    
    public static class Wrap extends BaseWrapCipher
    {
        public Wrap() {
            super(new RC2WrapEngine());
        }
    }
}
