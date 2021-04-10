package com.microsoft.xal.browser;

import android.app.*;
import com.microsoft.xal.logging.*;
import android.os.*;
import android.content.*;

public class IntentHandler extends Activity
{
    private final XalLogger m_logger;
    
    public IntentHandler() {
        this.m_logger = new XalLogger("IntentHandler");
    }
    
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.m_logger.Important("onCreate() New intent received.");
        this.m_logger.Flush();
        final Intent intent = new Intent((Context)this, (Class)WebView.class);
        intent.setData(this.getIntent().getData());
        intent.addFlags(603979776);
        this.startActivity(intent);
        this.finish();
    }
}
