package com.google.gson.internal;

import java.lang.reflect.*;
import com.google.gson.*;
import java.util.concurrent.*;
import com.google.gson.reflect.*;
import java.util.*;

public final class ConstructorConstructor
{
    private final Map<Type, InstanceCreator<?>> instanceCreators;
    
    public ConstructorConstructor(final Map<Type, InstanceCreator<?>> instanceCreators) {
        this.instanceCreators = instanceCreators;
    }
    
    private <T> ObjectConstructor<T> newDefaultConstructor(final Class<? super T> clazz) {
        try {
            final Constructor<? super T> declaredConstructor = clazz.getDeclaredConstructor((Class<?>[])new Class[0]);
            if (!declaredConstructor.isAccessible()) {
                declaredConstructor.setAccessible(true);
            }
            return new ObjectConstructor<T>() {
                @Override
                public T construct() {
                    try {
                        return declaredConstructor.newInstance((Object[])null);
                    }
                    catch (IllegalAccessException ex) {
                        throw new AssertionError((Object)ex);
                    }
                    catch (InvocationTargetException ex2) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Failed to invoke ");
                        sb.append(declaredConstructor);
                        sb.append(" with no args");
                        throw new RuntimeException(sb.toString(), ex2.getTargetException());
                    }
                    catch (InstantiationException ex3) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("Failed to invoke ");
                        sb2.append(declaredConstructor);
                        sb2.append(" with no args");
                        throw new RuntimeException(sb2.toString(), ex3);
                    }
                }
            };
        }
        catch (NoSuchMethodException ex) {
            return null;
        }
    }
    
    private <T> ObjectConstructor<T> newDefaultImplementationConstructor(final Type type, final Class<? super T> clazz) {
        if (Collection.class.isAssignableFrom(clazz)) {
            if (SortedSet.class.isAssignableFrom(clazz)) {
                return new ObjectConstructor<T>() {
                    @Override
                    public T construct() {
                        return (T)new TreeSet();
                    }
                };
            }
            if (EnumSet.class.isAssignableFrom(clazz)) {
                return new ObjectConstructor<T>() {
                    @Override
                    public T construct() {
                        final Type val$type = type;
                        if (!(val$type instanceof ParameterizedType)) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("Invalid EnumSet type: ");
                            sb.append(type.toString());
                            throw new JsonIOException(sb.toString());
                        }
                        final Type type = ((ParameterizedType)val$type).getActualTypeArguments()[0];
                        if (type instanceof Class) {
                            return (T)EnumSet.noneOf((Class<Enum>)type);
                        }
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("Invalid EnumSet type: ");
                        sb2.append(type.toString());
                        throw new JsonIOException(sb2.toString());
                    }
                };
            }
            if (Set.class.isAssignableFrom(clazz)) {
                return new ObjectConstructor<T>() {
                    @Override
                    public T construct() {
                        return (T)new LinkedHashSet();
                    }
                };
            }
            if (Queue.class.isAssignableFrom(clazz)) {
                return new ObjectConstructor<T>() {
                    @Override
                    public T construct() {
                        return (T)new LinkedList();
                    }
                };
            }
            return new ObjectConstructor<T>() {
                @Override
                public T construct() {
                    return (T)new ArrayList();
                }
            };
        }
        else {
            if (!Map.class.isAssignableFrom(clazz)) {
                return null;
            }
            if (ConcurrentNavigableMap.class.isAssignableFrom(clazz)) {
                return new ObjectConstructor<T>() {
                    @Override
                    public T construct() {
                        return (T)new ConcurrentSkipListMap();
                    }
                };
            }
            if (ConcurrentMap.class.isAssignableFrom(clazz)) {
                return new ObjectConstructor<T>() {
                    @Override
                    public T construct() {
                        return (T)new ConcurrentHashMap();
                    }
                };
            }
            if (SortedMap.class.isAssignableFrom(clazz)) {
                return new ObjectConstructor<T>() {
                    @Override
                    public T construct() {
                        return (T)new TreeMap();
                    }
                };
            }
            if (type instanceof ParameterizedType && !String.class.isAssignableFrom(TypeToken.get(((ParameterizedType)type).getActualTypeArguments()[0]).getRawType())) {
                return new ObjectConstructor<T>() {
                    @Override
                    public T construct() {
                        return (T)new LinkedHashMap();
                    }
                };
            }
            return new ObjectConstructor<T>() {
                @Override
                public T construct() {
                    return (T)new LinkedTreeMap();
                }
            };
        }
    }
    
    private <T> ObjectConstructor<T> newUnsafeAllocator(final Type type, final Class<? super T> clazz) {
        return new ObjectConstructor<T>() {
            private final UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
            
            @Override
            public T construct() {
                try {
                    return this.unsafeAllocator.newInstance(clazz);
                }
                catch (Exception ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unable to invoke no-args constructor for ");
                    sb.append(type);
                    sb.append(". ");
                    sb.append("Register an InstanceCreator with Gson for this type may fix this problem.");
                    throw new RuntimeException(sb.toString(), ex);
                }
            }
        };
    }
    
    public <T> ObjectConstructor<T> get(final TypeToken<T> typeToken) {
        final Type type = typeToken.getType();
        final Class<? super T> rawType = typeToken.getRawType();
        final InstanceCreator<?> instanceCreator = this.instanceCreators.get(type);
        if (instanceCreator != null) {
            return new ObjectConstructor<T>() {
                @Override
                public T construct() {
                    return instanceCreator.createInstance(type);
                }
            };
        }
        final InstanceCreator<?> instanceCreator2 = this.instanceCreators.get(rawType);
        if (instanceCreator2 != null) {
            return new ObjectConstructor<T>() {
                @Override
                public T construct() {
                    return instanceCreator2.createInstance(type);
                }
            };
        }
        final ObjectConstructor<Object> defaultConstructor = this.newDefaultConstructor((Class<? super Object>)rawType);
        if (defaultConstructor != null) {
            return (ObjectConstructor<T>)defaultConstructor;
        }
        final ObjectConstructor<Object> defaultImplementationConstructor = this.newDefaultImplementationConstructor(type, (Class<? super Object>)rawType);
        if (defaultImplementationConstructor != null) {
            return (ObjectConstructor<T>)defaultImplementationConstructor;
        }
        return (ObjectConstructor<T>)this.newUnsafeAllocator(type, (Class<? super Object>)rawType);
    }
    
    @Override
    public String toString() {
        return this.instanceCreators.toString();
    }
}
