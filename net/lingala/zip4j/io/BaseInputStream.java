package net.lingala.zip4j.io;

import java.io.*;
import net.lingala.zip4j.unzip.*;

public abstract class BaseInputStream extends InputStream
{
    @Override
    public int available() throws IOException {
        return 0;
    }
    
    public UnzipEngine getUnzipEngine() {
        return null;
    }
    
    @Override
    public int read() throws IOException {
        return 0;
    }
    
    public void seek(final long n) throws IOException {
    }
}
