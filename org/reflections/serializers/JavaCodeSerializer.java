package org.reflections.serializers;

import java.lang.annotation.*;
import java.lang.reflect.*;
import org.reflections.*;
import org.reflections.util.*;
import java.nio.charset.*;
import com.google.common.io.*;
import java.io.*;
import org.reflections.scanners.*;
import com.google.common.base.*;
import java.util.*;
import com.google.common.collect.*;

public class JavaCodeSerializer implements Serializer
{
    private static final String arrayDescriptor = "$$";
    private static final String dotSeparator = ".";
    private static final String doubleSeparator = "__";
    private static final String pathSeparator = "_";
    private static final String tokenSeparator = "_";
    
    private String getNonDuplicateName(final String s, final List<String> list) {
        return this.getNonDuplicateName(s, list, list.size());
    }
    
    private String getNonDuplicateName(String normalize, final List<String> list, final int n) {
        normalize = this.normalize(normalize);
        for (int i = 0; i < n; ++i) {
            if (normalize.equals(list.get(i))) {
                final StringBuilder sb = new StringBuilder();
                sb.append(normalize);
                sb.append("_");
                return this.getNonDuplicateName(sb.toString(), list, n);
            }
        }
        return normalize;
    }
    
    private String normalize(final String s) {
        return s.replace(".", "_");
    }
    
    public static Annotation resolveAnnotation(final Class clazz) {
        try {
            return resolveClassOf(clazz.getDeclaringClass().getDeclaringClass()).getAnnotation(ReflectionUtils.forName(clazz.getSimpleName().replace("_", "."), new ClassLoader[0]));
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("could not resolve to annotation ");
            sb.append(clazz.getName());
            throw new ReflectionsException(sb.toString(), ex);
        }
    }
    
    public static Class<?> resolveClass(final Class clazz) {
        try {
            return resolveClassOf(clazz);
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("could not resolve to class ");
            sb.append(clazz.getName());
            throw new ReflectionsException(sb.toString(), ex);
        }
    }
    
    public static Class<?> resolveClassOf(Class declaringClass) throws ClassNotFoundException {
        final LinkedList linkedList = Lists.newLinkedList();
        while (declaringClass != null) {
            linkedList.addFirst(declaringClass.getSimpleName());
            declaringClass = declaringClass.getDeclaringClass();
        }
        return Class.forName(Joiner.on(".").join((Iterable)linkedList.subList(1, linkedList.size())).replace(".$", "$"));
    }
    
    public static Field resolveField(final Class clazz) {
        try {
            return resolveClassOf(clazz.getDeclaringClass().getDeclaringClass()).getDeclaredField(clazz.getSimpleName());
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("could not resolve to field ");
            sb.append(clazz.getName());
            throw new ReflectionsException(sb.toString(), ex);
        }
    }
    
