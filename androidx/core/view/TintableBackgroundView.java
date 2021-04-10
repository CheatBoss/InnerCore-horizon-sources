package androidx.core.view;

import android.content.res.*;
import androidx.annotation.*;
import android.graphics.*;

public interface TintableBackgroundView
{
    @Nullable
    ColorStateList getSupportBackgroundTintList();
    
    @Nullable
    PorterDuff$Mode getSupportBackgroundTintMode();
    
    void setSupportBackgroundTintList(@Nullable final ColorStateList p0);
    
    void setSupportBackgroundTintMode(@Nullable final PorterDuff$Mode p0);
}
