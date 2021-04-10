package org.reflections.util;

import com.google.common.collect.*;
import org.reflections.*;
import java.util.*;
import com.google.common.base.*;
import java.util.regex.*;

public class FilterBuilder implements Predicate<String>
{
    private final List<Predicate<String>> chain;
    
    public FilterBuilder() {
        this.chain = (List<Predicate<String>>)Lists.newArrayList();
    }
    
    private FilterBuilder(final Iterable<Predicate<String>> iterable) {
        this.chain = (List<Predicate<String>>)Lists.newArrayList((Iterable)iterable);
    }
    
    private static String packageNameRegex(final Class<?> clazz) {
        final StringBuilder sb = new StringBuilder();
        sb.append(clazz.getPackage().getName());
        sb.append(".");
        return prefix(sb.toString());
    }
    
    public static FilterBuilder parse(String s) {
        final ArrayList<Predicate<String>> list = new ArrayList<Predicate<String>>();
        if (!Utils.isEmpty(s)) {
            final String[] split = s.split(",");
            for (int length = split.length, i = 0; i < length; ++i) {
                s = split[i].trim();
                final char char1 = s.charAt(0);
                s = s.substring(1);
                Matcher matcher;
                if (char1 != '+') {
                    if (char1 != '-') {
                        throw new ReflectionsException("includeExclude should start with either + or -");
                    }
                    matcher = new Exclude(s);
                }
                else {
                    matcher = new Include(s);
                }
                list.add((Predicate<String>)matcher);
            }
            return new FilterBuilder(list);
        }
        return new FilterBuilder();
    }
    
    public static FilterBuilder parsePackages(String s) {
        final ArrayList<Predicate<String>> list = new ArrayList<Predicate<String>>();
        if (!Utils.isEmpty(s)) {
            final String[] split = s.split(",");
            for (int length = split.length, i = 0; i < length; ++i) {
                s = split[i].trim();
                final char char1 = s.charAt(0);
                final String s2 = s = s.substring(1);
                if (!s2.endsWith(".")) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(s2);
                    sb.append(".");
                    s = sb.toString();
                }
                s = prefix(s);
                Matcher matcher;
                if (char1 != '+') {
                    if (char1 != '-') {
                        throw new ReflectionsException("includeExclude should start with either + or -");
                    }
                    matcher = new Exclude(s);
                }
                else {
                    matcher = new Include(s);
                }
                list.add((Predicate<String>)matcher);
            }
            return new FilterBuilder(list);
        }
        return new FilterBuilder();
    }
    
    public static String prefix(final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s.replace(".", "\\."));
        sb.append(".*");
        return sb.toString();
    }
    
    public FilterBuilder add(final Predicate<String> predicate) {
        this.chain.add(predicate);
        return this;
    }
    
    public boolean apply(final String s) {
        final List<Predicate<String>> chain = this.chain;
        boolean apply = false;
        if (chain == null || this.chain.isEmpty() || this.chain.get(0) instanceof Exclude) {
            apply = true;
        }
        boolean b = apply;
        if (this.chain != null) {
            final Iterator<Predicate<String>> iterator = this.chain.iterator();
            while (true) {
                b = apply;
                if (!iterator.hasNext()) {
                    break;
                }
                final Predicate<String> predicate = iterator.next();
                if (apply && predicate instanceof Include) {
                    continue;
                }
                if (!apply && predicate instanceof Exclude) {
                    continue;
                }
                apply = predicate.apply((Object)s);
                if (!apply && predicate instanceof Exclude) {
                    return apply;
                }
            }
        }
        return b;
    }
    
    public FilterBuilder exclude(final String s) {
        this.add((Predicate<String>)new Exclude(s));
        return this;
    }
    
    public FilterBuilder excludePackage(final Class<?> clazz) {
        return this.add((Predicate<String>)new Exclude(packageNameRegex(clazz)));
    }
    
    public FilterBuilder excludePackage(final String s) {
        return this.add((Predicate<String>)new Exclude(prefix(s)));
    }
    
    public FilterBuilder include(final String s) {
        return this.add((Predicate<String>)new Include(s));
    }
    
    public FilterBuilder includePackage(final Class<?> clazz) {
        return this.add((Predicate<String>)new Include(packageNameRegex(clazz)));
    }
    
    public FilterBuilder includePackage(final String... array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            this.add((Predicate<String>)new Include(prefix(array[i])));
        }
        return this;
    }
    
    @Override
    public String toString() {
        return Joiner.on(", ").join((Iterable)this.chain);
    }
    
    public static class Exclude extends Matcher
    {
        public Exclude(final String s) {
            super(s);
        }
        
        @Override
        public boolean apply(final String s) {
            return this.pattern.matcher(s).matches() ^ true;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("-");
            sb.append(super.toString());
            return sb.toString();
        }
    }
    
    public static class Include extends Matcher
    {
        public Include(final String s) {
            super(s);
        }
        
        @Override
        public boolean apply(final String s) {
            return this.pattern.matcher(s).matches();
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("+");
            sb.append(super.toString());
            return sb.toString();
        }
    }
    
    public abstract static class Matcher implements Predicate<String>
    {
        final Pattern pattern;
        
        public Matcher(final String s) {
            this.pattern = Pattern.compile(s);
        }
        
        public abstract boolean apply(final String p0);
        
        @Override
        public String toString() {
            return this.pattern.pattern();
        }
    }
}
