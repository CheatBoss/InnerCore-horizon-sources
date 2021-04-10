package com.android.multidex;

import com.android.dx.cf.attrib.*;
import java.util.*;
import com.android.dx.cf.direct.*;
import com.android.dx.cf.iface.*;
import java.io.*;

public class MainDexListBuilder
{
    private static final String CLASS_EXTENSION = ".class";
    private static final String DISABLE_ANNOTATION_RESOLUTION_WORKAROUND = "--disable-annotation-resolution-workaround";
    private static final String EOL;
    private static final int STATUS_ERROR = 1;
    private static String USAGE_MESSAGE;
    private Set<String> filesToKeep;
    
    static {
        EOL = System.getProperty("line.separator");
        final StringBuilder sb = new StringBuilder();
        sb.append("Usage:");
        sb.append(MainDexListBuilder.EOL);
        sb.append(MainDexListBuilder.EOL);
        sb.append("Short version: Don't use this.");
        sb.append(MainDexListBuilder.EOL);
        sb.append(MainDexListBuilder.EOL);
        sb.append("Slightly longer version: This tool is used by mainDexClasses script to build");
        sb.append(MainDexListBuilder.EOL);
        sb.append("the main dex list.");
        sb.append(MainDexListBuilder.EOL);
        MainDexListBuilder.USAGE_MESSAGE = sb.toString();
    }
    
