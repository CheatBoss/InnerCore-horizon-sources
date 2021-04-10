package org.spongycastle.pqc.crypto.sphincs;

import org.spongycastle.crypto.params.*;
import org.spongycastle.util.*;

public class SPHINCSPrivateKeyParameters extends AsymmetricKeyParameter
{
    private final byte[] keyData;
    
    public SPHINCSPrivateKeyParameters(final byte[] array) {
        super(true);
        this.keyData = Arrays.clone(array);
    }
    
    public byte[] getKeyData() {
        return Arrays.clone(this.keyData);
    }
}
