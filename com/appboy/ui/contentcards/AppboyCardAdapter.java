package com.appboy.ui.contentcards;

import com.appboy.ui.contentcards.view.*;
import com.appboy.ui.contentcards.recycler.*;
import com.appboy.models.cards.*;
import com.appboy.ui.contentcards.handlers.*;
import android.content.*;
import android.os.*;
import com.appboy.support.*;
import java.util.*;
import android.support.v7.widget.*;
import android.view.*;
import android.support.v7.util.*;

public class AppboyCardAdapter extends RecyclerView$Adapter<ContentCardViewHolder> implements ItemTouchHelperAdapter
{
    private static final String TAG;
    private List<Card> mCardData;
    private final IContentCardsViewBindingHandler mContentCardsViewBindingHandler;
    private final Context mContext;
    private final Handler mHandler;
    private Set<String> mImpressedCardIds;
    private final LinearLayoutManager mLayoutManager;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(AppboyCardAdapter.class);
    }
    
    public AppboyCardAdapter(final Context mContext, final LinearLayoutManager mLayoutManager, final List<Card> mCardData, final IContentCardsViewBindingHandler mContentCardsViewBindingHandler) {
        this.mImpressedCardIds = new HashSet<String>();
        this.mContext = mContext;
        this.mCardData = mCardData;
        this.mHandler = new Handler();
        this.mLayoutManager = mLayoutManager;
        this.mContentCardsViewBindingHandler = mContentCardsViewBindingHandler;
        this.setHasStableIds(true);
    }
    
    public List<String> getImpressedCardIds() {
        return new ArrayList<String>(this.mImpressedCardIds);
    }
    
    public int getItemCount() {
        return this.mCardData.size();
    }
    
    public long getItemId(final int n) {
        return this.mCardData.get(n).getId().hashCode();
    }
    
    public int getItemViewType(final int n) {
        return this.mContentCardsViewBindingHandler.getItemViewType(this.mContext, this.mCardData, n);
    }
    
    boolean isAdapterPositionOnScreen(final int n) {
        final int min = Math.min(this.mLayoutManager.findFirstVisibleItemPosition(), this.mLayoutManager.findFirstCompletelyVisibleItemPosition());
        final int max = Math.max(this.mLayoutManager.findLastVisibleItemPosition(), this.mLayoutManager.findLastCompletelyVisibleItemPosition());
        return min <= n && max >= n;
    }
    
    public boolean isControlCardAtPosition(final int n) {
        return n >= 0 && n < this.mCardData.size() && this.mCardData.get(n).isControl();
    }
    
    public boolean isItemDismissable(final int n) {
        return !this.mCardData.isEmpty() && this.mCardData.get(n).getIsDismissible();
    }
    
    void logImpression(final Card card) {
        String s;
        StringBuilder sb;
        String s2;
        if (!this.mImpressedCardIds.contains(card.getId())) {
            card.logImpression();
            this.mImpressedCardIds.add(card.getId());
            s = AppboyCardAdapter.TAG;
            sb = new StringBuilder();
            s2 = "Logged impression for card ";
        }
        else {
            s = AppboyCardAdapter.TAG;
            sb = new StringBuilder();
            s2 = "Already counted impression for card ";
        }
        sb.append(s2);
        sb.append(card.getId());
        AppboyLogger.v(s, sb.toString());
        if (!card.getViewed()) {
            card.setViewed(true);
        }
    }
    
    public void markOnScreenCardsAsRead() {
        if (this.mCardData.isEmpty()) {
            AppboyLogger.d(AppboyCardAdapter.TAG, "Card list is empty. Not marking on-screen cards as read.");
            return;
        }
        final int firstVisibleItemPosition = this.mLayoutManager.findFirstVisibleItemPosition();
        final int lastVisibleItemPosition = this.mLayoutManager.findLastVisibleItemPosition();
        if (firstVisibleItemPosition >= 0 && lastVisibleItemPosition >= 0) {
            for (int i = firstVisibleItemPosition; i <= lastVisibleItemPosition; ++i) {
                this.mCardData.get(i).setIsRead(true);
            }
            this.mHandler.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    final int val$lastVisibleIndex = lastVisibleItemPosition;
                    final int val$firstVisibleIndex = firstVisibleItemPosition;
                    AppboyCardAdapter.this.notifyItemRangeChanged(val$firstVisibleIndex, val$lastVisibleIndex - val$firstVisibleIndex + 1);
                }
            });
            return;
        }
        final String tag = AppboyCardAdapter.TAG;
        final StringBuilder sb = new StringBuilder();
        sb.append("Not marking all on-screen cards as read. Either the first or last index is negative. First visible: ");
        sb.append(firstVisibleItemPosition);
        sb.append(" . Last visible: ");
        sb.append(lastVisibleItemPosition);
        AppboyLogger.d(tag, sb.toString());
    }
    
    public void onBindViewHolder(final ContentCardViewHolder contentCardViewHolder, final int n) {
        this.mContentCardsViewBindingHandler.onBindViewHolder(this.mContext, this.mCardData, contentCardViewHolder, n);
    }
    
    public ContentCardViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
        return this.mContentCardsViewBindingHandler.onCreateViewHolder(this.mContext, this.mCardData, viewGroup, n);
    }
    
    public void onItemDismiss(final int n) {
        final Card card = this.mCardData.remove(n);
        card.setIsDismissed(true);
        this.notifyItemRemoved(n);
        AppboyContentCardsManager.getInstance().getContentCardsActionListener().onContentCardDismissed(this.mContext, card);
    }
    
    public void onViewAttachedToWindow(final ContentCardViewHolder contentCardViewHolder) {
        super.onViewAttachedToWindow((RecyclerView$ViewHolder)contentCardViewHolder);
        if (this.mCardData.isEmpty()) {
            return;
        }
        final int adapterPosition = contentCardViewHolder.getAdapterPosition();
        if (adapterPosition != -1 && this.isAdapterPositionOnScreen(adapterPosition)) {
            this.logImpression(this.mCardData.get(adapterPosition));
            return;
        }
        final String tag = AppboyCardAdapter.TAG;
        final StringBuilder sb = new StringBuilder();
        sb.append("The card at position ");
        sb.append(adapterPosition);
        sb.append(" isn't on screen or does not have a valid adapter position. Not logging impression.");
        AppboyLogger.v(tag, sb.toString());
    }
    
    public void onViewDetachedFromWindow(final ContentCardViewHolder contentCardViewHolder) {
        super.onViewDetachedFromWindow((RecyclerView$ViewHolder)contentCardViewHolder);
        if (this.mCardData.isEmpty()) {
            return;
        }
        final int adapterPosition = contentCardViewHolder.getAdapterPosition();
        if (adapterPosition != -1 && this.isAdapterPositionOnScreen(adapterPosition)) {
            this.mCardData.get(adapterPosition).setIsRead(true);
            this.mHandler.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    AppboyCardAdapter.this.notifyItemChanged(adapterPosition);
                }
            });
            return;
        }
        final String tag = AppboyCardAdapter.TAG;
        final StringBuilder sb = new StringBuilder();
        sb.append("The card at position ");
        sb.append(adapterPosition);
        sb.append(" isn't on screen or does not have a valid adapter position. Not marking as read.");
        AppboyLogger.v(tag, sb.toString());
    }
    
    public void replaceCards(final List<Card> list) {
        synchronized (this) {
            final DiffUtil$DiffResult calculateDiff = DiffUtil.calculateDiff((DiffUtil$Callback)new CardListDiffCallback(this.mCardData, list));
            this.mCardData.clear();
            this.mCardData.addAll(list);
            calculateDiff.dispatchUpdatesTo((RecyclerView$Adapter)this);
        }
    }
    
    public void setImpressedCardIds(final List<String> list) {
        this.mImpressedCardIds = new HashSet<String>(list);
    }
    
    private class CardListDiffCallback extends DiffUtil$Callback
    {
        private final List<Card> mNewCards;
        private final List<Card> mOldCards;
        
        CardListDiffCallback(final List<Card> mOldCards, final List<Card> mNewCards) {
            this.mOldCards = mOldCards;
            this.mNewCards = mNewCards;
        }
        
        private boolean doItemsShareIds(final int n, final int n2) {
            return this.mOldCards.get(n).getId().equals(this.mNewCards.get(n2).getId());
        }
        
        public boolean areContentsTheSame(final int n, final int n2) {
            return this.doItemsShareIds(n, n2);
        }
        
        public boolean areItemsTheSame(final int n, final int n2) {
            return this.doItemsShareIds(n, n2);
        }
        
        public int getNewListSize() {
            return this.mNewCards.size();
        }
        
        public int getOldListSize() {
            return this.mOldCards.size();
        }
    }
}
