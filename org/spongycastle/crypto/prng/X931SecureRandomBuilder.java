package org.spongycastle.crypto.prng;

import java.security.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.*;

public class X931SecureRandomBuilder
{
    private byte[] dateTimeVector;
    private EntropySourceProvider entropySourceProvider;
    private SecureRandom random;
    
    public X931SecureRandomBuilder() {
        this(new SecureRandom(), false);
    }
    
    public X931SecureRandomBuilder(final SecureRandom random, final boolean b) {
        this.random = random;
        this.entropySourceProvider = new BasicEntropySourceProvider(this.random, b);
    }
    
    public X931SecureRandomBuilder(final EntropySourceProvider entropySourceProvider) {
        this.random = null;
        this.entropySourceProvider = entropySourceProvider;
    }
    
    public X931SecureRandom build(final BlockCipher blockCipher, final KeyParameter keyParameter, final boolean b) {
        if (this.dateTimeVector == null) {
            this.dateTimeVector = new byte[blockCipher.getBlockSize()];
            Pack.longToBigEndian(System.currentTimeMillis(), this.dateTimeVector, 0);
        }
        blockCipher.init(true, keyParameter);
        return new X931SecureRandom(this.random, new X931RNG(blockCipher, this.dateTimeVector, this.entropySourceProvider.get(blockCipher.getBlockSize() * 8)), b);
    }
    
    public X931SecureRandomBuilder setDateTimeVector(final byte[] dateTimeVector) {
        this.dateTimeVector = dateTimeVector;
        return this;
    }
}
