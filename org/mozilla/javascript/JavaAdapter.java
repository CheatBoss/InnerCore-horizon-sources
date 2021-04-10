package org.mozilla.javascript;

import org.mozilla.classfile.*;
import java.security.*;
import java.lang.reflect.*;
import java.io.*;
import java.util.*;

public final class JavaAdapter implements IdFunctionCall
{
    private static final Object FTAG;
    private static final int Id_JavaAdapter = 1;
    
    static {
        FTAG = "JavaAdapter";
    }
    
    static int appendMethodSignature(final Class<?>[] array, final Class<?> clazz, final StringBuilder sb) {
        sb.append('(');
        int n = array.length + 1;
        int n2;
        for (int length = array.length, i = 0; i < length; ++i, n = n2) {
            final Class<?> clazz2 = array[i];
            appendTypeString(sb, clazz2);
            if (clazz2 != Long.TYPE) {
                n2 = n;
                if (clazz2 != Double.TYPE) {
                    continue;
                }
            }
            n2 = n + 1;
        }
        sb.append(')');
        appendTypeString(sb, clazz);
        return n;
    }
    
    private static void appendOverridableMethods(final Class<?> clazz, final ArrayList<Method> list, final HashSet<String> set) {
        final Method[] declaredMethods = clazz.getDeclaredMethods();
        for (int i = 0; i < declaredMethods.length; ++i) {
            final StringBuilder sb = new StringBuilder();
            sb.append(declaredMethods[i].getName());
            sb.append(getMethodSignature(declaredMethods[i], declaredMethods[i].getParameterTypes()));
            final String string = sb.toString();
            if (!set.contains(string)) {
                final int modifiers = declaredMethods[i].getModifiers();
                if (!Modifier.isStatic(modifiers)) {
                    if (Modifier.isFinal(modifiers)) {
                        set.add(string);
                    }
                    else if (Modifier.isPublic(modifiers) || Modifier.isProtected(modifiers)) {
                        list.add(declaredMethods[i]);
                        set.add(string);
                    }
                }
            }
        }
    }
    
    private static StringBuilder appendTypeString(final StringBuilder sb, Class<?> componentType) {
        while (componentType.isArray()) {
            sb.append('[');
            componentType = componentType.getComponentType();
        }
        if (componentType.isPrimitive()) {
            char upperCase;
            if (componentType == Boolean.TYPE) {
                upperCase = 'Z';
            }
            else if (componentType == Long.TYPE) {
                upperCase = 'J';
            }
            else {
                upperCase = Character.toUpperCase(componentType.getName().charAt(0));
            }
            sb.append(upperCase);
            return sb;
        }
        sb.append('L');
        sb.append(componentType.getName().replace('.', '/'));
        sb.append(';');
        return sb;
    }
    
    public static Object callMethod(final ContextFactory contextFactory, final Scriptable scriptable, final Function function, final Object[] array, final long n) {
        if (function == null) {
            return null;
        }
        ContextFactory global;
        if ((global = contextFactory) == null) {
            global = ContextFactory.getGlobal();
        }
        final Scriptable parentScope = function.getParentScope();
        if (n == 0L) {
            return Context.call(global, function, parentScope, scriptable, array);
        }
        final Context currentContext = Context.getCurrentContext();
        if (currentContext != null) {
            return doCall(currentContext, parentScope, scriptable, function, array, n);
        }
        return global.call(new ContextAction() {
            @Override
            public Object run(final Context context) {
                return doCall(context, parentScope, scriptable, function, array, n);
            }
        });
    }
    
    public static Object convertResult(final Object o, final Class<?> clazz) {
        if (o == Undefined.instance && clazz != ScriptRuntime.ObjectClass && clazz != ScriptRuntime.StringClass) {
            return null;
        }
        return Context.jsToJava(o, clazz);
    }
    
