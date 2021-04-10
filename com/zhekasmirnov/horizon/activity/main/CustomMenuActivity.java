package com.zhekasmirnov.horizon.activity.main;

import android.support.v7.app.*;
import com.zhekasmirnov.horizon.launcher.pack.*;
import android.os.*;
import android.app.*;
import android.widget.*;
import android.content.*;
import android.view.*;

public class CustomMenuActivity extends AppCompatActivity
{
    private static Pack.MenuActivityFactory pendingFactoryToAttach;
    private Pack.MenuActivityFactory attachedFactory;
    
    public CustomMenuActivity() {
        this.attachedFactory = null;
    }
    
    public static void prepareForStart(final Pack.MenuActivityFactory factory) {
        CustomMenuActivity.pendingFactoryToAttach = factory;
    }
    
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(2131427357);
        if (this.attachedFactory == null) {
            this.attachedFactory = CustomMenuActivity.pendingFactoryToAttach;
            if (this.attachedFactory == null) {
                throw new RuntimeException("CustomMenuActivity did not called prepareForStart");
            }
        }
        final RelativeLayout root = (RelativeLayout)this.findViewById(2131230787);
        this.attachedFactory.onCreateLayout((Activity)this, root);
        if (root.getChildCount() == 0) {
            final TextView text = new TextView((Context)this);
            text.setGravity(17);
            text.setText(2131624063);
            text.setTextColor(this.getResources().getColor(2131034258));
            text.setTextSize(2, 20.0f);
            root.addView((View)text, (ViewGroup.LayoutParams)new RelativeLayout.LayoutParams(-1, -1));
        }
    }
    
    public void onBackPressed() {
        if (this.attachedFactory != null) {
            final boolean handled = this.attachedFactory.onBackPressed();
            if (handled) {
                return;
            }
        }
        super.onBackPressed();
    }
    
    static {
        CustomMenuActivity.pendingFactoryToAttach = null;
    }
}
