package com.appboy.ui.contentcards.view;

import android.content.*;
import com.appboy.models.cards.*;
import android.view.*;
import com.appboy.ui.*;
import android.widget.*;
import com.facebook.drawee.view.*;

public class ShortNewsContentCardView extends BaseContentCardView<ShortNewsCard>
{
    private static final float DEFAULT_ASPECT_RATIO = 1.0f;
    
    public ShortNewsContentCardView(final Context context) {
        super(context);
    }
    
    @Override
    public void bindViewHolder(final ContentCardViewHolder contentCardViewHolder, final ShortNewsCard shortNewsCard) {
        super.bindViewHolder(contentCardViewHolder, shortNewsCard);
        final ViewHolder viewHolder = (ViewHolder)contentCardViewHolder;
        viewHolder.getTitle().setText((CharSequence)shortNewsCard.getTitle());
        viewHolder.getDescription().setText((CharSequence)shortNewsCard.getDescription());
        this.setOptionalTextView(viewHolder.getDomain(), shortNewsCard.getDomain());
        this.setOptionalCardImage(viewHolder.getImageView(), viewHolder.getSimpleDraweeView(), 1.0f, shortNewsCard.getImageUrl(), 1.0f);
    }
    
    @Override
    public ContentCardViewHolder createViewHolder(final ViewGroup viewGroup) {
        final View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R$layout.com_appboy_short_news_content_card, viewGroup, false);
        inflate.setBackground(this.getResources().getDrawable(R$drawable.com_appboy_card_background));
        return new ViewHolder(inflate);
    }
    
    private class ViewHolder extends ContentCardViewHolder
    {
        private View mCardImage;
        private final TextView mDescription;
        private final TextView mDomain;
        private final TextView mTitle;
        
        ViewHolder(final View view) {
            super(view, ShortNewsContentCardView.this.isUnreadIndicatorEnabled());
            this.mCardImage = this.createCardImageWithStyle(ShortNewsContentCardView.this.getContext(), view, ShortNewsContentCardView.this.canUseFresco(), R$style.Appboy_ContentCards_ShortNews_ImageContainer_Image, R$id.com_appboy_content_cards_short_news_card_image_container);
            this.mTitle = (TextView)view.findViewById(R$id.com_appboy_content_cards_short_news_card_title);
            this.mDescription = (TextView)view.findViewById(R$id.com_appboy_content_cards_short_news_card_description);
            this.mDomain = (TextView)view.findViewById(R$id.com_appboy_content_cards_short_news_card_domain);
        }
        
        TextView getDescription() {
            return this.mDescription;
        }
        
        TextView getDomain() {
            return this.mDomain;
        }
        
        ImageView getImageView() {
            final View mCardImage = this.mCardImage;
            if (mCardImage instanceof ImageView) {
                return (ImageView)mCardImage;
            }
            return null;
        }
        
        SimpleDraweeView getSimpleDraweeView() {
            final View mCardImage = this.mCardImage;
            if (mCardImage instanceof SimpleDraweeView) {
                return (SimpleDraweeView)mCardImage;
            }
            return null;
        }
        
        TextView getTitle() {
            return this.mTitle;
        }
    }
}