    public static byte[] createAdapterCode(final ObjToIntMap p0, final String p1, final Class<?> p2, final Class<?>[] p3, final String p4) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore          10
        //     3: new             Lorg/mozilla/classfile/ClassFileWriter;
        //     6: dup            
        //     7: aload_1        
        //     8: aload_2        
        //     9: invokevirtual   java/lang/Class.getName:()Ljava/lang/String;
        //    12: ldc             "<adapter>"
        //    14: invokespecial   org/mozilla/classfile/ClassFileWriter.<init>:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
        //    17: astore          13
        //    19: aload           13
        //    21: ldc             "factory"
        //    23: ldc             "Lorg/mozilla/javascript/ContextFactory;"
        //    25: bipush          17
        //    27: invokevirtual   org/mozilla/classfile/ClassFileWriter.addField:(Ljava/lang/String;Ljava/lang/String;S)V
        //    30: aload           13
        //    32: ldc             "delegee"
        //    34: ldc             "Lorg/mozilla/javascript/Scriptable;"
        //    36: bipush          17
        //    38: invokevirtual   org/mozilla/classfile/ClassFileWriter.addField:(Ljava/lang/String;Ljava/lang/String;S)V
        //    41: aload           13
        //    43: ldc             "self"
        //    45: ldc             "Lorg/mozilla/javascript/Scriptable;"
        //    47: bipush          17
        //    49: invokevirtual   org/mozilla/classfile/ClassFileWriter.addField:(Ljava/lang/String;Ljava/lang/String;S)V
        //    52: aload           10
        //    54: ifnonnull       63
        //    57: iconst_0       
        //    58: istore          5
        //    60: goto            68
        //    63: aload           10
        //    65: arraylength    
        //    66: istore          5
        //    68: iconst_0       
        //    69: istore          6
        //    71: iload           6
        //    73: iload           5
        //    75: if_icmpge       108
        //    78: aload           10
        //    80: iload           6
        //    82: aaload         
        //    83: ifnull          99
        //    86: aload           13
        //    88: aload           10
        //    90: iload           6
        //    92: aaload         
        //    93: invokevirtual   java/lang/Class.getName:()Ljava/lang/String;
        //    96: invokevirtual   org/mozilla/classfile/ClassFileWriter.addInterface:(Ljava/lang/String;)V
        //    99: iload           6
        //   101: iconst_1       
        //   102: iadd           
        //   103: istore          6
        //   105: goto            71
        //   108: aload_2        
        //   109: invokevirtual   java/lang/Class.getName:()Ljava/lang/String;
        //   112: bipush          46
        //   114: bipush          47
        //   116: invokevirtual   java/lang/String.replace:(CC)Ljava/lang/String;
        //   119: astore          14
        //   121: aload_2        
        //   122: invokevirtual   java/lang/Class.getDeclaredConstructors:()[Ljava/lang/reflect/Constructor;
        //   125: astore          10
        //   127: aload           10
        //   129: arraylength    
        //   130: istore          7
        //   132: iconst_0       
        //   133: istore          6
        //   135: iload           6
        //   137: iload           7
        //   139: if_icmpge       191
        //   142: aload           10
        //   144: iload           6
        //   146: aaload         
        //   147: astore          11
        //   149: aload           11
        //   151: invokevirtual   java/lang/reflect/Constructor.getModifiers:()I
        //   154: istore          8
        //   156: iload           8
        //   158: invokestatic    java/lang/reflect/Modifier.isPublic:(I)Z
        //   161: ifne            172
        //   164: iload           8
        //   166: invokestatic    java/lang/reflect/Modifier.isProtected:(I)Z
        //   169: ifeq            182
        //   172: aload           13
        //   174: aload_1        
        //   175: aload           14
        //   177: aload           11
        //   179: invokestatic    org/mozilla/javascript/JavaAdapter.generateCtor:(Lorg/mozilla/classfile/ClassFileWriter;Ljava/lang/String;Ljava/lang/String;Ljava/lang/reflect/Constructor;)V
        //   182: iload           6
        //   184: iconst_1       
        //   185: iadd           
        //   186: istore          6
        //   188: goto            135
        //   191: aload           13
        //   193: aload_1        
        //   194: aload           14
        //   196: invokestatic    org/mozilla/javascript/JavaAdapter.generateSerialCtor:(Lorg/mozilla/classfile/ClassFileWriter;Ljava/lang/String;Ljava/lang/String;)V
        //   199: aload           4
        //   201: ifnull          214
        //   204: aload           13
        //   206: aload_1        
        //   207: aload           14
        //   209: aload           4
        //   211: invokestatic    org/mozilla/javascript/JavaAdapter.generateEmptyCtor:(Lorg/mozilla/classfile/ClassFileWriter;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
        //   214: new             Lorg/mozilla/javascript/ObjToIntMap;
        //   217: dup            
        //   218: invokespecial   org/mozilla/javascript/ObjToIntMap.<init>:()V
        //   221: astore          11
        //   223: new             Lorg/mozilla/javascript/ObjToIntMap;
        //   226: dup            
        //   227: invokespecial   org/mozilla/javascript/ObjToIntMap.<init>:()V
        //   230: astore          4
        //   232: iconst_0       
        //   233: istore          6
        //   235: iload           6
        //   237: iload           5
        //   239: if_icmpge       454
        //   242: aload_3        
        //   243: iload           6
        //   245: aaload         
        //   246: invokevirtual   java/lang/Class.getMethods:()[Ljava/lang/reflect/Method;
        //   249: astore          12
        //   251: iconst_0       
        //   252: istore          7
        //   254: aload           4
        //   256: astore          10
        //   258: aload           12
        //   260: astore          4
        //   262: iload           7
        //   264: aload           4
        //   266: arraylength    
        //   267: if_icmpge       441
        //   270: aload           4
        //   272: iload           7
        //   274: aaload         
        //   275: astore          12
        //   277: aload           12
        //   279: invokevirtual   java/lang/reflect/Method.getModifiers:()I
        //   282: istore          8
        //   284: iload           8
        //   286: invokestatic    java/lang/reflect/Modifier.isStatic:(I)Z
        //   289: ifne            432
        //   292: iload           8
        //   294: invokestatic    java/lang/reflect/Modifier.isFinal:(I)Z
        //   297: ifeq            303
        //   300: goto            432
        //   303: aload           12
        //   305: invokevirtual   java/lang/reflect/Method.getName:()Ljava/lang/String;
        //   308: astore          15
        //   310: aload           12
        //   312: invokevirtual   java/lang/reflect/Method.getParameterTypes:()[Ljava/lang/Class;
        //   315: astore          16
        //   317: aload_0        
        //   318: aload           15
        //   320: invokevirtual   org/mozilla/javascript/ObjToIntMap.has:(Ljava/lang/Object;)Z
        //   323: ifne            343
        //   326: aload_2        
        //   327: aload           15
        //   329: aload           16
        //   331: invokevirtual   java/lang/Class.getMethod:(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
        //   334: pop            
        //   335: goto            432
        //   338: astore          17
        //   340: goto            343
        //   343: aload           12
        //   345: aload           16
        //   347: invokestatic    org/mozilla/javascript/JavaAdapter.getMethodSignature:(Ljava/lang/reflect/Method;[Ljava/lang/Class;)Ljava/lang/String;
        //   350: astore          17
        //   352: new             Ljava/lang/StringBuilder;
        //   355: dup            
        //   356: invokespecial   java/lang/StringBuilder.<init>:()V
        //   359: astore          18
        //   361: aload           18
        //   363: aload           15
        //   365: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   368: pop            
        //   369: aload           18
        //   371: aload           17
        //   373: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   376: pop            
        //   377: aload           18
        //   379: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   382: astore          17
        //   384: aload           11
        //   386: aload           17
        //   388: invokevirtual   org/mozilla/javascript/ObjToIntMap.has:(Ljava/lang/Object;)Z
        //   391: ifne            429
        //   394: aload           13
        //   396: aload_1        
        //   397: aload           15
        //   399: aload           16
        //   401: aload           12
        //   403: invokevirtual   java/lang/reflect/Method.getReturnType:()Ljava/lang/Class;
        //   406: iconst_1       
        //   407: invokestatic    org/mozilla/javascript/JavaAdapter.generateMethod:(Lorg/mozilla/classfile/ClassFileWriter;Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Class;Ljava/lang/Class;Z)V
        //   410: aload           11
        //   412: aload           17
        //   414: iconst_0       
        //   415: invokevirtual   org/mozilla/javascript/ObjToIntMap.put:(Ljava/lang/Object;I)V
        //   418: aload           10
        //   420: aload           15
        //   422: iconst_0       
        //   423: invokevirtual   org/mozilla/javascript/ObjToIntMap.put:(Ljava/lang/Object;I)V
        //   426: goto            432
        //   429: goto            432
        //   432: iload           7
        //   434: iconst_1       
        //   435: iadd           
        //   436: istore          7
        //   438: goto            262
        //   441: iload           6
        //   443: iconst_1       
        //   444: iadd           
        //   445: istore          6
        //   447: aload           10
        //   449: astore          4
        //   451: goto            235
        //   454: aload_2        
        //   455: invokestatic    org/mozilla/javascript/JavaAdapter.getOverridableMethods:(Ljava/lang/Class;)[Ljava/lang/reflect/Method;
        //   458: astore_3       
        //   459: iconst_0       
        //   460: istore          5
        //   462: aload           11
        //   464: astore_2       
        //   465: iload           5
        //   467: aload_3        
        //   468: arraylength    
        //   469: if_icmpge       639
        //   472: aload_3        
        //   473: iload           5
        //   475: aaload         
        //   476: astore          10
        //   478: aload           10
        //   480: invokevirtual   java/lang/reflect/Method.getModifiers:()I
        //   483: invokestatic    java/lang/reflect/Modifier.isAbstract:(I)Z
        //   486: istore          9
        //   488: aload           10
        //   490: invokevirtual   java/lang/reflect/Method.getName:()Ljava/lang/String;
        //   493: astore          11
        //   495: iload           9
        //   497: ifne            515
        //   500: aload_0        
        //   501: aload           11
        //   503: invokevirtual   org/mozilla/javascript/ObjToIntMap.has:(Ljava/lang/Object;)Z
        //   506: ifeq            512
        //   509: goto            515
        //   512: goto            630
        //   515: aload           10
        //   517: invokevirtual   java/lang/reflect/Method.getParameterTypes:()[Ljava/lang/Class;
        //   520: astore          12
        //   522: aload           10
        //   524: aload           12
        //   526: invokestatic    org/mozilla/javascript/JavaAdapter.getMethodSignature:(Ljava/lang/reflect/Method;[Ljava/lang/Class;)Ljava/lang/String;
        //   529: astore          15
        //   531: new             Ljava/lang/StringBuilder;
        //   534: dup            
        //   535: invokespecial   java/lang/StringBuilder.<init>:()V
        //   538: astore          16
        //   540: aload           16
        //   542: aload           11
        //   544: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   547: pop            
        //   548: aload           16
        //   550: aload           15
        //   552: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   555: pop            
        //   556: aload           16
        //   558: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   561: astore          16
        //   563: aload_2        
        //   564: aload           16
        //   566: invokevirtual   org/mozilla/javascript/ObjToIntMap.has:(Ljava/lang/Object;)Z
        //   569: ifne            630
        //   572: aload           13
        //   574: aload_1        
        //   575: aload           11
        //   577: aload           12
        //   579: aload           10
        //   581: invokevirtual   java/lang/reflect/Method.getReturnType:()Ljava/lang/Class;
        //   584: iconst_1       
        //   585: invokestatic    org/mozilla/javascript/JavaAdapter.generateMethod:(Lorg/mozilla/classfile/ClassFileWriter;Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Class;Ljava/lang/Class;Z)V
        //   588: aload_2        
        //   589: aload           16
        //   591: iconst_0       
        //   592: invokevirtual   org/mozilla/javascript/ObjToIntMap.put:(Ljava/lang/Object;I)V
        //   595: aload           4
        //   597: aload           11
        //   599: iconst_0       
        //   600: invokevirtual   org/mozilla/javascript/ObjToIntMap.put:(Ljava/lang/Object;I)V
        //   603: iload           9
        //   605: ifne            630
        //   608: aload           13
        //   610: aload_1        
        //   611: aload           14
        //   613: aload           11
        //   615: aload           15
        //   617: aload           12
        //   619: aload           10
        //   621: invokevirtual   java/lang/reflect/Method.getReturnType:()Ljava/lang/Class;
        //   624: invokestatic    org/mozilla/javascript/JavaAdapter.generateSuper:(Lorg/mozilla/classfile/ClassFileWriter;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Class;Ljava/lang/Class;)V
        //   627: goto            630
        //   630: iload           5
        //   632: iconst_1       
        //   633: iadd           
        //   634: istore          5
        //   636: goto            465
        //   639: new             Lorg/mozilla/javascript/ObjToIntMap$Iterator;
        //   642: dup            
        //   643: aload_0        
        //   644: invokespecial   org/mozilla/javascript/ObjToIntMap$Iterator.<init>:(Lorg/mozilla/javascript/ObjToIntMap;)V
        //   647: astore_0       
        //   648: aload_0        
        //   649: invokevirtual   org/mozilla/javascript/ObjToIntMap$Iterator.start:()V
        //   652: aload_0        
        //   653: invokevirtual   org/mozilla/javascript/ObjToIntMap$Iterator.done:()Z
        //   656: ifne            736
        //   659: aload_0        
        //   660: invokevirtual   org/mozilla/javascript/ObjToIntMap$Iterator.getKey:()Ljava/lang/Object;
        //   663: checkcast       Ljava/lang/String;
        //   666: astore_2       
        //   667: aload           4
        //   669: aload_2        
        //   670: invokevirtual   org/mozilla/javascript/ObjToIntMap.has:(Ljava/lang/Object;)Z
        //   673: ifeq            679
        //   676: goto            729
        //   679: aload_0        
        //   680: invokevirtual   org/mozilla/javascript/ObjToIntMap$Iterator.getValue:()I
        //   683: istore          6
        //   685: iload           6
        //   687: anewarray       Ljava/lang/Class;
        //   690: astore_3       
        //   691: iconst_0       
        //   692: istore          5
        //   694: iload           5
        //   696: iload           6
        //   698: if_icmpge       717
        //   701: aload_3        
        //   702: iload           5
        //   704: getstatic       org/mozilla/javascript/ScriptRuntime.ObjectClass:Ljava/lang/Class;
        //   707: aastore        
        //   708: iload           5
        //   710: iconst_1       
        //   711: iadd           
        //   712: istore          5
        //   714: goto            694
        //   717: aload           13
        //   719: aload_1        
        //   720: aload_2        
        //   721: aload_3        
        //   722: getstatic       org/mozilla/javascript/ScriptRuntime.ObjectClass:Ljava/lang/Class;
        //   725: iconst_0       
        //   726: invokestatic    org/mozilla/javascript/JavaAdapter.generateMethod:(Lorg/mozilla/classfile/ClassFileWriter;Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Class;Ljava/lang/Class;Z)V
        //   729: aload_0        
        //   730: invokevirtual   org/mozilla/javascript/ObjToIntMap$Iterator.next:()V
        //   733: goto            652
        //   736: aload           13
        //   738: invokevirtual   org/mozilla/classfile/ClassFileWriter.toByteArray:()[B
        //   741: areturn        
        //    Signature:
        //  (Lorg/mozilla/javascript/ObjToIntMap;Ljava/lang/String;Ljava/lang/Class<*>;[Ljava/lang/Class<*>;Ljava/lang/String;)[B
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                             
        //  -----  -----  -----  -----  ---------------------------------
        //  326    335    338    343    Ljava/lang/NoSuchMethodException;
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
    
