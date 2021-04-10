package com.microsoft.aad.adal;

import java.net.*;
import java.io.*;
import java.util.*;
import android.content.*;
import android.os.*;

class HttpWebRequest
{
    private static final int CONNECT_TIME_OUT;
    private static final int DEBUG_SIMULATE_DELAY = 0;
    private static final int READ_TIME_OUT;
    static final String REQUEST_METHOD_GET = "GET";
    static final String REQUEST_METHOD_POST = "POST";
    private static final String TAG = "HttpWebRequest";
    private final byte[] mRequestContent;
    private final String mRequestContentType;
    private final Map<String, String> mRequestHeaders;
    private final String mRequestMethod;
    private final URL mUrl;
    
    static {
        CONNECT_TIME_OUT = AuthenticationSettings.INSTANCE.getConnectTimeOut();
        READ_TIME_OUT = AuthenticationSettings.INSTANCE.getReadTimeOut();
    }
    
    public HttpWebRequest(final URL url, final String s, final Map<String, String> map) {
        this(url, s, map, null, null);
    }
    
    public HttpWebRequest(final URL mUrl, final String mRequestMethod, final Map<String, String> map, final byte[] mRequestContent, final String mRequestContentType) {
        this.mUrl = mUrl;
        this.mRequestMethod = mRequestMethod;
        final HashMap<String, String> mRequestHeaders = new HashMap<String, String>();
        this.mRequestHeaders = mRequestHeaders;
        final URL mUrl2 = this.mUrl;
        if (mUrl2 != null) {
            mRequestHeaders.put("Host", mUrl2.getAuthority());
        }
        this.mRequestHeaders.putAll(map);
        this.mRequestContent = mRequestContent;
        this.mRequestContentType = mRequestContentType;
    }
    
    private static String convertStreamToString(final InputStream inputStream) throws IOException {
        BufferedReader bufferedReader2;
        try {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            try {
                final StringBuilder sb = new StringBuilder();
                while (true) {
                    final String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                    if (sb.length() > 0) {
                        sb.append('\n');
                    }
                    sb.append(line);
                }
                final String string = sb.toString();
                bufferedReader.close();
                return string;
            }
            finally {}
        }
        finally {
            bufferedReader2 = null;
        }
        if (bufferedReader2 != null) {
            bufferedReader2.close();
        }
    }
    
