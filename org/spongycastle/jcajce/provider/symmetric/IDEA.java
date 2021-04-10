package org.spongycastle.jcajce.provider.symmetric;

import javax.crypto.spec.*;
import java.security.*;
import java.io.*;
import java.security.spec.*;
import org.spongycastle.crypto.engines.*;
import org.spongycastle.crypto.modes.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.macs.*;
import org.spongycastle.jcajce.provider.util.*;
import org.spongycastle.jcajce.provider.config.*;
import org.spongycastle.asn1.misc.*;
import org.spongycastle.asn1.*;
import org.spongycastle.jcajce.provider.symmetric.util.*;

public final class IDEA
{
    private IDEA() {
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
                final AlgorithmParameters parametersInstance = this.createParametersInstance("IDEA");
                parametersInstance.init(new IvParameterSpec(array));
                return parametersInstance;
            }
            catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }
        
        @Override
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for IDEA parameter generation.");
        }
    }
    
    public static class AlgParams extends BaseAlgorithmParameters
    {
        private byte[] iv;
        
        @Override
        protected byte[] engineGetEncoded() throws IOException {
            return this.engineGetEncoded("ASN.1");
        }
        
        @Override
        protected byte[] engineGetEncoded(final String s) throws IOException {
            if (this.isASN1FormatString(s)) {
                return new IDEACBCPar(this.engineGetEncoded("RAW")).getEncoded();
            }
            if (s.equals("RAW")) {
                final byte[] iv = this.iv;
                final byte[] array = new byte[iv.length];
                System.arraycopy(iv, 0, array, 0, iv.length);
                return array;
            }
            return null;
        }
        
        @Override
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
            if (algorithmParameterSpec instanceof IvParameterSpec) {
                this.iv = ((IvParameterSpec)algorithmParameterSpec).getIV();
                return;
            }
            throw new InvalidParameterSpecException("IvParameterSpec required to initialise a IV parameters algorithm parameters object");
        }
        
        @Override
        protected void engineInit(final byte[] array) throws IOException {
            final byte[] iv = new byte[array.length];
            System.arraycopy(array, 0, this.iv = iv, 0, iv.length);
        }
        
        @Override
        protected void engineInit(final byte[] array, final String s) throws IOException {
            if (s.equals("RAW")) {
                this.engineInit(array);
                return;
            }
            if (s.equals("ASN.1")) {
                this.engineInit(new IDEACBCPar((ASN1Sequence)new ASN1InputStream(array).readObject()).getIV());
                return;
            }
            throw new IOException("Unknown parameters format in IV parameters object");
        }
        
        @Override
        protected String engineToString() {
            return "IDEA Parameters";
        }
        
        @Override
        protected AlgorithmParameterSpec localEngineGetParameterSpec(final Class clazz) throws InvalidParameterSpecException {
            if (clazz == IvParameterSpec.class) {
                return new IvParameterSpec(this.iv);
            }
            throw new InvalidParameterSpecException("unknown parameter spec passed to IV parameters object.");
        }
    }
    
    public static class CBC extends BaseBlockCipher
    {
        public CBC() {
            super(new CBCBlockCipher(new IDEAEngine()), 64);
        }
    }
    
    public static class CFB8Mac extends BaseMac
    {
        public CFB8Mac() {
            super(new CFBBlockCipherMac(new IDEAEngine()));
        }
    }
    
    public static class ECB extends BaseBlockCipher
    {
        public ECB() {
            super(new IDEAEngine());
        }
    }
    
    public static class KeyGen extends BaseKeyGenerator
    {
        public KeyGen() {
            super("IDEA", 128, new CipherKeyGenerator());
        }
    }
    
    public static class Mac extends BaseMac
    {
        public Mac() {
            super(new CBCBlockCipherMac(new IDEAEngine()));
        }
    }
    
    public static class Mappings extends AlgorithmProvider
    {
        private static final String PREFIX;
        
        static {
            PREFIX = IDEA.class.getName();
        }
        
        @Override
        public void configure(final ConfigurableProvider configurableProvider) {
            final StringBuilder sb = new StringBuilder();
            sb.append(Mappings.PREFIX);
            sb.append("$AlgParamGen");
            configurableProvider.addAlgorithm("AlgorithmParameterGenerator.IDEA", sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(Mappings.PREFIX);
            sb2.append("$AlgParamGen");
            configurableProvider.addAlgorithm("AlgorithmParameterGenerator.1.3.6.1.4.1.188.7.1.1.2", sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(Mappings.PREFIX);
            sb3.append("$AlgParams");
            configurableProvider.addAlgorithm("AlgorithmParameters.IDEA", sb3.toString());
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(Mappings.PREFIX);
            sb4.append("$AlgParams");
            configurableProvider.addAlgorithm("AlgorithmParameters.1.3.6.1.4.1.188.7.1.1.2", sb4.toString());
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHAANDIDEA", "PKCS12PBE");
            configurableProvider.addAlgorithm("Alg.Alias.AlgorithmParameters.PBEWITHSHAANDIDEA-CBC", "PKCS12PBE");
            final StringBuilder sb5 = new StringBuilder();
            sb5.append(Mappings.PREFIX);
            sb5.append("$ECB");
            configurableProvider.addAlgorithm("Cipher.IDEA", sb5.toString());
            final ASN1ObjectIdentifier as_sys_sec_alg_ideaCBC = MiscObjectIdentifiers.as_sys_sec_alg_ideaCBC;
            final StringBuilder sb6 = new StringBuilder();
            sb6.append(Mappings.PREFIX);
            sb6.append("$CBC");
            configurableProvider.addAlgorithm("Cipher", as_sys_sec_alg_ideaCBC, sb6.toString());
            final StringBuilder sb7 = new StringBuilder();
            sb7.append(Mappings.PREFIX);
            sb7.append("$PBEWithSHAAndIDEA");
            configurableProvider.addAlgorithm("Cipher.PBEWITHSHAANDIDEA-CBC", sb7.toString());
            final StringBuilder sb8 = new StringBuilder();
            sb8.append(Mappings.PREFIX);
            sb8.append("$KeyGen");
            configurableProvider.addAlgorithm("KeyGenerator.IDEA", sb8.toString());
            final ASN1ObjectIdentifier as_sys_sec_alg_ideaCBC2 = MiscObjectIdentifiers.as_sys_sec_alg_ideaCBC;
            final StringBuilder sb9 = new StringBuilder();
            sb9.append(Mappings.PREFIX);
            sb9.append("$KeyGen");
            configurableProvider.addAlgorithm("KeyGenerator", as_sys_sec_alg_ideaCBC2, sb9.toString());
            final StringBuilder sb10 = new StringBuilder();
            sb10.append(Mappings.PREFIX);
            sb10.append("$PBEWithSHAAndIDEAKeyGen");
            configurableProvider.addAlgorithm("SecretKeyFactory.PBEWITHSHAANDIDEA-CBC", sb10.toString());
            final StringBuilder sb11 = new StringBuilder();
            sb11.append(Mappings.PREFIX);
            sb11.append("$Mac");
            configurableProvider.addAlgorithm("Mac.IDEAMAC", sb11.toString());
            configurableProvider.addAlgorithm("Alg.Alias.Mac.IDEA", "IDEAMAC");
            final StringBuilder sb12 = new StringBuilder();
            sb12.append(Mappings.PREFIX);
            sb12.append("$CFB8Mac");
            configurableProvider.addAlgorithm("Mac.IDEAMAC/CFB8", sb12.toString());
            configurableProvider.addAlgorithm("Alg.Alias.Mac.IDEA/CFB8", "IDEAMAC/CFB8");
        }
    }
    
    public static class PBEWithSHAAndIDEA extends BaseBlockCipher
    {
        public PBEWithSHAAndIDEA() {
            super(new CBCBlockCipher(new IDEAEngine()));
        }
    }
    
    public static class PBEWithSHAAndIDEAKeyGen extends PBESecretKeyFactory
    {
        public PBEWithSHAAndIDEAKeyGen() {
            super("PBEwithSHAandIDEA-CBC", null, true, 2, 1, 128, 64);
        }
    }
}
