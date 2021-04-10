package com.appboy.ui.inappmessage.views;

import android.view.*;
import android.content.*;
import android.util.*;
import android.widget.*;
import com.appboy.models.*;
import com.appboy.ui.inappmessage.*;
import com.appboy.enums.inappmessage.*;
import com.appboy.ui.*;

public class AppboyInAppMessageSlideupView extends AppboyInAppMessageBaseView
{
    private AppboyInAppMessageImageView mAppboyInAppMessageImageView;
    private View mSimpleDraweeView;
    
    public AppboyInAppMessageSlideupView(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    @Override
    public View getMessageBackgroundObject() {
        return this.findViewById(R$id.com_appboy_inappmessage_slideup);
    }
    
    public View getMessageChevronView() {
        return this.findViewById(R$id.com_appboy_inappmessage_slideup_chevron);
    }
    
    @Override
    public TextView getMessageIconView() {
        return (TextView)this.findViewById(R$id.com_appboy_inappmessage_slideup_icon);
    }
    
    @Override
    public ImageView getMessageImageView() {
        return this.mAppboyInAppMessageImageView;
    }
    
    @Override
    public View getMessageSimpleDraweeView() {
        return this.mSimpleDraweeView;
    }
    
    @Override
    public TextView getMessageTextView() {
        return (TextView)this.findViewById(R$id.com_appboy_inappmessage_slideup_message);
    }
    
    public void inflateStubViews(final IInAppMessage inAppMessage) {
        if (this.mCanUseFresco) {
            final View properViewFromInflatedStub = this.getProperViewFromInflatedStub(R$id.com_appboy_inappmessage_slideup_drawee_stub);
            this.mSimpleDraweeView = properViewFromInflatedStub;
            ((AppboyInAppMessageSimpleDraweeView)properViewFromInflatedStub).setInAppMessageImageCropType(inAppMessage.getCropType());
            return;
        }
        (this.mAppboyInAppMessageImageView = (AppboyInAppMessageImageView)this.getProperViewFromInflatedStub(R$id.com_appboy_inappmessage_slideup_imageview_stub)).setInAppMessageImageCropType(inAppMessage.getCropType());
    }
    
    public void setMessageChevron(final int n, final ClickAction clickAction) {
        if (AppboyInAppMessageSlideupView$1.$SwitchMap$com$appboy$enums$inappmessage$ClickAction[clickAction.ordinal()] != 1) {
            InAppMessageViewUtils.setViewBackgroundColorFilter(this.getMessageChevronView(), n, this.getContext().getResources().getColor(R$color.com_appboy_inappmessage_chevron));
            return;
        }
        this.getMessageChevronView().setVisibility(8);
    }
}
