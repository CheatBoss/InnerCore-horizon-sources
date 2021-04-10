package com.cedarsoftware.util.io;

import java.text.*;
import java.util.concurrent.*;
import java.math.*;
import java.sql.*;
import java.net.*;
import java.util.regex.*;
import java.util.*;
import java.util.function.*;
import java.lang.reflect.*;

public class MetaUtils
{
    private static final Byte[] byteCache;
    private static final Character[] charCache;
    private static final Map<Class, Map<String, Field>> classMetaCache;
    private static final ConcurrentMap<Class, Object[]> constructors;
    static final ThreadLocal<SimpleDateFormat> dateFormat;
    private static final Class[] emptyClassArray;
    private static final Pattern extraQuotes;
    static Exception loadClassException;
    private static final Map<String, Class> nameToClass;
    private static final Set<Class> prims;
    private static final Collection unmodifiableCollection;
    private static final Map unmodifiableMap;
    private static final Collection unmodifiableSet;
    private static final Map unmodifiableSortedMap;
    private static final Collection unmodifiableSortedSet;
    private static Unsafe unsafe;
    private static boolean useUnsafe;
    
    static {
        classMetaCache = new ConcurrentHashMap<Class, Map<String, Field>>();
        prims = new HashSet<Class>();
        nameToClass = new HashMap<String, Class>();
        byteCache = new Byte[256];
        charCache = new Character[128];
        extraQuotes = Pattern.compile("([\"]*)([^\"]*)([\"]*)");
        emptyClassArray = new Class[0];
        constructors = new ConcurrentHashMap<Class, Object[]>();
        unmodifiableCollection = Collections.unmodifiableCollection((Collection<?>)new ArrayList<Object>());
        unmodifiableSet = Collections.unmodifiableSet((Set<?>)new HashSet<Object>());
        unmodifiableSortedSet = Collections.unmodifiableSortedSet(new TreeSet<Object>());
        unmodifiableMap = Collections.unmodifiableMap((Map<?, ?>)new HashMap<Object, Object>());
        unmodifiableSortedMap = Collections.unmodifiableSortedMap((SortedMap<Object, ?>)new TreeMap<Object, Object>());
        dateFormat = new ThreadLocal<SimpleDateFormat>() {
            public SimpleDateFormat initialValue() {
                return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            }
        };
        MetaUtils.useUnsafe = false;
        MetaUtils.prims.add(Byte.class);
        MetaUtils.prims.add(Integer.class);
        MetaUtils.prims.add(Long.class);
        MetaUtils.prims.add(Double.class);
        MetaUtils.prims.add(Character.class);
        MetaUtils.prims.add(Float.class);
        MetaUtils.prims.add(Boolean.class);
        MetaUtils.prims.add(Short.class);
        MetaUtils.nameToClass.put("string", String.class);
        MetaUtils.nameToClass.put("boolean", Boolean.TYPE);
        MetaUtils.nameToClass.put("char", Character.TYPE);
        MetaUtils.nameToClass.put("byte", Byte.TYPE);
        MetaUtils.nameToClass.put("short", Short.TYPE);
        MetaUtils.nameToClass.put("int", Integer.TYPE);
        MetaUtils.nameToClass.put("long", Long.TYPE);
        MetaUtils.nameToClass.put("float", Float.TYPE);
        MetaUtils.nameToClass.put("double", Double.TYPE);
        MetaUtils.nameToClass.put("date", Date.class);
        MetaUtils.nameToClass.put("class", Class.class);
        for (int i = 0; i < MetaUtils.byteCache.length; ++i) {
            MetaUtils.byteCache[i] = (byte)(i - 128);
        }
        for (int j = 0; j < MetaUtils.charCache.length; ++j) {
            MetaUtils.charCache[j] = (char)j;
        }
    }
    
    private MetaUtils() {
    }
    
    static Class classForName(final String s, final ClassLoader classLoader) {
        return classForName(s, classLoader, false);
    }
    
    static Class classForName(final String s, final ClassLoader classLoader, final boolean b) {
        if (s != null) {
            if (!s.isEmpty()) {
                final Class clazz = MetaUtils.nameToClass.get(s);
                try {
                    MetaUtils.loadClassException = null;
                    if (clazz == null) {
                        return loadClass(s, classLoader);
                    }
                    return clazz;
                }
                catch (Exception loadClassException) {
                    MetaUtils.loadClassException = loadClassException;
                    if (b) {
                        final StringBuilder sb = new StringBuilder("Unable to create class: ");
                        sb.append(s);
                        throw new JsonIoException(sb.toString(), loadClassException);
                    }
                    return LinkedHashMap.class;
                }
            }
        }
        throw new JsonIoException("Class name cannot be null or empty.");
    }
    
