package androidx.recyclerview.widget;

import androidx.annotation.*;
import android.view.*;
import android.graphics.*;
import android.content.*;
import android.util.*;
import android.view.animation.*;

public class PagerSnapHelper extends SnapHelper
{
    private static final int MAX_SCROLL_ON_FLING_DURATION = 100;
    @Nullable
    private OrientationHelper mHorizontalHelper;
    @Nullable
    private OrientationHelper mVerticalHelper;
    
    private int distanceToCenter(@NonNull final LayoutManager layoutManager, @NonNull final View view, final OrientationHelper orientationHelper) {
        return orientationHelper.getDecoratedStart(view) + orientationHelper.getDecoratedMeasurement(view) / 2 - (orientationHelper.getStartAfterPadding() + orientationHelper.getTotalSpace() / 2);
    }
    
    @Nullable
    private View findCenterView(final LayoutManager layoutManager, final OrientationHelper orientationHelper) {
        final int childCount = layoutManager.getChildCount();
        if (childCount == 0) {
            return null;
        }
        View view = null;
        final int startAfterPadding = orientationHelper.getStartAfterPadding();
        final int n = orientationHelper.getTotalSpace() / 2;
        int n2 = Integer.MAX_VALUE;
        int n3;
        for (int i = 0; i < childCount; ++i, n2 = n3) {
            final View child = layoutManager.getChildAt(i);
            final int abs = Math.abs(orientationHelper.getDecoratedStart(child) + orientationHelper.getDecoratedMeasurement(child) / 2 - (startAfterPadding + n));
            if (abs < (n3 = n2)) {
                n3 = abs;
                view = child;
            }
        }
        return view;
    }
    
