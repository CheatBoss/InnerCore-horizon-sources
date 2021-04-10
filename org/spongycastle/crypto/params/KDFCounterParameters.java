package org.spongycastle.crypto.params;

import org.spongycastle.crypto.*;
import org.spongycastle.util.*;

public final class KDFCounterParameters implements DerivationParameters
{
    private byte[] fixedInputDataCounterPrefix;
    private byte[] fixedInputDataCounterSuffix;
    private byte[] ki;
    private int r;
    
    public KDFCounterParameters(final byte[] array, final byte[] array2, final int n) {
        this(array, null, array2, n);
    }
    
    public KDFCounterParameters(byte[] array, final byte[] array2, final byte[] array3, final int r) {
        if (array == null) {
            throw new IllegalArgumentException("A KDF requires Ki (a seed) as input");
        }
        this.ki = Arrays.clone(array);
        if (array2 == null) {
            array = new byte[0];
        }
        else {
            array = Arrays.clone(array2);
        }
        this.fixedInputDataCounterPrefix = array;
        if (array3 == null) {
            array = new byte[0];
        }
        else {
            array = Arrays.clone(array3);
        }
        this.fixedInputDataCounterSuffix = array;
        if (r != 8 && r != 16 && r != 24 && r != 32) {
            throw new IllegalArgumentException("Length of counter should be 8, 16, 24 or 32");
        }
        this.r = r;
    }
    
    public byte[] getFixedInputData() {
        return Arrays.clone(this.fixedInputDataCounterSuffix);
    }
    
    public byte[] getFixedInputDataCounterPrefix() {
        return Arrays.clone(this.fixedInputDataCounterPrefix);
    }
    
    public byte[] getFixedInputDataCounterSuffix() {
        return Arrays.clone(this.fixedInputDataCounterSuffix);
    }
    
    public byte[] getKI() {
        return this.ki;
    }
    
    public int getR() {
        return this.r;
    }
}
