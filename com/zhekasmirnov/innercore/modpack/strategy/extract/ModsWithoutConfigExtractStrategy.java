package com.zhekasmirnov.innercore.modpack.strategy.extract;

import com.android.tools.r8.annotations.*;
import java.io.*;
import java.util.*;
import java.util.function.*;

@SynthesizedClassMap({ -$$Lambda$ModsWithoutConfigExtractStrategy$qqnuqbFL3_OxQOYN52aLAQ139oI.class })
public class ModsWithoutConfigExtractStrategy extends AllFilesDirectoryExtractStrategy
{
    @Override
    public List<File> getFilesToExtract() {
        final ArrayList<File> list = new ArrayList<File>();
        this.addAllRecursive(this.getAssignedDirectory().getLocation(), list, -$$Lambda$ModsWithoutConfigExtractStrategy$qqnuqbFL3_OxQOYN52aLAQ139oI.INSTANCE);
        return list;
    }
}
