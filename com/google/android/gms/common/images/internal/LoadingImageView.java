package com.google.android.gms.common.images.internal;

import android.widget.*;
import com.google.android.gms.common.images.*;
import android.net.*;
import android.content.*;
import android.util.*;
import com.google.android.gms.base.*;
import com.google.android.gms.common.util.*;
import com.google.android.gms.common.internal.*;
import android.content.res.*;
import android.graphics.*;

public final class LoadingImageView extends ImageView
{
    private ImageManager.OnImageLoadedListener mOnImageLoadedListener;
    private int mPostProcessingFlags;
    private boolean zzpl;
    private boolean zzpm;
    private Uri zzqn;
    private int zzqo;
    private boolean zzqp;
    private int zzqq;
    private ClipPathProvider zzqr;
    private int zzqs;
    private float zzqt;
    
    public LoadingImageView(final Context context) {
        this(context, null, 0);
    }
    
    public LoadingImageView(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public LoadingImageView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.zzqo = 0;
        this.zzpl = true;
        this.zzpm = false;
        this.zzqp = false;
        this.zzqq = 0;
        this.mPostProcessingFlags = 0;
        this.zzqs = 0;
        this.zzqt = 1.0f;
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R$styleable.LoadingImageView);
        this.zzqs = obtainStyledAttributes.getInt(R$styleable.LoadingImageView_imageAspectRatioAdjust, 0);
        this.zzqt = obtainStyledAttributes.getFloat(R$styleable.LoadingImageView_imageAspectRatio, 1.0f);
        this.setCircleCropEnabled(obtainStyledAttributes.getBoolean(R$styleable.LoadingImageView_circleCrop, false));
        obtainStyledAttributes.recycle();
    }
    
    protected final void onDraw(final Canvas canvas) {
        final ClipPathProvider zzqr = this.zzqr;
        if (zzqr != null) {
            canvas.clipPath(zzqr.getClipPath(this.getWidth(), this.getHeight()));
        }
        super.onDraw(canvas);
        final int zzqq = this.zzqq;
        if (zzqq != 0) {
            canvas.drawColor(zzqq);
        }
    }
    
    protected final void onMeasure(int n, int measuredHeight) {
        super.onMeasure(n, measuredHeight);
        n = this.zzqs;
        if (n != 1) {
            if (n != 2) {
                return;
            }
            n = this.getMeasuredWidth();
            measuredHeight = (int)(n / this.zzqt);
        }
        else {
            measuredHeight = this.getMeasuredHeight();
            n = (int)(measuredHeight * this.zzqt);
        }
        this.setMeasuredDimension(n, measuredHeight);
    }
    
    public final void setCircleCropEnabled(final boolean b) {
        int mPostProcessingFlags;
        if (b) {
            mPostProcessingFlags = (this.mPostProcessingFlags | 0x1);
        }
        else {
            mPostProcessingFlags = (this.mPostProcessingFlags & 0xFFFFFFFE);
        }
        this.mPostProcessingFlags = mPostProcessingFlags;
    }
    
    public final void setClipPathProvider(final ClipPathProvider zzqr) {
        this.zzqr = zzqr;
        if (!PlatformVersion.isAtLeastJellyBean()) {
            this.setLayerType(1, (Paint)null);
        }
    }
    
    public final void setCrossFadeAlwaysEnabled(final boolean zzpm) {
        this.zzpm = zzpm;
    }
    
    public final void setCrossFadeEnabled(final boolean zzpl) {
        this.zzpl = zzpl;
    }
    
    public final void setImageAspectRatioAdjust(final int zzqs, final float zzqt) {
        final boolean b = false;
        Asserts.checkState(zzqs == 0 || zzqs == 1 || zzqs == 2);
        boolean b2 = b;
        if (zzqt > 0.0f) {
            b2 = true;
        }
        Asserts.checkState(b2);
        this.zzqs = zzqs;
        this.zzqt = zzqt;
        this.requestLayout();
    }
    
    public final void setLoadedNoDataPlaceholderResId(final int zzqo) {
        this.zzqo = zzqo;
    }
    
    public final void setLoadedUri(final Uri zzqn) {
        this.zzqn = zzqn;
    }
    
    public final void setOnImageLoadedListener(final ImageManager.OnImageLoadedListener mOnImageLoadedListener) {
        this.mOnImageLoadedListener = mOnImageLoadedListener;
    }
    
    public final void setTintColor(final int zzqq) {
        this.zzqq = zzqq;
        ColorFilter color_FILTER_BW;
        if (zzqq != 0) {
            color_FILTER_BW = ColorFilters.COLOR_FILTER_BW;
        }
        else {
            color_FILTER_BW = null;
        }
        this.setColorFilter(color_FILTER_BW);
        this.invalidate();
    }
    
    public final void setTintColorId(int color) {
        Label_0024: {
            if (color > 0) {
                final Resources resources = this.getResources();
                if (resources != null) {
                    color = resources.getColor(color);
                    break Label_0024;
                }
            }
            color = 0;
        }
        this.setTintColor(color);
    }
    
    public interface ClipPathProvider
    {
        Path getClipPath(final int p0, final int p1);
    }
}
