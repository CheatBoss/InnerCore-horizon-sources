package com.microsoft.xbox.toolkit.ui;

import android.os.*;
import java.lang.ref.*;
import android.widget.*;
import android.text.*;
import android.graphics.*;

public class XLETextTask extends AsyncTask<XLETextArg, Void, Bitmap>
{
    private static final String TAG;
    private final WeakReference<ImageView> img;
    private final int imgHeight;
    private final int imgWidth;
    
    static {
        TAG = XLETextTask.class.getSimpleName();
    }
    
    public XLETextTask(final ImageView imageView) {
        this.img = new WeakReference<ImageView>(imageView);
        this.imgWidth = imageView.getWidth();
        this.imgHeight = imageView.getHeight();
    }
    
    protected Bitmap doInBackground(final XLETextArg... array) {
        if (array.length > 0) {
            final XLETextArg xleTextArg = array[0];
            final XLETextArg.Params params = xleTextArg.getParams();
            final String text = xleTextArg.getText();
            final TextPaint textPaint = new TextPaint();
            textPaint.setTextSize(params.getTextSize());
            textPaint.setAntiAlias(true);
            textPaint.setColor(params.getColor());
            textPaint.setTypeface(params.getTypeface());
            final int round = Math.round(textPaint.measureText(text));
            final int round2 = Math.round(textPaint.descent() - textPaint.ascent());
            int max;
            int max2;
            if (params.isAdjustForImageSize()) {
                max = Math.max(round, this.imgWidth);
                max2 = Math.max(round2, this.imgHeight);
            }
            else {
                max = round;
                max2 = round2;
            }
            int n = max;
            int n2 = max2;
            if (params.hasTextAspectRatio()) {
                final float floatValue = params.getTextAspectRatio();
                n = max;
                n2 = max2;
                if (floatValue > 0.0f) {
                    final float n3 = (float)max2;
                    final float n4 = max * floatValue;
                    if (n3 > n4) {
                        n = (int)(n3 / floatValue);
                        n2 = max2;
                    }
                    else {
                        n2 = (int)n4;
                        n = max;
                    }
                }
            }
            final Bitmap bitmap = Bitmap.createBitmap(n, n2, Bitmap$Config.ARGB_8888);
            if (params.hasEraseColor()) {
                bitmap.eraseColor(params.getEraseColor());
            }
            new Canvas(bitmap).drawText(text, (float)(Math.max(0, n - round) / 2 + 0), -textPaint.ascent() + Math.max(0, n2 - round2) / 2, (Paint)textPaint);
            return bitmap;
        }
        return null;
    }
    
    protected void onPostExecute(final Bitmap imageBitmap) {
        final ImageView imageView = this.img.get();
        if (imageView != null) {
            imageView.setImageBitmap(imageBitmap);
        }
    }
}
