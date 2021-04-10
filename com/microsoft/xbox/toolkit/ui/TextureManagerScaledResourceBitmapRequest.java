package com.microsoft.xbox.toolkit.ui;

public class TextureManagerScaledResourceBitmapRequest
{
    public final TextureBindingOption bindingOption;
    public final int resourceId;
    
    public TextureManagerScaledResourceBitmapRequest(final int n) {
        this(n, new TextureBindingOption());
    }
    
    public TextureManagerScaledResourceBitmapRequest(final int resourceId, final TextureBindingOption bindingOption) {
        this.resourceId = resourceId;
        this.bindingOption = bindingOption;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TextureManagerScaledResourceBitmapRequest)) {
            return false;
        }
        final TextureManagerScaledResourceBitmapRequest textureManagerScaledResourceBitmapRequest = (TextureManagerScaledResourceBitmapRequest)o;
        return this.resourceId == textureManagerScaledResourceBitmapRequest.resourceId && this.bindingOption.equals(textureManagerScaledResourceBitmapRequest.bindingOption);
    }
    
    @Override
    public int hashCode() {
        return this.resourceId;
    }
}
