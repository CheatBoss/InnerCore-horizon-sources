package com.zhekasmirnov.horizon.modloader.library;

import com.zhekasmirnov.horizon.modloader.mod.*;
import java.io.*;
import org.json.*;
import com.zhekasmirnov.horizon.compiler.packages.*;
import com.zhekasmirnov.horizon.util.*;
import java.util.*;
import com.zhekasmirnov.horizon.modloader.*;
import android.content.*;
import com.zhekasmirnov.horizon.compiler.holder.*;
import com.zhekasmirnov.horizon.compiler.*;
import com.zhekasmirnov.horizon.runtime.logger.*;

public class LibraryDirectory
{
    public final Mod mod;
    public final File directory;
    public final LibraryManifest manifest;
    public final LibraryMakeFile makeFile;
    public final File soFile;
    private File executableFile;
    private Library libraryInstance;
    
    public LibraryDirectory(final Mod mod, final File directory) {
        this.executableFile = null;
        this.libraryInstance = null;
        this.mod = mod;
        if (!directory.isDirectory()) {
            throw new IllegalStateException("non-directory file passed to LibraryDirectory constructor: " + directory);
        }
        this.directory = directory;
        LibraryManifest manifest = null;
        try {
            manifest = new LibraryManifest(new File(directory, "manifest"));
        }
        catch (IOException err) {
            throw new RuntimeException("failed to read library manifest for " + directory, err);
        }
        catch (JSONException err2) {
            throw new RuntimeException("failed to read library manifest for " + directory, (Throwable)err2);
        }
        this.manifest = manifest;
        this.makeFile = new LibraryMakeFile(new File(directory, "make.txt"));
        this.soFile = this.findSharedObjectFile();
    }
    
    private File findSharedObjectFile() {
        final File noAbiFile = new File(this.directory, this.manifest.getSoName());
        if (noAbiFile.exists() && noAbiFile.isFile()) {
            return noAbiFile;
        }
        final File sharedObjectDir = new File(this.directory, "so");
        for (final String abi : Environment.getSupportedABIs()) {
            final File abiDir = new File(sharedObjectDir, abi);
            if (abiDir.exists() && abiDir.isDirectory()) {
                final File sharedObject = new File(abiDir, this.manifest.getSoName());
                if (sharedObject.exists() && sharedObject.isFile()) {
                    return sharedObject;
                }
            }
        }
        return noAbiFile;
    }
    
    public LibraryDirectory(final File directory) {
        this(null, directory);
    }
    
    public boolean isInDevMode() {
        return !this.soFile.exists();
    }
    
    public boolean isPreCompiled() {
        return !FileUtils.getFileFlag(this.directory, "not_precompiled");
    }
    
    public boolean isSharedLibrary() {
        return this.manifest.isSharedLibrary();
    }
    
    public int getVersionCode() {
        return this.manifest.getVersion();
    }
    
    public String getName() {
        return this.manifest.getName();
    }
    
    public String getSoFileName() {
        return this.soFile.getName();
    }
    
    public List<File> getIncludeDirs() {
        final List<String> names = this.manifest.getInclude();
        final List<File> dirs = new ArrayList<File>();
        for (final String name : names) {
            dirs.add(new File(this.directory, name));
        }
        return dirs;
    }
    
    public List<String> getDependencyNames() {
        return this.manifest.getDependencies();
    }
    
    public File getExecutableFile() {
        return this.executableFile;
    }
    
    public Library getLibrary() {
        return this.libraryInstance;
    }
    
    public void compileToTargetFile(final ExecutionDirectory directory, final Context context, final File target) {
        final CompilerHolder compilerHolder = CompilerHolder.getInstance(context);
        if (compilerHolder == null) {
            throw new RuntimeException("failed to compile " + this.getName() + ": no compiler holder found for context " + context);
        }
        final LibraryCompiler compiler = new LibraryCompiler(compilerHolder, this, target);
        compiler.initialize(context, directory);
        final CommandResult result = compiler.compile(context);
        if (result.getResultCode() != 0) {
            if (target.exists()) {
                target.delete();
            }
            throw new RuntimeException("failed to compile " + this.getName() + ": " + result.getMessage());
        }
    }
    
    public void setPreCompiled(final boolean preCompiled) {
        FileUtils.setFileFlag(this.directory, "not_precompiled", !preCompiled);
    }
    
    public void addToExecutionDirectory(final ExecutionDirectory directory, final Context context, final File target) {
        this.executableFile = target;
        if (this.soFile.exists()) {
            try {
                FileUtils.copy(this.soFile, target);
                return;
            }
            catch (IOException e) {
                throw new RuntimeException("failed to deploy " + this.getName() + ": " + e.toString(), e);
            }
        }
        this.compileToTargetFile(directory, context, target);
    }
    
    public void loadExecutableFile() {
        if (this.executableFile != null) {
            if (this.mod != null && !this.mod.getSafetyInterface().beginUnsafeSection()) {
                Logger.error("LibraryDirectory", "failed to create crash lock for some reason: mod=" + this.mod);
            }
            this.libraryInstance = Library.load(this.executableFile.getAbsolutePath());
            if (this.mod != null && !this.mod.getSafetyInterface().endUnsafeSection()) {
                Logger.error("LibraryDirectory", "failed to remove crash lock for some reason: mod=" + this.mod);
            }
            return;
        }
        throw new IllegalStateException("trying to load library directory with no execution file (dir = " + this.directory + ")");
    }
    
    @Override
    public int hashCode() {
        return this.directory.getAbsolutePath().hashCode();
    }
}
