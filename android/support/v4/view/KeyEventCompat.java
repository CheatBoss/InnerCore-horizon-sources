package android.support.v4.view;

import android.os.*;
import android.view.*;

public final class KeyEventCompat
{
    static final KeyEventVersionImpl IMPL;
    
    static {
        KeyEventVersionImpl impl;
        if (Build$VERSION.SDK_INT >= 11) {
            impl = new HoneycombKeyEventVersionImpl();
        }
        else {
            impl = new BaseKeyEventVersionImpl();
        }
        IMPL = impl;
    }
    
    private KeyEventCompat() {
    }
    
    public static boolean dispatch(final KeyEvent keyEvent, final KeyEvent$Callback keyEvent$Callback, final Object o, final Object o2) {
        return KeyEventCompat.IMPL.dispatch(keyEvent, keyEvent$Callback, o, o2);
    }
    
    public static Object getKeyDispatcherState(final View view) {
        return KeyEventCompat.IMPL.getKeyDispatcherState(view);
    }
    
    public static boolean hasModifiers(final KeyEvent keyEvent, final int n) {
        return KeyEventCompat.IMPL.metaStateHasModifiers(keyEvent.getMetaState(), n);
    }
    
    public static boolean hasNoModifiers(final KeyEvent keyEvent) {
        return KeyEventCompat.IMPL.metaStateHasNoModifiers(keyEvent.getMetaState());
    }
    
    public static boolean isTracking(final KeyEvent keyEvent) {
        return KeyEventCompat.IMPL.isTracking(keyEvent);
    }
    
    public static boolean metaStateHasModifiers(final int n, final int n2) {
        return KeyEventCompat.IMPL.metaStateHasModifiers(n, n2);
    }
    
    public static boolean metaStateHasNoModifiers(final int n) {
        return KeyEventCompat.IMPL.metaStateHasNoModifiers(n);
    }
    
    public static int normalizeMetaState(final int n) {
        return KeyEventCompat.IMPL.normalizeMetaState(n);
    }
    
    public static void startTracking(final KeyEvent keyEvent) {
        KeyEventCompat.IMPL.startTracking(keyEvent);
    }
    
    static class BaseKeyEventVersionImpl implements KeyEventVersionImpl
    {
        private static final int META_ALL_MASK = 247;
        private static final int META_MODIFIER_MASK = 247;
        
        private static int metaStateFilterDirectionalModifiers(final int n, int n2, final int n3, int n4, int n5) {
            final int n6 = 0;
            final boolean b = (n2 & n3) != 0x0;
            n5 |= n4;
            n4 = n6;
            if ((n2 & n5) != 0x0) {
                n4 = 1;
            }
            if (!b) {
                n2 = n;
                if (n4 != 0) {
                    n2 = (n & ~n3);
                }
                return n2;
            }
            if (n4 != 0) {
                throw new IllegalArgumentException("bad arguments");
            }
            return n & ~n5;
        }
        
        @Override
        public boolean dispatch(final KeyEvent keyEvent, final KeyEvent$Callback keyEvent$Callback, final Object o, final Object o2) {
            return keyEvent.dispatch(keyEvent$Callback);
        }
        
        @Override
        public Object getKeyDispatcherState(final View view) {
            return null;
        }
        
        @Override
        public boolean isTracking(final KeyEvent keyEvent) {
            return false;
        }
        
        @Override
        public boolean metaStateHasModifiers(final int n, final int n2) {
            return metaStateFilterDirectionalModifiers(metaStateFilterDirectionalModifiers(this.normalizeMetaState(n) & 0xF7, n2, 1, 64, 128), n2, 2, 16, 32) == n2;
        }
        
        @Override
        public boolean metaStateHasNoModifiers(final int n) {
            return (this.normalizeMetaState(n) & 0xF7) == 0x0;
        }
        
        @Override
        public int normalizeMetaState(int n) {
            int n2 = n;
            if ((n & 0xC0) != 0x0) {
                n2 = (n | 0x1);
            }
            n = n2;
            if ((n2 & 0x30) != 0x0) {
                n = (n2 | 0x2);
            }
            return n & 0xF7;
        }
        
        @Override
        public void startTracking(final KeyEvent keyEvent) {
        }
    }
    
    static class EclairKeyEventVersionImpl extends BaseKeyEventVersionImpl
    {
        @Override
        public boolean dispatch(final KeyEvent keyEvent, final KeyEvent$Callback keyEvent$Callback, final Object o, final Object o2) {
            return KeyEventCompatEclair.dispatch(keyEvent, keyEvent$Callback, o, o2);
        }
        
        @Override
        public Object getKeyDispatcherState(final View view) {
            return KeyEventCompatEclair.getKeyDispatcherState(view);
        }
        
        @Override
        public boolean isTracking(final KeyEvent keyEvent) {
            return KeyEventCompatEclair.isTracking(keyEvent);
        }
        
        @Override
        public void startTracking(final KeyEvent keyEvent) {
            KeyEventCompatEclair.startTracking(keyEvent);
        }
    }
    
    static class HoneycombKeyEventVersionImpl extends EclairKeyEventVersionImpl
    {
        @Override
        public boolean metaStateHasModifiers(final int n, final int n2) {
            return KeyEventCompatHoneycomb.metaStateHasModifiers(n, n2);
        }
        
        @Override
        public boolean metaStateHasNoModifiers(final int n) {
            return KeyEventCompatHoneycomb.metaStateHasNoModifiers(n);
        }
        
        @Override
        public int normalizeMetaState(final int n) {
            return KeyEventCompatHoneycomb.normalizeMetaState(n);
        }
    }
    
    interface KeyEventVersionImpl
    {
        boolean dispatch(final KeyEvent p0, final KeyEvent$Callback p1, final Object p2, final Object p3);
        
        Object getKeyDispatcherState(final View p0);
        
        boolean isTracking(final KeyEvent p0);
        
        boolean metaStateHasModifiers(final int p0, final int p1);
        
        boolean metaStateHasNoModifiers(final int p0);
        
        int normalizeMetaState(final int p0);
        
        void startTracking(final KeyEvent p0);
    }
}
