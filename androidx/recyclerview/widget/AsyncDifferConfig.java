package androidx.recyclerview.widget;

import androidx.annotation.*;
import java.util.concurrent.*;

public final class AsyncDifferConfig<T>
{
    @NonNull
    private final Executor mBackgroundThreadExecutor;
    @NonNull
    private final DiffUtil.ItemCallback<T> mDiffCallback;
    @Nullable
    private final Executor mMainThreadExecutor;
    
    AsyncDifferConfig(@Nullable final Executor mMainThreadExecutor, @NonNull final Executor mBackgroundThreadExecutor, @NonNull final DiffUtil.ItemCallback<T> mDiffCallback) {
        this.mMainThreadExecutor = mMainThreadExecutor;
        this.mBackgroundThreadExecutor = mBackgroundThreadExecutor;
        this.mDiffCallback = mDiffCallback;
    }
    
    @NonNull
    public Executor getBackgroundThreadExecutor() {
        return this.mBackgroundThreadExecutor;
    }
    
    @NonNull
    public DiffUtil.ItemCallback<T> getDiffCallback() {
        return this.mDiffCallback;
    }
    
    @Nullable
    @RestrictTo({ RestrictTo$Scope.LIBRARY })
    public Executor getMainThreadExecutor() {
        return this.mMainThreadExecutor;
    }
    
    public static final class Builder<T>
    {
        private static Executor sDiffExecutor;
        private static final Object sExecutorLock;
        private Executor mBackgroundThreadExecutor;
        private final DiffUtil.ItemCallback<T> mDiffCallback;
        @Nullable
        private Executor mMainThreadExecutor;
        
        static {
            sExecutorLock = new Object();
            Builder.sDiffExecutor = null;
        }
        
        public Builder(@NonNull final DiffUtil.ItemCallback<T> mDiffCallback) {
            this.mDiffCallback = mDiffCallback;
        }
        
        @NonNull
        public AsyncDifferConfig<T> build() {
            if (this.mBackgroundThreadExecutor == null) {
                synchronized (Builder.sExecutorLock) {
                    if (Builder.sDiffExecutor == null) {
                        Builder.sDiffExecutor = Executors.newFixedThreadPool(2);
                    }
                    // monitorexit(Builder.sExecutorLock)
                    this.mBackgroundThreadExecutor = Builder.sDiffExecutor;
                }
            }
            return new AsyncDifferConfig<T>(this.mMainThreadExecutor, this.mBackgroundThreadExecutor, this.mDiffCallback);
        }
        
        @NonNull
        public Builder<T> setBackgroundThreadExecutor(final Executor mBackgroundThreadExecutor) {
            this.mBackgroundThreadExecutor = mBackgroundThreadExecutor;
            return this;
        }
        
        @NonNull
        @RestrictTo({ RestrictTo$Scope.LIBRARY })
        public Builder<T> setMainThreadExecutor(final Executor mMainThreadExecutor) {
            this.mMainThreadExecutor = mMainThreadExecutor;
            return this;
        }
    }
}
