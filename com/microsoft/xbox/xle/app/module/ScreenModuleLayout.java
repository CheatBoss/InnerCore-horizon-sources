package com.microsoft.xbox.xle.app.module;

import android.widget.*;
import android.content.*;
import android.util.*;
import com.microsoft.xbox.xle.viewmodel.*;
import com.microsoft.xboxtcui.*;
import android.view.*;

public abstract class ScreenModuleLayout extends FrameLayout
{
    public ScreenModuleLayout(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public abstract ViewModelBase getViewModel();
    
    public void invalidateView() {
    }
    
    public void onApplicationPause() {
    }
    
    public void onApplicationResume() {
    }
    
    public void onDestroy() {
    }
    
    public void onPause() {
    }
    
    public void onResume() {
    }
    
    public void onStart() {
    }
    
    public void onStop() {
    }
    
    protected void setContentView(final int n) {
        ((LayoutInflater)XboxTcuiSdk.getSystemService("layout_inflater")).inflate(n, (ViewGroup)this, true);
    }
    
    public abstract void setViewModel(final ViewModelBase p0);
    
    public abstract void updateView();
}
