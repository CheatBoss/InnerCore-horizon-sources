package androidx.recyclerview.widget;

import android.view.*;
import androidx.core.view.*;
import android.os.*;
import androidx.recyclerview.*;
import android.graphics.*;

class ItemTouchUIUtilImpl implements ItemTouchUIUtil
{
    static final ItemTouchUIUtil INSTANCE;
    
    static {
        INSTANCE = new ItemTouchUIUtilImpl();
    }
    
    private static float findMaxElevation(final RecyclerView recyclerView, final View view) {
        final int childCount = recyclerView.getChildCount();
        float n = 0.0f;
        float n2;
        for (int i = 0; i < childCount; ++i, n = n2) {
            final View child = recyclerView.getChildAt(i);
            if (child == view) {
                n2 = n;
            }
            else {
                final float elevation = ViewCompat.getElevation(child);
                n2 = n;
                if (elevation > n) {
                    n2 = elevation;
                }
            }
        }
        return n;
    }
    
    @Override
    public void clearView(final View view) {
        if (Build$VERSION.SDK_INT >= 21) {
            final Object tag = view.getTag(R$id.item_touch_helper_previous_elevation);
            if (tag instanceof Float) {
                ViewCompat.setElevation(view, (float)tag);
            }
            view.setTag(R$id.item_touch_helper_previous_elevation, (Object)null);
        }
        view.setTranslationX(0.0f);
        view.setTranslationY(0.0f);
    }
    
    @Override
    public void onDraw(final Canvas canvas, final RecyclerView recyclerView, final View view, final float translationX, final float translationY, final int n, final boolean b) {
        if (Build$VERSION.SDK_INT >= 21 && b && view.getTag(R$id.item_touch_helper_previous_elevation) == null) {
            final float elevation = ViewCompat.getElevation(view);
            ViewCompat.setElevation(view, findMaxElevation(recyclerView, view) + 1.0f);
            view.setTag(R$id.item_touch_helper_previous_elevation, (Object)elevation);
        }
        view.setTranslationX(translationX);
        view.setTranslationY(translationY);
    }
    
    @Override
    public void onDrawOver(final Canvas canvas, final RecyclerView recyclerView, final View view, final float n, final float n2, final int n3, final boolean b) {
    }
    
    @Override
    public void onSelected(final View view) {
    }
}
