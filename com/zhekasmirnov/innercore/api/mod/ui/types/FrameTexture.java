package com.zhekasmirnov.innercore.api.mod.ui.types;

import android.graphics.*;

public class FrameTexture
{
    public static final int CORNER_BOTTOM_LEFT = 6;
    public static final int CORNER_BOTTOM_RIGHT = 7;
    public static final int CORNER_TOP_LEFT = 4;
    public static final int CORNER_TOP_RIGHT = 5;
    public static final int SIDE_BOTTOM = 3;
    public static final int SIDE_LEFT = 0;
    public static final int SIDE_RIGHT = 1;
    public static final int SIDE_TOP = 2;
    private static final Paint paint;
    private int[] majorPadding;
    private int[] minorPadding;
    private Rect[] rects;
    private Bitmap[] sides;
    private Bitmap source;
    
    static {
        (paint = new Paint()).setFilterBitmap(false);
        FrameTexture.paint.setDither(false);
        FrameTexture.paint.setAntiAlias(false);
    }
    
    public FrameTexture(final Bitmap source) {
        this.sides = new Bitmap[4];
        this.minorPadding = new int[4];
        this.majorPadding = new int[4];
        this.rects = new Rect[8];
        this.source = source;
        this.validateSource();
        this.initPadding();
        this.splitIntoSides();
        this.initRegions();
    }
    
    private static float convertCoord(final float n, final float n2, final float n3, final float n4, final int n5) {
        if (n < n5 / 2.0f) {
            return n * n4 + n2;
        }
        return (n - n5) * n4 + n3;
    }
    
    private static RectF convertRect(final Rect rect, final RectF rectF, final int n, final float n2) {
        return new RectF(convertCoord((float)rect.left, rectF.left, rectF.right, n2, n), convertCoord((float)rect.top, rectF.top, rectF.bottom, n2, n), convertCoord((float)rect.right, rectF.left, rectF.right, n2, n), convertCoord((float)rect.bottom, rectF.top, rectF.bottom, n2, n));
    }
    
    private void initPadding() {
        final int width = this.source.getWidth();
        final int n = width / 2;
        for (int i = 0; i < 4; ++i) {
            int n2 = 1;
            int n3;
            if (i == 1) {
                n3 = width - 1;
            }
            else {
                n3 = 0;
            }
            int n4;
            if (i == 3) {
                n4 = width - 1;
            }
            else {
                n4 = 0;
            }
            int n5;
            if (i == 0) {
                n5 = 1;
            }
            else if (i == 1) {
                n5 = -1;
            }
            else {
                n5 = 0;
            }
            if (i != 2) {
                if (i == 3) {
                    n2 = -1;
                }
                else {
                    n2 = 0;
                }
            }
            boolean b = true;
            int n6 = 0;
            int n7 = 0;
            int n9;
            int n11;
            for (int j = 0; j < width; ++j, n7 = n9, n6 = n11) {
                int n8 = 0;
                while (true) {
                    n9 = n7;
                    if (n8 >= n) {
                        break;
                    }
                    if (this.source.getPixel(Math.abs(n2) * j + n5 * n8 + n3, Math.abs(n5) * j + n2 * n8 + n4) != 0) {
                        if ((n9 = n7) < n8) {
                            n9 = n8;
                            break;
                        }
                        break;
                    }
                    else {
                        ++n8;
                    }
                }
                int n10 = n - 1;
                while (true) {
                    n11 = n6;
                    if (n10 < 0) {
                        break;
                    }
                    if (this.source.getPixel(Math.abs(n2) * j + n5 * n10 + n3, Math.abs(n5) * j + n2 * n10 + n4) != 0) {
                        if ((n11 = n6) >= n10 + 1) {
                            break;
                        }
                        n11 = n6;
                        if (n10 + 1 != n) {
                            n11 = n10 + 1;
                            break;
                        }
                        break;
                    }
                    else {
                        if (n10 + 1 == n) {
                            b = false;
                        }
                        --n10;
                    }
                }
            }
            this.minorPadding[i] = n7;
            final int[] majorPadding = this.majorPadding;
            if (b) {
                n6 = n - 1;
            }
            majorPadding[i] = n6;
        }
    }
    
    private void initRegions() {
        final int[] majorPadding = this.majorPadding;
        final int width = this.source.getWidth();
        this.rects[2] = new Rect(majorPadding[0], 0, width - majorPadding[1], majorPadding[2]);
        this.rects[3] = new Rect(majorPadding[0], width - majorPadding[3], width - majorPadding[1], width);
        this.rects[0] = new Rect(0, majorPadding[2], majorPadding[0], width - majorPadding[3]);
        this.rects[1] = new Rect(width - majorPadding[1], majorPadding[2], width, width - majorPadding[3]);
        this.rects[4] = new Rect(0, 0, majorPadding[0], majorPadding[2]);
        this.rects[5] = new Rect(width - majorPadding[1], 0, width, majorPadding[2]);
        this.rects[6] = new Rect(0, width - majorPadding[3], majorPadding[0], width);
        this.rects[7] = new Rect(width - majorPadding[1], width - majorPadding[3], width, width);
    }
    
