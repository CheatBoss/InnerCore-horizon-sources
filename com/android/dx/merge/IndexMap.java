package com.android.dx.merge;

import java.util.*;
import com.android.dx.util.*;
import com.android.dex.util.*;
import com.android.dex.*;

public final class IndexMap
{
    private final HashMap<Integer, Integer> annotationDirectoryOffsets;
    private final HashMap<Integer, Integer> annotationOffsets;
    private final HashMap<Integer, Integer> annotationSetOffsets;
    private final HashMap<Integer, Integer> annotationSetRefListOffsets;
    public final short[] fieldIds;
    public final short[] methodIds;
    public final short[] protoIds;
    private final HashMap<Integer, Integer> staticValuesOffsets;
    public final int[] stringIds;
    private final Dex target;
    public final short[] typeIds;
    private final HashMap<Integer, Integer> typeListOffsets;
    
    public IndexMap(final Dex target, final TableOfContents tableOfContents) {
        this.target = target;
        this.stringIds = new int[tableOfContents.stringIds.size];
        this.typeIds = new short[tableOfContents.typeIds.size];
        this.protoIds = new short[tableOfContents.protoIds.size];
        this.fieldIds = new short[tableOfContents.fieldIds.size];
        this.methodIds = new short[tableOfContents.methodIds.size];
        this.typeListOffsets = new HashMap<Integer, Integer>();
        this.annotationOffsets = new HashMap<Integer, Integer>();
        this.annotationSetOffsets = new HashMap<Integer, Integer>();
        this.annotationSetRefListOffsets = new HashMap<Integer, Integer>();
        this.annotationDirectoryOffsets = new HashMap<Integer, Integer>();
        this.staticValuesOffsets = new HashMap<Integer, Integer>();
        this.typeListOffsets.put(0, 0);
        this.annotationSetOffsets.put(0, 0);
        this.annotationDirectoryOffsets.put(0, 0);
        this.staticValuesOffsets.put(0, 0);
    }
    
    public Annotation adjust(final Annotation annotation) {
        final ByteArrayAnnotatedOutput byteArrayAnnotatedOutput = new ByteArrayAnnotatedOutput(32);
        new EncodedValueTransformer(byteArrayAnnotatedOutput).transformAnnotation(annotation.getReader());
        return new Annotation(this.target, annotation.getVisibility(), new EncodedValue(byteArrayAnnotatedOutput.toByteArray()));
    }
    
    public ClassDef adjust(final ClassDef classDef) {
        return new ClassDef(this.target, classDef.getOffset(), this.adjustType(classDef.getTypeIndex()), classDef.getAccessFlags(), this.adjustType(classDef.getSupertypeIndex()), this.adjustTypeListOffset(classDef.getInterfacesOffset()), classDef.getSourceFileIndex(), classDef.getAnnotationsOffset(), classDef.getClassDataOffset(), classDef.getStaticValuesOffset());
    }
    
    public FieldId adjust(final FieldId fieldId) {
        return new FieldId(this.target, this.adjustType(fieldId.getDeclaringClassIndex()), this.adjustType(fieldId.getTypeIndex()), this.adjustString(fieldId.getNameIndex()));
    }
    
    public MethodId adjust(final MethodId methodId) {
        return new MethodId(this.target, this.adjustType(methodId.getDeclaringClassIndex()), this.adjustProto(methodId.getProtoIndex()), this.adjustString(methodId.getNameIndex()));
    }
    
    public ProtoId adjust(final ProtoId protoId) {
        return new ProtoId(this.target, this.adjustString(protoId.getShortyIndex()), this.adjustType(protoId.getReturnTypeIndex()), this.adjustTypeListOffset(protoId.getParametersOffset()));
    }
    
    public SortableType adjust(final SortableType sortableType) {
        return new SortableType(sortableType.getDex(), sortableType.getIndexMap(), this.adjust(sortableType.getClassDef()));
    }
    
    public int adjustAnnotation(final int n) {
        return this.annotationOffsets.get(n);
    }
    
    public int adjustAnnotationDirectory(final int n) {
        return this.annotationDirectoryOffsets.get(n);
    }
    
    public int adjustAnnotationSet(final int n) {
        return this.annotationSetOffsets.get(n);
    }
    
    public int adjustAnnotationSetRefList(final int n) {
        return this.annotationSetRefListOffsets.get(n);
    }
    
