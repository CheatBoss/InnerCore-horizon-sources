package com.appboy.ui.contentcards.recycler;

import android.content.*;
import com.appboy.ui.*;
import android.graphics.*;
import android.view.*;
import android.support.v7.widget.*;
import com.appboy.ui.contentcards.*;

public class ContentCardsDividerItemDecoration extends RecyclerView$ItemDecoration
{
    private final Context mContext;
    private final int mItemDividerHeight;
    private final int mItemDividerMaxWidth;
    
    public ContentCardsDividerItemDecoration(final Context context) {
        this.mContext = context.getApplicationContext();
        this.mItemDividerHeight = this.getItemDividerHeight();
        this.mItemDividerMaxWidth = this.getContentCardsItemMaxWidth();
    }
    
    private int getContentCardsItemMaxWidth() {
        return this.mContext.getResources().getDimensionPixelSize(R$dimen.com_appboy_content_cards_max_width);
    }
    
    private int getItemDividerHeight() {
        return this.mContext.getResources().getDimensionPixelSize(R$dimen.com_appboy_content_cards_divider_height);
    }
    
    private int getLeftPaddingValue(final int n) {
        return Math.max((n - this.mItemDividerMaxWidth) / 2, 0);
    }
    
    public void getItemOffsets(final Rect rect, final View view, final RecyclerView recyclerView, final RecyclerView$State recyclerView$State) {
        super.getItemOffsets(rect, view, recyclerView, recyclerView$State);
        final boolean b = recyclerView.getAdapter() instanceof AppboyCardAdapter;
        int mItemDividerHeight = 0;
        boolean controlCardAtPosition = false;
        Label_0062: {
            if (b) {
                final AppboyCardAdapter appboyCardAdapter = (AppboyCardAdapter)recyclerView.getAdapter();
                final int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
                if (childAdapterPosition > 0) {
                    controlCardAtPosition = appboyCardAdapter.isControlCardAtPosition(childAdapterPosition);
                    break Label_0062;
                }
            }
            controlCardAtPosition = false;
        }
        if (!controlCardAtPosition) {
            mItemDividerHeight = this.mItemDividerHeight;
        }
        rect.top = mItemDividerHeight;
        rect.left = this.getLeftPaddingValue(recyclerView.getWidth());
    }
}
