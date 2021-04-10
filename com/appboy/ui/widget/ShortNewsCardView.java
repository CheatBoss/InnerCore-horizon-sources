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

public class ShortNewsCardView extends BaseFeedCardView<ShortNewsCard>
{
    private static final String TAG;
    private final float mAspectRatio;
    private IAction mCardAction;
    private final TextView mDescription;
    private final TextView mDomain;
    private SimpleDraweeView mDrawee;
    private ImageView mImage;
    private final TextView mTitle;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(ShortNewsCardView.class);
    }
    
    public ShortNewsCardView(final Context context) {
        this(context, null);
    }
    
    public ShortNewsCardView(final Context context, final ShortNewsCard card) {
        super(context);
        this.mAspectRatio = 1.0f;
        this.mDescription = (TextView)this.findViewById(R$id.com_appboy_short_news_card_description);
        this.mTitle = (TextView)this.findViewById(R$id.com_appboy_short_news_card_title);
        this.mDomain = (TextView)this.findViewById(R$id.com_appboy_short_news_card_domain);
        if (this.canUseFresco()) {
            this.mDrawee = (SimpleDraweeView)this.getProperViewFromInflatedStub(R$id.com_appboy_short_news_card_drawee_stub);
        }
        else {
            (this.mImage = (ImageView)this.getProperViewFromInflatedStub(R$id.com_appboy_short_news_card_imageview_stub)).setScaleType(ImageView$ScaleType.CENTER_CROP);
            this.mImage.setAdjustViewBounds(true);
        }
        if (card != null) {
            this.setCard(card);
        }
        this.setBackground(this.getResources().getDrawable(R$drawable.com_appboy_card_background));
    }
    
    @Override
    protected int getLayoutResource() {
        return R$layout.com_appboy_short_news_card;
    }
    
    public void onSetCard(final ShortNewsCard shortNewsCard) {
        this.mDescription.setText((CharSequence)shortNewsCard.getDescription());
        this.setOptionalTextView(this.mTitle, shortNewsCard.getTitle());
        this.setOptionalTextView(this.mDomain, shortNewsCard.getDomain());
        this.mCardAction = BaseCardView.getUriActionForCard(shortNewsCard);
        this.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                final ShortNewsCardView this$0 = ShortNewsCardView.this;
                this$0.handleCardClick(this$0.mContext, shortNewsCard, ShortNewsCardView.this.mCardAction, ShortNewsCardView.TAG);
            }
        });
        if (this.canUseFresco()) {
            this.setSimpleDraweeToUrl(this.mDrawee, shortNewsCard.getImageUrl(), 1.0f, true);
            return;
        }
        this.setImageViewToUrl(this.mImage, shortNewsCard.getImageUrl(), 1.0f);
    }
}
