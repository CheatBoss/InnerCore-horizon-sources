package okhttp3.internal.http2;

import java.io.*;
import java.util.*;
import okio.*;
import okhttp3.internal.*;

final class Hpack
{
    static final Map<ByteString, Integer> NAME_TO_FIRST_INDEX;
    static final Header[] STATIC_HEADER_TABLE;
    
    static {
        STATIC_HEADER_TABLE = new Header[] { new Header(Header.TARGET_AUTHORITY, ""), new Header(Header.TARGET_METHOD, "GET"), new Header(Header.TARGET_METHOD, "POST"), new Header(Header.TARGET_PATH, "/"), new Header(Header.TARGET_PATH, "/index.html"), new Header(Header.TARGET_SCHEME, "http"), new Header(Header.TARGET_SCHEME, "https"), new Header(Header.RESPONSE_STATUS, "200"), new Header(Header.RESPONSE_STATUS, "204"), new Header(Header.RESPONSE_STATUS, "206"), new Header(Header.RESPONSE_STATUS, "304"), new Header(Header.RESPONSE_STATUS, "400"), new Header(Header.RESPONSE_STATUS, "404"), new Header(Header.RESPONSE_STATUS, "500"), new Header("accept-charset", ""), new Header("accept-encoding", "gzip, deflate"), new Header("accept-language", ""), new Header("accept-ranges", ""), new Header("accept", ""), new Header("access-control-allow-origin", ""), new Header("age", ""), new Header("allow", ""), new Header("authorization", ""), new Header("cache-control", ""), new Header("content-disposition", ""), new Header("content-encoding", ""), new Header("content-language", ""), new Header("content-length", ""), new Header("content-location", ""), new Header("content-range", ""), new Header("content-type", ""), new Header("cookie", ""), new Header("date", ""), new Header("etag", ""), new Header("expect", ""), new Header("expires", ""), new Header("from", ""), new Header("host", ""), new Header("if-match", ""), new Header("if-modified-since", ""), new Header("if-none-match", ""), new Header("if-range", ""), new Header("if-unmodified-since", ""), new Header("last-modified", ""), new Header("link", ""), new Header("location", ""), new Header("max-forwards", ""), new Header("proxy-authenticate", ""), new Header("proxy-authorization", ""), new Header("range", ""), new Header("referer", ""), new Header("refresh", ""), new Header("retry-after", ""), new Header("server", ""), new Header("set-cookie", ""), new Header("strict-transport-security", ""), new Header("transfer-encoding", ""), new Header("user-agent", ""), new Header("vary", ""), new Header("via", ""), new Header("www-authenticate", "") };
        NAME_TO_FIRST_INDEX = nameToFirstIndex();
    }
    
    static ByteString checkLowercase(final ByteString byteString) throws IOException {
        for (int size = byteString.size(), i = 0; i < size; ++i) {
            final byte byte1 = byteString.getByte(i);
            if (byte1 >= 65 && byte1 <= 90) {
                final StringBuilder sb = new StringBuilder();
                sb.append("PROTOCOL_ERROR response malformed: mixed case name: ");
                sb.append(byteString.utf8());
                throw new IOException(sb.toString());
            }
        }
        return byteString;
    }
    
    private static Map<ByteString, Integer> nameToFirstIndex() {
        final LinkedHashMap<ByteString, Integer> linkedHashMap = new LinkedHashMap<ByteString, Integer>(Hpack.STATIC_HEADER_TABLE.length);
        int n = 0;
        while (true) {
            final Header[] static_HEADER_TABLE = Hpack.STATIC_HEADER_TABLE;
            if (n >= static_HEADER_TABLE.length) {
                break;
            }
            if (!linkedHashMap.containsKey(static_HEADER_TABLE[n].name)) {
                linkedHashMap.put(Hpack.STATIC_HEADER_TABLE[n].name, n);
            }
            ++n;
        }
        return Collections.unmodifiableMap((Map<? extends ByteString, ? extends Integer>)linkedHashMap);
    }
    
    static final class Reader
    {
        Header[] dynamicTable;
        int dynamicTableByteCount;
        int headerCount;
        private final List<Header> headerList;
        private final int headerTableSizeSetting;
        private int maxDynamicTableByteCount;
        int nextHeaderIndex;
        private final BufferedSource source;
        
