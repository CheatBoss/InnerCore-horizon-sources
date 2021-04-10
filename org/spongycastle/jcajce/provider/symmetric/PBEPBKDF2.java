package org.spongycastle.jcajce.provider.symmetric;

import java.util.*;
import org.spongycastle.asn1.cryptopro.*;
import org.spongycastle.util.*;
import org.spongycastle.asn1.nist.*;
import org.spongycastle.asn1.pkcs.*;
import java.io.*;
import org.spongycastle.asn1.*;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import org.spongycastle.jcajce.*;
import org.spongycastle.crypto.*;
import org.spongycastle.jcajce.spec.*;
import org.spongycastle.jcajce.provider.symmetric.util.*;
import org.spongycastle.jcajce.provider.util.*;
import org.spongycastle.jcajce.provider.config.*;

public class PBEPBKDF2
{
    private static final Map prfCodes;
    
    static {
        (prfCodes = new HashMap()).put(CryptoProObjectIdentifiers.gostR3411Hmac, Integers.valueOf(6));
        PBEPBKDF2.prfCodes.put(PKCSObjectIdentifiers.id_hmacWithSHA1, Integers.valueOf(1));
        PBEPBKDF2.prfCodes.put(PKCSObjectIdentifiers.id_hmacWithSHA256, Integers.valueOf(4));
        PBEPBKDF2.prfCodes.put(PKCSObjectIdentifiers.id_hmacWithSHA224, Integers.valueOf(7));
        PBEPBKDF2.prfCodes.put(PKCSObjectIdentifiers.id_hmacWithSHA384, Integers.valueOf(8));
        PBEPBKDF2.prfCodes.put(PKCSObjectIdentifiers.id_hmacWithSHA512, Integers.valueOf(9));
        PBEPBKDF2.prfCodes.put(NISTObjectIdentifiers.id_hmacWithSHA3_256, Integers.valueOf(11));
        PBEPBKDF2.prfCodes.put(NISTObjectIdentifiers.id_hmacWithSHA3_224, Integers.valueOf(10));
        PBEPBKDF2.prfCodes.put(NISTObjectIdentifiers.id_hmacWithSHA3_384, Integers.valueOf(12));
        PBEPBKDF2.prfCodes.put(NISTObjectIdentifiers.id_hmacWithSHA3_512, Integers.valueOf(13));
    }
    
    private PBEPBKDF2() {
    }
    
    public static class AlgParams extends BaseAlgorithmParameters
    {
        PBKDF2Params params;
        
        @Override
        protected byte[] engineGetEncoded() {
            try {
                return this.params.getEncoded("DER");
            }
            catch (IOException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Oooops! ");
                sb.append(ex.toString());
                throw new RuntimeException(sb.toString());
            }
        }
        
        @Override
        protected byte[] engineGetEncoded(final String s) {
            if (this.isASN1FormatString(s)) {
                return this.engineGetEncoded();
            }
            return null;
        }
        
        @Override
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
            if (algorithmParameterSpec instanceof PBEParameterSpec) {
                final PBEParameterSpec pbeParameterSpec = (PBEParameterSpec)algorithmParameterSpec;
                this.params = new PBKDF2Params(pbeParameterSpec.getSalt(), pbeParameterSpec.getIterationCount());
                return;
            }
            throw new InvalidParameterSpecException("PBEParameterSpec required to initialise a PBKDF2 PBE parameters algorithm parameters object");
        }
        
        @Override
        protected void engineInit(final byte[] array) throws IOException {
            this.params = PBKDF2Params.getInstance(ASN1Primitive.fromByteArray(array));
        }
        
        @Override
        protected void engineInit(final byte[] array, final String s) throws IOException {
            if (this.isASN1FormatString(s)) {
                this.engineInit(array);
                return;
            }
            throw new IOException("Unknown parameters format in PBKDF2 parameters object");
        }
        
        @Override
        protected String engineToString() {
            return "PBKDF2 Parameters";
        }
        
