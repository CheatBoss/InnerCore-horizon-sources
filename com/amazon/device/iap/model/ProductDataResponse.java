package com.amazon.device.iap.model;

import com.amazon.device.iap.internal.model.*;
import com.amazon.device.iap.internal.util.*;
import java.util.*;
import org.json.*;

public class ProductDataResponse
{
    private static final String PRODUCT_DATA = "productData";
    private static final String REQUEST_ID = "requestId";
    private static final String REQUEST_STATUS = "requestStatus";
    private static final String TO_STRING_FORMAT = "(%s, requestId: \"%s\", unavailableSkus: %s, requestStatus: \"%s\", productData: %s)";
    private static final String UNAVAILABLE_SKUS = "UNAVAILABLE_SKUS";
    private final Map<String, Product> productData;
    private final RequestId requestId;
    private final RequestStatus requestStatus;
    private final Set<String> unavailableSkus;
    
    public ProductDataResponse(final ProductDataResponseBuilder productDataResponseBuilder) {
        d.a(productDataResponseBuilder.getRequestId(), "requestId");
        d.a(productDataResponseBuilder.getRequestStatus(), "requestStatus");
        if (productDataResponseBuilder.getUnavailableSkus() == null) {
            productDataResponseBuilder.setUnavailableSkus(new HashSet<String>());
        }
        if (RequestStatus.SUCCESSFUL == productDataResponseBuilder.getRequestStatus()) {
            d.a(productDataResponseBuilder.getProductData(), "productData");
        }
        this.requestId = productDataResponseBuilder.getRequestId();
        this.requestStatus = productDataResponseBuilder.getRequestStatus();
        this.unavailableSkus = productDataResponseBuilder.getUnavailableSkus();
        this.productData = productDataResponseBuilder.getProductData();
    }
    
    public Map<String, Product> getProductData() {
        return this.productData;
    }
    
    public RequestId getRequestId() {
        return this.requestId;
    }
    
    public RequestStatus getRequestStatus() {
        return this.requestStatus;
    }
    
    public Set<String> getUnavailableSkus() {
        return this.unavailableSkus;
    }
    
    public JSONObject toJSON() throws JSONException {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("requestId", (Object)this.requestId);
        jsonObject.put("UNAVAILABLE_SKUS", (Object)this.unavailableSkus);
        jsonObject.put("requestStatus", (Object)this.requestStatus);
        final JSONObject jsonObject2 = new JSONObject();
        final Map<String, Product> productData = this.productData;
        if (productData != null) {
            for (final String s : productData.keySet()) {
                jsonObject2.put(s, (Object)this.productData.get(s).toJSON());
            }
        }
        jsonObject.put("productData", (Object)jsonObject2);
        return jsonObject;
    }
    
    @Override
    public String toString() {
        final String string = super.toString();
        final RequestId requestId = this.requestId;
        final Set<String> unavailableSkus = this.unavailableSkus;
        String string2 = "null";
        String string3;
        if (unavailableSkus != null) {
            string3 = unavailableSkus.toString();
        }
        else {
            string3 = "null";
        }
        final RequestStatus requestStatus = this.requestStatus;
        String string4;
        if (requestStatus != null) {
            string4 = requestStatus.toString();
        }
        else {
            string4 = "null";
        }
        final Map<String, Product> productData = this.productData;
        if (productData != null) {
            string2 = productData.toString();
        }
        return String.format("(%s, requestId: \"%s\", unavailableSkus: %s, requestStatus: \"%s\", productData: %s)", string, requestId, string3, string4, string2);
    }
    
    public enum RequestStatus
    {
        FAILED, 
        NOT_SUPPORTED, 
        SUCCESSFUL;
    }
}
