package androidx.core.view;

import android.view.*;
import androidx.annotation.*;

public interface NestedScrollingParent2 extends NestedScrollingParent
{
    void onNestedPreScroll(@NonNull final View p0, final int p1, final int p2, @NonNull final int[] p3, final int p4);
    
    void onNestedScroll(@NonNull final View p0, final int p1, final int p2, final int p3, final int p4, final int p5);
    
    void onNestedScrollAccepted(@NonNull final View p0, @NonNull final View p1, final int p2, final int p3);
    
    boolean onStartNestedScroll(@NonNull final View p0, @NonNull final View p1, final int p2, final int p3);
    
    void onStopNestedScroll(@NonNull final View p0, final int p1);
}
