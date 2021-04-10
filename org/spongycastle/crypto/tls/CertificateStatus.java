package org.spongycastle.crypto.tls;

import org.spongycastle.asn1.ocsp.*;
import java.io.*;

public class CertificateStatus
{
    protected Object response;
    protected short statusType;
    
    public CertificateStatus(final short statusType, final Object response) {
        if (isCorrectType(statusType, response)) {
            this.statusType = statusType;
            this.response = response;
            return;
        }
        throw new IllegalArgumentException("'response' is not an instance of the correct type");
    }
    
    protected static boolean isCorrectType(final short n, final Object o) {
        if (n == 1) {
            return o instanceof OCSPResponse;
        }
        throw new IllegalArgumentException("'statusType' is an unsupported CertificateStatusType");
    }
    
    public static CertificateStatus parse(final InputStream inputStream) throws IOException {
        final short uint8 = TlsUtils.readUint8(inputStream);
        if (uint8 == 1) {
            return new CertificateStatus(uint8, OCSPResponse.getInstance(TlsUtils.readDERObject(TlsUtils.readOpaque24(inputStream))));
        }
        throw new TlsFatalAlert((short)50);
    }
    
    public void encode(final OutputStream outputStream) throws IOException {
        TlsUtils.writeUint8(this.statusType, outputStream);
        if (this.statusType == 1) {
            TlsUtils.writeOpaque24(((OCSPResponse)this.response).getEncoded("DER"), outputStream);
            return;
        }
        throw new TlsFatalAlert((short)80);
    }
    
    public OCSPResponse getOCSPResponse() {
        if (isCorrectType((short)1, this.response)) {
            return (OCSPResponse)this.response;
        }
        throw new IllegalStateException("'response' is not an OCSPResponse");
    }
    
    public Object getResponse() {
        return this.response;
    }
    
    public short getStatusType() {
        return this.statusType;
    }
}
