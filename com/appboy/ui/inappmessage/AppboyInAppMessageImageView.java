package com.appboy.ui.inappmessage;

import com.appboy.support.*;
import android.content.*;
import android.util.*;
import android.graphics.*;
import com.appboy.enums.inappmessage.*;
import android.widget.*;

public class AppboyInAppMessageImageView extends ImageView implements IInAppMessageImageView
{
    private static final String TAG;
    private Path mClipPath;
    private float[] mInAppRadii;
    private RectF mRect;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(AppboyInAppMessageImageView.class);
    }
    
    public AppboyInAppMessageImageView(final Context context, final AttributeSet set) {
        super(context, set);
        this.mClipPath = new Path();
        this.mRect = new RectF();
    }
    
    boolean clipCanvasToPath(final Canvas canvas, final int n, final int n2) {
        if (this.mInAppRadii != null) {
            try {
                this.mClipPath.reset();
                this.mRect.set(0.0f, 0.0f, (float)n, (float)n2);
                this.mClipPath.addRoundRect(this.mRect, this.mInAppRadii, Path$Direction.CW);
                canvas.clipPath(this.mClipPath);
                return true;
            }
            catch (Exception ex) {
                AppboyLogger.e(AppboyInAppMessageImageView.TAG, "Encountered exception while trying to clip in-app message image", ex);
            }
        }
        return false;
    }
    
    Path getClipPath() {
        return this.mClipPath;
    }
    
    float[] getInAppRadii() {
        return this.mInAppRadii;
    }
    
    RectF getRectf() {
        return this.mRect;
    }
    
    protected void onDraw(final Canvas canvas) {
        this.clipCanvasToPath(canvas, this.getWidth(), this.getHeight());
        super.onDraw(canvas);
    }
    
    void setClipPath(final Path mClipPath) {
        this.mClipPath = mClipPath;
    }
    
    public void setCornersRadiiPx(final float n, final float n2, final float n3, final float n4) {
        this.mInAppRadii = new float[] { n, n, n2, n2, n3, n3, n4, n4 };
    }
    
    public void setCornersRadiusPx(final float n) {
        this.setCornersRadiiPx(n, n, n, n);
    }
    
    public void setInAppMessageImageCropType(final CropType cropType) {
        if (cropType.equals(CropType.FIT_CENTER)) {
            this.setScaleType(ImageView$ScaleType.FIT_CENTER);
            return;
        }
        if (cropType.equals(CropType.CENTER_CROP)) {
            this.setScaleType(ImageView$ScaleType.CENTER_CROP);
        }
    }
    
    void setRectf(final RectF mRect) {
        this.mRect = mRect;
    }
}
