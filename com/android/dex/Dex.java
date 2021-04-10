package com.android.dex;

import java.nio.*;
import java.util.zip.*;
import java.security.*;
import java.util.function.*;
import java.util.*;
import com.android.dex.util.*;
import java.io.*;

public final class Dex
{
    private static final int CHECKSUM_OFFSET = 8;
    private static final int CHECKSUM_SIZE = 4;
    static final short[] EMPTY_SHORT_ARRAY;
    private static final int SIGNATURE_OFFSET = 12;
    private static final int SIGNATURE_SIZE = 20;
    private ByteBuffer data;
    private final FieldIdTable fieldIds;
    private final MethodIdTable methodIds;
    private int nextSectionStart;
    private final ProtoIdTable protoIds;
    private final StringTable strings;
    private final TableOfContents tableOfContents;
    private final TypeIndexToDescriptorIndexTable typeIds;
    private final TypeIndexToDescriptorTable typeNames;
    
    static {
        EMPTY_SHORT_ARRAY = new short[0];
    }
    
    public Dex(final int n) throws IOException {
        this.tableOfContents = new TableOfContents();
        this.nextSectionStart = 0;
        this.strings = new StringTable();
        this.typeIds = new TypeIndexToDescriptorIndexTable();
        this.typeNames = new TypeIndexToDescriptorTable();
        this.protoIds = new ProtoIdTable();
        this.fieldIds = new FieldIdTable();
        this.methodIds = new MethodIdTable();
        (this.data = ByteBuffer.wrap(new byte[n])).order(ByteOrder.LITTLE_ENDIAN);
    }
    
