package android.support.v4.view;

import android.os.*;
import android.graphics.*;
import android.view.*;

public final class GravityCompat
{
    public static final int END = 8388613;
    static final GravityCompatImpl IMPL;
    public static final int RELATIVE_HORIZONTAL_GRAVITY_MASK = 8388615;
    public static final int RELATIVE_LAYOUT_DIRECTION = 8388608;
    public static final int START = 8388611;
    
    static {
        GravityCompatImpl impl;
        if (Build$VERSION.SDK_INT >= 17) {
            impl = new GravityCompatImplJellybeanMr1();
        }
        else {
            impl = new GravityCompatImplBase();
        }
        IMPL = impl;
    }
    
    private GravityCompat() {
    }
    
    public static void apply(final int n, final int n2, final int n3, final Rect rect, final int n4, final int n5, final Rect rect2, final int n6) {
        GravityCompat.IMPL.apply(n, n2, n3, rect, n4, n5, rect2, n6);
    }
    
    public static void apply(final int n, final int n2, final int n3, final Rect rect, final Rect rect2, final int n4) {
        GravityCompat.IMPL.apply(n, n2, n3, rect, rect2, n4);
    }
    
    public static void applyDisplay(final int n, final Rect rect, final Rect rect2, final int n2) {
        GravityCompat.IMPL.applyDisplay(n, rect, rect2, n2);
    }
    
    public static int getAbsoluteGravity(final int n, final int n2) {
        return GravityCompat.IMPL.getAbsoluteGravity(n, n2);
    }
    
    interface GravityCompatImpl
    {
        void apply(final int p0, final int p1, final int p2, final Rect p3, final int p4, final int p5, final Rect p6, final int p7);
        
        void apply(final int p0, final int p1, final int p2, final Rect p3, final Rect p4, final int p5);
        
        void applyDisplay(final int p0, final Rect p1, final Rect p2, final int p3);
        
        int getAbsoluteGravity(final int p0, final int p1);
    }
    
    static class GravityCompatImplBase implements GravityCompatImpl
    {
        @Override
        public void apply(final int n, final int n2, final int n3, final Rect rect, final int n4, final int n5, final Rect rect2, final int n6) {
            Gravity.apply(n, n2, n3, rect, n4, n5, rect2);
        }
        
        @Override
        public void apply(final int n, final int n2, final int n3, final Rect rect, final Rect rect2, final int n4) {
            Gravity.apply(n, n2, n3, rect, rect2);
        }
        
        @Override
        public void applyDisplay(final int n, final Rect rect, final Rect rect2, final int n2) {
            Gravity.applyDisplay(n, rect, rect2);
        }
        
        @Override
        public int getAbsoluteGravity(final int n, final int n2) {
            return n & 0xFF7FFFFF;
        }
    }
    
    static class GravityCompatImplJellybeanMr1 implements GravityCompatImpl
    {
        @Override
        public void apply(final int n, final int n2, final int n3, final Rect rect, final int n4, final int n5, final Rect rect2, final int n6) {
            GravityCompatJellybeanMr1.apply(n, n2, n3, rect, n4, n5, rect2, n6);
        }
        
        @Override
        public void apply(final int n, final int n2, final int n3, final Rect rect, final Rect rect2, final int n4) {
            GravityCompatJellybeanMr1.apply(n, n2, n3, rect, rect2, n4);
        }
        
        @Override
        public void applyDisplay(final int n, final Rect rect, final Rect rect2, final int n2) {
            GravityCompatJellybeanMr1.applyDisplay(n, rect, rect2, n2);
        }
        
        @Override
        public int getAbsoluteGravity(final int n, final int n2) {
            return GravityCompatJellybeanMr1.getAbsoluteGravity(n, n2);
        }
    }
}
