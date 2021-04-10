package org.spongycastle.crypto.tls;

public class TlsFatalAlert extends TlsException
{
    protected short alertDescription;
    
    public TlsFatalAlert(final short n) {
        this(n, null);
    }
    
    public TlsFatalAlert(final short alertDescription, final Throwable t) {
        super(AlertDescription.getText(alertDescription), t);
        this.alertDescription = alertDescription;
    }
    
    public short getAlertDescription() {
        return this.alertDescription;
    }
}
