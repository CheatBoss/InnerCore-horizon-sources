package com.android.dx.command.dexer;

import java.util.concurrent.atomic.*;
import java.util.zip.*;
import java.util.jar.*;
import com.android.dx.dex.file.*;
import com.android.dx.rop.cst.*;
import com.android.dx.rop.annotation.*;
import com.android.dx.merge.*;
import com.android.dex.*;
import com.android.dx.cf.direct.*;
import java.io.*;
import java.util.concurrent.*;
import com.android.dx.cf.iface.*;
import com.android.dx.dex.cf.*;
import com.android.dx.dex.*;
import com.android.dx.command.*;
import com.android.dex.util.*;
import java.util.*;
import com.android.dx.cf.code.*;

public class Main
{
    private static final Attributes.Name CREATED_BY;
    private static final String DEX_EXTENSION = ".dex";
    private static final String DEX_PREFIX = "classes";
    private static final String IN_RE_CORE_CLASSES = "Ill-advised or mistaken usage of a core class (java.* or javax.*)\nwhen not building a core library.\n\nThis is often due to inadvertently including a core library file\nin your application's project, when using an IDE (such as\nEclipse). If you are sure you're not intentionally defining a\ncore class, then this is the most likely explanation of what's\ngoing on.\n\nHowever, you might actually be trying to define a class in a core\nnamespace, the source of which you may have taken, for example,\nfrom a non-Android virtual machine project. This will most\nassuredly not work. At a minimum, it jeopardizes the\ncompatibility of your app with future versions of the platform.\nIt is also often of questionable legality.\n\nIf you really intend to build a core library -- which is only\nappropriate as part of creating a full virtual machine\ndistribution, as opposed to compiling an application -- then use\nthe \"--core-library\" option to suppress this error message.\n\nIf you go ahead and use \"--core-library\" but are in fact\nbuilding an application, then be forewarned that your application\nwill still fail to build or run, at some point. Please be\nprepared for angry customers who find, for example, that your\napplication ceases to function once they upgrade their operating\nsystem. You will be to blame for this problem.\n\nIf you are legitimately using some code that happens to be in a\ncore package, then the easiest safe alternative you have is to\nrepackage that code. That is, move the classes in question into\nyour own package namespace. This means that they will never be in\nconflict with core system classes. JarJar is a tool that may help\nyou in this endeavor. If you find that you cannot do this, then\nthat is an indication that the path you are on will ultimately\nlead to pain, suffering, grief, and lamentation.\n";
    private static final String[] JAVAX_CORE;
    private static final String MANIFEST_NAME = "META-INF/MANIFEST.MF";
    private static final int MAX_FIELD_ADDED_DURING_DEX_CREATION = 9;
    private static final int MAX_METHOD_ADDED_DURING_DEX_CREATION = 2;
    private static List<Future<Boolean>> addToDexFutures;
    private static volatile boolean anyFilesProcessed;
    private static Arguments args;
    private static ExecutorService classDefItemConsumer;
    private static ExecutorService classTranslatorPool;
    private static Set<String> classesInMainDex;
    private static ExecutorService dexOutPool;
    private static List<byte[]> dexOutputArrays;
    private static List<Future<byte[]>> dexOutputFutures;
    private static Object dexRotationLock;
    private static AtomicInteger errors;
    private static OutputStreamWriter humanOutWriter;
    private static final List<byte[]> libraryDexBuffers;
    private static int maxFieldIdsInProcess;
    private static int maxMethodIdsInProcess;
    private static long minimumFileAge;
    private static DexFile outputDex;
    private static TreeMap<String, byte[]> outputResources;
    
    static {
        CREATED_BY = new Attributes.Name("Created-By");
        JAVAX_CORE = new String[] { "accessibility", "crypto", "imageio", "management", "naming", "net", "print", "rmi", "security", "sip", "sound", "sql", "swing", "transaction", "xml" };
        Main.errors = new AtomicInteger(0);
        libraryDexBuffers = new ArrayList<byte[]>();
        Main.addToDexFutures = new ArrayList<Future<Boolean>>();
        Main.dexOutputFutures = new ArrayList<Future<byte[]>>();
        Main.dexRotationLock = new Object();
        Main.maxMethodIdsInProcess = 0;
        Main.maxFieldIdsInProcess = 0;
        Main.minimumFileAge = 0L;
        Main.classesInMainDex = null;
        Main.dexOutputArrays = new ArrayList<byte[]>();
        Main.humanOutWriter = null;
    }
    
    private Main() {
    }
    
    static /* synthetic */ int access$1812(int maxMethodIdsInProcess) {
        maxMethodIdsInProcess += Main.maxMethodIdsInProcess;
        return Main.maxMethodIdsInProcess = maxMethodIdsInProcess;
    }
    
    static /* synthetic */ int access$1820(int maxMethodIdsInProcess) {
        maxMethodIdsInProcess = Main.maxMethodIdsInProcess - maxMethodIdsInProcess;
        return Main.maxMethodIdsInProcess = maxMethodIdsInProcess;
    }
    
    static /* synthetic */ int access$2012(int maxFieldIdsInProcess) {
        maxFieldIdsInProcess += Main.maxFieldIdsInProcess;
        return Main.maxFieldIdsInProcess = maxFieldIdsInProcess;
    }
    
    static /* synthetic */ int access$2020(int maxFieldIdsInProcess) {
        maxFieldIdsInProcess = Main.maxFieldIdsInProcess - maxFieldIdsInProcess;
        return Main.maxFieldIdsInProcess = maxFieldIdsInProcess;
    }
    
    private static boolean addClassToDex(final ClassDefItem classDefItem) {
        synchronized (Main.outputDex) {
            Main.outputDex.add(classDefItem);
            return true;
        }
    }
    
    private static void checkClassName(final String s) {
        boolean b = false;
        if (s.startsWith("java/")) {
            b = true;
        }
        else if (s.startsWith("javax/")) {
            final int index = s.indexOf(47, 6);
            b = (index == -1 || Arrays.binarySearch(Main.JAVAX_CORE, s.substring(6, index)) >= 0);
        }
        if (!b) {
            return;
        }
        final PrintStream err = DxConsole.err;
        final StringBuilder sb = new StringBuilder();
        sb.append("\ntrouble processing \"");
        sb.append(s);
        sb.append("\":\n\n");
        sb.append("Ill-advised or mistaken usage of a core class (java.* or javax.*)\nwhen not building a core library.\n\nThis is often due to inadvertently including a core library file\nin your application's project, when using an IDE (such as\nEclipse). If you are sure you're not intentionally defining a\ncore class, then this is the most likely explanation of what's\ngoing on.\n\nHowever, you might actually be trying to define a class in a core\nnamespace, the source of which you may have taken, for example,\nfrom a non-Android virtual machine project. This will most\nassuredly not work. At a minimum, it jeopardizes the\ncompatibility of your app with future versions of the platform.\nIt is also often of questionable legality.\n\nIf you really intend to build a core library -- which is only\nappropriate as part of creating a full virtual machine\ndistribution, as opposed to compiling an application -- then use\nthe \"--core-library\" option to suppress this error message.\n\nIf you go ahead and use \"--core-library\" but are in fact\nbuilding an application, then be forewarned that your application\nwill still fail to build or run, at some point. Please be\nprepared for angry customers who find, for example, that your\napplication ceases to function once they upgrade their operating\nsystem. You will be to blame for this problem.\n\nIf you are legitimately using some code that happens to be in a\ncore package, then the easiest safe alternative you have is to\nrepackage that code. That is, move the classes in question into\nyour own package namespace. This means that they will never be in\nconflict with core system classes. JarJar is a tool that may help\nyou in this endeavor. If you find that you cannot do this, then\nthat is an indication that the path you are on will ultimately\nlead to pain, suffering, grief, and lamentation.\n");
        err.println(sb.toString());
        Main.errors.incrementAndGet();
        throw new StopProcessing();
    }
    
