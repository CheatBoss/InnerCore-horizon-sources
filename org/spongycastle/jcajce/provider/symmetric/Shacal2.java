package org.spongycastle.jcajce.provider.symmetric;

import javax.crypto.spec.*;
import java.security.spec.*;
import java.security.*;
import org.spongycastle.crypto.engines.*;
import org.spongycastle.crypto.modes.*;
import org.spongycastle.crypto.macs.*;
import org.spongycastle.jcajce.provider.symmetric.util.*;
import org.spongycastle.crypto.*;
import org.spongycastle.jcajce.provider.config.*;

public final class Shacal2
{
    private Shacal2() {
    }
    
    public static class AlgParamGen extends BaseAlgorithmParameterGenerator
    {
        @Override
        protected AlgorithmParameters engineGenerateParameters() {
            final byte[] array = new byte[32];
            if (this.random == null) {
                this.random = new SecureRandom();
            }
            this.random.nextBytes(array);
            try {
                final AlgorithmParameters parametersInstance = this.createParametersInstance("Shacal2");
                parametersInstance.init(new IvParameterSpec(array));
                return parametersInstance;
            }
            catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }
        
        @Override
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for Shacal2 parameter generation.");
        }
    }
    
    public static class AlgParams extends IvAlgorithmParameters
    {
        @Override
        protected String engineToString() {
            return "Shacal2 IV";
        }
    }
    
    public static class CBC extends BaseBlockCipher
    {
        public CBC() {
            super(new CBCBlockCipher(new Shacal2Engine()), 256);
        }
    }
    
    public static class CMAC extends BaseMac
    {
        public CMAC() {
            super(new CMac(new Shacal2Engine()));
        }
    }
    
    public static class ECB extends BaseBlockCipher
    {
        public ECB() {
            super(new BlockCipherProvider() {
                @Override
                public BlockCipher get() {
                    return new Shacal2Engine();
                }
            });
        }
    }
    
    public static class KeyGen extends BaseKeyGenerator
    {
        public KeyGen() {
            super("SHACAL-2", 128, new CipherKeyGenerator());
        }
    }
    
    public static class Mappings extends SymmetricAlgorithmProvider
    {
        private static final String PREFIX;
        
        static {
            PREFIX = Shacal2.class.getName();
        }
        
        @Override
        public void configure(final ConfigurableProvider configurableProvider) {
            final StringBuilder sb = new StringBuilder();
            sb.append(Mappings.PREFIX);
            sb.append("$CMAC");
            configurableProvider.addAlgorithm("Mac.Shacal-2CMAC", sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(Mappings.PREFIX);
            sb2.append("$ECB");
            configurableProvider.addAlgorithm("Cipher.Shacal2", sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(Mappings.PREFIX);
            sb3.append("$ECB");
            configurableProvider.addAlgorithm("Cipher.SHACAL-2", sb3.toString());
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(Mappings.PREFIX);
            sb4.append("$KeyGen");
            configurableProvider.addAlgorithm("KeyGenerator.Shacal2", sb4.toString());
            final StringBuilder sb5 = new StringBuilder();
            sb5.append(Mappings.PREFIX);
            sb5.append("$AlgParamGen");
            configurableProvider.addAlgorithm("AlgorithmParameterGenerator.Shacal2", sb5.toString());
            final StringBuilder sb6 = new StringBuilder();
            sb6.append(Mappings.PREFIX);
            sb6.append("$AlgParams");
            configurableProvider.addAlgorithm("AlgorithmParameters.Shacal2", sb6.toString());
            final StringBuilder sb7 = new StringBuilder();
            sb7.append(Mappings.PREFIX);
            sb7.append("$KeyGen");
            configurableProvider.addAlgorithm("KeyGenerator.SHACAL-2", sb7.toString());
            final StringBuilder sb8 = new StringBuilder();
            sb8.append(Mappings.PREFIX);
            sb8.append("$AlgParamGen");
            configurableProvider.addAlgorithm("AlgorithmParameterGenerator.SHACAL-2", sb8.toString());
            final StringBuilder sb9 = new StringBuilder();
            sb9.append(Mappings.PREFIX);
            sb9.append("$AlgParams");
            configurableProvider.addAlgorithm("AlgorithmParameters.SHACAL-2", sb9.toString());
        }
    }
}
