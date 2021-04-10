package com.android.dx.command.annotool;

import java.lang.annotation.*;
import java.util.*;

public class Main
{
    private Main() {
    }
    
    public static void main(final String[] array) {
        final Arguments arguments = new Arguments();
        try {
            arguments.parse(array);
            new AnnotationLister(arguments).process();
        }
        catch (InvalidArgumentException ex) {
            System.err.println(ex.getMessage());
            throw new RuntimeException("usage");
        }
    }
    
    static class Arguments
    {
        String aclass;
        EnumSet<ElementType> eTypes;
        String[] files;
        EnumSet<PrintType> printTypes;
        
        Arguments() {
            this.eTypes = EnumSet.noneOf(ElementType.class);
            this.printTypes = EnumSet.noneOf(PrintType.class);
        }
        
        void parse(final String[] array) throws InvalidArgumentException {
            int i = 0;
            while (i < array.length) {
                final String s = array[i];
                Label_0246: {
                    if (s.startsWith("--annotation=")) {
                        final String substring = s.substring(s.indexOf(61) + 1);
                        if (this.aclass != null) {
                            throw new InvalidArgumentException("--annotation can only be specified once.");
                        }
                        this.aclass = substring.replace('.', '/');
                        break Label_0246;
                    }
                    else {
                        if (s.startsWith("--element=")) {
                            final String substring2 = s.substring(s.indexOf(61) + 1);
                            try {
                                final String[] split = substring2.split(",");
                                for (int length = split.length, j = 0; j < length; ++j) {
                                    this.eTypes.add(ElementType.valueOf(split[j].toUpperCase(Locale.ROOT)));
                                }
                                break Label_0246;
                            }
                            catch (IllegalArgumentException ex) {
                                throw new InvalidArgumentException("invalid --element");
                            }
                        }
                        if (!s.startsWith("--print=")) {
                            break Label_0246;
                        }
                    }
                    final String substring3 = s.substring(s.indexOf(61) + 1);
                    try {
                        final String[] split2 = substring3.split(",");
                        for (int length2 = split2.length, k = 0; k < length2; ++k) {
                            this.printTypes.add(PrintType.valueOf(split2[k].toUpperCase(Locale.ROOT)));
                        }
                        ++i;
                        continue;
                    }
                    catch (IllegalArgumentException ex2) {
                        throw new InvalidArgumentException("invalid --print");
                    }
                }
                System.arraycopy(array, i, this.files = new String[array.length - i], 0, this.files.length);
                break;
            }
            if (this.aclass == null) {
                throw new InvalidArgumentException("--annotation must be specified");
            }
            if (this.printTypes.isEmpty()) {
                this.printTypes.add(PrintType.CLASS);
            }
            if (this.eTypes.isEmpty()) {
                this.eTypes.add(ElementType.TYPE);
            }
            final EnumSet<ElementType> clone = this.eTypes.clone();
            clone.remove(ElementType.TYPE);
            clone.remove(ElementType.PACKAGE);
            if (!clone.isEmpty()) {
                throw new InvalidArgumentException("only --element parameters 'type' and 'package' supported");
            }
        }
    }
    
    private static class InvalidArgumentException extends Exception
    {
        InvalidArgumentException() {
        }
        
        InvalidArgumentException(final String s) {
            super(s);
        }
    }
    
    enum PrintType
    {
        CLASS, 
        INNERCLASS, 
        METHOD, 
        PACKAGE;
    }
}
