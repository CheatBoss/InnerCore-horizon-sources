package com.android.dx.command.findusages;

import com.android.dex.*;
import java.io.*;

public final class Main
{
    public static void main(final String[] array) throws IOException {
        final String s = array[0];
        final String s2 = array[1];
        final String s3 = array[2];
        final Dex dex = new Dex(new File(s));
        final PrintWriter printWriter = new PrintWriter(System.out);
        new FindUsages(dex, s2, s3, printWriter).findUsages();
        printWriter.flush();
    }
}
