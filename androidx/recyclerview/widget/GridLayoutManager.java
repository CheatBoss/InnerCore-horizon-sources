package androidx.recyclerview.widget;

import android.graphics.*;
import android.content.*;
import android.util.*;
import android.view.*;
import java.util.*;
import androidx.core.view.accessibility.*;

public class GridLayoutManager extends LinearLayoutManager
{
    private static final boolean DEBUG = false;
    public static final int DEFAULT_SPAN_COUNT = -1;
    private static final String TAG = "GridLayoutManager";
    int[] mCachedBorders;
    final Rect mDecorInsets;
    boolean mPendingSpanCountChange;
    final SparseIntArray mPreLayoutSpanIndexCache;
    final SparseIntArray mPreLayoutSpanSizeCache;
    View[] mSet;
    int mSpanCount;
    SpanSizeLookup mSpanSizeLookup;
    private boolean mUsingSpansToEstimateScrollBarDimensions;
    
    public GridLayoutManager(final Context context, final int spanCount) {
        super(context);
        this.mPendingSpanCountChange = false;
        this.mSpanCount = -1;
        this.mPreLayoutSpanSizeCache = new SparseIntArray();
        this.mPreLayoutSpanIndexCache = new SparseIntArray();
        this.mSpanSizeLookup = (SpanSizeLookup)new DefaultSpanSizeLookup();
        this.mDecorInsets = new Rect();
        this.setSpanCount(spanCount);
    }
    
    public GridLayoutManager(final Context context, final int spanCount, final int n, final boolean b) {
        super(context, n, b);
        this.mPendingSpanCountChange = false;
        this.mSpanCount = -1;
        this.mPreLayoutSpanSizeCache = new SparseIntArray();
        this.mPreLayoutSpanIndexCache = new SparseIntArray();
        this.mSpanSizeLookup = (SpanSizeLookup)new DefaultSpanSizeLookup();
        this.mDecorInsets = new Rect();
        this.setSpanCount(spanCount);
    }
    
    public GridLayoutManager(final Context context, final AttributeSet set, final int n, final int n2) {
        super(context, set, n, n2);
        this.mPendingSpanCountChange = false;
        this.mSpanCount = -1;
        this.mPreLayoutSpanSizeCache = new SparseIntArray();
        this.mPreLayoutSpanIndexCache = new SparseIntArray();
        this.mSpanSizeLookup = (SpanSizeLookup)new DefaultSpanSizeLookup();
        this.mDecorInsets = new Rect();
        this.setSpanCount(RecyclerView.LayoutManager.getProperties(context, set, n, n2).spanCount);
    }
    
    private void assignSpans(final Recycler recycler, final State state, int i, final boolean b) {
        int n2;
        int n3;
        if (b) {
            final int n = 0;
            n2 = i;
            n3 = 1;
            i = n;
        }
        else {
            --i;
            n2 = -1;
            n3 = -1;
        }
        int mSpanIndex = 0;
        while (i != n2) {
            final View view = this.mSet[i];
            final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            layoutParams.mSpanSize = this.getSpanSize(recycler, state, ((RecyclerView.LayoutManager)this).getPosition(view));
            layoutParams.mSpanIndex = mSpanIndex;
            mSpanIndex += layoutParams.mSpanSize;
            i += n3;
        }
    }
    
    private void cachePreLayoutSpanMapping() {
        for (int childCount = ((RecyclerView.LayoutManager)this).getChildCount(), i = 0; i < childCount; ++i) {
            final LayoutParams layoutParams = (LayoutParams)((RecyclerView.LayoutManager)this).getChildAt(i).getLayoutParams();
            final int viewLayoutPosition = ((RecyclerView.LayoutParams)layoutParams).getViewLayoutPosition();
            this.mPreLayoutSpanSizeCache.put(viewLayoutPosition, layoutParams.getSpanSize());
            this.mPreLayoutSpanIndexCache.put(viewLayoutPosition, layoutParams.getSpanIndex());
        }
    }
    
    private void calculateItemBorders(final int n) {
        this.mCachedBorders = calculateItemBorders(this.mCachedBorders, this.mSpanCount, n);
    }
    
    static int[] calculateItemBorders(final int[] array, final int n, int n2) {
        int i = 1;
        int[] array2 = null;
        Label_0034: {
            if (array != null && array.length == n + 1) {
                array2 = array;
                if (array[array.length - 1] == n2) {
                    break Label_0034;
                }
            }
            array2 = new int[n + 1];
        }
        array2[0] = 0;
        final int n3 = n2 / n;
        final int n4 = n2 % n;
        int n5 = 0;
        n2 = 0;
        while (i <= n) {
            final int n6 = n3;
            final int n7 = n2 += n4;
            int n8 = n6;
            if (n7 > 0) {
                n2 = n7;
                n8 = n6;
                if (n - n7 < n4) {
                    n8 = n6 + 1;
                    n2 = n7 - n;
                }
            }
            n5 += n8;
            array2[i] = n5;
            ++i;
        }
        return array2;
    }
    
    private void clearPreLayoutSpanMappingCache() {
        this.mPreLayoutSpanSizeCache.clear();
        this.mPreLayoutSpanIndexCache.clear();
    }
    
