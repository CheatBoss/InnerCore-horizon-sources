package com.appboy.ui.widget;

import com.appboy.ui.feed.view.*;
import com.facebook.drawee.view.*;
import android.content.*;
import android.widget.*;
import java.util.*;
import java.text.*;
import com.appboy.ui.*;
import com.appboy.models.cards.*;
import com.appboy.support.*;
import com.appboy.enums.*;
import com.appboy.ui.actions.*;
import android.view.*;

public class CrossPromotionSmallCardView extends BaseFeedCardView<CrossPromotionSmallCard>
{
    private static final String TAG;
    private final float mAspectRatio;
    private final TextView mCaption;
    private SimpleDraweeView mDrawee;
    private ImageView mImage;
    private final Button mPrice;
    private IAction mPriceAction;
    private final TextView mReviewCount;
    private final StarRatingView mStarRating;
    private final TextView mSubtitle;
    private final TextView mTitle;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(CrossPromotionSmallCardView.class);
    }
    
    public CrossPromotionSmallCardView(final Context context) {
        this(context, null);
    }
    
    public CrossPromotionSmallCardView(final Context context, final CrossPromotionSmallCard card) {
        super(context);
        this.mAspectRatio = 1.0f;
        this.mTitle = (TextView)this.findViewById(R$id.com_appboy_cross_promotion_small_card_title);
        this.mSubtitle = (TextView)this.findViewById(R$id.com_appboy_cross_promotion_small_card_subtitle);
        this.mReviewCount = (TextView)this.findViewById(R$id.com_appboy_cross_promotion_small_card_review_count);
        this.mCaption = (TextView)this.findViewById(R$id.com_appboy_cross_promotion_small_card_recommendation_tab);
        this.mStarRating = (StarRatingView)this.findViewById(R$id.com_appboy_cross_promotion_small_card_star_rating);
        this.mPrice = (Button)this.findViewById(R$id.com_appboy_cross_promotion_small_card_price);
        if (this.canUseFresco()) {
            this.mDrawee = (SimpleDraweeView)this.getProperViewFromInflatedStub(R$id.com_appboy_cross_promotion_small_card_drawee_stub);
        }
        else {
            (this.mImage = (ImageView)this.getProperViewFromInflatedStub(R$id.com_appboy_cross_promotion_small_card_imageview_stub)).setScaleType(ImageView$ScaleType.CENTER_CROP);
            this.mImage.setAdjustViewBounds(true);
        }
        if (card != null) {
            this.setCard(card);
        }
    }
    
    private String getPriceString(final double n) {
        if (n == 0.0) {
            return this.mContext.getString(R$string.com_appboy_recommendation_free);
        }
        return NumberFormat.getCurrencyInstance(Locale.US).format(n);
    }
    
    @Override
    protected int getLayoutResource() {
        return R$layout.com_appboy_cross_promotion_small_card;
    }
    
    public void onSetCard(final CrossPromotionSmallCard crossPromotionSmallCard) {
        this.mTitle.setText((CharSequence)crossPromotionSmallCard.getTitle());
        if (crossPromotionSmallCard.getSubtitle() != null && !crossPromotionSmallCard.getSubtitle().toUpperCase(Locale.getDefault()).equals("NULL")) {
            this.mSubtitle.setText((CharSequence)crossPromotionSmallCard.getSubtitle().toUpperCase(Locale.getDefault()));
        }
        else {
            this.mSubtitle.setVisibility(8);
        }
        this.mCaption.setText((CharSequence)crossPromotionSmallCard.getCaption().toUpperCase(Locale.getDefault()));
        if (crossPromotionSmallCard.getRating() <= 0.0) {
            this.mReviewCount.setVisibility(8);
            this.mStarRating.setVisibility(8);
        }
        else {
            final TextView mReviewCount = this.mReviewCount;
            final StringBuilder sb = new StringBuilder();
            sb.append("(");
            sb.append(NumberFormat.getInstance().format(crossPromotionSmallCard.getReviewCount()));
            sb.append(")");
            mReviewCount.setText((CharSequence)sb.toString());
            this.mStarRating.setRating((float)crossPromotionSmallCard.getRating());
        }
        Button button;
        String text;
        if (!StringUtils.isNullOrBlank(crossPromotionSmallCard.getDisplayPrice())) {
            button = this.mPrice;
            text = crossPromotionSmallCard.getDisplayPrice();
        }
        else {
            button = this.mPrice;
            text = this.getPriceString(crossPromotionSmallCard.getPrice());
        }
        button.setText((CharSequence)text);
        this.mPriceAction = new GooglePlayAppDetailsAction(crossPromotionSmallCard.getPackage(), crossPromotionSmallCard.getOpenUriInWebView(), crossPromotionSmallCard.getAppStore(), crossPromotionSmallCard.getKindleId(), Channel.NEWS_FEED);
        this.mPrice.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                final CrossPromotionSmallCardView this$0 = CrossPromotionSmallCardView.this;
                this$0.handleCardClick(this$0.mContext, crossPromotionSmallCard, CrossPromotionSmallCardView.this.mPriceAction, CrossPromotionSmallCardView.TAG);
            }
        });
        if (this.canUseFresco()) {
            this.setSimpleDraweeToUrl(this.mDrawee, crossPromotionSmallCard.getImageUrl(), 1.0f, true);
            return;
        }
        this.setImageViewToUrl(this.mImage, crossPromotionSmallCard.getImageUrl(), 1.0f);
    }
}
