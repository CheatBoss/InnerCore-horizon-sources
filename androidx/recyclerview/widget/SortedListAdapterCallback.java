package androidx.recyclerview.widget;

public abstract class SortedListAdapterCallback<T2> extends Callback<T2>
{
    final RecyclerView.Adapter mAdapter;
    
    public SortedListAdapterCallback(final RecyclerView.Adapter mAdapter) {
        this.mAdapter = mAdapter;
    }
    
    @Override
    public void onChanged(final int n, final int n2) {
        this.mAdapter.notifyItemRangeChanged(n, n2);
    }
    
    @Override
    public void onChanged(final int n, final int n2, final Object o) {
        this.mAdapter.notifyItemRangeChanged(n, n2, o);
    }
    
    @Override
    public void onInserted(final int n, final int n2) {
        this.mAdapter.notifyItemRangeInserted(n, n2);
    }
    
    @Override
    public void onMoved(final int n, final int n2) {
        this.mAdapter.notifyItemMoved(n, n2);
    }
    
    @Override
    public void onRemoved(final int n, final int n2) {
        this.mAdapter.notifyItemRangeRemoved(n, n2);
    }
}
