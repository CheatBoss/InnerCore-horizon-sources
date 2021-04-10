package com.bumptech.glide.request.target;

import android.widget.*;
import com.bumptech.glide.load.resource.drawable.*;
import android.graphics.*;
import android.graphics.drawable.*;

public class ImageViewTargetFactory
{
    public <Z> Target<Z> buildTarget(final ImageView imageView, final Class<Z> clazz) {
        if (GlideDrawable.class.isAssignableFrom(clazz)) {
            return (Target<Z>)new GlideDrawableImageViewTarget(imageView);
        }
        if (Bitmap.class.equals(clazz)) {
            return (Target<Z>)new BitmapImageViewTarget(imageView);
        }
        if (Drawable.class.isAssignableFrom(clazz)) {
            return (Target<Z>)new DrawableImageViewTarget(imageView);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Unhandled class: ");
        sb.append(clazz);
        sb.append(", try .as*(Class).transcode(ResourceTranscoder)");
        throw new IllegalArgumentException(sb.toString());
    }
}
