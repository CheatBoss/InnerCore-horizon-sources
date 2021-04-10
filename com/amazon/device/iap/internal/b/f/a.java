package com.amazon.device.iap.internal.b.f;

import com.amazon.device.iap.internal.b.*;
import com.amazon.venezia.command.*;

abstract class a extends i
{
    a(final e e, final String s) {
        super(e, "response_received", s);
        this.b(false);
    }
    
    @Override
    protected boolean a(final SuccessResult successResult) throws Exception {
        return true;
    }
}
