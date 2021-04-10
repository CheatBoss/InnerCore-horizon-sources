package com.zhekasmirnov.innercore.api.mod.ui;

import com.zhekasmirnov.innercore.utils.*;
import com.zhekasmirnov.horizon.util.*;
import java.io.*;
import com.zhekasmirnov.innercore.api.log.*;

public class ItemModelCacheManager
{
    private static final ItemModelCacheManager singleton;
    private final File cacheRoot;
    private String currentCacheGroup;
    
    static {
        singleton = new ItemModelCacheManager();
    }
    
    private ItemModelCacheManager() {
        this.cacheRoot = new File(FileTools.DIR_WORK, "cache/item-models-new");
        this.currentCacheGroup = null;
        this.checkAndSetupRoot();
    }
    
    private void assureCacheDirectory(final File file, final String s) {
        if (file.isFile()) {
            file.delete();
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        if (s != null) {
            final File file2 = new File(file, ".lock");
            String trim = null;
            try {
                trim = FileUtils.readFileText(file2).trim();
            }
            catch (IOException ex) {}
            if (!s.equals(trim)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("cleaning up item model cache directory ");
                sb.append(file.getName());
                sb.append(", new lock = ");
                sb.append(s);
                sb.append(", stored lock = ");
                sb.append(trim);
                ICLog.d("ItemModelCache", sb.toString());
                FileUtils.clearFileTree(file, false);
                try {
                    FileUtils.writeFileText(file2, s);
                }
                catch (IOException ex2) {}
            }
        }
        final File file3 = new File(file, ".nomedia");
        if (!file3.exists()) {
            try {
                file3.createNewFile();
            }
            catch (IOException ex3) {}
        }
    }
    
    private void checkAndSetupRoot() {
        this.assureCacheDirectory(this.cacheRoot, null);
    }
    
    public static ItemModelCacheManager getSingleton() {
        return ItemModelCacheManager.singleton;
    }
    
    public File getCacheGroupDirectory(final String s) {
        return new File(this.cacheRoot, s);
    }
    
    public File getCachePath(final String s, final String s2) {
        if (s != null && s.length() > 0) {
            final File cacheGroupDirectory = this.getCacheGroupDirectory(s);
            if (!cacheGroupDirectory.exists()) {
                this.assureCacheDirectory(cacheGroupDirectory, null);
            }
            return new File(new File(this.cacheRoot, s), s2);
        }
        return new File(this.cacheRoot, s2);
    }
    
    public String getCurrentCacheGroup() {
        return this.currentCacheGroup;
    }
    
    public void setCurrentCacheGroup(final String currentCacheGroup, final String s) {
        if (currentCacheGroup != null) {
            this.assureCacheDirectory(this.getCacheGroupDirectory(currentCacheGroup), s);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("set current cache group ");
        sb.append(currentCacheGroup);
        ICLog.d("ItemModelCache", sb.toString());
        this.currentCacheGroup = currentCacheGroup;
    }
}
