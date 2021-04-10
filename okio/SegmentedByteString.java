package okio;

import java.util.*;

final class SegmentedByteString extends ByteString
{
    final transient int[] directory;
    final transient byte[][] segments;
    
    SegmentedByteString(final Buffer buffer, final int n) {
        super(null);
        Util.checkOffsetAndCount(buffer.size, 0L, n);
        Segment segment = buffer.head;
        final int n2 = 0;
        int i;
        int n3;
        for (i = 0, n3 = 0; i < n; i += segment.limit - segment.pos, ++n3, segment = segment.next) {
            if (segment.limit == segment.pos) {
                throw new AssertionError((Object)"s.limit == s.pos");
            }
        }
        this.segments = new byte[n3][];
        this.directory = new int[n3 * 2];
        Segment segment2 = buffer.head;
        int n4 = 0;
        int j = n2;
        while (j < n) {
            this.segments[n4] = segment2.data;
            if ((j += segment2.limit - segment2.pos) > n) {
                j = n;
            }
            final int[] directory = this.directory;
            directory[n4] = j;
            directory[this.segments.length + n4] = segment2.pos;
            segment2.shared = true;
            ++n4;
            segment2 = segment2.next;
        }
    }
    
    private int segment(int binarySearch) {
        binarySearch = Arrays.binarySearch(this.directory, 0, this.segments.length, binarySearch + 1);
        if (binarySearch >= 0) {
            return binarySearch;
        }
        return ~binarySearch;
    }
    
    private ByteString toByteString() {
        return new ByteString(this.toByteArray());
    }
    
    @Override
    public String base64() {
        return this.toByteString().base64();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ByteString) {
            final ByteString byteString = (ByteString)o;
            if (byteString.size() == this.size() && this.rangeEquals(0, byteString, 0, this.size())) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public byte getByte(final int n) {
        Util.checkOffsetAndCount(this.directory[this.segments.length - 1], n, 1L);
        final int segment = this.segment(n);
        int n2;
        if (segment == 0) {
            n2 = 0;
        }
        else {
            n2 = this.directory[segment - 1];
        }
        final int[] directory = this.directory;
        final byte[][] segments = this.segments;
        return segments[segment][n - n2 + directory[segments.length + segment]];
    }
    
    @Override
    public int hashCode() {
        final int hashCode = this.hashCode;
        if (hashCode != 0) {
            return hashCode;
        }
        final int length = this.segments.length;
        int i = 0;
        int hashCode2 = 1;
        int n = 0;
        while (i < length) {
            final byte[] array = this.segments[i];
            final int[] directory = this.directory;
            final int n2 = directory[length + i];
            final int n3 = directory[i];
            for (int j = n2; j < n3 - n + n2; ++j) {
                hashCode2 = hashCode2 * 31 + array[j];
            }
            ++i;
            n = n3;
        }
        return this.hashCode = hashCode2;
    }
    
    @Override
    public String hex() {
        return this.toByteString().hex();
    }
    
    @Override
    public boolean rangeEquals(int n, final ByteString byteString, int n2, int i) {
        if (n < 0) {
            return false;
        }
        if (n > this.size() - i) {
            return false;
        }
        final int segment = this.segment(n);
        int n3 = n;
        int n4;
        int min;
        int[] directory;
        byte[][] segments;
        for (n = segment; i > 0; i -= min, ++n) {
            if (n == 0) {
                n4 = 0;
            }
            else {
                n4 = this.directory[n - 1];
            }
            min = Math.min(i, this.directory[n] - n4 + n4 - n3);
            directory = this.directory;
            segments = this.segments;
            if (!byteString.rangeEquals(n2, segments[n], n3 - n4 + directory[segments.length + n], min)) {
                return false;
            }
            n3 += min;
            n2 += min;
        }
        return true;
    }
    
    @Override
    public boolean rangeEquals(int n, final byte[] array, int n2, int i) {
        if (n < 0 || n > this.size() - i || n2 < 0) {
            return false;
        }
        if (n2 > array.length - i) {
            return false;
        }
        final int segment = this.segment(n);
        int n3 = n;
        int n4;
        int min;
        int[] directory;
        byte[][] segments;
        for (n = segment; i > 0; i -= min, ++n) {
            if (n == 0) {
                n4 = 0;
            }
            else {
                n4 = this.directory[n - 1];
            }
            min = Math.min(i, this.directory[n] - n4 + n4 - n3);
            directory = this.directory;
            segments = this.segments;
            if (!Util.arrayRangeEquals(segments[n], n3 - n4 + directory[segments.length + n], array, n2, min)) {
                return false;
            }
            n3 += min;
            n2 += min;
        }
        return true;
    }
    
    @Override
    public ByteString sha1() {
        return this.toByteString().sha1();
    }
    
    @Override
    public ByteString sha256() {
        return this.toByteString().sha256();
    }
    
    @Override
    public int size() {
        return this.directory[this.segments.length - 1];
    }
    
    @Override
    public ByteString substring(final int n, final int n2) {
        return this.toByteString().substring(n, n2);
    }
    
    @Override
    public ByteString toAsciiLowercase() {
        return this.toByteString().toAsciiLowercase();
    }
    
    @Override
    public byte[] toByteArray() {
        final int[] directory = this.directory;
        final byte[][] segments = this.segments;
        final byte[] array = new byte[directory[segments.length - 1]];
        final int length = segments.length;
        int i = 0;
        int n = 0;
        while (i < length) {
            final int[] directory2 = this.directory;
            final int n2 = directory2[length + i];
            final int n3 = directory2[i];
            System.arraycopy(this.segments[i], n2, array, n, n3 - n);
            ++i;
            n = n3;
        }
        return array;
    }
    
    @Override
    public String toString() {
        return this.toByteString().toString();
    }
    
    @Override
    public String utf8() {
        return this.toByteString().utf8();
    }
    
    @Override
    void write(final Buffer buffer) {
        final int length = this.segments.length;
        int i = 0;
        int n = 0;
        while (i < length) {
            final int[] directory = this.directory;
            final int n2 = directory[length + i];
            final int n3 = directory[i];
            final Segment head = new Segment(this.segments[i], n2, n2 + n3 - n, true, false);
            if (buffer.head == null) {
                head.prev = head;
                head.next = head;
                buffer.head = head;
            }
            else {
                buffer.head.prev.push(head);
            }
            ++i;
            n = n3;
        }
        buffer.size += n;
    }
}
