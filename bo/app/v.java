package bo.app;

public enum v
{
    a("os_version"), 
    b("carrier"), 
    c("model"), 
    d("resolution"), 
    e("locale"), 
    f("time_zone"), 
    g("remote_notification_enabled");
    
    private String h;
    
    private v(final String h) {
        this.h = h;
    }
    
    public String a() {
        return this.h;
    }
}
