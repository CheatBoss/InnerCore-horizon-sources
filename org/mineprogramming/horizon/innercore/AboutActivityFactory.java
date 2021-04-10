package org.mineprogramming.horizon.innercore;

import com.zhekasmirnov.horizon.launcher.pack.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import android.content.*;
import android.app.*;
import android.widget.*;
import org.mineprogramming.horizon.innercore.view.*;
import android.os.*;
import android.graphics.*;
import android.view.*;

public class AboutActivityFactory extends Pack$MenuActivityFactory
{
    public String getIconGraphics() {
        return "about";
    }
    
    public String getMenuTitle() {
        return JsonInflater.getString((Context)this.getPack().getContextHolder().getContext(), "preferences_links");
    }
    
    public void onCreateLayout(final Activity activity, final RelativeLayout relativeLayout) {
        new AboutActivity((Context)activity).display((ViewGroup)relativeLayout);
        if (Build$VERSION.SDK_INT >= 21) {
            final Window window = activity.getWindow();
            window.clearFlags(67108864);
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(Color.parseColor("#2895F1"));
        }
    }
}
