package androidx.core.app;

import android.graphics.drawable.*;
import android.view.*;
import android.graphics.*;
import android.os.*;
import android.widget.*;
import android.content.*;
import java.util.*;

public abstract class SharedElementCallback
{
    private static final String BUNDLE_SNAPSHOT_BITMAP = "sharedElement:snapshot:bitmap";
    private static final String BUNDLE_SNAPSHOT_IMAGE_MATRIX = "sharedElement:snapshot:imageMatrix";
    private static final String BUNDLE_SNAPSHOT_IMAGE_SCALETYPE = "sharedElement:snapshot:imageScaleType";
    private static final int MAX_IMAGE_SIZE = 1048576;
    private Matrix mTempMatrix;
    
    private static Bitmap createDrawableBitmap(final Drawable drawable) {
        final int intrinsicWidth = drawable.getIntrinsicWidth();
        final int intrinsicHeight = drawable.getIntrinsicHeight();
        if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
            return null;
        }
        final float min = Math.min(1.0f, 1048576.0f / (intrinsicWidth * intrinsicHeight));
        if (drawable instanceof BitmapDrawable && min == 1.0f) {
            return ((BitmapDrawable)drawable).getBitmap();
        }
        final int n = (int)(intrinsicWidth * min);
        final int n2 = (int)(intrinsicHeight * min);
        final Bitmap bitmap = Bitmap.createBitmap(n, n2, Bitmap$Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        final Rect bounds = drawable.getBounds();
        final int left = bounds.left;
        final int top = bounds.top;
        final int right = bounds.right;
        final int bottom = bounds.bottom;
        drawable.setBounds(0, 0, n, n2);
        drawable.draw(canvas);
        drawable.setBounds(left, top, right, bottom);
        return bitmap;
    }
    
    public Parcelable onCaptureSharedElementSnapshot(final View view, Matrix imageMatrix, final RectF rectF) {
        if (view instanceof ImageView) {
            final ImageView imageView = (ImageView)view;
            final Drawable drawable = imageView.getDrawable();
            final Drawable background = imageView.getBackground();
            if (drawable != null && background == null) {
                final Bitmap drawableBitmap = createDrawableBitmap(drawable);
                if (drawableBitmap != null) {
                    final Bundle bundle = new Bundle();
                    bundle.putParcelable("sharedElement:snapshot:bitmap", (Parcelable)drawableBitmap);
                    bundle.putString("sharedElement:snapshot:imageScaleType", imageView.getScaleType().toString());
                    if (imageView.getScaleType() == ImageView$ScaleType.MATRIX) {
                        imageMatrix = imageView.getImageMatrix();
                        final float[] array = new float[9];
                        imageMatrix.getValues(array);
                        bundle.putFloatArray("sharedElement:snapshot:imageMatrix", array);
                    }
                    return (Parcelable)bundle;
                }
            }
        }
        final int round = Math.round(rectF.width());
        final int round2 = Math.round(rectF.height());
        Object bitmap;
        final Object o = bitmap = null;
        if (round > 0) {
            bitmap = o;
            if (round2 > 0) {
                final float min = Math.min(1.0f, 1048576.0f / (round * round2));
                final int n = (int)(round * min);
                final int n2 = (int)(round2 * min);
                if (this.mTempMatrix == null) {
                    this.mTempMatrix = new Matrix();
                }
                this.mTempMatrix.set(imageMatrix);
                this.mTempMatrix.postTranslate(-rectF.left, -rectF.top);
                this.mTempMatrix.postScale(min, min);
                bitmap = Bitmap.createBitmap(n, n2, Bitmap$Config.ARGB_8888);
                final Canvas canvas = new Canvas((Bitmap)bitmap);
                canvas.concat(this.mTempMatrix);
                view.draw(canvas);
            }
        }
        return (Parcelable)bitmap;
    }
    
    public View onCreateSnapshotView(final Context context, final Parcelable parcelable) {
        Object o = null;
        if (!(parcelable instanceof Bundle)) {
            if (parcelable instanceof Bitmap) {
                final Bitmap imageBitmap = (Bitmap)parcelable;
                o = new ImageView(context);
                ((ImageView)o).setImageBitmap(imageBitmap);
            }
            return (View)o;
        }
        final Bundle bundle = (Bundle)parcelable;
        final Bitmap imageBitmap2 = (Bitmap)bundle.getParcelable("sharedElement:snapshot:bitmap");
        if (imageBitmap2 == null) {
            return null;
        }
        final ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(imageBitmap2);
        imageView.setScaleType(ImageView$ScaleType.valueOf(bundle.getString("sharedElement:snapshot:imageScaleType")));
        if (imageView.getScaleType() == ImageView$ScaleType.MATRIX) {
            final float[] floatArray = bundle.getFloatArray("sharedElement:snapshot:imageMatrix");
            final Matrix imageMatrix = new Matrix();
            imageMatrix.setValues(floatArray);
            imageView.setImageMatrix(imageMatrix);
        }
        return (View)imageView;
    }
    
    public void onMapSharedElements(final List<String> list, final Map<String, View> map) {
    }
    
    public void onRejectSharedElements(final List<View> list) {
    }
    
    public void onSharedElementEnd(final List<String> list, final List<View> list2, final List<View> list3) {
    }
    
    public void onSharedElementStart(final List<String> list, final List<View> list2, final List<View> list3) {
    }
    
    public void onSharedElementsArrived(final List<String> list, final List<View> list2, final OnSharedElementsReadyListener onSharedElementsReadyListener) {
        onSharedElementsReadyListener.onSharedElementsReady();
    }
    
    public interface OnSharedElementsReadyListener
    {
        void onSharedElementsReady();
    }
}
