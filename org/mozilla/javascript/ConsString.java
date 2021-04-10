package org.mozilla.javascript;

import java.io.*;

public class ConsString implements CharSequence, Serializable
{
    private static final long serialVersionUID = -8432806714471372570L;
    private int depth;
    private final int length;
    private CharSequence s1;
    private CharSequence s2;
    
    public ConsString(final CharSequence s1, final CharSequence s2) {
        this.s1 = s1;
        this.s2 = s2;
        this.length = s1.length() + s2.length();
        this.depth = 1;
        if (s1 instanceof ConsString) {
            this.depth += ((ConsString)s1).depth;
        }
        if (s2 instanceof ConsString) {
            this.depth += ((ConsString)s2).depth;
        }
        if (this.depth > 2000) {
            this.flatten();
        }
    }
    
    private static void appendFragment(final CharSequence charSequence, final StringBuilder sb) {
        if (charSequence instanceof ConsString) {
            ((ConsString)charSequence).appendTo(sb);
            return;
        }
        sb.append(charSequence);
    }
    
    private void appendTo(final StringBuilder sb) {
        synchronized (this) {
            appendFragment(this.s1, sb);
            appendFragment(this.s2, sb);
        }
    }
    
    private String flatten() {
        synchronized (this) {
            if (this.depth > 0) {
                final StringBuilder sb = new StringBuilder(this.length);
                this.appendTo(sb);
                this.s1 = sb.toString();
                this.s2 = "";
                this.depth = 0;
            }
            return (String)this.s1;
        }
    }
    
    private Object writeReplace() {
        return this.toString();
    }
    
    @Override
    public char charAt(final int n) {
        String flatten;
        if (this.depth == 0) {
            flatten = (String)this.s1;
        }
        else {
            flatten = this.flatten();
        }
        return flatten.charAt(n);
    }
    
    @Override
    public int length() {
        return this.length;
    }
    
    @Override
    public CharSequence subSequence(final int n, final int n2) {
        String flatten;
        if (this.depth == 0) {
            flatten = (String)this.s1;
        }
        else {
            flatten = this.flatten();
        }
        return flatten.substring(n, n2);
    }
    
    @Override
    public String toString() {
        if (this.depth == 0) {
            return (String)this.s1;
        }
        return this.flatten();
    }
}
