package com.microsoft.xbox.toolkit.ui;

import android.app.*;
import com.microsoft.xbox.toolkit.*;
import android.widget.*;
import android.content.*;

public class BlockingScreen extends Dialog
{
    public BlockingScreen(final Context context) {
        super(context, XLERValueHelper.getStyleRValue("blocking_dialog_style"));
        this.requestWindowFeature(1);
    }
    
    public void setMessage(final CharSequence text) {
        ((TextView)this.findViewById(XLERValueHelper.getIdRValue("blocking_dialog_status_text"))).setText(text);
    }
    
    public void show(final Context context, final CharSequence message) {
        this.setCancelable(false);
        this.setOnCancelListener((DialogInterface$OnCancelListener)null);
        this.setContentView(XLERValueHelper.getLayoutRValue("blocking_dialog"));
        this.setMessage(message);
        this.show();
    }
}
