package org.spongycastle.asn1;

import java.io.*;

public interface ASN1ApplicationSpecificParser extends ASN1Encodable, InMemoryRepresentable
{
    ASN1Encodable readObject() throws IOException;
}
