package com.appboy.ui.inappmessage.factories;

import com.appboy.ui.inappmessage.*;
import android.app.*;
import com.appboy.ui.inappmessage.views.*;
import com.appboy.enums.inappmessage.*;
import com.appboy.models.*;
import com.appboy.ui.support.*;
import com.appboy.support.*;
import com.appboy.*;
import com.appboy.enums.*;
import android.content.*;
import com.appboy.ui.*;
import android.view.*;

public class AppboyModalViewFactory implements IInAppMessageViewFactory
{
    public AppboyInAppMessageModalView createInAppMessageView(final Activity activity, final IInAppMessage inAppMessage) {
        final Context applicationContext = activity.getApplicationContext();
        final InAppMessageModal inAppMessageModal = (InAppMessageModal)inAppMessage;
        final boolean equals = inAppMessageModal.getImageStyle().equals(ImageStyle.GRAPHIC);
        final AppboyInAppMessageModalView appropriateModalView = this.getAppropriateModalView(activity, equals);
        appropriateModalView.inflateStubViews(activity, inAppMessageModal);
        if (FrescoLibraryUtils.canUseFresco(applicationContext)) {
            appropriateModalView.setMessageSimpleDrawee(inAppMessageModal, activity);
        }
        else {
            final String appropriateImageUrl = appropriateModalView.getAppropriateImageUrl(inAppMessage);
            if (!StringUtils.isNullOrEmpty(appropriateImageUrl)) {
                Appboy.getInstance(applicationContext).getAppboyImageLoader().renderUrlIntoView(applicationContext, appropriateImageUrl, appropriateModalView.getMessageImageView(), AppboyViewBounds.IN_APP_MESSAGE_MODAL);
            }
        }
        appropriateModalView.getFrameView().setOnClickListener((View$OnClickListener)null);
        appropriateModalView.setMessageBackgroundColor(inAppMessage.getBackgroundColor());
        appropriateModalView.setFrameColor(inAppMessageModal.getFrameColor());
        appropriateModalView.setMessageButtons(inAppMessageModal.getMessageButtons());
        appropriateModalView.setMessageCloseButtonColor(inAppMessageModal.getCloseButtonColor());
        if (!equals) {
            appropriateModalView.setMessage(inAppMessage.getMessage());
            appropriateModalView.setMessageTextColor(inAppMessage.getMessageTextColor());
            appropriateModalView.setMessageHeaderText(inAppMessageModal.getHeader());
            appropriateModalView.setMessageHeaderTextColor(inAppMessageModal.getHeaderTextColor());
            appropriateModalView.setMessageIcon(inAppMessage.getIcon(), inAppMessage.getIconColor(), inAppMessage.getIconBackgroundColor());
            appropriateModalView.setMessageHeaderTextAlignment(inAppMessageModal.getHeaderTextAlign());
            appropriateModalView.setMessageTextAlign(inAppMessageModal.getMessageTextAlign());
            appropriateModalView.resetMessageMargins(inAppMessage.getImageDownloadSuccessful());
        }
        return appropriateModalView;
    }
    
    AppboyInAppMessageModalView getAppropriateModalView(final Activity activity, final boolean b) {
        LayoutInflater layoutInflater;
        int n;
        if (b) {
            layoutInflater = activity.getLayoutInflater();
            n = R$layout.com_appboy_inappmessage_modal_graphic;
        }
        else {
            layoutInflater = activity.getLayoutInflater();
            n = R$layout.com_appboy_inappmessage_modal;
        }
        return (AppboyInAppMessageModalView)layoutInflater.inflate(n, (ViewGroup)null);
    }
}
