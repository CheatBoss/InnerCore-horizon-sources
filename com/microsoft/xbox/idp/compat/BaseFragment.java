package com.microsoft.xbox.idp.compat;

import android.app.*;

public abstract class BaseFragment extends Fragment
{
    public static final String ARG_ALT_BUTTON_TEXT = "ARG_ALT_BUTTON_TEXT";
    public static final String ARG_LOG_IN_BUTTON_TEXT = "ARG_LOG_IN_BUTTON_TEXT";
    public static final String ARG_USER_PTR = "ARG_USER_PTR";
    
    protected CharSequence getActivityTitle() {
        final Activity activity = this.getActivity();
        if (activity == null) {
            return null;
        }
        return activity.getTitle();
    }
}
