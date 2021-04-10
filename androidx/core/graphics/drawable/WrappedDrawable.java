package androidx.core.graphics.drawable;

import androidx.annotation.*;
import android.graphics.drawable.*;

@RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
public interface WrappedDrawable
{
    Drawable getWrappedDrawable();
    
    void setWrappedDrawable(final Drawable p0);
}
