package net.lingala.zip4j.exception;

public class ZipException extends Exception
{
    private static final long serialVersionUID = 1L;
    private int code;
    
    public ZipException() {
        this.code = -1;
    }
    
    public ZipException(final String s) {
        super(s);
        this.code = -1;
    }
    
    public ZipException(final String s, final int code) {
        super(s);
        this.code = -1;
        this.code = code;
    }
    
    public ZipException(final String s, final Throwable t) {
        super(s, t);
        this.code = -1;
    }
    
    public ZipException(final String s, final Throwable t, final int code) {
        super(s, t);
        this.code = -1;
        this.code = code;
    }
    
    public ZipException(final Throwable t) {
        super(t);
        this.code = -1;
    }
    
    public ZipException(final Throwable t, final int code) {
        super(t);
        this.code = -1;
        this.code = code;
    }
    
    public int getCode() {
        return this.code;
    }
}
