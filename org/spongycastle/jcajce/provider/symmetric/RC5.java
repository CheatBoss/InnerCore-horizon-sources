package org.spongycastle.jcajce.provider.symmetric;

import javax.crypto.spec.*;
import java.security.spec.*;
import java.security.*;
import org.spongycastle.crypto.modes.*;
import org.spongycastle.crypto.engines.*;
import org.spongycastle.jcajce.provider.symmetric.util.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.macs.*;
import org.spongycastle.jcajce.provider.util.*;
import org.spongycastle.jcajce.provider.config.*;

public final class RC5
{
    private RC5() {
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
                final AlgorithmParameters parametersInstance = this.createParametersInstance("RC5");
                parametersInstance.init(new IvParameterSpec(array));
                return parametersInstance;
            }
            catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }
        
        @Override
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for RC5 parameter generation.");
        }
    }
    
    public static class AlgParams extends IvAlgorithmParameters
    {
        @Override
        protected String engineToString() {
            return "RC5 IV";
        }
    }
    
    public static class CBC32 extends BaseBlockCipher
    {
        public CBC32() {
            super(new CBCBlockCipher(new RC532Engine()), 64);
        }
    }
    
    public static class CFB8Mac32 extends BaseMac
    {
        public CFB8Mac32() {
            super(new CFBBlockCipherMac(new RC532Engine()));
        }
    }
    
    public static class ECB32 extends BaseBlockCipher
    {
        public ECB32() {
            super(new RC532Engine());
        }
    }
    
    public static class ECB64 extends BaseBlockCipher
    {
        public ECB64() {
            super(new RC564Engine());
        }
    }
    
    public static class KeyGen32 extends BaseKeyGenerator
    {
        public KeyGen32() {
            super("RC5", 128, new CipherKeyGenerator());
        }
    }
    
    public static class KeyGen64 extends BaseKeyGenerator
    {
        public KeyGen64() {
            super("RC5-64", 256, new CipherKeyGenerator());
        }
    }
    
    public static class Mac32 extends BaseMac
    {
        public Mac32() {
            super(new CBCBlockCipherMac(new RC532Engine()));
        }
    }
    
    public static class Mappings extends AlgorithmProvider
    {
        private static final String PREFIX;
        
        static {
            PREFIX = RC5.class.getName();
        }
        
        @Override
        public void configure(final ConfigurableProvider configurableProvider) {
            final StringBuilder sb = new StringBuilder();
            sb.append(Mappings.PREFIX);
            sb.append("$ECB32");
            configurableProvider.addAlgorithm("Cipher.RC5", sb.toString());
            configurableProvider.addAlgorithm("Alg.Alias.Cipher.RC5-32", "RC5");
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(Mappings.PREFIX);
            sb2.append("$ECB64");
            configurableProvider.addAlgorithm("Cipher.RC5-64", sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(Mappings.PREFIX);
            sb3.append("$KeyGen32");
            configurableProvider.addAlgorithm("KeyGenerator.RC5", sb3.toString());
            configurableProvider.addAlgorithm("Alg.Alias.KeyGenerator.RC5-32", "RC5");
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(Mappings.PREFIX);
            sb4.append("$KeyGen64");
            configurableProvider.addAlgorithm("KeyGenerator.RC5-64", sb4.toString());
            final StringBuilder sb5 = new StringBuilder();
            sb5.append(Mappings.PREFIX);
            sb5.append("$AlgParams");
            configurableProvider.addAlgorithm("AlgorithmParameters.RC5", sb5.toString());
            final StringBuilder sb6 = new StringBuilder();
            sb6.append(Mappings.PREFIX);
            sb6.append("$AlgParams");
            configurableProvider.addAlgorithm("AlgorithmParameters.RC5-64", sb6.toString());
            final StringBuilder sb7 = new StringBuilder();
            sb7.append(Mappings.PREFIX);
            sb7.append("$Mac32");
            configurableProvider.addAlgorithm("Mac.RC5MAC", sb7.toString());
            configurableProvider.addAlgorithm("Alg.Alias.Mac.RC5", "RC5MAC");
            final StringBuilder sb8 = new StringBuilder();
            sb8.append(Mappings.PREFIX);
            sb8.append("$CFB8Mac32");
            configurableProvider.addAlgorithm("Mac.RC5MAC/CFB8", sb8.toString());
            configurableProvider.addAlgorithm("Alg.Alias.Mac.RC5/CFB8", "RC5MAC/CFB8");
        }
    }
}
