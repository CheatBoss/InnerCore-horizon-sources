package com.zhekasmirnov.horizon.launcher.env;

import java.io.*;
import dalvik.system.*;
import android.util.*;
import com.zhekasmirnov.horizon.util.*;
import java.lang.reflect.*;
import java.util.zip.*;
import java.util.*;
import com.zhekasmirnov.horizon.*;

public class ClassLoaderPatch
{
    private static final List<Object> patchedObjects;
    
    public static Object newGenericArrayOfType(final Class type, final int len) {
        return Array.newInstance(type, len);
    }
    
    public static synchronized void addNativeLibraryPath(final ClassLoader classLoader, final File path) {
        try {
            final Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
            pathListField.setAccessible(true);
            final Object pathList = pathListField.get(classLoader);
            final Field libraryDirectoriesField = pathList.getClass().getDeclaredField("nativeLibraryDirectories");
            libraryDirectoriesField.setAccessible(true);
            try {
                final List<File> libraryDirectories = (List<File>)libraryDirectoriesField.get(pathList);
                libraryDirectories.add(0, path);
            }
            catch (ClassCastException e) {
                final File[] libraryDirectories2 = (File[])libraryDirectoriesField.get(pathList);
                final File[] newLibraryDirectories = new File[libraryDirectories2.length + 1];
                newLibraryDirectories[0] = path;
                System.arraycopy(libraryDirectories2, 0, newLibraryDirectories, 1, libraryDirectories2.length);
                libraryDirectoriesField.set(pathList, newLibraryDirectories);
            }
            ClassLoaderPatch.patchedObjects.add(path);
            try {
                final Field nativeLibraryPathElementsField = pathList.getClass().getDeclaredField("nativeLibraryPathElements");
                nativeLibraryPathElementsField.setAccessible(true);
                final Object[] nativeLibElements = (Object[])nativeLibraryPathElementsField.get(pathList);
                Object newInstance;
                try {
                    final Constructor constructor = nativeLibElements[0].getClass().getDeclaredConstructor(File.class);
                    constructor.setAccessible(true);
                    newInstance = constructor.newInstance(path);
                }
                catch (NoSuchMethodException ignore) {
                    final Constructor constructor2 = nativeLibElements[0].getClass().getDeclaredConstructor(File.class, Boolean.TYPE, File.class, DexFile.class);
                    constructor2.setAccessible(true);
                    newInstance = constructor2.newInstance(path, true, null, null);
                    try {
                        newInstance.getClass().getDeclaredMethod("maybeInit", (Class<?>[])new Class[0]).invoke(newInstance, new Object[0]);
                    }
                    catch (NoSuchMethodException ex) {}
                }
                final Object newList = newGenericArrayOfType(nativeLibElements[0].getClass(), nativeLibElements.length + 1);
                ((Object[])newList)[0] = newInstance;
                System.arraycopy(nativeLibElements, 0, newList, 1, nativeLibElements.length);
                nativeLibraryPathElementsField.set(pathList, newList);
                ClassLoaderPatch.patchedObjects.add(newInstance);
            }
            catch (NoSuchFieldException err2) {
                Log.e("ClassLoaderPatch", "Failed to modify nativeLibraryPathElements, no field found, it may be old version or something else");
                Log.i("ClassLoaderPatch", "Showing class loader inner structure:");
                ReflectionHelper.printClassStructure(classLoader.getClass(), classLoader, "ClassLoaderPatch", "    ", true);
                Log.i("ClassLoaderPatch", "Showing path list inner structure:");
                ReflectionHelper.printClassStructure(pathList.getClass(), pathList, "ClassLoaderPatch", "    ", true);
            }
            Log.d("ClassLoaderPatch", "modified class loader (native path, hash: " + classLoader.hashCode() + "): " + classLoader);
        }
        catch (Exception err) {
            System.out.println("ClassLoaderPatch: Showing class loader inner structure: \n" + ReflectionHelper.getClassStructureString(classLoader.getClass(), classLoader, "    ", true));
            throw new RuntimeException("failed to patch classloader with following error", err);
        }
    }
    
