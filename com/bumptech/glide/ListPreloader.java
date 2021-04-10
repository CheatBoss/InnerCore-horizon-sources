package com.bumptech.glide;

import android.widget.*;
import com.bumptech.glide.request.target.*;
import com.bumptech.glide.request.animation.*;
import java.util.*;
import com.bumptech.glide.util.*;

public class ListPreloader<T> implements AbsListView$OnScrollListener
{
    private boolean isIncreasing;
    private int lastEnd;
    private int lastFirstVisible;
    private int lastStart;
    private final int maxPreload;
    private final PreloadSizeProvider<T> preloadDimensionProvider;
    private final PreloadModelProvider<T> preloadModelProvider;
    private final PreloadTargetQueue preloadTargetQueue;
    private int totalItemCount;
    
    @Deprecated
    public ListPreloader(final int maxPreload) {
        this.isIncreasing = true;
        this.preloadModelProvider = (PreloadModelProvider<T>)new PreloadModelProvider<T>() {
            @Override
            public List<T> getPreloadItems(final int n) {
                return ListPreloader.this.getItems(n, n + 1);
            }
            
            @Override
            public GenericRequestBuilder getPreloadRequestBuilder(final T t) {
                return ListPreloader.this.getRequestBuilder(t);
            }
        };
        this.preloadDimensionProvider = (PreloadSizeProvider<T>)new PreloadSizeProvider<T>() {
            @Override
            public int[] getPreloadSize(final T t, final int n, final int n2) {
                return ListPreloader.this.getDimensions(t);
            }
        };
        this.maxPreload = maxPreload;
        this.preloadTargetQueue = new PreloadTargetQueue(maxPreload + 1);
    }
    
    public ListPreloader(final PreloadModelProvider<T> preloadModelProvider, final PreloadSizeProvider<T> preloadDimensionProvider, final int maxPreload) {
        this.isIncreasing = true;
        this.preloadModelProvider = preloadModelProvider;
        this.preloadDimensionProvider = preloadDimensionProvider;
        this.maxPreload = maxPreload;
        this.preloadTargetQueue = new PreloadTargetQueue(maxPreload + 1);
    }
    
    private void cancelAll() {
        for (int i = 0; i < this.maxPreload; ++i) {
            Glide.clear(this.preloadTargetQueue.next(0, 0));
        }
    }
    
    private void preload(int i, final int n) {
        int max;
        int min;
        if (i < n) {
            max = Math.max(this.lastEnd, i);
            min = n;
        }
        else {
            max = n;
            min = Math.min(this.lastStart, i);
        }
        final int min2 = Math.min(this.totalItemCount, min);
        final int min3 = Math.min(this.totalItemCount, Math.max(0, max));
        if (i < n) {
            for (i = min3; i < min2; ++i) {
                this.preloadAdapterPosition(this.preloadModelProvider.getPreloadItems(i), i, true);
            }
        }
        else {
            for (i = min2 - 1; i >= min3; --i) {
                this.preloadAdapterPosition(this.preloadModelProvider.getPreloadItems(i), i, false);
            }
        }
        this.lastStart = min3;
        this.lastEnd = min2;
    }
    
    private void preload(final int n, final boolean isIncreasing) {
        if (this.isIncreasing != isIncreasing) {
            this.isIncreasing = isIncreasing;
            this.cancelAll();
        }
        int maxPreload;
        if (isIncreasing) {
            maxPreload = this.maxPreload;
        }
        else {
            maxPreload = -this.maxPreload;
        }
        this.preload(n, maxPreload + n);
    }
    
    private void preloadAdapterPosition(final List<T> list, final int n, final boolean b) {
        final int size = list.size();
        if (b) {
            for (int i = 0; i < size; ++i) {
                this.preloadItem(list.get(i), n, i);
            }
        }
        else {
            for (int j = size - 1; j >= 0; --j) {
                this.preloadItem(list.get(j), n, j);
            }
        }
    }
    
    private void preloadItem(final T t, final int n, final int n2) {
        final int[] preloadSize = this.preloadDimensionProvider.getPreloadSize(t, n, n2);
        if (preloadSize != null) {
            this.preloadModelProvider.getPreloadRequestBuilder(t).into(this.preloadTargetQueue.next(preloadSize[0], preloadSize[1]));
        }
    }
    
    @Deprecated
    protected int[] getDimensions(final T t) {
        throw new IllegalStateException("You must either provide a PreloadDimensionProvider or override getDimensions()");
    }
    
    @Deprecated
    protected List<T> getItems(final int n, final int n2) {
        throw new IllegalStateException("You must either provide a PreloadModelProvider or override getItems()");
    }
    
    @Deprecated
    protected GenericRequestBuilder getRequestBuilder(final T t) {
        throw new IllegalStateException("You must either provide a PreloadModelProvider, or override getRequestBuilder()");
    }
    
    public void onScroll(final AbsListView absListView, final int lastFirstVisible, final int n, final int totalItemCount) {
        this.totalItemCount = totalItemCount;
        if (lastFirstVisible > this.lastFirstVisible) {
            this.preload(lastFirstVisible + n, true);
        }
        else if (lastFirstVisible < this.lastFirstVisible) {
            this.preload(lastFirstVisible, false);
        }
        this.lastFirstVisible = lastFirstVisible;
    }
    
    public void onScrollStateChanged(final AbsListView absListView, final int n) {
    }
    
    public interface PreloadModelProvider<U>
    {
        List<U> getPreloadItems(final int p0);
        
        GenericRequestBuilder getPreloadRequestBuilder(final U p0);
    }
    
    public interface PreloadSizeProvider<T>
    {
        int[] getPreloadSize(final T p0, final int p1, final int p2);
    }
    
    private static class PreloadTarget extends BaseTarget<Object>
    {
        private int photoHeight;
        private int photoWidth;
        
        @Override
        public void getSize(final SizeReadyCallback sizeReadyCallback) {
            sizeReadyCallback.onSizeReady(this.photoWidth, this.photoHeight);
        }
        
        @Override
        public void onResourceReady(final Object o, final GlideAnimation<? super Object> glideAnimation) {
        }
    }
    
    private static final class PreloadTargetQueue
    {
        private final Queue<PreloadTarget> queue;
        
        public PreloadTargetQueue(final int n) {
            this.queue = Util.createQueue(n);
            for (int i = 0; i < n; ++i) {
                this.queue.offer(new PreloadTarget());
            }
        }
        
        public PreloadTarget next(final int n, final int n2) {
            final PreloadTarget preloadTarget = this.queue.poll();
            this.queue.offer(preloadTarget);
            preloadTarget.photoWidth = n;
            preloadTarget.photoHeight = n2;
            return preloadTarget;
        }
    }
}
