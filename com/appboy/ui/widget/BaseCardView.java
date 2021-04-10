package com.appboy.ui.widget;

import com.appboy.models.cards.*;
import com.appboy.configuration.*;
import android.content.*;
import com.appboy.ui.feed.*;
import com.appboy.support.*;
import com.appboy.ui.support.*;
import android.os.*;
import java.util.*;
import com.appboy.ui.actions.*;
import android.util.*;
import com.appboy.ui.*;
import com.appboy.*;
import com.appboy.enums.*;
import android.view.*;
import android.widget.*;
import com.facebook.drawee.view.*;

public abstract class BaseCardView<T extends Card> extends RelativeLayout
{
    private static final String ICON_READ_TAG = "icon_read";
    private static final String ICON_UNREAD_TAG = "icon_unread";
    private static final float SQUARE_ASPECT_RATIO = 1.0f;
    private static final String TAG;
    private static Boolean sCanUseFresco;
    private static Boolean sUnreadCardVisualIndicatorEnabled;
    protected AppboyConfigurationProvider mAppboyConfigurationProvider;
    protected T mCard;
    private final String mClassLogTag;
    protected final Context mContext;
    protected AppboyImageSwitcher mImageSwitcher;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(BaseCardView.class);
    }
    
    public BaseCardView(final Context context) {
        super(context);
        this.mContext = context.getApplicationContext();
        if (BaseCardView.sCanUseFresco == null) {
            BaseCardView.sCanUseFresco = FrescoLibraryUtils.canUseFresco(context);
        }
        if (this.mAppboyConfigurationProvider == null) {
            this.mAppboyConfigurationProvider = new AppboyConfigurationProvider(context);
        }
        if (BaseCardView.sUnreadCardVisualIndicatorEnabled == null) {
            BaseCardView.sUnreadCardVisualIndicatorEnabled = this.mAppboyConfigurationProvider.getIsNewsfeedVisualIndicatorOn();
        }
        this.mClassLogTag = AppboyLogger.getAppboyLogTag(this.getClass());
    }
    
    protected static UriAction getUriActionForCard(final Card card) {
        final Bundle bundle = new Bundle();
        for (final String s : card.getExtras().keySet()) {
            bundle.putString(s, (String)card.getExtras().get(s));
        }
        return ActionFactory.createUriActionFromUrlString(card.getUrl(), bundle, card.getOpenUriInWebView(), Channel.NEWS_FEED);
    }
    
    public boolean canUseFresco() {
        return BaseCardView.sCanUseFresco;
    }
    
    public String getClassLogTag() {
        return this.mClassLogTag;
    }
    
    protected void handleCardClick(final Context context, final Card card, final IAction action, String tag) {
        card.setIsRead(true);
        if (action == null) {
            final String tag2 = BaseCardView.TAG;
            final StringBuilder sb = new StringBuilder();
            sb.append("Card action is null. Not performing any click action for card: ");
            sb.append(card.getId());
            AppboyLogger.v(tag2, sb.toString());
            return;
        }
        StringBuilder sb2;
        String s;
        if (card.logClick()) {
            sb2 = new StringBuilder();
            s = "Logged click for card: ";
        }
        else {
            sb2 = new StringBuilder();
            s = "Logging click failed for card: ";
        }
        sb2.append(s);
        sb2.append(card.getId());
        AppboyLogger.d(tag, sb2.toString());
        if (this.isClickHandled(context, card, action)) {
            final String tag3 = BaseCardView.TAG;
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Card click was handled by custom listener for card: ");
            sb3.append(card.getId());
            AppboyLogger.d(tag3, sb3.toString());
            return;
        }
        if (action instanceof UriAction) {
            AppboyNavigator.getAppboyNavigator().gotoUri(context, (UriAction)action);
            return;
        }
        tag = BaseCardView.TAG;
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("Executing non uri action for click on card: ");
        sb4.append(card.getId());
        Log.d(tag, sb4.toString());
        action.execute(context);
    }
    
    protected abstract boolean isClickHandled(final Context p0, final Card p1, final IAction p2);
    
    public boolean isUnreadIndicatorEnabled() {
        return BaseCardView.sUnreadCardVisualIndicatorEnabled;
    }
    
    public void setCardViewedIndicator(final AppboyImageSwitcher appboyImageSwitcher, final Card card) {
        if (card == null) {
            AppboyLogger.d(this.getClassLogTag(), "The card is null. Not setting read/unread indicator.");
            return;
        }
        if (appboyImageSwitcher == null) {
            return;
        }
        String s = (String)appboyImageSwitcher.getTag();
        if (s == null) {
            s = "";
        }
        if (card.isRead()) {
            if (!s.equals("icon_read")) {
                if (appboyImageSwitcher.getReadIcon() != null) {
                    appboyImageSwitcher.setImageDrawable(appboyImageSwitcher.getReadIcon());
                }
                else {
                    appboyImageSwitcher.setImageResource(R$drawable.icon_read);
                }
                appboyImageSwitcher.setTag((Object)"icon_read");
            }
        }
        else if (!s.equals("icon_unread")) {
            if (appboyImageSwitcher.getUnReadIcon() != null) {
                appboyImageSwitcher.setImageDrawable(appboyImageSwitcher.getUnReadIcon());
            }
            else {
                appboyImageSwitcher.setImageResource(R$drawable.icon_unread);
            }
            appboyImageSwitcher.setTag((Object)"icon_unread");
        }
    }
    
    public void setImageViewToUrl(final ImageView imageView, final String s) {
        this.setImageViewToUrl(imageView, s, 1.0f, false);
    }
    
    public void setImageViewToUrl(final ImageView imageView, final String s, final float n) {
        this.setImageViewToUrl(imageView, s, n, true);
    }
    
    public void setImageViewToUrl(final ImageView imageView, String s, final float n, final boolean b) {
        String s2;
        if (s == null) {
            s2 = BaseCardView.TAG;
            s = "The image url to render is null. Not setting the card image.";
        }
        else {
            if (n != 0.0f) {
                if (!s.equals(imageView.getTag(R$string.com_appboy_image_resize_tag_key))) {
                    if (n != 1.0f) {
                        final ViewTreeObserver viewTreeObserver = imageView.getViewTreeObserver();
                        if (viewTreeObserver.isAlive()) {
                            viewTreeObserver.addOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)new ViewTreeObserver$OnGlobalLayoutListener() {
                                public void onGlobalLayout() {
                                    final int width = imageView.getWidth();
                                    imageView.setLayoutParams((ViewGroup$LayoutParams)new RelativeLayout$LayoutParams(width, (int)(width / n)));
                                    imageView.getViewTreeObserver().removeOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)this);
                                }
                            });
                        }
                    }
                    imageView.setImageResource(17170445);
                    Appboy.getInstance(this.getContext()).getAppboyImageLoader().renderUrlIntoView(this.getContext(), s, imageView, AppboyViewBounds.BASE_CARD_VIEW);
                    imageView.setTag(R$string.com_appboy_image_resize_tag_key, (Object)s);
                }
                return;
            }
            s2 = BaseCardView.TAG;
            s = "The image aspect ratio is 0. Not setting the card image.";
        }
        AppboyLogger.w(s2, s);
    }
    
    public void setOptionalTextView(final TextView textView, final String text) {
        int visibility;
        if (text != null && !text.trim().equals("")) {
            textView.setText((CharSequence)text);
            visibility = 0;
        }
        else {
            textView.setText((CharSequence)"");
            visibility = 8;
        }
        textView.setVisibility(visibility);
    }
    
    public void setSimpleDraweeToUrl(final SimpleDraweeView simpleDraweeView, final String s, final float n, final boolean b) {
        if (s == null) {
            AppboyLogger.w(this.getClassLogTag(), "The image url to render is null. Not setting the card image.");
            return;
        }
        FrescoLibraryUtils.setDraweeControllerHelper(simpleDraweeView, s, n, b);
    }
}
