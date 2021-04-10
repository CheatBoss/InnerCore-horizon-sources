package org.mozilla.javascript.tools.shell;

import java.lang.reflect.*;
import java.util.*;
import org.mozilla.javascript.*;

class FlexibleCompletor implements InvocationHandler
{
    private Method completeMethod;
    private Scriptable global;
    
    FlexibleCompletor(final Class<?> clazz, final Scriptable global) throws NoSuchMethodException {
        this.global = global;
        this.completeMethod = clazz.getMethod("complete", String.class, Integer.TYPE, List.class);
    }
    
    public int complete(final String s, int i, final List<String> list) {
        int j;
        for (j = i - 1; j >= 0; --j) {
            final char char1 = s.charAt(j);
            if (!Character.isJavaIdentifierPart(char1) && char1 != '.') {
                break;
            }
        }
        final String[] split = s.substring(j + 1, i).split("\\.", -1);
        Scriptable global = this.global;
        final int n = 0;
        Object value;
        for (i = 0; i < split.length - 1; ++i) {
            value = global.get(split[i], this.global);
            if (!(value instanceof Scriptable)) {
                return s.length();
            }
            global = (Scriptable)value;
        }
        Object[] array;
        if (global instanceof ScriptableObject) {
            array = ((ScriptableObject)global).getAllIds();
        }
        else {
            array = global.getIds();
        }
        final String s2 = split[split.length - 1];
        String s3;
        String string;
        StringBuilder sb;
        for (i = n; i < array.length; ++i) {
            if (array[i] instanceof String) {
                s3 = (String)array[i];
                if (s3.startsWith(s2)) {
                    string = s3;
                    if (global.get(s3, global) instanceof Function) {
                        sb = new StringBuilder();
                        sb.append(s3);
                        sb.append("(");
                        string = sb.toString();
                    }
                    list.add(string);
                }
            }
        }
        return s.length() - s2.length();
    }
    
    @Override
    public Object invoke(final Object o, final Method method, final Object[] array) {
        if (method.equals(this.completeMethod)) {
            return this.complete((String)array[0], (int)array[1], (List<String>)array[2]);
        }
        throw new NoSuchMethodError(method.toString());
    }
}
