package com.microsoft.xboxtcui;

import android.app.*;
import android.content.*;
import android.widget.*;
import com.microsoft.xbox.toolkit.ui.*;
import com.microsoft.xbox.toolkit.*;
import android.view.*;
import android.os.*;

public class XboxTcuiWindowDialog extends Dialog
{
    private DetachedCallback detachedCallback;
    private final XboxTcuiWindow xboxTcuiWindow;
    
    public XboxTcuiWindowDialog(final Activity activity, final Class<? extends ScreenLayout> clazz, final ActivityParameters activityParameters) {
        super((Context)activity, R$style.TcuiDialog);
        this.xboxTcuiWindow = new XboxTcuiWindow(activity, clazz, activityParameters);
    }
    
    private void addCloseButton() {
        final FrameLayout frameLayout = new FrameLayout(this.getContext());
        final FrameLayout$LayoutParams frameLayout$LayoutParams = new FrameLayout$LayoutParams(-2, -2);
        frameLayout$LayoutParams.gravity = 5;
        final XLEButton xleButton = new XLEButton(this.getContext());
        xleButton.setPadding(60, 0, 0, 0);
        xleButton.setBackgroundResource(R$drawable.common_button_background);
        xleButton.setText(R$string.ic_Close);
        xleButton.setTextColor(-1);
        xleButton.setTextSize(2, 14.0f);
        xleButton.setTypeFace("fonts/SegXboxSymbol.ttf");
        xleButton.setContentDescription((CharSequence)this.getContext().getResources().getString(R$string.TextInput_Confirm));
        xleButton.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                try {
                    NavigationManager.getInstance().PopAllScreens();
                }
                catch (XLEException ex) {}
            }
        });
        xleButton.setOnKeyListener((View$OnKeyListener)NavigationManager.getInstance());
        frameLayout.addView((View)xleButton);
        this.addContentView((View)frameLayout, (ViewGroup$LayoutParams)frameLayout$LayoutParams);
    }
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.requestWindowFeature(1);
        this.getWindow().setLayout(-1, -1);
        this.getWindow().setFlags(1024, 1024);
        this.xboxTcuiWindow.onCreate(bundle);
        this.setContentView((View)this.xboxTcuiWindow);
        this.addCloseButton();
        NavigationManager.getInstance().setOnNavigatedListener((NavigationManager.OnNavigatedListener)new NavigationManager.OnNavigatedListener() {
            @Override
            public void onPageNavigated(final ScreenLayout screenLayout, final ScreenLayout screenLayout2) {
                if (screenLayout2 == null) {
                    XboxTcuiWindowDialog.this.dismiss();
                }
            }
            
            @Override
            public void onPageRestarted(final ScreenLayout screenLayout) {
            }
        });
    }
    
    public void onDetachedFromWindow() {
        final DetachedCallback detachedCallback = this.detachedCallback;
        if (detachedCallback != null) {
            detachedCallback.onDetachedFromWindow();
        }
        super.onDetachedFromWindow();
    }
    
    public void onStart() {
        this.xboxTcuiWindow.onStart();
    }
    
    protected void onStop() {
        this.xboxTcuiWindow.onStop();
    }
    
    public void setDetachedCallback(final DetachedCallback detachedCallback) {
        this.detachedCallback = detachedCallback;
    }
    
    public interface DetachedCallback
    {
        void onDetachedFromWindow();
    }
}
