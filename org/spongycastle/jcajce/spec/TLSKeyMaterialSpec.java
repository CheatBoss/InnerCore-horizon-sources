package org.spongycastle.jcajce.spec;

import java.security.spec.*;
import org.spongycastle.util.*;

public class TLSKeyMaterialSpec implements KeySpec
{
    public static final String KEY_EXPANSION = "key expansion";
    public static final String MASTER_SECRET = "master secret";
    private final String label;
    private final int length;
    private final byte[] secret;
    private final byte[] seed;
    
    public TLSKeyMaterialSpec(final byte[] array, final String label, final int length, final byte[]... array2) {
        this.secret = Arrays.clone(array);
        this.label = label;
        this.length = length;
        this.seed = Arrays.concatenate(array2);
    }
    
    public String getLabel() {
        return this.label;
    }
    
    public int getLength() {
        return this.length;
    }
    
    public byte[] getSecret() {
        return Arrays.clone(this.secret);
    }
    
    public byte[] getSeed() {
        return Arrays.clone(this.seed);
    }
}
