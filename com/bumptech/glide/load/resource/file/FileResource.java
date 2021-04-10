package com.bumptech.glide.load.resource.file;

import com.bumptech.glide.load.resource.*;
import java.io.*;

public class FileResource extends SimpleResource<File>
{
    public FileResource(final File file) {
        super(file);
    }
}
