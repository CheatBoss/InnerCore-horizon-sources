package com.zhekasmirnov.horizon.modloader.java;

import android.content.*;
import com.zhekasmirnov.horizon.modloader.*;
import org.eclipse.jdt.internal.compiler.batch.*;
import com.zhekasmirnov.horizon.compiler.packages.*;
import org.eclipse.jdt.core.compiler.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import com.zhekasmirnov.horizon.runtime.task.*;
import java.util.jar.*;
import java.util.zip.*;
import java.util.*;
import java.io.*;
import com.zhekasmirnov.horizon.util.*;

public class JavaCompilerHolder
{
    private final String COMPONENT_VERSION_UUID = "34b14f6e-d8d1-48af-86a7-8adcb41396ce";
    private static final String COMPONENT_PATH = "sdk/java/";
    private final Component[] COMPONENTS;
    private boolean configured;
    private static final HashMap<Context, JavaCompilerHolder> instances;
    private final ModContext context;
    private final File installationDir;
    private final Main main;
    private boolean isInitializing;
    private boolean isInitialized;
    private boolean isInstalled;
    
    public static JavaCompilerHolder getInstance(final Context ctx) {
        synchronized (JavaCompilerHolder.instances) {
            return JavaCompilerHolder.instances.get(ctx);
        }
    }
    
    public static void initializeForContext(final ModContext ctx, final TaskManager taskManager) {
        if (getInstance(ctx.context) == null) {
            synchronized (JavaCompilerHolder.instances) {
                final JavaCompilerHolder instance = new JavaCompilerHolder(ctx);
                JavaCompilerHolder.instances.put(ctx.context, instance);
                taskManager.addTask(instance.getInitializationTask());
            }
        }
    }
    
    public JavaCompilerHolder(final ModContext context) {
        this.COMPONENTS = new Component[] { new JarComponent("sdk/java/", "android.jar"), new JarComponent("sdk/java/", "android-support-multidex.jar"), new JarComponent("sdk/java/", "android-support-v4.jar"), new JarComponent("sdk/java/", "dx.jar"), new JarComponent("sdk/java/", "support-annotations-25.3.1.jar"), new JarComponent("sdk/java/", "gson-2.6.2.jar"), new JarComponent("sdk/java/", "horizon-classes.jar") };
        this.configured = false;
        this.isInitializing = false;
        this.isInitialized = false;
        this.isInstalled = false;
        this.context = context;
        this.installationDir = new File(Environment.getJavacDir(context.context));
        if (!this.installationDir.exists()) {
            this.installationDir.mkdirs();
        }
        if (!this.installationDir.isDirectory()) {
            throw new RuntimeException("failed to allocate installation directory " + this.installationDir);
        }
        final PrintWriter infoWriter = new PrintWriter(context.getEventLogger().getStream(EventLogger.MessageType.INFO, "BUILD"));
        final PrintWriter errorWriter = new PrintWriter(context.getEventLogger().getStream(EventLogger.MessageType.FAULT, "BUILD"));
        this.main = new Main(infoWriter, errorWriter, false, (Map)null, (CompilationProgress)null);
    }
    
    private void initialize() {
        this.isInstalled = true;
        for (final Component component : this.COMPONENTS) {
            if (!component.isInstalled()) {
                Logger.debug("JavaCompiler", "installing or re-installing java component: " + component);
                if (!component.install()) {
                    this.isInstalled = false;
                }
            }
        }
    }
    
    public Task getInitializationTask() {
        return new Task() {
            @Override
            public Object getLock() {
                return "initialize_javac";
            }
            
            @Override
            public void run() {
                if (!JavaCompilerHolder.this.isInitializing) {
                    JavaCompilerHolder.this.isInitializing = true;
                    if (!JavaCompilerHolder.this.isInitialized) {
                        JavaCompilerHolder.this.initialize();
                        JavaCompilerHolder.this.isInitialized = true;
                    }
                    JavaCompilerHolder.this.isInitializing = false;
                }
            }
            
            @Override
            public String getDescription() {
                return "initializing javac";
            }
        };
    }
    
    private void awaitInitialization() {
        while (!this.isInitialized) {
            Thread.yield();
        }
    }
    
