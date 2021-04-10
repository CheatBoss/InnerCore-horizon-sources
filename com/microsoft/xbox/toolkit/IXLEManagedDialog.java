package com.microsoft.xbox.toolkit;

import android.app.*;

public interface IXLEManagedDialog
{
    Dialog getDialog();
    
    DialogType getDialogType();
    
    void quickDismiss();
    
    void safeDismiss();
    
    void setDialogType(final DialogType p0);
    
    public enum DialogType
    {
        FATAL, 
        NON_FATAL, 
        NORMAL;
    }
}
