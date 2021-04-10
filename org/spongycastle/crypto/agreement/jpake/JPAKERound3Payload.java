package org.spongycastle.crypto.agreement.jpake;

import java.math.*;

public class JPAKERound3Payload
{
    private final BigInteger macTag;
    private final String participantId;
    
    public JPAKERound3Payload(final String participantId, final BigInteger macTag) {
        this.participantId = participantId;
        this.macTag = macTag;
    }
    
    public BigInteger getMacTag() {
        return this.macTag;
    }
    
    public String getParticipantId() {
        return this.participantId;
    }
}
