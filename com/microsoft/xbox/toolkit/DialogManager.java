package com.microsoft.xbox.toolkit;

import android.app.*;

public class DialogManager implements IProjectSpecificDialogManager
{
    private static DialogManager instance;
    private IProjectSpecificDialogManager manager;
    
    static {
        DialogManager.instance = new DialogManager();
    }
    
    private DialogManager() {
    }
    
    private void checkProvider() {
    }
    
    public static DialogManager getInstance() {
        return DialogManager.instance;
    }
    
    @Override
    public void addManagedDialog(final IXLEManagedDialog ixleManagedDialog) {
        this.checkProvider();
        final IProjectSpecificDialogManager manager = this.manager;
        if (manager != null) {
            manager.addManagedDialog(ixleManagedDialog);
        }
    }
    
    @Override
    public void dismissBlocking() {
        this.checkProvider();
        final IProjectSpecificDialogManager manager = this.manager;
        if (manager != null) {
            manager.dismissBlocking();
        }
    }
    
    @Override
    public void dismissManagedDialog(final IXLEManagedDialog ixleManagedDialog) {
        this.checkProvider();
        final IProjectSpecificDialogManager manager = this.manager;
        if (manager != null) {
            manager.dismissManagedDialog(ixleManagedDialog);
        }
    }
    
    @Override
    public void dismissToast() {
        this.checkProvider();
        final IProjectSpecificDialogManager manager = this.manager;
        if (manager != null) {
            manager.dismissToast();
        }
    }
    
    @Override
    public void dismissTopNonFatalAlert() {
        this.checkProvider();
        final IProjectSpecificDialogManager manager = this.manager;
        if (manager != null) {
            manager.dismissTopNonFatalAlert();
        }
    }
    
    @Override
    public void forceDismissAlerts() {
        this.checkProvider();
        final IProjectSpecificDialogManager manager = this.manager;
        if (manager != null) {
            manager.forceDismissAlerts();
        }
    }
    
    @Override
    public void forceDismissAll() {
        this.checkProvider();
        final IProjectSpecificDialogManager manager = this.manager;
        if (manager != null) {
            manager.forceDismissAll();
        }
    }
    
    @Override
    public boolean getIsBlocking() {
        this.checkProvider();
        final IProjectSpecificDialogManager manager = this.manager;
        return manager != null && manager.getIsBlocking();
    }
    
    public IProjectSpecificDialogManager getManager() {
        return this.manager;
    }
    
    @Override
    public Dialog getVisibleDialog() {
        this.checkProvider();
        final IProjectSpecificDialogManager manager = this.manager;
        if (manager != null) {
            return manager.getVisibleDialog();
        }
        return null;
    }
    
    @Override
    public void onApplicationPause() {
        final IProjectSpecificDialogManager manager = this.manager;
        if (manager != null) {
            manager.onApplicationPause();
        }
    }
    
    @Override
    public void onApplicationResume() {
        final IProjectSpecificDialogManager manager = this.manager;
        if (manager != null) {
            manager.onApplicationResume();
        }
    }
    
    @Override
    public void onDialogStopped(final IXLEManagedDialog ixleManagedDialog) {
        this.checkProvider();
        final IProjectSpecificDialogManager manager = this.manager;
        if (manager != null) {
            manager.onDialogStopped(ixleManagedDialog);
        }
    }
    
    @Override
    public void setBlocking(final boolean b, final String s) {
        this.checkProvider();
        final IProjectSpecificDialogManager manager = this.manager;
        if (manager != null) {
            manager.setBlocking(b, s);
        }
    }
    
    @Override
    public void setCancelableBlocking(final boolean b, final String s, final Runnable runnable) {
        this.checkProvider();
        final IProjectSpecificDialogManager manager = this.manager;
        if (manager != null) {
            manager.setCancelableBlocking(b, s, runnable);
        }
    }
    
    @Override
    public void setEnabled(final boolean enabled) {
        this.checkProvider();
        final IProjectSpecificDialogManager manager = this.manager;
        if (manager != null) {
            manager.setEnabled(enabled);
        }
    }
    
    public void setManager(final IProjectSpecificDialogManager manager) {
        this.manager = manager;
    }
    
    @Override
    public void showFatalAlertDialog(final String s, final String s2, final String s3, final Runnable runnable) {
        this.checkProvider();
        final IProjectSpecificDialogManager manager = this.manager;
        if (manager != null) {
            manager.showFatalAlertDialog(s, s2, s3, runnable);
        }
    }
    
    @Override
    public void showManagedDialog(final IXLEManagedDialog ixleManagedDialog) {
        this.checkProvider();
        final IProjectSpecificDialogManager manager = this.manager;
        if (manager != null) {
            manager.showManagedDialog(ixleManagedDialog);
        }
    }
    
    @Override
    public void showNonFatalAlertDialog(final String s, final String s2, final String s3, final Runnable runnable) {
        this.checkProvider();
        final IProjectSpecificDialogManager manager = this.manager;
        if (manager != null) {
            manager.showNonFatalAlertDialog(s, s2, s3, runnable);
        }
    }
    
    @Override
    public void showOkCancelDialog(final String s, final String s2, final String s3, final Runnable runnable, final String s4, final Runnable runnable2) {
        this.checkProvider();
        final IProjectSpecificDialogManager manager = this.manager;
        if (manager != null) {
            manager.showOkCancelDialog(s, s2, s3, runnable, s4, runnable2);
        }
    }
    
    @Override
    public void showToast(final int n) {
        this.checkProvider();
        final IProjectSpecificDialogManager manager = this.manager;
        if (manager != null) {
            manager.showToast(n);
        }
    }
}
