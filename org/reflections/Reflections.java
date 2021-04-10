package org.reflections;

import org.slf4j.*;
import javax.annotation.*;
import org.reflections.serializers.*;
import org.reflections.util.*;
import org.reflections.vfs.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import org.reflections.scanners.*;
import java.util.regex.*;
import java.lang.annotation.*;
import com.google.common.base.*;
import com.google.common.collect.*;
import java.util.concurrent.*;

public class Reflections
{
    @Nullable
    public static Logger log;
    protected final transient Configuration configuration;
    protected Store store;
    
    static {
        Reflections.log = Utils.findLogger(Reflections.class);
    }
    
    protected Reflections() {
        this.configuration = new ConfigurationBuilder();
        this.store = new Store(this.configuration);
    }
    
    public Reflections(final String s, @Nullable final Scanner... array) {
        this(new Object[] { s, array });
    }
    
    public Reflections(final Configuration configuration) {
        this.configuration = configuration;
        this.store = new Store(configuration);
        if (configuration.getScanners() != null && !configuration.getScanners().isEmpty()) {
            for (final Scanner scanner : configuration.getScanners()) {
                scanner.setConfiguration(configuration);
                scanner.setStore(this.store.getOrCreate(scanner.getClass().getSimpleName()));
            }
            this.scan();
        }
    }
    
    public Reflections(final Object... array) {
        this(ConfigurationBuilder.build(array));
    }
    
    public static Reflections collect() {
        return collect("META-INF/reflections/", (Predicate<String>)new FilterBuilder().include(".*-reflections.xml"), new Serializer[0]);
    }
    
    public static Reflections collect(String openInputStream, final Predicate<String> predicate, @Nullable Serializer... array) {
        if (array != null && array.length == 1) {
            array = (Serializer[])(Object)array[0];
        }
        else {
            array = (Serializer[])(Object)new XmlSerializer();
        }
        final Collection<URL> forPackage = ClasspathHelper.forPackage(openInputStream, new ClassLoader[0]);
        if (forPackage.isEmpty()) {
            return null;
        }
        final long currentTimeMillis = System.currentTimeMillis();
        final Reflections reflections = new Reflections();
        for (final Vfs.File file : Vfs.findFiles(forPackage, openInputStream, predicate)) {
            openInputStream = null;
            try {
                try {
                    final InputStream inputStream = (InputStream)(openInputStream = (String)file.openInputStream());
                    reflections.merge(((Serializer)(Object)array).read(inputStream));
                    Utils.close(inputStream);
                }
                finally {}
            }
            catch (IOException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("could not merge ");
                sb.append(file);
                throw new ReflectionsException(sb.toString(), ex);
            }
            Utils.close((InputStream)openInputStream);
        }
        if (Reflections.log != null) {
            final Store store = reflections.getStore();
            int n = 0;
            int n2 = 0;
            for (final String s : store.keySet()) {
                n += store.get(s).keySet().size();
                n2 += store.get(s).size();
            }
            final Logger log = Reflections.log;
            final long currentTimeMillis2 = System.currentTimeMillis();
            final int size = forPackage.size();
            if (forPackage.size() > 1) {
                openInputStream = "s";
            }
            else {
                openInputStream = "";
            }
            log.info(String.format("Reflections took %d ms to collect %d url%s, producing %d keys and %d values [%s]", currentTimeMillis2 - currentTimeMillis, size, openInputStream, n, n2, Joiner.on(", ").join((Iterable)forPackage)));
        }
        return reflections;
    }
    
    private static String index(final Class<? extends Scanner> clazz) {
        return clazz.getSimpleName();
    }
    
    private ClassLoader[] loaders() {
        return this.configuration.getClassLoaders();
    }
    
    public Reflections collect(final File file) {
        InputStream inputStream = null;
        try {
            try {
                final FileInputStream fileInputStream;
                final InputStream inputStream2 = fileInputStream = (FileInputStream)(inputStream = new FileInputStream(file));
                final Reflections collect = this.collect(inputStream2);
                Utils.close(inputStream2);
                return collect;
            }
            finally {}
        }
        catch (FileNotFoundException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("could not obtain input stream from file ");
            sb.append(file);
            throw new ReflectionsException(sb.toString(), ex);
        }
        Utils.close(inputStream);
    }
    
