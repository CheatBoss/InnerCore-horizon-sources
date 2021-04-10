package com.microsoft.aad.adal;

import com.google.gson.annotations.*;

final class DRSMetadata
{
    @SerializedName("IdentityProviderService")
    private IdentityProviderService mIdentityProviderService;
    
    IdentityProviderService getIdentityProviderService() {
        return this.mIdentityProviderService;
    }
    
    void setIdentityProviderService(final IdentityProviderService mIdentityProviderService) {
        this.mIdentityProviderService = mIdentityProviderService;
    }
}
