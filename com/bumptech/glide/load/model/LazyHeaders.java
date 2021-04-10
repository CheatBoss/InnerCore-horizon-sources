package com.bumptech.glide.load.model;

import java.util.*;

public final class LazyHeaders implements Headers
{
    private volatile Map<String, String> combinedHeaders;
    private final Map<String, List<LazyHeaderFactory>> headers;
    
    LazyHeaders(final Map<String, List<LazyHeaderFactory>> map) {
        this.headers = Collections.unmodifiableMap((Map<? extends String, ? extends List<LazyHeaderFactory>>)map);
    }
    
    private Map<String, String> generateHeaders() {
        final HashMap<Object, String> hashMap = new HashMap<Object, String>();
        for (final Map.Entry<String, List<LazyHeaderFactory>> entry : this.headers.entrySet()) {
            final StringBuilder sb = new StringBuilder();
            final List<LazyHeaderFactory> list = entry.getValue();
            for (int i = 0; i < list.size(); ++i) {
                sb.append(list.get(i).buildHeader());
                if (i != list.size() - 1) {
                    sb.append(',');
                }
            }
            hashMap.put(entry.getKey(), sb.toString());
        }
        return (Map<String, String>)hashMap;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof LazyHeaders && this.headers.equals(((LazyHeaders)o).headers);
    }
    
    @Override
    public Map<String, String> getHeaders() {
        if (this.combinedHeaders == null) {
            synchronized (this) {
                if (this.combinedHeaders == null) {
                    this.combinedHeaders = Collections.unmodifiableMap((Map<? extends String, ? extends String>)this.generateHeaders());
                }
            }
        }
        return this.combinedHeaders;
    }
    
    @Override
    public int hashCode() {
        return this.headers.hashCode();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("LazyHeaders{headers=");
        sb.append(this.headers);
        sb.append('}');
        return sb.toString();
    }
    
    public static final class Builder
    {
        private boolean copyOnModify;
        private Map<String, List<LazyHeaderFactory>> headers;
        
        public Builder() {
            this.headers = new HashMap<String, List<LazyHeaderFactory>>();
        }
        
        private Map<String, List<LazyHeaderFactory>> copyHeaders() {
            final HashMap<String, ArrayList> hashMap = (HashMap<String, ArrayList>)new HashMap<String, ArrayList<LazyHeaderFactory>>(this.headers.size());
            for (final Map.Entry<String, List<LazyHeaderFactory>> entry : this.headers.entrySet()) {
                hashMap.put(entry.getKey(), new ArrayList<LazyHeaderFactory>(entry.getValue()));
            }
            return (Map<String, List<LazyHeaderFactory>>)hashMap;
        }
        
        public Builder addHeader(final String s, final LazyHeaderFactory lazyHeaderFactory) {
            if (this.copyOnModify) {
                this.copyOnModify = false;
                this.headers = this.copyHeaders();
            }
            List<LazyHeaderFactory> list;
            if ((list = this.headers.get(s)) == null) {
                list = new ArrayList<LazyHeaderFactory>();
                this.headers.put(s, list);
            }
            list.add(lazyHeaderFactory);
            return this;
        }
        
        public Builder addHeader(final String s, final String s2) {
            return this.addHeader(s, new StringHeaderFactory(s2));
        }
        
        public LazyHeaders build() {
            this.copyOnModify = true;
            return new LazyHeaders(this.headers);
        }
    }
    
    static final class StringHeaderFactory implements LazyHeaderFactory
    {
        private final String value;
        
        StringHeaderFactory(final String value) {
            this.value = value;
        }
        
        @Override
        public String buildHeader() {
            return this.value;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof StringHeaderFactory && this.value.equals(((StringHeaderFactory)o).value);
        }
        
        @Override
        public int hashCode() {
            return this.value.hashCode();
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("StringHeaderFactory{value='");
            sb.append(this.value);
            sb.append('\'');
            sb.append('}');
            return sb.toString();
        }
    }
}
