package org.spongycastle.jcajce.provider.symmetric;

import org.spongycastle.crypto.engines.*;
import org.spongycastle.crypto.macs.*;
import org.spongycastle.jcajce.provider.symmetric.util.*;
import org.spongycastle.crypto.*;
import org.spongycastle.jcajce.provider.util.*;
import org.spongycastle.jcajce.provider.config.*;

public final class Threefish
{
    private Threefish() {
    }
    
    public static class AlgParams_1024 extends IvAlgorithmParameters
    {
        @Override
        protected String engineToString() {
            return "Threefish-1024 IV";
        }
    }
    
    public static class AlgParams_256 extends IvAlgorithmParameters
    {
        @Override
        protected String engineToString() {
            return "Threefish-256 IV";
        }
    }
    
    public static class AlgParams_512 extends IvAlgorithmParameters
    {
        @Override
        protected String engineToString() {
            return "Threefish-512 IV";
        }
    }
    
    public static class CMAC_1024 extends BaseMac
    {
        public CMAC_1024() {
            super(new CMac(new ThreefishEngine(1024)));
        }
    }
    
    public static class CMAC_256 extends BaseMac
    {
        public CMAC_256() {
            super(new CMac(new ThreefishEngine(256)));
        }
    }
    
    public static class CMAC_512 extends BaseMac
    {
        public CMAC_512() {
            super(new CMac(new ThreefishEngine(512)));
        }
    }
    
    public static class ECB_1024 extends BaseBlockCipher
    {
        public ECB_1024() {
            super(new ThreefishEngine(1024));
        }
    }
    
    public static class ECB_256 extends BaseBlockCipher
    {
        public ECB_256() {
            super(new ThreefishEngine(256));
        }
    }
    
    public static class ECB_512 extends BaseBlockCipher
    {
        public ECB_512() {
            super(new ThreefishEngine(512));
        }
    }
    
    public static class KeyGen_1024 extends BaseKeyGenerator
    {
        public KeyGen_1024() {
            super("Threefish-1024", 1024, new CipherKeyGenerator());
        }
    }
    
    public static class KeyGen_256 extends BaseKeyGenerator
    {
        public KeyGen_256() {
            super("Threefish-256", 256, new CipherKeyGenerator());
        }
    }
    
    public static class KeyGen_512 extends BaseKeyGenerator
    {
        public KeyGen_512() {
            super("Threefish-512", 512, new CipherKeyGenerator());
        }
    }
    
    public static class Mappings extends AlgorithmProvider
    {
        private static final String PREFIX;
        
        static {
            PREFIX = Threefish.class.getName();
        }
        
        @Override
        public void configure(final ConfigurableProvider configurableProvider) {
            final StringBuilder sb = new StringBuilder();
            sb.append(Mappings.PREFIX);
            sb.append("$CMAC_256");
            configurableProvider.addAlgorithm("Mac.Threefish-256CMAC", sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(Mappings.PREFIX);
            sb2.append("$CMAC_512");
            configurableProvider.addAlgorithm("Mac.Threefish-512CMAC", sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(Mappings.PREFIX);
            sb3.append("$CMAC_1024");
            configurableProvider.addAlgorithm("Mac.Threefish-1024CMAC", sb3.toString());
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(Mappings.PREFIX);
            sb4.append("$ECB_256");
            configurableProvider.addAlgorithm("Cipher.Threefish-256", sb4.toString());
            final StringBuilder sb5 = new StringBuilder();
            sb5.append(Mappings.PREFIX);
            sb5.append("$ECB_512");
            configurableProvider.addAlgorithm("Cipher.Threefish-512", sb5.toString());
            final StringBuilder sb6 = new StringBuilder();
            sb6.append(Mappings.PREFIX);
            sb6.append("$ECB_1024");
            configurableProvider.addAlgorithm("Cipher.Threefish-1024", sb6.toString());
            final StringBuilder sb7 = new StringBuilder();
            sb7.append(Mappings.PREFIX);
            sb7.append("$KeyGen_256");
            configurableProvider.addAlgorithm("KeyGenerator.Threefish-256", sb7.toString());
            final StringBuilder sb8 = new StringBuilder();
            sb8.append(Mappings.PREFIX);
            sb8.append("$KeyGen_512");
            configurableProvider.addAlgorithm("KeyGenerator.Threefish-512", sb8.toString());
            final StringBuilder sb9 = new StringBuilder();
            sb9.append(Mappings.PREFIX);
            sb9.append("$KeyGen_1024");
            configurableProvider.addAlgorithm("KeyGenerator.Threefish-1024", sb9.toString());
            final StringBuilder sb10 = new StringBuilder();
            sb10.append(Mappings.PREFIX);
            sb10.append("$AlgParams_256");
            configurableProvider.addAlgorithm("AlgorithmParameters.Threefish-256", sb10.toString());
            final StringBuilder sb11 = new StringBuilder();
            sb11.append(Mappings.PREFIX);
            sb11.append("$AlgParams_512");
            configurableProvider.addAlgorithm("AlgorithmParameters.Threefish-512", sb11.toString());
            final StringBuilder sb12 = new StringBuilder();
            sb12.append(Mappings.PREFIX);
            sb12.append("$AlgParams_1024");
            configurableProvider.addAlgorithm("AlgorithmParameters.Threefish-1024", sb12.toString());
        }
    }
}
