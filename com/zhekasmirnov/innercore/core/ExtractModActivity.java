package com.zhekasmirnov.innercore.core;

import android.app.*;
import java.lang.ref.*;
import android.widget.*;
import android.os.*;
import android.content.*;

public class ExtractModActivity extends Activity
{
    public static WeakReference<ExtractModActivity> current;
    private String currentDescription;
    private String currentLog;
    private ImageView icon;
    private Button installButton;
    private boolean isAccepted;
    private boolean isDestroyed;
    private TextView log;
    private TextView title;
    
    public ExtractModActivity() {
        this.isAccepted = false;
        this.isDestroyed = false;
    }
    
    private void updateLog() {
        this.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                if (ExtractModActivity.this.currentDescription != null && ExtractModActivity.this.currentDescription.length() > 0) {
                    final TextView access$200 = ExtractModActivity.this.log;
                    final StringBuilder sb = new StringBuilder();
                    sb.append(ExtractModActivity.this.currentDescription);
                    sb.append("\n\n----------------\n\n");
                    sb.append(ExtractModActivity.this.currentLog);
                    access$200.setText((CharSequence)sb.toString());
                    return;
                }
                ExtractModActivity.this.log.setText((CharSequence)ExtractModActivity.this.currentLog);
            }
        });
    }
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
    }
    
    protected void onDestroy() {
        super.onDestroy();
        this.isDestroyed = true;
    }
    
    public void onNewIntent(final Intent intent) {
    }
}
