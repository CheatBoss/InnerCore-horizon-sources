package com.amazon.device.iap.internal.b.b;

import com.amazon.device.iap.internal.b.*;
import com.amazon.android.framework.exception.*;

public final class b extends a
{
    public b(final e e, final String s) {
        super(e, "1.0", s);
    }
    
    protected void preExecution() throws KiwiException {
        super.preExecution();
        com.amazon.device.iap.internal.c.b.a().b(this.c());
    }
}
