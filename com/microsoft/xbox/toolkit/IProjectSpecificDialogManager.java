package com.microsoft.xbox.toolkit;

import android.app.*;

public interface IProjectSpecificDialogManager
{
    void addManagedDialog(final IXLEManagedDialog p0);
    
    void dismissBlocking();
    
    void dismissManagedDialog(final IXLEManagedDialog p0);
    
    void dismissToast();
    
    void dismissTopNonFatalAlert();
    
    void forceDismissAlerts();
    
    void forceDismissAll();
    
    boolean getIsBlocking();
    
    Dialog getVisibleDialog();
    
    void onApplicationPause();
    
    void onApplicationResume();
    
    void onDialogStopped(final IXLEManagedDialog p0);
    
    void setBlocking(final boolean p0, final String p1);
    
    void setCancelableBlocking(final boolean p0, final String p1, final Runnable p2);
    
    void setEnabled(final boolean p0);
    
    void showFatalAlertDialog(final String p0, final String p1, final String p2, final Runnable p3);
    
    void showManagedDialog(final IXLEManagedDialog p0);
    
    void showNonFatalAlertDialog(final String p0, final String p1, final String p2, final Runnable p3);
    
    void showOkCancelDialog(final String p0, final String p1, final String p2, final Runnable p3, final String p4, final Runnable p5);
    
    void showToast(final int p0);
}
