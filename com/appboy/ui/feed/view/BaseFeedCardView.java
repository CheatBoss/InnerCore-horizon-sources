package com.appboy.ui.feed.view;

import com.appboy.models.cards.*;
import com.appboy.ui.widget.*;
import com.appboy.support.*;
import android.content.*;
import com.appboy.ui.*;
import android.widget.*;
import android.view.*;
import com.appboy.ui.actions.*;
import com.appboy.ui.feed.*;
import java.util.*;

public abstract class BaseFeedCardView<T extends Card> extends BaseCardView<T> implements Observer
{
    private static final String TAG;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(BaseCardView.class);
    }
    
    public BaseFeedCardView(final Context context) {
        super(context);
        ((LayoutInflater)context.getSystemService("layout_inflater")).inflate(this.getLayoutResource(), (ViewGroup)this);
        this.mImageSwitcher = (AppboyImageSwitcher)this.findViewById(R$id.com_appboy_newsfeed_item_read_indicator_image_switcher);
        if (this.mImageSwitcher != null) {
            this.mImageSwitcher.setFactory((ViewSwitcher$ViewFactory)new ViewSwitcher$ViewFactory() {
                public View makeView() {
                    return (View)new ImageView(BaseFeedCardView.this.mContext.getApplicationContext());
                }
            });
        }
        if (!this.isUnreadIndicatorEnabled() && this.mImageSwitcher != null) {
            this.mImageSwitcher.setVisibility(8);
        }
    }
    
    public Card getCard() {
        return this.mCard;
    }
    
    protected abstract int getLayoutResource();
    
    public View getProperViewFromInflatedStub(int n) {
        ((ViewStub)this.findViewById(n)).inflate();
        if (this.canUseFresco()) {
            n = R$id.com_appboy_stubbed_feed_drawee_view;
        }
        else {
            n = R$id.com_appboy_stubbed_feed_image_view;
        }
        return this.findViewById(n);
    }
    
    @Override
    protected boolean isClickHandled(final Context context, final Card card, final IAction action) {
        return AppboyFeedManager.getInstance().getFeedCardClickActionListener().onFeedCardClicked(context, card, action);
    }
    
    protected abstract void onSetCard(final T p0);
    
    public void setCard(final T mCard) {
        this.onSetCard((T)(this.mCard = mCard));
        mCard.addObserver(this);
        this.setCardViewedIndicator(this.mImageSwitcher, this.getCard());
    }
    
    @Override
    public void update(final Observable observable, final Object o) {
        this.setCardViewedIndicator(this.mImageSwitcher, this.getCard());
    }
}
