package com.appboy.ui.inappmessage;

import android.app.*;
import com.appboy.models.*;
import android.view.*;

public interface IInAppMessageViewFactory
{
    View createInAppMessageView(final Activity p0, final IInAppMessage p1);
}