    private static int compareConstructors(final Constructor constructor, final Constructor constructor2) {
        final Class[] parameterTypes = constructor.getParameterTypes();
        final Class[] parameterTypes2 = constructor2.getParameterTypes();
        if (parameterTypes.length != parameterTypes2.length) {
            return parameterTypes.length - parameterTypes2.length;
        }
        for (int length = parameterTypes.length, i = 0; i < length; ++i) {
            final int compareTo = parameterTypes[i].getName().compareTo(parameterTypes2[i].getName());
            if (compareTo != 0) {
                return compareTo;
            }
        }
        return 0;
    }
    
    static Object[] fillArgs(final Class[] array, final boolean b) {
        final Object[] array2 = new Object[array.length];
        for (int i = 0; i < array.length; ++i) {
            final Class clazz = array[i];
            if (isPrimitive(clazz)) {
                array2[i] = newPrimitiveWrapper(clazz, null);
            }
            else if (b) {
                array2[i] = null;
            }
            else if (clazz == String.class) {
                array2[i] = "";
            }
            else if (clazz == Date.class) {
                array2[i] = new Date();
            }
            else if (List.class.isAssignableFrom(clazz)) {
                array2[i] = new ArrayList();
            }
            else if (SortedSet.class.isAssignableFrom(clazz)) {
                array2[i] = new TreeSet();
            }
            else if (Set.class.isAssignableFrom(clazz)) {
                array2[i] = new LinkedHashSet();
            }
            else if (SortedMap.class.isAssignableFrom(clazz)) {
                array2[i] = new TreeMap();
            }
            else if (Map.class.isAssignableFrom(clazz)) {
                array2[i] = new LinkedHashMap();
            }
            else if (Collection.class.isAssignableFrom(clazz)) {
                array2[i] = new ArrayList();
            }
            else if (Calendar.class.isAssignableFrom(clazz)) {
                array2[i] = Calendar.getInstance();
            }
            else if (TimeZone.class.isAssignableFrom(clazz)) {
                array2[i] = TimeZone.getDefault();
            }
            else if (clazz == BigInteger.class) {
                array2[i] = BigInteger.TEN;
            }
            else if (clazz == BigDecimal.class) {
                array2[i] = BigDecimal.TEN;
            }
            else if (clazz == StringBuilder.class) {
                array2[i] = new StringBuilder();
            }
            else if (clazz == StringBuffer.class) {
                array2[i] = new StringBuffer();
            }
            else if (clazz == Locale.class) {
                array2[i] = Locale.FRANCE;
            }
            else if (clazz == Class.class) {
                array2[i] = String.class;
            }
            else if (clazz == Timestamp.class) {
                array2[i] = new Timestamp(System.currentTimeMillis());
            }
            else if (clazz == java.sql.Date.class) {
                array2[i] = new java.sql.Date(System.currentTimeMillis());
            }
            else if (clazz == URL.class) {
                try {
                    array2[i] = new URL("http://localhost");
                }
                catch (MalformedURLException ex) {
                    array2[i] = null;
                }
            }
            else if (clazz == Object.class) {
                array2[i] = new Object();
            }
            else {
                array2[i] = null;
            }
        }
        return array2;
    }
    
    public static Map<String, Field> getDeepDeclaredFields(final Class clazz) {
        final Map<String, Field> map = MetaUtils.classMetaCache.get(clazz);
        if (map != null) {
            return map;
        }
        final LinkedHashMap<String, Field> linkedHashMap = new LinkedHashMap<String, Field>();
        if (clazz == null) {
            MetaUtils.classMetaCache.put(clazz, linkedHashMap);
            return linkedHashMap;
        }
        Label_0054: {
            break Label_0054;
        Label_0248:
            while (true) {
                int i;
                int length;
                do {
                    Label_0070: {
                        break Label_0070;
                        try {
                            final Field[] declaredFields = clazz.getDeclaredFields();
                            length = declaredFields.length;
                            i = 0;
                            continue Label_0248;
                            Field field;
                            StringBuilder sb;
                            Label_0142_Outer:Block_5_Outer:
                            while (true) {
                                ++i;
                                continue Label_0248;
                            Block_7:
                                while (true) {
                                Block_9:
                                    while (true) {
                                        Block_8: {
                                            break Block_8;
                                            Label_0211: {
                                                linkedHashMap.put(field.getName(), field);
                                            }
                                            continue Label_0142_Outer;
                                            break Block_9;
                                            break Block_7;
                                        }
                                        try {
                                            field.setAccessible(true);
                                        }
                                        catch (Exception ex) {}
                                        continue Block_5_Outer;
                                    }
                                    sb = new StringBuilder(String.valueOf(clazz.getName()));
                                    sb.append('.');
                                    sb.append(field.getName());
                                    linkedHashMap.put(sb.toString(), field);
                                    continue Label_0142_Outer;
                                    field = declaredFields[i];
                                    continue;
                                }
                                continue Label_0142_Outer;
                            }
                        }
                        // iftrue(Label_0142:, field.isAccessible())
                        // iftrue(Label_0211:, !linkedHashMap.containsKey((Object)field.getName()))
                        // iftrue(Label_0121:, !"metaClass".equals((Object)field.getName()) || !"groovy.lang.MetaClass".equals((Object)field.getType().getName()))
                        // iftrue(Label_0226:, field.getModifiers() & 0x8 != 0x0)
                        catch (Throwable t) {}
                        catch (ThreadDeath threadDeath) {
                            throw threadDeath;
                        }
                    }
                    continue Label_0248;
                } while (i < length);
                break;
            }
        }
        goto Label_0235;
    }
    
