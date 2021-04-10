package com.appboy.ui.widget;

import com.appboy.ui.feed.view.*;
import com.appboy.ui.actions.*;
import android.widget.*;
import com.appboy.support.*;
import android.content.*;
import com.appboy.ui.*;
import com.appboy.models.cards.*;
import android.view.*;

public class TextAnnouncementCardView extends BaseFeedCardView<TextAnnouncementCard>
{
    private static final String TAG;
    private IAction mCardAction;
    private final TextView mDescription;
    private final TextView mDomain;
    private final TextView mTitle;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(TextAnnouncementCardView.class);
    }
    
    public TextAnnouncementCardView(final Context context) {
        this(context, null);
    }
    
    public TextAnnouncementCardView(final Context context, final TextAnnouncementCard card) {
        super(context);
        this.mTitle = (TextView)this.findViewById(R$id.com_appboy_text_announcement_card_title);
        this.mDescription = (TextView)this.findViewById(R$id.com_appboy_text_announcement_card_description);
        this.mDomain = (TextView)this.findViewById(R$id.com_appboy_text_announcement_card_domain);
        if (card != null) {
            this.setCard(card);
        }
        this.setBackground(this.getResources().getDrawable(R$drawable.com_appboy_card_background));
    }
    
    @Override
    protected int getLayoutResource() {
        return R$layout.com_appboy_text_announcement_card;
    }
    
    public void onSetCard(final TextAnnouncementCard textAnnouncementCard) {
        this.mTitle.setText((CharSequence)textAnnouncementCard.getTitle());
        this.mDescription.setText((CharSequence)textAnnouncementCard.getDescription());
        this.setOptionalTextView(this.mDomain, textAnnouncementCard.getDomain());
        this.mCardAction = BaseCardView.getUriActionForCard(textAnnouncementCard);
        this.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                final TextAnnouncementCardView this$0 = TextAnnouncementCardView.this;
                this$0.handleCardClick(this$0.mContext, textAnnouncementCard, TextAnnouncementCardView.this.mCardAction, TextAnnouncementCardView.TAG);
            }
        });
    }
}