    public static Scriptable createAdapterWrapper(final Scriptable prototype, final Object o) {
        final NativeJavaObject nativeJavaObject = new NativeJavaObject(ScriptableObject.getTopLevelScope(prototype), o, null, true);
        nativeJavaObject.setPrototype(prototype);
        return nativeJavaObject;
    }
    
    private static Object doCall(final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Function function, final Object[] array, final long n) {
        for (int i = 0; i != array.length; ++i) {
            if (0x0L != (n & (long)(1 << i))) {
                final Object o = array[i];
                if (!(o instanceof Scriptable)) {
                    array[i] = context.getWrapFactory().wrap(context, scriptable, o, null);
                }
            }
        }
        return function.call(context, scriptable, scriptable2, array);
    }
    
    private static void generateCtor(final ClassFileWriter classFileWriter, final String s, final String s2, final Constructor<?> constructor) {
        short n = 3;
        final Class[] parameterTypes = constructor.getParameterTypes();
        if (parameterTypes.length == 0) {
            classFileWriter.startMethod("<init>", "(Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/ContextFactory;)V", (short)1);
            classFileWriter.add(42);
            classFileWriter.addInvoke(183, s2, "<init>", "()V");
        }
        else {
            final StringBuilder sb = new StringBuilder("(Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/ContextFactory;");
            final int length = sb.length();
            final int length2 = parameterTypes.length;
            final int n2 = 0;
            for (int i = 0; i < length2; ++i) {
                appendTypeString(sb, parameterTypes[i]);
            }
            sb.append(")V");
            classFileWriter.startMethod("<init>", sb.toString(), (short)1);
            classFileWriter.add(42);
            n = 3;
            for (int length3 = parameterTypes.length, j = n2; j < length3; ++j) {
                n += (short)generatePushParam(classFileWriter, n, parameterTypes[j]);
            }
            sb.delete(1, length);
            classFileWriter.addInvoke(183, s2, "<init>", sb.toString());
        }
        classFileWriter.add(42);
        classFileWriter.add(43);
        classFileWriter.add(181, s, "delegee", "Lorg/mozilla/javascript/Scriptable;");
        classFileWriter.add(42);
        classFileWriter.add(44);
        classFileWriter.add(181, s, "factory", "Lorg/mozilla/javascript/ContextFactory;");
        classFileWriter.add(42);
        classFileWriter.add(43);
        classFileWriter.add(42);
        classFileWriter.addInvoke(184, "org/mozilla/javascript/JavaAdapter", "createAdapterWrapper", "(Lorg/mozilla/javascript/Scriptable;Ljava/lang/Object;)Lorg/mozilla/javascript/Scriptable;");
        classFileWriter.add(181, s, "self", "Lorg/mozilla/javascript/Scriptable;");
        classFileWriter.add(177);
        classFileWriter.stopMethod(n);
    }
    