    public MainDexListBuilder(final boolean p0, final String p1, final String p2) throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokespecial   java/lang/Object.<init>:()V
        //     4: aload_0        
        //     5: new             Ljava/util/HashSet;
        //     8: dup            
        //     9: invokespecial   java/util/HashSet.<init>:()V
        //    12: putfield        com/android/multidex/MainDexListBuilder.filesToKeep:Ljava/util/Set;
        //    15: aconst_null    
        //    16: astore          7
        //    18: aconst_null    
        //    19: astore          6
        //    21: aload           7
        //    23: astore          5
        //    25: aload           6
        //    27: astore          4
        //    29: new             Ljava/util/zip/ZipFile;
        //    32: dup            
        //    33: aload_2        
        //    34: invokespecial   java/util/zip/ZipFile.<init>:(Ljava/lang/String;)V
        //    37: astore          8
        //    39: aload           8
        //    41: astore_2       
        //    42: aload_2        
        //    43: astore          5
        //    45: aload           6
        //    47: astore          4
        //    49: new             Lcom/android/multidex/Path;
        //    52: dup            
        //    53: aload_3        
        //    54: invokespecial   com/android/multidex/Path.<init>:(Ljava/lang/String;)V
        //    57: astore_3       
        //    58: aload_2        
        //    59: astore          5
        //    61: aload_3        
        //    62: astore          4
        //    64: new             Lcom/android/multidex/ClassReferenceListBuilder;
        //    67: dup            
        //    68: aload_3        
        //    69: invokespecial   com/android/multidex/ClassReferenceListBuilder.<init>:(Lcom/android/multidex/Path;)V
        //    72: astore          6
        //    74: aload_2        
        //    75: astore          5
        //    77: aload_3        
        //    78: astore          4
        //    80: aload           6
        //    82: aload_2        
        //    83: invokevirtual   com/android/multidex/ClassReferenceListBuilder.addRoots:(Ljava/util/zip/ZipFile;)V
        //    86: aload_2        
        //    87: astore          5
        //    89: aload_3        
        //    90: astore          4
        //    92: aload           6
        //    94: invokevirtual   com/android/multidex/ClassReferenceListBuilder.getClassNames:()Ljava/util/Set;
        //    97: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
        //   102: astore          6
        //   104: aload_2        
        //   105: astore          5
        //   107: aload_3        
        //   108: astore          4
        //   110: aload           6
        //   112: invokeinterface java/util/Iterator.hasNext:()Z
        //   117: ifeq            215
        //   120: aload_2        
        //   121: astore          5
        //   123: aload_3        
        //   124: astore          4
        //   126: aload           6
        //   128: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   133: checkcast       Ljava/lang/String;
        //   136: astore          7
        //   138: aload_2        
        //   139: astore          5
        //   141: aload_3        
        //   142: astore          4
        //   144: aload_0        
        //   145: getfield        com/android/multidex/MainDexListBuilder.filesToKeep:Ljava/util/Set;
        //   148: astore          8
        //   150: aload_2        
        //   151: astore          5
        //   153: aload_3        
        //   154: astore          4
        //   156: new             Ljava/lang/StringBuilder;
        //   159: dup            
        //   160: invokespecial   java/lang/StringBuilder.<init>:()V
        //   163: astore          9
        //   165: aload_2        
        //   166: astore          5
        //   168: aload_3        
        //   169: astore          4
        //   171: aload           9
        //   173: aload           7
        //   175: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   178: pop            
        //   179: aload_2        
        //   180: astore          5
        //   182: aload_3        
        //   183: astore          4
        //   185: aload           9
        //   187: ldc             ".class"
        //   189: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   192: pop            
        //   193: aload_2        
        //   194: astore          5
        //   196: aload_3        
        //   197: astore          4
        //   199: aload           8
        //   201: aload           9
        //   203: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   206: invokeinterface java/util/Set.add:(Ljava/lang/Object;)Z
        //   211: pop            
        //   212: goto            104
        //   215: iload_1        
        //   216: ifeq            230
        //   219: aload_2        
        //   220: astore          5
        //   222: aload_3        
        //   223: astore          4
        //   225: aload_0        
        //   226: aload_3        
        //   227: invokespecial   com/android/multidex/MainDexListBuilder.keepAnnotated:(Lcom/android/multidex/Path;)V
        //   230: aload_2        
        //   231: invokevirtual   java/util/zip/ZipFile.close:()V
        //   234: goto            238
        //   237: astore_2       
        //   238: aload_3        
        //   239: ifnull          284
        //   242: aload_3        
        //   243: getfield        com/android/multidex/Path.elements:Ljava/util/List;
        //   246: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
        //   251: astore_2       
        //   252: aload_2        
        //   253: invokeinterface java/util/Iterator.hasNext:()Z
        //   258: ifeq            284
        //   261: aload_2        
        //   262: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   267: checkcast       Lcom/android/multidex/ClassPathElement;
        //   270: astore_3       
        //   271: aload_3        
        //   272: invokeinterface com/android/multidex/ClassPathElement.close:()V
        //   277: goto            281
        //   280: astore_3       
        //   281: goto            252
        //   284: return         
        //   285: astore_2       
        //   286: goto            410
        //   289: astore_3       
        //   290: aload           7
        //   292: astore          5
        //   294: aload           6
        //   296: astore          4
        //   298: new             Ljava/lang/StringBuilder;
        //   301: dup            
        //   302: invokespecial   java/lang/StringBuilder.<init>:()V
        //   305: astore          8
        //   307: aload           7
        //   309: astore          5
        //   311: aload           6
        //   313: astore          4
        //   315: aload           8
        //   317: ldc             "\""
        //   319: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   322: pop            
        //   323: aload           7
        //   325: astore          5
        //   327: aload           6
        //   329: astore          4
        //   331: aload           8
        //   333: aload_2        
        //   334: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   337: pop            
        //   338: aload           7
        //   340: astore          5
        //   342: aload           6
        //   344: astore          4
        //   346: aload           8
        //   348: ldc             "\" can not be read as a zip archive. ("
        //   350: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   353: pop            
        //   354: aload           7
        //   356: astore          5
        //   358: aload           6
        //   360: astore          4
        //   362: aload           8
        //   364: aload_3        
        //   365: invokevirtual   java/io/IOException.getMessage:()Ljava/lang/String;
        //   368: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   371: pop            
        //   372: aload           7
        //   374: astore          5
        //   376: aload           6
        //   378: astore          4
        //   380: aload           8
        //   382: ldc             ")"
        //   384: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   387: pop            
        //   388: aload           7
        //   390: astore          5
        //   392: aload           6
        //   394: astore          4
        //   396: new             Ljava/io/IOException;
        //   399: dup            
        //   400: aload           8
        //   402: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   405: aload_3        
        //   406: invokespecial   java/io/IOException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   409: athrow         
        //   410: aload           5
        //   412: invokevirtual   java/util/zip/ZipFile.close:()V
        //   415: goto            419
        //   418: astore_3       
        //   419: aload           4
        //   421: ifnull          470
        //   424: aload           4
        //   426: getfield        com/android/multidex/Path.elements:Ljava/util/List;
        //   429: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
        //   434: astore_3       
        //   435: aload_3        
        //   436: invokeinterface java/util/Iterator.hasNext:()Z
        //   441: ifeq            470
        //   444: aload_3        
        //   445: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   450: checkcast       Lcom/android/multidex/ClassPathElement;
        //   453: astore          4
        //   455: aload           4
        //   457: invokeinterface com/android/multidex/ClassPathElement.close:()V
        //   462: goto            467
        //   465: astore          4
        //   467: goto            435
        //   470: aload_2        
        //   471: athrow         
        //    Exceptions:
        //  throws java.io.IOException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  29     39     289    410    Ljava/io/IOException;
        //  29     39     285    472    Any
        //  49     58     285    472    Any
        //  64     74     285    472    Any
        //  80     86     285    472    Any
        //  92     104    285    472    Any
        //  110    120    285    472    Any
        //  126    138    285    472    Any
        //  144    150    285    472    Any
        //  156    165    285    472    Any
        //  171    179    285    472    Any
        //  185    193    285    472    Any
        //  199    212    285    472    Any
        //  225    230    285    472    Any
        //  230    234    237    238    Ljava/io/IOException;
        //  271    277    280    281    Ljava/io/IOException;
        //  298    307    285    472    Any
        //  315    323    285    472    Any
        //  331    338    285    472    Any
        //  346    354    285    472    Any
        //  362    372    285    472    Any
        //  380    388    285    472    Any
        //  396    410    285    472    Any
        //  410    415    418    419    Ljava/io/IOException;
        //  455    462    465    467    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 230, Size: 230
        //     at java.util.ArrayList.rangeCheck(Unknown Source)
        //     at java.util.ArrayList.get(Unknown Source)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createConstructor(AstBuilder.java:713)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:549)
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
    
