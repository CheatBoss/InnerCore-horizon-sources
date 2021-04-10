package org.mozilla.classfile;

import org.mozilla.javascript.*;
import java.util.*;
import java.io.*;

public class ClassFileWriter
{
    public static final short ACC_ABSTRACT = 1024;
    public static final short ACC_FINAL = 16;
    public static final short ACC_NATIVE = 256;
    public static final short ACC_PRIVATE = 2;
    public static final short ACC_PROTECTED = 4;
    public static final short ACC_PUBLIC = 1;
    public static final short ACC_STATIC = 8;
    public static final short ACC_SUPER = 32;
    public static final short ACC_SYNCHRONIZED = 32;
    public static final short ACC_TRANSIENT = 128;
    public static final short ACC_VOLATILE = 64;
    private static final boolean DEBUGCODE = false;
    private static final boolean DEBUGLABELS = false;
    private static final boolean DEBUGSTACK = false;
    private static final int ExceptionTableSize = 4;
    private static final int FileHeaderConstant = -889275714;
    private static final boolean GenerateStackMap;
    private static final int LineNumberTableSize = 16;
    private static final int MIN_FIXUP_TABLE_SIZE = 40;
    private static final int MIN_LABEL_TABLE_SIZE = 32;
    private static final int MajorVersion;
    private static final int MinorVersion;
    private static final int SuperBlockStartsSize = 4;
    private String generatedClassName;
    private byte[] itsCodeBuffer;
    private int itsCodeBufferTop;
    private ConstantPool itsConstantPool;
    private ClassFileMethod itsCurrentMethod;
    private ExceptionTableEntry[] itsExceptionTable;
    private int itsExceptionTableTop;
    private ObjArray itsFields;
    private long[] itsFixupTable;
    private int itsFixupTableTop;
    private short itsFlags;
    private ObjArray itsInterfaces;
    private UintMap itsJumpFroms;
    private int[] itsLabelTable;
    private int itsLabelTableTop;
    private int[] itsLineNumberTable;
    private int itsLineNumberTableTop;
    private short itsMaxLocals;
    private short itsMaxStack;
    private ObjArray itsMethods;
    private short itsSourceFileNameIndex;
    private short itsStackTop;
    private int[] itsSuperBlockStarts;
    private int itsSuperBlockStartsTop;
    private short itsSuperClassIndex;
    private short itsThisClassIndex;
    private ObjArray itsVarDescriptors;
    private char[] tmpCharBuffer;
    
    static {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore          9
        //     3: aconst_null    
        //     4: astore          8
        //     6: iconst_0       
        //     7: istore          5
        //     9: iconst_0       
        //    10: istore          6
        //    12: iconst_0       
        //    13: istore          4
        //    15: ldc             Lorg/mozilla/classfile/ClassFileWriter;.class
        //    17: ldc             "ClassFileWriter.class"
        //    19: invokevirtual   java/lang/Class.getResourceAsStream:(Ljava/lang/String;)Ljava/io/InputStream;
        //    22: astore          10
        //    24: aload           10
        //    26: astore          7
        //    28: aload           10
        //    30: ifnonnull       48
        //    33: aload           10
        //    35: astore          8
        //    37: aload           10
        //    39: astore          9
        //    41: ldc             "org/mozilla/classfile/ClassFileWriter.class"
        //    43: invokestatic    java/lang/ClassLoader.getSystemResourceAsStream:(Ljava/lang/String;)Ljava/io/InputStream;
        //    46: astore          7
        //    48: aload           7
        //    50: astore          8
        //    52: aload           7
        //    54: astore          9
        //    56: bipush          8
        //    58: newarray        B
        //    60: astore          10
        //    62: iconst_0       
        //    63: istore_0       
        //    64: iload_0        
        //    65: bipush          8
        //    67: if_icmpge       111
        //    70: aload           7
        //    72: astore          8
        //    74: aload           7
        //    76: astore          9
        //    78: aload           7
        //    80: aload           10
        //    82: iload_0        
        //    83: bipush          8
        //    85: iload_0        
        //    86: isub           
        //    87: invokevirtual   java/io/InputStream.read:([BII)I
        //    90: istore_1       
        //    91: iload_1        
        //    92: ifge            281
        //    95: aload           7
        //    97: astore          8
        //    99: aload           7
        //   101: astore          9
        //   103: new             Ljava/io/IOException;
        //   106: dup            
        //   107: invokespecial   java/io/IOException.<init>:()V
        //   110: athrow         
        //   111: aload           10
        //   113: iconst_4       
        //   114: baload         
        //   115: istore_0       
        //   116: aload           10
        //   118: iconst_5       
        //   119: baload         
        //   120: istore_1       
        //   121: aload           10
        //   123: bipush          6
        //   125: baload         
        //   126: istore_2       
        //   127: aload           10
        //   129: bipush          7
        //   131: baload         
        //   132: istore_3       
        //   133: iload_2        
        //   134: bipush          8
        //   136: ishl           
        //   137: iload_3        
        //   138: sipush          255
        //   141: iand           
        //   142: ior            
        //   143: istore_2       
        //   144: iload_0        
        //   145: bipush          8
        //   147: ishl           
        //   148: iload_1        
        //   149: sipush          255
        //   152: iand           
        //   153: ior            
        //   154: putstatic       org/mozilla/classfile/ClassFileWriter.MinorVersion:I
        //   157: iload_2        
        //   158: putstatic       org/mozilla/classfile/ClassFileWriter.MajorVersion:I
        //   161: iload_2        
        //   162: bipush          50
        //   164: if_icmplt       170
        //   167: iconst_1       
        //   168: istore          4
        //   170: iload           4
        //   172: putstatic       org/mozilla/classfile/ClassFileWriter.GenerateStackMap:Z
        //   175: aload           7
        //   177: ifnull          280
        //   180: aload           7
        //   182: invokevirtual   java/io/InputStream.close:()V
        //   185: goto            276
        //   188: astore          7
        //   190: iconst_0       
        //   191: putstatic       org/mozilla/classfile/ClassFileWriter.MinorVersion:I
        //   194: bipush          48
        //   196: putstatic       org/mozilla/classfile/ClassFileWriter.MajorVersion:I
        //   199: iload           5
        //   201: istore          4
        //   203: bipush          48
        //   205: bipush          50
        //   207: if_icmplt       213
        //   210: iconst_1       
        //   211: istore          4
        //   213: iload           4
        //   215: putstatic       org/mozilla/classfile/ClassFileWriter.GenerateStackMap:Z
        //   218: aload           8
        //   220: ifnull          233
        //   223: aload           8
        //   225: invokevirtual   java/io/InputStream.close:()V
        //   228: goto            233
        //   231: astore          8
        //   233: aload           7
        //   235: athrow         
        //   236: astore          7
        //   238: iconst_0       
        //   239: putstatic       org/mozilla/classfile/ClassFileWriter.MinorVersion:I
        //   242: bipush          48
        //   244: putstatic       org/mozilla/classfile/ClassFileWriter.MajorVersion:I
        //   247: iload           6
        //   249: istore          4
        //   251: bipush          48
        //   253: bipush          50
        //   255: if_icmplt       261
        //   258: iconst_1       
        //   259: istore          4
        //   261: iload           4
        //   263: putstatic       org/mozilla/classfile/ClassFileWriter.GenerateStackMap:Z
        //   266: aload           9
        //   268: ifnull          280
        //   271: aload           9
        //   273: invokevirtual   java/io/InputStream.close:()V
        //   276: return         
        //   277: astore          7
        //   279: return         
        //   280: return         
        //   281: iload_0        
        //   282: iload_1        
        //   283: iadd           
        //   284: istore_0       
        //   285: goto            64
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  15     24     236    276    Ljava/lang/Exception;
        //  15     24     188    236    Any
        //  41     48     236    276    Ljava/lang/Exception;
        //  41     48     188    236    Any
        //  56     62     236    276    Ljava/lang/Exception;
        //  56     62     188    236    Any
        //  78     91     236    276    Ljava/lang/Exception;
        //  78     91     188    236    Any
        //  103    111    236    276    Ljava/lang/Exception;
        //  103    111    188    236    Any
        //  180    185    277    280    Ljava/io/IOException;
        //  223    228    231    233    Ljava/io/IOException;
        //  271    276    277    280    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0213:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
        //     at java.lang.Thread.run(Unknown Source)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public ClassFileWriter(final String generatedClassName, final String s, final String s2) {
        this.itsSuperBlockStarts = null;
        this.itsSuperBlockStartsTop = 0;
        this.itsJumpFroms = null;
        this.itsCodeBuffer = new byte[256];
        this.itsMethods = new ObjArray();
        this.itsFields = new ObjArray();
        this.itsInterfaces = new ObjArray();
        this.tmpCharBuffer = new char[64];
        this.generatedClassName = generatedClassName;
        this.itsConstantPool = new ConstantPool(this);
        this.itsThisClassIndex = this.itsConstantPool.addClass(generatedClassName);
        this.itsSuperClassIndex = this.itsConstantPool.addClass(s);
        if (s2 != null) {
            this.itsSourceFileNameIndex = this.itsConstantPool.addUtf8(s2);
        }
        this.itsFlags = 33;
    }
    
    private void addLabelFixup(int n, final int n2) {
        if (n >= 0) {
            throw new IllegalArgumentException("Bad label, no biscuit");
        }
        n &= Integer.MAX_VALUE;
        if (n >= this.itsLabelTableTop) {
            throw new IllegalArgumentException("Bad label");
        }
        final int itsFixupTableTop = this.itsFixupTableTop;
        if (this.itsFixupTable == null || itsFixupTableTop == this.itsFixupTable.length) {
            if (this.itsFixupTable == null) {
                this.itsFixupTable = new long[40];
            }
            else {
                final long[] itsFixupTable = new long[this.itsFixupTable.length * 2];
                System.arraycopy(this.itsFixupTable, 0, itsFixupTable, 0, itsFixupTableTop);
                this.itsFixupTable = itsFixupTable;
            }
        }
        this.itsFixupTableTop = itsFixupTableTop + 1;
        this.itsFixupTable[itsFixupTableTop] = ((long)n << 32 | (long)n2);
    }
    
    private int addReservedCodeSpace(int n) {
        if (this.itsCurrentMethod == null) {
            throw new IllegalArgumentException("No method to add to");
        }
        final int itsCodeBufferTop = this.itsCodeBufferTop;
        final int itsCodeBufferTop2 = itsCodeBufferTop + n;
        if (itsCodeBufferTop2 > this.itsCodeBuffer.length) {
            if (itsCodeBufferTop2 > (n = this.itsCodeBuffer.length * 2)) {
                n = itsCodeBufferTop2;
            }
            final byte[] itsCodeBuffer = new byte[n];
            System.arraycopy(this.itsCodeBuffer, 0, itsCodeBuffer, 0, itsCodeBufferTop);
            this.itsCodeBuffer = itsCodeBuffer;
        }
        this.itsCodeBufferTop = itsCodeBufferTop2;
        return itsCodeBufferTop;
    }
    
    private void addSuperBlockStart(final int n) {
        if (ClassFileWriter.GenerateStackMap) {
            if (this.itsSuperBlockStarts == null) {
                this.itsSuperBlockStarts = new int[4];
            }
            else if (this.itsSuperBlockStarts.length == this.itsSuperBlockStartsTop) {
                final int[] itsSuperBlockStarts = new int[this.itsSuperBlockStartsTop * 2];
                System.arraycopy(this.itsSuperBlockStarts, 0, itsSuperBlockStarts, 0, this.itsSuperBlockStartsTop);
                this.itsSuperBlockStarts = itsSuperBlockStarts;
            }
            this.itsSuperBlockStarts[this.itsSuperBlockStartsTop++] = n;
        }
    }
    
    private void addToCodeBuffer(final int n) {
        this.itsCodeBuffer[this.addReservedCodeSpace(1)] = (byte)n;
    }
    
    private void addToCodeInt16(final int n) {
        putInt16(n, this.itsCodeBuffer, this.addReservedCodeSpace(2));
    }
    
