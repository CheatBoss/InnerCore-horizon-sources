package org.mozilla.javascript.tools.shell;

import java.lang.reflect.*;
import java.util.regex.*;
import org.mozilla.javascript.tools.*;
import java.nio.charset.*;
import org.mozilla.javascript.serialize.*;
import java.io.*;
import org.mozilla.javascript.*;
import java.util.*;
import java.net.*;
import org.mozilla.javascript.commonjs.module.provider.*;
import org.mozilla.javascript.commonjs.module.*;

public class Global extends ImporterTopLevel
{
    static final long serialVersionUID = 4029130780977538005L;
    boolean attemptedJLineLoad;
    private ShellConsole console;
    private HashMap<String, String> doctestCanonicalizations;
    private PrintStream errStream;
    NativeArray history;
    private InputStream inStream;
    boolean initialized;
    private PrintStream outStream;
    private String[] prompts;
    private QuitAction quitAction;
    private boolean sealedStdLib;
    
    public Global() {
        this.sealedStdLib = false;
        this.prompts = new String[] { "js> ", "  > " };
    }
    
    public Global(final Context context) {
        this.sealedStdLib = false;
        this.prompts = new String[] { "js> ", "  > " };
        this.init(context);
    }
    
    public static void defineClass(final Context context, final Scriptable scriptable, final Object[] array, final Function function) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        final Class<?> class1 = getClass(array);
        if (!Scriptable.class.isAssignableFrom(class1)) {
            throw reportRuntimeError("msg.must.implement.Scriptable");
        }
        ScriptableObject.defineClass(scriptable, class1);
    }
    
    public static Object deserialize(final Context context, final Scriptable scriptable, final Object[] array, final Function function) throws IOException, ClassNotFoundException {
        if (array.length < 1) {
            throw Context.reportRuntimeError("Expected a filename to read the serialization from");
        }
        final FileInputStream fileInputStream = new FileInputStream(Context.toString(array[0]));
        final Scriptable topLevelScope = ScriptableObject.getTopLevelScope(scriptable);
        final ScriptableInputStream scriptableInputStream = new ScriptableInputStream(fileInputStream, topLevelScope);
        final Object object = scriptableInputStream.readObject();
        scriptableInputStream.close();
        return Context.toObject(object, topLevelScope);
    }
    
    private static Object doPrint(final Object[] array, final Function function, final boolean b) {
        final PrintStream out = getInstance(function).getOut();
        for (int i = 0; i < array.length; ++i) {
            if (i > 0) {
                out.print(" ");
            }
            out.print(Context.toString(array[i]));
        }
        if (b) {
            out.println();
        }
        return Context.getUndefinedValue();
    }
    
    public static Object doctest(final Context context, final Scriptable scriptable, final Object[] array, final Function function) {
        if (array.length == 0) {
            return Boolean.FALSE;
        }
        final String string = Context.toString(array[0]);
        final Global instance = getInstance(function);
        return new Integer(instance.runDoctest(context, instance, string, null, 0));
    }
    
    private boolean doctestOutputMatches(String s, String replace) {
        s = s.trim();
        replace = replace.trim().replace("\r\n", "\n");
        if (s.equals(replace)) {
            return true;
        }
        for (final Map.Entry<String, String> entry : this.doctestCanonicalizations.entrySet()) {
            s = s.replace(entry.getKey(), entry.getValue());
        }
        if (s.equals(replace)) {
            return true;
        }
        final Pattern compile = Pattern.compile("@[0-9a-fA-F]+");
        final Matcher matcher = compile.matcher(s);
        final Matcher matcher2 = compile.matcher(replace);
        while (matcher.find()) {
            if (!matcher2.find()) {
                return false;
            }
            if (matcher2.start() != matcher.start()) {
                return false;
            }
            final int start = matcher.start();
            if (!s.substring(0, start).equals(replace.substring(0, start))) {
                return false;
            }
            final String group = matcher.group();
            final String group2 = matcher2.group();
            final String s2 = this.doctestCanonicalizations.get(group);
            if (s2 == null) {
                this.doctestCanonicalizations.put(group, group2);
                s = s.replace(group, group2);
            }
            else if (!group2.equals(s2)) {
                return false;
            }
            if (s.equals(replace)) {
                return true;
            }
        }
        return false;
    }
    
    public static void gc(final Context context, final Scriptable scriptable, final Object[] array, final Function function) {
        System.gc();
    }
    
    private static String getCharCodingFromType(final String s) {
        final int index = s.indexOf(59);
        if (index >= 0) {
            int length;
            int n;
            for (length = s.length(), n = index + 1; n != length && s.charAt(n) <= ' '; ++n) {}
            if ("charset".regionMatches(true, 0, s, n, "charset".length())) {
                int n2;
                for (n2 = n + "charset".length(); n2 != length && s.charAt(n2) <= ' '; ++n2) {}
                if (n2 != length && s.charAt(n2) == '=') {
                    int n3;
                    for (n3 = n2 + 1; n3 != length && s.charAt(n3) <= ' '; ++n3) {}
                    if (n3 != length) {
                        while (s.charAt(length - 1) <= ' ') {
                            --length;
                        }
                        return s.substring(n3, length);
                    }
                }
            }
        }
        return null;
    }
    
    private static Class<?> getClass(Object[] string) {
        if (string.length == 0) {
            throw reportRuntimeError("msg.expected.string.arg");
        }
        final Object o = string[0];
        if (o instanceof Wrapper) {
            final Object unwrap = ((Wrapper)o).unwrap();
            if (unwrap instanceof Class) {
                return (Class<?>)unwrap;
            }
        }
        string = (Object[])(Object)Context.toString(string[0]);
        try {
            return Class.forName((String)string);
        }
        catch (ClassNotFoundException ex) {
            throw reportRuntimeError("msg.class.not.found", (String)string);
        }
    }
    
    private static Global getInstance(final Function function) {
        final Scriptable parentScope = function.getParentScope();
        if (!(parentScope instanceof Global)) {
            throw reportRuntimeError("msg.bad.shell.function.scope", String.valueOf(parentScope));
        }
        return (Global)parentScope;
    }
    
    public static void help(final Context context, final Scriptable scriptable, final Object[] array, final Function function) {
        getInstance(function).getOut().println(ToolErrorReporter.getMessage("msg.help"));
    }
    
    public static void load(final Context context, final Scriptable scriptable, final Object[] array, Function string) {
        final int length = array.length;
        int i = 0;
        while (i < length) {
            string = (Function)Context.toString(array[i]);
            try {
                Main.processFile(context, scriptable, (String)string);
                ++i;
                continue;
            }
            catch (VirtualMachineError virtualMachineError) {
                virtualMachineError.printStackTrace();
                throw Context.reportRuntimeError(ToolErrorReporter.getMessage("msg.uncaughtJSException", virtualMachineError.toString()));
            }
            catch (IOException ex) {
                throw Context.reportRuntimeError(ToolErrorReporter.getMessage("msg.couldnt.read.source", string, ex.getMessage()));
            }
            break;
        }
    }
    
    public static void loadClass(final Context context, final Scriptable scriptable, final Object[] array, final Function function) throws IllegalAccessException, InstantiationException {
        final Class<?> class1 = getClass(array);
        if (!Script.class.isAssignableFrom(class1)) {
            throw reportRuntimeError("msg.must.implement.Script");
        }
        ((Script)class1.newInstance()).exec(context, scriptable);
    }
    
    private boolean loadJLine(final Charset charset) {
        if (!this.attemptedJLineLoad) {
            this.attemptedJLineLoad = true;
            this.console = ShellConsole.getConsole(this, charset);
        }
        return this.console != null;
    }
    
    static void pipe(final boolean p0, final InputStream p1, final OutputStream p2) throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: newarray        B
        //     5: astore          4
        //     7: iload_0        
        //     8: ifne            25
        //    11: aload_1        
        //    12: aload           4
        //    14: iconst_0       
        //    15: sipush          4096
        //    18: invokevirtual   java/io/InputStream.read:([BII)I
        //    21: istore_3       
        //    22: goto            36
        //    25: aload_1        
        //    26: aload           4
        //    28: iconst_0       
        //    29: sipush          4096
        //    32: invokevirtual   java/io/InputStream.read:([BII)I
        //    35: istore_3       
        //    36: iload_3        
        //    37: ifge            43
        //    40: goto            84
        //    43: iload_0        
        //    44: ifeq            62
        //    47: aload_2        
        //    48: aload           4
        //    50: iconst_0       
        //    51: iload_3        
        //    52: invokevirtual   java/io/OutputStream.write:([BII)V
        //    55: aload_2        
        //    56: invokevirtual   java/io/OutputStream.flush:()V
        //    59: goto            74
        //    62: aload_2        
        //    63: aload           4
        //    65: iconst_0       
        //    66: iload_3        
        //    67: invokevirtual   java/io/OutputStream.write:([BII)V
        //    70: aload_2        
        //    71: invokevirtual   java/io/OutputStream.flush:()V
        //    74: goto            7
        //    77: astore          4
        //    79: goto            84
        //    82: astore          4
        //    84: iload_0        
        //    85: ifeq            95
        //    88: aload_1        
        //    89: invokevirtual   java/io/InputStream.close:()V
        //    92: goto            99
        //    95: aload_2        
        //    96: invokevirtual   java/io/OutputStream.close:()V
        //    99: return         
        //   100: return         
        //   101: astore          4
        //   103: iload_0        
        //   104: ifeq            114
        //   107: aload_1        
        //   108: invokevirtual   java/io/InputStream.close:()V
        //   111: goto            118
        //   114: aload_2        
        //   115: invokevirtual   java/io/OutputStream.close:()V
        //   118: goto            121
        //   121: aload           4
        //   123: athrow         
        //   124: astore_1       
        //   125: goto            100
        //   128: astore_1       
        //   129: goto            121
        //    Exceptions:
        //  throws java.io.IOException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  0      7      101    124    Any
        //  11     22     101    124    Any
        //  25     36     82     84     Ljava/io/IOException;
        //  25     36     101    124    Any
        //  47     59     101    124    Any
        //  62     74     77     82     Ljava/io/IOException;
        //  62     74     101    124    Any
        //  88     92     124    101    Ljava/io/IOException;
        //  95     99     124    101    Ljava/io/IOException;
        //  107    111    128    132    Ljava/io/IOException;
        //  114    118    128    132    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 66, Size: 66
        //     at java.util.ArrayList.rangeCheck(Unknown Source)
        //     at java.util.ArrayList.get(Unknown Source)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
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
    
    public static Object print(final Context context, final Scriptable scriptable, final Object[] array, final Function function) {
        return doPrint(array, function, true);
    }
    
    public static void quit(final Context context, final Scriptable scriptable, final Object[] array, final Function function) {
        final Global instance = getInstance(function);
        if (instance.quitAction != null) {
            final int length = array.length;
            int int32 = 0;
            if (length != 0) {
                int32 = ScriptRuntime.toInt32(array[0]);
            }
            instance.quitAction.quit(context, int32);
        }
    }
    
    public static Object readFile(final Context context, final Scriptable scriptable, final Object[] array, final Function function) throws IOException {
        if (array.length == 0) {
            throw reportRuntimeError("msg.shell.readFile.bad.args");
        }
        final String string = ScriptRuntime.toString(array[0]);
        String string2 = null;
        if (array.length >= 2) {
            string2 = ScriptRuntime.toString(array[1]);
        }
        return readUrl(string, string2, true);
    }
    
    private static String readReader(final Reader reader) throws IOException {
        return readReader(reader, 4096);
    }
    
    private static String readReader(final Reader reader, int n) throws IOException {
        char[] array = new char[n];
        n = 0;
        while (true) {
            final int read = reader.read(array, n, array.length - n);
            if (read < 0) {
                break;
            }
            n += read;
            char[] array2 = array;
            if (n == array.length) {
                array2 = new char[array.length * 2];
                System.arraycopy(array, 0, array2, 0, n);
            }
            array = array2;
        }
        return new String(array, 0, n);
    }
    
    public static Object readUrl(final Context context, final Scriptable scriptable, final Object[] array, final Function function) throws IOException {
        if (array.length == 0) {
            throw reportRuntimeError("msg.shell.readUrl.bad.args");
        }
        final String string = ScriptRuntime.toString(array[0]);
        String string2 = null;
        if (array.length >= 2) {
            string2 = ScriptRuntime.toString(array[1]);
        }
        return readUrl(string, string2, false);
    }
    
    private static String readUrl(final String s, String reader, final boolean b) throws IOException {
        final String s2 = null;
        String s3;
        URLConnection openConnection;
        InputStream inputStream;
        int contentLength;
        String contentType;
        InputStreamReader inputStreamReader;
        File file;
        StringBuilder sb;
        StringBuilder sb2;
        long length;
        StringBuilder sb3;
        Block_9_Outer:Block_14_Outer:Label_0393_Outer:
        while (true) {
            if (!b) {
                s3 = s2;
                while (true) {
                    try {
                        openConnection = new URL(s).openConnection();
                        s3 = s2;
                        inputStream = (InputStream)(s3 = (String)openConnection.getInputStream());
                        if ((contentLength = openConnection.getContentLength()) <= 0) {
                            contentLength = 1024;
                        }
                        if ((s3 = reader) == null) {
                            s3 = (String)inputStream;
                            contentType = openConnection.getContentType();
                            s3 = reader;
                            if (contentType != null) {
                                s3 = (String)inputStream;
                                reader = (s3 = getCharCodingFromType(contentType));
                            }
                        }
                        reader = s3;
                        // iftrue(Label_0324:, !false)
                        // iftrue(Label_0172:, file.exists())
                        // iftrue(Label_0328:, contentLength != 0)
                    Block_7:
                        while (true) {
                            while (true) {
                                if (reader == null) {
                                    s3 = (String)inputStream;
                                    inputStreamReader = new InputStreamReader(inputStream);
                                }
                                else {
                                    s3 = (String)inputStream;
                                    inputStreamReader = new InputStreamReader(inputStream, reader);
                                }
                                s3 = (String)inputStream;
                                reader = readReader(inputStreamReader, contentLength);
                                if (inputStream != null) {
                                    inputStream.close();
                                }
                                return reader;
                                throw new NullPointerException();
                                Label_0328: {
                                    s3 = s2;
                                }
                                inputStream = new FileInputStream(file);
                                continue Block_9_Outer;
                            }
                            s3 = s2;
                            file = new File(s);
                            s3 = s2;
                            break Block_7;
                            Label_0308: {
                                continue Block_9_Outer;
                            }
                        }
                        s3 = s2;
                        sb = new StringBuilder();
                        s3 = s2;
                        sb.append("File not found: ");
                        s3 = s2;
                        sb.append(s);
                        s3 = s2;
                        throw new FileNotFoundException(sb.toString());
                        // iftrue(Label_0308:, (long)contentLength == length)
                    Block_8_Outer:
                        while (true) {
                            while (true) {
                                s3 = s2;
                                sb2 = new StringBuilder();
                                s3 = s2;
                                sb2.append("Too big file size: ");
                                s3 = s2;
                                sb2.append(length);
                                s3 = s2;
                                throw new IOException(sb2.toString());
                                ((InputStream)s3).close();
                                Label_0403: {
                                    s3 = s2;
                                }
                                length = file.length();
                                contentLength = (int)length;
                                continue Block_14_Outer;
                            }
                            while (true) {
                                s3 = s2;
                                sb3 = new StringBuilder();
                                s3 = s2;
                                sb3.append("Cannot read file: ");
                                s3 = s2;
                                sb3.append(s);
                                s3 = s2;
                                throw new IOException(sb3.toString());
                                Label_0324: {
                                    return "";
                                }
                                Label_0172:
                                s3 = s2;
                                continue Label_0393_Outer;
                            }
                            continue Block_8_Outer;
                        }
                    }
                    // iftrue(Label_0403:, file.canRead())
                    // iftrue(Label_0403:, s3 == null)
                    finally {}
                    continue;
                }
            }
            continue;
        }
    }
    
    public static Object readline(final Context context, final Scriptable scriptable, final Object[] array, final Function function) throws IOException {
        final Global instance = getInstance(function);
        if (array.length > 0) {
            return instance.console.readLine(Context.toString(array[0]));
        }
        return instance.console.readLine();
    }
    
    static RuntimeException reportRuntimeError(final String s) {
        return Context.reportRuntimeError(ToolErrorReporter.getMessage(s));
    }
    
    static RuntimeException reportRuntimeError(final String s, final String s2) {
        return Context.reportRuntimeError(ToolErrorReporter.getMessage(s, s2));
    }
    
    public static Object runCommand(final Context context, final Scriptable scriptable, final Object[] array, final Function function) throws IOException {
        int length = array.length;
        if (length != 0 && (length != 1 || !(array[0] instanceof Scriptable))) {
            final File file = null;
            final File file2 = null;
            final InputStream inputStream = null;
            final InputStream inputStream2 = null;
            OutputStream outputStream = null;
            final OutputStream outputStream2 = null;
            OutputStream outputStream3 = null;
            final OutputStream outputStream4 = null;
            final ByteArrayOutputStream byteArrayOutputStream = null;
            final ByteArrayOutputStream byteArrayOutputStream2 = null;
            String[] array2 = null;
            Object[] array3 = null;
            final Object[] array4 = null;
            ByteArrayOutputStream byteArrayOutputStream3;
            String[] array7;
            Object o4;
            ByteArrayOutputStream byteArrayOutputStream5;
            File file5;
            InputStream inputStream6;
            Scriptable scriptable4;
            Object o5;
            if (array[length - 1] instanceof Scriptable) {
                final Scriptable scriptable2 = (Scriptable)array[length - 1];
                final int n = length - 1;
                final Object property = ScriptableObject.getProperty(scriptable2, "env");
                File file3;
                InputStream inputStream3;
                if (property != Scriptable.NOT_FOUND) {
                    if (property == null) {
                        array2 = new String[0];
                        file3 = null;
                        inputStream3 = null;
                    }
                    else {
                        if (!(property instanceof Scriptable)) {
                            throw reportRuntimeError("msg.runCommand.bad.env");
                        }
                        final Scriptable scriptable3 = (Scriptable)property;
                        final Object[] propertyIds = ScriptableObject.getPropertyIds(scriptable3);
                        final String[] array5 = new String[propertyIds.length];
                        int i = 0;
                        final InputStream inputStream4 = inputStream2;
                        file3 = file2;
                        for (Object[] array6 = propertyIds; i != array6.length; ++i) {
                            final Object o = array6[i];
                            String string;
                            Object o2;
                            if (o instanceof String) {
                                string = (String)o;
                                o2 = ScriptableObject.getProperty(scriptable3, string);
                            }
                            else {
                                final int intValue = ((Number)o).intValue();
                                string = Integer.toString(intValue);
                                o2 = ScriptableObject.getProperty(scriptable3, intValue);
                            }
                            Object instance = o2;
                            if (o2 == ScriptableObject.NOT_FOUND) {
                                instance = Undefined.instance;
                            }
                            final StringBuilder sb = new StringBuilder();
                            sb.append(string);
                            sb.append('=');
                            sb.append(ScriptRuntime.toString(instance));
                            array5[i] = sb.toString();
                        }
                        inputStream3 = inputStream4;
                        array2 = array5;
                    }
                }
                else {
                    file3 = null;
                    inputStream3 = null;
                }
                final Object property2 = ScriptableObject.getProperty(scriptable2, "dir");
                File file4;
                if (property2 != Scriptable.NOT_FOUND) {
                    file4 = new File(ScriptRuntime.toString(property2));
                }
                else {
                    file4 = file3;
                }
                final Object property3 = ScriptableObject.getProperty(scriptable2, "input");
                InputStream inputStream5;
                if (property3 != Scriptable.NOT_FOUND) {
                    inputStream5 = toInputStream(property3);
                }
                else {
                    inputStream5 = inputStream3;
                }
                final Object property4 = ScriptableObject.getProperty(scriptable2, "output");
                outputStream = outputStream2;
                byteArrayOutputStream3 = byteArrayOutputStream;
                if (property4 != Scriptable.NOT_FOUND) {
                    final OutputStream outputStream5 = outputStream = toOutputStream(property4);
                    byteArrayOutputStream3 = byteArrayOutputStream;
                    if (outputStream5 == null) {
                        byteArrayOutputStream3 = (ByteArrayOutputStream)(outputStream = new ByteArrayOutputStream());
                    }
                }
                final Object property5 = ScriptableObject.getProperty(scriptable2, "err");
                outputStream3 = outputStream4;
                ByteArrayOutputStream byteArrayOutputStream4 = byteArrayOutputStream2;
                if (property5 != Scriptable.NOT_FOUND) {
                    outputStream3 = toOutputStream(property5);
                    byteArrayOutputStream4 = byteArrayOutputStream2;
                    if (outputStream3 == null) {
                        byteArrayOutputStream4 = (ByteArrayOutputStream)(outputStream3 = new ByteArrayOutputStream());
                    }
                }
                final Object property6 = ScriptableObject.getProperty(scriptable2, "args");
                Object[] elements;
                if (property6 != Scriptable.NOT_FOUND) {
                    elements = context.getElements(Context.toObject(property6, ScriptableObject.getTopLevelScope(scriptable)));
                }
                else {
                    elements = array4;
                }
                array7 = array2;
                length = n;
                final Object o3 = property4;
                o4 = property5;
                byteArrayOutputStream5 = byteArrayOutputStream4;
                file5 = file4;
                inputStream6 = inputStream5;
                scriptable4 = scriptable2;
                o5 = o3;
                array3 = elements;
            }
            else {
                byteArrayOutputStream3 = null;
                array7 = null;
                scriptable4 = null;
                o5 = null;
                o4 = null;
                byteArrayOutputStream5 = null;
                inputStream6 = inputStream;
                file5 = file;
            }
            final Global instance2 = getInstance(function);
            OutputStream outputStream6 = outputStream;
            if (outputStream == null) {
                PrintStream printStream;
                if (instance2 != null) {
                    printStream = instance2.getOut();
                }
                else {
                    printStream = System.out;
                }
                outputStream6 = printStream;
            }
            OutputStream outputStream7;
            if ((outputStream7 = outputStream3) == null) {
                if (instance2 != null) {
                    outputStream7 = instance2.getErr();
                }
                else {
                    outputStream7 = System.err;
                }
            }
            int n2;
            if (array3 == null) {
                n2 = length;
            }
            else {
                n2 = array3.length + length;
            }
            final String[] array8 = new String[n2];
            for (int j = 0; j != length; ++j) {
                array8[j] = ScriptRuntime.toString(array[j]);
            }
            if (array3 != null) {
                for (int k = 0; k != array3.length; ++k) {
                    array8[length + k] = ScriptRuntime.toString(array3[k]);
                }
            }
            final int runProcess = runProcess(array8, array7, file5, inputStream6, outputStream6, outputStream7);
            if (byteArrayOutputStream3 != null) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(ScriptRuntime.toString(o5));
                sb2.append(byteArrayOutputStream3.toString());
                ScriptableObject.putProperty(scriptable4, "output", sb2.toString());
            }
            if (byteArrayOutputStream5 != null) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append(ScriptRuntime.toString(o4));
                sb3.append(byteArrayOutputStream5.toString());
                ScriptableObject.putProperty(scriptable4, "err", sb3.toString());
            }
            return new Integer(runProcess);
        }
        throw reportRuntimeError("msg.runCommand.bad.args");
    }
    
    private static int runProcess(String[] o, String[] array, File file, InputStream inputStream, final OutputStream outputStream, final OutputStream outputStream2) throws IOException {
        if (array == null) {
            o = Runtime.getRuntime().exec((String[])o, null, file);
        }
        else {
            o = Runtime.getRuntime().exec((String[])o, array, file);
        }
        array = null;
        Label_0054: {
            if (inputStream == null) {
                break Label_0054;
            }
            while (true) {
                try {
                    array = (String[])(Object)new PipeThread(false, inputStream, ((Process)o).getOutputStream());
                    ((Thread)(Object)array).start();
                    while (true) {
                        file = null;
                        if (outputStream != null) {
                            file = (File)new PipeThread(true, ((Process)o).getInputStream(), outputStream);
                            ((Thread)file).start();
                        }
                        else {
                            ((Process)o).getInputStream().close();
                        }
                        inputStream = null;
                        if (outputStream2 != null) {
                            inputStream = (InputStream)new PipeThread(true, ((Process)o).getErrorStream(), outputStream2);
                            ((Thread)inputStream).start();
                        }
                        else {
                            ((Process)o).getErrorStream().close();
                        }
                        while (true) {
                            try {
                                ((Process)o).waitFor();
                                if (file != null) {
                                    ((Thread)file).join();
                                }
                                if (array != null) {
                                    ((Thread)(Object)array).join();
                                }
                                if (inputStream != null) {
                                    ((Thread)inputStream).join();
                                }
                                final int exitValue = ((Process)o).exitValue();
                                ((Process)o).destroy();
                                return exitValue;
                            }
                            catch (InterruptedException ex) {
                                continue;
                            }
                            break;
                        }
                        ((Process)o).destroy();
                        throw;
                        ((Process)o).getOutputStream().close();
                        continue;
                    }
                }
                finally {
                    continue;
                }
                break;
            }
        }
    }
    
    public static void seal(final Context context, final Scriptable scriptable, final Object[] array, final Function function) {
        final int n = 0;
        int i = 0;
        while (i != array.length) {
            final Object o = array[i];
            if (o instanceof ScriptableObject && o != Undefined.instance) {
                ++i;
            }
            else {
                if (o instanceof Scriptable && o != Undefined.instance) {
                    throw reportRuntimeError("msg.shell.seal.not.scriptable");
                }
                throw reportRuntimeError("msg.shell.seal.not.object");
            }
        }
        for (int j = n; j != array.length; ++j) {
            ((ScriptableObject)array[j]).sealObject();
        }
    }
    
    public static void serialize(final Context context, final Scriptable scriptable, final Object[] array, final Function function) throws IOException {
        if (array.length < 2) {
            throw Context.reportRuntimeError("Expected an object to serialize and a filename to write the serialization to");
        }
        final Object o = array[0];
        final ScriptableOutputStream scriptableOutputStream = new ScriptableOutputStream(new FileOutputStream(Context.toString(array[1])), ScriptableObject.getTopLevelScope(scriptable));
        scriptableOutputStream.writeObject(o);
        scriptableOutputStream.close();
    }
    
    public static Object spawn(final Context context, final Scriptable scriptable, final Object[] array, final Function function) {
        final Scriptable parentScope = function.getParentScope();
        Runner runner;
        if (array.length != 0 && array[0] instanceof Function) {
            Object[] elements = null;
            if (array.length > 1) {
                elements = elements;
                if (array[1] instanceof Scriptable) {
                    elements = context.getElements((Scriptable)array[1]);
                }
            }
            Object[] emptyArgs;
            if ((emptyArgs = elements) == null) {
                emptyArgs = ScriptRuntime.emptyArgs;
            }
            runner = new Runner(parentScope, (Function)array[0], emptyArgs);
        }
        else {
            if (array.length == 0 || !(array[0] instanceof Script)) {
                throw reportRuntimeError("msg.spawn.args");
            }
            runner = new Runner(parentScope, (Script)array[0]);
        }
        runner.factory = context.getFactory();
        final Thread thread = new Thread(runner);
        thread.start();
        return thread;
    }
    
    public static Object sync(final Context context, final Scriptable scriptable, final Object[] array, final Function function) {
        if (array.length >= 1 && array.length <= 2 && array[0] instanceof Function) {
            Object o = null;
            if (array.length == 2) {
                o = o;
                if (array[1] != Undefined.instance) {
                    o = array[1];
                }
            }
            return new Synchronizer((Scriptable)array[0], o);
        }
        throw reportRuntimeError("msg.sync.args");
    }
    
    private static InputStream toInputStream(final Object o) throws IOException {
        final InputStream inputStream = null;
        final String s = null;
        InputStream inputStream2 = inputStream;
        String reader = s;
        if (o instanceof Wrapper) {
            final Object unwrap = ((Wrapper)o).unwrap();
            if (unwrap instanceof InputStream) {
                inputStream2 = (InputStream)unwrap;
                reader = s;
            }
            else if (unwrap instanceof byte[]) {
                inputStream2 = new ByteArrayInputStream((byte[])unwrap);
                reader = s;
            }
            else if (unwrap instanceof Reader) {
                reader = readReader((Reader)unwrap);
                inputStream2 = inputStream;
            }
            else {
                inputStream2 = inputStream;
                reader = s;
                if (unwrap instanceof char[]) {
                    reader = new String((char[])unwrap);
                    inputStream2 = inputStream;
                }
            }
        }
        InputStream inputStream3;
        if ((inputStream3 = inputStream2) == null) {
            String string;
            if ((string = reader) == null) {
                string = ScriptRuntime.toString(o);
            }
            inputStream3 = new ByteArrayInputStream(string.getBytes());
        }
        return inputStream3;
    }
    
    private static OutputStream toOutputStream(Object unwrap) {
        OutputStream outputStream = null;
        if (unwrap instanceof Wrapper) {
            unwrap = ((Wrapper)unwrap).unwrap();
            outputStream = outputStream;
            if (unwrap instanceof OutputStream) {
                outputStream = (OutputStream)unwrap;
            }
        }
        return outputStream;
    }
    
    public static Object toint32(final Context context, final Scriptable scriptable, final Object[] array, final Function function) {
        Object instance;
        if (array.length != 0) {
            instance = array[0];
        }
        else {
            instance = Undefined.instance;
        }
        if (instance instanceof Integer) {
            return instance;
        }
        return ScriptRuntime.wrapInt(ScriptRuntime.toInt32(instance));
    }
    
    public static double version(final Context context, final Scriptable scriptable, final Object[] array, final Function function) {
        final double n = context.getLanguageVersion();
        if (array.length > 0) {
            context.setLanguageVersion((int)Context.toNumber(array[0]));
        }
        return n;
    }
    
    public static Object write(final Context context, final Scriptable scriptable, final Object[] array, final Function function) {
        return doPrint(array, function, false);
    }
    
    public ShellConsole getConsole(final Charset charset) {
        if (!this.loadJLine(charset)) {
            this.console = ShellConsole.getConsole(this.getIn(), this.getErr(), charset);
        }
        return this.console;
    }
    
    public PrintStream getErr() {
        if (this.errStream == null) {
            return System.err;
        }
        return this.errStream;
    }
    
    public InputStream getIn() {
        if (this.inStream == null && !this.attemptedJLineLoad && this.loadJLine(Charset.defaultCharset())) {
            this.inStream = this.console.getIn();
        }
        if (this.inStream == null) {
            return System.in;
        }
        return this.inStream;
    }
    
    public PrintStream getOut() {
        if (this.outStream == null) {
            return System.out;
        }
        return this.outStream;
    }
    
    public String[] getPrompts(final Context context) {
        if (ScriptableObject.hasProperty(this, "prompts")) {
            final Object property = ScriptableObject.getProperty(this, "prompts");
            if (property instanceof Scriptable) {
                final Scriptable scriptable = (Scriptable)property;
                if (ScriptableObject.hasProperty(scriptable, 0) && ScriptableObject.hasProperty(scriptable, 1)) {
                    Object o2;
                    final Object o = o2 = ScriptableObject.getProperty(scriptable, 0);
                    if (o instanceof Function) {
                        o2 = ((Function)o).call(context, this, scriptable, new Object[0]);
                    }
                    this.prompts[0] = Context.toString(o2);
                    Object o4;
                    final Object o3 = o4 = ScriptableObject.getProperty(scriptable, 1);
                    if (o3 instanceof Function) {
                        o4 = ((Function)o3).call(context, this, scriptable, new Object[0]);
                    }
                    this.prompts[1] = Context.toString(o4);
                }
            }
        }
        return this.prompts;
    }
    
    public void init(final Context context) {
        this.initStandardObjects(context, this.sealedStdLib);
        this.defineFunctionProperties(new String[] { "defineClass", "deserialize", "doctest", "gc", "help", "load", "loadClass", "print", "quit", "readline", "readFile", "readUrl", "runCommand", "seal", "serialize", "spawn", "sync", "toint32", "version", "write" }, Global.class, 2);
        Environment.defineClass(this);
        this.defineProperty("environment", new Environment(this), 2);
        this.defineProperty("history", this.history = (NativeArray)context.newArray(this, 0), 2);
        this.initialized = true;
    }
    
    public void init(final ContextFactory contextFactory) {
        contextFactory.call(new ContextAction() {
            @Override
            public Object run(final Context context) {
                Global.this.init(context);
                return null;
            }
        });
    }
    
    public void initQuitAction(final QuitAction quitAction) {
        if (quitAction == null) {
            throw new IllegalArgumentException("quitAction is null");
        }
        if (this.quitAction != null) {
            throw new IllegalArgumentException("The method is once-call.");
        }
        this.quitAction = quitAction;
    }
    
    public Require installRequire(final Context context, final List<String> list, final boolean sandboxed) {
        final RequireBuilder requireBuilder = new RequireBuilder();
        requireBuilder.setSandboxed(sandboxed);
        final ArrayList<URI> list2 = new ArrayList<URI>();
        if (list != null) {
            for (final String s : list) {
                try {
                    URI resolve;
                    if (!(resolve = new URI(s)).isAbsolute()) {
                        resolve = new File(s).toURI().resolve("");
                    }
                    URI uri = resolve;
                    if (!resolve.toString().endsWith("/")) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append(resolve);
                        sb.append("/");
                        uri = new URI(sb.toString());
                    }
                    list2.add(uri);
                    continue;
                }
                catch (URISyntaxException ex) {
                    throw new RuntimeException(ex);
                }
                break;
            }
        }
        requireBuilder.setModuleScriptProvider(new SoftCachingModuleScriptProvider(new UrlModuleSourceProvider(list2, null)));
        final Require require = requireBuilder.createRequire(context, this);
        require.install(this);
        return require;
    }
    
    public boolean isInitialized() {
        return this.initialized;
    }
    
    public int runDoctest(final Context p0, final Scriptable p1, final String p2, final String p3, final int p4) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: new             Ljava/util/HashMap;
        //     4: dup            
        //     5: invokespecial   java/util/HashMap.<init>:()V
        //     8: putfield        org/mozilla/javascript/tools/shell/Global.doctestCanonicalizations:Ljava/util/HashMap;
        //    11: aload_3        
        //    12: ldc_w           "\r\n?|\n"
        //    15: invokevirtual   java/lang/String.split:(Ljava/lang/String;)[Ljava/lang/String;
        //    18: astore          10
        //    20: aload_0        
        //    21: getfield        org/mozilla/javascript/tools/shell/Global.prompts:[Ljava/lang/String;
        //    24: iconst_0       
        //    25: aaload         
        //    26: invokevirtual   java/lang/String.trim:()Ljava/lang/String;
        //    29: astore          13
        //    31: aload_0        
        //    32: getfield        org/mozilla/javascript/tools/shell/Global.prompts:[Ljava/lang/String;
        //    35: iconst_1       
        //    36: aaload         
        //    37: invokevirtual   java/lang/String.trim:()Ljava/lang/String;
        //    40: astore          14
        //    42: iconst_0       
        //    43: istore          9
        //    45: iconst_0       
        //    46: istore          6
        //    48: iload           9
        //    50: istore          7
        //    52: iload           6
        //    54: istore          8
        //    56: aload           10
        //    58: astore_3       
        //    59: iload           6
        //    61: aload           10
        //    63: arraylength    
        //    64: if_icmpge       103
        //    67: iload           9
        //    69: istore          7
        //    71: iload           6
        //    73: istore          8
        //    75: aload           10
        //    77: astore_3       
        //    78: aload           10
        //    80: iload           6
        //    82: aaload         
        //    83: invokevirtual   java/lang/String.trim:()Ljava/lang/String;
        //    86: aload           13
        //    88: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //    91: ifne            103
        //    94: iload           6
        //    96: iconst_1       
        //    97: iadd           
        //    98: istore          6
        //   100: goto            48
        //   103: iload           8
        //   105: aload_3        
        //   106: arraylength    
        //   107: if_icmpge       832
        //   110: aload_3        
        //   111: iload           8
        //   113: aaload         
        //   114: invokevirtual   java/lang/String.trim:()Ljava/lang/String;
        //   117: aload           13
        //   119: invokevirtual   java/lang/String.length:()I
        //   122: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //   125: astore          10
        //   127: new             Ljava/lang/StringBuilder;
        //   130: dup            
        //   131: invokespecial   java/lang/StringBuilder.<init>:()V
        //   134: astore          11
        //   136: aload           11
        //   138: aload           10
        //   140: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   143: pop            
        //   144: aload           11
        //   146: ldc             "\n"
        //   148: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   151: pop            
        //   152: aload           11
        //   154: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   157: astore          10
        //   159: iload           8
        //   161: iconst_1       
        //   162: iadd           
        //   163: istore          6
        //   165: iload           6
        //   167: aload_3        
        //   168: arraylength    
        //   169: if_icmpge       273
        //   172: aload_3        
        //   173: iload           6
        //   175: aaload         
        //   176: invokevirtual   java/lang/String.trim:()Ljava/lang/String;
        //   179: aload           14
        //   181: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //   184: ifeq            273
        //   187: new             Ljava/lang/StringBuilder;
        //   190: dup            
        //   191: invokespecial   java/lang/StringBuilder.<init>:()V
        //   194: astore          11
        //   196: aload           11
        //   198: aload           10
        //   200: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   203: pop            
        //   204: aload           11
        //   206: aload_3        
        //   207: iload           6
        //   209: aaload         
        //   210: invokevirtual   java/lang/String.trim:()Ljava/lang/String;
        //   213: aload           14
        //   215: invokevirtual   java/lang/String.length:()I
        //   218: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //   221: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   224: pop            
        //   225: aload           11
        //   227: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   230: astore          10
        //   232: new             Ljava/lang/StringBuilder;
        //   235: dup            
        //   236: invokespecial   java/lang/StringBuilder.<init>:()V
        //   239: astore          11
        //   241: aload           11
        //   243: aload           10
        //   245: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   248: pop            
        //   249: aload           11
        //   251: ldc             "\n"
        //   253: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   256: pop            
        //   257: aload           11
        //   259: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   262: astore          10
        //   264: iload           6
        //   266: iconst_1       
        //   267: iadd           
        //   268: istore          6
        //   270: goto            165
        //   273: ldc_w           ""
        //   276: astore          11
        //   278: iload           6
        //   280: aload_3        
        //   281: arraylength    
        //   282: if_icmpge       351
        //   285: aload_3        
        //   286: iload           6
        //   288: aaload         
        //   289: invokevirtual   java/lang/String.trim:()Ljava/lang/String;
        //   292: aload           13
        //   294: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //   297: ifne            351
        //   300: new             Ljava/lang/StringBuilder;
        //   303: dup            
        //   304: invokespecial   java/lang/StringBuilder.<init>:()V
        //   307: astore          12
        //   309: aload           12
        //   311: aload           11
        //   313: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   316: pop            
        //   317: aload           12
        //   319: aload_3        
        //   320: iload           6
        //   322: aaload         
        //   323: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   326: pop            
        //   327: aload           12
        //   329: ldc             "\n"
        //   331: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   334: pop            
        //   335: aload           12
        //   337: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   340: astore          11
        //   342: iload           6
        //   344: iconst_1       
        //   345: iadd           
        //   346: istore          6
        //   348: goto            278
        //   351: aload_0        
        //   352: invokevirtual   org/mozilla/javascript/tools/shell/Global.getOut:()Ljava/io/PrintStream;
        //   355: astore          17
        //   357: aload_0        
        //   358: invokevirtual   org/mozilla/javascript/tools/shell/Global.getErr:()Ljava/io/PrintStream;
        //   361: astore          18
        //   363: new             Ljava/io/ByteArrayOutputStream;
        //   366: dup            
        //   367: invokespecial   java/io/ByteArrayOutputStream.<init>:()V
        //   370: astore          15
        //   372: new             Ljava/io/ByteArrayOutputStream;
        //   375: dup            
        //   376: invokespecial   java/io/ByteArrayOutputStream.<init>:()V
        //   379: astore          16
        //   381: aload_0        
        //   382: new             Ljava/io/PrintStream;
        //   385: dup            
        //   386: aload           15
        //   388: invokespecial   java/io/PrintStream.<init>:(Ljava/io/OutputStream;)V
        //   391: invokevirtual   org/mozilla/javascript/tools/shell/Global.setOut:(Ljava/io/PrintStream;)V
        //   394: aload_0        
        //   395: new             Ljava/io/PrintStream;
        //   398: dup            
        //   399: aload           16
        //   401: invokespecial   java/io/PrintStream.<init>:(Ljava/io/OutputStream;)V
        //   404: invokevirtual   org/mozilla/javascript/tools/shell/Global.setErr:(Ljava/io/PrintStream;)V
        //   407: aload_1        
        //   408: invokevirtual   org/mozilla/javascript/Context.getErrorReporter:()Lorg/mozilla/javascript/ErrorReporter;
        //   411: astore          19
        //   413: aload_1        
        //   414: new             Lorg/mozilla/javascript/tools/ToolErrorReporter;
        //   417: dup            
        //   418: iconst_0       
        //   419: aload_0        
        //   420: invokevirtual   org/mozilla/javascript/tools/shell/Global.getErr:()Ljava/io/PrintStream;
        //   423: invokespecial   org/mozilla/javascript/tools/ToolErrorReporter.<init>:(ZLjava/io/PrintStream;)V
        //   426: invokevirtual   org/mozilla/javascript/Context.setErrorReporter:(Lorg/mozilla/javascript/ErrorReporter;)Lorg/mozilla/javascript/ErrorReporter;
        //   429: pop            
        //   430: aload_1        
        //   431: aload_2        
        //   432: aload           10
        //   434: ldc_w           "doctest input"
        //   437: iconst_1       
        //   438: aconst_null    
        //   439: invokevirtual   org/mozilla/javascript/Context.evaluateString:(Lorg/mozilla/javascript/Scriptable;Ljava/lang/String;Ljava/lang/String;ILjava/lang/Object;)Ljava/lang/Object;
        //   442: astore          12
        //   444: invokestatic    org/mozilla/javascript/Context.getUndefinedValue:()Ljava/lang/Object;
        //   447: astore          20
        //   449: aload           12
        //   451: aload           20
        //   453: if_acmpeq       497
        //   456: aload           12
        //   458: instanceof      Lorg/mozilla/javascript/Function;
        //   461: ifeq            478
        //   464: aload           10
        //   466: invokevirtual   java/lang/String.trim:()Ljava/lang/String;
        //   469: ldc_w           "function"
        //   472: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //   475: ifne            497
        //   478: aload           12
        //   480: invokestatic    org/mozilla/javascript/Context.toString:(Ljava/lang/Object;)Ljava/lang/String;
        //   483: astore          12
        //   485: goto            502
        //   488: astore_2       
        //   489: goto            770
        //   492: astore          12
        //   494: goto            579
        //   497: ldc_w           ""
        //   500: astore          12
        //   502: aload_0        
        //   503: aload           17
        //   505: invokevirtual   org/mozilla/javascript/tools/shell/Global.setOut:(Ljava/io/PrintStream;)V
        //   508: aload_0        
        //   509: aload           18
        //   511: invokevirtual   org/mozilla/javascript/tools/shell/Global.setErr:(Ljava/io/PrintStream;)V
        //   514: aload_1        
        //   515: aload           19
        //   517: invokevirtual   org/mozilla/javascript/Context.setErrorReporter:(Lorg/mozilla/javascript/ErrorReporter;)Lorg/mozilla/javascript/ErrorReporter;
        //   520: pop            
        //   521: new             Ljava/lang/StringBuilder;
        //   524: dup            
        //   525: invokespecial   java/lang/StringBuilder.<init>:()V
        //   528: astore          17
        //   530: aload           17
        //   532: aload           12
        //   534: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   537: pop            
        //   538: aload           17
        //   540: aload           16
        //   542: invokevirtual   java/io/ByteArrayOutputStream.toString:()Ljava/lang/String;
        //   545: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   548: pop            
        //   549: aload           17
        //   551: aload           15
        //   553: invokevirtual   java/io/ByteArrayOutputStream.toString:()Ljava/lang/String;
        //   556: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   559: pop            
        //   560: aload           17
        //   562: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   565: astore          12
        //   567: goto            654
        //   570: astore_2       
        //   571: goto            770
        //   574: astore          12
        //   576: goto            579
        //   579: aload_1        
        //   580: invokevirtual   org/mozilla/javascript/Context.getErrorReporter:()Lorg/mozilla/javascript/ErrorReporter;
        //   583: aload           12
        //   585: invokestatic    org/mozilla/javascript/tools/ToolErrorReporter.reportException:(Lorg/mozilla/javascript/ErrorReporter;Lorg/mozilla/javascript/RhinoException;)V
        //   588: aload_0        
        //   589: aload           17
        //   591: invokevirtual   org/mozilla/javascript/tools/shell/Global.setOut:(Ljava/io/PrintStream;)V
        //   594: aload_0        
        //   595: aload           18
        //   597: invokevirtual   org/mozilla/javascript/tools/shell/Global.setErr:(Ljava/io/PrintStream;)V
        //   600: aload_1        
        //   601: aload           19
        //   603: invokevirtual   org/mozilla/javascript/Context.setErrorReporter:(Lorg/mozilla/javascript/ErrorReporter;)Lorg/mozilla/javascript/ErrorReporter;
        //   606: pop            
        //   607: new             Ljava/lang/StringBuilder;
        //   610: dup            
        //   611: invokespecial   java/lang/StringBuilder.<init>:()V
        //   614: astore          12
        //   616: aload           12
        //   618: ldc_w           ""
        //   621: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   624: pop            
        //   625: aload           12
        //   627: aload           16
        //   629: invokevirtual   java/io/ByteArrayOutputStream.toString:()Ljava/lang/String;
        //   632: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   635: pop            
        //   636: aload           12
        //   638: aload           15
        //   640: invokevirtual   java/io/ByteArrayOutputStream.toString:()Ljava/lang/String;
        //   643: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   646: pop            
        //   647: aload           12
        //   649: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   652: astore          12
        //   654: aload_0        
        //   655: aload           11
        //   657: aload           12
        //   659: invokespecial   org/mozilla/javascript/tools/shell/Global.doctestOutputMatches:(Ljava/lang/String;Ljava/lang/String;)Z
        //   662: ifne            756
        //   665: new             Ljava/lang/StringBuilder;
        //   668: dup            
        //   669: invokespecial   java/lang/StringBuilder.<init>:()V
        //   672: astore_1       
        //   673: aload_1        
        //   674: ldc_w           "doctest failure running:\n"
        //   677: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   680: pop            
        //   681: aload_1        
        //   682: aload           10
        //   684: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   687: pop            
        //   688: aload_1        
        //   689: ldc_w           "expected: "
        //   692: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   695: pop            
        //   696: aload_1        
        //   697: aload           11
        //   699: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   702: pop            
        //   703: aload_1        
        //   704: ldc_w           "actual: "
        //   707: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   710: pop            
        //   711: aload_1        
        //   712: aload           12
        //   714: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   717: pop            
        //   718: aload_1        
        //   719: ldc             "\n"
        //   721: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   724: pop            
        //   725: aload_1        
        //   726: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   729: astore_1       
        //   730: aload           4
        //   732: ifnull          751
        //   735: aload_1        
        //   736: aload           4
        //   738: iload           5
        //   740: iload           6
        //   742: iadd           
        //   743: iconst_1       
        //   744: isub           
        //   745: aconst_null    
        //   746: iconst_0       
        //   747: invokestatic    org/mozilla/javascript/Context.reportRuntimeError:(Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;I)Lorg/mozilla/javascript/EvaluatorException;
        //   750: athrow         
        //   751: aload_1        
        //   752: invokestatic    org/mozilla/javascript/Context.reportRuntimeError:(Ljava/lang/String;)Lorg/mozilla/javascript/EvaluatorException;
        //   755: athrow         
        //   756: iload           7
        //   758: iconst_1       
        //   759: iadd           
        //   760: istore          7
        //   762: iload           6
        //   764: istore          8
        //   766: goto            103
        //   769: astore_2       
        //   770: aload_0        
        //   771: aload           17
        //   773: invokevirtual   org/mozilla/javascript/tools/shell/Global.setOut:(Ljava/io/PrintStream;)V
        //   776: aload_0        
        //   777: aload           18
        //   779: invokevirtual   org/mozilla/javascript/tools/shell/Global.setErr:(Ljava/io/PrintStream;)V
        //   782: aload_1        
        //   783: aload           19
        //   785: invokevirtual   org/mozilla/javascript/Context.setErrorReporter:(Lorg/mozilla/javascript/ErrorReporter;)Lorg/mozilla/javascript/ErrorReporter;
        //   788: pop            
        //   789: new             Ljava/lang/StringBuilder;
        //   792: dup            
        //   793: invokespecial   java/lang/StringBuilder.<init>:()V
        //   796: astore_1       
        //   797: aload_1        
        //   798: ldc_w           ""
        //   801: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   804: pop            
        //   805: aload_1        
        //   806: aload           16
        //   808: invokevirtual   java/io/ByteArrayOutputStream.toString:()Ljava/lang/String;
        //   811: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   814: pop            
        //   815: aload_1        
        //   816: aload           15
        //   818: invokevirtual   java/io/ByteArrayOutputStream.toString:()Ljava/lang/String;
        //   821: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   824: pop            
        //   825: aload_1        
        //   826: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   829: pop            
        //   830: aload_2        
        //   831: athrow         
        //   832: iload           7
        //   834: ireturn        
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                   
        //  -----  -----  -----  -----  ---------------------------------------
        //  430    449    574    579    Lorg/mozilla/javascript/RhinoException;
        //  430    449    570    574    Any
        //  456    478    492    497    Lorg/mozilla/javascript/RhinoException;
        //  456    478    488    492    Any
        //  478    485    492    497    Lorg/mozilla/javascript/RhinoException;
        //  478    485    488    492    Any
        //  579    588    769    770    Any
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
    
    public void setErr(final PrintStream errStream) {
        this.errStream = errStream;
    }
    
    public void setIn(final InputStream inStream) {
        this.inStream = inStream;
    }
    
    public void setOut(final PrintStream outStream) {
        this.outStream = outStream;
    }
    
    public void setSealedStdLib(final boolean sealedStdLib) {
        this.sealedStdLib = sealedStdLib;
    }
}
