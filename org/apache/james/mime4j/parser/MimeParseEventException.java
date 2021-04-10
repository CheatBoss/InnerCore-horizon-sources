package org.apache.james.mime4j.parser;

import org.apache.james.mime4j.*;

public class MimeParseEventException extends MimeException
{
    private static final long serialVersionUID = 4632991604246852302L;
    private final Event event;
    
    public MimeParseEventException(final Event event) {
        super(event.toString());
        this.event = event;
    }
    
    public Event getEvent() {
        return this.event;
    }
}
