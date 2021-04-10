package org.spongycastle.asn1;

import java.io.*;

public interface InMemoryRepresentable
{
    ASN1Primitive getLoadedObject() throws IOException;
}
