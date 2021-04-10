package org.spongycastle.asn1;

import java.io.*;

public interface ASN1OctetStringParser extends ASN1Encodable, InMemoryRepresentable
{
    InputStream getOctetStream();
}
