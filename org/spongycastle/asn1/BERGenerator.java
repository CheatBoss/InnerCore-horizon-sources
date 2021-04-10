package org.spongycastle.asn1;

import java.io.*;

public class BERGenerator extends ASN1Generator
{
    private boolean _isExplicit;
    private int _tagNo;
    private boolean _tagged;
    
    protected BERGenerator(final OutputStream outputStream) {
        super(outputStream);
        this._tagged = false;
    }
    
    protected BERGenerator(final OutputStream outputStream, final int tagNo, final boolean isExplicit) {
        super(outputStream);
        this._tagged = false;
        this._tagged = true;
        this._isExplicit = isExplicit;
        this._tagNo = tagNo;
    }
    
    private void writeHdr(final int n) throws IOException {
        this._out.write(n);
        this._out.write(128);
    }
    
    @Override
    public OutputStream getRawOutputStream() {
        return this._out;
    }
    
    protected void writeBEREnd() throws IOException {
        this._out.write(0);
        this._out.write(0);
        if (this._tagged && this._isExplicit) {
            this._out.write(0);
            this._out.write(0);
        }
    }
    
    protected void writeBERHeader(int n) throws IOException {
        if (this._tagged) {
            final int n2 = this._tagNo | 0x80;
            if (this._isExplicit) {
                this.writeHdr(n2 | 0x20);
            }
            else {
                if ((n & 0x20) == 0x0) {
                    this.writeHdr(n2);
                    return;
                }
                n = (n2 | 0x20);
            }
            this.writeHdr(n);
            return;
        }
        this.writeHdr(n);
    }
}
