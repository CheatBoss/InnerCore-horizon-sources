package com.appboy.ui.inappmessage.views;

import com.appboy.support.*;
import android.content.*;
import android.util.*;
import android.app.*;
import com.appboy.models.*;
import com.appboy.ui.support.*;
import com.appboy.ui.inappmessage.config.*;
import com.appboy.enums.inappmessage.*;
import java.util.*;
import android.widget.*;
import com.appboy.ui.inappmessage.*;
import android.view.*;
import android.graphics.drawable.*;
import com.appboy.ui.*;

public class AppboyInAppMessageFullView extends AppboyInAppMessageImmersiveBaseView
{
    private static final String TAG;
    private AppboyInAppMessageImageView mAppboyInAppMessageImageView;
    private View mSimpleDraweeView;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(AppboyInAppMessageFullView.class);
    }
    
    public AppboyInAppMessageFullView(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    private void setInAppMessageImageViewAttributes(final Activity activity, final IInAppMessageImmersive inAppMessageImmersive, final IInAppMessageImageView inAppMessageImageView) {
        inAppMessageImageView.setInAppMessageImageCropType(inAppMessageImmersive.getCropType());
        if (!ViewUtils.isRunningOnTablet(activity)) {
            inAppMessageImageView.setCornersRadiusPx(0.0f);
            return;
        }
        final float cornersRadiusPx = (float)ViewUtils.convertDpToPixels(activity, AppboyInAppMessageParams.getModalizedImageRadiusDp());
        if (inAppMessageImmersive.getImageStyle().equals(ImageStyle.GRAPHIC)) {
            inAppMessageImageView.setCornersRadiusPx(cornersRadiusPx);
            return;
        }
        inAppMessageImageView.setCornersRadiiPx(cornersRadiusPx, cornersRadiusPx, 0.0f, 0.0f);
    }
    
    @Override
    public View getFrameView() {
        return this.findViewById(R$id.com_appboy_inappmessage_full_frame);
    }
    
    public int getLongEdge() {
        return this.findViewById(R$id.com_appboy_inappmessage_full).getLayoutParams().height;
    }
    
    @Override
    public View getMessageBackgroundObject() {
        return this.findViewById(R$id.com_appboy_inappmessage_full);
    }
    
    @Override
    public List<View> getMessageButtonViews() {
        final ArrayList<View> list = new ArrayList<View>();
        if (this.findViewById(R$id.com_appboy_inappmessage_full_button_one) != null) {
            list.add(this.findViewById(R$id.com_appboy_inappmessage_full_button_one));
        }
        if (this.findViewById(R$id.com_appboy_inappmessage_full_button_two) != null) {
            list.add(this.findViewById(R$id.com_appboy_inappmessage_full_button_two));
        }
        return list;
    }
    
    @Override
    public View getMessageButtonsView() {
        return this.findViewById(R$id.com_appboy_inappmessage_full_button_layout);
    }
    
    @Override
    public View getMessageClickableView() {
        return this.findViewById(R$id.com_appboy_inappmessage_full);
    }
    
    @Override
    public View getMessageCloseButtonView() {
        return this.findViewById(R$id.com_appboy_inappmessage_full_close_button);
    }
    
    @Override
    public TextView getMessageHeaderTextView() {
        return (TextView)this.findViewById(R$id.com_appboy_inappmessage_full_header_text);
    }
    
    @Override
    public TextView getMessageIconView() {
        return null;
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
        return (TextView)this.findViewById(R$id.com_appboy_inappmessage_full_message);
    }
    
    public int getShortEdge() {
        return this.findViewById(R$id.com_appboy_inappmessage_full).getLayoutParams().width;
    }
    
    public void inflateStubViews(final Activity activity, final IInAppMessageImmersive inAppMessageImmersive) {
        IInAppMessageImageView mAppboyInAppMessageImageView;
        if (this.mCanUseFresco) {
            final View properViewFromInflatedStub = this.getProperViewFromInflatedStub(R$id.com_appboy_inappmessage_full_drawee_stub);
            this.mSimpleDraweeView = properViewFromInflatedStub;
            mAppboyInAppMessageImageView = (AppboyInAppMessageSimpleDraweeView)properViewFromInflatedStub;
        }
        else {
            mAppboyInAppMessageImageView = (AppboyInAppMessageImageView)this.getProperViewFromInflatedStub(R$id.com_appboy_inappmessage_full_imageview_stub);
            this.mAppboyInAppMessageImageView = (AppboyInAppMessageImageView)mAppboyInAppMessageImageView;
        }
        this.setInAppMessageImageViewAttributes(activity, inAppMessageImmersive, mAppboyInAppMessageImageView);
    }
    
    @Override
    public void resetMessageMargins(final boolean b) {
        super.resetMessageMargins(b);
        this.findViewById(R$id.com_appboy_inappmessage_full_text_layout).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                AppboyLogger.d(AppboyInAppMessageFullView.TAG, "Passing scrollView click event to message clickable view.");
                AppboyInAppMessageFullView.this.getMessageClickableView().performClick();
            }
        });
    }
    
    @Override
    public void setMessageBackgroundColor(final int messageBackgroundColor) {
        if (this.getMessageBackgroundObject().getBackground() instanceof GradientDrawable) {
            InAppMessageViewUtils.setViewBackgroundColorFilter(this.findViewById(R$id.com_appboy_inappmessage_full), messageBackgroundColor, this.getContext().getResources().getColor(R$color.com_appboy_inappmessage_background_light));
            return;
        }
        super.setMessageBackgroundColor(messageBackgroundColor);
    }
}
