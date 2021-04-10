package org.mozilla.javascript.tools.shell;

import java.security.*;
import org.mozilla.javascript.tools.*;
import java.net.*;
import org.mozilla.javascript.commonjs.module.*;
import java.io.*;
import java.nio.charset.*;
import org.mozilla.javascript.*;
import java.lang.ref.*;
import java.util.*;

public class Main
{
    private static final int EXITCODE_FILE_NOT_FOUND = 4;
    private static final int EXITCODE_RUNTIME_ERROR = 3;
    protected static ToolErrorReporter errorReporter;
    protected static int exitCode;
    static List<String> fileList;
    public static Global global;
    static String mainModule;
    static List<String> modulePath;
    static boolean processStdin;
    static Require require;
    static boolean sandboxed;
    private static final ScriptCache scriptCache;
    private static SecurityProxy securityImpl;
    public static ShellContextFactory shellContextFactory;
    static boolean useRequire;
    
    static {
        Main.shellContextFactory = new ShellContextFactory();
        Main.global = new Global();
        Main.exitCode = 0;
        Main.processStdin = true;
        Main.fileList = new ArrayList<String>();
        Main.sandboxed = false;
        Main.useRequire = false;
        scriptCache = new ScriptCache(32);
        Main.global.initQuitAction(new IProxy(3));
    }
    
    static void evalInlineScript(final Context context, final String s) {
        try {
            final Script compileString = context.compileString(s, "<command>", 1, null);
            if (compileString != null) {
                compileString.exec(context, getShellScope());
            }
        }
        catch (VirtualMachineError virtualMachineError) {
            virtualMachineError.printStackTrace();
            Context.reportError(ToolErrorReporter.getMessage("msg.uncaughtJSException", virtualMachineError.toString()));
            Main.exitCode = 3;
        }
        catch (RhinoException ex) {
            ToolErrorReporter.reportException(context.getErrorReporter(), ex);
            Main.exitCode = 3;
        }
    }
    
    public static int exec(String[] processOptions) {
        Main.errorReporter = new ToolErrorReporter(false, Main.global.getErr());
        Main.shellContextFactory.setErrorReporter(Main.errorReporter);
        processOptions = processOptions(processOptions);
        if (Main.exitCode > 0) {
            return Main.exitCode;
        }
        if (Main.processStdin) {
            Main.fileList.add(null);
        }
        if (!Main.global.initialized) {
            Main.global.init(Main.shellContextFactory);
        }
        final IProxy proxy = new IProxy(1);
        proxy.args = processOptions;
        Main.shellContextFactory.call(proxy);
        return Main.exitCode;
    }
    
