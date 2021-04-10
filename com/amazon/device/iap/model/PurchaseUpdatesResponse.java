package com.amazon.device.iap.model;

import com.amazon.device.iap.internal.model.*;
import com.amazon.device.iap.internal.util.*;
import org.json.*;
import java.util.*;

public final class PurchaseUpdatesResponse
{
    private static final String HAS_MORE = "HAS_MORE";
    private static final String RECEIPTS = "RECEIPTS";
    private static final String REQUEST_ID = "REQUEST_ID";
    private static final String REQUEST_STATUS = "REQUEST_STATUS";
    private static final String TO_STRING_FORMAT = "(%s, requestId: \"%s\", requestStatus: \"%s\", userData: \"%s\", receipts: %s, hasMore: \"%b\")";
    private static final String USER_DATA = "USER_DATA";
    private final boolean hasMore;
    private final List<Receipt> receipts;
    private final RequestId requestId;
    private final RequestStatus requestStatus;
    private final UserData userData;
    
    public PurchaseUpdatesResponse(final PurchaseUpdatesResponseBuilder purchaseUpdatesResponseBuilder) {
        d.a(purchaseUpdatesResponseBuilder.getRequestId(), "requestId");
        d.a(purchaseUpdatesResponseBuilder.getRequestStatus(), "requestStatus");
        if (RequestStatus.SUCCESSFUL == purchaseUpdatesResponseBuilder.getRequestStatus()) {
            d.a(purchaseUpdatesResponseBuilder.getUserData(), "userData");
            d.a((Object)purchaseUpdatesResponseBuilder.getReceipts(), "receipts");
        }
        this.requestId = purchaseUpdatesResponseBuilder.getRequestId();
        this.requestStatus = purchaseUpdatesResponseBuilder.getRequestStatus();
        this.userData = purchaseUpdatesResponseBuilder.getUserData();
        List<Receipt> receipts;
        if (purchaseUpdatesResponseBuilder.getReceipts() == null) {
            receipts = new ArrayList<Receipt>();
        }
        else {
            receipts = purchaseUpdatesResponseBuilder.getReceipts();
        }
        this.receipts = receipts;
        this.hasMore = purchaseUpdatesResponseBuilder.hasMore();
    }
    
    public List<Receipt> getReceipts() {
        return this.receipts;
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
    
    public boolean hasMore() {
        return this.hasMore;
    }
    
    public JSONObject toJSON() throws JSONException {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("REQUEST_ID", (Object)this.requestId);
        jsonObject.put("REQUEST_STATUS", (Object)this.requestStatus);
        final UserData userData = this.userData;
        Object json;
        if (userData != null) {
            json = userData.toJSON();
        }
        else {
            json = "";
        }
        jsonObject.put("USER_DATA", json);
        final JSONArray jsonArray = new JSONArray();
        final List<Receipt> receipts = this.receipts;
        if (receipts != null) {
            final Iterator<Receipt> iterator = receipts.iterator();
            while (iterator.hasNext()) {
                jsonArray.put((Object)iterator.next().toJSON());
            }
        }
        jsonObject.put("RECEIPTS", (Object)jsonArray);
        jsonObject.put("HAS_MORE", this.hasMore);
        return jsonObject;
    }
    
    @Override
    public String toString() {
        final String string = super.toString();
        final RequestId requestId = this.requestId;
        final RequestStatus requestStatus = this.requestStatus;
        final UserData userData = this.userData;
        final List<Receipt> receipts = this.receipts;
        String string2;
        if (receipts != null) {
            string2 = Arrays.toString(receipts.toArray());
        }
        else {
            string2 = "null";
        }
        return String.format("(%s, requestId: \"%s\", requestStatus: \"%s\", userData: \"%s\", receipts: %s, hasMore: \"%b\")", string, requestId, requestStatus, userData, string2, this.hasMore);
    }
    
    public enum RequestStatus
    {
        FAILED, 
        NOT_SUPPORTED, 
        SUCCESSFUL;
    }
}
