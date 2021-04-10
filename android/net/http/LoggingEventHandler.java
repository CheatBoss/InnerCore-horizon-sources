package android.net.http;

public class LoggingEventHandler implements EventHandler
{
    public LoggingEventHandler() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void certificate(final SslCertificate sslCertificate) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void data(final byte[] array, final int n) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void endData() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void error(final int n, final String s) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean handleSslErrorRequest(final SslError sslError) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void headers(final Headers headers) {
        throw new RuntimeException("Stub!");
    }
    
    public void locationChanged(final String s, final boolean b) {
        throw new RuntimeException("Stub!");
    }
    
    public void requestSent() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void status(final int n, final int n2, final int n3, final String s) {
        throw new RuntimeException("Stub!");
    }
}
