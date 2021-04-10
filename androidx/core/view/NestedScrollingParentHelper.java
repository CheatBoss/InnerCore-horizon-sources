package androidx.core.view;

import androidx.annotation.*;
import android.view.*;

public class NestedScrollingParentHelper
{
    private int mNestedScrollAxesNonTouch;
    private int mNestedScrollAxesTouch;
    
    public NestedScrollingParentHelper(@NonNull final ViewGroup viewGroup) {
    }
    
    public int getNestedScrollAxes() {
        return this.mNestedScrollAxesTouch | this.mNestedScrollAxesNonTouch;
    }
    
    public void onNestedScrollAccepted(@NonNull final View view, @NonNull final View view2, final int n) {
        this.onNestedScrollAccepted(view, view2, n, 0);
    }
    
    public void onNestedScrollAccepted(@NonNull final View view, @NonNull final View view2, final int n, final int n2) {
        if (n2 == 1) {
            this.mNestedScrollAxesNonTouch = n;
            return;
        }
        this.mNestedScrollAxesTouch = n;
    }
    
    public void onStopNestedScroll(@NonNull final View view) {
        this.onStopNestedScroll(view, 0);
    }
    
    public void onStopNestedScroll(@NonNull final View view, final int n) {
        if (n == 1) {
            this.mNestedScrollAxesNonTouch = 0;
            return;
        }
        this.mNestedScrollAxesTouch = 0;
    }
}