    private static void closeOutput(final OutputStream outputStream) throws IOException {
        if (outputStream == null) {
            return;
        }
        outputStream.flush();
        if (outputStream != System.out) {
            outputStream.close();
        }
    }
    
    private static void createDexFile() {
        Main.outputDex = new DexFile(Main.args.dexOptions);
        if (Main.args.dumpWidth != 0) {
            Main.outputDex.setDumpWidth(Main.args.dumpWidth);
        }
    }
    
    private static boolean createJar(String openOutput) {
        try {
            final Manifest manifest = makeManifest();
            openOutput = (String)openOutput(openOutput);
            final JarOutputStream jarOutputStream = new JarOutputStream((OutputStream)openOutput, manifest);
            try {
                for (final Map.Entry<String, byte[]> entry : Main.outputResources.entrySet()) {
                    final String s = entry.getKey();
                    final byte[] array = entry.getValue();
                    final JarEntry jarEntry = new JarEntry(s);
                    final int length = array.length;
                    if (Main.args.verbose) {
                        final PrintStream out = DxConsole.out;
                        final StringBuilder sb = new StringBuilder();
                        sb.append("writing ");
                        sb.append(s);
                        sb.append("; size ");
                        sb.append(length);
                        sb.append("...");
                        out.println(sb.toString());
                    }
                    jarEntry.setSize(length);
                    jarOutputStream.putNextEntry(jarEntry);
                    jarOutputStream.write(array);
                    jarOutputStream.closeEntry();
                }
                return true;
            }
            finally {
                jarOutputStream.finish();
                jarOutputStream.flush();
                closeOutput((OutputStream)openOutput);
            }
        }
        catch (Exception ex) {
            if (Main.args.debug) {
                DxConsole.err.println("\ntrouble writing output:");
                ex.printStackTrace(DxConsole.err);
            }
            else {
                final PrintStream err = DxConsole.err;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("\ntrouble writing output: ");
                sb2.append(ex.getMessage());
                err.println(sb2.toString());
            }
            return false;
        }
    }
    
    private static void dumpMethod(final DexFile dexFile, final String s, final OutputStreamWriter outputStreamWriter) {
        final boolean endsWith = s.endsWith("*");
        final int lastIndex = s.lastIndexOf(46);
        if (lastIndex <= 0 || lastIndex == s.length() - 1) {
            final PrintStream err = DxConsole.err;
            final StringBuilder sb = new StringBuilder();
            sb.append("bogus fully-qualified method name: ");
            sb.append(s);
            err.println(sb.toString());
            return;
        }
        final String replace = s.substring(0, lastIndex).replace('.', '/');
        final String substring = s.substring(lastIndex + 1);
        final ClassDefItem classOrNull = dexFile.getClassOrNull(replace);
        if (classOrNull == null) {
            final PrintStream err2 = DxConsole.err;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("no such class: ");
            sb2.append(replace);
            err2.println(sb2.toString());
            return;
        }
        String substring2 = substring;
        if (endsWith) {
            substring2 = substring.substring(0, substring.length() - 1);
        }
        final ArrayList<EncodedMethod> methods = classOrNull.getMethods();
        final TreeMap<CstNat, EncodedMethod> treeMap = new TreeMap<CstNat, EncodedMethod>();
        for (final EncodedMethod encodedMethod : methods) {
            final String string = encodedMethod.getName().getString();
            if ((endsWith && string.startsWith(substring2)) || (!endsWith && string.equals(substring2))) {
                treeMap.put(encodedMethod.getRef().getNat(), encodedMethod);
            }
        }
        if (treeMap.size() == 0) {
            final PrintStream err3 = DxConsole.err;
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("no such method: ");
            sb3.append(s);
            err3.println(sb3.toString());
            return;
        }
        final PrintWriter printWriter = new PrintWriter(outputStreamWriter);
        for (final EncodedMethod encodedMethod2 : treeMap.values()) {
            encodedMethod2.debugPrint(printWriter, Main.args.verboseDump);
            final CstString sourceFile = classOrNull.getSourceFile();
            if (sourceFile != null) {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("  source file: ");
                sb4.append(sourceFile.toQuoted());
                printWriter.println(sb4.toString());
            }
            final Annotations methodAnnotations = classOrNull.getMethodAnnotations(encodedMethod2.getRef());
            final AnnotationsList parameterAnnotations = classOrNull.getParameterAnnotations(encodedMethod2.getRef());
            if (methodAnnotations != null) {
                printWriter.println("  method annotations:");
                for (final Annotation annotation : methodAnnotations.getAnnotations()) {
                    final StringBuilder sb5 = new StringBuilder();
                    sb5.append("    ");
                    sb5.append(annotation);
                    printWriter.println(sb5.toString());
                }
            }
            if (parameterAnnotations != null) {
                printWriter.println("  parameter annotations:");
                for (int size = parameterAnnotations.size(), i = 0; i < size; ++i) {
                    final StringBuilder sb6 = new StringBuilder();
                    sb6.append("    parameter ");
                    sb6.append(i);
                    printWriter.println(sb6.toString());
                    for (final Annotation annotation2 : parameterAnnotations.get(i).getAnnotations()) {
                        final StringBuilder sb7 = new StringBuilder();
                        sb7.append("      ");
                        sb7.append(annotation2);
                        printWriter.println(sb7.toString());
                    }
                }
            }
        }
        printWriter.flush();
    }
    
    private static String fixPath(final String s) {
        String replace = s;
        if (File.separatorChar == '\\') {
            replace = s.replace('\\', '/');
        }
        final int lastIndex = replace.lastIndexOf("/./");
        if (lastIndex != -1) {
            return replace.substring(lastIndex + 3);
        }
        if (replace.startsWith("./")) {
            return replace.substring(2);
        }
        return replace;
    }
    
