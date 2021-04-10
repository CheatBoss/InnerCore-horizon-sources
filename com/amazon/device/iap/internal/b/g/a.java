package com.amazon.device.iap.internal.b.g;

import java.util.*;
import com.amazon.device.iap.internal.b.*;
import com.amazon.venezia.command.*;
import android.os.*;
import com.amazon.android.framework.exception.*;

public final class a extends i
{
    protected final Set<String> a;
    protected final String b;
    
    public a(final e e, final Set<String> a, final String b) {
        super(e, "purchase_fulfilled", "2.0");
        this.a = a;
        this.b = b;
        this.b(false);
        this.a("receiptIds", this.a);
        this.a("fulfillmentStatus", this.b);
    }
    
    @Override
    protected boolean a(final SuccessResult successResult) throws RemoteException, KiwiException {
        return true;
    }
    
    @Override
    public void a_() {
        final Object a = this.b().d().a("notifyListenerResult");
        if (a != null && Boolean.FALSE.equals(a)) {
            this.a("fulfillmentStatus", com.amazon.device.iap.internal.model.a.b.toString());
        }
        super.a_();
    }
}
