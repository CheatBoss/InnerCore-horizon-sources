package com.amazon.device.iap.internal.a;

import java.math.*;
import java.text.*;
import android.util.*;
import com.amazon.device.iap.internal.*;
import com.amazon.device.iap.internal.util.*;
import android.content.*;
import java.io.*;
import org.json.*;
import com.amazon.device.iap.internal.model.*;
import com.amazon.device.iap.model.*;
import java.util.*;
import android.os.*;
import com.amazon.device.iap.*;

public final class c implements com.amazon.device.iap.internal.c
{
    private static final String a;
    
    static {
        a = c.class.getSimpleName();
    }
    
    private Intent a(final String s) {
        final Intent intent = new Intent(s);
        intent.setComponent(new ComponentName("com.amazon.sdktestclient", "com.amazon.sdktestclient.command.CommandBroker"));
        return intent;
    }
    
    private Product a(final String sku, final JSONObject jsonObject) throws JSONException {
        final ProductType value = ProductType.valueOf(jsonObject.optString("itemType"));
        final JSONObject jsonObject2 = jsonObject.getJSONObject("priceJson");
        final Currency instance = Currency.getInstance(jsonObject2.optString("currency"));
        final BigDecimal bigDecimal = new BigDecimal(jsonObject2.optString("value"));
        final StringBuilder sb = new StringBuilder();
        sb.append(instance.getSymbol());
        sb.append(bigDecimal);
        return new ProductBuilder().setSku(sku).setProductType(value).setDescription(jsonObject.optString("description")).setPrice(sb.toString()).setSmallIconUrl(jsonObject.optString("smallIconUrl")).setTitle(jsonObject.optString("title")).build();
    }
    
    private Receipt a(final JSONObject jsonObject) throws ParseException {
        final String optString = jsonObject.optString("receiptId");
        final String optString2 = jsonObject.optString("sku");
        final ProductType value = ProductType.valueOf(jsonObject.optString("itemType"));
        final Date parse = b.a.parse(jsonObject.optString("purchaseDate"));
        final String optString3 = jsonObject.optString("cancelDate");
        Date parse2;
        if (optString3 != null && optString3.length() != 0) {
            parse2 = b.a.parse(optString3);
        }
        else {
            parse2 = null;
        }
        return new ReceiptBuilder().setReceiptId(optString).setSku(optString2).setProductType(value).setPurchaseDate(parse).setCancelDate(parse2).build();
    }
    
    private void a(final Intent intent) throws JSONException {
        final PurchaseUpdatesResponse b = this.b(intent);
        if (b.getRequestStatus() == PurchaseUpdatesResponse.RequestStatus.SUCCESSFUL) {
            final String optString = new JSONObject(intent.getStringExtra("purchaseUpdatesOutput")).optString("offset");
            final String a = c.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Offset for PurchaseUpdatesResponse:");
            sb.append(optString);
            Log.i(a, sb.toString());
            com.amazon.device.iap.internal.util.b.a(b.getUserData().getUserId(), optString);
        }
        this.a(b);
    }
    
    private void a(String s, final String s2, final boolean b) {
        try {
            final Context b2 = d.d().b();
            final String a = com.amazon.device.iap.internal.util.b.a(s2);
            final String a2 = c.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("send PurchaseUpdates with user id:");
            sb.append(s2);
            sb.append(";reset flag:");
            sb.append(b);
            sb.append(", local cursor:");
            sb.append(a);
            sb.append(", parsed from old requestId:");
            sb.append(s);
            Log.i(a2, sb.toString());
            final Bundle bundle = new Bundle();
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("requestId", (Object)s.toString());
            s = a;
            if (b) {
                s = null;
            }
            jsonObject.put("offset", (Object)s);
            jsonObject.put("sdkVersion", (Object)"2.0.61.0");
            jsonObject.put("packageName", (Object)b2.getPackageName());
            bundle.putString("purchaseUpdatesInput", jsonObject.toString());
            final Intent a3 = this.a("com.amazon.testclient.iap.purchaseUpdates");
            a3.addFlags(268435456);
            a3.putExtras(bundle);
            b2.startService(a3);
        }
        catch (JSONException ex) {
            e.b(c.a, "Error in sendPurchaseUpdatesRequest.");
        }
    }
    
