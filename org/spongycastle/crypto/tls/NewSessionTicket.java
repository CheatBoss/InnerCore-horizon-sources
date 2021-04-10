package org.spongycastle.crypto.tls;

import java.io.*;

public class NewSessionTicket
{
    protected byte[] ticket;
    protected long ticketLifetimeHint;
    
    public NewSessionTicket(final long ticketLifetimeHint, final byte[] ticket) {
        this.ticketLifetimeHint = ticketLifetimeHint;
        this.ticket = ticket;
    }
    
    public static NewSessionTicket parse(final InputStream inputStream) throws IOException {
        return new NewSessionTicket(TlsUtils.readUint32(inputStream), TlsUtils.readOpaque16(inputStream));
    }
    
    public void encode(final OutputStream outputStream) throws IOException {
        TlsUtils.writeUint32(this.ticketLifetimeHint, outputStream);
        TlsUtils.writeOpaque16(this.ticket, outputStream);
    }
    
    public byte[] getTicket() {
        return this.ticket;
    }
    
    public long getTicketLifetimeHint() {
        return this.ticketLifetimeHint;
    }
}
