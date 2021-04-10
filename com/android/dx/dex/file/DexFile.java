package com.android.dx.dex.file;

import com.android.dx.dex.*;
import java.util.zip.*;
import java.security.*;
import com.android.dex.util.*;
import com.android.dx.util.*;
import com.android.dx.rop.type.*;
import com.android.dx.rop.cst.*;
import java.io.*;

public final class DexFile
{
    private final MixedItemSection byteData;
    private final MixedItemSection classData;
    private final ClassDefsSection classDefs;
    private DexOptions dexOptions;
    private int dumpWidth;
    private final FieldIdsSection fieldIds;
    private int fileSize;
    private final HeaderSection header;
    private final MixedItemSection map;
    private final MethodIdsSection methodIds;
    private final ProtoIdsSection protoIds;
    private final Section[] sections;
    private final MixedItemSection stringData;
    private final StringIdsSection stringIds;
    private final TypeIdsSection typeIds;
    private final MixedItemSection typeLists;
    private final MixedItemSection wordData;
    
    public DexFile(final DexOptions dexOptions) {
        this.dexOptions = dexOptions;
        this.header = new HeaderSection(this);
        this.typeLists = new MixedItemSection(null, this, 4, MixedItemSection.SortType.NONE);
        this.wordData = new MixedItemSection("word_data", this, 4, MixedItemSection.SortType.TYPE);
        this.stringData = new MixedItemSection("string_data", this, 1, MixedItemSection.SortType.INSTANCE);
        this.classData = new MixedItemSection(null, this, 1, MixedItemSection.SortType.NONE);
        this.byteData = new MixedItemSection("byte_data", this, 1, MixedItemSection.SortType.TYPE);
        this.stringIds = new StringIdsSection(this);
        this.typeIds = new TypeIdsSection(this);
        this.protoIds = new ProtoIdsSection(this);
        this.fieldIds = new FieldIdsSection(this);
        this.methodIds = new MethodIdsSection(this);
        this.classDefs = new ClassDefsSection(this);
        this.map = new MixedItemSection("map", this, 4, MixedItemSection.SortType.NONE);
        this.sections = new Section[] { this.header, this.stringIds, this.typeIds, this.protoIds, this.fieldIds, this.methodIds, this.classDefs, this.wordData, this.typeLists, this.stringData, this.byteData, this.classData, this.map };
        this.fileSize = -1;
        this.dumpWidth = 79;
    }
    
    private static void calcChecksum(final byte[] array) {
        final Adler32 adler32 = new Adler32();
        adler32.update(array, 12, array.length - 12);
        final int n = (int)adler32.getValue();
        array[8] = (byte)n;
        array[9] = (byte)(n >> 8);
        array[10] = (byte)(n >> 16);
        array[11] = (byte)(n >> 24);
    }
    