    private static void generateEmptyCtor(final ClassFileWriter classFileWriter, final String s, final String s2, final String s3) {
        classFileWriter.startMethod("<init>", "()V", (short)1);
        classFileWriter.add(42);
        classFileWriter.addInvoke(183, s2, "<init>", "()V");
        classFileWriter.add(42);
        classFileWriter.add(1);
        classFileWriter.add(181, s, "factory", "Lorg/mozilla/javascript/ContextFactory;");
        classFileWriter.add(187, s3);
        classFileWriter.add(89);
        classFileWriter.addInvoke(183, s3, "<init>", "()V");
        classFileWriter.addInvoke(184, "org/mozilla/javascript/JavaAdapter", "runScript", "(Lorg/mozilla/javascript/Script;)Lorg/mozilla/javascript/Scriptable;");
        classFileWriter.add(76);
        classFileWriter.add(42);
        classFileWriter.add(43);
        classFileWriter.add(181, s, "delegee", "Lorg/mozilla/javascript/Scriptable;");
        classFileWriter.add(42);
        classFileWriter.add(43);
        classFileWriter.add(42);
        classFileWriter.addInvoke(184, "org/mozilla/javascript/JavaAdapter", "createAdapterWrapper", "(Lorg/mozilla/javascript/Scriptable;Ljava/lang/Object;)Lorg/mozilla/javascript/Scriptable;");
        classFileWriter.add(181, s, "self", "Lorg/mozilla/javascript/Scriptable;");
        classFileWriter.add(177);
        classFileWriter.stopMethod((short)2);
    }
    