        Reader(final int headerTableSizeSetting, final int maxDynamicTableByteCount, final Source source) {
            this.headerList = new ArrayList<Header>();
            final Header[] dynamicTable = new Header[8];
            this.dynamicTable = dynamicTable;
            this.nextHeaderIndex = dynamicTable.length - 1;
            this.headerCount = 0;
            this.dynamicTableByteCount = 0;
            this.headerTableSizeSetting = headerTableSizeSetting;
            this.maxDynamicTableByteCount = maxDynamicTableByteCount;
            this.source = Okio.buffer(source);
        }
        
        Reader(final int n, final Source source) {
            this(n, n, source);
        }
        
        private void adjustDynamicTableByteCount() {
            final int maxDynamicTableByteCount = this.maxDynamicTableByteCount;
            final int dynamicTableByteCount = this.dynamicTableByteCount;
            if (maxDynamicTableByteCount < dynamicTableByteCount) {
                if (maxDynamicTableByteCount == 0) {
                    this.clearDynamicTable();
                    return;
                }
                this.evictToRecoverBytes(dynamicTableByteCount - maxDynamicTableByteCount);
            }
        }
        
        private void clearDynamicTable() {
            Arrays.fill(this.dynamicTable, null);
            this.nextHeaderIndex = this.dynamicTable.length - 1;
            this.headerCount = 0;
            this.dynamicTableByteCount = 0;
        }
        
        private int dynamicTableIndex(final int n) {
            return this.nextHeaderIndex + 1 + n;
        }
        
        private int evictToRecoverBytes(int n) {
            int n2 = 0;
            final int n3 = 0;
            if (n > 0) {
                int length = this.dynamicTable.length;
                int n4 = n;
                n = n3;
                while (true) {
                    --length;
                    if (length < this.nextHeaderIndex || n4 <= 0) {
                        break;
                    }
                    n4 -= this.dynamicTable[length].hpackSize;
                    this.dynamicTableByteCount -= this.dynamicTable[length].hpackSize;
                    --this.headerCount;
                    ++n;
                }
                final Header[] dynamicTable = this.dynamicTable;
                final int n5 = this.nextHeaderIndex + 1;
                System.arraycopy(dynamicTable, n5, dynamicTable, n5 + n, this.headerCount);
                this.nextHeaderIndex += n;
                n2 = n;
            }
            return n2;
        }
        
