package org.spongycastle.crypto.tls;

public class TlsFatalAlertReceived extends TlsException
{
    protected short alertDescription;
    
    public TlsFatalAlertReceived(final short alertDescription) {
        super(AlertDescription.getText(alertDescription), null);
        this.alertDescription = alertDescription;
    }
    
    public short getAlertDescription() {
        return this.alertDescription;
    }
}