    public static Method resolveMethod(final Class clazz) {
        while (true) {
            final String simpleName = clazz.getSimpleName();
            while (true) {
                Label_0155: {
                    Label_0152: {
                        try {
                            if (simpleName.contains("_")) {
                                final String substring = simpleName.substring(0, simpleName.indexOf("_"));
                                final String[] split = simpleName.substring(simpleName.indexOf("_") + 1).split("__");
                                final Class<?>[] array = (Class<?>[])new Class[split.length];
                                for (int i = 0; i < split.length; ++i) {
                                    array[i] = ReflectionUtils.forName(split[i].replace("$$", "[]").replace("_", "."), new ClassLoader[0]);
                                }
                                break Label_0152;
                            }
                            break Label_0155;
                            String substring = null;
                            Class<?>[] array = null;
                            return resolveClassOf(clazz.getDeclaringClass().getDeclaringClass()).getDeclaredMethod(substring, array);
                        }
                        catch (Exception ex) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("could not resolve to method ");
                            sb.append(clazz.getName());
                            throw new ReflectionsException(sb.toString(), ex);
                        }
                    }
                    continue;
                }
                final String substring = simpleName;
                final Class<?>[] array = null;
                continue;
            }
        }
    }
    
    @Override
    public Reflections read(final InputStream inputStream) {
        throw new UnsupportedOperationException("read is not implemented on JavaCodeSerializer");
    }
    
    @Override
    public File save(final Reflections reflections, String s) {
        String substring = s;
        if (s.endsWith("/")) {
            substring = s.substring(0, s.length() - 1);
        }
        final String concat = substring.replace('.', '/').concat(".java");
        final File prepareFile = Utils.prepareFile(concat);
        final int lastIndex = substring.lastIndexOf(46);
        String s3;
        if (lastIndex == -1) {
            final String s2 = "";
            s = substring.substring(substring.lastIndexOf(47) + 1);
            s3 = s2;
        }
        else {
            s = substring.substring(substring.lastIndexOf(47) + 1, lastIndex);
            final String substring2 = substring.substring(lastIndex + 1);
            s3 = s;
            s = substring2;
        }
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("//generated using Reflections JavaCodeSerializer");
            sb.append(" [");
            sb.append(new Date());
            sb.append("]");
            sb.append("\n");
            if (s3.length() != 0) {
                sb.append("package ");
                sb.append(s3);
                sb.append(";\n");
                sb.append("\n");
            }
            sb.append("public interface ");
            sb.append(s);
            sb.append(" {\n\n");
            sb.append(this.toString(reflections));
            sb.append("}\n");
            Files.write((CharSequence)sb.toString(), new File(concat), Charset.defaultCharset());
            return prepareFile;
        }
        catch (IOException ex) {
            throw new RuntimeException();
        }
    }
    
    @Override
    public String toString(final Reflections reflections) {
        if (reflections.getStore().get(TypeElementsScanner.class.getSimpleName()).isEmpty() && Reflections.log != null) {
            Reflections.log.warn("JavaCodeSerializer needs TypeElementsScanner configured");
        }
        final StringBuilder sb = new StringBuilder();
        ArrayList<String> arrayList = (ArrayList<String>)Lists.newArrayList();
        int n = 1;
        final ArrayList arrayList2 = Lists.newArrayList((Iterable)reflections.getStore().get(TypeElementsScanner.class.getSimpleName()).keySet());
        Collections.sort((List<Comparable>)arrayList2);
        for (final String s : arrayList2) {
            ArrayList arrayList3;
            int n2;
            for (arrayList3 = Lists.newArrayList((Object[])s.split("\\.")), n2 = 0; n2 < Math.min(arrayList3.size(), arrayList.size()) && ((String)arrayList3.get(n2)).equals(arrayList.get(n2)); ++n2) {}
            for (int i = arrayList.size(); i > n2; --i) {
                --n;
                sb.append(Utils.repeat("\t", n));
                sb.append("}\n");
            }
            for (int j = n2; j < arrayList3.size() - 1; ++j, ++n) {
                sb.append(Utils.repeat("\t", n));
                sb.append("public interface ");
                sb.append(this.getNonDuplicateName((String)arrayList3.get(j), arrayList3, j));
                sb.append(" {\n");
            }
            final String s2 = arrayList3.get(arrayList3.size() - 1);
            final ArrayList arrayList4 = Lists.newArrayList();
            final ArrayList arrayList5 = Lists.newArrayList();
            final SetMultimap setMultimap = Multimaps.newSetMultimap((Map)new HashMap(), (Supplier)new Supplier<Set<String>>() {
                public Set<String> get() {
                    return (Set<String>)Sets.newHashSet();
                }
            });
            for (final String s3 : reflections.getStore().get(TypeElementsScanner.class.getSimpleName(), s)) {
                if (s3.startsWith("@")) {
                    arrayList4.add(s3.substring(1));
                }
                else if (s3.contains("(")) {
                    if (s3.startsWith("<")) {
                        continue;
                    }
                    final int index = s3.indexOf(40);
                    final String substring = s3.substring(0, index);
                    final String substring2 = s3.substring(index + 1, s3.indexOf(")"));
                    String string = "";
                    if (substring2.length() != 0) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("_");
                        sb2.append(substring2.replace(".", "_").replace(", ", "__").replace("[]", "$$"));
                        string = sb2.toString();
                    }
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append(substring);
                    sb3.append(string);
                    ((Multimap)setMultimap).put((Object)substring, (Object)sb3.toString());
                }
                else {
                    if (Utils.isEmpty(s3)) {
                        continue;
                    }
                    arrayList5.add(s3);
                }
            }
            final int n3 = n + 1;
            sb.append(Utils.repeat("\t", n));
            sb.append("public interface ");
            sb.append(this.getNonDuplicateName(s2, arrayList3, arrayList3.size() - 1));
            sb.append(" {\n");
            int n4 = n3;
            if (!arrayList5.isEmpty()) {
                final int n5 = n3 + 1;
                sb.append(Utils.repeat("\t", n3));
                sb.append("public interface fields {\n");
                for (final String s4 : arrayList5) {
                    sb.append(Utils.repeat("\t", n5));
                    sb.append("public interface ");
                    sb.append(this.getNonDuplicateName(s4, arrayList3));
                    sb.append(" {}\n");
                }
                n4 = n5 - 1;
                sb.append(Utils.repeat("\t", n4));
                sb.append("}\n");
            }
            int n6 = n4;
            if (!((Multimap)setMultimap).isEmpty()) {
                final int n7 = n4 + 1;
                sb.append(Utils.repeat("\t", n4));
                sb.append("public interface methods {\n");
                for (final Map.Entry<String, V> entry : ((Multimap)setMultimap).entries()) {
                    String s5 = entry.getKey();
                    final String s6 = (String)entry.getValue();
                    if (((Multimap)setMultimap).get((Object)s5).size() != 1) {
                        s5 = s6;
                    }
                    final String nonDuplicateName = this.getNonDuplicateName(s5, arrayList5);
                    sb.append(Utils.repeat("\t", n7));
                    sb.append("public interface ");
                    sb.append(this.getNonDuplicateName(nonDuplicateName, arrayList3));
                    sb.append(" {}\n");
                }
                n6 = n7 - 1;
                sb.append(Utils.repeat("\t", n6));
                sb.append("}\n");
            }
            int n8 = n6;
            if (!arrayList4.isEmpty()) {
                final int n9 = n6 + 1;
                sb.append(Utils.repeat("\t", n6));
                sb.append("public interface annotations {\n");
                final Iterator<String> iterator5 = arrayList4.iterator();
                while (iterator5.hasNext()) {
                    final String nonDuplicateName2 = this.getNonDuplicateName(iterator5.next(), arrayList3);
                    sb.append(Utils.repeat("\t", n9));
                    sb.append("public interface ");
                    sb.append(nonDuplicateName2);
                    sb.append(" {}\n");
                }
                n8 = n9 - 1;
                sb.append(Utils.repeat("\t", n8));
                sb.append("}\n");
            }
            arrayList = (ArrayList<String>)arrayList3;
            n = n8;
        }
        for (int k = arrayList.size(); k >= 1; --k) {
            sb.append(Utils.repeat("\t", k));
            sb.append("}\n");
        }
        return sb.toString();
    }
}
