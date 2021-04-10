package org.spongycastle.jcajce.provider.symmetric;

import java.security.*;
import org.spongycastle.crypto.modes.*;
import org.spongycastle.crypto.paddings.*;
import org.spongycastle.crypto.macs.*;
import org.spongycastle.asn1.*;
import javax.crypto.*;
import java.security.spec.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.jcajce.*;
import javax.crypto.spec.*;
import org.spongycastle.crypto.generators.*;
import org.spongycastle.jcajce.provider.util.*;
import org.spongycastle.jcajce.provider.config.*;
import org.spongycastle.asn1.oiw.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.jcajce.provider.symmetric.util.*;
import org.spongycastle.crypto.engines.*;
import org.spongycastle.crypto.*;

public final class DES
{
    private DES() {
    }
    
    public static class AlgParamGen extends BaseAlgorithmParameterGenerator
    {
        @Override
        protected AlgorithmParameters engineGenerateParameters() {
            final byte[] array = new byte[8];
            if (this.random == null) {
                this.random = new SecureRandom();
            }
            this.random.nextBytes(array);
            try {
                final AlgorithmParameters parametersInstance = this.createParametersInstance("DES");
                parametersInstance.init(new IvParameterSpec(array));
                return parametersInstance;
            }
            catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }
        
