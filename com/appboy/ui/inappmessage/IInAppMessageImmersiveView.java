package com.appboy.ui.inappmessage;

import java.util.*;
import android.view.*;

public interface IInAppMessageImmersiveView extends IInAppMessageView
{
    List<View> getMessageButtonViews();
    
    View getMessageCloseButtonView();
}
