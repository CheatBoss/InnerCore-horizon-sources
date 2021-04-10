package okio;

import java.nio.channels.*;
import java.io.*;
import java.nio.charset.*;

public interface BufferedSource extends ReadableByteChannel, Source
{
    Buffer buffer();
    
    boolean exhausted() throws IOException;
    
    long indexOf(final byte p0) throws IOException;
    
    InputStream inputStream();
    
    boolean rangeEquals(final long p0, final ByteString p1) throws IOException;
    
    byte readByte() throws IOException;
    
    byte[] readByteArray() throws IOException;
    
    byte[] readByteArray(final long p0) throws IOException;
    
    ByteString readByteString(final long p0) throws IOException;
    
    void readFully(final Buffer p0, final long p1) throws IOException;
    
    void readFully(final byte[] p0) throws IOException;
    
    long readHexadecimalUnsignedLong() throws IOException;
    
    int readInt() throws IOException;
    
    int readIntLe() throws IOException;
    
    long readLong() throws IOException;
    
    short readShort() throws IOException;
    
    short readShortLe() throws IOException;
    
    String readString(final Charset p0) throws IOException;
    
    String readUtf8LineStrict() throws IOException;
    
    String readUtf8LineStrict(final long p0) throws IOException;
    
    boolean request(final long p0) throws IOException;
    
    void require(final long p0) throws IOException;
    
    void skip(final long p0) throws IOException;
}
