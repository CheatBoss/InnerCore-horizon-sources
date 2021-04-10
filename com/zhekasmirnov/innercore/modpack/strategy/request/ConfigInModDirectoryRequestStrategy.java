package com.zhekasmirnov.innercore.modpack.strategy.request;

import com.zhekasmirnov.horizon.util.*;
import java.io.*;
import java.util.*;

public class ConfigInModDirectoryRequestStrategy extends DirectoryRequestStrategy
{
    @Override
    public File get(String s) {
        s = (String)new File(this.getAssignedDirectory().getLocation(), s);
        final File file = new File((File)s, ".redirect");
        try {
            final File file2 = new File(FileUtils.readFileText(file).trim());
            if (file2.isDirectory()) {
                return file2;
            }
            return (File)s;
        }
        catch (IOException ex) {
            return (File)s;
        }
    }
    
    @Override
    public File get(final String s, final String s2) {
        if (s.equalsIgnoreCase("innercore") && s2.equalsIgnoreCase("ids.json")) {
            return new File(this.getAssignedDirectory().getLocation(), ".staticids");
        }
        return new File(this.get(s), s2);
    }
    
    @Override
    public List<File> getAll(final String s) {
        final ArrayList<Object> list = new ArrayList<Object>();
        final File[] listFiles = this.get(s).listFiles();
        if (listFiles != null) {
            Collections.addAll(list, listFiles);
        }
        if ("innercore".equalsIgnoreCase(s)) {
            list.add(this.get("innercore", "ids.json"));
        }
        return (List<File>)list;
    }
    
    @Override
    public List<String> getAllLocations() {
        final HashSet<String> set = new HashSet<String>();
        set.add("innercore");
        final File[] listFiles = this.getAssignedDirectory().getLocation().listFiles();
        if (listFiles != null) {
            for (int length = listFiles.length, i = 0; i < length; ++i) {
                final File file = listFiles[i];
                if (file.isDirectory()) {
                    set.add(file.getName());
                }
            }
        }
        return new ArrayList<String>(set);
    }
}
