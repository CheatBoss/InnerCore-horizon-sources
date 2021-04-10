package androidx.recyclerview.widget;

import android.view.*;
import android.graphics.*;

public interface ItemTouchUIUtil
{
    void clearView(final View p0);
    
    void onDraw(final Canvas p0, final RecyclerView p1, final View p2, final float p3, final float p4, final int p5, final boolean p6);
    
    void onDrawOver(final Canvas p0, final RecyclerView p1, final View p2, final float p3, final float p4, final int p5, final boolean p6);
    
    void onSelected(final View p0);
}
