package com.amazon.device.iap.internal.model;

import com.amazon.device.iap.model.*;
import java.util.*;

public class ProductDataResponseBuilder
{
    private Map<String, Product> productData;
    private RequestId requestId;
    private ProductDataResponse.RequestStatus requestStatus;
    private Set<String> unavailableSkus;
    
    public ProductDataResponse build() {
        return new ProductDataResponse(this);
    }
    
    public Map<String, Product> getProductData() {
        return this.productData;
    }
    
    public RequestId getRequestId() {
        return this.requestId;
    }
    
    public ProductDataResponse.RequestStatus getRequestStatus() {
        return this.requestStatus;
    }
    
    public Set<String> getUnavailableSkus() {
        return this.unavailableSkus;
    }
    
    public ProductDataResponseBuilder setProductData(final Map<String, Product> productData) {
        this.productData = productData;
        return this;
    }
    
    public ProductDataResponseBuilder setRequestId(final RequestId requestId) {
        this.requestId = requestId;
        return this;
    }
    
    public ProductDataResponseBuilder setRequestStatus(final ProductDataResponse.RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
        return this;
    }
    
    public ProductDataResponseBuilder setUnavailableSkus(final Set<String> unavailableSkus) {
        this.unavailableSkus = unavailableSkus;
        return this;
    }
}
