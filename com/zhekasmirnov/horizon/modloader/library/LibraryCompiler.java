package com.zhekasmirnov.horizon.modloader.library;

import com.zhekasmirnov.horizon.compiler.holder.*;
import android.content.*;
import com.zhekasmirnov.horizon.modloader.*;
import com.zhekasmirnov.horizon.compiler.packages.*;
import java.util.*;
import com.zhekasmirnov.horizon.util.*;
import java.io.*;
import com.pdaxrom.utils.*;
import com.zhekasmirnov.horizon.compiler.*;
import android.content.res.*;

public class LibraryCompiler
{
    private static final String compiler = "g++-4.9 -std=c++11 -v";
    private final LibraryDirectory library;
    private final LibraryMakeFile make;
    private File target;
    private List<String> files;
    private List<String> includes;
    private List<String> dependencies;
    private final CompilerHolder compilerHolder;
    private static File builtInIncludeDirectory;
    
    public LibraryCompiler(final CompilerHolder compilerHolder, final LibraryDirectory library, final File target) {
        this.files = new ArrayList<String>();
        this.includes = new ArrayList<String>();
        this.dependencies = new ArrayList<String>();
        this.compilerHolder = compilerHolder;
        this.library = library;
        this.target = target;
        this.make = library.makeFile;
    }
    
    public LibraryCompiler(final CompilerHolder compilerHolder, final LibraryDirectory library) {
        this(compilerHolder, library, null);
    }
    
    private void addSourceFilesFrom(final File directory) {
        for (final File file : directory.listFiles()) {
            if (file.isDirectory()) {
                this.addSourceFilesFrom(file);
            }
            else {
                final String name = file.getName();
                if (name.endsWith(".cpp")) {
                    this.files.add(file.getAbsolutePath());
                }
            }
        }
    }
    
    public void initialize(final Context context, final ExecutionDirectory directory) {
        this.files.clear();
        this.includes.clear();
        this.dependencies.clear();
        this.dependencies.add("-L" + directory.directory.getAbsolutePath());
        this.dependencies.add("-L" + new File(Environment.getApplicationLibraryDirectory(), "libhorizon.so").getAbsolutePath());
        this.dependencies.add("-landroid");
        this.dependencies.add("-lm");
        this.dependencies.add("-llog");
        if (this.make.getFiles() != null) {
            this.files.addAll(this.make.getFiles());
        }
        else {
            this.addSourceFilesFrom(this.library.directory);
        }
        for (final File envLibrary : this.compilerHolder.getEnvironmentLibraries()) {
            this.dependencies.add("-l:" + new File("../../../../../../../../../../../", envLibrary.getAbsolutePath()));
        }
        final String globalExecDirPath = new File("../../../../../../../../../../../", directory.directory.getAbsolutePath()).getAbsolutePath();
        for (final String dependency : this.library.getDependencyNames()) {
            final File dependencyFile = new File(globalExecDirPath, "lib" + dependency + ".so");
            if (!dependencyFile.exists()) {
                this.compilePlaceholder(context, dependencyFile);
            }
            this.dependencies.add("-l:" + dependencyFile);
            final LibraryDirectory lib = directory.getLibByName(dependency);
            if (lib != null) {
                for (final File include : lib.getIncludeDirs()) {
                    this.includes.add("-I" + include.getAbsolutePath());
                }
            }
        }
    }
    
    private void compilePlaceholder(final Context context, final File file) {
        final File empty = new File(file.getParentFile(), ".placeholder.cpp");
        empty.delete();
        try {
            FileUtils.writeFileText(empty, "#define THIS_IS_A_PLACEHOLDER\n");
        }
        catch (IOException e) {
            throw new RuntimeException("failed to create empty file for placeholder compilation " + file, e);
        }
        final StringBuilder command = new StringBuilder().append("g++-4.9 -std=c++11 -v").append(" ");
        command.append(empty.getAbsolutePath()).append(" ");
        command.append("-shared -o ");
        command.append(file.getAbsolutePath()).append(" ");
        Utils.emptyDirectory(new File(Environment.getTmpExeDir(context)));
        Utils.emptyDirectory(new File(Environment.getSdCardTmpDir()));
        final CommandResult result = this.compilerHolder.execute(context, file.getParentFile().getAbsolutePath(), command.toString());
        empty.delete();
        System.out.println("COMPILER: compiled placeholder file " + file + " with result " + result.getResultCode() + " in " + result.getTime() + " ms");
    }
    
    public CommandResult compile(final Context context) {
        final StringBuilder command = new StringBuilder().append("g++-4.9 -std=c++11 -v").append(" ");
        for (final String file : this.files) {
            command.append(file).append(" ");
        }
        if (this.make.getCppFlags() != null) {
            command.append(this.make.getCppFlags()).append(" ");
        }
        command.append("-shared -o ");
        command.append(this.target.getAbsolutePath()).append(" ");
        command.append("-I").append(unpackBuiltInIncludesIfRequired(context)).append(" ");
        for (final String include : this.includes) {
            command.append(include).append(" ");
        }
        command.append("-L").append(new File(Environment.getDataDirFile(context), "lib").getAbsolutePath()).append(" ");
        for (final String dependency : this.dependencies) {
            command.append(dependency).append(" ");
        }
        Utils.emptyDirectory(new File(Environment.getTmpExeDir(context)));
        Utils.emptyDirectory(new File(Environment.getSdCardTmpDir()));
        return this.compilerHolder.execute(context, this.library.directory.getAbsolutePath(), command.toString());
    }
    
    private static synchronized String unpackBuiltInIncludesIfRequired(final Context context) {
        if (LibraryCompiler.builtInIncludeDirectory == null) {
            LibraryCompiler.builtInIncludeDirectory = new File(Environment.getDataDirFile(context), "includes/builtin");
            if (!LibraryCompiler.builtInIncludeDirectory.exists()) {
                LibraryCompiler.builtInIncludeDirectory.mkdirs();
            }
            FileUtils.clearFileTree(LibraryCompiler.builtInIncludeDirectory, false);
            final AssetManager assets = context.getAssets();
            try {
                FileUtils.unpackAssetOrDirectory(assets, LibraryCompiler.builtInIncludeDirectory, "includes");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return LibraryCompiler.builtInIncludeDirectory.getAbsolutePath();
    }
    
    static {
        LibraryCompiler.builtInIncludeDirectory = null;
    }
}