    private static void generateMethod(final ClassFileWriter classFileWriter, final String s, final String s2, final Class<?>[] array, final Class<?> clazz, final boolean b) {
        final StringBuilder sb = new StringBuilder();
        final int appendMethodSignature = appendMethodSignature(array, clazz, sb);
        classFileWriter.startMethod(s2, sb.toString(), (short)1);
        classFileWriter.add(42);
        classFileWriter.add(180, s, "factory", "Lorg/mozilla/javascript/ContextFactory;");
        classFileWriter.add(42);
        classFileWriter.add(180, s, "self", "Lorg/mozilla/javascript/Scriptable;");
        classFileWriter.add(42);
        classFileWriter.add(180, s, "delegee", "Lorg/mozilla/javascript/Scriptable;");
        classFileWriter.addPush(s2);
        classFileWriter.addInvoke(184, "org/mozilla/javascript/JavaAdapter", "getFunction", "(Lorg/mozilla/javascript/Scriptable;Ljava/lang/String;)Lorg/mozilla/javascript/Function;");
        generatePushWrappedArgs(classFileWriter, array, array.length);
        if (array.length > 64) {
            throw Context.reportRuntimeError0("JavaAdapter can not subclass methods with more then 64 arguments.");
        }
        long n = 0L;
        long n2;
        for (int i = 0; i != array.length; ++i, n = n2) {
            n2 = n;
            if (!array[i].isPrimitive()) {
                n2 = (n | (long)(1 << i));
            }
        }
        classFileWriter.addPush(n);
        classFileWriter.addInvoke(184, "org/mozilla/javascript/JavaAdapter", "callMethod", "(Lorg/mozilla/javascript/ContextFactory;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Function;[Ljava/lang/Object;J)Ljava/lang/Object;");
        generateReturnResult(classFileWriter, clazz, b);
        classFileWriter.stopMethod((short)appendMethodSignature);
    }
    
    private static void generatePopResult(final ClassFileWriter classFileWriter, final Class<?> clazz) {
        if (clazz.isPrimitive()) {
            final char char1 = clazz.getName().charAt(0);
            if (char1 != 'f') {
                if (char1 != 'i') {
                    if (char1 == 'l') {
                        classFileWriter.add(173);
                        return;
                    }
                    if (char1 != 's' && char1 != 'z') {
                        switch (char1) {
                            default: {
                                return;
                            }
                            case 100: {
                                classFileWriter.add(175);
                                return;
                            }
                            case 98:
                            case 99: {
                                break;
                            }
                        }
                    }
                }
                classFileWriter.add(172);
            }
            else {
                classFileWriter.add(174);
            }
            return;
        }
        classFileWriter.add(176);
    }
    
    private static int generatePushParam(final ClassFileWriter classFileWriter, final int n, final Class<?> clazz) {
        if (!clazz.isPrimitive()) {
            classFileWriter.addALoad(n);
            return 1;
        }
        final char char1 = clazz.getName().charAt(0);
        if (char1 != 'f') {
            if (char1 != 'i') {
                if (char1 == 'l') {
                    classFileWriter.addLLoad(n);
                    return 2;
                }
                if (char1 != 's' && char1 != 'z') {
                    switch (char1) {
                        default: {
                            throw Kit.codeBug();
                        }
                        case 100: {
                            classFileWriter.addDLoad(n);
                            return 2;
                        }
                        case 98:
                        case 99: {
                            break;
                        }
                    }
                }
            }
            classFileWriter.addILoad(n);
            return 1;
        }
        classFileWriter.addFLoad(n);
        return 1;
    }
    
    static void generatePushWrappedArgs(final ClassFileWriter classFileWriter, final Class<?>[] array, int i) {
        classFileWriter.addPush(i);
        classFileWriter.add(189, "java/lang/Object");
        int n = 1;
        for (i = 0; i != array.length; ++i) {
            classFileWriter.add(89);
            classFileWriter.addPush(i);
            n += generateWrapArg(classFileWriter, n, array[i]);
            classFileWriter.add(83);
        }
    }
    
    static void generateReturnResult(final ClassFileWriter classFileWriter, final Class<?> clazz, final boolean b) {
        if (clazz == Void.TYPE) {
            classFileWriter.add(87);
            classFileWriter.add(177);
            return;
        }
        if (clazz == Boolean.TYPE) {
            classFileWriter.addInvoke(184, "org/mozilla/javascript/Context", "toBoolean", "(Ljava/lang/Object;)Z");
            classFileWriter.add(172);
            return;
        }
        if (clazz == Character.TYPE) {
            classFileWriter.addInvoke(184, "org/mozilla/javascript/Context", "toString", "(Ljava/lang/Object;)Ljava/lang/String;");
            classFileWriter.add(3);
            classFileWriter.addInvoke(182, "java/lang/String", "charAt", "(I)C");
            classFileWriter.add(172);
            return;
        }
        if (clazz.isPrimitive()) {
            classFileWriter.addInvoke(184, "org/mozilla/javascript/Context", "toNumber", "(Ljava/lang/Object;)D");
            final char char1 = clazz.getName().charAt(0);
            if (char1 != 'b') {
                if (char1 == 'd') {
                    classFileWriter.add(175);
                    return;
                }
                if (char1 == 'f') {
                    classFileWriter.add(144);
                    classFileWriter.add(174);
                    return;
                }
                if (char1 != 'i') {
                    if (char1 == 'l') {
                        classFileWriter.add(143);
                        classFileWriter.add(173);
                        return;
                    }
                    if (char1 != 's') {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Unexpected return type ");
                        sb.append(clazz.toString());
                        throw new RuntimeException(sb.toString());
                    }
                }
            }
            classFileWriter.add(142);
            classFileWriter.add(172);
            return;
        }
        final String name = clazz.getName();
        if (b) {
            classFileWriter.addLoadConstant(name);
            classFileWriter.addInvoke(184, "java/lang/Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;");
            classFileWriter.addInvoke(184, "org/mozilla/javascript/JavaAdapter", "convertResult", "(Ljava/lang/Object;Ljava/lang/Class;)Ljava/lang/Object;");
        }
        classFileWriter.add(192, name);
        classFileWriter.add(176);
    }
    