    public static int getDistance(final Class clazz, Class superclass) {
        if (clazz.isInterface()) {
            return getDistanceToInterface(clazz, superclass);
        }
        int n = 0;
        while (superclass != clazz) {
            ++n;
            if ((superclass = superclass.getSuperclass()) == null) {
                return Integer.MAX_VALUE;
            }
        }
        return n;
    }
    
    static int getDistanceToInterface(final Class<?> clazz, final Class<?> clazz2) {
        final LinkedHashSet<Class<?>> set = new LinkedHashSet<Class<?>>();
        final Class[] interfaces = clazz2.getInterfaces();
        for (int length = interfaces.length, i = 0; i < length; ++i) {
            final Class clazz3 = interfaces[i];
            if (clazz.equals(clazz3)) {
                return 1;
            }
            if (clazz.isAssignableFrom(clazz3)) {
                set.add(clazz3);
            }
        }
        if (clazz2.getSuperclass() != null && clazz.isAssignableFrom(clazz2.getSuperclass())) {
            set.add(clazz2.getSuperclass());
        }
        int n = Integer.MAX_VALUE;
        final Iterator<Object> iterator = set.iterator();
        while (iterator.hasNext()) {
            final int distanceToInterface = getDistanceToInterface(clazz, iterator.next());
            if (distanceToInterface < n) {
                n = distanceToInterface + 1;
            }
        }
        return n;
    }
    
    public static Field getField(final Class clazz, final String s) {
        return getDeepDeclaredFields(clazz).get(s);
    }
    
    private static String getJsonStringToMaxLength(final Object o, final int n) {
        final HashMap<String, Boolean> hashMap = new HashMap<String, Boolean>();
        hashMap.put("TYPE", false);
        hashMap.put("SHORT_META_KEYS", true);
        String s2;
        final String s = s2 = JsonWriter.objectToJson(o, (Map<String, Object>)hashMap);
        if (s.length() > n) {
            final StringBuilder sb = new StringBuilder(String.valueOf(s.substring(0, n)));
            sb.append("...");
            s2 = sb.toString();
        }
        return s2;
    }
    
    public static String getLogMessage(final String s, final Object[] array) {
        return getLogMessage(s, array, 50);
    }
    
    public static String getLogMessage(final String s, final Object[] array, final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append('(');
        for (int length = array.length, i = 0; i < length; ++i) {
            sb.append(getJsonStringToMaxLength(array[i], n));
            sb.append("  ");
        }
        final StringBuilder sb2 = new StringBuilder(String.valueOf(sb.toString().trim()));
        sb2.append(')');
        return sb2.toString();
    }
    
    public static boolean isLogicalPrimitive(final Class clazz) {
        return clazz.isPrimitive() || MetaUtils.prims.contains(clazz) || String.class.isAssignableFrom(clazz) || Number.class.isAssignableFrom(clazz) || Date.class.isAssignableFrom(clazz) || clazz.isEnum() || clazz.equals(Class.class);
    }
    
    public static boolean isPrimitive(final Class clazz) {
        return clazz.isPrimitive() || MetaUtils.prims.contains(clazz);
    }
    
