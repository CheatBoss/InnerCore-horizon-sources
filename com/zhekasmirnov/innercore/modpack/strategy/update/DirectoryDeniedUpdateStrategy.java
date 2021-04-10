package com.zhekasmirnov.innercore.modpack.strategy.update;

import java.io.*;

public class DirectoryDeniedUpdateStrategy extends DirectoryUpdateStrategy
{
    @Override
    public void beginUpdate() throws IOException {
        final StringBuilder sb = new StringBuilder();
        sb.append("update denied for directory ");
        sb.append(this.getAssignedDirectory());
        sb.append(", following calls will be ignored");
        throw new IOException(sb.toString());
    }
    
    @Override
    public void finishUpdate() throws IOException {
    }
    
    @Override
    public void updateFile(final String s, final InputStream inputStream) throws IOException {
    }
}
