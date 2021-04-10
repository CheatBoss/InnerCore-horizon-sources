package com.zhekasmirnov.innercore.api.mod.ui.types;

import com.zhekasmirnov.innercore.api.mod.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.mod.executable.*;
import com.zhekasmirnov.innercore.api.mod.ui.window.*;

public class WindowContentAdapter
{
    ScriptableObject content;
    NativeArray drawing;
    ScriptableObject elements;
    
    public WindowContentAdapter() {
        this(ScriptableObjectHelper.createEmpty());
    }
    
    public WindowContentAdapter(final ScriptableObject scriptableObject) {
        ScriptableObject empty = scriptableObject;
        if (scriptableObject == null) {
            empty = ScriptableObjectHelper.createEmpty();
        }
        this.content = empty;
        this.drawing = ScriptableObjectHelper.getNativeArrayProperty(this.content, "drawing", null);
        if (this.drawing == null) {
            this.drawing = ScriptableObjectHelper.createEmptyArray();
            this.content.put("drawing", (Scriptable)empty, (Object)this.drawing);
        }
        this.elements = ScriptableObjectHelper.getScriptableObjectProperty(this.content, "elements", null);
        if (this.elements == null) {
            this.elements = ScriptableObjectHelper.createEmpty();
            this.content.put("elements", (Scriptable)empty, (Object)this.elements);
        }
    }
    
    public void addDrawing(final ScriptableObject scriptableObject) {
        this.drawing.execIdCall(new IdFunctionObject((IdFunctionCall)null, (Object)"Array", 8, 0), Compiler.assureContextForCurrentThread(), (Scriptable)this.drawing, (Scriptable)this.drawing, new Object[] { scriptableObject });
    }
    
    public void addElement(final String s, final ScriptableObject scriptableObject) {
        this.elements.put(s, (Scriptable)this.elements, (Object)scriptableObject);
    }
    
    public ScriptableObject getContent() {
        return this.content;
    }
    
    public ScriptableObject getDrawing() {
        return (ScriptableObject)this.drawing;
    }
    
    public ScriptableObject getElements() {
        return this.elements;
    }
    
    public void insertDrawing(final ScriptableObject scriptableObject) {
        this.drawing.execIdCall(new IdFunctionObject((IdFunctionCall)null, (Object)"Array", 11, 0), Compiler.assureContextForCurrentThread(), (Scriptable)this.drawing, (Scriptable)this.drawing, new Object[] { scriptableObject });
    }
    
    public void setLocation(final UIWindowLocation uiWindowLocation) {
        this.content.put("location", (Scriptable)this.content, (Object)uiWindowLocation.asScriptable());
    }
}