    private static void safeCloseStream(final Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            }
            catch (IOException ex) {
                Logger.e("HttpWebRequest", "Failed to close the stream. ", "", ADALError.IO_EXCEPTION, ex);
            }
        }
    }
    
    private static void setRequestBody(final HttpURLConnection httpURLConnection, final byte[] array, final String s) throws IOException {
        if (array == null) {
            return;
        }
        httpURLConnection.setDoOutput(true);
        if (s != null && !s.isEmpty()) {
            httpURLConnection.setRequestProperty("Content-Type", s);
        }
        httpURLConnection.setRequestProperty("Content-Length", Integer.toString(array.length));
        httpURLConnection.setFixedLengthStreamingMode(array.length);
        Closeable closeable;
        try {
            final OutputStream outputStream = httpURLConnection.getOutputStream();
            try {
                outputStream.write(array);
                safeCloseStream(outputStream);
                return;
            }
            finally {}
        }
        finally {
            closeable = null;
        }
        safeCloseStream(closeable);
    }
    
    private HttpURLConnection setupConnection() throws IOException {
        final StringBuilder sb = new StringBuilder();
        sb.append("Thread:");
        sb.append(Process.myTid());
        Logger.v("HttpWebRequest:setupConnection", "HttpWebRequest setupConnection.", sb.toString(), null);
        final URL mUrl = this.mUrl;
        if (mUrl == null) {
            throw new IllegalArgumentException("requestURL");
        }
        if (!mUrl.getProtocol().equalsIgnoreCase("http") && !this.mUrl.getProtocol().equalsIgnoreCase("https")) {
            throw new IllegalArgumentException("requestURL");
        }
        HttpURLConnection.setFollowRedirects(true);
        final HttpURLConnection httpUrlConnection = HttpUrlConnectionFactory.createHttpUrlConnection(this.mUrl);
        httpUrlConnection.setConnectTimeout(HttpWebRequest.CONNECT_TIME_OUT);
        if (Build$VERSION.SDK_INT > 13) {
            httpUrlConnection.setRequestProperty("Connection", "close");
        }
        for (final Map.Entry<String, String> entry : this.mRequestHeaders.entrySet()) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Header: ");
            sb2.append(entry.getKey());
            Logger.v("HttpWebRequest:setupConnection", "Setting header. ", sb2.toString(), null);
            httpUrlConnection.setRequestProperty(entry.getKey(), entry.getValue());
        }
        httpUrlConnection.setReadTimeout(HttpWebRequest.READ_TIME_OUT);
        httpUrlConnection.setInstanceFollowRedirects(true);
        httpUrlConnection.setUseCaches(false);
        httpUrlConnection.setRequestMethod(this.mRequestMethod);
        httpUrlConnection.setDoInput(true);
        setRequestBody(httpUrlConnection, this.mRequestContent, this.mRequestContentType);
        return httpUrlConnection;
    }
    
    static void throwIfNetworkNotAvailable(final Context context) throws AuthenticationException {
        final DefaultConnectionService defaultConnectionService = new DefaultConnectionService(context);
        if (defaultConnectionService.isConnectionAvailable()) {
            return;
        }
        if (defaultConnectionService.isNetworkDisabledFromOptimizations()) {
            final ADALError no_NETWORK_CONNECTION_POWER_OPTIMIZATION = ADALError.NO_NETWORK_CONNECTION_POWER_OPTIMIZATION;
            final StringBuilder sb = new StringBuilder();
            sb.append("Connection is not available to refresh token because power optimization is enabled. And the device is in doze mode or the app is standby");
            sb.append(ADALError.NO_NETWORK_CONNECTION_POWER_OPTIMIZATION.getDescription());
            final AuthenticationException ex = new AuthenticationException(no_NETWORK_CONNECTION_POWER_OPTIMIZATION, sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Connection is not available to refresh token because power optimization is enabled. And the device is in doze mode or the app is standby");
            sb2.append(ADALError.NO_NETWORK_CONNECTION_POWER_OPTIMIZATION.getDescription());
            Logger.w("HttpWebRequest", sb2.toString(), "", ADALError.NO_NETWORK_CONNECTION_POWER_OPTIMIZATION);
            throw ex;
        }
        final AuthenticationException ex2 = new AuthenticationException(ADALError.DEVICE_CONNECTION_IS_NOT_AVAILABLE, "Connection is not available to refresh token");
        Logger.w("HttpWebRequest", "Connection is not available to refresh token", "", ADALError.DEVICE_CONNECTION_IS_NOT_AVAILABLE);
        throw ex2;
    }
    
    public HttpWebResponse send() throws IOException {
        final StringBuilder sb = new StringBuilder();
        sb.append(" Thread: ");
        sb.append(Process.myTid());
        final String string = sb.toString();
        final Closeable closeable = null;
        Logger.v("HttpWebRequest:send", "HttpWebRequest send. ", string, null);
        final HttpURLConnection setupConnection = this.setupConnection();
        Closeable closeable2 = closeable;
        try {
            try {
                setupConnection.getInputStream();
            }
            finally {}
        }
        catch (IOException ex) {
            Logger.e("HttpWebRequest:send", "IOException is thrown when sending the request. ", ex.getMessage(), ADALError.SERVER_ERROR);
            final InputStream errorStream = setupConnection.getErrorStream();
            if (errorStream != null) {
                final int responseCode = setupConnection.getResponseCode();
                final String convertStreamToString = convertStreamToString(errorStream);
                Debug.isDebuggerConnected();
                Logger.v("HttpWebRequest:send", "Response is received.");
                final HttpWebResponse httpWebResponse = new HttpWebResponse(responseCode, convertStreamToString, setupConnection.getHeaderFields());
                safeCloseStream(errorStream);
                return httpWebResponse;
            }
            try {
                throw ex;
            }
            finally {
                closeable2 = errorStream;
            }
        }
        safeCloseStream(closeable2);
    }
}