    public Reflections collect(final InputStream inputStream) {
        try {
            this.merge(this.configuration.getSerializer().read(inputStream));
            if (Reflections.log != null) {
                final Logger log = Reflections.log;
                final StringBuilder sb = new StringBuilder();
                sb.append("Reflections collected metadata from input stream using serializer ");
                sb.append(this.configuration.getSerializer().getClass().getName());
                log.info(sb.toString());
            }
            return this;
        }
        catch (Exception ex) {
            throw new ReflectionsException("could not merge input stream", ex);
        }
    }
    
    protected Iterable<String> getAllAnnotated(final Iterable<String> iterable, final boolean b, final boolean b2) {
        if (!b2) {
            final Iterable concat = Iterables.concat((Iterable)iterable, (Iterable)this.store.getAll(index(TypeAnnotationsScanner.class), iterable));
            return (Iterable<String>)Iterables.concat(concat, (Iterable)this.store.getAll(index(SubTypesScanner.class), concat));
        }
        if (b) {
            final Iterable<String> value = this.store.get(index(SubTypesScanner.class), (Iterable<String>)ReflectionUtils.filter((Iterable<Object>)iterable, (Predicate)new Predicate<String>() {
                public boolean apply(@Nullable final String s) {
                    return ReflectionUtils.forName(s, Reflections.this.loaders()).isInterface() ^ true;
                }
            }));
            return (Iterable<String>)Iterables.concat((Iterable)value, (Iterable)this.store.getAll(index(SubTypesScanner.class), value));
        }
        return iterable;
    }
    
    public Set<String> getAllTypes() {
        final HashSet hashSet = Sets.newHashSet((Iterable)this.store.getAll(index(SubTypesScanner.class), Object.class.getName()));
        if (hashSet.isEmpty()) {
            throw new ReflectionsException("Couldn't find subtypes of Object. Make sure SubTypesScanner initialized to include Object class - new SubTypesScanner(false)");
        }
        return (Set<String>)hashSet;
    }
    
    public Configuration getConfiguration() {
        return this.configuration;
    }
    
    public List<String> getConstructorParamNames(final Constructor constructor) {
        final Iterable<String> value = this.store.get(index(MethodParameterNamesScanner.class), Utils.name(constructor));
        String[] split;
        if (!Iterables.isEmpty((Iterable)value)) {
            split = ((String)Iterables.getOnlyElement((Iterable)value)).split(", ");
        }
        else {
            split = new String[0];
        }
        return Arrays.asList(split);
    }
    
    public Set<Member> getConstructorUsage(final Constructor constructor) {
        return Utils.getMembersFromDescriptors(this.store.get(index(MemberUsageScanner.class), Utils.name(constructor)), new ClassLoader[0]);
    }
    
    public Set<Constructor> getConstructorsAnnotatedWith(final Class<? extends Annotation> clazz) {
        return Utils.getConstructorsFromDescriptors(this.store.get(index(MethodAnnotationsScanner.class), clazz.getName()), this.loaders());
    }
    
    public Set<Constructor> getConstructorsAnnotatedWith(final Annotation annotation) {
        return (Set<Constructor>)ReflectionUtils.filter(this.getConstructorsAnnotatedWith(annotation.annotationType()), ReflectionUtils.withAnnotation(annotation));
    }
    
    public Set<Constructor> getConstructorsMatchParams(final Class<?>... array) {
        return Utils.getConstructorsFromDescriptors(this.store.get(index(MethodParameterScanner.class), Utils.names(array).toString()), this.loaders());
    }
    
    public Set<Constructor> getConstructorsWithAnyParamAnnotated(final Class<? extends Annotation> clazz) {
        return Utils.getConstructorsFromDescriptors(this.store.get(index(MethodParameterScanner.class), clazz.getName()), this.loaders());
    }
    
    public Set<Constructor> getConstructorsWithAnyParamAnnotated(final Annotation annotation) {
        return (Set<Constructor>)ReflectionUtils.filter(this.getConstructorsWithAnyParamAnnotated(annotation.annotationType()), ReflectionUtils.withAnyParameterAnnotation(annotation));
    }
    
    public Set<Member> getFieldUsage(final Field field) {
        return Utils.getMembersFromDescriptors(this.store.get(index(MemberUsageScanner.class), Utils.name(field)), new ClassLoader[0]);
    }
    
    public Set<Field> getFieldsAnnotatedWith(final Class<? extends Annotation> clazz) {
        final HashSet hashSet = Sets.newHashSet();
        final Iterator<String> iterator = this.store.get(index(FieldAnnotationsScanner.class), clazz.getName()).iterator();
        while (iterator.hasNext()) {
            hashSet.add(Utils.getFieldFromString(iterator.next(), this.loaders()));
        }
        return (Set<Field>)hashSet;
    }
    