    private void a(final String s, final boolean b, final boolean b2) {
        try {
            final Context b3 = d.d().b();
            final Bundle bundle = new Bundle();
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("requestId", (Object)s);
            jsonObject.put("packageName", (Object)b3.getPackageName());
            jsonObject.put("sdkVersion", (Object)"2.0.61.0");
            jsonObject.put("isPurchaseUpdates", b);
            jsonObject.put("reset", b2);
            bundle.putString("userInput", jsonObject.toString());
            final Intent a = this.a("com.amazon.testclient.iap.appUserId");
            a.addFlags(268435456);
            a.putExtras(bundle);
            b3.startService(a);
        }
        catch (JSONException ex) {
            e.b(c.a, "Error in sendGetUserDataRequest.");
        }
    }
    
    private PurchaseUpdatesResponse b(Intent intent) {
        Serializable marketplace = PurchaseUpdatesResponse.RequestStatus.FAILED;
        boolean optBoolean = false;
        int i = 0;
        Object build = null;
        Object optJSONObject = null;
        Object value;
        String s2 = null;
        Throwable t;
        try {
            final JSONObject jsonObject = new JSONObject(intent.getStringExtra("purchaseUpdatesOutput"));
            final RequestId fromString = RequestId.fromString(jsonObject.optString("requestId"));
            String s = null;
            Label_0292: {
                try {
                    value = PurchaseUpdatesResponse.RequestStatus.valueOf(jsonObject.optString("status"));
                    try {
                        optBoolean = jsonObject.optBoolean("isMore");
                        try {
                            final String optString = jsonObject.optString("userId");
                            marketplace = jsonObject.optString("marketplace");
                            build = new UserDataBuilder().setUserId(optString).setMarketplace((String)marketplace).build();
                            try {
                                if (value == PurchaseUpdatesResponse.RequestStatus.SUCCESSFUL) {
                                    intent = (Intent)new ArrayList();
                                    try {
                                        final JSONArray optJSONArray = jsonObject.optJSONArray("receipts");
                                        if (optJSONArray != null) {
                                            while (i < optJSONArray.length()) {
                                                optJSONObject = optJSONArray.optJSONObject(i);
                                                try {
                                                    ((List<Receipt>)intent).add(this.a((JSONObject)optJSONObject));
                                                }
                                                catch (Exception ex) {
                                                    final String a = c.a;
                                                    final StringBuilder sb = new StringBuilder();
                                                    sb.append("Failed to parse receipt from json:");
                                                    sb.append(optJSONObject);
                                                    Log.e(a, sb.toString());
                                                }
                                                ++i;
                                            }
                                        }
                                        return new PurchaseUpdatesResponseBuilder().setRequestId(fromString).setRequestStatus((PurchaseUpdatesResponse.RequestStatus)value).setUserData((UserData)build).setReceipts((List<Receipt>)intent).setHasMore(optBoolean).build();
                                    }
                                    catch (Exception marketplace) {
                                        optJSONObject = intent;
                                        s = (String)marketplace;
                                        marketplace = (Serializable)value;
                                        value = build;
                                        break Label_0292;
                                    }
                                }
                                intent = null;
                            }
                            catch (Exception s) {
                                marketplace = (Serializable)value;
                                value = build;
                            }
                        }
                        catch (Exception s) {
                            build = null;
                            marketplace = (Serializable)value;
                            value = build;
                        }
                    }
                    catch (Exception s) {}
                }
                catch (Exception s) {
                    value = marketplace;
                }
                marketplace = (Serializable)value;
                value = null;
                optBoolean = false;
            }
            build = fromString;
            s2 = s;
            intent = (Intent)optJSONObject;
            t = (Throwable)build;
        }
        catch (Exception s2) {
            intent = (Intent)(value = null);
            t = (Throwable)build;
        }
        Log.e(c.a, "Error parsing purchase updates output", (Throwable)s2);
        final RequestId fromString = (RequestId)t;
        build = value;
        value = marketplace;
        return new PurchaseUpdatesResponseBuilder().setRequestId(fromString).setRequestStatus((PurchaseUpdatesResponse.RequestStatus)value).setUserData((UserData)build).setReceipts((List<Receipt>)intent).setHasMore(optBoolean).build();
    }
    
