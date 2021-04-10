package org.apache.james.mime4j.parser;

import org.apache.james.mime4j.descriptor.*;
import org.apache.james.mime4j.*;
import java.io.*;

public abstract class AbstractContentHandler implements ContentHandler
{
    @Override
    public void body(final BodyDescriptor bodyDescriptor, final InputStream inputStream) throws MimeException, IOException {
    }
    
    @Override
    public void endBodyPart() throws MimeException {
    }
    
    @Override
    public void endHeader() throws MimeException {
    }
    
    @Override
    public void endMessage() throws MimeException {
    }
    
    @Override
    public void endMultipart() throws MimeException {
    }
    
    @Override
    public void epilogue(final InputStream inputStream) throws MimeException, IOException {
    }
    
    @Override
    public void field(final Field field) throws MimeException {
    }
    
    @Override
    public void preamble(final InputStream inputStream) throws MimeException, IOException {
    }
    
    @Override
    public void raw(final InputStream inputStream) throws MimeException, IOException {
    }
    
    @Override
    public void startBodyPart() throws MimeException {
    }
    
    @Override
    public void startHeader() throws MimeException {
    }
    
    @Override
    public void startMessage() throws MimeException {
    }
    
    @Override
    public void startMultipart(final BodyDescriptor bodyDescriptor) throws MimeException {
    }
}
