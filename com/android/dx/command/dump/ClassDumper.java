package com.android.dx.command.dump;

import java.io.*;
import com.android.dx.util.*;
import com.android.dx.cf.direct.*;
import com.android.dx.cf.iface.*;

public final class ClassDumper extends BaseDumper
{
    private ClassDumper(final byte[] array, final PrintStream printStream, final String s, final Args args) {
        super(array, printStream, s, args);
    }
    
    public static void dump(final byte[] array, final PrintStream printStream, final String s, final Args args) {
        new ClassDumper(array, printStream, s, args).dump();
    }
    
    public void dump() {
        final byte[] bytes = this.getBytes();
        final ByteArray byteArray = new ByteArray(bytes);
        final DirectClassFile directClassFile = new DirectClassFile(byteArray, this.getFilePath(), this.getStrictParse());
        directClassFile.setAttributeFactory(StdAttributeFactory.THE_ONE);
        directClassFile.setObserver(this);
        directClassFile.getMagic();
        final int at = this.getAt();
        if (at != bytes.length) {
            this.parsed(byteArray, at, bytes.length - at, "<extra data at end of file>");
        }
    }
}
