package org.spongycastle.crypto.params;

import org.spongycastle.crypto.*;
import org.spongycastle.util.*;

public final class KDFFeedbackParameters implements DerivationParameters
{
    private static final int UNUSED_R = -1;
    private final byte[] fixedInputData;
    private final byte[] iv;
    private final byte[] ki;
    private final int r;
    private final boolean useCounter;
    
    private KDFFeedbackParameters(byte[] array, final byte[] array2, final byte[] array3, final int r, final boolean useCounter) {
        if (array != null) {
            this.ki = Arrays.clone(array);
            if (array3 == null) {
                array = new byte[0];
            }
            else {
                array = Arrays.clone(array3);
            }
            this.fixedInputData = array;
            this.r = r;
            if (array2 == null) {
                array = new byte[0];
            }
            else {
                array = Arrays.clone(array2);
            }
            this.iv = array;
            this.useCounter = useCounter;
            return;
        }
        throw new IllegalArgumentException("A KDF requires Ki (a seed) as input");
    }
    
    public static KDFFeedbackParameters createWithCounter(final byte[] array, final byte[] array2, final byte[] array3, final int n) {
        if (n != 8 && n != 16 && n != 24 && n != 32) {
            throw new IllegalArgumentException("Length of counter should be 8, 16, 24 or 32");
        }
        return new KDFFeedbackParameters(array, array2, array3, n, true);
    }
    
    public static KDFFeedbackParameters createWithoutCounter(final byte[] array, final byte[] array2, final byte[] array3) {
        return new KDFFeedbackParameters(array, array2, array3, -1, false);
    }
    
    public byte[] getFixedInputData() {
        return Arrays.clone(this.fixedInputData);
    }
    
    public byte[] getIV() {
        return this.iv;
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
