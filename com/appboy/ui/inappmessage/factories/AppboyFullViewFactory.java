package com.appboy.ui.inappmessage.factories;

import com.appboy.ui.inappmessage.*;
import android.app.*;
import com.appboy.ui.inappmessage.views.*;
import com.appboy.models.*;
import com.appboy.support.*;
import com.appboy.*;
import com.appboy.enums.*;
import android.content.*;
import com.appboy.ui.*;
import com.appboy.ui.support.*;
import com.appboy.enums.inappmessage.*;
import android.widget.*;
import android.view.*;

public class AppboyFullViewFactory implements IInAppMessageViewFactory
{
    public AppboyInAppMessageFullView createInAppMessageView(final Activity activity, final IInAppMessage inAppMessage) {
        final Context applicationContext = activity.getApplicationContext();
        final InAppMessageFull messageSimpleDrawee = (InAppMessageFull)inAppMessage;
        final boolean equals = messageSimpleDrawee.getImageStyle().equals(ImageStyle.GRAPHIC);
        final AppboyInAppMessageFullView appropriateFullView = this.getAppropriateFullView(activity, equals);
        appropriateFullView.inflateStubViews(activity, messageSimpleDrawee);
        if (FrescoLibraryUtils.canUseFresco(applicationContext)) {
            appropriateFullView.setMessageSimpleDrawee(messageSimpleDrawee);
        }
        else {
            final String appropriateImageUrl = appropriateFullView.getAppropriateImageUrl(inAppMessage);
            if (!StringUtils.isNullOrEmpty(appropriateImageUrl)) {
                Appboy.getInstance(applicationContext).getAppboyImageLoader().renderUrlIntoView(applicationContext, appropriateImageUrl, appropriateFullView.getMessageImageView(), AppboyViewBounds.NO_BOUNDS);
            }
        }
        appropriateFullView.getFrameView().setOnClickListener((View$OnClickListener)null);
        appropriateFullView.setMessageBackgroundColor(messageSimpleDrawee.getBackgroundColor());
        appropriateFullView.setFrameColor(messageSimpleDrawee.getFrameColor());
        appropriateFullView.setMessageButtons(messageSimpleDrawee.getMessageButtons());
        appropriateFullView.setMessageCloseButtonColor(messageSimpleDrawee.getCloseButtonColor());
        if (!equals) {
            appropriateFullView.setMessage(messageSimpleDrawee.getMessage());
            appropriateFullView.setMessageTextColor(messageSimpleDrawee.getMessageTextColor());
            appropriateFullView.setMessageHeaderText(messageSimpleDrawee.getHeader());
            appropriateFullView.setMessageHeaderTextColor(messageSimpleDrawee.getHeaderTextColor());
            appropriateFullView.setMessageHeaderTextAlignment(messageSimpleDrawee.getHeaderTextAlign());
            appropriateFullView.setMessageTextAlign(messageSimpleDrawee.getMessageTextAlign());
            appropriateFullView.resetMessageMargins(messageSimpleDrawee.getImageDownloadSuccessful());
        }
        this.resetLayoutParamsIfAppropriate(activity, messageSimpleDrawee, appropriateFullView);
        return appropriateFullView;
    }
    
    AppboyInAppMessageFullView getAppropriateFullView(final Activity activity, final boolean b) {
        LayoutInflater layoutInflater;
        int n;
        if (b) {
            layoutInflater = activity.getLayoutInflater();
            n = R$layout.com_appboy_inappmessage_full_graphic;
        }
        else {
            layoutInflater = activity.getLayoutInflater();
            n = R$layout.com_appboy_inappmessage_full;
        }
        return (AppboyInAppMessageFullView)layoutInflater.inflate(n, (ViewGroup)null);
    }
    
    boolean resetLayoutParamsIfAppropriate(final Activity activity, final IInAppMessage inAppMessage, final AppboyInAppMessageFullView appboyInAppMessageFullView) {
        if (!ViewUtils.isRunningOnTablet(activity)) {
            return false;
        }
        if (inAppMessage.getOrientation() != null) {
            if (inAppMessage.getOrientation() == Orientation.ANY) {
                return false;
            }
            final int longEdge = appboyInAppMessageFullView.getLongEdge();
            final int shortEdge = appboyInAppMessageFullView.getShortEdge();
            if (longEdge > 0 && shortEdge > 0) {
                RelativeLayout$LayoutParams layoutParams;
                if (inAppMessage.getOrientation() == Orientation.LANDSCAPE) {
                    layoutParams = new RelativeLayout$LayoutParams(longEdge, shortEdge);
                }
                else {
                    layoutParams = new RelativeLayout$LayoutParams(shortEdge, longEdge);
                }
                layoutParams.addRule(13, -1);
                appboyInAppMessageFullView.getMessageBackgroundObject().setLayoutParams((ViewGroup$LayoutParams)layoutParams);
                return true;
            }
        }
        return false;
    }
}
