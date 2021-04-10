package com.zhekasmirnov.innercore.api.mod.ui.elements;

import java.util.*;
import com.zhekasmirnov.innercore.api.mod.ui.window.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.mod.*;
import com.zhekasmirnov.innercore.utils.*;
import java.lang.reflect.*;

public class ElementFactory
{
    private static HashMap<String, Class<? extends UIElement>> elementConstructorMap;
    
    static {
        ElementFactory.elementConstructorMap = new HashMap<String, Class<? extends UIElement>>();
        put("slot", UISlotElement.class);
        put("invSlot", UIInvSlotElement.class);
        put("invslot", UIInvSlotElement.class);
        put("image", UIImageElement.class);
        put("button", UIButtonElement.class);
        put("closeButton", UICloseButtonElement.class);
        put("close_button", UICloseButtonElement.class);
        put("text", UITextElement.class);
        put("fps", UIFPSTextElement.class);
        put("scale", UIScaleElement.class);
        put("frame", UIFrameElement.class);
        put("tab", UITabElement.class);
        put("switch", UISwitchElement.class);
        put("scroll", UIScrollElement.class);
        put("custom", UICustomElement.class);
    }
    
    public static UIElement construct(final UIWindow uiWindow, final ScriptableObject scriptableObject) {
        final String stringProperty = ScriptableObjectHelper.getStringProperty(scriptableObject, "type", ScriptableObjectHelper.getStringProperty(scriptableObject, "TYPE", null));
        if (stringProperty != null) {
            return construct(stringProperty, uiWindow, scriptableObject);
        }
        return null;
    }
    
    public static UIElement construct(final String s, final UIWindow uiWindow, final ScriptableObject scriptableObject) {
        if (!ElementFactory.elementConstructorMap.containsKey(s)) {
            return null;
        }
        final Constructor<?> constructor = ElementFactory.elementConstructorMap.get(s).getConstructors()[0];
        try {
            return (UIElement)constructor.newInstance(uiWindow, scriptableObject);
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("failed to create element instance: ");
            sb.append(s);
            UIUtils.log(sb.toString());
            ex.printStackTrace();
            return null;
        }
    }
    
    public static void put(final String s, final Class<? extends UIElement> clazz) {
        ElementFactory.elementConstructorMap.put(s, clazz);
    }
}
