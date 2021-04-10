package com.appboy.ui.activities;

import android.app.*;
import com.appboy.ui.inappmessage.*;
import com.appboy.*;
import android.content.*;

public class AppboyBaseActivity extends Activity
{
    public void onPause() {
        super.onPause();
        AppboyInAppMessageManager.getInstance().unregisterInAppMessageManager(this);
    }
    
    public void onResume() {
        super.onResume();
        AppboyInAppMessageManager.getInstance().registerInAppMessageManager(this);
    }
    
    public void onStart() {
        super.onStart();
        Appboy.getInstance((Context)this).openSession(this);
    }
    
    public void onStop() {
        super.onStop();
        Appboy.getInstance((Context)this).closeSession(this);
    }
}