    public Set<Field> getFieldsAnnotatedWith(final Annotation annotation) {
        return ReflectionUtils.filter(this.getFieldsAnnotatedWith(annotation.annotationType()), ReflectionUtils.withAnnotation(annotation));
    }
    
    public List<String> getMethodParamNames(final Method method) {
        final Iterable<String> value = this.store.get(index(MethodParameterNamesScanner.class), Utils.name(method));
        String[] split;
        if (!Iterables.isEmpty((Iterable)value)) {
            split = ((String)Iterables.getOnlyElement((Iterable)value)).split(", ");
        }
        else {
            split = new String[0];
        }
        return Arrays.asList(split);
    }
    
    public Set<Member> getMethodUsage(final Method method) {
        return Utils.getMembersFromDescriptors(this.store.get(index(MemberUsageScanner.class), Utils.name(method)), new ClassLoader[0]);
    }
    
    public Set<Method> getMethodsAnnotatedWith(final Class<? extends Annotation> clazz) {
        return Utils.getMethodsFromDescriptors(this.store.get(index(MethodAnnotationsScanner.class), clazz.getName()), this.loaders());
    }
    
    public Set<Method> getMethodsAnnotatedWith(final Annotation annotation) {
        return ReflectionUtils.filter(this.getMethodsAnnotatedWith(annotation.annotationType()), ReflectionUtils.withAnnotation(annotation));
    }
    
    public Set<Method> getMethodsMatchParams(final Class<?>... array) {
        return Utils.getMethodsFromDescriptors(this.store.get(index(MethodParameterScanner.class), Utils.names(array).toString()), this.loaders());
    }
    
    public Set<Method> getMethodsReturn(final Class clazz) {
        return Utils.getMethodsFromDescriptors(this.store.get(index(MethodParameterScanner.class), Utils.names(clazz)), this.loaders());
    }
    
    public Set<Method> getMethodsWithAnyParamAnnotated(final Class<? extends Annotation> clazz) {
        return Utils.getMethodsFromDescriptors(this.store.get(index(MethodParameterScanner.class), clazz.getName()), this.loaders());
    }
    
    public Set<Method> getMethodsWithAnyParamAnnotated(final Annotation annotation) {
        return ReflectionUtils.filter(this.getMethodsWithAnyParamAnnotated(annotation.annotationType()), ReflectionUtils.withAnyParameterAnnotation(annotation));
    }
    
    public Set<String> getResources(final Predicate<String> predicate) {
        return (Set<String>)Sets.newHashSet((Iterable)this.store.get(index(ResourcesScanner.class), Iterables.filter((Iterable)this.store.get(index(ResourcesScanner.class)).keySet(), (Predicate)predicate)));
    }
    
    public Set<String> getResources(final Pattern pattern) {
        return this.getResources((Predicate<String>)new Predicate<String>() {
            public boolean apply(final String s) {
                return pattern.matcher(s).matches();
            }
        });
    }
    
    public Store getStore() {
        return this.store;
    }
    
    public <T> Set<Class<? extends T>> getSubTypesOf(final Class<T> clazz) {
        return (Set<Class<? extends T>>)Sets.newHashSet((Iterable)ReflectionUtils.forNames(this.store.getAll(index(SubTypesScanner.class), Arrays.asList(clazz.getName())), this.loaders()));
    }
    
    public Set<Class<?>> getTypesAnnotatedWith(final Class<? extends Annotation> clazz) {
        return this.getTypesAnnotatedWith(clazz, false);
    }
    
    public Set<Class<?>> getTypesAnnotatedWith(final Class<? extends Annotation> clazz, final boolean b) {
        final Iterable<String> value = this.store.get(index(TypeAnnotationsScanner.class), clazz.getName());
        return (Set<Class<?>>)Sets.newHashSet(Iterables.concat((Iterable)ReflectionUtils.forNames(value, this.loaders()), (Iterable)ReflectionUtils.forNames(this.getAllAnnotated(value, clazz.isAnnotationPresent(Inherited.class), b), this.loaders())));
    }
    
    public Set<Class<?>> getTypesAnnotatedWith(final Annotation annotation) {
        return this.getTypesAnnotatedWith(annotation, false);
    }
    