    private static String getDexFileName(final int n) {
        if (n == 0) {
            return "classes.dex";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("classes");
        sb.append(n + 1);
        sb.append(".dex");
        return sb.toString();
    }
    
    public static String getTooManyIdsErrorMessage() {
        if (Main.args.multiDex) {
            return "The list of classes given in --main-dex-list is too big and does not fit in the main dex.";
        }
        return "You may try using --multi-dex option.";
    }
    
    public static void main(final String[] array) throws IOException {
        final Arguments arguments = new Arguments();
        arguments.parse(array);
        final int run = run(arguments);
        if (run != 0) {
            System.exit(run);
        }
    }
    
    private static Manifest makeManifest() throws IOException {
        final byte[] array = Main.outputResources.get("META-INF/MANIFEST.MF");
        Manifest manifest;
        Attributes attributes;
        if (array == null) {
            manifest = new Manifest();
            attributes = manifest.getMainAttributes();
            attributes.put(Attributes.Name.MANIFEST_VERSION, "1.0");
        }
        else {
            manifest = new Manifest(new ByteArrayInputStream(array));
            attributes = manifest.getMainAttributes();
            Main.outputResources.remove("META-INF/MANIFEST.MF");
        }
        final String value = attributes.getValue(Main.CREATED_BY);
        String string;
        if (value == null) {
            string = "";
        }
        else {
            final StringBuilder sb = new StringBuilder();
            sb.append(value);
            sb.append(" + ");
            string = sb.toString();
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(string);
        sb2.append("dx 1.11");
        attributes.put(Main.CREATED_BY, sb2.toString());
        attributes.putValue("Dex-Location", "classes.dex");
        return manifest;
    }
    
    private static byte[] mergeIncremental(final byte[] array, final File file) throws IOException {
        Dex dex = null;
        final Dex dex2 = null;
        if (array != null) {
            dex = new Dex(array);
        }
        Dex merge = dex2;
        if (file.exists()) {
            merge = new Dex(file);
        }
        if (dex == null && merge == null) {
            return null;
        }
        if (dex != null) {
            if (merge == null) {
                merge = dex;
            }
            else {
                merge = new DexMerger(new Dex[] { dex, merge }, CollisionPolicy.KEEP_FIRST).merge();
            }
        }
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        merge.writeTo(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    
    private static byte[] mergeLibraryDexBuffers(final byte[] array) throws IOException {
        final ArrayList<Dex> list = new ArrayList<Dex>();
        if (array != null) {
            list.add(new Dex(array));
        }
        final Iterator<byte[]> iterator = Main.libraryDexBuffers.iterator();
        while (iterator.hasNext()) {
            list.add(new Dex(iterator.next()));
        }
        if (list.isEmpty()) {
            return null;
        }
        return new DexMerger(list.toArray(new Dex[list.size()]), CollisionPolicy.FAIL).merge().getBytes();
    }
    
    private static OutputStream openOutput(final String s) throws IOException {
        if (!s.equals("-") && !s.startsWith("-.")) {
            return new FileOutputStream(s);
        }
        return System.out;
    }
    
    private static DirectClassFile parseClass(final String s, final byte[] array) {
        final DirectClassFile directClassFile = new DirectClassFile(array, s, Main.args.cfOptions.strictNameCheck);
        directClassFile.setAttributeFactory(StdAttributeFactory.THE_ONE);
        directClassFile.getMagic();
        return directClassFile;
    }
    
    private static boolean processAllFiles() {
        createDexFile();
        if (Main.args.jarOutput) {
            Main.outputResources = new TreeMap<String, byte[]>();
        }
    Label_0260_Outer:
        while (true) {
            Main.anyFilesProcessed = false;
            final String[] fileNames = Main.args.fileNames;
            Arrays.sort(fileNames);
            Main.classTranslatorPool = new ThreadPoolExecutor(Main.args.numThreads, Main.args.numThreads, 0L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(Main.args.numThreads * 2, true), new ThreadPoolExecutor.CallerRunsPolicy());
            Main.classDefItemConsumer = Executors.newSingleThreadExecutor();
        Label_0260:
            while (true) {
                ClassPathOpener.FileNameFilter fileNameFilter;
                int n;
                int n2;
                int n3;
                Iterator<Future<Boolean>> iterator;
                Future<Boolean> future;
                PrintStream err;
                StringBuilder sb;
                int value;
                PrintStream err2;
                StringBuilder sb2;
                String s;
                Label_0133_Outer:Label_0232_Outer:
                while (true) {
                Label_0232:
                    while (true) {
                    Block_10_Outer:
                        while (true) {
                            Label_0286: {
                                try {
                                    if (Main.args.mainDexListFile == null) {
                                        break Label_0133_Outer;
                                    }
                                    if (Main.args.strictNameCheck) {
                                        fileNameFilter = new MainDexListFilter();
                                        break Label_0286;
                                    }
                                    fileNameFilter = new BestEffortMainDexListFilter();
                                    break Label_0286;
                                    Label_0153: {
                                        throw new DexException("Too many classes in --main-dex-list, main dex capacity exceeded");
                                    }
                                    // iftrue(Label_0175:, Main.dexOutputFutures.size() <= 0)
                                    break Label_0286;
                                    // iftrue(Label_0153:, n >= fileNames.length)
                                    processOne(fileNames[n], fileNameFilter);
                                    ++n;
                                    continue Block_10_Outer;
                                    while (true) {
                                        try {
                                            while (true) {
                                                if (Main.maxMethodIdsInProcess <= 0) {
                                                    if (Main.maxFieldIdsInProcess <= 0) {
                                                        break;
                                                    }
                                                }
                                                try {
                                                    Main.dexRotationLock.wait();
                                                }
                                                catch (InterruptedException ex4) {}
                                            }
                                            // monitorexit(Main.dexRotationLock)
                                            rotateDexFile();
                                            break Block_10_Outer;
                                        }
                                        finally {
                                        }
                                        // monitorexit(Main.dexRotationLock)
                                        break Label_0232;
                                    Block_9:
                                        while (true) {
                                            while (true) {
                                                processOne(fileNames[n2], ClassPathOpener.acceptAll);
                                                ++n2;
                                                break Label_0260;
                                                processOne(fileNames[n3], new NotFilter(fileNameFilter));
                                                ++n3;
                                                break Label_0232;
                                                continue Label_0260_Outer;
                                            }
                                            Label_0175: {
                                                break Block_9;
                                            }
                                            continue;
                                        }
                                        continue Label_0232_Outer;
                                    }
                                }
                                // iftrue(Label_0282:, n2 >= fileNames.length)
                                // iftrue(Label_0657:, !Main.args.minimalMainDex)
                                // iftrue(Label_0662:, n3 >= fileNames.length)
                                // monitorenter(Main.dexRotationLock)
                                catch (StopProcessing stopProcessing) {}
                                while (true) {
                                    while (true) {
                                        Label_0670: {
                                            try {
                                                Main.classTranslatorPool.shutdown();
                                                Main.classTranslatorPool.awaitTermination(600L, TimeUnit.SECONDS);
                                                Main.classDefItemConsumer.shutdown();
                                                Main.classDefItemConsumer.awaitTermination(600L, TimeUnit.SECONDS);
                                                iterator = Main.addToDexFutures.iterator();
                                                if (iterator.hasNext()) {
                                                    future = iterator.next();
                                                    try {
                                                        future.get();
                                                        break Label_0670;
                                                    }
                                                    catch (ExecutionException ex) {
                                                        if (Main.errors.incrementAndGet() < 10) {
                                                            err = DxConsole.err;
                                                            sb = new StringBuilder();
                                                            sb.append("Uncaught translation error: ");
                                                            sb.append(ex.getCause());
                                                            err.println(sb.toString());
                                                            break Label_0670;
                                                        }
                                                        throw new InterruptedException("Too many errors");
                                                    }
                                                }
                                                value = Main.errors.get();
                                                if (value != 0) {
                                                    err2 = DxConsole.err;
                                                    sb2 = new StringBuilder();
                                                    sb2.append(value);
                                                    sb2.append(" error");
                                                    if (value == 1) {
                                                        s = "";
                                                    }
                                                    else {
                                                        s = "s";
                                                    }
                                                    sb2.append(s);
                                                    sb2.append("; aborting");
                                                    err2.println(sb2.toString());
                                                    return false;
                                                }
                                                if (Main.args.incremental && !Main.anyFilesProcessed) {
                                                    return true;
                                                }
                                                if (!Main.anyFilesProcessed && !Main.args.emptyOk) {
                                                    DxConsole.err.println("no classfiles specified");
                                                    return false;
                                                }
                                                if (Main.args.optimize && Main.args.statistics) {
                                                    CodeStatistics.dumpStatistics(DxConsole.out);
                                                }
                                                return true;
                                            }
                                            catch (Exception ex2) {
                                                Main.classTranslatorPool.shutdownNow();
                                                Main.classDefItemConsumer.shutdownNow();
                                                ex2.printStackTrace(System.out);
                                                throw new RuntimeException("Unexpected exception in translator thread.", ex2);
                                            }
                                            catch (InterruptedException ex3) {
                                                Main.classTranslatorPool.shutdownNow();
                                                Main.classDefItemConsumer.shutdownNow();
                                                throw new RuntimeException("Translation has been interrupted", ex3);
                                            }
                                            break;
                                        }
                                        continue;
                                    }
                                }
                            }
                            n = 0;
                            continue Label_0232_Outer;
                        }
                        n3 = 0;
                        continue Label_0232;
                    }
                    Label_0662: {
                        continue Label_0133_Outer;
                    }
                }
                n2 = 0;
                continue Label_0260;
            }
        }
    }
    
    private static boolean processClass(final String s, final byte[] array) {
        if (!Main.args.coreLibrary) {
            checkClassName(s);
        }
        try {
            new DirectClassFileConsumer(s, array, (Future)null).call(new ClassParserTask(s, array).call());
            return true;
        }
        catch (Exception ex) {
            throw new RuntimeException("Exception parsing classes", ex);
        }
    }
    
    private static boolean processFileBytes(String fixPath, final long n, final byte[] array) {
        final boolean endsWith = fixPath.endsWith(".class");
        final boolean equals = fixPath.equals("classes.dex");
        final boolean b = Main.outputResources != null;
        if (!endsWith && !equals && !b) {
            if (Main.args.verbose) {
                final PrintStream out = DxConsole.out;
                final StringBuilder sb = new StringBuilder();
                sb.append("ignored resource ");
                sb.append(fixPath);
                out.println(sb.toString());
            }
            return false;
        }
        if (Main.args.verbose) {
            final PrintStream out2 = DxConsole.out;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("processing ");
            sb2.append(fixPath);
            sb2.append("...");
            out2.println(sb2.toString());
        }
        fixPath = fixPath(fixPath);
        if (endsWith) {
            if (b && Main.args.keepClassesInJar) {
                synchronized (Main.outputResources) {
                    Main.outputResources.put(fixPath, array);
                }
            }
            if (n < Main.minimumFileAge) {
                return true;
            }
            processClass(fixPath, array);
            return false;
        }
        else {
            if (equals) {
                synchronized (Main.libraryDexBuffers) {
                    Main.libraryDexBuffers.add(array);
                    return true;
                }
            }
            synchronized (Main.outputResources) {
                Main.outputResources.put(fixPath, array);
                return true;
            }
        }
    }
    
    private static void processOne(final String s, final ClassPathOpener.FileNameFilter fileNameFilter) {
        if (new ClassPathOpener(s, true, fileNameFilter, (ClassPathOpener.Consumer)new FileBytesConsumer()).process()) {
            updateStatus(true);
        }
    }
    
    private static void readPathsFromFile(final String s, final Collection<String> collection) throws IOException {
        BufferedReader bufferedReader = null;
        try {
            final BufferedReader bufferedReader2 = new BufferedReader(new FileReader(s));
            while (true) {
                bufferedReader = bufferedReader2;
                final String line = bufferedReader2.readLine();
                if (line == null) {
                    break;
                }
                bufferedReader = bufferedReader2;
                collection.add(fixPath(line));
            }
        }
        finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
    }
    
    private static void rotateDexFile() {
        if (Main.outputDex != null) {
            if (Main.dexOutPool != null) {
                Main.dexOutputFutures.add(Main.dexOutPool.submit((Callable<byte[]>)new DexWriter(Main.outputDex)));
            }
            else {
                Main.dexOutputArrays.add(writeDex(Main.outputDex));
            }
        }
        createDexFile();
    }
    
    public static int run(Arguments openOutput) throws IOException {
        Main.errors.set(0);
        Main.libraryDexBuffers.clear();
        (Main.args = (Arguments)openOutput).makeOptionsObjects();
        openOutput = null;
        if (Main.args.humanOutName != null) {
            openOutput = openOutput(Main.args.humanOutName);
            Main.humanOutWriter = new OutputStreamWriter((OutputStream)openOutput);
        }
        try {
            int n;
            if (Main.args.multiDex) {
                n = runMultiDex();
            }
            else {
                n = runMonoDex();
            }
            return n;
        }
        finally {
            closeOutput((OutputStream)openOutput);
        }
    }
    
    private static int runMonoDex() throws IOException {
        File file = null;
        if (Main.args.incremental) {
            if (Main.args.outName == null) {
                System.err.println("error: no incremental output name specified");
                return -1;
            }
            final File file2 = file = new File(Main.args.outName);
            if (file2.exists()) {
                Main.minimumFileAge = file2.lastModified();
                file = file2;
            }
        }
        if (!processAllFiles()) {
            return 1;
        }
        if (Main.args.incremental && !Main.anyFilesProcessed) {
            return 0;
        }
        byte[] writeDex = null;
        if ((!Main.outputDex.isEmpty() || Main.args.humanOutName != null) && (writeDex = writeDex(Main.outputDex)) == null) {
            return 2;
        }
        byte[] mergeIncremental = writeDex;
        if (Main.args.incremental) {
            mergeIncremental = mergeIncremental(writeDex, file);
        }
        final byte[] mergeLibraryDexBuffers = mergeLibraryDexBuffers(mergeIncremental);
        if (Main.args.jarOutput) {
            Main.outputDex = null;
            if (mergeLibraryDexBuffers != null) {
                Main.outputResources.put("classes.dex", mergeLibraryDexBuffers);
            }
            if (!createJar(Main.args.outName)) {
                return 3;
            }
        }
        else if (mergeLibraryDexBuffers != null && Main.args.outName != null) {
            final OutputStream openOutput = openOutput(Main.args.outName);
            openOutput.write(mergeLibraryDexBuffers);
            closeOutput(openOutput);
        }
        return 0;
    }
    
    private static int runMultiDex() throws IOException {
        if (Main.args.mainDexListFile != null) {
            Main.classesInMainDex = new HashSet<String>();
            readPathsFromFile(Main.args.mainDexListFile, Main.classesInMainDex);
        }
        Main.dexOutPool = Executors.newFixedThreadPool(Main.args.numThreads);
        if (!processAllFiles()) {
            return 1;
        }
        if (!Main.libraryDexBuffers.isEmpty()) {
            throw new DexException("Library dex files are not supported in multi-dex mode");
        }
        if (Main.outputDex != null) {
            Main.dexOutputFutures.add(Main.dexOutPool.submit((Callable<byte[]>)new DexWriter(Main.outputDex)));
            Main.outputDex = null;
        }
        try {
            Main.dexOutPool.shutdown();
            if (!Main.dexOutPool.awaitTermination(600L, TimeUnit.SECONDS)) {
                throw new RuntimeException("Timed out waiting for dex writer threads.");
            }
            final Iterator<Future<byte[]>> iterator = Main.dexOutputFutures.iterator();
            while (iterator.hasNext()) {
                Main.dexOutputArrays.add(iterator.next().get());
            }
            if (Main.args.jarOutput) {
                for (int i = 0; i < Main.dexOutputArrays.size(); ++i) {
                    Main.outputResources.put(getDexFileName(i), Main.dexOutputArrays.get(i));
                }
                if (!createJar(Main.args.outName)) {
                    return 3;
                }
            }
            else if (Main.args.outName != null) {
                final File file = new File(Main.args.outName);
                int j = 0;
                while (j < Main.dexOutputArrays.size()) {
                    final FileOutputStream fileOutputStream = new FileOutputStream(new File(file, getDexFileName(j)));
                    try {
                        fileOutputStream.write(Main.dexOutputArrays.get(j));
                        closeOutput(fileOutputStream);
                        ++j;
                        continue;
                    }
                    finally {
                        closeOutput(fileOutputStream);
                    }
                    break;
                }
            }
            return 0;
        }
        catch (Exception ex) {
            Main.dexOutPool.shutdownNow();
            throw new RuntimeException("Unexpected exception in dex writer thread");
        }
        catch (InterruptedException ex2) {
            Main.dexOutPool.shutdownNow();
            throw new RuntimeException("A dex writer thread has been interrupted.");
        }
    }
    
    private static ClassDefItem translateClass(final byte[] array, final DirectClassFile directClassFile) {
        try {
            return CfTranslator.translate(directClassFile, array, Main.args.cfOptions, Main.args.dexOptions, Main.outputDex);
        }
        catch (ParseException ex) {
            DxConsole.err.println("\ntrouble processing:");
            if (Main.args.debug) {
                ex.printStackTrace(DxConsole.err);
            }
            else {
                ex.printContext(DxConsole.err);
            }
            Main.errors.incrementAndGet();
            return null;
        }
    }
    
    private static void updateStatus(final boolean b) {
        Main.anyFilesProcessed |= b;
    }
    
    private static byte[] writeDex(final DexFile p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore_1       
        //     2: getstatic       com/android/dx/command/dexer/Main.args:Lcom/android/dx/command/dexer/Main$Arguments;
        //     5: getfield        com/android/dx/command/dexer/Main$Arguments.methodToDump:Ljava/lang/String;
        //     8: ifnull          34
        //    11: aload_0        
        //    12: aconst_null    
        //    13: iconst_0       
        //    14: invokevirtual   com/android/dx/dex/file/DexFile.toDex:(Ljava/io/Writer;Z)[B
        //    17: pop            
        //    18: aload_0        
        //    19: getstatic       com/android/dx/command/dexer/Main.args:Lcom/android/dx/command/dexer/Main$Arguments;
        //    22: getfield        com/android/dx/command/dexer/Main$Arguments.methodToDump:Ljava/lang/String;
        //    25: getstatic       com/android/dx/command/dexer/Main.humanOutWriter:Ljava/io/OutputStreamWriter;
        //    28: invokestatic    com/android/dx/command/dexer/Main.dumpMethod:(Lcom/android/dx/dex/file/DexFile;Ljava/lang/String;Ljava/io/OutputStreamWriter;)V
        //    31: goto            48
        //    34: aload_0        
        //    35: getstatic       com/android/dx/command/dexer/Main.humanOutWriter:Ljava/io/OutputStreamWriter;
        //    38: getstatic       com/android/dx/command/dexer/Main.args:Lcom/android/dx/command/dexer/Main$Arguments;
        //    41: getfield        com/android/dx/command/dexer/Main$Arguments.verboseDump:Z
        //    44: invokevirtual   com/android/dx/dex/file/DexFile.toDex:(Ljava/io/Writer;Z)[B
        //    47: astore_1       
        //    48: getstatic       com/android/dx/command/dexer/Main.args:Lcom/android/dx/command/dexer/Main$Arguments;
        //    51: getfield        com/android/dx/command/dexer/Main$Arguments.statistics:Z
        //    54: ifeq            70
        //    57: getstatic       com/android/dx/command/DxConsole.out:Ljava/io/PrintStream;
        //    60: aload_0        
        //    61: invokevirtual   com/android/dx/dex/file/DexFile.getStatistics:()Lcom/android/dx/dex/file/Statistics;
        //    64: invokevirtual   com/android/dx/dex/file/Statistics.toHuman:()Ljava/lang/String;
        //    67: invokevirtual   java/io/PrintStream.println:(Ljava/lang/String;)V
        //    70: getstatic       com/android/dx/command/dexer/Main.humanOutWriter:Ljava/io/OutputStreamWriter;
        //    73: ifnull          166
        //    76: getstatic       com/android/dx/command/dexer/Main.humanOutWriter:Ljava/io/OutputStreamWriter;
        //    79: invokevirtual   java/io/OutputStreamWriter.flush:()V
        //    82: aload_1        
        //    83: areturn        
        //    84: astore_0       
        //    85: getstatic       com/android/dx/command/dexer/Main.humanOutWriter:Ljava/io/OutputStreamWriter;
        //    88: ifnull          97
        //    91: getstatic       com/android/dx/command/dexer/Main.humanOutWriter:Ljava/io/OutputStreamWriter;
        //    94: invokevirtual   java/io/OutputStreamWriter.flush:()V
        //    97: aload_0        
        //    98: athrow         
        //    99: astore_0       
        //   100: getstatic       com/android/dx/command/dexer/Main.args:Lcom/android/dx/command/dexer/Main$Arguments;
        //   103: getfield        com/android/dx/command/dexer/Main$Arguments.debug:Z
        //   106: ifeq            127
        //   109: getstatic       com/android/dx/command/DxConsole.err:Ljava/io/PrintStream;
        //   112: ldc_w           "\ntrouble writing output:"
        //   115: invokevirtual   java/io/PrintStream.println:(Ljava/lang/String;)V
        //   118: aload_0        
        //   119: getstatic       com/android/dx/command/DxConsole.err:Ljava/io/PrintStream;
        //   122: invokevirtual   java/lang/Exception.printStackTrace:(Ljava/io/PrintStream;)V
        //   125: aconst_null    
        //   126: areturn        
        //   127: getstatic       com/android/dx/command/DxConsole.err:Ljava/io/PrintStream;
        //   130: astore_1       
        //   131: new             Ljava/lang/StringBuilder;
        //   134: dup            
        //   135: invokespecial   java/lang/StringBuilder.<init>:()V
        //   138: astore_2       
        //   139: aload_2        
        //   140: ldc_w           "\ntrouble writing output: "
        //   143: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   146: pop            
        //   147: aload_2        
        //   148: aload_0        
        //   149: invokevirtual   java/lang/Exception.getMessage:()Ljava/lang/String;
        //   152: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   155: pop            
        //   156: aload_1        
        //   157: aload_2        
        //   158: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   161: invokevirtual   java/io/PrintStream.println:(Ljava/lang/String;)V
        //   164: aconst_null    
        //   165: areturn        
        //   166: aload_1        
        //   167: areturn        
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  2      31     84     99     Any
        //  34     48     84     99     Any
        //  48     70     84     99     Any
        //  70     82     99     166    Ljava/lang/Exception;
        //  85     97     99     166    Ljava/lang/Exception;
        //  97     99     99     166    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0070:
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
    
    public static class Arguments
    {
        private static final String INCREMENTAL_OPTION = "--incremental";
        private static final String INPUT_LIST_OPTION = "--input-list";
        private static final String MAIN_DEX_LIST_OPTION = "--main-dex-list";
        private static final String MINIMAL_MAIN_DEX_OPTION = "--minimal-main-dex";
        private static final String MULTI_DEX_OPTION = "--multi-dex";
        private static final String NUM_THREADS_OPTION = "--num-threads";
        public CfOptions cfOptions;
        public boolean coreLibrary;
        public boolean debug;
        public DexOptions dexOptions;
        public String dontOptimizeListFile;
        public int dumpWidth;
        public boolean emptyOk;
        public String[] fileNames;
        public boolean forceJumbo;
        public String humanOutName;
        public boolean incremental;
        private List<String> inputList;
        public boolean jarOutput;
        public boolean keepClassesInJar;
        public boolean localInfo;
        public String mainDexListFile;
        private int maxNumberOfIdxPerDex;
        public String methodToDump;
        public boolean minimalMainDex;
        public boolean multiDex;
        public int numThreads;
        public boolean optimize;
        public String optimizeListFile;
        public String outName;
        public int positionInfo;
        public boolean statistics;
        public boolean strictNameCheck;
        public boolean verbose;
        public boolean verboseDump;
        public boolean warnings;
        
        public Arguments() {
            this.debug = false;
            this.warnings = true;
            this.verbose = false;
            this.verboseDump = false;
            this.coreLibrary = false;
            this.methodToDump = null;
            this.dumpWidth = 0;
            this.outName = null;
            this.humanOutName = null;
            this.strictNameCheck = true;
            this.emptyOk = false;
            this.jarOutput = false;
            this.keepClassesInJar = false;
            this.positionInfo = 2;
            this.localInfo = true;
            this.incremental = false;
            this.forceJumbo = false;
            this.optimize = true;
            this.optimizeListFile = null;
            this.dontOptimizeListFile = null;
            this.numThreads = 1;
            this.multiDex = false;
            this.mainDexListFile = null;
            this.minimalMainDex = false;
            this.inputList = null;
            this.maxNumberOfIdxPerDex = 65536;
        }
        
        private void makeOptionsObjects() {
            this.cfOptions = new CfOptions();
            this.cfOptions.positionInfo = this.positionInfo;
            this.cfOptions.localInfo = this.localInfo;
            this.cfOptions.strictNameCheck = this.strictNameCheck;
            this.cfOptions.optimize = this.optimize;
            this.cfOptions.optimizeListFile = this.optimizeListFile;
            this.cfOptions.dontOptimizeListFile = this.dontOptimizeListFile;
            this.cfOptions.statistics = this.statistics;
            if (this.warnings) {
                this.cfOptions.warn = DxConsole.err;
            }
            else {
                this.cfOptions.warn = DxConsole.noop;
            }
            this.dexOptions = new DexOptions();
            this.dexOptions.forceJumbo = this.forceJumbo;
        }
        
        public void parse(final String[] array) {
            final ArgumentsParser argumentsParser = new ArgumentsParser(array);
            boolean b = false;
            boolean b2 = false;
            while (argumentsParser.getNext()) {
                if (argumentsParser.isArg("--debug")) {
                    this.debug = true;
                }
                else if (argumentsParser.isArg("--no-warning")) {
                    this.warnings = false;
                }
                else if (argumentsParser.isArg("--verbose")) {
                    this.verbose = true;
                }
                else if (argumentsParser.isArg("--verbose-dump")) {
                    this.verboseDump = true;
                }
                else if (argumentsParser.isArg("--no-files")) {
                    this.emptyOk = true;
                }
                else if (argumentsParser.isArg("--no-optimize")) {
                    this.optimize = false;
                }
                else if (argumentsParser.isArg("--no-strict")) {
                    this.strictNameCheck = false;
                }
                else if (argumentsParser.isArg("--core-library")) {
                    this.coreLibrary = true;
                }
                else if (argumentsParser.isArg("--statistics")) {
                    this.statistics = true;
                }
                else if (argumentsParser.isArg("--optimize-list=")) {
                    if (this.dontOptimizeListFile != null) {
                        System.err.println("--optimize-list and --no-optimize-list are incompatible.");
                        throw new UsageException();
                    }
                    this.optimize = true;
                    this.optimizeListFile = argumentsParser.getLastValue();
                }
                else if (argumentsParser.isArg("--no-optimize-list=")) {
                    if (this.dontOptimizeListFile != null) {
                        System.err.println("--optimize-list and --no-optimize-list are incompatible.");
                        throw new UsageException();
                    }
                    this.optimize = true;
                    this.dontOptimizeListFile = argumentsParser.getLastValue();
                }
                else if (argumentsParser.isArg("--keep-classes")) {
                    this.keepClassesInJar = true;
                }
                else if (argumentsParser.isArg("--output=")) {
                    this.outName = argumentsParser.getLastValue();
                    if (new File(this.outName).isDirectory()) {
                        this.jarOutput = false;
                        b = true;
                    }
                    else if (FileUtils.hasArchiveSuffix(this.outName)) {
                        this.jarOutput = true;
                    }
                    else {
                        if (!this.outName.endsWith(".dex") && !this.outName.equals("-")) {
                            final PrintStream err = System.err;
                            final StringBuilder sb = new StringBuilder();
                            sb.append("unknown output extension: ");
                            sb.append(this.outName);
                            err.println(sb.toString());
                            throw new UsageException();
                        }
                        this.jarOutput = false;
                        b2 = true;
                    }
                }
                else if (argumentsParser.isArg("--dump-to=")) {
                    this.humanOutName = argumentsParser.getLastValue();
                }
                else if (argumentsParser.isArg("--dump-width=")) {
                    this.dumpWidth = Integer.parseInt(argumentsParser.getLastValue());
                }
                else if (argumentsParser.isArg("--dump-method=")) {
                    this.methodToDump = argumentsParser.getLastValue();
                    this.jarOutput = false;
                }
                else if (argumentsParser.isArg("--positions=")) {
                    final String intern = argumentsParser.getLastValue().intern();
                    if (intern == "none") {
                        this.positionInfo = 1;
                    }
                    else if (intern == "important") {
                        this.positionInfo = 3;
                    }
                    else {
                        if (intern != "lines") {
                            final PrintStream err2 = System.err;
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("unknown positions option: ");
                            sb2.append(intern);
                            err2.println(sb2.toString());
                            throw new UsageException();
                        }
                        this.positionInfo = 2;
                    }
                }
                else if (argumentsParser.isArg("--no-locals")) {
                    this.localInfo = false;
                }
                else if (argumentsParser.isArg("--num-threads=")) {
                    this.numThreads = Integer.parseInt(argumentsParser.getLastValue());
                }
                else if (argumentsParser.isArg("--incremental")) {
                    this.incremental = true;
                }
                else if (argumentsParser.isArg("--force-jumbo")) {
                    this.forceJumbo = true;
                }
                else if (argumentsParser.isArg("--multi-dex")) {
                    this.multiDex = true;
                }
                else if (argumentsParser.isArg("--main-dex-list=")) {
                    this.mainDexListFile = argumentsParser.getLastValue();
                }
                else if (argumentsParser.isArg("--minimal-main-dex")) {
                    this.minimalMainDex = true;
                }
                else {
                    if (!argumentsParser.isArg("--set-max-idx-number=")) {
                        if (argumentsParser.isArg("--input-list=")) {
                            final File file = new File(argumentsParser.getLastValue());
                            try {
                                this.inputList = new ArrayList<String>();
                                readPathsFromFile(file.getAbsolutePath(), this.inputList);
                                continue;
                            }
                            catch (IOException ex) {
                                final PrintStream err3 = System.err;
                                final StringBuilder sb3 = new StringBuilder();
                                sb3.append("Unable to read input list file: ");
                                sb3.append(file.getName());
                                err3.println(sb3.toString());
                                throw new UsageException();
                            }
                        }
                        final PrintStream err4 = System.err;
                        final StringBuilder sb4 = new StringBuilder();
                        sb4.append("unknown option: ");
                        sb4.append(argumentsParser.getCurrent());
                        err4.println(sb4.toString());
                        throw new UsageException();
                    }
                    this.maxNumberOfIdxPerDex = Integer.parseInt(argumentsParser.getLastValue());
                }
            }
            this.fileNames = argumentsParser.getRemaining();
            if (this.inputList != null && !this.inputList.isEmpty()) {
                this.inputList.addAll(Arrays.asList(this.fileNames));
                this.fileNames = this.inputList.toArray(new String[this.inputList.size()]);
            }
            if (this.fileNames.length == 0) {
                if (!this.emptyOk) {
                    System.err.println("no input files specified");
                    throw new UsageException();
                }
            }
            else if (this.emptyOk) {
                System.out.println("ignoring input files");
            }
            if (this.humanOutName == null && this.methodToDump != null) {
                this.humanOutName = "-";
            }
            if (this.mainDexListFile != null && !this.multiDex) {
                System.err.println("--main-dex-list is only supported in combination with --multi-dex");
                throw new UsageException();
            }
            if (this.minimalMainDex && (this.mainDexListFile == null || !this.multiDex)) {
                System.err.println("--minimal-main-dex is only supported in combination with --multi-dex and --main-dex-list");
                throw new UsageException();
            }
            if (this.multiDex && this.incremental) {
                System.err.println("--incremental is not supported with --multi-dex");
                throw new UsageException();
            }
            if (this.multiDex && b2) {
                final PrintStream err5 = System.err;
                final StringBuilder sb5 = new StringBuilder();
                sb5.append("Unsupported output \"");
                sb5.append(this.outName);
                sb5.append("\". ");
                sb5.append("--multi-dex");
                sb5.append(" supports only archive or directory output");
                err5.println(sb5.toString());
                throw new UsageException();
            }
            if (b && !this.multiDex) {
                this.outName = new File(this.outName, "classes.dex").getPath();
            }
            this.makeOptionsObjects();
        }
        
        private static class ArgumentsParser
        {
            private final String[] arguments;
            private String current;
            private int index;
            private String lastValue;
            
            public ArgumentsParser(final String[] arguments) {
                this.arguments = arguments;
                this.index = 0;
            }
            
            private boolean getNextValue() {
                if (this.index >= this.arguments.length) {
                    return false;
                }
                this.current = this.arguments[this.index];
                ++this.index;
                return true;
            }
            
            public String getCurrent() {
                return this.current;
            }
            
            public String getLastValue() {
                return this.lastValue;
            }
            
            public boolean getNext() {
                if (this.index >= this.arguments.length) {
                    return false;
                }
                this.current = this.arguments[this.index];
                if (this.current.equals("--")) {
                    return false;
                }
                if (!this.current.startsWith("--")) {
                    return false;
                }
                ++this.index;
                return true;
            }
            
            public String[] getRemaining() {
                final int n = this.arguments.length - this.index;
                final String[] array = new String[n];
                if (n > 0) {
                    System.arraycopy(this.arguments, this.index, array, 0, n);
                }
                return array;
            }
            
            public boolean isArg(String substring) {
                final int length = substring.length();
                if (length <= 0 || substring.charAt(length - 1) != '=') {
                    return this.current.equals(substring);
                }
                if (this.current.startsWith(substring)) {
                    this.lastValue = this.current.substring(length);
                    return true;
                }
                substring = substring.substring(0, length - 1);
                if (!this.current.equals(substring)) {
                    return false;
                }
                if (this.getNextValue()) {
                    this.lastValue = this.current;
                    return true;
                }
                final PrintStream err = System.err;
                final StringBuilder sb = new StringBuilder();
                sb.append("Missing value after parameter ");
                sb.append(substring);
                err.println(sb.toString());
                throw new UsageException();
            }
        }
    }
    
    private static class BestEffortMainDexListFilter implements FileNameFilter
    {
        Map<String, List<String>> map;
        
        public BestEffortMainDexListFilter() {
            this.map = new HashMap<String, List<String>>();
            final Iterator<String> iterator = Main.classesInMainDex.iterator();
            while (iterator.hasNext()) {
                final String access$900 = fixPath(iterator.next());
                final String simpleName = getSimpleName(access$900);
                List<String> list;
                if ((list = this.map.get(simpleName)) == null) {
                    list = new ArrayList<String>(1);
                    this.map.put(simpleName, list);
                }
                list.add(access$900);
            }
        }
        
        private static String getSimpleName(final String s) {
            final int lastIndex = s.lastIndexOf(47);
            if (lastIndex >= 0) {
                return s.substring(lastIndex + 1);
            }
            return s;
        }
        
        @Override
        public boolean accept(String access$900) {
            if (access$900.endsWith(".class")) {
                access$900 = fixPath(access$900);
                final List<String> list = this.map.get(getSimpleName(access$900));
                if (list != null) {
                    final Iterator<String> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        if (access$900.endsWith(iterator.next())) {
                            return true;
                        }
                    }
                }
                return false;
            }
            return true;
        }
    }
    
    private static class ClassDefItemConsumer implements Callable<Boolean>
    {
        Future<ClassDefItem> futureClazz;
        int maxFieldIdsInClass;
        int maxMethodIdsInClass;
        String name;
        
        private ClassDefItemConsumer(final String name, final Future<ClassDefItem> futureClazz, final int maxMethodIdsInClass, final int maxFieldIdsInClass) {
            this.name = name;
            this.futureClazz = futureClazz;
            this.maxMethodIdsInClass = maxMethodIdsInClass;
            this.maxFieldIdsInClass = maxFieldIdsInClass;
        }
        
        @Override
        public Boolean call() throws Exception {
            while (true) {
                try {
                    try {
                        final ClassDefItem classDefItem = this.futureClazz.get();
                        if (classDefItem != null) {
                            addClassToDex(classDefItem);
                            updateStatus(true);
                        }
                        final Boolean value = true;
                        if (Main.args.multiDex) {
                            synchronized (Main.dexRotationLock) {
                                Main.access$1820(this.maxMethodIdsInClass);
                                Main.access$2020(this.maxFieldIdsInClass);
                                Main.dexRotationLock.notifyAll();
                                return value;
                            }
                        }
                        return value;
                    }
                    finally {
                        if (Main.args.multiDex) {
                            // monitorenter(Main.access$1600())
                            final ClassDefItemConsumer classDefItemConsumer = this;
                            final int n = classDefItemConsumer.maxMethodIdsInClass;
                            Main.access$1820(n);
                            final ClassDefItemConsumer classDefItemConsumer2 = this;
                            final int n2 = classDefItemConsumer2.maxFieldIdsInClass;
                            Main.access$2020(n2);
                            final Object o = Main.dexRotationLock;
                            o.notifyAll();
                        }
                        final Throwable t;
                        throw (Exception)t;
                    }
                }
                catch (ExecutionException ex) {}
                try {
                    final ClassDefItemConsumer classDefItemConsumer = this;
                    final int n = classDefItemConsumer.maxMethodIdsInClass;
                    Main.access$1820(n);
                    final ClassDefItemConsumer classDefItemConsumer2 = this;
                    final int n2 = classDefItemConsumer2.maxFieldIdsInClass;
                    Main.access$2020(n2);
                    final Object o = Main.dexRotationLock;
                    o.notifyAll();
                    continue;
                }
                finally {}
                break;
            }
        }
    }
    
    private static class ClassParserTask implements Callable<DirectClassFile>
    {
        byte[] bytes;
        String name;
        
        private ClassParserTask(final String name, final byte[] bytes) {
            this.name = name;
            this.bytes = bytes;
        }
        
        @Override
        public DirectClassFile call() throws Exception {
            return parseClass(this.name, this.bytes);
        }
    }
    
    private static class ClassTranslatorTask implements Callable<ClassDefItem>
    {
        byte[] bytes;
        DirectClassFile classFile;
        String name;
        
        private ClassTranslatorTask(final String name, final byte[] bytes, final DirectClassFile classFile) {
            this.name = name;
            this.bytes = bytes;
            this.classFile = classFile;
        }
        
        @Override
        public ClassDefItem call() {
            return translateClass(this.bytes, this.classFile);
        }
    }
    
    private static class DexWriter implements Callable<byte[]>
    {
        private DexFile dexFile;
        
        private DexWriter(final DexFile dexFile) {
            this.dexFile = dexFile;
        }
        
        @Override
        public byte[] call() throws IOException {
            return writeDex(this.dexFile);
        }
    }
    
    private static class DirectClassFileConsumer implements Callable<Boolean>
    {
        byte[] bytes;
        Future<DirectClassFile> dcff;
        String name;
        
        private DirectClassFileConsumer(final String name, final byte[] bytes, final Future<DirectClassFile> dcff) {
            this.name = name;
            this.bytes = bytes;
            this.dcff = dcff;
        }
        
        private Boolean call(final DirectClassFile directClassFile) {
            int n = 0;
            int n2 = 0;
            if (Main.args.multiDex) {
                n = directClassFile.getConstantPool().size();
                final int n3 = directClassFile.getMethods().size() + n + 2;
                final int n4 = directClassFile.getFields().size() + n + 9;
                synchronized (Main.dexRotationLock) {
                    synchronized (Main.outputDex) {
                        // monitorexit(Main.access$1700())
                        for (n2 = Main.outputDex.getMethodIds().items().size(), n = Main.outputDex.getFieldIds().items().size(); n2 + n3 + Main.maxMethodIdsInProcess > Main.args.maxNumberOfIdxPerDex || n + n4 + Main.maxFieldIdsInProcess > Main.args.maxNumberOfIdxPerDex; n2 = Main.outputDex.getMethodIds().items().size(), n = Main.outputDex.getFieldIds().items().size()) {
                            Label_0210: {
                                if (Main.maxMethodIdsInProcess <= 0) {
                                    if (Main.maxFieldIdsInProcess <= 0) {
                                        if (Main.outputDex.getClassDefs().items().size() > 0) {
                                            rotateDexFile();
                                            break Label_0210;
                                        }
                                        break;
                                    }
                                }
                                try {
                                    Main.dexRotationLock.wait();
                                }
                                catch (InterruptedException ex) {}
                            }
                            Main.outputDex;
                            synchronized (Main.outputDex) {}
                        }
                        Main.access$1812(n3);
                        Main.access$2012(n4);
                        // monitorexit(Main.access$1600())
                        n = n3;
                        n2 = n4;
                    }
                }
            }
            Main.addToDexFutures.add(Main.classDefItemConsumer.submit((Callable<Object>)new ClassDefItemConsumer(this.name, (Future)Main.classTranslatorPool.submit((Callable<Object>)new ClassTranslatorTask(this.name, this.bytes, directClassFile)), n, n2)));
            return true;
        }
        
        @Override
        public Boolean call() throws Exception {
            return this.call(this.dcff.get());
        }
    }
    
    private static class FileBytesConsumer implements Consumer
    {
        @Override
        public void onException(final Exception ex) {
            if (ex instanceof StopProcessing) {
                throw (StopProcessing)ex;
            }
            if (ex instanceof SimException) {
                DxConsole.err.println("\nEXCEPTION FROM SIMULATION:");
                final PrintStream err = DxConsole.err;
                final StringBuilder sb = new StringBuilder();
                sb.append(ex.getMessage());
                sb.append("\n");
                err.println(sb.toString());
                DxConsole.err.println(((SimException)ex).getContext());
            }
            else {
                DxConsole.err.println("\nUNEXPECTED TOP-LEVEL EXCEPTION:");
                ex.printStackTrace(DxConsole.err);
            }
            Main.errors.incrementAndGet();
        }
        
        @Override
        public void onProcessArchiveStart(final File file) {
            if (Main.args.verbose) {
                final PrintStream out = DxConsole.out;
                final StringBuilder sb = new StringBuilder();
                sb.append("processing archive ");
                sb.append(file);
                sb.append("...");
                out.println(sb.toString());
            }
        }
        
        @Override
        public boolean processFileBytes(final String s, final long n, final byte[] array) {
            return processFileBytes(s, n, array);
        }
    }
    
    private static class MainDexListFilter implements FileNameFilter
    {
        @Override
        public boolean accept(String access$900) {
            if (access$900.endsWith(".class")) {
                access$900 = fixPath(access$900);
                return Main.classesInMainDex.contains(access$900);
            }
            return true;
        }
    }
    
    private static class NotFilter implements FileNameFilter
    {
        private final FileNameFilter filter;
        
        private NotFilter(final FileNameFilter filter) {
            this.filter = filter;
        }
        
        @Override
        public boolean accept(final String s) {
            return this.filter.accept(s) ^ true;
        }
    }
    
    private static class StopProcessing extends RuntimeException
    {
    }
}
