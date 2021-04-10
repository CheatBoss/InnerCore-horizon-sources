package okio;

import java.util.zip.*;
import java.io.*;

public final class GzipSource implements Source
{
    private final CRC32 crc;
    private final Inflater inflater;
    private final InflaterSource inflaterSource;
    private int section;
    private final BufferedSource source;
    
    public GzipSource(final Source source) {
        this.section = 0;
        this.crc = new CRC32();
        if (source != null) {
            this.inflater = new Inflater(true);
            this.source = Okio.buffer(source);
            this.inflaterSource = new InflaterSource(this.source, this.inflater);
            return;
        }
        throw new IllegalArgumentException("source == null");
    }
    
    private void checkEqual(final String s, final int n, final int n2) throws IOException {
        if (n2 == n) {
            return;
        }
        throw new IOException(String.format("%s: actual 0x%08x != expected 0x%08x", s, n2, n));
    }
    
    private void consumeHeader() throws IOException {
        this.source.require(10L);
        final byte byte1 = this.source.buffer().getByte(3L);
        final boolean b = (byte1 >> 1 & 0x1) == 0x1;
        if (b) {
            this.updateCrc(this.source.buffer(), 0L, 10L);
        }
        this.checkEqual("ID1ID2", 8075, this.source.readShort());
        this.source.skip(8L);
        if ((byte1 >> 2 & 0x1) == 0x1) {
            this.source.require(2L);
            if (b) {
                this.updateCrc(this.source.buffer(), 0L, 2L);
            }
            final short shortLe = this.source.buffer().readShortLe();
            final BufferedSource source = this.source;
            final long n = shortLe;
            source.require(n);
            if (b) {
                this.updateCrc(this.source.buffer(), 0L, n);
            }
            this.source.skip(n);
        }
        if ((byte1 >> 3 & 0x1) == 0x1) {
            final long index = this.source.indexOf((byte)0);
            if (index == -1L) {
                throw new EOFException();
            }
            if (b) {
                this.updateCrc(this.source.buffer(), 0L, index + 1L);
            }
            this.source.skip(index + 1L);
        }
        if ((byte1 >> 4 & 0x1) == 0x1) {
            final long index2 = this.source.indexOf((byte)0);
            if (index2 == -1L) {
                throw new EOFException();
            }
            if (b) {
                this.updateCrc(this.source.buffer(), 0L, index2 + 1L);
            }
            this.source.skip(index2 + 1L);
        }
        if (b) {
            this.checkEqual("FHCRC", this.source.readShortLe(), (short)this.crc.getValue());
            this.crc.reset();
        }
    }
    
    private void consumeTrailer() throws IOException {
        this.checkEqual("CRC", this.source.readIntLe(), (int)this.crc.getValue());
        this.checkEqual("ISIZE", this.source.readIntLe(), (int)this.inflater.getBytesWritten());
    }
    
    private void updateCrc(final Buffer buffer, long n, long n2) {
        Segment segment;
        long n3;
        for (segment = buffer.head; n >= segment.limit - segment.pos; segment = segment.next, n -= n3) {
            n3 = segment.limit - segment.pos;
        }
        while (n2 > 0L) {
            final int n4 = (int)(segment.pos + n);
            final int n5 = (int)Math.min(segment.limit - n4, n2);
            this.crc.update(segment.data, n4, n5);
            final long n6 = n5;
            segment = segment.next;
            n = 0L;
            n2 -= n6;
        }
    }
    
    @Override
    public void close() throws IOException {
        this.inflaterSource.close();
    }
    
    @Override
    public long read(final Buffer buffer, long read) throws IOException {
        if (read < 0L) {
            final StringBuilder sb = new StringBuilder();
            sb.append("byteCount < 0: ");
            sb.append(read);
            throw new IllegalArgumentException(sb.toString());
        }
        if (read == 0L) {
            return 0L;
        }
        if (this.section == 0) {
            this.consumeHeader();
            this.section = 1;
        }
        if (this.section == 1) {
            final long size = buffer.size;
            read = this.inflaterSource.read(buffer, read);
            if (read != -1L) {
                this.updateCrc(buffer, size, read);
                return read;
            }
            this.section = 2;
        }
        if (this.section != 2) {
            return -1L;
        }
        this.consumeTrailer();
        this.section = 3;
        if (this.source.exhausted()) {
            return -1L;
        }
        throw new IOException("gzip finished without exhausting source");
    }
    
    @Override
    public Timeout timeout() {
        return this.source.timeout();
    }
}
