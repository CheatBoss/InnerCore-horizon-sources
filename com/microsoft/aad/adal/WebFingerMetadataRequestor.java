package com.microsoft.aad.adal;

import java.net.*;
import com.google.gson.*;
import java.util.*;
import java.io.*;

class WebFingerMetadataRequestor extends AbstractMetadataRequestor<WebFingerMetadata, WebFingerMetadataRequestParameters>
{
    private static final String TAG;
    
    static {
        TAG = WebFingerMetadataRequestor.class.getSimpleName();
    }
    
    static URL buildWebFingerUrl(final URL url, final DRSMetadata drsMetadata) throws MalformedURLException {
        final URL url2 = new URL(drsMetadata.getIdentityProviderService().getPassiveAuthEndpoint());
        final StringBuilder sb = new StringBuilder("https://");
        sb.append(url2.getHost());
        sb.append("/.well-known/webfinger?resource=");
        sb.append(url.toString());
        final String string = sb.toString();
        final String tag = WebFingerMetadataRequestor.TAG;
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("WebFinger URL: ");
        sb2.append(string);
        Logger.i(tag, "Validator will use WebFinger URL. ", sb2.toString());
        return new URL(string);
    }
    
    @Override
    WebFingerMetadata parseMetadata(final HttpWebResponse httpWebResponse) throws AuthenticationException {
        Logger.v(WebFingerMetadataRequestor.TAG, "Parsing WebFinger response.");
        try {
            return (WebFingerMetadata)this.parser().fromJson(httpWebResponse.getBody(), (Class)WebFingerMetadata.class);
        }
        catch (JsonSyntaxException ex) {
            throw new AuthenticationException(ADALError.JSON_PARSE_ERROR);
        }
    }
    
    @Override
    WebFingerMetadata requestMetadata(final WebFingerMetadataRequestParameters webFingerMetadataRequestParameters) throws AuthenticationException {
        final URL domain = webFingerMetadataRequestParameters.getDomain();
        final DRSMetadata drsMetadata = webFingerMetadataRequestParameters.getDrsMetadata();
        final String tag = WebFingerMetadataRequestor.TAG;
        final StringBuilder sb = new StringBuilder();
        sb.append("Auth endpoint: ");
        sb.append(domain.toString());
        Logger.i(tag, "Validating authority for auth endpoint. ", sb.toString());
        try {
            final HttpWebResponse sendGet = this.getWebrequestHandler().sendGet(buildWebFingerUrl(domain, drsMetadata), new HashMap<String, String>());
            if (200 == sendGet.getStatusCode()) {
                return this.parseMetadata(sendGet);
            }
            throw new AuthenticationException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_INSTANCE);
        }
        catch (IOException ex) {
            throw new AuthenticationException(ADALError.IO_EXCEPTION, "Unexpected error", ex);
        }
    }
}
