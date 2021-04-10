package com.zhekasmirnov.horizon.activity.util;

import com.zhekasmirnov.horizon.modloader.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import java.util.*;
import com.zhekasmirnov.horizon.*;
import android.app.*;
import android.content.*;

public class CompilerErrorDialogHelper
{
    public static boolean showCompilationErrors(final ModContext context) {
        final List<EventLogger.Message> messages = context.getEventLogger().getMessages(new EventLogger.Filter() {
            @Override
            public boolean filter(final EventLogger.Message message) {
                return message.type == EventLogger.MessageType.EXCEPTION || message.type == EventLogger.MessageType.FAULT;
            }
        });
        context.getEventLogger().clear();
        final List<String> errors = new ArrayList<String>();
        for (final EventLogger.Message message : messages) {
            errors.add(message.message);
        }
        if (errors.size() == 0) {
            return false;
        }
        showDialog(errors, 2131624005, true);
        return true;
    }
    
    public static void showErrors(final ModContext context, final int title) {
        final List<EventLogger.Message> messages = context.getEventLogger().getMessages(new EventLogger.Filter() {
            @Override
            public boolean filter(final EventLogger.Message message) {
                return message.type == EventLogger.MessageType.EXCEPTION || message.type == EventLogger.MessageType.FAULT;
            }
        });
        context.getEventLogger().clear();
        final List<String> errors = new ArrayList<String>();
        for (final EventLogger.Message message : messages) {
            errors.add(message.message);
        }
        showDialog(errors, title, false);
    }
    
    private static void showDialog(final List<String> errors, final int title, final boolean monospace) {
        final Activity activity = HorizonApplication.getTopRunningActivity();
        final BooleanContainer complete = new BooleanContainer();
        activity.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                final CompilerErrorDialog dialog = new CompilerErrorDialog((Context)activity, errors, 2131689480);
                dialog.setPositiveButton(17039370, (DialogInterface.OnClickListener)new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int which) {
                        complete.aBoolean = true;
                    }
                });
                dialog.setTitle(title);
                dialog.create().show();
            }
        });
        while (!complete.aBoolean) {
            Thread.yield();
        }
    }
    
    private static class BooleanContainer
    {
        boolean aBoolean;
        
        private BooleanContainer() {
            this.aBoolean = false;
        }
    }
}
