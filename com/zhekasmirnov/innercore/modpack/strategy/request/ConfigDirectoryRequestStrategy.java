package com.zhekasmirnov.innercore.modpack.strategy.request;

import java.io.*;
import java.util.function.*;
import java.util.*;

public class ConfigDirectoryRequestStrategy extends DirectoryRequestStrategy
{
    private static String normalizeFileName(final String s) {
        return s.replaceAll("[\\\\/\\s]", "-");
    }
    
    protected void addAllRecursive(final File file, final List<File> list, final Predicate<File> predicate) {
        if (file.isDirectory()) {
            final File[] listFiles = file.listFiles();
            if (listFiles != null) {
                for (int length = listFiles.length, i = 0; i < length; ++i) {
                    this.addAllRecursive(listFiles[i], list, predicate);
                }
            }
            return;
        }
        if (file.isFile() && (predicate == null || predicate.test(file))) {
            list.add(file);
        }
    }
    
    @Override
    public File get(final String s) {
        return this.getAssignedDirectory().getLocation();
    }
    
    @Override
    public File get(String normalizeFileName, final String s) {
        normalizeFileName = normalizeFileName(normalizeFileName);
        if (s.startsWith("config/")) {
            return new File(new File(this.getAssignedDirectory().getLocation(), normalizeFileName), s.substring(7));
        }
        final File location = this.getAssignedDirectory().getLocation();
        final StringBuilder sb = new StringBuilder();
        sb.append(normalizeFileName);
        sb.append("-");
        sb.append(normalizeFileName(s));
        return new File(location, sb.toString());
    }
    
    @Override
    public List<File> getAll(final String s) {
        final ArrayList<File> list = new ArrayList<File>();
        final File[] listFiles = this.getAssignedDirectory().getLocation().listFiles();
        if (listFiles != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(s.toLowerCase());
            sb.append("-");
            final String string = sb.toString();
            for (int length = listFiles.length, i = 0; i < length; ++i) {
                final File file = listFiles[i];
                if (file.isDirectory() && file.getName().equalsIgnoreCase(s)) {
                    this.addAllRecursive(file, list, null);
                }
                else if (file.isFile() && file.getName().toLowerCase().startsWith(string)) {
                    list.add(file);
                }
            }
        }
        return list;
    }
    
    @Override
    public List<String> getAllLocations() {
        final HashSet<String> set = new HashSet<String>();
        set.add("innercore");
        final File[] listFiles = this.getAssignedDirectory().getLocation().listFiles();
        if (listFiles != null) {
            for (int length = listFiles.length, i = 0; i < length; ++i) {
                final File file = listFiles[i];
                final String lowerCase = file.getName().toLowerCase();
                if (file.isDirectory() && !lowerCase.equals(".keep-unchanged")) {
                    set.add(lowerCase);
                }
                else if (file.isFile()) {
                    final int index = lowerCase.indexOf(45);
                    if (index != -1) {
                        set.add(lowerCase.substring(0, index));
                    }
                }
            }
        }
        return new ArrayList<String>(set);
    }
}
