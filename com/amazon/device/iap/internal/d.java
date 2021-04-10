package com.amazon.device.iap.internal;

import com.amazon.device.iap.*;
import java.util.*;
import android.content.*;
import com.amazon.device.iap.model.*;

public class d
{
    private static String a;
    private static String b;
    private static d c;
    private final c d;
    private Context e;
    private PurchasingListener f;
    
    static {
        d.a = d.class.getSimpleName();
        d.b = "sku";
        d.c = new d();
    }
    
    private d() {
        this.d = com.amazon.device.iap.internal.e.b();
    }
    
    public static d d() {
        return d.c;
    }
    
    private void e() {
        if (this.f != null) {
            return;
        }
        throw new IllegalStateException("You must register a PurchasingListener before invoking this operation");
    }
    
    public PurchasingListener a() {
        return this.f;
    }
    
    public RequestId a(final String s) {
        com.amazon.device.iap.internal.util.d.a((Object)s, com.amazon.device.iap.internal.d.b);
        this.e();
        final RequestId requestId = new RequestId();
        this.d.a(requestId, s);
        return requestId;
    }
    
    public RequestId a(final Set<String> set) {
        com.amazon.device.iap.internal.util.d.a((Object)set, "skus");
        com.amazon.device.iap.internal.util.d.a(set, "skus");
        final Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().trim().length() != 0) {
                continue;
            }
            throw new IllegalArgumentException("Empty SKU values are not allowed");
        }
        if (set.size() <= 100) {
            this.e();
            final RequestId requestId = new RequestId();
            this.d.a(requestId, new LinkedHashSet<String>(set));
            return requestId;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(set.size());
        sb.append(" SKUs were provided, but no more than ");
        sb.append(100);
        sb.append(" SKUs are allowed");
        throw new IllegalArgumentException(sb.toString());
    }
    
    public RequestId a(final boolean b) {
        this.e();
        final RequestId requestId = new RequestId();
        this.d.a(requestId, b);
        return requestId;
    }
    
    public void a(final Context context, final Intent intent) {
        try {
            this.d.a(context, intent);
        }
        catch (Exception ex) {
            final String a = com.amazon.device.iap.internal.d.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Error in onReceive: ");
            sb.append(ex);
            com.amazon.device.iap.internal.util.e.b(a, sb.toString());
        }
    }
    
    public void a(final Context context, final PurchasingListener f) {
        final String a = com.amazon.device.iap.internal.d.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("PurchasingListener registered: ");
        sb.append(f);
        com.amazon.device.iap.internal.util.e.a(a, sb.toString());
        final String a2 = com.amazon.device.iap.internal.d.a;
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("PurchasingListener Context: ");
        sb2.append(context);
        com.amazon.device.iap.internal.util.e.a(a2, sb2.toString());
        if (f != null && context != null) {
            this.e = context.getApplicationContext();
            this.f = f;
            return;
        }
        throw new IllegalArgumentException("Neither PurchasingListener or its Context can be null");
    }
    
    public void a(final String s, final FulfillmentResult fulfillmentResult) {
        if (!com.amazon.device.iap.internal.util.d.a(s)) {
            com.amazon.device.iap.internal.util.d.a(fulfillmentResult, "fulfillmentResult");
            this.e();
            this.d.a(new RequestId(), s, fulfillmentResult);
            return;
        }
        throw new IllegalArgumentException("Empty receiptId is not allowed");
    }
    
    public Context b() {
        return this.e;
    }
    
    public RequestId c() {
        this.e();
        final RequestId requestId = new RequestId();
        this.d.a(requestId);
        return requestId;
    }
}
