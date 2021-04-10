package org.spongycastle.util.io.pem;

import org.spongycastle.util.encoders.*;
import java.util.*;
import java.io.*;

public class PemReader extends BufferedReader
{
    private static final String BEGIN = "-----BEGIN ";
    private static final String END = "-----END ";
    
    public PemReader(final Reader reader) {
        super(reader);
    }
    
    private PemObject loadObject(final String s) throws IOException {
        final StringBuilder sb = new StringBuilder();
        sb.append("-----END ");
        sb.append(s);
        final String string = sb.toString();
        final StringBuffer sb2 = new StringBuffer();
        final ArrayList<PemHeader> list = new ArrayList<PemHeader>();
        String line;
        while (true) {
            line = this.readLine();
            if (line == null) {
                break;
            }
            if (line.indexOf(":") >= 0) {
                final int index = line.indexOf(58);
                list.add(new PemHeader(line.substring(0, index), line.substring(index + 1).trim()));
            }
            else {
                if (line.indexOf(string) != -1) {
                    break;
                }
                sb2.append(line.trim());
            }
        }
        if (line != null) {
            return new PemObject(s, list, Base64.decode(sb2.toString()));
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(string);
        sb3.append(" not found");
        throw new IOException(sb3.toString());
    }
    
    public PemObject readPemObject() throws IOException {
        String line;
        do {
            line = this.readLine();
        } while (line != null && !line.startsWith("-----BEGIN "));
        if (line != null) {
            final String substring = line.substring(11);
            final int index = substring.indexOf(45);
            final String substring2 = substring.substring(0, index);
            if (index > 0) {
                return this.loadObject(substring2);
            }
        }
        return null;
    }
}