    private void c(final Intent intent) {
        this.a(this.d(intent));
    }
    
    private ProductDataResponse d(Intent value) {
        Object failed = ProductDataResponse.RequestStatus.FAILED;
        final Map<String, Product> map = null;
        Intent intent = null;
        Object unavailableSkus = null;
        Intent intent2 = null;
        Object o = null;
        Label_0366: {
            Object fromString;
            Object o3;
            Object o6 = null;
            try {
                final JSONObject jsonObject = new JSONObject(value.getStringExtra("itemDataOutput"));
                fromString = RequestId.fromString(jsonObject.optString("requestId"));
                Object o5 = null;
                Label_0296: {
                    try {
                        value = (Intent)ProductDataResponse.RequestStatus.valueOf(jsonObject.optString("status"));
                        try {
                            if (value != ProductDataResponse.RequestStatus.FAILED) {
                                failed = new LinkedHashSet<String>();
                                try {
                                    final HashMap hashMap = new HashMap<String, Product>();
                                    try {
                                        final JSONArray optJSONArray = jsonObject.optJSONArray("unavailableSkus");
                                        if (optJSONArray != null) {
                                            for (int i = 0; i < optJSONArray.length(); ++i) {
                                                ((Set<String>)failed).add(optJSONArray.getString(i));
                                            }
                                        }
                                        final JSONObject optJSONObject = jsonObject.optJSONObject("items");
                                        unavailableSkus = failed;
                                        intent2 = value;
                                        intent = (Intent)hashMap;
                                        o = fromString;
                                        if (optJSONObject == null) {
                                            break Label_0366;
                                        }
                                        final Iterator keys = optJSONObject.keys();
                                        while (true) {
                                            unavailableSkus = failed;
                                            intent2 = value;
                                            intent = (Intent)hashMap;
                                            o = fromString;
                                            if (!keys.hasNext()) {
                                                break Label_0366;
                                            }
                                            final String s = keys.next();
                                            hashMap.put(s, this.a(s, optJSONObject.optJSONObject(s)));
                                        }
                                    }
                                    catch (Exception o6) {
                                        final Object o2 = fromString;
                                        fromString = failed;
                                        failed = o2;
                                        o3 = hashMap;
                                    }
                                }
                                catch (Exception ex) {
                                    final Object o4 = failed;
                                    failed = value;
                                    value = (Intent)o4;
                                    o5 = ex;
                                    break Label_0296;
                                }
                            }
                            unavailableSkus = null;
                            final Map<String, Product> productData = map;
                        }
                        catch (Exception failed) {}
                    }
                    catch (Exception ex2) {
                        value = (Intent)failed;
                        failed = ex2;
                    }
                    final Intent intent3 = null;
                    o5 = failed;
                    failed = value;
                    value = intent3;
                }
                final Intent intent4 = null;
                intent = (Intent)fromString;
                o6 = o5;
                final Object o7 = failed;
                fromString = value;
                failed = intent;
                value = (Intent)o7;
                o3 = intent4;
            }
            catch (Exception o6) {
                value = (Intent)failed;
                fromString = (o3 = null);
                failed = intent;
            }
            Log.e(c.a, "Error parsing item data output", (Throwable)o6);
            o = failed;
            intent = (Intent)o3;
            intent2 = value;
            unavailableSkus = fromString;
        }
        final Map<String, Product> productData = (Map<String, Product>)intent;
        Object fromString = o;
        value = intent2;
        return new ProductDataResponseBuilder().setRequestId((RequestId)fromString).setRequestStatus((ProductDataResponse.RequestStatus)value).setProductData(productData).setUnavailableSkus((Set<String>)unavailableSkus).build();
    }
    
