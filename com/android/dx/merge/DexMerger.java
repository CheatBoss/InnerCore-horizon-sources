package com.android.dx.merge;

import java.io.*;
import com.android.dex.*;
import com.android.dex.util.*;
import java.util.*;

public final class DexMerger
{
    private static final byte DBG_ADVANCE_LINE = 2;
    private static final byte DBG_ADVANCE_PC = 1;
    private static final byte DBG_END_LOCAL = 5;
    private static final byte DBG_END_SEQUENCE = 0;
    private static final byte DBG_RESTART_LOCAL = 6;
    private static final byte DBG_SET_EPILOGUE_BEGIN = 8;
    private static final byte DBG_SET_FILE = 9;
    private static final byte DBG_SET_PROLOGUE_END = 7;
    private static final byte DBG_START_LOCAL = 3;
    private static final byte DBG_START_LOCAL_EXTENDED = 4;
    private final Dex.Section annotationOut;
    private final Dex.Section annotationSetOut;
    private final Dex.Section annotationSetRefListOut;
    private final Dex.Section annotationsDirectoryOut;
    private final Dex.Section classDataOut;
    private final Dex.Section codeOut;
    private final CollisionPolicy collisionPolicy;
    private int compactWasteThreshold;
    private final TableOfContents contentsOut;
    private final Dex.Section debugInfoOut;
    private final Dex dexOut;
    private final Dex[] dexes;
    private final Dex.Section encodedArrayOut;
    private final Dex.Section headerOut;
    private final Dex.Section idsDefsOut;
    private final IndexMap[] indexMaps;
    private final InstructionTransformer instructionTransformer;
    private final Dex.Section mapListOut;
    private final Dex.Section stringDataOut;
    private final Dex.Section typeListOut;
    private final WriterSizes writerSizes;
    
    public DexMerger(final Dex[] array, final CollisionPolicy collisionPolicy) throws IOException {
        this(array, collisionPolicy, new WriterSizes(array));
    }
    
    private DexMerger(final Dex[] dexes, final CollisionPolicy collisionPolicy, final WriterSizes writerSizes) throws IOException {
        this.compactWasteThreshold = 1048576;
        this.dexes = dexes;
        this.collisionPolicy = collisionPolicy;
        this.writerSizes = writerSizes;
        this.dexOut = new Dex(writerSizes.size());
        this.indexMaps = new IndexMap[dexes.length];
        for (int i = 0; i < dexes.length; ++i) {
            this.indexMaps[i] = new IndexMap(this.dexOut, dexes[i].getTableOfContents());
        }
        this.instructionTransformer = new InstructionTransformer();
        this.headerOut = this.dexOut.appendSection(writerSizes.header, "header");
        this.idsDefsOut = this.dexOut.appendSection(writerSizes.idsDefs, "ids defs");
        this.contentsOut = this.dexOut.getTableOfContents();
        this.contentsOut.dataOff = this.dexOut.getNextSectionStart();
        this.contentsOut.mapList.off = this.dexOut.getNextSectionStart();
        this.contentsOut.mapList.size = 1;
        this.mapListOut = this.dexOut.appendSection(writerSizes.mapList, "map list");
        this.contentsOut.typeLists.off = this.dexOut.getNextSectionStart();
        this.typeListOut = this.dexOut.appendSection(writerSizes.typeList, "type list");
        this.contentsOut.annotationSetRefLists.off = this.dexOut.getNextSectionStart();
        this.annotationSetRefListOut = this.dexOut.appendSection(writerSizes.annotationsSetRefList, "annotation set ref list");
        this.contentsOut.annotationSets.off = this.dexOut.getNextSectionStart();
        this.annotationSetOut = this.dexOut.appendSection(writerSizes.annotationsSet, "annotation sets");
        this.contentsOut.classDatas.off = this.dexOut.getNextSectionStart();
        this.classDataOut = this.dexOut.appendSection(writerSizes.classData, "class data");
        this.contentsOut.codes.off = this.dexOut.getNextSectionStart();
        this.codeOut = this.dexOut.appendSection(writerSizes.code, "code");
        this.contentsOut.stringDatas.off = this.dexOut.getNextSectionStart();
        this.stringDataOut = this.dexOut.appendSection(writerSizes.stringData, "string data");
        this.contentsOut.debugInfos.off = this.dexOut.getNextSectionStart();
        this.debugInfoOut = this.dexOut.appendSection(writerSizes.debugInfo, "debug info");
        this.contentsOut.annotations.off = this.dexOut.getNextSectionStart();
        this.annotationOut = this.dexOut.appendSection(writerSizes.annotation, "annotation");
        this.contentsOut.encodedArrays.off = this.dexOut.getNextSectionStart();
        this.encodedArrayOut = this.dexOut.appendSection(writerSizes.encodedArray, "encoded array");
        this.contentsOut.annotationsDirectories.off = this.dexOut.getNextSectionStart();
        this.annotationsDirectoryOut = this.dexOut.appendSection(writerSizes.annotationsDirectory, "annotations directory");
        this.contentsOut.dataSize = this.dexOut.getNextSectionStart() - this.contentsOut.dataOff;
    }
    
