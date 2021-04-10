package com.appboy.ui.adapters;

import android.widget.*;
import android.content.*;
import com.appboy.support.*;
import com.appboy.models.cards.*;
import android.view.*;
import com.appboy.ui.widget.*;
import com.appboy.ui.feed.view.*;
import java.util.*;

public class AppboyListAdapter extends ArrayAdapter<Card>
{
    private static final String TAG;
    private final Set<String> mCardIdImpressions;
    private final Context mContext;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(AppboyListAdapter.class);
    }
    
    public AppboyListAdapter(final Context mContext, final int n, final List<Card> list) {
        super(mContext, n, (List)list);
        this.mContext = mContext;
        this.mCardIdImpressions = new HashSet<String>();
    }
    
    private void logCardImpression(final Card card) {
        final String id = card.getId();
        String s;
        StringBuilder sb;
        String s2;
        if (!this.mCardIdImpressions.contains(id)) {
            this.mCardIdImpressions.add(id);
            card.logImpression();
            s = AppboyListAdapter.TAG;
            sb = new StringBuilder();
            s2 = "Logged impression for card ";
        }
        else {
            s = AppboyListAdapter.TAG;
            sb = new StringBuilder();
            s2 = "Already counted impression for card ";
        }
        sb.append(s2);
        sb.append(id);
        AppboyLogger.v(s, sb.toString());
        if (!card.getViewed()) {
            card.setViewed(true);
        }
    }
    
    public void add(final Card card) {
        synchronized (this) {
            super.add((Object)card);
        }
    }
    
    public void batchSetCardsToRead(int i, int min) {
        String s = null;
        String s2 = null;
        Label_0015: {
            if (this.getCount() != 0) {
                Card card;
                for (i = Math.max(0, i), min = Math.min(this.getCount(), min); i < min; ++i) {
                    card = (Card)this.getItem(i);
                    if (card == null) {
                        s = AppboyListAdapter.TAG;
                        s2 = "Card was null in setting some cards to viewed.";
                        break Label_0015;
                    }
                    if (!card.isRead()) {
                        card.setIsRead(true);
                    }
                }
                return;
            }
            s = AppboyListAdapter.TAG;
            s2 = "mAdapter is empty in setting some cards to viewed.";
        }
        AppboyLogger.d(s, s2);
    }
    
    public int getItemViewType(final int n) {
        final Card card = (Card)this.getItem(n);
        if (card instanceof BannerImageCard) {
            return 1;
        }
        if (card instanceof CaptionedImageCard) {
            return 2;
        }
        if (card instanceof CrossPromotionSmallCard) {
            return 3;
        }
        if (card instanceof ShortNewsCard) {
            return 4;
        }
        if (card instanceof TextAnnouncementCard) {
            return 5;
        }
        return 0;
    }
    
    public View getView(final int n, final View view, final ViewGroup viewGroup) {
        final Card card = (Card)this.getItem(n);
        Observer observer;
        if (view == null) {
            if (card instanceof BannerImageCard) {
                observer = new BannerImageCardView(this.mContext);
            }
            else if (card instanceof CaptionedImageCard) {
                observer = new CaptionedImageCardView(this.mContext);
            }
            else if (card instanceof CrossPromotionSmallCard) {
                observer = new CrossPromotionSmallCardView(this.mContext);
            }
            else if (card instanceof ShortNewsCard) {
                observer = new ShortNewsCardView(this.mContext);
            }
            else if (card instanceof TextAnnouncementCard) {
                observer = new TextAnnouncementCardView(this.mContext);
            }
            else {
                observer = new DefaultCardView(this.mContext);
            }
        }
        else {
            final String tag = AppboyListAdapter.TAG;
            final StringBuilder sb = new StringBuilder();
            sb.append("Reusing convertView for rendering of item ");
            sb.append(n);
            AppboyLogger.v(tag, sb.toString());
            observer = (BaseFeedCardView)view;
        }
        final String tag2 = AppboyListAdapter.TAG;
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Using view of type: ");
        sb2.append(((BaseFeedCardView<Card>)observer).getClass().getName());
        sb2.append(" for card at position ");
        sb2.append(n);
        sb2.append(": ");
        sb2.append(card.toString());
        AppboyLogger.v(tag2, sb2.toString());
        ((BaseFeedCardView<Card>)observer).setCard(card);
        this.logCardImpression(card);
        return (View)observer;
    }
    
    public int getViewTypeCount() {
        return 8;
    }
    
    public void replaceFeed(final List<Card> list) {
        // monitorenter(this)
        int i = 0;
        try {
            this.setNotifyOnChange(false);
            if (list == null) {
                this.clear();
                this.notifyDataSetChanged();
                return;
            }
            final String tag = AppboyListAdapter.TAG;
            final StringBuilder sb = new StringBuilder();
            sb.append("Replacing existing feed of ");
            sb.append(this.getCount());
            sb.append(" cards with new feed containing ");
            sb.append(list.size());
            sb.append(" cards.");
            AppboyLogger.d(tag, sb.toString());
            final int size = list.size();
            int n = 0;
            while (i < this.getCount()) {
                final Card card = (Card)this.getItem(i);
                Card card2 = null;
                if (n < size) {
                    card2 = list.get(n);
                }
                if (card2 != null && card2.isEqualToCard(card)) {
                    ++i;
                    ++n;
                }
                else {
                    this.remove((Object)card);
                }
            }
            super.addAll((Collection)list.subList(n, size));
            this.notifyDataSetChanged();
        }
        finally {
        }
        // monitorexit(this)
    }
    
    public void resetCardImpressionTracker() {
        this.mCardIdImpressions.clear();
    }
}
