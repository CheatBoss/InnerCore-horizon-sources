package org.spongycastle.crypto.agreement.jpake;

import java.math.*;
import org.spongycastle.util.*;

public class JPAKERound1Payload
{
    private final BigInteger gx1;
    private final BigInteger gx2;
    private final BigInteger[] knowledgeProofForX1;
    private final BigInteger[] knowledgeProofForX2;
    private final String participantId;
    
    public JPAKERound1Payload(final String participantId, final BigInteger gx1, final BigInteger gx2, final BigInteger[] array, final BigInteger[] array2) {
        JPAKEUtil.validateNotNull(participantId, "participantId");
        JPAKEUtil.validateNotNull(gx1, "gx1");
        JPAKEUtil.validateNotNull(gx2, "gx2");
        JPAKEUtil.validateNotNull(array, "knowledgeProofForX1");
        JPAKEUtil.validateNotNull(array2, "knowledgeProofForX2");
        this.participantId = participantId;
        this.gx1 = gx1;
        this.gx2 = gx2;
        this.knowledgeProofForX1 = Arrays.copyOf(array, array.length);
        this.knowledgeProofForX2 = Arrays.copyOf(array2, array2.length);
    }
    
    public BigInteger getGx1() {
        return this.gx1;
    }
    
    public BigInteger getGx2() {
        return this.gx2;
    }
    
    public BigInteger[] getKnowledgeProofForX1() {
        final BigInteger[] knowledgeProofForX1 = this.knowledgeProofForX1;
        return Arrays.copyOf(knowledgeProofForX1, knowledgeProofForX1.length);
    }
    
    public BigInteger[] getKnowledgeProofForX2() {
        final BigInteger[] knowledgeProofForX2 = this.knowledgeProofForX2;
        return Arrays.copyOf(knowledgeProofForX2, knowledgeProofForX2.length);
    }
    
    public String getParticipantId() {
        return this.participantId;
    }
}
