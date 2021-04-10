package com.appboy.ui.contentcards.recycler;

import android.support.v7.widget.helper.*;
import android.support.v7.widget.*;

public class SimpleItemTouchHelperCallback extends ItemTouchHelper$Callback
{
    private final ItemTouchHelperAdapter mAdapter;
    
    public SimpleItemTouchHelperCallback(final ItemTouchHelperAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }
    
    public int getMovementFlags(final RecyclerView recyclerView, final RecyclerView$ViewHolder recyclerView$ViewHolder) {
        int n;
        if (this.mAdapter.isItemDismissable(recyclerView$ViewHolder.getAdapterPosition())) {
            n = 16;
        }
        else {
            n = 0;
        }
        return makeMovementFlags(0, n);
    }
    
    public boolean isItemViewSwipeEnabled() {
        return true;
    }
    
    public boolean isLongPressDragEnabled() {
        return false;
    }
    
    public boolean onMove(final RecyclerView recyclerView, final RecyclerView$ViewHolder recyclerView$ViewHolder, final RecyclerView$ViewHolder recyclerView$ViewHolder2) {
        return false;
    }
    
    public void onSwiped(final RecyclerView$ViewHolder recyclerView$ViewHolder, final int n) {
        this.mAdapter.onItemDismiss(recyclerView$ViewHolder.getAdapterPosition());
    }
}
