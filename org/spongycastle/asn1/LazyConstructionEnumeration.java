package org.spongycastle.asn1;

import java.util.*;
import java.io.*;

class LazyConstructionEnumeration implements Enumeration
{
    private ASN1InputStream aIn;
    private Object nextObj;
    
    public LazyConstructionEnumeration(final byte[] array) {
        this.aIn = new ASN1InputStream(array, true);
        this.nextObj = this.readObject();
    }
    
    private Object readObject() {
        try {
            return this.aIn.readObject();
        }
        catch (IOException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("malformed DER construction: ");
            sb.append(ex);
            throw new ASN1ParsingException(sb.toString(), ex);
        }
    }
    
    @Override
    public boolean hasMoreElements() {
        return this.nextObj != null;
    }
    
    @Override
    public Object nextElement() {
        final Object nextObj = this.nextObj;
        this.nextObj = this.readObject();
        return nextObj;
    }
}
