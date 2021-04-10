package com.zhekasmirnov.apparatus.util;

import java.util.function.*;
import java.util.stream.*;
import com.zhekasmirnov.horizon.modloader.java.*;
import com.zhekasmirnov.horizon.modloader.mod.*;
import com.zhekasmirnov.mcpe161.*;
import com.zhekasmirnov.horizon.launcher.pack.*;
import dalvik.system.*;
import java.io.*;
import java.util.*;

public class HorizonPackUtils
{
    private static final List<Class<?>> allClasses;
    
    static {
        allClasses = new ArrayList<Class<?>>();
    }
    
    public static <T> List<Class<? extends T>> getAll(final Class<T> clazz) {
        while (true) {
            rebuildClassCacheIfRequired();
            while (true) {
                Label_0076: {
                    synchronized (HorizonPackUtils.allClasses) {
                        final ArrayList<Class<? extends T>> list = new ArrayList<Class<? extends T>>();
                        final Iterator<Class<?>> iterator = HorizonPackUtils.allClasses.iterator();
                        if (!iterator.hasNext()) {
                            return list;
                        }
                        final Class<?> clazz2 = iterator.next();
                        if (clazz.isAssignableFrom(clazz2)) {
                            list.add((Class<? extends T>)clazz2);
                            break Label_0076;
                        }
                        break Label_0076;
                    }
                }
                continue;
            }
        }
    }
    
    public static List<Class<?>> getAllClasses(final Predicate<Class<?>> predicate) {
        rebuildClassCacheIfRequired();
        synchronized (HorizonPackUtils.allClasses) {
            return (List<Class<?>>)Java8BackComp.stream(HorizonPackUtils.allClasses).filter(predicate).collect((Collector<? super Class<?>, ?, List<? super Class<?>>>)Collectors.toList());
        }
    }
    
    public static List<File> getAllDexFiles() {
        final ArrayList<Object> list = new ArrayList<Object>();
        try {
            list.addAll(new JavaDirectory((Mod)null, new File(getPack().directory, "java/innercore")).getCompiledClassesFiles());
        }
        catch (Exception ex) {}
        final Iterator<File> iterator = InnerCore.getJavaDirectoriesFromProxy().iterator();
        while (iterator.hasNext()) {
            list.addAll(new JavaDirectory((Mod)null, (File)iterator.next()).getCompiledClassesFiles());
        }
        return (List<File>)list;
    }
    
    public static Pack getPack() {
        return InnerCore.getInstance().getPack();
    }
    
    private static void rebuildClassCacheIfRequired() {
        while (true) {
            while (true) {
                Label_0109: {
                    synchronized (HorizonPackUtils.allClasses) {
                        if (HorizonPackUtils.allClasses.isEmpty()) {
                            final Iterator<File> iterator = getAllDexFiles().iterator();
                            if (iterator.hasNext()) {
                                final File file = iterator.next();
                                try {
                                    final Enumeration entries = new DexFile(file).entries();
                                    while (entries.hasMoreElements()) {
                                        HorizonPackUtils.allClasses.add(Class.forName(entries.nextElement()));
                                    }
                                    break Label_0109;
                                }
                                catch (IOException | ClassNotFoundException ex) {
                                    final Throwable t;
                                    t.printStackTrace();
                                    break Label_0109;
                                }
                                break Label_0109;
                            }
                        }
                        return;
                    }
                }
                continue;
            }
        }
    }
}
