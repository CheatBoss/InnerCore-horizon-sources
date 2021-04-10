package org.spongycastle.jcajce.provider.asymmetric.x509;

import java.io.*;
import org.spongycastle.asn1.*;
import org.spongycastle.util.encoders.*;

class PEMUtil
{
    private final String _footer1;
    private final String _footer2;
    private final String _footer3;
    private final String _header1;
    private final String _header2;
    private final String _header3;
    
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
        this._header3 = "-----BEGIN PKCS7-----";
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
        this._footer3 = "-----END PKCS7-----";
    }
    
    private String readLine(final InputStream inputStream) throws IOException {
        final StringBuffer sb = new StringBuffer();
        int read;
        while (true) {
            read = inputStream.read();
            if (read != 13 && read != 10 && read >= 0) {
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
        if (read == 13) {
            inputStream.mark(1);
            final int read2 = inputStream.read();
            if (read2 == 10) {
                inputStream.mark(1);
            }
            if (read2 > 0) {
                inputStream.reset();
            }
        }
        return sb.toString();
    }
    
    ASN1Sequence readPEMObject(final InputStream inputStream) throws IOException {
        final StringBuffer sb = new StringBuffer();
        String line;
        do {
            line = this.readLine(inputStream);
        } while (line != null && !line.startsWith(this._header1) && !line.startsWith(this._header2) && !line.startsWith(this._header3));
        while (true) {
            final String line2 = this.readLine(inputStream);
            if (line2 == null || line2.startsWith(this._footer1) || line2.startsWith(this._footer2) || line2.startsWith(this._footer3)) {
                break;
            }
            sb.append(line2);
        }
        if (sb.length() != 0) {
            try {
                return ASN1Sequence.getInstance(Base64.decode(sb.toString()));
            }
            catch (Exception ex) {
                throw new IOException("malformed PEM data encountered");
            }
        }
        return null;
    }
}
