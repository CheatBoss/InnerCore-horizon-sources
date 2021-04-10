package com.microsoft.xbox.toolkit;

import com.microsoft.xbox.toolkit.ui.*;
import java.util.*;
import android.widget.*;
import com.microsoft.xboxtcui.*;
import android.content.*;
import android.app.*;
import android.view.*;

public abstract class DialogManagerBase implements IProjectSpecificDialogManager
{
    private BlockingScreen blockingSpinner;
    private CancellableBlockingScreen cancelableBlockingDialog;
    private Stack<IXLEManagedDialog> dialogStack;
    private boolean isEnabled;
    private Toast visibleToast;
    
    protected DialogManagerBase() {
        this.dialogStack = new Stack<IXLEManagedDialog>();
        XLEAssert.assertTrue(Thread.currentThread() == ThreadManager.UIThread);
    }
    
    private XLEManagedAlertDialog buildDialog(final String title, final String message, final String s, final Runnable runnable, final String s2, final Runnable runnable2) {
        final XLEManagedAlertDialog xleManagedAlertDialog = new XLEManagedAlertDialog((Context)XboxTcuiSdk.getActivity());
        xleManagedAlertDialog.setTitle((CharSequence)title);
        xleManagedAlertDialog.setMessage((CharSequence)message);
        xleManagedAlertDialog.setButton(-1, (CharSequence)s, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                ThreadManager.UIThreadPost(runnable);
            }
        });
        final Runnable runnable3 = new Runnable() {
            @Override
            public void run() {
                DialogManagerBase.this.dismissManagedDialog(xleManagedAlertDialog);
                final Runnable val$cancelHandler = runnable2;
                if (val$cancelHandler != null) {
                    val$cancelHandler.run();
                }
            }
        };
        xleManagedAlertDialog.setButton(-2, (CharSequence)s2, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                ThreadManager.UIThreadPost(runnable3);
            }
        });
        if (s2 != null && s2.length() != 0) {
            xleManagedAlertDialog.setCancelable(true);
            xleManagedAlertDialog.setOnCancelListener((DialogInterface$OnCancelListener)new DialogInterface$OnCancelListener() {
                public void onCancel(final DialogInterface dialogInterface) {
                    ThreadManager.UIThreadPost(runnable3);
                }
            });
            return xleManagedAlertDialog;
        }
        xleManagedAlertDialog.setCancelable(false);
        return xleManagedAlertDialog;
    }
    
    @Override
    public void addManagedDialog(final IXLEManagedDialog ixleManagedDialog) {
        if (this.isEnabled) {
            this.dialogStack.push(ixleManagedDialog);
            ixleManagedDialog.getDialog().show();
        }
    }
    
    @Override
    public void dismissBlocking() {
        final BlockingScreen blockingSpinner = this.blockingSpinner;
        if (blockingSpinner != null) {
            blockingSpinner.dismiss();
            this.blockingSpinner = null;
        }
        final CancellableBlockingScreen cancelableBlockingDialog = this.cancelableBlockingDialog;
        if (cancelableBlockingDialog != null) {
            cancelableBlockingDialog.dismiss();
            this.cancelableBlockingDialog = null;
        }
    }
    
    @Override
    public void dismissManagedDialog(final IXLEManagedDialog ixleManagedDialog) {
        if (this.isEnabled) {
            this.dialogStack.remove(ixleManagedDialog);
            ixleManagedDialog.getDialog().dismiss();
        }
    }
    
    @Override
    public void dismissToast() {
        final Toast visibleToast = this.visibleToast;
        if (visibleToast != null) {
            visibleToast.cancel();
            this.visibleToast = null;
        }
    }
    
    @Override
    public void dismissTopNonFatalAlert() {
        if (this.dialogStack.size() > 0 && this.dialogStack.peek().getDialogType() != IXLEManagedDialog.DialogType.FATAL) {
            this.dialogStack.pop().getDialog().dismiss();
        }
    }
    
    @Override
    public void forceDismissAlerts() {
        while (this.dialogStack.size() > 0) {
            this.dialogStack.pop().quickDismiss();
        }
    }
    
    @Override
    public void forceDismissAll() {
        this.dismissToast();
        this.forceDismissAlerts();
        this.dismissBlocking();
    }
    
    @Override
    public boolean getIsBlocking() {
        final BlockingScreen blockingSpinner = this.blockingSpinner;
        if (blockingSpinner == null || !blockingSpinner.isShowing()) {
            final CancellableBlockingScreen cancelableBlockingDialog = this.cancelableBlockingDialog;
            if (cancelableBlockingDialog == null || !cancelableBlockingDialog.isShowing()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public Dialog getVisibleDialog() {
        if (!this.dialogStack.isEmpty()) {
            return this.dialogStack.peek().getDialog();
        }
        return null;
    }
    
    @Override
    public void onDialogStopped(final IXLEManagedDialog ixleManagedDialog) {
        this.dialogStack.remove(ixleManagedDialog);
    }
    
    @Override
    public void setBlocking(final boolean b, final String s) {
        XLEAssert.assertTrue(Thread.currentThread() == ThreadManager.UIThread);
        if (this.isEnabled) {
            if (b) {
                if (this.blockingSpinner == null) {
                    this.blockingSpinner = new BlockingScreen((Context)XboxTcuiSdk.getActivity());
                }
                this.blockingSpinner.show((Context)XboxTcuiSdk.getActivity(), s);
                return;
            }
            final BlockingScreen blockingSpinner = this.blockingSpinner;
            if (blockingSpinner != null) {
                blockingSpinner.dismiss();
                this.blockingSpinner = null;
            }
        }
    }
    
    @Override
    public void setCancelableBlocking(final boolean b, final String s, final Runnable runnable) {
        XLEAssert.assertTrue(Thread.currentThread() == ThreadManager.UIThread);
        if (this.isEnabled) {
            if (b) {
                if (this.cancelableBlockingDialog == null) {
                    (this.cancelableBlockingDialog = new CancellableBlockingScreen((Context)XboxTcuiSdk.getActivity())).setCancelButtonAction((View$OnClickListener)new View$OnClickListener() {
                        public void onClick(final View view) {
                            DialogManagerBase.this.cancelableBlockingDialog.dismiss();
                            DialogManagerBase.this.cancelableBlockingDialog = null;
                            runnable.run();
                        }
                    });
                }
                this.cancelableBlockingDialog.show((Context)XboxTcuiSdk.getActivity(), s);
                return;
            }
            final CancellableBlockingScreen cancelableBlockingDialog = this.cancelableBlockingDialog;
            if (cancelableBlockingDialog != null) {
                cancelableBlockingDialog.dismiss();
                this.cancelableBlockingDialog = null;
            }
        }
    }
    
    @Override
    public void setEnabled(final boolean isEnabled) {
        if (this.isEnabled != isEnabled) {
            this.isEnabled = isEnabled;
        }
    }
    
    protected boolean shouldDismissAllBeforeOpeningADialog() {
        return true;
    }
    
    @Override
    public void showFatalAlertDialog(final String s, final String s2, final String s3, final Runnable runnable) {
        this.forceDismissAll();
        if (this.isEnabled) {
            final XLEManagedAlertDialog buildDialog = this.buildDialog(s, s2, s3, runnable, null, null);
            buildDialog.setDialogType(IXLEManagedDialog.DialogType.FATAL);
            this.dialogStack.push(buildDialog);
            buildDialog.show();
        }
    }
    
    @Override
    public void showManagedDialog(final IXLEManagedDialog ixleManagedDialog) {
        if (this.shouldDismissAllBeforeOpeningADialog()) {
            this.forceDismissAll();
        }
        if (this.isEnabled && XboxTcuiSdk.getActivity() != null && !XboxTcuiSdk.getActivity().isFinishing()) {
            this.dialogStack.push(ixleManagedDialog);
            try {
                ixleManagedDialog.getDialog().show();
            }
            catch (RuntimeException ex) {
                final String message = ex.getMessage();
                if (message != null && message.contains("Adding window failed")) {
                    return;
                }
                throw ex;
            }
        }
    }
    
    @Override
    public void showNonFatalAlertDialog(final String s, final String s2, final String s3, final Runnable runnable) {
        if (this.isEnabled) {
            final XLEManagedAlertDialog buildDialog = this.buildDialog(s, s2, s3, runnable, null, null);
            buildDialog.setDialogType(IXLEManagedDialog.DialogType.NON_FATAL);
            this.dialogStack.push(buildDialog);
            buildDialog.show();
        }
    }
    
    @Override
    public void showOkCancelDialog(final String s, final String s2, final String s3, final Runnable runnable, final String s4, final Runnable runnable2) {
        XLEAssert.assertNotNull("You must supply cancel text if this is not a must-act dialog.", s4);
        if (this.dialogStack.size() > 0) {
            return;
        }
        if (this.isEnabled && XboxTcuiSdk.getActivity() != null && !XboxTcuiSdk.getActivity().isFinishing()) {
            final XLEManagedAlertDialog buildDialog = this.buildDialog(s, s2, s3, runnable, s4, runnable2);
            buildDialog.setDialogType(IXLEManagedDialog.DialogType.NORMAL);
            this.dialogStack.push(buildDialog);
            buildDialog.show();
        }
    }
    
    @Override
    public void showToast(final int n) {
        this.dismissToast();
        if (this.isEnabled) {
            (this.visibleToast = Toast.makeText((Context)XboxTcuiSdk.getActivity(), n, 1)).show();
        }
    }
}
