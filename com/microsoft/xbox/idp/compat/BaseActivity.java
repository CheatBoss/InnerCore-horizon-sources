package com.microsoft.xbox.idp.compat;

import android.app.*;
import android.os.*;

public abstract class BaseActivity extends Activity
{
    public void addFragment(final int n, final BaseFragment baseFragment) {
        this.getFragmentManager().beginTransaction().add(n, (Fragment)baseFragment).commit();
    }
    
    public boolean hasFragment(final int n) {
        return this.getFragmentManager().findFragmentById(n) != null;
    }
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setOrientation();
    }
    
    public void setOrientation() {
        if ((this.getApplicationContext().getResources().getConfiguration().screenLayout & 0xF) < 3) {
            this.setRequestedOrientation(1);
        }
    }
}
