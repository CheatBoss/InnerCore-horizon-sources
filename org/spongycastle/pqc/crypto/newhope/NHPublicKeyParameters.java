package org.spongycastle.pqc.crypto.newhope;

import org.spongycastle.crypto.params.*;
import org.spongycastle.util.*;

public class NHPublicKeyParameters extends AsymmetricKeyParameter
{
    final byte[] pubData;
    
    public NHPublicKeyParameters(final byte[] array) {
        super(false);
        this.pubData = Arrays.clone(array);
    }
    
    public byte[] getPubData() {
        return Arrays.clone(this.pubData);
    }
}
