package com.zhekasmirnov.innercore.api.mod.preloader;

import com.zhekasmirnov.innercore.api.log.*;
import org.mozilla.javascript.annotations.*;
import com.zhekasmirnov.innercore.api.mod.adaptedscript.*;
import android.widget.*;
import android.content.*;
import android.app.*;
import com.zhekasmirnov.innercore.mod.executable.*;
import com.zhekasmirnov.innercore.api.annotations.*;
import com.zhekasmirnov.innercore.api.runtime.*;
import com.zhekasmirnov.innercore.mod.build.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.mcpe161.*;
import java.io.*;
import com.zhekasmirnov.innercore.api.mod.*;
import java.util.*;

public class PreloaderAPI extends API
{
    @JSStaticFunction
    public static void log(final String s) {
        ICLog.d("PRELOADER", s);
    }
    
    @JSStaticFunction
    public static void print(final String s) {
        ICLog.d("PRELOADER-PRINT", s);
        AdaptedScriptAPI.MCSystem.getContext().runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                final Activity context = AdaptedScriptAPI.MCSystem.getContext();
                final StringBuilder sb = new StringBuilder();
                sb.append("(PRELOADER) ");
                sb.append(s);
                Toast.makeText((Context)context, (CharSequence)sb.toString(), 0).show();
            }
        });
    }
    
    @Override
    public int getLevel() {
        return 1;
    }
    
    @Override
    public String getName() {
        return "Preloader";
    }
    
    @Override
    public void onCallback(final String s, final Object[] array) {
    }
    
    @Override
    public void onLoaded() {
    }
    
    @Override
    public void onModLoaded(final Mod mod) {
    }
    
    @Override
    public void setupCallbacks(final Executable executable) {
    }
    
    @APIStaticModule
    public static class Callback
    {
        @JSStaticFunction
        public static void addCallback(final String s, final Function function, final int n) {
            com.zhekasmirnov.innercore.api.runtime.Callback.addCallback(s, function, n);
        }
        
        @JSStaticFunction
        public static void invokeCallback(final String s, final Object o, final Object o2, final Object o3, final Object o4, final Object o5, final Object o6, final Object o7, final Object o8, final Object o9, final Object o10) {
            com.zhekasmirnov.innercore.api.runtime.Callback.invokeCallback(s, o, o2, o3, o4, o5, o6, o7, o8, o9, o10);
        }
    }
    
    @APIStaticModule
    public static class Resources extends AdaptedScriptAPI.Resources
    {
        @Deprecated
        @JSStaticFunction
        public static void __placeholder() {
        }
        
        @JSStaticFunction
        public static String addRuntimePack(final String s, final String s2) {
            return ModLoader.instance.addRuntimePack(ModLoader.MinecraftPackType.fromString(s), s2).getAbsolutePath();
        }
        
        @JSStaticFunction
        public static NativeArray getAllMatchingResources(final String s) {
            final HashSet<String> set = new HashSet<String>();
            for (final File file : InnerCore.getInstance().allResourceDirectories) {
                searchFilesInDir(set, file, file, s);
            }
            return ScriptableObjectHelper.createArray(set.toArray());
        }
        
        @JSStaticFunction
        public static NativeArray getAllMatchingResourcesInDir(final Object o, final String s) {
            final File file = (File)org.mozilla.javascript.Context.jsToJava(o, (Class)File.class);
            final HashSet<String> set = new HashSet<String>();
            searchFilesInDir(set, file, file, s);
            return ScriptableObjectHelper.createArray(set.toArray());
        }
        
        @JSStaticFunction
        public static NativeArray getAllMatchingResourcesInPath(final String s, final String s2) {
            final File file = new File(s);
            final HashSet<String> set = new HashSet<String>();
            searchFilesInDir(set, file, file, s2);
            return ScriptableObjectHelper.createArray(set.toArray());
        }
        
        @JSStaticFunction
        public static NativeArray getAllResourceDirectories() {
            return ScriptableObjectHelper.createArray(InnerCore.getInstance().allResourceDirectories.toArray());
        }
        
        @JSStaticFunction
        public static NativeArray getAllResourceDirectoriesPaths() {
            final ArrayList<String> list = new ArrayList<String>();
            final Iterator<File> iterator = InnerCore.getInstance().allResourceDirectories.iterator();
            while (iterator.hasNext()) {
                list.add(iterator.next().getAbsolutePath());
            }
            return ScriptableObjectHelper.createArray(list.toArray());
        }
        
        @JSStaticFunction
        public static String getResourcePath(final String s) {
            final File resourcePathNoVariants = getResourcePathNoVariants(s);
            if (resourcePathNoVariants != null) {
                return resourcePathNoVariants.getAbsolutePath();
            }
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(".png");
            final File resourcePathNoVariants2 = getResourcePathNoVariants(sb.toString());
            if (resourcePathNoVariants2 != null) {
                return resourcePathNoVariants2.getAbsolutePath();
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(s);
            sb2.append(".tga");
            final File resourcePathNoVariants3 = getResourcePathNoVariants(sb2.toString());
            if (resourcePathNoVariants3 != null) {
                return resourcePathNoVariants3.getAbsolutePath();
            }
            return null;
        }
        
        private static File getResourcePathNoVariants(final String s) {
            final InnerCore instance = InnerCore.getInstance();
            final Iterator<File> iterator = instance.allResourceDirectories.iterator();
            while (iterator.hasNext()) {
                final File file = new File(iterator.next(), s);
                if (file.isFile()) {
                    return file;
                }
            }
            final File file2 = new File(instance.getWorkingDirectory(), "assets");
            final File file3 = new File(file2, s);
            if (file3.isFile()) {
                return file3;
            }
            final File file4 = new File(new File(file2, "resource_packs/vanilla"), s);
            if (file4.isFile()) {
                return file4;
            }
            return null;
        }
        
        private static void searchFilesInDir(final Collection<String> collection, final File file, final File file2, final String s) {
            if (file2.isDirectory()) {
                final File[] listFiles = file2.listFiles();
                for (int length = listFiles.length, i = 0; i < length; ++i) {
                    searchFilesInDir(collection, file, listFiles[i], s);
                }
            }
            else if (file2.getAbsolutePath().substring(file.getAbsolutePath().length() + 1).matches(s)) {
                collection.add(file2.getAbsolutePath());
            }
        }
    }
    
    @APIStaticModule
    public static class Textures
    {
        @Deprecated
        @JSStaticFunction
        public static void __placeholder() {
        }
    }
}
