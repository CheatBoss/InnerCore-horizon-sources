package com.microsoft.xbox.toolkit.ui;

import android.widget.*;
import android.graphics.*;

public interface OnBitmapSetListener
{
    void onAfterImageSet(final ImageView p0, final Bitmap p1);
    
    void onBeforeImageSet(final ImageView p0, final Bitmap p1);
}