    public Dex(final File file) throws IOException {
        this.tableOfContents = new TableOfContents();
        this.nextSectionStart = 0;
        this.strings = new StringTable();
        this.typeIds = new TypeIndexToDescriptorIndexTable();
        this.typeNames = new TypeIndexToDescriptorTable();
        this.protoIds = new ProtoIdTable();
        this.fieldIds = new FieldIdTable();
        this.methodIds = new MethodIdTable();
        if (FileUtils.hasArchiveSuffix(file.getName())) {
            final ZipFile zipFile = new ZipFile(file);
            final ZipEntry entry = zipFile.getEntry("classes.dex");
            if (entry != null) {
                this.loadFrom(zipFile.getInputStream(entry));
                zipFile.close();
                return;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Expected classes.dex in ");
            sb.append(file);
            throw new DexException(sb.toString());
        }
        else {
            if (file.getName().endsWith(".dex")) {
                this.loadFrom(new FileInputStream(file));
                return;
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("unknown output extension: ");
            sb2.append(file);
            throw new DexException(sb2.toString());
        }
    }
    
    public Dex(final InputStream inputStream) throws IOException {
        this.tableOfContents = new TableOfContents();
        this.nextSectionStart = 0;
        this.strings = new StringTable();
        this.typeIds = new TypeIndexToDescriptorIndexTable();
        this.typeNames = new TypeIndexToDescriptorTable();
        this.protoIds = new ProtoIdTable();
        this.fieldIds = new FieldIdTable();
        this.methodIds = new MethodIdTable();
        this.loadFrom(inputStream);
    }
    
    private Dex(final ByteBuffer data) throws IOException {
        this.tableOfContents = new TableOfContents();
        this.nextSectionStart = 0;
        this.strings = new StringTable();
        this.typeIds = new TypeIndexToDescriptorIndexTable();
        this.typeNames = new TypeIndexToDescriptorTable();
        this.protoIds = new ProtoIdTable();
        this.fieldIds = new FieldIdTable();
        this.methodIds = new MethodIdTable();
        (this.data = data).order(ByteOrder.LITTLE_ENDIAN);
        this.tableOfContents.readFrom(this);
    }
    
    public Dex(final byte[] array) throws IOException {
        this(ByteBuffer.wrap(array));
    }
    
    private static void checkBounds(final int n, final int n2) {
        if (n >= 0 && n < n2) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("index:");
        sb.append(n);
        sb.append(", length=");
        sb.append(n2);
        throw new IndexOutOfBoundsException(sb.toString());
    }
    
    public static Dex create(final ByteBuffer byteBuffer) throws IOException {
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        ByteBuffer slice = byteBuffer;
        if (byteBuffer.get(0) == 100) {
            slice = byteBuffer;
            if (byteBuffer.get(1) == 101) {
                slice = byteBuffer;
                if (byteBuffer.get(2) == 121) {
                    slice = byteBuffer;
                    if (byteBuffer.get(3) == 10) {
                        byteBuffer.position(8);
                        final int int1 = byteBuffer.getInt();
                        final int int2 = byteBuffer.getInt();
                        byteBuffer.position(int1);
                        byteBuffer.limit(int1 + int2);
                        slice = byteBuffer.slice();
                    }
                }
            }
        }
        return new Dex(slice);
    }
    
    private void loadFrom(final InputStream inputStream) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final byte[] array = new byte[8192];
        while (true) {
            final int read = inputStream.read(array);
            if (read == -1) {
                break;
            }
            byteArrayOutputStream.write(array, 0, read);
        }
        inputStream.close();
        (this.data = ByteBuffer.wrap(byteArrayOutputStream.toByteArray())).order(ByteOrder.LITTLE_ENDIAN);
        this.tableOfContents.readFrom(this);
    }
    
    public int annotationDirectoryOffsetFromClassDefIndex(final int n) {
        checkBounds(n, this.tableOfContents.classDefs.size);
        return this.data.getInt(this.tableOfContents.classDefs.off + n * 32 + 4 + 4 + 4 + 4 + 4);
    }
    
    public Section appendSection(int nextSectionStart, final String s) {
        if ((nextSectionStart & 0x3) != 0x0) {
            throw new IllegalStateException("Not four byte aligned!");
        }
        nextSectionStart += this.nextSectionStart;
        final ByteBuffer duplicate = this.data.duplicate();
        duplicate.order(ByteOrder.LITTLE_ENDIAN);
        duplicate.position(this.nextSectionStart);
        duplicate.limit(nextSectionStart);
        final Section section = new Section(s, duplicate);
        this.nextSectionStart = nextSectionStart;
        return section;
    }
    
    public Iterable<ClassDef> classDefs() {
        return new ClassDefIterable();
    }
    
    public int computeChecksum() throws IOException {
        final Adler32 adler32 = new Adler32();
        final byte[] array = new byte[8192];
        final ByteBuffer duplicate = this.data.duplicate();
        duplicate.limit(duplicate.capacity());
        duplicate.position(12);
        while (duplicate.hasRemaining()) {
            final int min = Math.min(array.length, duplicate.remaining());
            duplicate.get(array, 0, min);
            adler32.update(array, 0, min);
        }
        return (int)adler32.getValue();
    }
    
    public byte[] computeSignature() throws IOException {
        try {
            final MessageDigest instance = MessageDigest.getInstance("SHA-1");
            final byte[] array = new byte[8192];
            final ByteBuffer duplicate = this.data.duplicate();
            duplicate.limit(duplicate.capacity());
            duplicate.position(32);
            while (duplicate.hasRemaining()) {
                final int min = Math.min(array.length, duplicate.remaining());
                duplicate.get(array, 0, min);
                instance.update(array, 0, min);
            }
            return instance.digest();
        }
        catch (NoSuchAlgorithmException ex) {
            throw new AssertionError();
        }
    }
    
    public int declaringClassIndexFromMethodIndex(final int n) {
        checkBounds(n, this.tableOfContents.methodIds.size);
        return this.data.getShort(this.tableOfContents.methodIds.off + n * 8) & 0xFFFF;
    }
    
    public int descriptorIndexFromTypeIndex(final int n) {
        checkBounds(n, this.tableOfContents.typeIds.size);
        return this.data.getInt(this.tableOfContents.typeIds.off + n * 4);
    }
    
    public List<FieldId> fieldIds() {
        return this.fieldIds;
    }
    
    public int findClassDefIndexFromTypeIndex(final int n) {
        checkBounds(n, this.tableOfContents.typeIds.size);
        if (!this.tableOfContents.classDefs.exists()) {
            return -1;
        }
        for (int i = 0; i < this.tableOfContents.classDefs.size; ++i) {
            if (this.typeIndexFromClassDefIndex(i) == n) {
                return i;
            }
        }
        return -1;
    }
    
    public int findFieldIndex(final FieldId fieldId) {
        return Collections.binarySearch(this.fieldIds, fieldId);
    }
    
    public int findMethodIndex(final MethodId methodId) {
        return Collections.binarySearch(this.methodIds, methodId);
    }
    
    public int findStringIndex(final String s) {
        return Collections.binarySearch(this.strings, s);
    }
    
    public int findTypeIndex(final String s) {
        return Collections.binarySearch(this.typeNames, s);
    }
    
    public byte[] getBytes() {
        final ByteBuffer duplicate = this.data.duplicate();
        final byte[] array = new byte[duplicate.capacity()];
        duplicate.position(0);
        duplicate.get(array);
        return array;
    }
    
    public int getLength() {
        return this.data.capacity();
    }
    
    public int getNextSectionStart() {
        return this.nextSectionStart;
    }
    
    public TableOfContents getTableOfContents() {
        return this.tableOfContents;
    }
    
    public short[] interfaceTypeIndicesFromClassDefIndex(int i) {
        checkBounds(i, this.tableOfContents.classDefs.size);
        i = this.data.getInt(this.tableOfContents.classDefs.off + i * 32 + 4 + 4 + 4);
        if (i == 0) {
            return Dex.EMPTY_SHORT_ARRAY;
        }
        final int int1 = this.data.getInt(i);
        if (int1 <= 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unexpected interfaces list size: ");
            sb.append(int1);
            throw new AssertionError((Object)sb.toString());
        }
        int n = i + 4;
        final short[] array = new short[int1];
        for (i = 0; i < int1; ++i) {
            array[i] = this.data.getShort(n);
            n += 2;
        }
        return array;
    }
    
    public List<MethodId> methodIds() {
        return this.methodIds;
    }
    
    public int nameIndexFromFieldIndex(final int n) {
        checkBounds(n, this.tableOfContents.fieldIds.size);
        return this.data.getInt(this.tableOfContents.fieldIds.off + n * 8 + 2 + 2);
    }
    
    public int nameIndexFromMethodIndex(final int n) {
        checkBounds(n, this.tableOfContents.methodIds.size);
        return this.data.getInt(this.tableOfContents.methodIds.off + n * 8 + 2 + 2);
    }
    
    public Section open(final int n) {
        if (n >= 0 && n < this.data.capacity()) {
            final ByteBuffer duplicate = this.data.duplicate();
            duplicate.order(ByteOrder.LITTLE_ENDIAN);
            duplicate.position(n);
            duplicate.limit(this.data.capacity());
            return new Section("section", duplicate);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("position=");
        sb.append(n);
        sb.append(" length=");
        sb.append(this.data.capacity());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public short[] parameterTypeIndicesFromMethodIndex(int i) {
        checkBounds(i, this.tableOfContents.methodIds.size);
        i = (this.data.getShort(this.tableOfContents.methodIds.off + i * 8 + 2) & 0xFFFF);
        checkBounds(i, this.tableOfContents.protoIds.size);
        i = this.data.getInt(this.tableOfContents.protoIds.off + i * 12 + 4 + 4);
        if (i == 0) {
            return Dex.EMPTY_SHORT_ARRAY;
        }
        final int int1 = this.data.getInt(i);
        if (int1 <= 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unexpected parameter type list size: ");
            sb.append(int1);
            throw new AssertionError((Object)sb.toString());
        }
        int n = i + 4;
        final short[] array = new short[int1];
        for (i = 0; i < int1; ++i) {
            array[i] = this.data.getShort(n);
            n += 2;
        }
        return array;
    }
    
    public List<ProtoId> protoIds() {
        return this.protoIds;
    }
    
    public ClassData readClassData(final ClassDef classDef) {
        final int classDataOffset = classDef.getClassDataOffset();
        if (classDataOffset == 0) {
            throw new IllegalArgumentException("offset == 0");
        }
        return this.open(classDataOffset).readClassData();
    }
    
    public Code readCode(final ClassData.Method method) {
        final int codeOffset = method.getCodeOffset();
        if (codeOffset == 0) {
            throw new IllegalArgumentException("offset == 0");
        }
        return this.open(codeOffset).readCode();
    }
    
    public TypeList readTypeList(final int n) {
        if (n == 0) {
            return TypeList.EMPTY;
        }
        return this.open(n).readTypeList();
    }
    
    public int returnTypeIndexFromMethodIndex(int n) {
        checkBounds(n, this.tableOfContents.methodIds.size);
        n = (this.data.getShort(this.tableOfContents.methodIds.off + n * 8 + 2) & 0xFFFF);
        checkBounds(n, this.tableOfContents.protoIds.size);
        return this.data.getInt(this.tableOfContents.protoIds.off + n * 12 + 4);
    }
    
    public List<String> strings() {
        return this.strings;
    }
    
    public List<Integer> typeIds() {
        return this.typeIds;
    }
    
    public int typeIndexFromClassDefIndex(final int n) {
        checkBounds(n, this.tableOfContents.classDefs.size);
        return this.data.getInt(this.tableOfContents.classDefs.off + n * 32);
    }
    
    public int typeIndexFromFieldIndex(final int n) {
        checkBounds(n, this.tableOfContents.fieldIds.size);
        return this.data.getShort(this.tableOfContents.fieldIds.off + n * 8 + 2) & 0xFFFF;
    }
    
    public List<String> typeNames() {
        return this.typeNames;
    }
    
    public void writeHashes() throws IOException {
        this.open(12).write(this.computeSignature());
        this.open(8).writeInt(this.computeChecksum());
    }
    
    public void writeTo(final File file) throws IOException {
        final FileOutputStream fileOutputStream = new FileOutputStream(file);
        this.writeTo(fileOutputStream);
        fileOutputStream.close();
    }
    
    public void writeTo(final OutputStream outputStream) throws IOException {
        final byte[] array = new byte[8192];
        final ByteBuffer duplicate = this.data.duplicate();
        duplicate.clear();
        while (duplicate.hasRemaining()) {
            final int min = Math.min(array.length, duplicate.remaining());
            duplicate.get(array, 0, min);
            outputStream.write(array, 0, min);
        }
    }
    
    private final class ClassDefIterable implements Iterable<ClassDef>
    {
        @Override
        public void forEach(final Consumer<?> p0) {
            // 
            // This method could not be decompiled.
            // 
            // Could not show original bytecode, likely due to the same error.
            // 
            // The error that occurred was:
            // 
            // java.lang.NullPointerException
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        @Override
        public Iterator<ClassDef> iterator() {
            if (!Dex.this.tableOfContents.classDefs.exists()) {
                return Collections.emptySet().iterator();
            }
            return new ClassDefIterator();
        }
        
        @Override
        public Spliterator<Object> spliterator() {
            return Iterable-CC.$default$spliterator();
        }
    }
    
    private final class ClassDefIterator implements Iterator<ClassDef>
    {
        private int count;
        private final Section in;
        
        private ClassDefIterator() {
            this.in = Dex.this.open(Dex.this.tableOfContents.classDefs.off);
            this.count = 0;
        }
        
        @Override
        public void forEachRemaining(final Consumer<?> p0) {
            // 
            // This method could not be decompiled.
            // 
            // Could not show original bytecode, likely due to the same error.
            // 
            // The error that occurred was:
            // 
            // java.lang.NullPointerException
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        @Override
        public boolean hasNext() {
            return this.count < Dex.this.tableOfContents.classDefs.size;
        }
        
        @Override
        public ClassDef next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            ++this.count;
            return this.in.readClassDef();
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    
    private final class FieldIdTable extends AbstractList<FieldId> implements RandomAccess
    {
        @Override
        public FieldId get(final int n) {
            checkBounds(n, Dex.this.tableOfContents.fieldIds.size);
            return Dex.this.open(Dex.this.tableOfContents.fieldIds.off + n * 8).readFieldId();
        }
        
        @Override
        public int size() {
            return Dex.this.tableOfContents.fieldIds.size;
        }
    }
    
    private final class MethodIdTable extends AbstractList<MethodId> implements RandomAccess
    {
        @Override
        public MethodId get(final int n) {
            checkBounds(n, Dex.this.tableOfContents.methodIds.size);
            return Dex.this.open(Dex.this.tableOfContents.methodIds.off + n * 8).readMethodId();
        }
        
        @Override
        public int size() {
            return Dex.this.tableOfContents.methodIds.size;
        }
    }
    
    private final class ProtoIdTable extends AbstractList<ProtoId> implements RandomAccess
    {
        @Override
        public ProtoId get(final int n) {
            checkBounds(n, Dex.this.tableOfContents.protoIds.size);
            return Dex.this.open(Dex.this.tableOfContents.protoIds.off + n * 12).readProtoId();
        }
        
        @Override
        public int size() {
            return Dex.this.tableOfContents.protoIds.size;
        }
    }
    
    public final class Section implements ByteInput, ByteOutput
    {
        private final ByteBuffer data;
        private final int initialPosition;
        private final String name;
        
        private Section(final String name, final ByteBuffer data) {
            this.name = name;
            this.data = data;
            this.initialPosition = data.position();
        }
        
        private int findCatchHandlerIndex(final Code.CatchHandler[] array, final int n) {
            for (int i = 0; i < array.length; ++i) {
                if (array[i].getOffset() == n) {
                    return i;
                }
            }
            throw new IllegalArgumentException();
        }
        
        private byte[] getBytesFrom(final int n) {
            final byte[] array = new byte[this.data.position() - n];
            this.data.position(n);
            this.data.get(array);
            return array;
        }
        
        private Code.CatchHandler readCatchHandler(final int n) {
            final int sleb128 = this.readSleb128();
            final int abs = Math.abs(sleb128);
            final int[] array = new int[abs];
            final int[] array2 = new int[abs];
            for (int i = 0; i < abs; ++i) {
                array[i] = this.readUleb128();
                array2[i] = this.readUleb128();
            }
            int uleb128;
            if (sleb128 <= 0) {
                uleb128 = this.readUleb128();
            }
            else {
                uleb128 = -1;
            }
            return new Code.CatchHandler(array, array2, uleb128, n);
        }
        
        private Code.CatchHandler[] readCatchHandlers() {
            final int position = this.data.position();
            final int uleb128 = this.readUleb128();
            final Code.CatchHandler[] array = new Code.CatchHandler[uleb128];
            for (int i = 0; i < uleb128; ++i) {
                array[i] = this.readCatchHandler(this.data.position() - position);
            }
            return array;
        }
        
        private ClassData readClassData() {
            return new ClassData(this.readFields(this.readUleb128()), this.readFields(this.readUleb128()), this.readMethods(this.readUleb128()), this.readMethods(this.readUleb128()));
        }
        
        private Code readCode() {
            final int unsignedShort = this.readUnsignedShort();
            final int unsignedShort2 = this.readUnsignedShort();
            final int unsignedShort3 = this.readUnsignedShort();
            final int unsignedShort4 = this.readUnsignedShort();
            final int int1 = this.readInt();
            final short[] shortArray = this.readShortArray(this.readInt());
            Code.CatchHandler[] catchHandlers;
            Code.Try[] tries;
            if (unsignedShort4 > 0) {
                if (shortArray.length % 2 == 1) {
                    this.readShort();
                }
                final Section open = Dex.this.open(this.data.position());
                this.skip(unsignedShort4 * 8);
                catchHandlers = this.readCatchHandlers();
                tries = open.readTries(unsignedShort4, catchHandlers);
            }
            else {
                catchHandlers = new Code.CatchHandler[0];
                tries = new Code.Try[0];
            }
            return new Code(unsignedShort, unsignedShort2, unsignedShort3, int1, shortArray, tries, catchHandlers);
        }
        
        private ClassData.Field[] readFields(final int n) {
            final ClassData.Field[] array = new ClassData.Field[n];
            int n2 = 0;
            for (int i = 0; i < n; ++i) {
                n2 += this.readUleb128();
                array[i] = new ClassData.Field(n2, this.readUleb128());
            }
            return array;
        }
        
        private ClassData.Method[] readMethods(final int n) {
            final ClassData.Method[] array = new ClassData.Method[n];
            int n2 = 0;
            for (int i = 0; i < n; ++i) {
                n2 += this.readUleb128();
                array[i] = new ClassData.Method(n2, this.readUleb128(), this.readUleb128());
            }
            return array;
        }
        
        private Code.Try[] readTries(final int n, final Code.CatchHandler[] array) {
            final Code.Try[] array2 = new Code.Try[n];
            for (int i = 0; i < n; ++i) {
                array2[i] = new Code.Try(this.readInt(), this.readUnsignedShort(), this.findCatchHandlerIndex(array, this.readUnsignedShort()));
            }
            return array2;
        }
        
        public void alignToFourBytes() {
            this.data.position(this.data.position() + 3 & 0xFFFFFFFC);
        }
        
        public void alignToFourBytesWithZeroFill() {
            while ((this.data.position() & 0x3) != 0x0) {
                this.data.put((byte)0);
            }
        }
        
        public void assertFourByteAligned() {
            if ((this.data.position() & 0x3) != 0x0) {
                throw new IllegalStateException("Not four byte aligned!");
            }
        }
        
        public int getPosition() {
            return this.data.position();
        }
        
        public Annotation readAnnotation() {
            final byte byte1 = this.readByte();
            final int position = this.data.position();
            new EncodedValueReader(this, 29).skipValue();
            return new Annotation(Dex.this, byte1, new EncodedValue(this.getBytesFrom(position)));
        }
        
        @Override
        public byte readByte() {
            return this.data.get();
        }
        
        public byte[] readByteArray(final int n) {
            final byte[] array = new byte[n];
            this.data.get(array);
            return array;
        }
        
        public ClassDef readClassDef() {
            return new ClassDef(Dex.this, this.getPosition(), this.readInt(), this.readInt(), this.readInt(), this.readInt(), this.readInt(), this.readInt(), this.readInt(), this.readInt());
        }
        
        public EncodedValue readEncodedArray() {
            final int position = this.data.position();
            new EncodedValueReader(this, 28).skipValue();
            return new EncodedValue(this.getBytesFrom(position));
        }
        
        public FieldId readFieldId() {
            return new FieldId(Dex.this, this.readUnsignedShort(), this.readUnsignedShort(), this.readInt());
        }
        
        public int readInt() {
            return this.data.getInt();
        }
        
        public MethodId readMethodId() {
            return new MethodId(Dex.this, this.readUnsignedShort(), this.readUnsignedShort(), this.readInt());
        }
        
        public ProtoId readProtoId() {
            return new ProtoId(Dex.this, this.readInt(), this.readInt(), this.readInt());
        }
        
        public short readShort() {
            return this.data.getShort();
        }
        
        public short[] readShortArray(final int n) {
            if (n == 0) {
                return Dex.EMPTY_SHORT_ARRAY;
            }
            final short[] array = new short[n];
            for (int i = 0; i < n; ++i) {
                array[i] = this.readShort();
            }
            return array;
        }
        
        public int readSleb128() {
            return Leb128.readSignedLeb128(this);
        }
        
        public String readString() {
            final int int1 = this.readInt();
            final int position = this.data.position();
            final int limit = this.data.limit();
            this.data.position(int1);
            this.data.limit(this.data.capacity());
            try {
                try {
                    final int uleb128 = this.readUleb128();
                    final String decode = Mutf8.decode(this, new char[uleb128]);
                    if (decode.length() != uleb128) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Declared length ");
                        sb.append(uleb128);
                        sb.append(" doesn't match decoded length of ");
                        sb.append(decode.length());
                        throw new DexException(sb.toString());
                    }
                    this.data.position(position);
                    this.data.limit(limit);
                    return decode;
                }
                finally {}
            }
            catch (UTFDataFormatException ex) {
                throw new DexException(ex);
            }
            this.data.position(position);
            this.data.limit(limit);
        }
        
        public TypeList readTypeList() {
            final short[] shortArray = this.readShortArray(this.readInt());
            this.alignToFourBytes();
            return new TypeList(Dex.this, shortArray);
        }
        
        public int readUleb128() {
            return Leb128.readUnsignedLeb128(this);
        }
        
        public int readUleb128p1() {
            return Leb128.readUnsignedLeb128(this) - 1;
        }
        
        public int readUnsignedShort() {
            return this.readShort() & 0xFFFF;
        }
        
        public int remaining() {
            return this.data.remaining();
        }
        
        public void skip(final int n) {
            if (n < 0) {
                throw new IllegalArgumentException();
            }
            this.data.position(this.data.position() + n);
        }
        
        public int used() {
            return this.data.position() - this.initialPosition;
        }
        
        public void write(final byte[] array) {
            this.data.put(array);
        }
        
        public void write(final short[] array) {
            for (int length = array.length, i = 0; i < length; ++i) {
                this.writeShort(array[i]);
            }
        }
        
        @Override
        public void writeByte(final int n) {
            this.data.put((byte)n);
        }
        
        public void writeInt(final int n) {
            this.data.putInt(n);
        }
        
        public void writeShort(final short n) {
            this.data.putShort(n);
        }
        
        public void writeSleb128(final int n) {
            try {
                Leb128.writeSignedLeb128(this, n);
            }
            catch (ArrayIndexOutOfBoundsException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Section limit ");
                sb.append(this.data.limit());
                sb.append(" exceeded by ");
                sb.append(this.name);
                throw new DexException(sb.toString());
            }
        }
        
        public void writeStringData(final String s) {
            try {
                this.writeUleb128(s.length());
                this.write(Mutf8.encode(s));
                this.writeByte(0);
            }
            catch (UTFDataFormatException ex) {
                throw new AssertionError();
            }
        }
        
        public void writeTypeList(final TypeList list) {
            final short[] types = list.getTypes();
            this.writeInt(types.length);
            for (int length = types.length, i = 0; i < length; ++i) {
                this.writeShort(types[i]);
            }
            this.alignToFourBytesWithZeroFill();
        }
        
        public void writeUleb128(final int n) {
            try {
                Leb128.writeUnsignedLeb128(this, n);
            }
            catch (ArrayIndexOutOfBoundsException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Section limit ");
                sb.append(this.data.limit());
                sb.append(" exceeded by ");
                sb.append(this.name);
                throw new DexException(sb.toString());
            }
        }
        
        public void writeUleb128p1(final int n) {
            this.writeUleb128(n + 1);
        }
        
        public void writeUnsignedShort(final int n) {
            final short n2 = (short)n;
            if (n != (0xFFFF & n2)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Expected an unsigned short: ");
                sb.append(n);
                throw new IllegalArgumentException(sb.toString());
            }
            this.writeShort(n2);
        }
    }
    
    private final class StringTable extends AbstractList<String> implements RandomAccess
    {
        @Override
        public String get(final int n) {
            checkBounds(n, Dex.this.tableOfContents.stringIds.size);
            return Dex.this.open(Dex.this.tableOfContents.stringIds.off + n * 4).readString();
        }
        
        @Override
        public int size() {
            return Dex.this.tableOfContents.stringIds.size;
        }
    }
    
    private final class TypeIndexToDescriptorIndexTable extends AbstractList<Integer> implements RandomAccess
    {
        @Override
        public Integer get(final int n) {
            return Dex.this.descriptorIndexFromTypeIndex(n);
        }
        
        @Override
        public int size() {
            return Dex.this.tableOfContents.typeIds.size;
        }
    }
    
    private final class TypeIndexToDescriptorTable extends AbstractList<String> implements RandomAccess
    {
        @Override
        public String get(final int n) {
            return Dex.this.strings.get(Dex.this.descriptorIndexFromTypeIndex(n));
        }
        
        @Override
        public int size() {
            return Dex.this.tableOfContents.typeIds.size;
        }
    }
}