        @Override
        protected AlgorithmParameterSpec localEngineGetParameterSpec(final Class clazz) throws InvalidParameterSpecException {
            if (clazz == PBEParameterSpec.class) {
                return new PBEParameterSpec(this.params.getSalt(), this.params.getIterationCount().intValue());
            }
            throw new InvalidParameterSpecException("unknown parameter spec passed to PBKDF2 PBE parameters object.");
        }
    }
    
    public static class BasePBKDF2 extends BaseSecretKeyFactory
    {
        private int defaultDigest;
        private int scheme;
        
        public BasePBKDF2(final String s, final int n) {
            this(s, n, 1);
        }
        
        public BasePBKDF2(final String s, final int scheme, final int defaultDigest) {
            super(s, PKCSObjectIdentifiers.id_PBKDF2);
            this.scheme = scheme;
            this.defaultDigest = defaultDigest;
        }
        
        private int getDigestCode(final ASN1ObjectIdentifier asn1ObjectIdentifier) throws InvalidKeySpecException {
            final Integer n = PBEPBKDF2.prfCodes.get(asn1ObjectIdentifier);
            if (n != null) {
                return n;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid KeySpec: unknown PRF algorithm ");
            sb.append(asn1ObjectIdentifier);
            throw new InvalidKeySpecException(sb.toString());
        }
        
        @Override
        protected SecretKey engineGenerateSecret(final KeySpec keySpec) throws InvalidKeySpecException {
            if (!(keySpec instanceof PBEKeySpec)) {
                throw new InvalidKeySpecException("Invalid KeySpec");
            }
            final PBEKeySpec pbeKeySpec = (PBEKeySpec)keySpec;
            if (pbeKeySpec.getSalt() == null) {
                final char[] password = pbeKeySpec.getPassword();
                PasswordConverter passwordConverter;
                if (this.scheme == 1) {
                    passwordConverter = PasswordConverter.ASCII;
                }
                else {
                    passwordConverter = PasswordConverter.UTF8;
                }
                return new PBKDF2Key(password, passwordConverter);
            }
            if (pbeKeySpec.getIterationCount() <= 0) {
                final StringBuilder sb = new StringBuilder();
                sb.append("positive iteration count required: ");
                sb.append(pbeKeySpec.getIterationCount());
                throw new InvalidKeySpecException(sb.toString());
            }
            if (pbeKeySpec.getKeyLength() <= 0) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("positive key length required: ");
                sb2.append(pbeKeySpec.getKeyLength());
                throw new InvalidKeySpecException(sb2.toString());
            }
            if (pbeKeySpec.getPassword().length == 0) {
                throw new IllegalArgumentException("password empty");
            }
            if (pbeKeySpec instanceof PBKDF2KeySpec) {
                final int digestCode = this.getDigestCode(((PBKDF2KeySpec)pbeKeySpec).getPrf().getAlgorithm());
                final int keyLength = pbeKeySpec.getKeyLength();
                return new BCPBEKey(this.algName, this.algOid, this.scheme, digestCode, keyLength, -1, pbeKeySpec, PBE.Util.makePBEMacParameters(pbeKeySpec, this.scheme, digestCode, keyLength));
            }
            final int defaultDigest = this.defaultDigest;
            final int keyLength2 = pbeKeySpec.getKeyLength();
            return new BCPBEKey(this.algName, this.algOid, this.scheme, defaultDigest, keyLength2, -1, pbeKeySpec, PBE.Util.makePBEMacParameters(pbeKeySpec, this.scheme, defaultDigest, keyLength2));
        }
    }
    
    public static class Mappings extends AlgorithmProvider
    {
        private static final String PREFIX;
        
        static {
            PREFIX = PBEPBKDF2.class.getName();
        }
        
        @Override
        public void configure(final ConfigurableProvider configurableProvider) {
            final StringBuilder sb = new StringBuilder();
            sb.append(Mappings.PREFIX);
            sb.append("$AlgParams");
            configurableProvider.addAlgorithm("AlgorithmParameters.PBKDF2", sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Alg.Alias.AlgorithmParameters.");
            sb2.append(PKCSObjectIdentifiers.id_PBKDF2);
            configurableProvider.addAlgorithm(sb2.toString(), "PBKDF2");
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(Mappings.PREFIX);
            sb3.append("$PBKDF2withUTF8");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBKDF2", sb3.toString());
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory.PBKDF2WITHHMACSHA1", "PBKDF2");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory.PBKDF2WITHHMACSHA1ANDUTF8", "PBKDF2");
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Alg.Alias.SecretKeyFactory.");
            sb4.append(PKCSObjectIdentifiers.id_PBKDF2);
            configurableProvider.addAlgorithm(sb4.toString(), "PBKDF2");
            final StringBuilder sb5 = new StringBuilder();
            sb5.append(Mappings.PREFIX);
            sb5.append("$PBKDF2with8BIT");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBKDF2WITHASCII", sb5.toString());
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory.PBKDF2WITH8BIT", "PBKDF2WITHASCII");
            configurableProvider.addAlgorithm("Alg.Alias.SecretKeyFactory.PBKDF2WITHHMACSHA1AND8BIT", "PBKDF2WITHASCII");
            final StringBuilder sb6 = new StringBuilder();
            sb6.append(Mappings.PREFIX);
            sb6.append("$PBKDF2withSHA224");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBKDF2WITHHMACSHA224", sb6.toString());
            final StringBuilder sb7 = new StringBuilder();
            sb7.append(Mappings.PREFIX);
            sb7.append("$PBKDF2withSHA256");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBKDF2WITHHMACSHA256", sb7.toString());
            final StringBuilder sb8 = new StringBuilder();
            sb8.append(Mappings.PREFIX);
            sb8.append("$PBKDF2withSHA384");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBKDF2WITHHMACSHA384", sb8.toString());
            final StringBuilder sb9 = new StringBuilder();
            sb9.append(Mappings.PREFIX);
            sb9.append("$PBKDF2withSHA512");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBKDF2WITHHMACSHA512", sb9.toString());
            final StringBuilder sb10 = new StringBuilder();
            sb10.append(Mappings.PREFIX);
            sb10.append("$PBKDF2withSHA3_224");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBKDF2WITHHMACSHA3-224", sb10.toString());
            final StringBuilder sb11 = new StringBuilder();
            sb11.append(Mappings.PREFIX);
            sb11.append("$PBKDF2withSHA3_256");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBKDF2WITHHMACSHA3-256", sb11.toString());
            final StringBuilder sb12 = new StringBuilder();
            sb12.append(Mappings.PREFIX);
            sb12.append("$PBKDF2withSHA3_384");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBKDF2WITHHMACSHA3-384", sb12.toString());
            final StringBuilder sb13 = new StringBuilder();
            sb13.append(Mappings.PREFIX);
            sb13.append("$PBKDF2withSHA3_512");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBKDF2WITHHMACSHA3-512", sb13.toString());
            final StringBuilder sb14 = new StringBuilder();
            sb14.append(Mappings.PREFIX);
            sb14.append("$PBKDF2withGOST3411");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBKDF2WITHHMACGOST3411", sb14.toString());
        }
    }
    
    public static class PBKDF2with8BIT extends BasePBKDF2
    {
        public PBKDF2with8BIT() {
            super("PBKDF2", 1);
        }
    }
    
    public static class PBKDF2withGOST3411 extends BasePBKDF2
    {
        public PBKDF2withGOST3411() {
            super("PBKDF2", 5, 6);
        }
    }
    
    public static class PBKDF2withSHA224 extends BasePBKDF2
    {
        public PBKDF2withSHA224() {
            super("PBKDF2", 5, 7);
        }
    }
    
    public static class PBKDF2withSHA256 extends BasePBKDF2
    {
        public PBKDF2withSHA256() {
            super("PBKDF2", 5, 4);
        }
    }
    
    public static class PBKDF2withSHA384 extends BasePBKDF2
    {
        public PBKDF2withSHA384() {
            super("PBKDF2", 5, 8);
        }
    }
    
    public static class PBKDF2withSHA3_224 extends BasePBKDF2
    {
        public PBKDF2withSHA3_224() {
            super("PBKDF2", 5, 10);
        }
    }
    
    public static class PBKDF2withSHA3_256 extends BasePBKDF2
    {
        public PBKDF2withSHA3_256() {
            super("PBKDF2", 5, 11);
        }
    }
    
    public static class PBKDF2withSHA3_384 extends BasePBKDF2
    {
        public PBKDF2withSHA3_384() {
            super("PBKDF2", 5, 12);
        }
    }
    
    public static class PBKDF2withSHA3_512 extends BasePBKDF2
    {
        public PBKDF2withSHA3_512() {
            super("PBKDF2", 5, 13);
        }
    }
    
    public static class PBKDF2withSHA512 extends BasePBKDF2
    {
        public PBKDF2withSHA512() {
            super("PBKDF2", 5, 9);
        }
    }
    
    public static class PBKDF2withUTF8 extends BasePBKDF2
    {
        public PBKDF2withUTF8() {
            super("PBKDF2", 5);
        }
    }
}