    private void e(final Intent intent) {
        final UserDataResponse f = this.f(intent);
        final RequestId requestId = f.getRequestId();
        final String stringExtra = intent.getStringExtra("userInput");
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(stringExtra);
        }
        catch (JSONException ex) {
            final String a = c.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Unable to parse request data: ");
            sb.append(stringExtra);
            Log.e(a, sb.toString(), (Throwable)ex);
            jsonObject = null;
        }
        if (requestId == null || jsonObject == null) {
            this.a(f);
            return;
        }
        if (!jsonObject.optBoolean("isPurchaseUpdates", false)) {
            this.a(f);
            return;
        }
        if (f.getUserData() != null && !com.amazon.device.iap.internal.util.d.a(f.getUserData().getUserId())) {
            final String a2 = c.a;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("sendGetPurchaseUpdates with user id");
            sb2.append(f.getUserData().getUserId());
            Log.i(a2, sb2.toString());
            this.a(requestId.toString(), f.getUserData().getUserId(), jsonObject.optBoolean("reset", true));
            return;
        }
        final String a3 = c.a;
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("No Userid found in userDataResponse");
        sb3.append(f);
        Log.e(a3, sb3.toString());
        this.a(new PurchaseUpdatesResponseBuilder().setRequestId(requestId).setRequestStatus(PurchaseUpdatesResponse.RequestStatus.FAILED).setUserData(f.getUserData()).setReceipts(new ArrayList<Receipt>()).setHasMore(false).build());
    }
    
    private UserDataResponse f(Intent value) {
        Object failed = UserDataResponse.RequestStatus.FAILED;
        final UserData userData = null;
        RequestId requestId2 = null;
        try {
            final JSONObject jsonObject = new JSONObject(value.getStringExtra("userOutput"));
            final RequestId fromString = RequestId.fromString(jsonObject.optString("requestId"));
            try {
                value = (Intent)UserDataResponse.RequestStatus.valueOf(jsonObject.optString("status"));
                UserData build = userData;
                Intent requestStatus = value;
                failed = fromString;
                try {
                    if (value == UserDataResponse.RequestStatus.SUCCESSFUL) {
                        build = new UserDataBuilder().setUserId(jsonObject.optString("userId")).setMarketplace(jsonObject.optString("marketplace")).build();
                        requestStatus = value;
                        failed = fromString;
                        return new UserDataResponseBuilder().setRequestId((RequestId)failed).setRequestStatus((UserDataResponse.RequestStatus)requestStatus).setUserData(build).build();
                    }
                    return new UserDataResponseBuilder().setRequestId((RequestId)failed).setRequestStatus((UserDataResponse.RequestStatus)requestStatus).setUserData(build).build();
                }
                catch (Exception failed) {}
            }
            catch (Exception ex) {
                value = (Intent)failed;
                failed = ex;
            }
            final RequestId requestId = fromString;
            requestId2 = (RequestId)failed;
            failed = requestId;
        }
        catch (Exception requestId2) {
            value = (Intent)failed;
            failed = null;
        }
        Log.e(c.a, "Error parsing userid output", (Throwable)requestId2);
        Intent requestStatus = value;
        UserData build = userData;
        return new UserDataResponseBuilder().setRequestId((RequestId)failed).setRequestStatus((UserDataResponse.RequestStatus)requestStatus).setUserData(build).build();
    }
    
    private void g(final Intent intent) {
        this.a(this.h(intent));
    }
    
    private PurchaseResponse h(Intent fromString) {
        Object failed = PurchaseResponse.RequestStatus.FAILED;
        final Receipt receipt = null;
        Object safeValue;
        RequestId requestId2 = null;
        try {
            final JSONObject jsonObject = new JSONObject(fromString.getStringExtra("purchaseOutput"));
            fromString = (Intent)RequestId.fromString(jsonObject.optString("requestId"));
            try {
                final UserData build = new UserDataBuilder().setUserId(jsonObject.optString("userId")).setMarketplace(jsonObject.optString("marketplace")).build();
                try {
                    safeValue = PurchaseResponse.RequestStatus.safeValueOf(jsonObject.optString("purchaseStatus"));
                    try {
                        final JSONObject optJSONObject = jsonObject.optJSONObject("receipt");
                        Receipt a = receipt;
                        Object userData = build;
                        Object requestStatus = safeValue;
                        failed = fromString;
                        if (optJSONObject != null) {
                            a = this.a(optJSONObject);
                            userData = build;
                            requestStatus = safeValue;
                            failed = fromString;
                            return new PurchaseResponseBuilder().setRequestId((RequestId)failed).setRequestStatus((PurchaseResponse.RequestStatus)requestStatus).setUserData((UserData)userData).setReceipt(a).build();
                        }
                        return new PurchaseResponseBuilder().setRequestId((RequestId)failed).setRequestStatus((PurchaseResponse.RequestStatus)requestStatus).setUserData((UserData)userData).setReceipt(a).build();
                    }
                    catch (Exception failed) {}
                }
                catch (Exception ex) {
                    safeValue = failed;
                    failed = ex;
                }
                final RequestId requestId = (RequestId)failed;
                failed = fromString;
                fromString = (Intent)build;
                requestId2 = requestId;
            }
            catch (Exception requestId2) {
                safeValue = failed;
                final Intent intent = null;
                failed = fromString;
                fromString = intent;
            }
        }
        catch (Exception requestId2) {
            safeValue = failed;
            failed = (fromString = null);
        }
        Log.e(c.a, "Error parsing purchase output", (Throwable)requestId2);
        Object requestStatus = safeValue;
        Object userData = fromString;
        Receipt a = receipt;
        return new PurchaseResponseBuilder().setRequestId((RequestId)failed).setRequestStatus((PurchaseResponse.RequestStatus)requestStatus).setUserData((UserData)userData).setReceipt(a).build();
    }
    
    @Override
    public void a(final Context context, final Intent intent) {
        e.a(c.a, "handleResponse");
        intent.setComponent(new ComponentName("com.amazon.sdktestclient", "com.amazon.sdktestclient.command.CommandBroker"));
        try {
            final String string = intent.getExtras().getString("responseType");
            if (string.equalsIgnoreCase("com.amazon.testclient.iap.purchase")) {
                this.g(intent);
                return;
            }
            if (string.equalsIgnoreCase("com.amazon.testclient.iap.appUserId")) {
                this.e(intent);
                return;
            }
            if (string.equalsIgnoreCase("com.amazon.testclient.iap.itemData")) {
                this.c(intent);
                return;
            }
            if (string.equalsIgnoreCase("com.amazon.testclient.iap.purchaseUpdates")) {
                this.a(intent);
            }
        }
        catch (Exception ex) {
            Log.e(c.a, "Error handling response.", (Throwable)ex);
        }
    }
    
    @Override
    public void a(final RequestId requestId) {
        e.a(c.a, "sendGetUserDataRequest");
        this.a(requestId.toString(), false, false);
    }
    
    @Override
    public void a(final RequestId requestId, final String s) {
        e.a(c.a, "sendPurchaseRequest");
        try {
            final Context b = d.d().b();
            final Bundle bundle = new Bundle();
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("sku", (Object)s);
            jsonObject.put("requestId", (Object)requestId.toString());
            jsonObject.put("packageName", (Object)b.getPackageName());
            jsonObject.put("sdkVersion", (Object)"2.0.61.0");
            bundle.putString("purchaseInput", jsonObject.toString());
            final Intent a = this.a("com.amazon.testclient.iap.purchase");
            a.addFlags(268435456);
            a.putExtras(bundle);
            b.startService(a);
        }
        catch (JSONException ex) {
            e.b(c.a, "Error in sendPurchaseRequest.");
        }
    }
    
    @Override
    public void a(final RequestId requestId, final String s, final FulfillmentResult fulfillmentResult) {
        e.a(c.a, "sendNotifyPurchaseFulfilled");
        try {
            final Context b = d.d().b();
            final Bundle bundle = new Bundle();
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("requestId", (Object)requestId.toString());
            jsonObject.put("packageName", (Object)b.getPackageName());
            jsonObject.put("receiptId", (Object)s);
            jsonObject.put("fulfillmentResult", (Object)fulfillmentResult);
            jsonObject.put("sdkVersion", (Object)"2.0.61.0");
            bundle.putString("purchaseFulfilledInput", jsonObject.toString());
            final Intent a = this.a("com.amazon.testclient.iap.purchaseFulfilled");
            a.addFlags(268435456);
            a.putExtras(bundle);
            b.startService(a);
        }
        catch (JSONException ex) {
            e.b(c.a, "Error in sendNotifyPurchaseFulfilled.");
        }
    }
    
    @Override
    public void a(final RequestId requestId, final Set<String> set) {
        e.a(c.a, "sendItemDataRequest");
        try {
            final Context b = d.d().b();
            final Bundle bundle = new Bundle();
            final JSONObject jsonObject = new JSONObject();
            final JSONArray jsonArray = new JSONArray((Collection)set);
            jsonObject.put("requestId", (Object)requestId.toString());
            jsonObject.put("packageName", (Object)b.getPackageName());
            jsonObject.put("skus", (Object)jsonArray);
            jsonObject.put("sdkVersion", (Object)"2.0.61.0");
            bundle.putString("itemDataInput", jsonObject.toString());
            final Intent a = this.a("com.amazon.testclient.iap.itemData");
            a.addFlags(268435456);
            a.putExtras(bundle);
            b.startService(a);
        }
        catch (JSONException ex) {
            e.b(c.a, "Error in sendItemDataRequest.");
        }
    }
    
    @Override
    public void a(final RequestId requestId, final boolean b) {
        RequestId requestId2 = requestId;
        if (requestId == null) {
            requestId2 = new RequestId();
        }
        final String a = c.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("sendPurchaseUpdatesRequest/sendGetUserData first:");
        sb.append(requestId2);
        e.a(a, sb.toString());
        this.a(requestId2.toString(), true, b);
    }
    
    protected void a(final Object o) {
        com.amazon.device.iap.internal.util.d.a(o, "response");
        final Context b = d.d().b();
        final PurchasingListener a = d.d().a();
        if (b != null && a != null) {
            new Handler(b.getMainLooper()).post((Runnable)new Runnable() {
                @Override
                public void run() {
                    try {
                        if (o instanceof ProductDataResponse) {
                            a.onProductDataResponse((ProductDataResponse)o);
                            return;
                        }
                        if (o instanceof UserDataResponse) {
                            a.onUserDataResponse((UserDataResponse)o);
                            return;
                        }
                        if (o instanceof PurchaseUpdatesResponse) {
                            a.onPurchaseUpdatesResponse((PurchaseUpdatesResponse)o);
                            return;
                        }
                        if (o instanceof PurchaseResponse) {
                            a.onPurchaseResponse((PurchaseResponse)o);
                            return;
                        }
                        final String a = com.amazon.device.iap.internal.a.c.a;
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Unknown response type:");
                        sb.append(o.getClass().getName());
                        e.b(a, sb.toString());
                    }
                    catch (Exception ex) {
                        final String a2 = com.amazon.device.iap.internal.a.c.a;
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("Error in sendResponse: ");
                        sb2.append(ex);
                        e.b(a2, sb2.toString());
                    }
                }
            });
            return;
        }
        final String a2 = c.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("PurchasingListener is not set. Dropping response: ");
        sb.append(o);
        e.a(a2, sb.toString());
    }
}
