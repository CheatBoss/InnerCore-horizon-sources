package org.apache.james.mime4j.util;

import java.io.*;
import java.util.*;

public class StringArrayMap implements Serializable
{
    private static final long serialVersionUID = -5833051164281786907L;
    private final Map<String, Object> map;
    
    public StringArrayMap() {
        this.map = new HashMap<String, Object>();
    }
    
    public static Map<String, String[]> asMap(final Map<String, Object> map) {
        final HashMap<String, String[]> hashMap = new HashMap<String, String[]>(map.size());
        for (final Map.Entry<String, Object> entry : map.entrySet()) {
            hashMap.put((K)entry.getKey(), asStringArray(entry.getValue()));
        }
        return (Map<String, String[]>)Collections.unmodifiableMap((Map<?, ?>)hashMap);
    }
    
    public static String asString(final Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof String) {
            return (String)o;
        }
        if (o instanceof String[]) {
            return ((String[])o)[0];
        }
        if (o instanceof List) {
            return ((List)o).get(0);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Invalid parameter class: ");
        sb.append(o.getClass().getName());
        throw new IllegalStateException(sb.toString());
    }
    
    public static String[] asStringArray(final Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof String) {
            return new String[] { (String)o };
        }
        if (o instanceof String[]) {
            return (String[])o;
        }
        if (o instanceof List) {
            final List list = (List)o;
            return list.toArray(new String[list.size()]);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Invalid parameter class: ");
        sb.append(o.getClass().getName());
        throw new IllegalStateException(sb.toString());
    }
    
    public static Enumeration<String> asStringEnum(final Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof String) {
            return new Enumeration<String>() {
                private Object value = o;
                
                @Override
                public boolean hasMoreElements() {
                    return this.value != null;
                }
                
                @Override
                public String nextElement() {
                    final Object value = this.value;
                    if (value != null) {
                        final String s = (String)value;
                        this.value = null;
                        return s;
                    }
                    throw new NoSuchElementException();
                }
            };
        }
        if (o instanceof String[]) {
            return new Enumeration<String>() {
                private int offset;
                final /* synthetic */ String[] val$values = (String[])o;
                
                @Override
                public boolean hasMoreElements() {
                    return this.offset < this.val$values.length;
                }
                
                @Override
                public String nextElement() {
                    final int offset = this.offset;
                    final String[] val$values = this.val$values;
                    if (offset < val$values.length) {
                        this.offset = offset + 1;
                        return val$values[offset];
                    }
                    throw new NoSuchElementException();
                }
            };
        }
        if (o instanceof List) {
            return Collections.enumeration((Collection<String>)o);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Invalid parameter class: ");
        sb.append(o.getClass().getName());
        throw new IllegalStateException(sb.toString());
    }
    
    protected void addMapValue(final Map<String, Object> map, final String s, String s2) {
        final List<Object> value = map.get(s);
        Label_0154: {
            if (value != null) {
                Serializable s3;
                if (value instanceof String) {
                    final ArrayList<String> list = new ArrayList<String>();
                    list.add((String)value);
                    s3 = list;
                }
                else {
                    if (value instanceof List) {
                        value.add(s2);
                        s2 = (String)value;
                        break Label_0154;
                    }
                    if (!(value instanceof String[])) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Invalid object type: ");
                        sb.append(value.getClass().getName());
                        throw new IllegalStateException(sb.toString());
                    }
                    final ArrayList list2 = new ArrayList<String>();
                    final String[] array = (Object)value;
                    final int length = array.length;
                    int n = 0;
                    while (true) {
                        s3 = list2;
                        if (n >= length) {
                            break;
                        }
                        list2.add(array[n]);
                        ++n;
                    }
                }
                ((List<String>)s3).add(s2);
                s2 = (String)s3;
            }
        }
        map.put(s, s2);
    }
    
    public void addValue(final String s, final String s2) {
        this.addMapValue(this.map, this.convertName(s), s2);
    }
    
    protected String convertName(final String s) {
        return s.toLowerCase();
    }
    
    public Map<String, String[]> getMap() {
        return asMap(this.map);
    }
    
    public String[] getNameArray() {
        final Set<String> keySet = this.map.keySet();
        return keySet.toArray(new String[keySet.size()]);
    }
    
    public Enumeration<String> getNames() {
        return Collections.enumeration(this.map.keySet());
    }
    
    public String getValue(final String s) {
        return asString(this.map.get(this.convertName(s)));
    }
    
    public Enumeration<String> getValueEnum(final String s) {
        return asStringEnum(this.map.get(this.convertName(s)));
    }
    
    public String[] getValues(final String s) {
        return asStringArray(this.map.get(this.convertName(s)));
    }
}
