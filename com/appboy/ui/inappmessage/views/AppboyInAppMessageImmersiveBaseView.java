package com.appboy.ui.inappmessage.views;

import com.appboy.ui.inappmessage.*;
import android.content.*;
import android.util.*;
import java.util.*;
import android.widget.*;
import android.view.*;
import com.appboy.support.*;
import com.appboy.ui.support.*;
import com.appboy.models.*;
import com.appboy.ui.*;
import com.appboy.enums.inappmessage.*;

public abstract class AppboyInAppMessageImmersiveBaseView extends AppboyInAppMessageBaseView implements IInAppMessageImmersiveView
{
    public AppboyInAppMessageImmersiveBaseView(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public abstract View getFrameView();
    
    @Override
    public abstract List<View> getMessageButtonViews();
    
    public abstract View getMessageButtonsView();
    
    public abstract TextView getMessageHeaderTextView();
    
    @Override
    public abstract TextView getMessageTextView();
    
    public boolean onKeyDown(final int n, final KeyEvent keyEvent) {
        if (n == 4) {
            InAppMessageViewUtils.closeInAppMessageOnKeycodeBack();
            return true;
        }
        return super.onKeyDown(n, keyEvent);
    }
    
    public void resetMessageMargins() {
        this.resetMessageMargins(this.getMessageImageView() != null && this.getMessageImageView().getDrawable() != null);
    }
    
    @Override
    public void resetMessageMargins(final boolean b) {
        super.resetMessageMargins(b);
        if (StringUtils.isNullOrBlank(this.getMessageTextView().getText().toString())) {
            ViewUtils.removeViewFromParent((View)this.getMessageTextView());
        }
        if (StringUtils.isNullOrBlank(this.getMessageHeaderTextView().getText().toString())) {
            ViewUtils.removeViewFromParent((View)this.getMessageHeaderTextView());
        }
        InAppMessageViewUtils.resetMessageMarginsIfNecessary(this.getMessageTextView(), this.getMessageHeaderTextView());
    }
    
    public void setFrameColor(final Integer n) {
        InAppMessageViewUtils.setFrameColor(this.getFrameView(), n);
    }
    
    public void setMessageButtons(final List<MessageButton> list) {
        InAppMessageViewUtils.setButtons(this.getMessageButtonViews(), this.getMessageButtonsView(), this.getContext().getResources().getColor(R$color.com_appboy_inappmessage_button_bg_light), list);
        InAppMessageViewUtils.resetButtonSizesIfNecessary(this.getMessageButtonViews(), list);
    }
    
    public void setMessageCloseButtonColor(final int n) {
        InAppMessageViewUtils.setViewBackgroundColorFilter(this.getMessageCloseButtonView(), n, this.getContext().getResources().getColor(R$color.com_appboy_inappmessage_button_close_light));
    }
    
    public void setMessageHeaderText(final String text) {
        this.getMessageHeaderTextView().setText((CharSequence)text);
    }
    
    public void setMessageHeaderTextAlignment(final TextAlign textAlign) {
        InAppMessageViewUtils.setTextAlignment(this.getMessageHeaderTextView(), textAlign);
    }
    
    public void setMessageHeaderTextColor(final int n) {
        InAppMessageViewUtils.setTextViewColor(this.getMessageHeaderTextView(), n);
    }
}
