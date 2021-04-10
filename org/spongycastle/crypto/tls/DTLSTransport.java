package org.spongycastle.crypto.tls;

import java.io.*;

public class DTLSTransport implements DatagramTransport
{
    private final DTLSRecordLayer recordLayer;
    
    DTLSTransport(final DTLSRecordLayer recordLayer) {
        this.recordLayer = recordLayer;
    }
    
    @Override
    public void close() throws IOException {
        this.recordLayer.close();
    }
    
    @Override
    public int getReceiveLimit() throws IOException {
        return this.recordLayer.getReceiveLimit();
    }
    
    @Override
    public int getSendLimit() throws IOException {
        return this.recordLayer.getSendLimit();
    }
    
    @Override
    public int receive(final byte[] array, int receive, final int n, final int n2) throws IOException {
        try {
            receive = this.recordLayer.receive(array, receive, n, n2);
            return receive;
        }
        catch (RuntimeException ex) {
            this.recordLayer.fail((short)80);
            throw new TlsFatalAlert((short)80, ex);
        }
        catch (IOException ex2) {
            this.recordLayer.fail((short)80);
            throw ex2;
        }
        catch (TlsFatalAlert tlsFatalAlert) {
            this.recordLayer.fail(tlsFatalAlert.getAlertDescription());
            throw tlsFatalAlert;
        }
    }
    
    @Override
    public void send(final byte[] array, final int n, final int n2) throws IOException {
        try {
            this.recordLayer.send(array, n, n2);
        }
        catch (RuntimeException ex) {
            this.recordLayer.fail((short)80);
            throw new TlsFatalAlert((short)80, ex);
        }
        catch (IOException ex2) {
            this.recordLayer.fail((short)80);
            throw ex2;
        }
        catch (TlsFatalAlert tlsFatalAlert) {
            this.recordLayer.fail(tlsFatalAlert.getAlertDescription());
            throw tlsFatalAlert;
        }
    }
}
