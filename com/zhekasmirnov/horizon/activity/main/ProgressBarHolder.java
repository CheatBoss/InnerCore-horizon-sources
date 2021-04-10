package com.zhekasmirnov.horizon.activity.main;

import com.zhekasmirnov.horizon.runtime.task.*;
import android.app.*;
import android.widget.*;
import android.view.*;

public class ProgressBarHolder implements TaskManager.StateCallback
{
    private final Activity context;
    private final ProgressBar progressBar;
    private final TextView label;
    
    public ProgressBarHolder(final Activity context, final View progressBar, final View label) {
        this.context = context;
        this.progressBar = (ProgressBar)progressBar;
        this.label = (TextView)label;
    }
    
    @Override
    public void onStateUpdated(final TaskManager manager, final TaskManager.ThreadHolder holder) {
        final boolean isVisible = manager.getNumRunningThreads() > 0;
        final String description = manager.getFormattedTaskDescriptions(1);
        this.context.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                ProgressBarHolder.this.progressBar.setVisibility(isVisible ? 0 : 4);
                ProgressBarHolder.this.label.setVisibility(isVisible ? 0 : 4);
                ProgressBarHolder.this.label.setText((CharSequence)description);
            }
        });
    }
}
