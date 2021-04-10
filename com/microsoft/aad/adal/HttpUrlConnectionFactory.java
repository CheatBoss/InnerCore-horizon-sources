package com.microsoft.aad.adal;

import java.net.*;
import java.io.*;

final class HttpUrlConnectionFactory
{
    private static HttpURLConnection sMockedConnection;
    private static URL sMockedConnectionOpenUrl;
    
    private HttpUrlConnectionFactory() {
    }
    
    static HttpURLConnection createHttpUrlConnection(final URL sMockedConnectionOpenUrl) throws IOException {
        final HttpURLConnection sMockedConnection = HttpUrlConnectionFactory.sMockedConnection;
        if (sMockedConnection != null) {
            HttpUrlConnectionFactory.sMockedConnectionOpenUrl = sMockedConnectionOpenUrl;
            return sMockedConnection;
        }
        return (HttpURLConnection)sMockedConnectionOpenUrl.openConnection();
    }
    
    static URL getMockedConnectionOpenUrl() {
        return HttpUrlConnectionFactory.sMockedConnectionOpenUrl;
    }
    
    static void setMockedHttpUrlConnection(final HttpURLConnection sMockedConnection) {
        HttpUrlConnectionFactory.sMockedConnection = sMockedConnection;
        if (sMockedConnection == null) {
            HttpUrlConnectionFactory.sMockedConnectionOpenUrl = null;
        }
    }
}
