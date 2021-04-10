package org.mozilla.javascript;

import java.lang.reflect.*;

class FieldAndMethods extends NativeJavaMethod
{
    static final long serialVersionUID = -9222428244284796755L;
    Field field;
    Object javaObject;
    
    FieldAndMethods(final Scriptable parentScope, final MemberBox[] array, final Field field) {
        super(array);
        this.field = field;
        this.setParentScope(parentScope);
        this.setPrototype(ScriptableObject.getFunctionPrototype(parentScope));
    }
    
    @Override
    public Object getDefaultValue(final Class<?> clazz) {
        if (clazz == ScriptRuntime.FunctionClass) {
            return this;
        }
        try {
            final Object value = this.field.get(this.javaObject);
            final Class<?> type = this.field.getType();
            final Context context = Context.getContext();
            Object o2;
            final Object o = o2 = context.getWrapFactory().wrap(context, this, value, type);
            if (o instanceof Scriptable) {
                o2 = ((Scriptable)o).getDefaultValue(clazz);
            }
            return o2;
        }
        catch (IllegalAccessException ex) {
            throw Context.reportRuntimeError1("msg.java.internal.private", this.field.getName());
        }
    }
}
