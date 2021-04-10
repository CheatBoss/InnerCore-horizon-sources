package net.lingala.zip4j.crypto.PBKDF2;

interface PRF
{
    byte[] doFinal(final byte[] p0);
    
    int getHLen();
    
    void init(final byte[] p0);
}
