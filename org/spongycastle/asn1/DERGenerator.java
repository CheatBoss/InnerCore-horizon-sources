package org.spongycastle.asn1;

import java.io.*;

public abstract class DERGenerator extends ASN1Generator
{
    private boolean _isExplicit;
    private int _tagNo;
    private boolean _tagged;
    
    protected DERGenerator(final OutputStream outputStream) {
        super(outputStream);
        this._tagged = false;
    }
    
    public DERGenerator(final OutputStream outputStream, final int tagNo, final boolean isExplicit) {
        super(outputStream);
        this._tagged = false;
        this._tagged = true;
        this._isExplicit = isExplicit;
        this._tagNo = tagNo;
    }
    
    private void writeLength(final OutputStream outputStream, final int n) throws IOException {
        if (n > 127) {
            int n2 = n;
            int n3 = 1;
            while (true) {
                n2 >>>= 8;
                if (n2 == 0) {
                    break;
                }
                ++n3;
            }
            outputStream.write((byte)(n3 | 0x80));
            for (int i = (n3 - 1) * 8; i >= 0; i -= 8) {
                outputStream.write((byte)(n >> i));
            }
        }
        else {
            outputStream.write((byte)n);
        }
    }
    
    void writeDEREncoded(final int n, final byte[] array) throws IOException {
        if (!this._tagged) {
            this.writeDEREncoded(this._out, n, array);
            return;
        }
        final int tagNo = this._tagNo;
        final int n2 = tagNo | 0x80;
        if (this._isExplicit) {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            this.writeDEREncoded(byteArrayOutputStream, n, array);
            this.writeDEREncoded(this._out, tagNo | 0x20 | 0x80, byteArrayOutputStream.toByteArray());
            return;
        }
        if ((n & 0x20) != 0x0) {
            this.writeDEREncoded(this._out, n2 | 0x20, array);
            return;
        }
        this.writeDEREncoded(this._out, n2, array);
    }
    
    void writeDEREncoded(final OutputStream outputStream, final int n, final byte[] array) throws IOException {
        outputStream.write(n);
        this.writeLength(outputStream, array.length);
        outputStream.write(array);
    }
}