    public boolean compile(final JavaCompilerArguments args) {
        if (!this.configured) {
            this.main.configure(args.toArray());
            this.configured = true;
        }
        this.awaitInitialization();
        return this.main.compile(new String[0]);
    }
    
    public List<File> getBootFiles() {
        final List<File> files = new ArrayList<File>();
        for (final Component component : this.COMPONENTS) {
            files.addAll(component.getBootFiles());
        }
        return files;
    }
    
    void installLibraries(final List<File> files, final File directory) {
        for (final File file : files) {
            final String name = file.getName();
            if (name.endsWith(".jar") || name.endsWith("zip")) {
                this.installJarLibrary(file, directory);
            }
            else {
                if (!name.endsWith(".dex")) {
                    throw new RuntimeException("Unsupported file format:  " + file.getName());
                }
                final File jar = new File(file.getAbsolutePath().replace(".dex", ".jar"));
                this.installJarLibrary(jar, directory);
                jar.delete();
            }
        }
    }
    
    private void installJarLibrary(final File file, final File directory) {
        try {
            final JarFile jar = new JarFile(file);
            final Enumeration entries = jar.entries();
            directory.mkdirs();
            while (entries.hasMoreElements()) {
                final JarEntry clazz = entries.nextElement();
                if (!clazz.getName().endsWith(".class")) {
                    continue;
                }
                final File f = new File(directory + File.separator + clazz.getName());
                if (clazz.isDirectory()) {
                    f.mkdir();
                }
                else {
                    if (f.exists()) {
                        continue;
                    }
                    f.getParentFile().mkdirs();
                    final InputStream is = jar.getInputStream(clazz);
                    final FileOutputStream fos = new FileOutputStream(f);
                    final byte[] buffer = new byte[8192];
                    int byteCount;
                    while ((byteCount = is.read(buffer)) > 0) {
                        fos.write(buffer, 0, byteCount);
                    }
                    fos.close();
                    is.close();
                }
            }
        }
        catch (IOException e) {
            throw new RuntimeException("failed to install jar library " + file.getName(), e);
        }
    }
    
    static {
        instances = new HashMap<Context, JavaCompilerHolder>();
    }
    
    private class JarComponent implements Component
    {
        private final String assetPath;
        private final String assetName;
        
        private JarComponent(final String assetPath, final String assetName) {
            this.assetPath = assetPath;
            this.assetName = assetName;
        }
        
        private File getJarFile() {
            return new File(Environment.getJavacDir(JavaCompilerHolder.this.context.context), this.assetName);
        }
        
        private String getLockFile() {
            try {
                return FileUtils.readFileText(new File(Environment.getJavacDir(JavaCompilerHolder.this.context.context), this.assetName + ".uuid")).trim();
            }
            catch (IOException e) {
                return null;
            }
            catch (NullPointerException e2) {
                return null;
            }
        }
        
        private void setLockFile() {
            try {
                FileUtils.writeFileText(new File(Environment.getJavacDir(JavaCompilerHolder.this.context.context), this.assetName + ".uuid"), "34b14f6e-d8d1-48af-86a7-8adcb41396ce");
            }
            catch (IOException e) {
                throw new RuntimeException("failed to write UUID lock for jar component: " + this.assetName);
            }
        }
        
        @Override
        public boolean isInstalled() {
            return this.getJarFile().exists() && "34b14f6e-d8d1-48af-86a7-8adcb41396ce".equals(this.getLockFile());
        }
        
        @Override
        public boolean install() {
            try {
                FileUtils.unpackAssetOrDirectory(JavaCompilerHolder.this.context.context.getAssets(), this.getJarFile(), this.assetPath + this.assetName);
                this.setLockFile();
                return true;
            }
            catch (Exception e) {
                throw new RuntimeException("failed to install jar component " + this.assetName, e);
            }
        }
        
        @Override
        public List<File> getBootFiles() {
            final List<File> files = new ArrayList<File>();
            files.add(this.getJarFile());
            return files;
        }
        
        @Override
        public String toString() {
            return "[jar component " + this.assetName + "]";
        }
    }
    
    private interface Component
    {
        boolean isInstalled();
        
        boolean install();
        
        List<File> getBootFiles();
    }
}
