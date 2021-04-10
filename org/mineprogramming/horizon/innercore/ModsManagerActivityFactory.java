package org.mineprogramming.horizon.innercore;

import com.zhekasmirnov.horizon.launcher.pack.*;
import org.mineprogramming.horizon.innercore.view.*;
import org.mineprogramming.horizon.innercore.util.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import android.widget.*;
import android.os.*;
import android.graphics.*;
import android.app.*;
import android.view.*;
import android.content.*;

public class ModsManagerActivityFactory extends Pack$MenuActivityFactory
{
    private ModsManagerActivity activityMain;
    
    public String getIconGraphics() {
        return "mod_manager";
    }
    
    public String getMenuTitle() {
        ResourceReader.init();
        return JsonInflater.getString((Context)this.getPack().getContextHolder().getContext(), "mods_manager");
    }
    
    public boolean onBackPressed() {
        return this.activityMain.onBackPressed();
    }
    
    public void onCreateLayout(final Activity activity, final RelativeLayout relativeLayout) {
        (this.activityMain = new ModsManagerActivity((Context)activity)).display((ViewGroup)relativeLayout);
        if (Build$VERSION.SDK_INT >= 21) {
            final Window window = activity.getWindow();
            window.clearFlags(67108864);
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(Color.parseColor("#2895F1"));
        }
        final SharedPreferences sharedPreferences = activity.getSharedPreferences("ModsManager", 0);
        if (sharedPreferences.getBoolean("firstLaunch", true)) {
            sharedPreferences.edit().putBoolean("firstLaunch", false).commit();
            new AlertDialog$Builder((Context)activity, activity.getResources().getIdentifier("AppTheme.Dialog", "style", "com.zheka.horizon")).setMessage((CharSequence)JsonInflater.getString((Context)activity, "optimized_warning")).setPositiveButton((CharSequence)JsonInflater.getString((Context)activity, "ok"), (DialogInterface$OnClickListener)null).show();
        }
    }
}
