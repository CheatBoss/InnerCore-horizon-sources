package com.appboy.ui.inappmessage.views;

import com.appboy.support.*;
import android.content.*;
import android.util.*;
import android.app.*;
import com.appboy.enums.inappmessage.*;
import com.appboy.ui.inappmessage.config.*;
import java.util.*;
import com.appboy.ui.inappmessage.*;
import android.widget.*;
import android.view.*;
import com.appboy.ui.*;
import com.appboy.models.*;
import com.facebook.imagepipeline.image.*;
import android.graphics.drawable.*;
import com.appboy.ui.support.*;
import com.facebook.drawee.view.*;
import com.facebook.drawee.controller.*;

public class AppboyInAppMessageModalView extends AppboyInAppMessageImmersiveBaseView
{
    private static final String TAG;
    private AppboyInAppMessageImageView mAppboyInAppMessageImageView;
    private View mSimpleDraweeView;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(AppboyInAppMessageModalView.class);
    }
    
    public AppboyInAppMessageModalView(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    private void resizeGraphicFrameIfAppropriate(final Activity activity, final IInAppMessageImmersive inAppMessageImmersive, final double n) {
        if (!inAppMessageImmersive.getImageStyle().equals(ImageStyle.GRAPHIC)) {
            return;
        }
        final double graphicModalMaxWidthDp = AppboyInAppMessageParams.getGraphicModalMaxWidthDp();
        final double graphicModalMaxHeightDp = AppboyInAppMessageParams.getGraphicModalMaxHeightDp();
        final double n2 = graphicModalMaxWidthDp / graphicModalMaxHeightDp;
        final RelativeLayout$LayoutParams layoutParams = (RelativeLayout$LayoutParams)this.findViewById(R$id.com_appboy_inappmessage_modal_graphic_bound).getLayoutParams();
        int height;
        if (n >= n2) {
            layoutParams.width = (int)ViewUtils.convertDpToPixels(activity, graphicModalMaxWidthDp);
            height = (int)(ViewUtils.convertDpToPixels(activity, graphicModalMaxWidthDp) / n);
        }
        else {
            layoutParams.width = (int)(ViewUtils.convertDpToPixels(activity, graphicModalMaxHeightDp) * n);
            height = (int)ViewUtils.convertDpToPixels(activity, graphicModalMaxHeightDp);
        }
        layoutParams.height = height;
        this.findViewById(R$id.com_appboy_inappmessage_modal_graphic_bound).setLayoutParams((ViewGroup$LayoutParams)layoutParams);
    }
    
    private void setInAppMessageImageViewAttributes(final Activity activity, final IInAppMessageImmersive inAppMessageImmersive, final IInAppMessageImageView inAppMessageImageView) {
        final float cornersRadiusPx = (float)ViewUtils.convertDpToPixels(activity, AppboyInAppMessageParams.getModalizedImageRadiusDp());
        if (inAppMessageImmersive.getImageStyle().equals(ImageStyle.GRAPHIC)) {
            inAppMessageImageView.setCornersRadiusPx(cornersRadiusPx);
        }
        else {
            inAppMessageImageView.setCornersRadiiPx(cornersRadiusPx, cornersRadiusPx, 0.0f, 0.0f);
        }
        inAppMessageImageView.setInAppMessageImageCropType(inAppMessageImmersive.getCropType());
    }
    
    @Override
    public View getFrameView() {
        return this.findViewById(R$id.com_appboy_inappmessage_modal_frame);
    }
    
    @Override
    public Drawable getMessageBackgroundObject() {
        return this.getMessageClickableView().getBackground();
    }
    
    @Override
    public List<View> getMessageButtonViews() {
        final ArrayList<View> list = new ArrayList<View>();
        if (this.findViewById(R$id.com_appboy_inappmessage_modal_button_one) != null) {
            list.add(this.findViewById(R$id.com_appboy_inappmessage_modal_button_one));
        }
        if (this.findViewById(R$id.com_appboy_inappmessage_modal_button_two) != null) {
            list.add(this.findViewById(R$id.com_appboy_inappmessage_modal_button_two));
        }
        return list;
    }
    
    @Override
    public View getMessageButtonsView() {
        return this.findViewById(R$id.com_appboy_inappmessage_modal_button_layout);
    }
    
    @Override
    public View getMessageClickableView() {
        return this.findViewById(R$id.com_appboy_inappmessage_modal);
    }
    
    @Override
    public View getMessageCloseButtonView() {
        return this.findViewById(R$id.com_appboy_inappmessage_modal_close_button);
    }
    
    @Override
    public TextView getMessageHeaderTextView() {
        return (TextView)this.findViewById(R$id.com_appboy_inappmessage_modal_header_text);
    }
    
    @Override
    public TextView getMessageIconView() {
        return (TextView)this.findViewById(R$id.com_appboy_inappmessage_modal_icon);
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
        return (TextView)this.findViewById(R$id.com_appboy_inappmessage_modal_message);
    }
    
    public void inflateStubViews(final Activity activity, final IInAppMessageImmersive inAppMessageImmersive) {
        if (this.mCanUseFresco) {
            final View properViewFromInflatedStub = this.getProperViewFromInflatedStub(R$id.com_appboy_inappmessage_modal_drawee_stub);
            this.mSimpleDraweeView = properViewFromInflatedStub;
            this.setInAppMessageImageViewAttributes(activity, inAppMessageImmersive, (IInAppMessageImageView)properViewFromInflatedStub);
            return;
        }
        this.setInAppMessageImageViewAttributes(activity, inAppMessageImmersive, this.mAppboyInAppMessageImageView = (AppboyInAppMessageImageView)this.getProperViewFromInflatedStub(R$id.com_appboy_inappmessage_modal_imageview_stub));
        if (inAppMessageImmersive.getImageStyle().equals(ImageStyle.GRAPHIC) && inAppMessageImmersive.getBitmap() != null) {
            final double n = inAppMessageImmersive.getBitmap().getWidth();
            final double n2 = inAppMessageImmersive.getBitmap().getHeight();
            Double.isNaN(n);
            Double.isNaN(n2);
            this.resizeGraphicFrameIfAppropriate(activity, inAppMessageImmersive, n / n2);
        }
    }
    
    @Override
    public void resetMessageMargins(final boolean b) {
        super.resetMessageMargins(b);
        final RelativeLayout relativeLayout = (RelativeLayout)this.findViewById(R$id.com_appboy_inappmessage_modal_image_layout);
        if ((b || this.getMessageIconView() != null) && relativeLayout != null) {
            final RelativeLayout$LayoutParams layoutParams = new RelativeLayout$LayoutParams(-1, -2);
            layoutParams.setMargins(0, 0, 0, 0);
            relativeLayout.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        }
        this.findViewById(R$id.com_appboy_inappmessage_modal_text_layout).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                AppboyLogger.d(AppboyInAppMessageModalView.TAG, "Passing scrollView click event to message clickable view.");
                AppboyInAppMessageModalView.this.getMessageClickableView().performClick();
            }
        });
    }
    
    @Override
    public void setMessageBackgroundColor(final int n) {
        InAppMessageViewUtils.setViewBackgroundColorFilter(this.findViewById(R$id.com_appboy_inappmessage_modal), n, this.getContext().getResources().getColor(R$color.com_appboy_inappmessage_background_light));
    }
    
    public void setMessageSimpleDrawee(final IInAppMessageImmersive messageSimpleDrawee, final Activity activity) {
        if (messageSimpleDrawee.getImageStyle().equals(ImageStyle.GRAPHIC)) {
            FrescoLibraryUtils.setDraweeControllerHelper((SimpleDraweeView)this.getMessageSimpleDraweeView(), this.getAppropriateImageUrl(messageSimpleDrawee), 0.0f, false, (ControllerListener<ImageInfo>)new BaseControllerListener<ImageInfo>() {
                public void onFinalImageSet(final String s, final ImageInfo imageInfo, final Animatable animatable) {
                    if (imageInfo == null) {
                        return;
                    }
                    final double n = imageInfo.getWidth();
                    final double n2 = imageInfo.getHeight();
                    Double.isNaN(n);
                    Double.isNaN(n2);
                    AppboyInAppMessageModalView.this.mSimpleDraweeView.post((Runnable)new Runnable() {
                        final /* synthetic */ double val$imageAspectRatio = n / n2;
                        
                        @Override
                        public void run() {
                            AppboyInAppMessageModalView.this.resizeGraphicFrameIfAppropriate(activity, messageSimpleDrawee, this.val$imageAspectRatio);
                        }
                    });
                }
            });
            return;
        }
        this.setMessageSimpleDrawee(messageSimpleDrawee);
    }
}