    private static byte[] getDigest(Object o) {
        if (o != null) {
            if (o instanceof String) {
                try {
                    o = ((String)o).getBytes("UTF-8");
                }
                catch (UnsupportedEncodingException ex2) {
                    o = ((String)o).getBytes();
                }
            }
            else {
                o = o;
            }
            try {
                return MessageDigest.getInstance("MD5").digest((byte[])o);
            }
            catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            }
        }
        return null;
    }
    
    public static PrintStream getErr() {
        return getGlobal().getErr();
    }
    
    public static Global getGlobal() {
        return Main.global;
    }
    
    public static InputStream getIn() {
        return getGlobal().getIn();
    }
    
    public static PrintStream getOut() {
        return getGlobal().getOut();
    }
    
    static Scriptable getScope(String s) {
        if (Main.useRequire) {
            if (s == null) {
                s = (String)new File(System.getProperty("user.dir")).toURI();
            }
            else if (SourceReader.toUrl(s) != null) {
                try {
                    s = (String)new URI(s);
                }
                catch (URISyntaxException ex) {
                    s = (String)new File(s).toURI();
                }
            }
            else {
                s = (String)new File(s).toURI();
            }
            return new ModuleScope(Main.global, (URI)s, null);
        }
        return Main.global;
    }
    
    static Scriptable getShellScope() {
        return getScope(null);
    }
    
    private static void initJavaPolicySecuritySupport() {
        try {
            SecurityController.initGlobal(Main.securityImpl = (SecurityProxy)Class.forName("org.mozilla.javascript.tools.shell.JavaPolicySecurity").newInstance());
            return;
        }
        catch (LinkageError linkageError) {}
        catch (InstantiationException linkageError) {}
        catch (IllegalAccessException linkageError) {}
        catch (ClassNotFoundException ex) {}
        final StringBuilder sb = new StringBuilder();
        sb.append("Can not load security support: ");
        final LinkageError linkageError;
        sb.append(linkageError);
        throw Kit.initCause(new IllegalStateException(sb.toString()), linkageError);
    }
    
    private static Script loadCompiledScript(final Context context, String substring, final byte[] array, final Object o) throws FileNotFoundException {
        if (array == null) {
            throw new FileNotFoundException(substring);
        }
        final int lastIndex = substring.lastIndexOf(47);
        int n;
        if (lastIndex < 0) {
            n = 0;
        }
        else {
            n = lastIndex + 1;
        }
        int n2;
        if ((n2 = substring.lastIndexOf(46)) < n) {
            n2 = substring.length();
        }
        substring = substring.substring(n, n2);
        try {
            final GeneratedClassLoader loader = SecurityController.createLoader(context.getApplicationClassLoader(), o);
            final Class<?> defineClass = loader.defineClass(substring, array);
            loader.linkClass(defineClass);
            if (!Script.class.isAssignableFrom(defineClass)) {
                throw Context.reportRuntimeError("msg.must.implement.Script");
            }
            return (Script)defineClass.newInstance();
        }
        catch (InstantiationException ex) {
            Context.reportError(ex.toString());
            throw new RuntimeException(ex);
        }
        catch (IllegalAccessException ex2) {
            Context.reportError(ex2.toString());
            throw new RuntimeException(ex2);
        }
    }
    
    public static void main(final String[] array) {
        try {
            if (Boolean.getBoolean("rhino.use_java_policy_security")) {
                initJavaPolicySecuritySupport();
            }
        }
        catch (SecurityException ex) {
            ex.printStackTrace(System.err);
        }
        final int exec = exec(array);
        if (exec != 0) {
            System.exit(exec);
        }
    }
    
    public static void processFile(final Context context, final Scriptable scriptable, final String s) throws IOException {
        if (Main.securityImpl == null) {
            processFileSecure(context, scriptable, s, null);
            return;
        }
        Main.securityImpl.callProcessFileSecure(context, scriptable, s);
    }
    
    public static void processFileNoThrow(final Context context, final Scriptable scriptable, final String s) {
        try {
            processFile(context, scriptable, s);
        }
        catch (VirtualMachineError virtualMachineError) {
            virtualMachineError.printStackTrace();
            Context.reportError(ToolErrorReporter.getMessage("msg.uncaughtJSException", virtualMachineError.toString()));
            Main.exitCode = 3;
        }
        catch (RhinoException ex) {
            ToolErrorReporter.reportException(context.getErrorReporter(), ex);
            Main.exitCode = 3;
        }
        catch (IOException ex2) {
            Context.reportError(ToolErrorReporter.getMessage("msg.couldnt.read.source", s, ex2.getMessage()));
            Main.exitCode = 4;
        }
    }
    
    static void processFileSecure(final Context context, final Scriptable scriptable, final String s, final Object o) throws IOException {
        final boolean endsWith = s.endsWith(".class");
        final Object fileOrUrl = readFileOrUrl(s, endsWith ^ true);
        final byte[] digest = getDigest(fileOrUrl);
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append("_");
        sb.append(context.getOptimizationLevel());
        final String string = sb.toString();
        final ScriptReference value = Main.scriptCache.get(string, digest);
        Script script;
        if (value != null) {
            script = value.get();
        }
        else {
            script = null;
        }
        Script script2 = script;
        if (script == null) {
            Script script3;
            if (endsWith) {
                script3 = loadCompiledScript(context, s, (byte[])fileOrUrl, o);
            }
            else {
                String substring;
                final String s2 = substring = (String)fileOrUrl;
                if (s2.length() > 0) {
                    substring = s2;
                    if (s2.charAt(0) == '#') {
                        int n = 1;
                        while (true) {
                            substring = s2;
                            if (n == s2.length()) {
                                break;
                            }
                            final char char1 = s2.charAt(n);
                            if (char1 == '\n' || char1 == '\r') {
                                substring = s2.substring(n);
                                break;
                            }
                            ++n;
                        }
                    }
                }
                script3 = context.compileString(substring, s, 1, o);
            }
            Main.scriptCache.put(string, digest, script3);
            script2 = script3;
        }
        if (script2 != null) {
            script2.exec(context, scriptable);
        }
    }
    
    static void processFiles(final Context context, String[] iterator) {
        final Object[] array = new Object[iterator.length];
        System.arraycopy(iterator, 0, array, 0, iterator.length);
        Main.global.defineProperty("arguments", context.newArray(Main.global, array), 2);
        iterator = (String[])(Object)Main.fileList.iterator();
        while (((Iterator)(Object)iterator).hasNext()) {
            final String s = ((Iterator<String>)(Object)iterator).next();
            try {
                processSource(context, s);
            }
            catch (VirtualMachineError virtualMachineError) {
                virtualMachineError.printStackTrace();
                Context.reportError(ToolErrorReporter.getMessage("msg.uncaughtJSException", virtualMachineError.toString()));
                Main.exitCode = 3;
            }
            catch (RhinoException s) {
                ToolErrorReporter.reportException(context.getErrorReporter(), (RhinoException)s);
                Main.exitCode = 3;
            }
            catch (IOException ex) {
                Context.reportError(ToolErrorReporter.getMessage("msg.couldnt.read.source", s, ex.getMessage()));
                Main.exitCode = 4;
            }
        }
    }
    
    public static String[] processOptions(final String[] p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: istore_1       
        //     2: iload_1        
        //     3: aload_0        
        //     4: arraylength    
        //     5: if_icmpne       13
        //     8: iconst_0       
        //     9: anewarray       Ljava/lang/String;
        //    12: areturn        
        //    13: aload_0        
        //    14: iload_1        
        //    15: aaload         
        //    16: astore          4
        //    18: aload           4
        //    20: ldc_w           "-"
        //    23: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //    26: ifne            79
        //    29: iconst_0       
        //    30: putstatic       org/mozilla/javascript/tools/shell/Main.processStdin:Z
        //    33: getstatic       org/mozilla/javascript/tools/shell/Main.fileList:Ljava/util/List;
        //    36: aload           4
        //    38: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //    43: pop            
        //    44: aload           4
        //    46: putstatic       org/mozilla/javascript/tools/shell/Main.mainModule:Ljava/lang/String;
        //    49: aload_0        
        //    50: arraylength    
        //    51: iload_1        
        //    52: isub           
        //    53: iconst_1       
        //    54: isub           
        //    55: anewarray       Ljava/lang/String;
        //    58: astore          4
        //    60: aload_0        
        //    61: iload_1        
        //    62: iconst_1       
        //    63: iadd           
        //    64: aload           4
        //    66: iconst_0       
        //    67: aload_0        
        //    68: arraylength    
        //    69: iload_1        
        //    70: isub           
        //    71: iconst_1       
        //    72: isub           
        //    73: invokestatic    java/lang/System.arraycopy:(Ljava/lang/Object;ILjava/lang/Object;II)V
        //    76: aload           4
        //    78: areturn        
        //    79: aload           4
        //    81: ldc_w           "-version"
        //    84: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    87: ifeq            146
        //    90: iload_1        
        //    91: iconst_1       
        //    92: iadd           
        //    93: istore_1       
        //    94: iload_1        
        //    95: aload_0        
        //    96: arraylength    
        //    97: if_icmpne       106
        //   100: aload           4
        //   102: astore_0       
        //   103: goto            723
        //   106: aload_0        
        //   107: iload_1        
        //   108: aaload         
        //   109: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
        //   112: istore_2       
        //   113: iload_2        
        //   114: invokestatic    org/mozilla/javascript/Context.isValidLanguageVersion:(I)Z
        //   117: ifne            127
        //   120: aload_0        
        //   121: iload_1        
        //   122: aaload         
        //   123: astore_0       
        //   124: goto            723
        //   127: getstatic       org/mozilla/javascript/tools/shell/Main.shellContextFactory:Lorg/mozilla/javascript/tools/shell/ShellContextFactory;
        //   130: iload_2        
        //   131: invokevirtual   org/mozilla/javascript/tools/shell/ShellContextFactory.setLanguageVersion:(I)V
        //   134: goto            707
        //   137: astore          4
        //   139: aload_0        
        //   140: iload_1        
        //   141: aaload         
        //   142: astore_0       
        //   143: goto            723
        //   146: aload           4
        //   148: ldc_w           "-opt"
        //   151: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   154: ifne            648
        //   157: aload           4
        //   159: ldc_w           "-O"
        //   162: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   165: ifeq            171
        //   168: goto            648
        //   171: aload           4
        //   173: ldc_w           "-encoding"
        //   176: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   179: ifeq            214
        //   182: iload_1        
        //   183: iconst_1       
        //   184: iadd           
        //   185: istore_1       
        //   186: iload_1        
        //   187: aload_0        
        //   188: arraylength    
        //   189: if_icmpne       198
        //   192: aload           4
        //   194: astore_0       
        //   195: goto            723
        //   198: aload_0        
        //   199: iload_1        
        //   200: aaload         
        //   201: astore          4
        //   203: getstatic       org/mozilla/javascript/tools/shell/Main.shellContextFactory:Lorg/mozilla/javascript/tools/shell/ShellContextFactory;
        //   206: aload           4
        //   208: invokevirtual   org/mozilla/javascript/tools/shell/ShellContextFactory.setCharacterEncoding:(Ljava/lang/String;)V
        //   211: goto            707
        //   214: aload           4
        //   216: ldc_w           "-strict"
        //   219: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   222: ifeq            249
        //   225: getstatic       org/mozilla/javascript/tools/shell/Main.shellContextFactory:Lorg/mozilla/javascript/tools/shell/ShellContextFactory;
        //   228: iconst_1       
        //   229: invokevirtual   org/mozilla/javascript/tools/shell/ShellContextFactory.setStrictMode:(Z)V
        //   232: getstatic       org/mozilla/javascript/tools/shell/Main.shellContextFactory:Lorg/mozilla/javascript/tools/shell/ShellContextFactory;
        //   235: iconst_0       
        //   236: invokevirtual   org/mozilla/javascript/tools/shell/ShellContextFactory.setAllowReservedKeywords:(Z)V
        //   239: getstatic       org/mozilla/javascript/tools/shell/Main.errorReporter:Lorg/mozilla/javascript/tools/ToolErrorReporter;
        //   242: iconst_1       
        //   243: invokevirtual   org/mozilla/javascript/tools/ToolErrorReporter.setIsReportingWarnings:(Z)V
        //   246: goto            707
        //   249: aload           4
        //   251: ldc_w           "-fatal-warnings"
        //   254: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   257: ifeq            270
        //   260: getstatic       org/mozilla/javascript/tools/shell/Main.shellContextFactory:Lorg/mozilla/javascript/tools/shell/ShellContextFactory;
        //   263: iconst_1       
        //   264: invokevirtual   org/mozilla/javascript/tools/shell/ShellContextFactory.setWarningAsError:(Z)V
        //   267: goto            707
        //   270: aload           4
        //   272: ldc_w           "-e"
        //   275: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   278: ifeq            349
        //   281: iconst_0       
        //   282: putstatic       org/mozilla/javascript/tools/shell/Main.processStdin:Z
        //   285: iload_1        
        //   286: iconst_1       
        //   287: iadd           
        //   288: istore_1       
        //   289: iload_1        
        //   290: aload_0        
        //   291: arraylength    
        //   292: if_icmpne       301
        //   295: aload           4
        //   297: astore_0       
        //   298: goto            723
        //   301: getstatic       org/mozilla/javascript/tools/shell/Main.global:Lorg/mozilla/javascript/tools/shell/Global;
        //   304: getfield        org/mozilla/javascript/tools/shell/Global.initialized:Z
        //   307: ifne            319
        //   310: getstatic       org/mozilla/javascript/tools/shell/Main.global:Lorg/mozilla/javascript/tools/shell/Global;
        //   313: getstatic       org/mozilla/javascript/tools/shell/Main.shellContextFactory:Lorg/mozilla/javascript/tools/shell/ShellContextFactory;
        //   316: invokevirtual   org/mozilla/javascript/tools/shell/Global.init:(Lorg/mozilla/javascript/ContextFactory;)V
        //   319: new             Lorg/mozilla/javascript/tools/shell/Main$IProxy;
        //   322: dup            
        //   323: iconst_2       
        //   324: invokespecial   org/mozilla/javascript/tools/shell/Main$IProxy.<init>:(I)V
        //   327: astore          4
        //   329: aload           4
        //   331: aload_0        
        //   332: iload_1        
        //   333: aaload         
        //   334: putfield        org/mozilla/javascript/tools/shell/Main$IProxy.scriptText:Ljava/lang/String;
        //   337: getstatic       org/mozilla/javascript/tools/shell/Main.shellContextFactory:Lorg/mozilla/javascript/tools/shell/ShellContextFactory;
        //   340: aload           4
        //   342: invokevirtual   org/mozilla/javascript/tools/shell/ShellContextFactory.call:(Lorg/mozilla/javascript/ContextAction;)Ljava/lang/Object;
        //   345: pop            
        //   346: goto            707
        //   349: aload           4
        //   351: ldc_w           "-require"
        //   354: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   357: ifeq            367
        //   360: iconst_1       
        //   361: putstatic       org/mozilla/javascript/tools/shell/Main.useRequire:Z
        //   364: goto            707
        //   367: aload           4
        //   369: ldc_w           "-sandbox"
        //   372: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   375: ifeq            389
        //   378: iconst_1       
        //   379: putstatic       org/mozilla/javascript/tools/shell/Main.sandboxed:Z
        //   382: iconst_1       
        //   383: putstatic       org/mozilla/javascript/tools/shell/Main.useRequire:Z
        //   386: goto            707
        //   389: aload           4
        //   391: ldc_w           "-modules"
        //   394: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   397: ifeq            451
        //   400: iload_1        
        //   401: iconst_1       
        //   402: iadd           
        //   403: istore_1       
        //   404: iload_1        
        //   405: aload_0        
        //   406: arraylength    
        //   407: if_icmpne       416
        //   410: aload           4
        //   412: astore_0       
        //   413: goto            723
        //   416: getstatic       org/mozilla/javascript/tools/shell/Main.modulePath:Ljava/util/List;
        //   419: ifnonnull       432
        //   422: new             Ljava/util/ArrayList;
        //   425: dup            
        //   426: invokespecial   java/util/ArrayList.<init>:()V
        //   429: putstatic       org/mozilla/javascript/tools/shell/Main.modulePath:Ljava/util/List;
        //   432: getstatic       org/mozilla/javascript/tools/shell/Main.modulePath:Ljava/util/List;
        //   435: aload_0        
        //   436: iload_1        
        //   437: aaload         
        //   438: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   443: pop            
        //   444: iconst_1       
        //   445: putstatic       org/mozilla/javascript/tools/shell/Main.useRequire:Z
        //   448: goto            707
        //   451: aload           4
        //   453: ldc_w           "-w"
        //   456: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   459: ifeq            472
        //   462: getstatic       org/mozilla/javascript/tools/shell/Main.errorReporter:Lorg/mozilla/javascript/tools/ToolErrorReporter;
        //   465: iconst_1       
        //   466: invokevirtual   org/mozilla/javascript/tools/ToolErrorReporter.setIsReportingWarnings:(Z)V
        //   469: goto            707
        //   472: aload           4
        //   474: ldc_w           "-f"
        //   477: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   480: ifeq            549
        //   483: iconst_0       
        //   484: putstatic       org/mozilla/javascript/tools/shell/Main.processStdin:Z
        //   487: iload_1        
        //   488: iconst_1       
        //   489: iadd           
        //   490: istore_1       
        //   491: iload_1        
        //   492: aload_0        
        //   493: arraylength    
        //   494: if_icmpne       503
        //   497: aload           4
        //   499: astore_0       
        //   500: goto            723
        //   503: aload_0        
        //   504: iload_1        
        //   505: aaload         
        //   506: ldc_w           "-"
        //   509: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   512: ifeq            528
        //   515: getstatic       org/mozilla/javascript/tools/shell/Main.fileList:Ljava/util/List;
        //   518: aconst_null    
        //   519: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   524: pop            
        //   525: goto            707
        //   528: getstatic       org/mozilla/javascript/tools/shell/Main.fileList:Ljava/util/List;
        //   531: aload_0        
        //   532: iload_1        
        //   533: aaload         
        //   534: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   539: pop            
        //   540: aload_0        
        //   541: iload_1        
        //   542: aaload         
        //   543: putstatic       org/mozilla/javascript/tools/shell/Main.mainModule:Ljava/lang/String;
        //   546: goto            707
        //   549: aload           4
        //   551: ldc_w           "-sealedlib"
        //   554: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   557: ifeq            570
        //   560: getstatic       org/mozilla/javascript/tools/shell/Main.global:Lorg/mozilla/javascript/tools/shell/Global;
        //   563: iconst_1       
        //   564: invokevirtual   org/mozilla/javascript/tools/shell/Global.setSealedStdLib:(Z)V
        //   567: goto            707
        //   570: aload           4
        //   572: ldc_w           "-debug"
        //   575: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   578: ifeq            591
        //   581: getstatic       org/mozilla/javascript/tools/shell/Main.shellContextFactory:Lorg/mozilla/javascript/tools/shell/ShellContextFactory;
        //   584: iconst_1       
        //   585: invokevirtual   org/mozilla/javascript/tools/shell/ShellContextFactory.setGeneratingDebug:(Z)V
        //   588: goto            707
        //   591: aload           4
        //   593: ldc_w           "-?"
        //   596: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   599: ifne            622
        //   602: aload           4
        //   604: ldc_w           "-help"
        //   607: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   610: ifeq            616
        //   613: goto            622
        //   616: aload           4
        //   618: astore_0       
        //   619: goto            723
        //   622: getstatic       org/mozilla/javascript/tools/shell/Main.global:Lorg/mozilla/javascript/tools/shell/Global;
        //   625: invokevirtual   org/mozilla/javascript/tools/shell/Global.getOut:()Ljava/io/PrintStream;
        //   628: ldc_w           "msg.shell.usage"
        //   631: ldc             Lorg/mozilla/javascript/tools/shell/Main;.class
        //   633: invokevirtual   java/lang/Class.getName:()Ljava/lang/String;
        //   636: invokestatic    org/mozilla/javascript/tools/ToolErrorReporter.getMessage:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   639: invokevirtual   java/io/PrintStream.println:(Ljava/lang/String;)V
        //   642: iconst_1       
        //   643: putstatic       org/mozilla/javascript/tools/shell/Main.exitCode:I
        //   646: aconst_null    
        //   647: areturn        
        //   648: iload_1        
        //   649: iconst_1       
        //   650: iadd           
        //   651: istore_3       
        //   652: iload_3        
        //   653: aload_0        
        //   654: arraylength    
        //   655: if_icmpne       664
        //   658: aload           4
        //   660: astore_0       
        //   661: goto            723
        //   664: aload_0        
        //   665: iload_3        
        //   666: aaload         
        //   667: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
        //   670: istore_1       
        //   671: iload_1        
        //   672: bipush          -2
        //   674: if_icmpne       682
        //   677: iconst_m1      
        //   678: istore_2       
        //   679: goto            698
        //   682: iload_1        
        //   683: istore_2       
        //   684: iload_1        
        //   685: invokestatic    org/mozilla/javascript/Context.isValidOptimizationLevel:(I)Z
        //   688: ifne            698
        //   691: aload_0        
        //   692: iload_3        
        //   693: aaload         
        //   694: astore_0       
        //   695: goto            723
        //   698: getstatic       org/mozilla/javascript/tools/shell/Main.shellContextFactory:Lorg/mozilla/javascript/tools/shell/ShellContextFactory;
        //   701: iload_2        
        //   702: invokevirtual   org/mozilla/javascript/tools/shell/ShellContextFactory.setOptimizationLevel:(I)V
        //   705: iload_3        
        //   706: istore_1       
        //   707: iload_1        
        //   708: iconst_1       
        //   709: iadd           
        //   710: istore_1       
        //   711: goto            2
        //   714: astore          4
        //   716: aload_0        
        //   717: iload_3        
        //   718: aaload         
        //   719: astore_0       
        //   720: goto            143
        //   723: getstatic       org/mozilla/javascript/tools/shell/Main.global:Lorg/mozilla/javascript/tools/shell/Global;
        //   726: invokevirtual   org/mozilla/javascript/tools/shell/Global.getOut:()Ljava/io/PrintStream;
        //   729: ldc_w           "msg.shell.invalid"
        //   732: aload_0        
        //   733: invokestatic    org/mozilla/javascript/tools/ToolErrorReporter.getMessage:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   736: invokevirtual   java/io/PrintStream.println:(Ljava/lang/String;)V
        //   739: getstatic       org/mozilla/javascript/tools/shell/Main.global:Lorg/mozilla/javascript/tools/shell/Global;
        //   742: invokevirtual   org/mozilla/javascript/tools/shell/Global.getOut:()Ljava/io/PrintStream;
        //   745: ldc_w           "msg.shell.usage"
        //   748: ldc             Lorg/mozilla/javascript/tools/shell/Main;.class
        //   750: invokevirtual   java/lang/Class.getName:()Ljava/lang/String;
        //   753: invokestatic    org/mozilla/javascript/tools/ToolErrorReporter.getMessage:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   756: invokevirtual   java/io/PrintStream.println:(Ljava/lang/String;)V
        //   759: iconst_1       
        //   760: putstatic       org/mozilla/javascript/tools/shell/Main.exitCode:I
        //   763: aconst_null    
        //   764: areturn        
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                             
        //  -----  -----  -----  -----  ---------------------------------
        //  106    113    137    143    Ljava/lang/NumberFormatException;
        //  664    671    714    723    Ljava/lang/NumberFormatException;
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
    
    public static void processSource(final Context context, final String s) throws IOException {
        if (s == null || s.equals("-")) {
            final Scriptable shellScope = getShellScope();
            final String characterEncoding = Main.shellContextFactory.getCharacterEncoding();
            Charset charset;
            if (characterEncoding != null) {
                charset = Charset.forName(characterEncoding);
            }
            else {
                charset = Charset.defaultCharset();
            }
            final ShellConsole console = Main.global.getConsole(charset);
            if (s == null) {
                console.println(context.getImplementationVersion());
            }
            int n = 1;
            int i = 0;
        Label_0357_Outer:
            while (i == 0) {
                final String[] prompts = Main.global.getPrompts(context);
                String s2 = null;
                if (s == null) {
                    s2 = prompts[0];
                }
                console.flush();
                String string = "";
                try {
                    while (true) {
                        final String line = console.readLine(s2);
                        if (line == null) {
                            i = 1;
                            break;
                        }
                        final StringBuilder sb = new StringBuilder();
                        sb.append(string);
                        sb.append(line);
                        sb.append("\n");
                        string = sb.toString();
                        ++n;
                        if (context.stringIsCompilableUnit(string)) {
                            break;
                        }
                        s2 = prompts[1];
                    }
                }
                catch (IOException ex) {
                    console.println(ex.toString());
                }
                Label_0431: {
                    Label_0406: {
                        try {
                            final Script compileString = context.compileString(string, "<stdin>", n, null);
                            if (compileString == null) {
                                continue Label_0357_Outer;
                            }
                            final Object exec = compileString.exec(context, shellScope);
                            while (true) {
                                Label_0360: {
                                    if (exec == Context.getUndefinedValue()) {
                                        break Label_0360;
                                    }
                                    try {
                                        if (exec instanceof Function) {
                                            if (string.trim().startsWith("function")) {
                                                break Label_0360;
                                            }
                                        }
                                        try {
                                            console.println(Context.toString(exec));
                                        }
                                        catch (RhinoException ex2) {
                                            ToolErrorReporter.reportException(context.getErrorReporter(), ex2);
                                        }
                                        break Label_0360;
                                    }
                                    catch (VirtualMachineError string) {
                                        break Label_0406;
                                    }
                                    catch (RhinoException ex3) {}
                                    break Label_0431;
                                }
                                final NativeArray history = Main.global.history;
                                try {
                                    history.put((int)history.getLength(), history, string);
                                }
                                catch (VirtualMachineError string) {
                                    break Label_0406;
                                }
                                catch (RhinoException string) {
                                    continue;
                                }
                                break;
                            }
                            continue Label_0357_Outer;
                        }
                        catch (RhinoException string) {
                            break Label_0431;
                        }
                        catch (VirtualMachineError virtualMachineError) {}
                    }
                    ((Throwable)string).printStackTrace();
                    Context.reportError(ToolErrorReporter.getMessage("msg.uncaughtJSException", ((Throwable)string).toString()));
                    Main.exitCode = 3;
                    continue;
                }
                ToolErrorReporter.reportException(context.getErrorReporter(), (RhinoException)string);
                Main.exitCode = 3;
            }
            console.println();
            console.flush();
            return;
        }
        if (Main.useRequire && s.equals(Main.mainModule)) {
            Main.require.requireMain(context, s);
            return;
        }
        processFile(context, getScope(s), s);
    }
    
    private static Object readFileOrUrl(final String s, final boolean b) throws IOException {
        return SourceReader.readFileOrUrl(s, b, Main.shellContextFactory.getCharacterEncoding());
    }
    
    public static void setErr(final PrintStream err) {
        getGlobal().setErr(err);
    }
    
    public static void setIn(final InputStream in) {
        getGlobal().setIn(in);
    }
    
    public static void setOut(final PrintStream out) {
        getGlobal().setOut(out);
    }
    
    private static class IProxy implements ContextAction, QuitAction
    {
        private static final int EVAL_INLINE_SCRIPT = 2;
        private static final int PROCESS_FILES = 1;
        private static final int SYSTEM_EXIT = 3;
        String[] args;
        String scriptText;
        private int type;
        
        IProxy(final int type) {
            this.type = type;
        }
        
        @Override
        public void quit(final Context context, final int n) {
            if (this.type == 3) {
                System.exit(n);
                return;
            }
            throw Kit.codeBug();
        }
        
        @Override
        public Object run(final Context context) {
            if (Main.useRequire) {
                Main.require = Main.global.installRequire(context, Main.modulePath, Main.sandboxed);
            }
            if (this.type == 1) {
                Main.processFiles(context, this.args);
            }
            else {
                if (this.type != 2) {
                    throw Kit.codeBug();
                }
                Main.evalInlineScript(context, this.scriptText);
            }
            return null;
        }
    }
    
    static class ScriptCache extends LinkedHashMap<String, ScriptReference>
    {
        int capacity;
        ReferenceQueue<Script> queue;
        
        ScriptCache(final int capacity) {
            super(capacity + 1, 2.0f, true);
            this.capacity = capacity;
            this.queue = new ReferenceQueue<Script>();
        }
        
        ScriptReference get(final String s, final byte[] array) {
            while (true) {
                final ScriptReference scriptReference = (ScriptReference)this.queue.poll();
                if (scriptReference == null) {
                    break;
                }
                this.remove(scriptReference.path);
            }
            final ScriptReference scriptReference2 = ((LinkedHashMap<K, ScriptReference>)this).get(s);
            ScriptReference scriptReference3;
            if ((scriptReference3 = scriptReference2) != null) {
                scriptReference3 = scriptReference2;
                if (!Arrays.equals(array, scriptReference2.digest)) {
                    this.remove(scriptReference2.path);
                    scriptReference3 = null;
                }
            }
            return scriptReference3;
        }
        
        void put(final String s, final byte[] array, final Script script) {
            this.put(s, new ScriptReference(s, array, script, this.queue));
        }
        
        @Override
        protected boolean removeEldestEntry(final Map.Entry<String, ScriptReference> entry) {
            return this.size() > this.capacity;
        }
    }
    
    static class ScriptReference extends SoftReference<Script>
    {
        byte[] digest;
        String path;
        
        ScriptReference(final String path, final byte[] digest, final Script script, final ReferenceQueue<Script> referenceQueue) {
            super(script, referenceQueue);
            this.path = path;
            this.digest = digest;
        }
    }
}