    private static void calcSignature(final byte[] array) {
        try {
            final MessageDigest instance = MessageDigest.getInstance("SHA-1");
            instance.update(array, 32, array.length - 32);
            try {
                final int digest = instance.digest(array, 12, 20);
                if (digest != 20) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("unexpected digest write: ");
                    sb.append(digest);
                    sb.append(" bytes");
                    throw new RuntimeException(sb.toString());
                }
            }
            catch (DigestException ex) {
                throw new RuntimeException(ex);
            }
        }
        catch (NoSuchAlgorithmException ex2) {
            throw new RuntimeException(ex2);
        }
    }
    
    private ByteArrayAnnotatedOutput toDex0(final boolean b, final boolean b2) {
        this.classDefs.prepare();
        this.classData.prepare();
        this.wordData.prepare();
        this.byteData.prepare();
        this.methodIds.prepare();
        this.fieldIds.prepare();
        this.protoIds.prepare();
        this.typeLists.prepare();
        this.typeIds.prepare();
        this.stringIds.prepare();
        this.stringData.prepare();
        this.header.prepare();
        final int length = this.sections.length;
        final int n = 0;
        int n2 = 0;
        int i = 0;
        while (i < length) {
            final Section section = this.sections[i];
            final int setFileOffset = section.setFileOffset(n2);
            if (setFileOffset < n2) {
                final StringBuilder sb = new StringBuilder();
                sb.append("bogus placement for section ");
                sb.append(i);
                throw new RuntimeException(sb.toString());
            }
            try {
                if (section == this.map) {
                    MapItem.addMap(this.sections, this.map);
                    this.map.prepare();
                }
                if (section instanceof MixedItemSection) {
                    ((MixedItemSection)section).placeItems();
                }
                n2 = setFileOffset + section.writeSize();
                ++i;
                continue;
            }
            catch (RuntimeException ex) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("...while writing section ");
                sb2.append(i);
                throw ExceptionWithContext.withContext(ex, sb2.toString());
            }
            break;
        }
        this.fileSize = n2;
        final byte[] array = new byte[this.fileSize];
        final ByteArrayAnnotatedOutput byteArrayAnnotatedOutput = new ByteArrayAnnotatedOutput(array);
        if (b) {
            byteArrayAnnotatedOutput.enableAnnotations(this.dumpWidth, b2);
        }
        int j = n;
        while (j < length) {
            try {
                final Section section2 = this.sections[j];
                final int n3 = section2.getFileOffset() - byteArrayAnnotatedOutput.getCursor();
                if (n3 < 0) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("excess write of ");
                    sb3.append(-n3);
                    throw new ExceptionWithContext(sb3.toString());
                }
                byteArrayAnnotatedOutput.writeZeroes(section2.getFileOffset() - byteArrayAnnotatedOutput.getCursor());
                section2.writeTo(byteArrayAnnotatedOutput);
                ++j;
                continue;
            }
            catch (RuntimeException ex2) {
                ExceptionWithContext exceptionWithContext;
                if (ex2 instanceof ExceptionWithContext) {
                    exceptionWithContext = (ExceptionWithContext)ex2;
                }
                else {
                    exceptionWithContext = new ExceptionWithContext(ex2);
                }
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("...while writing section ");
                sb4.append(j);
                exceptionWithContext.addContext(sb4.toString());
                throw exceptionWithContext;
            }
            break;
        }
        if (byteArrayAnnotatedOutput.getCursor() != this.fileSize) {
            throw new RuntimeException("foreshortened write");
        }
        calcSignature(array);
        calcChecksum(array);
        if (b) {
            this.wordData.writeIndexAnnotation(byteArrayAnnotatedOutput, ItemType.TYPE_CODE_ITEM, "\nmethod code index:\n\n");
            this.getStatistics().writeAnnotation(byteArrayAnnotatedOutput);
            byteArrayAnnotatedOutput.finishAnnotating();
        }
        return byteArrayAnnotatedOutput;
    }
    
    public void add(final ClassDefItem classDefItem) {
        this.classDefs.add(classDefItem);
    }
    
    IndexedItem findItemOrNull(final Constant constant) {
        if (constant instanceof CstString) {
            return this.stringIds.get(constant);
        }
        if (constant instanceof CstType) {
            return this.typeIds.get(constant);
        }
        if (constant instanceof CstBaseMethodRef) {
            return this.methodIds.get(constant);
        }
        if (constant instanceof CstFieldRef) {
            return this.fieldIds.get(constant);
        }
        return null;
    }
    
    MixedItemSection getByteData() {
        return this.byteData;
    }
    
    MixedItemSection getClassData() {
        return this.classData;
    }
    
    public ClassDefsSection getClassDefs() {
        return this.classDefs;
    }
    
    public ClassDefItem getClassOrNull(final String s) {
        try {
            return (ClassDefItem)this.classDefs.get(new CstType(Type.internClassName(s)));
        }
        catch (IllegalArgumentException ex) {
            return null;
        }
    }
    
    public DexOptions getDexOptions() {
        return this.dexOptions;
    }
    
    public FieldIdsSection getFieldIds() {
        return this.fieldIds;
    }
    
    public int getFileSize() {
        if (this.fileSize < 0) {
            throw new RuntimeException("file size not yet known");
        }
        return this.fileSize;
    }
    
    Section getFirstDataSection() {
        return this.wordData;
    }
    
    Section getLastDataSection() {
        return this.map;
    }
    
    MixedItemSection getMap() {
        return this.map;
    }
    
    public MethodIdsSection getMethodIds() {
        return this.methodIds;
    }
    
    ProtoIdsSection getProtoIds() {
        return this.protoIds;
    }
    
    public Statistics getStatistics() {
        final Statistics statistics = new Statistics();
        final Section[] sections = this.sections;
        for (int length = sections.length, i = 0; i < length; ++i) {
            statistics.addAll(sections[i]);
        }
        return statistics;
    }
    
    MixedItemSection getStringData() {
        return this.stringData;
    }
    
    StringIdsSection getStringIds() {
        return this.stringIds;
    }
    
    public TypeIdsSection getTypeIds() {
        return this.typeIds;
    }
    
    MixedItemSection getTypeLists() {
        return this.typeLists;
    }
    
    MixedItemSection getWordData() {
        return this.wordData;
    }
    
    void internIfAppropriate(final Constant constant) {
        if (constant instanceof CstString) {
            this.stringIds.intern((CstString)constant);
            return;
        }
        if (constant instanceof CstType) {
            this.typeIds.intern((CstType)constant);
            return;
        }
        if (constant instanceof CstBaseMethodRef) {
            this.methodIds.intern((CstBaseMethodRef)constant);
            return;
        }
        if (constant instanceof CstFieldRef) {
            this.fieldIds.intern((CstFieldRef)constant);
            return;
        }
        if (constant instanceof CstEnumRef) {
            this.fieldIds.intern(((CstEnumRef)constant).getFieldRef());
            return;
        }
        if (constant == null) {
            throw new NullPointerException("cst == null");
        }
    }
    
    public boolean isEmpty() {
        return this.classDefs.items().isEmpty();
    }
    
    public void setDumpWidth(final int dumpWidth) {
        if (dumpWidth < 40) {
            throw new IllegalArgumentException("dumpWidth < 40");
        }
        this.dumpWidth = dumpWidth;
    }
    
    public byte[] toDex(final Writer writer, final boolean b) throws IOException {
        final boolean b2 = writer != null;
        final ByteArrayAnnotatedOutput dex0 = this.toDex0(b2, b);
        if (b2) {
            dex0.writeAnnotationsTo(writer);
        }
        return dex0.getArray();
    }
    
    public void writeTo(final OutputStream outputStream, final Writer writer, final boolean b) throws IOException {
        final boolean b2 = writer != null;
        final ByteArrayAnnotatedOutput dex0 = this.toDex0(b2, b);
        if (outputStream != null) {
            outputStream.write(dex0.getArray());
        }
        if (b2) {
            dex0.writeAnnotationsTo(writer);
        }
    }
}
