package org.spongycastle.crypto.tls;

import java.io.*;

public class CertificateStatusRequest
{
    protected Object request;
    protected short statusType;
    
    public CertificateStatusRequest(final short statusType, final Object request) {
        if (isCorrectType(statusType, request)) {
            this.statusType = statusType;
            this.request = request;
            return;
        }
        throw new IllegalArgumentException("'request' is not an instance of the correct type");
    }
    
    protected static boolean isCorrectType(final short n, final Object o) {
        if (n == 1) {
            return o instanceof OCSPStatusRequest;
        }
        throw new IllegalArgumentException("'statusType' is an unsupported CertificateStatusType");
    }
    
    public static CertificateStatusRequest parse(final InputStream inputStream) throws IOException {
        final short uint8 = TlsUtils.readUint8(inputStream);
        if (uint8 == 1) {
            return new CertificateStatusRequest(uint8, OCSPStatusRequest.parse(inputStream));
        }
        throw new TlsFatalAlert((short)50);
    }
    
    public void encode(final OutputStream outputStream) throws IOException {
        TlsUtils.writeUint8(this.statusType, outputStream);
        if (this.statusType == 1) {
            ((OCSPStatusRequest)this.request).encode(outputStream);
            return;
        }
        throw new TlsFatalAlert((short)80);
    }
    
    public OCSPStatusRequest getOCSPStatusRequest() {
        if (isCorrectType((short)1, this.request)) {
            return (OCSPStatusRequest)this.request;
        }
        throw new IllegalStateException("'request' is not an OCSPStatusRequest");
    }
    
    public Object getRequest() {
        return this.request;
    }
    
    public short getStatusType() {
        return this.statusType;
    }
}