    private static boolean isRegionEnabled(final boolean[] array, final int n) {
        final boolean b = true;
        final boolean b2 = true;
        final boolean b3 = true;
        boolean b4 = true;
        if (array == null) {
            return true;
        }
        if (n < 4) {
            return array[n];
        }
        switch (n) {
            default: {
                return true;
            }
            case 7: {
                if (!array[1]) {
                    if (array[3]) {
                        return true;
                    }
                    b4 = false;
                }
                return b4;
            }
            case 6: {
                boolean b5 = b;
                if (!array[0]) {
                    if (array[3]) {
                        return true;
                    }
                    b5 = false;
                }
                return b5;
            }
            case 5: {
                boolean b6 = b2;
                if (!array[1]) {
                    if (array[2]) {
                        return true;
                    }
                    b6 = false;
                }
                return b6;
            }
            case 4: {
                boolean b7 = b3;
                if (!array[0]) {
                    if (array[2]) {
                        return true;
                    }
                    b7 = false;
                }
                return b7;
            }
        }
    }
    
    private void splitIntoSides() {
        final int width = this.source.getWidth();
        for (int i = 0; i < 4; ++i) {
            if (this.sides[i] != null) {
                this.sides[i].recycle();
            }
            this.sides[i] = Bitmap.createBitmap(width, width, Bitmap$Config.ARGB_8888);
        }
        for (int j = 0; j < width; ++j) {
            for (int k = 0; k < width; ++k) {
                final int pixel = this.source.getPixel(j, k);
                final int n = this.majorPadding[0];
                int n2 = 1;
                if (j < n) {
                    n2 = 0;
                }
                else if (j <= width - 1 - this.majorPadding[1]) {
                    n2 = -1;
                }
                final int[] majorPadding = this.majorPadding;
                int n3 = 2;
                if (k >= majorPadding[2]) {
                    if (k > width - 1 - this.majorPadding[3]) {
                        n3 = 3;
                    }
                    else {
                        n3 = -1;
                    }
                }
                if (n2 != -1) {
                    this.sides[n2].setPixel(j, k, pixel);
                }
                if (n3 != -1) {
                    this.sides[n3].setPixel(j, k, pixel);
                }
            }
        }
    }
    
    private void validateSource() {
        if (this.source.getHeight() != this.source.getWidth() || this.source.getWidth() > 128) {
            final int min = Math.min(128, this.source.getWidth());
            this.source = Bitmap.createScaledBitmap(this.source, min, min, false);
        }
    }
    
    public void draw(final Canvas canvas, final RectF rectF, final float n, int i, final boolean[] array) {
        final int width = this.source.getWidth();
        final int n2 = 0;
        if (i != 0) {
            final Paint paint = new Paint();
            paint.setColor(i);
            canvas.drawRect(convertRect(new Rect(this.minorPadding[0], this.minorPadding[2], width - this.minorPadding[1], width - this.minorPadding[3]), rectF, width, n), paint);
        }
        for (i = n2; i < 8; ++i) {
            if (isRegionEnabled(array, i)) {
                canvas.drawBitmap(this.source, this.rects[i], convertRect(this.rects[i], rectF, width, n), FrameTexture.paint);
            }
        }
    }
    
    public Bitmap expand(final int n, final int n2, final int n3) {
        return this.expand(n, n2, n3, new boolean[] { true, true, true, true });
    }
    