    public static synchronized void addDexPath(final ClassLoader classLoader, final File path) {
        try {
            final Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
            pathListField.setAccessible(true);
            final Object pathList = pathListField.get(classLoader);
            final Field dexElementsField = pathList.getClass().getDeclaredField("dexElements");
            dexElementsField.setAccessible(true);
            final Object[] dexElements = (Object[])dexElementsField.get(pathList);
            Object element = null;
            try {
                final Constructor constructor = dexElements[0].getClass().getDeclaredConstructor(DexFile.class, File.class);
                constructor.setAccessible(true);
                element = constructor.newInstance(new DexFile(path), null);
            }
            catch (NoSuchMethodException ignore) {
                try {
                    final Constructor constructor2 = dexElements[0].getClass().getDeclaredConstructor(File.class, ZipFile.class, DexFile.class);
                    constructor2.setAccessible(true);
                    element = constructor2.newInstance(null, null, new DexFile(path));
                }
                catch (NoSuchMethodException ignore2) {
                    final Constructor constructor3 = dexElements[0].getClass().getDeclaredConstructor(File.class, Boolean.TYPE, File.class, DexFile.class);
                    constructor3.setAccessible(true);
                    element = constructor3.newInstance(null, false, null, new DexFile(path));
                }
            }
            final Object newList = newGenericArrayOfType(dexElements[0].getClass(), dexElements.length + 1);
            ((Object[])newList)[dexElements.length] = element;
            System.arraycopy(dexElements, 0, newList, 0, dexElements.length);
            dexElementsField.set(pathList, newList);
            ClassLoaderPatch.patchedObjects.add(element);
            Log.d("ClassLoaderPatch", "modified class loader (dex path, hash: " + classLoader.hashCode() + "): " + classLoader);
        }
        catch (Exception err) {
            System.out.println("ClassLoaderPatch: Showing class loader inner structure: \n" + ReflectionHelper.getClassStructureString(classLoader.getClass(), classLoader, "    ", true));
            throw new RuntimeException("failed to patch classloader with following error", err);
        }
    }
    
    private static Object removePatchesFromGenericArray(final Object[] arr, final Class clazz) {
        final List<Object> result = new ArrayList<Object>();
        for (final Object obj : arr) {
            if (!ClassLoaderPatch.patchedObjects.contains(obj)) {
                result.add(obj);
            }
        }
        int index = 0;
        final Object generic = newGenericArrayOfType(clazz, result.size());
        for (final Object obj : result) {
            ((Object[])generic)[index++] = obj;
        }
        return generic;
    }
    
    public static synchronized void revertClassLoaderPatches(final ClassLoader classLoader) {
        System.out.println("CLASSLOADER BEFORE REVERTED: " + classLoader);
        try {
            final Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
            pathListField.setAccessible(true);
            final Object pathList = pathListField.get(classLoader);
            final Field libraryDirectoriesField = pathList.getClass().getDeclaredField("nativeLibraryDirectories");
            libraryDirectoriesField.setAccessible(true);
            final List<File> libraryDirectories = (List<File>)libraryDirectoriesField.get(pathList);
            for (final Object patched : ClassLoaderPatch.patchedObjects) {
                if (patched instanceof File) {
                    libraryDirectories.remove(patched);
                }
            }
            final Field dexElementsField = pathList.getClass().getDeclaredField("dexElements");
            dexElementsField.setAccessible(true);
            final Object[] dexElements = (Object[])dexElementsField.get(pathList);
            dexElementsField.set(pathList, removePatchesFromGenericArray(dexElements, dexElements[0].getClass()));
            final Field nativeLibraryPathElementsField = pathList.getClass().getDeclaredField("nativeLibraryPathElements");
            nativeLibraryPathElementsField.setAccessible(true);
            final Object[] nativeLibElements = (Object[])nativeLibraryPathElementsField.get(pathList);
            nativeLibraryPathElementsField.set(pathList, removePatchesFromGenericArray(nativeLibElements, nativeLibElements[0].getClass()));
        }
        catch (Exception err) {
            throw new RuntimeException("failed to revert patched classloader with following error", err);
        }
        System.out.println("CLASSLOADER AFTER REVERTED: " + classLoader);
    }
    
    static {
        HorizonLibrary.include();
        patchedObjects = new ArrayList<Object>();
    }
}
