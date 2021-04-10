package com.zhekasmirnov.innercore.mod.java;

import dalvik.system.*;
import com.faendir.rhino_android.*;
import com.zhekasmirnov.innercore.api.log.*;
import java.io.*;
import java.util.*;

public class JavaModDexLoader
{
    private Class loadClassFromDex(final DexFile dexFile, final String s) {
        return dexFile.loadClass(s, AndroidContextFactory.class.getClassLoader());
    }
    
    public ArrayList<Class> loadDexFile(final File file) {
        try {
            final DexFile dexFile = new DexFile(file);
            final Enumeration entries = dexFile.entries();
            final ArrayList<Class> list = new ArrayList<Class>();
            while (entries.hasMoreElements()) {
                final String s = entries.nextElement();
                if (s != null) {
                    final Class loadClassFromDex = this.loadClassFromDex(dexFile, s);
                    if (loadClassFromDex == null) {
                        continue;
                    }
                    list.add(loadClassFromDex);
                }
            }
            return (ArrayList<Class>)list;
        }
        catch (IOException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("failed to load java class from dex ");
            sb.append(ex);
            ICLog.i("ERROR", sb.toString());
            return null;
        }
    }
}
