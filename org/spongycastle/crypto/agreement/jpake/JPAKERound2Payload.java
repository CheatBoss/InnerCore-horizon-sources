package org.spongycastle.crypto.agreement.jpake;

import java.math.*;
import org.spongycastle.util.*;

public class JPAKERound2Payload
{
    private final BigInteger a;
    private final BigInteger[] knowledgeProofForX2s;
    private final String participantId;
    
    public JPAKERound2Payload(final String participantId, final BigInteger a, final BigInteger[] array) {
        JPAKEUtil.validateNotNull(participantId, "participantId");
        JPAKEUtil.validateNotNull(a, "a");
        JPAKEUtil.validateNotNull(array, "knowledgeProofForX2s");
        this.participantId = participantId;
        this.a = a;
        this.knowledgeProofForX2s = Arrays.copyOf(array, array.length);
    }
    
    public BigInteger getA() {
        return this.a;
    }
    
    public BigInteger[] getKnowledgeProofForX2s() {
        final BigInteger[] knowledgeProofForX2s = this.knowledgeProofForX2s;
        return Arrays.copyOf(knowledgeProofForX2s, knowledgeProofForX2s.length);
    }
    
    public String getParticipantId() {
        return this.participantId;
    }
}
