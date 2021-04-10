package com.microsoft.xbox.idp.toolkit;

import android.widget.*;
import android.content.*;
import android.util.*;
import android.graphics.*;
import android.graphics.drawable.*;

public class CircleImageView extends ImageView
{
    public CircleImageView(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public CircleImageView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    private Bitmap createBitmap(final Drawable drawable) {
        final Bitmap bitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap$Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
    
    private Bitmap createRoundBitmap(final Bitmap bitmap, final int n) {
        final Bitmap bitmap2 = Bitmap.createBitmap(n, n, Bitmap$Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap2);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(bitmap.getWidth() / 2 + 0.7f, bitmap.getHeight() / 2 + 0.7f, bitmap.getWidth() / 2 + 0.1f, paint);
        paint.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff$Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return bitmap2;
    }
    
    private void drawBitmap(final Canvas canvas, Bitmap scaledBitmap) {
        if (scaledBitmap != null && !scaledBitmap.isRecycled()) {
            final int min = Math.min(this.getWidth(), this.getHeight());
            if (scaledBitmap.getWidth() == min && scaledBitmap.getHeight() == min) {
                this.drawRoundBitmap(canvas, scaledBitmap, min);
                return;
            }
            scaledBitmap = Bitmap.createScaledBitmap(scaledBitmap, min, min, false);
            try {
                this.drawRoundBitmap(canvas, scaledBitmap, min);
            }
            finally {
                scaledBitmap.recycle();
            }
        }
    }
    
    private void drawRoundBitmap(final Canvas canvas, Bitmap roundBitmap, final int n) {
        roundBitmap = this.createRoundBitmap(roundBitmap, n);
        try {
            canvas.drawBitmap(roundBitmap, 0.0f, 0.0f, (Paint)null);
        }
        finally {
            roundBitmap.recycle();
        }
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.getWidth() != 0) {
            if (this.getHeight() == 0) {
                return;
            }
            final Drawable drawable = this.getDrawable();
            if (drawable == null) {
                return;
            }
            if (drawable instanceof BitmapDrawable) {
                this.drawBitmap(canvas, ((BitmapDrawable)drawable).getBitmap());
                return;
            }
            final Bitmap bitmap = this.createBitmap(drawable);
            try {
                this.drawBitmap(canvas, bitmap);
            }
            finally {
                bitmap.recycle();
            }
        }
    }
}
