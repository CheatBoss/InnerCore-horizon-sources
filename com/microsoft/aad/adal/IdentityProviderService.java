package com.microsoft.aad.adal;

import com.google.gson.annotations.*;

final class IdentityProviderService
{
    @SerializedName("PassiveAuthEndpoint")
    private String mPassiveAuthEndpoint;
    
    String getPassiveAuthEndpoint() {
        return this.mPassiveAuthEndpoint;
    }
    
    void setPassiveAuthEndpoint(final String mPassiveAuthEndpoint) {
        this.mPassiveAuthEndpoint = mPassiveAuthEndpoint;
    }
}
