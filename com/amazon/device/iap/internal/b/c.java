package com.amazon.device.iap.internal.b;

import android.content.*;
import com.amazon.device.iap.internal.util.*;
import com.amazon.device.iap.internal.b.a.*;
import com.amazon.device.iap.internal.b.e.*;
import com.amazon.device.iap.model.*;
import com.amazon.device.iap.internal.b.g.*;
import java.util.*;

public final class c implements com.amazon.device.iap.internal.c
{
    private static final String a;
    
    static {
        a = c.class.getSimpleName();
    }
    
    @Override
    public void a(final Context context, final Intent intent) {
        e.a(c.a, "handleResponse");
        final String stringExtra = intent.getStringExtra("response_type");
        if (stringExtra == null) {
            e.a(c.a, "Invalid response type: null");
            return;
        }
        final String a = c.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("Found response type: ");
        sb.append(stringExtra);
        e.a(a, sb.toString());
        if ("purchase_response".equals(stringExtra)) {
            new d(RequestId.fromString(intent.getStringExtra("requestId"))).e();
        }
    }
    
    @Override
    public void a(final RequestId requestId) {
        e.a(c.a, "sendGetUserData");
        new a(requestId).e();
    }
    
    @Override
    public void a(final RequestId requestId, final String s) {
        e.a(c.a, "sendPurchaseRequest");
        new com.amazon.device.iap.internal.b.b.d(requestId, s).e();
    }
    
    @Override
    public void a(final RequestId requestId, final String s, final FulfillmentResult fulfillmentResult) {
        e.a(c.a, "sendNotifyFulfillment");
        new b(requestId, s, fulfillmentResult).e();
    }
    
    @Override
    public void a(final RequestId requestId, final Set<String> set) {
        e.a(c.a, "sendGetProductDataRequest");
        new com.amazon.device.iap.internal.b.c.d(requestId, set).e();
    }
    
    @Override
    public void a(final RequestId requestId, final boolean b) {
        e.a(c.a, "sendGetPurchaseUpdates");
        new com.amazon.device.iap.internal.b.d.a(requestId, b).e();
    }
}