    private SortableType[] getSortedTypes() {
        final SortableType[] array = new SortableType[this.contentsOut.typeIds.size];
        for (int i = 0; i < this.dexes.length; ++i) {
            this.readSortableTypes(array, this.dexes[i], this.indexMaps[i]);
        }
        boolean b;
        do {
            final int length = array.length;
            b = true;
            boolean b2;
            for (int j = 0; j < length; ++j, b = b2) {
                final SortableType sortableType = array[j];
                b2 = b;
                if (sortableType != null) {
                    b2 = b;
                    if (!sortableType.isDepthAssigned()) {
                        b2 = (b & sortableType.tryAssignDepth(array));
                    }
                }
            }
        } while (!b);
        Arrays.sort(array, SortableType.NULLS_LAST_ORDER);
        final int index = Arrays.asList(array).indexOf(null);
        if (index != -1) {
            return Arrays.copyOfRange(array, 0, index);
        }
        return array;
    }
    
    public static void main(final String[] array) throws IOException {
        if (array.length < 2) {
            printUsage();
            return;
        }
        final int length = array.length;
        int i = 1;
        final Dex[] array2 = new Dex[length - 1];
        while (i < array.length) {
            array2[i - 1] = new Dex(new File(array[i]));
            ++i;
        }
        new DexMerger(array2, CollisionPolicy.KEEP_FIRST).merge().writeTo(new File(array[0]));
    }
    
    private void mergeAnnotations() {
        ((IdMerger)new IdMerger<Annotation>(this.annotationOut) {
            @Override
            TableOfContents.Section getSection(final TableOfContents tableOfContents) {
                return tableOfContents.annotations;
            }
            
            Annotation read(final Dex.Section section, final IndexMap indexMap, final int n) {
                return indexMap.adjust(section.readAnnotation());
            }
            
            @Override
            void updateIndex(final int n, final IndexMap indexMap, final int n2, final int n3) {
                indexMap.putAnnotationOffset(n, DexMerger.this.annotationOut.getPosition());
            }
            
            void write(final Annotation annotation) {
                annotation.writeTo(DexMerger.this.annotationOut);
            }
        }).mergeUnsorted();
    }
    
    private void mergeClassDefs() {
        final SortableType[] sortedTypes = this.getSortedTypes();
        this.contentsOut.classDefs.off = this.idsDefsOut.getPosition();
        this.contentsOut.classDefs.size = sortedTypes.length;
        for (int length = sortedTypes.length, i = 0; i < length; ++i) {
            final SortableType sortableType = sortedTypes[i];
            this.transformClassDef(sortableType.getDex(), sortableType.getClassDef(), sortableType.getIndexMap());
        }
    }
    
    private Dex mergeDexes() throws IOException {
        this.mergeStringIds();
        this.mergeTypeIds();
        this.mergeTypeLists();
        this.mergeProtoIds();
        this.mergeFieldIds();
        this.mergeMethodIds();
        this.mergeAnnotations();
        this.unionAnnotationSetsAndDirectories();
        this.mergeClassDefs();
        this.contentsOut.header.off = 0;
        this.contentsOut.header.size = 1;
        this.contentsOut.fileSize = this.dexOut.getLength();
        this.contentsOut.computeSizesFromOffsets();
        this.contentsOut.writeHeader(this.headerOut);
        this.contentsOut.writeMap(this.mapListOut);
        this.dexOut.writeHashes();
        return this.dexOut;
    }
    
