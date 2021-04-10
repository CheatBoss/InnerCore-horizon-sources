package com.microsoft.xbox.toolkit.ui;

import android.graphics.*;
import com.microsoft.xbox.toolkit.system.*;

public class XLETextArg
{
    private final Params params;
    private final String text;
    
    public XLETextArg(final Params params) {
        this(null, params);
    }
    
    public XLETextArg(final String text, final Params params) {
        this.text = text;
        this.params = params;
    }
    
    public Params getParams() {
        return this.params;
    }
    
    public String getText() {
        return this.text;
    }
    
    public boolean hasText() {
        return this.text != null;
    }
    
    public static class Params
    {
        private final boolean adjustForImageSize;
        private final int color;
        private final int eraseColor;
        private final Float textAspectRatio;
        private final float textSize;
        private final Typeface typeface;
        
        public Params() {
            this((float)SystemUtil.SPtoPixels(8.0f), -1, Typeface.DEFAULT, 0, false, null);
        }
        
        public Params(final float textSize, final int color, final Typeface typeface, final int eraseColor, final boolean adjustForImageSize, final Float textAspectRatio) {
            this.textSize = textSize;
            this.color = color;
            this.typeface = typeface;
            this.eraseColor = eraseColor;
            this.adjustForImageSize = adjustForImageSize;
            this.textAspectRatio = textAspectRatio;
        }
        
        public int getColor() {
            return this.color;
        }
        
        public int getEraseColor() {
            return this.eraseColor;
        }
        
        public Float getTextAspectRatio() {
            return this.textAspectRatio;
        }
        
        public float getTextSize() {
            return this.textSize;
        }
        
        public Typeface getTypeface() {
            return this.typeface;
        }
        
        public boolean hasEraseColor() {
            return this.eraseColor != 0;
        }
        
        public boolean hasTextAspectRatio() {
            return this.textAspectRatio != null;
        }
        
        public boolean isAdjustForImageSize() {
            return this.adjustForImageSize;
        }
    }
}
