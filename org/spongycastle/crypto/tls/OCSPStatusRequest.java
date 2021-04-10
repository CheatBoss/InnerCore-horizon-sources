package org.spongycastle.crypto.tls;

import org.spongycastle.asn1.x509.*;
import java.util.*;
import org.spongycastle.asn1.ocsp.*;
import java.io.*;
import org.spongycastle.util.io.*;

public class OCSPStatusRequest
{
    protected Extensions requestExtensions;
    protected Vector responderIDList;
    
    public OCSPStatusRequest(final Vector responderIDList, final Extensions requestExtensions) {
        this.responderIDList = responderIDList;
        this.requestExtensions = requestExtensions;
    }
    
    public static OCSPStatusRequest parse(final InputStream inputStream) throws IOException {
        final Vector<ResponderID> vector = new Vector<ResponderID>();
        final int uint16 = TlsUtils.readUint16(inputStream);
        if (uint16 > 0) {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(TlsUtils.readFully(uint16, inputStream));
            do {
                vector.addElement(ResponderID.getInstance(TlsUtils.readDERObject(TlsUtils.readOpaque16(byteArrayInputStream))));
            } while (byteArrayInputStream.available() > 0);
        }
        Extensions instance = null;
        final int uint17 = TlsUtils.readUint16(inputStream);
        if (uint17 > 0) {
            instance = Extensions.getInstance(TlsUtils.readDERObject(TlsUtils.readFully(uint17, inputStream)));
        }
        return new OCSPStatusRequest(vector, instance);
    }
    
    public void encode(final OutputStream outputStream) throws IOException {
        final Vector responderIDList = this.responderIDList;
        if (responderIDList != null && !responderIDList.isEmpty()) {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            for (int i = 0; i < this.responderIDList.size(); ++i) {
                TlsUtils.writeOpaque16(((ResponderID)this.responderIDList.elementAt(i)).getEncoded("DER"), byteArrayOutputStream);
            }
            TlsUtils.checkUint16(byteArrayOutputStream.size());
            TlsUtils.writeUint16(byteArrayOutputStream.size(), outputStream);
            Streams.writeBufTo(byteArrayOutputStream, outputStream);
        }
        else {
            TlsUtils.writeUint16(0, outputStream);
        }
        final Extensions requestExtensions = this.requestExtensions;
        if (requestExtensions == null) {
            TlsUtils.writeUint16(0, outputStream);
            return;
        }
        final byte[] encoded = requestExtensions.getEncoded("DER");
        TlsUtils.checkUint16(encoded.length);
        TlsUtils.writeUint16(encoded.length, outputStream);
        outputStream.write(encoded);
    }
    
    public Extensions getRequestExtensions() {
        return this.requestExtensions;
    }
    
    public Vector getResponderIDList() {
        return this.responderIDList;
    }
}
