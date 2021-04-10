package com.zhekasmirnov.horizon.modloader.java;

import android.content.*;
import com.zhekasmirnov.horizon.util.*;
import com.android.dx.command.dexer.*;
import java.io.*;
import java.util.*;

public class JavaCompiler
{
    private final Context context;
    private final JavaCompilerHolder holder;
    
    public JavaCompiler(final Context ctx) {
        this.context = ctx;
        this.holder = JavaCompilerHolder.getInstance(this.context);
    }
    
    private void getClassFiles(final List<File> files, final File file) {
        if (file.isDirectory()) {
            for (final File f : file.listFiles()) {
                this.getClassFiles(files, f);
            }
        }
        else {
            files.add(file);
        }
    }
    
    public boolean compile(final JavaDirectory directory) {
        final JavaCompilerArguments args = new JavaCompilerArguments(new String[0]);
        final File destinationDirectory = directory.getDestinationDirectory();
        FileUtils.clearFileTree(destinationDirectory, false);
        System.out.println("BOOT PATHS: " + directory.getLibraryPaths(this.holder.getBootFiles()));
        args.add(directory.isVerboseRequired() ? "-verbose" : "-verbose");
        args.add("-nowarn");
        args.add("-bootclasspath", directory.getLibraryPaths(this.holder.getBootFiles()));
        args.add(directory.getArguments());
        args.add("-proc:none");
        args.add("-source", "1.7");
        args.add("-d", destinationDirectory.getAbsolutePath());
        args.add(directory.getAllSourceFiles());
        System.out.println("javac args=" + args);
        if (this.holder.compile(args)) {
            System.out.println("compilation complete, installing libraries...");
            this.holder.installLibraries(directory.manifest.libraryPaths, destinationDirectory);
            final List<File> classFiles = new ArrayList<File>();
            this.getClassFiles(classFiles, destinationDirectory);
            System.out.println("got " + classFiles.size() + " class files, adding...");
            final List<String> classFilePaths = new ArrayList<String>();
            for (final File classFile : classFiles) {
                classFilePaths.add(classFile.getAbsolutePath());
            }
            final Main.Arguments arguments = new Main.Arguments();
            arguments.fileNames = classFilePaths.toArray(new String[classFilePaths.size()]);
            arguments.outName = directory.getBuildDexFile().getAbsolutePath();
            arguments.jarOutput = true;
            arguments.strictNameCheck = false;
            arguments.verbose = true;
            try {
                System.out.println("Dexing java library: " + directory);
                Main.run(arguments);
                System.out.println("Dexing java library complete: " + directory);
                return true;
            }
            catch (IOException err) {
                throw new RuntimeException("error occurred while building dex file for " + directory, err);
            }
        }
        return false;
    }
}
