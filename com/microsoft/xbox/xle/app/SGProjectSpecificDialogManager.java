package com.microsoft.xbox.xle.app;

import com.microsoft.xbox.xle.app.dialog.*;
import com.microsoft.xbox.toolkit.*;
import com.microsoft.xbox.xle.viewmodel.*;
import com.microsoft.xboxtcui.*;
import android.content.*;

public class SGProjectSpecificDialogManager extends DialogManagerBase
{
    private static IProjectSpecificDialogManager instance;
    private ChangeFriendshipDialog changeFriendshipDialog;
    
    static {
        SGProjectSpecificDialogManager.instance = new SGProjectSpecificDialogManager();
    }
    
    private SGProjectSpecificDialogManager() {
    }
    
    public static IProjectSpecificDialogManager getInstance() {
        return SGProjectSpecificDialogManager.instance;
    }
    
    public static SGProjectSpecificDialogManager getProjectSpecificInstance() {
        return (SGProjectSpecificDialogManager)DialogManager.getInstance().getManager();
    }
    
    public void dismissChangeFriendshipDialog() {
        final ChangeFriendshipDialog changeFriendshipDialog = this.changeFriendshipDialog;
        if (changeFriendshipDialog != null) {
            this.dismissManagedDialog(changeFriendshipDialog);
            this.changeFriendshipDialog = null;
        }
    }
    
    @Override
    public void forceDismissAll() {
        super.forceDismissAll();
        this.dismissChangeFriendshipDialog();
    }
    
    public void notifyChangeFriendshipDialogAsyncTaskCompleted() {
        final ChangeFriendshipDialog changeFriendshipDialog = this.changeFriendshipDialog;
        if (changeFriendshipDialog != null) {
            changeFriendshipDialog.reportAsyncTaskCompleted();
        }
    }
    
    public void notifyChangeFriendshipDialogAsyncTaskFailed(final String s) {
        final ChangeFriendshipDialog changeFriendshipDialog = this.changeFriendshipDialog;
        if (changeFriendshipDialog != null) {
            changeFriendshipDialog.reportAsyncTaskFailed(s);
        }
    }
    
    public void notifyChangeFriendshipDialogUpdateView() {
        final ChangeFriendshipDialog changeFriendshipDialog = this.changeFriendshipDialog;
        if (changeFriendshipDialog != null) {
            changeFriendshipDialog.updateView();
        }
    }
    
    @Override
    public void onApplicationPause() {
        this.forceDismissAll();
    }
    
    @Override
    public void onApplicationResume() {
    }
    
    @Override
    protected boolean shouldDismissAllBeforeOpeningADialog() {
        return false;
    }
    
    public void showChangeFriendshipDialog(final ChangeFriendshipDialogViewModel vm, final ViewModelBase viewModelBase) {
        final ChangeFriendshipDialog changeFriendshipDialog = this.changeFriendshipDialog;
        if (changeFriendshipDialog != null) {
            changeFriendshipDialog.setVm(vm);
            this.changeFriendshipDialog.getDialog().show();
            return;
        }
        this.addManagedDialog(this.changeFriendshipDialog = new ChangeFriendshipDialog((Context)XboxTcuiSdk.getActivity(), vm, viewModelBase));
    }
}
