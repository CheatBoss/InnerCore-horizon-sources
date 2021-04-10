package androidx.core.widget;

import android.content.res.*;
import androidx.annotation.*;
import android.graphics.*;

public interface TintableCompoundButton
{
    @Nullable
    ColorStateList getSupportButtonTintList();
    
    @Nullable
    PorterDuff$Mode getSupportButtonTintMode();
    
    void setSupportButtonTintList(@Nullable final ColorStateList p0);
    
    void setSupportButtonTintMode(@Nullable final PorterDuff$Mode p0);
}
