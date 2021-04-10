package com.android.dx.command.dump;

import com.android.dex.util.*;
import com.android.dx.util.*;
import com.android.dx.cf.iface.*;
import java.io.*;

public class Main
{
    static Args parsedArgs;
    
    static {
        Main.parsedArgs = new Args();
    }
    
    private Main() {
    }
    
    public static void main(final String[] array) {
        int i;
        for (i = 0; i < array.length; ++i) {
            final String s = array[i];
            if (s.equals("--")) {
                break;
            }
            if (!s.startsWith("--")) {
                break;
            }
            if (s.equals("--bytes")) {
                Main.parsedArgs.rawBytes = true;
            }
            else if (s.equals("--basic-blocks")) {
                Main.parsedArgs.basicBlocks = true;
            }
            else if (s.equals("--rop-blocks")) {
                Main.parsedArgs.ropBlocks = true;
            }
            else if (s.equals("--optimize")) {
                Main.parsedArgs.optimize = true;
            }
            else if (s.equals("--ssa-blocks")) {
                Main.parsedArgs.ssaBlocks = true;
            }
            else if (s.startsWith("--ssa-step=")) {
                Main.parsedArgs.ssaStep = s.substring(s.indexOf(61) + 1);
            }
            else if (s.equals("--debug")) {
                Main.parsedArgs.debug = true;
            }
            else if (s.equals("--dot")) {
                Main.parsedArgs.dotDump = true;
            }
            else if (s.equals("--strict")) {
                Main.parsedArgs.strictParse = true;
            }
            else if (s.startsWith("--width=")) {
                Main.parsedArgs.width = Integer.parseInt(s.substring(s.indexOf(61) + 1));
            }
            else {
                if (!s.startsWith("--method=")) {
                    final PrintStream err = System.err;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("unknown option: ");
                    sb.append(s);
                    err.println(sb.toString());
                    throw new RuntimeException("usage");
                }
                Main.parsedArgs.method = s.substring(s.indexOf(61) + 1);
            }
        }
        int j;
        if ((j = i) == array.length) {
            System.err.println("no input files specified");
            throw new RuntimeException("usage");
        }
        while (j < array.length) {
            final String s2 = array[j];
            try {
                final PrintStream out = System.out;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("reading ");
                sb2.append(s2);
                sb2.append("...");
                out.println(sb2.toString());
                final byte[] file = FileUtils.readFile(s2);
                final boolean endsWith = s2.endsWith(".class");
                byte[] parse = file;
                if (!endsWith) {
                    try {
                        parse = HexParser.parse(new String(file, "utf-8"));
                    }
                    catch (UnsupportedEncodingException ex) {
                        throw new RuntimeException("shouldn't happen", ex);
                    }
                }
                processOne(s2, parse);
            }
            catch (ParseException ex2) {
                System.err.println("\ntrouble parsing:");
                if (Main.parsedArgs.debug) {
                    ex2.printStackTrace();
                }
                else {
                    ex2.printContext(System.err);
                }
            }
            ++j;
        }
    }
    
    private static void processOne(final String s, final byte[] array) {
        if (Main.parsedArgs.dotDump) {
            DotDumper.dump(array, s, Main.parsedArgs);
            return;
        }
        if (Main.parsedArgs.basicBlocks) {
            BlockDumper.dump(array, System.out, s, false, Main.parsedArgs);
            return;
        }
        if (Main.parsedArgs.ropBlocks) {
            BlockDumper.dump(array, System.out, s, true, Main.parsedArgs);
            return;
        }
        if (Main.parsedArgs.ssaBlocks) {
            Main.parsedArgs.optimize = false;
            SsaDumper.dump(array, System.out, s, Main.parsedArgs);
            return;
        }
        ClassDumper.dump(array, System.out, s, Main.parsedArgs);
    }
}
