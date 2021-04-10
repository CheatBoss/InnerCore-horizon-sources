package com.microsoft.xbox.toolkit.ui;

import android.content.*;
import android.util.*;
import android.widget.*;
import com.microsoft.xbox.toolkit.*;
import android.graphics.*;
import java.net.*;
import android.content.res.*;
import android.view.*;
import android.graphics.drawable.*;
import android.text.*;

public class XLEUniversalImageView extends XLEImageView
{
    private static final int JELLY_BEAN_MR1 = 17;
    private static final String TAG;
    private boolean adjustViewBounds;
    private Params arg;
    private final View$OnLayoutChangeListener listener;
    private int maxHeight;
    private int maxWidth;
    
    static {
        TAG = XLEUniversalImageView.class.getSimpleName();
    }
    
    public XLEUniversalImageView(final Context context) {
        this(context, new Params());
    }
    
    public XLEUniversalImageView(final Context context, final AttributeSet set) {
        super(context, set);
        this.listener = (View$OnLayoutChangeListener)new View$OnLayoutChangeListener() {
            public void onLayoutChange(final View view, final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final int n8) {
                if ((n3 - n != n7 - n5 || n4 - n2 != n8 - n6) && XLEUniversalImageView.this.arg.hasText()) {
                    new XLETextTask(XLEUniversalImageView.this).execute((Object[])new XLETextArg[] { XLEUniversalImageView.this.arg.getArgText() });
                }
            }
        };
        this.arg = this.initializeAttributes(context, set, 0);
        this.updateImage();
    }
    
