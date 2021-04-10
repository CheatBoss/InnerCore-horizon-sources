package com.appboy.ui.inappmessage;

import com.appboy.models.*;
import android.view.*;
import android.app.*;

public interface IInAppMessageViewWrapper
{
    void close();
    
    IInAppMessage getInAppMessage();
    
    View getInAppMessageView();
    
    boolean getIsAnimatingClose();
    
    void open(final Activity p0);
}
