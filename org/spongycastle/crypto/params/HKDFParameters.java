package org.spongycastle.crypto.params;

import org.spongycastle.crypto.*;
import org.spongycastle.util.*;

public class HKDFParameters implements DerivationParameters
{
    private final byte[] ikm;
    private final byte[] info;
    private final byte[] salt;
    private final boolean skipExpand;
    
    private HKDFParameters(byte[] array, final boolean skipExpand, final byte[] array2, final byte[] array3) {
        if (array != null) {
            this.ikm = Arrays.clone(array);
            this.skipExpand = skipExpand;
            if (array2 != null && array2.length != 0) {
                array = Arrays.clone(array2);
            }
            else {
                array = null;
            }
            this.salt = array;
            if (array3 == null) {
                array = new byte[0];
            }
            else {
                array = Arrays.clone(array3);
            }
            this.info = array;
            return;
        }
        throw new IllegalArgumentException("IKM (input keying material) should not be null");
    }
    
    public HKDFParameters(final byte[] array, final byte[] array2, final byte[] array3) {
        this(array, false, array2, array3);
    }
    
    public static HKDFParameters defaultParameters(final byte[] array) {
        return new HKDFParameters(array, false, null, null);
    }
    
    public static HKDFParameters skipExtractParameters(final byte[] array, final byte[] array2) {
        return new HKDFParameters(array, true, null, array2);
    }
    
    public byte[] getIKM() {
        return Arrays.clone(this.ikm);
    }
    
    public byte[] getInfo() {
        return Arrays.clone(this.info);
    }
    
    public byte[] getSalt() {
        return Arrays.clone(this.salt);
    }
    
    public boolean skipExtract() {
        return this.skipExpand;
    }
}
