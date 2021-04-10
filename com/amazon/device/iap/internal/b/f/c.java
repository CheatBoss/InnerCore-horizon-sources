package com.amazon.device.iap.internal.b.f;

import com.amazon.device.iap.internal.b.*;

public final class c extends a
{
    public c(final e e, final boolean b) {
        super(e, "2.0");
        this.a("receiptDelivered", b);
    }
    
    @Override
    public void a_() {
        final Object a = this.b().d().a("notifyListenerResult");
        this.a("notifyListenerSucceeded", a != null && Boolean.TRUE.equals(a));
        super.a_();
    }
}
