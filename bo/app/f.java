package bo.app;

import com.appboy.support.*;
import java.util.zip.*;
import java.net.*;
import org.json.*;
import java.io.*;
import java.util.*;

public final class f implements g
{
    private static final String a;
    private final int b;
    
    static {
        a = AppboyLogger.getAppboyLogTag(f.class);
    }
    
    public f(final int b) {
        this.b = b;
    }
    
    private InputStream a(final HttpURLConnection httpURLConnection) {
        httpURLConnection.connect();
        final int responseCode = httpURLConnection.getResponseCode();
        if (responseCode / 100 != 2) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Bad Http response code from Appboy: [");
            sb.append(responseCode);
            sb.append("]");
            throw new aw(sb.toString());
        }
        if ("gzip".equalsIgnoreCase(httpURLConnection.getContentEncoding())) {
            return new GZIPInputStream(httpURLConnection.getInputStream());
        }
        return new BufferedInputStream(httpURLConnection.getInputStream());
    }
    
    private String a(final BufferedReader bufferedReader) {
        final StringBuilder sb = new StringBuilder();
        while (true) {
            final String line = bufferedReader.readLine();
            if (line == null) {
                break;
            }
            sb.append(line);
        }
        return sb.toString();
    }
    
    private JSONObject a(URI a, JSONObject a2, final Map<String, String> map, final y y) {
        a = (URI)ea.a(a);
        if (a != null) {
            try {
                a2 = this.a((URL)a, a2, map, y);
                return a2;
            }
            catch (IOException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Experienced IOException during request to [");
                sb.append(((URL)a).toString());
                sb.append("], failing: [");
                sb.append(ex.getMessage());
                sb.append("]");
                throw new aw(sb.toString(), ex);
            }
        }
        return null;
    }
    
    private JSONObject a(final URL url, JSONObject jsonObject, final Map<String, String> map, y y) {
        Serializable s = null;
        Object o = null;
        Label_0229: {
            Label_0031: {
                if (url != null) {
                    try {
                        this.b(url, jsonObject, map, y);
                        break Label_0031;
                    }
                    finally {
                        jsonObject = null;
                        o = s;
                        break Label_0229;
                    }
                }
                jsonObject = null;
            }
            if (jsonObject == null) {
                o = null;
                if (jsonObject != null) {
                    ((HttpURLConnection)jsonObject).disconnect();
                }
                if (o != null) {
                    try {
                        ((InputStream)o).close();
                    }
                    catch (Exception ex) {
                        AppboyLogger.e(f.a, "Caught an error trying to close the inputStream in getJsonResultFromUrl", ex);
                    }
                }
                final String a = f.a;
                final StringBuilder sb = new StringBuilder();
                sb.append("Failed to get result from [");
                sb.append(url);
                sb.append("]. Returning null.");
                AppboyLogger.w(a, sb.toString());
                return null;
            }
            try {
                final InputStream a2 = this.a((HttpURLConnection)jsonObject);
                try {
                    final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(a2, "UTF-8"));
                    try {
                        y = (y)new JSONObject(this.a(bufferedReader));
                        if (jsonObject != null) {
                            ((HttpURLConnection)jsonObject).disconnect();
                        }
                        if (a2 != null) {
                            try {
                                a2.close();
                                return (JSONObject)y;
                            }
                            catch (Exception ex2) {
                                AppboyLogger.e(f.a, "Caught an error trying to close the inputStream in getJsonResultFromUrl", ex2);
                            }
                        }
                        return (JSONObject)y;
                    }
                    catch (JSONException ex3) {
                        final String s2 = f.a;
                        s = new StringBuilder();
                        ((StringBuilder)s).append("Unable to parse response [");
                        ((StringBuilder)s).append(ex3);
                        ((StringBuilder)s).append("]");
                        s = ((StringBuilder)s).toString();
                    }
                    catch (IOException ex3) {
                        final String s2 = f.a;
                        s = new StringBuilder();
                        ((StringBuilder)s).append("Could not read from response stream [");
                        ((StringBuilder)s).append(((Throwable)ex3).getMessage());
                        ((StringBuilder)s).append("]");
                        s = ((StringBuilder)s).toString();
                    }
                }
                finally {}
            }
            finally {
                o = s;
            }
        }
        if (jsonObject != null) {
            ((HttpURLConnection)jsonObject).disconnect();
        }
        if (o != null) {
            try {
                ((InputStream)o).close();
            }
            catch (Exception ex4) {
                AppboyLogger.e(f.a, "Caught an error trying to close the inputStream in getJsonResultFromUrl", ex4);
            }
        }
        throw url;
    }
    
    private void a(final HttpURLConnection httpURLConnection, final JSONObject jsonObject) {
        httpURLConnection.setDoOutput(true);
        final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(httpURLConnection.getOutputStream());
        bufferedOutputStream.write(jsonObject.toString().getBytes("UTF-8"));
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
    }
    
    private HttpURLConnection b(final URL url, final JSONObject jsonObject, final Map<String, String> map, final y y) {
        try {
            final HttpURLConnection httpURLConnection = (HttpURLConnection)k.a(url);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setReadTimeout(this.b);
            httpURLConnection.setInstanceFollowRedirects(false);
            httpURLConnection.setRequestMethod(y.toString());
            this.a(httpURLConnection, map);
            if (y == y.b) {
                this.a(httpURLConnection, jsonObject);
            }
            return httpURLConnection;
        }
        catch (IOException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Could not set up connection [");
            sb.append(url.toString());
            sb.append("] [");
            sb.append(ex.getMessage());
            sb.append("].  Appboy will try to reconnect periodically.");
            throw new aw(sb.toString(), ex);
        }
    }
    
    @Override
    public JSONObject a(final URI uri, final Map<String, String> map) {
        return this.a(uri, null, map, y.a);
    }
    
    @Override
    public JSONObject a(final URI uri, final Map<String, String> map, final JSONObject jsonObject) {
        return this.a(uri, jsonObject, map, y.b);
    }
    
    void a(final HttpURLConnection httpURLConnection, final Map<String, String> map) {
        for (final Map.Entry<String, String> entry : map.entrySet()) {
            httpURLConnection.setRequestProperty(entry.getKey(), entry.getValue());
        }
    }
}
