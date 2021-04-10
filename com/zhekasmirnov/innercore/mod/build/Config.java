package com.zhekasmirnov.innercore.mod.build;

import com.zhekasmirnov.innercore.utils.*;
import java.io.*;
import org.json.*;
import com.zhekasmirnov.innercore.api.mod.*;
import org.mozilla.javascript.*;
import java.util.*;

public class Config
{
    private JSONObject data;
    private File file;
    private Config parent;
    
    private Config(final Config parent, final JSONObject data) {
        this.parent = parent;
        this.data = data;
    }
    
    public Config(final File file) {
        this.file = file;
        try {
            this.data = FileTools.readJSON(file.getAbsolutePath());
        }
        catch (IOException | JSONException ex) {
            final Object o;
            final Throwable t = (Throwable)o;
            this.data = new JSONObject();
            t.printStackTrace();
        }
    }
    
    public Config(final String s) {
        this(new File(s));
    }
    
    private JSONObject checkAndRestoreRecursive(final JSONObject jsonObject, final JSONObject jsonObject2) throws JSONException {
        JSONObject jsonObject3 = jsonObject2;
        if (jsonObject2 == null) {
            jsonObject3 = new JSONObject();
        }
        final JSONArray names = jsonObject.names();
        for (int length = names.length(), i = 0; i < length; ++i) {
            final String optString = names.optString(i);
            if (optString != null) {
                final Object opt = jsonObject.opt(optString);
                final Object opt2 = jsonObject3.opt(optString);
                if (opt != null) {
                    if (opt instanceof JSONObject) {
                        jsonObject3.put(optString, (Object)this.checkAndRestoreRecursive((JSONObject)opt, jsonObject3.optJSONObject(optString)));
                    }
                    else if (opt2 != null) {
                        if (opt2.getClass() != ((JSONObject)opt).getClass()) {
                            jsonObject3.put(optString, opt);
                        }
                    }
                    else {
                        jsonObject3.put(optString, opt);
                    }
                }
            }
        }
        return jsonObject3;
    }
    
    public Object access(final String s) {
        return this.get(s);
    }
    
    public void checkAndRestore(final String s) throws JSONException {
        this.checkAndRestore(new JSONObject(s));
    }
    
    public void checkAndRestore(final JSONObject jsonObject) {
        try {
            this.data = this.checkAndRestoreRecursive(jsonObject, this.data);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        this.save();
    }
    
    public void checkAndRestore(final ScriptableObject scriptableObject) {
        try {
            this.checkAndRestore(NativeJSON.stringify(Context.enter(), (Scriptable)ScriptableObjectHelper.getDefaultScope(), (Object)scriptableObject, (Object)null, (Object)null).toString());
        }
        catch (JSONException ex) {
            ex.printStackTrace();
        }
    }
    
    public Object get(String substring) {
        final int index = substring.indexOf(46);
        final String s = null;
        String substring2;
        if (index != -1) {
            substring2 = substring.substring(0, index);
            substring = substring.substring(index + 1);
        }
        else {
            substring2 = substring;
            substring = s;
        }
        final Object opt = this.data.opt(substring2);
        if (opt == null) {
            return null;
        }
        if (opt instanceof JSONObject) {
            final Config config = new Config(this, (JSONObject)opt);
            if (substring != null) {
                return config.get(substring);
            }
            return config;
        }
        else {
            if (substring == null) {
                return opt;
            }
            return null;
        }
    }
    
    public boolean getBool(final String s) {
        final Object value = this.get(s);
        return value instanceof Boolean && (boolean)value;
    }
    
    public double getDouble(final String s) {
        return this.getNumber(s).doubleValue();
    }
    
    public float getFloat(final String s) {
        return this.getNumber(s).floatValue();
    }
    
    public int getInteger(final String s) {
        return this.getNumber(s).intValue();
    }
    
    public ArrayList<String> getNames() {
        final ArrayList<String> list = new ArrayList<String>();
        final JSONArray names = this.data.names();
        if (names != null) {
            for (int i = 0; i < names.length(); ++i) {
                list.add(names.optString(i));
            }
        }
        return list;
    }
    
    public Number getNumber(final String s) {
        final Object value = this.get(s);
        if (value instanceof Number) {
            return (Number)value;
        }
        return 0;
    }
    
    public String getString(final String s) {
        final Object value = this.get(s);
        if (value instanceof CharSequence) {
            final StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(value);
            return sb.toString();
        }
        return null;
    }
    
    public ConfigValue getValue(final String s) {
        final ConfigValue configValue = null;
        final ConfigValue configValue2 = new ConfigValue(this, s);
        ConfigValue configValue3 = configValue;
        if (configValue2.isValid) {
            configValue3 = configValue2;
        }
        return configValue3;
    }
    
    public void save() {
        if (this.parent != null) {
            this.parent.save();
            return;
        }
        try {
            if (this.data == null) {
                this.data = new JSONObject();
            }
            FileTools.writeJSON(this.file.getAbsolutePath(), this.data);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public boolean set(String substring, final Object o) {
        final int index = substring.indexOf(46);
        final String s = null;
        String substring2;
        if (index != -1) {
            substring2 = substring.substring(0, index);
            substring = substring.substring(index + 1);
        }
        else {
            substring2 = substring;
            substring = s;
        }
        if (substring == null) {
            try {
                this.data.put(substring2, o);
                return true;
            }
            catch (JSONException ex) {
                ex.printStackTrace();
                return false;
            }
        }
        final Object opt = this.data.opt(substring2);
        return opt != null && opt instanceof JSONObject && new Config(this, (JSONObject)opt).set(substring, o);
    }
    
    public static class ConfigValue
    {
        private boolean isValid;
        private Config parent;
        private String path;
        
        private ConfigValue(final Config parent, final String path) {
            this.parent = parent;
            this.path = path;
            this.isValid = (parent.get(path) != null);
        }
        
        public Object get() {
            if (!this.isValid) {
                return null;
            }
            return this.parent.get(this.path);
        }
        
        public void set(final Object o) {
            if (!this.isValid) {
                return;
            }
            this.parent.set(this.path, o);
            this.parent.save();
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("[ConfigValue name=");
            sb.append(this.path);
            sb.append("]");
            return sb.toString();
        }
    }
}
