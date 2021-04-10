package com.microsoft.xbox.toolkit.ui;

import java.net.*;
import android.content.*;
import android.util.*;
import com.microsoft.xbox.toolkit.*;
import android.content.res.*;
import android.widget.*;
import android.net.*;
import android.view.*;

public class XLEImageViewFast extends XLEImageView
{
    private TextureBindingOption option;
    protected int pendingBitmapResourceId;
    private String pendingFilePath;
    protected URI pendingUri;
    private boolean useFileCache;
    
    public XLEImageViewFast(final Context context) {
        super(context);
        this.pendingBitmapResourceId = -1;
        this.pendingUri = null;
        this.pendingFilePath = null;
        this.useFileCache = true;
        this.setSoundEffectsEnabled(false);
    }
    
    public XLEImageViewFast(final Context context, final AttributeSet set) {
        super(context, set);
        this.pendingBitmapResourceId = -1;
        this.pendingUri = null;
        this.pendingFilePath = null;
        this.useFileCache = true;
        if (this.isInEditMode()) {
            return;
        }
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, XLERValueHelper.getStyleableRValueArray("XLEImageViewFast"));
        this.setImageResource(obtainStyledAttributes.getResourceId(XLERValueHelper.getStyleableRValue("XLEImageViewFast_src"), -1));
        obtainStyledAttributes.recycle();
        this.setSoundEffectsEnabled(false);
    }
    
    private void bindToFilePath(final String s) {
        this.pendingFilePath = null;
        TextureManager.Instance().bindToViewFromFile(s, this, this.getWidth(), this.getHeight());
    }
    
    private void bindToResourceId(final int n) {
        this.pendingBitmapResourceId = -1;
        TextureManager.Instance().bindToView(n, this, this.getWidth(), this.getHeight());
    }
    
    private void bindToUri(final URI uri, final TextureBindingOption textureBindingOption) {
        this.pendingUri = null;
        this.option = null;
        TextureManager.Instance().bindToView(uri, this, textureBindingOption);
    }
    
    protected void bindToUri(final URI uri) {
        this.pendingUri = null;
        this.bindToUri(uri, new TextureBindingOption(this.getWidth(), this.getHeight(), this.useFileCache));
    }
    
    protected boolean hasSize() {
        return this.getWidth() > 0 && this.getHeight() > 0;
    }
    
    protected void onMeasure(final int n, final int n2) {
        this.setMeasuredDimension(resolveSize(0, n), resolveSize(0, n2));
    }
    
    protected void onSizeChanged(int pendingBitmapResourceId, final int n, final int n2, final int n3) {
        super.onSizeChanged(pendingBitmapResourceId, n, n2, n3);
        if (this.hasSize()) {
            pendingBitmapResourceId = this.pendingBitmapResourceId;
            if (pendingBitmapResourceId >= 0) {
                this.bindToResourceId(pendingBitmapResourceId);
            }
            final URI pendingUri = this.pendingUri;
            if (pendingUri != null || (pendingUri == null && this.option != null)) {
                if (this.option != null) {
                    this.bindToUri(this.pendingUri, new TextureBindingOption(this.getWidth(), this.getHeight(), this.option.resourceIdForLoading, this.option.resourceIdForError, this.option.useFileCache));
                }
                else {
                    this.bindToUri(this.pendingUri);
                }
            }
            final String pendingFilePath = this.pendingFilePath;
            if (pendingFilePath != null) {
                this.bindToFilePath(pendingFilePath);
            }
        }
    }
    
    public void setImageFilePath(final String pendingFilePath) {
        if (this.hasSize()) {
            this.bindToFilePath(pendingFilePath);
            return;
        }
        this.pendingFilePath = pendingFilePath;
    }
    
    public void setImageResource(final int pendingBitmapResourceId) {
        if (this.hasSize()) {
            this.bindToResourceId(pendingBitmapResourceId);
            return;
        }
        this.pendingBitmapResourceId = pendingBitmapResourceId;
    }
    
    public void setImageURI(final Uri uri) {
        throw new UnsupportedOperationException();
    }
    
    public void setImageURI2(final URI pendingUri) {
        if (this.hasSize()) {
            this.bindToUri(pendingUri);
            return;
        }
        this.pendingUri = pendingUri;
    }
    
    public void setImageURI2(final URI pendingUri, final int n, final int n2) {
        this.option = new TextureBindingOption(this.getWidth(), this.getHeight(), n, n2, this.useFileCache);
        if (this.hasSize()) {
            this.bindToUri(pendingUri, this.option);
            return;
        }
        this.pendingUri = pendingUri;
    }
    
    public void setImageURI2(final URI pendingUri, final boolean useFileCache) {
        this.useFileCache = useFileCache;
        this.option = new TextureBindingOption(this.getWidth(), this.getHeight(), this.useFileCache);
        if (this.hasSize()) {
            this.bindToUri(pendingUri, this.option);
            return;
        }
        this.pendingUri = pendingUri;
    }
    
    @Override
    public void setOnClickListener(final View$OnClickListener view$OnClickListener) {
        super.setOnClickListener(TouchUtil.createOnClickListener(view$OnClickListener));
    }
}