    private int computeScrollOffsetWithSpanInfo(final State state) {
        if (((RecyclerView.LayoutManager)this).getChildCount() == 0) {
            return 0;
        }
        if (state.getItemCount() == 0) {
            return 0;
        }
        this.ensureLayoutState();
        final boolean smoothScrollbarEnabled = this.isSmoothScrollbarEnabled();
        final View firstVisibleChildClosestToStart = this.findFirstVisibleChildClosestToStart(smoothScrollbarEnabled ^ true, true);
        final View firstVisibleChildClosestToEnd = this.findFirstVisibleChildClosestToEnd(smoothScrollbarEnabled ^ true, true);
        if (firstVisibleChildClosestToStart == null) {
            return 0;
        }
        if (firstVisibleChildClosestToEnd == null) {
            return 0;
        }
        final int cachedSpanGroupIndex = this.mSpanSizeLookup.getCachedSpanGroupIndex(((RecyclerView.LayoutManager)this).getPosition(firstVisibleChildClosestToStart), this.mSpanCount);
        final int cachedSpanGroupIndex2 = this.mSpanSizeLookup.getCachedSpanGroupIndex(((RecyclerView.LayoutManager)this).getPosition(firstVisibleChildClosestToEnd), this.mSpanCount);
        final int min = Math.min(cachedSpanGroupIndex, cachedSpanGroupIndex2);
        final int max = Math.max(cachedSpanGroupIndex, cachedSpanGroupIndex2);
        final int cachedSpanGroupIndex3 = this.mSpanSizeLookup.getCachedSpanGroupIndex(state.getItemCount() - 1, this.mSpanCount);
        int n;
        if (this.mShouldReverseLayout) {
            n = Math.max(0, cachedSpanGroupIndex3 + 1 - max - 1);
        }
        else {
            n = Math.max(0, min);
        }
        if (!smoothScrollbarEnabled) {
            return n;
        }
        return Math.round(n * (Math.abs(this.mOrientationHelper.getDecoratedEnd(firstVisibleChildClosestToEnd) - this.mOrientationHelper.getDecoratedStart(firstVisibleChildClosestToStart)) / (float)(this.mSpanSizeLookup.getCachedSpanGroupIndex(((RecyclerView.LayoutManager)this).getPosition(firstVisibleChildClosestToEnd), this.mSpanCount) - this.mSpanSizeLookup.getCachedSpanGroupIndex(((RecyclerView.LayoutManager)this).getPosition(firstVisibleChildClosestToStart), this.mSpanCount) + 1)) + (this.mOrientationHelper.getStartAfterPadding() - this.mOrientationHelper.getDecoratedStart(firstVisibleChildClosestToStart)));
    }
    
    private int computeScrollRangeWithSpanInfo(final State state) {
        if (((RecyclerView.LayoutManager)this).getChildCount() == 0) {
            return 0;
        }
        if (state.getItemCount() == 0) {
            return 0;
        }
        this.ensureLayoutState();
        final View firstVisibleChildClosestToStart = this.findFirstVisibleChildClosestToStart(this.isSmoothScrollbarEnabled() ^ true, true);
        final View firstVisibleChildClosestToEnd = this.findFirstVisibleChildClosestToEnd(this.isSmoothScrollbarEnabled() ^ true, true);
        if (firstVisibleChildClosestToStart == null) {
            return 0;
        }
        if (firstVisibleChildClosestToEnd == null) {
            return 0;
        }
        if (!this.isSmoothScrollbarEnabled()) {
            return this.mSpanSizeLookup.getCachedSpanGroupIndex(state.getItemCount() - 1, this.mSpanCount) + 1;
        }
        return (int)((this.mOrientationHelper.getDecoratedEnd(firstVisibleChildClosestToEnd) - this.mOrientationHelper.getDecoratedStart(firstVisibleChildClosestToStart)) / (float)(this.mSpanSizeLookup.getCachedSpanGroupIndex(((RecyclerView.LayoutManager)this).getPosition(firstVisibleChildClosestToEnd), this.mSpanCount) - this.mSpanSizeLookup.getCachedSpanGroupIndex(((RecyclerView.LayoutManager)this).getPosition(firstVisibleChildClosestToStart), this.mSpanCount) + 1) * (this.mSpanSizeLookup.getCachedSpanGroupIndex(state.getItemCount() - 1, this.mSpanCount) + 1));
    }
    
    private void ensureAnchorIsInCorrectSpan(final Recycler recycler, final State state, final AnchorInfo anchorInfo, int n) {
        final boolean b = n == 1;
        n = this.getSpanIndex(recycler, state, anchorInfo.mPosition);
        if (b) {
            while (n > 0 && anchorInfo.mPosition > 0) {
                --anchorInfo.mPosition;
                n = this.getSpanIndex(recycler, state, anchorInfo.mPosition);
            }
        }
        else {
            int itemCount;
            int i;
            int spanIndex;
            for (itemCount = state.getItemCount(), i = anchorInfo.mPosition; i < itemCount - 1; ++i, n = spanIndex) {
                spanIndex = this.getSpanIndex(recycler, state, i + 1);
                if (spanIndex <= n) {
                    break;
                }
            }
            anchorInfo.mPosition = i;
        }
    }
    
    private void ensureViewSet() {
        if (this.mSet == null || this.mSet.length != this.mSpanCount) {
            this.mSet = new View[this.mSpanCount];
        }
    }
    
    private int getSpanGroupIndex(final Recycler recycler, final State state, final int n) {
        if (!state.isPreLayout()) {
            return this.mSpanSizeLookup.getCachedSpanGroupIndex(n, this.mSpanCount);
        }
        final int convertPreLayoutPositionToPostLayout = recycler.convertPreLayoutPositionToPostLayout(n);
        if (convertPreLayoutPositionToPostLayout == -1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Cannot find span size for pre layout position. ");
            sb.append(n);
            Log.w("GridLayoutManager", sb.toString());
            return 0;
        }
        return this.mSpanSizeLookup.getCachedSpanGroupIndex(convertPreLayoutPositionToPostLayout, this.mSpanCount);
    }
    
    private int getSpanIndex(final Recycler recycler, final State state, final int n) {
        if (!state.isPreLayout()) {
            return this.mSpanSizeLookup.getCachedSpanIndex(n, this.mSpanCount);
        }
        final int value = this.mPreLayoutSpanIndexCache.get(n, -1);
        if (value != -1) {
            return value;
        }
        final int convertPreLayoutPositionToPostLayout = recycler.convertPreLayoutPositionToPostLayout(n);
        if (convertPreLayoutPositionToPostLayout == -1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:");
            sb.append(n);
            Log.w("GridLayoutManager", sb.toString());
            return 0;
        }
        return this.mSpanSizeLookup.getCachedSpanIndex(convertPreLayoutPositionToPostLayout, this.mSpanCount);
    }
    
