package com.zhekasmirnov.innercore.modpack.installation;

import java.io.*;
import java.nio.channels.*;
import java.nio.*;

public abstract class ModPackExtractionTarget
{
    public abstract OutputStream write(final String p0) throws IOException;
    
    public void writeFile(String t, final File file) throws IOException {
        final OutputStream write = this.write((String)t);
        Object allocateDirect;
        final Object o = allocateDirect = null;
        try {
            final FileInputStream fileInputStream = new FileInputStream(file);
            try {
                final ReadableByteChannel channel = Channels.newChannel(fileInputStream);
                final WritableByteChannel channel2 = Channels.newChannel(write);
                allocateDirect = ByteBuffer.allocateDirect(16384);
                while (channel.read((ByteBuffer)allocateDirect) != -1) {
                    ((ByteBuffer)allocateDirect).flip();
                    channel2.write((ByteBuffer)allocateDirect);
                    ((ByteBuffer)allocateDirect).compact();
                }
                ((ByteBuffer)allocateDirect).flip();
                while (((Buffer)allocateDirect).hasRemaining()) {
                    channel2.write((ByteBuffer)allocateDirect);
                }
                if (fileInputStream != null) {
                    allocateDirect = o;
                    fileInputStream.close();
                }
                if (write != null) {
                    write.close();
                }
                return;
            }
            catch (Throwable t) {
                try {
                    throw t;
                }
                finally {
                    allocateDirect = t;
                    final Throwable t2;
                    t = t2;
                }
            }
            finally {
                allocateDirect = null;
            }
            if (fileInputStream != null) {
                if (allocateDirect != null) {
                    allocateDirect = o;
                    try {
                        fileInputStream.close();
                    }
                    catch (Throwable t3) {}
                }
                else {
                    allocateDirect = o;
                    fileInputStream.close();
                }
            }
            allocateDirect = o;
            throw t;
        }
        catch (Throwable t4) {}
        finally {
            if (write != null) {
                if (allocateDirect != null) {
                    try {
                        write.close();
                    }
                    catch (Throwable t5) {}
                }
                else {
                    write.close();
                }
            }
        }
    }
}
