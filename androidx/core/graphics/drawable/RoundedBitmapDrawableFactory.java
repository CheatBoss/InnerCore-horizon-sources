package androidx.core.graphics.drawable;

import android.content.res.*;
import androidx.annotation.*;
import android.os.*;
import java.io.*;
import android.util.*;
import android.graphics.*;
import androidx.core.view.*;
import androidx.core.graphics.*;

public final class RoundedBitmapDrawableFactory
{
    private static final String TAG = "RoundedBitmapDrawableFa";
    
    private RoundedBitmapDrawableFactory() {
    }
    
    @NonNull
    public static RoundedBitmapDrawable create(@NonNull final Resources resources, @Nullable final Bitmap bitmap) {
        if (Build$VERSION.SDK_INT >= 21) {
            return new RoundedBitmapDrawable21(resources, bitmap);
        }
        return new DefaultRoundedBitmapDrawable(resources, bitmap);
    }
    
    @NonNull
    public static RoundedBitmapDrawable create(@NonNull final Resources resources, @NonNull final InputStream inputStream) {
        final RoundedBitmapDrawable create = create(resources, BitmapFactory.decodeStream(inputStream));
        if (create.getBitmap() == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("RoundedBitmapDrawable cannot decode ");
            sb.append(inputStream);
            Log.w("RoundedBitmapDrawableFa", sb.toString());
        }
        return create;
    }
    
    @NonNull
    public static RoundedBitmapDrawable create(@NonNull final Resources resources, @NonNull final String s) {
        final RoundedBitmapDrawable create = create(resources, BitmapFactory.decodeFile(s));
        if (create.getBitmap() == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("RoundedBitmapDrawable cannot decode ");
            sb.append(s);
            Log.w("RoundedBitmapDrawableFa", sb.toString());
        }
        return create;
    }
    
    private static class DefaultRoundedBitmapDrawable extends RoundedBitmapDrawable
    {
        DefaultRoundedBitmapDrawable(final Resources resources, final Bitmap bitmap) {
            super(resources, bitmap);
        }
        
        @Override
        void gravityCompatApply(final int n, final int n2, final int n3, final Rect rect, final Rect rect2) {
            GravityCompat.apply(n, n2, n3, rect, rect2, 0);
        }
        
        @Override
        public boolean hasMipMap() {
            return this.mBitmap != null && BitmapCompat.hasMipMap(this.mBitmap);
        }
        
        @Override
        public void setMipMap(final boolean b) {
            if (this.mBitmap != null) {
                BitmapCompat.setHasMipMap(this.mBitmap, b);
                this.invalidateSelf();
            }
        }
    }
}
