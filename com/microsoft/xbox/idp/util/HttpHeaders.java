package com.microsoft.xbox.idp.util;

import java.util.*;

public class HttpHeaders
{
    private final List<Header> headers;
    
    public HttpHeaders() {
        this.headers = new ArrayList<Header>();
    }
    
    public void add(final String s, final String s2) {
        this.headers.add(new Header(s, s2));
    }
    
    public Collection<Header> getAllHeaders() {
        return this.headers;
    }
    
    public Header getFirstHeader(final String s) {
        if (s != null) {
            for (final Header header : this.headers) {
                if (s.equals(header.key)) {
                    return header;
                }
            }
        }
        return null;
    }
    
    public Header getLastHeader(final String s) {
        if (s != null) {
            int size = this.headers.size();
            Header header;
            do {
                --size;
                if (size < 0) {
                    return null;
                }
                header = this.headers.get(size);
            } while (!s.equals(header.key));
            return header;
        }
        return null;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        final Iterator<Header> iterator = this.headers.iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next());
        }
        sb.append(" ]");
        return sb.toString();
    }
    
    public static class Header
    {
        private final String key;
        private final String value;
        
        public Header(final String key, final String value) {
            this.key = key;
            this.value = value;
        }
        
        public String getKey() {
            return this.key;
        }
        
        public String getValue() {
            return this.value;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("{ ");
            sb.append("\"");
            sb.append(this.key);
            sb.append("\": ");
            sb.append("\"");
            sb.append(this.value);
            sb.append("\"");
            sb.append(" }");
            return sb.toString();
        }
    }
}