    private static void generateSerialCtor(final ClassFileWriter classFileWriter, final String s, final String s2) {
        classFileWriter.startMethod("<init>", "(Lorg/mozilla/javascript/ContextFactory;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;)V", (short)1);
        classFileWriter.add(42);
        classFileWriter.addInvoke(183, s2, "<init>", "()V");
        classFileWriter.add(42);
        classFileWriter.add(43);
        classFileWriter.add(181, s, "factory", "Lorg/mozilla/javascript/ContextFactory;");
        classFileWriter.add(42);
        classFileWriter.add(44);
        classFileWriter.add(181, s, "delegee", "Lorg/mozilla/javascript/Scriptable;");
        classFileWriter.add(42);
        classFileWriter.add(45);
        classFileWriter.add(181, s, "self", "Lorg/mozilla/javascript/Scriptable;");
        classFileWriter.add(177);
        classFileWriter.stopMethod((short)4);
    }
    
    private static void generateSuper(final ClassFileWriter classFileWriter, final String s, final String s2, final String s3, final String s4, final Class<?>[] array, final Class<?> clazz) {
        final StringBuilder sb = new StringBuilder();
        sb.append("super$");
        sb.append(s3);
        classFileWriter.startMethod(sb.toString(), s4, (short)1);
        int i = 0;
        classFileWriter.add(25, 0);
        int n = 1;
        while (i < array.length) {
            n += generatePushParam(classFileWriter, n, array[i]);
            ++i;
        }
        classFileWriter.addInvoke(183, s2, s3, s4);
        if (!clazz.equals(Void.TYPE)) {
            generatePopResult(classFileWriter, clazz);
        }
        else {
            classFileWriter.add(177);
        }
        classFileWriter.stopMethod((short)(n + 1));
    }
    
    private static int generateWrapArg(final ClassFileWriter classFileWriter, int n, final Class<?> clazz) {
        final int n2 = 1;
        if (!clazz.isPrimitive()) {
            classFileWriter.add(25, n);
            return 1;
        }
        if (clazz == Boolean.TYPE) {
            classFileWriter.add(187, "java/lang/Boolean");
            classFileWriter.add(89);
            classFileWriter.add(21, n);
            classFileWriter.addInvoke(183, "java/lang/Boolean", "<init>", "(Z)V");
            return 1;
        }
        if (clazz == Character.TYPE) {
            classFileWriter.add(21, n);
            classFileWriter.addInvoke(184, "java/lang/String", "valueOf", "(C)Ljava/lang/String;");
            return 1;
        }
        classFileWriter.add(187, "java/lang/Double");
        classFileWriter.add(89);
        final char char1 = clazz.getName().charAt(0);
        Label_0237: {
            if (char1 != 'b') {
                if (char1 == 'd') {
                    classFileWriter.add(24, n);
                    n = 2;
                    break Label_0237;
                }
                if (char1 == 'f') {
                    classFileWriter.add(23, n);
                    classFileWriter.add(141);
                    n = n2;
                    break Label_0237;
                }
                if (char1 != 'i') {
                    if (char1 == 'l') {
                        classFileWriter.add(22, n);
                        classFileWriter.add(138);
                        n = 2;
                        break Label_0237;
                    }
                    if (char1 != 's') {
                        n = n2;
                        break Label_0237;
                    }
                }
            }
            classFileWriter.add(21, n);
            classFileWriter.add(135);
            n = n2;
        }
        classFileWriter.addInvoke(183, "java/lang/Double", "<init>", "(D)V");
        return n;
    }
    
    private static Class<?> getAdapterClass(final Scriptable scriptable, final Class<?> clazz, final Class<?>[] array, final Scriptable scriptable2) {
        final ClassCache value = ClassCache.get(scriptable);
        final Map<JavaAdapterSignature, Class<?>> interfaceAdapterCacheMap = value.getInterfaceAdapterCacheMap();
        final ObjToIntMap objectFunctionNames = getObjectFunctionNames(scriptable2);
        final JavaAdapterSignature javaAdapterSignature = new JavaAdapterSignature(clazz, array, objectFunctionNames);
        Class<?> loadAdapterClass;
        if ((loadAdapterClass = interfaceAdapterCacheMap.get(javaAdapterSignature)) == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("adapter");
            sb.append(value.newClassSerialNumber());
            final String string = sb.toString();
            final Class<?> clazz2 = loadAdapterClass = loadAdapterClass(string, createAdapterCode(objectFunctionNames, string, clazz, array, null));
            if (value.isCachingEnabled()) {
                interfaceAdapterCacheMap.put(javaAdapterSignature, clazz2);
                loadAdapterClass = clazz2;
            }
        }
        return loadAdapterClass;
    }
    
    public static Object getAdapterSelf(final Class<?> clazz, final Object o) throws NoSuchFieldException, IllegalAccessException {
        return clazz.getDeclaredField("self").get(o);
    }
    
    static int[] getArgsToConvert(final Class<?>[] array) {
        final int n = 0;
        int n2 = 0;
        int n3;
        for (int i = 0; i != array.length; ++i, n2 = n3) {
            n3 = n2;
            if (!array[i].isPrimitive()) {
                n3 = n2 + 1;
            }
        }
        if (n2 == 0) {
            return null;
        }
        final int[] array2 = new int[n2];
        int n4 = 0;
        int n5;
        for (int j = n; j != array.length; ++j, n4 = n5) {
            n5 = n4;
            if (!array[j].isPrimitive()) {
                array2[n4] = j;
                n5 = n4 + 1;
            }
        }
        return array2;
    }
    
