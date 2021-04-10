package org.mineprogramming.horizon.innercore.view;

import org.mineprogramming.horizon.innercore.*;
import android.app.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import android.content.*;
import com.zhekasmirnov.innercore.utils.*;

public abstract class ModView extends Displayable
{
    protected ModView(final Context context) {
        super(context);
    }
    
    protected void assureDelete(final String s) {
        new AlertDialog$Builder(this.context).setMessage((CharSequence)JsonInflater.getString(this.context, "assure_delete")).setPositiveButton((CharSequence)JsonInflater.getString(this.context, "yes"), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                FileTools.delete(s);
                ModView.this.onDelete();
            }
        }).setNegativeButton((CharSequence)JsonInflater.getString(this.context, "no"), (DialogInterface$OnClickListener)null).create().show();
    }
    
    protected void onDelete() {
    }
}
