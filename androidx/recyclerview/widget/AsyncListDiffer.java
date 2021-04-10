package androidx.recyclerview.widget;

import androidx.annotation.*;
import java.util.concurrent.*;
import java.util.*;
import android.os.*;

public class AsyncListDiffer<T>
{
    private static final Executor sMainThreadExecutor;
    final AsyncDifferConfig<T> mConfig;
    @Nullable
    private List<T> mList;
    private final List<ListListener<T>> mListeners;
    Executor mMainThreadExecutor;
    int mMaxScheduledGeneration;
    @NonNull
    private List<T> mReadOnlyList;
    private final ListUpdateCallback mUpdateCallback;
    
    static {
        sMainThreadExecutor = new MainThreadExecutor();
    }
    
    public AsyncListDiffer(@NonNull final ListUpdateCallback mUpdateCallback, @NonNull final AsyncDifferConfig<T> mConfig) {
        this.mListeners = new CopyOnWriteArrayList<ListListener<T>>();
        this.mReadOnlyList = Collections.emptyList();
        this.mUpdateCallback = mUpdateCallback;
        this.mConfig = mConfig;
        if (mConfig.getMainThreadExecutor() != null) {
            this.mMainThreadExecutor = mConfig.getMainThreadExecutor();
            return;
        }
        this.mMainThreadExecutor = AsyncListDiffer.sMainThreadExecutor;
    }
    
    public AsyncListDiffer(@NonNull final RecyclerView.Adapter adapter, @NonNull final DiffUtil.ItemCallback<T> itemCallback) {
        this(new AdapterListUpdateCallback(adapter), new AsyncDifferConfig.Builder<T>(itemCallback).build());
    }
    
    private void onCurrentListChanged(@NonNull final List<T> list, @Nullable final Runnable runnable) {
        final Iterator<ListListener<T>> iterator = this.mListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onCurrentListChanged(list, this.mReadOnlyList);
        }
        if (runnable != null) {
            runnable.run();
        }
    }
    
    public void addListListener(@NonNull final ListListener<T> listListener) {
        this.mListeners.add(listListener);
    }
    
    @NonNull
    public List<T> getCurrentList() {
        return this.mReadOnlyList;
    }
    
    void latchList(@NonNull final List<T> mList, @NonNull final DiffUtil.DiffResult diffResult, @Nullable final Runnable runnable) {
        final List<T> mReadOnlyList = this.mReadOnlyList;
        this.mList = mList;
        this.mReadOnlyList = Collections.unmodifiableList((List<? extends T>)mList);
        diffResult.dispatchUpdatesTo(this.mUpdateCallback);
        this.onCurrentListChanged(mReadOnlyList, runnable);
    }
    
    public void removeListListener(@NonNull final ListListener<T> listListener) {
        this.mListeners.remove(listListener);
    }
    
    public void submitList(@Nullable final List<T> list) {
        this.submitList(list, null);
    }
    
    public void submitList(@Nullable final List<T> mList, @Nullable final Runnable runnable) {
        final int mMaxScheduledGeneration = this.mMaxScheduledGeneration + 1;
        this.mMaxScheduledGeneration = mMaxScheduledGeneration;
        if (mList == this.mList) {
            if (runnable != null) {
                runnable.run();
            }
            return;
        }
        final List<T> mReadOnlyList = this.mReadOnlyList;
        if (mList == null) {
            final int size = this.mList.size();
            this.mList = null;
            this.mReadOnlyList = Collections.emptyList();
            this.mUpdateCallback.onRemoved(0, size);
            this.onCurrentListChanged(mReadOnlyList, runnable);
            return;
        }
        if (this.mList == null) {
            this.mList = mList;
            this.mReadOnlyList = Collections.unmodifiableList((List<? extends T>)mList);
            this.mUpdateCallback.onInserted(0, mList.size());
            this.onCurrentListChanged(mReadOnlyList, runnable);
            return;
        }
        this.mConfig.getBackgroundThreadExecutor().execute(new Runnable() {
            final /* synthetic */ List val$oldList = AsyncListDiffer.this.mList;
            
            @Override
            public void run() {
                AsyncListDiffer.this.mMainThreadExecutor.execute(new Runnable() {
                    final /* synthetic */ DiffUtil.DiffResult val$result = DiffUtil.calculateDiff((DiffUtil.Callback)new DiffUtil.Callback(this) {
                        @Override
                        public boolean areContentsTheSame(final int n, final int n2) {
                            final T value = Runnable.this.val$oldList.get(n);
                            final T value2 = mList.get(n2);
                            if (value != null && value2 != null) {
                                return AsyncListDiffer.this.mConfig.getDiffCallback().areContentsTheSame(value, value2);
                            }
                            if (value == null && value2 == null) {
                                return true;
                            }
                            throw new AssertionError();
                        }
                        
                        @Override
                        public boolean areItemsTheSame(final int n, final int n2) {
                            final T value = Runnable.this.val$oldList.get(n);
                            final T value2 = mList.get(n2);
                            if (value != null && value2 != null) {
                                return AsyncListDiffer.this.mConfig.getDiffCallback().areItemsTheSame(value, value2);
                            }
                            return value == null && value2 == null;
                        }
                        
                        @Nullable
                        @Override
                        public Object getChangePayload(final int n, final int n2) {
                            final T value = Runnable.this.val$oldList.get(n);
                            final T value2 = mList.get(n2);
                            if (value != null && value2 != null) {
                                return AsyncListDiffer.this.mConfig.getDiffCallback().getChangePayload(value, value2);
                            }
                            throw new AssertionError();
                        }
                        
                        @Override
                        public int getNewListSize() {
                            return mList.size();
                        }
                        
                        @Override
                        public int getOldListSize() {
                            return Runnable.this.val$oldList.size();
                        }
                    });
                    
                    @Override
                    public void run() {
                        if (AsyncListDiffer.this.mMaxScheduledGeneration == mMaxScheduledGeneration) {
                            AsyncListDiffer.this.latchList(mList, this.val$result, runnable);
                        }
                    }
                });
            }
        });
    }
    
    public interface ListListener<T>
    {
        void onCurrentListChanged(@NonNull final List<T> p0, @NonNull final List<T> p1);
    }
    
    private static class MainThreadExecutor implements Executor
    {
        final Handler mHandler;
        
        MainThreadExecutor() {
            this.mHandler = new Handler(Looper.getMainLooper());
        }
        
        @Override
        public void execute(@NonNull final Runnable runnable) {
            this.mHandler.post(runnable);
        }
    }
}
