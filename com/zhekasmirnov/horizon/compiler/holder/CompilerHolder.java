package com.zhekasmirnov.horizon.compiler.holder;

import android.content.*;
import android.app.*;
import com.zhekasmirnov.horizon.compiler.packages.*;
import java.io.*;
import com.zhekasmirnov.horizon.compiler.*;
import com.zhekasmirnov.horizon.runtime.task.*;
import java.util.*;

public class CompilerHolder
{
    private static final HashMap<Context, CompilerHolder> instances;
    private final Activity context;
    private final CompilerInstaller installer;
    private final File installationDir;
    private final File downloadDir;
    private final List<File> environmentLibraries;
    private boolean isInitializing;
    private boolean isInitialized;
    private InstallationStatus installationStatus;
    
    public static CompilerHolder getInstance(final Context ctx) {
        synchronized (CompilerHolder.instances) {
            return CompilerHolder.instances.get(ctx);
        }
    }
    
    public static void initializeForContext(final Activity ctx, final TaskManager taskManager) {
        if (getInstance((Context)ctx) == null) {
            synchronized (CompilerHolder.instances) {
                final CompilerHolder instance = new CompilerHolder(ctx);
                CompilerHolder.instances.put((Context)ctx, instance);
                taskManager.addTask(instance.getInitializationTask());
            }
        }
    }
    
    public CompilerHolder(final Activity context) {
        this.environmentLibraries = new ArrayList<File>();
        this.isInitializing = false;
        this.isInitialized = false;
        this.installationStatus = InstallationStatus.NOT_INSTALLED;
        this.context = context;
        this.installationDir = new File(Environment.getToolchainsDir((Context)context));
        if (!this.installationDir.exists()) {
            this.installationDir.mkdirs();
        }
        if (!this.installationDir.isDirectory()) {
            throw new RuntimeException("failed to allocate installation directory " + this.installationDir);
        }
        this.downloadDir = new File(this.installationDir.getParentFile(), "compiler-download");
        if (!this.downloadDir.exists()) {
            this.downloadDir.mkdirs();
        }
        if (!this.downloadDir.isDirectory()) {
            throw new RuntimeException("failed to allocate download directory " + this.downloadDir);
        }
        this.installer = new CompilerInstaller(context, this.downloadDir);
    }
    
    private boolean getInstallationLock() {
        return new File(this.downloadDir, ".first-installation-lock").exists();
    }
    
    private boolean createInstallationLock() {
        try {
            return new File(this.downloadDir, ".first-installation-lock").createNewFile();
        }
        catch (IOException e) {
            return false;
        }
    }
    
    private boolean isGCCCommandAvailable() {
        final CommandResult result = Shell.exec((Context)this.context, this.downloadDir.getAbsolutePath(), "g++-4.9");
        System.out.println("test command: " + result);
        return result.getResultCode() == 0 || result.getResultCode() == 1;
    }
    
    private void initialize() {
        if (this.getInstallationLock()) {
            if (this.isGCCCommandAvailable()) {
                this.installationStatus = InstallationStatus.INSTALLED;
            }
            else {
                final Throwable result = this.installer.runSilentInstallationTasks(2);
                if (result != null || !this.installer.areInstallationPackagesFound()) {
                    this.installationStatus = InstallationStatus.CORRUPT;
                }
                else {
                    this.installationStatus = InstallationStatus.DOWNLOADED;
                }
            }
        }
        else {
            this.installationStatus = InstallationStatus.NOT_INSTALLED;
        }
    }
    
    public Task getInitializationTask() {
        return new Task() {
            @Override
            public Object getLock() {
                return "initialize_compiler";
            }
            
            @Override
            public void run() {
                if (!CompilerHolder.this.isInitializing) {
                    CompilerHolder.this.isInitializing = true;
                    if (!CompilerHolder.this.isInitialized) {
                        CompilerHolder.this.initialize();
                        CompilerHolder.this.isInitialized = true;
                    }
                    CompilerHolder.this.isInitializing = false;
                }
            }
            
            @Override
            public String getDescription() {
                return "initializing compiler";
            }
        };
    }
    
    public InstallationStatus getInstallationStatus() {
        return this.installationStatus;
    }
    
    private void awaitInitialization() {
        while (!this.isInitialized) {
            Thread.yield();
        }
    }
    
    private CompilerInstaller.InstallationStatus requestCompilerInstallation() {
        if (this.installationStatus == InstallationStatus.NOT_INSTALLED) {
            return this.installer.runInstallationSequence(false);
        }
        if (this.installationStatus == InstallationStatus.DOWNLOADED || this.installationStatus == InstallationStatus.CORRUPT) {
            return this.installer.runInstallationSequence(true);
        }
        return CompilerInstaller.InstallationStatus.FAILED;
    }
    
    public CommandResult execute(final Context context, final String directory, final String command) {
        this.awaitInitialization();
        if (this.installationStatus != InstallationStatus.INSTALLED) {
            final CompilerInstaller.InstallationStatus result = this.requestCompilerInstallation();
            System.out.println("installation result: " + result);
            if (result == CompilerInstaller.InstallationStatus.COMPLETED) {
                this.installationStatus = InstallationStatus.INSTALLED;
                this.createInstallationLock();
            }
        }
        return Shell.exec(context, directory, command);
    }
    
    public List<File> getEnvironmentLibraries() {
        return this.environmentLibraries;
    }
    
    public void clearEnvironmentLibraries() {
        this.environmentLibraries.clear();
    }
    
    public void addEnvironmentLibraries(final Collection<File> libraries) {
        this.environmentLibraries.addAll(libraries);
    }
    
    static {
        instances = new HashMap<Context, CompilerHolder>();
    }
    
    enum InstallationStatus
    {
        NOT_INSTALLED, 
        DOWNLOADED, 
        INSTALLED, 
        CORRUPT;
    }
}
