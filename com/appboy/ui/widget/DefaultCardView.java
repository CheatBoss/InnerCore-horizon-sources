package com.appboy.ui.widget;

import com.appboy.ui.feed.view.*;
import com.appboy.models.cards.*;
import com.appboy.support.*;
import android.content.*;
import com.appboy.ui.*;

public class DefaultCardView extends BaseFeedCardView<Card>
{
    private static final String TAG;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(DefaultCardView.class);
    }
    
    public DefaultCardView(final Context context) {
        this(context, null);
    }
    
    public DefaultCardView(final Context context, final Card card) {
        super(context);
        if (card != null) {
            this.setCard(card);
        }
    }
    
    @Override
    protected int getLayoutResource() {
        return R$layout.com_appboy_default_card;
    }
    
    public void onSetCard(final Card card) {
        final String tag = DefaultCardView.TAG;
        final StringBuilder sb = new StringBuilder();
        sb.append("onSetCard called for blank view with: ");
        sb.append(card.toString());
        AppboyLogger.w(tag, sb.toString());
    }
}
