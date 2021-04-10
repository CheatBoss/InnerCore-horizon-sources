package org.spongycastle.crypto.tls;

import java.io.*;
import java.util.*;

public interface TlsClient extends TlsPeer
{
    TlsAuthentication getAuthentication() throws IOException;
    
    int[] getCipherSuites();
    
    Hashtable getClientExtensions() throws IOException;
    
    ProtocolVersion getClientHelloRecordLayerVersion();
    
    Vector getClientSupplementalData() throws IOException;
    
    ProtocolVersion getClientVersion();
    
    short[] getCompressionMethods();
    
    TlsKeyExchange getKeyExchange() throws IOException;
    
    TlsSession getSessionToResume();
    
    void init(final TlsClientContext p0);
    
    boolean isFallback();
    
    void notifyNewSessionTicket(final NewSessionTicket p0) throws IOException;
    
    void notifySelectedCipherSuite(final int p0);
    
    void notifySelectedCompressionMethod(final short p0);
    
    void notifyServerVersion(final ProtocolVersion p0) throws IOException;
    
    void notifySessionID(final byte[] p0);
    
    void processServerExtensions(final Hashtable p0) throws IOException;
    
    void processServerSupplementalData(final Vector p0) throws IOException;
}
