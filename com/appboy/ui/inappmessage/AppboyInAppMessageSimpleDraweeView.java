package com.appboy.ui.inappmessage;

import com.facebook.drawee.view.*;
import com.appboy.support.*;
import android.content.*;
import android.util.*;
import android.graphics.*;
import com.appboy.enums.inappmessage.*;
import com.facebook.drawee.generic.*;
import com.facebook.drawee.drawable.*;

public class AppboyInAppMessageSimpleDraweeView extends SimpleDraweeView implements IInAppMessageImageView
{
    private static final String TAG;
    private Path mClipPath;
    private float[] mInAppRadii;
    private RectF mRect;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(AppboyInAppMessageSimpleDraweeView.class);
    }
    
    public AppboyInAppMessageSimpleDraweeView(final Context context, final AttributeSet set) {
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
                AppboyLogger.e(AppboyInAppMessageSimpleDraweeView.TAG, "Encountered exception while trying to clip in-app message image", ex);
            }
        }
        return false;
    }
    
    protected void onDraw(final Canvas canvas) {
        this.clipCanvasToPath(canvas, this.getWidth(), this.getHeight());
        super.onDraw(canvas);
    }
    
    public void setCornersRadiiPx(final float n, final float n2, final float n3, final float n4) {
        this.mInAppRadii = new float[] { n, n, n2, n2, n3, n3, n4, n4 };
    }
    
    public void setCornersRadiusPx(final float n) {
        this.setCornersRadiiPx(n, n, n, n);
    }
    
    public void setInAppMessageImageCropType(final CropType cropType) {
        if (cropType.equals(CropType.FIT_CENTER)) {
            ((GenericDraweeHierarchy)this.getHierarchy()).setActualImageScaleType(ScalingUtils$ScaleType.FIT_CENTER);
            return;
        }
        if (cropType.equals(CropType.CENTER_CROP)) {
            ((GenericDraweeHierarchy)this.getHierarchy()).setActualImageScaleType(ScalingUtils$ScaleType.CENTER_CROP);
        }
    }
}