    public Set<Class<?>> getTypesAnnotatedWith(final Annotation annotation, final boolean b) {
        final Iterable<String> value = this.store.get(index(TypeAnnotationsScanner.class), annotation.annotationType().getName());
        final Set<Class<?>> filter = ReflectionUtils.filter(ReflectionUtils.forNames(value, this.loaders()), ReflectionUtils.withAnnotation(annotation));
        return (Set<Class<?>>)Sets.newHashSet(Iterables.concat((Iterable)filter, (Iterable)ReflectionUtils.forNames(ReflectionUtils.filter(this.getAllAnnotated(Utils.names(filter), annotation.annotationType().isAnnotationPresent(Inherited.class), b), Predicates.not(Predicates.in((Collection)Sets.newHashSet((Iterable)value)))), this.loaders())));
    }
    
    public Reflections merge(final Reflections reflections) {
        if (reflections.store != null) {
            for (final String s : reflections.store.keySet()) {
                final Multimap<String, String> value = reflections.store.get(s);
                for (final String s2 : value.keySet()) {
                    final Iterator iterator3 = value.get((Object)s2).iterator();
                    while (iterator3.hasNext()) {
                        this.store.getOrCreate(s).put((Object)s2, (Object)iterator3.next());
                    }
                }
            }
        }
        return this;
    }
    
    public File save(final String s) {
        return this.save(s, this.configuration.getSerializer());
    }
    
    public File save(final String s, final Serializer serializer) {
        final File save = serializer.save(this, s);
        if (Reflections.log != null) {
            final Logger log = Reflections.log;
            final StringBuilder sb = new StringBuilder();
            sb.append("Reflections successfully saved in ");
            sb.append(save.getAbsolutePath());
            sb.append(" using ");
            sb.append(serializer.getClass().getSimpleName());
            log.info(sb.toString());
        }
        return save;
    }
    
