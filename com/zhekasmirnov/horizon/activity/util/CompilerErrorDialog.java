package com.zhekasmirnov.horizon.activity.util;

import android.app.*;
import java.util.*;
import android.content.*;
import android.widget.*;

public class CompilerErrorDialog extends AlertDialog.Builder
{
    public CompilerErrorDialog(final Context context, final List<String> messages, final int themeResId) {
        super(context, themeResId);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, 2131427367, 2131230775) {
            public boolean isEnabled(final int position) {
                return false;
            }
        };
        adapter.addAll((Collection)messages);
        this.setAdapter((ListAdapter)adapter, (DialogInterface.OnClickListener)new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, final int which) {
            }
        });
        this.setCancelable(false);
    }
}
