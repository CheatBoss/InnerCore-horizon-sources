package android.support.v4.widget;

import java.lang.reflect.*;
import android.widget.*;

class PopupWindowCompatGingerbread
{
    private static Method sGetWindowLayoutTypeMethod;
    private static boolean sGetWindowLayoutTypeMethodAttempted;
    private static Method sSetWindowLayoutTypeMethod;
    private static boolean sSetWindowLayoutTypeMethodAttempted;
    
    static int getWindowLayoutType(final PopupWindow popupWindow) {
        if (!PopupWindowCompatGingerbread.sGetWindowLayoutTypeMethodAttempted) {
            try {
                (PopupWindowCompatGingerbread.sGetWindowLayoutTypeMethod = PopupWindow.class.getDeclaredMethod("getWindowLayoutType", (Class<?>[])new Class[0])).setAccessible(true);
            }
            catch (Exception ex) {}
            PopupWindowCompatGingerbread.sGetWindowLayoutTypeMethodAttempted = true;
        }
        if (PopupWindowCompatGingerbread.sGetWindowLayoutTypeMethod != null) {
            try {
                return (int)PopupWindowCompatGingerbread.sGetWindowLayoutTypeMethod.invoke(popupWindow, new Object[0]);
            }
            catch (Exception ex2) {}
        }
        return 0;
    }
    
    static void setWindowLayoutType(final PopupWindow popupWindow, final int n) {
        if (!PopupWindowCompatGingerbread.sSetWindowLayoutTypeMethodAttempted) {
            try {
                (PopupWindowCompatGingerbread.sSetWindowLayoutTypeMethod = PopupWindow.class.getDeclaredMethod("setWindowLayoutType", Integer.TYPE)).setAccessible(true);
            }
            catch (Exception ex) {}
            PopupWindowCompatGingerbread.sSetWindowLayoutTypeMethodAttempted = true;
        }
        if (PopupWindowCompatGingerbread.sSetWindowLayoutTypeMethod != null) {
            try {
                PopupWindowCompatGingerbread.sSetWindowLayoutTypeMethod.invoke(popupWindow, n);
            }
            catch (Exception ex2) {}
        }
    }
}
