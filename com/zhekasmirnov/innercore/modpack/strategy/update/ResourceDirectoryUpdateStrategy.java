package com.zhekasmirnov.innercore.modpack.strategy.update;

import com.zhekasmirnov.horizon.util.*;
import com.zhekasmirnov.innercore.utils.*;
import java.io.*;

public class ResourceDirectoryUpdateStrategy extends DirectoryUpdateStrategy
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
    public void updateFile(final String s, InputStream t) throws IOException {
        final File file = new File(this.getAssignedDirectory().getLocation(), s);
        file.getParentFile().mkdirs();
        final FileOutputStream fileOutputStream = new FileOutputStream(file);
        try {
            FileTools.inStreamToOutStream((InputStream)t, fileOutputStream);
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            return;
        }
        catch (Throwable t) {
            try {
                throw t;
            }
            finally {}
        }
        finally {
            t = null;
        }
        if (fileOutputStream != null) {
            if (t != null) {
                try {
                    fileOutputStream.close();
                }
                catch (Throwable t2) {}
            }
            else {
                fileOutputStream.close();
            }
        }
        throw s;
    }
}
