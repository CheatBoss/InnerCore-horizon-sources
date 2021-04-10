package com.android.dx.command;

public class Main
{
    private static String USAGE_MESSAGE;
    
    static {
        Main.USAGE_MESSAGE = "usage:\n  dx --dex [--debug] [--verbose] [--positions=<style>] [--no-locals]\n  [--no-optimize] [--statistics] [--[no-]optimize-list=<file>] [--no-strict]\n  [--keep-classes] [--output=<file>] [--dump-to=<file>] [--dump-width=<n>]\n  [--dump-method=<name>[*]] [--verbose-dump] [--no-files] [--core-library]\n  [--num-threads=<n>] [--incremental] [--force-jumbo] [--no-warning]\n  [--multi-dex [--main-dex-list=<file> [--minimal-main-dex]]\n  [--input-list=<file>]\n  [<file>.class | <file>.{zip,jar,apk} | <directory>] ...\n    Convert a set of classfiles into a dex file, optionally embedded in a\n    jar/zip. Output name must end with one of: .dex .jar .zip .apk or be a directory.\n    Positions options: none, important, lines.\n    --multi-dex: allows to generate several dex files if needed. This option is \n    exclusive with --incremental, causes --num-threads to be ignored and only\n    supports folder or archive output.\n    --main-dex-list=<file>: <file> is a list of class file names, classes defined by\n    those class files are put in classes.dex.\n    --minimal-main-dex: only classes selected by --main-dex-list are to be put in\n    the main dex.\n    --input-list: <file> is a list of inputs.\n    Each line in <file> must end with one of: .class .jar .zip .apk or be a directory.\n  dx --annotool --annotation=<class> [--element=<element types>]\n  [--print=<print types>]\n  dx --dump [--debug] [--strict] [--bytes] [--optimize]\n  [--basic-blocks | --rop-blocks | --ssa-blocks | --dot] [--ssa-step=<step>]\n  [--width=<n>] [<file>.class | <file>.txt] ...\n    Dump classfiles, or transformations thereof, in a human-oriented format.\n  dx --find-usages <file.dex> <declaring type> <member>\n    Find references and declarations to a field or method.\n    declaring type: a class name in internal form, like Ljava/lang/Object;\n    member: a field or method name, like hashCode\n  dx -J<option> ... <arguments, in one of the above forms>\n    Pass VM-specific options to the virtual machine that runs dx.\n  dx --version\n    Print the version of this tool (1.11).\n  dx --help\n    Print this message.";
    }
    
    private Main() {
    }
    
    public static void main(final String[] array) {
        int n = 0;
        final boolean b = false;
        final boolean b2 = false;
        int n2;
        boolean b7;
        while (true) {
            final boolean b3 = false;
            final boolean b4 = false;
            n2 = 0;
            int n4;
            final int n3 = n4 = 0;
            boolean b5 = b3;
            boolean b6 = b4;
            b7 = b2;
            try {
                if (n < array.length) {
                    final String s = array[n];
                    n4 = n3;
                    b5 = b3;
                    b6 = b4;
                    if (!s.equals("--")) {
                        n4 = n3;
                        b5 = b3;
                        b6 = b4;
                        if (s.startsWith("--")) {
                            final boolean b8 = true;
                            final boolean b9 = true;
                            n2 = 1;
                            final int n5 = n4 = 1;
                            b5 = b8;
                            b6 = b9;
                            if (s.equals("--dex")) {
                                n4 = n5;
                                b5 = b8;
                                b6 = b9;
                                com.android.dx.command.dexer.Main.main(without(array, n));
                                b7 = b2;
                                break;
                            }
                            n4 = n5;
                            b5 = b8;
                            b6 = b9;
                            if (s.equals("--dump")) {
                                n4 = n5;
                                b5 = b8;
                                b6 = b9;
                                com.android.dx.command.dump.Main.main(without(array, n));
                                b7 = b2;
                                break;
                            }
                            n4 = n5;
                            b5 = b8;
                            b6 = b9;
                            if (s.equals("--annotool")) {
                                n4 = n5;
                                b5 = b8;
                                b6 = b9;
                                com.android.dx.command.annotool.Main.main(without(array, n));
                                b7 = b2;
                                break;
                            }
                            n4 = n5;
                            b5 = b8;
                            b6 = b9;
                            if (s.equals("--find-usages")) {
                                n4 = n5;
                                b5 = b8;
                                b6 = b9;
                                com.android.dx.command.findusages.Main.main(without(array, n));
                                b7 = b2;
                                break;
                            }
                            n4 = n5;
                            b5 = b8;
                            b6 = b9;
                            if (s.equals("--version")) {
                                n4 = n5;
                                b5 = b8;
                                b6 = b9;
                                version();
                                b7 = b2;
                                break;
                            }
                            n4 = n5;
                            b5 = b8;
                            b6 = b9;
                            if (s.equals("--help")) {
                                b7 = true;
                                break;
                            }
                            ++n;
                            continue;
                        }
                    }
                    n2 = 0;
                    b7 = true;
                }
            }
            catch (Throwable t) {
                System.err.println("\nUNEXPECTED TOP-LEVEL ERROR:");
                t.printStackTrace();
                if (t instanceof NoClassDefFoundError || t instanceof NoSuchMethodError) {
                    System.err.println("Note: You may be using an incompatible virtual machine or class library.\n(This program is known to be incompatible with recent releases of GCJ.)");
                }
                System.exit(3);
                n2 = n4;
                b7 = b;
            }
            catch (RuntimeException ex) {
                System.err.println("\nUNEXPECTED TOP-LEVEL EXCEPTION:");
                ex.printStackTrace();
                System.exit(2);
                n2 = (b5 ? 1 : 0);
                b7 = b2;
            }
            catch (UsageException ex2) {
                b7 = true;
                n2 = (b6 ? 1 : 0);
            }
            break;
        }
        if (n2 == 0) {
            System.err.println("error: no command specified");
            b7 = true;
        }
        if (b7) {
            usage();
            System.exit(1);
        }
    }
    
    private static void usage() {
        System.err.println(Main.USAGE_MESSAGE);
    }
    
    private static void version() {
        System.err.println("dx version 1.11");
        System.exit(0);
    }
    
    private static String[] without(final String[] array, final int n) {
        final int n2 = array.length - 1;
        final String[] array2 = new String[n2];
        System.arraycopy(array, 0, array2, 0, n);
        System.arraycopy(array, n + 1, array2, n, n2 - n);
        return array2;
    }
}