    public Bitmap expand(final int n, final int n2, int i, final boolean[] array) {
        final int n3 = this.source.getWidth() / 2;
        final Bitmap bitmap = Bitmap.createBitmap(n, n2, Bitmap$Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        final int n4 = 0;
        if (i != 0) {
            final Paint paint = new Paint();
            paint.setColor(i);
            float n5;
            if (array[0]) {
                n5 = (float)this.minorPadding[0];
            }
            else {
                n5 = 0.0f;
            }
            float n6;
            if (array[2]) {
                n6 = (float)this.minorPadding[2];
            }
            else {
                n6 = 0.0f;
            }
            if (array[1]) {
                i = this.minorPadding[1];
            }
            else {
                i = 0;
            }
            final float n7 = (float)(n - i);
            if (array[3]) {
                i = this.minorPadding[3];
            }
            else {
                i = 0;
            }
            canvas.drawRect(n5, n6, n7, (float)(n2 - i), paint);
        }
        int n8;
        Bitmap expandSide;
        for (i = n4; i < 4; ++i) {
            if (array[i]) {
                if (i > 1) {
                    n8 = n;
                }
                else {
                    n8 = n2;
                }
                expandSide = this.expandSide(i, n8);
                switch (i) {
                    case 3: {
                        canvas.drawBitmap(expandSide, 0.0f, (float)(n2 - n3), (Paint)null);
                        break;
                    }
                    case 1: {
                        canvas.drawBitmap(expandSide, (float)(n - n3), 0.0f, (Paint)null);
                        break;
                    }
                    case 0:
                    case 2: {
                        canvas.drawBitmap(expandSide, 0.0f, 0.0f, (Paint)null);
                        break;
                    }
                }
                expandSide.recycle();
            }
        }
        return bitmap;
    }
    
    public Bitmap expandAndScale(final float n, final float n2, final float n3, final int n4) {
        return this.expandAndScale(n, n2, n3, n4, new boolean[] { true, true, true, true });
    }
    
    public Bitmap expandAndScale(final float n, final float n2, final float n3, final int n4, final boolean[] array) {
        final Bitmap expand = this.expand((int)(n / n3), (int)(n2 / n3), n4, array);
        final Bitmap scaledBitmap = Bitmap.createScaledBitmap(expand, (int)n, (int)n2, false);
        expand.recycle();
        return scaledBitmap;
    }
    
    public Bitmap expandSide(int n, final int n2) {
        final int width = this.source.getWidth();
        final int n3 = width / 2;
        final Bitmap sideSource = this.getSideSource(n);
        if (n == 2 || n == 3) {
            final Bitmap bitmap = Bitmap.createBitmap(n2, n3, Bitmap$Config.ARGB_8888);
            final Canvas canvas = new Canvas(bitmap);
            int n4;
            if (n == 3) {
                n4 = n3;
            }
            else {
                n4 = 0;
            }
            final Bitmap bitmap2 = Bitmap.createBitmap(sideSource, 0, n4, n3, n3);
            int n5;
            if (n == 3) {
                n5 = n3;
            }
            else {
                n5 = 0;
            }
            final Bitmap bitmap3 = Bitmap.createBitmap(sideSource, n3, n5, n3, n3);
            canvas.drawBitmap(bitmap2, 0.0f, 0.0f, (Paint)null);
            canvas.drawBitmap(bitmap3, (float)(n2 - n3), 0.0f, (Paint)null);
            bitmap2.recycle();
            bitmap3.recycle();
            if (n2 > width) {
                if (n == 3) {
                    n = n3;
                }
                else {
                    n = 0;
                }
                final Bitmap bitmap4 = Bitmap.createBitmap(sideSource, n3, n, 1, n3);
                final Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap4, n2 - width, n3, false);
                canvas.drawBitmap(scaledBitmap, (float)n3, 0.0f, (Paint)null);
                bitmap4.recycle();
                scaledBitmap.recycle();
            }
            return bitmap;
        }
        if (n != 0 && n != 1) {
            return null;
        }
        final Bitmap bitmap5 = Bitmap.createBitmap(n3, n2, Bitmap$Config.ARGB_8888);
        final Canvas canvas2 = new Canvas(bitmap5);
        int n6;
        if (n == 1) {
            n6 = n3;
        }
        else {
            n6 = 0;
        }
        final Bitmap bitmap6 = Bitmap.createBitmap(sideSource, n6, 0, n3, n3);
        int n7;
        if (n == 1) {
            n7 = n3;
        }
        else {
            n7 = 0;
        }
        final Bitmap bitmap7 = Bitmap.createBitmap(sideSource, n7, n3, n3, n3);
        canvas2.drawBitmap(bitmap6, 0.0f, 0.0f, (Paint)null);
        canvas2.drawBitmap(bitmap7, 0.0f, (float)(n2 - n3), (Paint)null);
        bitmap6.recycle();
        bitmap7.recycle();
        if (n2 > width) {
            if (n == 1) {
                n = n3;
            }
            else {
                n = 0;
            }
            final Bitmap bitmap8 = Bitmap.createBitmap(sideSource, n, n3, n3, 1);
            final Bitmap scaledBitmap2 = Bitmap.createScaledBitmap(bitmap8, n3, n2 - width, false);
            canvas2.drawBitmap(scaledBitmap2, 0.0f, (float)n3, (Paint)null);
            bitmap8.recycle();
            scaledBitmap2.recycle();
        }
        return bitmap5;
    }
    
    public int getCentralColor() {
        if (this.source == null) {
            return 0;
        }
        return this.source.getPixel(this.source.getWidth() / 2, this.source.getHeight() / 2);
    }
    
    public Bitmap getSideSource(final int n) {
        return this.sides[n];
    }
    
    public Bitmap getSource() {
        return this.source;
    }
}
