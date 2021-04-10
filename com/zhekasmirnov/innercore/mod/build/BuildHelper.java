package com.zhekasmirnov.innercore.mod.build;

import java.util.*;
import com.zhekasmirnov.innercore.utils.*;
import com.zhekasmirnov.innercore.api.log.*;
import java.io.*;

public class BuildHelper
{
    private static void addFileTree(final File file, final ArrayList<File> list) {
        if (file.isFile()) {
            if (!".includes".equals(file.getName())) {
                list.add(file);
            }
        }
        else if (file.isDirectory()) {
            final File[] listFiles = file.listFiles();
            for (int length = listFiles.length, i = 0; i < length; ++i) {
                addFileTree(listFiles[i], list);
            }
        }
    }
    
    public static void buildDir(final File file, final File file2, final File file3) {
        if (file.exists()) {
            if (file.isDirectory()) {
                try {
                    final ArrayList<File> includesFile = readIncludesFile(file);
                    final StringBuilder sb = new StringBuilder();
                    sb.append("/*\nBUILD INFO:\n  dir: ");
                    sb.append(FileTools.getPrettyPath(file3, file));
                    sb.append("\n  target: ");
                    sb.append(FileTools.getPrettyPath(file3, file2));
                    sb.append("\n  files: ");
                    sb.append(includesFile.size());
                    sb.append("\n*/\n\n\n\n");
                    String s = sb.toString();
                    for (int i = 0; i < includesFile.size(); ++i) {
                        final File file4 = includesFile.get(i);
                        final String fileText = FileTools.readFileText(file4.getAbsolutePath());
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append(s);
                        sb2.append("// file: ");
                        sb2.append(FileTools.getPrettyPath(file, file4));
                        sb2.append("\n\n");
                        sb2.append(fileText);
                        sb2.append("\n\n\n\n");
                        s = sb2.toString();
                    }
                    FileTools.assureFileDir(file2);
                    FileTools.writeFileText(file2.getAbsolutePath(), s);
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("directory build succeeded: dir=");
                    sb3.append(FileTools.getPrettyPath(file3, file));
                    sb3.append(" target=");
                    sb3.append(FileTools.getPrettyPath(file3, file2));
                    ICLog.d("INNERCORE-MOD-BUILD", sb3.toString());
                    return;
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                    return;
                }
            }
        }
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("failed to build dir ");
        sb4.append(FileTools.getPrettyPath(file3, file));
        sb4.append(" it does not exist or not a directory.");
        ICLog.d("INNERCORE-MOD-BUILD", sb4.toString());
    }
    
    public static void buildDir(final String s, final BuildConfig.BuildableDir buildableDir) {
        buildDir(new File(s, buildableDir.dir), new File(s, buildableDir.targetSource), new File(s));
    }
    
    public static ArrayList<File> readIncludesFile(final File file) throws IOException {
        final String[] split = FileTools.readFileText(new File(file, ".includes").getAbsolutePath()).split("\n");
        final ArrayList<File> list = new ArrayList<File>();
        for (int i = 0; i < split.length; ++i) {
            final String trim = split[i].trim();
            if (trim.length() != 0 && !trim.startsWith("//")) {
                if (!trim.startsWith("#")) {
                    final File file2 = new File(file, trim);
                    if (file2.exists()) {
                        if (file2.isFile()) {
                            list.add(file2);
                        }
                        else {
                            if (!trim.endsWith("/.")) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("directories in .includes should end with '/.' - ");
                                sb.append(trim);
                                ICLog.i("ERROR", sb.toString());
                            }
                            addFileTree(file2, list);
                        }
                    }
                    else {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("failed to include file due it does not exist: ");
                        sb2.append(trim);
                        ICLog.d("INNERCORE-MOD-BUILD", sb2.toString());
                    }
                }
            }
        }
        return list;
    }
}
