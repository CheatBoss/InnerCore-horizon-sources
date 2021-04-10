package com.appboy.ui.contentcards.handlers;

import com.appboy.enums.*;
import android.content.*;
import android.widget.*;
import java.util.*;
import com.appboy.models.cards.*;
import com.appboy.ui.contentcards.view.*;
import android.view.*;

public class DefaultContentCardsViewBindingHandler implements IContentCardsViewBindingHandler
{
    private final Map<CardType, BaseContentCardView> mContentCardViewCache;
    
    public DefaultContentCardsViewBindingHandler() {
        this.mContentCardViewCache = new HashMap<CardType, BaseContentCardView>();
    }
    
    BaseContentCardView getContentCardsViewFromCache(final Context context, final CardType cardType) {
        if (!this.mContentCardViewCache.containsKey(cardType)) {
            final int n = DefaultContentCardsViewBindingHandler$1.$SwitchMap$com$appboy$enums$CardType[cardType.ordinal()];
            RelativeLayout relativeLayout;
            if (n != 1) {
                if (n != 2) {
                    if (n != 3) {
                        if (n != 4) {
                            relativeLayout = new DefaultContentCardView(context);
                        }
                        else {
                            relativeLayout = new TextAnnouncementContentCardView(context);
                        }
                    }
                    else {
                        relativeLayout = new ShortNewsContentCardView(context);
                    }
                }
                else {
                    relativeLayout = new CaptionedImageContentCardView(context);
                }
            }
            else {
                relativeLayout = new BannerImageContentCardView(context);
            }
            this.mContentCardViewCache.put(cardType, (BaseContentCardView)relativeLayout);
        }
        return this.mContentCardViewCache.get(cardType);
    }
    
    @Override
    public int getItemViewType(final Context context, final List<Card> list, final int n) {
        return list.get(n).getCardType().getValue();
    }
    
    @Override
    public void onBindViewHolder(final Context context, final List<Card> list, final ContentCardViewHolder contentCardViewHolder, final int n) {
        final Card card = list.get(n);
        this.getContentCardsViewFromCache(context, card.getCardType()).bindViewHolder(contentCardViewHolder, card);
    }
    
    @Override
    public ContentCardViewHolder onCreateViewHolder(final Context context, final List<Card> list, final ViewGroup viewGroup, final int n) {
        return this.getContentCardsViewFromCache(context, CardType.fromValue(n)).createViewHolder(viewGroup);
    }
}
