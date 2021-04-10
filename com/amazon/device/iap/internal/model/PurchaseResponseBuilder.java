package com.amazon.device.iap.internal.model;

import com.amazon.device.iap.model.*;

public class PurchaseResponseBuilder
{
    private Receipt receipt;
    private RequestId requestId;
    private PurchaseResponse.RequestStatus requestStatus;
    private UserData userData;
    
    public PurchaseResponse build() {
        return new PurchaseResponse(this);
    }
    
    public Receipt getReceipt() {
        return this.receipt;
    }
    
    public RequestId getRequestId() {
        return this.requestId;
    }
    
    public PurchaseResponse.RequestStatus getRequestStatus() {
        return this.requestStatus;
    }
    
    public UserData getUserData() {
        return this.userData;
    }
    
    public PurchaseResponseBuilder setReceipt(final Receipt receipt) {
        this.receipt = receipt;
        return this;
    }
    
    public PurchaseResponseBuilder setRequestId(final RequestId requestId) {
        this.requestId = requestId;
        return this;
    }
    
    public PurchaseResponseBuilder setRequestStatus(final PurchaseResponse.RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
        return this;
    }
    
    public PurchaseResponseBuilder setUserData(final UserData userData) {
        this.userData = userData;
        return this;
    }
}
