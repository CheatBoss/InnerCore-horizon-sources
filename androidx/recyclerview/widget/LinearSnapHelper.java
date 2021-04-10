package androidx.recyclerview.widget;

import android.view.*;
import androidx.annotation.*;
import android.graphics.*;

public class LinearSnapHelper extends SnapHelper
{
    private static final float INVALID_DISTANCE = 1.0f;
    @Nullable
    private OrientationHelper mHorizontalHelper;
    @Nullable
    private OrientationHelper mVerticalHelper;
    
    private float computeDistancePerChild(final LayoutManager layoutManager, final OrientationHelper orientationHelper) {
        View view = null;
        View view2 = null;
        int n = Integer.MAX_VALUE;
        int n2 = Integer.MIN_VALUE;
        final int childCount = layoutManager.getChildCount();
        if (childCount == 0) {
            return 1.0f;
        }
        View view3;
        int n3;
        for (int i = 0; i < childCount; ++i, view = view3, n2 = n3) {
            final View child = layoutManager.getChildAt(i);
            final int position = layoutManager.getPosition(child);
            if (position == -1) {
                view3 = view;
                n3 = n2;
            }
            else {
                int n4;
                if (position < (n4 = n)) {
                    n4 = position;
                    view = child;
                }
                view3 = view;
                n = n4;
                if (position > (n3 = n2)) {
                    view2 = child;
                    n3 = position;
                    n = n4;
                    view3 = view;
                }
            }
        }
        if (view == null) {
            return 1.0f;
        }
        if (view2 == null) {
            return 1.0f;
        }
        final int n5 = Math.max(orientationHelper.getDecoratedEnd(view), orientationHelper.getDecoratedEnd(view2)) - Math.min(orientationHelper.getDecoratedStart(view), orientationHelper.getDecoratedStart(view2));
        if (n5 == 0) {
            return 1.0f;
        }
        return n5 * 1.0f / (n2 - n + 1);
    }
    
    private int distanceToCenter(@NonNull final LayoutManager layoutManager, @NonNull final View view, final OrientationHelper orientationHelper) {
        return orientationHelper.getDecoratedStart(view) + orientationHelper.getDecoratedMeasurement(view) / 2 - (orientationHelper.getStartAfterPadding() + orientationHelper.getTotalSpace() / 2);
    }
    
    private int estimateNextPositionDiffForFling(final LayoutManager layoutManager, final OrientationHelper orientationHelper, int n, final int n2) {
        final int[] calculateScrollDistance = this.calculateScrollDistance(n, n2);
        final float computeDistancePerChild = this.computeDistancePerChild(layoutManager, orientationHelper);
        if (computeDistancePerChild <= 0.0f) {
            return 0;
        }
        if (Math.abs(calculateScrollDistance[0]) > Math.abs(calculateScrollDistance[1])) {
            n = calculateScrollDistance[0];
        }
        else {
            n = calculateScrollDistance[1];
        }
        return Math.round(n / computeDistancePerChild);
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
    
    @NonNull
    private OrientationHelper getVerticalHelper(@NonNull final LayoutManager layoutManager) {
        if (this.mVerticalHelper == null || this.mVerticalHelper.mLayoutManager != layoutManager) {
            this.mVerticalHelper = OrientationHelper.createVerticalHelper(layoutManager);
        }
        return this.mVerticalHelper;
    }
    
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
    public int findTargetSnapPosition(final LayoutManager layoutManager, int estimateNextPositionDiffForFling, int estimateNextPositionDiffForFling2) {
        if (!(layoutManager instanceof ScrollVectorProvider)) {
            return -1;
        }
        final int itemCount = layoutManager.getItemCount();
        if (itemCount == 0) {
            return -1;
        }
        final View snapView = this.findSnapView(layoutManager);
        if (snapView == null) {
            return -1;
        }
        final int position = layoutManager.getPosition(snapView);
        if (position == -1) {
            return -1;
        }
        final PointF computeScrollVectorForPosition = ((ScrollVectorProvider)layoutManager).computeScrollVectorForPosition(itemCount - 1);
        if (computeScrollVectorForPosition == null) {
            return -1;
        }
        final boolean canScrollHorizontally = layoutManager.canScrollHorizontally();
        final int n = 0;
        if (canScrollHorizontally) {
            final int n2 = estimateNextPositionDiffForFling = this.estimateNextPositionDiffForFling(layoutManager, this.getHorizontalHelper(layoutManager), estimateNextPositionDiffForFling, 0);
            if (computeScrollVectorForPosition.x < 0.0f) {
                estimateNextPositionDiffForFling = -n2;
            }
        }
        else {
            estimateNextPositionDiffForFling = 0;
        }
        if (layoutManager.canScrollVertically()) {
            final int n3 = estimateNextPositionDiffForFling2 = this.estimateNextPositionDiffForFling(layoutManager, this.getVerticalHelper(layoutManager), 0, estimateNextPositionDiffForFling2);
            if (computeScrollVectorForPosition.y < 0.0f) {
                estimateNextPositionDiffForFling2 = -n3;
            }
        }
        else {
            estimateNextPositionDiffForFling2 = n;
        }
        if (layoutManager.canScrollVertically()) {
            estimateNextPositionDiffForFling = estimateNextPositionDiffForFling2;
        }
        if (estimateNextPositionDiffForFling == 0) {
            return -1;
        }
        estimateNextPositionDiffForFling2 = position + estimateNextPositionDiffForFling;
        if ((estimateNextPositionDiffForFling = estimateNextPositionDiffForFling2) < 0) {
            estimateNextPositionDiffForFling = 0;
        }
        if ((estimateNextPositionDiffForFling2 = estimateNextPositionDiffForFling) >= itemCount) {
            estimateNextPositionDiffForFling2 = itemCount - 1;
        }
        return estimateNextPositionDiffForFling2;
    }
}
