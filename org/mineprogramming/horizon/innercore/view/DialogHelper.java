package org.mineprogramming.horizon.innercore.view;

import android.app.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import android.content.*;
import android.widget.*;
import android.view.*;

public class DialogHelper
{
    public static void assure(final Context context, final String message, final Runnable runnable) {
        new AlertDialog$Builder(context).setMessage((CharSequence)message).setPositiveButton((CharSequence)JsonInflater.getString(context, "yes"), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                runnable.run();
            }
        }).setNegativeButton((CharSequence)JsonInflater.getString(context, "no"), (DialogInterface$OnClickListener)null).create().show();
    }
    
    public static void input(final Context context, final String message, final String text, final InputListener inputListener) {
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder(context);
        final EditText view = new EditText(context);
        view.setInputType(1);
        view.setText((CharSequence)text);
        alertDialog$Builder.setView((View)view);
        alertDialog$Builder.setMessage((CharSequence)message).setPositiveButton((CharSequence)JsonInflater.getString(context, "yes"), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                inputListener.onInput(view.getText().toString());
            }
        }).setNegativeButton((CharSequence)JsonInflater.getString(context, "no"), (DialogInterface$OnClickListener)null).create().show();
    }
    
    public interface InputListener
    {
        void onInput(final String p0);
    }
}
