package com.amazon.device.iap.internal.b;

import android.os.*;
import com.amazon.device.iap.*;
import com.amazon.device.iap.internal.util.*;
import com.amazon.device.iap.model.*;
import android.content.*;

public class e
{
    private static final String a;
    private final RequestId b;
    private final h c;
    private i d;
    
    static {
        a = e.class.getSimpleName();
    }
    
    public e(final RequestId b) {
        this.b = b;
        this.c = new h();
        this.d = null;
    }
    
    public void a() {
    }
    
    protected void a(final i d) {
        this.d = d;
    }
    
    protected void a(final Object o) {
        this.a(o, null);
    }
    
    protected void a(final Object o, final i i) {
        com.amazon.device.iap.internal.util.d.a(o, "response");
        final Context b = com.amazon.device.iap.internal.d.d().b();
        final PurchasingListener a = com.amazon.device.iap.internal.d.d().a();
        if (b != null && a != null) {
            new Handler(b.getMainLooper()).post((Runnable)new Runnable() {
                @Override
                public void run() {
                    e.this.d().a("notifyListenerResult", Boolean.FALSE);
                    try {
                        if (o instanceof ProductDataResponse) {
                            a.onProductDataResponse((ProductDataResponse)o);
                        }
                        else if (o instanceof UserDataResponse) {
                            a.onUserDataResponse((UserDataResponse)o);
                        }
                        else if (o instanceof PurchaseUpdatesResponse) {
                            final PurchaseUpdatesResponse purchaseUpdatesResponse = (PurchaseUpdatesResponse)o;
                            a.onPurchaseUpdatesResponse(purchaseUpdatesResponse);
                            final Object a = e.this.d().a("newCursor");
                            if (a != null && a instanceof String) {
                                com.amazon.device.iap.internal.util.b.a(purchaseUpdatesResponse.getUserData().getUserId(), a.toString());
                            }
                        }
                        else if (o instanceof PurchaseResponse) {
                            a.onPurchaseResponse((PurchaseResponse)o);
                        }
                        else {
                            final String f = e.a;
                            final StringBuilder sb = new StringBuilder();
                            sb.append("Unknown response type:");
                            sb.append(o.getClass().getName());
                            com.amazon.device.iap.internal.util.e.b(f, sb.toString());
                        }
                        e.this.d().a("notifyListenerResult", Boolean.TRUE);
                    }
                    finally {
                        final String f2 = e.a;
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("Error in sendResponse: ");
                        final Throwable t;
                        sb2.append(t);
                        com.amazon.device.iap.internal.util.e.b(f2, sb2.toString());
                    }
                    final i c = i;
                    if (c != null) {
                        c.a(true);
                        i.a_();
                    }
                }
            });
            return;
        }
        final String a2 = e.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("PurchasingListener is not set. Dropping response: ");
        sb.append(o);
        com.amazon.device.iap.internal.util.e.a(a2, sb.toString());
    }
    
    public void b() {
    }
    
    public RequestId c() {
        return this.b;
    }
    
    public h d() {
        return this.c;
    }
    
    public void e() {
        final i d = this.d;
        if (d != null) {
            d.a_();
            return;
        }
        this.a();
    }
}