        @Override
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for DES parameter generation.");
        }
    }
    
    public static class CBC extends BaseBlockCipher
    {
        public CBC() {
            super(new CBCBlockCipher(new DESEngine()), 64);
        }
    }
    
    public static class CBCMAC extends BaseMac
    {
        public CBCMAC() {
            super(new CBCBlockCipherMac(new DESEngine()));
        }
    }
    
    public static class CMAC extends BaseMac
    {
        public CMAC() {
            super(new CMac(new DESEngine()));
        }
    }
    
    public static class DES64 extends BaseMac
    {
        public DES64() {
            super(new CBCBlockCipherMac(new DESEngine(), 64));
        }
    }
    
    public static class DES64with7816d4 extends BaseMac
    {
        public DES64with7816d4() {
            super(new CBCBlockCipherMac(new DESEngine(), 64, new ISO7816d4Padding()));
        }
    }
    
    public static class DES9797Alg3 extends BaseMac
    {
        public DES9797Alg3() {
            super(new ISO9797Alg3Mac(new DESEngine()));
        }
    }
    
    public static class DES9797Alg3with7816d4 extends BaseMac
    {
        public DES9797Alg3with7816d4() {
            super(new ISO9797Alg3Mac(new DESEngine(), new ISO7816d4Padding()));
        }
    }
    
    public static class DESCFB8 extends BaseMac
    {
        public DESCFB8() {
            super(new CFBBlockCipherMac(new DESEngine()));
        }
    }
    
    public static class DESPBEKeyFactory extends BaseSecretKeyFactory
    {
        private int digest;
        private boolean forCipher;
        private int ivSize;
        private int keySize;
        private int scheme;
        
        public DESPBEKeyFactory(final String s, final ASN1ObjectIdentifier asn1ObjectIdentifier, final boolean forCipher, final int scheme, final int digest, final int keySize, final int ivSize) {
            super(s, asn1ObjectIdentifier);
            this.forCipher = forCipher;
            this.scheme = scheme;
            this.digest = digest;
            this.keySize = keySize;
            this.ivSize = ivSize;
        }
        
        @Override
        protected SecretKey engineGenerateSecret(final KeySpec keySpec) throws InvalidKeySpecException {
            if (!(keySpec instanceof PBEKeySpec)) {
                throw new InvalidKeySpecException("Invalid KeySpec");
            }
            final PBEKeySpec pbeKeySpec = (PBEKeySpec)keySpec;
            if (pbeKeySpec.getSalt() != null) {
                CipherParameters cipherParameters;
                if (this.forCipher) {
                    cipherParameters = PBE.Util.makePBEParameters(pbeKeySpec, this.scheme, this.digest, this.keySize, this.ivSize);
                }
                else {
                    cipherParameters = PBE.Util.makePBEMacParameters(pbeKeySpec, this.scheme, this.digest, this.keySize);
                }
                KeyParameter keyParameter;
                if (cipherParameters instanceof ParametersWithIV) {
                    keyParameter = (KeyParameter)((ParametersWithIV)cipherParameters).getParameters();
                }
                else {
                    keyParameter = (KeyParameter)cipherParameters;
                }
                DESParameters.setOddParity(keyParameter.getKey());
                return new BCPBEKey(this.algName, this.algOid, this.scheme, this.digest, this.keySize, this.ivSize, pbeKeySpec, cipherParameters);
            }
            final int scheme = this.scheme;
            if (scheme != 0 && scheme != 4) {
                return new BCPBEKey(this.algName, this.algOid, this.scheme, this.digest, this.keySize, this.ivSize, pbeKeySpec, null);
            }
            final char[] password = pbeKeySpec.getPassword();
            PasswordConverter passwordConverter;
            if (this.scheme == 0) {
                passwordConverter = PasswordConverter.ASCII;
            }
            else {
                passwordConverter = PasswordConverter.UTF8;
            }
            return new PBKDF1Key(password, passwordConverter);
        }
    }
    
    public static class ECB extends BaseBlockCipher
    {
        public ECB() {
            super(new DESEngine());
        }
    }
    
    public static class KeyFactory extends BaseSecretKeyFactory
    {
        public KeyFactory() {
            super("DES", null);
        }
        
        @Override
        protected SecretKey engineGenerateSecret(final KeySpec keySpec) throws InvalidKeySpecException {
            if (keySpec instanceof DESKeySpec) {
                return new SecretKeySpec(((DESKeySpec)keySpec).getKey(), "DES");
            }
            return super.engineGenerateSecret(keySpec);
        }
        
        @Override
        protected KeySpec engineGetKeySpec(final SecretKey secretKey, final Class clazz) throws InvalidKeySpecException {
            if (clazz == null) {
                throw new InvalidKeySpecException("keySpec parameter is null");
            }
            if (secretKey == null) {
                throw new InvalidKeySpecException("key parameter is null");
            }
            if (SecretKeySpec.class.isAssignableFrom(clazz)) {
                return new SecretKeySpec(secretKey.getEncoded(), this.algName);
            }
            if (DESKeySpec.class.isAssignableFrom(clazz)) {
                final byte[] encoded = secretKey.getEncoded();
                try {
                    return new DESKeySpec(encoded);
                }
                catch (Exception ex) {
                    throw new InvalidKeySpecException(ex.toString());
                }
            }
            throw new InvalidKeySpecException("Invalid KeySpec");
        }
    }
    
    public static class KeyGenerator extends BaseKeyGenerator
    {
        public KeyGenerator() {
            super("DES", 64, new DESKeyGenerator());
        }
        
        @Override
        protected SecretKey engineGenerateKey() {
            if (this.uninitialised) {
                this.engine.init(new KeyGenerationParameters(new SecureRandom(), this.defaultKeySize));
                this.uninitialised = false;
            }
            return new SecretKeySpec(this.engine.generateKey(), this.algName);
        }
        
        @Override
        protected void engineInit(final int n, final SecureRandom secureRandom) {
            super.engineInit(n, secureRandom);
        }
    }
    
    public static class Mappings extends AlgorithmProvider
    {
        private static final String PACKAGE = "org.spongycastle.jcajce.provider.symmetric";
        private static final String PREFIX;
        
        static {
            PREFIX = DES.class.getName();
        }
        
        private void addAlias(final ConfigurableProvider configurableProvider, final ASN1ObjectIdentifier asn1ObjectIdentifier, final String s) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Alg.Alias.KeyGenerator.");
            sb.append(asn1ObjectIdentifier.getId());
            configurableProvider.addAlgorithm(sb.toString(), s);
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Alg.Alias.KeyFactory.");
            sb2.append(asn1ObjectIdentifier.getId());
            configurableProvider.addAlgorithm(sb2.toString(), s);
        }
        
        @Override
        public void configure(final ConfigurableProvider configurableProvider) {
            final StringBuilder sb = new StringBuilder();
            sb.append(Mappings.PREFIX);
            sb.append("$ECB");
            configurableProvider.addAlgorithm("Cipher.DES", sb.toString());
            final ASN1ObjectIdentifier desCBC = OIWObjectIdentifiers.desCBC;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(Mappings.PREFIX);
            sb2.append("$CBC");
            configurableProvider.addAlgorithm("Cipher", desCBC, sb2.toString());
            this.addAlias(configurableProvider, OIWObjectIdentifiers.desCBC, "DES");
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(Mappings.PREFIX);
            sb3.append("$RFC3211");
            configurableProvider.addAlgorithm("Cipher.DESRFC3211WRAP", sb3.toString());
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(Mappings.PREFIX);
            sb4.append("$KeyGenerator");
            configurableProvider.addAlgorithm("KeyGenerator.DES", sb4.toString());
            final StringBuilder sb5 = new StringBuilder();
            sb5.append(Mappings.PREFIX);
            sb5.append("$KeyFactory");
            configurableProvider.addAlgorithm("SecretKeyFactory.DES", sb5.toString());
            final StringBuilder sb6 = new StringBuilder();
            sb6.append(Mappings.PREFIX);
            sb6.append("$CMAC");
            configurableProvider.addAlgorithm("Mac.DESCMAC", sb6.toString());
            final StringBuilder sb7 = new StringBuilder();
            sb7.append(Mappings.PREFIX);
            sb7.append("$CBCMAC");
            configurableProvider.addAlgorithm("Mac.DESMAC", sb7.toString());
            configurableProvider.addAlgorithm("Alg.Alias.Mac.DES", "DESMAC");
            final StringBuilder sb8 = new StringBuilder();
            sb8.append(Mappings.PREFIX);
            sb8.append("$DESCFB8");
            configurableProvider.addAlgorithm("Mac.DESMAC/CFB8", sb8.toString());
            configurableProvider.addAlgorithm("Alg.Alias.Mac.DES/CFB8", "DESMAC/CFB8");
            final StringBuilder sb9 = new StringBuilder();
            sb9.append(Mappings.PREFIX);
            sb9.append("$DES64");
            configurableProvider.addAlgorithm("Mac.DESMAC64", sb9.toString());
            configurableProvider.addAlgorithm("Alg.Alias.Mac.DES64", "DESMAC64");
            final StringBuilder sb10 = new StringBuilder();
            sb10.append(Mappings.PREFIX);
            sb10.append("$DES64with7816d4");
            configurableProvider.addAlgorithm("Mac.DESMAC64WITHISO7816-4PADDING", sb10.toString());
            configurableProvider.addAlgorithm("Alg.Alias.Mac.DES64WITHISO7816-4PADDING", "DESMAC64WITHISO7816-4PADDING");
            configurableProvider.addAlgorithm("Alg.Alias.Mac.DESISO9797ALG1MACWITHISO7816-4PADDING", "DESMAC64WITHISO7816-4PADDING");
            configurableProvider.addAlgorithm("Alg.Alias.Mac.DESISO9797ALG1WITHISO7816-4PADDING", "DESMAC64WITHISO7816-4PADDING");
            final StringBuilder sb11 = new StringBuilder();
            sb11.append(Mappings.PREFIX);
            sb11.append("$DES9797Alg3");
            configurableProvider.addAlgorithm("Mac.DESWITHISO9797", sb11.toString());
            configurableProvider.addAlgorithm("Alg.Alias.Mac.DESISO9797MAC", "DESWITHISO9797");
            final StringBuilder sb12 = new StringBuilder();
            sb12.append(Mappings.PREFIX);
            sb12.append("$DES9797Alg3");
            configurableProvider.addAlgorithm("Mac.ISO9797ALG3MAC", sb12.toString());
            configurableProvider.addAlgorithm("Alg.Alias.Mac.ISO9797ALG3", "ISO9797ALG3MAC");
            final StringBuilder sb13 = new StringBuilder();
            sb13.append(Mappings.PREFIX);
            sb13.append("$DES9797Alg3with7816d4");
            configurableProvider.addAlgorithm("Mac.ISO9797ALG3WITHISO7816-4PADDING", sb13.toString());
            configurableProvider.addAlgorithm("Alg.Alias.Mac.ISO9797ALG3MACWITHISO7816-4PADDING", "ISO9797ALG3WITHISO7816-4PADDING");
            configurableProvider.addAlgorithm("AlgorithmParameters.DES", "org.spongycastle.jcajce.provider.symmetric.util.IvAlgorithmParameters");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters", OIWObjectIdentifiers.desCBC, "DES");
            final StringBuilder sb14 = new StringBuilder();
            sb14.append(Mappings.PREFIX);
            sb14.append("$AlgParamGen");
            configurableProvider.addAlgorithm("AlgorithmParameterGenerator.DES", sb14.toString());
            final StringBuilder sb15 = new StringBuilder();
            sb15.append("Alg.Alias.AlgorithmParameterGenerator.");
            sb15.append(OIWObjectIdentifiers.desCBC);
            configurableProvider.addAlgorithm(sb15.toString(), "DES");
            final StringBuilder sb16 = new StringBuilder();
            sb16.append(Mappings.PREFIX);
            sb16.append("$PBEWithMD2");
            configurableProvider.addAlgorithm("Cipher.PBEWITHMD2ANDDES", sb16.toString());
            final StringBuilder sb17 = new StringBuilder();
            sb17.append(Mappings.PREFIX);
            sb17.append("$PBEWithMD5");
            configurableProvider.addAlgorithm("Cipher.PBEWITHMD5ANDDES", sb17.toString());
            final StringBuilder sb18 = new StringBuilder();
            sb18.append(Mappings.PREFIX);
            sb18.append("$PBEWithSHA1");
            configurableProvider.addAlgorithm("Cipher.PBEWITHSHA1ANDDES", sb18.toString());
            configurableProvider.addAlgorithm("Alg.Alias.Cipher", PKCSObjectIdentifiers.pbeWithMD2AndDES_CBC, "PBEWITHMD2ANDDES");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher", PKCSObjectIdentifiers.pbeWithMD5AndDES_CBC, "PBEWITHMD5ANDDES");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher", PKCSObjectIdentifiers.pbeWithSHA1AndDES_CBC, "PBEWITHSHA1ANDDES");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHMD2ANDDES-CBC", "PBEWITHMD2ANDDES");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHMD5ANDDES-CBC", "PBEWITHMD5ANDDES");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.PBEWITHSHA1ANDDES-CBC", "PBEWITHSHA1ANDDES");
            final StringBuilder sb19 = new StringBuilder();
            sb19.append(Mappings.PREFIX);
            sb19.append("$PBEWithMD2KeyFactory");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBEWITHMD2ANDDES", sb19.toString());
            final StringBuilder sb20 = new StringBuilder();
            sb20.append(Mappings.PREFIX);
            sb20.append("$PBEWithMD5KeyFactory");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBEWITHMD5ANDDES", sb20.toString());
            final StringBuilder sb21 = new StringBuilder();
            sb21.append(Mappings.PREFIX);
            sb21.append("$PBEWithSHA1KeyFactory");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBEWITHSHA1ANDDES", sb21.toString());
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory.PBEWITHMD2ANDDES-CBC", "PBEWITHMD2ANDDES");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory.PBEWITHMD5ANDDES-CBC", "PBEWITHMD5ANDDES");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory.PBEWITHSHA1ANDDES-CBC", "PBEWITHSHA1ANDDES");
            final StringBuilder sb22 = new StringBuilder();
            sb22.append("Alg.Alias.SecretKeyFactory.");
            sb22.append(PKCSObjectIdentifiers.pbeWithMD2AndDES_CBC);
            configurableProvider.addAlgorithm(sb22.toString(), "PBEWITHMD2ANDDES");
            final StringBuilder sb23 = new StringBuilder();
            sb23.append("Alg.Alias.SecretKeyFactory.");
            sb23.append(PKCSObjectIdentifiers.pbeWithMD5AndDES_CBC);
            configurableProvider.addAlgorithm(sb23.toString(), "PBEWITHMD5ANDDES");
            final StringBuilder sb24 = new StringBuilder();
            sb24.append("Alg.Alias.SecretKeyFactory.");
            sb24.append(PKCSObjectIdentifiers.pbeWithSHA1AndDES_CBC);
            configurableProvider.addAlgorithm(sb24.toString(), "PBEWITHSHA1ANDDES");
        }
    }
    
    public static class PBEWithMD2 extends BaseBlockCipher
    {
        public PBEWithMD2() {
            super(new CBCBlockCipher(new DESEngine()), 0, 5, 64, 8);
        }
    }
    
    public static class PBEWithMD2KeyFactory extends DESPBEKeyFactory
    {
        public PBEWithMD2KeyFactory() {
            super("PBEwithMD2andDES", PKCSObjectIdentifiers.pbeWithMD2AndDES_CBC, true, 0, 5, 64, 64);
        }
    }
    
    public static class PBEWithMD5 extends BaseBlockCipher
    {
        public PBEWithMD5() {
            super(new CBCBlockCipher(new DESEngine()), 0, 0, 64, 8);
        }
    }
    
    public static class PBEWithMD5KeyFactory extends DESPBEKeyFactory
    {
        public PBEWithMD5KeyFactory() {
            super("PBEwithMD5andDES", PKCSObjectIdentifiers.pbeWithMD5AndDES_CBC, true, 0, 0, 64, 64);
        }
    }
    
    public static class PBEWithSHA1 extends BaseBlockCipher
    {
        public PBEWithSHA1() {
            super(new CBCBlockCipher(new DESEngine()), 0, 1, 64, 8);
        }
    }
    
    public static class PBEWithSHA1KeyFactory extends DESPBEKeyFactory
    {
        public PBEWithSHA1KeyFactory() {
            super("PBEwithSHA1andDES", PKCSObjectIdentifiers.pbeWithSHA1AndDES_CBC, true, 0, 1, 64, 64);
        }
    }
    
    public static class RFC3211 extends BaseWrapCipher
    {
        public RFC3211() {
            super(new RFC3211WrapEngine(new DESEngine()), 8);
        }
    }
}
