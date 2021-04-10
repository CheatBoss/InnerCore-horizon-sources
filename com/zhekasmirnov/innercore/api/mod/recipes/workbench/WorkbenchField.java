package com.zhekasmirnov.innercore.api.mod.recipes.workbench;

import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.mod.ui.container.*;

public interface WorkbenchField
{
    Scriptable asScriptableField();
    
    AbstractSlot getFieldSlot(final int p0);
}