    private static Class loadClass(String substring, final ClassLoader classLoader) throws ClassNotFoundException {
        String substring2 = substring;
        boolean b = false;
        Class<?> clazz = null;
        while (true) {
            final boolean startsWith = substring2.startsWith("[");
            final int n = 1;
            if (!startsWith) {
                break;
            }
            final boolean b2 = true;
            String substring3 = substring2;
            if (substring2.endsWith(";")) {
                substring3 = substring2.substring(0, substring2.length() - 1);
            }
            if (substring3.equals("[B")) {
                clazz = byte[].class;
            }
            else if (substring3.equals("[S")) {
                clazz = short[].class;
            }
            else if (substring3.equals("[I")) {
                clazz = int[].class;
            }
            else if (substring3.equals("[J")) {
                clazz = long[].class;
            }
            else if (substring3.equals("[F")) {
                clazz = float[].class;
            }
            else if (substring3.equals("[D")) {
                clazz = double[].class;
            }
            else if (substring3.equals("[Z")) {
                clazz = boolean[].class;
            }
            else if (substring3.equals("[C")) {
                clazz = char[].class;
            }
            int n2 = n;
            if (substring3.startsWith("[L")) {
                n2 = 2;
            }
            substring2 = substring3.substring(n2);
            b = b2;
        }
        Class<?> loadClass = null;
        if (clazz == null) {
            loadClass = classLoader.loadClass(substring2);
        }
        if (b) {
            Class<?> clazz2;
            if (clazz != null) {
                clazz2 = clazz;
            }
            else {
                clazz2 = Array.newInstance(loadClass, 0).getClass();
            }
            while (substring.startsWith("[[")) {
                clazz2 = Array.newInstance(clazz2, 0).getClass();
                substring = substring.substring(1);
            }
            return clazz2;
        }
        return loadClass;
    }
    
    public static Object newInstance(final Class clazz) {
        if (MetaUtils.unmodifiableSortedMap.getClass().isAssignableFrom(clazz)) {
            return new TreeMap();
        }
        if (MetaUtils.unmodifiableMap.getClass().isAssignableFrom(clazz)) {
            return new LinkedHashMap();
        }
        if (MetaUtils.unmodifiableSortedSet.getClass().isAssignableFrom(clazz)) {
            return new TreeSet();
        }
        if (MetaUtils.unmodifiableSet.getClass().isAssignableFrom(clazz)) {
            return new LinkedHashSet();
        }
        if (MetaUtils.unmodifiableCollection.getClass().isAssignableFrom(clazz)) {
            return new ArrayList();
        }
        if (clazz.isInterface()) {
            final StringBuilder sb = new StringBuilder("Cannot instantiate unknown interface: ");
            sb.append(clazz.getName());
            throw new JsonIoException(sb.toString());
        }
        final Object[] array = MetaUtils.constructors.get(clazz);
        if (array != null) {
            final Constructor constructor = (Constructor)array[0];
            if (constructor == null && MetaUtils.useUnsafe) {
                try {
                    return MetaUtils.unsafe.allocateInstance(clazz);
                }
                catch (Exception ex) {
                    final StringBuilder sb2 = new StringBuilder("Could not instantiate ");
                    sb2.append(clazz.getName());
                    throw new JsonIoException(sb2.toString(), ex);
                }
            }
            if (constructor == null) {
                final StringBuilder sb3 = new StringBuilder("No constructor found to instantiate ");
                sb3.append(clazz.getName());
                throw new JsonIoException(sb3.toString());
            }
            final Boolean b = (Boolean)array[1];
            final Class[] parameterTypes = constructor.getParameterTypes();
            if (parameterTypes != null) {
                if (parameterTypes.length != 0) {
                    final Object[] fillArgs = fillArgs(parameterTypes, b);
                    try {
                        return constructor.newInstance(fillArgs);
                    }
                    catch (Exception ex2) {
                        final StringBuilder sb4 = new StringBuilder("Could not instantiate ");
                        sb4.append(clazz.getName());
                        throw new JsonIoException(sb4.toString(), ex2);
                    }
                }
            }
            try {
                return constructor.newInstance(new Object[0]);
            }
            catch (Exception ex3) {
                final StringBuilder sb5 = new StringBuilder("Could not instantiate ");
                sb5.append(clazz.getName());
                throw new JsonIoException(sb5.toString(), ex3);
            }
        }
        final Object[] instanceEx = newInstanceEx(clazz);
        MetaUtils.constructors.put(clazz, new Object[] { instanceEx[1], instanceEx[2] });
        return instanceEx[0];
    }
    
    static Object[] newInstanceEx(final Class clazz) {
        try {
            final Constructor<Object> constructor = clazz.getConstructor((Class<?>[])MetaUtils.emptyClassArray);
            if (constructor != null) {
                return new Object[] { constructor.newInstance(new Object[0]), constructor, true };
            }
            return tryOtherConstruction(clazz);
        }
        catch (Exception ex) {
            return tryOtherConstruction(clazz);
        }
    }
    
