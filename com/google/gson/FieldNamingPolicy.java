package com.google.gson;

import java.lang.reflect.*;
import java.util.*;

public enum FieldNamingPolicy implements FieldNamingStrategy
{
    IDENTITY {
        @Override
        public String translateName(final Field field) {
            return field.getName();
        }
    }, 
    LOWER_CASE_WITH_DASHES {
        @Override
        public String translateName(final Field field) {
            return separateCamelCase(field.getName(), "-").toLowerCase(Locale.ENGLISH);
        }
    }, 
    LOWER_CASE_WITH_UNDERSCORES {
        @Override
        public String translateName(final Field field) {
            return separateCamelCase(field.getName(), "_").toLowerCase(Locale.ENGLISH);
        }
    }, 
    UPPER_CAMEL_CASE {
        @Override
        public String translateName(final Field field) {
            return upperCaseFirstLetter(field.getName());
        }
    }, 
    UPPER_CAMEL_CASE_WITH_SPACES {
        @Override
        public String translateName(final Field field) {
            return upperCaseFirstLetter(separateCamelCase(field.getName(), " "));
        }
    };
    
    private static String modifyString(final char c, final String s, final int n) {
        if (n < s.length()) {
            final StringBuilder sb = new StringBuilder();
            sb.append(c);
            sb.append(s.substring(n));
            return sb.toString();
        }
        return String.valueOf(c);
    }
    
    private static String separateCamelCase(final String s, final String s2) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); ++i) {
            final char char1 = s.charAt(i);
            if (Character.isUpperCase(char1) && sb.length() != 0) {
                sb.append(s2);
            }
            sb.append(char1);
        }
        return sb.toString();
    }
    
    private static String upperCaseFirstLetter(final String s) {
        final StringBuilder sb = new StringBuilder();
        int n = 0;
        char char1;
        while (true) {
            char1 = s.charAt(n);
            if (n >= s.length() - 1 || Character.isLetter(char1)) {
                break;
            }
            sb.append(char1);
            ++n;
        }
        if (n == s.length()) {
            return sb.toString();
        }
        String string = s;
        if (!Character.isUpperCase(char1)) {
            sb.append(modifyString(Character.toUpperCase(char1), s, n + 1));
            string = sb.toString();
        }
        return string;
    }
}
