package org.mozilla.javascript.commonjs.module.provider;

import java.io.*;
import java.util.*;
import java.net.*;

public class UrlModuleSourceProvider extends ModuleSourceProviderBase
{
    private static final long serialVersionUID = 1L;
    private final Iterable<URI> fallbackUris;
    private final Iterable<URI> privilegedUris;
    private final UrlConnectionExpiryCalculator urlConnectionExpiryCalculator;
    private final UrlConnectionSecurityDomainProvider urlConnectionSecurityDomainProvider;
    
    public UrlModuleSourceProvider(final Iterable<URI> iterable, final Iterable<URI> iterable2) {
        this(iterable, iterable2, new DefaultUrlConnectionExpiryCalculator(), null);
    }
    
    public UrlModuleSourceProvider(final Iterable<URI> privilegedUris, final Iterable<URI> fallbackUris, final UrlConnectionExpiryCalculator urlConnectionExpiryCalculator, final UrlConnectionSecurityDomainProvider urlConnectionSecurityDomainProvider) {
        this.privilegedUris = privilegedUris;
        this.fallbackUris = fallbackUris;
        this.urlConnectionExpiryCalculator = urlConnectionExpiryCalculator;
        this.urlConnectionSecurityDomainProvider = urlConnectionSecurityDomainProvider;
    }
    
    private void close(final URLConnection urlConnection) {
        try {
            urlConnection.getInputStream().close();
        }
        catch (IOException ex) {
            this.onFailedClosingUrlConnection(urlConnection, ex);
        }
    }
    
    private static String getCharacterEncoding(final URLConnection urlConnection) {
        final ParsedContentType parsedContentType = new ParsedContentType(urlConnection.getContentType());
        final String encoding = parsedContentType.getEncoding();
        if (encoding != null) {
            return encoding;
        }
        final String contentType = parsedContentType.getContentType();
        if (contentType != null && contentType.startsWith("text/")) {
            return "8859_1";
        }
        return "utf-8";
    }
    
    private static Reader getReader(final URLConnection urlConnection) throws IOException {
        return new InputStreamReader(urlConnection.getInputStream(), getCharacterEncoding(urlConnection));
    }
    
    private Object getSecurityDomain(final URLConnection urlConnection) {
        if (this.urlConnectionSecurityDomainProvider == null) {
            return null;
        }
        return this.urlConnectionSecurityDomainProvider.getSecurityDomain(urlConnection);
    }
    
    private ModuleSource loadFromPathList(final String s, final Object o, final Iterable<URI> iterable) throws IOException, URISyntaxException {
        if (iterable == null) {
            return null;
        }
        for (final URI uri : iterable) {
            final ModuleSource loadFromUri = this.loadFromUri(uri.resolve(s), uri, o);
            if (loadFromUri != null) {
                return loadFromUri;
            }
        }
        return null;
    }
    
    @Override
    protected boolean entityNeedsRevalidation(final Object o) {
        return !(o instanceof URLValidator) || ((URLValidator)o).entityNeedsRevalidation();
    }
    
    protected ModuleSource loadFromActualUri(final URI ex, final URI uri, final Object o) throws IOException {
        URL url;
        if (uri == null) {
            url = null;
        }
        else {
            url = uri.toURL();
        }
        final URL url2 = new URL(url, ((URI)ex).toString());
        final long currentTimeMillis = System.currentTimeMillis();
        final URLConnection openUrlConnection = this.openUrlConnection(url2);
        URLValidator urlValidator;
        if (o instanceof URLValidator) {
            urlValidator = (URLValidator)o;
            if (!urlValidator.appliesTo((URI)ex)) {
                urlValidator = null;
            }
        }
        else {
            urlValidator = null;
        }
        if (urlValidator != null) {
            urlValidator.applyConditionals(openUrlConnection);
        }
        try {
            openUrlConnection.connect();
            if (urlValidator != null) {
                try {
                    if (urlValidator.updateValidator(openUrlConnection, currentTimeMillis, this.urlConnectionExpiryCalculator)) {
                        this.close(openUrlConnection);
                        return UrlModuleSourceProvider.NOT_MODIFIED;
                    }
                }
                catch (IOException ex) {
                    goto Label_0192;
                }
                catch (RuntimeException ex) {
                    goto Label_0201;
                }
                catch (FileNotFoundException ex) {
                    return null;
                }
            }
            final Reader reader = getReader(openUrlConnection);
            final Object securityDomain = this.getSecurityDomain(openUrlConnection);
            final UrlConnectionExpiryCalculator urlConnectionExpiryCalculator = this.urlConnectionExpiryCalculator;
            try {
                return new ModuleSource(reader, securityDomain, (URI)ex, uri, new URLValidator((URI)ex, openUrlConnection, currentTimeMillis, urlConnectionExpiryCalculator));
            }
            catch (IOException ex) {}
            catch (RuntimeException ex) {}
            catch (FileNotFoundException ex) {
                return null;
            }
        }
        catch (IOException ex2) {}
        catch (RuntimeException ex3) {}
        catch (FileNotFoundException ex4) {
            return null;
        }
    }
    
    @Override
    protected ModuleSource loadFromFallbackLocations(final String s, final Object o) throws IOException, URISyntaxException {
        return this.loadFromPathList(s, o, this.fallbackUris);
    }
    
