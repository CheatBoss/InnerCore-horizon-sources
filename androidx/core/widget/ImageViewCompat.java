package androidx.core.widget;

import android.widget.*;
import android.content.res.*;
import android.os.*;
import androidx.annotation.*;
import android.graphics.*;
import android.graphics.drawable.*;

public class ImageViewCompat
{
    private ImageViewCompat() {
    }
    
    @Nullable
    public static ColorStateList getImageTintList(@NonNull final ImageView imageView) {
        if (Build$VERSION.SDK_INT >= 21) {
            return imageView.getImageTintList();
        }
        if (imageView instanceof TintableImageSourceView) {
            return ((TintableImageSourceView)imageView).getSupportImageTintList();
        }
        return null;
    }
    
    @Nullable
    public static PorterDuff$Mode getImageTintMode(@NonNull final ImageView imageView) {
        if (Build$VERSION.SDK_INT >= 21) {
            return imageView.getImageTintMode();
        }
        if (imageView instanceof TintableImageSourceView) {
            return ((TintableImageSourceView)imageView).getSupportImageTintMode();
        }
        return null;
    }
    
    public static void setImageTintList(@NonNull final ImageView imageView, @Nullable final ColorStateList list) {
        if (Build$VERSION.SDK_INT >= 21) {
            imageView.setImageTintList(list);
            if (Build$VERSION.SDK_INT == 21) {
                final Drawable drawable = imageView.getDrawable();
                if (drawable != null && imageView.getImageTintList() != null) {
                    if (drawable.isStateful()) {
                        drawable.setState(imageView.getDrawableState());
                    }
                    imageView.setImageDrawable(drawable);
                }
            }
        }
        else if (imageView instanceof TintableImageSourceView) {
            ((TintableImageSourceView)imageView).setSupportImageTintList(list);
        }
    }
    
    public static void setImageTintMode(@NonNull final ImageView imageView, @Nullable final PorterDuff$Mode porterDuff$Mode) {
        if (Build$VERSION.SDK_INT >= 21) {
            imageView.setImageTintMode(porterDuff$Mode);
            if (Build$VERSION.SDK_INT == 21) {
                final Drawable drawable = imageView.getDrawable();
                if (drawable != null && imageView.getImageTintList() != null) {
                    if (drawable.isStateful()) {
                        drawable.setState(imageView.getDrawableState());
                    }
                    imageView.setImageDrawable(drawable);
                }
            }
        }
        else if (imageView instanceof TintableImageSourceView) {
            ((TintableImageSourceView)imageView).setSupportImageTintMode(porterDuff$Mode);
        }
    }
}
