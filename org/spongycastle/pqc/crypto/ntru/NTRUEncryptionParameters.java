package org.spongycastle.pqc.crypto.ntru;

import org.spongycastle.crypto.digests.*;
import org.spongycastle.crypto.*;
import java.util.*;
import java.io.*;

public class NTRUEncryptionParameters implements Cloneable
{
    public int N;
    public int bufferLenBits;
    int bufferLenTrits;
    public int c;
    public int db;
    public int df;
    public int df1;
    public int df2;
    public int df3;
    public int dg;
    public int dm0;
    public int dr;
    public int dr1;
    public int dr2;
    public int dr3;
    public boolean fastFp;
    public Digest hashAlg;
    public boolean hashSeed;
    int llen;
    public int maxMsgLenBytes;
    public int minCallsMask;
    public int minCallsR;
    public byte[] oid;
    public int pkLen;
    public int polyType;
    public int q;
    public boolean sparse;
    
    public NTRUEncryptionParameters(final int n, final int q, final int df1, final int df2, final int df3, final int dm0, final int db, final int c, final int minCallsR, final int minCallsMask, final boolean hashSeed, final byte[] oid, final boolean sparse, final boolean fastFp, final Digest hashAlg) {
        this.N = n;
        this.q = q;
        this.df1 = df1;
        this.df2 = df2;
        this.df3 = df3;
        this.db = db;
        this.dm0 = dm0;
        this.c = c;
        this.minCallsR = minCallsR;
        this.minCallsMask = minCallsMask;
        this.hashSeed = hashSeed;
        this.oid = oid;
        this.sparse = sparse;
        this.fastFp = fastFp;
        this.polyType = 1;
        this.hashAlg = hashAlg;
        this.init();
    }
    
    public NTRUEncryptionParameters(final int n, final int q, final int df, final int dm0, final int db, final int c, final int minCallsR, final int minCallsMask, final boolean hashSeed, final byte[] oid, final boolean sparse, final boolean fastFp, final Digest hashAlg) {
        this.N = n;
        this.q = q;
        this.df = df;
        this.db = db;
        this.dm0 = dm0;
        this.c = c;
        this.minCallsR = minCallsR;
        this.minCallsMask = minCallsMask;
        this.hashSeed = hashSeed;
        this.oid = oid;
        this.sparse = sparse;
        this.fastFp = fastFp;
        this.polyType = 0;
        this.hashAlg = hashAlg;
        this.init();
    }
    
    public NTRUEncryptionParameters(final InputStream inputStream) throws IOException {
        final DataInputStream dataInputStream = new DataInputStream(inputStream);
        this.N = dataInputStream.readInt();
        this.q = dataInputStream.readInt();
        this.df = dataInputStream.readInt();
        this.df1 = dataInputStream.readInt();
        this.df2 = dataInputStream.readInt();
        this.df3 = dataInputStream.readInt();
        this.db = dataInputStream.readInt();
        this.dm0 = dataInputStream.readInt();
        this.c = dataInputStream.readInt();
        this.minCallsR = dataInputStream.readInt();
        this.minCallsMask = dataInputStream.readInt();
        this.hashSeed = dataInputStream.readBoolean();
        dataInputStream.read(this.oid = new byte[3]);
        this.sparse = dataInputStream.readBoolean();
        this.fastFp = dataInputStream.readBoolean();
        this.polyType = dataInputStream.read();
        final String utf = dataInputStream.readUTF();
        Label_0198: {
            ExtendedDigest hashAlg;
            if ("SHA-512".equals(utf)) {
                hashAlg = new SHA512Digest();
            }
            else {
                if (!"SHA-256".equals(utf)) {
                    break Label_0198;
                }
                hashAlg = new SHA256Digest();
            }
            this.hashAlg = hashAlg;
        }
        this.init();
    }
    
    private void init() {
        this.dr = this.df;
        this.dr1 = this.df1;
        this.dr2 = this.df2;
        this.dr3 = this.df3;
        final int n = this.N;
        this.dg = n / 3;
        this.llen = 1;
        final int n2 = n * 3 / 2;
        final int n3 = n2 / 8;
        final int db = this.db;
        this.maxMsgLenBytes = n3 - 1 - db / 8 - 1;
        this.bufferLenBits = (n2 + 7) / 8 * 8 + 1;
        this.bufferLenTrits = n - 1;
        this.pkLen = db;
    }
    
