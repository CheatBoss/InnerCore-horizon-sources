package android.support.v4.view;

import android.view.*;

class ViewPropertyAnimatorCompatLollipop
{
    public static void translationZ(final View view, final float n) {
        view.animate().translationZ(n);
    }
    
    public static void translationZBy(final View view, final float n) {
        view.animate().translationZBy(n);
    }
    
    public static void z(final View view, final float n) {
        view.animate().z(n);
    }
    
    public static void zBy(final View view, final float n) {
        view.animate().zBy(n);
    }
}
