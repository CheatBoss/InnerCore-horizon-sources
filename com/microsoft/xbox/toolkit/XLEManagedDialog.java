package com.microsoft.xbox.toolkit;

import android.app.*;
import android.view.*;
import android.content.*;
import com.microsoft.xbox.toolkit.system.*;
import com.microsoft.xbox.toolkit.ui.*;
import android.os.*;
import com.microsoft.xbox.toolkit.anim.*;
import com.microsoft.xbox.xle.anim.*;

public class XLEManagedDialog extends Dialog implements IXLEManagedDialog
{
    protected static final String BODY_ANIMATION_NAME = "Dialog";
    protected String bodyAnimationName;
    final Runnable callAfterAnimationIn;
    final Runnable callAfterAnimationOut;
    protected View dialogBody;
    private DialogType dialogType;
    protected Runnable onAnimateOutCompletedRunable;
    
    public XLEManagedDialog(final Context context) {
        super(context);
        this.dialogBody = null;
        this.onAnimateOutCompletedRunable = null;
        this.dialogType = DialogType.NORMAL;
        this.bodyAnimationName = "Dialog";
        this.callAfterAnimationIn = new Runnable() {
            @Override
            public void run() {
                XLEManagedDialog.this.OnAnimationInEnd();
            }
        };
        this.callAfterAnimationOut = new Runnable() {
            @Override
            public void run() {
                XLEManagedDialog.this.OnAnimationOutEnd();
            }
        };
    }
    
    public XLEManagedDialog(final Context context, final int n) {
        super(context, n);
        this.dialogBody = null;
        this.onAnimateOutCompletedRunable = null;
        this.dialogType = DialogType.NORMAL;
        this.bodyAnimationName = "Dialog";
        this.callAfterAnimationIn = new Runnable() {
            @Override
            public void run() {
                XLEManagedDialog.this.OnAnimationInEnd();
            }
        };
        this.callAfterAnimationOut = new Runnable() {
            @Override
            public void run() {
                XLEManagedDialog.this.OnAnimationOutEnd();
            }
        };
    }
    
    protected XLEManagedDialog(final Context context, final boolean b, final DialogInterface$OnCancelListener dialogInterface$OnCancelListener) {
        super(context, b, dialogInterface$OnCancelListener);
        this.dialogBody = null;
        this.onAnimateOutCompletedRunable = null;
        this.dialogType = DialogType.NORMAL;
        this.bodyAnimationName = "Dialog";
        this.callAfterAnimationIn = new Runnable() {
            @Override
            public void run() {
                XLEManagedDialog.this.OnAnimationInEnd();
            }
        };
        this.callAfterAnimationOut = new Runnable() {
            @Override
            public void run() {
                XLEManagedDialog.this.OnAnimationOutEnd();
            }
        };
    }
    
    protected static boolean isKindle() {
        return SystemUtil.isKindle();
    }
    
    public void OnAnimationInEnd() {
        NavigationManager.getInstance().setAnimationBlocking(false);
    }
    
    public void OnAnimationOutEnd() {
        NavigationManager.getInstance().setAnimationBlocking(false);
        super.dismiss();
        final Runnable onAnimateOutCompletedRunable = this.onAnimateOutCompletedRunable;
        if (onAnimateOutCompletedRunable != null) {
            try {
                onAnimateOutCompletedRunable.run();
            }
            catch (Exception ex) {}
        }
    }
    
    public void dismiss() {
        if (!this.isShowing()) {
            super.dismiss();
            return;
        }
        final XLEAnimationPackage animateOut = this.getAnimateOut();
        if (this.getDialogBody() != null && animateOut != null) {
            NavigationManager.getInstance().setAnimationBlocking(true);
            animateOut.setOnAnimationEndRunnable(this.callAfterAnimationOut);
            animateOut.startAnimation();
            return;
        }
        final Runnable onAnimateOutCompletedRunable = this.onAnimateOutCompletedRunable;
        if (onAnimateOutCompletedRunable != null) {
            onAnimateOutCompletedRunable.run();
        }
        super.dismiss();
    }
    
    protected void forceKindleRespectDimOptions() {
        new Handler().postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                XLEManagedDialog.this.getWindow().addFlags(2);
            }
        }, 100L);
    }
    
    public XLEAnimationPackage getAnimateIn() {
        final XLEAnimation bodyAnimation = this.getBodyAnimation(MAAS.MAASAnimationType.ANIMATE_IN, false);
        if (bodyAnimation != null) {
            final XLEAnimationPackage xleAnimationPackage = new XLEAnimationPackage();
            xleAnimationPackage.add(bodyAnimation);
            return xleAnimationPackage;
        }
        return null;
    }
    
    public XLEAnimationPackage getAnimateOut() {
        final XLEAnimation bodyAnimation = this.getBodyAnimation(MAAS.MAASAnimationType.ANIMATE_OUT, true);
        if (bodyAnimation != null) {
            final XLEAnimationPackage xleAnimationPackage = new XLEAnimationPackage();
            xleAnimationPackage.add(bodyAnimation);
            return xleAnimationPackage;
        }
        return null;
    }
    
    protected XLEAnimation getBodyAnimation(final MAAS.MAASAnimationType maasAnimationType, final boolean b) {
        if (this.getDialogBody() != null) {
            return ((XLEMAASAnimationPackageNavigationManager)MAAS.getInstance().getAnimation(this.bodyAnimationName)).compile(maasAnimationType, b, this.getDialogBody());
        }
        return null;
    }
    
    public String getBodyAnimationName() {
        return this.bodyAnimationName;
    }
    
    public Dialog getDialog() {
        return this;
    }
    
    public View getDialogBody() {
        return this.dialogBody;
    }
    
    public DialogType getDialogType() {
        return this.dialogType;
    }
    
    public void makeFullScreen() {
        this.getWindow().setLayout(-1, -2);
    }
    
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        final XLEAnimationPackage animateIn = this.getAnimateIn();
        if (this.getDialogBody() != null && animateIn != null) {
            NavigationManager.getInstance().setAnimationBlocking(true);
            animateIn.setOnAnimationEndRunnable(this.callAfterAnimationIn);
            animateIn.startAnimation();
        }
    }
    
    public void onWindowFocusChanged(final boolean b) {
        if (!b) {
            this.safeDismiss();
        }
    }
    
    public void quickDismiss() {
        super.dismiss();
    }
    
    public void safeDismiss() {
        DialogManager.getInstance().dismissManagedDialog(this);
    }
    
    public void setAnimateOutRunnable(final Runnable onAnimateOutCompletedRunable) {
        this.onAnimateOutCompletedRunable = onAnimateOutCompletedRunable;
    }
    
    public void setBodyAnimationName(final String bodyAnimationName) {
        this.bodyAnimationName = bodyAnimationName;
    }
    
    public void setDialogType(final DialogType dialogType) {
        this.dialogType = dialogType;
    }
}
