package org.spongycastle.jcajce.provider.drbg;

import org.spongycastle.crypto.digests.*;
import org.spongycastle.jcajce.provider.symmetric.util.*;
import org.spongycastle.util.*;
import java.security.*;
import org.spongycastle.crypto.prng.*;
import org.spongycastle.crypto.macs.*;
import org.spongycastle.crypto.*;
import java.util.concurrent.atomic.*;
import org.spongycastle.jcajce.provider.util.*;
import org.spongycastle.jcajce.provider.config.*;

public class DRBG
{
    private static final String PREFIX;
    private static final Object[] initialEntropySourceAndSpi;
    private static final String[][] initialEntropySourceNames;
    
    static {
        PREFIX = DRBG.class.getName();
        initialEntropySourceNames = new String[][] { { "sun.security.provider.Sun", "sun.security.provider.SecureRandom" }, { "org.apache.harmony.security.provider.crypto.CryptoProvider", "org.apache.harmony.security.provider.crypto.SHA1PRNG_SecureRandomImpl" }, { "com.android.org.conscrypt.OpenSSLProvider", "com.android.org.conscrypt.OpenSSLRandom" }, { "org.conscrypt.OpenSSLProvider", "org.conscrypt.OpenSSLRandom" } };
        initialEntropySourceAndSpi = findSource();
    }
    
    private static SecureRandom createBaseRandom(final boolean b) {
        if (System.getProperty("org.spongycastle.drbg.entropysource") != null) {
            final EntropySourceProvider entropySource = createEntropySource();
            final EntropySource value = entropySource.get(128);
            byte[] personalizationString;
            if (b) {
                personalizationString = generateDefaultPersonalizationString(value.getEntropy());
            }
            else {
                personalizationString = generateNonceIVPersonalizationString(value.getEntropy());
            }
            return new SP800SecureRandomBuilder(entropySource).setPersonalizationString(personalizationString).buildHash(new SHA512Digest(), Arrays.concatenate(value.getEntropy(), value.getEntropy()), b);
        }
        final HybridSecureRandom hybridSecureRandom = new HybridSecureRandom();
        byte[] personalizationString2;
        if (b) {
            personalizationString2 = generateDefaultPersonalizationString(hybridSecureRandom.generateSeed(16));
        }
        else {
            personalizationString2 = generateNonceIVPersonalizationString(hybridSecureRandom.generateSeed(16));
        }
        return new SP800SecureRandomBuilder(hybridSecureRandom, true).setPersonalizationString(personalizationString2).buildHash(new SHA512Digest(), hybridSecureRandom.generateSeed(32), b);
    }
    
    private static EntropySourceProvider createEntropySource() {
        return AccessController.doPrivileged((PrivilegedAction<EntropySourceProvider>)new PrivilegedAction<EntropySourceProvider>() {
            final /* synthetic */ String val$sourceClass = System.getProperty("org.spongycastle.drbg.entropysource");
            
            @Override
            public EntropySourceProvider run() {
                try {
                    return ClassUtil.loadClass(DRBG.class, this.val$sourceClass).newInstance();
                }
                catch (Exception ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("entropy source ");
                    sb.append(this.val$sourceClass);
                    sb.append(" not created: ");
                    sb.append(ex.getMessage());
                    throw new IllegalStateException(sb.toString(), ex);
                }
            }
        });
    }
    
    private static SecureRandom createInitialEntropySource() {
        if (DRBG.initialEntropySourceAndSpi != null) {
            return new CoreSecureRandom();
        }
        return new SecureRandom();
    }
    
    private static final Object[] findSource() {
        int n = 0;
        while (true) {
            final String[][] initialEntropySourceNames = DRBG.initialEntropySourceNames;
            if (n < initialEntropySourceNames.length) {
                final String[] array = initialEntropySourceNames[n];
                try {
                    return new Object[] { Class.forName(array[0]).newInstance(), Class.forName(array[1]).newInstance() };
                }
                finally {
                    ++n;
                    continue;
                }
                break;
            }
            break;
        }
        return null;
    }
    
    private static byte[] generateDefaultPersonalizationString(final byte[] array) {
        return Arrays.concatenate(Strings.toByteArray("Default"), array, Pack.longToBigEndian(Thread.currentThread().getId()), Pack.longToBigEndian(System.currentTimeMillis()));
    }
    
    private static byte[] generateNonceIVPersonalizationString(final byte[] array) {
        return Arrays.concatenate(Strings.toByteArray("Nonce"), array, Pack.longToLittleEndian(Thread.currentThread().getId()), Pack.longToLittleEndian(System.currentTimeMillis()));
    }
    
    private static class CoreSecureRandom extends SecureRandom
    {
        CoreSecureRandom() {
            super((SecureRandomSpi)DRBG.initialEntropySourceAndSpi[1], (Provider)DRBG.initialEntropySourceAndSpi[0]);
        }
    }
    