        private ByteString getName(final int n) throws IOException {
            if (!this.isStaticHeader(n)) {
                final int dynamicTableIndex = this.dynamicTableIndex(n - Hpack.STATIC_HEADER_TABLE.length);
                if (dynamicTableIndex >= 0) {
                    final Header[] dynamicTable = this.dynamicTable;
                    if (dynamicTableIndex < dynamicTable.length) {
                        final Header header = dynamicTable[dynamicTableIndex];
                        return header.name;
                    }
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("Header index too large ");
                sb.append(n + 1);
                throw new IOException(sb.toString());
            }
            final Header header = Hpack.STATIC_HEADER_TABLE[n];
            return header.name;
        }
        
        private void insertIntoDynamicTable(int headerCount, final Header header) {
            this.headerList.add(header);
            int hpackSize;
            final int n = hpackSize = header.hpackSize;
            if (headerCount != -1) {
                hpackSize = n - this.dynamicTable[this.dynamicTableIndex(headerCount)].hpackSize;
            }
            final int maxDynamicTableByteCount = this.maxDynamicTableByteCount;
            if (hpackSize > maxDynamicTableByteCount) {
                this.clearDynamicTable();
                return;
            }
            final int evictToRecoverBytes = this.evictToRecoverBytes(this.dynamicTableByteCount + hpackSize - maxDynamicTableByteCount);
            if (headerCount == -1) {
                headerCount = this.headerCount;
                final Header[] dynamicTable = this.dynamicTable;
                if (headerCount + 1 > dynamicTable.length) {
                    final Header[] dynamicTable2 = new Header[dynamicTable.length * 2];
                    System.arraycopy(dynamicTable, 0, dynamicTable2, dynamicTable.length, dynamicTable.length);
                    this.nextHeaderIndex = this.dynamicTable.length - 1;
                    this.dynamicTable = dynamicTable2;
                }
                headerCount = this.nextHeaderIndex--;
                this.dynamicTable[headerCount] = header;
                ++this.headerCount;
            }
            else {
                this.dynamicTable[headerCount + (this.dynamicTableIndex(headerCount) + evictToRecoverBytes)] = header;
            }
            this.dynamicTableByteCount += hpackSize;
        }
        
        private boolean isStaticHeader(final int n) {
            return n >= 0 && n <= Hpack.STATIC_HEADER_TABLE.length - 1;
        }
        
        private int readByte() throws IOException {
            return this.source.readByte() & 0xFF;
        }
        
        private void readIndexedHeader(final int n) throws IOException {
            if (this.isStaticHeader(n)) {
                this.headerList.add(Hpack.STATIC_HEADER_TABLE[n]);
                return;
            }
            final int dynamicTableIndex = this.dynamicTableIndex(n - Hpack.STATIC_HEADER_TABLE.length);
            if (dynamicTableIndex >= 0) {
                final Header[] dynamicTable = this.dynamicTable;
                if (dynamicTableIndex < dynamicTable.length) {
                    this.headerList.add(dynamicTable[dynamicTableIndex]);
                    return;
                }
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Header index too large ");
            sb.append(n + 1);
            throw new IOException(sb.toString());
        }
        
        private void readLiteralHeaderWithIncrementalIndexingIndexedName(final int n) throws IOException {
            this.insertIntoDynamicTable(-1, new Header(this.getName(n), this.readByteString()));
        }
        
        private void readLiteralHeaderWithIncrementalIndexingNewName() throws IOException {
            this.insertIntoDynamicTable(-1, new Header(Hpack.checkLowercase(this.readByteString()), this.readByteString()));
        }
        
        private void readLiteralHeaderWithoutIndexingIndexedName(final int n) throws IOException {
            this.headerList.add(new Header(this.getName(n), this.readByteString()));
        }
        
        private void readLiteralHeaderWithoutIndexingNewName() throws IOException {
            this.headerList.add(new Header(Hpack.checkLowercase(this.readByteString()), this.readByteString()));
        }
        
        public List<Header> getAndResetHeaderList() {
            final ArrayList<Header> list = new ArrayList<Header>(this.headerList);
            this.headerList.clear();
            return list;
        }
        
        ByteString readByteString() throws IOException {
            final int byte1 = this.readByte();
            final boolean b = (byte1 & 0x80) == 0x80;
            final int int1 = this.readInt(byte1, 127);
            if (b) {
                return ByteString.of(Huffman.get().decode(this.source.readByteArray(int1)));
            }
            return this.source.readByteString(int1);
        }
        
        void readHeaders() throws IOException {
            while (!this.source.exhausted()) {
                final int n = this.source.readByte() & 0xFF;
                if (n == 128) {
                    throw new IOException("index == 0");
                }
                if ((n & 0x80) == 0x80) {
                    this.readIndexedHeader(this.readInt(n, 127) - 1);
                }
                else if (n == 64) {
                    this.readLiteralHeaderWithIncrementalIndexingNewName();
                }
                else if ((n & 0x40) == 0x40) {
                    this.readLiteralHeaderWithIncrementalIndexingIndexedName(this.readInt(n, 63) - 1);
                }
                else if ((n & 0x20) == 0x20) {
                    final int int1 = this.readInt(n, 31);
                    this.maxDynamicTableByteCount = int1;
                    if (int1 < 0 || int1 > this.headerTableSizeSetting) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Invalid dynamic table size update ");
                        sb.append(this.maxDynamicTableByteCount);
                        throw new IOException(sb.toString());
                    }
                    this.adjustDynamicTableByteCount();
                }
                else if (n != 16 && n != 0) {
                    this.readLiteralHeaderWithoutIndexingIndexedName(this.readInt(n, 15) - 1);
                }
                else {
                    this.readLiteralHeaderWithoutIndexingNewName();
                }
            }
        }
        
        int readInt(int n, int n2) throws IOException {
            n &= n2;
            if (n < n2) {
                return n;
            }
            n = 0;
            int byte1;
            while (true) {
                byte1 = this.readByte();
                if ((byte1 & 0x80) == 0x0) {
                    break;
                }
                n2 += (byte1 & 0x7F) << n;
                n += 7;
            }
            return n2 + (byte1 << n);
        }
    }
    
