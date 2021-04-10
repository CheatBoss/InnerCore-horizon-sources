package com.zhekasmirnov.horizon.modloader.java;

import com.zhekasmirnov.horizon.util.*;
import java.util.*;
import java.io.*;
import org.json.*;

public class JavaLibraryManifest
{
    private final File file;
    private final File directory;
    private final JSONObject content;
    public final String[] arguments;
    public final boolean verbose;
    public final List<File> sourceDirs;
    public final List<File> libraryDirs;
    public final List<File> libraryPaths;
    public final List<String> bootClasses;
    
    public JavaLibraryManifest(final File file) throws IOException, JSONException {
        this.sourceDirs = new ArrayList<File>();
        this.libraryDirs = new ArrayList<File>();
        this.libraryPaths = new ArrayList<File>();
        this.bootClasses = new ArrayList<String>();
        this.file = file;
        this.directory = file.getParentFile();
        this.content = FileUtils.readJSON(file);
        this.verbose = this.content.optBoolean("verbose");
        final JSONArray arguments = this.content.optJSONArray("options");
        if (arguments != null) {
            this.arguments = JSONUtils.toList(arguments).toArray(new String[arguments.length()]);
        }
        else {
            this.arguments = new String[0];
        }
        final JSONArray bootClasses = this.content.optJSONArray("boot-classes");
        if (bootClasses != null) {
            this.bootClasses.addAll((Collection<? extends String>)JSONUtils.toList(bootClasses));
        }
        final JSONArray sourceDirs = this.content.optJSONArray("source-dirs");
        if (sourceDirs != null) {
            for (final String path : JSONUtils.toList(sourceDirs)) {
                final File dir = new File(this.directory, path);
                if (dir.exists() && dir.isDirectory()) {
                    this.sourceDirs.add(dir);
                }
            }
        }
        final JSONArray libraryDirs = this.content.optJSONArray("library-dirs");
        if (libraryDirs != null) {
            for (final String path2 : JSONUtils.toList(libraryDirs)) {
                final File dir2 = new File(this.directory, path2);
                if (dir2.exists() && dir2.isDirectory()) {
                    this.libraryDirs.add(dir2);
                    for (final File lib : dir2.listFiles()) {
                        final String name = lib.getName();
                        if (!name.endsWith(".zip") && !name.endsWith(".jar") && !name.endsWith(".dex")) {
                            throw new IllegalArgumentException("illegal java library, it can be dex file, zip or jar archive: " + lib);
                        }
                        this.libraryPaths.add(lib);
                    }
                }
            }
        }
    }
}