    private boolean hasRuntimeVisibleAnnotation(final HasAttribute hasAttribute) {
        final Attribute first = hasAttribute.getAttributes().findFirst("RuntimeVisibleAnnotations");
        return first != null && ((AttRuntimeVisibleAnnotations)first).getAnnotations().size() > 0;
    }
    
    private void keepAnnotated(final Path path) throws FileNotFoundException {
        final Iterator<ClassPathElement> iterator = path.getElements().iterator();
        while (iterator.hasNext()) {
        Label_0043:
            for (final String s : iterator.next().list()) {
                if (s.endsWith(".class")) {
                    final DirectClassFile class1 = path.getClass(s);
                    if (this.hasRuntimeVisibleAnnotation(class1)) {
                        this.filesToKeep.add(s);
                    }
                    else {
                        final MethodList methods = class1.getMethods();
                        final int n = 0;
                        for (int i = 0; i < methods.size(); ++i) {
                            if (this.hasRuntimeVisibleAnnotation(methods.get(i))) {
                                this.filesToKeep.add(s);
                                continue Label_0043;
                            }
                        }
                        final FieldList fields = class1.getFields();
                        for (int j = n; j < fields.size(); ++j) {
                            if (this.hasRuntimeVisibleAnnotation(fields.get(j))) {
                                this.filesToKeep.add(s);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
    
    public static void main(final String[] array) {
        int i = 0;
        boolean b = true;
        while (i < array.length - 2) {
            if (array[i].equals("--disable-annotation-resolution-workaround")) {
                b = false;
            }
            else {
                final PrintStream err = System.err;
                final StringBuilder sb = new StringBuilder();
                sb.append("Invalid option ");
                sb.append(array[i]);
                err.println(sb.toString());
                printUsage();
                System.exit(1);
            }
            ++i;
        }
        if (array.length - i != 2) {
            printUsage();
            System.exit(1);
        }
        try {
            printList(new MainDexListBuilder(b, array[i], array[i + 1]).getMainDexList());
        }
        catch (IOException ex) {
            final PrintStream err2 = System.err;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("A fatal error occured: ");
            sb2.append(ex.getMessage());
            err2.println(sb2.toString());
            System.exit(1);
        }
    }
    
    private static void printList(final Set<String> set) {
        final Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
    
    private static void printUsage() {
        System.err.print(MainDexListBuilder.USAGE_MESSAGE);
    }
    
    public Set<String> getMainDexList() {
        return this.filesToKeep;
    }
}
