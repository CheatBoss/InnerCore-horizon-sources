package org.spongycastle.crypto.prng;

import java.security.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.prng.drbg.*;

public class SP800SecureRandomBuilder
{
    private int entropyBitsRequired;
    private final EntropySourceProvider entropySourceProvider;
    private byte[] personalizationString;
    private final SecureRandom random;
    private int securityStrength;
    
    public SP800SecureRandomBuilder() {
        this(new SecureRandom(), false);
    }
    
    public SP800SecureRandomBuilder(final SecureRandom random, final boolean b) {
        this.securityStrength = 256;
        this.entropyBitsRequired = 256;
        this.random = random;
        this.entropySourceProvider = new BasicEntropySourceProvider(this.random, b);
    }
    
    public SP800SecureRandomBuilder(final EntropySourceProvider entropySourceProvider) {
        this.securityStrength = 256;
        this.entropyBitsRequired = 256;
        this.random = null;
        this.entropySourceProvider = entropySourceProvider;
    }
    
    public SP800SecureRandom buildCTR(final BlockCipher blockCipher, final int n, final byte[] array, final boolean b) {
        return new SP800SecureRandom(this.random, this.entropySourceProvider.get(this.entropyBitsRequired), new CTRDRBGProvider(blockCipher, n, array, this.personalizationString, this.securityStrength), b);
    }
    
    public SP800SecureRandom buildHMAC(final Mac mac, final byte[] array, final boolean b) {
        return new SP800SecureRandom(this.random, this.entropySourceProvider.get(this.entropyBitsRequired), new HMacDRBGProvider(mac, array, this.personalizationString, this.securityStrength), b);
    }
    
    public SP800SecureRandom buildHash(final Digest digest, final byte[] array, final boolean b) {
        return new SP800SecureRandom(this.random, this.entropySourceProvider.get(this.entropyBitsRequired), new HashDRBGProvider(digest, array, this.personalizationString, this.securityStrength), b);
    }
    
    public SP800SecureRandomBuilder setEntropyBitsRequired(final int entropyBitsRequired) {
        this.entropyBitsRequired = entropyBitsRequired;
        return this;
    }
    
    public SP800SecureRandomBuilder setPersonalizationString(final byte[] personalizationString) {
        this.personalizationString = personalizationString;
        return this;
    }
    
    public SP800SecureRandomBuilder setSecurityStrength(final int securityStrength) {
        this.securityStrength = securityStrength;
        return this;
    }
    
    private static class CTRDRBGProvider implements DRBGProvider
    {
        private final BlockCipher blockCipher;
        private final int keySizeInBits;
        private final byte[] nonce;
        private final byte[] personalizationString;
        private final int securityStrength;
        
        public CTRDRBGProvider(final BlockCipher blockCipher, final int keySizeInBits, final byte[] nonce, final byte[] personalizationString, final int securityStrength) {
            this.blockCipher = blockCipher;
            this.keySizeInBits = keySizeInBits;
            this.nonce = nonce;
            this.personalizationString = personalizationString;
            this.securityStrength = securityStrength;
        }
        
        @Override
        public SP80090DRBG get(final EntropySource entropySource) {
            return new CTRSP800DRBG(this.blockCipher, this.keySizeInBits, this.securityStrength, entropySource, this.personalizationString, this.nonce);
        }
    }
    
    private static class HMacDRBGProvider implements DRBGProvider
    {
        private final Mac hMac;
        private final byte[] nonce;
        private final byte[] personalizationString;
        private final int securityStrength;
        
        public HMacDRBGProvider(final Mac hMac, final byte[] nonce, final byte[] personalizationString, final int securityStrength) {
            this.hMac = hMac;
            this.nonce = nonce;
            this.personalizationString = personalizationString;
            this.securityStrength = securityStrength;
        }
        
        @Override
        public SP80090DRBG get(final EntropySource entropySource) {
            return new HMacSP800DRBG(this.hMac, this.securityStrength, entropySource, this.personalizationString, this.nonce);
        }
    }
    
    private static class HashDRBGProvider implements DRBGProvider
    {
        private final Digest digest;
        private final byte[] nonce;
        private final byte[] personalizationString;
        private final int securityStrength;
        
        public HashDRBGProvider(final Digest digest, final byte[] nonce, final byte[] personalizationString, final int securityStrength) {
            this.digest = digest;
            this.nonce = nonce;
            this.personalizationString = personalizationString;
            this.securityStrength = securityStrength;
        }
        
        @Override
        public SP80090DRBG get(final EntropySource entropySource) {
            return new HashSP800DRBG(this.digest, this.securityStrength, entropySource, this.personalizationString, this.nonce);
        }
    }
}