    public static class Default extends SecureRandomSpi
    {
        private static final SecureRandom random;
        
        static {
            random = createBaseRandom(true);
        }
        
        @Override
        protected byte[] engineGenerateSeed(final int n) {
            return Default.random.generateSeed(n);
        }
        
        @Override
        protected void engineNextBytes(final byte[] array) {
            Default.random.nextBytes(array);
        }
        
        @Override
        protected void engineSetSeed(final byte[] seed) {
            Default.random.setSeed(seed);
        }
    }
    
    private static class HybridSecureRandom extends SecureRandom
    {
        private final SecureRandom baseRandom;
        private final SP800SecureRandom drbg;
        private final AtomicInteger samples;
        private final AtomicBoolean seedAvailable;
        
        HybridSecureRandom() {
            super(null, null);
            this.seedAvailable = new AtomicBoolean(false);
            this.samples = new AtomicInteger(0);
            this.baseRandom = createInitialEntropySource();
            this.drbg = new SP800SecureRandomBuilder(new EntropySourceProvider() {
                @Override
                public EntropySource get(final int n) {
                    return new SignallingEntropySource(n);
                }
            }).setPersonalizationString(Strings.toByteArray("Bouncy Castle Hybrid Entropy Source")).buildHMAC(new HMac(new SHA512Digest()), this.baseRandom.generateSeed(32), false);
        }
        
        @Override
        public byte[] generateSeed(final int n) {
            final byte[] array = new byte[n];
            if (this.samples.getAndIncrement() > 20 && this.seedAvailable.getAndSet(false)) {
                this.samples.set(0);
                this.drbg.reseed(null);
            }
            this.drbg.nextBytes(array);
            return array;
        }
        
        @Override
        public void setSeed(final long seed) {
            final SP800SecureRandom drbg = this.drbg;
            if (drbg != null) {
                drbg.setSeed(seed);
            }
        }
        
        @Override
        public void setSeed(final byte[] seed) {
            final SP800SecureRandom drbg = this.drbg;
            if (drbg != null) {
                drbg.setSeed(seed);
            }
        }
        
        private class SignallingEntropySource implements EntropySource
        {
            private final int byteLength;
            private final AtomicReference entropy;
            private final AtomicBoolean scheduled;
            
            SignallingEntropySource(final int n) {
                this.entropy = new AtomicReference();
                this.scheduled = new AtomicBoolean(false);
                this.byteLength = (n + 7) / 8;
            }
            
            @Override
            public int entropySize() {
                return this.byteLength * 8;
            }
            
            @Override
            public byte[] getEntropy() {
                byte[] generateSeed = this.entropy.getAndSet(null);
                if (generateSeed != null && generateSeed.length == this.byteLength) {
                    this.scheduled.set(false);
                }
                else {
                    generateSeed = HybridSecureRandom.this.baseRandom.generateSeed(this.byteLength);
                }
                if (!this.scheduled.getAndSet(true)) {
                    new Thread(new EntropyGatherer(this.byteLength)).start();
                }
                return generateSeed;
            }
            
            @Override
            public boolean isPredictionResistant() {
                return true;
            }
            
            private class EntropyGatherer implements Runnable
            {
                private final int numBytes;
                
                EntropyGatherer(final int numBytes) {
                    this.numBytes = numBytes;
                }
                
                @Override
                public void run() {
                    SignallingEntropySource.this.entropy.set(HybridSecureRandom.this.baseRandom.generateSeed(this.numBytes));
                    HybridSecureRandom.this.seedAvailable.set(true);
                }
            }
        }
    }
    
    public static class Mappings extends AsymmetricAlgorithmProvider
    {
        @Override
        public void configure(final ConfigurableProvider configurableProvider) {
            final StringBuilder sb = new StringBuilder();
            sb.append(DRBG.PREFIX);
            sb.append("$Default");
            configurableProvider.addAlgorithm("SecureRandom.DEFAULT", sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(DRBG.PREFIX);
            sb2.append("$NonceAndIV");
            configurableProvider.addAlgorithm("SecureRandom.NONCEANDIV", sb2.toString());
        }
    }
    
    public static class NonceAndIV extends SecureRandomSpi
    {
        private static final SecureRandom random;
        
        static {
            random = createBaseRandom(false);
        }
        
        @Override
        protected byte[] engineGenerateSeed(final int n) {
            return NonceAndIV.random.generateSeed(n);
        }
        
        @Override
        protected void engineNextBytes(final byte[] array) {
            NonceAndIV.random.nextBytes(array);
        }
        
        @Override
        protected void engineSetSeed(final byte[] seed) {
            NonceAndIV.random.setSeed(seed);
        }
    }
}
