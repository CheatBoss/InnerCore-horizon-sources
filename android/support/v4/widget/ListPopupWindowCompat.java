package android.support.v4.widget;

import android.os.*;
import android.view.*;

public final class ListPopupWindowCompat
{
    static final ListPopupWindowImpl IMPL;
    
    static {
        ListPopupWindowImpl impl;
        if (Build$VERSION.SDK_INT >= 19) {
            impl = new KitKatListPopupWindowImpl();
        }
        else {
            impl = new BaseListPopupWindowImpl();
        }
        IMPL = impl;
    }
    
    private ListPopupWindowCompat() {
    }
    
    public static View$OnTouchListener createDragToOpenListener(final Object o, final View view) {
        return ListPopupWindowCompat.IMPL.createDragToOpenListener(o, view);
    }
    
    static class BaseListPopupWindowImpl implements ListPopupWindowImpl
    {
        @Override
        public View$OnTouchListener createDragToOpenListener(final Object o, final View view) {
            return null;
        }
    }
    
    static class KitKatListPopupWindowImpl extends BaseListPopupWindowImpl
    {
        @Override
        public View$OnTouchListener createDragToOpenListener(final Object o, final View view) {
            return ListPopupWindowCompatKitKat.createDragToOpenListener(o, view);
        }
    }
    
    interface ListPopupWindowImpl
    {
        View$OnTouchListener createDragToOpenListener(final Object p0, final View p1);
    }
}
