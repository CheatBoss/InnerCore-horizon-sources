package com.microsoft.aad.adal;

import java.util.*;
import java.io.*;
import java.net.*;
import com.google.gson.*;

final class DRSMetadataRequestor extends AbstractMetadataRequestor<DRSMetadata, String>
{
    private static final String CLOUD_RESOLVER_DOMAIN = "windows.net/";
    private static final String DRS_URL_PREFIX = "https://enterpriseregistration.";
    private static final String TAG;
    
    static {
        TAG = DRSMetadataRequestor.class.getSimpleName();
    }
    
    private DRSMetadata requestCloud(final String s) throws AuthenticationException {
        Logger.v(DRSMetadataRequestor.TAG, "Requesting DRS discovery (cloud)");
        try {
            return this.requestDrsDiscoveryInternal(Type.CLOUD, s);
        }
        catch (UnknownHostException ex) {
            throw new AuthenticationException(ADALError.DRS_DISCOVERY_FAILED_UNKNOWN_HOST);
        }
    }
    
    private DRSMetadata requestDrsDiscoveryInternal(final Type type, final String s) throws AuthenticationException, UnknownHostException {
        try {
            final URL url = new URL(this.buildRequestUrlByType(type, s));
            final HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("Accept", "application/json");
            if (this.getCorrelationId() != null) {
                hashMap.put("client-request-id", this.getCorrelationId().toString());
            }
            try {
                final HttpWebResponse sendGet = this.getWebrequestHandler().sendGet(url, hashMap);
                final int statusCode = sendGet.getStatusCode();
                if (200 == statusCode) {
                    return this.parseMetadata(sendGet);
                }
                final ADALError drs_FAILED_SERVER_ERROR = ADALError.DRS_FAILED_SERVER_ERROR;
                final StringBuilder sb = new StringBuilder();
                sb.append("Unexpected error code: [");
                sb.append(statusCode);
                sb.append("]");
                throw new AuthenticationException(drs_FAILED_SERVER_ERROR, sb.toString());
            }
            catch (IOException ex2) {
                throw new AuthenticationException(ADALError.IO_EXCEPTION);
            }
            catch (UnknownHostException ex) {
                throw ex;
            }
        }
        catch (MalformedURLException ex3) {
            throw new AuthenticationException(ADALError.DRS_METADATA_URL_INVALID);
        }
    }
    
    private DRSMetadata requestOnPrem(final String s) throws UnknownHostException, AuthenticationException {
        Logger.v(DRSMetadataRequestor.TAG, "Requesting DRS discovery (on-prem)");
        return this.requestDrsDiscoveryInternal(Type.ON_PREM, s);
    }
    
    String buildRequestUrlByType(final Type type, String tag) {
        final StringBuilder sb = new StringBuilder("https://enterpriseregistration.");
        Label_0043: {
            if (Type.CLOUD == type) {
                sb.append("windows.net/");
            }
            else if (Type.ON_PREM != type) {
                break Label_0043;
            }
            sb.append(tag);
        }
        sb.append("/enrollmentserver/contract?api-version=1.0");
        final String string = sb.toString();
        tag = DRSMetadataRequestor.TAG;
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("URL: ");
        sb2.append(string);
        Logger.v(tag, "Request will use DRS url. ", sb2.toString(), null);
        return string;
    }
    
    @Override
    DRSMetadata parseMetadata(final HttpWebResponse httpWebResponse) throws AuthenticationException {
        Logger.v(DRSMetadataRequestor.TAG, "Parsing DRS metadata response");
        try {
            return (DRSMetadata)this.parser().fromJson(httpWebResponse.getBody(), (Class)DRSMetadata.class);
        }
        catch (JsonSyntaxException ex) {
            throw new AuthenticationException(ADALError.JSON_PARSE_ERROR);
        }
    }
    
    @Override
    DRSMetadata requestMetadata(final String s) throws AuthenticationException {
        try {
            return this.requestOnPrem(s);
        }
        catch (UnknownHostException ex) {
            return this.requestCloud(s);
        }
    }
    
    enum Type
    {
        CLOUD, 
        ON_PREM;
    }
}
