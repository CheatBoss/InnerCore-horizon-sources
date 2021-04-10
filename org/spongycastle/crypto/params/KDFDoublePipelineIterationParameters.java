package org.spongycastle.crypto.params;

import org.spongycastle.crypto.*;
import org.spongycastle.util.*;

public final class KDFDoublePipelineIterationParameters implements DerivationParameters
{
    private static final int UNUSED_R = 32;
    private final byte[] fixedInputData;
    private final byte[] ki;
    private final int r;
    private final boolean useCounter;
    
    private KDFDoublePipelineIterationParameters(byte[] clone, final byte[] array, final int r, final boolean useCounter) {
        if (clone == null) {
            throw new IllegalArgumentException("A KDF requires Ki (a seed) as input");
        }
        this.ki = Arrays.clone(clone);
        if (array == null) {
            clone = new byte[0];
        }
        else {
            clone = Arrays.clone(array);
        }
        this.fixedInputData = clone;
        if (r != 8 && r != 16 && r != 24 && r != 32) {
            throw new IllegalArgumentException("Length of counter should be 8, 16, 24 or 32");
        }
        this.r = r;
        this.useCounter = useCounter;
    }
    
    public static KDFDoublePipelineIterationParameters createWithCounter(final byte[] array, final byte[] array2, final int n) {
        return new KDFDoublePipelineIterationParameters(array, array2, n, true);
    }
    
    public static KDFDoublePipelineIterationParameters createWithoutCounter(final byte[] array, final byte[] array2) {
        return new KDFDoublePipelineIterationParameters(array, array2, 32, false);
    }
    
    public byte[] getFixedInputData() {
        return Arrays.clone(this.fixedInputData);
    }
    
    public byte[] getKI() {
        return this.ki;
    }
    
    public int getR() {
        return this.r;
    }
    
    public boolean useCounter() {
        return this.useCounter;
    }
}