    static final class Writer
    {
        Header[] dynamicTable;
        int dynamicTableByteCount;
        private boolean emitDynamicTableSizeUpdate;
        int headerCount;
        int headerTableSizeSetting;
        int maxDynamicTableByteCount;
        int nextHeaderIndex;
        private final Buffer out;
        private int smallestHeaderTableSizeSetting;
        private final boolean useCompression;
        
        Writer(final int n, final boolean useCompression, final Buffer out) {
            this.smallestHeaderTableSizeSetting = Integer.MAX_VALUE;
            final Header[] dynamicTable = new Header[8];
            this.dynamicTable = dynamicTable;
            this.nextHeaderIndex = dynamicTable.length - 1;
            this.headerCount = 0;
            this.dynamicTableByteCount = 0;
            this.headerTableSizeSetting = n;
            this.maxDynamicTableByteCount = n;
            this.useCompression = useCompression;
            this.out = out;
        }
        
        Writer(final Buffer buffer) {
            this(4096, true, buffer);
        }
        
        private void adjustDynamicTableByteCount() {
            final int maxDynamicTableByteCount = this.maxDynamicTableByteCount;
            final int dynamicTableByteCount = this.dynamicTableByteCount;
            if (maxDynamicTableByteCount < dynamicTableByteCount) {
                if (maxDynamicTableByteCount == 0) {
                    this.clearDynamicTable();
                    return;
                }
                this.evictToRecoverBytes(dynamicTableByteCount - maxDynamicTableByteCount);
            }
        }
        
        private void clearDynamicTable() {
            Arrays.fill(this.dynamicTable, null);
            this.nextHeaderIndex = this.dynamicTable.length - 1;
            this.headerCount = 0;
            this.dynamicTableByteCount = 0;
        }
        
        private int evictToRecoverBytes(int n) {
            int n2 = 0;
            final int n3 = 0;
            if (n > 0) {
                int length = this.dynamicTable.length;
                int n4 = n;
                n = n3;
                while (true) {
                    --length;
                    if (length < this.nextHeaderIndex || n4 <= 0) {
                        break;
                    }
                    n4 -= this.dynamicTable[length].hpackSize;
                    this.dynamicTableByteCount -= this.dynamicTable[length].hpackSize;
                    --this.headerCount;
                    ++n;
                }
                final Header[] dynamicTable = this.dynamicTable;
                final int n5 = this.nextHeaderIndex + 1;
                System.arraycopy(dynamicTable, n5, dynamicTable, n5 + n, this.headerCount);
                final Header[] dynamicTable2 = this.dynamicTable;
                final int n6 = this.nextHeaderIndex + 1;
                Arrays.fill(dynamicTable2, n6, n6 + n, null);
                this.nextHeaderIndex += n;
                n2 = n;
            }
            return n2;
        }
        
        private void insertIntoDynamicTable(final Header header) {
            final int hpackSize = header.hpackSize;
            final int maxDynamicTableByteCount = this.maxDynamicTableByteCount;
            if (hpackSize > maxDynamicTableByteCount) {
                this.clearDynamicTable();
                return;
            }
            this.evictToRecoverBytes(this.dynamicTableByteCount + hpackSize - maxDynamicTableByteCount);
            final int headerCount = this.headerCount;
            final Header[] dynamicTable = this.dynamicTable;
            if (headerCount + 1 > dynamicTable.length) {
                final Header[] dynamicTable2 = new Header[dynamicTable.length * 2];
                System.arraycopy(dynamicTable, 0, dynamicTable2, dynamicTable.length, dynamicTable.length);
                this.nextHeaderIndex = this.dynamicTable.length - 1;
                this.dynamicTable = dynamicTable2;
            }
            this.dynamicTable[this.nextHeaderIndex--] = header;
            ++this.headerCount;
            this.dynamicTableByteCount += hpackSize;
        }
        
        void setHeaderTableSizeSetting(int min) {
            this.headerTableSizeSetting = min;
            min = Math.min(min, 16384);
            final int maxDynamicTableByteCount = this.maxDynamicTableByteCount;
            if (maxDynamicTableByteCount == min) {
                return;
            }
            if (min < maxDynamicTableByteCount) {
                this.smallestHeaderTableSizeSetting = Math.min(this.smallestHeaderTableSizeSetting, min);
            }
            this.emitDynamicTableSizeUpdate = true;
            this.maxDynamicTableByteCount = min;
            this.adjustDynamicTableByteCount();
        }
        
