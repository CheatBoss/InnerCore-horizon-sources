package com.microsoft.xbox.idp.ui;

import android.view.*;
import android.widget.*;
import android.text.*;
import android.text.style.*;
import android.text.method.*;
import com.microsoft.xboxtcui.*;
import android.os.*;
import com.microsoft.xbox.idp.compat.*;
import android.util.*;

public final class UiUtil
{
    private static final String TAG;
    
    static {
        TAG = UiUtil.class.getSimpleName();
    }
    
    public static boolean canScroll(final ScrollView scrollView) {
        final boolean b = false;
        final View child = scrollView.getChildAt(0);
        boolean b2 = b;
        if (child != null) {
            final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams = (ViewGroup$MarginLayoutParams)child.getLayoutParams();
            final int topMargin = viewGroup$MarginLayoutParams.topMargin;
            final int height = child.getHeight();
            final int bottomMargin = viewGroup$MarginLayoutParams.bottomMargin;
            b2 = b;
            if (scrollView.getHeight() < topMargin + height + bottomMargin) {
                b2 = true;
            }
        }
        return b2;
    }
    
    public static void ensureClickableSpanOnUnderlineSpan(final TextView textView, final int n, final ClickableSpan clickableSpan) {
        final SpannableStringBuilder text = new SpannableStringBuilder((CharSequence)Html.fromHtml(textView.getResources().getString(n)));
        final UnderlineSpan[] array = (UnderlineSpan[])text.getSpans(0, text.length(), (Class)UnderlineSpan.class);
        if (array != null && array.length > 0) {
            final UnderlineSpan underlineSpan = array[0];
            text.setSpan((Object)clickableSpan, text.getSpanStart((Object)underlineSpan), text.getSpanEnd((Object)underlineSpan), 33);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
        textView.setText((CharSequence)text);
    }
    
    public static boolean ensureErrorButtonsFragment(final BaseActivity baseActivity, final ErrorActivity.ErrorScreen errorScreen) {
        if (!baseActivity.hasFragment(R$id.xbid_error_buttons)) {
            final Bundle bundle = new Bundle();
            bundle.putInt("ARG_LEFT_ERROR_BUTTON_STRING_ID", errorScreen.leftButtonTextId);
            return ensureFragment(ErrorButtonsFragment.class, baseActivity, R$id.xbid_error_buttons, bundle);
        }
        return false;
    }
    
    public static boolean ensureErrorFragment(final BaseActivity baseActivity, final ErrorActivity.ErrorScreen errorScreen) {
        return !baseActivity.hasFragment(R$id.xbid_body_fragment) && ensureFragment(errorScreen.errorFragmentClass, baseActivity, R$id.xbid_body_fragment, baseActivity.getIntent().getExtras());
    }
    
    private static boolean ensureFragment(final Class<? extends BaseFragment> clazz, final BaseActivity baseActivity, final int n, final Bundle arguments) {
        if (!baseActivity.hasFragment(n)) {
            String s;
            String s2;
            try {
                final BaseFragment baseFragment = (BaseFragment)clazz.newInstance();
                baseFragment.setArguments(arguments);
                baseActivity.addFragment(n, baseFragment);
                return true;
            }
            catch (IllegalAccessException ex) {
                s = UiUtil.TAG;
                s2 = ex.getMessage();
            }
            catch (InstantiationException ex2) {
                s = UiUtil.TAG;
                s2 = ex2.getMessage();
            }
            Log.e(s, s2);
        }
        return false;
    }
    
    public static boolean ensureHeaderFragment(final BaseActivity baseActivity, final int n, final Bundle bundle) {
        return ensureFragment(HeaderFragment.class, baseActivity, n, bundle);
    }
}
