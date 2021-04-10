package com.faendir.rhino_android;

import org.mozilla.javascript.*;
import dalvik.system.*;
import net.lingala.zip4j.core.*;
import java.io.*;
import net.lingala.zip4j.exception.*;
import com.android.dx.command.dexer.*;
import com.zhekasmirnov.innercore.utils.*;
import java.util.*;
import net.lingala.zip4j.model.*;

public class AndroidClassLoader extends ClassLoader implements GeneratedClassLoader
{
    private static String compilationModePath;
    private static int instanceCounter;
    private final File classFile;
    private final File dexFile;
    private List<DexFile> dx;
    private final File odexOatFile;
    private final ClassLoader parent;
    
    static {
        AndroidClassLoader.instanceCounter = 0;
        AndroidClassLoader.compilationModePath = null;
    }
    
    public AndroidClassLoader(final ClassLoader parent, final File file) {
        this.parent = parent;
        this.dx = new ArrayList<DexFile>();
        final int instanceCounter = AndroidClassLoader.instanceCounter;
        AndroidClassLoader.instanceCounter = instanceCounter + 1;
        final StringBuilder sb = new StringBuilder();
        sb.append("dex-");
        sb.append(instanceCounter);
        sb.append(".jar");
        this.dexFile = new File(file, sb.toString());
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("odex_oat-");
        sb2.append(instanceCounter);
        sb2.append(".tmp");
        this.odexOatFile = new File(file, sb2.toString());
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("class-");
        sb3.append(instanceCounter);
        sb3.append(".jar");
        this.classFile = new File(file, sb3.toString());
        file.mkdirs();
        this.reset();
    }
    
    public static void enterCompilationMode(final String compilationModePath) {
        AndroidClassLoader.compilationModePath = compilationModePath;
    }
    
    public static void exitCompilationMode() {
        AndroidClassLoader.compilationModePath = null;
    }
    
    public Class<?> defineClass(final String s, final byte[] array) {
        try {
            try {
                final ZipFile zipFile = new ZipFile(this.classFile);
                final ZipParameters zipParameters = new ZipParameters();
                final StringBuilder sb = new StringBuilder();
                sb.append(s.replace('.', '/'));
                sb.append(".class");
                zipParameters.setFileNameInZip(sb.toString());
                zipParameters.setSourceExternalStream(true);
                zipFile.addStream(new ByteArrayInputStream(array), zipParameters);
                final Class loadClass = this.dexJar().loadClass(s, this.parent);
                this.dexFile.delete();
                this.odexOatFile.delete();
                return (Class<?>)loadClass;
            }
            finally {}
        }
        catch (Throwable t) {
            t.printStackTrace();
            throw t;
        }
        catch (IOException | ZipException ex) {
            final Object o;
            throw new FatalLoadingException((Throwable)o);
        }
        this.dexFile.delete();
        this.odexOatFile.delete();
    }
    
    public DexFile dexJar() throws IOException {
        if (!this.classFile.exists()) {
            this.classFile.createNewFile();
        }
        final Main.Arguments arguments = new Main.Arguments();
        arguments.fileNames = new String[] { this.classFile.getPath() };
        arguments.outName = this.dexFile.getPath();
        arguments.jarOutput = true;
        Main.run(arguments);
        final DexFile loadDex = DexFile.loadDex(this.dexFile.getPath(), this.odexOatFile.getPath(), 0);
        if (AndroidClassLoader.compilationModePath != null) {
            FileTools.copy(this.dexFile, new File(AndroidClassLoader.compilationModePath));
        }
        this.dx.add(loadDex);
        return loadDex;
    }
    
    public void linkClass(final Class<?> clazz) {
    }
    
    public Class<?> loadClass(final String s, final boolean b) throws ClassNotFoundException {
        Class<?> clazz;
        Class<?> loadClass = clazz = this.findLoadedClass(s);
        if (loadClass == null) {
            final Iterator<DexFile> iterator = this.dx.iterator();
            while (iterator.hasNext()) {
                loadClass = (Class<?>)iterator.next().loadClass(s, this.parent);
                if (loadClass != null) {
                    break;
                }
            }
            if ((clazz = loadClass) == null) {
                clazz = this.parent.loadClass(s);
            }
        }
        return clazz;
    }
    
    void loadJar(final File file) throws IOException {
        while (true) {
            while (true) {
                Label_0122: {
                    try {
                        final ZipFile zipFile = new ZipFile(this.classFile);
                        final ZipFile zipFile2 = new ZipFile(file);
                        final Iterator iterator = zipFile2.getFileHeaders().iterator();
                        if (!iterator.hasNext()) {
                            this.dexJar();
                            return;
                        }
                        final FileHeader fileHeader = iterator.next();
                        if (!fileHeader.isDirectory()) {
                            final ZipParameters zipParameters = new ZipParameters();
                            zipParameters.setFileNameInZip(fileHeader.getFileName());
                            zipParameters.setSourceExternalStream(true);
                            zipFile.addStream(zipFile2.getInputStream(fileHeader), zipParameters);
                            break Label_0122;
                        }
                        break Label_0122;
                    }
                    catch (ZipException ex2) {
                        final IOException ex = new IOException();
                        ex.initCause(ex2);
                        throw ex;
                    }
                }
                continue;
            }
        }
    }
    
    void reset() {
        this.dexFile.delete();
        this.odexOatFile.delete();
        this.classFile.delete();
    }
    
    public static class FatalLoadingException extends RuntimeException
    {
        FatalLoadingException(final Throwable t) {
            super("Failed to define class", t);
        }
    }
}
