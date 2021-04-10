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

public class BannerImageCardView extends BaseFeedCardView<BannerImageCard>
{
    private static final String TAG;
    private float mAspectRatio;
    private IAction mCardAction;
    private SimpleDraweeView mDrawee;
    private ImageView mImage;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(BannerImageCardView.class);
    }
    
    public BannerImageCardView(final Context context) {
        this(context, null);
    }
    
    public BannerImageCardView(final Context context, final BannerImageCard card) {
        super(context);
        this.mAspectRatio = 6.0f;
        if (this.canUseFresco()) {
            this.mDrawee = (SimpleDraweeView)this.getProperViewFromInflatedStub(R$id.com_appboy_banner_image_card_drawee_stub);
        }
        else {
            (this.mImage = (ImageView)this.getProperViewFromInflatedStub(R$id.com_appboy_banner_image_card_imageview_stub)).setScaleType(ImageView$ScaleType.CENTER_CROP);
            this.mImage.setAdjustViewBounds(true);
        }
        if (card != null) {
            this.setCard(card);
        }
        this.setBackground(this.getResources().getDrawable(R$drawable.com_appboy_card_background));
    }
    
    @Override
    protected int getLayoutResource() {
        return R$layout.com_appboy_banner_image_card;
    }
    
    public void onSetCard(final BannerImageCard bannerImageCard) {
        boolean b;
        if (bannerImageCard.getAspectRatio() != 0.0f) {
            this.mAspectRatio = bannerImageCard.getAspectRatio();
            b = true;
        }
        else {
            b = false;
        }
        if (this.canUseFresco()) {
            this.setSimpleDraweeToUrl(this.mDrawee, bannerImageCard.getImageUrl(), this.mAspectRatio, b);
        }
        else {
            this.setImageViewToUrl(this.mImage, bannerImageCard.getImageUrl(), this.mAspectRatio, b);
        }
        this.mCardAction = BaseCardView.getUriActionForCard(bannerImageCard);
        this.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                final BannerImageCardView this$0 = BannerImageCardView.this;
                this$0.handleCardClick(this$0.mContext, bannerImageCard, BannerImageCardView.this.mCardAction, BannerImageCardView.TAG);
            }
        });
    }
}
