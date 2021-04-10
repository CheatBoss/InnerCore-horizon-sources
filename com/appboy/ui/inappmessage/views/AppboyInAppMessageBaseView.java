package com.appboy.ui.inappmessage.views;

import android.content.*;
import android.util.*;
import com.appboy.models.*;
import com.appboy.support.*;
import android.widget.*;
import android.view.*;
import com.appboy.ui.*;
import com.appboy.ui.support.*;
import android.graphics.*;
import com.appboy.ui.inappmessage.*;
import com.facebook.drawee.view.*;
import com.appboy.enums.inappmessage.*;

public abstract class AppboyInAppMessageBaseView extends RelativeLayout implements IInAppMessageView
{
    final boolean mCanUseFresco;
    
    public AppboyInAppMessageBaseView(final Context context, final AttributeSet set) {
        super(context, set);
        this.mCanUseFresco = FrescoLibraryUtils.canUseFresco(context);
        this.setLayerType(1, (Paint)null);
    }
    
    public String getAppropriateImageUrl(final IInAppMessage inAppMessage) {
        if (!StringUtils.isNullOrBlank(inAppMessage.getLocalImageUrl())) {
            return inAppMessage.getLocalImageUrl();
        }
        return inAppMessage.getRemoteImageUrl();
    }
    
    public abstract Object getMessageBackgroundObject();
    
    public View getMessageClickableView() {
        return (View)this;
    }
    
    public abstract TextView getMessageIconView();
    
    public abstract ImageView getMessageImageView();
    
    public abstract View getMessageSimpleDraweeView();
    
    public abstract TextView getMessageTextView();
    
    View getProperViewFromInflatedStub(int n) {
        ((ViewStub)this.findViewById(n)).inflate();
        if (this.mCanUseFresco) {
            n = R$id.com_appboy_stubbed_inappmessage_drawee_view;
        }
        else {
            n = R$id.com_appboy_stubbed_inappmessage_image_view;
        }
        return this.findViewById(n);
    }
    
    public void resetMessageMargins(final boolean b) {
        Object o;
        int n;
        if (this.mCanUseFresco) {
            o = this.getMessageSimpleDraweeView();
            n = R$id.com_appboy_stubbed_inappmessage_drawee_view_parent;
        }
        else {
            o = this.getMessageImageView();
            n = R$id.com_appboy_stubbed_inappmessage_image_view_parent;
        }
        final RelativeLayout relativeLayout = (RelativeLayout)this.findViewById(n);
        if (o != null) {
            if (!b) {
                ViewUtils.removeViewFromParent((View)o);
                if (relativeLayout != null) {
                    ViewUtils.removeViewFromParent((View)relativeLayout);
                }
            }
            else {
                ViewUtils.removeViewFromParent((View)this.getMessageIconView());
            }
        }
        if (this.getMessageIconView() != null && StringUtils.isNullOrBlank((String)this.getMessageIconView().getText())) {
            ViewUtils.removeViewFromParent((View)this.getMessageIconView());
        }
    }
    
    public void setMessage(final String text) {
        this.getMessageTextView().setText((CharSequence)text);
    }
    
    public void setMessageBackgroundColor(final int n) {
        InAppMessageViewUtils.setViewBackgroundColor((View)this.getMessageBackgroundObject(), n);
    }
    
    public void setMessageIcon(final String s, final int n, final int n2) {
        if (this.getMessageIconView() != null) {
            InAppMessageViewUtils.setIcon(this.getContext(), s, n, n2, this.getMessageIconView());
        }
    }
    
    public void setMessageImageView(final Bitmap bitmap) {
        InAppMessageViewUtils.setImage(bitmap, this.getMessageImageView());
    }
    
    public void setMessageSimpleDrawee(final IInAppMessage inAppMessage) {
        FrescoLibraryUtils.setDraweeControllerHelper((SimpleDraweeView)this.getMessageSimpleDraweeView(), this.getAppropriateImageUrl(inAppMessage), 0.0f, false);
    }
    
    public void setMessageTextAlign(final TextAlign textAlign) {
        InAppMessageViewUtils.setTextAlignment(this.getMessageTextView(), textAlign);
    }
    
    public void setMessageTextColor(final int n) {
        InAppMessageViewUtils.setTextViewColor(this.getMessageTextView(), n);
    }
}
