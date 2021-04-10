package com.microsoft.xbox.toolkit.ui;

import android.graphics.*;

public class TextureResizer
{
    public static Bitmap createScaledBitmap8888(final Bitmap bitmap, int n, int n2, final boolean filterBitmap) {
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        final float n3 = (float)n;
        final float n4 = (float)width;
        final float n5 = n3 / n4;
        final float n6 = (float)n2;
        final float n7 = (float)height;
        final float n8 = n6 / n7;
        final Matrix matrix = new Matrix();
        matrix.setScale(n5, n8);
        n = width + 0;
        if (n > bitmap.getWidth()) {
            throw new IllegalArgumentException("x + width must be <= bitmap.width()");
        }
        n2 = height + 0;
        if (n2 > bitmap.getHeight()) {
            throw new IllegalArgumentException("y + height must be <= bitmap.height()");
        }
        if (!bitmap.isMutable() && width == bitmap.getWidth() && height == bitmap.getHeight() && matrix.isIdentity()) {
            return bitmap;
        }
        final Canvas canvas = new Canvas();
        final Rect rect = new Rect(0, 0, n, n2);
        final RectF rectF = new RectF(0.0f, 0.0f, n4, n7);
        Bitmap bitmap2;
        Paint paint;
        if (matrix.isIdentity()) {
            bitmap2 = Bitmap.createBitmap(width, height, Bitmap$Config.ARGB_8888);
            paint = null;
        }
        else {
            if (!bitmap.hasAlpha() && matrix.rectStaysRect()) {
                n = 0;
            }
            else {
                n = 1;
            }
            final RectF rectF2 = new RectF();
            matrix.mapRect(rectF2, rectF);
            bitmap2 = Bitmap.createBitmap(Math.round(rectF2.width()), Math.round(rectF2.height()), Bitmap$Config.ARGB_8888);
            if (n != 0) {
                bitmap2.eraseColor(0);
            }
            canvas.translate(-rectF2.left, -rectF2.top);
            canvas.concat(matrix);
            paint = new Paint();
            paint.setFilterBitmap(filterBitmap);
            if (!matrix.rectStaysRect()) {
                paint.setAntiAlias(true);
            }
        }
        bitmap2.setDensity(bitmap.getDensity());
        canvas.setBitmap(bitmap2);
        canvas.drawBitmap(bitmap, rect, rectF, paint);
        return bitmap2;
    }
}