    private static char arrayTypeToName(final int n) {
        switch (n) {
            default: {
                throw new IllegalArgumentException("bad operand");
            }
            case 11: {
                return 'J';
            }
            case 10: {
                return 'I';
            }
            case 9: {
                return 'S';
            }
            case 8: {
                return 'B';
            }
            case 7: {
                return 'D';
            }
            case 6: {
                return 'F';
            }
            case 5: {
                return 'C';
            }
            case 4: {
                return 'Z';
            }
        }
    }
    
    private static void badStack(final int n) {
        String s;
        if (n < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Stack underflow: ");
            sb.append(n);
            s = sb.toString();
        }
        else {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Too big stack: ");
            sb2.append(n);
            s = sb2.toString();
        }
        throw new IllegalStateException(s);
    }
    
    private static String bytecodeStr(final int n) {
        return "";
    }
    
    private static String classDescriptorToInternalName(final String s) {
        return s.substring(1, s.length() - 1);
    }
    
    public static String classNameToSignature(final String s) {
        final int length = s.length();
        final int n = length + 1;
        final char[] array = new char[n + 1];
        array[0] = 'L';
        array[n] = ';';
        int i = 1;
        s.getChars(0, length, array, 1);
        while (i != n) {
            if (array[i] == '.') {
                array[i] = '/';
            }
            ++i;
        }
        return new String(array, 0, n + 1);
    }
    
    private int[] createInitialLocals() {
        final int[] array = new int[this.itsMaxLocals];
        int n;
        if ((this.itsCurrentMethod.getFlags() & 0x8) == 0x0) {
            if ("<init>".equals(this.itsCurrentMethod.getName())) {
                n = 0 + 1;
                array[0] = 6;
            }
            else {
                n = 0 + 1;
                array[0] = TypeInfo.OBJECT(this.itsThisClassIndex);
            }
        }
        else {
            n = 0;
        }
        final String type = this.itsCurrentMethod.getType();
        final int index = type.indexOf(40);
        final int index2 = type.indexOf(41);
        if (index == 0 && index2 >= 0) {
            int i = index + 1;
            final StringBuilder sb = new StringBuilder();
            int n2 = n;
            while (i < index2) {
                int n3 = 0;
                switch (type.charAt(i)) {
                    default: {
                        n3 = i;
                        break;
                    }
                    case '[': {
                        sb.append('[');
                        ++i;
                        continue;
                    }
                    case 'L': {
                        n3 = type.indexOf(59, i) + 1;
                        sb.append(type.substring(i, n3));
                        break;
                    }
                    case 'B':
                    case 'C':
                    case 'D':
                    case 'F':
                    case 'I':
                    case 'J':
                    case 'S':
                    case 'Z': {
                        sb.append(type.charAt(i));
                        n3 = i + 1;
                        break;
                    }
                }
                final int fromType = TypeInfo.fromType(descriptorToInternalName(sb.toString()), this.itsConstantPool);
                final int n4 = n2 + 1;
                array[n2] = fromType;
                int n5 = n4;
                if (TypeInfo.isTwoWords(fromType)) {
                    n5 = n4 + 1;
                }
                sb.setLength(0);
                n2 = n5;
                i = n3;
            }
            return array;
        }
        throw new IllegalArgumentException("bad method type");
    }
    
