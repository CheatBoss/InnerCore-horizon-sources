package com.microsoft.xbox.toolkit.ui;

import android.content.*;
import android.util.*;
import android.graphics.*;
import android.graphics.drawable.*;

public class XLERoundedUniversalImageView extends XLEUniversalImageView
{
    public XLERoundedUniversalImageView(final Context context) {
        super(context, new Params());
    }
    
    public XLERoundedUniversalImageView(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public XLERoundedUniversalImageView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    public static Bitmap getRoundedCroppedBitmap(Bitmap bitmap, final int n) {
        Bitmap scaledBitmap = null;
        Label_0026: {
            if (bitmap.getWidth() == n) {
                scaledBitmap = bitmap;
                if (bitmap.getHeight() == n) {
                    break Label_0026;
                }
            }
            scaledBitmap = Bitmap.createScaledBitmap(bitmap, n, n, false);
        }
        bitmap = Bitmap.createBitmap(scaledBitmap.getWidth(), scaledBitmap.getHeight(), Bitmap$Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight());
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(scaledBitmap.getWidth() / 2 + 0.7f, scaledBitmap.getHeight() / 2 + 0.7f, scaledBitmap.getWidth() / 2 + 0.1f, paint);
        paint.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff$Mode.SRC_IN));
        canvas.drawBitmap(scaledBitmap, rect, rect, paint);
        return bitmap;
    }
    
    protected void onDraw(final Canvas canvas) {
        final Drawable drawable = this.getDrawable();
        if (drawable == null) {
            return;
        }
        if (this.getWidth() != 0) {
            if (this.getHeight() == 0) {
                return;
            }
            final Bitmap copy = ((BitmapDrawable)drawable).getBitmap().copy(Bitmap$Config.ARGB_8888, true);
            final int width = this.getWidth();
            this.getHeight();
            canvas.drawBitmap(getRoundedCroppedBitmap(copy, width), 0.0f, 0.0f, (Paint)null);
        }
    }
}
