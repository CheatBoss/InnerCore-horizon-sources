package net.lingala.zip4j.crypto.PBKDF2;

public class PBKDF2Parameters
{
    protected byte[] derivedKey;
    protected String hashAlgorithm;
    protected String hashCharset;
    protected int iterationCount;
    protected byte[] salt;
    
    public PBKDF2Parameters() {
        this.hashAlgorithm = null;
        this.hashCharset = "UTF-8";
        this.salt = null;
        this.iterationCount = 1000;
        this.derivedKey = null;
    }
    
    public PBKDF2Parameters(final String hashAlgorithm, final String hashCharset, final byte[] salt, final int iterationCount) {
        this.hashAlgorithm = hashAlgorithm;
        this.hashCharset = hashCharset;
        this.salt = salt;
        this.iterationCount = iterationCount;
        this.derivedKey = null;
    }
    
    public PBKDF2Parameters(final String hashAlgorithm, final String hashCharset, final byte[] salt, final int iterationCount, final byte[] derivedKey) {
        this.hashAlgorithm = hashAlgorithm;
        this.hashCharset = hashCharset;
        this.salt = salt;
        this.iterationCount = iterationCount;
        this.derivedKey = derivedKey;
    }
    
    public byte[] getDerivedKey() {
        return this.derivedKey;
    }
    
    public String getHashAlgorithm() {
        return this.hashAlgorithm;
    }
    
    public String getHashCharset() {
        return this.hashCharset;
    }
    
    public int getIterationCount() {
        return this.iterationCount;
    }
    
    public byte[] getSalt() {
        return this.salt;
    }
    
    public void setDerivedKey(final byte[] derivedKey) {
        this.derivedKey = derivedKey;
    }
    
    public void setHashAlgorithm(final String hashAlgorithm) {
        this.hashAlgorithm = hashAlgorithm;
    }
    
    public void setHashCharset(final String hashCharset) {
        this.hashCharset = hashCharset;
    }
    
    public void setIterationCount(final int iterationCount) {
        this.iterationCount = iterationCount;
    }
    
    public void setSalt(final byte[] salt) {
        this.salt = salt;
    }
}
