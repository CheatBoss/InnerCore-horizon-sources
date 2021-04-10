package com.zhekasmirnov.innercore.api.mod.ui.background;

import android.graphics.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.mod.ui.types.*;

public interface IDrawing
{
    void onDraw(final Canvas p0, final float p1);
    
    void onSetup(final ScriptableObject p0, final UIStyle p1);
}