    private void mergeFieldIds() {
        ((IdMerger)new IdMerger<FieldId>(this.idsDefsOut) {
            @Override
            TableOfContents.Section getSection(final TableOfContents tableOfContents) {
                return tableOfContents.fieldIds;
            }
            
            FieldId read(final Dex.Section section, final IndexMap indexMap, final int n) {
                return indexMap.adjust(section.readFieldId());
            }
            
            @Override
            void updateIndex(final int n, final IndexMap indexMap, final int n2, final int n3) {
                if (n3 >= 0 && n3 <= 65535) {
                    indexMap.fieldIds[n2] = (short)n3;
                    return;
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("field ID not in [0, 0xffff]: ");
                sb.append(n3);
                throw new DexIndexOverflowException(sb.toString());
            }
            
            void write(final FieldId fieldId) {
                fieldId.writeTo(DexMerger.this.idsDefsOut);
            }
        }).mergeSorted();
    }
    
    private void mergeMethodIds() {
        ((IdMerger)new IdMerger<MethodId>(this.idsDefsOut) {
            @Override
            TableOfContents.Section getSection(final TableOfContents tableOfContents) {
                return tableOfContents.methodIds;
            }
            
            MethodId read(final Dex.Section section, final IndexMap indexMap, final int n) {
                return indexMap.adjust(section.readMethodId());
            }
            
            @Override
            void updateIndex(final int n, final IndexMap indexMap, final int n2, final int n3) {
                if (n3 >= 0 && n3 <= 65535) {
                    indexMap.methodIds[n2] = (short)n3;
                    return;
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("method ID not in [0, 0xffff]: ");
                sb.append(n3);
                throw new DexIndexOverflowException(sb.toString());
            }
            
            void write(final MethodId methodId) {
                methodId.writeTo(DexMerger.this.idsDefsOut);
            }
        }).mergeSorted();
    }
    
    private void mergeProtoIds() {
        ((IdMerger)new IdMerger<ProtoId>(this.idsDefsOut) {
            @Override
            TableOfContents.Section getSection(final TableOfContents tableOfContents) {
                return tableOfContents.protoIds;
            }
            
            ProtoId read(final Dex.Section section, final IndexMap indexMap, final int n) {
                return indexMap.adjust(section.readProtoId());
            }
            
            @Override
            void updateIndex(final int n, final IndexMap indexMap, final int n2, final int n3) {
                if (n3 >= 0 && n3 <= 65535) {
                    indexMap.protoIds[n2] = (short)n3;
                    return;
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("proto ID not in [0, 0xffff]: ");
                sb.append(n3);
                throw new DexIndexOverflowException(sb.toString());
            }
            
            void write(final ProtoId protoId) {
                protoId.writeTo(DexMerger.this.idsDefsOut);
            }
        }).mergeSorted();
    }
    
    private void mergeStringIds() {
        ((IdMerger)new IdMerger<String>(this.idsDefsOut) {
            @Override
            TableOfContents.Section getSection(final TableOfContents tableOfContents) {
                return tableOfContents.stringIds;
            }
            
            String read(final Dex.Section section, final IndexMap indexMap, final int n) {
                return section.readString();
            }
            
            @Override
            void updateIndex(final int n, final IndexMap indexMap, final int n2, final int n3) {
                indexMap.stringIds[n2] = n3;
            }
            
            void write(final String s) {
                final TableOfContents.Section stringDatas = DexMerger.this.contentsOut.stringDatas;
                ++stringDatas.size;
                DexMerger.this.idsDefsOut.writeInt(DexMerger.this.stringDataOut.getPosition());
                DexMerger.this.stringDataOut.writeStringData(s);
            }
        }).mergeSorted();
    }
    
    private void mergeTypeIds() {
        ((IdMerger)new IdMerger<Integer>(this.idsDefsOut) {
            @Override
            TableOfContents.Section getSection(final TableOfContents tableOfContents) {
                return tableOfContents.typeIds;
            }
            
            Integer read(final Dex.Section section, final IndexMap indexMap, final int n) {
                return indexMap.adjustString(section.readInt());
            }
            
            @Override
            void updateIndex(final int n, final IndexMap indexMap, final int n2, final int n3) {
                if (n3 >= 0 && n3 <= 65535) {
                    indexMap.typeIds[n2] = (short)n3;
                    return;
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("type ID not in [0, 0xffff]: ");
                sb.append(n3);
                throw new DexIndexOverflowException(sb.toString());
            }
            
            void write(final Integer n) {
                DexMerger.this.idsDefsOut.writeInt(n);
            }
        }).mergeSorted();
    }
    
    private void mergeTypeLists() {
        ((IdMerger)new IdMerger<TypeList>(this.typeListOut) {
            @Override
            TableOfContents.Section getSection(final TableOfContents tableOfContents) {
                return tableOfContents.typeLists;
            }
            
            TypeList read(final Dex.Section section, final IndexMap indexMap, final int n) {
                return indexMap.adjustTypeList(section.readTypeList());
            }
            
            @Override
            void updateIndex(final int n, final IndexMap indexMap, final int n2, final int n3) {
                indexMap.putTypeListOffset(n, DexMerger.this.typeListOut.getPosition());
            }
            
            void write(final TypeList list) {
                DexMerger.this.typeListOut.writeTypeList(list);
            }
        }).mergeUnsorted();
    }
    
    private static void printUsage() {
        System.out.println("Usage: DexMerger <out.dex> <a.dex> <b.dex> ...");
        System.out.println();
        System.out.println("If a class is defined in several dex, the class found in the first dex will be used.");
    }
    
    private void readSortableTypes(final SortableType[] array, final Dex dex, final IndexMap indexMap) {
        for (final ClassDef classDef : dex.classDefs()) {
            final SortableType adjust = indexMap.adjust(new SortableType(dex, indexMap, classDef));
            final int typeIndex = adjust.getTypeIndex();
            if (array[typeIndex] == null) {
                array[typeIndex] = adjust;
            }
            else {
                if (this.collisionPolicy != CollisionPolicy.KEEP_FIRST) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Multiple dex files define ");
                    sb.append(dex.typeNames().get(classDef.getTypeIndex()));
                    throw new DexException(sb.toString());
                }
                continue;
            }
        }
    }
    
    private void transformAnnotationDirectories(final Dex dex, final IndexMap indexMap) {
        final TableOfContents.Section annotationsDirectories = dex.getTableOfContents().annotationsDirectories;
        if (annotationsDirectories.exists()) {
            final Dex.Section open = dex.open(annotationsDirectories.off);
            for (int i = 0; i < annotationsDirectories.size; ++i) {
                this.transformAnnotationDirectory(open, indexMap);
            }
        }
    }
    
    private void transformAnnotationDirectory(final Dex.Section section, final IndexMap indexMap) {
        final TableOfContents.Section annotationsDirectories = this.contentsOut.annotationsDirectories;
        ++annotationsDirectories.size;
        this.annotationsDirectoryOut.assertFourByteAligned();
        indexMap.putAnnotationDirectoryOffset(section.getPosition(), this.annotationsDirectoryOut.getPosition());
        this.annotationsDirectoryOut.writeInt(indexMap.adjustAnnotationSet(section.readInt()));
        final int int1 = section.readInt();
        this.annotationsDirectoryOut.writeInt(int1);
        final int int2 = section.readInt();
        this.annotationsDirectoryOut.writeInt(int2);
        final int int3 = section.readInt();
        this.annotationsDirectoryOut.writeInt(int3);
        final int n = 0;
        for (int i = 0; i < int1; ++i) {
            this.annotationsDirectoryOut.writeInt(indexMap.adjustField(section.readInt()));
            this.annotationsDirectoryOut.writeInt(indexMap.adjustAnnotationSet(section.readInt()));
        }
        for (int j = 0; j < int2; ++j) {
            this.annotationsDirectoryOut.writeInt(indexMap.adjustMethod(section.readInt()));
            this.annotationsDirectoryOut.writeInt(indexMap.adjustAnnotationSet(section.readInt()));
        }
        for (int k = n; k < int3; ++k) {
            this.annotationsDirectoryOut.writeInt(indexMap.adjustMethod(section.readInt()));
            this.annotationsDirectoryOut.writeInt(indexMap.adjustAnnotationSetRefList(section.readInt()));
        }
    }
    
    private void transformAnnotationSet(final IndexMap indexMap, final Dex.Section section) {
        final TableOfContents.Section annotationSets = this.contentsOut.annotationSets;
        ++annotationSets.size;
        this.annotationSetOut.assertFourByteAligned();
        indexMap.putAnnotationSetOffset(section.getPosition(), this.annotationSetOut.getPosition());
        final int int1 = section.readInt();
        this.annotationSetOut.writeInt(int1);
        for (int i = 0; i < int1; ++i) {
            this.annotationSetOut.writeInt(indexMap.adjustAnnotation(section.readInt()));
        }
    }
    
    private void transformAnnotationSetRefList(final IndexMap indexMap, final Dex.Section section) {
        final TableOfContents.Section annotationSetRefLists = this.contentsOut.annotationSetRefLists;
        ++annotationSetRefLists.size;
        this.annotationSetRefListOut.assertFourByteAligned();
        indexMap.putAnnotationSetRefListOffset(section.getPosition(), this.annotationSetRefListOut.getPosition());
        final int int1 = section.readInt();
        this.annotationSetRefListOut.writeInt(int1);
        for (int i = 0; i < int1; ++i) {
            this.annotationSetRefListOut.writeInt(indexMap.adjustAnnotationSet(section.readInt()));
        }
    }
    
    private void transformAnnotationSetRefLists(final Dex dex, final IndexMap indexMap) {
        final TableOfContents.Section annotationSetRefLists = dex.getTableOfContents().annotationSetRefLists;
        if (annotationSetRefLists.exists()) {
            final Dex.Section open = dex.open(annotationSetRefLists.off);
            for (int i = 0; i < annotationSetRefLists.size; ++i) {
                this.transformAnnotationSetRefList(indexMap, open);
            }
        }
    }
    
    private void transformAnnotationSets(final Dex dex, final IndexMap indexMap) {
        final TableOfContents.Section annotationSets = dex.getTableOfContents().annotationSets;
        if (annotationSets.exists()) {
            final Dex.Section open = dex.open(annotationSets.off);
            for (int i = 0; i < annotationSets.size; ++i) {
                this.transformAnnotationSet(indexMap, open);
            }
        }
    }
    
    private int[] transformCatchHandlers(final IndexMap indexMap, final Code.CatchHandler[] array) {
        final int position = this.codeOut.getPosition();
        this.codeOut.writeUleb128(array.length);
        final int[] array2 = new int[array.length];
        for (int i = 0; i < array.length; ++i) {
            array2[i] = this.codeOut.getPosition() - position;
            this.transformEncodedCatchHandler(array[i], indexMap);
        }
        return array2;
    }
    
    private void transformClassData(final Dex dex, final ClassData classData, final IndexMap indexMap) {
        final TableOfContents.Section classDatas = this.contentsOut.classDatas;
        ++classDatas.size;
        final ClassData.Field[] staticFields = classData.getStaticFields();
        final ClassData.Field[] instanceFields = classData.getInstanceFields();
        final ClassData.Method[] directMethods = classData.getDirectMethods();
        final ClassData.Method[] virtualMethods = classData.getVirtualMethods();
        this.classDataOut.writeUleb128(staticFields.length);
        this.classDataOut.writeUleb128(instanceFields.length);
        this.classDataOut.writeUleb128(directMethods.length);
        this.classDataOut.writeUleb128(virtualMethods.length);
        this.transformFields(indexMap, staticFields);
        this.transformFields(indexMap, instanceFields);
        this.transformMethods(dex, indexMap, directMethods);
        this.transformMethods(dex, indexMap, virtualMethods);
    }
    
    private void transformClassDef(final Dex dex, final ClassDef classDef, final IndexMap indexMap) {
        this.idsDefsOut.assertFourByteAligned();
        this.idsDefsOut.writeInt(classDef.getTypeIndex());
        this.idsDefsOut.writeInt(classDef.getAccessFlags());
        this.idsDefsOut.writeInt(classDef.getSupertypeIndex());
        this.idsDefsOut.writeInt(classDef.getInterfacesOffset());
        this.idsDefsOut.writeInt(indexMap.adjustString(classDef.getSourceFileIndex()));
        this.idsDefsOut.writeInt(indexMap.adjustAnnotationDirectory(classDef.getAnnotationsOffset()));
        if (classDef.getClassDataOffset() == 0) {
            this.idsDefsOut.writeInt(0);
        }
        else {
            this.idsDefsOut.writeInt(this.classDataOut.getPosition());
            this.transformClassData(dex, dex.readClassData(classDef), indexMap);
        }
        this.idsDefsOut.writeInt(indexMap.adjustStaticValues(classDef.getStaticValuesOffset()));
    }
    
    private void transformCode(final Dex dex, final Code code, final IndexMap indexMap) {
        final TableOfContents.Section codes = this.contentsOut.codes;
        ++codes.size;
        this.codeOut.assertFourByteAligned();
        this.codeOut.writeUnsignedShort(code.getRegistersSize());
        this.codeOut.writeUnsignedShort(code.getInsSize());
        this.codeOut.writeUnsignedShort(code.getOutsSize());
        final Code.Try[] tries = code.getTries();
        final Code.CatchHandler[] catchHandlers = code.getCatchHandlers();
        this.codeOut.writeUnsignedShort(tries.length);
        final int debugInfoOffset = code.getDebugInfoOffset();
        if (debugInfoOffset != 0) {
            this.codeOut.writeInt(this.debugInfoOut.getPosition());
            this.transformDebugInfoItem(dex.open(debugInfoOffset), indexMap);
        }
        else {
            this.codeOut.writeInt(0);
        }
        final short[] transform = this.instructionTransformer.transform(indexMap, code.getInstructions());
        this.codeOut.writeInt(transform.length);
        this.codeOut.write(transform);
        if (tries.length > 0) {
            if (transform.length % 2 == 1) {
                this.codeOut.writeShort((short)0);
            }
            final Dex.Section open = this.dexOut.open(this.codeOut.getPosition());
            this.codeOut.skip(tries.length * 8);
            this.transformTries(open, tries, this.transformCatchHandlers(indexMap, catchHandlers));
        }
    }
    
    private void transformDebugInfoItem(final Dex.Section section, final IndexMap indexMap) {
        final TableOfContents.Section debugInfos = this.contentsOut.debugInfos;
        ++debugInfos.size;
        this.debugInfoOut.writeUleb128(section.readUleb128());
        final int uleb128 = section.readUleb128();
        this.debugInfoOut.writeUleb128(uleb128);
        for (int i = 0; i < uleb128; ++i) {
            this.debugInfoOut.writeUleb128p1(indexMap.adjustString(section.readUleb128p1()));
        }
    Label_0273:
        while (true) {
            final byte byte1 = section.readByte();
            this.debugInfoOut.writeByte(byte1);
            if (byte1 != 9) {
                switch (byte1) {
                    default: {
                        continue;
                    }
                    case 5:
                    case 6: {
                        this.debugInfoOut.writeUleb128(section.readUleb128());
                        continue;
                    }
                    case 3:
                    case 4: {
                        this.debugInfoOut.writeUleb128(section.readUleb128());
                        this.debugInfoOut.writeUleb128p1(indexMap.adjustString(section.readUleb128p1()));
                        this.debugInfoOut.writeUleb128p1(indexMap.adjustType(section.readUleb128p1()));
                        if (byte1 == 4) {
                            this.debugInfoOut.writeUleb128p1(indexMap.adjustString(section.readUleb128p1()));
                            continue;
                        }
                        continue;
                    }
                    case 2: {
                        this.debugInfoOut.writeSleb128(section.readSleb128());
                        continue;
                    }
                    case 1: {
                        this.debugInfoOut.writeUleb128(section.readUleb128());
                        continue;
                    }
                    case 0: {
                        break Label_0273;
                    }
                }
            }
            else {
                this.debugInfoOut.writeUleb128p1(indexMap.adjustString(section.readUleb128p1()));
            }
        }
    }
    
    private void transformEncodedCatchHandler(final Code.CatchHandler catchHandler, final IndexMap indexMap) {
        final int catchAllAddress = catchHandler.getCatchAllAddress();
        final int[] typeIndexes = catchHandler.getTypeIndexes();
        final int[] addresses = catchHandler.getAddresses();
        if (catchAllAddress != -1) {
            this.codeOut.writeSleb128(-typeIndexes.length);
        }
        else {
            this.codeOut.writeSleb128(typeIndexes.length);
        }
        for (int i = 0; i < typeIndexes.length; ++i) {
            this.codeOut.writeUleb128(indexMap.adjustType(typeIndexes[i]));
            this.codeOut.writeUleb128(addresses[i]);
        }
        if (catchAllAddress != -1) {
            this.codeOut.writeUleb128(catchAllAddress);
        }
    }
    
    private void transformFields(final IndexMap indexMap, final ClassData.Field[] array) {
        int n = 0;
        for (int length = array.length, i = 0; i < length; ++i) {
            final ClassData.Field field = array[i];
            final int adjustField = indexMap.adjustField(field.getFieldIndex());
            this.classDataOut.writeUleb128(adjustField - n);
            n = adjustField;
            this.classDataOut.writeUleb128(field.getAccessFlags());
        }
    }
    
    private void transformMethods(final Dex dex, final IndexMap indexMap, final ClassData.Method[] array) {
        final int length = array.length;
        int n = 0;
        for (int i = 0; i < length; ++i) {
            final ClassData.Method method = array[i];
            final int adjustMethod = indexMap.adjustMethod(method.getMethodIndex());
            this.classDataOut.writeUleb128(adjustMethod - n);
            n = adjustMethod;
            this.classDataOut.writeUleb128(method.getAccessFlags());
            if (method.getCodeOffset() == 0) {
                this.classDataOut.writeUleb128(0);
            }
            else {
                this.codeOut.alignToFourBytesWithZeroFill();
                this.classDataOut.writeUleb128(this.codeOut.getPosition());
                this.transformCode(dex, dex.readCode(method), indexMap);
            }
        }
    }
    
    private void transformStaticValues(final Dex.Section section, final IndexMap indexMap) {
        final TableOfContents.Section encodedArrays = this.contentsOut.encodedArrays;
        ++encodedArrays.size;
        indexMap.putStaticValuesOffset(section.getPosition(), this.encodedArrayOut.getPosition());
        indexMap.adjustEncodedArray(section.readEncodedArray()).writeTo(this.encodedArrayOut);
    }
    
    private void transformStaticValues(final Dex dex, final IndexMap indexMap) {
        final TableOfContents.Section encodedArrays = dex.getTableOfContents().encodedArrays;
        if (encodedArrays.exists()) {
            final Dex.Section open = dex.open(encodedArrays.off);
            for (int i = 0; i < encodedArrays.size; ++i) {
                this.transformStaticValues(open, indexMap);
            }
        }
    }
    
    private void transformTries(final Dex.Section section, final Code.Try[] array, final int[] array2) {
        for (int length = array.length, i = 0; i < length; ++i) {
            final Code.Try try1 = array[i];
            section.writeInt(try1.getStartAddress());
            section.writeUnsignedShort(try1.getInstructionCount());
            section.writeUnsignedShort(array2[try1.getCatchHandlerIndex()]);
        }
    }
    
    private void unionAnnotationSetsAndDirectories() {
        final int n = 0;
        for (int i = 0; i < this.dexes.length; ++i) {
            this.transformAnnotationSets(this.dexes[i], this.indexMaps[i]);
        }
        for (int j = 0; j < this.dexes.length; ++j) {
            this.transformAnnotationSetRefLists(this.dexes[j], this.indexMaps[j]);
        }
        for (int k = 0; k < this.dexes.length; ++k) {
            this.transformAnnotationDirectories(this.dexes[k], this.indexMaps[k]);
        }
        for (int l = n; l < this.dexes.length; ++l) {
            this.transformStaticValues(this.dexes[l], this.indexMaps[l]);
        }
    }
    
    public Dex merge() throws IOException {
        if (this.dexes.length == 1) {
            return this.dexes[0];
        }
        if (this.dexes.length == 0) {
            return null;
        }
        final long nanoTime = System.nanoTime();
        Dex dex = this.mergeDexes();
        final WriterSizes writerSizes = new WriterSizes(this);
        final int n = this.writerSizes.size() - writerSizes.size();
        if (n > this.compactWasteThreshold) {
            dex = new DexMerger(new Dex[] { this.dexOut, new Dex(0) }, CollisionPolicy.FAIL, writerSizes).mergeDexes();
            System.out.printf("Result compacted from %.1fKiB to %.1fKiB to save %.1fKiB%n", this.dexOut.getLength() / 1024.0f, dex.getLength() / 1024.0f, n / 1024.0f);
        }
        final long nanoTime2 = System.nanoTime();
        for (int i = 0; i < this.dexes.length; ++i) {
            System.out.printf("Merged dex #%d (%d defs/%.1fKiB)%n", i + 1, this.dexes[i].getTableOfContents().classDefs.size, this.dexes[i].getLength() / 1024.0f);
        }
        System.out.printf("Result is %d defs/%.1fKiB. Took %.1fs%n", dex.getTableOfContents().classDefs.size, dex.getLength() / 1024.0f, (nanoTime2 - nanoTime) / 1.0E9f);
        return dex;
    }
    
    public void setCompactWasteThreshold(final int compactWasteThreshold) {
        this.compactWasteThreshold = compactWasteThreshold;
    }
    
    abstract class IdMerger<T extends Comparable<T>>
    {
        private final Dex.Section out;
        
        protected IdMerger(final Dex.Section out) {
            this.out = out;
        }
        
        private int readIntoMap(final Dex.Section section, final TableOfContents.Section section2, final IndexMap indexMap, final int n, final TreeMap<T, List<Integer>> treeMap, final int n2) {
            int position;
            if (section != null) {
                position = section.getPosition();
            }
            else {
                position = -1;
            }
            if (n < section2.size) {
                final Comparable<Comparable<T>> read = (Comparable<Comparable<T>>)this.read(section, indexMap, n);
                List<Integer> list;
                if ((list = treeMap.get(read)) == null) {
                    list = new ArrayList<Integer>();
                    treeMap.put((T)read, list);
                }
                list.add(new Integer(n2));
            }
            return position;
        }
        
        private List<UnsortedValue> readUnsortedValues(final Dex dex, final IndexMap indexMap) {
            final TableOfContents.Section section = this.getSection(dex.getTableOfContents());
            if (!section.exists()) {
                return Collections.emptyList();
            }
            final ArrayList<UnsortedValue> list = new ArrayList<UnsortedValue>();
            final Dex.Section open = dex.open(section.off);
            for (int i = 0; i < section.size; ++i) {
                list.add(new UnsortedValue(dex, indexMap, this.read(open, indexMap, 0), i, open.getPosition()));
            }
            return list;
        }
        
        abstract TableOfContents.Section getSection(final TableOfContents p0);
        
        public final void mergeSorted() {
            final TableOfContents.Section[] array = new TableOfContents.Section[DexMerger.this.dexes.length];
            final Dex.Section[] array2 = new Dex.Section[DexMerger.this.dexes.length];
            final int[] array3 = new int[DexMerger.this.dexes.length];
            final int[] array4 = new int[DexMerger.this.dexes.length];
            final TreeMap<T, List<Integer>> treeMap = new TreeMap<T, List<Integer>>();
            final int n = 0;
            for (int i = 0; i < DexMerger.this.dexes.length; ++i) {
                array[i] = this.getSection(DexMerger.this.dexes[i].getTableOfContents());
                ByteInput open;
                if (array[i].exists()) {
                    open = DexMerger.this.dexes[i].open(array[i].off);
                }
                else {
                    open = null;
                }
                array2[i] = (Dex.Section)open;
                array3[i] = this.readIntoMap(array2[i], array[i], DexMerger.this.indexMaps[i], array4[i], treeMap, i);
            }
            this.getSection(DexMerger.this.contentsOut).off = this.out.getPosition();
            int size = n;
            while (!treeMap.isEmpty()) {
                final Map.Entry<T, List<Integer>> pollFirstEntry = treeMap.pollFirstEntry();
                for (final Integer n2 : pollFirstEntry.getValue()) {
                    this.updateIndex(array3[n2], DexMerger.this.indexMaps[n2], array4[n2]++, size);
                    array3[n2] = this.readIntoMap(array2[n2], array[n2], DexMerger.this.indexMaps[n2], array4[n2], treeMap, n2);
                }
                this.write(pollFirstEntry.getKey());
                ++size;
            }
            this.getSection(DexMerger.this.contentsOut).size = size;
        }
        
        public final void mergeUnsorted() {
            this.getSection(DexMerger.this.contentsOut).off = this.out.getPosition();
            final ArrayList<Comparable> list = new ArrayList<Comparable>();
            final int n = 0;
            for (int i = 0; i < DexMerger.this.dexes.length; ++i) {
                list.addAll((Collection<?>)this.readUnsortedValues(DexMerger.this.dexes[i], DexMerger.this.indexMaps[i]));
            }
            Collections.sort(list);
            int size = 0;
            int j = n;
            while (j < list.size()) {
                final int n2 = j + 1;
                final UnsortedValue unsortedValue = list.get(j);
                this.updateIndex(unsortedValue.offset, unsortedValue.indexMap, unsortedValue.index, size - 1);
                for (j = n2; j < list.size() && unsortedValue.compareTo((UnsortedValue)list.get(j)) == 0; ++j) {
                    final UnsortedValue unsortedValue2 = list.get(j);
                    this.updateIndex(unsortedValue2.offset, unsortedValue2.indexMap, unsortedValue2.index, size - 1);
                }
                this.write(unsortedValue.value);
                ++size;
            }
            this.getSection(DexMerger.this.contentsOut).size = size;
        }
        
        abstract T read(final Dex.Section p0, final IndexMap p1, final int p2);
        
        abstract void updateIndex(final int p0, final IndexMap p1, final int p2, final int p3);
        
        abstract void write(final T p0);
        
        class UnsortedValue implements Comparable<UnsortedValue>
        {
            final int index;
            final IndexMap indexMap;
            final int offset;
            final Dex source;
            final T value;
            
            UnsortedValue(final Dex source, final IndexMap indexMap, final T value, final int index, final int offset) {
                this.source = source;
                this.indexMap = indexMap;
                this.value = value;
                this.index = index;
                this.offset = offset;
            }
            
            @Override
            public int compareTo(final UnsortedValue unsortedValue) {
                return this.value.compareTo(unsortedValue.value);
            }
        }
    }
    
    private static class WriterSizes
    {
        private int annotation;
        private int annotationsDirectory;
        private int annotationsSet;
        private int annotationsSetRefList;
        private int classData;
        private int code;
        private int debugInfo;
        private int encodedArray;
        private int header;
        private int idsDefs;
        private int mapList;
        private int stringData;
        private int typeList;
        
        public WriterSizes(final DexMerger dexMerger) {
            this.header = 112;
            this.header = dexMerger.headerOut.used();
            this.idsDefs = dexMerger.idsDefsOut.used();
            this.mapList = dexMerger.mapListOut.used();
            this.typeList = dexMerger.typeListOut.used();
            this.classData = dexMerger.classDataOut.used();
            this.code = dexMerger.codeOut.used();
            this.stringData = dexMerger.stringDataOut.used();
            this.debugInfo = dexMerger.debugInfoOut.used();
            this.encodedArray = dexMerger.encodedArrayOut.used();
            this.annotationsDirectory = dexMerger.annotationsDirectoryOut.used();
            this.annotationsSet = dexMerger.annotationSetOut.used();
            this.annotationsSetRefList = dexMerger.annotationSetRefListOut.used();
            this.annotation = dexMerger.annotationOut.used();
            this.fourByteAlign();
        }
        
        public WriterSizes(final Dex[] array) {
            this.header = 112;
            for (int i = 0; i < array.length; ++i) {
                this.plus(array[i].getTableOfContents(), false);
            }
            this.fourByteAlign();
        }
        
        private static int fourByteAlign(final int n) {
            return n + 3 & 0xFFFFFFFC;
        }
        
        private void fourByteAlign() {
            this.header = fourByteAlign(this.header);
            this.idsDefs = fourByteAlign(this.idsDefs);
            this.mapList = fourByteAlign(this.mapList);
            this.typeList = fourByteAlign(this.typeList);
            this.classData = fourByteAlign(this.classData);
            this.code = fourByteAlign(this.code);
            this.stringData = fourByteAlign(this.stringData);
            this.debugInfo = fourByteAlign(this.debugInfo);
            this.encodedArray = fourByteAlign(this.encodedArray);
            this.annotationsDirectory = fourByteAlign(this.annotationsDirectory);
            this.annotationsSet = fourByteAlign(this.annotationsSet);
            this.annotationsSetRefList = fourByteAlign(this.annotationsSetRefList);
            this.annotation = fourByteAlign(this.annotation);
        }
        
        private void plus(final TableOfContents tableOfContents, final boolean b) {
            this.idsDefs += tableOfContents.stringIds.size * 4 + tableOfContents.typeIds.size * 4 + tableOfContents.protoIds.size * 12 + tableOfContents.fieldIds.size * 8 + tableOfContents.methodIds.size * 8 + tableOfContents.classDefs.size * 32;
            this.mapList = tableOfContents.sections.length * 12 + 4;
            this.typeList += fourByteAlign(tableOfContents.typeLists.byteCount);
            this.stringData += tableOfContents.stringDatas.byteCount;
            this.annotationsDirectory += tableOfContents.annotationsDirectories.byteCount;
            this.annotationsSet += tableOfContents.annotationSets.byteCount;
            this.annotationsSetRefList += tableOfContents.annotationSetRefLists.byteCount;
            if (b) {
                this.code += tableOfContents.codes.byteCount;
                this.classData += tableOfContents.classDatas.byteCount;
                this.encodedArray += tableOfContents.encodedArrays.byteCount;
                this.annotation += tableOfContents.annotations.byteCount;
                this.debugInfo += tableOfContents.debugInfos.byteCount;
                return;
            }
            this.code += (int)Math.ceil(tableOfContents.codes.byteCount * 1.25);
            this.classData += (int)Math.ceil(tableOfContents.classDatas.byteCount * 1.34);
            this.encodedArray += tableOfContents.encodedArrays.byteCount * 2;
            this.annotation += (int)Math.ceil(tableOfContents.annotations.byteCount * 2);
            this.debugInfo += tableOfContents.debugInfos.byteCount * 2;
        }
        
        public int size() {
            return this.header + this.idsDefs + this.mapList + this.typeList + this.classData + this.code + this.stringData + this.debugInfo + this.encodedArray + this.annotationsDirectory + this.annotationsSet + this.annotationsSetRefList + this.annotation;
        }
    }
}
