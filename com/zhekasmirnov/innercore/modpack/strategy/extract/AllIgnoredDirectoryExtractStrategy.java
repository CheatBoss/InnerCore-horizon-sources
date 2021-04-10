package com.zhekasmirnov.innercore.modpack.strategy.extract;

import java.io.*;
import java.util.*;

public class AllIgnoredDirectoryExtractStrategy extends DirectoryExtractStrategy
{
    @Override
    public String getEntryName(final String s, final File file) {
        return null;
    }
    
    @Override
    public List<File> getFilesToExtract() {
        return new ArrayList<File>();
    }
}
