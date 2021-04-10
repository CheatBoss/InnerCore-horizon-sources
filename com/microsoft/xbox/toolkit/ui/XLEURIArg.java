package com.microsoft.xbox.toolkit.ui;

import java.net.*;

public class XLEURIArg
{
    private final int errorResourceId;
    private final int loadingResourceId;
    private final URI uri;
    
    public XLEURIArg(final URI uri) {
        this(uri, -1, -1);
    }
    
    public XLEURIArg(final URI uri, final int loadingResourceId, final int errorResourceId) {
        this.uri = uri;
        this.loadingResourceId = loadingResourceId;
        this.errorResourceId = errorResourceId;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = false;
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof XLEURIArg)) {
            return false;
        }
        final XLEURIArg xleuriArg = (XLEURIArg)o;
        if (this.loadingResourceId != xleuriArg.loadingResourceId) {
            return false;
        }
        if (this.errorResourceId != xleuriArg.errorResourceId) {
            return false;
        }
        final URI uri = this.uri;
        final URI uri2 = xleuriArg.uri;
        if (uri != uri2) {
            boolean b2 = b;
            if (uri == null) {
                return b2;
            }
            b2 = b;
            if (!uri.equals(uri2)) {
                return b2;
            }
        }
        return true;
    }
    
    public int getErrorResourceId() {
        return this.errorResourceId;
    }
    
    public int getLoadingResourceId() {
        return this.loadingResourceId;
    }
    
    public TextureBindingOption getTextureBindingOption() {
        return new TextureBindingOption(-1, -1, this.loadingResourceId, this.errorResourceId, false);
    }
    
    public URI getUri() {
        return this.uri;
    }
    
    @Override
    public int hashCode() {
        final int n = (this.loadingResourceId + 13) * 17 + this.errorResourceId;
        final URI uri = this.uri;
        int n2 = n;
        if (uri != null) {
            n2 = n * 23 + uri.hashCode();
        }
        return n2;
    }
}