    public EncodedValue adjustEncodedArray(final EncodedValue encodedValue) {
        final ByteArrayAnnotatedOutput byteArrayAnnotatedOutput = new ByteArrayAnnotatedOutput(32);
        new EncodedValueTransformer(byteArrayAnnotatedOutput).transformArray(new EncodedValueReader(encodedValue, 28));
        return new EncodedValue(byteArrayAnnotatedOutput.toByteArray());
    }
    
    public EncodedValue adjustEncodedValue(final EncodedValue encodedValue) {
        final ByteArrayAnnotatedOutput byteArrayAnnotatedOutput = new ByteArrayAnnotatedOutput(32);
        new EncodedValueTransformer(byteArrayAnnotatedOutput).transform(new EncodedValueReader(encodedValue));
        return new EncodedValue(byteArrayAnnotatedOutput.toByteArray());
    }
    
    public int adjustField(final int n) {
        return this.fieldIds[n] & 0xFFFF;
    }
    
    public int adjustMethod(final int n) {
        return this.methodIds[n] & 0xFFFF;
    }
    
    public int adjustProto(final int n) {
        return this.protoIds[n] & 0xFFFF;
    }
    
    public int adjustStaticValues(final int n) {
        return this.staticValuesOffsets.get(n);
    }
    
    public int adjustString(final int n) {
        if (n == -1) {
            return -1;
        }
        return this.stringIds[n];
    }
    
    public int adjustType(final int n) {
        if (n == -1) {
            return -1;
        }
        return this.typeIds[n] & 0xFFFF;
    }
    
    public TypeList adjustTypeList(final TypeList list) {
        if (list == TypeList.EMPTY) {
            return list;
        }
        final short[] array = list.getTypes().clone();
        for (int i = 0; i < array.length; ++i) {
            array[i] = (short)this.adjustType(array[i]);
        }
        return new TypeList(this.target, array);
    }
    
    public int adjustTypeListOffset(final int n) {
        return this.typeListOffsets.get(n);
    }
    
    public void putAnnotationDirectoryOffset(final int n, final int n2) {
        if (n > 0 && n2 > 0) {
            this.annotationDirectoryOffsets.put(n, n2);
            return;
        }
        throw new IllegalArgumentException();
    }
    
    public void putAnnotationOffset(final int n, final int n2) {
        if (n > 0 && n2 > 0) {
            this.annotationOffsets.put(n, n2);
            return;
        }
        throw new IllegalArgumentException();
    }
    
    public void putAnnotationSetOffset(final int n, final int n2) {
        if (n > 0 && n2 > 0) {
            this.annotationSetOffsets.put(n, n2);
            return;
        }
        throw new IllegalArgumentException();
    }
    
    public void putAnnotationSetRefListOffset(final int n, final int n2) {
        if (n > 0 && n2 > 0) {
            this.annotationSetRefListOffsets.put(n, n2);
            return;
        }
        throw new IllegalArgumentException();
    }
    
    public void putStaticValuesOffset(final int n, final int n2) {
        if (n > 0 && n2 > 0) {
            this.staticValuesOffsets.put(n, n2);
            return;
        }
        throw new IllegalArgumentException();
    }
    
    public void putTypeListOffset(final int n, final int n2) {
        if (n > 0 && n2 > 0) {
            this.typeListOffsets.put(n, n2);
            return;
        }
        throw new IllegalArgumentException();
    }
    
    private final class EncodedValueTransformer
    {
        private final ByteOutput out;
        
        public EncodedValueTransformer(final ByteOutput out) {
            this.out = out;
        }
        
        private void transformAnnotation(final EncodedValueReader encodedValueReader) {
            final int annotation = encodedValueReader.readAnnotation();
            Leb128.writeUnsignedLeb128(this.out, IndexMap.this.adjustType(encodedValueReader.getAnnotationType()));
            Leb128.writeUnsignedLeb128(this.out, annotation);
            for (int i = 0; i < annotation; ++i) {
                Leb128.writeUnsignedLeb128(this.out, IndexMap.this.adjustString(encodedValueReader.readAnnotationName()));
                this.transform(encodedValueReader);
            }
        }
        
        private void transformArray(final EncodedValueReader encodedValueReader) {
            final int array = encodedValueReader.readArray();
            Leb128.writeUnsignedLeb128(this.out, array);
            for (int i = 0; i < array; ++i) {
                this.transform(encodedValueReader);
            }
        }
        
        private void writeTypeAndArg(final int n, final int n2) {
            this.out.writeByte(n2 << 5 | n);
        }
        
        public void transform(final EncodedValueReader encodedValueReader) {
            throw new Runtime("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\r\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:659)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n");
        }
    }
}