    public XLEUniversalImageView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.listener = (View$OnLayoutChangeListener)new View$OnLayoutChangeListener() {
            public void onLayoutChange(final View view, final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final int n8) {
                if ((n3 - n != n7 - n5 || n4 - n2 != n8 - n6) && XLEUniversalImageView.this.arg.hasText()) {
                    new XLETextTask(XLEUniversalImageView.this).execute((Object[])new XLETextArg[] { XLEUniversalImageView.this.arg.getArgText() });
                }
            }
        };
        this.arg = this.initializeAttributes(context, set, n);
        this.updateImage();
    }
    
    public XLEUniversalImageView(final Context context, final Params arg) {
        super(context);
        this.listener = (View$OnLayoutChangeListener)new View$OnLayoutChangeListener() {
            public void onLayoutChange(final View view, final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final int n8) {
                if ((n3 - n != n7 - n5 || n4 - n2 != n8 - n6) && XLEUniversalImageView.this.arg.hasText()) {
                    new XLETextTask(XLEUniversalImageView.this).execute((Object[])new XLETextArg[] { XLEUniversalImageView.this.arg.getArgText() });
                }
            }
        };
        this.setMaxWidth(Integer.MAX_VALUE);
        this.setMaxHeight(Integer.MAX_VALUE);
        this.arg = arg;
    }
    
    private Params initializeAttributes(final Context context, AttributeSet string, int color) {
    Label_0194_Outer:
        while (true) {
            final TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(string, XLERValueHelper.getStyleableRValueArray("XLEUniversalImageView"), color, 0);
        Label_0194:
            while (true) {
            Block_5_Outer:
                while (true) {
                    Label_0389: {
                        try {
                            float n = context.getResources().getDisplayMetrics().scaledDensity;
                            n = obtainStyledAttributes.getDimension(XLERValueHelper.getStyleableRValue("XLEUniversalImageView_android_textSize"), n * 8.0f);
                            color = obtainStyledAttributes.getColor(XLERValueHelper.getStyleableRValue("XLEUniversalImageView_android_textColor"), 0);
                            int n2 = obtainStyledAttributes.getInt(XLERValueHelper.getStyleableRValue("XLEUniversalImageView_android_typeface"), -1);
                            final int int1 = obtainStyledAttributes.getInt(XLERValueHelper.getStyleableRValue("XLEUniversalImageView_android_textStyle"), 0);
                            final String string2 = obtainStyledAttributes.getString(XLERValueHelper.getStyleableRValue("XLEUniversalImageView_typefaceSource"));
                            if (string2 == null) {
                                Typeface.create(TypefaceXml.typefaceFromIndex(n2), int1);
                                break Label_0389;
                            }
                            FontManager.Instance().getTypeface(context, string2);
                            break Label_0389;
                            n2 = obtainStyledAttributes.getColor(XLERValueHelper.getStyleableRValue("XLEUniversalImageView_eraseColor"), 0);
                            final boolean boolean1 = obtainStyledAttributes.getBoolean(XLERValueHelper.getStyleableRValue("XLEUniversalImageView_adjustForImageSize"), false);
                            final boolean hasValue = obtainStyledAttributes.hasValue(XLERValueHelper.getStyleableRValue("XLEUniversalImageView_android_src"));
                            // iftrue(Label_0392:, !obtainStyledAttributes.hasValue(XLERValueHelper.getStyleableRValue("XLEUniversalImageView_textAspectRatio")))
                            Block_4: {
                                break Block_4;
                                Label_0374: {
                                    return;
                                }
                            }
                            final Float value = obtainStyledAttributes.getFloat(XLERValueHelper.getStyleableRValue("XLEUniversalImageView_textAspectRatio"), 0.0f);
                            break Label_0194;
                            // iftrue(Label_0343:, string == null)
                            // iftrue(Label_0374:, !boolean1)
                            while (true) {
                                Params params = null;
                            Label_0361_Outer:
                                while (true) {
                                    final XLETextArg.Params params2;
                                    params = new Params(new XLETextArg(params2), hasValue);
                                    while (true) {
                                        break Label_0361;
                                        final String string3;
                                        params = new Params(new XLETextArg(string3, params2), false);
                                        break Label_0361;
                                        Label_0247: {
                                            string = (AttributeSet)obtainStyledAttributes.getString(XLERValueHelper.getStyleableRValue("XLEUniversalImageView_uri"));
                                        }
                                        Block_6: {
                                            break Block_6;
                                            break Label_0361_Outer;
                                        }
                                        try {
                                            params = new Params(new XLETextArg(params2), new XLEURIArg(new URI((String)string)));
                                            continue Block_5_Outer;
                                        }
                                        catch (URISyntaxException ex) {
                                            final StringBuilder sb = new StringBuilder();
                                            sb.append("Error parsing URI '");
                                            sb.append((String)string);
                                            sb.append("'");
                                            throw new RuntimeException(sb.toString(), ex);
                                        }
                                        break;
                                    }
                                    continue Label_0361_Outer;
                                }
                                this.addOnLayoutChangeListener(this.listener);
                                return params;
                                final Typeface typeface;
                                final XLETextArg.Params params2 = new XLETextArg.Params(n, color, typeface, n2, boolean1, value);
                                final String string3 = obtainStyledAttributes.getString(XLERValueHelper.getStyleableRValue("XLEUniversalImageView_android_text"));
                                continue;
                            }
                        }
                        // iftrue(Label_0247:, string3 == null)
                        finally {
                            obtainStyledAttributes.recycle();
                        }
                    }
                    continue Label_0194_Outer;
                }
                Label_0392: {
                    final Float value = null;
                }
                continue Label_0194;
            }
        }
    }
    
    private int resolveAdjustedSize(final int n, final int n2, int size) {
        final int mode = View$MeasureSpec.getMode(size);
        size = View$MeasureSpec.getSize(size);
        if (mode == Integer.MIN_VALUE) {
            return Math.min(Math.min(n, size), n2);
        }
        if (mode == 0) {
            return Math.min(n, n2);
        }
        if (mode != 1073741824) {
            return n;
        }
        return size;
    }
    
    private void updateImage() {
        if (this.arg.hasText()) {
            new XLETextTask(this).execute((Object[])new XLETextArg[] { this.arg.getArgText() });
            return;
        }
        if (this.arg.hasArgUri()) {
            TextureManager.Instance().bindToView(this.arg.getArgUri().getUri(), this, this.arg.getArgUri().getTextureBindingOption());
            return;
        }
        if (!this.arg.hasSrc()) {
            this.setImageDrawable((Drawable)null);
        }
    }
    
    public void clearImage() {
        this.arg = this.arg.cloneEmpty();
        this.updateImage();
    }
    
    protected void onMeasure(int resolveSizeAndState, int resolveSizeAndState2) {
        final int mode = View$MeasureSpec.getMode(resolveSizeAndState);
        final int mode2 = View$MeasureSpec.getMode(resolveSizeAndState2);
        final Drawable drawable = this.getDrawable();
        int n2 = 0;
        boolean b2 = false;
        float n5 = 0.0f;
        boolean b3 = false;
        int n6 = 0;
        Label_0196: {
            int n;
            if (drawable == null) {
                n = 0;
                n2 = 0;
            }
            else {
                final int intrinsicWidth = drawable.getIntrinsicWidth();
                final int intrinsicHeight = drawable.getIntrinsicHeight();
                if ((n = intrinsicWidth) <= 0) {
                    n = 1;
                }
                if ((n2 = intrinsicHeight) <= 0) {
                    n2 = 1;
                }
                if (this.adjustViewBounds) {
                    final boolean b = mode != 1073741824;
                    b2 = (mode2 != 1073741824);
                    final int size = View$MeasureSpec.getSize(resolveSizeAndState);
                    final int size2 = View$MeasureSpec.getSize(resolveSizeAndState2);
                    int n3;
                    int n4;
                    if (size > size2) {
                        n3 = n2 * size / n;
                        n4 = size;
                    }
                    else {
                        n4 = n * size2 / n2;
                        n3 = size2;
                    }
                    n5 = n4 / (float)n3;
                    b3 = b;
                    n6 = n4;
                    n2 = n3;
                    break Label_0196;
                }
            }
            b3 = false;
            b2 = false;
            n5 = 0.0f;
            n6 = n;
        }
        final int paddingLeft = this.getPaddingLeft();
        final int paddingRight = this.getPaddingRight();
        final int paddingTop = this.getPaddingTop();
        final int paddingBottom = this.getPaddingBottom();
        final boolean b4 = this.getContext().getApplicationInfo().targetSdkVersion <= 17;
        Label_0577: {
            if (!b3 && !b2) {
                final int max = Math.max(n6 + (paddingLeft + paddingRight), this.getSuggestedMinimumWidth());
                final int max2 = Math.max(n2 + (paddingTop + paddingBottom), this.getSuggestedMinimumHeight());
                resolveSizeAndState = resolveSizeAndState(max, resolveSizeAndState, 0);
                resolveSizeAndState2 = resolveSizeAndState(max2, resolveSizeAndState2, 0);
            }
            else {
                int resolveAdjustedSize = this.resolveAdjustedSize(n6 + paddingLeft + paddingRight, this.maxWidth, resolveSizeAndState);
                final int resolveAdjustedSize2 = this.resolveAdjustedSize(n2 + paddingTop + paddingBottom, this.maxHeight, resolveSizeAndState2);
                int n11 = 0;
                Label_0571: {
                    if (n5 != 0.0f) {
                        final float n7 = (float)(resolveAdjustedSize - paddingLeft - paddingRight);
                        final float n8 = (float)(resolveAdjustedSize2 - paddingTop - paddingBottom);
                        if (Math.abs(n7 / n8 - n5) > 1.0E-7) {
                            int n9 = resolveAdjustedSize;
                            boolean b5 = false;
                            Label_0473: {
                                if (b3) {
                                    final int n10 = (int)(n8 * n5) + paddingLeft + paddingRight;
                                    int resolveAdjustedSize3 = resolveAdjustedSize;
                                    if (!b2) {
                                        resolveAdjustedSize3 = resolveAdjustedSize;
                                        if (!b4) {
                                            resolveAdjustedSize3 = this.resolveAdjustedSize(n10, this.maxWidth, resolveSizeAndState);
                                        }
                                    }
                                    if (n10 <= (n9 = resolveAdjustedSize3)) {
                                        resolveSizeAndState = n10;
                                        b5 = true;
                                        break Label_0473;
                                    }
                                }
                                resolveSizeAndState = n9;
                                b5 = false;
                            }
                            resolveAdjustedSize = resolveSizeAndState;
                            n11 = resolveAdjustedSize2;
                            if (b5) {
                                break Label_0571;
                            }
                            resolveAdjustedSize = resolveSizeAndState;
                            n11 = resolveAdjustedSize2;
                            if (!b2) {
                                break Label_0571;
                            }
                            final int n12 = (int)((resolveSizeAndState - paddingLeft - paddingRight) / n5) + paddingTop + paddingBottom;
                            int resolveAdjustedSize4 = resolveAdjustedSize2;
                            if (!b3) {
                                resolveAdjustedSize4 = resolveAdjustedSize2;
                                if (!b4) {
                                    resolveAdjustedSize4 = this.resolveAdjustedSize(n12, this.maxHeight, resolveSizeAndState2);
                                }
                            }
                            resolveAdjustedSize = resolveSizeAndState;
                            if (n12 <= (n11 = resolveAdjustedSize4)) {
                                resolveSizeAndState2 = n12;
                                break Label_0577;
                            }
                            break Label_0571;
                        }
                    }
                    n11 = resolveAdjustedSize2;
                }
                resolveSizeAndState2 = n11;
                resolveSizeAndState = resolveAdjustedSize;
            }
        }
        this.setMeasuredDimension(resolveSizeAndState, resolveSizeAndState2);
    }
    
    public void setAdjustViewBounds(final boolean adjustViewBounds) {
        super.setAdjustViewBounds(this.adjustViewBounds = adjustViewBounds);
    }
    
    public void setImageURI2(final URI uri) {
        this.arg = this.arg.cloneWithUri(uri);
        this.updateImage();
    }
    
    public void setImageURI2(final URI uri, final int n, final int n2) {
        this.arg = this.arg.cloneWithUri(uri, n, n2);
        this.updateImage();
    }
    
    public void setMaxHeight(final int n) {
        super.setMaxHeight(n);
        this.maxHeight = n;
    }
    
    public void setMaxWidth(final int n) {
        super.setMaxWidth(n);
        this.maxWidth = n;
    }
    
    public void setText(final int n) {
        this.setText(this.getResources().getString(n));
    }
    
    public void setText(final String s) {
        if (!TextUtils.equals((CharSequence)s, (CharSequence)this.arg.getArgText().getText())) {
            this.arg = this.arg.cloneWithText(s);
            this.updateImage();
        }
    }
    
    public static class Params
    {
        private final XLETextArg argText;
        private final XLEURIArg argUri;
        private final boolean hasSrc;
        
        public Params() {
            this(new XLETextArg(new XLETextArg.Params()), null, false);
        }
        
        public Params(final XLETextArg xleTextArg, final XLEURIArg xleuriArg) {
            this(xleTextArg, xleuriArg, false);
        }
        
        private Params(final XLETextArg argText, final XLEURIArg argUri, final boolean hasSrc) {
            this.argText = argText;
            this.argUri = argUri;
            this.hasSrc = hasSrc;
        }
        
        public Params(final XLETextArg xleTextArg, final boolean b) {
            this(xleTextArg, null, b);
        }
        
        private Params cloneWithText(final String s) {
            return new Params(new XLETextArg(s, this.argText.getParams()), null, this.hasSrc);
        }
        
        public Params cloneEmpty() {
            return new Params(new XLETextArg(this.argText.getParams()), null, false);
        }
        
        public Params cloneWithSrc(final boolean b) {
            return new Params(new XLETextArg(this.argText.getParams()), null, b);
        }
        
        public Params cloneWithUri(final URI uri) {
            final XLEURIArg argUri = this.argUri;
            int errorResourceId = -1;
            int loadingResourceId;
            if (argUri == null) {
                loadingResourceId = -1;
            }
            else {
                loadingResourceId = argUri.getLoadingResourceId();
            }
            final XLEURIArg argUri2 = this.argUri;
            if (argUri2 != null) {
                errorResourceId = argUri2.getErrorResourceId();
            }
            return this.cloneWithUri(uri, loadingResourceId, errorResourceId);
        }
        
        public Params cloneWithUri(final URI uri, final int n, final int n2) {
            return new Params(new XLETextArg(this.argText.getParams()), new XLEURIArg(uri, n, n2), this.hasSrc);
        }
        
        public XLETextArg getArgText() {
            return this.argText;
        }
        
        public XLEURIArg getArgUri() {
            return this.argUri;
        }
        
        public boolean hasArgUri() {
            return this.argUri != null;
        }
        
        public boolean hasSrc() {
            return this.hasSrc;
        }
        
        public boolean hasText() {
            return this.argText.hasText();
        }
    }
    
    public enum TypefaceXml
    {
        MONOSPACE, 
        NORMAL, 
        SANS, 
        SERIF;
        
        public static TypefaceXml fromIndex(final int n) {
            final TypefaceXml[] values = values();
            if (n >= 0 && n < values.length) {
                return values[n];
            }
            return null;
        }
        
        public static Typeface typefaceFromIndex(int n) {
            final TypefaceXml fromIndex = fromIndex(n);
            if (fromIndex != null) {
                n = XLEUniversalImageView$2.$SwitchMap$com$microsoft$xbox$toolkit$ui$XLEUniversalImageView$TypefaceXml[fromIndex.ordinal()];
                if (n == 2) {
                    return Typeface.SANS_SERIF;
                }
                if (n == 3) {
                    return Typeface.SERIF;
                }
                if (n == 4) {
                    return Typeface.MONOSPACE;
                }
            }
            return null;
        }
    }
}
