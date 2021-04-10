package com.microsoft.aad.adal;

import java.net.*;

final class WebFingerMetadataRequestParameters
{
    private final URL mDomain;
    private final DRSMetadata mMetadata;
    
    WebFingerMetadataRequestParameters(final URL mDomain, final DRSMetadata mMetadata) {
        this.mDomain = mDomain;
        this.mMetadata = mMetadata;
    }
    
    URL getDomain() {
        return this.mDomain;
    }
    
    DRSMetadata getDrsMetadata() {
        return this.mMetadata;
    }
}
