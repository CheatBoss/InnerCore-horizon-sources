package com.zhekasmirnov.innercore.mod.executable.library;

import com.zhekasmirnov.innercore.api.mod.util.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.log.*;
import java.util.*;

public class LibraryAnnotation
{
    private static final String NAME_ID = "$_annotation";
    private final String name;
    private final Class[] parameterTypes;
    
    public LibraryAnnotation(final String s) {
        this(s, new Class[0]);
    }
    
    public LibraryAnnotation(final String name, final Class[] parameterTypes) {
        this.name = name;
        this.parameterTypes = parameterTypes;
    }
    
    private void checkParameters(final Object[] array) {
        if (array.length != this.parameterTypes.length) {
            this.reportInvalidParameters(array);
        }
        for (int i = 0; i < array.length; ++i) {
            if (!this.parameterTypes[i].isInstance(array[i])) {
                this.reportInvalidParameters(array);
            }
        }
    }
    
    public static ArrayList<AnnotationSet> getAllAnnotations(final Scriptable scriptable) {
        final ArrayList<AnnotationSet> list = new ArrayList<AnnotationSet>();
        final Object[] ids = scriptable.getIds();
        final ArrayList<AnnotationInstance> list2 = new ArrayList<AnnotationInstance>();
        for (int length = ids.length, i = 0; i < length; ++i) {
            final Object o = ids[i];
            if (o instanceof String) {
                final String s = (String)o;
                if (s.contains("$_annotation")) {
                    list2.add((AnnotationInstance)scriptable.get(s, scriptable));
                    continue;
                }
            }
            Object o2;
            if (o instanceof String) {
                o2 = scriptable.get((String)o, scriptable);
            }
            else {
                o2 = scriptable.get((int)o, scriptable);
            }
            list.add(new AnnotationSet(o2, list2));
            list2.clear();
        }
        return list;
    }
    
    private static String objectToTypeName(final Object o) {
        if (o == null) {
            return "null";
        }
        return o.getClass().getSimpleName();
    }
    
    private void reportInvalidParameters(final Object[] array) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.name);
        sb.append(" got invalid parameters: required (");
        final Class[] parameterTypes = this.parameterTypes;
        final int length = parameterTypes.length;
        final int n = 0;
        for (int i = 0; i < length; ++i) {
            sb.append(parameterTypes[i]);
            sb.append(", ");
        }
        sb.append(") got (");
        for (int length2 = array.length, j = n; j < length2; ++j) {
            sb.append(objectToTypeName(array[j]));
            sb.append(", ");
        }
        sb.append(")");
        throw new IllegalArgumentException(sb.toString());
    }
    
    public String getName() {
        return this.name;
    }
    
    public void injectMethod(final Scriptable scriptable) {
        scriptable.put(this.name, scriptable, (Object)new ScriptableFunctionImpl() {
            public Object call(final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
                LibraryAnnotation.this.checkParameters(array);
                final StringBuilder sb = new StringBuilder();
                sb.append("$_annotation");
                sb.append(LibraryAnnotation.this.name);
                String s;
                StringBuilder sb2;
                for (s = sb.toString(); scriptable.has(s, scriptable); s = sb2.toString()) {
                    sb2 = new StringBuilder();
                    sb2.append("$");
                    sb2.append(s);
                }
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("annotation injected ");
                sb3.append(s);
                ICLog.d("LIBRARY", sb3.toString());
                scriptable.put(s, scriptable, (Object)new AnnotationInstance(LibraryAnnotation.this, array));
                return null;
            }
        });
    }
    
    public static class AnnotationInstance
    {
        private final Object[] params;
        private final LibraryAnnotation parent;
        
        private AnnotationInstance(final LibraryAnnotation parent, final Object[] params) {
            this.parent = parent;
            this.params = params;
        }
        
        public <T> T getParameter(final int n, final Class<? extends T> clazz) {
            return (T)this.params[n];
        }
        
        public Object[] getParams() {
            return this.params;
        }
    }
    
    public static class AnnotationSet
    {
        private final HashSet<AnnotationInstance> instances;
        private final Object target;
        
        public AnnotationSet(final Object target) {
            this.instances = new HashSet<AnnotationInstance>();
            this.target = target;
        }
        
        public AnnotationSet(final Object o, final ArrayList<AnnotationInstance> list) {
            this(o);
            this.instances.addAll((Collection<?>)list);
        }
        
        public AnnotationInstance find(final String s) {
            final ArrayList<AnnotationInstance> all = this.findAll(s);
            if (all.size() > 0) {
                return all.get(0);
            }
            return null;
        }
        
        public ArrayList<AnnotationInstance> findAll(final String s) {
            final ArrayList<AnnotationInstance> list = new ArrayList<AnnotationInstance>();
            for (final AnnotationInstance annotationInstance : this.instances) {
                if (s.equals(annotationInstance.parent.getName())) {
                    list.add(annotationInstance);
                }
            }
            return list;
        }
        
        public Object getTarget() {
            return this.target;
        }
    }
}