    static Object newPrimitiveWrapper(final Class clazz, final Object o) {
        Block_0: {
            break Block_0;
        Label_0765:
            while (true) {
                Class<Byte> type;
                int intValue;
                byte byteValue;
                short shortValue;
                String removeLeadingAndTrailingQuotes;
                String removeLeadingAndTrailingQuotes2;
                long longValue;
                String removeLeadingAndTrailingQuotes3;
                String removeLeadingAndTrailingQuotes4;
                float floatValue;
                String removeLeadingAndTrailingQuotes5;
                double doubleValue;
                String removeLeadingAndTrailingQuotes6;
                StringBuilder sb;
                String removeLeadingAndTrailingQuotes7;
                String removeLeadingAndTrailingQuotes8;
                String name;
                StringBuilder sb2;
                Label_0390_Outer:Label_0457_Outer:
                do {
                    while (true) {
                    Label_0759:
                        while (true) {
                        Label_0753:
                            while (true) {
                                Label_0691: {
                                    break Label_0691;
                                    try {
                                        if (clazz != Boolean.TYPE && clazz != Boolean.class) {
                                            type = Byte.TYPE;
                                            intValue = 0;
                                            byteValue = 0;
                                            shortValue = 0;
                                            if (clazz != type && clazz != Byte.class) {
                                                if (clazz != Character.TYPE && clazz != Character.class) {
                                                    if (clazz != Double.TYPE && clazz != Double.class) {
                                                        if (clazz != Float.TYPE && clazz != Float.class) {
                                                            if (clazz != Integer.TYPE && clazz != Integer.class) {
                                                                if (clazz != Long.TYPE && clazz != Long.class) {
                                                                    if (clazz == Short.TYPE || clazz == Short.class) {
                                                                        if (o instanceof String) {
                                                                            if ("".equals(removeLeadingAndTrailingQuotes = removeLeadingAndTrailingQuotes((String)o))) {
                                                                                removeLeadingAndTrailingQuotes = "0";
                                                                            }
                                                                            return Short.parseShort(removeLeadingAndTrailingQuotes);
                                                                        }
                                                                        if (o != null) {
                                                                            shortValue = ((Number)o).shortValue();
                                                                        }
                                                                        return shortValue;
                                                                    }
                                                                }
                                                                else {
                                                                    if (o instanceof String) {
                                                                        if ("".equals(removeLeadingAndTrailingQuotes2 = removeLeadingAndTrailingQuotes((String)o))) {
                                                                            removeLeadingAndTrailingQuotes2 = "0";
                                                                        }
                                                                        return Long.parseLong(removeLeadingAndTrailingQuotes2);
                                                                    }
                                                                    if (o != null) {
                                                                        longValue = ((Number)o).longValue();
                                                                        return longValue;
                                                                    }
                                                                    break Label_0691;
                                                                }
                                                            }
                                                            else {
                                                                if (o instanceof String) {
                                                                    if ("".equals(removeLeadingAndTrailingQuotes3 = removeLeadingAndTrailingQuotes((String)o))) {
                                                                        removeLeadingAndTrailingQuotes3 = "0";
                                                                    }
                                                                    return Integer.parseInt(removeLeadingAndTrailingQuotes3);
                                                                }
                                                                if (o != null) {
                                                                    intValue = ((Number)o).intValue();
                                                                }
                                                                return intValue;
                                                            }
                                                        }
                                                        else {
                                                            if (o instanceof String) {
                                                                if ("".equals(removeLeadingAndTrailingQuotes4 = removeLeadingAndTrailingQuotes((String)o))) {
                                                                    removeLeadingAndTrailingQuotes4 = "0.0f";
                                                                }
                                                                return Float.parseFloat(removeLeadingAndTrailingQuotes4);
                                                            }
                                                            if (o != null) {
                                                                floatValue = ((Number)o).floatValue();
                                                                return floatValue;
                                                            }
                                                            break Label_0753;
                                                        }
                                                    }
                                                    else {
                                                        if (o instanceof String) {
                                                            if ("".equals(removeLeadingAndTrailingQuotes5 = removeLeadingAndTrailingQuotes((String)o))) {
                                                                removeLeadingAndTrailingQuotes5 = "0.0";
                                                            }
                                                            return Double.parseDouble(removeLeadingAndTrailingQuotes5);
                                                        }
                                                        if (o != null) {
                                                            doubleValue = ((Number)o).doubleValue();
                                                            return doubleValue;
                                                        }
                                                        break Label_0759;
                                                    }
                                                }
                                                else {
                                                    if (o == null) {
                                                        return '\0';
                                                    }
                                                    if (o instanceof String) {
                                                        if ("".equals(removeLeadingAndTrailingQuotes6 = removeLeadingAndTrailingQuotes((String)o))) {
                                                            removeLeadingAndTrailingQuotes6 = "\u0000";
                                                        }
                                                        return removeLeadingAndTrailingQuotes6.charAt(0);
                                                    }
                                                    if (o instanceof Character) {
                                                        return o;
                                                    }
                                                }
                                                sb = new StringBuilder("Class '");
                                                sb.append(clazz.getName());
                                                sb.append("' does not have primitive wrapper.");
                                                throw new JsonIoException(sb.toString());
                                            }
                                            if (o instanceof String) {
                                                if ("".equals(removeLeadingAndTrailingQuotes7 = removeLeadingAndTrailingQuotes((String)o))) {
                                                    removeLeadingAndTrailingQuotes7 = "0";
                                                }
                                                return Byte.parseByte(removeLeadingAndTrailingQuotes7);
                                            }
                                            if (o != null) {
                                                byteValue = MetaUtils.byteCache[((Number)o).byteValue() + 128];
                                            }
                                            return byteValue;
                                        }
                                        else {
                                            if (o instanceof String) {
                                                if ("".equals(removeLeadingAndTrailingQuotes8 = removeLeadingAndTrailingQuotes((String)o))) {
                                                    removeLeadingAndTrailingQuotes8 = "false";
                                                }
                                                return Boolean.parseBoolean(removeLeadingAndTrailingQuotes8);
                                            }
                                            continue Label_0765;
                                        }
                                        return Boolean.FALSE;
                                    }
                                    catch (Exception ex) {
                                        if (clazz == null) {
                                            name = "null";
                                        }
                                        else {
                                            name = clazz.getName();
                                        }
                                        sb2 = new StringBuilder("Error creating primitive wrapper instance for Class: ");
                                        sb2.append(name);
                                        throw new JsonIoException(sb2.toString(), ex);
                                    }
                                }
                                longValue = 0L;
                                continue Label_0390_Outer;
                            }
                            floatValue = 0.0f;
                            continue Label_0457_Outer;
                        }
                        doubleValue = 0.0;
                        continue;
                    }
                } while (o == null);
                break;
            }
        }
        return o;
    }
    
