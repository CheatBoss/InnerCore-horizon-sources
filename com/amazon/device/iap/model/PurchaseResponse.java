package com.amazon.device.iap.model;

import com.amazon.device.iap.internal.model.*;
import com.amazon.device.iap.internal.util.*;
import org.json.*;

public final class PurchaseResponse
{
    private static final String RECEIPT = "receipt";
    private static final String REQUEST_ID = "requestId";
    private static final String REQUEST_STATUS = "requestStatus";
    private static final String TO_STRING_FORMAT = "(%s, requestId: \"%s\", purchaseRequestStatus: \"%s\", userId: \"%s\", receipt: %s)";
    private static final String USER_DATA = "userData";
    private final Receipt receipt;
    private final RequestId requestId;
    private final RequestStatus requestStatus;
    private final UserData userData;
    
    public PurchaseResponse(final PurchaseResponseBuilder purchaseResponseBuilder) {
        d.a(purchaseResponseBuilder.getRequestId(), "requestId");
        d.a(purchaseResponseBuilder.getRequestStatus(), "requestStatus");
        if (purchaseResponseBuilder.getRequestStatus() == RequestStatus.SUCCESSFUL) {
            d.a(purchaseResponseBuilder.getReceipt(), "receipt");
            d.a(purchaseResponseBuilder.getUserData(), "userData");
        }
        this.requestId = purchaseResponseBuilder.getRequestId();
        this.userData = purchaseResponseBuilder.getUserData();
        this.receipt = purchaseResponseBuilder.getReceipt();
        this.requestStatus = purchaseResponseBuilder.getRequestStatus();
    }
    
    public Receipt getReceipt() {
        return this.receipt;
    }
    
    public RequestId getRequestId() {
        return this.requestId;
    }
    
    public RequestStatus getRequestStatus() {
        return this.requestStatus;
    }
    
    public UserData getUserData() {
        return this.userData;
    }
    
    public JSONObject toJSON() throws JSONException {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("requestId", (Object)this.requestId);
        jsonObject.put("requestStatus", (Object)this.requestStatus);
        final UserData userData = this.userData;
        final String s = "";
        Object json;
        if (userData != null) {
            json = userData.toJSON();
        }
        else {
            json = "";
        }
        jsonObject.put("userData", json);
        Object json2 = s;
        if (this.getReceipt() != null) {
            json2 = this.getReceipt().toJSON();
        }
        jsonObject.put("receipt", json2);
        return jsonObject;
    }
    
    @Override
    public String toString() {
        final String string = super.toString();
        final RequestId requestId = this.requestId;
        final RequestStatus requestStatus = this.requestStatus;
        String string2;
        if (requestStatus != null) {
            string2 = requestStatus.toString();
        }
        else {
            string2 = "null";
        }
        return String.format("(%s, requestId: \"%s\", purchaseRequestStatus: \"%s\", userId: \"%s\", receipt: %s)", string, requestId, string2, this.userData, this.receipt);
    }
    
    public enum RequestStatus
    {
        ALREADY_PURCHASED, 
        FAILED, 
        INVALID_SKU, 
        NOT_SUPPORTED, 
        SUCCESSFUL;
        
        public static RequestStatus safeValueOf(final String s) {
            if (d.a(s)) {
                return null;
            }
            if ("ALREADY_ENTITLED".equalsIgnoreCase(s)) {
                return RequestStatus.ALREADY_PURCHASED;
            }
            return valueOf(s.toUpperCase());
        }
    }
}
