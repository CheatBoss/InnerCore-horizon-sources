package org.spongycastle.jcajce.provider.symmetric;

import org.spongycastle.crypto.engines.*;
import org.spongycastle.jcajce.provider.symmetric.util.*;
import org.spongycastle.crypto.*;
import org.spongycastle.jcajce.provider.util.*;
import org.spongycastle.jcajce.provider.config.*;

public final class ChaCha
{
    private ChaCha() {
    }
    
    public static class Base extends BaseStreamCipher
    {
        public Base() {
            super(new ChaChaEngine(), 8);
        }
    }
    
    public static class Base7539 extends BaseStreamCipher
    {
        public Base7539() {
            super(new ChaCha7539Engine(), 12);
        }
    }
    
    public static class KeyGen extends BaseKeyGenerator
    {
        public KeyGen() {
            super("ChaCha", 128, new CipherKeyGenerator());
        }
    }
    
    public static class KeyGen7539 extends BaseKeyGenerator
    {
        public KeyGen7539() {
            super("ChaCha7539", 256, new CipherKeyGenerator());
        }
    }
    
    public static class Mappings extends AlgorithmProvider
    {
        private static final String PREFIX;
        
        static {
            PREFIX = ChaCha.class.getName();
        }
        
        @Override
        public void configure(final ConfigurableProvider configurableProvider) {
            final StringBuilder sb = new StringBuilder();
            sb.append(Mappings.PREFIX);
            sb.append("$Base");
            configurableProvider.addAlgorithm("Cipher.CHACHA", sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(Mappings.PREFIX);
            sb2.append("$KeyGen");
            configurableProvider.addAlgorithm("KeyGenerator.CHACHA", sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(Mappings.PREFIX);
            sb3.append("$Base7539");
            configurableProvider.addAlgorithm("Cipher.CHACHA7539", sb3.toString());
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(Mappings.PREFIX);
            sb4.append("$KeyGen7539");
            configurableProvider.addAlgorithm("KeyGenerator.CHACHA7539", sb4.toString());
        }
    }
}
