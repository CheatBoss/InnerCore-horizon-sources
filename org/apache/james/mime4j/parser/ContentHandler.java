package org.apache.james.mime4j.parser;

import org.apache.james.mime4j.descriptor.*;
import org.apache.james.mime4j.*;
import java.io.*;

public interface ContentHandler
{
    void body(final BodyDescriptor p0, final InputStream p1) throws MimeException, IOException;
    
    void endBodyPart() throws MimeException;
    
    void endHeader() throws MimeException;
    
    void endMessage() throws MimeException;
    
    void endMultipart() throws MimeException;
    
    void epilogue(final InputStream p0) throws MimeException, IOException;
    
    void field(final Field p0) throws MimeException;
    
    void preamble(final InputStream p0) throws MimeException, IOException;
    
    void raw(final InputStream p0) throws MimeException, IOException;
    
    void startBodyPart() throws MimeException;
    
    void startHeader() throws MimeException;
    
    void startMessage() throws MimeException;
    
    void startMultipart(final BodyDescriptor p0) throws MimeException;
}