    static String removeLeadingAndTrailingQuotes(String group) {
        final Matcher matcher = MetaUtils.extraQuotes.matcher(group);
        if (matcher.find()) {
            group = matcher.group(2);
        }
        return group;
    }
    
    public static void setUseUnsafe(final boolean useUnsafe) {
        MetaUtils.useUnsafe = useUnsafe;
        if (useUnsafe) {
            try {
                MetaUtils.unsafe = new Unsafe();
            }
            catch (InvocationTargetException ex) {
                MetaUtils.useUnsafe = false;
            }
        }
    }
    
    static Object[] tryOtherConstruction(final Class clazz) {
        final Constructor[] declaredConstructors = clazz.getDeclaredConstructors();
        if (declaredConstructors.length == 0) {
            final StringBuilder sb = new StringBuilder("Cannot instantiate '");
            sb.append(clazz.getName());
            sb.append("' - Primitive, interface, array[] or void");
            throw new JsonIoException(sb.toString());
        }
        final List<Constructor<Object>> list = Arrays.asList((Constructor<Object>[])declaredConstructors);
        Collections.sort((List<Object>)list, (Comparator<? super Object>)new Comparator<Constructor>() {
            @Override
            public int compare(final Constructor constructor, final Constructor constructor2) {
                final int modifiers = constructor.getModifiers();
                final int modifiers2 = constructor2.getModifiers();
                if (modifiers == modifiers2) {
                    return compareConstructors(constructor, constructor2);
                }
                final boolean public1 = Modifier.isPublic(modifiers);
                final boolean public2 = Modifier.isPublic(modifiers2);
                final boolean b = true;
                int n = 1;
                if (public1 != public2) {
                    if (Modifier.isPublic(modifiers)) {
                        n = -1;
                    }
                    return n;
                }
                if (Modifier.isProtected(modifiers) != Modifier.isProtected(modifiers2)) {
                    int n2 = b ? 1 : 0;
                    if (Modifier.isProtected(modifiers)) {
                        n2 = -1;
                    }
                    return n2;
                }
                if (Modifier.isPrivate(modifiers) == Modifier.isPrivate(modifiers2)) {
                    return 0;
                }
                if (Modifier.isPrivate(modifiers)) {
                    return 1;
                }
                return -1;
            }
            
            @Override
            public Comparator<Object> reversed() {
                return Comparator-CC.$default$reversed();
            }
            
            @Override
            public Comparator<Object> thenComparing(final Comparator<?> p0) {
                // 
                // This method could not be decompiled.
                // 
                // Could not show original bytecode, likely due to the same error.
                // 
                // The error that occurred was:
                // 
                // java.lang.NullPointerException
                // 
                throw new IllegalStateException("An error occurred while decompiling this method.");
            }
            
            @Override
            public <U extends Comparable<? super U>> Comparator<Object> thenComparing(final Function<?, ? extends U> p0) {
                // 
                // This method could not be decompiled.
                // 
                // Could not show original bytecode, likely due to the same error.
                // 
                // The error that occurred was:
                // 
                // java.lang.NullPointerException
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
                //     at com.strobel.assembler.metadata.MetadataHelper$5.visitWildcard(MetadataHelper.java:1793)
                //     at com.strobel.assembler.metadata.MetadataHelper$5.visitWildcard(MetadataHelper.java:1790)
                //     at com.strobel.assembler.metadata.WildcardType.accept(WildcardType.java:83)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
                //     at com.strobel.assembler.metadata.MetadataHelper.getLowerBound(MetadataHelper.java:1240)
                //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitWildcard(MetadataHelper.java:2278)
                //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitWildcard(MetadataHelper.java:2222)
                //     at com.strobel.assembler.metadata.WildcardType.accept(WildcardType.java:83)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.adaptRecursive(MetadataHelper.java:2256)
                //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.adaptRecursive(MetadataHelper.java:2233)
                //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitParameterizedType(MetadataHelper.java:2246)
                //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitParameterizedType(MetadataHelper.java:2222)
                //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                //     at com.strobel.assembler.metadata.MetadataHelper.adapt(MetadataHelper.java:1312)
                //     at com.strobel.assembler.metadata.MetadataHelper$11.visitClassType(MetadataHelper.java:2708)
                //     at com.strobel.assembler.metadata.MetadataHelper$11.visitClassType(MetadataHelper.java:2692)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visitParameterizedType(DefaultTypeVisitor.java:65)
                //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                //     at com.strobel.assembler.metadata.MetadataHelper.asSubType(MetadataHelper.java:720)
                //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:926)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2515)
                //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
                //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1656)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
                //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
                //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
                //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
                //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
                //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:109)
                //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1162)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1009)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
                //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
                //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
                //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
                //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
                //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
                //     at java.lang.Thread.run(Unknown Source)
                // 
                throw new IllegalStateException("An error occurred while decompiling this method.");
            }
            
            @Override
            public <U> Comparator<Object> thenComparing(final Function<?, ? extends U> p0, final Comparator<? super U> p1) {
                // 
                // This method could not be decompiled.
                // 
                // Could not show original bytecode, likely due to the same error.
                // 
                // The error that occurred was:
                // 
                // java.lang.NullPointerException
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
                //     at com.strobel.assembler.metadata.MetadataHelper$5.visitWildcard(MetadataHelper.java:1793)
                //     at com.strobel.assembler.metadata.MetadataHelper$5.visitWildcard(MetadataHelper.java:1790)
                //     at com.strobel.assembler.metadata.WildcardType.accept(WildcardType.java:83)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
                //     at com.strobel.assembler.metadata.MetadataHelper.getLowerBound(MetadataHelper.java:1240)
                //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitWildcard(MetadataHelper.java:2278)
                //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitWildcard(MetadataHelper.java:2222)
                //     at com.strobel.assembler.metadata.WildcardType.accept(WildcardType.java:83)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.adaptRecursive(MetadataHelper.java:2256)
                //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.adaptRecursive(MetadataHelper.java:2233)
                //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitParameterizedType(MetadataHelper.java:2246)
                //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitParameterizedType(MetadataHelper.java:2222)
                //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                //     at com.strobel.assembler.metadata.MetadataHelper.adapt(MetadataHelper.java:1312)
                //     at com.strobel.assembler.metadata.MetadataHelper$11.visitClassType(MetadataHelper.java:2708)
                //     at com.strobel.assembler.metadata.MetadataHelper$11.visitClassType(MetadataHelper.java:2692)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visitParameterizedType(DefaultTypeVisitor.java:65)
                //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
                //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
                //     at com.strobel.assembler.metadata.MetadataHelper.asSubType(MetadataHelper.java:720)
                //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:926)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2515)
                //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
                //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1656)
                //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
                //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
                //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
                //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
                //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
                //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:109)
                //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1162)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1009)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
                //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
                //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
                //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
                //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
                //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
                //     at java.lang.Thread.run(Unknown Source)
                // 
                throw new IllegalStateException("An error occurred while decompiling this method.");
            }
            
            @Override
            public Comparator<Object> thenComparingDouble(final ToDoubleFunction<?> p0) {
                // 
                // This method could not be decompiled.
                // 
                // Could not show original bytecode, likely due to the same error.
                // 
                // The error that occurred was:
                // 
                // java.lang.NullPointerException
                // 
                throw new IllegalStateException("An error occurred while decompiling this method.");
            }
            
            @Override
            public Comparator<Object> thenComparingInt(final ToIntFunction<?> p0) {
                // 
                // This method could not be decompiled.
                // 
                // Could not show original bytecode, likely due to the same error.
                // 
                // The error that occurred was:
                // 
                // java.lang.NullPointerException
                // 
                throw new IllegalStateException("An error occurred while decompiling this method.");
            }
            
            @Override
            public Comparator<Object> thenComparingLong(final ToLongFunction<?> p0) {
                // 
                // This method could not be decompiled.
                // 
                // Could not show original bytecode, likely due to the same error.
                // 
                // The error that occurred was:
                // 
                // java.lang.NullPointerException
                // 
                throw new IllegalStateException("An error occurred while decompiling this method.");
            }
        });
        final Iterator<Constructor<Object>> iterator = list.iterator();
        while (true) {
            Label_0216: {
                if (!iterator.hasNext()) {
                    for (final Constructor<Object> constructor : list) {
                        constructor.setAccessible(true);
                        final Object[] fillArgs = fillArgs(constructor.getParameterTypes(), false);
                        try {
                            return new Object[] { constructor.newInstance(fillArgs), constructor, false };
                        }
                        catch (Exception ex) {
                            continue;
                        }
                        break Label_0216;
                    }
                    break;
                }
            }
            final Constructor<Object> constructor2 = iterator.next();
            constructor2.setAccessible(true);
            final Object[] fillArgs2 = fillArgs(constructor2.getParameterTypes(), true);
            try {
                return new Object[] { constructor2.newInstance(fillArgs2), constructor2, true };
            }
            catch (Exception ex2) {}
        }
        if (MetaUtils.useUnsafe) {
            try {
                return new Object[] { MetaUtils.unsafe.allocateInstance(clazz), null, null };
            }
            catch (Exception ex3) {}
        }
        final StringBuilder sb2 = new StringBuilder("Could not instantiate ");
        sb2.append(clazz.getName());
        sb2.append(" using any constructor");
        throw new JsonIoException(sb2.toString());
    }
    
