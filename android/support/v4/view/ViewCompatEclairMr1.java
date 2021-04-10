package android.support.v4.view;

import android.view.*;
import android.util.*;
import java.lang.reflect.*;

class ViewCompatEclairMr1
{
    public static final String TAG = "ViewCompat";
    private static Method sChildrenDrawingOrderMethod;
    
    public static boolean isOpaque(final View view) {
        return view.isOpaque();
    }
    
    public static void setChildrenDrawingOrderEnabled(final ViewGroup viewGroup, final boolean b) {
        if (ViewCompatEclairMr1.sChildrenDrawingOrderMethod == null) {
            try {
                ViewCompatEclairMr1.sChildrenDrawingOrderMethod = ViewGroup.class.getDeclaredMethod("setChildrenDrawingOrderEnabled", Boolean.TYPE);
            }
            catch (NoSuchMethodException ex) {
                Log.e("ViewCompat", "Unable to find childrenDrawingOrderEnabled", (Throwable)ex);
            }
            ViewCompatEclairMr1.sChildrenDrawingOrderMethod.setAccessible(true);
        }
        try {
            ViewCompatEclairMr1.sChildrenDrawingOrderMethod.invoke(viewGroup, b);
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex2) {
            final Throwable t;
            Log.e("ViewCompat", "Unable to invoke childrenDrawingOrderEnabled", t);
        }
    }
}
