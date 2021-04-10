package android.support.v4.widget;

import android.os.*;
import android.widget.*;
import android.view.*;

public final class PopupWindowCompat
{
    static final PopupWindowImpl IMPL;
    
    static {
        final int sdk_INT = Build$VERSION.SDK_INT;
        PopupWindowImpl impl;
        if (sdk_INT >= 23) {
            impl = new Api23PopupWindowImpl();
        }
        else if (sdk_INT >= 21) {
            impl = new Api21PopupWindowImpl();
        }
        else if (sdk_INT >= 19) {
            impl = new KitKatPopupWindowImpl();
        }
        else if (sdk_INT >= 9) {
            impl = new GingerbreadPopupWindowImpl();
        }
        else {
            impl = new BasePopupWindowImpl();
        }
        IMPL = impl;
    }
    
    private PopupWindowCompat() {
    }
    
    public static boolean getOverlapAnchor(final PopupWindow popupWindow) {
        return PopupWindowCompat.IMPL.getOverlapAnchor(popupWindow);
    }
    
    public static int getWindowLayoutType(final PopupWindow popupWindow) {
        return PopupWindowCompat.IMPL.getWindowLayoutType(popupWindow);
    }
    
    public static void setOverlapAnchor(final PopupWindow popupWindow, final boolean b) {
        PopupWindowCompat.IMPL.setOverlapAnchor(popupWindow, b);
    }
    
    public static void setWindowLayoutType(final PopupWindow popupWindow, final int n) {
        PopupWindowCompat.IMPL.setWindowLayoutType(popupWindow, n);
    }
    
    public static void showAsDropDown(final PopupWindow popupWindow, final View view, final int n, final int n2, final int n3) {
        PopupWindowCompat.IMPL.showAsDropDown(popupWindow, view, n, n2, n3);
    }
    
    static class Api21PopupWindowImpl extends KitKatPopupWindowImpl
    {
        @Override
        public boolean getOverlapAnchor(final PopupWindow popupWindow) {
            return PopupWindowCompatApi21.getOverlapAnchor(popupWindow);
        }
        
        @Override
        public void setOverlapAnchor(final PopupWindow popupWindow, final boolean b) {
            PopupWindowCompatApi21.setOverlapAnchor(popupWindow, b);
        }
    }
    
    static class Api23PopupWindowImpl extends Api21PopupWindowImpl
    {
        @Override
        public boolean getOverlapAnchor(final PopupWindow popupWindow) {
            return PopupWindowCompatApi23.getOverlapAnchor(popupWindow);
        }
        
        @Override
        public int getWindowLayoutType(final PopupWindow popupWindow) {
            return PopupWindowCompatApi23.getWindowLayoutType(popupWindow);
        }
        
        @Override
        public void setOverlapAnchor(final PopupWindow popupWindow, final boolean b) {
            PopupWindowCompatApi23.setOverlapAnchor(popupWindow, b);
        }
        
        @Override
        public void setWindowLayoutType(final PopupWindow popupWindow, final int n) {
            PopupWindowCompatApi23.setWindowLayoutType(popupWindow, n);
        }
    }
    
    static class BasePopupWindowImpl implements PopupWindowImpl
    {
        @Override
        public boolean getOverlapAnchor(final PopupWindow popupWindow) {
            return false;
        }
        
        @Override
        public int getWindowLayoutType(final PopupWindow popupWindow) {
            return 0;
        }
        
        @Override
        public void setOverlapAnchor(final PopupWindow popupWindow, final boolean b) {
        }
        
        @Override
        public void setWindowLayoutType(final PopupWindow popupWindow, final int n) {
        }
        
        @Override
        public void showAsDropDown(final PopupWindow popupWindow, final View view, final int n, final int n2, final int n3) {
            popupWindow.showAsDropDown(view, n, n2);
        }
    }
    
    static class GingerbreadPopupWindowImpl extends BasePopupWindowImpl
    {
        @Override
        public int getWindowLayoutType(final PopupWindow popupWindow) {
            return PopupWindowCompatGingerbread.getWindowLayoutType(popupWindow);
        }
        
        @Override
        public void setWindowLayoutType(final PopupWindow popupWindow, final int n) {
            PopupWindowCompatGingerbread.setWindowLayoutType(popupWindow, n);
        }
    }
    
    static class KitKatPopupWindowImpl extends GingerbreadPopupWindowImpl
    {
        @Override
        public void showAsDropDown(final PopupWindow popupWindow, final View view, final int n, final int n2, final int n3) {
            PopupWindowCompatKitKat.showAsDropDown(popupWindow, view, n, n2, n3);
        }
    }
    
    interface PopupWindowImpl
    {
        boolean getOverlapAnchor(final PopupWindow p0);
        
        int getWindowLayoutType(final PopupWindow p0);
        
        void setOverlapAnchor(final PopupWindow p0, final boolean p1);
        
        void setWindowLayoutType(final PopupWindow p0, final int p1);
        
        void showAsDropDown(final PopupWindow p0, final View p1, final int p2, final int p3, final int p4);
    }
}
