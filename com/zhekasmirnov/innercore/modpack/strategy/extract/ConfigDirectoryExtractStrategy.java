package com.zhekasmirnov.innercore.modpack.strategy.extract;

import com.android.tools.r8.annotations.*;
import java.io.*;
import java.util.*;
import java.util.function.*;

@SynthesizedClassMap({ -$$Lambda$ConfigDirectoryExtractStrategy$nCTTs9-UwvB-y-nBjowc5u-ZMa0.class })
public class ConfigDirectoryExtractStrategy extends AllFilesDirectoryExtractStrategy
{
    @Override
    public List<File> getFilesToExtract() {
        final ArrayList<File> list = new ArrayList<File>();
        this.addAllRecursive(this.getAssignedDirectory().getLocation(), list, -$$Lambda$ConfigDirectoryExtractStrategy$nCTTs9-UwvB-y-nBjowc5u-ZMa0.INSTANCE);
        return list;
    }
}
