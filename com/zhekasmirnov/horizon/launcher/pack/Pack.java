package com.zhekasmirnov.horizon.launcher.pack;

import com.zhekasmirnov.horizon.launcher.*;
import com.zhekasmirnov.horizon.modloader.repo.*;
import com.zhekasmirnov.horizon.modloader.resource.*;
import com.zhekasmirnov.horizon.modloader.library.*;
import org.json.*;
import java.io.*;
import com.zhekasmirnov.horizon.compiler.packages.*;
import com.zhekasmirnov.horizon.launcher.env.*;
import com.zhekasmirnov.horizon.modloader.repo.storage.*;
import com.zhekasmirnov.horizon.modloader.mod.*;
import com.zhekasmirnov.horizon.modloader.resource.directory.*;
import com.zhekasmirnov.horizon.modloader.*;
import com.zhekasmirnov.horizon.modloader.java.*;
import com.zhekasmirnov.horizon.compiler.holder.*;
import com.zhekasmirnov.horizon.util.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import android.annotation.*;
import java.util.*;
import com.zhekasmirnov.horizon.launcher.ads.*;
import android.app.*;
import com.zhekasmirnov.horizon.*;
import android.content.*;
import android.graphics.drawable.*;
import android.view.*;
import android.graphics.*;
import android.widget.*;

public class Pack
{
    public final File directory;
    public final PackManifest manifest;
    public final ContextHolder contextHolder;
    public ModContext modContext;
    public ModList modList;
    public ModRepository modRepository;
    public ResourceManager resourceManager;
    private List<File> executableSoFiles;
    private List<JavaLibrary> bootJavaLibraries;
    private List<LibraryDirectory> bootNativeLibraries;
    private final List<MenuActivityFactory> menuActivityFactories;
    private boolean isLaunchAborted;
    
