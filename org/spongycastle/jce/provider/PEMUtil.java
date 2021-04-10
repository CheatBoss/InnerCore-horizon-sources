package org.spongycastle.jce.provider;

import java.io.*;
import org.spongycastle.util.encoders.*;
import org.spongycastle.asn1.*;

public class PEMUtil
{
    private final String _footer1;
    private final String _footer2;
    private final String _header1;
    private final String _header2;
    
    PEMUtil(final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append("-----BEGIN ");
        sb.append(s);
        sb.append("-----");
        this._header1 = sb.toString();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("-----BEGIN X509 ");
        sb2.append(s);
        sb2.append("-----");
        this._header2 = sb2.toString();
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("-----END ");
        sb3.append(s);
        sb3.append("-----");
        this._footer1 = sb3.toString();
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("-----END X509 ");
        sb4.append(s);
        sb4.append("-----");
        this._footer2 = sb4.toString();
    }
    
    private String readLine(final InputStream inputStream) throws IOException {
        final StringBuffer sb = new StringBuffer();
        int read;
        while (true) {
            read = inputStream.read();
            if (read != 13 && read != 10 && read >= 0) {
                if (read == 13) {
                    continue;
                }
                sb.append((char)read);
            }
            else {
                if (read < 0 || sb.length() != 0) {
                    break;
                }
                continue;
            }
        }
        if (read < 0) {
            return null;
        }
        return sb.toString();
    }
    
    ASN1Sequence readPEMObject(final InputStream inputStream) throws IOException {
        final StringBuffer sb = new StringBuffer();
        String line;
        do {
            line = this.readLine(inputStream);
        } while (line != null && !line.startsWith(this._header1) && !line.startsWith(this._header2));
        while (true) {
            final String line2 = this.readLine(inputStream);
            if (line2 == null || line2.startsWith(this._footer1) || line2.startsWith(this._footer2)) {
                break;
            }
            sb.append(line2);
        }
        if (sb.length() == 0) {
            return null;
        }
        final ASN1Primitive object = new ASN1InputStream(Base64.decode(sb.toString())).readObject();
        if (object instanceof ASN1Sequence) {
            return (ASN1Sequence)object;
        }
        throw new IOException("malformed PEM data encountered");
    }
}
