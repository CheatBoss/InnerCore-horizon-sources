package com.bumptech.glide.load.resource.file;

import com.bumptech.glide.load.*;
import com.bumptech.glide.load.engine.*;
import java.io.*;

public class FileDecoder implements ResourceDecoder<File, File>
{
    @Override
    public Resource<File> decode(final File file, final int n, final int n2) {
        return new FileResource(file);
    }
    
    @Override
    public String getId() {
        return "";
    }
}
