package org.apache.http.entity.mime.content;

import java.io.*;

public class FileBody extends AbstractContentBody
{
    private final File file;
    
    public FileBody(final File file) {
        this(file, "application/octet-stream");
    }
    
    public FileBody(final File file, final String s) {
        super(s);
        if (file != null) {
            this.file = file;
            return;
        }
        throw new IllegalArgumentException("File may not be null");
    }
    
    @Override
    public String getCharset() {
        return null;
    }
    
    @Override
    public long getContentLength() {
        return this.file.length();
    }
    
    public File getFile() {
        return this.file;
    }
    
    @Override
    public String getFilename() {
        return this.file.getName();
    }
    
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(this.file);
    }
    
    @Override
    public String getTransferEncoding() {
        return "binary";
    }
    
    @Override
    public void writeTo(final OutputStream outputStream) throws IOException {
        if (outputStream != null) {
            final FileInputStream fileInputStream = new FileInputStream(this.file);
            try {
                final byte[] array = new byte[4096];
                while (true) {
                    final int read = fileInputStream.read(array);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(array, 0, read);
                }
                outputStream.flush();
                return;
            }
            finally {
                fileInputStream.close();
            }
        }
        throw new IllegalArgumentException("Output stream may not be null");
    }
    
    @Deprecated
    public void writeTo(final OutputStream outputStream, final int n) throws IOException {
        this.writeTo(outputStream);
    }
}
