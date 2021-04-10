package org.spongycastle.jcajce.provider.symmetric;

import java.util.*;
import org.spongycastle.jcajce.spec.*;
import java.security.*;
import javax.crypto.spec.*;
import java.security.spec.*;
import org.spongycastle.asn1.cryptopro.*;
import java.io.*;
import org.spongycastle.asn1.*;
import org.spongycastle.crypto.modes.*;
import org.spongycastle.crypto.engines.*;
import org.spongycastle.jcajce.provider.symmetric.util.*;
import org.spongycastle.crypto.macs.*;
import org.spongycastle.crypto.*;
import org.spongycastle.jcajce.provider.util.*;
import org.spongycastle.jcajce.provider.config.*;

public final class GOST28147
{
    private static Map<String, ASN1ObjectIdentifier> nameMappings;
    private static Map<ASN1ObjectIdentifier, String> oidMappings;
    
    static {
        GOST28147.oidMappings = new HashMap<ASN1ObjectIdentifier, String>();
        GOST28147.nameMappings = new HashMap<String, ASN1ObjectIdentifier>();
        GOST28147.oidMappings.put(CryptoProObjectIdentifiers.id_Gost28147_89_CryptoPro_TestParamSet, "E-TEST");
        GOST28147.oidMappings.put(CryptoProObjectIdentifiers.id_Gost28147_89_CryptoPro_A_ParamSet, "E-A");
        GOST28147.oidMappings.put(CryptoProObjectIdentifiers.id_Gost28147_89_CryptoPro_B_ParamSet, "E-B");
        GOST28147.oidMappings.put(CryptoProObjectIdentifiers.id_Gost28147_89_CryptoPro_C_ParamSet, "E-C");
        GOST28147.oidMappings.put(CryptoProObjectIdentifiers.id_Gost28147_89_CryptoPro_D_ParamSet, "E-D");
        GOST28147.nameMappings.put("E-A", CryptoProObjectIdentifiers.id_Gost28147_89_CryptoPro_A_ParamSet);
        GOST28147.nameMappings.put("E-B", CryptoProObjectIdentifiers.id_Gost28147_89_CryptoPro_B_ParamSet);
        GOST28147.nameMappings.put("E-C", CryptoProObjectIdentifiers.id_Gost28147_89_CryptoPro_C_ParamSet);
        GOST28147.nameMappings.put("E-D", CryptoProObjectIdentifiers.id_Gost28147_89_CryptoPro_D_ParamSet);
    }
    
    private GOST28147() {
    }
    
    public static class AlgParamGen extends BaseAlgorithmParameterGenerator
    {
        byte[] iv;
        byte[] sBox;
        
        public AlgParamGen() {
            this.iv = new byte[8];
            this.sBox = GOST28147Engine.getSBox("E-A");
        }
        
        @Override
        protected AlgorithmParameters engineGenerateParameters() {
            if (this.random == null) {
                this.random = new SecureRandom();
            }
            this.random.nextBytes(this.iv);
            try {
                final AlgorithmParameters parametersInstance = this.createParametersInstance("GOST28147");
                parametersInstance.init(new GOST28147ParameterSpec(this.sBox, this.iv));
                return parametersInstance;
            }
            catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }
        