    public static Function getFunction(final Scriptable scriptable, final String s) {
        final Object property = ScriptableObject.getProperty(scriptable, s);
        if (property == Scriptable.NOT_FOUND) {
            return null;
        }
        if (!(property instanceof Function)) {
            throw ScriptRuntime.notFunctionError(property, s);
        }
        return (Function)property;
    }
    
    private static String getMethodSignature(final Method method, final Class<?>[] array) {
        final StringBuilder sb = new StringBuilder();
        appendMethodSignature(array, method.getReturnType(), sb);
        return sb.toString();
    }
    
    private static ObjToIntMap getObjectFunctionNames(final Scriptable scriptable) {
        final Object[] propertyIds = ScriptableObject.getPropertyIds(scriptable);
        final ObjToIntMap objToIntMap = new ObjToIntMap(propertyIds.length);
        for (int i = 0; i != propertyIds.length; ++i) {
            if (propertyIds[i] instanceof String) {
                final String s = (String)propertyIds[i];
                final Object property = ScriptableObject.getProperty(scriptable, s);
                if (property instanceof Function) {
                    int int32;
                    if ((int32 = ScriptRuntime.toInt32(ScriptableObject.getProperty((Scriptable)property, "length"))) < 0) {
                        int32 = 0;
                    }
                    objToIntMap.put(s, int32);
                }
            }
        }
        return objToIntMap;
    }
    
    static Method[] getOverridableMethods(Class<?> superclass) {
        final ArrayList<Method> list = new ArrayList<Method>();
        final HashSet<String> set = new HashSet<String>();
        for (Class<?> superclass2 = superclass; superclass2 != null; superclass2 = superclass2.getSuperclass()) {
            appendOverridableMethods(superclass2, list, set);
        }
        while (superclass != null) {
            final Class[] interfaces = superclass.getInterfaces();
            for (int length = interfaces.length, i = 0; i < length; ++i) {
                appendOverridableMethods(interfaces[i], list, set);
            }
            superclass = superclass.getSuperclass();
        }
        return list.toArray(new Method[list.size()]);
    }
    
    public static void init(final Context context, final Scriptable scriptable, final boolean b) {
        final IdFunctionObject idFunctionObject = new IdFunctionObject(new JavaAdapter(), JavaAdapter.FTAG, 1, "JavaAdapter", 1, scriptable);
        idFunctionObject.markAsConstructor(null);
        if (b) {
            idFunctionObject.sealObject();
        }
        idFunctionObject.exportAsScopeProperty();
    }
    
    static Object js_createAdapter(Context ex, final Scriptable scriptable, final Object[] array) {
        final int length = array.length;
        if (length == 0) {
            throw ScriptRuntime.typeError0("msg.adapter.zero.args");
        }
        int i;
        for (i = 0; i < length - 1; ++i) {
            final Object o = array[i];
            if (o instanceof NativeObject) {
                break;
            }
            if (!(o instanceof NativeJavaClass)) {
                throw ScriptRuntime.typeError2("msg.not.java.class.arg", String.valueOf(i), ScriptRuntime.toString(o));
            }
        }
        final Class[] array2 = new Class[i];
        int n = 0;
        Class<?> clazz = null;
        for (int j = 0; j < i; ++j) {
            final Class<?> classObject = ((NativeJavaClass)array[j]).getClassObject();
            if (!classObject.isInterface()) {
                if (clazz != null) {
                    throw ScriptRuntime.typeError2("msg.only.one.super", clazz.getName(), classObject.getName());
                }
                clazz = classObject;
            }
            else {
                array2[n] = classObject;
                ++n;
            }
        }
        Class<?> objectClass;
        if ((objectClass = clazz) == null) {
            objectClass = ScriptRuntime.ObjectClass;
        }
        final Class[] array3 = new Class[n];
        System.arraycopy(array2, 0, array3, 0, n);
        final Scriptable ensureScriptable = ScriptableObject.ensureScriptable(array[i]);
        final Class<?> adapterClass = getAdapterClass(scriptable, objectClass, array3, ensureScriptable);
        final int n2 = length - i - 1;
        Label_0331: {
            if (n2 <= 0) {
                break Label_0331;
            }
            Object[] array4;
            NativeJavaMethod ctors;
            int cachedFunction;
            Class<Scriptable> scriptableClass;
            Class<?> contextFactoryClass;
            Object adapterSelf;
            Object unwrap;
            Label_0315_Outer:Label_0435_Outer:
            while (true) {
                while (true) {
                    try {
                        array4 = new Object[n2 + 2];
                        array4[0] = ensureScriptable;
                        array4[1] = ((Context)ex).getFactory();
                        System.arraycopy(array, i + 1, array4, 2, n2);
                        ctors = new NativeJavaClass(scriptable, adapterClass, true).members.ctors;
                        try {
                            cachedFunction = ctors.findCachedFunction((Context)ex, array4);
                            while (true) {
                                if (cachedFunction < 0) {
                                    ex = (Exception)NativeJavaMethod.scriptSignature(array);
                                    try {
                                        throw Context.reportRuntimeError2("msg.no.java.ctor", adapterClass.getName(), ex);
                                        scriptableClass = ScriptRuntime.ScriptableClass;
                                        contextFactoryClass = ScriptRuntime.ContextFactoryClass;
                                        ex = (Exception)((Context)ex).getFactory();
                                        ex = (Exception)adapterClass.getConstructor(scriptableClass, contextFactoryClass).newInstance(ensureScriptable, ex);
                                        while (true) {
                                            try {
                                                adapterSelf = getAdapterSelf(adapterClass, ex);
                                                if (adapterSelf instanceof Wrapper) {
                                                    unwrap = ((Wrapper)adapterSelf).unwrap();
                                                    if (unwrap instanceof Scriptable) {
                                                        if (unwrap instanceof ScriptableObject) {
                                                            ScriptRuntime.setObjectProtoAndParent((ScriptableObject)unwrap, scriptable);
                                                        }
                                                        return unwrap;
                                                    }
                                                }
                                                return adapterSelf;
                                            }
                                            catch (Exception ex) {}
                                            ex = (Exception)NativeJavaClass.constructInternal(array4, ctors.methods[cachedFunction]);
                                            continue Label_0315_Outer;
                                        }
                                    }
                                    catch (Exception ex2) {}
                                    throw Context.throwAsScriptRuntimeEx(ex);
                                }
                                continue Label_0435_Outer;
                            }
                        }
                        catch (Exception ex) {}
                    }
                    catch (Exception ex3) {}
                    continue;
                }
            }
        }
    }
    
