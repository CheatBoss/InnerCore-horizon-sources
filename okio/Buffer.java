package okio;

import java.nio.channels.*;
import javax.annotation.*;
import java.nio.*;
import java.nio.charset.*;
import java.io.*;

public final class Buffer implements Cloneable, ByteChannel, BufferedSink, BufferedSource
{
    private static final byte[] DIGITS;
    @Nullable
    Segment head;
    long size;
    
    static {
        DIGITS = new byte[] { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102 };
    }
    
    @Override
    public Buffer buffer() {
        return this;
    }
    
    public void clear() {
        try {
            this.skip(this.size);
        }
        catch (EOFException ex) {
            throw new AssertionError((Object)ex);
        }
    }
    
    public Buffer clone() {
        final Buffer buffer = new Buffer();
        if (this.size == 0L) {
            return buffer;
        }
        final Segment sharedCopy = this.head.sharedCopy();
        buffer.head = sharedCopy;
        sharedCopy.prev = sharedCopy;
        sharedCopy.next = sharedCopy;
        Segment segment = this.head;
        while (true) {
            segment = segment.next;
            if (segment == this.head) {
                break;
            }
            buffer.head.prev.push(segment.sharedCopy());
        }
        buffer.size = this.size;
        return buffer;
    }
    
    @Override
    public void close() {
    }
    
    public long completeSegmentByteCount() {
        final long size = this.size;
        if (size == 0L) {
            return 0L;
        }
        final Segment prev = this.head.prev;
        long n = size;
        if (prev.limit < 8192) {
            n = size;
            if (prev.owner) {
                n = size - (prev.limit - prev.pos);
            }
        }
        return n;
    }
    
    public Buffer copyTo(final Buffer buffer, long n, final long n2) {
        if (buffer == null) {
            throw new IllegalArgumentException("out == null");
        }
        Util.checkOffsetAndCount(this.size, n, n2);
        if (n2 == 0L) {
            return this;
        }
        buffer.size += n2;
        Segment segment = this.head;
        Segment next;
        long n3;
        long n4;
        while (true) {
            next = segment;
            n3 = n;
            n4 = n2;
            if (n < segment.limit - segment.pos) {
                break;
            }
            final long n5 = segment.limit - segment.pos;
            segment = segment.next;
            n -= n5;
        }
        while (n4 > 0L) {
            final Segment sharedCopy = next.sharedCopy();
            sharedCopy.pos += (int)n3;
            sharedCopy.limit = Math.min(sharedCopy.pos + (int)n4, sharedCopy.limit);
            final Segment head = buffer.head;
            if (head == null) {
                sharedCopy.prev = sharedCopy;
                sharedCopy.next = sharedCopy;
                buffer.head = sharedCopy;
            }
            else {
                head.prev.push(sharedCopy);
            }
            n = sharedCopy.limit - sharedCopy.pos;
            next = next.next;
            n3 = 0L;
            n4 -= n;
        }
        return this;
    }
    
    @Override
    public BufferedSink emit() {
        return this;
    }
    
