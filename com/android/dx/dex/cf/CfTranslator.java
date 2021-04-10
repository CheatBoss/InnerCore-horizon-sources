package com.android.dx.dex.cf;

import com.android.dx.rop.type.*;
import com.android.dx.cf.direct.*;
import com.android.dex.util.*;
import com.android.dx.cf.iface.*;
import com.android.dx.rop.annotation.*;
import com.android.dx.dex.*;
import com.android.dx.dex.file.*;
import com.android.dx.rop.cst.*;
import com.android.dx.rop.code.*;
import com.android.dx.dex.code.*;

public class CfTranslator
{
    private static final boolean DEBUG = false;
    
    private CfTranslator() {
    }
    
    private static TypedConstant coerceConstant(final TypedConstant typedConstant, final Type type) {
        if (typedConstant.getType().equals(type)) {
            return typedConstant;
        }
        final int basicType = type.getBasicType();
        if (basicType == 8) {
            return CstShort.make(((CstInteger)typedConstant).getValue());
        }
        switch (basicType) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("can't coerce ");
                sb.append(typedConstant);
                sb.append(" to ");
                sb.append(type);
                throw new UnsupportedOperationException(sb.toString());
            }
            case 3: {
                return CstChar.make(((CstInteger)typedConstant).getValue());
            }
            case 2: {
                return CstByte.make(((CstInteger)typedConstant).getValue());
            }
            case 1: {
                return CstBoolean.make(((CstInteger)typedConstant).getValue());
            }
        }
    }
    
    private static void processFields(final DirectClassFile directClassFile, final ClassDefItem classDefItem, final DexFile dexFile) {
        final CstType thisClass = directClassFile.getThisClass();
        final FieldList fields = directClassFile.getFields();
        final int size = fields.size();
        int i = 0;
        while (i < size) {
            final Field value = fields.get(i);
            try {
                final CstFieldRef cstFieldRef = new CstFieldRef(thisClass, value.getNat());
                final int accessFlags = value.getAccessFlags();
                if (AccessFlags.isStatic(accessFlags)) {
                    final TypedConstant constantValue = value.getConstantValue();
                    final EncodedField encodedField = new EncodedField(cstFieldRef, accessFlags);
                    TypedConstant coerceConstant;
                    if ((coerceConstant = constantValue) != null) {
                        coerceConstant = coerceConstant(constantValue, cstFieldRef.getType());
                    }
                    classDefItem.addStaticField(encodedField, coerceConstant);
                }
                else {
                    classDefItem.addInstanceField(new EncodedField(cstFieldRef, accessFlags));
                }
                final Annotations annotations = AttributeTranslator.getAnnotations(value.getAttributes());
                if (annotations.size() != 0) {
                    classDefItem.addFieldAnnotations(cstFieldRef, annotations, dexFile);
                }
                dexFile.getFieldIds().intern(cstFieldRef);
                ++i;
                continue;
            }
            catch (RuntimeException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("...while processing ");
                sb.append(value.getName().toHuman());
                sb.append(" ");
                sb.append(value.getDescriptor().toHuman());
                throw ExceptionWithContext.withContext(ex, sb.toString());
            }
            break;
        }
    }
    
    private static void processMethods(final DirectClassFile p0, final CfOptions p1, final DexOptions p2, final ClassDefItem p3, final DexFile p4) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   com/android/dx/cf/direct/DirectClassFile.getThisClass:()Lcom/android/dx/rop/cst/CstType;
        //     4: astore          19
        //     6: aload_0        
        //     7: invokevirtual   com/android/dx/cf/direct/DirectClassFile.getMethods:()Lcom/android/dx/cf/iface/MethodList;
        //    10: astore          18
        //    12: aload           18
        //    14: invokeinterface com/android/dx/cf/iface/MethodList.size:()I
        //    19: istore          9
        //    21: iconst_0       
        //    22: istore          6
        //    24: aload_1        
        //    25: astore          24
        //    27: iload           6
        //    29: iload           9
        //    31: if_icmpge       685
        //    34: aload           18
        //    36: iload           6
        //    38: invokeinterface com/android/dx/cf/iface/MethodList.get:(I)Lcom/android/dx/cf/iface/Method;
        //    43: astore          17
        //    45: new             Lcom/android/dx/rop/cst/CstMethodRef;
        //    48: dup            
        //    49: aload           19
        //    51: aload           17
        //    53: invokeinterface com/android/dx/cf/iface/Method.getNat:()Lcom/android/dx/rop/cst/CstNat;
        //    58: invokespecial   com/android/dx/rop/cst/CstMethodRef.<init>:(Lcom/android/dx/rop/cst/CstType;Lcom/android/dx/rop/cst/CstNat;)V
        //    61: astore          23
        //    63: aload           17
        //    65: invokeinterface com/android/dx/cf/iface/Method.getAccessFlags:()I
        //    70: istore          5
        //    72: iload           5
        //    74: invokestatic    com/android/dx/rop/code/AccessFlags.isStatic:(I)Z
        //    77: istore          12
        //    79: iload           5
        //    81: invokestatic    com/android/dx/rop/code/AccessFlags.isPrivate:(I)Z
        //    84: istore          13
        //    86: iload           5
        //    88: invokestatic    com/android/dx/rop/code/AccessFlags.isNative:(I)Z
        //    91: istore          14
        //    93: iload           5
        //    95: invokestatic    com/android/dx/rop/code/AccessFlags.isAbstract:(I)Z
        //    98: istore          15
        //   100: aload           23
        //   102: invokevirtual   com/android/dx/rop/cst/CstMethodRef.isInstanceInit:()Z
        //   105: istore          16
        //   107: iconst_1       
        //   108: istore          11
        //   110: iload           16
        //   112: ifne            140
        //   115: aload           23
        //   117: invokevirtual   com/android/dx/rop/cst/CstMethodRef.isClassInit:()Z
        //   120: istore          16
        //   122: iload           16
        //   124: ifeq            130
        //   127: goto            140
        //   130: iconst_0       
        //   131: istore          7
        //   133: goto            143
        //   136: astore_0       
        //   137: goto            624
        //   140: iconst_1       
        //   141: istore          7
        //   143: iload           14
        //   145: ifne            711
        //   148: iload           15
        //   150: ifeq            156
        //   153: goto            711
        //   156: aload           24
        //   158: getfield        com/android/dx/dex/cf/CfOptions.positionInfo:I
        //   161: iconst_1       
        //   162: if_icmpeq       686
        //   165: goto            168
        //   168: aload           24
        //   170: getfield        com/android/dx/dex/cf/CfOptions.localInfo:Z
        //   173: istore          15
        //   175: new             Lcom/android/dx/cf/code/ConcreteMethod;
        //   178: dup            
        //   179: aload           17
        //   181: aload_0        
        //   182: iload           11
        //   184: iload           15
        //   186: invokespecial   com/android/dx/cf/code/ConcreteMethod.<init>:(Lcom/android/dx/cf/iface/Method;Lcom/android/dx/cf/iface/ClassFile;ZZ)V
        //   189: astore          26
        //   191: getstatic       com/android/dx/rop/code/DexTranslationAdvice.THE_ONE:Lcom/android/dx/rop/code/DexTranslationAdvice;
        //   194: astore          22
        //   196: aload           26
        //   198: aload           22
        //   200: aload           18
        //   202: invokestatic    com/android/dx/cf/code/Ropper.convert:(Lcom/android/dx/cf/code/ConcreteMethod;Lcom/android/dx/rop/code/TranslationAdvice;Lcom/android/dx/cf/iface/MethodList;)Lcom/android/dx/rop/code/RopMethod;
        //   205: astore          20
        //   207: aconst_null    
        //   208: astore          21
        //   210: aload           23
        //   212: iload           12
        //   214: invokevirtual   com/android/dx/rop/cst/CstMethodRef.getParameterWordCount:(Z)I
        //   217: istore          8
        //   219: new             Ljava/lang/StringBuilder;
        //   222: dup            
        //   223: invokespecial   java/lang/StringBuilder.<init>:()V
        //   226: astore          25
        //   228: aload           25
        //   230: aload           19
        //   232: invokevirtual   com/android/dx/rop/cst/CstType.getClassType:()Lcom/android/dx/rop/type/Type;
        //   235: invokevirtual   com/android/dx/rop/type/Type.getDescriptor:()Ljava/lang/String;
        //   238: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   241: pop            
        //   242: aload           25
        //   244: ldc_w           "."
        //   247: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   250: pop            
        //   251: aload           25
        //   253: aload           17
        //   255: invokeinterface com/android/dx/cf/iface/Method.getName:()Lcom/android/dx/rop/cst/CstString;
        //   260: invokevirtual   com/android/dx/rop/cst/CstString.getString:()Ljava/lang/String;
        //   263: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   266: pop            
        //   267: aload           25
        //   269: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   272: astore          25
        //   274: aload           24
        //   276: getfield        com/android/dx/dex/cf/CfOptions.optimize:Z
        //   279: istore          11
        //   281: iload           11
        //   283: ifeq            350
        //   286: aload           25
        //   288: invokestatic    com/android/dx/dex/cf/OptimizerOptions.shouldOptimize:(Ljava/lang/String;)Z
        //   291: ifeq            350
        //   294: aload           24
        //   296: getfield        com/android/dx/dex/cf/CfOptions.localInfo:Z
        //   299: istore          11
        //   301: aload           20
        //   303: iload           8
        //   305: iload           12
        //   307: iload           11
        //   309: aload           22
        //   311: invokestatic    com/android/dx/ssa/Optimizer.optimize:(Lcom/android/dx/rop/code/RopMethod;IZZLcom/android/dx/rop/code/TranslationAdvice;)Lcom/android/dx/rop/code/RopMethod;
        //   314: astore          21
        //   316: aload           24
        //   318: getfield        com/android/dx/dex/cf/CfOptions.statistics:Z
        //   321: ifeq            331
        //   324: aload           20
        //   326: aload           21
        //   328: invokestatic    com/android/dx/dex/cf/CodeStatistics.updateRopStatistics:(Lcom/android/dx/rop/code/RopMethod;Lcom/android/dx/rop/code/RopMethod;)V
        //   331: aload           20
        //   333: astore          22
        //   335: aload           21
        //   337: astore          20
        //   339: aload           22
        //   341: astore          21
        //   343: goto            350
        //   346: astore_0       
        //   347: goto            624
        //   350: aload           24
        //   352: getfield        com/android/dx/dex/cf/CfOptions.localInfo:Z
        //   355: istore          11
        //   357: iload           11
        //   359: ifeq            376
        //   362: aload           20
        //   364: invokestatic    com/android/dx/rop/code/LocalVariableExtractor.extract:(Lcom/android/dx/rop/code/RopMethod;)Lcom/android/dx/rop/code/LocalVariableInfo;
        //   367: astore          22
        //   369: goto            379
        //   372: astore_0       
        //   373: goto            624
        //   376: aconst_null    
        //   377: astore          22
        //   379: aload           24
        //   381: getfield        com/android/dx/dex/cf/CfOptions.positionInfo:I
        //   384: istore          10
        //   386: aload           20
        //   388: iload           10
        //   390: aload           22
        //   392: iload           8
        //   394: aload_2        
        //   395: invokestatic    com/android/dx/dex/code/RopTranslator.translate:(Lcom/android/dx/rop/code/RopMethod;ILcom/android/dx/rop/code/LocalVariableInfo;ILcom/android/dx/dex/DexOptions;)Lcom/android/dx/dex/code/DalvCode;
        //   398: astore          25
        //   400: aload           24
        //   402: getfield        com/android/dx/dex/cf/CfOptions.statistics:Z
        //   405: ifeq            692
        //   408: aload           21
        //   410: ifnull          692
        //   413: aload           26
        //   415: invokevirtual   com/android/dx/cf/code/ConcreteMethod.getCode:()Lcom/android/dx/cf/code/BytecodeArray;
        //   418: invokevirtual   com/android/dx/cf/code/BytecodeArray.size:()I
        //   421: istore          10
        //   423: aload           24
        //   425: aload_2        
        //   426: aload           20
        //   428: aload           21
        //   430: aload           22
        //   432: iload           8
        //   434: iload           10
        //   436: invokestatic    com/android/dx/dex/cf/CfTranslator.updateDexStatistics:(Lcom/android/dx/dex/cf/CfOptions;Lcom/android/dx/dex/DexOptions;Lcom/android/dx/rop/code/RopMethod;Lcom/android/dx/rop/code/RopMethod;Lcom/android/dx/rop/code/LocalVariableInfo;II)V
        //   439: aload           25
        //   441: astore          20
        //   443: goto            446
        //   446: aload           17
        //   448: astore          21
        //   450: iload           5
        //   452: invokestatic    com/android/dx/rop/code/AccessFlags.isSynchronized:(I)Z
        //   455: ifeq            717
        //   458: iload           5
        //   460: ldc_w           131072
        //   463: ior            
        //   464: istore          8
        //   466: iload           8
        //   468: istore          5
        //   470: iload           14
        //   472: ifne            717
        //   475: iload           8
        //   477: bipush          -33
        //   479: iand           
        //   480: istore          5
        //   482: goto            717
        //   485: new             Lcom/android/dx/dex/file/EncodedMethod;
        //   488: dup            
        //   489: aload           23
        //   491: iload           8
        //   493: aload           20
        //   495: aload           21
        //   497: invokestatic    com/android/dx/dex/cf/AttributeTranslator.getExceptions:(Lcom/android/dx/cf/iface/Method;)Lcom/android/dx/rop/type/TypeList;
        //   500: invokespecial   com/android/dx/dex/file/EncodedMethod.<init>:(Lcom/android/dx/rop/cst/CstMethodRef;ILcom/android/dx/dex/code/DalvCode;Lcom/android/dx/rop/type/TypeList;)V
        //   503: astore          20
        //   505: aload           23
        //   507: invokevirtual   com/android/dx/rop/cst/CstMethodRef.isInstanceInit:()Z
        //   510: ifne            543
        //   513: aload           23
        //   515: invokevirtual   com/android/dx/rop/cst/CstMethodRef.isClassInit:()Z
        //   518: ifne            543
        //   521: iload           12
        //   523: ifne            543
        //   526: iload           13
        //   528: ifeq            534
        //   531: goto            543
        //   534: aload_3        
        //   535: aload           20
        //   537: invokevirtual   com/android/dx/dex/file/ClassDefItem.addVirtualMethod:(Lcom/android/dx/dex/file/EncodedMethod;)V
        //   540: goto            549
        //   543: aload_3        
        //   544: aload           20
        //   546: invokevirtual   com/android/dx/dex/file/ClassDefItem.addDirectMethod:(Lcom/android/dx/dex/file/EncodedMethod;)V
        //   549: aload           21
        //   551: invokestatic    com/android/dx/dex/cf/AttributeTranslator.getMethodAnnotations:(Lcom/android/dx/cf/iface/Method;)Lcom/android/dx/rop/annotation/Annotations;
        //   554: astore          20
        //   556: aload           20
        //   558: invokevirtual   com/android/dx/rop/annotation/Annotations.size:()I
        //   561: ifeq            574
        //   564: aload_3        
        //   565: aload           23
        //   567: aload           20
        //   569: aload           4
        //   571: invokevirtual   com/android/dx/dex/file/ClassDefItem.addMethodAnnotations:(Lcom/android/dx/rop/cst/CstMethodRef;Lcom/android/dx/rop/annotation/Annotations;Lcom/android/dx/dex/file/DexFile;)V
        //   574: aload           21
        //   576: invokestatic    com/android/dx/dex/cf/AttributeTranslator.getParameterAnnotations:(Lcom/android/dx/cf/iface/Method;)Lcom/android/dx/rop/annotation/AnnotationsList;
        //   579: astore          20
        //   581: aload           20
        //   583: invokevirtual   com/android/dx/rop/annotation/AnnotationsList.size:()I
        //   586: ifeq            599
        //   589: aload_3        
        //   590: aload           23
        //   592: aload           20
        //   594: aload           4
        //   596: invokevirtual   com/android/dx/dex/file/ClassDefItem.addParameterAnnotations:(Lcom/android/dx/rop/cst/CstMethodRef;Lcom/android/dx/rop/annotation/AnnotationsList;Lcom/android/dx/dex/file/DexFile;)V
        //   599: aload           4
        //   601: invokevirtual   com/android/dx/dex/file/DexFile.getMethodIds:()Lcom/android/dx/dex/file/MethodIdsSection;
        //   604: aload           23
        //   606: invokevirtual   com/android/dx/dex/file/MethodIdsSection.intern:(Lcom/android/dx/rop/cst/CstBaseMethodRef;)Lcom/android/dx/dex/file/MethodIdItem;
        //   609: pop            
        //   610: iload           6
        //   612: iconst_1       
        //   613: iadd           
        //   614: istore          6
        //   616: goto            24
        //   619: astore_0       
        //   620: goto            624
        //   623: astore_0       
        //   624: new             Ljava/lang/StringBuilder;
        //   627: dup            
        //   628: invokespecial   java/lang/StringBuilder.<init>:()V
        //   631: astore_1       
        //   632: aload_1        
        //   633: ldc             "...while processing "
        //   635: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   638: pop            
        //   639: aload_1        
        //   640: aload           17
        //   642: invokeinterface com/android/dx/cf/iface/Method.getName:()Lcom/android/dx/rop/cst/CstString;
        //   647: invokevirtual   com/android/dx/rop/cst/CstString.toHuman:()Ljava/lang/String;
        //   650: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   653: pop            
        //   654: aload_1        
        //   655: ldc             " "
        //   657: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   660: pop            
        //   661: aload_1        
        //   662: aload           17
        //   664: invokeinterface com/android/dx/cf/iface/Method.getDescriptor:()Lcom/android/dx/rop/cst/CstString;
        //   669: invokevirtual   com/android/dx/rop/cst/CstString.toHuman:()Ljava/lang/String;
        //   672: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   675: pop            
        //   676: aload_0        
        //   677: aload_1        
        //   678: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   681: invokestatic    com/android/dex/util/ExceptionWithContext.withContext:(Ljava/lang/Throwable;Ljava/lang/String;)Lcom/android/dex/util/ExceptionWithContext;
        //   684: athrow         
        //   685: return         
        //   686: iconst_0       
        //   687: istore          11
        //   689: goto            168
        //   692: aload           25
        //   694: astore          20
        //   696: goto            446
        //   699: astore_0       
        //   700: goto            624
        //   703: astore_0       
        //   704: goto            624
        //   707: astore_0       
        //   708: goto            624
        //   711: aconst_null    
        //   712: astore          20
        //   714: goto            446
        //   717: iload           5
        //   719: istore          8
        //   721: iload           7
        //   723: ifeq            485
        //   726: iload           5
        //   728: ldc_w           65536
        //   731: ior            
        //   732: istore          8
        //   734: goto            485
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                        
        //  -----  -----  -----  -----  ----------------------------
        //  45     107    623    624    Ljava/lang/RuntimeException;
        //  115    122    136    140    Ljava/lang/RuntimeException;
        //  156    165    623    624    Ljava/lang/RuntimeException;
        //  168    175    623    624    Ljava/lang/RuntimeException;
        //  175    207    707    711    Ljava/lang/RuntimeException;
        //  210    281    707    711    Ljava/lang/RuntimeException;
        //  286    301    346    350    Ljava/lang/RuntimeException;
        //  301    331    372    376    Ljava/lang/RuntimeException;
        //  350    357    703    707    Ljava/lang/RuntimeException;
        //  362    369    372    376    Ljava/lang/RuntimeException;
        //  379    386    703    707    Ljava/lang/RuntimeException;
        //  386    408    699    703    Ljava/lang/RuntimeException;
        //  413    423    699    703    Ljava/lang/RuntimeException;
        //  423    439    619    623    Ljava/lang/RuntimeException;
        //  450    458    619    623    Ljava/lang/RuntimeException;
        //  485    521    619    623    Ljava/lang/RuntimeException;
        //  534    540    619    623    Ljava/lang/RuntimeException;
        //  543    549    619    623    Ljava/lang/RuntimeException;
        //  549    574    619    623    Ljava/lang/RuntimeException;
        //  574    599    619    623    Ljava/lang/RuntimeException;
        //  599    610    619    623    Ljava/lang/RuntimeException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0331:
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
    
    public static ClassDefItem translate(final DirectClassFile directClassFile, final byte[] array, final CfOptions cfOptions, final DexOptions dexOptions, final DexFile dexFile) {
        try {
            return translate0(directClassFile, array, cfOptions, dexOptions, dexFile);
        }
        catch (RuntimeException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("...while processing ");
            sb.append(directClassFile.getFilePath());
            throw ExceptionWithContext.withContext(ex, sb.toString());
        }
    }
    
    private static ClassDefItem translate0(final DirectClassFile directClassFile, final byte[] array, final CfOptions cfOptions, final DexOptions dexOptions, final DexFile dexFile) {
        OptimizerOptions.loadOptimizeLists(cfOptions.optimizeListFile, cfOptions.dontOptimizeListFile);
        final CstType thisClass = directClassFile.getThisClass();
        final int accessFlags = directClassFile.getAccessFlags();
        CstString sourceFile;
        if (cfOptions.positionInfo == 1) {
            sourceFile = null;
        }
        else {
            sourceFile = directClassFile.getSourceFile();
        }
        final ClassDefItem classDefItem = new ClassDefItem(thisClass, accessFlags & 0xFFFFFFDF, directClassFile.getSuperclass(), directClassFile.getInterfaces(), sourceFile);
        final Annotations classAnnotations = AttributeTranslator.getClassAnnotations(directClassFile, cfOptions);
        if (classAnnotations.size() != 0) {
            classDefItem.setClassAnnotations(classAnnotations, dexFile);
        }
        final FieldIdsSection fieldIds = dexFile.getFieldIds();
        final MethodIdsSection methodIds = dexFile.getMethodIds();
        processFields(directClassFile, classDefItem, dexFile);
        processMethods(directClassFile, cfOptions, dexOptions, classDefItem, dexFile);
        final ConstantPool constantPool = directClassFile.getConstantPool();
        for (int size = constantPool.size(), i = 0; i < size; ++i) {
            final Constant orNull = constantPool.getOrNull(i);
            if (orNull instanceof CstMethodRef) {
                methodIds.intern((CstBaseMethodRef)orNull);
            }
            else if (orNull instanceof CstInterfaceMethodRef) {
                methodIds.intern(((CstInterfaceMethodRef)orNull).toMethodRef());
            }
            else if (orNull instanceof CstFieldRef) {
                fieldIds.intern((CstFieldRef)orNull);
            }
            else if (orNull instanceof CstEnumRef) {
                fieldIds.intern(((CstEnumRef)orNull).getFieldRef());
            }
        }
        return classDefItem;
    }
    
    private static void updateDexStatistics(final CfOptions cfOptions, final DexOptions dexOptions, final RopMethod ropMethod, final RopMethod ropMethod2, final LocalVariableInfo localVariableInfo, final int n, final int n2) {
        final DalvCode translate = RopTranslator.translate(ropMethod, cfOptions.positionInfo, localVariableInfo, n, dexOptions);
        final DalvCode translate2 = RopTranslator.translate(ropMethod2, cfOptions.positionInfo, localVariableInfo, n, dexOptions);
        final DalvCode.AssignIndicesCallback assignIndicesCallback = new DalvCode.AssignIndicesCallback() {
            @Override
            public int getIndex(final Constant constant) {
                return 0;
            }
        };
        translate.assignIndices((DalvCode.AssignIndicesCallback)assignIndicesCallback);
        translate2.assignIndices((DalvCode.AssignIndicesCallback)assignIndicesCallback);
        CodeStatistics.updateDexStatistics(translate2, translate);
        CodeStatistics.updateOriginalByteCount(n2);
    }
}
