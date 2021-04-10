package com.microsoft.xbox.toolkit.ui;

import android.widget.*;
import android.content.*;
import android.util.*;
import android.graphics.*;
import android.view.*;

public class XLEImageView extends ImageView
{
    public static final int IMAGE_ERROR = 2;
    public static final int IMAGE_FINAL = 0;
    public static final int IMAGE_LOADING = 1;
    public String TEST_loadingOrLoadedImageUrl;
    protected boolean isFinal;
    protected boolean shouldAnimate;
    
    public XLEImageView(final Context context) {
        this(context, null, 0);
    }
    
    public XLEImageView(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public XLEImageView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.shouldAnimate = true;
        this.setSoundEffectsEnabled(this.isFinal = false);
    }
    
    public boolean getShouldAnimate() {
        return this.shouldAnimate && !this.isFinal;
    }
    
    public void setFinal(final boolean isFinal) {
        this.isFinal = isFinal;
    }
    
    public void setImageBitmap(final Bitmap imageBitmap) {
        if (imageBitmap == null) {
            return;
        }
        super.setImageBitmap(imageBitmap);
    }
    
    public void setImageSource(final Bitmap imageBitmap, final int n) {
        if (imageBitmap == null) {
            return;
        }
        super.setImageBitmap(imageBitmap);
    }
    
    public void setOnClickListener(final View$OnClickListener view$OnClickListener) {
        super.setOnClickListener(TouchUtil.createOnClickListener(view$OnClickListener));
    }
    
    public void setShouldAnimate(final boolean shouldAnimate) {
        this.shouldAnimate = shouldAnimate;
    }
}
