package com.amazon.device.iap.internal.b.d;

import com.amazon.device.iap.internal.b.*;
import com.amazon.android.framework.exception.*;

abstract class b extends i
{
    protected final boolean a;
    
    b(final e e, final String s, final boolean a) {
        super(e, "purchase_updates", s);
        this.a = a;
    }
    
    protected void preExecution() throws KiwiException {
        super.preExecution();
        final String s = (String)this.b().d().a("userId");
        Object a;
        if (this.a) {
            a = null;
        }
        else {
            a = com.amazon.device.iap.internal.util.b.a(s);
        }
        this.a("cursor", a);
    }
}
