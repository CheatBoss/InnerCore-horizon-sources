package com.cedarsoftware.util.io;

import java.util.*;
import java.lang.reflect.*;

public class ObjectResolver extends Resolver
{
    private final ClassLoader classLoader;
    protected JsonReader.MissingFieldHandler missingFieldHandler;
    
    protected ObjectResolver(final JsonReader jsonReader, final ClassLoader classLoader) {
        super(jsonReader);
        this.classLoader = classLoader;
        this.missingFieldHandler = jsonReader.getMissingFieldHandler();
    }
    
    public static Class getRawType(final Type type) {
        if (type instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType)type;
            if (parameterizedType.getRawType() instanceof Class) {
                return (Class)parameterizedType.getRawType();
            }
        }
        return null;
    }
    
    private static void getTemplateTraverseWorkItem(final Deque<Object[]> deque, final Object[] array, final Type type) {
        if (array == null) {
            return;
        }
        if (array.length < 1) {
            return;
        }
        final Class rawType = getRawType(type);
        if (rawType != null && Collection.class.isAssignableFrom(rawType)) {
            deque.add(new Object[] { type, array });
            return;
        }
        for (int length = array.length, i = 0; i < length; ++i) {
            deque.add(new Object[] { type, array[i] });
        }
    }
    
    private static void markUntypedObjects(Type type, final Object o, final Map<String, Field> map) {
        final ArrayDeque arrayDeque = new ArrayDeque<Object[]>();
        int n = 2;
        int n2 = 0;
        int n3 = 1;
        arrayDeque.addFirst(new Object[] { type, o });
        while (!arrayDeque.isEmpty()) {
            final Object[] array = arrayDeque.removeFirst();
            type = (Type)array[n2];
            final Object o2 = array[n3];
            if (type instanceof ParameterizedType) {
                final Class rawType = getRawType(type);
                final Type[] actualTypeArguments = ((ParameterizedType)type).getActualTypeArguments();
                if (actualTypeArguments == null || actualTypeArguments.length < n3) {
                    continue;
                }
                if (rawType == null) {
                    continue;
                }
                stampTypeOnJsonObject(o2, type);
                if (Map.class.isAssignableFrom(rawType)) {
                    final JsonObject<Object, Object[]> jsonObject = (JsonObject<Object, Object[]>)o2;
                    if (!jsonObject.containsKey("@keys") && !jsonObject.containsKey("@items") && jsonObject instanceof JsonObject) {
                        Resolver.convertMapToKeysItems((JsonObject<String, Object>)jsonObject);
                    }
                    getTemplateTraverseWorkItem(arrayDeque, (Object[])jsonObject.get("@keys"), actualTypeArguments[n2]);
                    getTemplateTraverseWorkItem(arrayDeque, (Object[])jsonObject.get("@items"), actualTypeArguments[n3]);
                    continue;
                }
                if (Collection.class.isAssignableFrom(rawType)) {
                    if (o2 instanceof Object[]) {
                        final Object[] array2 = (Object[])o2;
                        for (int i = 0; i < array2.length; ++i, n = 2, n2 = 0, n3 = 1) {
                            final Object o3 = array2[i];
                            final Object[] array3 = new Object[n];
                            array3[n2] = type;
                            array3[n3] = o3;
                            arrayDeque.addFirst(array3);
                            if (o3 instanceof JsonObject) {
                                final Object[] array4 = new Object[n];
                                array4[n2] = type;
                                array4[n3] = o3;
                                arrayDeque.addFirst(array4);
                            }
                            else if (o3 instanceof Object[]) {
                                final JsonObject<String, Object[]> jsonObject2 = new JsonObject<String, Object[]>();
                                jsonObject2.type = rawType.getName();
                                final List<Object> list = Arrays.asList((Object[])o3);
                                jsonObject2.put("@items", list.toArray());
                                arrayDeque.addFirst(new Object[] { type, list });
                                array2[i] = jsonObject2;
                            }
                            else {
                                final Object[] array5 = new Object[n];
                                array5[0] = type;
                                array5[1] = o3;
                                arrayDeque.addFirst(array5);
                            }
                        }
                        continue;
                    }
                    if (o2 instanceof Collection) {
                        final Iterator<Object> iterator = ((Collection<Object>)o2).iterator();
                        while (iterator.hasNext()) {
                            arrayDeque.addFirst(new Object[] { actualTypeArguments[0], iterator.next() });
                        }
                    }
                    else if (o2 instanceof JsonObject) {
                        final Object[] array6 = ((JsonObject<Object, Object[]>)o2).getArray();
                        if (array6 != null) {
                            for (int length = array6.length, j = 0; j < length; ++j) {
                                arrayDeque.addFirst(new Object[] { actualTypeArguments[0], array6[j] });
                            }
                        }
                    }
                }
                else if (o2 instanceof JsonObject) {
                    for (final Map.Entry<String, Object[]> entry : ((JsonObject<Object, Object[]>)o2).entrySet()) {
                        final String s = entry.getKey();
                        if (!s.startsWith("this$")) {
                            final Field field = map.get(s);
                            if (field == null) {
                                continue;
                            }
                            if (field.getType().getTypeParameters().length <= 0 && !(field.getGenericType() instanceof TypeVariable)) {
                                continue;
                            }
                            arrayDeque.addFirst(new Object[] { actualTypeArguments[0], entry.getValue() });
                        }
                    }
                }
            }
            else {
                stampTypeOnJsonObject(o2, type);
            }
            n = 2;
            n2 = 0;
            n3 = 1;
        }
    }
    
    private static String safeToString(final Object o) {
        if (o == null) {
            return "null";
        }
        try {
            return o.toString();
        }
        catch (Exception ex) {
            return o.getClass().toString();
        }
    }
    
    private static void stampTypeOnJsonObject(final Object o, final Type type) {
        Class rawType;
        if (type instanceof Class) {
            rawType = (Class)type;
        }
        else {
            rawType = getRawType(type);
        }
        if (o instanceof JsonObject && rawType != null) {
            final JsonObject jsonObject = (JsonObject)o;
            if ((jsonObject.type == null || jsonObject.type.isEmpty()) && jsonObject.target == null) {
                jsonObject.type = rawType.getName();
            }
        }
    }
    
    private void storeMissingField(final Object o, final String s, final Object o2) {
        this.missingFields.add(new Missingfields(o, s, o2));
    }
    
    protected void assignField(final Deque<JsonObject<String, Object>> deque, final JsonObject jsonObject, final Field field, final Object o) {
        final Object target = jsonObject.target;
        try {
            final Class<?> type = field.getType();
            if (o == null) {
                if (type.isPrimitive()) {
                    field.set(target, MetaUtils.newPrimitiveWrapper(type, "0"));
                    return;
                }
                field.set(target, null);
            }
            else {
                if (o instanceof JsonObject) {
                    if (field.getGenericType() instanceof ParameterizedType) {
                        markUntypedObjects(field.getGenericType(), o, MetaUtils.getDeepDeclaredFields(type));
                    }
                    final JsonObject jsonObject2 = (JsonObject)o;
                    final String type2 = jsonObject2.type;
                    if (type2 == null || type2.isEmpty()) {
                        jsonObject2.setType(type.getName());
                    }
                }
                if (o == "~!o~") {
                    final JsonObject jsonObject3 = new JsonObject();
                    jsonObject3.type = type.getName();
                    field.set(target, this.createJavaObjectInstance(type, jsonObject3));
                    return;
                }
                final Object ifMatching = this.readIfMatching(o, type, deque);
                if (ifMatching != null) {
                    field.set(target, ifMatching);
                    return;
                }
                if (!o.getClass().isArray()) {
                    if (o instanceof JsonObject) {
                        final JsonObject jsonObject4 = (JsonObject)o;
                        final Long referenceId = jsonObject4.getReferenceId();
                        if (referenceId != null) {
                            final JsonObject referencedObj = this.getReferencedObj(referenceId);
                            if (referencedObj.target != null) {
                                field.set(target, referencedObj.target);
                                return;
                            }
                            this.unresolvedRefs.add(new UnresolvedReference(jsonObject, field.getName(), referenceId));
                        }
                        else {
                            field.set(target, this.createJavaObjectInstance(type, jsonObject4));
                            if (!MetaUtils.isLogicalPrimitive(jsonObject4.getTargetClass())) {
                                deque.addFirst((JsonObject<String, Object>)o);
                            }
                        }
                    }
                    else {
                        if (MetaUtils.isPrimitive(type)) {
                            field.set(target, MetaUtils.newPrimitiveWrapper(type, o));
                            return;
                        }
                        if (o instanceof String && "".equals(((String)o).trim()) && type != String.class) {
                            field.set(target, null);
                            return;
                        }
                        field.set(target, o);
                    }
                    return;
                }
                final Object[] array = (Object[])o;
                final JsonObject<String, Object> jsonObject5 = new JsonObject<String, Object>();
                if (char[].class != type) {
                    jsonObject5.put("@items", array);
                    this.createJavaObjectInstance(type, jsonObject5);
                    field.set(target, jsonObject5.target);
                    deque.addFirst(jsonObject5);
                    return;
                }
                if (array.length == 0) {
                    field.set(target, new char[0]);
                    return;
                }
                field.set(target, ((String)array[0]).toCharArray());
            }
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder(String.valueOf(ex.getClass().getSimpleName()));
            sb.append(" setting field '");
            sb.append(field.getName());
            sb.append("' on target: ");
            sb.append(safeToString(target));
            sb.append(" with value: ");
            sb.append(o);
            String s = sb.toString();
            if (MetaUtils.loadClassException != null) {
                final StringBuilder sb2 = new StringBuilder(String.valueOf(s));
                sb2.append(" Caused by: ");
                sb2.append(MetaUtils.loadClassException);
                sb2.append(" (which created a LinkedHashMap instead of the desired class)");
                s = sb2.toString();
            }
            throw new JsonIoException(s, ex);
        }
    }
    
    protected void handleMissingField(final Deque<JsonObject<String, Object>> deque, JsonObject target, final Object o, final String s) {
        target = ((JsonObject)target).target;
        Label_0018: {
            if (o != null) {
                break Label_0018;
            }
            while (true) {
                try {
                    this.storeMissingField(target, s, null);
                    return;
                    // iftrue(Label_0033:, o != "~!o~")
                    // iftrue(Label_0118:, referenceId == null)
                    // iftrue(Label_0156:, MetaUtils.isLogicalPrimitive((Class)jsonObject.getTargetClass()))
                    // iftrue(Label_0175:, !o instanceof JsonObject)
                    // iftrue(Label_0303:, MetaUtils.loadClassException == null)
                    // iftrue(Label_0076:, !o.getClass().isArray())
                Block_8_Outer:
                    while (true) {
                        while (true) {
                            while (true) {
                            Block_6_Outer:
                                while (true) {
                                    final StringBuilder sb = new StringBuilder(String.valueOf(target));
                                    sb.append(" Caused by: ");
                                    sb.append(MetaUtils.loadClassException);
                                    sb.append(" (which created a LinkedHashMap instead of the desired class)");
                                    String s2 = sb.toString();
                                    final Exception ex;
                                    throw new JsonIoException(s2, ex);
                                Block_9_Outer:
                                    while (true) {
                                        while (true) {
                                            Block_3: {
                                                break Block_3;
                                                this.storeMissingField(target, s, null);
                                                return;
                                                while (true) {
                                                    final Long referenceId;
                                                    this.storeMissingField(target, s, this.getReferencedObj(referenceId).target);
                                                    return;
                                                    Label_0303: {
                                                        throw new JsonIoException(s2, ex);
                                                    }
                                                    final JsonObject jsonObject = (JsonObject)o;
                                                    referenceId = jsonObject.getReferenceId();
                                                    continue Block_6_Outer;
                                                }
                                                deque.addFirst((JsonObject<String, Object>)o);
                                                Label_0156: {
                                                    final Object javaObjectInstance;
                                                    this.storeMissingField(target, s, javaObjectInstance);
                                                }
                                                return;
                                                final Object ifMatching;
                                                this.storeMissingField(target, s, ifMatching);
                                                return;
                                            }
                                            this.storeMissingField(target, s, null);
                                            return;
                                            Label_0175: {
                                                this.storeMissingField(target, s, o);
                                            }
                                            return;
                                            JsonObject jsonObject = null;
                                            final Object javaObjectInstance = this.createJavaObjectInstance(null, jsonObject);
                                            continue Block_8_Outer;
                                        }
                                        Label_0076: {
                                            continue Block_9_Outer;
                                        }
                                    }
                                    final StringBuilder sb2 = new StringBuilder(String.valueOf(ex.getClass().getSimpleName()));
                                    sb2.append(" missing field '");
                                    sb2.append(s);
                                    sb2.append("' on target: ");
                                    sb2.append(safeToString(target));
                                    sb2.append(" with value: ");
                                    sb2.append(o);
                                    target = (s2 = sb2.toString());
                                    continue Block_6_Outer;
                                }
                                Label_0057: {
                                    continue Block_8_Outer;
                                }
                            }
                            Label_0166: {
                                this.storeMissingField(target, s, null);
                            }
                            return;
                            Label_0118:
                            continue;
                        }
                        Label_0033: {
                            final Object ifMatching = this.readIfMatching(o, null, deque);
                        }
                        continue Block_8_Outer;
                    }
                }
                // iftrue(Label_0166:, jsonObject.getType() == null)
                // iftrue(Label_0057:, ifMatching == null)
                catch (Exception ex) {
                    continue;
                }
                break;
            }
        }
    }
    
    @Override
    protected Object readIfMatching(final Object o, Class class1, final Deque<JsonObject<String, Object>> deque) {
        if (o == null) {
            throw new JsonIoException("Bug in json-io, null must be checked before calling this method.");
        }
        if (class1 != null && this.notCustom(class1)) {
            return null;
        }
        final boolean b = o instanceof JsonObject;
        if (!b && class1 == null) {
            return null;
        }
        final boolean b2 = false;
        int n = 0;
        Label_0212: {
            if (b) {
                final JsonObject jsonObject = (JsonObject)o;
                if (jsonObject.isReference()) {
                    return null;
                }
                if (jsonObject.target == null) {
                Label_0150_Outer:
                    while (true) {
                        final String s = null;
                        final String s2 = null;
                        String s3 = s;
                        while (true) {
                            while (true) {
                                Label_0293: {
                                    try {
                                        final String type = jsonObject.type;
                                        if (type != null) {
                                            s3 = s;
                                            s3 = type;
                                            final Class classForName = MetaUtils.classForName(type, this.classLoader);
                                            s3 = s3;
                                            class1 = (Class<?>)classForName;
                                            this.createJavaObjectInstance(class1, jsonObject);
                                            break Label_0212;
                                        }
                                        break Label_0293;
                                        return null;
                                    }
                                    catch (Exception ex) {
                                        final StringBuilder sb = new StringBuilder("Class listed in @type [");
                                        sb.append(s3);
                                        sb.append("] is not found");
                                        throw new JsonIoException(sb.toString(), ex);
                                    }
                                    break;
                                }
                                if (class1 != null) {
                                    n = 1;
                                    s3 = s2;
                                    continue Label_0150_Outer;
                                }
                                break;
                            }
                            continue;
                        }
                    }
                }
                class1 = jsonObject.target.getClass();
                n = (b2 ? 1 : 0);
            }
            else {
                n = (b2 ? 1 : 0);
            }
        }
        if (this.notCustom(class1)) {
            return null;
        }
        final JsonReader.JsonClassReaderBase customReader = this.getCustomReader(class1);
        if (customReader == null) {
            return null;
        }
        if (n != 0) {
            ((JsonObject)o).setType(class1.getName());
        }
        if (customReader instanceof JsonReader.JsonClassReaderEx) {
            return ((JsonReader.JsonClassReaderEx)customReader).read(o, deque, this.getReader().getArgs());
        }
        return ((JsonReader.JsonClassReader)customReader).read(o, deque);
    }
    
    @Override
    protected void traverseArray(final Deque<JsonObject<String, Object>> deque, final JsonObject<String, Object> jsonObject) {
        final int length = jsonObject.getLength();
        if (length == 0) {
            return;
        }
        final Class componentType = jsonObject.getComponentType();
        if (Character.TYPE == componentType) {
            return;
        }
        if (Byte.TYPE == componentType) {
            jsonObject.moveBytesToMate();
            jsonObject.clearArray();
            return;
        }
        final boolean primitive = MetaUtils.isPrimitive(componentType);
        final Object target = jsonObject.target;
        final Object[] array = jsonObject.getArray();
        for (int i = 0; i < length; ++i) {
            final Object o = array[i];
            if (o == null) {
                Array.set(target, i, null);
            }
            else if (o == "~!o~") {
                Array.set(target, i, this.createJavaObjectInstance(componentType, new JsonObject()));
            }
            else {
                final Object ifMatching = this.readIfMatching(o, componentType, deque);
                if (ifMatching != null) {
                    Array.set(target, i, ifMatching);
                }
                else if (primitive) {
                    Array.set(target, i, MetaUtils.newPrimitiveWrapper(componentType, o));
                }
                else if (((JsonObject<String, Object>)o).getClass().isArray()) {
                    if (char[].class == componentType) {
                        final Object[] array2 = (Object[])o;
                        if (array2.length == 0) {
                            Array.set(target, i, new char[0]);
                        }
                        else {
                            final String s = (String)array2[0];
                            final int length2 = s.length();
                            final char[] array3 = new char[length2];
                            for (int j = 0; j < length2; ++j) {
                                array3[j] = s.charAt(j);
                            }
                            Array.set(target, i, array3);
                        }
                    }
                    else {
                        final JsonObject<String, Object> jsonObject2 = new JsonObject<String, Object>();
                        jsonObject2.put("@items", o);
                        Array.set(target, i, this.createJavaObjectInstance(componentType, jsonObject2));
                        deque.addFirst(jsonObject2);
                    }
                }
                else if (o instanceof JsonObject) {
                    final JsonObject<String, Object> jsonObject3 = (JsonObject<String, Object>)o;
                    final Long referenceId = jsonObject3.getReferenceId();
                    if (referenceId != null) {
                        final JsonObject referencedObj = this.getReferencedObj(referenceId);
                        if (referencedObj.target != null) {
                            Array.set(target, i, referencedObj.target);
                        }
                        else {
                            this.unresolvedRefs.add(new UnresolvedReference(jsonObject, i, referenceId));
                        }
                    }
                    else {
                        final Object javaObjectInstance = this.createJavaObjectInstance(componentType, jsonObject3);
                        Array.set(target, i, javaObjectInstance);
                        if (!MetaUtils.isLogicalPrimitive(javaObjectInstance.getClass())) {
                            deque.addFirst(jsonObject3);
                        }
                    }
                }
                else if (o instanceof String && "".equals(((String)o).trim()) && componentType != String.class && componentType != Object.class) {
                    Array.set(target, i, null);
                }
                else {
                    Array.set(target, i, o);
                }
            }
        }
        jsonObject.clearArray();
    }
    
    @Override
    protected void traverseCollection(final Deque<JsonObject<String, Object>> deque, final JsonObject<String, Object> jsonObject) {
        final Object[] array = jsonObject.getArray();
        if (array != null && array.length != 0) {
            final Collection collection = (Collection)jsonObject.target;
            final boolean b = collection instanceof List;
            int n = 0;
            for (int length = array.length, i = 0; i < length; ++i) {
                final Object o = array[i];
                if (o == null) {
                    collection.add(null);
                }
                else if (o == "~!o~") {
                    collection.add(new JsonObject());
                }
                else {
                    final Object ifMatching = this.readIfMatching(o, null, deque);
                    if (ifMatching != null) {
                        collection.add(ifMatching);
                    }
                    else if (!(o instanceof String) && !(o instanceof Boolean) && !(o instanceof Double) && !(o instanceof Long)) {
                        if (((JsonObject<String, Object>)o).getClass().isArray()) {
                            final JsonObject<String, Object> jsonObject2 = new JsonObject<String, Object>();
                            jsonObject2.put("@items", o);
                            this.createJavaObjectInstance(Object.class, jsonObject2);
                            collection.add(jsonObject2.target);
                            this.convertMapsToObjects(jsonObject2);
                        }
                        else {
                            final JsonObject<String, Object> jsonObject3 = (JsonObject<String, Object>)o;
                            final Long referenceId = jsonObject3.getReferenceId();
                            if (referenceId != null) {
                                final JsonObject referencedObj = this.getReferencedObj(referenceId);
                                if (referencedObj.target != null) {
                                    collection.add(referencedObj.target);
                                }
                                else {
                                    this.unresolvedRefs.add(new UnresolvedReference(jsonObject, n, referenceId));
                                    if (b) {
                                        collection.add(null);
                                    }
                                }
                            }
                            else {
                                this.createJavaObjectInstance(Object.class, jsonObject3);
                                if (!MetaUtils.isLogicalPrimitive(jsonObject3.getTargetClass())) {
                                    this.convertMapsToObjects(jsonObject3);
                                }
                                collection.add(jsonObject3.target);
                            }
                        }
                    }
                    else {
                        collection.add(o);
                    }
                }
                ++n;
            }
            jsonObject.remove("@items");
        }
    }
    
    @Override
    public void traverseFields(final Deque<JsonObject<String, Object>> deque, final JsonObject<String, Object> jsonObject) {
        final Object target = jsonObject.target;
        final Iterator<Map.Entry<Object, Object>> iterator = jsonObject.entrySet().iterator();
        final Class<?> class1 = target.getClass();
        while (iterator.hasNext()) {
            final Map.Entry<Object, Object> entry = iterator.next();
            final String s = entry.getKey();
            final Field field = MetaUtils.getField(class1, s);
            final Object value = entry.getValue();
            if (field != null) {
                this.assignField(deque, jsonObject, field, value);
            }
            else {
                if (this.missingFieldHandler == null) {
                    continue;
                }
                this.handleMissingField(deque, jsonObject, value, s);
            }
        }
    }
}
