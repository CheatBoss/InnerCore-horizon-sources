package com.bumptech.glide.load.resource.bitmap;

import com.bumptech.glide.load.engine.bitmap_recycle.*;
import android.util.*;
import android.media.*;
import android.annotation.*;
import android.graphics.*;
import android.os.*;

public final class TransformationUtils
{
    public static final int PAINT_FLAGS = 6;
    private static final String TAG = "TransformationUtils";
    
    private TransformationUtils() {
    }
    
    public static Bitmap centerCrop(Bitmap bitmap, final Bitmap bitmap2, final int n, final int n2) {
        if (bitmap2 == null) {
            return null;
        }
        if (bitmap2.getWidth() == n && bitmap2.getHeight() == n2) {
            return bitmap2;
        }
        float n3 = 0.0f;
        float n4 = 0.0f;
        final Matrix matrix = new Matrix();
        float n5;
        if (bitmap2.getWidth() * n2 > bitmap2.getHeight() * n) {
            n5 = n2 / (float)bitmap2.getHeight();
            n3 = (n - bitmap2.getWidth() * n5) * 0.5f;
        }
        else {
            n5 = n / (float)bitmap2.getWidth();
            n4 = (n2 - bitmap2.getHeight() * n5) * 0.5f;
        }
        matrix.setScale(n5, n5);
        matrix.postTranslate((float)(int)(n3 + 0.5f), (float)(int)(0.5f + n4));
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(n, n2, getSafeConfig(bitmap2));
        }
        setAlpha(bitmap2, bitmap);
        new Canvas(bitmap).drawBitmap(bitmap2, matrix, new Paint(6));
        return bitmap;
    }
    
    public static Bitmap fitCenter(final Bitmap bitmap, final BitmapPool bitmapPool, final int n, final int n2) {
        if (bitmap.getWidth() == n && bitmap.getHeight() == n2) {
            if (Log.isLoggable("TransformationUtils", 2)) {
                Log.v("TransformationUtils", "requested target size matches input, returning input");
            }
            return bitmap;
        }
        final float min = Math.min(n / (float)bitmap.getWidth(), n2 / (float)bitmap.getHeight());
        final int n3 = (int)(bitmap.getWidth() * min);
        final int n4 = (int)(bitmap.getHeight() * min);
        if (bitmap.getWidth() == n3 && bitmap.getHeight() == n4) {
            if (Log.isLoggable("TransformationUtils", 2)) {
                Log.v("TransformationUtils", "adjusted target size matches input, returning input");
            }
            return bitmap;
        }
        final Bitmap$Config safeConfig = getSafeConfig(bitmap);
        Bitmap bitmap2;
        if ((bitmap2 = bitmapPool.get(n3, n4, safeConfig)) == null) {
            bitmap2 = Bitmap.createBitmap(n3, n4, safeConfig);
        }
        setAlpha(bitmap, bitmap2);
        if (Log.isLoggable("TransformationUtils", 2)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("request: ");
            sb.append(n);
            sb.append("x");
            sb.append(n2);
            Log.v("TransformationUtils", sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("toFit:   ");
            sb2.append(bitmap.getWidth());
            sb2.append("x");
            sb2.append(bitmap.getHeight());
            Log.v("TransformationUtils", sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("toReuse: ");
            sb3.append(bitmap2.getWidth());
            sb3.append("x");
            sb3.append(bitmap2.getHeight());
            Log.v("TransformationUtils", sb3.toString());
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("minPct:   ");
            sb4.append(min);
            Log.v("TransformationUtils", sb4.toString());
        }
        final Canvas canvas = new Canvas(bitmap2);
        final Matrix matrix = new Matrix();
        matrix.setScale(min, min);
        canvas.drawBitmap(bitmap, matrix, new Paint(6));
        return bitmap2;
    }
    
    public static int getExifOrientationDegrees(final int n) {
        switch (n) {
            default: {
                return 0;
            }
            case 7:
            case 8: {
                return 270;
            }
            case 5:
            case 6: {
                return 90;
            }
            case 3:
            case 4: {
                return 180;
            }
        }
    }
    
    @Deprecated
    @TargetApi(5)
    public static int getOrientation(final String s) {
        try {
            return getExifOrientationDegrees(new ExifInterface(s).getAttributeInt("Orientation", 0));
        }
        catch (Exception ex) {
            if (Log.isLoggable("TransformationUtils", 6)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unable to get orientation for image with path=");
                sb.append(s);
                Log.e("TransformationUtils", sb.toString(), (Throwable)ex);
            }
            return 0;
        }
    }
    
    private static Bitmap$Config getSafeConfig(final Bitmap bitmap) {
        if (bitmap.getConfig() != null) {
            return bitmap.getConfig();
        }
        return Bitmap$Config.ARGB_8888;
    }
    
    static void initializeMatrixForRotation(final int n, final Matrix matrix) {
        switch (n) {
            default: {}
            case 8: {
                matrix.setRotate(-90.0f);
            }
            case 7: {
                matrix.setRotate(-90.0f);
                matrix.postScale(-1.0f, 1.0f);
            }
            case 6: {
                matrix.setRotate(90.0f);
            }
            case 5: {
                matrix.setRotate(90.0f);
                matrix.postScale(-1.0f, 1.0f);
            }
            case 4: {
                matrix.setRotate(180.0f);
                matrix.postScale(-1.0f, 1.0f);
            }
            case 3: {
                matrix.setRotate(180.0f);
            }
            case 2: {
                matrix.setScale(-1.0f, 1.0f);
            }
        }
    }
    
    @Deprecated
    public static Bitmap orientImage(final String s, final Bitmap bitmap) {
        return rotateImage(bitmap, getOrientation(s));
    }
    
    public static Bitmap rotateImage(final Bitmap bitmap, final int n) {
        Bitmap bitmap2 = bitmap;
        if (n != 0) {
            try {
                final Matrix matrix = new Matrix();
                matrix.setRotate((float)n);
                bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }
            catch (Exception ex) {
                bitmap2 = bitmap;
                if (Log.isLoggable("TransformationUtils", 6)) {
                    Log.e("TransformationUtils", "Exception when trying to orient image", (Throwable)ex);
                    return bitmap;
                }
            }
        }
        return bitmap2;
    }
    
    public static Bitmap rotateImageExif(final Bitmap bitmap, final BitmapPool bitmapPool, int round) {
        final Matrix matrix = new Matrix();
        initializeMatrixForRotation(round, matrix);
        if (matrix.isIdentity()) {
            return bitmap;
        }
        final RectF rectF = new RectF(0.0f, 0.0f, (float)bitmap.getWidth(), (float)bitmap.getHeight());
        matrix.mapRect(rectF);
        round = Math.round(rectF.width());
        final int round2 = Math.round(rectF.height());
        final Bitmap$Config safeConfig = getSafeConfig(bitmap);
        Bitmap bitmap2;
        if ((bitmap2 = bitmapPool.get(round, round2, safeConfig)) == null) {
            bitmap2 = Bitmap.createBitmap(round, round2, safeConfig);
        }
        matrix.postTranslate(-rectF.left, -rectF.top);
        new Canvas(bitmap2).drawBitmap(bitmap, matrix, new Paint(6));
        return bitmap2;
    }
    
    @TargetApi(12)
    public static void setAlpha(final Bitmap bitmap, final Bitmap bitmap2) {
        if (Build$VERSION.SDK_INT >= 12 && bitmap2 != null) {
            bitmap2.setHasAlpha(bitmap.hasAlpha());
        }
    }
}
