package org.spongycastle.util.io.pem;

import org.spongycastle.util.*;
import org.spongycastle.util.encoders.*;
import java.io.*;
import java.util.*;

public class PemWriter extends BufferedWriter
{
    private static final int LINE_LENGTH = 64;
    private char[] buf;
    private final int nlLength;
    
    public PemWriter(final Writer writer) {
        super(writer);
        this.buf = new char[64];
        final String lineSeparator = Strings.lineSeparator();
        int length;
        if (lineSeparator != null) {
            length = lineSeparator.length();
        }
        else {
            length = 2;
        }
        this.nlLength = length;
    }
    
    private void writeEncoded(byte[] encode) throws IOException {
        encode = Base64.encode(encode);
        for (int i = 0; i < encode.length; i += this.buf.length) {
            int n = 0;
            while (true) {
                final char[] buf = this.buf;
                if (n == buf.length) {
                    break;
                }
                final int n2 = i + n;
                if (n2 >= encode.length) {
                    break;
                }
                buf[n] = (char)encode[n2];
                ++n;
            }
            this.write(this.buf, 0, n);
            this.newLine();
        }
    }
    
    private void writePostEncapsulationBoundary(final String s) throws IOException {
        final StringBuilder sb = new StringBuilder();
        sb.append("-----END ");
        sb.append(s);
        sb.append("-----");
        this.write(sb.toString());
        this.newLine();
    }
    
    private void writePreEncapsulationBoundary(final String s) throws IOException {
        final StringBuilder sb = new StringBuilder();
        sb.append("-----BEGIN ");
        sb.append(s);
        sb.append("-----");
        this.write(sb.toString());
        this.newLine();
    }
    
    public int getOutputSize(final PemObject pemObject) {
        int n2;
        int n = n2 = (pemObject.getType().length() + 10 + this.nlLength) * 2 + 6 + 4;
        if (!pemObject.getHeaders().isEmpty()) {
            for (final PemHeader pemHeader : pemObject.getHeaders()) {
                n += pemHeader.getName().length() + 2 + pemHeader.getValue().length() + this.nlLength;
            }
            n2 = n + this.nlLength;
        }
        final int n3 = (pemObject.getContent().length + 2) / 3 * 4;
        return n2 + (n3 + (n3 + 64 - 1) / 64 * this.nlLength);
    }
    
    public void writeObject(final PemObjectGenerator pemObjectGenerator) throws IOException {
        final PemObject generate = pemObjectGenerator.generate();
        this.writePreEncapsulationBoundary(generate.getType());
        if (!generate.getHeaders().isEmpty()) {
            for (final PemHeader pemHeader : generate.getHeaders()) {
                this.write(pemHeader.getName());
                this.write(": ");
                this.write(pemHeader.getValue());
                this.newLine();
            }
            this.newLine();
        }
        this.writeEncoded(generate.getContent());
        this.writePostEncapsulationBoundary(generate.getType());
    }
}
