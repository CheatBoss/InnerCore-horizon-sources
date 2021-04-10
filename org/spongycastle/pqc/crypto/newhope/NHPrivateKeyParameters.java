package org.spongycastle.pqc.crypto.newhope;

import org.spongycastle.crypto.params.*;
import org.spongycastle.util.*;

public class NHPrivateKeyParameters extends AsymmetricKeyParameter
{
    final short[] secData;
    
    public NHPrivateKeyParameters(final short[] array) {
        super(true);
        this.secData = Arrays.clone(array);
    }
    
    public short[] getSecData() {
        return Arrays.clone(this.secData);
    }
}
