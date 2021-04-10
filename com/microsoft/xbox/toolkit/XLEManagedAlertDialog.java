package com.microsoft.xbox.toolkit;

import android.content.*;
import android.app.*;

public class XLEManagedAlertDialog extends AlertDialog implements IXLEManagedDialog
{
    private DialogType dialogType;
    
    protected XLEManagedAlertDialog(final Context context) {
        super(context);
        this.dialogType = DialogType.NORMAL;
    }
    
    public Dialog getDialog() {
        return (Dialog)this;
    }
    
    public DialogType getDialogType() {
        return this.dialogType;
    }
    
    public void onStop() {
        super.onStop();
        DialogManager.getInstance().onDialogStopped(this);
    }
    
    public void quickDismiss() {
        super.dismiss();
    }
    
    public void safeDismiss() {
        DialogManager.getInstance().dismissManagedDialog(this);
    }
    
    public void setDialogType(final DialogType dialogType) {
        this.dialogType = dialogType;
    }
}
