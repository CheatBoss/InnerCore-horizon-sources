package org.spongycastle.asn1;

import java.io.*;

public interface ASN1TaggedObjectParser extends ASN1Encodable, InMemoryRepresentable
{
    ASN1Encodable getObjectParser(final int p0, final boolean p1) throws IOException;
    
    int getTagNo();
}
