package com.android.dx.command.grep;

import com.android.dex.*;
import java.util.regex.*;
import java.io.*;

public final class Main
{
    public static void main(final String[] array) throws IOException {
        int n = 0;
        if (new Grep(new Dex(new File(array[0])), Pattern.compile(array[1]), new PrintWriter(System.out)).grep() <= 0) {
            n = 1;
        }
        System.exit(n);
    }
}
