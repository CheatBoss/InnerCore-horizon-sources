package com.zhekasmirnov.innercore.modpack.strategy.extract;

import java.io.*;
import java.util.*;
import java.util.function.*;

public class ConfigFromModsExtractStrategy extends AllFilesDirectoryExtractStrategy
{
    private static String normalizeFileName(final String s) {
        return s.replaceAll("[\\\\/\\s]", "-");
    }
    
    @Override
    public String getEntryName(String substring, final File file) {
        if (substring.equalsIgnoreCase(".staticids")) {
            return "innercore-ids.json";
        }
        final int index = substring.indexOf(47);
        if (index == -1) {
            return substring;
        }
        final String normalizeFileName = normalizeFileName(substring.substring(0, index));
        substring = substring.substring(index + 1);
        if (substring.startsWith("config/")) {
            final StringBuilder sb = new StringBuilder();
            sb.append(normalizeFileName);
            sb.append("/");
            sb.append(substring.substring(7));
            return sb.toString();
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(normalizeFileName);
        sb2.append("-");
        sb2.append(normalizeFileName(substring));
        return sb2.toString();
    }
    
    @Override
    public List<File> getFilesToExtract() {
        final ArrayList<File> list = new ArrayList<File>();
        final File[] listFiles = this.getAssignedDirectory().getLocation().listFiles();
        if (listFiles != null) {
            for (int length = listFiles.length, i = 0; i < length; ++i) {
                final File file = listFiles[i];
                if (file.isDirectory()) {
                    final File[] listFiles2 = file.listFiles();
                    if (listFiles2 != null) {
                        for (int length2 = listFiles2.length, j = 0; j < length2; ++j) {
                            final File file2 = listFiles2[j];
                            if (file2.isFile() && file2.getName().toLowerCase().matches("config(.info)?.json")) {
                                list.add(file2);
                            }
                            else if (file2.isDirectory() && file2.getName().equalsIgnoreCase("config")) {
                                this.addAllRecursive(file2, list, null);
                            }
                        }
                    }
                }
                else if (file.isFile() && file.getName().equalsIgnoreCase(".staticids")) {
                    list.add(file);
                }
            }
        }
        return list;
    }
}
