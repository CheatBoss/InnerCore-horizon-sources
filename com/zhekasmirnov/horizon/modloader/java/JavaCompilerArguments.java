package com.zhekasmirnov.horizon.modloader.java;

import java.util.*;

public class JavaCompilerArguments
{
    private ArrayList<String> args;
    
    public JavaCompilerArguments(final String... args) {
        this.args = new ArrayList<String>();
        this.add(args);
    }
    
    public JavaCompilerArguments add(final String... args) {
        this.args.addAll(Arrays.asList(args));
        return this;
    }
    
    @Override
    public String toString() {
        return "Argument{args=" + this.args + '}';
    }
    
    public String[] toArray() {
        final ArrayList<String> clean = new ArrayList<String>();
        for (final String arg : this.args) {
            if (arg != null && !arg.isEmpty()) {
                clean.add(arg);
            }
        }
        final String[] array = new String[clean.size()];
        clean.toArray(array);
        return array;
    }
}
