package com.appboy.ui.contentcards.view;

import com.appboy.models.cards.*;
import com.appboy.ui.widget.*;
import android.content.*;
import com.appboy.ui.actions.*;
import android.view.*;
import com.appboy.ui.contentcards.*;
import android.widget.*;
import com.facebook.drawee.view.*;

public abstract class BaseContentCardView<T extends Card> extends BaseCardView<T>
{
    public BaseContentCardView(final Context context) {
        super(context);
    }
    
    public void bindViewHolder(final ContentCardViewHolder contentCardViewHolder, final T t) {
        contentCardViewHolder.setPinnedIconVisible(t.getIsPinned());
        contentCardViewHolder.setUnreadBarVisible(this.mAppboyConfigurationProvider.isContentCardsUnreadVisualIndicatorEnabled() && !t.isRead());
        contentCardViewHolder.itemView.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            final /* synthetic */ UriAction val$mCardAction = BaseCardView.getUriActionForCard(t);
            
            public void onClick(final View view) {
                final BaseContentCardView this$0 = BaseContentCardView.this;
                this$0.handleCardClick(this$0.mContext, t, this.val$mCardAction, BaseContentCardView.this.getClassLogTag());
            }
        });
    }
    
    public abstract ContentCardViewHolder createViewHolder(final ViewGroup p0);
    
    @Override
    protected boolean isClickHandled(final Context context, final Card card, final IAction action) {
        return AppboyContentCardsManager.getInstance().getContentCardsActionListener().onContentCardClicked(context, card, action);
    }
    
    public void setOptionalCardImage(final ImageView imageView, final SimpleDraweeView simpleDraweeView, float n, final String s, final float n2) {
        boolean b;
        if (n != 0.0f) {
            b = true;
        }
        else {
            b = false;
            n = n2;
        }
        if (this.canUseFresco()) {
            if (simpleDraweeView != null) {
                this.setSimpleDraweeToUrl(simpleDraweeView, s, n, b);
            }
        }
        else if (imageView != null) {
            this.setImageViewToUrl(imageView, s, n, b);
        }
    }
}
