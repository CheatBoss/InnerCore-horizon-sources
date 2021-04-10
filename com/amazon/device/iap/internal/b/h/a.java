package com.amazon.device.iap.internal.b.h;

import com.amazon.device.iap.internal.b.*;
import com.amazon.venezia.command.*;
import android.os.*;
import com.amazon.android.framework.exception.*;

public class a extends i
{
    public a(final e e, final String s, final String s2) {
        super(e, "submit_metric", "1.0");
        this.a("metricName", s);
        this.a("metricAttributes", s2);
        this.b(false);
    }
    
    @Override
    protected boolean a(final SuccessResult successResult) throws RemoteException, KiwiException {
        return true;
    }
}
