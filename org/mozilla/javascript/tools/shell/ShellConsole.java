package org.mozilla.javascript.tools.shell;

import java.nio.charset.*;
import org.mozilla.javascript.*;
import java.lang.reflect.*;
import java.io.*;

public abstract class ShellConsole
{
    private static final Class[] BOOLEAN_ARG;
    private static final Class[] CHARSEQ_ARG;
    private static final Class[] NO_ARG;
    private static final Class[] STRING_ARG;
    
    static {
        NO_ARG = new Class[0];
        BOOLEAN_ARG = new Class[] { Boolean.TYPE };
        STRING_ARG = new Class[] { String.class };
        CHARSEQ_ARG = new Class[] { CharSequence.class };
    }
    
    protected ShellConsole() {
    }
    
    public static ShellConsole getConsole(final InputStream inputStream, final PrintStream printStream, final Charset charset) {
        return new SimpleShellConsole(inputStream, printStream, charset);
    }
    
    public static ShellConsole getConsole(final Scriptable scriptable, final Charset charset) {
        ClassLoader classLoader;
        if ((classLoader = ShellConsole.class.getClassLoader()) == null) {
            classLoader = ClassLoader.getSystemClassLoader();
        }
        if (classLoader == null) {
            return null;
        }
        try {
            final Class<?> classOrNull = Kit.classOrNull(classLoader, "jline.console.ConsoleReader");
            if (classOrNull != null) {
                return getJLineShellConsoleV2(classLoader, classOrNull, scriptable, charset);
            }
            final Class<?> classOrNull2 = Kit.classOrNull(classLoader, "jline.ConsoleReader");
            if (classOrNull2 != null) {
                return getJLineShellConsoleV1(classLoader, classOrNull2, scriptable, charset);
            }
        }
        catch (InvocationTargetException ex) {
            return null;
        }
        catch (InstantiationException ex2) {}
        catch (IllegalAccessException ex3) {}
        catch (NoSuchMethodException ex4) {}
        return null;
    }
    
