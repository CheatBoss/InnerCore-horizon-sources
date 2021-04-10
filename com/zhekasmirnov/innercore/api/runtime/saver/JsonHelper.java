package com.zhekasmirnov.innercore.api.runtime.saver;

import com.zhekasmirnov.innercore.api.mod.*;
import java.util.*;
import org.json.*;
import org.mozilla.javascript.*;
import java.text.*;

public class JsonHelper
{
    private static HashMap<Integer, ScriptableObject> scriptableByHashCode;
    
    static {
        JsonHelper.scriptableByHashCode = new HashMap<Integer, ScriptableObject>();
    }
    
    public static Scriptable jsonToScriptable(Object o) {
        final boolean b = o instanceof JSONArray;
        final int n = 0;
        int i = 0;
        Object array;
        if (b) {
            final JSONArray jsonArray = (JSONArray)o;
            final ArrayList<Object> list = new ArrayList<Object>();
            while (i < jsonArray.length()) {
                final Object opt = jsonArray.opt(i);
                Label_0070: {
                    if (!(opt instanceof JSONObject)) {
                        o = opt;
                        if (!(opt instanceof JSONArray)) {
                            break Label_0070;
                        }
                    }
                    o = jsonToScriptable(opt);
                }
                list.add(o);
                ++i;
            }
            array = ScriptableObjectHelper.createArray(list);
        }
        else {
            if (!(o instanceof JSONObject)) {
                throw new IllegalArgumentException("FAILED ASSERTION: JsonHelper.jsonToScriptable can get only JSONObject or JSONArray");
            }
            final ScriptableObject empty = ScriptableObjectHelper.createEmpty();
            final JSONObject jsonObject = (JSONObject)o;
            final JSONArray names = jsonObject.names();
            if (names != null) {
                for (int j = n; j < names.length(); ++j) {
                    final String optString = names.optString(j);
                    final Object opt2 = jsonObject.opt(optString);
                    Label_0178: {
                        if (!(opt2 instanceof JSONObject)) {
                            o = opt2;
                            if (!(opt2 instanceof JSONArray)) {
                                break Label_0178;
                            }
                        }
                        o = jsonToScriptable(opt2);
                    }
                    empty.put(optString, (Scriptable)empty, o);
                }
            }
            array = empty;
        }
        return ObjectSaverRegistry.readObject((ScriptableObject)array);
    }
    
    public static Scriptable parseJsonString(final String s) throws JSONException {
        return jsonToScriptable(new JSONObject(s));
    }
    
    public static String scriptableToJsonString(final ScriptableObject scriptableObject, final boolean b) {
        // monitorenter(JsonHelper.class)
        if (scriptableObject == null) {
            // monitorexit(JsonHelper.class)
            return "{}";
        }
        while (true) {
            JsonHelper.scriptableByHashCode.clear();
            final JsonToString jsonToString = new JsonToString(b);
            stringify(jsonToString, scriptableObject);
            return jsonToString.getResult();
            throw;
            continue;
        }
    }
    // monitorexit(JsonHelper.class)
    // monitorexit(JsonHelper.class)
    
    private static void stringify(final JsonToString jsonToString, final ScriptableObject scriptableObject) {
        JsonHelper.scriptableByHashCode.put(scriptableObject.hashCode(), scriptableObject);
        final boolean b = scriptableObject instanceof NativeArray;
        jsonToString.begin(b);
        final Object[] allIds = scriptableObject.getAllIds();
        for (int length = allIds.length, i = 0; i < length; ++i) {
            final Object o = allIds[i];
            if (!b || !(o instanceof CharSequence)) {
                final Object value = scriptableObject.get(o);
                if (value != null) {
                    final Object unwrapIfNeeded = ObjectSaverRegistry.unwrapIfNeeded(value);
                    if (unwrapIfNeeded instanceof ScriptableObject) {
                        if (!JsonHelper.scriptableByHashCode.containsKey(unwrapIfNeeded.hashCode())) {
                            final ScriptableObject saveObjectAndCheckSaveIgnoring = ObjectSaverRegistry.saveObjectAndCheckSaveIgnoring(unwrapIfNeeded);
                            if (saveObjectAndCheckSaveIgnoring != null) {
                                jsonToString.key(o);
                                stringify(jsonToString, saveObjectAndCheckSaveIgnoring);
                            }
                        }
                    }
                    else if (!(unwrapIfNeeded instanceof CharSequence) && !(unwrapIfNeeded instanceof Number) && !(unwrapIfNeeded instanceof Boolean)) {
                        final ScriptableObject saveObject = ObjectSaverRegistry.saveObject(unwrapIfNeeded);
                        if (saveObject != null) {
                            jsonToString.key(o);
                            stringify(jsonToString, saveObject);
                        }
                    }
                    else {
                        jsonToString.key(o);
                        jsonToString.value(unwrapIfNeeded);
                    }
                }
            }
        }
        jsonToString.end();
    }
    
