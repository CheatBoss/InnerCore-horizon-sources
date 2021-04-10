package android.net.http;

import java.io.*;
import javax.net.ssl.*;

public class CertificateChainValidator
{
    CertificateChainValidator() {
        throw new RuntimeException("Stub!");
    }
    
    public static CertificateChainValidator getInstance() {
        throw new RuntimeException("Stub!");
    }
    
    public static void handleTrustStorageUpdate() {
        throw new RuntimeException("Stub!");
    }
    
    public static SslError verifyServerCertificates(final byte[][] array, final String s, final String s2) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    public SslError doHandshakeAndValidateServerCertificates(final HttpsConnection httpsConnection, final SSLSocket sslSocket, final String s) throws IOException {
        throw new RuntimeException("Stub!");
    }
}