    private int getSpanSize(final Recycler recycler, final State state, final int n) {
        if (!state.isPreLayout()) {
            return this.mSpanSizeLookup.getSpanSize(n);
        }
        final int value = this.mPreLayoutSpanSizeCache.get(n, -1);
        if (value != -1) {
            return value;
        }
        final int convertPreLayoutPositionToPostLayout = recycler.convertPreLayoutPositionToPostLayout(n);
        if (convertPreLayoutPositionToPostLayout == -1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:");
            sb.append(n);
            Log.w("GridLayoutManager", sb.toString());
            return 1;
        }
        return this.mSpanSizeLookup.getSpanSize(convertPreLayoutPositionToPostLayout);
    }
    
    private void guessMeasurement(final float n, final int n2) {
        this.calculateItemBorders(Math.max(Math.round(this.mSpanCount * n), n2));
    }
    
    private void measureChild(final View view, int n, final boolean b) {
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        final Rect mDecorInsets = layoutParams.mDecorInsets;
        final int n2 = mDecorInsets.top + mDecorInsets.bottom + layoutParams.topMargin + layoutParams.bottomMargin;
        final int n3 = mDecorInsets.left + mDecorInsets.right + layoutParams.leftMargin + layoutParams.rightMargin;
        final int spaceForSpanRange = this.getSpaceForSpanRange(layoutParams.mSpanIndex, layoutParams.mSpanSize);
        int n4;
        if (this.mOrientation == 1) {
            n = RecyclerView.LayoutManager.getChildMeasureSpec(spaceForSpanRange, n, n3, layoutParams.width, false);
            n4 = RecyclerView.LayoutManager.getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), ((RecyclerView.LayoutManager)this).getHeightMode(), n2, layoutParams.height, true);
        }
        else {
            n4 = RecyclerView.LayoutManager.getChildMeasureSpec(spaceForSpanRange, n, n2, layoutParams.height, false);
            n = RecyclerView.LayoutManager.getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), ((RecyclerView.LayoutManager)this).getWidthMode(), n3, layoutParams.width, true);
        }
        this.measureChildWithDecorationsAndMargin(view, n, n4, b);
    }
    
    private void measureChildWithDecorationsAndMargin(final View view, final int n, final int n2, final boolean b) {
        final RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)view.getLayoutParams();
        boolean b2;
        if (b) {
            b2 = ((RecyclerView.LayoutManager)this).shouldReMeasureChild(view, n, n2, layoutParams);
        }
        else {
            b2 = ((RecyclerView.LayoutManager)this).shouldMeasureChild(view, n, n2, layoutParams);
        }
        if (b2) {
            view.measure(n, n2);
        }
    }
    
    private void updateMeasurements() {
        int n;
        if (this.getOrientation() == 1) {
            n = ((RecyclerView.LayoutManager)this).getWidth() - ((RecyclerView.LayoutManager)this).getPaddingRight() - ((RecyclerView.LayoutManager)this).getPaddingLeft();
        }
        else {
            n = ((RecyclerView.LayoutManager)this).getHeight() - ((RecyclerView.LayoutManager)this).getPaddingBottom() - ((RecyclerView.LayoutManager)this).getPaddingTop();
        }
        this.calculateItemBorders(n);
    }
    
    @Override
    public boolean checkLayoutParams(final RecyclerView.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }
    
    @Override
    void collectPrefetchPositionsForLayoutState(final State state, final LayoutState layoutState, final LayoutPrefetchRegistry layoutPrefetchRegistry) {
        int mCurrentPosition;
        for (int mSpanCount = this.mSpanCount, n = 0; n < this.mSpanCount && layoutState.hasMore(state) && mSpanCount > 0; mSpanCount -= this.mSpanSizeLookup.getSpanSize(mCurrentPosition), layoutState.mCurrentPosition += layoutState.mItemDirection, ++n) {
            mCurrentPosition = layoutState.mCurrentPosition;
            layoutPrefetchRegistry.addPosition(mCurrentPosition, Math.max(0, layoutState.mScrollingOffset));
        }
    }
    
    @Override
    public int computeHorizontalScrollOffset(final State state) {
        if (this.mUsingSpansToEstimateScrollBarDimensions) {
            return this.computeScrollOffsetWithSpanInfo(state);
        }
        return super.computeHorizontalScrollOffset(state);
    }
    
    @Override
    public int computeHorizontalScrollRange(final State state) {
        if (this.mUsingSpansToEstimateScrollBarDimensions) {
            return this.computeScrollRangeWithSpanInfo(state);
        }
        return super.computeHorizontalScrollRange(state);
    }
    
    @Override
    public int computeVerticalScrollOffset(final State state) {
        if (this.mUsingSpansToEstimateScrollBarDimensions) {
            return this.computeScrollOffsetWithSpanInfo(state);
        }
        return super.computeVerticalScrollOffset(state);
    }
    
    @Override
    public int computeVerticalScrollRange(final State state) {
        if (this.mUsingSpansToEstimateScrollBarDimensions) {
            return this.computeScrollRangeWithSpanInfo(state);
        }
        return super.computeVerticalScrollRange(state);
    }
    
    @Override
    View findReferenceChild(final Recycler recycler, final State state, int i, final int n, final int n2) {
        this.ensureLayoutState();
        View view = null;
        final int startAfterPadding = this.mOrientationHelper.getStartAfterPadding();
        final int endAfterPadding = this.mOrientationHelper.getEndAfterPadding();
        int n3;
        if (n > i) {
            n3 = 1;
        }
        else {
            n3 = -1;
        }
        View view2 = null;
        while (i != n) {
            final View child = ((RecyclerView.LayoutManager)this).getChildAt(i);
            final int position = ((RecyclerView.LayoutManager)this).getPosition(child);
            View view3 = view;
            View view4 = view2;
            if (position >= 0) {
                view3 = view;
                view4 = view2;
                if (position < n2) {
                    if (this.getSpanIndex(recycler, state, position) != 0) {
                        view3 = view;
                        view4 = view2;
                    }
                    else if (((RecyclerView.LayoutParams)child.getLayoutParams()).isItemRemoved()) {
                        view3 = view;
                        if ((view4 = view2) == null) {
                            view4 = child;
                            view3 = view;
                        }
                    }
                    else {
                        if (this.mOrientationHelper.getDecoratedStart(child) < endAfterPadding && this.mOrientationHelper.getDecoratedEnd(child) >= startAfterPadding) {
                            return child;
                        }
                        view3 = view;
                        view4 = view2;
                        if (view == null) {
                            view3 = child;
                            view4 = view2;
                        }
                    }
                }
            }
            i += n3;
            view = view3;
            view2 = view4;
        }
        if (view != null) {
            return view;
        }
        return view2;
    }
    
    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        if (this.mOrientation == 0) {
            return new LayoutParams(-2, -1);
        }
        return new LayoutParams(-1, -2);
    }
    
    @Override
    public RecyclerView.LayoutParams generateLayoutParams(final Context context, final AttributeSet set) {
        return new LayoutParams(context, set);
    }
    
    @Override
    public RecyclerView.LayoutParams generateLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        if (viewGroup$LayoutParams instanceof ViewGroup$MarginLayoutParams) {
            return new LayoutParams((ViewGroup$MarginLayoutParams)viewGroup$LayoutParams);
        }
        return new LayoutParams(viewGroup$LayoutParams);
    }
    
    @Override
    public int getColumnCountForAccessibility(final Recycler recycler, final State state) {
        if (this.mOrientation == 1) {
            return this.mSpanCount;
        }
        if (state.getItemCount() < 1) {
            return 0;
        }
        return this.getSpanGroupIndex(recycler, state, state.getItemCount() - 1) + 1;
    }
    
    @Override
    public int getRowCountForAccessibility(final Recycler recycler, final State state) {
        if (this.mOrientation == 0) {
            return this.mSpanCount;
        }
        if (state.getItemCount() < 1) {
            return 0;
        }
        return this.getSpanGroupIndex(recycler, state, state.getItemCount() - 1) + 1;
    }
    
    int getSpaceForSpanRange(final int n, final int n2) {
        if (this.mOrientation == 1 && this.isLayoutRTL()) {
            return this.mCachedBorders[this.mSpanCount - n] - this.mCachedBorders[this.mSpanCount - n - n2];
        }
        return this.mCachedBorders[n + n2] - this.mCachedBorders[n];
    }
    
    public int getSpanCount() {
        return this.mSpanCount;
    }
    
    public SpanSizeLookup getSpanSizeLookup() {
        return this.mSpanSizeLookup;
    }
    
    public boolean isUsingSpansToEstimateScrollbarDimensions() {
        return this.mUsingSpansToEstimateScrollBarDimensions;
    }
    
    @Override
    void layoutChunk(final Recycler recycler, final State state, final LayoutState layoutState, final LayoutChunkResult layoutChunkResult) {
        final int modeInOther = this.mOrientationHelper.getModeInOther();
        final boolean b = modeInOther != 1073741824;
        int n;
        if (((RecyclerView.LayoutManager)this).getChildCount() > 0) {
            n = this.mCachedBorders[this.mSpanCount];
        }
        else {
            n = 0;
        }
        if (b) {
            this.updateMeasurements();
        }
        final boolean b2 = layoutState.mItemDirection == 1;
        int mSpanCount = this.mSpanCount;
        if (!b2) {
            mSpanCount = this.getSpanIndex(recycler, state, layoutState.mCurrentPosition) + this.getSpanSize(recycler, state, layoutState.mCurrentPosition);
        }
        int n2 = 0;
        int n3 = 0;
        int n4;
        while (n2 < this.mSpanCount && layoutState.hasMore(state) && (n4 = mSpanCount) > 0) {
            final int mCurrentPosition = layoutState.mCurrentPosition;
            final int spanSize = this.getSpanSize(recycler, state, mCurrentPosition);
            if (spanSize > this.mSpanCount) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Item at position ");
                sb.append(mCurrentPosition);
                sb.append(" requires ");
                sb.append(spanSize);
                sb.append(" spans but GridLayoutManager has only ");
                sb.append(this.mSpanCount);
                sb.append(" spans.");
                throw new IllegalArgumentException(sb.toString());
            }
            mSpanCount -= spanSize;
            if (mSpanCount < 0) {
                break;
            }
            final View next = layoutState.next(recycler);
            if (next == null) {
                break;
            }
            n3 += spanSize;
            this.mSet[n2] = next;
            ++n2;
        }
        if (n2 == 0) {
            layoutChunkResult.mFinished = true;
            return;
        }
        this.assignSpans(recycler, state, n2, b2);
        float n5 = 0.0f;
        int n6 = 0;
        int n7;
        float n9;
        for (int i = 0; i < n2; ++i, n6 = n7, n5 = n9) {
            final View view = this.mSet[i];
            if (layoutState.mScrapList == null) {
                if (b2) {
                    ((RecyclerView.LayoutManager)this).addView(view);
                }
                else {
                    ((RecyclerView.LayoutManager)this).addView(view, 0);
                }
            }
            else if (b2) {
                ((RecyclerView.LayoutManager)this).addDisappearingView(view);
            }
            else {
                ((RecyclerView.LayoutManager)this).addDisappearingView(view, 0);
            }
            ((RecyclerView.LayoutManager)this).calculateItemDecorationsForChild(view, this.mDecorInsets);
            this.measureChild(view, modeInOther, false);
            final int decoratedMeasurement = this.mOrientationHelper.getDecoratedMeasurement(view);
            n7 = n6;
            if (decoratedMeasurement > n6) {
                n7 = decoratedMeasurement;
            }
            final float n8 = this.mOrientationHelper.getDecoratedMeasurementInOther(view) * 1.0f / ((LayoutParams)view.getLayoutParams()).mSpanSize;
            n9 = n5;
            if (n8 > n5) {
                n9 = n8;
            }
        }
        int mConsumed = n6;
        if (b) {
            this.guessMeasurement(n5, n);
            int n10 = 0;
            int n11 = 0;
            while (true) {
                mConsumed = n10;
                if (n11 >= n2) {
                    break;
                }
                final View view2 = this.mSet[n11];
                this.measureChild(view2, 1073741824, true);
                final int decoratedMeasurement2 = this.mOrientationHelper.getDecoratedMeasurement(view2);
                int n12;
                if (decoratedMeasurement2 > (n12 = n10)) {
                    n12 = decoratedMeasurement2;
                }
                ++n11;
                n10 = n12;
            }
        }
        for (int j = 0; j < n2; ++j) {
            final View view3 = this.mSet[j];
            if (this.mOrientationHelper.getDecoratedMeasurement(view3) != mConsumed) {
                final LayoutParams layoutParams = (LayoutParams)view3.getLayoutParams();
                final Rect mDecorInsets = layoutParams.mDecorInsets;
                final int n13 = mDecorInsets.top + mDecorInsets.bottom + layoutParams.topMargin + layoutParams.bottomMargin;
                final int n14 = mDecorInsets.left + mDecorInsets.right + layoutParams.leftMargin + layoutParams.rightMargin;
                final int spaceForSpanRange = this.getSpaceForSpanRange(layoutParams.mSpanIndex, layoutParams.mSpanSize);
                int n15;
                int n16;
                if (this.mOrientation == 1) {
                    n15 = RecyclerView.LayoutManager.getChildMeasureSpec(spaceForSpanRange, 1073741824, n14, layoutParams.width, false);
                    n16 = View$MeasureSpec.makeMeasureSpec(mConsumed - n13, 1073741824);
                }
                else {
                    n15 = View$MeasureSpec.makeMeasureSpec(mConsumed - n14, 1073741824);
                    n16 = RecyclerView.LayoutManager.getChildMeasureSpec(spaceForSpanRange, 1073741824, n13, layoutParams.height, false);
                }
                this.measureChildWithDecorationsAndMargin(view3, n15, n16, true);
            }
        }
        layoutChunkResult.mConsumed = mConsumed;
        int mOffset = 0;
        int mOffset2 = 0;
        int mOffset3 = 0;
        final boolean b3 = false;
        int n17;
        if (this.mOrientation == 1) {
            if (layoutState.mLayoutDirection == -1) {
                final int mOffset4 = layoutState.mOffset;
                mOffset3 = mOffset4 - mConsumed;
                n17 = mOffset4;
            }
            else {
                mOffset3 = layoutState.mOffset;
                n17 = mOffset3 + mConsumed;
            }
        }
        else if (layoutState.mLayoutDirection == -1) {
            mOffset2 = layoutState.mOffset;
            mOffset = mOffset2 - mConsumed;
            n17 = (b3 ? 1 : 0);
        }
        else {
            mOffset = layoutState.mOffset;
            mOffset2 = mOffset + mConsumed;
            n17 = (b3 ? 1 : 0);
        }
        int n25;
        for (int k = 0; k < n2; k = n25) {
            final View view4 = this.mSet[k];
            final LayoutParams layoutParams2 = (LayoutParams)view4.getLayoutParams();
            int n19;
            int n20;
            int n21;
            if (this.mOrientation == 1) {
                if (this.isLayoutRTL()) {
                    final int n18 = ((RecyclerView.LayoutManager)this).getPaddingLeft() + this.mCachedBorders[this.mSpanCount - layoutParams2.mSpanIndex];
                    mOffset = n18 - this.mOrientationHelper.getDecoratedMeasurementInOther(view4);
                    n19 = mOffset3;
                    n20 = n17;
                    n21 = n18;
                }
                else {
                    final int n22 = ((RecyclerView.LayoutManager)this).getPaddingLeft() + this.mCachedBorders[layoutParams2.mSpanIndex];
                    final int decoratedMeasurementInOther = this.mOrientationHelper.getDecoratedMeasurementInOther(view4);
                    mOffset = n22;
                    final int n23 = decoratedMeasurementInOther + n22;
                    n19 = mOffset3;
                    n20 = n17;
                    n21 = n23;
                }
            }
            else {
                n21 = mOffset2;
                final int n24 = ((RecyclerView.LayoutManager)this).getPaddingTop() + this.mCachedBorders[layoutParams2.mSpanIndex];
                final int decoratedMeasurementInOther2 = this.mOrientationHelper.getDecoratedMeasurementInOther(view4);
                n19 = n24;
                n20 = decoratedMeasurementInOther2 + n24;
            }
            ((RecyclerView.LayoutManager)this).layoutDecoratedWithMargins(view4, mOffset, n19, n21, n20);
            if (((RecyclerView.LayoutParams)layoutParams2).isItemRemoved() || ((RecyclerView.LayoutParams)layoutParams2).isItemChanged()) {
                layoutChunkResult.mIgnoreConsumed = true;
            }
            layoutChunkResult.mFocusable |= view4.hasFocusable();
            n25 = k + 1;
            final int n26 = n19;
            final int n27 = n20;
            mOffset2 = n21;
            mOffset3 = n26;
            n17 = n27;
        }
        Arrays.fill(this.mSet, null);
    }
    
    @Override
    void onAnchorReady(final Recycler recycler, final State state, final AnchorInfo anchorInfo, final int n) {
        super.onAnchorReady(recycler, state, anchorInfo, n);
        this.updateMeasurements();
        if (state.getItemCount() > 0 && !state.isPreLayout()) {
            this.ensureAnchorIsInCorrectSpan(recycler, state, anchorInfo, n);
        }
        this.ensureViewSet();
    }
    
    @Override
    public View onFocusSearchFailed(View view, int min, final Recycler recycler, final State state) {
        final View containingItemView = ((RecyclerView.LayoutManager)this).findContainingItemView(view);
        if (containingItemView == null) {
            return null;
        }
        final LayoutParams layoutParams = (LayoutParams)containingItemView.getLayoutParams();
        final int mSpanIndex = layoutParams.mSpanIndex;
        final int n = layoutParams.mSpanIndex + layoutParams.mSpanSize;
        if (super.onFocusSearchFailed(view, min, recycler, state) == null) {
            return null;
        }
        int n2;
        int childCount;
        if (this.convertFocusDirectionToLayoutDirection(min) == 1 != this.mShouldReverseLayout) {
            min = ((RecyclerView.LayoutManager)this).getChildCount() - 1;
            n2 = -1;
            childCount = -1;
        }
        else {
            min = 0;
            n2 = 1;
            childCount = ((RecyclerView.LayoutManager)this).getChildCount();
        }
        final boolean b = this.mOrientation == 1 && this.isLayoutRTL();
        View view2 = null;
        final int spanGroupIndex = this.getSpanGroupIndex(recycler, state, min);
        final int n3 = -1;
        final int n4 = 0;
        int mSpanIndex2 = -1;
        int n5 = 0;
        View view3 = null;
        int i = min;
        min = n4;
        int mSpanIndex3 = n3;
        view = containingItemView;
        while (i != childCount) {
            final int spanGroupIndex2 = this.getSpanGroupIndex(recycler, state, i);
            final View child = ((RecyclerView.LayoutManager)this).getChildAt(i);
            if (child != view) {
                if (child.hasFocusable() && spanGroupIndex2 != spanGroupIndex) {
                    if (view3 != null) {
                        break;
                    }
                }
                else {
                    final LayoutParams layoutParams2 = (LayoutParams)child.getLayoutParams();
                    final int mSpanIndex4 = layoutParams2.mSpanIndex;
                    final int n6 = layoutParams2.mSpanIndex + layoutParams2.mSpanSize;
                    if (child.hasFocusable() && mSpanIndex4 == mSpanIndex && n6 == n) {
                        return child;
                    }
                    boolean b3 = false;
                    Label_0490: {
                        Label_0340: {
                            if ((child.hasFocusable() || view3 != null) && (child.hasFocusable() || view2 != null)) {
                                final int n7 = Math.min(n6, n) - Math.max(mSpanIndex4, mSpanIndex);
                                if (child.hasFocusable()) {
                                    if (n7 > min) {
                                        break Label_0340;
                                    }
                                    if (n7 == min) {
                                        if (b == mSpanIndex4 > mSpanIndex3) {
                                            break Label_0340;
                                        }
                                    }
                                }
                                else if (view3 == null) {
                                    boolean b2 = true;
                                    if (((RecyclerView.LayoutManager)this).isViewPartiallyVisible(child, false, true)) {
                                        if (n7 > n5) {
                                            b3 = true;
                                            break Label_0490;
                                        }
                                        if (n7 == n5) {
                                            if (mSpanIndex4 <= mSpanIndex2) {
                                                b2 = false;
                                            }
                                            if (b == b2) {
                                                b3 = true;
                                                break Label_0490;
                                            }
                                        }
                                    }
                                }
                                b3 = false;
                                break Label_0490;
                            }
                        }
                        b3 = true;
                    }
                    if (b3) {
                        if (child.hasFocusable()) {
                            mSpanIndex3 = layoutParams2.mSpanIndex;
                            min = Math.min(n6, n);
                            final int max = Math.max(mSpanIndex4, mSpanIndex);
                            view3 = child;
                            min -= max;
                        }
                        else {
                            mSpanIndex2 = layoutParams2.mSpanIndex;
                            final int min2 = Math.min(n6, n);
                            final int max2 = Math.max(mSpanIndex4, mSpanIndex);
                            view2 = child;
                            n5 = min2 - max2;
                        }
                    }
                }
                i += n2;
                continue;
            }
            break;
        }
        if (view3 != null) {
            return view3;
        }
        return view2;
    }
    
    @Override
    public void onInitializeAccessibilityNodeInfoForItem(final Recycler recycler, final State state, final View view, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        final ViewGroup$LayoutParams layoutParams = view.getLayoutParams();
        if (!(layoutParams instanceof LayoutParams)) {
            super.onInitializeAccessibilityNodeInfoForItem(view, accessibilityNodeInfoCompat);
            return;
        }
        final LayoutParams layoutParams2 = (LayoutParams)layoutParams;
        final int spanGroupIndex = this.getSpanGroupIndex(recycler, state, ((RecyclerView.LayoutParams)layoutParams2).getViewLayoutPosition());
        if (this.mOrientation == 0) {
            accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(layoutParams2.getSpanIndex(), layoutParams2.getSpanSize(), spanGroupIndex, 1, false, false));
            return;
        }
        accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(spanGroupIndex, 1, layoutParams2.getSpanIndex(), layoutParams2.getSpanSize(), false, false));
    }
    
    @Override
    public void onItemsAdded(final RecyclerView recyclerView, final int n, final int n2) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
        this.mSpanSizeLookup.invalidateSpanGroupIndexCache();
    }
    
    @Override
    public void onItemsChanged(final RecyclerView recyclerView) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
        this.mSpanSizeLookup.invalidateSpanGroupIndexCache();
    }
    
    @Override
    public void onItemsMoved(final RecyclerView recyclerView, final int n, final int n2, final int n3) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
        this.mSpanSizeLookup.invalidateSpanGroupIndexCache();
    }
    
    @Override
    public void onItemsRemoved(final RecyclerView recyclerView, final int n, final int n2) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
        this.mSpanSizeLookup.invalidateSpanGroupIndexCache();
    }
    
    @Override
    public void onItemsUpdated(final RecyclerView recyclerView, final int n, final int n2, final Object o) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
        this.mSpanSizeLookup.invalidateSpanGroupIndexCache();
    }
    
    @Override
    public void onLayoutChildren(final Recycler recycler, final State state) {
        if (state.isPreLayout()) {
            this.cachePreLayoutSpanMapping();
        }
        super.onLayoutChildren(recycler, state);
        this.clearPreLayoutSpanMappingCache();
    }
    
    @Override
    public void onLayoutCompleted(final State state) {
        super.onLayoutCompleted(state);
        this.mPendingSpanCountChange = false;
    }
    
    @Override
    public int scrollHorizontallyBy(final int n, final Recycler recycler, final State state) {
        this.updateMeasurements();
        this.ensureViewSet();
        return super.scrollHorizontallyBy(n, recycler, state);
    }
    
    @Override
    public int scrollVerticallyBy(final int n, final Recycler recycler, final State state) {
        this.updateMeasurements();
        this.ensureViewSet();
        return super.scrollVerticallyBy(n, recycler, state);
    }
    
    @Override
    public void setMeasuredDimension(final Rect rect, int chooseSize, int chooseSize2) {
        if (this.mCachedBorders == null) {
            super.setMeasuredDimension(rect, chooseSize, chooseSize2);
        }
        final int n = ((RecyclerView.LayoutManager)this).getPaddingLeft() + ((RecyclerView.LayoutManager)this).getPaddingRight();
        final int n2 = ((RecyclerView.LayoutManager)this).getPaddingTop() + ((RecyclerView.LayoutManager)this).getPaddingBottom();
        if (this.mOrientation == 1) {
            chooseSize2 = RecyclerView.LayoutManager.chooseSize(chooseSize2, rect.height() + n2, ((RecyclerView.LayoutManager)this).getMinimumHeight());
            final int chooseSize3 = RecyclerView.LayoutManager.chooseSize(chooseSize, this.mCachedBorders[this.mCachedBorders.length - 1] + n, ((RecyclerView.LayoutManager)this).getMinimumWidth());
            chooseSize = chooseSize2;
            chooseSize2 = chooseSize3;
        }
        else {
            chooseSize = RecyclerView.LayoutManager.chooseSize(chooseSize, rect.width() + n, ((RecyclerView.LayoutManager)this).getMinimumWidth());
            final int chooseSize4 = RecyclerView.LayoutManager.chooseSize(chooseSize2, this.mCachedBorders[this.mCachedBorders.length - 1] + n2, ((RecyclerView.LayoutManager)this).getMinimumHeight());
            chooseSize2 = chooseSize;
            chooseSize = chooseSize4;
        }
        ((RecyclerView.LayoutManager)this).setMeasuredDimension(chooseSize2, chooseSize);
    }
    
    public void setSpanCount(final int mSpanCount) {
        if (mSpanCount == this.mSpanCount) {
            return;
        }
        this.mPendingSpanCountChange = true;
        if (mSpanCount < 1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Span count should be at least 1. Provided ");
            sb.append(mSpanCount);
            throw new IllegalArgumentException(sb.toString());
        }
        this.mSpanCount = mSpanCount;
        this.mSpanSizeLookup.invalidateSpanIndexCache();
        ((RecyclerView.LayoutManager)this).requestLayout();
    }
    
    public void setSpanSizeLookup(final SpanSizeLookup mSpanSizeLookup) {
        this.mSpanSizeLookup = mSpanSizeLookup;
    }
    
    @Override
    public void setStackFromEnd(final boolean b) {
        if (b) {
            throw new UnsupportedOperationException("GridLayoutManager does not support stack from end. Consider using reverse layout");
        }
        super.setStackFromEnd(false);
    }
    
    public void setUsingSpansToEstimateScrollbarDimensions(final boolean mUsingSpansToEstimateScrollBarDimensions) {
        this.mUsingSpansToEstimateScrollBarDimensions = mUsingSpansToEstimateScrollBarDimensions;
    }
    
    @Override
    public boolean supportsPredictiveItemAnimations() {
        return this.mPendingSavedState == null && !this.mPendingSpanCountChange;
    }
    
    public static final class DefaultSpanSizeLookup extends SpanSizeLookup
    {
        @Override
        public int getSpanIndex(final int n, final int n2) {
            return n % n2;
        }
        
        @Override
        public int getSpanSize(final int n) {
            return 1;
        }
    }
    
    public static class LayoutParams extends RecyclerView.LayoutParams
    {
        public static final int INVALID_SPAN_ID = -1;
        int mSpanIndex;
        int mSpanSize;
        
        public LayoutParams(final int n, final int n2) {
            super(n, n2);
            this.mSpanIndex = -1;
            this.mSpanSize = 0;
        }
        
        public LayoutParams(final Context context, final AttributeSet set) {
            super(context, set);
            this.mSpanIndex = -1;
            this.mSpanSize = 0;
        }
        
        public LayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
            super(viewGroup$LayoutParams);
            this.mSpanIndex = -1;
            this.mSpanSize = 0;
        }
        
        public LayoutParams(final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams) {
            super(viewGroup$MarginLayoutParams);
            this.mSpanIndex = -1;
            this.mSpanSize = 0;
        }
        
        public LayoutParams(final RecyclerView.LayoutParams layoutParams) {
            super(layoutParams);
            this.mSpanIndex = -1;
            this.mSpanSize = 0;
        }
        
        public int getSpanIndex() {
            return this.mSpanIndex;
        }
        
        public int getSpanSize() {
            return this.mSpanSize;
        }
    }
    
    public abstract static class SpanSizeLookup
    {
        private boolean mCacheSpanGroupIndices;
        private boolean mCacheSpanIndices;
        final SparseIntArray mSpanGroupIndexCache;
        final SparseIntArray mSpanIndexCache;
        
        public SpanSizeLookup() {
            this.mSpanIndexCache = new SparseIntArray();
            this.mSpanGroupIndexCache = new SparseIntArray();
            this.mCacheSpanIndices = false;
            this.mCacheSpanGroupIndices = false;
        }
        
        static int findFirstKeyLessThan(final SparseIntArray sparseIntArray, int n) {
            int i = 0;
            int n2 = sparseIntArray.size() - 1;
            while (i <= n2) {
                final int n3 = i + n2 >>> 1;
                if (sparseIntArray.keyAt(n3) < n) {
                    i = n3 + 1;
                }
                else {
                    n2 = n3 - 1;
                }
            }
            n = i - 1;
            if (n >= 0 && n < sparseIntArray.size()) {
                return sparseIntArray.keyAt(n);
            }
            return -1;
        }
        
        int getCachedSpanGroupIndex(final int n, int spanGroupIndex) {
            if (!this.mCacheSpanGroupIndices) {
                return this.getSpanGroupIndex(n, spanGroupIndex);
            }
            final int value = this.mSpanGroupIndexCache.get(n, -1);
            if (value != -1) {
                return value;
            }
            spanGroupIndex = this.getSpanGroupIndex(n, spanGroupIndex);
            this.mSpanGroupIndexCache.put(n, spanGroupIndex);
            return spanGroupIndex;
        }
        
        int getCachedSpanIndex(final int n, int spanIndex) {
            if (!this.mCacheSpanIndices) {
                return this.getSpanIndex(n, spanIndex);
            }
            final int value = this.mSpanIndexCache.get(n, -1);
            if (value != -1) {
                return value;
            }
            spanIndex = this.getSpanIndex(n, spanIndex);
            this.mSpanIndexCache.put(n, spanIndex);
            return spanIndex;
        }
        
        public int getSpanGroupIndex(int n, final int n2) {
            final int n3 = 0;
            final int n4 = 0;
            final boolean b = false;
            int n5 = n3;
            int n6 = n4;
            int n7 = b ? 1 : 0;
            if (this.mCacheSpanGroupIndices) {
                final int firstKeyLessThan = findFirstKeyLessThan(this.mSpanGroupIndexCache, n);
                n5 = n3;
                n6 = n4;
                n7 = (b ? 1 : 0);
                if (firstKeyLessThan != -1) {
                    final int value = this.mSpanGroupIndexCache.get(firstKeyLessThan);
                    final int n8 = firstKeyLessThan + 1;
                    final int n9 = n5 = this.getCachedSpanIndex(firstKeyLessThan, n2) + this.getSpanSize(firstKeyLessThan);
                    n6 = value;
                    n7 = n8;
                    if (n9 == n2) {
                        n5 = 0;
                        n6 = value + 1;
                        n7 = n8;
                    }
                }
            }
            final int spanSize = this.getSpanSize(n);
            int n11;
            for (int i = n7; i < n; ++i, n6 = n11) {
                final int spanSize2 = this.getSpanSize(i);
                final int n10 = n5 + spanSize2;
                if (n10 == n2) {
                    n5 = 0;
                    n11 = n6 + 1;
                }
                else {
                    n5 = n10;
                    n11 = n6;
                    if (n10 > n2) {
                        n5 = spanSize2;
                        n11 = n6 + 1;
                    }
                }
            }
            n = n6;
            if (n5 + spanSize > n2) {
                n = n6 + 1;
            }
            return n;
        }
        
        public int getSpanIndex(final int n, final int n2) {
            final int spanSize = this.getSpanSize(n);
            if (spanSize == n2) {
                return 0;
            }
            final boolean b = false;
            final boolean b2 = false;
            int n3 = b ? 1 : 0;
            int n4 = b2 ? 1 : 0;
            if (this.mCacheSpanIndices) {
                final int firstKeyLessThan = findFirstKeyLessThan(this.mSpanIndexCache, n);
                n3 = (b ? 1 : 0);
                n4 = (b2 ? 1 : 0);
                if (firstKeyLessThan >= 0) {
                    n3 = this.mSpanIndexCache.get(firstKeyLessThan) + this.getSpanSize(firstKeyLessThan);
                    n4 = firstKeyLessThan + 1;
                }
            }
            final int n5 = n4;
            int n6 = n3;
            for (int i = n5; i < n; ++i) {
                final int spanSize2 = this.getSpanSize(i);
                final int n7 = n6 + spanSize2;
                if (n7 == n2) {
                    n6 = 0;
                }
                else if ((n6 = n7) > n2) {
                    n6 = spanSize2;
                }
            }
            if (n6 + spanSize <= n2) {
                return n6;
            }
            return 0;
        }
        
        public abstract int getSpanSize(final int p0);
        
        public void invalidateSpanGroupIndexCache() {
            this.mSpanGroupIndexCache.clear();
        }
        
        public void invalidateSpanIndexCache() {
            this.mSpanIndexCache.clear();
        }
        
        public boolean isSpanGroupIndexCacheEnabled() {
            return this.mCacheSpanGroupIndices;
        }
        
        public boolean isSpanIndexCacheEnabled() {
            return this.mCacheSpanIndices;
        }
        
        public void setSpanGroupIndexCacheEnabled(final boolean mCacheSpanGroupIndices) {
            if (!mCacheSpanGroupIndices) {
                this.mSpanGroupIndexCache.clear();
            }
            this.mCacheSpanGroupIndices = mCacheSpanGroupIndices;
        }
        
        public void setSpanIndexCacheEnabled(final boolean mCacheSpanIndices) {
            if (!mCacheSpanIndices) {
                this.mSpanGroupIndexCache.clear();
            }
            this.mCacheSpanIndices = mCacheSpanIndices;
        }
    }
}
