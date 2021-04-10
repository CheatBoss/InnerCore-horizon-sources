package com.microsoft.xbox.toolkit.ui;

import com.microsoft.xbox.toolkit.*;

public class TextureBindingOption
{
    public static final int DO_NOT_SCALE = -1;
    public static final int DO_NOT_USE_PLACEHOLDER = -1;
    public static final TextureBindingOption DefaultBindingOption;
    public static final int DefaultResourceIdForEmpty;
    public static final int DefaultResourceIdForError;
    public static final int DefaultResourceIdForLoading;
    public static final TextureBindingOption KeepAsIsBindingOption;
    public final int height;
    public final int resourceIdForError;
    public final int resourceIdForLoading;
    public final boolean useFileCache;
    public final int width;
    
    static {
        DefaultResourceIdForLoading = XLERValueHelper.getDrawableRValue("empty");
        DefaultResourceIdForEmpty = XLERValueHelper.getDrawableRValue("empty");
        DefaultResourceIdForError = XLERValueHelper.getDrawableRValue("error");
        DefaultBindingOption = new TextureBindingOption();
        KeepAsIsBindingOption = new TextureBindingOption(-1, -1, -1, -1, false);
    }
    
    public TextureBindingOption() {
        this(-1, -1, TextureBindingOption.DefaultResourceIdForLoading, TextureBindingOption.DefaultResourceIdForError, false);
    }
    
    public TextureBindingOption(final int n, final int n2) {
        this(n, n2, true);
    }
    
    public TextureBindingOption(final int width, final int height, final int resourceIdForLoading, final int resourceIdForError, final boolean useFileCache) {
        this.width = width;
        this.height = height;
        this.resourceIdForLoading = resourceIdForLoading;
        this.resourceIdForError = resourceIdForError;
        this.useFileCache = useFileCache;
    }
    
    public TextureBindingOption(final int n, final int n2, final boolean b) {
        this(n, n2, TextureBindingOption.DefaultResourceIdForLoading, TextureBindingOption.DefaultResourceIdForError, b);
    }
    
    public static TextureBindingOption createDoNotScale(final int n, final int n2, final boolean b) {
        return new TextureBindingOption(-1, -1, n, n2, b);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TextureBindingOption)) {
            return false;
        }
        final TextureBindingOption textureBindingOption = (TextureBindingOption)o;
        return this.width == textureBindingOption.width && this.height == textureBindingOption.height && this.resourceIdForError == textureBindingOption.resourceIdForError && this.resourceIdForLoading == textureBindingOption.resourceIdForLoading;
    }
    
    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }
}