    @Override
    protected ModuleSource loadFromPrivilegedLocations(final String s, final Object o) throws IOException, URISyntaxException {
        return this.loadFromPathList(s, o, this.privilegedUris);
    }
    
    @Override
    protected ModuleSource loadFromUri(final URI uri, final URI uri2, final Object o) throws IOException, URISyntaxException {
        final StringBuilder sb = new StringBuilder();
        sb.append(uri);
        sb.append(".js");
        final ModuleSource loadFromActualUri = this.loadFromActualUri(new URI(sb.toString()), uri2, o);
        if (loadFromActualUri != null) {
            return loadFromActualUri;
        }
        return this.loadFromActualUri(uri, uri2, o);
    }
    
    protected void onFailedClosingUrlConnection(final URLConnection urlConnection, final IOException ex) {
    }
    
    protected URLConnection openUrlConnection(final URL url) throws IOException {
        return url.openConnection();
    }
    
    private static class URLValidator implements Serializable
    {
        private static final long serialVersionUID = 1L;
        private final String entityTags;
        private long expiry;
        private final long lastModified;
        private final URI uri;
        
        public URLValidator(final URI uri, final URLConnection urlConnection, final long n, final UrlConnectionExpiryCalculator urlConnectionExpiryCalculator) {
            this.uri = uri;
            this.lastModified = urlConnection.getLastModified();
            this.entityTags = this.getEntityTags(urlConnection);
            this.expiry = this.calculateExpiry(urlConnection, n, urlConnectionExpiryCalculator);
        }
        
        private long calculateExpiry(final URLConnection urlConnection, long headerFieldDate, final UrlConnectionExpiryCalculator urlConnectionExpiryCalculator) {
            if ("no-cache".equals(urlConnection.getHeaderField("Pragma"))) {
                return 0L;
            }
            final String headerField = urlConnection.getHeaderField("Cache-Control");
            if (headerField != null) {
                if (headerField.indexOf("no-cache") != -1) {
                    return 0L;
                }
                final int maxAge = this.getMaxAge(headerField);
                if (-1 != maxAge) {
                    final long currentTimeMillis = System.currentTimeMillis();
                    return maxAge * 1000L + (currentTimeMillis - (Math.max(Math.max(0L, currentTimeMillis - urlConnection.getDate()), urlConnection.getHeaderFieldInt("Age", 0) * 1000L) + (currentTimeMillis - headerFieldDate)));
                }
            }
            headerFieldDate = urlConnection.getHeaderFieldDate("Expires", -1L);
            if (headerFieldDate != -1L) {
                return headerFieldDate;
            }
            if (urlConnectionExpiryCalculator == null) {
                return 0L;
            }
            return urlConnectionExpiryCalculator.calculateExpiry(urlConnection);
        }
        
        private String getEntityTags(final URLConnection urlConnection) {
            final List<String> list = urlConnection.getHeaderFields().get("ETag");
            if (list != null && !list.isEmpty()) {
                final StringBuilder sb = new StringBuilder();
                final Iterator<String> iterator = list.iterator();
                sb.append(iterator.next());
                while (iterator.hasNext()) {
                    sb.append(", ");
                    sb.append(iterator.next());
                }
                return sb.toString();
            }
            return null;
        }
        
        private int getMaxAge(String s) {
            final int index = s.indexOf("max-age");
            if (index == -1) {
                return -1;
            }
            final int index2 = s.indexOf(61, index + 7);
            if (index2 == -1) {
                return -1;
            }
            final int index3 = s.indexOf(44, index2 + 1);
            if (index3 == -1) {
                s = s.substring(index2 + 1);
            }
            else {
                s = s.substring(index2 + 1, index3);
            }
            try {
                return Integer.parseInt(s);
            }
            catch (NumberFormatException ex) {
                return -1;
            }
        }
        
        private boolean isResourceChanged(final URLConnection urlConnection) throws IOException {
            final boolean b = urlConnection instanceof HttpURLConnection;
            final boolean b2 = false;
            boolean b3 = false;
            if (b) {
                if (((HttpURLConnection)urlConnection).getResponseCode() == 304) {
                    b3 = true;
                }
                return b3;
            }
            boolean b4 = b2;
            if (this.lastModified == urlConnection.getLastModified()) {
                b4 = true;
            }
            return b4;
        }
        
        boolean appliesTo(final URI uri) {
            return this.uri.equals(uri);
        }
        
        void applyConditionals(final URLConnection urlConnection) {
            if (this.lastModified != 0L) {
                urlConnection.setIfModifiedSince(this.lastModified);
            }
            if (this.entityTags != null && this.entityTags.length() > 0) {
                urlConnection.addRequestProperty("If-None-Match", this.entityTags);
            }
        }
        
        boolean entityNeedsRevalidation() {
            return System.currentTimeMillis() > this.expiry;
        }
        
        boolean updateValidator(final URLConnection urlConnection, final long n, final UrlConnectionExpiryCalculator urlConnectionExpiryCalculator) throws IOException {
            final boolean resourceChanged = this.isResourceChanged(urlConnection);
            if (!resourceChanged) {
                this.expiry = this.calculateExpiry(urlConnection, n, urlConnectionExpiryCalculator);
            }
            return resourceChanged;
        }
    }
}
