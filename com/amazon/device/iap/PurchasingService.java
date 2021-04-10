package com.amazon.device.iap;

import android.util.*;
import java.util.*;
import com.amazon.device.iap.internal.*;
import com.amazon.device.iap.model.*;
import android.content.*;

public final class PurchasingService
{
    public static final boolean IS_SANDBOX_MODE;
    public static final String SDK_VERSION = "2.0.61.0";
    private static final String TAG;
    
    static {
        TAG = PurchasingService.class.getSimpleName();
        IS_SANDBOX_MODE = e.a();
    }
    
    private PurchasingService() {
        final String tag = PurchasingService.TAG;
        final StringBuilder sb = new StringBuilder();
        sb.append("In-App Purchasing SDK initializing. SDK Version 2.0.61.0, IS_SANDBOX_MODE: ");
        sb.append(PurchasingService.IS_SANDBOX_MODE);
        Log.i(tag, sb.toString());
    }
    
    public static RequestId getProductData(final Set<String> set) {
        return d.d().a(set);
    }
    
    public static RequestId getPurchaseUpdates(final boolean b) {
        return d.d().a(b);
    }
    
    public static RequestId getUserData() {
        return d.d().c();
    }
    
    public static void notifyFulfillment(final String s, final FulfillmentResult fulfillmentResult) {
        d.d().a(s, fulfillmentResult);
    }
    
    public static RequestId purchase(final String s) {
        return d.d().a(s);
    }
    
    public static void registerListener(final Context context, final PurchasingListener purchasingListener) {
        d.d().a(context, purchasingListener);
    }
}
