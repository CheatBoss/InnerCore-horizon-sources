package org.spongycastle.jcajce.spec;

import java.security.spec.*;
import org.spongycastle.util.*;

public class UserKeyingMaterialSpec implements AlgorithmParameterSpec
{
    private final byte[] userKeyingMaterial;
    
    public UserKeyingMaterialSpec(final byte[] array) {
        this.userKeyingMaterial = Arrays.clone(array);
    }
    
    public byte[] getUserKeyingMaterial() {
        return Arrays.clone(this.userKeyingMaterial);
    }
}
