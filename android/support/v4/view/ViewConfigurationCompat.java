package android.support.v4.view;

import android.os.*;
import android.view.*;

public final class ViewConfigurationCompat
{
    static final ViewConfigurationVersionImpl IMPL;
    
    static {
        ViewConfigurationVersionImpl impl;
        if (Build$VERSION.SDK_INT >= 14) {
            impl = new IcsViewConfigurationVersionImpl();
        }
        else if (Build$VERSION.SDK_INT >= 11) {
            impl = new HoneycombViewConfigurationVersionImpl();
        }
        else if (Build$VERSION.SDK_INT >= 8) {
            impl = new FroyoViewConfigurationVersionImpl();
        }
        else {
            impl = new BaseViewConfigurationVersionImpl();
        }
        IMPL = impl;
    }
    
    private ViewConfigurationCompat() {
    }
    
    public static int getScaledPagingTouchSlop(final ViewConfiguration viewConfiguration) {
        return ViewConfigurationCompat.IMPL.getScaledPagingTouchSlop(viewConfiguration);
    }
    
    public static boolean hasPermanentMenuKey(final ViewConfiguration viewConfiguration) {
        return ViewConfigurationCompat.IMPL.hasPermanentMenuKey(viewConfiguration);
    }
    
    static class BaseViewConfigurationVersionImpl implements ViewConfigurationVersionImpl
    {
        @Override
        public int getScaledPagingTouchSlop(final ViewConfiguration viewConfiguration) {
            return viewConfiguration.getScaledTouchSlop();
        }
        
        @Override
        public boolean hasPermanentMenuKey(final ViewConfiguration viewConfiguration) {
            return true;
        }
    }
    
    static class FroyoViewConfigurationVersionImpl extends BaseViewConfigurationVersionImpl
    {
        @Override
        public int getScaledPagingTouchSlop(final ViewConfiguration viewConfiguration) {
            return ViewConfigurationCompatFroyo.getScaledPagingTouchSlop(viewConfiguration);
        }
    }
    
    static class HoneycombViewConfigurationVersionImpl extends FroyoViewConfigurationVersionImpl
    {
        @Override
        public boolean hasPermanentMenuKey(final ViewConfiguration viewConfiguration) {
            return false;
        }
    }
    
    static class IcsViewConfigurationVersionImpl extends HoneycombViewConfigurationVersionImpl
    {
        @Override
        public boolean hasPermanentMenuKey(final ViewConfiguration viewConfiguration) {
            return ViewConfigurationCompatICS.hasPermanentMenuKey(viewConfiguration);
        }
    }
    
    interface ViewConfigurationVersionImpl
    {
        int getScaledPagingTouchSlop(final ViewConfiguration p0);
        
        boolean hasPermanentMenuKey(final ViewConfiguration p0);
    }
}