    public NTRUEncryptionParameters clone() {
        if (this.polyType == 0) {
            return new NTRUEncryptionParameters(this.N, this.q, this.df, this.dm0, this.db, this.c, this.minCallsR, this.minCallsMask, this.hashSeed, this.oid, this.sparse, this.fastFp, this.hashAlg);
        }
        return new NTRUEncryptionParameters(this.N, this.q, this.df1, this.df2, this.df3, this.dm0, this.db, this.c, this.minCallsR, this.minCallsMask, this.hashSeed, this.oid, this.sparse, this.fastFp, this.hashAlg);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        final NTRUEncryptionParameters ntruEncryptionParameters = (NTRUEncryptionParameters)o;
        if (this.N != ntruEncryptionParameters.N) {
            return false;
        }
        if (this.bufferLenBits != ntruEncryptionParameters.bufferLenBits) {
            return false;
        }
        if (this.bufferLenTrits != ntruEncryptionParameters.bufferLenTrits) {
            return false;
        }
        if (this.c != ntruEncryptionParameters.c) {
            return false;
        }
        if (this.db != ntruEncryptionParameters.db) {
            return false;
        }
        if (this.df != ntruEncryptionParameters.df) {
            return false;
        }
        if (this.df1 != ntruEncryptionParameters.df1) {
            return false;
        }
        if (this.df2 != ntruEncryptionParameters.df2) {
            return false;
        }
        if (this.df3 != ntruEncryptionParameters.df3) {
            return false;
        }
        if (this.dg != ntruEncryptionParameters.dg) {
            return false;
        }
        if (this.dm0 != ntruEncryptionParameters.dm0) {
            return false;
        }
        if (this.dr != ntruEncryptionParameters.dr) {
            return false;
        }
        if (this.dr1 != ntruEncryptionParameters.dr1) {
            return false;
        }
        if (this.dr2 != ntruEncryptionParameters.dr2) {
            return false;
        }
        if (this.dr3 != ntruEncryptionParameters.dr3) {
            return false;
        }
        if (this.fastFp != ntruEncryptionParameters.fastFp) {
            return false;
        }
        final Digest hashAlg = this.hashAlg;
        if (hashAlg == null) {
            if (ntruEncryptionParameters.hashAlg != null) {
                return false;
            }
        }
        else if (!hashAlg.getAlgorithmName().equals(ntruEncryptionParameters.hashAlg.getAlgorithmName())) {
            return false;
        }
        return this.hashSeed == ntruEncryptionParameters.hashSeed && this.llen == ntruEncryptionParameters.llen && this.maxMsgLenBytes == ntruEncryptionParameters.maxMsgLenBytes && this.minCallsMask == ntruEncryptionParameters.minCallsMask && this.minCallsR == ntruEncryptionParameters.minCallsR && Arrays.equals(this.oid, ntruEncryptionParameters.oid) && this.pkLen == ntruEncryptionParameters.pkLen && this.polyType == ntruEncryptionParameters.polyType && this.q == ntruEncryptionParameters.q && this.sparse == ntruEncryptionParameters.sparse;
    }
    
    public int getMaxMessageLength() {
        return this.maxMsgLenBytes;
    }
    
