package com.zhekasmirnov.horizon.modloader.java;

import com.zhekasmirnov.horizon.modloader.mod.*;
import java.io.*;
import org.json.*;
import java.util.*;
import com.googlecode.dex2jar.v3.*;
import com.zhekasmirnov.horizon.modloader.*;
import android.content.*;
import com.zhekasmirnov.horizon.util.*;

public class JavaDirectory
{
    public final Mod mod;
    public final File directory;
    public final JavaLibraryManifest manifest;
    
    public JavaDirectory(final Mod mod, final File directory) {
        this.mod = mod;
        System.out.println("java dir=" + directory);
        if (!directory.isDirectory()) {
            throw new IllegalStateException("non-directory file passed to JavaDirectory constructor: " + directory);
        }
        this.directory = directory;
        JavaLibraryManifest manifest = null;
        try {
            manifest = new JavaLibraryManifest(new File(directory, "manifest"));
        }
        catch (IOException err) {
            throw new RuntimeException("failed to read java library manifest for " + directory, err);
        }
        catch (JSONException err2) {
            throw new RuntimeException("failed to read java library manifest for " + directory, (Throwable)err2);
        }
        this.manifest = manifest;
    }
    
    public String getName() {
        return this.directory.getName();
    }
    
    public File getSubDirectory(final String path, final boolean createIfRequired) {
        final File dir = new File(this.directory, path);
        if (!dir.exists()) {
            if (!createIfRequired) {
                return null;
            }
            dir.mkdirs();
        }
        if (!dir.isDirectory()) {
            return null;
        }
        return dir;
    }
    
    public File getDestinationDirectory() {
        return this.getSubDirectory(".build/classes", true);
    }
    
    public File getJarDirectory() {
        return this.getSubDirectory(".build/jar", true);
    }
    
    private static String makeSeparatedString(final List<File> files) {
        final StringBuilder string = new StringBuilder();
        for (final File src : files) {
            if (string.length() > 0) {
                string.append(':');
            }
            string.append(src.getAbsolutePath());
        }
        return string.toString();
    }
    
    public File getBuildDexFile() {
        final File buildDir = this.getSubDirectory(".build", true);
        return (buildDir != null) ? new File(buildDir, "build.dex") : null;
    }
    
    public File getCompiledDexFile() {
        return new File(this.directory, ".compiled.dex");
    }
    
    public String getSourceDirectories() {
        return makeSeparatedString(this.manifest.sourceDirs);
    }
    
    public String getLibraryPaths(final List<File> bootPaths) {
        final List<File> all = new ArrayList<File>();
        all.addAll(bootPaths);
        for (final File lib : this.manifest.libraryPaths) {
            if (lib.getName().endsWith(".dex")) {
                try {
                    final Dex2jar dex2jar = Dex2jar.from(lib);
                    final File jar = new File(lib.getAbsolutePath().replace(".dex", ".jar"));
                    dex2jar.to(jar);
                    all.add(jar);
                    continue;
                }
                catch (IOException e) {
                    throw new RuntimeException("Cannot create jar file of dex " + lib, e);
                }
            }
            all.add(lib);
        }
        all.addAll(this.manifest.libraryPaths);
        return makeSeparatedString(all);
    }
    
    public String[] getArguments() {
        return this.manifest.arguments;
    }
    
    public boolean isVerboseRequired() {
        return this.manifest.verbose;
    }
    
    public String[] getAllSourceFiles() {
        final ArrayList<String> javaFiles = new ArrayList<String>();
        for (final File sourcePath : this.manifest.sourceDirs) {
            this.getAllSourceFiles(javaFiles, sourcePath);
        }
        System.out.println("source size: " + javaFiles.size());
        final String[] sources = new String[javaFiles.size()];
        return javaFiles.toArray(sources);
    }
    
    private void getAllSourceFiles(final ArrayList<String> toAdd, final File parent) {
        if (!parent.exists()) {
            return;
        }
        for (final File child : parent.listFiles()) {
            if (child.isDirectory()) {
                this.getAllSourceFiles(toAdd, child);
            }
            else if (child.exists() && child.isFile() && child.getName().endsWith(".java")) {
                toAdd.add(child.getAbsolutePath());
            }
        }
    }
    
    public List<String> getBootClassNames() {
        return this.manifest.bootClasses;
    }
    
    public JavaLibrary addToExecutionDirectory(final ExecutionDirectory executionDirectory, final Context context) {
        File compiled = this.getCompiledClassesFile();
        JavaLibrary library;
        if (compiled.exists() && !compiled.isDirectory()) {
            final List<File> files = this.getCompiledClassesFiles();
            library = new JavaLibrary(this, files);
        }
        else {
            new JavaCompiler(context).compile(this);
            compiled = this.getCompiledDexFile();
            if (compiled.exists()) {
                library = new JavaLibrary(this, compiled);
            }
            else {
                final File build = this.getBuildDexFile();
                if (!build.exists()) {
                    throw new RuntimeException("failed to build library " + this + " for some reason");
                }
                library = new JavaLibrary(this, build);
            }
        }
        return library;
    }
    
    public void compileToClassesFile(final Context context) {
        new JavaCompiler(context).compile(this);
        final File compiled = this.getCompiledDexFile();
        if (compiled.exists()) {
            try {
                FileUtils.copy(compiled, this.getCompiledClassesFile());
                return;
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            throw new RuntimeException("failed to build library " + this + " for some reason");
        }
        throw new RuntimeException("failed to build library " + this + " for some reason");
    }
    
    public File getCompiledClassesFile() {
        return new File(this.directory, "classes.dex");
    }
    
    public List<File> getCompiledClassesFiles() {
        final String[] files = this.directory.list();
        final List<File> result = new ArrayList<File>(files.length);
        for (final String file : files) {
            if (file.matches("classes[0-9]*\\.dex")) {
                result.add(new File(this.directory, file));
            }
        }
        return result;
    }
    
    public boolean isInDevMode() {
        return this.getCompiledClassesFile().exists();
    }
    
    public void setPreCompiled(final boolean preCompiled) {
        FileUtils.setFileFlag(this.directory, "not_precompiled", !preCompiled);
    }
    
    public boolean isPreCompiled() {
        return !FileUtils.getFileFlag(this.directory, "not_precompiled");
    }
}
