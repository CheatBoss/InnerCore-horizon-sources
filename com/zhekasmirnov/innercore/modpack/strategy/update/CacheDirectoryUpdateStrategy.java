package com.zhekasmirnov.innercore.modpack.strategy.update;

import com.zhekasmirnov.horizon.util.*;
import java.io.*;

public class CacheDirectoryUpdateStrategy extends DirectoryUpdateStrategy
{
    @Override
    public void beginUpdate() throws IOException {
        FileUtils.clearFileTree(this.getAssignedDirectory().getLocation(), true);
        this.getAssignedDirectory().getLocation().mkdirs();
    }
    
    @Override
    public void finishUpdate() throws IOException {
    }
    
    @Override
    public void updateFile(final String s, final InputStream inputStream) throws IOException {
    }
}
