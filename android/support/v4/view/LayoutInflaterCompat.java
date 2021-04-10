package android.support.v4.view;

import android.os.*;
import android.view.*;

public final class LayoutInflaterCompat
{
    static final LayoutInflaterCompatImpl IMPL;
    
    static {
        final int sdk_INT = Build$VERSION.SDK_INT;
        LayoutInflaterCompatImpl impl;
        if (sdk_INT >= 21) {
            impl = new LayoutInflaterCompatImplV21();
        }
        else if (sdk_INT >= 11) {
            impl = new LayoutInflaterCompatImplV11();
        }
        else {
            impl = new LayoutInflaterCompatImplBase();
        }
        IMPL = impl;
    }
    
    private LayoutInflaterCompat() {
    }
    
    public static LayoutInflaterFactory getFactory(final LayoutInflater layoutInflater) {
        return LayoutInflaterCompat.IMPL.getFactory(layoutInflater);
    }
    
    public static void setFactory(final LayoutInflater layoutInflater, final LayoutInflaterFactory layoutInflaterFactory) {
        LayoutInflaterCompat.IMPL.setFactory(layoutInflater, layoutInflaterFactory);
    }
    
    interface LayoutInflaterCompatImpl
    {
        LayoutInflaterFactory getFactory(final LayoutInflater p0);
        
        void setFactory(final LayoutInflater p0, final LayoutInflaterFactory p1);
    }
    
    static class LayoutInflaterCompatImplBase implements LayoutInflaterCompatImpl
    {
        @Override
        public LayoutInflaterFactory getFactory(final LayoutInflater layoutInflater) {
            return LayoutInflaterCompatBase.getFactory(layoutInflater);
        }
        
        @Override
        public void setFactory(final LayoutInflater layoutInflater, final LayoutInflaterFactory layoutInflaterFactory) {
            LayoutInflaterCompatBase.setFactory(layoutInflater, layoutInflaterFactory);
        }
    }
    
    static class LayoutInflaterCompatImplV11 extends LayoutInflaterCompatImplBase
    {
        @Override
        public void setFactory(final LayoutInflater layoutInflater, final LayoutInflaterFactory layoutInflaterFactory) {
            LayoutInflaterCompatHC.setFactory(layoutInflater, layoutInflaterFactory);
        }
    }
    
    static class LayoutInflaterCompatImplV21 extends LayoutInflaterCompatImplV11
    {
        @Override
        public void setFactory(final LayoutInflater layoutInflater, final LayoutInflaterFactory layoutInflaterFactory) {
            LayoutInflaterCompatLollipop.setFactory(layoutInflater, layoutInflaterFactory);
        }
    }
}
