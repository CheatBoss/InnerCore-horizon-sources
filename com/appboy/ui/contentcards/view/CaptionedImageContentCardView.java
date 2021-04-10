package com.appboy.ui.contentcards.view;

import android.content.*;
import com.appboy.models.cards.*;
import android.view.*;
import com.appboy.ui.*;
import android.widget.*;
import com.facebook.drawee.view.*;

public class CaptionedImageContentCardView extends BaseContentCardView<CaptionedImageCard>
{
    private static final float DEFAULT_ASPECT_RATIO = 1.3333334f;
    
    public CaptionedImageContentCardView(final Context context) {
        super(context);
    }
    
    @Override
    public void bindViewHolder(final ContentCardViewHolder contentCardViewHolder, final CaptionedImageCard captionedImageCard) {
        super.bindViewHolder(contentCardViewHolder, captionedImageCard);
        final ViewHolder viewHolder = (ViewHolder)contentCardViewHolder;
        viewHolder.getTitle().setText((CharSequence)captionedImageCard.getTitle());
        viewHolder.getDescription().setText((CharSequence)captionedImageCard.getDescription());
        this.setOptionalTextView(viewHolder.getDomain(), captionedImageCard.getDomain());
        this.setOptionalCardImage(viewHolder.getImageView(), viewHolder.getSimpleDraweeView(), captionedImageCard.getAspectRatio(), captionedImageCard.getImageUrl(), 1.3333334f);
    }
    
    @Override
    public ContentCardViewHolder createViewHolder(final ViewGroup viewGroup) {
        final View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R$layout.com_appboy_captioned_image_content_card, viewGroup, false);
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
            super(view, CaptionedImageContentCardView.this.isUnreadIndicatorEnabled());
            this.mCardImage = this.createCardImageWithStyle(CaptionedImageContentCardView.this.getContext(), view, CaptionedImageContentCardView.this.canUseFresco(), R$style.Appboy_ContentCards_CaptionedImage_ImageContainer_Image, R$id.com_appboy_content_cards_captioned_image_card_image_container);
            this.mTitle = (TextView)view.findViewById(R$id.com_appboy_content_cards_captioned_image_title);
            this.mDescription = (TextView)view.findViewById(R$id.com_appboy_content_cards_captioned_image_description);
            this.mDomain = (TextView)view.findViewById(R$id.com_appboy_content_cards_captioned_image_card_domain);
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
