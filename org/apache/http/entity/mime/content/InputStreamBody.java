package org.apache.http.entity.mime.content;

import java.io.*;

public class InputStreamBody extends AbstractContentBody
{
    private final String filename;
    private final InputStream in;
    
    public InputStreamBody(final InputStream inputStream, final String s) {
        this(inputStream, "application/octet-stream", s);
    }
    
    public InputStreamBody(final InputStream in, final String s, final String filename) {
        super(s);
        if (in != null) {
            this.in = in;
            this.filename = filename;
            return;
        }
        throw new IllegalArgumentException("Input stream may not be null");
    }
    
    @Override
    public String getCharset() {
        return null;
    }
    
    @Override
    public long getContentLength() {
        return -1L;
    }
    
    @Override
    public String getFilename() {
        return this.filename;
    }
    
    public InputStream getInputStream() {
        return this.in;
    }
    
    @Override
    public String getTransferEncoding() {
        return "binary";
    }
    
    @Override
    public void writeTo(final OutputStream outputStream) throws IOException {
        if (outputStream != null) {
            try {
                final byte[] array = new byte[4096];
                while (true) {
                    final int read = this.in.read(array);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(array, 0, read);
                }
                outputStream.flush();
                return;
            }
            finally {
                this.in.close();
            }
        }
        throw new IllegalArgumentException("Output stream may not be null");
    }
    
    @Deprecated
    public void writeTo(final OutputStream outputStream, final int n) throws IOException {
        this.writeTo(outputStream);
    }
}
