package com.amazon.device.iap.internal.model;

import com.amazon.device.iap.model.*;

public class UserDataBuilder
{
    private String marketplace;
    private String userId;
    
    public UserData build() {
        return new UserData(this);
    }
    
    public String getMarketplace() {
        return this.marketplace;
    }
    
    public String getUserId() {
        return this.userId;
    }
    
    public UserDataBuilder setMarketplace(final String marketplace) {
        this.marketplace = marketplace;
        return this;
    }
    
    public UserDataBuilder setUserId(final String userId) {
        this.userId = userId;
        return this;
    }
}
