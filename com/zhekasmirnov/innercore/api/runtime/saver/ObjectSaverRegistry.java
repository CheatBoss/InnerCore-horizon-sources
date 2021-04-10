package com.zhekasmirnov.innercore.api.runtime.saver;

import java.util.*;
import com.zhekasmirnov.innercore.api.mod.ui.container.*;
import com.zhekasmirnov.innercore.api.*;
import com.zhekasmirnov.innercore.api.runtime.saver.world.*;
import org.mozilla.javascript.*;

public class ObjectSaverRegistry
{
    public static final String PROPERTY_IGNORE_SAVE = "_json_ignore";
    public static final String PROPERTY_SAVER_ID = "_json_saver_id";
    private static HashMap<Integer, ObjectSaver> saverByObjectHash;
    private static HashMap<Integer, ObjectSaver> saverMap;
    private static HashMap<Integer, String> saverNameById;
    private static final ScriptableObject scope;
    
    static {
        ObjectSaverRegistry.saverMap = new HashMap<Integer, ObjectSaver>();
        ObjectSaverRegistry.saverByObjectHash = new HashMap<Integer, ObjectSaver>();
        ObjectSaverRegistry.saverNameById = new HashMap<Integer, String>();
        scope = Context.enter().initStandardObjects();
        Container.initSaverId();
        NativeItemInstanceExtra.initSaverId();
    }
    
    public static ObjectSaver getSaverFor(Object value) {
        final ObjectSaver objectSaver = ObjectSaverRegistry.saverByObjectHash.get(value.hashCode());
        if (objectSaver != null) {
            return objectSaver;
        }
        if (value instanceof ScriptableObject) {
            value = ((ScriptableObject)value).get((Object)"_json_saver_id");
            if (value instanceof Number) {
                return ObjectSaverRegistry.saverMap.get(((Number)value).intValue());
            }
        }
        return null;
    }
    
    public static String getSaverName(final int n) {
        return ObjectSaverRegistry.saverNameById.get(n);
    }
    
    public static Scriptable readObject(final ScriptableObject scriptableObject) {
        final ObjectSaver saver = getSaverFor(scriptableObject);
        if (saver != null) {
            try {
                Object o2;
                final Object o = o2 = saver.read(scriptableObject);
                if (!(o instanceof Scriptable) && !((o2 = Context.javaToJS(o, (Scriptable)ObjectSaverRegistry.scope)) instanceof Scriptable)) {
                    return null;
                }
                return (Scriptable)o2;
            }
            catch (Throwable t) {
                final StringBuilder sb = new StringBuilder();
                sb.append("error in reading object of saver type ");
                sb.append(getSaverName(saver.getSaverId()));
                WorldDataSaver.logErrorStatic(sb.toString(), t);
                return null;
            }
        }
        return (Scriptable)scriptableObject;
    }
    
    public static void registerObject(Object unwrapIfNeeded, final int n) {
        if (!ObjectSaverRegistry.saverMap.containsKey(n)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("no saver found for id ");
            sb.append(n);
            sb.append(" use only registerObjectSaver return values");
            throw new IllegalArgumentException(sb.toString());
        }
        unwrapIfNeeded = unwrapIfNeeded(unwrapIfNeeded);
        ObjectSaverRegistry.saverByObjectHash.put(unwrapIfNeeded.hashCode(), ObjectSaverRegistry.saverMap.get(n));
    }
    
    public static int registerSaver(final String s, final ObjectSaver objectSaver) {
        int hashCode;
        for (hashCode = s.hashCode(); ObjectSaverRegistry.saverMap.containsKey(hashCode); ++hashCode) {}
        ObjectSaverRegistry.saverMap.put(hashCode, objectSaver);
        ObjectSaverRegistry.saverNameById.put(hashCode, s);
        objectSaver.setSaverId(hashCode);
        return hashCode;
    }
    
    public static ScriptableObject saveObject(final Object o) {
        final Object unwrapIfNeeded = unwrapIfNeeded(o);
        final ObjectSaver saver = getSaverFor(unwrapIfNeeded);
        ScriptableObject scriptableObject = null;
        if (saver != null) {
            try {
                final ScriptableObject save = saver.save(unwrapIfNeeded);
                if (save != null) {
                    save.put("_json_saver_id", (Scriptable)save, (Object)saver.getSaverId());
                }
                return save;
            }
            catch (Throwable t) {
                final StringBuilder sb = new StringBuilder();
                sb.append("error in saving object of saver type ");
                sb.append(getSaverName(saver.getSaverId()));
                WorldDataSaver.logErrorStatic(sb.toString(), t);
                return null;
            }
        }
        if (unwrapIfNeeded instanceof ScriptableObject) {
            scriptableObject = (ScriptableObject)unwrapIfNeeded;
        }
        return scriptableObject;
    }
    
    public static ScriptableObject saveObjectAndCheckSaveIgnoring(final Object o) {
        if (o instanceof ScriptableObject) {
            final Object value = ((ScriptableObject)o).get((Object)"_json_ignore");
            if (value instanceof Boolean && (boolean)value) {
                return null;
            }
        }
        return saveObject(o);
    }
    
    public static Object saveOrSkipObject(Object saver) {
        final Object unwrapIfNeeded = unwrapIfNeeded(saver);
        if (unwrapIfNeeded == null) {
            return null;
        }
        if (unwrapIfNeeded == Undefined.instance) {
            return null;
        }
        saver = getSaverFor(unwrapIfNeeded);
        if (saver != null) {
            try {
                final ScriptableObject save = ((ObjectSaver)saver).save(unwrapIfNeeded);
                if (save != null) {
                    save.put("_json_saver_id", (Scriptable)save, (Object)((ObjectSaver)saver).getSaverId());
                }
                return save;
            }
            catch (Throwable t) {
                final StringBuilder sb = new StringBuilder();
                sb.append("error in saving object of saver type ");
                sb.append(getSaverName(((ObjectSaver)saver).getSaverId()));
                WorldDataSaver.logErrorStatic(sb.toString(), t);
                return null;
            }
        }
        return unwrapIfNeeded;
    }
    
    public static void setObjectIgnored(final ScriptableObject scriptableObject, final boolean b) {
        scriptableObject.put("_json_ignore", (Scriptable)scriptableObject, (Object)b);
    }
    
    static Object unwrapIfNeeded(final Object o) {
        if (o instanceof Wrapper) {
            return ((Wrapper)o).unwrap();
        }
        return o;
    }
}
