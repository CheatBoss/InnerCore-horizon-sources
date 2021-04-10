package androidx.recyclerview.widget;

import java.util.*;
import androidx.annotation.*;

public abstract class ListAdapter<T, VH extends ViewHolder> extends Adapter<VH>
{
    final AsyncListDiffer<T> mDiffer;
    private final AsyncListDiffer.ListListener<T> mListener;
    
    protected ListAdapter(@NonNull final AsyncDifferConfig<T> asyncDifferConfig) {
        this.mListener = new AsyncListDiffer.ListListener<T>() {
            @Override
            public void onCurrentListChanged(@NonNull final List<T> list, @NonNull final List<T> list2) {
                ListAdapter.this.onCurrentListChanged(list, list2);
            }
        };
        (this.mDiffer = new AsyncListDiffer<T>(new AdapterListUpdateCallback(this), asyncDifferConfig)).addListListener(this.mListener);
    }
    
    protected ListAdapter(@NonNull final DiffUtil.ItemCallback<T> itemCallback) {
        this.mListener = new AsyncListDiffer.ListListener<T>() {
            @Override
            public void onCurrentListChanged(@NonNull final List<T> list, @NonNull final List<T> list2) {
                ListAdapter.this.onCurrentListChanged(list, list2);
            }
        };
        (this.mDiffer = new AsyncListDiffer<T>(new AdapterListUpdateCallback(this), new AsyncDifferConfig.Builder<T>(itemCallback).build())).addListListener(this.mListener);
    }
    
    @NonNull
    public List<T> getCurrentList() {
        return this.mDiffer.getCurrentList();
    }
    
    protected T getItem(final int n) {
        return this.mDiffer.getCurrentList().get(n);
    }
    
    @Override
    public int getItemCount() {
        return this.mDiffer.getCurrentList().size();
    }
    
    public void onCurrentListChanged(@NonNull final List<T> list, @NonNull final List<T> list2) {
    }
    
    public void submitList(@Nullable final List<T> list) {
        this.mDiffer.submitList(list);
    }
    
    public void submitList(@Nullable final List<T> list, @Nullable final Runnable runnable) {
        this.mDiffer.submitList(list, runnable);
    }
}