    static Character valueOf(char charValue) {
        if (charValue <= '\u007f') {
            charValue = MetaUtils.charCache[charValue];
        }
        return charValue;
    }
    
    static final class Unsafe
    {
        private final Method allocateInstance;
        private final Object sunUnsafe;
        
        public Unsafe() throws InvocationTargetException {
            try {
                final Constructor<Object> declaredConstructor = MetaUtils.classForName("sun.misc.Unsafe", MetaUtils.class.getClassLoader()).getDeclaredConstructor((Class<?>[])new Class[0]);
                declaredConstructor.setAccessible(true);
                this.sunUnsafe = declaredConstructor.newInstance(new Object[0]);
                (this.allocateInstance = this.sunUnsafe.getClass().getMethod("allocateInstance", Class.class)).setAccessible(true);
            }
            catch (Exception ex) {
                throw new InvocationTargetException(ex);
            }
        }
        
        public Object allocateInstance(final Class clazz) {
            try {
                return this.allocateInstance.invoke(this.sunUnsafe, clazz);
            }
            catch (InvocationTargetException ex) {
                String name;
                if (clazz == null) {
                    name = "null";
                }
                else {
                    name = clazz.getName();
                }
                final StringBuilder sb = new StringBuilder("Unable to create instance of class: ");
                sb.append(name);
                final String string = sb.toString();
                Throwable cause;
                if (ex.getCause() != null) {
                    cause = ex.getCause();
                }
                else {
                    cause = ex;
                }
                throw new JsonIoException(string, cause);
            }
            catch (IllegalArgumentException ex2) {
                String name2;
                if (clazz == null) {
                    name2 = "null";
                }
                else {
                    name2 = clazz.getName();
                }
                final StringBuilder sb2 = new StringBuilder("Unable to create instance of class: ");
                sb2.append(name2);
                throw new JsonIoException(sb2.toString(), ex2);
            }
            catch (IllegalAccessException ex3) {
                String name3;
                if (clazz == null) {
                    name3 = "null";
                }
                else {
                    name3 = clazz.getName();
                }
                final StringBuilder sb3 = new StringBuilder("Unable to create instance of class: ");
                sb3.append(name3);
                throw new JsonIoException(sb3.toString(), ex3);
            }
        }
    }
}
