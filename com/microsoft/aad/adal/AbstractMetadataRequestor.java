package com.microsoft.aad.adal;

import java.util.*;
import com.google.gson.*;

abstract class AbstractMetadataRequestor<MetadataType, MetadataRequestOptions>
{
    private UUID mCorrelationId;
    private Gson mGson;
    private final IWebRequestHandler mWebrequestHandler;
    
    AbstractMetadataRequestor() {
        this.mWebrequestHandler = new WebRequestHandler();
    }
    
    public final UUID getCorrelationId() {
        return this.mCorrelationId;
    }
    
    IWebRequestHandler getWebrequestHandler() {
        return this.mWebrequestHandler;
    }
    
    abstract MetadataType parseMetadata(final HttpWebResponse p0) throws Exception;
    
    Gson parser() {
        synchronized (this) {
            if (this.mGson == null) {
                this.mGson = new Gson();
            }
            return this.mGson;
        }
    }
    
    abstract MetadataType requestMetadata(final MetadataRequestOptions p0) throws Exception;
    
    public final void setCorrelationId(final UUID mCorrelationId) {
        this.mCorrelationId = mCorrelationId;
    }
}