    private static class JsonToString
    {
        private static DecimalFormat format;
        private boolean beautify;
        private int depth;
        private ArrayList<Boolean> isArrayStack;
        private StringBuilder result;
        
        static {
            JsonToString.format = new DecimalFormat("#");
        }
        
        public JsonToString(final boolean beautify) {
            this.beautify = false;
            this.depth = 0;
            this.isArrayStack = new ArrayList<Boolean>();
            this.result = new StringBuilder();
            this.beautify = beautify;
            this.depth = 0;
        }
        
        private void begin(final boolean b) {
            final StringBuilder result = this.result;
            String s;
            if (b) {
                s = "[";
            }
            else {
                s = "{";
            }
            result.append(s);
            String s2;
            if (this.beautify) {
                s2 = "\n";
            }
            else {
                s2 = "";
            }
            result.append(s2);
            this.push(b);
        }
        
        private void end() {
            final boolean array = this.isArray();
            this.pop();
            final StringBuilder result = this.result;
            String string;
            if (this.beautify) {
                final StringBuilder sb = new StringBuilder();
                sb.append("\n");
                sb.append(this.getIntend());
                string = sb.toString();
            }
            else {
                string = "";
            }
            result.append(string);
            String s;
            if (array) {
                s = "]";
            }
            else {
                s = "}";
            }
            result.append(s);
        }
        
        private String getIntend() {
            String string = "";
            if (!this.beautify) {
                return "";
            }
            for (int i = 0; i < this.depth; ++i) {
                final StringBuilder sb = new StringBuilder();
                sb.append(string);
                sb.append("  ");
                string = sb.toString();
            }
            return string;
        }
        
        private boolean isArray() {
            return this.isArrayStack.size() > 0 && this.isArrayStack.get(0);
        }
        
        private void pop() {
            if (this.isArrayStack.size() != 0 && this.depth >= 1) {
                --this.depth;
                this.isArrayStack.remove(0);
                return;
            }
            throw new IllegalArgumentException("excess object or array end");
        }
        
        private void push(final boolean b) {
            this.isArrayStack.add(0, b);
            ++this.depth;
        }
        
        private void putCommaIfNeeded() {
            int i;
            for (i = this.result.length() - 1; i != -1; --i) {
                final char char1 = this.result.charAt(i);
                if (char1 != ' ' && char1 != '\n') {
                    break;
                }
            }
            if (i == -1) {
                return;
            }
            final char char2 = this.result.charAt(i);
            if (char2 != ',' && char2 != '{' && char2 != '[') {
                this.result.append(",");
            }
        }
        
        public String getResult() {
            return this.result.toString();
        }
        
        public void key(final Object o) {
            this.putCommaIfNeeded();
            if (this.beautify && this.result.charAt(this.result.length() - 1) != '\n') {
                this.result.append("\n");
            }
            if (this.beautify) {
                this.result.append(this.getIntend());
            }
            if (!this.isArray()) {
                final StringBuilder result = this.result;
                result.append("\"");
                result.append(o);
                result.append("\":");
            }
        }
        
        public void value(final Object o) {
            if (o == null) {
                throw new IllegalArgumentException("value cannot be null");
            }
            String s;
            if (o instanceof CharSequence) {
                final String replace = o.toString().replace("\n", "\\n").replace("\"", "\\\"");
                final StringBuilder sb = new StringBuilder();
                sb.append("\"");
                sb.append(replace);
                sb.append("\"");
                s = sb.toString();
            }
            else if (o instanceof Number && ((Number)o).doubleValue() == ((Number)o).longValue()) {
                s = JsonToString.format.format(o);
            }
            else {
                s = o.toString();
            }
            this.result.append(s);
        }
    }
}
