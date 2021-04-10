package android.support.v4.widget;

import android.os.*;
import android.view.*;

public final class PopupMenuCompat
{
    static final PopupMenuImpl IMPL;
    
    static {
        PopupMenuImpl impl;
        if (Build$VERSION.SDK_INT >= 19) {
            impl = new KitKatPopupMenuImpl();
        }
        else {
            impl = new BasePopupMenuImpl();
        }
        IMPL = impl;
    }
    
    private PopupMenuCompat() {
    }
    
    public static View$OnTouchListener getDragToOpenListener(final Object o) {
        return PopupMenuCompat.IMPL.getDragToOpenListener(o);
    }
    
    static class BasePopupMenuImpl implements PopupMenuImpl
    {
        @Override
        public View$OnTouchListener getDragToOpenListener(final Object o) {
            return null;
        }
    }
    
    static class KitKatPopupMenuImpl extends BasePopupMenuImpl
    {
        @Override
        public View$OnTouchListener getDragToOpenListener(final Object o) {
            return PopupMenuCompatKitKat.getDragToOpenListener(o);
        }
    }
    
    interface PopupMenuImpl
    {
        View$OnTouchListener getDragToOpenListener(final Object p0);
    }
}
