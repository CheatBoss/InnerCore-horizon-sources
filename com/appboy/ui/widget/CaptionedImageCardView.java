package com.appboy.ui.widget;

import com.appboy.ui.feed.view.*;
import com.appboy.ui.actions.*;
import com.facebook.drawee.view.*;
import com.appboy.support.*;
import android.content.*;
import android.widget.*;
import com.appboy.ui.*;
import com.appboy.models.cards.*;
import android.view.*;

public class CaptionedImageCardView extends BaseFeedCardView<CaptionedImageCard>
{
    private static final String TAG;
    private float mAspectRatio;
    private IAction mCardAction;
    private final TextView mDescription;
    private final TextView mDomain;
    private SimpleDraweeView mDrawee;
    private ImageView mImage;
    private final TextView mTitle;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(CaptionedImageCardView.class);
    }
    
    public CaptionedImageCardView(final Context context) {
        this(context, null);
    }
    
    public CaptionedImageCardView(final Context context, final CaptionedImageCard card) {
        super(context);
        this.mAspectRatio = 1.3333334f;
        if (this.canUseFresco()) {
            this.mDrawee = (SimpleDraweeView)this.getProperViewFromInflatedStub(R$id.com_appboy_captioned_image_card_drawee_stub);
        }
        else {
            (this.mImage = (ImageView)this.getProperViewFromInflatedStub(R$id.com_appboy_captioned_image_card_imageview_stub)).setScaleType(ImageView$ScaleType.CENTER_CROP);
            this.mImage.setAdjustViewBounds(true);
        }
        this.mTitle = (TextView)this.findViewById(R$id.com_appboy_captioned_image_title);
        this.mDescription = (TextView)this.findViewById(R$id.com_appboy_captioned_image_description);
        this.mDomain = (TextView)this.findViewById(R$id.com_appboy_captioned_image_card_domain);
        if (card != null) {
            this.setCard(card);
        }
        this.setBackground(this.getResources().getDrawable(R$drawable.com_appboy_card_background));
    }
    
    @Override
    protected int getLayoutResource() {
        return R$layout.com_appboy_captioned_image_card;
    }
    
    public void onSetCard(final CaptionedImageCard captionedImageCard) {
        this.mTitle.setText((CharSequence)captionedImageCard.getTitle());
        this.mDescription.setText((CharSequence)captionedImageCard.getDescription());
        this.setOptionalTextView(this.mDomain, captionedImageCard.getDomain());
        this.mCardAction = BaseCardView.getUriActionForCard(captionedImageCard);
        boolean b;
        if (captionedImageCard.getAspectRatio() != 0.0f) {
            this.mAspectRatio = captionedImageCard.getAspectRatio();
            b = true;
        }
        else {
            b = false;
        }
        this.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                final CaptionedImageCardView this$0 = CaptionedImageCardView.this;
                this$0.handleCardClick(this$0.mContext, captionedImageCard, CaptionedImageCardView.this.mCardAction, CaptionedImageCardView.TAG);
            }
        });
        if (this.canUseFresco()) {
            this.setSimpleDraweeToUrl(this.mDrawee, captionedImageCard.getImageUrl(), this.mAspectRatio, b);
            return;
        }
        this.setImageViewToUrl(this.mImage, captionedImageCard.getImageUrl(), this.mAspectRatio, b);
    }
}