    @Override
    public Buffer emitCompleteSegments() {
        return this;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Buffer)) {
            return false;
        }
        final Buffer buffer = (Buffer)o;
        final long size = this.size;
        if (size != buffer.size) {
            return false;
        }
        long n = 0L;
        if (size == 0L) {
            return true;
        }
        Segment segment = this.head;
        Segment segment2 = buffer.head;
        int n2 = segment.pos;
        int n3 = segment2.pos;
        while (n < this.size) {
            final long n4 = Math.min(segment.limit - n2, segment2.limit - n3);
            for (int n5 = 0; n5 < n4; ++n5, ++n2, ++n3) {
                if (segment.data[n2] != segment2.data[n3]) {
                    return false;
                }
            }
            if (n2 == segment.limit) {
                segment = segment.next;
                n2 = segment.pos;
            }
            if (n3 == segment2.limit) {
                segment2 = segment2.next;
                n3 = segment2.pos;
            }
            n += n4;
        }
        return true;
    }
    
    @Override
    public boolean exhausted() {
        return this.size == 0L;
    }
    
    @Override
    public void flush() {
    }
    
    public byte getByte(long n) {
        Util.checkOffsetAndCount(this.size, n, 1L);
        final long size = this.size;
        if (size - n > n) {
            Segment segment = this.head;
            while (true) {
                final long n2 = segment.limit - segment.pos;
                if (n < n2) {
                    break;
                }
                segment = segment.next;
                n -= n2;
            }
            return segment.data[segment.pos + (int)n];
        }
        n -= size;
        Segment segment2 = this.head;
        do {
            segment2 = segment2.prev;
            n += segment2.limit - segment2.pos;
        } while (n < 0L);
        return segment2.data[segment2.pos + (int)n];
    }
    
    @Override
    public int hashCode() {
        Segment segment = this.head;
        if (segment == null) {
            return 0;
        }
        int n = 1;
        Segment segment2;
        int n2;
        do {
            int i = segment.pos;
            final int limit = segment.limit;
            n2 = n;
            while (i < limit) {
                n2 = n2 * 31 + segment.data[i];
                ++i;
            }
            segment2 = (segment = segment.next);
            n = n2;
        } while (segment2 != this.head);
        return n2;
    }
    
    @Override
    public long indexOf(final byte b) {
        return this.indexOf(b, 0L, Long.MAX_VALUE);
    }
    
    public long indexOf(final byte b, long n, long n2) {
        if (n < 0L || n2 < n) {
            throw new IllegalArgumentException(String.format("size=%s fromIndex=%s toIndex=%s", this.size, n, n2));
        }
        long size = this.size;
        if (n2 <= size) {
            size = n2;
        }
        if (n == size) {
            return -1L;
        }
        Segment segment = this.head;
        if (segment == null) {
            return -1L;
        }
        long size2 = this.size;
        Segment next;
        if (size2 - n < n) {
            while (true) {
                next = segment;
                n2 = size2;
                if (size2 <= n) {
                    break;
                }
                segment = segment.prev;
                size2 -= segment.limit - segment.pos;
            }
        }
        else {
            n2 = 0L;
            while (true) {
                final long n3 = segment.limit - segment.pos + n2;
                next = segment;
                if (n3 >= n) {
                    break;
                }
                segment = segment.next;
                n2 = n3;
            }
        }
        while (n2 < size) {
            final byte[] data = next.data;
            for (int n4 = (int)Math.min(next.limit, next.pos + size - n2), i = (int)(next.pos + n - n2); i < n4; ++i) {
                if (data[i] == b) {
                    return i - next.pos + n2;
                }
            }
            n = next.limit - next.pos;
            next = next.next;
            n = (n2 += n);
        }
        return -1L;
    }
    
    @Override
    public InputStream inputStream() {
        return new InputStream() {
            @Override
            public int available() {
                return (int)Math.min(Buffer.this.size, 2147483647L);
            }
            
            @Override
            public void close() {
            }
            
            @Override
            public int read() {
                if (Buffer.this.size > 0L) {
                    return Buffer.this.readByte() & 0xFF;
                }
                return -1;
            }
            
            @Override
            public int read(final byte[] array, final int n, final int n2) {
                return Buffer.this.read(array, n, n2);
            }
            
            @Override
            public String toString() {
                final StringBuilder sb = new StringBuilder();
                sb.append(Buffer.this);
                sb.append(".inputStream()");
                return sb.toString();
            }
        };
    }
    
    @Override
    public boolean isOpen() {
        return true;
    }
    
    @Override
    public boolean rangeEquals(final long n, final ByteString byteString) {
        return this.rangeEquals(n, byteString, 0, byteString.size());
    }
    
    public boolean rangeEquals(final long n, final ByteString byteString, final int n2, final int n3) {
        if (n < 0L || n2 < 0 || n3 < 0 || this.size - n < n3) {
            return false;
        }
        if (byteString.size() - n2 < n3) {
            return false;
        }
        for (int i = 0; i < n3; ++i) {
            if (this.getByte(i + n) != byteString.getByte(n2 + i)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int read(final ByteBuffer byteBuffer) throws IOException {
        final Segment head = this.head;
        if (head == null) {
            return -1;
        }
        final int min = Math.min(byteBuffer.remaining(), head.limit - head.pos);
        byteBuffer.put(head.data, head.pos, min);
        head.pos += min;
        this.size -= min;
        if (head.pos == head.limit) {
            this.head = head.pop();
            SegmentPool.recycle(head);
        }
        return min;
    }
    
    public int read(final byte[] array, final int n, int min) {
        Util.checkOffsetAndCount(array.length, n, min);
        final Segment head = this.head;
        if (head == null) {
            return -1;
        }
        min = Math.min(min, head.limit - head.pos);
        System.arraycopy(head.data, head.pos, array, n, min);
        head.pos += min;
        this.size -= min;
        if (head.pos == head.limit) {
            this.head = head.pop();
            SegmentPool.recycle(head);
        }
        return min;
    }
    
    @Override
    public long read(final Buffer buffer, final long n) {
        if (buffer == null) {
            throw new IllegalArgumentException("sink == null");
        }
        if (n < 0L) {
            final StringBuilder sb = new StringBuilder();
            sb.append("byteCount < 0: ");
            sb.append(n);
            throw new IllegalArgumentException(sb.toString());
        }
        final long size = this.size;
        if (size == 0L) {
            return -1L;
        }
        long n2 = n;
        if (n > size) {
            n2 = size;
        }
        buffer.write(this, n2);
        return n2;
    }
    
    public UnsafeCursor readAndWriteUnsafe(final UnsafeCursor unsafeCursor) {
        if (unsafeCursor.buffer == null) {
            unsafeCursor.buffer = this;
            unsafeCursor.readWrite = true;
            return unsafeCursor;
        }
        throw new IllegalStateException("already attached to a buffer");
    }
    
    @Override
    public byte readByte() {
        if (this.size == 0L) {
            throw new IllegalStateException("size == 0");
        }
        final Segment head = this.head;
        final int pos = head.pos;
        final int limit = head.limit;
        final byte[] data = head.data;
        final int pos2 = pos + 1;
        final byte b = data[pos];
        --this.size;
        if (pos2 == limit) {
            this.head = head.pop();
            SegmentPool.recycle(head);
            return b;
        }
        head.pos = pos2;
        return b;
    }
    
    @Override
    public byte[] readByteArray() {
        try {
            return this.readByteArray(this.size);
        }
        catch (EOFException ex) {
            throw new AssertionError((Object)ex);
        }
    }
    
    @Override
    public byte[] readByteArray(final long n) throws EOFException {
        Util.checkOffsetAndCount(this.size, 0L, n);
        if (n <= 2147483647L) {
            final byte[] array = new byte[(int)n];
            this.readFully(array);
            return array;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("byteCount > Integer.MAX_VALUE: ");
        sb.append(n);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public ByteString readByteString() {
        return new ByteString(this.readByteArray());
    }
    
    @Override
    public ByteString readByteString(final long n) throws EOFException {
        return new ByteString(this.readByteArray(n));
    }
    
    @Override
    public void readFully(final Buffer buffer, final long n) throws EOFException {
        final long size = this.size;
        if (size >= n) {
            buffer.write(this, n);
            return;
        }
        buffer.write(this, size);
        throw new EOFException();
    }
    
    @Override
    public void readFully(final byte[] array) throws EOFException {
        int read;
        for (int i = 0; i < array.length; i += read) {
            read = this.read(array, i, array.length - i);
            if (read == -1) {
                throw new EOFException();
            }
        }
    }
    
    @Override
    public long readHexadecimalUnsignedLong() {
        if (this.size != 0L) {
            long n = 0L;
            int n2 = 0;
            int n3 = 0;
            int n4;
            long n5;
            do {
                final Segment head = this.head;
                final byte[] data = head.data;
                int pos = head.pos;
                final int limit = head.limit;
                n4 = n2;
                n5 = n;
                int n6;
                while (true) {
                    n6 = n3;
                    if (pos >= limit) {
                        break;
                    }
                    final byte b = data[pos];
                    int n7;
                    if (b >= 48 && b <= 57) {
                        n7 = b - 48;
                    }
                    else {
                        int n8;
                        if (b >= 97 && b <= 102) {
                            n8 = b - 97;
                        }
                        else if (b >= 65 && b <= 70) {
                            n8 = b - 65;
                        }
                        else {
                            if (n4 != 0) {
                                n6 = 1;
                                break;
                            }
                            final StringBuilder sb = new StringBuilder();
                            sb.append("Expected leading [0-9a-fA-F] character but was 0x");
                            sb.append(Integer.toHexString(b));
                            throw new NumberFormatException(sb.toString());
                        }
                        n7 = n8 + 10;
                    }
                    if ((n5 & 0xF000000000000000L) != 0x0L) {
                        final Buffer writeByte = new Buffer().writeHexadecimalUnsignedLong(n5).writeByte((int)b);
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("Number too large: ");
                        sb2.append(writeByte.readUtf8());
                        throw new NumberFormatException(sb2.toString());
                    }
                    final long n9 = n7;
                    ++pos;
                    ++n4;
                    n5 = (n5 << 4 | n9);
                }
                if (pos == limit) {
                    this.head = head.pop();
                    SegmentPool.recycle(head);
                }
                else {
                    head.pos = pos;
                }
                if (n6 != 0) {
                    break;
                }
                n = n5;
                n2 = n4;
                n3 = n6;
            } while (this.head != null);
            this.size -= n4;
            return n5;
        }
        throw new IllegalStateException("size == 0");
    }
    
    @Override
    public int readInt() {
        if (this.size < 4L) {
            final StringBuilder sb = new StringBuilder();
            sb.append("size < 4: ");
            sb.append(this.size);
            throw new IllegalStateException(sb.toString());
        }
        final Segment head = this.head;
        final int pos = head.pos;
        final int limit = head.limit;
        if (limit - pos < 4) {
            return (this.readByte() & 0xFF) << 24 | (this.readByte() & 0xFF) << 16 | (this.readByte() & 0xFF) << 8 | (this.readByte() & 0xFF);
        }
        final byte[] data = head.data;
        final int n = pos + 1;
        final byte b = data[pos];
        final int n2 = n + 1;
        final byte b2 = data[n];
        final int n3 = n2 + 1;
        final byte b3 = data[n2];
        final int pos2 = n3 + 1;
        final int n4 = (b & 0xFF) << 24 | (b2 & 0xFF) << 16 | (b3 & 0xFF) << 8 | (data[n3] & 0xFF);
        this.size -= 4L;
        if (pos2 == limit) {
            this.head = head.pop();
            SegmentPool.recycle(head);
            return n4;
        }
        head.pos = pos2;
        return n4;
    }
    
    @Override
    public int readIntLe() {
        return Util.reverseBytesInt(this.readInt());
    }
    
    @Override
    public long readLong() {
        if (this.size < 8L) {
            final StringBuilder sb = new StringBuilder();
            sb.append("size < 8: ");
            sb.append(this.size);
            throw new IllegalStateException(sb.toString());
        }
        final Segment head = this.head;
        final int pos = head.pos;
        final int limit = head.limit;
        if (limit - pos < 8) {
            return ((long)this.readInt() & 0xFFFFFFFFL) << 32 | ((long)this.readInt() & 0xFFFFFFFFL);
        }
        final byte[] data = head.data;
        final int n = pos + 1;
        final long n2 = data[pos];
        final int n3 = n + 1;
        final long n4 = data[n];
        final int n5 = n3 + 1;
        final long n6 = data[n3];
        final int n7 = n5 + 1;
        final long n8 = data[n5];
        final int n9 = n7 + 1;
        final long n10 = data[n7];
        final int n11 = n9 + 1;
        final long n12 = data[n9];
        final int n13 = n11 + 1;
        final long n14 = data[n11];
        final int pos2 = n13 + 1;
        final long n15 = ((long)data[n13] & 0xFFL) | ((n14 & 0xFFL) << 8 | ((n2 & 0xFFL) << 56 | (n4 & 0xFFL) << 48 | (n6 & 0xFFL) << 40 | (n8 & 0xFFL) << 32 | (n10 & 0xFFL) << 24 | (n12 & 0xFFL) << 16));
        this.size -= 8L;
        if (pos2 == limit) {
            this.head = head.pop();
            SegmentPool.recycle(head);
            return n15;
        }
        head.pos = pos2;
        return n15;
    }
    
    @Override
    public short readShort() {
        if (this.size >= 2L) {
            final Segment head = this.head;
            final int pos = head.pos;
            final int limit = head.limit;
            int n;
            int n2;
            if (limit - pos < 2) {
                n = (this.readByte() & 0xFF) << 8;
                n2 = (this.readByte() & 0xFF);
            }
            else {
                final byte[] data = head.data;
                final int n3 = pos + 1;
                final byte b = data[pos];
                final int pos2 = n3 + 1;
                final byte b2 = data[n3];
                this.size -= 2L;
                if (pos2 == limit) {
                    this.head = head.pop();
                    SegmentPool.recycle(head);
                }
                else {
                    head.pos = pos2;
                }
                n = (b & 0xFF) << 8;
                n2 = (b2 & 0xFF);
            }
            return (short)(n | n2);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("size < 2: ");
        sb.append(this.size);
        throw new IllegalStateException(sb.toString());
    }
    
    @Override
    public short readShortLe() {
        return Util.reverseBytesShort(this.readShort());
    }
    
    public String readString(final long n, final Charset charset) throws EOFException {
        Util.checkOffsetAndCount(this.size, 0L, n);
        if (charset == null) {
            throw new IllegalArgumentException("charset == null");
        }
        if (n > 2147483647L) {
            final StringBuilder sb = new StringBuilder();
            sb.append("byteCount > Integer.MAX_VALUE: ");
            sb.append(n);
            throw new IllegalArgumentException(sb.toString());
        }
        if (n == 0L) {
            return "";
        }
        final Segment head = this.head;
        if (head.pos + n > head.limit) {
            return new String(this.readByteArray(n), charset);
        }
        final String s = new String(head.data, head.pos, (int)n, charset);
        head.pos += (int)n;
        this.size -= n;
        if (head.pos == head.limit) {
            this.head = head.pop();
            SegmentPool.recycle(head);
        }
        return s;
    }
    
    @Override
    public String readString(final Charset charset) {
        try {
            return this.readString(this.size, charset);
        }
        catch (EOFException ex) {
            throw new AssertionError((Object)ex);
        }
    }
    
    public String readUtf8() {
        try {
            return this.readString(this.size, Util.UTF_8);
        }
        catch (EOFException ex) {
            throw new AssertionError((Object)ex);
        }
    }
    
    public String readUtf8(final long n) throws EOFException {
        return this.readString(n, Util.UTF_8);
    }
    
    String readUtf8Line(long n) throws EOFException {
        final long n2 = 1L;
        while (true) {
            Label_0044: {
                if (n <= 0L) {
                    break Label_0044;
                }
                final long n3 = n - 1L;
                if (this.getByte(n3) != 13) {
                    break Label_0044;
                }
                final String s = this.readUtf8(n3);
                n = 2L;
                this.skip(n);
                return s;
            }
            final String s = this.readUtf8(n);
            n = n2;
            continue;
        }
    }
    
    @Override
    public String readUtf8LineStrict() throws EOFException {
        return this.readUtf8LineStrict(Long.MAX_VALUE);
    }
    
    @Override
    public String readUtf8LineStrict(final long n) throws EOFException {
        if (n < 0L) {
            final StringBuilder sb = new StringBuilder();
            sb.append("limit < 0: ");
            sb.append(n);
            throw new IllegalArgumentException(sb.toString());
        }
        long n2 = Long.MAX_VALUE;
        if (n != Long.MAX_VALUE) {
            n2 = n + 1L;
        }
        final long index = this.indexOf((byte)10, 0L, n2);
        if (index != -1L) {
            return this.readUtf8Line(index);
        }
        if (n2 < this.size() && this.getByte(n2 - 1L) == 13 && this.getByte(n2) == 10) {
            return this.readUtf8Line(n2);
        }
        final Buffer buffer = new Buffer();
        this.copyTo(buffer, 0L, Math.min(32L, this.size()));
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("\\n not found: limit=");
        sb2.append(Math.min(this.size(), n));
        sb2.append(" content=");
        sb2.append(buffer.readByteString().hex());
        sb2.append('\u2026');
        throw new EOFException(sb2.toString());
    }
    
    @Override
    public boolean request(final long n) {
        return this.size >= n;
    }
    
    @Override
    public void require(final long n) throws EOFException {
        if (this.size >= n) {
            return;
        }
        throw new EOFException();
    }
    
    public long size() {
        return this.size;
    }
    
    @Override
    public void skip(long n) throws EOFException {
        while (n > 0L) {
            final Segment head = this.head;
            if (head == null) {
                throw new EOFException();
            }
            final int n2 = (int)Math.min(n, head.limit - this.head.pos);
            final long size = this.size;
            final long n3 = n2;
            this.size = size - n3;
            final Segment head2 = this.head;
            head2.pos += n2;
            if (this.head.pos == this.head.limit) {
                final Segment head3 = this.head;
                this.head = head3.pop();
                SegmentPool.recycle(head3);
            }
            n -= n3;
        }
    }
    
    public ByteString snapshot() {
        final long size = this.size;
        if (size <= 2147483647L) {
            return this.snapshot((int)size);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("size > Integer.MAX_VALUE: ");
        sb.append(this.size);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public ByteString snapshot(final int n) {
        if (n == 0) {
            return ByteString.EMPTY;
        }
        return new SegmentedByteString(this, n);
    }
    
    @Override
    public Timeout timeout() {
        return Timeout.NONE;
    }
    
    @Override
    public String toString() {
        return this.snapshot().toString();
    }
    
    Segment writableSegment(final int n) {
        if (n < 1 || n > 8192) {
            throw new IllegalArgumentException();
        }
        final Segment head = this.head;
        if (head == null) {
            final Segment take = SegmentPool.take();
            this.head = take;
            take.prev = take;
            return take.next = take;
        }
        final Segment prev = head.prev;
        if (prev.limit + n <= 8192) {
            final Segment push = prev;
            if (prev.owner) {
                return push;
            }
        }
        return prev.push(SegmentPool.take());
    }
    
    @Override
    public int write(final ByteBuffer byteBuffer) throws IOException {
        if (byteBuffer != null) {
            int i;
            int n;
            Segment writableSegment;
            int min;
            for (n = (i = byteBuffer.remaining()); i > 0; i -= min, writableSegment.limit += min) {
                writableSegment = this.writableSegment(1);
                min = Math.min(i, 8192 - writableSegment.limit);
                byteBuffer.get(writableSegment.data, writableSegment.limit, min);
            }
            this.size += n;
            return n;
        }
        throw new IllegalArgumentException("source == null");
    }
    
    @Override
    public Buffer write(final ByteString byteString) {
        if (byteString != null) {
            byteString.write(this);
            return this;
        }
        throw new IllegalArgumentException("byteString == null");
    }
    
    @Override
    public Buffer write(final byte[] array) {
        if (array != null) {
            return this.write(array, 0, array.length);
        }
        throw new IllegalArgumentException("source == null");
    }
    
    @Override
    public Buffer write(final byte[] array, int i, int n) {
        if (array != null) {
            final long n2 = array.length;
            final long n3 = i;
            final long n4 = n;
            Util.checkOffsetAndCount(n2, n3, n4);
            Segment writableSegment;
            int min;
            for (n += i; i < n; i += min, writableSegment.limit += min) {
                writableSegment = this.writableSegment(1);
                min = Math.min(n - i, 8192 - writableSegment.limit);
                System.arraycopy(array, i, writableSegment.data, writableSegment.limit, min);
            }
            this.size += n4;
            return this;
        }
        throw new IllegalArgumentException("source == null");
    }
    
    @Override
    public void write(final Buffer buffer, long n) {
        if (buffer == null) {
            throw new IllegalArgumentException("source == null");
        }
        if (buffer != this) {
            Util.checkOffsetAndCount(buffer.size, 0L, n);
            while (n > 0L) {
                if (n < buffer.head.limit - buffer.head.pos) {
                    final Segment head = this.head;
                    Segment prev;
                    if (head != null) {
                        prev = head.prev;
                    }
                    else {
                        prev = null;
                    }
                    if (prev != null && prev.owner) {
                        final long n2 = prev.limit;
                        int pos;
                        if (prev.shared) {
                            pos = 0;
                        }
                        else {
                            pos = prev.pos;
                        }
                        if (n2 + n - pos <= 8192L) {
                            buffer.head.writeTo(prev, (int)n);
                            buffer.size -= n;
                            this.size += n;
                            return;
                        }
                    }
                    buffer.head = buffer.head.split((int)n);
                }
                final Segment head2 = buffer.head;
                final long n3 = head2.limit - head2.pos;
                buffer.head = head2.pop();
                final Segment head3 = this.head;
                if (head3 == null) {
                    this.head = head2;
                    head2.prev = head2;
                    head2.next = head2;
                }
                else {
                    head3.prev.push(head2).compact();
                }
                buffer.size -= n3;
                this.size += n3;
                n -= n3;
            }
            return;
        }
        throw new IllegalArgumentException("source == this");
    }
    
    @Override
    public long writeAll(final Source source) throws IOException {
        if (source != null) {
            long n = 0L;
            while (true) {
                final long read = source.read(this, 8192L);
                if (read == -1L) {
                    break;
                }
                n += read;
            }
            return n;
        }
        throw new IllegalArgumentException("source == null");
    }
    
    @Override
    public Buffer writeByte(final int n) {
        final Segment writableSegment = this.writableSegment(1);
        writableSegment.data[writableSegment.limit++] = (byte)n;
        ++this.size;
        return this;
    }
    
    @Override
    public Buffer writeDecimalLong(final long n) {
        if (n == 0L) {
            return this.writeByte(48);
        }
        boolean b = false;
        long n2 = n;
        if (n < 0L) {
            n2 = -n;
            if (n2 < 0L) {
                return this.writeUtf8("-9223372036854775808");
            }
            b = true;
        }
        int n3;
        if (n2 < 100000000L) {
            if (n2 < 10000L) {
                if (n2 < 100L) {
                    if (n2 < 10L) {
                        n3 = 1;
                    }
                    else {
                        n3 = 2;
                    }
                }
                else if (n2 < 1000L) {
                    n3 = 3;
                }
                else {
                    n3 = 4;
                }
            }
            else if (n2 < 1000000L) {
                if (n2 < 100000L) {
                    n3 = 5;
                }
                else {
                    n3 = 6;
                }
            }
            else if (n2 < 10000000L) {
                n3 = 7;
            }
            else {
                n3 = 8;
            }
        }
        else if (n2 < 1000000000000L) {
            if (n2 < 10000000000L) {
                if (n2 < 1000000000L) {
                    n3 = 9;
                }
                else {
                    n3 = 10;
                }
            }
            else if (n2 < 100000000000L) {
                n3 = 11;
            }
            else {
                n3 = 12;
            }
        }
        else if (n2 < 1000000000000000L) {
            if (n2 < 10000000000000L) {
                n3 = 13;
            }
            else if (n2 < 100000000000000L) {
                n3 = 14;
            }
            else {
                n3 = 15;
            }
        }
        else if (n2 < 100000000000000000L) {
            if (n2 < 10000000000000000L) {
                n3 = 16;
            }
            else {
                n3 = 17;
            }
        }
        else if (n2 < 1000000000000000000L) {
            n3 = 18;
        }
        else {
            n3 = 19;
        }
        int n4 = n3;
        if (b) {
            n4 = n3 + 1;
        }
        final Segment writableSegment = this.writableSegment(n4);
        final byte[] data = writableSegment.data;
        int n5 = writableSegment.limit + n4;
        while (n2 != 0L) {
            final int n6 = (int)(n2 % 10L);
            --n5;
            data[n5] = Buffer.DIGITS[n6];
            n2 /= 10L;
        }
        if (b) {
            data[n5 - 1] = 45;
        }
        writableSegment.limit += n4;
        this.size += n4;
        return this;
    }
    
    @Override
    public Buffer writeHexadecimalUnsignedLong(long n) {
        if (n == 0L) {
            return this.writeByte(48);
        }
        final int n2 = Long.numberOfTrailingZeros(Long.highestOneBit(n)) / 4 + 1;
        final Segment writableSegment = this.writableSegment(n2);
        final byte[] data = writableSegment.data;
        for (int i = writableSegment.limit + n2 - 1; i >= writableSegment.limit; --i) {
            data[i] = Buffer.DIGITS[(int)(n & 0xFL)];
            n >>>= 4;
        }
        writableSegment.limit += n2;
        this.size += n2;
        return this;
    }
    
    @Override
    public Buffer writeInt(final int n) {
        final Segment writableSegment = this.writableSegment(4);
        final byte[] data = writableSegment.data;
        final int limit = writableSegment.limit;
        final int n2 = limit + 1;
        data[limit] = (byte)(n >>> 24 & 0xFF);
        final int n3 = n2 + 1;
        data[n2] = (byte)(n >>> 16 & 0xFF);
        final int n4 = n3 + 1;
        data[n3] = (byte)(n >>> 8 & 0xFF);
        data[n4] = (byte)(n & 0xFF);
        writableSegment.limit = n4 + 1;
        this.size += 4L;
        return this;
    }
    
    public Buffer writeLong(final long n) {
        final Segment writableSegment = this.writableSegment(8);
        final byte[] data = writableSegment.data;
        final int limit = writableSegment.limit;
        final int n2 = limit + 1;
        data[limit] = (byte)(n >>> 56 & 0xFFL);
        final int n3 = n2 + 1;
        data[n2] = (byte)(n >>> 48 & 0xFFL);
        final int n4 = n3 + 1;
        data[n3] = (byte)(n >>> 40 & 0xFFL);
        final int n5 = n4 + 1;
        data[n4] = (byte)(n >>> 32 & 0xFFL);
        final int n6 = n5 + 1;
        data[n5] = (byte)(n >>> 24 & 0xFFL);
        final int n7 = n6 + 1;
        data[n6] = (byte)(n >>> 16 & 0xFFL);
        final int n8 = n7 + 1;
        data[n7] = (byte)(n >>> 8 & 0xFFL);
        data[n8] = (byte)(n & 0xFFL);
        writableSegment.limit = n8 + 1;
        this.size += 8L;
        return this;
    }
    
    @Override
    public Buffer writeShort(final int n) {
        final Segment writableSegment = this.writableSegment(2);
        final byte[] data = writableSegment.data;
        final int limit = writableSegment.limit;
        final int n2 = limit + 1;
        data[limit] = (byte)(n >>> 8 & 0xFF);
        data[n2] = (byte)(n & 0xFF);
        writableSegment.limit = n2 + 1;
        this.size += 2L;
        return this;
    }
    
    public Buffer writeString(final String s, final int n, final int n2, final Charset charset) {
        if (s == null) {
            throw new IllegalArgumentException("string == null");
        }
        if (n < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("beginIndex < 0: ");
            sb.append(n);
            throw new IllegalAccessError(sb.toString());
        }
        if (n2 < n) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("endIndex < beginIndex: ");
            sb2.append(n2);
            sb2.append(" < ");
            sb2.append(n);
            throw new IllegalArgumentException(sb2.toString());
        }
        if (n2 > s.length()) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("endIndex > string.length: ");
            sb3.append(n2);
            sb3.append(" > ");
            sb3.append(s.length());
            throw new IllegalArgumentException(sb3.toString());
        }
        if (charset == null) {
            throw new IllegalArgumentException("charset == null");
        }
        if (charset.equals(Util.UTF_8)) {
            return this.writeUtf8(s, n, n2);
        }
        final byte[] bytes = s.substring(n, n2).getBytes(charset);
        return this.write(bytes, 0, bytes.length);
    }
    
    public Buffer writeString(final String s, final Charset charset) {
        return this.writeString(s, 0, s.length(), charset);
    }
    
    @Override
    public Buffer writeUtf8(final String s) {
        return this.writeUtf8(s, 0, s.length());
    }
    
    public Buffer writeUtf8(final String s, int i, final int n) {
        if (s == null) {
            throw new IllegalArgumentException("string == null");
        }
        if (i < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("beginIndex < 0: ");
            sb.append(i);
            throw new IllegalArgumentException(sb.toString());
        }
        if (n < i) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("endIndex < beginIndex: ");
            sb2.append(n);
            sb2.append(" < ");
            sb2.append(i);
            throw new IllegalArgumentException(sb2.toString());
        }
        if (n <= s.length()) {
            while (i < n) {
                final char char1 = s.charAt(i);
                if (char1 < '\u0080') {
                    final Segment writableSegment = this.writableSegment(1);
                    final byte[] data = writableSegment.data;
                    final int n2 = writableSegment.limit - i;
                    final int min = Math.min(n, 8192 - n2);
                    data[i + n2] = (byte)char1;
                    char char2;
                    for (++i; i < min; ++i) {
                        char2 = s.charAt(i);
                        if (char2 >= '\u0080') {
                            break;
                        }
                        data[i + n2] = (byte)char2;
                    }
                    final int n3 = n2 + i - writableSegment.limit;
                    writableSegment.limit += n3;
                    this.size += n3;
                }
                else {
                    int n4;
                    if (char1 < '\u0800') {
                        n4 = (char1 >> 6 | 0xC0);
                    }
                    else if (char1 >= '\ud800' && char1 <= '\udfff') {
                        final int n5 = i + 1;
                        char char3;
                        if (n5 < n) {
                            char3 = s.charAt(n5);
                        }
                        else {
                            char3 = '\0';
                        }
                        if (char1 <= '\udbff' && char3 >= '\udc00' && char3 <= '\udfff') {
                            final int n6 = ((char1 & 0xFFFF27FF) << 10 | (0xFFFF23FF & char3)) + 65536;
                            this.writeByte(n6 >> 18 | 0xF0);
                            this.writeByte((n6 >> 12 & 0x3F) | 0x80);
                            this.writeByte((n6 >> 6 & 0x3F) | 0x80);
                            this.writeByte((n6 & 0x3F) | 0x80);
                            i += 2;
                            continue;
                        }
                        this.writeByte(63);
                        i = n5;
                        continue;
                    }
                    else {
                        this.writeByte(char1 >> 12 | 0xE0);
                        n4 = ((char1 >> 6 & 0x3F) | 0x80);
                    }
                    this.writeByte(n4);
                    this.writeByte((char1 & '?') | 0x80);
                    ++i;
                }
            }
            return this;
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("endIndex > string.length: ");
        sb3.append(n);
        sb3.append(" > ");
        sb3.append(s.length());
        throw new IllegalArgumentException(sb3.toString());
    }
    
    public Buffer writeUtf8CodePoint(final int n) {
        if (n < 128) {
            this.writeByte(n);
            return this;
        }
        int n2;
        if (n < 2048) {
            n2 = (n >> 6 | 0xC0);
        }
        else {
            int n3;
            if (n < 65536) {
                if (n >= 55296 && n <= 57343) {
                    this.writeByte(63);
                    return this;
                }
                n3 = (n >> 12 | 0xE0);
            }
            else {
                if (n > 1114111) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unexpected code point: ");
                    sb.append(Integer.toHexString(n));
                    throw new IllegalArgumentException(sb.toString());
                }
                this.writeByte(n >> 18 | 0xF0);
                n3 = ((n >> 12 & 0x3F) | 0x80);
            }
            this.writeByte(n3);
            n2 = ((n >> 6 & 0x3F) | 0x80);
        }
        this.writeByte(n2);
        this.writeByte((n & 0x3F) | 0x80);
        return this;
    }
    
    public static final class UnsafeCursor implements Closeable
    {
        public Buffer buffer;
        public byte[] data;
        public int end;
        public long offset;
        public boolean readWrite;
        private Segment segment;
        public int start;
        
        public UnsafeCursor() {
            this.offset = -1L;
            this.start = -1;
            this.end = -1;
        }
        
        @Override
        public void close() {
            if (this.buffer != null) {
                this.buffer = null;
                this.segment = null;
                this.offset = -1L;
                this.data = null;
                this.start = -1;
                this.end = -1;
                return;
            }
            throw new IllegalStateException("not attached to a buffer");
        }
        
        public int next() {
            if (this.offset == this.buffer.size) {
                throw new IllegalStateException();
            }
            final long offset = this.offset;
            if (offset == -1L) {
                return this.seek(0L);
            }
            return this.seek(offset + (this.end - this.start));
        }
        
        public int seek(final long n) {
            if (n < -1L || n > this.buffer.size) {
                throw new ArrayIndexOutOfBoundsException(String.format("offset=%s > size=%s", n, this.buffer.size));
            }
            if (n != -1L && n != this.buffer.size) {
                final long n2 = 0L;
                final long size = this.buffer.size;
                final Segment head = this.buffer.head;
                final Segment head2 = this.buffer.head;
                final Segment segment = this.segment;
                long n3 = n2;
                long n4 = size;
                Segment segment2 = head;
                Segment segment3 = head2;
                if (segment != null) {
                    n3 = this.offset - (this.start - segment.pos);
                    if (n3 > n) {
                        segment3 = this.segment;
                        n4 = n3;
                        n3 = n2;
                        segment2 = head;
                    }
                    else {
                        segment2 = this.segment;
                        segment3 = head2;
                        n4 = size;
                    }
                }
                long n5;
                Segment segment4;
                if (n4 - n > n - n3) {
                    Segment next = segment2;
                    while (true) {
                        n5 = n3;
                        segment4 = next;
                        if (n < next.limit - next.pos + n3) {
                            break;
                        }
                        final long n6 = next.limit - next.pos;
                        next = next.next;
                        n3 += n6;
                    }
                }
                else {
                    long n7 = n4;
                    while (true) {
                        n5 = n7;
                        segment4 = segment3;
                        if (n7 <= n) {
                            break;
                        }
                        segment3 = segment3.prev;
                        n7 -= segment3.limit - segment3.pos;
                    }
                }
                Segment push = segment4;
                if (this.readWrite) {
                    push = segment4;
                    if (segment4.shared) {
                        final Segment unsharedCopy = segment4.unsharedCopy();
                        if (this.buffer.head == segment4) {
                            this.buffer.head = unsharedCopy;
                        }
                        push = segment4.push(unsharedCopy);
                        push.prev.pop();
                    }
                }
                this.segment = push;
                this.offset = n;
                this.data = push.data;
                this.start = push.pos + (int)(n - n5);
                final int limit = push.limit;
                this.end = limit;
                return limit - this.start;
            }
            this.segment = null;
            this.offset = n;
            this.data = null;
            this.start = -1;
            return this.end = -1;
        }
    }
}
