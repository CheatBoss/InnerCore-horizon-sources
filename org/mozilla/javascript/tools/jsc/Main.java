package org.mozilla.javascript.tools.jsc;

import org.mozilla.javascript.optimizer.*;
import org.mozilla.javascript.*;
import org.mozilla.javascript.tools.*;
import java.io.*;

public class Main
{
    private String characterEncoding;
    private ClassCompiler compiler;
    private CompilerEnvirons compilerEnv;
    private String destinationDir;
    private boolean printHelp;
    private ToolErrorReporter reporter;
    private String targetName;
    private String targetPackage;
    
    public Main() {
        this.reporter = new ToolErrorReporter(true);
        (this.compilerEnv = new CompilerEnvirons()).setErrorReporter(this.reporter);
        this.compiler = new ClassCompiler(this.compilerEnv);
    }
    
    private void addError(String s, final String s2) {
        if (s2 == null) {
            s = ToolErrorReporter.getMessage(s);
        }
        else {
            s = ToolErrorReporter.getMessage(s, s2);
        }
        this.addFormatedError(s);
    }
    
    private void addFormatedError(final String s) {
        this.reporter.error(s, null, -1, null, -1);
    }
    
    private static void badUsage(final String s) {
        System.err.println(ToolErrorReporter.getMessage("msg.jsc.bad.usage", Main.class.getName(), s));
    }
    
    private File getOutputFile(File file, String parent) {
        file = new File(file, parent.replace('.', File.separatorChar).concat(".class"));
        parent = file.getParent();
        if (parent != null) {
            final File file2 = new File(parent);
            if (!file2.exists()) {
                file2.mkdirs();
            }
        }
        return file;
    }
    
    public static void main(String[] processOptions) {
        final Main main = new Main();
        processOptions = main.processOptions(processOptions);
        if (processOptions == null) {
            if (main.printHelp) {
                System.out.println(ToolErrorReporter.getMessage("msg.jsc.usage", Main.class.getName()));
                System.exit(0);
            }
            System.exit(1);
        }
        if (!main.reporter.hasReportedError()) {
            main.processSource(processOptions);
        }
    }
    
    private static void p(final String s) {
        System.out.println(s);
    }
    
    private String readSource(final File file) {
        final String absolutePath = file.getAbsolutePath();
        if (!file.isFile()) {
            this.addError("msg.jsfile.not.found", absolutePath);
            return null;
        }
        try {
            return (String)SourceReader.readFileOrUrl(absolutePath, true, this.characterEncoding);
        }
        catch (IOException ex) {
            this.addFormatedError(ex.toString());
            return null;
        }
        catch (FileNotFoundException ex2) {
            this.addError("msg.couldnt.open", absolutePath);
            return null;
        }
    }
    
    String getClassName(final String s) {
        final char[] array = new char[s.length() + 1];
        int n = 0;
        int i;
        final int n2 = i = 0;
        if (!Character.isJavaIdentifierStart(s.charAt(0))) {
            array[0] = '_';
            n = 0 + 1;
            i = n2;
        }
        while (i < s.length()) {
            final char char1 = s.charAt(i);
            if (Character.isJavaIdentifierPart(char1)) {
                array[n] = char1;
            }
            else {
                array[n] = '_';
            }
            ++i;
            ++n;
        }
        return new String(array).trim();
    }
    
