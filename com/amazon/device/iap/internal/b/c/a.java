package com.amazon.device.iap.internal.b.c;

import com.amazon.device.iap.internal.b.*;
import java.math.*;
import org.json.*;
import com.amazon.venezia.command.*;
import com.amazon.device.iap.internal.util.*;
import com.amazon.device.iap.internal.model.*;
import com.amazon.device.iap.model.*;
import java.util.*;
import android.os.*;
import com.amazon.android.framework.exception.*;

public final class a extends c
{
    private static final String b;
    
    static {
        b = a.class.getSimpleName();
    }
    
    public a(final e e, final Set<String> set) {
        super(e, "2.0", set);
    }
    
    private Product a(final String sku, final Map map) throws IllegalArgumentException {
        final String s = map.get(sku);
        try {
            final JSONObject jsonObject = new JSONObject(s);
            final ProductType value = ProductType.valueOf(jsonObject.getString("itemType").toUpperCase());
            final String string = jsonObject.getString("description");
            String price;
            final String s2 = price = jsonObject.optString("price", (String)null);
            if (com.amazon.device.iap.internal.util.d.a(s2)) {
                final JSONObject optJSONObject = jsonObject.optJSONObject("priceJson");
                price = s2;
                if (optJSONObject != null) {
                    final Currency instance = Currency.getInstance(optJSONObject.getString("currency"));
                    final BigDecimal bigDecimal = new BigDecimal(optJSONObject.getString("value"));
                    final StringBuilder sb = new StringBuilder();
                    sb.append(instance.getSymbol());
                    sb.append(bigDecimal);
                    price = sb.toString();
                }
            }
            return new ProductBuilder().setSku(sku).setProductType(value).setDescription(string).setPrice(price).setSmallIconUrl(jsonObject.getString("iconUrl")).setTitle(jsonObject.getString("title")).build();
        }
        catch (JSONException ex) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("error in parsing json string");
            sb2.append(s);
            throw new IllegalArgumentException(sb2.toString());
        }
    }
    
    @Override
    protected boolean a(SuccessResult data) throws RemoteException, KiwiException {
        data = (SuccessResult)data.getData();
        final String b = a.b;
        final StringBuilder sb = new StringBuilder();
        sb.append("data: ");
        sb.append(data);
        com.amazon.device.iap.internal.util.e.a(b, sb.toString());
        final LinkedHashSet<String> unavailableSkus = new LinkedHashSet<String>();
        final HashMap<String, Product> productData = new HashMap<String, Product>();
        for (final String s : this.a) {
            if (!((Map)data).containsKey(s)) {
                unavailableSkus.add(s);
            }
            else {
                try {
                    productData.put(s, this.a(s, (Map)data));
                }
                catch (IllegalArgumentException ex) {
                    unavailableSkus.add(s);
                    final String s2 = ((Map<K, String>)data).get(s);
                    final String c = this.c();
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(a.b);
                    sb2.append(".onResult()");
                    MetricsHelper.submitJsonParsingExceptionMetrics(c, s2, sb2.toString());
                    final String b2 = a.b;
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("Error parsing JSON for SKU ");
                    sb3.append(s);
                    sb3.append(": ");
                    sb3.append(ex.getMessage());
                    com.amazon.device.iap.internal.util.e.b(b2, sb3.toString());
                }
            }
        }
        final e b3 = this.b();
        b3.d().a(new ProductDataResponseBuilder().setRequestId(b3.c()).setRequestStatus(ProductDataResponse.RequestStatus.SUCCESSFUL).setUnavailableSkus(unavailableSkus).setProductData(productData).build());
        return true;
    }
}
