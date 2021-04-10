package com.zhekasmirnov.innercore.api.log;

import com.zhekasmirnov.innercore.utils.*;
import java.io.*;

public class LogWriter
{
    private String buffer;
    private final File file;
    
    public LogWriter(final File file) {
        this.buffer = "";
        this.file = file;
    }
    
    public void clear() {
        try {
            FileTools.writeFileText(this.file.getAbsolutePath(), "");
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void flush() {
        try {
            FileTools.addFileText(this.file.getAbsolutePath(), this.buffer);
            this.buffer = "";
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void logMsg(final LogType logType, final String s, final String s2) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.buffer);
        sb.append("[");
        sb.append(s);
        sb.append("] ");
        sb.append(s2);
        sb.append("\n");
        this.buffer = sb.toString();
        if (this.buffer.length() > 2048) {
            this.flush();
        }
    }
}
