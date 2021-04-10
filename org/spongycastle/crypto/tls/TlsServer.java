package org.spongycastle.crypto.tls;

import java.io.*;
import java.util.*;

public interface TlsServer extends TlsPeer
{
    CertificateRequest getCertificateRequest() throws IOException;
    
    CertificateStatus getCertificateStatus() throws IOException;
    
    TlsCredentials getCredentials() throws IOException;
    
    TlsKeyExchange getKeyExchange() throws IOException;
    
    NewSessionTicket getNewSessionTicket() throws IOException;
    
    int getSelectedCipherSuite() throws IOException;
    
    short getSelectedCompressionMethod() throws IOException;
    
    Hashtable getServerExtensions() throws IOException;
    
    Vector getServerSupplementalData() throws IOException;
    
    ProtocolVersion getServerVersion() throws IOException;
    
    void init(final TlsServerContext p0);
    
    void notifyClientCertificate(final Certificate p0) throws IOException;
    
    void notifyClientVersion(final ProtocolVersion p0) throws IOException;
    
    void notifyFallback(final boolean p0) throws IOException;
    
    void notifyOfferedCipherSuites(final int[] p0) throws IOException;
    
    void notifyOfferedCompressionMethods(final short[] p0) throws IOException;
    
    void processClientExtensions(final Hashtable p0) throws IOException;
    
    void processClientSupplementalData(final Vector p0) throws IOException;
}