    @NonNull
    private OrientationHelper getHorizontalHelper(@NonNull final LayoutManager layoutManager) {
        if (this.mHorizontalHelper == null || this.mHorizontalHelper.mLayoutManager != layoutManager) {
            this.mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager);
        }
        return this.mHorizontalHelper;
    }
    
    @Nullable
    private OrientationHelper getOrientationHelper(final LayoutManager layoutManager) {
        if (layoutManager.canScrollVertically()) {
            return this.getVerticalHelper(layoutManager);
        }
        if (layoutManager.canScrollHorizontally()) {
            return this.getHorizontalHelper(layoutManager);
        }
        return null;
    }
    
    @NonNull
    private OrientationHelper getVerticalHelper(@NonNull final LayoutManager layoutManager) {
        if (this.mVerticalHelper == null || this.mVerticalHelper.mLayoutManager != layoutManager) {
            this.mVerticalHelper = OrientationHelper.createVerticalHelper(layoutManager);
        }
        return this.mVerticalHelper;
    }
    
    private boolean isForwardFling(final LayoutManager layoutManager, final int n, final int n2) {
        final boolean canScrollHorizontally = layoutManager.canScrollHorizontally();
        final boolean b = false;
        boolean b2 = false;
        if (canScrollHorizontally) {
            if (n > 0) {
                b2 = true;
            }
            return b2;
        }
        boolean b3 = b;
        if (n2 > 0) {
            b3 = true;
        }
        return b3;
    }
    
    private boolean isReverseLayout(final LayoutManager layoutManager) {
        final int itemCount = layoutManager.getItemCount();
        if (layoutManager instanceof ScrollVectorProvider) {
            final PointF computeScrollVectorForPosition = ((ScrollVectorProvider)layoutManager).computeScrollVectorForPosition(itemCount - 1);
            if (computeScrollVectorForPosition != null) {
                return computeScrollVectorForPosition.x < 0.0f || computeScrollVectorForPosition.y < 0.0f;
            }
        }
        return false;
    }
    
    @Nullable
    @Override
    public int[] calculateDistanceToFinalSnap(@NonNull final LayoutManager layoutManager, @NonNull final View view) {
        final int[] array = new int[2];
        if (layoutManager.canScrollHorizontally()) {
            array[0] = this.distanceToCenter(layoutManager, view, this.getHorizontalHelper(layoutManager));
        }
        else {
            array[0] = 0;
        }
        if (layoutManager.canScrollVertically()) {
            array[1] = this.distanceToCenter(layoutManager, view, this.getVerticalHelper(layoutManager));
            return array;
        }
        array[1] = 0;
        return array;
    }
    
    @Override
    protected LinearSmoothScroller createSnapScroller(final LayoutManager layoutManager) {
        if (!(layoutManager instanceof ScrollVectorProvider)) {
            return null;
        }
        return new LinearSmoothScroller(this.mRecyclerView.getContext()) {
            @Override
            protected float calculateSpeedPerPixel(final DisplayMetrics displayMetrics) {
                return 100.0f / displayMetrics.densityDpi;
            }
            
            @Override
            protected int calculateTimeForScrolling(final int n) {
                return Math.min(100, super.calculateTimeForScrolling(n));
            }
            
            @Override
            protected void onTargetFound(final View view, final State state, final Action action) {
                final int[] calculateDistanceToFinalSnap = PagerSnapHelper.this.calculateDistanceToFinalSnap(PagerSnapHelper.this.mRecyclerView.getLayoutManager(), view);
                final int n = calculateDistanceToFinalSnap[0];
                final int n2 = calculateDistanceToFinalSnap[1];
                final int calculateTimeForDeceleration = this.calculateTimeForDeceleration(Math.max(Math.abs(n), Math.abs(n2)));
                if (calculateTimeForDeceleration > 0) {
                    action.update(n, n2, calculateTimeForDeceleration, (Interpolator)this.mDecelerateInterpolator);
                }
            }
        };
    }
    
    @Nullable
    @Override
    public View findSnapView(final LayoutManager layoutManager) {
        if (layoutManager.canScrollVertically()) {
            return this.findCenterView(layoutManager, this.getVerticalHelper(layoutManager));
        }
        if (layoutManager.canScrollHorizontally()) {
            return this.findCenterView(layoutManager, this.getHorizontalHelper(layoutManager));
        }
        return null;
    }
    
    @Override
    public int findTargetSnapPosition(final LayoutManager layoutManager, int n, int position) {
        final int itemCount = layoutManager.getItemCount();
        if (itemCount == 0) {
            return -1;
        }
        final OrientationHelper orientationHelper = this.getOrientationHelper(layoutManager);
        if (orientationHelper == null) {
            return -1;
        }
        View view = null;
        int n2 = Integer.MIN_VALUE;
        View view2 = null;
        int n3 = Integer.MAX_VALUE;
        View view3;
        int n4;
        for (int childCount = layoutManager.getChildCount(), i = 0; i < childCount; ++i, view2 = view3, n3 = n4) {
            final View child = layoutManager.getChildAt(i);
            if (child == null) {
                view3 = view2;
                n4 = n3;
            }
            else {
                final int distanceToCenter = this.distanceToCenter(layoutManager, child, orientationHelper);
                View view4 = view;
                int n5 = n2;
                if (distanceToCenter <= 0) {
                    view4 = view;
                    if (distanceToCenter > (n5 = n2)) {
                        n5 = distanceToCenter;
                        view4 = child;
                    }
                }
                view = view4;
                n2 = n5;
                view3 = view2;
                n4 = n3;
                if (distanceToCenter >= 0) {
                    view = view4;
                    n2 = n5;
                    view3 = view2;
                    if (distanceToCenter < (n4 = n3)) {
                        n4 = distanceToCenter;
                        view3 = child;
                        n2 = n5;
                        view = view4;
                    }
                }
            }
        }
        final boolean forwardFling = this.isForwardFling(layoutManager, n, position);
        if (forwardFling && view2 != null) {
            return layoutManager.getPosition(view2);
        }
        if (!forwardFling && view != null) {
            return layoutManager.getPosition(view);
        }
        if (!forwardFling) {
            view = view2;
        }
        if (view == null) {
            return -1;
        }
        position = layoutManager.getPosition(view);
        if (this.isReverseLayout(layoutManager) == forwardFling) {
            n = -1;
        }
        else {
            n = 1;
        }
        n += position;
        if (n < 0) {
            return -1;
        }
        if (n >= itemCount) {
            return -1;
        }
        return n;
    }
}
