package com.microsoft.xbox.toolkit.ui;

import android.app.*;
import android.widget.*;
import com.microsoft.xbox.toolkit.*;
import android.content.*;
import android.view.*;
import android.view.animation.*;

public class CancellableBlockingScreen extends Dialog
{
    private XLEButton cancelButton;
    private View container;
    private TextView statusText;
    
    public CancellableBlockingScreen(final Context context) {
        super(context, XLERValueHelper.getStyleRValue("cancellable_dialog_style"));
        this.setCancelable(false);
        this.setOnCancelListener((DialogInterface$OnCancelListener)null);
        this.requestWindowFeature(1);
        this.setContentView(XLERValueHelper.getLayoutRValue("cancellable_blocking_dialog"));
        this.container = this.findViewById(XLERValueHelper.getIdRValue("blocking_dialog_container"));
        this.cancelButton = (XLEButton)this.findViewById(XLERValueHelper.getIdRValue("blocking_dialog_cancel"));
        this.statusText = (TextView)this.findViewById(XLERValueHelper.getIdRValue("blocking_dialog_status_text"));
    }
    
    public void setCancelButtonAction(final View$OnClickListener onClickListener) {
        if (onClickListener != null) {
            this.cancelButton.setOnClickListener(null);
        }
        this.cancelButton.setOnClickListener(onClickListener);
    }
    
    public void setMessage(final CharSequence text) {
        this.statusText.setText(text);
    }
    
    public void show(final Context context, final CharSequence message) {
        final boolean showing = this.isShowing();
        this.setMessage(message);
        this.show();
        if (!showing) {
            final AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            alphaAnimation.setFillAfter(true);
            alphaAnimation.setStartOffset(1000L);
            alphaAnimation.setDuration(1000L);
            this.container.startAnimation((Animation)alphaAnimation);
        }
    }
}
