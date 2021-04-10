package com.microsoft.xbox.toolkit.ui;

import com.microsoft.xbox.toolkit.*;

public class TextureManagerScaledNetworkBitmapRequest implements XLEFileCacheItemKey
{
    public final TextureBindingOption bindingOption;
    public final String url;
    
    public TextureManagerScaledNetworkBitmapRequest(final String s) {
        this(s, new TextureBindingOption());
    }
    
    public TextureManagerScaledNetworkBitmapRequest(final String url, final TextureBindingOption bindingOption) {
        this.url = url;
        this.bindingOption = bindingOption;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TextureManagerScaledNetworkBitmapRequest)) {
            return false;
        }
        final TextureManagerScaledNetworkBitmapRequest textureManagerScaledNetworkBitmapRequest = (TextureManagerScaledNetworkBitmapRequest)o;
        return this.url.equals(textureManagerScaledNetworkBitmapRequest.url) && this.bindingOption.equals(textureManagerScaledNetworkBitmapRequest.bindingOption);
    }
    
    @Override
    public String getKeyString() {
        return this.url;
    }
    
    @Override
    public int hashCode() {
        final String url = this.url;
        if (url == null) {
            return 0;
        }
        return url.hashCode();
    }
}
