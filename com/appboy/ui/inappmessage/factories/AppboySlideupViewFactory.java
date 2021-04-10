package com.appboy.ui.inappmessage.factories;

import com.appboy.ui.inappmessage.*;
import android.app.*;
import com.appboy.ui.inappmessage.views.*;
import com.appboy.models.*;
import com.appboy.ui.*;
import android.view.*;
import com.appboy.ui.support.*;
import com.appboy.support.*;
import com.appboy.*;
import com.appboy.enums.*;
import android.content.*;

public class AppboySlideupViewFactory implements IInAppMessageViewFactory
{
    public AppboyInAppMessageSlideupView createInAppMessageView(final Activity activity, final IInAppMessage messageSimpleDrawee) {
        final Context applicationContext = activity.getApplicationContext();
        final InAppMessageSlideup inAppMessageSlideup = (InAppMessageSlideup)messageSimpleDrawee;
        final AppboyInAppMessageSlideupView appboyInAppMessageSlideupView = (AppboyInAppMessageSlideupView)activity.getLayoutInflater().inflate(R$layout.com_appboy_inappmessage_slideup, (ViewGroup)null);
        appboyInAppMessageSlideupView.inflateStubViews(messageSimpleDrawee);
        if (FrescoLibraryUtils.canUseFresco(applicationContext)) {
            appboyInAppMessageSlideupView.setMessageSimpleDrawee(messageSimpleDrawee);
        }
        else {
            final String appropriateImageUrl = appboyInAppMessageSlideupView.getAppropriateImageUrl(messageSimpleDrawee);
            if (!StringUtils.isNullOrEmpty(appropriateImageUrl)) {
                Appboy.getInstance(applicationContext).getAppboyImageLoader().renderUrlIntoView(applicationContext, appropriateImageUrl, appboyInAppMessageSlideupView.getMessageImageView(), AppboyViewBounds.IN_APP_MESSAGE_SLIDEUP);
            }
        }
        appboyInAppMessageSlideupView.setMessageBackgroundColor(inAppMessageSlideup.getBackgroundColor());
        appboyInAppMessageSlideupView.setMessage(inAppMessageSlideup.getMessage());
        appboyInAppMessageSlideupView.setMessageTextColor(inAppMessageSlideup.getMessageTextColor());
        appboyInAppMessageSlideupView.setMessageTextAlign(inAppMessageSlideup.getMessageTextAlign());
        appboyInAppMessageSlideupView.setMessageIcon(inAppMessageSlideup.getIcon(), inAppMessageSlideup.getIconColor(), inAppMessageSlideup.getIconBackgroundColor());
        appboyInAppMessageSlideupView.setMessageChevron(inAppMessageSlideup.getChevronColor(), inAppMessageSlideup.getClickAction());
        appboyInAppMessageSlideupView.resetMessageMargins(messageSimpleDrawee.getImageDownloadSuccessful());
        return appboyInAppMessageSlideupView;
    }
}
