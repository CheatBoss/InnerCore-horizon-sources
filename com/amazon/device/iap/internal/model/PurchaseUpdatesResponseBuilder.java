package com.amazon.device.iap.internal.model;

import java.util.*;
import com.amazon.device.iap.model.*;

public class PurchaseUpdatesResponseBuilder
{
    private boolean hasMore;
    private List<Receipt> receipts;
    private RequestId requestId;
    private PurchaseUpdatesResponse.RequestStatus requestStatus;
    private UserData userData;
    
    public PurchaseUpdatesResponse build() {
        return new PurchaseUpdatesResponse(this);
    }
    
    public List<Receipt> getReceipts() {
        return this.receipts;
    }
    
    public RequestId getRequestId() {
        return this.requestId;
    }
    
    public PurchaseUpdatesResponse.RequestStatus getRequestStatus() {
        return this.requestStatus;
    }
    
    public UserData getUserData() {
        return this.userData;
    }
    
    public boolean hasMore() {
        return this.hasMore;
    }
    
    public PurchaseUpdatesResponseBuilder setHasMore(final boolean hasMore) {
        this.hasMore = hasMore;
        return this;
    }
    
    public PurchaseUpdatesResponseBuilder setReceipts(final List<Receipt> receipts) {
        this.receipts = receipts;
        return this;
    }
    
    public PurchaseUpdatesResponseBuilder setRequestId(final RequestId requestId) {
        this.requestId = requestId;
        return this;
    }
    
    public PurchaseUpdatesResponseBuilder setRequestStatus(final PurchaseUpdatesResponse.RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
        return this;
    }
    
    public PurchaseUpdatesResponseBuilder setUserData(final UserData userData) {
        this.userData = userData;
        return this;
    }
}