    protected void scan() {
        while (true) {
            Label_0514: {
                if (this.configuration.getUrls() == null || this.configuration.getUrls().isEmpty()) {
                    break Label_0514;
                }
                if (Reflections.log != null && Reflections.log.isDebugEnabled()) {
                    final Logger log = Reflections.log;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("going to scan these urls:\n");
                    sb.append(Joiner.on("\n").join((Iterable)this.configuration.getUrls()));
                    log.debug(sb.toString());
                }
                final long currentTimeMillis = System.currentTimeMillis();
                int n = 0;
                Object o = this.configuration.getExecutorService();
                Object o2 = Lists.newArrayList();
                Object iterator = this.configuration.getUrls().iterator();
                URL url;
                int n2;
                long currentTimeMillis2 = 0L;
                final ReflectionsException ex;
                int n3 = 0;
                int n4 = 0;
                Label_0208_Outer:Label_0344_Outer:Block_9_Outer:
                while (true) {
                    Label_0256: {
                        if (!((Iterator)iterator).hasNext()) {
                            break Label_0256;
                        }
                        url = ((Iterator<URL>)iterator).next();
                        Label_0202: {
                            if (o == null) {
                                break Label_0202;
                            }
                            try {
                                ((List<Future<?>>)o2).add(((ExecutorService)o).submit((Runnable)new Runnable() {
                                    @Override
                                    public void run() {
                                        if (Reflections.log != null && Reflections.log.isDebugEnabled()) {
                                            final Logger log = Reflections.log;
                                            final StringBuilder sb = new StringBuilder();
                                            sb.append("[");
                                            sb.append(Thread.currentThread().toString());
                                            sb.append("] scanning ");
                                            sb.append(url);
                                            log.debug(sb.toString());
                                        }
                                        Reflections.this.scan(url);
                                    }
                                }));
                                // iftrue(Label_0408:, !o2.hasNext())
                                // iftrue(Label_0513:, Reflections.log == null)
                                // iftrue(Label_0455:, o == null || !o instanceof ThreadPoolExecutor)
                                // iftrue(Label_0251:, Reflections.log == null)
                                // iftrue(Label_0531:, Reflections.log == null)
                                // iftrue(Label_0315:, !o2.hasNext())
                                // iftrue(Label_0315:, o == null)
                            Label_0270_Outer:
                                while (true) {
                                Label_0270:
                                    while (true) {
                                    Label_0459_Outer:
                                        while (true) {
                                        Label_0459:
                                            while (true) {
                                                Block_16: {
                                                    while (true) {
                                                        Block_17: {
                                                            Block_13: {
                                                            Label_0251_Outer:
                                                                while (true) {
                                                                    n2 = n + 1;
                                                                    while (true) {
                                                                        while (true) {
                                                                            n = n2;
                                                                            continue Label_0208_Outer;
                                                                            break Label_0459;
                                                                            currentTimeMillis2 = System.currentTimeMillis();
                                                                            break Block_13;
                                                                            Label_0513: {
                                                                                return;
                                                                            }
                                                                            Label_0408:
                                                                            o2 = Reflections.log;
                                                                            break Block_16;
                                                                            Label_0455:
                                                                            o = "";
                                                                            break Label_0459;
                                                                            n2 = n;
                                                                            break Label_0270;
                                                                            Label_0531:
                                                                            return;
                                                                            Reflections.log.warn("could not create Vfs.Dir from url. ignoring the exception and continuing", (Throwable)ex);
                                                                            n2 = n;
                                                                            continue Label_0344_Outer;
                                                                        }
                                                                        break Block_17;
                                                                        iterator = ((Iterator<Future<?>>)o2).next();
                                                                        try {
                                                                            ((Future)iterator).get();
                                                                            break Label_0270;
                                                                        }
                                                                        catch (Exception o) {
                                                                            throw new RuntimeException((Throwable)o);
                                                                        }
                                                                        continue Label_0459_Outer;
                                                                    }
                                                                    this.scan(url);
                                                                    continue Label_0251_Outer;
                                                                }
                                                                ((Logger)o2).info(String.format("Reflections took %d ms to scan %d urls, producing %d keys and %d values %s", currentTimeMillis2 - currentTimeMillis, n, n3, n4, o));
                                                                return;
                                                            }
                                                            n3 = 0;
                                                            n4 = 0;
                                                            o2 = this.store.keySet().iterator();
                                                            continue Label_0459_Outer;
                                                        }
                                                        Reflections.log.warn("given scan urls are empty. set urls in the configuration");
                                                        return;
                                                        continue Label_0270_Outer;
                                                    }
                                                }
                                                o = String.format("[using %d cores]", ((ThreadPoolExecutor)o).getMaximumPoolSize());
                                                continue Label_0459;
                                            }
                                            iterator = ((Iterator<Future<?>>)o2).next();
                                            n3 += this.store.get((String)iterator).keySet().size();
                                            n4 += this.store.get((String)iterator).size();
                                            continue Block_9_Outer;
                                        }
                                        o2 = ((List<Future<?>>)o2).iterator();
                                        continue Label_0270;
                                    }
                                    n2 = n;
                                    continue;
                                }
                            }
                            // iftrue(Label_0251:, !Reflections.log.isWarnEnabled())
                            catch (ReflectionsException ex2) {}
                        }
                    }
                    break;
                }
            }
            final ReflectionsException ex2;
            final ReflectionsException ex = ex2;
            continue;
        }
    }
    
    protected void scan(final URL url) {
        final Vfs.Dir fromURL = Vfs.fromURL(url);
        try {
            for (final Vfs.File file : fromURL.getFiles()) {
                final Predicate<String> inputsFilter = this.configuration.getInputsFilter();
                final String relativePath = file.getRelativePath();
                final String replace = relativePath.replace('/', '.');
                if (inputsFilter == null || inputsFilter.apply((Object)relativePath) || inputsFilter.apply((Object)replace)) {
                    Object o = null;
                    for (final Scanner scanner : this.configuration.getScanners()) {
                        Object scan = null;
                        try {
                            Label_0175: {
                                if (!scanner.acceptsInput(relativePath)) {
                                    scan = o;
                                    if (!scanner.acceptResult(replace)) {
                                        break Label_0175;
                                    }
                                }
                                scan = scanner.scan(file, o);
                            }
                        }
                        catch (Exception ex) {
                            scan = o;
                            if (Reflections.log != null) {
                                scan = o;
                                if (Reflections.log.isDebugEnabled()) {
                                    final Logger log = Reflections.log;
                                    final StringBuilder sb = new StringBuilder();
                                    sb.append("could not scan file ");
                                    sb.append(file.getRelativePath());
                                    sb.append(" in url ");
                                    sb.append(url.toExternalForm());
                                    sb.append(" with scanner ");
                                    sb.append(scanner.getClass().getSimpleName());
                                    log.debug(sb.toString(), (Object)ex.getMessage());
                                    scan = o;
                                }
                            }
                        }
                        o = scan;
                    }
                }
            }
        }
        finally {
            fromURL.close();
        }
    }
}