        @Override
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            if (algorithmParameterSpec instanceof GOST28147ParameterSpec) {
                this.sBox = ((GOST28147ParameterSpec)algorithmParameterSpec).getSBox();
                return;
            }
            throw new InvalidAlgorithmParameterException("parameter spec not supported");
        }
    }
    
    public static class AlgParams extends BaseAlgParams
    {
        private byte[] iv;
        private ASN1ObjectIdentifier sBox;
        
        public AlgParams() {
            this.sBox = CryptoProObjectIdentifiers.id_Gost28147_89_CryptoPro_A_ParamSet;
        }
        
        @Override
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
            if (algorithmParameterSpec instanceof IvParameterSpec) {
                this.iv = ((IvParameterSpec)algorithmParameterSpec).getIV();
                return;
            }
            if (algorithmParameterSpec instanceof GOST28147ParameterSpec) {
                this.iv = ((GOST28147ParameterSpec)algorithmParameterSpec).getIV();
                try {
                    this.sBox = BaseAlgParams.getSBoxOID(((GOST28147ParameterSpec)algorithmParameterSpec).getSBox());
                    return;
                }
                catch (IllegalArgumentException ex) {
                    throw new InvalidParameterSpecException(ex.getMessage());
                }
            }
            throw new InvalidParameterSpecException("IvParameterSpec required to initialise a IV parameters algorithm parameters object");
        }
        
        @Override
        protected String engineToString() {
            return "GOST 28147 IV Parameters";
        }
        
        @Override
        protected AlgorithmParameterSpec localEngineGetParameterSpec(final Class clazz) throws InvalidParameterSpecException {
            if (clazz == IvParameterSpec.class) {
                return new IvParameterSpec(this.iv);
            }
            if (clazz != GOST28147ParameterSpec.class && clazz != AlgorithmParameterSpec.class) {
                final StringBuilder sb = new StringBuilder();
                sb.append("AlgorithmParameterSpec not recognized: ");
                sb.append(clazz.getName());
                throw new InvalidParameterSpecException(sb.toString());
            }
            return new GOST28147ParameterSpec(this.sBox, this.iv);
        }
        
        @Override
        protected byte[] localGetEncoded() throws IOException {
            return new GOST28147Parameters(this.iv, this.sBox).getEncoded();
        }
        
        protected void localInit(byte[] iv) throws IOException {
            final ASN1Primitive fromByteArray = ASN1Primitive.fromByteArray(iv);
            if (fromByteArray instanceof ASN1OctetString) {
                iv = ASN1OctetString.getInstance(fromByteArray).getOctets();
            }
            else {
                if (!(fromByteArray instanceof ASN1Sequence)) {
                    throw new IOException("Unable to recognize parameters");
                }
                final GOST28147Parameters instance = GOST28147Parameters.getInstance(fromByteArray);
                this.sBox = instance.getEncryptionParamSet();
                iv = instance.getIV();
            }
            this.iv = iv;
        }
    }
    
    public abstract static class BaseAlgParams extends BaseAlgorithmParameters
    {
        private byte[] iv;
        private ASN1ObjectIdentifier sBox;
        
        public BaseAlgParams() {
            this.sBox = CryptoProObjectIdentifiers.id_Gost28147_89_CryptoPro_A_ParamSet;
        }
        
        protected static ASN1ObjectIdentifier getSBoxOID(final String s) {
            final ASN1ObjectIdentifier asn1ObjectIdentifier = GOST28147.nameMappings.get(s);
            if (asn1ObjectIdentifier != null) {
                return asn1ObjectIdentifier;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Unknown SBOX name: ");
            sb.append(s);
            throw new IllegalArgumentException(sb.toString());
        }
        
        protected static ASN1ObjectIdentifier getSBoxOID(final byte[] array) {
            return getSBoxOID(GOST28147Engine.getSBoxName(array));
        }
        
        @Override
        protected final byte[] engineGetEncoded() throws IOException {
            return this.engineGetEncoded("ASN.1");
        }
        
        @Override
        protected final byte[] engineGetEncoded(final String s) throws IOException {
            if (this.isASN1FormatString(s)) {
                return this.localGetEncoded();
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Unknown parameter format: ");
            sb.append(s);
            throw new IOException(sb.toString());
        }
        
        @Override
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
            if (algorithmParameterSpec instanceof IvParameterSpec) {
                this.iv = ((IvParameterSpec)algorithmParameterSpec).getIV();
                return;
            }
            if (algorithmParameterSpec instanceof GOST28147ParameterSpec) {
                this.iv = ((GOST28147ParameterSpec)algorithmParameterSpec).getIV();
                try {
                    this.sBox = getSBoxOID(((GOST28147ParameterSpec)algorithmParameterSpec).getSBox());
                    return;
                }
                catch (IllegalArgumentException ex) {
                    throw new InvalidParameterSpecException(ex.getMessage());
                }
            }
            throw new InvalidParameterSpecException("IvParameterSpec required to initialise a IV parameters algorithm parameters object");
        }
        
        @Override
        protected final void engineInit(final byte[] array) throws IOException {
            this.engineInit(array, "ASN.1");
        }
        
        @Override
        protected final void engineInit(final byte[] array, final String s) throws IOException {
            if (array != null) {
                if (this.isASN1FormatString(s)) {
                    try {
                        this.localInit(array);
                        return;
                    }
                    catch (Exception ex) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Parameter parsing failed: ");
                        sb.append(ex.getMessage());
                        throw new IOException(sb.toString());
                    }
                    catch (IOException ex2) {
                        throw ex2;
                    }
                }
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Unknown parameter format: ");
                sb2.append(s);
                throw new IOException(sb2.toString());
            }
            throw new NullPointerException("Encoded parameters cannot be null");
        }
        
        @Override
        protected AlgorithmParameterSpec localEngineGetParameterSpec(final Class clazz) throws InvalidParameterSpecException {
            if (clazz == IvParameterSpec.class) {
                return new IvParameterSpec(this.iv);
            }
            if (clazz != GOST28147ParameterSpec.class && clazz != AlgorithmParameterSpec.class) {
                final StringBuilder sb = new StringBuilder();
                sb.append("AlgorithmParameterSpec not recognized: ");
                sb.append(clazz.getName());
                throw new InvalidParameterSpecException(sb.toString());
            }
            return new GOST28147ParameterSpec(this.sBox, this.iv);
        }
        
        protected byte[] localGetEncoded() throws IOException {
            return new GOST28147Parameters(this.iv, this.sBox).getEncoded();
        }
        
        abstract void localInit(final byte[] p0) throws IOException;
    }
    
    public static class CBC extends BaseBlockCipher
    {
        public CBC() {
            super(new CBCBlockCipher(new GOST28147Engine()), 64);
        }
    }
    
    public static class CryptoProWrap extends BaseWrapCipher
    {
        public CryptoProWrap() {
            super(new CryptoProWrapEngine());
        }
    }
    
    public static class ECB extends BaseBlockCipher
    {
        public ECB() {
            super(new GOST28147Engine());
        }
    }
    
    public static class GCFB extends BaseBlockCipher
    {
        public GCFB() {
            super(new BufferedBlockCipher(new GCFBBlockCipher(new GOST28147Engine())), 64);
        }
    }
    
    public static class GostWrap extends BaseWrapCipher
    {
        public GostWrap() {
            super(new GOST28147WrapEngine());
        }
    }
    
    public static class KeyGen extends BaseKeyGenerator
    {
        public KeyGen() {
            this(256);
        }
        
        public KeyGen(final int n) {
            super("GOST28147", n, new CipherKeyGenerator());
        }
    }
    
    public static class Mac extends BaseMac
    {
        public Mac() {
            super(new GOST28147Mac());
        }
    }
    
    public static class Mappings extends AlgorithmProvider
    {
        private static final String PREFIX;
        
        static {
            PREFIX = GOST28147.class.getName();
        }
        
        @Override
        public void configure(final ConfigurableProvider configurableProvider) {
            final StringBuilder sb = new StringBuilder();
            sb.append(Mappings.PREFIX);
            sb.append("$ECB");
            configurableProvider.addAlgorithm("Cipher.GOST28147", sb.toString());
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.GOST", "GOST28147");
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.GOST-28147", "GOST28147");
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Cipher.");
            sb2.append(CryptoProObjectIdentifiers.gostR28147_gcfb);
            final String string = sb2.toString();
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(Mappings.PREFIX);
            sb3.append("$GCFB");
            configurableProvider.addAlgorithm(string, sb3.toString());
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(Mappings.PREFIX);
            sb4.append("$KeyGen");
            configurableProvider.addAlgorithm("KeyGenerator.GOST28147", sb4.toString());
            configurableProvider.addAlgorithm("Alg.Alias.KeyGenerator.GOST", "GOST28147");
            configurableProvider.addAlgorithm("Alg.Alias.KeyGenerator.GOST-28147", "GOST28147");
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("Alg.Alias.KeyGenerator.");
            sb5.append(CryptoProObjectIdentifiers.gostR28147_gcfb);
            configurableProvider.addAlgorithm(sb5.toString(), "GOST28147");
            final StringBuilder sb6 = new StringBuilder();
            sb6.append(Mappings.PREFIX);
            sb6.append("$AlgParams");
            configurableProvider.addAlgorithm("AlgorithmParameters.GOST28147", sb6.toString());
            final StringBuilder sb7 = new StringBuilder();
            sb7.append(Mappings.PREFIX);
            sb7.append("$AlgParamGen");
            configurableProvider.addAlgorithm("AlgorithmParameterGenerator.GOST28147", sb7.toString());
            final StringBuilder sb8 = new StringBuilder();
            sb8.append("Alg.Alias.AlgorithmParameters.");
            sb8.append(CryptoProObjectIdentifiers.gostR28147_gcfb);
            configurableProvider.addAlgorithm(sb8.toString(), "GOST28147");
            final StringBuilder sb9 = new StringBuilder();
            sb9.append("Alg.Alias.AlgorithmParameterGenerator.");
            sb9.append(CryptoProObjectIdentifiers.gostR28147_gcfb);
            configurableProvider.addAlgorithm(sb9.toString(), "GOST28147");
            final StringBuilder sb10 = new StringBuilder();
            sb10.append("Cipher.");
            sb10.append(CryptoProObjectIdentifiers.id_Gost28147_89_CryptoPro_KeyWrap);
            final String string2 = sb10.toString();
            final StringBuilder sb11 = new StringBuilder();
            sb11.append(Mappings.PREFIX);
            sb11.append("$CryptoProWrap");
            configurableProvider.addAlgorithm(string2, sb11.toString());
            final StringBuilder sb12 = new StringBuilder();
            sb12.append("Cipher.");
            sb12.append(CryptoProObjectIdentifiers.id_Gost28147_89_None_KeyWrap);
            final String string3 = sb12.toString();
            final StringBuilder sb13 = new StringBuilder();
            sb13.append(Mappings.PREFIX);
            sb13.append("$GostWrap");
            configurableProvider.addAlgorithm(string3, sb13.toString());
            final StringBuilder sb14 = new StringBuilder();
            sb14.append(Mappings.PREFIX);
            sb14.append("$Mac");
            configurableProvider.addAlgorithm("Mac.GOST28147MAC", sb14.toString());
            configurableProvider.addAlgorithm("Alg.Alias.Mac.GOST28147", "GOST28147MAC");
        }
    }
}