    private static JLineShellConsoleV1 getJLineShellConsoleV1(final ClassLoader classLoader, final Class<?> clazz, final Scriptable scriptable, final Charset charset) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        final Object instance = clazz.getConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
        tryInvoke(instance, "setBellEnabled", ShellConsole.BOOLEAN_ARG, Boolean.FALSE);
        final Class<?> classOrNull = Kit.classOrNull(classLoader, "jline.Completor");
        tryInvoke(instance, "addCompletor", new Class[] { classOrNull }, Proxy.newProxyInstance(classLoader, new Class[] { classOrNull }, new FlexibleCompletor(classOrNull, scriptable)));
        return new JLineShellConsoleV1(instance, charset);
    }
    
    private static JLineShellConsoleV2 getJLineShellConsoleV2(final ClassLoader classLoader, final Class<?> clazz, final Scriptable scriptable, final Charset charset) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        final Object instance = clazz.getConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
        tryInvoke(instance, "setBellEnabled", ShellConsole.BOOLEAN_ARG, Boolean.FALSE);
        final Class<?> classOrNull = Kit.classOrNull(classLoader, "jline.console.completer.Completer");
        tryInvoke(instance, "addCompleter", new Class[] { classOrNull }, Proxy.newProxyInstance(classLoader, new Class[] { classOrNull }, new FlexibleCompletor(classOrNull, scriptable)));
        return new JLineShellConsoleV2(instance, charset);
    }
    
    private static Object tryInvoke(Object invoke, final String s, final Class[] array, final Object... array2) {
        try {
            final Method declaredMethod = invoke.getClass().getDeclaredMethod(s, (Class<?>[])array);
            if (declaredMethod != null) {
                invoke = declaredMethod.invoke(invoke, array2);
                return invoke;
            }
        }
        catch (InvocationTargetException ex) {}
        catch (IllegalAccessException ex2) {}
        catch (IllegalArgumentException ex3) {}
        catch (NoSuchMethodException ex4) {}
        return null;
    }
    
    public abstract void flush() throws IOException;
    
    public abstract InputStream getIn();
    
    public abstract void print(final String p0) throws IOException;
    
    public abstract void println() throws IOException;
    
    public abstract void println(final String p0) throws IOException;
    
    public abstract String readLine() throws IOException;
    
    public abstract String readLine(final String p0) throws IOException;
    
    private static class ConsoleInputStream extends InputStream
    {
        private static final byte[] EMPTY;
        private boolean atEOF;
        private byte[] buffer;
        private final ShellConsole console;
        private final Charset cs;
        private int cursor;
        
        static {
            EMPTY = new byte[0];
        }
        
        public ConsoleInputStream(final ShellConsole console, final Charset cs) {
            this.buffer = ConsoleInputStream.EMPTY;
            this.cursor = -1;
            this.atEOF = false;
            this.console = console;
            this.cs = cs;
        }
        
        private boolean ensureInput() throws IOException {
            if (this.atEOF) {
                return false;
            }
            if (this.cursor < 0 || this.cursor > this.buffer.length) {
                if (this.readNextLine() == -1) {
                    this.atEOF = true;
                    return false;
                }
                this.cursor = 0;
            }
            return true;
        }
        
        private int readNextLine() throws IOException {
            final String line = this.console.readLine(null);
            if (line != null) {
                this.buffer = line.getBytes(this.cs);
                return this.buffer.length;
            }
            this.buffer = ConsoleInputStream.EMPTY;
            return -1;
        }
        
        @Override
        public int read() throws IOException {
            synchronized (this) {
                if (!this.ensureInput()) {
                    return -1;
                }
                if (this.cursor == this.buffer.length) {
                    ++this.cursor;
                    return 10;
                }
                return this.buffer[this.cursor++];
            }
        }
        
        @Override
        public int read(final byte[] array, int n, int n2) throws IOException {
            // monitorenter(this)
            Label_0014: {
                if (array != null) {
                    break Label_0014;
                }
                int n3;
                final Throwable t;
                int min;
                Label_0037_Outer:Label_0135_Outer:Label_0113_Outer:
                while (true) {
                    while (true) {
                        while (true) {
                            try {
                                throw new NullPointerException();
                                // monitorexit(this)
                                // iftrue(Label_0048:, n2 != 0)
                                // iftrue(Label_0063:, this.ensureInput())
                                // monitorexit(this)
                                // monitorexit(this)
                                // monitorexit(this)
                                // iftrue(Label_0127:, n < 0 || n2 < 0 || n2 > array.length - n)
                                while (true) {
                                Label_0079:
                                    while (true) {
                                        Block_7: {
                                            while (true) {
                                                return -1;
                                                n3 = 0;
                                                break Block_7;
                                                Label_0048: {
                                                    continue Label_0037_Outer;
                                                }
                                            }
                                            throw t;
                                            Label_0063: {
                                                min = Math.min(n2, this.buffer.length - this.cursor);
                                            }
                                            break Label_0079;
                                            this.cursor += n;
                                            return n;
                                            array[n + n3] = this.buffer[this.cursor + n3];
                                            ++n3;
                                            break Label_0079;
                                        }
                                        return 0;
                                        Label_0127: {
                                            throw new IndexOutOfBoundsException();
                                        }
                                        continue Label_0135_Outer;
                                    }
                                    continue;
                                }
                            }
                            // iftrue(Label_0143:, n3 >= min)
                            finally {
                                continue Label_0113_Outer;
                            }
                            break;
                        }
                        Label_0143: {
                            if (min < n2) {
                                n2 = min + 1;
                                t[min + n] = 10;
                                n = n2;
                                continue;
                            }
                        }
                        n = min;
                        continue;
                    }
                }
            }
        }
    }
    
    private static class JLineShellConsoleV1 extends ShellConsole
    {
        private final InputStream in;
        private final Object reader;
        
        JLineShellConsoleV1(final Object reader, final Charset charset) {
            this.reader = reader;
            this.in = new ConsoleInputStream(this, charset);
        }
        
        @Override
        public void flush() throws IOException {
            tryInvoke(this.reader, "flushConsole", ShellConsole.NO_ARG, new Object[0]);
        }
        
        @Override
        public InputStream getIn() {
            return this.in;
        }
        
        @Override
        public void print(final String s) throws IOException {
            tryInvoke(this.reader, "printString", ShellConsole.STRING_ARG, s);
        }
        
        @Override
        public void println() throws IOException {
            tryInvoke(this.reader, "printNewline", ShellConsole.NO_ARG, new Object[0]);
        }
        
        @Override
        public void println(final String s) throws IOException {
            tryInvoke(this.reader, "printString", ShellConsole.STRING_ARG, s);
            tryInvoke(this.reader, "printNewline", ShellConsole.NO_ARG, new Object[0]);
        }
        
        @Override
        public String readLine() throws IOException {
            return (String)tryInvoke(this.reader, "readLine", ShellConsole.NO_ARG, new Object[0]);
        }
        
        @Override
        public String readLine(final String s) throws IOException {
            return (String)tryInvoke(this.reader, "readLine", ShellConsole.STRING_ARG, s);
        }
    }
    
    private static class JLineShellConsoleV2 extends ShellConsole
    {
        private final InputStream in;
        private final Object reader;
        
        JLineShellConsoleV2(final Object reader, final Charset charset) {
            this.reader = reader;
            this.in = new ConsoleInputStream(this, charset);
        }
        
        @Override
        public void flush() throws IOException {
            tryInvoke(this.reader, "flush", ShellConsole.NO_ARG, new Object[0]);
        }
        
        @Override
        public InputStream getIn() {
            return this.in;
        }
        
        @Override
        public void print(final String s) throws IOException {
            tryInvoke(this.reader, "print", ShellConsole.CHARSEQ_ARG, s);
        }
        
        @Override
        public void println() throws IOException {
            tryInvoke(this.reader, "println", ShellConsole.NO_ARG, new Object[0]);
        }
        
        @Override
        public void println(final String s) throws IOException {
            tryInvoke(this.reader, "println", ShellConsole.CHARSEQ_ARG, s);
        }
        
        @Override
        public String readLine() throws IOException {
            return (String)tryInvoke(this.reader, "readLine", ShellConsole.NO_ARG, new Object[0]);
        }
        
        @Override
        public String readLine(final String s) throws IOException {
            return (String)tryInvoke(this.reader, "readLine", ShellConsole.STRING_ARG, s);
        }
    }
    
    private static class SimpleShellConsole extends ShellConsole
    {
        private final InputStream in;
        private final PrintWriter out;
        private final BufferedReader reader;
        
        SimpleShellConsole(final InputStream in, final PrintStream printStream, final Charset charset) {
            this.in = in;
            this.out = new PrintWriter(printStream);
            this.reader = new BufferedReader(new InputStreamReader(in, charset));
        }
        
        @Override
        public void flush() throws IOException {
            this.out.flush();
        }
        
        @Override
        public InputStream getIn() {
            return this.in;
        }
        
        @Override
        public void print(final String s) throws IOException {
            this.out.print(s);
        }
        
        @Override
        public void println() throws IOException {
            this.out.println();
        }
        
        @Override
        public void println(final String s) throws IOException {
            this.out.println(s);
        }
        
        @Override
        public String readLine() throws IOException {
            return this.reader.readLine();
        }
        
        @Override
        public String readLine(final String s) throws IOException {
            if (s != null) {
                this.out.write(s);
                this.out.flush();
            }
            return this.reader.readLine();
        }
    }
}
