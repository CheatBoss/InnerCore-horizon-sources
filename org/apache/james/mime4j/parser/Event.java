package org.apache.james.mime4j.parser;

public final class Event
{
    public static final Event HEADERS_PREMATURE_END;
    public static final Event INALID_HEADER;
    public static final Event MIME_BODY_PREMATURE_END;
    private final String code;
    
    static {
        MIME_BODY_PREMATURE_END = new Event("Body part ended prematurely. Boundary detected in header or EOF reached.");
        HEADERS_PREMATURE_END = new Event("Unexpected end of headers detected. Higher level boundary detected or EOF reached.");
        INALID_HEADER = new Event("Invalid header encountered");
    }
    
    public Event(final String code) {
        if (code != null) {
            this.code = code;
            return;
        }
        throw new IllegalArgumentException("Code may not be null");
    }
    
    @Override
    public boolean equals(final Object o) {
        return o != null && (this == o || (o instanceof Event && this.code.equals(((Event)o).code)));
    }
    
    @Override
    public int hashCode() {
        return this.code.hashCode();
    }
    
    @Override
    public String toString() {
        return this.code;
    }
}