    @Override
    public int hashCode() {
        final int n = this.N;
        final int bufferLenBits = this.bufferLenBits;
        final int bufferLenTrits = this.bufferLenTrits;
        final int c = this.c;
        final int db = this.db;
        final int df = this.df;
        final int df2 = this.df1;
        final int df3 = this.df2;
        final int df4 = this.df3;
        final int dg = this.dg;
        final int dm0 = this.dm0;
        final int dr = this.dr;
        final int dr2 = this.dr1;
        final int dr3 = this.dr2;
        final int dr4 = this.dr3;
        final boolean fastFp = this.fastFp;
        int n2 = 1237;
        int n3;
        if (fastFp) {
            n3 = 1231;
        }
        else {
            n3 = 1237;
        }
        final Digest hashAlg = this.hashAlg;
        int hashCode;
        if (hashAlg == null) {
            hashCode = 0;
        }
        else {
            hashCode = hashAlg.getAlgorithmName().hashCode();
        }
        int n4;
        if (this.hashSeed) {
            n4 = 1231;
        }
        else {
            n4 = 1237;
        }
        final int llen = this.llen;
        final int maxMsgLenBytes = this.maxMsgLenBytes;
        final int minCallsMask = this.minCallsMask;
        final int minCallsR = this.minCallsR;
        final int hashCode2 = Arrays.hashCode(this.oid);
        final int pkLen = this.pkLen;
        final int polyType = this.polyType;
        final int q = this.q;
        if (this.sparse) {
            n2 = 1231;
        }
        return ((((((((((((((((((((((((((n + 31) * 31 + bufferLenBits) * 31 + bufferLenTrits) * 31 + c) * 31 + db) * 31 + df) * 31 + df2) * 31 + df3) * 31 + df4) * 31 + dg) * 31 + dm0) * 31 + dr) * 31 + dr2) * 31 + dr3) * 31 + dr4) * 31 + n3) * 31 + hashCode) * 31 + n4) * 31 + llen) * 31 + maxMsgLenBytes) * 31 + minCallsMask) * 31 + minCallsR) * 31 + hashCode2) * 31 + pkLen) * 31 + polyType) * 31 + q) * 31 + n2;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("EncryptionParameters(N=");
        sb.append(this.N);
        sb.append(" q=");
        sb.append(this.q);
        final StringBuilder sb2 = new StringBuilder(sb.toString());
        StringBuilder sb3;
        int n;
        if (this.polyType == 0) {
            sb3 = new StringBuilder();
            sb3.append(" polyType=SIMPLE df=");
            n = this.df;
        }
        else {
            sb3 = new StringBuilder();
            sb3.append(" polyType=PRODUCT df1=");
            sb3.append(this.df1);
            sb3.append(" df2=");
            sb3.append(this.df2);
            sb3.append(" df3=");
            n = this.df3;
        }
        sb3.append(n);
        sb2.append(sb3.toString());
        final StringBuilder sb4 = new StringBuilder();
        sb4.append(" dm0=");
        sb4.append(this.dm0);
        sb4.append(" db=");
        sb4.append(this.db);
        sb4.append(" c=");
        sb4.append(this.c);
        sb4.append(" minCallsR=");
        sb4.append(this.minCallsR);
        sb4.append(" minCallsMask=");
        sb4.append(this.minCallsMask);
        sb4.append(" hashSeed=");
        sb4.append(this.hashSeed);
        sb4.append(" hashAlg=");
        sb4.append(this.hashAlg);
        sb4.append(" oid=");
        sb4.append(Arrays.toString(this.oid));
        sb4.append(" sparse=");
        sb4.append(this.sparse);
        sb4.append(")");
        sb2.append(sb4.toString());
        return sb2.toString();
    }
    
    public void writeTo(final OutputStream outputStream) throws IOException {
        final DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        dataOutputStream.writeInt(this.N);
        dataOutputStream.writeInt(this.q);
        dataOutputStream.writeInt(this.df);
        dataOutputStream.writeInt(this.df1);
        dataOutputStream.writeInt(this.df2);
        dataOutputStream.writeInt(this.df3);
        dataOutputStream.writeInt(this.db);
        dataOutputStream.writeInt(this.dm0);
        dataOutputStream.writeInt(this.c);
        dataOutputStream.writeInt(this.minCallsR);
        dataOutputStream.writeInt(this.minCallsMask);
        dataOutputStream.writeBoolean(this.hashSeed);
        dataOutputStream.write(this.oid);
        dataOutputStream.writeBoolean(this.sparse);
        dataOutputStream.writeBoolean(this.fastFp);
        dataOutputStream.write(this.polyType);
        dataOutputStream.writeUTF(this.hashAlg.getAlgorithmName());
    }
}