    private static String descriptorToInternalName(final String s) {
        switch (s.charAt(0)) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("bad descriptor:");
                sb.append(s);
                throw new IllegalArgumentException(sb.toString());
            }
            case 'L': {
                return classDescriptorToInternalName(s);
            }
            case 'B':
            case 'C':
            case 'D':
            case 'F':
            case 'I':
            case 'J':
            case 'S':
            case 'V':
            case 'Z':
            case '[': {
                return s;
            }
        }
    }
    
    private void finalizeSuperBlockStarts() {
        if (ClassFileWriter.GenerateStackMap) {
            for (int i = 0; i < this.itsExceptionTableTop; ++i) {
                this.addSuperBlockStart((short)this.getLabelPC(this.itsExceptionTable[i].itsHandlerLabel));
            }
            Arrays.sort(this.itsSuperBlockStarts, 0, this.itsSuperBlockStartsTop);
            int n = this.itsSuperBlockStarts[0];
            int itsSuperBlockStartsTop = 1;
            int n3;
            int n4;
            for (int j = 1; j < this.itsSuperBlockStartsTop; ++j, n = n3, itsSuperBlockStartsTop = n4) {
                final int n2 = this.itsSuperBlockStarts[j];
                n3 = n;
                n4 = itsSuperBlockStartsTop;
                if (n != n2) {
                    if (itsSuperBlockStartsTop != j) {
                        this.itsSuperBlockStarts[itsSuperBlockStartsTop] = n2;
                    }
                    n4 = itsSuperBlockStartsTop + 1;
                    n3 = n2;
                }
            }
            this.itsSuperBlockStartsTop = itsSuperBlockStartsTop;
            if (this.itsSuperBlockStarts[itsSuperBlockStartsTop - 1] == this.itsCodeBufferTop) {
                --this.itsSuperBlockStartsTop;
            }
        }
    }
    
    private void fixLabelGotos() {
        final byte[] itsCodeBuffer = this.itsCodeBuffer;
        for (int i = 0; i < this.itsFixupTableTop; ++i) {
            final long n = this.itsFixupTable[i];
            final int n2 = (int)(n >> 32);
            final int n3 = (int)n;
            final int n4 = this.itsLabelTable[n2];
            if (n4 == -1) {
                throw new RuntimeException();
            }
            this.addSuperBlockStart(n4);
            this.itsJumpFroms.put(n4, n3 - 1);
            final int n5 = n4 - (n3 - 1);
            if ((short)n5 != n5) {
                throw new ClassFileFormatException("Program too complex: too big jump offset");
            }
            itsCodeBuffer[n3] = (byte)(n5 >> 8);
            itsCodeBuffer[n3 + 1] = (byte)n5;
        }
        this.itsFixupTableTop = 0;
    }
    
    static String getSlashedForm(final String s) {
        return s.replace('.', '/');
    }
    
    private int getWriteSize() {
        if (this.itsSourceFileNameIndex != 0) {
            this.itsConstantPool.addUtf8("SourceFile");
        }
        final int writeSize = this.itsConstantPool.getWriteSize();
        final int size = this.itsInterfaces.size();
        final int n = 0;
        int n2 = 0 + 8 + writeSize + 2 + 2 + 2 + 2 + size * 2 + 2;
        for (int i = 0; i < this.itsFields.size(); ++i) {
            n2 += ((ClassFileField)this.itsFields.get(i)).getWriteSize();
        }
        int n3 = n2 + 2;
        for (int j = n; j < this.itsMethods.size(); ++j) {
            n3 += ((ClassFileMethod)this.itsMethods.get(j)).getWriteSize();
        }
        if (this.itsSourceFileNameIndex != 0) {
            return n3 + 2 + 2 + 4 + 2;
        }
        return n3 + 2;
    }
    
    static int opcodeCount(final int n) {
        switch (n) {
            default: {
                switch (n) {
                    default: {
                        switch (n) {
                            default: {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("Bad opcode: ");
                                sb.append(n);
                                throw new IllegalArgumentException(sb.toString());
                            }
                            case 254:
                            case 255: {
                                return 0;
                            }
                        }
                        break;
                    }
                    case 197: {
                        return 2;
                    }
                    case 187:
                    case 188:
                    case 189:
                    case 192:
                    case 193:
                    case 198:
                    case 199:
                    case 200:
                    case 201: {
                        return 1;
                    }
                    case 190:
                    case 191:
                    case 194:
                    case 195:
                    case 196:
                    case 202: {
                        return 0;
                    }
                }
                break;
            }
            case 170:
            case 171: {
                return -1;
            }
            case 132: {
                return 2;
            }
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158:
            case 159:
            case 160:
            case 161:
            case 162:
            case 163:
            case 164:
            case 165:
            case 166:
            case 167:
            case 168:
            case 169:
            case 178:
            case 179:
            case 180:
            case 181:
            case 182:
            case 183:
            case 184:
            case 185: {
                return 1;
            }
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 68:
            case 69:
            case 70:
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
            case 78:
            case 79:
            case 80:
            case 81:
            case 82:
            case 83:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case 91:
            case 92:
            case 93:
            case 94:
            case 95:
            case 96:
            case 97:
            case 98:
            case 99:
            case 100:
            case 101:
            case 102:
            case 103:
            case 104:
            case 105:
            case 106:
            case 107:
            case 108:
            case 109:
            case 110:
            case 111:
            case 112:
            case 113:
            case 114:
            case 115:
            case 116:
            case 117:
            case 118:
            case 119:
            case 120:
            case 121:
            case 122:
            case 123:
            case 124:
            case 125:
            case 126:
            case 127:
            case 128:
            case 129:
            case 130:
            case 131:
            case 133:
            case 134:
            case 135:
            case 136:
            case 137:
            case 138:
            case 139:
            case 140:
            case 141:
            case 142:
            case 143:
            case 144:
            case 145:
            case 146:
            case 147:
            case 148:
            case 149:
            case 150:
            case 151:
            case 152:
            case 172:
            case 173:
            case 174:
            case 175:
            case 176:
            case 177: {
                return 0;
            }
        }
    }
    
    static int opcodeLength(int n, final boolean b) {
        final int n2 = 2;
        switch (n) {
            default: {
                switch (n) {
                    default: {
                        switch (n) {
                            default: {
                                switch (n) {
                                    default: {
                                        final StringBuilder sb = new StringBuilder();
                                        sb.append("Bad opcode: ");
                                        sb.append(n);
                                        throw new IllegalArgumentException(sb.toString());
                                    }
                                    case 254:
                                    case 255: {
                                        return 1;
                                    }
                                }
                                break;
                            }
                            case 197: {
                                return 4;
                            }
                            case 200:
                            case 201: {
                                return 5;
                            }
                            case 187:
                            case 189:
                            case 192:
                            case 193:
                            case 198:
                            case 199: {
                                return 3;
                            }
                            case 188: {
                                return 2;
                            }
                            case 190:
                            case 191:
                            case 194:
                            case 195:
                            case 196:
                            case 202: {
                                return 1;
                            }
                        }
                        break;
                    }
                    case 185: {
                        return 5;
                    }
                    case 178:
                    case 179:
                    case 180:
                    case 181:
                    case 182:
                    case 183:
                    case 184: {
                        return 3;
                    }
                    case 172:
                    case 173:
                    case 174:
                    case 175:
                    case 176:
                    case 177: {
                        return 1;
                    }
                }
                break;
            }
            case 132: {
                if (b) {
                    return 5;
                }
                return 3;
            }
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 169: {
                n = n2;
                if (b) {
                    n = 3;
                }
                return n;
            }
            case 17:
            case 19:
            case 20:
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158:
            case 159:
            case 160:
            case 161:
            case 162:
            case 163:
            case 164:
            case 165:
            case 166:
            case 167:
            case 168: {
                return 3;
            }
            case 16:
            case 18: {
                return 2;
            }
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 68:
            case 69:
            case 70:
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
            case 78:
            case 79:
            case 80:
            case 81:
            case 82:
            case 83:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case 91:
            case 92:
            case 93:
            case 94:
            case 95:
            case 96:
            case 97:
            case 98:
            case 99:
            case 100:
            case 101:
            case 102:
            case 103:
            case 104:
            case 105:
            case 106:
            case 107:
            case 108:
            case 109:
            case 110:
            case 111:
            case 112:
            case 113:
            case 114:
            case 115:
            case 116:
            case 117:
            case 118:
            case 119:
            case 120:
            case 121:
            case 122:
            case 123:
            case 124:
            case 125:
            case 126:
            case 127:
            case 128:
            case 129:
            case 130:
            case 131:
            case 133:
            case 134:
            case 135:
            case 136:
            case 137:
            case 138:
            case 139:
            case 140:
            case 141:
            case 142:
            case 143:
            case 144:
            case 145:
            case 146:
            case 147:
            case 148:
            case 149:
            case 150:
            case 151:
            case 152: {
                return 1;
            }
        }
    }
    
    static int putInt16(final int n, final byte[] array, final int n2) {
        array[n2 + 0] = (byte)(n >>> 8);
        array[n2 + 1] = (byte)n;
        return n2 + 2;
    }
    
    static int putInt32(final int n, final byte[] array, final int n2) {
        array[n2 + 0] = (byte)(n >>> 24);
        array[n2 + 1] = (byte)(n >>> 16);
        array[n2 + 2] = (byte)(n >>> 8);
        array[n2 + 3] = (byte)n;
        return n2 + 4;
    }
    
    static int putInt64(final long n, final byte[] array, int putInt32) {
        putInt32 = putInt32((int)(n >>> 32), array, putInt32);
        return putInt32((int)n, array, putInt32);
    }
    
    private static int sizeOfParameters(final String s) {
        final int length = s.length();
        final int lastIndex = s.lastIndexOf(41);
        if (3 <= length) {
            int n = 0;
            if (s.charAt(0) == '(' && 1 <= lastIndex && lastIndex + 1 < length) {
                final boolean b = true;
                int n2 = 1;
                int n3 = 0;
                int n4 = 0;
                boolean b2 = false;
                int n5 = 0;
            Label_0404:
                while (true) {
                    n4 = n;
                    b2 = b;
                    n5 = n3;
                    if (n2 == lastIndex) {
                        break;
                    }
                    int n6 = n2;
                    int n7 = n3;
                    Label_0323: {
                        switch (s.charAt(n2)) {
                            default: {
                                b2 = false;
                                n4 = n;
                                n5 = n3;
                                break Label_0404;
                            }
                            case '[': {
                                int n8;
                                char c;
                                for (n8 = n2 + 1, c = s.charAt(n8); c == '['; c = s.charAt(n8)) {
                                    ++n8;
                                }
                                Label_0308: {
                                    if (c != 'F') {
                                        n6 = n8;
                                        if (c == 'L') {
                                            break Label_0323;
                                        }
                                        if (c != 'S' && c != 'Z') {
                                            switch (c) {
                                                default: {
                                                    switch (c) {
                                                        default: {
                                                            b2 = false;
                                                            n4 = n;
                                                            n5 = n3;
                                                            break Label_0404;
                                                        }
                                                        case 73:
                                                        case 74: {
                                                            break Label_0308;
                                                        }
                                                    }
                                                    break;
                                                }
                                                case 66:
                                                case 67:
                                                case 68: {
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                                --n3;
                                ++n;
                                n2 = n8 + 1;
                                continue;
                            }
                            case 'L': {
                                --n3;
                                ++n;
                                final int n9 = n6 + 1;
                                final int index = s.indexOf(59, n9);
                                if (n9 + 1 <= index && index < lastIndex) {
                                    n2 = index + 1;
                                    continue;
                                }
                                b2 = false;
                                n4 = n;
                                n5 = n3;
                                break Label_0404;
                            }
                            case 'D':
                            case 'J': {
                                n7 = n3 - 1;
                            }
                            case 'B':
                            case 'C':
                            case 'F':
                            case 'I':
                            case 'S':
                            case 'Z': {
                                n3 = n7 - 1;
                                ++n;
                                ++n2;
                                continue;
                            }
                        }
                    }
                }
                if (b2) {
                    int n10 = n5;
                    boolean b3 = b2;
                    int n11 = n5;
                    switch (s.charAt(lastIndex + 1)) {
                        case 'D':
                        case 'J': {
                            n10 = n5 + 1;
                        }
                        case 'B':
                        case 'C':
                        case 'F':
                        case 'I':
                        case 'L':
                        case 'S':
                        case 'Z':
                        case '[': {
                            n11 = n10 + 1;
                            b3 = b2;
                        }
                        default: {
                            b3 = false;
                            n11 = n5;
                        }
                        case 'V': {
                            if (b3) {
                                return n4 << 16 | (0xFFFF & n11);
                            }
                            break;
                        }
                    }
                }
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Bad parameter signature: ");
        sb.append(s);
        throw new IllegalArgumentException(sb.toString());
    }
    
    static int stackChange(final int n) {
        switch (n) {
            default: {
                switch (n) {
                    default: {
                        switch (n) {
                            default: {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("Bad opcode: ");
                                sb.append(n);
                                throw new IllegalArgumentException(sb.toString());
                            }
                            case 254:
                            case 255: {
                                return 0;
                            }
                        }
                        break;
                    }
                    case 191:
                    case 194:
                    case 195:
                    case 198:
                    case 199: {
                        return -1;
                    }
                    case 187:
                    case 197:
                    case 201: {
                        return 1;
                    }
                    case 188:
                    case 189:
                    case 190:
                    case 192:
                    case 193:
                    case 196:
                    case 200:
                    case 202: {
                        return 0;
                    }
                }
                break;
            }
            case 80:
            case 82: {
                return -4;
            }
            case 79:
            case 81:
            case 83:
            case 84:
            case 85:
            case 86:
            case 148:
            case 151:
            case 152: {
                return -3;
            }
            case 55:
            case 57:
            case 63:
            case 64:
            case 65:
            case 66:
            case 71:
            case 72:
            case 73:
            case 74:
            case 88:
            case 97:
            case 99:
            case 101:
            case 103:
            case 105:
            case 107:
            case 109:
            case 111:
            case 113:
            case 115:
            case 127:
            case 129:
            case 131:
            case 159:
            case 160:
            case 161:
            case 162:
            case 163:
            case 164:
            case 165:
            case 166:
            case 173:
            case 175: {
                return -2;
            }
            case 46:
            case 48:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 56:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 67:
            case 68:
            case 69:
            case 70:
            case 75:
            case 76:
            case 77:
            case 78:
            case 87:
            case 96:
            case 98:
            case 100:
            case 102:
            case 104:
            case 106:
            case 108:
            case 110:
            case 112:
            case 114:
            case 120:
            case 121:
            case 122:
            case 123:
            case 124:
            case 125:
            case 126:
            case 128:
            case 130:
            case 136:
            case 137:
            case 142:
            case 144:
            case 149:
            case 150:
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158:
            case 170:
            case 171:
            case 172:
            case 174:
            case 176:
            case 180:
            case 181:
            case 182:
            case 183:
            case 185: {
                return -1;
            }
            case 9:
            case 10:
            case 14:
            case 15:
            case 20:
            case 22:
            case 24:
            case 30:
            case 31:
            case 32:
            case 33:
            case 38:
            case 39:
            case 40:
            case 41:
            case 92:
            case 93:
            case 94: {
                return 2;
            }
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 11:
            case 12:
            case 13:
            case 16:
            case 17:
            case 18:
            case 19:
            case 21:
            case 23:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 34:
            case 35:
            case 36:
            case 37:
            case 42:
            case 43:
            case 44:
            case 45:
            case 89:
            case 90:
            case 91:
            case 133:
            case 135:
            case 140:
            case 141:
            case 168: {
                return 1;
            }
            case 0:
            case 47:
            case 49:
            case 95:
            case 116:
            case 117:
            case 118:
            case 119:
            case 132:
            case 134:
            case 138:
            case 139:
            case 143:
            case 145:
            case 146:
            case 147:
            case 167:
            case 169:
            case 177:
            case 178:
            case 179:
            case 184: {
                return 0;
            }
        }
    }
    
    private void xop(final int n, final int n2, final int n3) {
        switch (n3) {
            default: {
                this.add(n2, n3);
            }
            case 3: {
                this.add(n + 3);
            }
            case 2: {
                this.add(n + 2);
            }
            case 1: {
                this.add(n + 1);
            }
            case 0: {
                this.add(n);
            }
        }
    }
    
    public int acquireLabel() {
        final int itsLabelTableTop = this.itsLabelTableTop;
        if (this.itsLabelTable == null || itsLabelTableTop == this.itsLabelTable.length) {
            if (this.itsLabelTable == null) {
                this.itsLabelTable = new int[32];
            }
            else {
                final int[] itsLabelTable = new int[this.itsLabelTable.length * 2];
                System.arraycopy(this.itsLabelTable, 0, itsLabelTable, 0, itsLabelTableTop);
                this.itsLabelTable = itsLabelTable;
            }
        }
        this.itsLabelTableTop = itsLabelTableTop + 1;
        this.itsLabelTable[itsLabelTableTop] = -1;
        return Integer.MIN_VALUE | itsLabelTableTop;
    }
    
    public void add(final int n) {
        if (opcodeCount(n) != 0) {
            throw new IllegalArgumentException("Unexpected operands");
        }
        final int n2 = this.itsStackTop + stackChange(n);
        if (n2 < 0 || 32767 < n2) {
            badStack(n2);
        }
        this.addToCodeBuffer(n);
        this.itsStackTop = (short)n2;
        if (n2 > this.itsMaxStack) {
            this.itsMaxStack = (short)n2;
        }
        if (n == 191) {
            this.addSuperBlockStart(this.itsCodeBufferTop);
        }
    }
    
    public void add(int labelPC, final int n) {
        final int n2 = this.itsStackTop + stackChange(labelPC);
        if (n2 < 0 || 32767 < n2) {
            badStack(n2);
        }
        Label_0690: {
            if (labelPC != 188) {
                Label_0452: {
                    switch (labelPC) {
                        default: {
                            switch (labelPC) {
                                default: {
                                    Label_0315: {
                                        switch (labelPC) {
                                            default: {
                                                switch (labelPC) {
                                                    default: {
                                                        switch (labelPC) {
                                                            default: {
                                                                throw new IllegalArgumentException("Unexpected opcode for 1 operand");
                                                            }
                                                            case 198:
                                                            case 199: {
                                                                break Label_0315;
                                                            }
                                                        }
                                                        break;
                                                    }
                                                    case 180:
                                                    case 181: {
                                                        if (n >= 0 && n < 65536) {
                                                            this.addToCodeBuffer(labelPC);
                                                            this.addToCodeInt16(n);
                                                            break Label_0690;
                                                        }
                                                        throw new IllegalArgumentException("out of range field");
                                                    }
                                                }
                                                break;
                                            }
                                            case 167: {
                                                this.addSuperBlockStart(this.itsCodeBufferTop + 3);
                                            }
                                            case 153:
                                            case 154:
                                            case 155:
                                            case 156:
                                            case 157:
                                            case 158:
                                            case 159:
                                            case 160:
                                            case 161:
                                            case 162:
                                            case 163:
                                            case 164:
                                            case 165:
                                            case 166:
                                            case 168: {
                                                if ((n & Integer.MIN_VALUE) != Integer.MIN_VALUE && (n < 0 || n > 65535)) {
                                                    throw new IllegalArgumentException("Bad label for branch");
                                                }
                                                final int itsCodeBufferTop = this.itsCodeBufferTop;
                                                this.addToCodeBuffer(labelPC);
                                                if ((n & Integer.MIN_VALUE) != Integer.MIN_VALUE) {
                                                    this.addToCodeInt16(n);
                                                    labelPC = n + itsCodeBufferTop;
                                                    this.addSuperBlockStart(labelPC);
                                                    this.itsJumpFroms.put(labelPC, itsCodeBufferTop);
                                                }
                                                else {
                                                    labelPC = this.getLabelPC(n);
                                                    if (labelPC != -1) {
                                                        this.addToCodeInt16(labelPC - itsCodeBufferTop);
                                                        this.addSuperBlockStart(labelPC);
                                                        this.itsJumpFroms.put(labelPC, itsCodeBufferTop);
                                                    }
                                                    else {
                                                        this.addLabelFixup(n, itsCodeBufferTop + 1);
                                                        this.addToCodeInt16(0);
                                                    }
                                                }
                                                break Label_0690;
                                            }
                                            case 169: {
                                                break Label_0452;
                                            }
                                        }
                                    }
                                    break;
                                }
                                case 54:
                                case 55:
                                case 56:
                                case 57:
                                case 58: {
                                    break Label_0452;
                                }
                            }
                            break;
                        }
                        case 21:
                        case 22:
                        case 23:
                        case 24:
                        case 25: {
                            if (n < 0 || n >= 65536) {
                                throw new ClassFileFormatException("out of range variable");
                            }
                            if (n >= 256) {
                                this.addToCodeBuffer(196);
                                this.addToCodeBuffer(labelPC);
                                this.addToCodeInt16(n);
                                break;
                            }
                            this.addToCodeBuffer(labelPC);
                            this.addToCodeBuffer(n);
                            break;
                        }
                        case 18:
                        case 19:
                        case 20: {
                            if (n < 0 || n >= 65536) {
                                throw new IllegalArgumentException("out of range index");
                            }
                            if (n < 256 && labelPC != 19 && labelPC != 20) {
                                this.addToCodeBuffer(labelPC);
                                this.addToCodeBuffer(n);
                                break;
                            }
                            if (labelPC == 18) {
                                this.addToCodeBuffer(19);
                            }
                            else {
                                this.addToCodeBuffer(labelPC);
                            }
                            this.addToCodeInt16(n);
                            break;
                        }
                        case 17: {
                            if ((short)n != n) {
                                throw new IllegalArgumentException("out of range short");
                            }
                            this.addToCodeBuffer(labelPC);
                            this.addToCodeInt16(n);
                            break;
                        }
                        case 16: {
                            if ((byte)n != n) {
                                throw new IllegalArgumentException("out of range byte");
                            }
                            this.addToCodeBuffer(labelPC);
                            this.addToCodeBuffer((byte)n);
                            break;
                        }
                    }
                }
            }
            else {
                if (n < 0 || n >= 256) {
                    throw new IllegalArgumentException("out of range index");
                }
                this.addToCodeBuffer(labelPC);
                this.addToCodeBuffer(n);
            }
        }
        this.itsStackTop = (short)n2;
        if (n2 > this.itsMaxStack) {
            this.itsMaxStack = (short)n2;
        }
    }
    
    public void add(final int n, final int n2, final int n3) {
        final int n4 = this.itsStackTop + stackChange(n);
        if (n4 < 0 || 32767 < n4) {
            badStack(n4);
        }
        if (n == 132) {
            if (n2 < 0 || n2 >= 65536) {
                throw new ClassFileFormatException("out of range variable");
            }
            if (n3 < 0 || n3 >= 65536) {
                throw new ClassFileFormatException("out of range increment");
            }
            if (n2 <= 255 && n3 >= -128 && n3 <= 127) {
                this.addToCodeBuffer(132);
                this.addToCodeBuffer(n2);
                this.addToCodeBuffer(n3);
            }
            else {
                this.addToCodeBuffer(196);
                this.addToCodeBuffer(132);
                this.addToCodeInt16(n2);
                this.addToCodeInt16(n3);
            }
        }
        else {
            if (n != 197) {
                throw new IllegalArgumentException("Unexpected opcode for 2 operands");
            }
            if (n2 < 0 || n2 >= 65536) {
                throw new IllegalArgumentException("out of range index");
            }
            if (n3 < 0 || n3 >= 256) {
                throw new IllegalArgumentException("out of range dimensions");
            }
            this.addToCodeBuffer(197);
            this.addToCodeInt16(n2);
            this.addToCodeBuffer(n3);
        }
        this.itsStackTop = (short)n4;
        if (n4 > this.itsMaxStack) {
            this.itsMaxStack = (short)n4;
        }
    }
    
    public void add(final int n, final String s) {
        final int n2 = this.itsStackTop + stackChange(n);
        if (n2 < 0 || 32767 < n2) {
            badStack(n2);
        }
        if (n != 187 && n != 189) {
            switch (n) {
                default: {
                    throw new IllegalArgumentException("bad opcode for class reference");
                }
                case 192:
                case 193: {
                    break;
                }
            }
        }
        final short addClass = this.itsConstantPool.addClass(s);
        this.addToCodeBuffer(n);
        this.addToCodeInt16(addClass);
        this.itsStackTop = (short)n2;
        if (n2 > this.itsMaxStack) {
            this.itsMaxStack = (short)n2;
        }
    }
    
    public void add(final int n, final String s, final String s2, final String s3) {
        final int n2 = this.itsStackTop + stackChange(n);
        final char char1 = s3.charAt(0);
        int n3;
        if (char1 != 'J' && char1 != 'D') {
            n3 = 1;
        }
        else {
            n3 = 2;
        }
        int n4 = 0;
        switch (n) {
            default: {
                throw new IllegalArgumentException("bad opcode for field reference");
            }
            case 179:
            case 181: {
                n4 = n2 - n3;
                break;
            }
            case 178:
            case 180: {
                n4 = n2 + n3;
                break;
            }
        }
        if (n4 < 0 || 32767 < n4) {
            badStack(n4);
        }
        final short addFieldRef = this.itsConstantPool.addFieldRef(s, s2, s3);
        this.addToCodeBuffer(n);
        this.addToCodeInt16(addFieldRef);
        this.itsStackTop = (short)n4;
        if (n4 > this.itsMaxStack) {
            this.itsMaxStack = (short)n4;
        }
    }
    
    public void addALoad(final int n) {
        this.xop(42, 25, n);
    }
    
    public void addAStore(final int n) {
        this.xop(75, 58, n);
    }
    
    public void addDLoad(final int n) {
        this.xop(38, 24, n);
    }
    
    public void addDStore(final int n) {
        this.xop(71, 57, n);
    }
    
    public void addExceptionHandler(int itsExceptionTableTop, final int n, final int n2, final String s) {
        if ((itsExceptionTableTop & Integer.MIN_VALUE) != Integer.MIN_VALUE) {
            throw new IllegalArgumentException("Bad startLabel");
        }
        if ((n & Integer.MIN_VALUE) != Integer.MIN_VALUE) {
            throw new IllegalArgumentException("Bad endLabel");
        }
        if ((n2 & Integer.MIN_VALUE) != Integer.MIN_VALUE) {
            throw new IllegalArgumentException("Bad handlerLabel");
        }
        short addClass;
        if (s == null) {
            addClass = 0;
        }
        else {
            addClass = this.itsConstantPool.addClass(s);
        }
        final ExceptionTableEntry exceptionTableEntry = new ExceptionTableEntry(itsExceptionTableTop, n, n2, addClass);
        itsExceptionTableTop = this.itsExceptionTableTop;
        if (itsExceptionTableTop == 0) {
            this.itsExceptionTable = new ExceptionTableEntry[4];
        }
        else if (itsExceptionTableTop == this.itsExceptionTable.length) {
            final ExceptionTableEntry[] itsExceptionTable = new ExceptionTableEntry[itsExceptionTableTop * 2];
            System.arraycopy(this.itsExceptionTable, 0, itsExceptionTable, 0, itsExceptionTableTop);
            this.itsExceptionTable = itsExceptionTable;
        }
        this.itsExceptionTable[itsExceptionTableTop] = exceptionTableEntry;
        this.itsExceptionTableTop = itsExceptionTableTop + 1;
    }
    
    public void addFLoad(final int n) {
        this.xop(34, 23, n);
    }
    
    public void addFStore(final int n) {
        this.xop(67, 56, n);
    }
    
    public void addField(final String s, final String s2, final short n) {
        this.itsFields.add(new ClassFileField(this.itsConstantPool.addUtf8(s), this.itsConstantPool.addUtf8(s2), n));
    }
    
    public void addField(final String s, final String s2, final short n, final double n2) {
        final ClassFileField classFileField = new ClassFileField(this.itsConstantPool.addUtf8(s), this.itsConstantPool.addUtf8(s2), n);
        classFileField.setAttributes(this.itsConstantPool.addUtf8("ConstantValue"), (short)0, (short)2, this.itsConstantPool.addConstant(n2));
        this.itsFields.add(classFileField);
    }
    
    public void addField(final String s, final String s2, final short n, final int n2) {
        final ClassFileField classFileField = new ClassFileField(this.itsConstantPool.addUtf8(s), this.itsConstantPool.addUtf8(s2), n);
        classFileField.setAttributes(this.itsConstantPool.addUtf8("ConstantValue"), (short)0, (short)0, this.itsConstantPool.addConstant(n2));
        this.itsFields.add(classFileField);
    }
    
    public void addField(final String s, final String s2, final short n, final long n2) {
        final ClassFileField classFileField = new ClassFileField(this.itsConstantPool.addUtf8(s), this.itsConstantPool.addUtf8(s2), n);
        classFileField.setAttributes(this.itsConstantPool.addUtf8("ConstantValue"), (short)0, (short)2, this.itsConstantPool.addConstant(n2));
        this.itsFields.add(classFileField);
    }
    
    public void addILoad(final int n) {
        this.xop(26, 21, n);
    }
    
    public void addIStore(final int n) {
        this.xop(59, 54, n);
    }
    
    public void addInterface(final String s) {
        this.itsInterfaces.add(this.itsConstantPool.addClass(s));
    }
    
    public void addInvoke(final int n, final String s, final String s2, final String s3) {
        final int sizeOfParameters = sizeOfParameters(s3);
        final int n2 = this.itsStackTop + (short)sizeOfParameters + stackChange(n);
        if (n2 < 0 || 32767 < n2) {
            badStack(n2);
        }
        switch (n) {
            default: {
                throw new IllegalArgumentException("bad opcode for method reference");
            }
            case 182:
            case 183:
            case 184:
            case 185: {
                this.addToCodeBuffer(n);
                if (n == 185) {
                    this.addToCodeInt16(this.itsConstantPool.addInterfaceMethodRef(s, s2, s3));
                    this.addToCodeBuffer((sizeOfParameters >>> 16) + 1);
                    this.addToCodeBuffer(0);
                }
                else {
                    this.addToCodeInt16(this.itsConstantPool.addMethodRef(s, s2, s3));
                }
                this.itsStackTop = (short)n2;
                if (n2 > this.itsMaxStack) {
                    this.itsMaxStack = (short)n2;
                }
            }
        }
    }
    
    public void addLLoad(final int n) {
        this.xop(30, 22, n);
    }
    
    public void addLStore(final int n) {
        this.xop(63, 55, n);
    }
    
    public void addLineNumberEntry(final short n) {
        if (this.itsCurrentMethod == null) {
            throw new IllegalArgumentException("No method to stop");
        }
        final int itsLineNumberTableTop = this.itsLineNumberTableTop;
        if (itsLineNumberTableTop == 0) {
            this.itsLineNumberTable = new int[16];
        }
        else if (itsLineNumberTableTop == this.itsLineNumberTable.length) {
            final int[] itsLineNumberTable = new int[itsLineNumberTableTop * 2];
            System.arraycopy(this.itsLineNumberTable, 0, itsLineNumberTable, 0, itsLineNumberTableTop);
            this.itsLineNumberTable = itsLineNumberTable;
        }
        this.itsLineNumberTable[itsLineNumberTableTop] = (this.itsCodeBufferTop << 16) + n;
        this.itsLineNumberTableTop = itsLineNumberTableTop + 1;
    }
    
    public void addLoadConstant(final double n) {
        this.add(20, this.itsConstantPool.addConstant(n));
    }
    
    public void addLoadConstant(final float n) {
        this.add(18, this.itsConstantPool.addConstant(n));
    }
    
    public void addLoadConstant(final int n) {
        switch (n) {
            default: {
                this.add(18, this.itsConstantPool.addConstant(n));
            }
            case 5: {
                this.add(8);
            }
            case 4: {
                this.add(7);
            }
            case 3: {
                this.add(6);
            }
            case 2: {
                this.add(5);
            }
            case 1: {
                this.add(4);
            }
            case 0: {
                this.add(3);
            }
        }
    }
    
    public void addLoadConstant(final long n) {
        this.add(20, this.itsConstantPool.addConstant(n));
    }
    
    public void addLoadConstant(final String s) {
        this.add(18, this.itsConstantPool.addConstant(s));
    }
    
    public void addLoadThis() {
        this.add(42);
    }
    
    public void addPush(final double n) {
        if (n == 0.0) {
            this.add(14);
            if (1.0 / n < 0.0) {
                this.add(119);
            }
        }
        else {
            if (n != 1.0 && n != -1.0) {
                this.addLoadConstant(n);
                return;
            }
            this.add(15);
            if (n < 0.0) {
                this.add(119);
            }
        }
    }
    
    public void addPush(final int n) {
        if ((byte)n == n) {
            if (n == -1) {
                this.add(2);
                return;
            }
            if (n >= 0 && n <= 5) {
                this.add((byte)(n + 3));
                return;
            }
            this.add(16, (byte)n);
        }
        else {
            if ((short)n == n) {
                this.add(17, (short)n);
                return;
            }
            this.addLoadConstant(n);
        }
    }
    
    public void addPush(final long n) {
        final int n2 = (int)n;
        if (n2 == n) {
            this.addPush(n2);
            this.add(133);
            return;
        }
        this.addLoadConstant(n);
    }
    
    public void addPush(final String s) {
        final int length = s.length();
        final ConstantPool itsConstantPool = this.itsConstantPool;
        int n = 0;
        int n2 = itsConstantPool.getUtfEncodingLimit(s, 0, length);
        if (n2 == length) {
            this.addLoadConstant(s);
            return;
        }
        this.add(187, "java/lang/StringBuilder");
        this.add(89);
        this.addPush(length);
        this.addInvoke(183, "java/lang/StringBuilder", "<init>", "(I)V");
        while (true) {
            this.add(89);
            this.addLoadConstant(s.substring(n, n2));
            this.addInvoke(182, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
            this.add(87);
            if (n2 == length) {
                break;
            }
            n = n2;
            n2 = this.itsConstantPool.getUtfEncodingLimit(s, n2, length);
        }
        this.addInvoke(182, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
    }
    
    public void addPush(final boolean b) {
        int n;
        if (b) {
            n = 4;
        }
        else {
            n = 3;
        }
        this.add(n);
    }
    
    public int addTableSwitch(int putInt32, final int n) {
        if (putInt32 > n) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Bad bounds: ");
            sb.append(putInt32);
            sb.append(' ');
            sb.append(n);
            throw new ClassFileFormatException(sb.toString());
        }
        final int n2 = this.itsStackTop + stackChange(170);
        if (n2 < 0 || 32767 < n2) {
            badStack(n2);
        }
        int i = ~this.itsCodeBufferTop & 0x3;
        final int addReservedCodeSpace = this.addReservedCodeSpace(i + 1 + (n - putInt32 + 1 + 3) * 4);
        final byte[] itsCodeBuffer = this.itsCodeBuffer;
        int n3 = addReservedCodeSpace + 1;
        itsCodeBuffer[addReservedCodeSpace] = -86;
        while (i != 0) {
            this.itsCodeBuffer[n3] = 0;
            --i;
            ++n3;
        }
        putInt32 = putInt32(putInt32, this.itsCodeBuffer, n3 + 4);
        putInt32(n, this.itsCodeBuffer, putInt32);
        this.itsStackTop = (short)n2;
        if (n2 > this.itsMaxStack) {
            this.itsMaxStack = (short)n2;
        }
        return addReservedCodeSpace;
    }
    
    public void addVariableDescriptor(final String s, final String s2, final int n, final int n2) {
        final short addUtf8 = this.itsConstantPool.addUtf8(s);
        final short addUtf9 = this.itsConstantPool.addUtf8(s2);
        if (this.itsVarDescriptors == null) {
            this.itsVarDescriptors = new ObjArray();
        }
        this.itsVarDescriptors.add(new int[] { addUtf8, addUtf9, n, n2 });
    }
    
    public void adjustStackTop(int n) {
        n += this.itsStackTop;
        if (n < 0 || 32767 < n) {
            badStack(n);
        }
        this.itsStackTop = (short)n;
        if (n > this.itsMaxStack) {
            this.itsMaxStack = (short)n;
        }
    }
    
    final char[] getCharBuffer(final int n) {
        if (n > this.tmpCharBuffer.length) {
            int n2;
            if (n > (n2 = this.tmpCharBuffer.length * 2)) {
                n2 = n;
            }
            this.tmpCharBuffer = new char[n2];
        }
        return this.tmpCharBuffer;
    }
    
    public final String getClassName() {
        return this.generatedClassName;
    }
    
    public int getCurrentCodeOffset() {
        return this.itsCodeBufferTop;
    }
    
    public int getLabelPC(int n) {
        if (n >= 0) {
            throw new IllegalArgumentException("Bad label, no biscuit");
        }
        n &= Integer.MAX_VALUE;
        if (n >= this.itsLabelTableTop) {
            throw new IllegalArgumentException("Bad label");
        }
        return this.itsLabelTable[n];
    }
    
    public short getStackTop() {
        return this.itsStackTop;
    }
    
    public boolean isUnderStringSizeLimit(final String s) {
        return this.itsConstantPool.isUnderUtfEncodingLimit(s);
    }
    
    public void markHandler(final int n) {
        this.itsStackTop = 1;
        this.markLabel(n);
    }
    
    public void markLabel(int n) {
        if (n >= 0) {
            throw new IllegalArgumentException("Bad label, no biscuit");
        }
        n &= Integer.MAX_VALUE;
        if (n > this.itsLabelTableTop) {
            throw new IllegalArgumentException("Bad label");
        }
        if (this.itsLabelTable[n] != -1) {
            throw new IllegalStateException("Can only mark label once");
        }
        this.itsLabelTable[n] = this.itsCodeBufferTop;
    }
    
    public void markLabel(final int n, final short itsStackTop) {
        this.markLabel(n);
        this.itsStackTop = itsStackTop;
    }
    
    public final void markTableSwitchCase(final int n, final int n2) {
        this.addSuperBlockStart(this.itsCodeBufferTop);
        this.itsJumpFroms.put(this.itsCodeBufferTop, n);
        this.setTableSwitchJump(n, n2, this.itsCodeBufferTop);
    }
    
    public final void markTableSwitchCase(final int n, final int n2, final int n3) {
        if (n3 >= 0 && n3 <= this.itsMaxStack) {
            this.itsStackTop = (short)n3;
            this.addSuperBlockStart(this.itsCodeBufferTop);
            this.itsJumpFroms.put(this.itsCodeBufferTop, n);
            this.setTableSwitchJump(n, n2, this.itsCodeBufferTop);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Bad stack index: ");
        sb.append(n3);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public final void markTableSwitchDefault(final int n) {
        this.addSuperBlockStart(this.itsCodeBufferTop);
        this.itsJumpFroms.put(this.itsCodeBufferTop, n);
        this.setTableSwitchJump(n, -1, this.itsCodeBufferTop);
    }
    
    public void setFlags(final short itsFlags) {
        this.itsFlags = itsFlags;
    }
    
    public void setStackTop(final short itsStackTop) {
        this.itsStackTop = itsStackTop;
    }
    
    public void setTableSwitchJump(final int n, final int n2, final int n3) {
        if (n3 < 0 || n3 > this.itsCodeBufferTop) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Bad jump target: ");
            sb.append(n3);
            throw new IllegalArgumentException(sb.toString());
        }
        if (n2 < -1) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Bad case index: ");
            sb2.append(n2);
            throw new IllegalArgumentException(sb2.toString());
        }
        final int n4 = ~n & 0x3;
        int n5;
        if (n2 < 0) {
            n5 = n + 1 + n4;
        }
        else {
            n5 = n + 1 + n4 + (n2 + 3) * 4;
        }
        if (n < 0 || n > this.itsCodeBufferTop - 16 - n4 - 1) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(n);
            sb3.append(" is outside a possible range of tableswitch");
            sb3.append(" in already generated code");
            throw new IllegalArgumentException(sb3.toString());
        }
        if ((this.itsCodeBuffer[n] & 0xFF) != 0xAA) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(n);
            sb4.append(" is not offset of tableswitch statement");
            throw new IllegalArgumentException(sb4.toString());
        }
        if (n5 >= 0 && n5 + 4 <= this.itsCodeBufferTop) {
            putInt32(n3 - n, this.itsCodeBuffer, n5);
            return;
        }
        final StringBuilder sb5 = new StringBuilder();
        sb5.append("Too big case index: ");
        sb5.append(n2);
        throw new ClassFileFormatException(sb5.toString());
    }
    
    public void startMethod(final String s, final String s2, final short n) {
        this.itsCurrentMethod = new ClassFileMethod(s, this.itsConstantPool.addUtf8(s), s2, this.itsConstantPool.addUtf8(s2), n);
        this.itsJumpFroms = new UintMap();
        this.itsMethods.add(this.itsCurrentMethod);
        this.addSuperBlockStart(0);
    }
    
    public void stopMethod(final short itsMaxLocals) {
        if (this.itsCurrentMethod == null) {
            throw new IllegalStateException("No method to stop");
        }
        this.fixLabelGotos();
        this.itsMaxLocals = itsMaxLocals;
        StackMapTable stackMapTable = null;
        if (ClassFileWriter.GenerateStackMap) {
            this.finalizeSuperBlockStarts();
            stackMapTable = new StackMapTable();
            stackMapTable.generate();
        }
        int n = 0;
        if (this.itsLineNumberTable != null) {
            n = this.itsLineNumberTableTop * 4 + 8;
        }
        int n2 = 0;
        if (this.itsVarDescriptors != null) {
            n2 = this.itsVarDescriptors.size() * 10 + 8;
        }
        int n4;
        final int n3 = n4 = 0;
        if (stackMapTable != null) {
            final int computeWriteSize = stackMapTable.computeWriteSize();
            n4 = n3;
            if (computeWriteSize > 0) {
                n4 = computeWriteSize + 6;
            }
        }
        final int n5 = this.itsCodeBufferTop + 14 + 2 + this.itsExceptionTableTop * 8 + 2 + n + n2 + n4;
        if (n5 > 65536) {
            throw new ClassFileFormatException("generated bytecode for method exceeds 64K limit.");
        }
        final byte[] codeAttribute = new byte[n5];
        final int putInt32 = putInt32(this.itsCodeBufferTop, codeAttribute, putInt16(this.itsMaxLocals, codeAttribute, putInt16(this.itsMaxStack, codeAttribute, putInt32(n5 - 6, codeAttribute, putInt16(this.itsConstantPool.addUtf8("Code"), codeAttribute, 0)))));
        System.arraycopy(this.itsCodeBuffer, 0, codeAttribute, putInt32, this.itsCodeBufferTop);
        final int n6 = putInt32 + this.itsCodeBufferTop;
        int putInt33;
        if (this.itsExceptionTableTop > 0) {
            int n7 = putInt16(this.itsExceptionTableTop, codeAttribute, n6);
            for (int i = 0; i < this.itsExceptionTableTop; ++i) {
                final ExceptionTableEntry exceptionTableEntry = this.itsExceptionTable[i];
                final short n8 = (short)this.getLabelPC(exceptionTableEntry.itsStartLabel);
                final short n9 = (short)this.getLabelPC(exceptionTableEntry.itsEndLabel);
                final short n10 = (short)this.getLabelPC(exceptionTableEntry.itsHandlerLabel);
                final short itsCatchType = exceptionTableEntry.itsCatchType;
                if (n8 == -1) {
                    throw new IllegalStateException("start label not defined");
                }
                if (n9 == -1) {
                    throw new IllegalStateException("end label not defined");
                }
                if (n10 == -1) {
                    throw new IllegalStateException("handler label not defined");
                }
                n7 = putInt16(itsCatchType, codeAttribute, putInt16(n10, codeAttribute, putInt16(n9, codeAttribute, putInt16(n8, codeAttribute, n7))));
            }
            putInt33 = n7;
        }
        else {
            putInt33 = putInt16(0, codeAttribute, n6);
        }
        int n11 = 0;
        if (this.itsLineNumberTable != null) {
            n11 = 0 + 1;
        }
        int n12 = n11;
        if (this.itsVarDescriptors != null) {
            n12 = n11 + 1;
        }
        int n13 = n12;
        if (n4 > 0) {
            n13 = n12 + 1;
        }
        int n15;
        final int n14 = n15 = putInt16(n13, codeAttribute, putInt33);
        if (this.itsLineNumberTable != null) {
            n15 = putInt16(this.itsLineNumberTableTop, codeAttribute, putInt32(this.itsLineNumberTableTop * 4 + 2, codeAttribute, putInt16(this.itsConstantPool.addUtf8("LineNumberTable"), codeAttribute, n14)));
            for (int j = 0; j < this.itsLineNumberTableTop; ++j) {
                n15 = putInt32(this.itsLineNumberTable[j], codeAttribute, n15);
            }
        }
        if (this.itsVarDescriptors != null) {
            final int putInt34 = putInt16(this.itsConstantPool.addUtf8("LocalVariableTable"), codeAttribute, n15);
            final int size = this.itsVarDescriptors.size();
            n15 = putInt16(size, codeAttribute, putInt32(size * 10 + 2, codeAttribute, putInt34));
            for (int k = 0; k < size; ++k) {
                final int[] array = (int[])this.itsVarDescriptors.get(k);
                final int n16 = array[0];
                final int n17 = array[1];
                final int n18 = array[2];
                n15 = putInt16(array[3], codeAttribute, putInt16(n17, codeAttribute, putInt16(n16, codeAttribute, putInt16(this.itsCodeBufferTop - n18, codeAttribute, putInt16(n18, codeAttribute, n15)))));
            }
        }
        if (n4 > 0) {
            stackMapTable.write(codeAttribute, putInt16(this.itsConstantPool.addUtf8("StackMapTable"), codeAttribute, n15));
        }
        this.itsCurrentMethod.setCodeAttribute(codeAttribute);
        this.itsExceptionTable = null;
        this.itsExceptionTableTop = 0;
        this.itsLineNumberTableTop = 0;
        this.itsCodeBufferTop = 0;
        this.itsCurrentMethod = null;
        this.itsMaxStack = 0;
        this.itsStackTop = 0;
        this.itsLabelTableTop = 0;
        this.itsFixupTableTop = 0;
        this.itsVarDescriptors = null;
        this.itsSuperBlockStarts = null;
        this.itsSuperBlockStartsTop = 0;
        this.itsJumpFroms = null;
    }
    
    public byte[] toByteArray() {
        final int writeSize = this.getWriteSize();
        final byte[] array = new byte[writeSize];
        int addUtf8 = 0;
        if (this.itsSourceFileNameIndex != 0) {
            addUtf8 = this.itsConstantPool.addUtf8("SourceFile");
        }
        int n = putInt16(this.itsInterfaces.size(), array, putInt16(this.itsSuperClassIndex, array, putInt16(this.itsThisClassIndex, array, putInt16(this.itsFlags, array, this.itsConstantPool.write(array, putInt16(ClassFileWriter.MajorVersion, array, putInt16(ClassFileWriter.MinorVersion, array, putInt32(-889275714, array, 0))))))));
        for (int i = 0; i < this.itsInterfaces.size(); ++i) {
            n = putInt16((short)this.itsInterfaces.get(i), array, n);
        }
        int n2 = putInt16(this.itsFields.size(), array, n);
        for (int j = 0; j < this.itsFields.size(); ++j) {
            n2 = ((ClassFileField)this.itsFields.get(j)).write(array, n2);
        }
        int n3 = putInt16(this.itsMethods.size(), array, n2);
        for (int k = 0; k < this.itsMethods.size(); ++k) {
            n3 = ((ClassFileMethod)this.itsMethods.get(k)).write(array, n3);
        }
        int n4;
        if (this.itsSourceFileNameIndex != 0) {
            n4 = putInt16(this.itsSourceFileNameIndex, array, putInt32(2, array, putInt16(addUtf8, array, putInt16(1, array, n3))));
        }
        else {
            n4 = putInt16(0, array, n3);
        }
        if (n4 != writeSize) {
            throw new RuntimeException();
        }
        return array;
    }
    
    public void write(final OutputStream outputStream) throws IOException {
        outputStream.write(this.toByteArray());
    }
    
    public static class ClassFileFormatException extends RuntimeException
    {
        private static final long serialVersionUID = 1263998431033790599L;
        
        ClassFileFormatException(final String s) {
            super(s);
        }
    }
    
    final class StackMapTable
    {
        static final boolean DEBUGSTACKMAP = false;
        private int[] locals;
        private int localsTop;
        private byte[] rawStackMap;
        private int rawStackMapTop;
        private int[] stack;
        private int stackTop;
        private SuperBlock[] superBlockDeps;
        private SuperBlock[] superBlocks;
        private boolean wide;
        private SuperBlock[] workList;
        private int workListTop;
        
        StackMapTable() {
            this.superBlocks = null;
            this.stack = null;
            this.locals = null;
            this.workList = null;
            this.rawStackMap = null;
            this.localsTop = 0;
            this.stackTop = 0;
            this.workListTop = 0;
            this.rawStackMapTop = 0;
            this.wide = false;
        }
        
        private void addToWorkList(final SuperBlock superBlock) {
            if (!superBlock.isInQueue()) {
                superBlock.setInQueue(true);
                superBlock.setInitialized(true);
                if (this.workListTop == this.workList.length) {
                    final SuperBlock[] workList = new SuperBlock[this.workListTop * 2];
                    System.arraycopy(this.workList, 0, workList, 0, this.workListTop);
                    this.workList = workList;
                }
                this.workList[this.workListTop++] = superBlock;
            }
        }
        
        private void clearStack() {
            this.stackTop = 0;
        }
        
        private void computeRawStackMap() {
            int[] trimmedLocals = this.superBlocks[0].getTrimmedLocals();
            int start = -1;
            for (int i = 1; i < this.superBlocks.length; ++i) {
                final SuperBlock superBlock = this.superBlocks[i];
                final int[] trimmedLocals2 = superBlock.getTrimmedLocals();
                final int[] stack = superBlock.getStack();
                final int n = superBlock.getStart() - start - 1;
                if (stack.length == 0) {
                    int n2;
                    if (trimmedLocals.length > trimmedLocals2.length) {
                        n2 = trimmedLocals2.length;
                    }
                    else {
                        n2 = trimmedLocals.length;
                    }
                    final int abs = Math.abs(trimmedLocals.length - trimmedLocals2.length);
                    int n3;
                    for (n3 = 0; n3 < n2 && trimmedLocals[n3] == trimmedLocals2[n3]; ++n3) {}
                    if (n3 == trimmedLocals2.length && abs == 0) {
                        this.writeSameFrame(trimmedLocals2, n);
                    }
                    else if (n3 == trimmedLocals2.length && abs <= 3) {
                        this.writeChopFrame(abs, n);
                    }
                    else if (n3 == trimmedLocals.length && abs <= 3) {
                        this.writeAppendFrame(trimmedLocals2, abs, n);
                    }
                    else {
                        this.writeFullFrame(trimmedLocals2, stack, n);
                    }
                }
                else if (stack.length == 1) {
                    if (Arrays.equals(trimmedLocals, trimmedLocals2)) {
                        this.writeSameLocalsOneStackItemFrame(trimmedLocals2, stack, n);
                    }
                    else {
                        this.writeFullFrame(trimmedLocals2, stack, n);
                    }
                }
                else {
                    this.writeFullFrame(trimmedLocals2, stack, n);
                }
                trimmedLocals = trimmedLocals2;
                start = superBlock.getStart();
            }
        }
        
        private int execute(int i) {
            final int n = ClassFileWriter.this.itsCodeBuffer[i] & 0xFF;
            final int n2 = 0;
            final int n3 = 0;
            final int n4 = 0;
            final int n5 = 0;
            final int n6 = 0;
            int n7 = 2;
            final int n8 = 1;
            Label_2300: {
                Label_1617: {
                    Label_1596: {
                        Label_1565: {
                            switch (n) {
                                default: {
                                    final StringBuilder sb = new StringBuilder();
                                    sb.append("bad opcode: ");
                                    sb.append(n);
                                    throw new IllegalArgumentException(sb.toString());
                                }
                                case 196: {
                                    this.wide = true;
                                    i = n6;
                                    break Label_2300;
                                }
                                case 192: {
                                    this.pop();
                                    this.push(TypeInfo.OBJECT(this.getOperand(i + 1, 2)));
                                    i = n6;
                                    break Label_2300;
                                }
                                case 191: {
                                    i = this.pop();
                                    this.clearStack();
                                    this.push(i);
                                    break Label_1617;
                                }
                                case 189: {
                                    i = this.getOperand(i + 1, 2);
                                    final String s = (String)ClassFileWriter.this.itsConstantPool.getConstantData(i);
                                    this.pop();
                                    final StringBuilder sb2 = new StringBuilder();
                                    sb2.append("[L");
                                    sb2.append(s);
                                    sb2.append(';');
                                    this.push(TypeInfo.OBJECT(sb2.toString(), ClassFileWriter.this.itsConstantPool));
                                    break;
                                }
                                case 188: {
                                    this.pop();
                                    final char access$900 = arrayTypeToName(ClassFileWriter.this.itsCodeBuffer[i + 1]);
                                    final ConstantPool access$901 = ClassFileWriter.this.itsConstantPool;
                                    final StringBuilder sb3 = new StringBuilder();
                                    sb3.append("[");
                                    sb3.append(access$900);
                                    this.push(TypeInfo.OBJECT(access$901.addClass(sb3.toString())));
                                    break;
                                }
                                case 187: {
                                    this.push(TypeInfo.UNINITIALIZED_VARIABLE(i));
                                    i = n6;
                                    break Label_2300;
                                }
                                case 182:
                                case 183:
                                case 184:
                                case 185: {
                                    final int operand = this.getOperand(i + 1, 2);
                                    final FieldOrMethodRef fieldOrMethodRef = (FieldOrMethodRef)ClassFileWriter.this.itsConstantPool.getConstantData(operand);
                                    final String type = fieldOrMethodRef.getType();
                                    final String name = fieldOrMethodRef.getName();
                                    int access$902;
                                    for (access$902 = sizeOfParameters(type), i = 0; i < access$902 >>> 16; ++i) {
                                        this.pop();
                                    }
                                    if (n != 184) {
                                        i = this.pop();
                                        final int tag = TypeInfo.getTag(i);
                                        if (tag == TypeInfo.UNINITIALIZED_VARIABLE(0) || tag == 6) {
                                            if (!"<init>".equals(name)) {
                                                throw new IllegalStateException("bad instance");
                                            }
                                            this.initializeTypeInfo(i, TypeInfo.OBJECT(ClassFileWriter.this.itsThisClassIndex));
                                        }
                                    }
                                    final String access$903 = descriptorToInternalName(type.substring(type.indexOf(41) + 1));
                                    i = operand;
                                    if (!access$903.equals("V")) {
                                        this.push(TypeInfo.fromType(access$903, ClassFileWriter.this.itsConstantPool));
                                        i = operand;
                                        break;
                                    }
                                    break;
                                }
                                case 180: {
                                    this.pop();
                                }
                                case 178: {
                                    i = this.getOperand(i + 1, 2);
                                    this.push(TypeInfo.fromType(descriptorToInternalName(((FieldOrMethodRef)ClassFileWriter.this.itsConstantPool.getConstantData(i)).getType()), ClassFileWriter.this.itsConstantPool));
                                    break;
                                }
                                case 172:
                                case 173:
                                case 174:
                                case 175:
                                case 176:
                                case 177: {
                                    this.clearStack();
                                    i = n6;
                                    break Label_2300;
                                }
                                case 170: {
                                    final int n9 = i + 1 + (0x3 & ~i);
                                    i = (this.getOperand(n9 + 8, 4) - this.getOperand(n9 + 4, 4) + 4) * 4 + n9 - i;
                                    this.pop();
                                    break Label_2300;
                                }
                                case 95: {
                                    i = this.pop();
                                    final int pop = this.pop();
                                    this.push(i);
                                    this.push(pop);
                                    break Label_1596;
                                }
                                case 94: {
                                    final long pop2 = this.pop2();
                                    final long pop3 = this.pop2();
                                    this.push2(pop2);
                                    this.push2(pop3);
                                    this.push2(pop2);
                                    i = n2;
                                    break Label_2300;
                                }
                                case 93: {
                                    final long pop4 = this.pop2();
                                    i = this.pop();
                                    this.push2(pop4);
                                    this.push(i);
                                    this.push2(pop4);
                                    break Label_1565;
                                }
                                case 92: {
                                    final long pop5 = this.pop2();
                                    this.push2(pop5);
                                    this.push2(pop5);
                                    break Label_1565;
                                }
                                case 91: {
                                    i = this.pop();
                                    final long pop6 = this.pop2();
                                    this.push(i);
                                    this.push2(pop6);
                                    this.push(i);
                                    break Label_1565;
                                }
                                case 90: {
                                    i = this.pop();
                                    final int pop7 = this.pop();
                                    this.push(i);
                                    this.push(pop7);
                                    this.push(i);
                                    break Label_1596;
                                }
                                case 89: {
                                    i = this.pop();
                                    this.push(i);
                                    this.push(i);
                                    break Label_1617;
                                }
                                case 88: {
                                    this.pop2();
                                    i = n6;
                                    break Label_2300;
                                }
                                case 79:
                                case 80:
                                case 81:
                                case 82:
                                case 83:
                                case 84:
                                case 85:
                                case 86: {
                                    this.pop();
                                }
                                case 159:
                                case 160:
                                case 161:
                                case 162:
                                case 163:
                                case 164:
                                case 165:
                                case 166:
                                case 181: {
                                    this.pop();
                                }
                                case 87:
                                case 153:
                                case 154:
                                case 155:
                                case 156:
                                case 157:
                                case 158:
                                case 179:
                                case 194:
                                case 195:
                                case 198:
                                case 199: {
                                    this.pop();
                                    i = n6;
                                    break Label_2300;
                                }
                                case 75:
                                case 76:
                                case 77:
                                case 78: {
                                    this.executeAStore(n - 75);
                                    i = n6;
                                    break Label_2300;
                                }
                                case 71:
                                case 72:
                                case 73:
                                case 74: {
                                    this.executeStore(n - 71, 3);
                                    i = n6;
                                    break Label_2300;
                                }
                                case 67:
                                case 68:
                                case 69:
                                case 70: {
                                    this.executeStore(n - 67, 2);
                                    i = n6;
                                    break Label_2300;
                                }
                                case 63:
                                case 64:
                                case 65:
                                case 66: {
                                    this.executeStore(n - 63, 4);
                                    i = n6;
                                    break Label_2300;
                                }
                                case 59:
                                case 60:
                                case 61:
                                case 62: {
                                    this.executeStore(n - 59, 1);
                                    i = n6;
                                    break Label_2300;
                                }
                                case 58: {
                                    if (!this.wide) {
                                        n7 = 1;
                                    }
                                    this.executeAStore(this.getOperand(i + 1, n7));
                                    i = n6;
                                    break Label_2300;
                                }
                                case 57: {
                                    if (!this.wide) {
                                        n7 = 1;
                                    }
                                    this.executeStore(this.getOperand(i + 1, n7), 3);
                                    i = n6;
                                    break Label_2300;
                                }
                                case 56: {
                                    int n10 = n8;
                                    if (this.wide) {
                                        n10 = 2;
                                    }
                                    this.executeStore(this.getOperand(i + 1, n10), 2);
                                    i = n6;
                                    break Label_2300;
                                }
                                case 55: {
                                    if (!this.wide) {
                                        n7 = 1;
                                    }
                                    this.executeStore(this.getOperand(i + 1, n7), 4);
                                    i = n6;
                                    break Label_2300;
                                }
                                case 54: {
                                    if (!this.wide) {
                                        n7 = 1;
                                    }
                                    this.executeStore(this.getOperand(i + 1, n7), 1);
                                    i = n6;
                                    break Label_2300;
                                }
                                case 50: {
                                    this.pop();
                                    i = this.pop();
                                    final String s2 = (String)ClassFileWriter.this.itsConstantPool.getConstantData(i >>> 8);
                                    if (s2.charAt(0) != '[') {
                                        throw new IllegalStateException("bad array type");
                                    }
                                    this.push(TypeInfo.OBJECT(ClassFileWriter.this.itsConstantPool.addClass(descriptorToInternalName(s2.substring(1)))));
                                    i = n6;
                                    break Label_2300;
                                }
                                case 42:
                                case 43:
                                case 44:
                                case 45: {
                                    this.executeALoad(n - 42);
                                    i = n6;
                                    break Label_2300;
                                }
                                case 25: {
                                    if (!this.wide) {
                                        n7 = 1;
                                    }
                                    this.executeALoad(this.getOperand(i + 1, n7));
                                    i = n6;
                                    break Label_2300;
                                }
                                case 18:
                                case 19:
                                case 20: {
                                    if (n == 18) {
                                        i = this.getOperand(i + 1);
                                    }
                                    else {
                                        i = this.getOperand(i + 1, 2);
                                    }
                                    i = ClassFileWriter.this.itsConstantPool.getConstantType(i);
                                    switch (i) {
                                        default: {
                                            final StringBuilder sb4 = new StringBuilder();
                                            sb4.append("bad const type ");
                                            sb4.append(i);
                                            throw new IllegalArgumentException(sb4.toString());
                                        }
                                        case 8: {
                                            this.push(TypeInfo.OBJECT("java/lang/String", ClassFileWriter.this.itsConstantPool));
                                            break;
                                        }
                                        case 6: {
                                            this.push(3);
                                            break;
                                        }
                                        case 5: {
                                            this.push(4);
                                            break;
                                        }
                                        case 4: {
                                            this.push(2);
                                            break;
                                        }
                                        case 3: {
                                            this.push(1);
                                            break;
                                        }
                                    }
                                    break;
                                }
                                case 119:
                                case 135:
                                case 138:
                                case 141: {
                                    this.pop();
                                }
                                case 14:
                                case 15:
                                case 24:
                                case 38:
                                case 39:
                                case 40:
                                case 41: {
                                    this.push(3);
                                    i = n6;
                                    break Label_2300;
                                }
                                case 49:
                                case 99:
                                case 103:
                                case 107:
                                case 111:
                                case 115: {
                                    this.pop();
                                }
                                case 118:
                                case 134:
                                case 137:
                                case 144: {
                                    this.pop();
                                }
                                case 11:
                                case 12:
                                case 13:
                                case 23:
                                case 34:
                                case 35:
                                case 36:
                                case 37: {
                                    this.push(2);
                                    i = n6;
                                    break Label_2300;
                                }
                                case 48:
                                case 98:
                                case 102:
                                case 106:
                                case 110:
                                case 114: {
                                    this.pop();
                                }
                                case 117:
                                case 133:
                                case 140:
                                case 143: {
                                    this.pop();
                                }
                                case 9:
                                case 10:
                                case 22:
                                case 30:
                                case 31:
                                case 32:
                                case 33: {
                                    this.push(4);
                                    i = n6;
                                    break Label_2300;
                                }
                                case 47:
                                case 97:
                                case 101:
                                case 105:
                                case 109:
                                case 113:
                                case 121:
                                case 123:
                                case 125:
                                case 127:
                                case 129:
                                case 131: {
                                    this.pop();
                                }
                                case 116:
                                case 136:
                                case 139:
                                case 142:
                                case 145:
                                case 146:
                                case 147:
                                case 190:
                                case 193: {
                                    this.pop();
                                }
                                case 2:
                                case 3:
                                case 4:
                                case 5:
                                case 6:
                                case 7:
                                case 8:
                                case 16:
                                case 17:
                                case 21:
                                case 26:
                                case 27:
                                case 28:
                                case 29: {
                                    this.push(1);
                                    i = n6;
                                    break Label_2300;
                                }
                                case 46:
                                case 51:
                                case 52:
                                case 53:
                                case 96:
                                case 100:
                                case 104:
                                case 108:
                                case 112:
                                case 120:
                                case 122:
                                case 124:
                                case 126:
                                case 128:
                                case 130:
                                case 148:
                                case 149:
                                case 150:
                                case 151:
                                case 152: {
                                    this.pop();
                                }
                                case 1: {
                                    this.push(5);
                                    i = n6;
                                    break Label_2300;
                                }
                                case 0:
                                case 132:
                                case 167:
                                case 200: {
                                    i = n6;
                                    break Label_2300;
                                }
                            }
                            i = n5;
                            break Label_2300;
                        }
                        i = n5;
                        break Label_2300;
                    }
                    i = n4;
                    break Label_2300;
                }
                i = n3;
            }
            int opcodeLength = i;
            if (i == 0) {
                opcodeLength = ClassFileWriter.opcodeLength(n, this.wide);
            }
            if (this.wide && n != 196) {
                this.wide = false;
            }
            return opcodeLength;
        }
        
        private void executeALoad(final int n) {
            final int local = this.getLocal(n);
            final int tag = TypeInfo.getTag(local);
            if (tag != 7 && tag != 6 && tag != 8 && tag != 5) {
                final StringBuilder sb = new StringBuilder();
                sb.append("bad local variable type: ");
                sb.append(local);
                sb.append(" at index: ");
                sb.append(n);
                throw new IllegalStateException(sb.toString());
            }
            this.push(local);
        }
        
        private void executeAStore(final int n) {
            this.setLocal(n, this.pop());
        }
        
        private void executeBlock(final SuperBlock superBlock) {
            int n = 0;
            int n2;
            int execute;
            for (int i = superBlock.getStart(); i < superBlock.getEnd(); i += execute, n = n2) {
                n2 = (ClassFileWriter.this.itsCodeBuffer[i] & 0xFF);
                execute = this.execute(i);
                if (this.isBranch(n2)) {
                    this.flowInto(this.getBranchTarget(i));
                }
                else if (n2 == 170) {
                    final int n3 = i + 1 + (~i & 0x3);
                    this.flowInto(this.getSuperBlockFromOffset(i + this.getOperand(n3, 4)));
                    for (int operand = this.getOperand(n3 + 4, 4), operand2 = this.getOperand(n3 + 8, 4), j = 0; j < operand2 - operand + 1; ++j) {
                        this.flowInto(this.getSuperBlockFromOffset(this.getOperand(j * 4 + (n3 + 12), 4) + i));
                    }
                }
                for (int k = 0; k < ClassFileWriter.this.itsExceptionTableTop; ++k) {
                    final ExceptionTableEntry exceptionTableEntry = ClassFileWriter.this.itsExceptionTable[k];
                    final short n4 = (short)ClassFileWriter.this.getLabelPC(exceptionTableEntry.itsStartLabel);
                    final short n5 = (short)ClassFileWriter.this.getLabelPC(exceptionTableEntry.itsEndLabel);
                    if (i >= n4) {
                        if (i < n5) {
                            final SuperBlock superBlockFromOffset = this.getSuperBlockFromOffset((short)ClassFileWriter.this.getLabelPC(exceptionTableEntry.itsHandlerLabel));
                            int n6;
                            if (exceptionTableEntry.itsCatchType == 0) {
                                n6 = TypeInfo.OBJECT(ClassFileWriter.this.itsConstantPool.addClass("java/lang/Throwable"));
                            }
                            else {
                                n6 = TypeInfo.OBJECT(exceptionTableEntry.itsCatchType);
                            }
                            superBlockFromOffset.merge(this.locals, this.localsTop, new int[] { n6 }, 1, ClassFileWriter.this.itsConstantPool);
                            this.addToWorkList(superBlockFromOffset);
                        }
                    }
                }
            }
            if (!this.isSuperBlockEnd(n)) {
                final int n7 = superBlock.getIndex() + 1;
                if (n7 < this.superBlocks.length) {
                    this.flowInto(this.superBlocks[n7]);
                }
            }
        }
        
        private void executeStore(final int n, final int n2) {
            this.pop();
            this.setLocal(n, n2);
        }
        
        private void executeWorkList() {
            while (this.workListTop > 0) {
                final SuperBlock[] workList = this.workList;
                final int workListTop = this.workListTop - 1;
                this.workListTop = workListTop;
                final SuperBlock superBlock = workList[workListTop];
                superBlock.setInQueue(false);
                this.locals = superBlock.getLocals();
                this.stack = superBlock.getStack();
                this.localsTop = this.locals.length;
                this.stackTop = this.stack.length;
                this.executeBlock(superBlock);
            }
        }
        
        private void flowInto(final SuperBlock superBlock) {
            if (superBlock.merge(this.locals, this.localsTop, this.stack, this.stackTop, ClassFileWriter.this.itsConstantPool)) {
                this.addToWorkList(superBlock);
            }
        }
        
        private SuperBlock getBranchTarget(int n) {
            if ((ClassFileWriter.this.itsCodeBuffer[n] & 0xFF) == 0xC8) {
                n += this.getOperand(n + 1, 4);
            }
            else {
                n += (short)this.getOperand(n + 1, 2);
            }
            return this.getSuperBlockFromOffset(n);
        }
        
        private int getLocal(final int n) {
            if (n < this.localsTop) {
                return this.locals[n];
            }
            return 0;
        }
        
        private int getOperand(final int n) {
            return this.getOperand(n, 1);
        }
        
        private int getOperand(final int n, final int n2) {
            int n3 = 0;
            if (n2 > 4) {
                throw new IllegalArgumentException("bad operand size");
            }
            for (int i = 0; i < n2; ++i) {
                n3 = (n3 << 8 | (ClassFileWriter.this.itsCodeBuffer[n + i] & 0xFF));
            }
            return n3;
        }
        
        private SuperBlock[] getSuperBlockDependencies() {
            final SuperBlock[] array = new SuperBlock[this.superBlocks.length];
            final int n = 0;
            for (int i = 0; i < ClassFileWriter.this.itsExceptionTableTop; ++i) {
                final ExceptionTableEntry exceptionTableEntry = ClassFileWriter.this.itsExceptionTable[i];
                array[this.getSuperBlockFromOffset((short)ClassFileWriter.this.getLabelPC(exceptionTableEntry.itsHandlerLabel)).getIndex()] = this.getSuperBlockFromOffset((short)ClassFileWriter.this.getLabelPC(exceptionTableEntry.itsStartLabel));
            }
            final int[] keys = ClassFileWriter.this.itsJumpFroms.getKeys();
            for (int j = n; j < keys.length; ++j) {
                final int n2 = keys[j];
                array[this.getSuperBlockFromOffset(n2).getIndex()] = this.getSuperBlockFromOffset(ClassFileWriter.this.itsJumpFroms.getInt(n2, -1));
            }
            return array;
        }
        
        private SuperBlock getSuperBlockFromOffset(final int n) {
            for (int i = 0; i < this.superBlocks.length; ++i) {
                final SuperBlock superBlock = this.superBlocks[i];
                if (superBlock == null) {
                    break;
                }
                if (n >= superBlock.getStart() && n < superBlock.getEnd()) {
                    return superBlock;
                }
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("bad offset: ");
            sb.append(n);
            throw new IllegalArgumentException(sb.toString());
        }
        
        private int getWorstCaseWriteSize() {
            return (this.superBlocks.length - 1) * (ClassFileWriter.this.itsMaxLocals * 3 + 7 + ClassFileWriter.this.itsMaxStack * 3);
        }
        
        private void initializeTypeInfo(final int n, final int n2) {
            this.initializeTypeInfo(n, n2, this.locals, this.localsTop);
            this.initializeTypeInfo(n, n2, this.stack, this.stackTop);
        }
        
        private void initializeTypeInfo(final int n, final int n2, final int[] array, final int n3) {
            for (int i = 0; i < n3; ++i) {
                if (array[i] == n) {
                    array[i] = n2;
                }
            }
        }
        
        private boolean isBranch(final int n) {
            switch (n) {
                default: {
                    switch (n) {
                        default: {
                            return false;
                        }
                        case 198:
                        case 199:
                        case 200: {
                            return true;
                        }
                    }
                    break;
                }
                case 153:
                case 154:
                case 155:
                case 156:
                case 157:
                case 158:
                case 159:
                case 160:
                case 161:
                case 162:
                case 163:
                case 164:
                case 165:
                case 166:
                case 167: {
                    return true;
                }
            }
        }
        
        private boolean isSuperBlockEnd(final int n) {
            Label_0082: {
                if (n != 167 && n != 191 && n != 200) {
                    switch (n) {
                        default: {
                            switch (n) {
                                default: {
                                    return false;
                                }
                                case 176:
                                case 177: {
                                    break Label_0082;
                                }
                            }
                            break;
                        }
                        case 170:
                        case 171:
                        case 172:
                        case 173:
                        case 174: {
                            break;
                        }
                    }
                }
            }
            return true;
        }
        
        private void killSuperBlock(final SuperBlock superBlock) {
            final int[] array = new int[0];
            final int[] array2 = { TypeInfo.OBJECT("java/lang/Throwable", ClassFileWriter.this.itsConstantPool) };
            int n = 0;
            int[] locals;
            while (true) {
                locals = array;
                if (n >= ClassFileWriter.this.itsExceptionTableTop) {
                    break;
                }
                final ExceptionTableEntry exceptionTableEntry = ClassFileWriter.this.itsExceptionTable[n];
                final int labelPC = ClassFileWriter.this.getLabelPC(exceptionTableEntry.itsStartLabel);
                final int labelPC2 = ClassFileWriter.this.getLabelPC(exceptionTableEntry.itsEndLabel);
                final SuperBlock superBlockFromOffset = this.getSuperBlockFromOffset(ClassFileWriter.this.getLabelPC(exceptionTableEntry.itsHandlerLabel));
                if ((superBlock.getStart() > labelPC && superBlock.getStart() < labelPC2) || (labelPC > superBlock.getStart() && labelPC < superBlock.getEnd() && superBlockFromOffset.isInitialized())) {
                    locals = superBlockFromOffset.getLocals();
                    break;
                }
                ++n;
            }
            int n2;
            for (int i = 0; i < ClassFileWriter.this.itsExceptionTableTop; i = n2 + 1) {
                final ExceptionTableEntry exceptionTableEntry2 = ClassFileWriter.this.itsExceptionTable[i];
                n2 = i;
                if (ClassFileWriter.this.getLabelPC(exceptionTableEntry2.itsStartLabel) == superBlock.getStart()) {
                    for (int j = i + 1; j < ClassFileWriter.this.itsExceptionTableTop; ++j) {
                        ClassFileWriter.this.itsExceptionTable[j - 1] = ClassFileWriter.this.itsExceptionTable[j];
                    }
                    ClassFileWriter.this.itsExceptionTableTop--;
                    n2 = i - 1;
                }
            }
            superBlock.merge(locals, locals.length, array2, array2.length, ClassFileWriter.this.itsConstantPool);
            final int n3 = superBlock.getEnd() - 1;
            ClassFileWriter.this.itsCodeBuffer[n3] = -65;
            for (int k = superBlock.getStart(); k < n3; ++k) {
                ClassFileWriter.this.itsCodeBuffer[k] = 0;
            }
        }
        
        private int pop() {
            final int[] stack = this.stack;
            final int stackTop = this.stackTop - 1;
            this.stackTop = stackTop;
            return stack[stackTop];
        }
        
        private long pop2() {
            final long n = this.pop();
            if (TypeInfo.isTwoWords((int)n)) {
                return n;
            }
            return n << 32 | (long)(this.pop() & 0xFFFFFF);
        }
        
        private void push(final int n) {
            if (this.stackTop == this.stack.length) {
                final int[] stack = new int[Math.max(this.stackTop * 2, 4)];
                System.arraycopy(this.stack, 0, stack, 0, this.stackTop);
                this.stack = stack;
            }
            this.stack[this.stackTop++] = n;
        }
        
        private void push2(long n) {
            this.push((int)(n & 0xFFFFFFL));
            n >>>= 32;
            if (n != 0L) {
                this.push((int)(n & 0xFFFFFFL));
            }
        }
        
        private void setLocal(final int n, final int n2) {
            if (n >= this.localsTop) {
                final int[] locals = new int[n + 1];
                System.arraycopy(this.locals, 0, locals, 0, this.localsTop);
                this.locals = locals;
                this.localsTop = n + 1;
            }
            this.locals[n] = n2;
        }
        
        private void verify() {
            final int[] access$100 = ClassFileWriter.this.createInitialLocals();
            final SuperBlock[] superBlocks = this.superBlocks;
            int i = 0;
            superBlocks[0].merge(access$100, access$100.length, new int[0], 0, ClassFileWriter.this.itsConstantPool);
            this.workList = new SuperBlock[] { this.superBlocks[0] };
            this.workListTop = 1;
            this.executeWorkList();
            while (i < this.superBlocks.length) {
                final SuperBlock superBlock = this.superBlocks[i];
                if (!superBlock.isInitialized()) {
                    this.killSuperBlock(superBlock);
                }
                ++i;
            }
            this.executeWorkList();
        }
        
        private void writeAppendFrame(final int[] array, final int n, final int n2) {
            final int length = array.length;
            this.rawStackMap[this.rawStackMapTop++] = (byte)(n + 251);
            this.rawStackMapTop = ClassFileWriter.putInt16(n2, this.rawStackMap, this.rawStackMapTop);
            this.rawStackMapTop = this.writeTypes(array, length - n);
        }
        
        private void writeChopFrame(final int n, final int n2) {
            this.rawStackMap[this.rawStackMapTop++] = (byte)(251 - n);
            this.rawStackMapTop = ClassFileWriter.putInt16(n2, this.rawStackMap, this.rawStackMapTop);
        }
        
        private void writeFullFrame(final int[] array, final int[] array2, final int n) {
            this.rawStackMap[this.rawStackMapTop++] = -1;
            this.rawStackMapTop = ClassFileWriter.putInt16(n, this.rawStackMap, this.rawStackMapTop);
            this.rawStackMapTop = ClassFileWriter.putInt16(array.length, this.rawStackMap, this.rawStackMapTop);
            this.rawStackMapTop = this.writeTypes(array);
            this.rawStackMapTop = ClassFileWriter.putInt16(array2.length, this.rawStackMap, this.rawStackMapTop);
            this.rawStackMapTop = this.writeTypes(array2);
        }
        
        private void writeSameFrame(final int[] array, final int n) {
            if (n <= 63) {
                this.rawStackMap[this.rawStackMapTop++] = (byte)n;
                return;
            }
            this.rawStackMap[this.rawStackMapTop++] = -5;
            this.rawStackMapTop = ClassFileWriter.putInt16(n, this.rawStackMap, this.rawStackMapTop);
        }
        
        private void writeSameLocalsOneStackItemFrame(final int[] array, final int[] array2, final int n) {
            if (n <= 63) {
                this.rawStackMap[this.rawStackMapTop++] = (byte)(n + 64);
            }
            else {
                this.rawStackMap[this.rawStackMapTop++] = -9;
                this.rawStackMapTop = ClassFileWriter.putInt16(n, this.rawStackMap, this.rawStackMapTop);
            }
            this.writeType(array2[0]);
        }
        
        private int writeType(final int n) {
            final int n2 = n & 0xFF;
            this.rawStackMap[this.rawStackMapTop++] = (byte)n2;
            if (n2 == 7 || n2 == 8) {
                this.rawStackMapTop = ClassFileWriter.putInt16(n >>> 8, this.rawStackMap, this.rawStackMapTop);
            }
            return this.rawStackMapTop;
        }
        
        private int writeTypes(final int[] array) {
            return this.writeTypes(array, 0);
        }
        
        private int writeTypes(final int[] array, int i) {
            final int rawStackMapTop = this.rawStackMapTop;
            while (i < array.length) {
                this.rawStackMapTop = this.writeType(array[i]);
                ++i;
            }
            return this.rawStackMapTop;
        }
        
        int computeWriteSize() {
            this.rawStackMap = new byte[this.getWorstCaseWriteSize()];
            this.computeRawStackMap();
            return this.rawStackMapTop + 2;
        }
        
        void generate() {
            this.superBlocks = new SuperBlock[ClassFileWriter.this.itsSuperBlockStartsTop];
            final int[] access$100 = ClassFileWriter.this.createInitialLocals();
            for (int i = 0; i < ClassFileWriter.this.itsSuperBlockStartsTop; ++i) {
                final int n = ClassFileWriter.this.itsSuperBlockStarts[i];
                int access$101;
                if (i == ClassFileWriter.this.itsSuperBlockStartsTop - 1) {
                    access$101 = ClassFileWriter.this.itsCodeBufferTop;
                }
                else {
                    access$101 = ClassFileWriter.this.itsSuperBlockStarts[i + 1];
                }
                this.superBlocks[i] = new SuperBlock(i, n, access$101, access$100);
            }
            this.superBlockDeps = this.getSuperBlockDependencies();
            this.verify();
        }
        
        int write(final byte[] array, int n) {
            n = ClassFileWriter.putInt32(this.rawStackMapTop + 2, array, n);
            n = ClassFileWriter.putInt16(this.superBlocks.length - 1, array, n);
            System.arraycopy(this.rawStackMap, 0, array, n, this.rawStackMapTop);
            return this.rawStackMapTop + n;
        }
    }
}
