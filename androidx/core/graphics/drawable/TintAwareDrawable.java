package androidx.core.graphics.drawable;

import androidx.annotation.*;
import android.content.res.*;
import android.graphics.*;

@RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
public interface TintAwareDrawable
{
    void setTint(@ColorInt final int p0);
    
    void setTintList(final ColorStateList p0);
    
    void setTintMode(final PorterDuff$Mode p0);
}
