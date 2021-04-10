package org.spongycastle.pqc.crypto.sphincs;

import org.spongycastle.crypto.params.*;
import org.spongycastle.util.*;

public class SPHINCSPublicKeyParameters extends AsymmetricKeyParameter
{
    private final byte[] keyData;
    
    public SPHINCSPublicKeyParameters(final byte[] array) {
        super(false);
        this.keyData = Arrays.clone(array);
    }
    
    public byte[] getKeyData() {
        return Arrays.clone(this.keyData);
    }
}