    static Class<?> loadAdapterClass(final String s, final byte[] array) {
        final Class<?> staticSecurityDomainClass = SecurityController.getStaticSecurityDomainClass();
        Object o;
        if (staticSecurityDomainClass != CodeSource.class && staticSecurityDomainClass != ProtectionDomain.class) {
            o = null;
        }
        else {
            ProtectionDomain protectionDomain;
            if ((protectionDomain = SecurityUtilities.getScriptProtectionDomain()) == null) {
                protectionDomain = JavaAdapter.class.getProtectionDomain();
            }
            o = protectionDomain;
            if (staticSecurityDomainClass == CodeSource.class) {
                Object codeSource;
                if (protectionDomain == null) {
                    codeSource = null;
                }
                else {
                    codeSource = protectionDomain.getCodeSource();
                }
                o = codeSource;
            }
        }
        final GeneratedClassLoader loader = SecurityController.createLoader(null, o);
        final Class<?> defineClass = loader.defineClass(s, array);
        loader.linkClass(defineClass);
        return defineClass;
    }
    
    public static Object readAdapterObject(final Scriptable scriptable, final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        final Context currentContext = Context.getCurrentContext();
        ContextFactory factory;
        if (currentContext != null) {
            factory = currentContext.getFactory();
        }
        else {
            factory = null;
        }
        final Class<?> forName = Class.forName((String)objectInputStream.readObject());
        final String[] array = (String[])objectInputStream.readObject();
        final Class[] array2 = new Class[array.length];
        for (int i = 0; i < array.length; ++i) {
            array2[i] = Class.forName(array[i]);
        }
        final Scriptable scriptable2 = (Scriptable)objectInputStream.readObject();
        final Class<?> adapterClass = getAdapterClass(scriptable, forName, array2, scriptable2);
        final Class<?> contextFactoryClass = ScriptRuntime.ContextFactoryClass;
        final Class<Scriptable> scriptableClass = ScriptRuntime.ScriptableClass;
        final Class<Scriptable> scriptableClass2 = ScriptRuntime.ScriptableClass;
        try {
            return adapterClass.getConstructor(contextFactoryClass, scriptableClass, scriptableClass2).newInstance(factory, scriptable2, scriptable);
        }
        catch (NoSuchMethodException ex) {}
        catch (InvocationTargetException ex2) {}
        catch (IllegalAccessException ex3) {}
        catch (InstantiationException ex4) {}
        throw new ClassNotFoundException("adapter");
    }
    
    public static Scriptable runScript(final Script script) {
        return (Scriptable)ContextFactory.getGlobal().call(new ContextAction() {
            @Override
            public Object run(final Context context) {
                final ScriptableObject global = ScriptRuntime.getGlobal(context);
                script.exec(context, global);
                return global;
            }
        });
    }
    
    public static void writeAdapterObject(final Object o, final ObjectOutputStream objectOutputStream) throws IOException {
        final Class<?> class1 = o.getClass();
        objectOutputStream.writeObject(class1.getSuperclass().getName());
        final Class<?>[] interfaces = class1.getInterfaces();
        final String[] array = new String[interfaces.length];
        for (int i = 0; i < interfaces.length; ++i) {
            array[i] = interfaces[i].getName();
        }
        objectOutputStream.writeObject(array);
        try {
            objectOutputStream.writeObject(class1.getField("delegee").get(o));
            return;
        }
        catch (NoSuchFieldException ex) {}
        catch (IllegalAccessException ex2) {}
        throw new IOException();
    }
    
    @Override
    public Object execIdCall(final IdFunctionObject idFunctionObject, final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        if (idFunctionObject.hasTag(JavaAdapter.FTAG) && idFunctionObject.methodId() == 1) {
            return js_createAdapter(context, scriptable, array);
        }
        throw idFunctionObject.unknown();
    }
    
    static class JavaAdapterSignature
    {
        Class<?>[] interfaces;
        ObjToIntMap names;
        Class<?> superClass;
        
        JavaAdapterSignature(final Class<?> superClass, final Class<?>[] interfaces, final ObjToIntMap names) {
            this.superClass = superClass;
            this.interfaces = interfaces;
            this.names = names;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof JavaAdapterSignature)) {
                return false;
            }
            final JavaAdapterSignature javaAdapterSignature = (JavaAdapterSignature)o;
            if (this.superClass != javaAdapterSignature.superClass) {
                return false;
            }
            if (this.interfaces != javaAdapterSignature.interfaces) {
                if (this.interfaces.length != javaAdapterSignature.interfaces.length) {
                    return false;
                }
                for (int i = 0; i < this.interfaces.length; ++i) {
                    if (this.interfaces[i] != javaAdapterSignature.interfaces[i]) {
                        return false;
                    }
                }
            }
            if (this.names.size() != javaAdapterSignature.names.size()) {
                return false;
            }
            final ObjToIntMap.Iterator iterator = new ObjToIntMap.Iterator(this.names);
            iterator.start();
            while (!iterator.done()) {
                final String s = (String)iterator.getKey();
                final int value = iterator.getValue();
                if (value != javaAdapterSignature.names.get(s, value + 1)) {
                    return false;
                }
                iterator.next();
            }
            return true;
        }
        
        @Override
        public int hashCode() {
            return this.superClass.hashCode() + Arrays.hashCode(this.interfaces) ^ this.names.size();
        }
    }
}
