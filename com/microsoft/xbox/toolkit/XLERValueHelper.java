package com.microsoft.xbox.toolkit;

import java.lang.reflect.*;
import android.view.*;
import com.microsoft.xboxtcui.*;

public class XLERValueHelper
{
    public static int findDimensionIdByName(final String s) {
        Field field;
        try {
            field = R$dimen.class.getField(s);
        }
        catch (NoSuchFieldException ex) {
            field = null;
        }
        if (field != null) {
            try {
                return field.getInt(null);
            }
            catch (IllegalAccessException ex2) {
                return -1;
            }
        }
        return -1;
    }
    
    public static View findViewByString(final String s) {
        Field field;
        try {
            field = R$id.class.getField(s);
        }
        catch (NoSuchFieldException ex) {
            field = null;
        }
        if (field != null) {
            try {
                final int int1 = field.getInt(null);
                return XboxTcuiSdk.getActivity().findViewById(int1);
            }
            catch (IllegalAccessException ex2) {}
        }
        final int int1 = -1;
        return XboxTcuiSdk.getActivity().findViewById(int1);
    }
    
    protected static Class getColorRClass() {
        return R$color.class;
    }
    
    public static int getColorRValue(final String s) {
        try {
            return getColorRClass().getDeclaredField(s).getInt(null);
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Can't find ");
            sb.append(s);
            XLEAssert.assertTrue(sb.toString(), false);
            return -1;
        }
    }
    
    protected static Class getDimenRClass() {
        return R$dimen.class;
    }
    
    public static int getDimenRValue(final String s) {
        try {
            return getDimenRClass().getDeclaredField(s).getInt(null);
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Can't find ");
            sb.append(s);
            XLEAssert.assertTrue(sb.toString(), false);
            return -1;
        }
    }
    
    protected static Class getDrawableRClass() {
        return R$drawable.class;
    }
    
    public static int getDrawableRValue(final String s) {
        try {
            return getDrawableRClass().getDeclaredField(s).getInt(null);
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Can't find ");
            sb.append(s);
            XLEAssert.assertTrue(sb.toString(), false);
            return -1;
        }
    }
    
    protected static Class getIdRClass() {
        return R$id.class;
    }
    
    public static int getIdRValue(final String s) {
        try {
            return getIdRClass().getDeclaredField(s).getInt(null);
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Can't find ");
            sb.append(s);
            XLEAssert.assertTrue(sb.toString(), false);
            return -1;
        }
    }
    
    protected static Class getLayoutRClass() {
        return R$layout.class;
    }
    
    public static int getLayoutRValue(final String s) {
        try {
            return getLayoutRClass().getDeclaredField(s).getInt(null);
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Can't find ");
            sb.append(s);
            XLEAssert.assertTrue(sb.toString(), false);
            return -1;
        }
    }
    
    protected static Class getStringRClass() {
        return R$string.class;
    }
    
    public static int getStringRValue(final String s) {
        try {
            return getStringRClass().getDeclaredField(s).getInt(null);
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Can't find ");
            sb.append(s);
            XLEAssert.assertTrue(sb.toString(), false);
            return -1;
        }
    }
    
    protected static Class getStyleRClass() {
        return R$style.class;
    }
    
    public static int getStyleRValue(final String s) {
        try {
            return getStyleRClass().getDeclaredField(s).getInt(null);
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Can't find ");
            sb.append(s);
            XLEAssert.assertTrue(sb.toString(), false);
            return -1;
        }
    }
    
    protected static Class getStyleableRClass() {
        return R$styleable.class;
    }
    
    public static int getStyleableRValue(final String s) {
        try {
            return getStyleableRClass().getDeclaredField(s).getInt(null);
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Can't find ");
            sb.append(s);
            XLEAssert.assertTrue(sb.toString(), false);
            return -1;
        }
    }
    
    public static int[] getStyleableRValueArray(final String s) {
        try {
            return (int[])getStyleableRClass().getDeclaredField(s).get(null);
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Can't find ");
            sb.append(s);
            XLEAssert.assertTrue(sb.toString(), false);
            return null;
        }
    }
}
