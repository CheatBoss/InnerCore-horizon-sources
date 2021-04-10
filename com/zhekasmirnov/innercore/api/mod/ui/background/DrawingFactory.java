package com.zhekasmirnov.innercore.api.mod.ui.background;

import java.util.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.mod.ui.types.*;
import com.zhekasmirnov.innercore.api.mod.*;
import java.lang.reflect.*;

public class DrawingFactory
{
    private static HashMap<String, Class<? extends IDrawing>> drawingConstructorMap;
    
    static {
        DrawingFactory.drawingConstructorMap = new HashMap<String, Class<? extends IDrawing>>();
        put("background", DrawColor.class);
        put("color", DrawColor.class);
        put("bitmap", DrawImage.class);
        put("frame", DrawFrame.class);
        put("text", DrawText.class);
        put("line", DrawLine.class);
        put("custom", DrawCustom.class);
    }
    
    public static IDrawing construct(final ScriptableObject scriptableObject, final UIStyle uiStyle) {
        final String stringProperty = ScriptableObjectHelper.getStringProperty(scriptableObject, "type", ScriptableObjectHelper.getStringProperty(scriptableObject, "TYPE", null));
        if (stringProperty != null) {
            if (!DrawingFactory.drawingConstructorMap.containsKey(stringProperty)) {
                return null;
            }
            final Constructor<?> constructor = DrawingFactory.drawingConstructorMap.get(stringProperty).getConstructors()[0];
            try {
                final IDrawing drawing = (IDrawing)constructor.newInstance(new Object[0]);
                drawing.onSetup(scriptableObject, uiStyle);
                return drawing;
            }
            catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
        return null;
    }
    
    public static void put(final String s, final Class<? extends IDrawing> clazz) {
        DrawingFactory.drawingConstructorMap.put(s, clazz);
    }
}
