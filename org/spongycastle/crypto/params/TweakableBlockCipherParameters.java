package org.spongycastle.crypto.params;

import org.spongycastle.crypto.*;
import org.spongycastle.util.*;

public class TweakableBlockCipherParameters implements CipherParameters
{
    private final KeyParameter key;
    private final byte[] tweak;
    
    public TweakableBlockCipherParameters(final KeyParameter key, final byte[] array) {
        this.key = key;
        this.tweak = Arrays.clone(array);
    }
    
    public KeyParameter getKey() {
        return this.key;
    }
    
    public byte[] getTweak() {
        return this.tweak;
    }
}
