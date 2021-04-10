package org.mozilla.javascript.xml;

import org.mozilla.javascript.*;

public abstract class XMLObject extends IdScriptableObject
{
    static final long serialVersionUID = 8455156490438576500L;
    
    public XMLObject() {
    }
    
    public XMLObject(final Scriptable scriptable, final Scriptable scriptable2) {
        super(scriptable, scriptable2);
    }
    
    public Object addValues(final Context context, final boolean b, final Object o) {
        return Scriptable.NOT_FOUND;
    }
    
    public abstract boolean delete(final Context p0, final Object p1);
    
    public abstract NativeWith enterDotQuery(final Scriptable p0);
    
    public abstract NativeWith enterWith(final Scriptable p0);
    
    public abstract Object get(final Context p0, final Object p1);
    
    public abstract Scriptable getExtraMethodSource(final Context p0);
    
    public abstract Object getFunctionProperty(final Context p0, final int p1);
    
    public abstract Object getFunctionProperty(final Context p0, final String p1);
    
    @Override
    public String getTypeOf() {
        if (this.avoidObjectDetection()) {
            return "undefined";
        }
        return "xml";
    }
    
    public abstract boolean has(final Context p0, final Object p1);
    
    public abstract Ref memberRef(final Context p0, final Object p1, final int p2);
    
    public abstract Ref memberRef(final Context p0, final Object p1, final Object p2, final int p3);
    
    public abstract void put(final Context p0, final Object p1, final Object p2);
}
