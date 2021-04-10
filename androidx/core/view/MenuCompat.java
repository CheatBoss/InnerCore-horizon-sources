package androidx.core.view;

import androidx.core.internal.view.*;
import android.os.*;
import android.annotation.*;
import android.view.*;

public final class MenuCompat
{
    private MenuCompat() {
    }
    
    @SuppressLint({ "NewApi" })
    public static void setGroupDividerEnabled(final Menu menu, final boolean b) {
        if (menu instanceof SupportMenu) {
            ((SupportMenu)menu).setGroupDividerEnabled(b);
            return;
        }
        if (Build$VERSION.SDK_INT >= 28) {
            menu.setGroupDividerEnabled(b);
        }
    }
    
    @Deprecated
    public static void setShowAsAction(final MenuItem menuItem, final int showAsAction) {
        menuItem.setShowAsAction(showAsAction);
    }
}
