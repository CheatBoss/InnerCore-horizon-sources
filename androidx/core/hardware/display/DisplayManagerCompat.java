package androidx.core.hardware.display;

import java.util.*;
import android.content.*;
import android.os.*;
import android.hardware.display.*;
import android.view.*;
import androidx.annotation.*;

public final class DisplayManagerCompat
{
    public static final String DISPLAY_CATEGORY_PRESENTATION = "android.hardware.display.category.PRESENTATION";
    private static final WeakHashMap<Context, DisplayManagerCompat> sInstances;
    private final Context mContext;
    
    static {
        sInstances = new WeakHashMap<Context, DisplayManagerCompat>();
    }
    
    private DisplayManagerCompat(final Context mContext) {
        this.mContext = mContext;
    }
    
    @NonNull
    public static DisplayManagerCompat getInstance(@NonNull final Context context) {
        synchronized (DisplayManagerCompat.sInstances) {
            DisplayManagerCompat displayManagerCompat;
            if ((displayManagerCompat = DisplayManagerCompat.sInstances.get(context)) == null) {
                displayManagerCompat = new DisplayManagerCompat(context);
                DisplayManagerCompat.sInstances.put(context, displayManagerCompat);
            }
            return displayManagerCompat;
        }
    }
    
    @Nullable
    public Display getDisplay(final int n) {
        if (Build$VERSION.SDK_INT >= 17) {
            return ((DisplayManager)this.mContext.getSystemService("display")).getDisplay(n);
        }
        final Display defaultDisplay = ((WindowManager)this.mContext.getSystemService("window")).getDefaultDisplay();
        if (defaultDisplay.getDisplayId() == n) {
            return defaultDisplay;
        }
        return null;
    }
    
    @NonNull
    public Display[] getDisplays() {
        if (Build$VERSION.SDK_INT >= 17) {
            return ((DisplayManager)this.mContext.getSystemService("display")).getDisplays();
        }
        return new Display[] { ((WindowManager)this.mContext.getSystemService("window")).getDefaultDisplay() };
    }
    
    @NonNull
    public Display[] getDisplays(@Nullable final String s) {
        if (Build$VERSION.SDK_INT >= 17) {
            return ((DisplayManager)this.mContext.getSystemService("display")).getDisplays(s);
        }
        if (s == null) {
            return new Display[0];
        }
        return new Display[] { ((WindowManager)this.mContext.getSystemService("window")).getDefaultDisplay() };
    }
}
