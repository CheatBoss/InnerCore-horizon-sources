package com.bumptech.glide.load.resource.file;

import com.bumptech.glide.load.*;
import com.bumptech.glide.load.engine.*;
import java.io.*;

public class FileToStreamDecoder<T> implements ResourceDecoder<File, T>
{
    private static final FileOpener DEFAULT_FILE_OPENER;
    private final FileOpener fileOpener;
    private ResourceDecoder<InputStream, T> streamDecoder;
    
    static {
        DEFAULT_FILE_OPENER = new FileOpener();
    }
    
    public FileToStreamDecoder(final ResourceDecoder<InputStream, T> resourceDecoder) {
        this(resourceDecoder, FileToStreamDecoder.DEFAULT_FILE_OPENER);
    }
    
    FileToStreamDecoder(final ResourceDecoder<InputStream, T> streamDecoder, final FileOpener fileOpener) {
        this.streamDecoder = streamDecoder;
        this.fileOpener = fileOpener;
    }
    
    @Override
    public Resource<T> decode(final File file, final int n, final int n2) throws IOException {
        InputStream open = null;
        try {
            final InputStream inputStream = open = this.fileOpener.open(file);
            final Resource<T> decode = this.streamDecoder.decode(inputStream, n, n2);
            if (inputStream != null) {
                try {
                    inputStream.close();
                    return decode;
                }
                catch (IOException ex) {
                    return decode;
                }
            }
            return decode;
        }
        finally {
            if (open != null) {
                try {
                    open.close();
                }
                catch (IOException ex2) {}
            }
        }
    }
    
    @Override
    public String getId() {
        return "";
    }
    
    static class FileOpener
    {
        public InputStream open(final File file) throws FileNotFoundException {
            return new FileInputStream(file);
        }
    }
}
