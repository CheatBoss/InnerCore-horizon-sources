package com.appboy.ui.contentcards.recycler;

public interface ItemTouchHelperAdapter
{
    boolean isItemDismissable(final int p0);
    
    void onItemDismiss(final int p0);
}
