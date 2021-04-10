package android.support.v4.view;

import java.lang.reflect.*;
import android.view.*;
import android.content.*;
import android.util.*;

class LayoutInflaterCompatHC
{
    private static final String TAG = "LayoutInflaterCompatHC";
    private static boolean sCheckedField;
    private static Field sLayoutInflaterFactory2Field;
    
    static void forceSetFactory2(final LayoutInflater layoutInflater, final LayoutInflater$Factory2 layoutInflater$Factory2) {
        if (!LayoutInflaterCompatHC.sCheckedField) {
            try {
                (LayoutInflaterCompatHC.sLayoutInflaterFactory2Field = LayoutInflater.class.getDeclaredField("mFactory2")).setAccessible(true);
            }
            catch (NoSuchFieldException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("forceSetFactory2 Could not find field 'mFactory2' on class ");
                sb.append(LayoutInflater.class.getName());
                sb.append("; inflation may have unexpected results.");
                Log.e("LayoutInflaterCompatHC", sb.toString(), (Throwable)ex);
            }
            LayoutInflaterCompatHC.sCheckedField = true;
        }
        if (LayoutInflaterCompatHC.sLayoutInflaterFactory2Field != null) {
            try {
                LayoutInflaterCompatHC.sLayoutInflaterFactory2Field.set(layoutInflater, layoutInflater$Factory2);
            }
            catch (IllegalAccessException ex2) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("forceSetFactory2 could not set the Factory2 on LayoutInflater ");
                sb2.append(layoutInflater);
                sb2.append("; inflation may have unexpected results.");
                Log.e("LayoutInflaterCompatHC", sb2.toString(), (Throwable)ex2);
            }
        }
    }
    
    static void setFactory(final LayoutInflater layoutInflater, final LayoutInflaterFactory layoutInflaterFactory) {
        Object factory2;
        if (layoutInflaterFactory != null) {
            factory2 = new FactoryWrapperHC(layoutInflaterFactory);
        }
        else {
            factory2 = null;
        }
        layoutInflater.setFactory2((LayoutInflater$Factory2)factory2);
        final LayoutInflater$Factory factory3 = layoutInflater.getFactory();
        if (factory3 instanceof LayoutInflater$Factory2) {
            forceSetFactory2(layoutInflater, (LayoutInflater$Factory2)factory3);
            return;
        }
        forceSetFactory2(layoutInflater, (LayoutInflater$Factory2)factory2);
    }
    
    static class FactoryWrapperHC extends FactoryWrapper implements LayoutInflater$Factory2
    {
        FactoryWrapperHC(final LayoutInflaterFactory layoutInflaterFactory) {
            super(layoutInflaterFactory);
        }
        
        public View onCreateView(final View view, final String s, final Context context, final AttributeSet set) {
            return this.mDelegateFactory.onCreateView(view, s, context, set);
        }
    }
}