    public Pack(final ContextHolder contextHolder, final File directory) {
        this.executableSoFiles = new ArrayList<File>();
        this.bootJavaLibraries = new ArrayList<JavaLibrary>();
        this.bootNativeLibraries = new ArrayList<LibraryDirectory>();
        this.menuActivityFactories = new ArrayList<MenuActivityFactory>();
        this.isLaunchAborted = false;
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("File is not a directory " + directory);
        }
        this.directory = directory;
        File manifest = new File(this.directory, "manifest");
        if (!manifest.exists()) {
            manifest = new File(this.directory, "manifest.json");
            if (!manifest.exists()) {
                throw new IllegalArgumentException("pack missing manifest.json: " + directory);
            }
        }
        try {
            this.manifest = new PackManifest(manifest);
        }
        catch (IOException exception) {
            throw new RuntimeException("failed to read pack manifest for: " + directory, exception);
        }
        catch (JSONException exception2) {
            throw new RuntimeException("failed to read pack manifest for: " + directory, (Throwable)exception2);
        }
        this.contextHolder = contextHolder;
    }
    
    public File getWorkingDirectory() {
        return this.directory;
    }
    
    public ModContext getModContext() {
        return this.modContext;
    }
    
    public ContextHolder getContextHolder() {
        return this.contextHolder;
    }
    
    public ModList getModList() {
        return this.modList;
    }
    
    public ModRepository getModRepository() {
        return this.modRepository;
    }
    
    public ResourceManager getResourceManager() {
        return this.resourceManager;
    }
    
    private List<String> loadOrderFile(final File orderFile) {
        List<String> order = null;
        if (orderFile.exists() && !orderFile.isDirectory()) {
            try {
                order = new ArrayList<String>();
                final BufferedReader reader = new BufferedReader(new FileReader(orderFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.length() > 0) {
                        order.add(line);
                    }
                }
            }
            catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        }
        return order;
    }
    
    private boolean initializeSharedObjects() {
        final File sharedObjectDir = new File(this.directory, "so");
        if (sharedObjectDir.exists() && sharedObjectDir.isDirectory()) {
            final File executionDir = Environment.getPackExecutionDir((Context)this.contextHolder.getContext());
            if (executionDir.exists()) {
                if (executionDir.isDirectory()) {
                    FileUtils.clearFileTree(executionDir, false);
                }
                executionDir.delete();
            }
            executionDir.mkdirs();
            this.executableSoFiles.clear();
            List<String> order = this.loadOrderFile(new File(sharedObjectDir, "order.txt"));
            ClassLoaderPatch.addNativeLibraryPath(this.getClass().getClassLoader(), executionDir);
            for (final String abi : Environment.getSupportedABIs()) {
                final File abiDir = new File(sharedObjectDir, abi);
                if (abiDir.exists() && abiDir.isDirectory()) {
                    if (order == null) {
                        order = new ArrayList<String>();
                        for (final File soFile : abiDir.listFiles()) {
                            order.add(soFile.getName());
                        }
                    }
                    for (final String soName : order) {
                        final File soFile2 = new File(abiDir, soName);
                        if (!soFile2.exists() || soFile2.isDirectory()) {
                            throw new RuntimeException("invalid so file: " + soName + " for " + abi);
                        }
                        final File soExecFile = new File(executionDir, soName);
                        try {
                            FileUtils.copy(soFile2, soExecFile);
                            this.executableSoFiles.add(soExecFile);
                        }
                        catch (IOException throwable) {
                            throw new RuntimeException("failed to unpack so file " + soName, throwable);
                        }
                    }
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
    private void initializeAssetsAndResources() {
        AssetPatch.setAssetDirectory(new File(this.directory, "assets").getAbsolutePath());
        this.resourceManager = new ResourceManager((Context)this.contextHolder.getContext());
    }
    
    private void initializeModContext() {
        this.modContext = new ModContext((Context)this.contextHolder.getContext(), this.resourceManager, this.contextHolder.getExecutionDir());
        this.modRepository = new DirectoryRepository(new File(this.directory, "native_mods"));
        (this.modList = new ModList(this.modContext, this.contextHolder.getTaskManager(), this.contextHolder.getTemporaryStorage())).addModRepository(this.modRepository);
        this.modContext.addEventReceiver("injectAll", new ModContext.EventReceiver() {
            @Override
            public void onEvent(final Mod... mods) {
                final ExecutionDirectory executionDir = Pack.this.contextHolder.getExecutionDir();
                for (final LibraryDirectory nativeDir : Pack.this.bootNativeLibraries) {
                    executionDir.addLibraryDirectory(nativeDir);
                }
                Pack.this.invokeEnvironmentClassMethod("prepareForInjection", new Class[] { Pack.class }, new Object[] { Pack.this }, false);
                final List<LibraryDirectory> additionalNativeDirectories = new ArrayList<LibraryDirectory>();
                Pack.this.invokeEnvironmentClassMethod("getAdditionalNativeDirectories", new Class[] { Pack.class, additionalNativeDirectories.getClass() }, new Object[] { Pack.this, additionalNativeDirectories }, false);
                for (final LibraryDirectory nativeDir2 : additionalNativeDirectories) {
                    executionDir.addLibraryDirectory(nativeDir2);
                }
                final List<JavaDirectory> additionalJavaDirectories = new ArrayList<JavaDirectory>();
                Pack.this.invokeEnvironmentClassMethod("getAdditionalJavaDirectories", new Class[] { Pack.class, additionalJavaDirectories.getClass() }, new Object[] { Pack.this, additionalJavaDirectories }, false);
                for (final JavaDirectory javaDir : additionalJavaDirectories) {
                    executionDir.addJavaDirectory(javaDir);
                }
                final List<ResourceDirectory> additionalResourceDirectories = new ArrayList<ResourceDirectory>();
                Pack.this.invokeEnvironmentClassMethod("getAdditionalResourceDirectories", new Class[] { Pack.class, additionalResourceDirectories.getClass() }, new Object[] { Pack.this, additionalResourceDirectories }, false);
                for (final ResourceDirectory resourceDir : additionalResourceDirectories) {
                    Pack.this.resourceManager.addResourceDirectory(resourceDir);
                }
                Pack.this.invokeEnvironmentClassMethod("injectIntoModContext", new Class[] { Pack.class }, new Object[] { Pack.this }, false);
            }
        });
    }
    
    private void initializeBootJavaDirs() {
        JavaCompilerHolder.initializeForContext(this.modContext, this.contextHolder.getTaskManager());
        final File allJavaDirs = new File(this.directory, "java");
        if (allJavaDirs.exists() && allJavaDirs.isDirectory()) {
            List<String> order = this.loadOrderFile(new File(allJavaDirs, "order.txt"));
            if (order == null) {
                order = new ArrayList<String>();
                for (final File javaDirFile : allJavaDirs.listFiles()) {
                    if (javaDirFile.isDirectory()) {
                        order.add(javaDirFile.getName());
                    }
                }
            }
            this.bootJavaLibraries.clear();
            for (final String javaDirName : order) {
                final File javaDirFile2 = new File(allJavaDirs, javaDirName);
                if (javaDirFile2.isDirectory()) {
                    final JavaDirectory javaDirectory = new JavaDirectory(null, javaDirFile2);
                    this.bootJavaLibraries.add(javaDirectory.addToExecutionDirectory(null, (Context)this.contextHolder.getContext()));
                }
            }
        }
    }
    
    private void initializeNativeDirectories() {
        CompilerHolder.initializeForContext(this.contextHolder.getContext(), this.contextHolder.getTaskManager());
        final File allNativeDirs = new File(this.directory, "native");
        if (allNativeDirs.exists() && allNativeDirs.isDirectory()) {
            this.bootNativeLibraries.clear();
            for (final File nativeDirFile : allNativeDirs.listFiles()) {
                if (nativeDirFile.isDirectory()) {
                    final LibraryDirectory nativeDirectory = new LibraryDirectory(nativeDirFile);
                    this.bootNativeLibraries.add(nativeDirectory);
                }
            }
        }
    }
    
    private void invokeEnvironmentClassMethod(final String name, final Class[] parameterTypes, final Object[] parameters, final boolean mustBePresent) {
        final ClassLoader loader = this.getClass().getClassLoader();
        for (final PackManifest.ClassInfo environmentClass : this.manifest.environmentClasses) {
            final Class clazz = environmentClass.getDeclaredClass(loader);
            try {
                ReflectionHelper.invokeMethod(null, clazz, name, parameterTypes, parameters);
                Logger.debug("Pack", "environment class " + clazz + " called method " + name);
            }
            catch (NoSuchMethodException exception) {
                if (mustBePresent) {
                    throw new RuntimeException("environment class " + clazz + " missing required method " + name, exception);
                }
                continue;
            }
        }
    }
    
    public void initialize() {
        this.initializeAssetsAndResources();
        this.initializeModContext();
        this.initializeBootJavaDirs();
        this.initializeSharedObjects();
        this.initializeNativeDirectories();
        this.loadBootJavaLibraries();
        this.loadMenuActivityFactories();
        this.initializeAds();
    }
    
    private void loadBootJavaLibraries() {
        for (final JavaLibrary library : this.bootJavaLibraries) {
            library.initialize();
        }
        final ClassLoader loader = this.getClass().getClassLoader();
        for (final PackManifest.ClassInfo classInfo : this.manifest.environmentClasses) {
            classInfo.getDeclaredClass(loader);
        }
        for (final PackManifest.ClassInfo classInfo : this.manifest.activities) {
            classInfo.getDeclaredClass(loader);
        }
    }
    
    @SuppressLint({ "UnsafeDynamicallyLoadedCode" })
    private void loadSharedObjects() {
        for (final File soFile : this.executableSoFiles) {
            System.load(soFile.getAbsolutePath());
        }
    }
    
    private void loadResourceManager() {
        this.invokeEnvironmentClassMethod("setupResourceManager", new Class[] { ResourceManager.class }, new Object[] { this.resourceManager }, false);
    }
    
    private void loadNativeDirectories() {
        final File environmentLibsDir = Environment.getPackExecutionDir((Context)this.contextHolder.getContext());
        final List<File> environmentLibs = new ArrayList<File>();
        this.invokeEnvironmentClassMethod("addEnvironmentLibraries", new Class[] { environmentLibs.getClass(), File.class }, new Object[] { environmentLibs, environmentLibsDir }, false);
        for (final File envLib : environmentLibs) {
            if (!envLib.exists()) {
                throw new RuntimeException("pack declared non-existing environment library: " + envLib.getAbsolutePath());
            }
            Logger.info("Pack", "added environment library: " + envLib.getName() + "  (" + envLib.getAbsolutePath() + ")");
        }
        final CompilerHolder compilerHolder = CompilerHolder.getInstance((Context)this.contextHolder.getContext());
        compilerHolder.clearEnvironmentLibraries();
        compilerHolder.addEnvironmentLibraries(environmentLibs);
    }
    
    private void loadMenuActivityFactories() {
        this.menuActivityFactories.clear();
        this.invokeEnvironmentClassMethod("addMenuActivities", new Class[] { Pack.class, this.menuActivityFactories.getClass() }, new Object[] { this, this.menuActivityFactories }, false);
        for (final MenuActivityFactory factory : this.menuActivityFactories) {
            factory.pack = this;
        }
    }
    
    public void load() {
        this.loadResourceManager();
        this.loadSharedObjects();
        this.loadNativeDirectories();
    }
    
    public void unload() {
        if (this.modContext != null) {
            this.modContext.clearModsAndContext();
        }
        this.modContext = null;
        this.resourceManager = null;
    }
    
    private void initializeAds() {
        final AdDistributionModel distributionModel = AdsManager.getInstance().getDistributionModel();
        distributionModel.removeDistributionNodes("pack-dev");
        distributionModel.addDistributionNode("root", 1.0, "pack-dev");
        this.invokeEnvironmentClassMethod("initializePackRelatedAds", new Class[] { Pack.class, AdsManager.class, AdDistributionModel.class }, new Object[] { this, AdsManager.getInstance(), distributionModel.getNodeDistributionModel("pack-dev") }, false);
    }
    
    public void abortLaunch() {
        this.isLaunchAborted = true;
    }
    
    private boolean abortIfRequired(final Activity launchingActivity, final Runnable onInterrupt) {
        if (this.isLaunchAborted) {
            if (onInterrupt != null) {
                onInterrupt.run();
            }
            if (!launchingActivity.isDestroyed() && !launchingActivity.isFinishing()) {
                launchingActivity.runOnUiThread((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText((Context)launchingActivity, 2131624033, 1).show();
                    }
                });
            }
            return true;
        }
        return false;
    }
    
    public void launch(final Activity launchingActivity, final String activityName, final Runnable onInterrupt) {
        final File assetDir = new File(this.directory, "assets");
        if (assetDir.exists() && assetDir.isDirectory()) {
            AssetPatch.setAssetDirectory(assetDir.getAbsolutePath());
        }
        else {
            AssetPatch.setAssetDirectory(null);
        }
        this.isLaunchAborted = false;
        this.invokeEnvironmentClassMethod("abortLaunchIfRequired", new Class[] { Pack.class }, new Object[] { this }, false);
        if (this.abortIfRequired(launchingActivity, onInterrupt)) {
            return;
        }
        this.modList.startLaunchTask(new Runnable() {
            @Override
            public void run() {
                Pack.this.invokeEnvironmentClassMethod("prepareForLaunch", new Class[] { Pack.class }, new Object[] { Pack.this }, false);
                if (Pack.this.abortIfRequired(launchingActivity, onInterrupt)) {
                    return;
                }
                final PackManifest.ClassInfo activityInfo = Pack.this.manifest.getActivityInfoForName(activityName);
                if (activityInfo != null) {
                    if (Pack.this.manifest.optClearActivityStack) {
                        final HorizonApplication application = HorizonApplication.getInstance();
                        if (application != null) {
                            final List<Activity> activityStack = new ArrayList<Activity>(HorizonApplication.getActivityStack());
                            for (final Activity activity : activityStack) {
                                activity.finish();
                                System.out.println("finished activity before launching pack: " + activity);
                            }
                        }
                    }
                    final Class activityClass = activityInfo.getDeclaredClass(this.getClass().getClassLoader());
                    if (activityClass == null) {
                        throw new RuntimeException("failed to get launching class for some reason");
                    }
                    System.out.println("launching pack activity");
                    System.out.println("pack activity class loader: " + activityClass.getClassLoader());
                    System.out.println("patched library class loader: " + JavaLibrary.class.getClassLoader());
                    final Intent launchIntent = new Intent((Context)launchingActivity, activityClass);
                    launchingActivity.startActivity(launchIntent);
                    final File baseApkZip = new File(Pack.this.directory.getAbsolutePath(), "base.apk.zip");
                    if (baseApkZip.exists()) {
                        AssetPatch.setRootOverrideDirectory(baseApkZip.getAbsolutePath());
                    }
                    else {
                        AssetPatch.setRootOverrideDirectory(null);
                    }
                    Pack.this.invokeEnvironmentClassMethod("onActivityStarted", new Class[] { Pack.class, String.class }, new Object[] { this, activityName }, false);
                }
            }
        }, onInterrupt);
    }
    
    public List<MenuActivityFactory> getMenuActivityFactories() {
        return this.menuActivityFactories;
    }
    
    public List<Drawable> getCustomDrawables(final String category) {
        final List<Drawable> drawables = new ArrayList<Drawable>();
        this.invokeEnvironmentClassMethod("addCustomDrawables", new Class[] { Pack.class, String.class, drawables.getClass() }, new Object[] { this, category, drawables }, false);
        return drawables;
    }
    
    public Drawable getRandomCustomDrawable(final String category) {
        final List<Drawable> drawables = this.getCustomDrawables(category);
        return (drawables.size() > 0) ? drawables.get((int)(Math.random() * drawables.size())) : null;
    }
    
    public void buildCustomMenuLayout(final View layout, final View button) {
        this.invokeEnvironmentClassMethod("buildCustomMenuLayout", new Class[] { View.class, View.class }, new Object[] { layout, button }, false);
    }
    
    public static class MenuActivityFactory
    {
        private Pack pack;
        
        public Pack getPack() {
            return this.pack;
        }
        
        public String getIconGraphics() {
            return null;
        }
        
        public Collection<Bitmap> getIconGraphicsBitmaps() {
            return null;
        }
        
        public String getMenuTitle() {
            return null;
        }
        
        public void onCreateLayout(final Activity context, final RelativeLayout root) {
        }
        
        public boolean onBackPressed() {
            return false;
        }
    }
}
