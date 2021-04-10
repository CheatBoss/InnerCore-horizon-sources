package com.zhekasmirnov.innercore.api.mod.ui.background;

import com.zhekasmirnov.innercore.api.mod.*;
import com.zhekasmirnov.innercore.mod.executable.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.utils.*;
import android.graphics.*;
import com.zhekasmirnov.innercore.api.mod.ui.types.*;

public class DrawCustom implements IDrawing
{
    private ScriptableObject description;
    
    protected Object callDescriptionMethodSafe(final String s, final Object... array) {
        try {
            final Object property = ScriptableObjectHelper.getProperty(this.description, s, null);
            if (property != null && property instanceof Function) {
                return ((Function)property).call(Compiler.assureContextForCurrentThread(), this.description.getParentScope(), (Scriptable)this.description, array);
            }
            return null;
        }
        catch (Exception ex) {
            UIUtils.processError(ex);
            return null;
        }
    }
    
    @Override
    public void onDraw(final Canvas canvas, final float n) {
        this.callDescriptionMethodSafe("onDraw", canvas, n);
    }
    
    @Override
    public void onSetup(final ScriptableObject description, final UIStyle uiStyle) {
        this.description = description;
    }
}
