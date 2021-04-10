package android.support.v4.app;

import android.view.*;
import android.support.annotation.*;

public abstract class FragmentContainer
{
    @Nullable
    public abstract View onFindViewById(@IdRes final int p0);
    
    public abstract boolean onHasView();
}
