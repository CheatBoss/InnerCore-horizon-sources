package org.spongycastle.jcajce.spec;

import javax.crypto.spec.*;
import org.spongycastle.util.*;

public class AEADParameterSpec extends IvParameterSpec
{
    private final byte[] associatedData;
    private final int macSizeInBits;
    
    public AEADParameterSpec(final byte[] array, final int n) {
        this(array, n, null);
    }
    
    public AEADParameterSpec(final byte[] array, final int macSizeInBits, final byte[] array2) {
        super(array);
        this.macSizeInBits = macSizeInBits;
        this.associatedData = Arrays.clone(array2);
    }
    
    public byte[] getAssociatedData() {
        return Arrays.clone(this.associatedData);
    }
    
    public int getMacSizeInBits() {
        return this.macSizeInBits;
    }
    
    public byte[] getNonce() {
        return this.getIV();
    }
}
