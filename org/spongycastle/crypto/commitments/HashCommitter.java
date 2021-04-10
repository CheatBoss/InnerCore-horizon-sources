package org.spongycastle.crypto.commitments;

import java.security.*;
import org.spongycastle.crypto.*;
import org.spongycastle.util.*;

public class HashCommitter implements Committer
{
    private final int byteLength;
    private final Digest digest;
    private final SecureRandom random;
    
    public HashCommitter(final ExtendedDigest digest, final SecureRandom random) {
        this.digest = digest;
        this.byteLength = digest.getByteLength();
        this.random = random;
    }
    
    private byte[] calculateCommitment(final byte[] array, final byte[] array2) {
        final byte[] array3 = new byte[this.digest.getDigestSize()];
        this.digest.update(array, 0, array.length);
        this.digest.update(array2, 0, array2.length);
        this.digest.doFinal(array3, 0);
        return array3;
    }
    
    @Override
    public Commitment commit(final byte[] array) {
        final int length = array.length;
        final int byteLength = this.byteLength;
        if (length <= byteLength / 2) {
            final byte[] array2 = new byte[byteLength - array.length];
            this.random.nextBytes(array2);
            return new Commitment(array2, this.calculateCommitment(array2, array));
        }
        throw new DataLengthException("Message to be committed to too large for digest.");
    }
    
    @Override
    public boolean isRevealed(final Commitment commitment, byte[] calculateCommitment) {
        if (calculateCommitment.length + commitment.getSecret().length == this.byteLength) {
            calculateCommitment = this.calculateCommitment(commitment.getSecret(), calculateCommitment);
            return Arrays.constantTimeAreEqual(commitment.getCommitment(), calculateCommitment);
        }
        throw new DataLengthException("Message and witness secret lengths do not match.");
    }
}
