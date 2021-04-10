package com.appboy.ui.contentcards.view;

import android.content.*;
import com.appboy.models.cards.*;
import android.view.*;
import com.appboy.ui.*;
import android.widget.*;
import com.facebook.drawee.view.*;

public class BannerImageContentCardView extends BaseContentCardView<BannerImageCard>
{
    private static final float DEFAULT_ASPECT_RATIO = 6.0f;
    
    public BannerImageContentCardView(final Context context) {
        super(context);
    }
    
    @Override
    public void bindViewHolder(final ContentCardViewHolder contentCardViewHolder, final BannerImageCard bannerImageCard) {
        super.bindViewHolder(contentCardViewHolder, bannerImageCard);
        final ViewHolder viewHolder = (ViewHolder)contentCardViewHolder;
        this.setOptionalCardImage(viewHolder.getImageView(), viewHolder.getSimpleDraweeView(), bannerImageCard.getAspectRatio(), bannerImageCard.getImageUrl(), 6.0f);
    }
    
    @Override
    public ContentCardViewHolder createViewHolder(final ViewGroup viewGroup) {
        final View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R$layout.com_appboy_banner_image_content_card, viewGroup, false);
        inflate.setBackground(this.getResources().getDrawable(R$drawable.com_appboy_card_background));
        return new ViewHolder(inflate);
    }
    
    private class ViewHolder extends ContentCardViewHolder
    {
        private View mCardImage;
        
        ViewHolder(final View view) {
            super(view, BannerImageContentCardView.this.isUnreadIndicatorEnabled());
            this.mCardImage = this.createCardImageWithStyle(BannerImageContentCardView.this.getContext(), view, BannerImageContentCardView.this.canUseFresco(), R$style.Appboy_ContentCards_BannerImage_ImageContainer_Image, R$id.com_appboy_content_cards_banner_image_card_image_container);
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
    }
}
