package com.microsoft.xbox.xle.app;

import java.util.*;
import android.view.*;
import android.view.inputmethod.*;
import com.microsoft.xboxtcui.*;
import com.microsoft.xbox.toolkit.*;
import android.widget.*;

public class XLEUtil
{
    public static <T> boolean isNullOrEmpty(final Iterable<T> iterable) {
        return iterable == null || !iterable.iterator().hasNext();
    }
    
    public static <T> boolean isNullOrEmpty(final T[] array) {
        return array == null || array.length == 0;
    }
    
    public static boolean shouldRefresh(final Date date, final long n) {
        return date == null || new Date().getTime() - date.getTime() > n;
    }
    
    public static void showKeyboard(final View view, final int n) {
        ThreadManager.UIThreadPostDelayed(new Runnable() {
            @Override
            public void run() {
                ((InputMethodManager)XboxTcuiSdk.getSystemService("input_method")).showSoftInput(view, 1);
            }
        }, n);
    }
    
    public static void showOkCancelDialog(final String s, final String s2, final String s3, final Runnable runnable, final String s4, final Runnable runnable2) {
        XLEAssert.assertNotNull("You must supply cancel text if this is not a must-act dialog.", s4);
        DialogManager.getInstance().showOkCancelDialog(s, s2, s3, runnable, s4, runnable2);
    }
    
    public static void updateAndShowTextViewUnlessEmpty(final TextView textView, final CharSequence text) {
        if (textView != null) {
            if (text != null && text.length() > 0) {
                textView.setText(text);
                textView.setVisibility(0);
                return;
            }
            textView.setVisibility(8);
        }
    }
    
    public static void updateTextAndVisibilityIfNotNull(final TextView textView, final CharSequence text, final int visibility) {
        if (textView != null) {
            textView.setText(text);
            textView.setVisibility(visibility);
        }
    }
    
    public static void updateVisibilityIfNotNull(final View view, final int visibility) {
        if (view != null) {
            view.setVisibility(visibility);
        }
    }
}
