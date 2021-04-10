package com.amazon.device.iap.internal.b.c;

import com.amazon.device.iap.internal.b.*;
import org.json.*;
import com.amazon.venezia.command.*;
import com.amazon.device.iap.internal.model.*;
import com.amazon.device.iap.model.*;
import java.util.*;
import android.os.*;
import com.amazon.android.framework.exception.*;

public final class b extends c
{
    private static final String b;
    
    static {
        b = b.class.getSimpleName();
    }
    
    public b(final e e, final Set<String> set) {
        super(e, "1.0", set);
    }
    
    private Product a(final String sku, Map s) throws IllegalArgumentException {
        s = ((Map<K, String>)s).get(sku);
        try {
            final JSONObject jsonObject = new JSONObject(s);
            return new ProductBuilder().setSku(sku).setProductType(ProductType.valueOf(jsonObject.getString("itemType").toUpperCase())).setDescription(jsonObject.getString("description")).setPrice(jsonObject.optString("price")).setSmallIconUrl(jsonObject.getString("iconUrl")).setTitle(jsonObject.getString("title")).build();
        }
        catch (JSONException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("error in parsing json string");
            sb.append(s);
            throw new IllegalArgumentException(sb.toString());
        }
    }
    
    @Override
    protected boolean a(SuccessResult data) throws RemoteException, KiwiException {
        data = (SuccessResult)data.getData();
        final String b = com.amazon.device.iap.internal.b.c.b.b;
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
                    final String b2 = com.amazon.device.iap.internal.b.c.b.b;
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Error parsing JSON for SKU ");
                    sb2.append(s);
                    sb2.append(": ");
                    sb2.append(ex.getMessage());
                    com.amazon.device.iap.internal.util.e.b(b2, sb2.toString());
                }
            }
        }
        final e b3 = this.b();
        b3.d().a(new ProductDataResponseBuilder().setRequestId(b3.c()).setRequestStatus(ProductDataResponse.RequestStatus.SUCCESSFUL).setUnavailableSkus(unavailableSkus).setProductData(productData).build());
        return true;
    }
}