    public String[] processOptions(final String[] p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             ""
        //     3: putfield        org/mozilla/javascript/tools/jsc/Main.targetPackage:Ljava/lang/String;
        //     6: aload_0        
        //     7: getfield        org/mozilla/javascript/tools/jsc/Main.compilerEnv:Lorg/mozilla/javascript/CompilerEnvirons;
        //    10: astore          9
        //    12: iconst_0       
        //    13: istore          6
        //    15: aload           9
        //    17: iconst_0       
        //    18: invokevirtual   org/mozilla/javascript/CompilerEnvirons.setGenerateDebugInfo:(Z)V
        //    21: iconst_0       
        //    22: istore          4
        //    24: iload           4
        //    26: aload_1        
        //    27: arraylength    
        //    28: if_icmpge       1059
        //    31: aload_1        
        //    32: iload           4
        //    34: aaload         
        //    35: astore          9
        //    37: aload           9
        //    39: ldc             "-"
        //    41: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //    44: ifne            115
        //    47: aload_1        
        //    48: arraylength    
        //    49: iload           4
        //    51: isub           
        //    52: istore          5
        //    54: aload_0        
        //    55: getfield        org/mozilla/javascript/tools/jsc/Main.targetName:Ljava/lang/String;
        //    58: ifnull          79
        //    61: iload           5
        //    63: iconst_1       
        //    64: if_icmple       79
        //    67: aload_0        
        //    68: ldc             "msg.multiple.js.to.file"
        //    70: aload_0        
        //    71: getfield        org/mozilla/javascript/tools/jsc/Main.targetName:Ljava/lang/String;
        //    74: invokespecial   org/mozilla/javascript/tools/jsc/Main.addError:(Ljava/lang/String;Ljava/lang/String;)V
        //    77: aconst_null    
        //    78: areturn        
        //    79: iload           5
        //    81: anewarray       Ljava/lang/String;
        //    84: astore          9
        //    86: iload           6
        //    88: istore_3       
        //    89: iload_3        
        //    90: iload           5
        //    92: if_icmpeq       112
        //    95: aload           9
        //    97: iload_3        
        //    98: aload_1        
        //    99: iload           4
        //   101: iload_3        
        //   102: iadd           
        //   103: aaload         
        //   104: aastore        
        //   105: iload_3        
        //   106: iconst_1       
        //   107: iadd           
        //   108: istore_3       
        //   109: goto            89
        //   112: aload           9
        //   114: areturn        
        //   115: aload           9
        //   117: ldc             "-help"
        //   119: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   122: ifne            1052
        //   125: aload           9
        //   127: ldc             "-h"
        //   129: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   132: ifne            1052
        //   135: aload           9
        //   137: ldc             "--help"
        //   139: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   142: ifeq            148
        //   145: goto            1052
        //   148: iload           4
        //   150: istore          5
        //   152: iload           4
        //   154: istore_3       
        //   155: aload           9
        //   157: ldc             "-version"
        //   159: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   162: ifeq            215
        //   165: iload           4
        //   167: iconst_1       
        //   168: iadd           
        //   169: istore          4
        //   171: iload           4
        //   173: istore          5
        //   175: iload           4
        //   177: istore_3       
        //   178: iload           4
        //   180: aload_1        
        //   181: arraylength    
        //   182: if_icmpge       215
        //   185: iload           4
        //   187: istore_3       
        //   188: aload_1        
        //   189: iload           4
        //   191: aaload         
        //   192: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
        //   195: istore          5
        //   197: iload           4
        //   199: istore_3       
        //   200: aload_0        
        //   201: getfield        org/mozilla/javascript/tools/jsc/Main.compilerEnv:Lorg/mozilla/javascript/CompilerEnvirons;
        //   204: iload           5
        //   206: invokevirtual   org/mozilla/javascript/CompilerEnvirons.setLanguageVersion:(I)V
        //   209: iload           4
        //   211: istore_3       
        //   212: goto            1034
        //   215: iload           5
        //   217: istore_3       
        //   218: aload           9
        //   220: ldc             "-opt"
        //   222: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   225: ifne            245
        //   228: iload           5
        //   230: istore          4
        //   232: iload           5
        //   234: istore_3       
        //   235: aload           9
        //   237: ldc             "-O"
        //   239: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   242: ifeq            295
        //   245: iload           5
        //   247: iconst_1       
        //   248: iadd           
        //   249: istore          5
        //   251: iload           5
        //   253: istore          4
        //   255: iload           5
        //   257: istore_3       
        //   258: iload           5
        //   260: aload_1        
        //   261: arraylength    
        //   262: if_icmpge       295
        //   265: iload           5
        //   267: istore_3       
        //   268: aload_1        
        //   269: iload           5
        //   271: aaload         
        //   272: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
        //   275: istore          4
        //   277: iload           5
        //   279: istore_3       
        //   280: aload_0        
        //   281: getfield        org/mozilla/javascript/tools/jsc/Main.compilerEnv:Lorg/mozilla/javascript/CompilerEnvirons;
        //   284: iload           4
        //   286: invokevirtual   org/mozilla/javascript/CompilerEnvirons.setOptimizationLevel:(I)V
        //   289: iload           5
        //   291: istore_3       
        //   292: goto            1034
        //   295: aload           9
        //   297: ldc             "-nosource"
        //   299: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   302: ifeq            319
        //   305: aload_0        
        //   306: getfield        org/mozilla/javascript/tools/jsc/Main.compilerEnv:Lorg/mozilla/javascript/CompilerEnvirons;
        //   309: iconst_0       
        //   310: invokevirtual   org/mozilla/javascript/CompilerEnvirons.setGeneratingSource:(Z)V
        //   313: iload           4
        //   315: istore_3       
        //   316: goto            1034
        //   319: aload           9
        //   321: ldc             "-debug"
        //   323: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   326: ifne            1023
        //   329: aload           9
        //   331: ldc             "-g"
        //   333: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   336: ifeq            342
        //   339: goto            1023
        //   342: iload           4
        //   344: istore_3       
        //   345: aload           9
        //   347: ldc_w           "-main-method-class"
        //   350: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   353: ifeq            389
        //   356: iload           4
        //   358: iconst_1       
        //   359: iadd           
        //   360: istore          4
        //   362: iload           4
        //   364: istore_3       
        //   365: iload           4
        //   367: aload_1        
        //   368: arraylength    
        //   369: if_icmpge       389
        //   372: aload_0        
        //   373: getfield        org/mozilla/javascript/tools/jsc/Main.compiler:Lorg/mozilla/javascript/optimizer/ClassCompiler;
        //   376: aload_1        
        //   377: iload           4
        //   379: aaload         
        //   380: invokevirtual   org/mozilla/javascript/optimizer/ClassCompiler.setMainMethodClass:(Ljava/lang/String;)V
        //   383: iload           4
        //   385: istore_3       
        //   386: goto            1034
        //   389: iload_3        
        //   390: istore          4
        //   392: aload           9
        //   394: ldc_w           "-encoding"
        //   397: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   400: ifeq            426
        //   403: iload_3        
        //   404: iconst_1       
        //   405: iadd           
        //   406: istore_3       
        //   407: iload_3        
        //   408: istore          4
        //   410: iload_3        
        //   411: aload_1        
        //   412: arraylength    
        //   413: if_icmpge       426
        //   416: aload_0        
        //   417: aload_1        
        //   418: iload_3        
        //   419: aaload         
        //   420: putfield        org/mozilla/javascript/tools/jsc/Main.characterEncoding:Ljava/lang/String;
        //   423: goto            1034
        //   426: iload           4
        //   428: istore_3       
        //   429: aload           9
        //   431: ldc_w           "-o"
        //   434: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   437: ifeq            602
        //   440: iload           4
        //   442: iconst_1       
        //   443: iadd           
        //   444: istore          4
        //   446: iload           4
        //   448: istore_3       
        //   449: iload           4
        //   451: aload_1        
        //   452: arraylength    
        //   453: if_icmpge       602
        //   456: aload_1        
        //   457: iload           4
        //   459: aaload         
        //   460: astore          10
        //   462: aload           10
        //   464: invokevirtual   java/lang/String.length:()I
        //   467: istore          5
        //   469: iload           5
        //   471: ifeq            587
        //   474: aload           10
        //   476: iconst_0       
        //   477: invokevirtual   java/lang/String.charAt:(I)C
        //   480: invokestatic    java/lang/Character.isJavaIdentifierStart:(C)Z
        //   483: ifne            489
        //   486: goto            587
        //   489: iconst_1       
        //   490: istore_3       
        //   491: aload           10
        //   493: astore          9
        //   495: iload_3        
        //   496: iload           5
        //   498: if_icmpge       575
        //   501: aload           10
        //   503: iload_3        
        //   504: invokevirtual   java/lang/String.charAt:(I)C
        //   507: istore_2       
        //   508: iload_2        
        //   509: invokestatic    java/lang/Character.isJavaIdentifierPart:(C)Z
        //   512: ifne            568
        //   515: iload_2        
        //   516: bipush          46
        //   518: if_icmpne       552
        //   521: iload_3        
        //   522: iload           5
        //   524: bipush          6
        //   526: isub           
        //   527: if_icmpne       552
        //   530: aload           10
        //   532: ldc             ".class"
        //   534: invokevirtual   java/lang/String.endsWith:(Ljava/lang/String;)Z
        //   537: ifeq            552
        //   540: aload           10
        //   542: iconst_0       
        //   543: iload_3        
        //   544: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //   547: astore          9
        //   549: goto            575
        //   552: aload_0        
        //   553: ldc_w           "msg.invalid.classfile.name"
        //   556: aload           10
        //   558: invokespecial   org/mozilla/javascript/tools/jsc/Main.addError:(Ljava/lang/String;Ljava/lang/String;)V
        //   561: aload           10
        //   563: astore          9
        //   565: goto            575
        //   568: iload_3        
        //   569: iconst_1       
        //   570: iadd           
        //   571: istore_3       
        //   572: goto            491
        //   575: aload_0        
        //   576: aload           9
        //   578: putfield        org/mozilla/javascript/tools/jsc/Main.targetName:Ljava/lang/String;
        //   581: iload           4
        //   583: istore_3       
        //   584: goto            1034
        //   587: aload_0        
        //   588: ldc_w           "msg.invalid.classfile.name"
        //   591: aload           10
        //   593: invokespecial   org/mozilla/javascript/tools/jsc/Main.addError:(Ljava/lang/String;Ljava/lang/String;)V
        //   596: iload           4
        //   598: istore_3       
        //   599: goto            1034
        //   602: aload           9
        //   604: ldc_w           "-observe-instruction-count"
        //   607: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   610: ifeq            621
        //   613: aload_0        
        //   614: getfield        org/mozilla/javascript/tools/jsc/Main.compilerEnv:Lorg/mozilla/javascript/CompilerEnvirons;
        //   617: iconst_1       
        //   618: invokevirtual   org/mozilla/javascript/CompilerEnvirons.setGenerateObserverCount:(Z)V
        //   621: iload_3        
        //   622: istore          4
        //   624: aload           9
        //   626: ldc_w           "-package"
        //   629: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   632: ifeq            784
        //   635: iload_3        
        //   636: iconst_1       
        //   637: iadd           
        //   638: istore          5
        //   640: iload           5
        //   642: istore          4
        //   644: iload           5
        //   646: aload_1        
        //   647: arraylength    
        //   648: if_icmpge       784
        //   651: aload_1        
        //   652: iload           5
        //   654: aaload         
        //   655: astore          9
        //   657: aload           9
        //   659: invokevirtual   java/lang/String.length:()I
        //   662: istore          8
        //   664: iconst_0       
        //   665: istore_3       
        //   666: iload_3        
        //   667: iload           8
        //   669: if_icmpeq       772
        //   672: aload           9
        //   674: iload_3        
        //   675: invokevirtual   java/lang/String.charAt:(I)C
        //   678: istore_2       
        //   679: iload_2        
        //   680: invokestatic    java/lang/Character.isJavaIdentifierStart:(C)Z
        //   683: ifeq            759
        //   686: iload_2        
        //   687: istore          4
        //   689: iload_3        
        //   690: iconst_1       
        //   691: iadd           
        //   692: istore          7
        //   694: iload           7
        //   696: iload           8
        //   698: if_icmpeq       725
        //   701: aload           9
        //   703: iload           7
        //   705: invokevirtual   java/lang/String.charAt:(I)C
        //   708: istore_2       
        //   709: iload           7
        //   711: istore_3       
        //   712: iload_2        
        //   713: istore          4
        //   715: iload_2        
        //   716: invokestatic    java/lang/Character.isJavaIdentifierPart:(C)Z
        //   719: ifne            689
        //   722: iload_2        
        //   723: istore          4
        //   725: iload           7
        //   727: iload           8
        //   729: if_icmpne       735
        //   732: goto            772
        //   735: iload           4
        //   737: bipush          46
        //   739: if_icmpne       759
        //   742: iload           7
        //   744: iload           8
        //   746: iconst_1       
        //   747: isub           
        //   748: if_icmpeq       759
        //   751: iload           7
        //   753: iconst_1       
        //   754: iadd           
        //   755: istore_3       
        //   756: goto            666
        //   759: aload_0        
        //   760: ldc_w           "msg.package.name"
        //   763: aload_0        
        //   764: getfield        org/mozilla/javascript/tools/jsc/Main.targetPackage:Ljava/lang/String;
        //   767: invokespecial   org/mozilla/javascript/tools/jsc/Main.addError:(Ljava/lang/String;Ljava/lang/String;)V
        //   770: aconst_null    
        //   771: areturn        
        //   772: aload_0        
        //   773: aload           9
        //   775: putfield        org/mozilla/javascript/tools/jsc/Main.targetPackage:Ljava/lang/String;
        //   778: iload           5
        //   780: istore_3       
        //   781: goto            1034
        //   784: iload           4
        //   786: istore_3       
        //   787: aload           9
        //   789: ldc_w           "-extends"
        //   792: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   795: ifeq            855
        //   798: iload           4
        //   800: iconst_1       
        //   801: iadd           
        //   802: istore          4
        //   804: iload           4
        //   806: istore_3       
        //   807: iload           4
        //   809: aload_1        
        //   810: arraylength    
        //   811: if_icmpge       855
        //   814: aload_1        
        //   815: iload           4
        //   817: aaload         
        //   818: astore          9
        //   820: aload           9
        //   822: invokestatic    java/lang/Class.forName:(Ljava/lang/String;)Ljava/lang/Class;
        //   825: astore          9
        //   827: aload_0        
        //   828: getfield        org/mozilla/javascript/tools/jsc/Main.compiler:Lorg/mozilla/javascript/optimizer/ClassCompiler;
        //   831: aload           9
        //   833: invokevirtual   org/mozilla/javascript/optimizer/ClassCompiler.setTargetExtends:(Ljava/lang/Class;)V
        //   836: iload           4
        //   838: istore_3       
        //   839: goto            1034
        //   842: astore_1       
        //   843: new             Ljava/lang/Error;
        //   846: dup            
        //   847: aload_1        
        //   848: invokevirtual   java/lang/ClassNotFoundException.toString:()Ljava/lang/String;
        //   851: invokespecial   java/lang/Error.<init>:(Ljava/lang/String;)V
        //   854: athrow         
        //   855: iload_3        
        //   856: istore          4
        //   858: aload           9
        //   860: ldc_w           "-implements"
        //   863: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   866: ifeq            984
        //   869: iload_3        
        //   870: iconst_1       
        //   871: iadd           
        //   872: istore_3       
        //   873: iload_3        
        //   874: istore          4
        //   876: iload_3        
        //   877: aload_1        
        //   878: arraylength    
        //   879: if_icmpge       984
        //   882: new             Ljava/util/StringTokenizer;
        //   885: dup            
        //   886: aload_1        
        //   887: iload_3        
        //   888: aaload         
        //   889: ldc_w           ","
        //   892: invokespecial   java/util/StringTokenizer.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //   895: astore          9
        //   897: new             Ljava/util/ArrayList;
        //   900: dup            
        //   901: invokespecial   java/util/ArrayList.<init>:()V
        //   904: astore          10
        //   906: aload           9
        //   908: invokevirtual   java/util/StringTokenizer.hasMoreTokens:()Z
        //   911: ifeq            950
        //   914: aload           9
        //   916: invokevirtual   java/util/StringTokenizer.nextToken:()Ljava/lang/String;
        //   919: astore          11
        //   921: aload           10
        //   923: aload           11
        //   925: invokestatic    java/lang/Class.forName:(Ljava/lang/String;)Ljava/lang/Class;
        //   928: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   933: pop            
        //   934: goto            906
        //   937: astore_1       
        //   938: new             Ljava/lang/Error;
        //   941: dup            
        //   942: aload_1        
        //   943: invokevirtual   java/lang/ClassNotFoundException.toString:()Ljava/lang/String;
        //   946: invokespecial   java/lang/Error.<init>:(Ljava/lang/String;)V
        //   949: athrow         
        //   950: aload           10
        //   952: aload           10
        //   954: invokeinterface java/util/List.size:()I
        //   959: anewarray       Ljava/lang/Class;
        //   962: invokeinterface java/util/List.toArray:([Ljava/lang/Object;)[Ljava/lang/Object;
        //   967: checkcast       [Ljava/lang/Class;
        //   970: astore          9
        //   972: aload_0        
        //   973: getfield        org/mozilla/javascript/tools/jsc/Main.compiler:Lorg/mozilla/javascript/optimizer/ClassCompiler;
        //   976: aload           9
        //   978: invokevirtual   org/mozilla/javascript/optimizer/ClassCompiler.setTargetImplements:([Ljava/lang/Class;)V
        //   981: goto            1034
        //   984: aload           9
        //   986: ldc_w           "-d"
        //   989: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   992: ifeq            1016
        //   995: iload           4
        //   997: iconst_1       
        //   998: iadd           
        //   999: istore_3       
        //  1000: iload_3        
        //  1001: aload_1        
        //  1002: arraylength    
        //  1003: if_icmpge       1016
        //  1006: aload_0        
        //  1007: aload_1        
        //  1008: iload_3        
        //  1009: aaload         
        //  1010: putfield        org/mozilla/javascript/tools/jsc/Main.destinationDir:Ljava/lang/String;
        //  1013: goto            1034
        //  1016: aload           9
        //  1018: invokestatic    org/mozilla/javascript/tools/jsc/Main.badUsage:(Ljava/lang/String;)V
        //  1021: aconst_null    
        //  1022: areturn        
        //  1023: aload_0        
        //  1024: getfield        org/mozilla/javascript/tools/jsc/Main.compilerEnv:Lorg/mozilla/javascript/CompilerEnvirons;
        //  1027: iconst_1       
        //  1028: invokevirtual   org/mozilla/javascript/CompilerEnvirons.setGenerateDebugInfo:(Z)V
        //  1031: iload           4
        //  1033: istore_3       
        //  1034: iload_3        
        //  1035: iconst_1       
        //  1036: iadd           
        //  1037: istore          4
        //  1039: goto            24
        //  1042: astore          9
        //  1044: aload_1        
        //  1045: iload_3        
        //  1046: aaload         
        //  1047: invokestatic    org/mozilla/javascript/tools/jsc/Main.badUsage:(Ljava/lang/String;)V
        //  1050: aconst_null    
        //  1051: areturn        
        //  1052: aload_0        
        //  1053: iconst_1       
        //  1054: putfield        org/mozilla/javascript/tools/jsc/Main.printHelp:Z
        //  1057: aconst_null    
        //  1058: areturn        
        //  1059: ldc_w           "msg.no.file"
        //  1062: invokestatic    org/mozilla/javascript/tools/ToolErrorReporter.getMessage:(Ljava/lang/String;)Ljava/lang/String;
        //  1065: invokestatic    org/mozilla/javascript/tools/jsc/Main.p:(Ljava/lang/String;)V
        //  1068: aconst_null    
        //  1069: areturn        
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                              
        //  -----  -----  -----  -----  ----------------------------------
        //  155    165    1042   1052   Ljava/lang/NumberFormatException;
        //  178    185    1042   1052   Ljava/lang/NumberFormatException;
        //  188    197    1042   1052   Ljava/lang/NumberFormatException;
        //  200    209    1042   1052   Ljava/lang/NumberFormatException;
        //  218    228    1042   1052   Ljava/lang/NumberFormatException;
        //  235    245    1042   1052   Ljava/lang/NumberFormatException;
        //  258    265    1042   1052   Ljava/lang/NumberFormatException;
        //  268    277    1042   1052   Ljava/lang/NumberFormatException;
        //  280    289    1042   1052   Ljava/lang/NumberFormatException;
        //  820    827    842    855    Ljava/lang/ClassNotFoundException;
        //  921    934    937    950    Ljava/lang/ClassNotFoundException;
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.assembler.ir.StackMappingVisitor.push(StackMappingVisitor.java:290)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:833)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2030)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
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
    
    public void processSource(final String[] array) {
        for (int i = 0; i != array.length; ++i) {
            final String s = array[i];
            if (!s.endsWith(".js")) {
                this.addError("msg.extension.not.js", s);
                return;
            }
            final File file = new File(s);
            final String source = this.readSource(file);
            if (source == null) {
                return;
            }
            String s2;
            if ((s2 = this.targetName) == null) {
                final String name = file.getName();
                s2 = this.getClassName(name.substring(0, name.length() - 3));
            }
            String string = s2;
            if (this.targetPackage.length() != 0) {
                final StringBuilder sb = new StringBuilder();
                sb.append(this.targetPackage);
                sb.append(".");
                sb.append(s2);
                string = sb.toString();
            }
            final Object[] compileToClassFiles = this.compiler.compileToClassFiles(source, s, 1, string);
            if (compileToClassFiles == null) {
                return;
            }
            if (compileToClassFiles.length == 0) {
                return;
            }
            File file2 = null;
            if (this.destinationDir != null) {
                file2 = new File(this.destinationDir);
            }
            else {
                final String parent = file.getParent();
                if (parent != null) {
                    file2 = new File(parent);
                }
            }
            for (int j = 0; j != compileToClassFiles.length; j += 2) {
                final String s3 = (String)compileToClassFiles[j];
                final byte[] array2 = (byte[])compileToClassFiles[j + 1];
                final File outputFile = this.getOutputFile(file2, s3);
                try {
                    final FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
                    try {
                        fileOutputStream.write(array2);
                    }
                    finally {
                        fileOutputStream.close();
                    }
                }
                catch (IOException ex) {
                    this.addFormatedError(ex.toString());
                }
            }
        }
    }
}
