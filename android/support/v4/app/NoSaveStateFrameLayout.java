package android.support.v4.app;

import android.content.*;
import android.widget.*;
import android.view.*;
import android.util.*;
import android.os.*;

class NoSaveStateFrameLayout extends FrameLayout
{
    public NoSaveStateFrameLayout(final Context context) {
        super(context);
    }
    
    static ViewGroup wrap(final View view) {
        final NoSaveStateFrameLayout noSaveStateFrameLayout = new NoSaveStateFrameLayout(view.getContext());
        final ViewGroup$LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams != null) {
            noSaveStateFrameLayout.setLayoutParams(layoutParams);
        }
        view.setLayoutParams((ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, -1));
        noSaveStateFrameLayout.addView(view);
        return (ViewGroup)noSaveStateFrameLayout;
    }
    
    protected void dispatchRestoreInstanceState(final SparseArray<Parcelable> sparseArray) {
        this.dispatchThawSelfOnly((SparseArray)sparseArray);
    }
    
    protected void dispatchSaveInstanceState(final SparseArray<Parcelable> sparseArray) {
        this.dispatchFreezeSelfOnly((SparseArray)sparseArray);
    }
}