        void writeByteString(ByteString byteString) throws IOException {
            int n;
            int n2;
            if (this.useCompression && Huffman.get().encodedLength(byteString) < byteString.size()) {
                final Buffer buffer = new Buffer();
                Huffman.get().encode(byteString, buffer);
                byteString = buffer.readByteString();
                n = byteString.size();
                n2 = 128;
            }
            else {
                n = byteString.size();
                n2 = 0;
            }
            this.writeInt(n, 127, n2);
            this.out.write(byteString);
        }
        
        void writeHeaders(final List<Header> list) throws IOException {
            if (this.emitDynamicTableSizeUpdate) {
                final int smallestHeaderTableSizeSetting = this.smallestHeaderTableSizeSetting;
                if (smallestHeaderTableSizeSetting < this.maxDynamicTableByteCount) {
                    this.writeInt(smallestHeaderTableSizeSetting, 31, 32);
                }
                this.emitDynamicTableSizeUpdate = false;
                this.smallestHeaderTableSizeSetting = Integer.MAX_VALUE;
                this.writeInt(this.maxDynamicTableByteCount, 31, 32);
            }
            for (int size = list.size(), i = 0; i < size; ++i) {
                final Header header = list.get(i);
                final ByteString asciiLowercase = header.name.toAsciiLowercase();
                final ByteString value = header.value;
                final Integer n = Hpack.NAME_TO_FIRST_INDEX.get(asciiLowercase);
                int n2 = 0;
                int n3 = 0;
                Label_0198: {
                    if (n != null) {
                        n2 = n + 1;
                        if (n2 > 1 && n2 < 8) {
                            if (Util.equal(Hpack.STATIC_HEADER_TABLE[n2 - 1].value, value)) {
                                n3 = n2;
                                break Label_0198;
                            }
                            if (Util.equal(Hpack.STATIC_HEADER_TABLE[n2].value, value)) {
                                n3 = n2;
                                ++n2;
                                break Label_0198;
                            }
                        }
                        n3 = n2;
                        n2 = -1;
                    }
                    else {
                        n2 = -1;
                        n3 = -1;
                    }
                }
                int n4 = n2;
                int n5 = n3;
                if (n2 == -1) {
                    int n6 = this.nextHeaderIndex + 1;
                    final int length = this.dynamicTable.length;
                    while (true) {
                        n4 = n2;
                        n5 = n3;
                        if (n6 >= length) {
                            break;
                        }
                        int n7 = n3;
                        if (Util.equal(this.dynamicTable[n6].name, asciiLowercase)) {
                            if (Util.equal(this.dynamicTable[n6].value, value)) {
                                n4 = Hpack.STATIC_HEADER_TABLE.length + (n6 - this.nextHeaderIndex);
                                n5 = n3;
                                break;
                            }
                            if ((n7 = n3) == -1) {
                                n7 = n6 - this.nextHeaderIndex + Hpack.STATIC_HEADER_TABLE.length;
                            }
                        }
                        ++n6;
                        n3 = n7;
                    }
                }
                if (n4 != -1) {
                    this.writeInt(n4, 127, 128);
                }
                else {
                    if (n5 == -1) {
                        this.out.writeByte(64);
                        this.writeByteString(asciiLowercase);
                    }
                    else {
                        if (asciiLowercase.startsWith(Header.PSEUDO_PREFIX) && !Header.TARGET_AUTHORITY.equals(asciiLowercase)) {
                            this.writeInt(n5, 15, 0);
                            this.writeByteString(value);
                            continue;
                        }
                        this.writeInt(n5, 63, 64);
                    }
                    this.writeByteString(value);
                    this.insertIntoDynamicTable(header);
                }
            }
        }
        
        void writeInt(int i, final int n, final int n2) {
            Buffer buffer;
            if (i < n) {
                buffer = this.out;
                i |= n2;
            }
            else {
                this.out.writeByte(n2 | n);
                for (i -= n; i >= 128; i >>>= 7) {
                    this.out.writeByte(0x80 | (i & 0x7F));
                }
                buffer = this.out;
            }
            buffer.writeByte(i);
        }
    }
}
