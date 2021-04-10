package com.appboy.ui.contentcards.view;

import android.content.*;
import com.appboy.models.cards.*;
import android.view.*;
import android.widget.*;
import com.appboy.ui.*;

public class TextAnnouncementContentCardView extends BaseContentCardView<TextAnnouncementCard>
{
    public TextAnnouncementContentCardView(final Context context) {
        super(context);
    }
    
    @Override
    public void bindViewHolder(final ContentCardViewHolder contentCardViewHolder, final TextAnnouncementCard textAnnouncementCard) {
        super.bindViewHolder(contentCardViewHolder, textAnnouncementCard);
        final ViewHolder viewHolder = (ViewHolder)contentCardViewHolder;
        viewHolder.getTitle().setText((CharSequence)textAnnouncementCard.getTitle());
        viewHolder.getDescription().setText((CharSequence)textAnnouncementCard.getDescription());
        this.setOptionalTextView(viewHolder.getDomain(), textAnnouncementCard.getDomain());
    }
    
    @Override
    public ContentCardViewHolder createViewHolder(final ViewGroup viewGroup) {
        final View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R$layout.com_appboy_text_announcement_content_card, viewGroup, false);
        inflate.setBackground(this.getResources().getDrawable(R$drawable.com_appboy_card_background));
        return new ViewHolder(inflate);
    }
    
    private class ViewHolder extends ContentCardViewHolder
    {
        private final TextView mDescription;
        private final TextView mDomain;
        private final TextView mTitle;
        
        ViewHolder(final View view) {
            super(view, TextAnnouncementContentCardView.this.isUnreadIndicatorEnabled());
            this.mTitle = (TextView)view.findViewById(R$id.com_appboy_content_cards_text_announcement_card_title);
            this.mDescription = (TextView)view.findViewById(R$id.com_appboy_content_cards_text_announcement_card_description);
            this.mDomain = (TextView)view.findViewById(R$id.com_appboy_content_cards_text_announcement_card_domain);
        }
        
        TextView getDescription() {
            return this.mDescription;
        }
        
        TextView getDomain() {
            return this.mDomain;
        }
        
        TextView getTitle() {
            return this.mTitle;
        }
    }
}
