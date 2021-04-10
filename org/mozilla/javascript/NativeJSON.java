package org.mozilla.javascript;

import org.mozilla.javascript.json.*;
import java.util.*;

public final class NativeJSON extends IdScriptableObject
{
    private static final int Id_parse = 2;
    private static final int Id_stringify = 3;
    private static final int Id_toSource = 1;
    private static final Object JSON_TAG;
    private static final int LAST_METHOD_ID = 3;
    private static final int MAX_ID = 3;
    private static final int MAX_STRINGIFY_GAP_LENGTH = 10;
    static final long serialVersionUID = -4567599697595654984L;
    
    static {
        JSON_TAG = "JSON";
    }
    
    private NativeJSON() {
    }
    
    static void init(final Scriptable parentScope, final boolean b) {
        final NativeJSON nativeJSON = new NativeJSON();
        nativeJSON.activatePrototypeMap(3);
        nativeJSON.setPrototype(ScriptableObject.getObjectPrototype(parentScope));
        nativeJSON.setParentScope(parentScope);
        if (b) {
            nativeJSON.sealObject();
        }
        ScriptableObject.defineProperty(parentScope, "JSON", nativeJSON, 2);
    }
    
    private static String ja(final NativeArray nativeArray, final StringifyState stringifyState) {
        if (stringifyState.stack.search(nativeArray) != -1) {
            throw ScriptRuntime.typeError0("msg.cyclic.value");
        }
        stringifyState.stack.push(nativeArray);
        final String indent = stringifyState.indent;
        final StringBuilder sb = new StringBuilder();
        sb.append(stringifyState.indent);
        sb.append(stringifyState.gap);
        stringifyState.indent = sb.toString();
        final LinkedList<String> list = new LinkedList<String>();
        for (long length = nativeArray.getLength(), n = 0L; n < length; ++n) {
            Object o;
            if (n > 2147483647L) {
                o = str(Long.toString(n), nativeArray, stringifyState);
            }
            else {
                o = str((int)n, nativeArray, stringifyState);
            }
            if (o == Undefined.instance) {
                list.add("null");
            }
            else {
                list.add((String)o);
            }
        }
        String s;
        if (list.isEmpty()) {
            s = "[]";
        }
        else if (stringifyState.gap.length() == 0) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append('[');
            sb2.append(join((Collection<Object>)list, ","));
            sb2.append(']');
            s = sb2.toString();
        }
        else {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(",\n");
            sb3.append(stringifyState.indent);
            final String join = join((Collection<Object>)list, sb3.toString());
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("[\n");
            sb4.append(stringifyState.indent);
            sb4.append(join);
            sb4.append('\n');
            sb4.append(indent);
            sb4.append(']');
            s = sb4.toString();
        }
        stringifyState.stack.pop();
        stringifyState.indent = indent;
        return s;
    }
    
    private static String jo(final Scriptable scriptable, final StringifyState stringifyState) {
        if (stringifyState.stack.search(scriptable) != -1) {
            throw ScriptRuntime.typeError0("msg.cyclic.value");
        }
        stringifyState.stack.push(scriptable);
        final String indent = stringifyState.indent;
        final StringBuilder sb = new StringBuilder();
        sb.append(stringifyState.indent);
        sb.append(stringifyState.gap);
        stringifyState.indent = sb.toString();
        Object[] array;
        if (stringifyState.propertyList != null) {
            array = stringifyState.propertyList.toArray();
        }
        else {
            array = scriptable.getIds();
        }
        final LinkedList<Object> list = new LinkedList<Object>();
        for (int length = array.length, i = 0; i < length; ++i) {
            final Object o = array[i];
            final Object str = str(o, scriptable, stringifyState);
            if (str != Undefined.instance) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(quote(o.toString()));
                sb2.append(":");
                String s = sb2.toString();
                if (stringifyState.gap.length() > 0) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append(s);
                    sb3.append(" ");
                    s = sb3.toString();
                }
                final StringBuilder sb4 = new StringBuilder();
                sb4.append(s);
                sb4.append(str);
                list.add(sb4.toString());
            }
        }
        String s2;
        if (list.isEmpty()) {
            s2 = "{}";
        }
        else if (stringifyState.gap.length() == 0) {
            final StringBuilder sb5 = new StringBuilder();
            sb5.append('{');
            sb5.append(join(list, ","));
            sb5.append('}');
            s2 = sb5.toString();
        }
        else {
            final StringBuilder sb6 = new StringBuilder();
            sb6.append(",\n");
            sb6.append(stringifyState.indent);
            final String join = join(list, sb6.toString());
            final StringBuilder sb7 = new StringBuilder();
            sb7.append("{\n");
            sb7.append(stringifyState.indent);
            sb7.append(join);
            sb7.append('\n');
            sb7.append(indent);
            sb7.append('}');
            s2 = sb7.toString();
        }
        stringifyState.stack.pop();
        stringifyState.indent = indent;
        return s2;
    }
    
    private static String join(final Collection<Object> collection, final String s) {
        if (collection == null || collection.isEmpty()) {
            return "";
        }
        final Iterator<Object> iterator = collection.iterator();
        if (!iterator.hasNext()) {
            return "";
        }
        final StringBuilder sb = new StringBuilder(iterator.next().toString());
        while (iterator.hasNext()) {
            sb.append(s);
            sb.append(iterator.next().toString());
        }
        return sb.toString();
    }
    
    private static Object parse(final Context context, final Scriptable scriptable, final String s) {
        try {
            return new JsonParser(context, scriptable).parseValue(s);
        }
        catch (JsonParser.ParseException ex) {
            throw ScriptRuntime.constructError("SyntaxError", ex.getMessage());
        }
    }
    
    public static Object parse(final Context context, final Scriptable scriptable, final String s, final Callable callable) {
        final Object parse = parse(context, scriptable, s);
        final Scriptable object = context.newObject(scriptable);
        object.put("", object, parse);
        return walk(context, scriptable, callable, object, "");
    }
    
    private static String quote(final String s) {
        final StringBuilder sb = new StringBuilder(s.length() + 2);
        sb.append('\"');
        for (int length = s.length(), i = 0; i < length; ++i) {
            final char char1 = s.charAt(i);
            if (char1 != '\"') {
                if (char1 != '\\') {
                    switch (char1) {
                        default: {
                            switch (char1) {
                                default: {
                                    if (char1 < ' ') {
                                        sb.append("\\u");
                                        sb.append(String.format("%04x", (int)char1));
                                        continue;
                                    }
                                    sb.append(char1);
                                    continue;
                                }
                                case 13: {
                                    sb.append("\\r");
                                    continue;
                                }
                                case 12: {
                                    sb.append("\\f");
                                    continue;
                                }
                            }
                            break;
                        }
                        case 10: {
                            sb.append("\\n");
                            break;
                        }
                        case 9: {
                            sb.append("\\t");
                            break;
                        }
                        case 8: {
                            sb.append("\\b");
                            break;
                        }
                    }
                }
                else {
                    sb.append("\\\\");
                }
            }
            else {
                sb.append("\\\"");
            }
        }
        sb.append('\"');
        return sb.toString();
    }
    
    private static String repeat(final char c, final int n) {
        final char[] array = new char[n];
        Arrays.fill(array, c);
        return new String(array);
    }
    
    private static Object str(Object o, final Scriptable scriptable, final StringifyState stringifyState) {
        Object o2;
        if (o instanceof String) {
            o2 = ScriptableObject.getProperty(scriptable, (String)o);
        }
        else {
            o2 = ScriptableObject.getProperty(scriptable, ((Number)o).intValue());
        }
        Object callMethod = o2;
        if (o2 instanceof Scriptable) {
            callMethod = o2;
            if (ScriptableObject.getProperty((Scriptable)o2, "toJSON") instanceof Callable) {
                callMethod = ScriptableObject.callMethod(stringifyState.cx, (Scriptable)o2, "toJSON", new Object[] { o });
            }
        }
        Object call = callMethod;
        if (stringifyState.replacer != null) {
            call = stringifyState.replacer.call(stringifyState.cx, stringifyState.scope, scriptable, new Object[] { o, callMethod });
        }
        if (call instanceof NativeNumber) {
            o = ScriptRuntime.toNumber(call);
        }
        else if (call instanceof NativeString) {
            o = ScriptRuntime.toString(call);
        }
        else {
            o = call;
            if (call instanceof NativeBoolean) {
                o = ((NativeBoolean)call).getDefaultValue(ScriptRuntime.BooleanClass);
            }
        }
        if (o == null) {
            return "null";
        }
        if (o.equals(Boolean.TRUE)) {
            return "true";
        }
        if (o.equals(Boolean.FALSE)) {
            return "false";
        }
        if (o instanceof CharSequence) {
            return quote(o.toString());
        }
        if (o instanceof Number) {
            final double doubleValue = ((Number)o).doubleValue();
            if (doubleValue == doubleValue && doubleValue != Double.POSITIVE_INFINITY && doubleValue != Double.NEGATIVE_INFINITY) {
                return ScriptRuntime.toString(o);
            }
            return "null";
        }
        else {
            if (!(o instanceof Scriptable) || o instanceof Callable) {
                return Undefined.instance;
            }
            if (o instanceof NativeArray) {
                return ja((NativeArray)o, stringifyState);
            }
            return jo((Scriptable)o, stringifyState);
        }
    }
    
    public static Object stringify(final Context context, final Scriptable parentScope, final Object o, Object o2, Object value) {
        final Object o3 = value;
        final String s = "";
        Object o4 = null;
        final Callable callable = null;
        Callable callable2;
        if (o2 instanceof Callable) {
            callable2 = (Callable)o2;
        }
        else {
            callable2 = callable;
            if (o2 instanceof NativeArray) {
                final LinkedList<String> list = new LinkedList<String>();
                final NativeArray nativeArray = (NativeArray)o2;
                final Integer[] indexIds = nativeArray.getIndexIds();
                final int length = indexIds.length;
                int n = 0;
                while (true) {
                    o4 = list;
                    callable2 = callable;
                    if (n >= length) {
                        break;
                    }
                    final Object value2 = nativeArray.get(indexIds[n], nativeArray);
                    if (!(value2 instanceof String) && !(value2 instanceof Number)) {
                        if (value2 instanceof NativeString || value2 instanceof NativeNumber) {
                            list.add(ScriptRuntime.toString(value2));
                        }
                    }
                    else {
                        list.add((String)value2);
                    }
                    ++n;
                }
            }
        }
        Object o5;
        if (o3 instanceof NativeNumber) {
            o5 = ScriptRuntime.toNumber(value);
        }
        else {
            o5 = o3;
            if (o3 instanceof NativeString) {
                o5 = ScriptRuntime.toString(value);
            }
        }
        String s2;
        if (o5 instanceof Number) {
            final int min = Math.min(10, (int)ScriptRuntime.toInteger(o5));
            if (min > 0) {
                s2 = repeat(' ', min);
            }
            else {
                s2 = "";
            }
            value = min;
        }
        else {
            value = o5;
            s2 = s;
            if (o5 instanceof String) {
                final String s3 = (String)o5;
                value = o5;
                s2 = s3;
                if (s3.length() > 10) {
                    s2 = s3.substring(0, 10);
                    value = o5;
                }
            }
        }
        final StringifyState stringifyState = new StringifyState(context, parentScope, "", s2, callable2, (List<Object>)o4, value);
        o2 = new NativeObject();
        ((ScriptableObject)o2).setParentScope(parentScope);
        ((ScriptableObject)o2).setPrototype(ScriptableObject.getObjectPrototype(parentScope));
        ((ScriptableObject)o2).defineProperty("", o, 0);
        return str("", (Scriptable)o2, stringifyState);
    }
    
    private static Object walk(final Context context, final Scriptable scriptable, final Callable callable, final Scriptable scriptable2, final Object o) {
        Object o2;
        if (o instanceof Number) {
            o2 = scriptable2.get(((Number)o).intValue(), scriptable2);
        }
        else {
            o2 = scriptable2.get((String)o, scriptable2);
        }
        if (o2 instanceof Scriptable) {
            final Scriptable scriptable3 = (Scriptable)o2;
            if (scriptable3 instanceof NativeArray) {
                for (long length = ((NativeArray)scriptable3).getLength(), n = 0L; n < length; ++n) {
                    if (n > 2147483647L) {
                        final String string = Long.toString(n);
                        final Object walk = walk(context, scriptable, callable, scriptable3, string);
                        if (walk == Undefined.instance) {
                            scriptable3.delete(string);
                        }
                        else {
                            scriptable3.put(string, scriptable3, walk);
                        }
                    }
                    else {
                        final int n2 = (int)n;
                        final Object walk2 = walk(context, scriptable, callable, scriptable3, n2);
                        if (walk2 == Undefined.instance) {
                            scriptable3.delete(n2);
                        }
                        else {
                            scriptable3.put(n2, scriptable3, walk2);
                        }
                    }
                }
            }
            else {
                final Object[] ids = scriptable3.getIds();
                for (int length2 = ids.length, i = 0; i < length2; ++i) {
                    final Object o3 = ids[i];
                    final Object walk3 = walk(context, scriptable, callable, scriptable3, o3);
                    if (walk3 == Undefined.instance) {
                        if (o3 instanceof Number) {
                            scriptable3.delete(((Number)o3).intValue());
                        }
                        else {
                            scriptable3.delete((String)o3);
                        }
                    }
                    else if (o3 instanceof Number) {
                        scriptable3.put(((Number)o3).intValue(), scriptable3, walk3);
                    }
                    else {
                        scriptable3.put((String)o3, scriptable3, walk3);
                    }
                }
            }
        }
        return callable.call(context, scriptable, scriptable2, new Object[] { o, o2 });
    }
    
    @Override
    public Object execIdCall(final IdFunctionObject idFunctionObject, final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        if (!idFunctionObject.hasTag(NativeJSON.JSON_TAG)) {
            return super.execIdCall(idFunctionObject, context, scriptable, scriptable2, array);
        }
        final int methodId = idFunctionObject.methodId();
        switch (methodId) {
            default: {
                throw new IllegalStateException(String.valueOf(methodId));
            }
            case 3: {
                Object o = null;
                Object o2 = null;
                Object o3 = null;
                Object o4 = null;
                Object o5 = null;
                Object o6 = null;
                switch (array.length) {
                    default: {
                        o6 = array[2];
                    }
                    case 2: {
                        o3 = array[1];
                        o4 = o6;
                    }
                    case 1: {
                        o = array[0];
                        o5 = o4;
                        o2 = o3;
                    }
                    case 0: {
                        return stringify(context, scriptable, o, o2, o5);
                    }
                }
                break;
            }
            case 2: {
                final String string = ScriptRuntime.toString(array, 0);
                Object o7 = null;
                if (array.length > 1) {
                    o7 = array[1];
                }
                if (o7 instanceof Callable) {
                    return parse(context, scriptable, string, (Callable)o7);
                }
                return parse(context, scriptable, string);
            }
            case 1: {
                return "JSON";
            }
        }
    }
    
    @Override
    protected int findPrototypeId(final String s) {
        int n = 0;
        String s2 = null;
        final int length = s.length();
        if (length != 5) {
            switch (length) {
                case 9: {
                    s2 = "stringify";
                    n = 3;
                    break;
                }
                case 8: {
                    s2 = "toSource";
                    n = 1;
                    break;
                }
            }
        }
        else {
            s2 = "parse";
            n = 2;
        }
        int n2 = n;
        if (s2 != null) {
            n2 = n;
            if (s2 != s) {
                n2 = n;
                if (!s2.equals(s)) {
                    n2 = 0;
                }
            }
        }
        return n2;
    }
    
    @Override
    public String getClassName() {
        return "JSON";
    }
    
    @Override
    protected void initPrototypeId(final int n) {
        if (n <= 3) {
            int n2 = 0;
            String s = null;
            switch (n) {
                default: {
                    throw new IllegalStateException(String.valueOf(n));
                }
                case 3: {
                    n2 = 3;
                    s = "stringify";
                    break;
                }
                case 2: {
                    n2 = 2;
                    s = "parse";
                    break;
                }
                case 1: {
                    n2 = 0;
                    s = "toSource";
                    break;
                }
            }
            this.initPrototypeMethod(NativeJSON.JSON_TAG, n, s, n2);
            return;
        }
        throw new IllegalStateException(String.valueOf(n));
    }
    
    private static class StringifyState
    {
        Context cx;
        String gap;
        String indent;
        List<Object> propertyList;
        Callable replacer;
        Scriptable scope;
        Object space;
        Stack<Scriptable> stack;
        
        StringifyState(final Context cx, final Scriptable scope, final String indent, final String gap, final Callable replacer, final List<Object> propertyList, final Object space) {
            this.stack = new Stack<Scriptable>();
            this.cx = cx;
            this.scope = scope;
            this.indent = indent;
            this.gap = gap;
            this.replacer = replacer;
            this.propertyList = propertyList;
            this.space = space;
        }
    }
}
