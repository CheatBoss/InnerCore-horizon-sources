package org.spongycastle.asn1;

import java.io.*;

class ConstructedOctetStream extends InputStream
{
    private InputStream _currentStream;
    private boolean _first;
    private final ASN1StreamParser _parser;
    
    ConstructedOctetStream(final ASN1StreamParser parser) {
        this._first = true;
        this._parser = parser;
    }
    
    @Override
    public int read() throws IOException {
        while (true) {
            Label_0048: {
                if (this._currentStream != null) {
                    break Label_0048;
                }
                if (!this._first) {
                    return -1;
                }
                final ASN1OctetStringParser asn1OctetStringParser = (ASN1OctetStringParser)this._parser.readObject();
                if (asn1OctetStringParser == null) {
                    return -1;
                }
                this._first = false;
                this._currentStream = asn1OctetStringParser.getOctetStream();
            }
            final int read = this._currentStream.read();
            if (read >= 0) {
                return read;
            }
            ASN1OctetStringParser asn1OctetStringParser;
            if ((asn1OctetStringParser = (ASN1OctetStringParser)this._parser.readObject()) == null) {
                this._currentStream = null;
                return -1;
            }
            continue;
        }
    }
    
    @Override
    public int read(final byte[] array, final int n, final int n2) throws IOException {
        final InputStream currentStream = this._currentStream;
        int n3 = 0;
        int n4 = 0;
        while (true) {
            ASN1OctetStringParser asn1OctetStringParser = null;
            Label_0137: {
                if (currentStream == null) {
                    if (!this._first) {
                        return -1;
                    }
                    asn1OctetStringParser = (ASN1OctetStringParser)this._parser.readObject();
                    if (asn1OctetStringParser == null) {
                        return -1;
                    }
                    this._first = false;
                    break Label_0137;
                }
                int n5;
                do {
                    final int read = this._currentStream.read(array, n + n4, n2 - n4);
                    if (read >= 0) {
                        n5 = n4 + read;
                    }
                    else {
                        final ASN1OctetStringParser asn1OctetStringParser2 = asn1OctetStringParser = (ASN1OctetStringParser)this._parser.readObject();
                        n3 = n4;
                        if (asn1OctetStringParser2 != null) {
                            break Label_0137;
                        }
                        this._currentStream = null;
                        if (n4 < 1) {
                            return -1;
                        }
                        return n4;
                    }
                } while ((n4 = n5) != n2);
                return n5;
            }
            this._currentStream = asn1OctetStringParser.getOctetStream();
            n4 = n3;
            continue;
        }
    }
}
