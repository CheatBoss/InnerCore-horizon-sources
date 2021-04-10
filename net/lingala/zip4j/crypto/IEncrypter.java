package net.lingala.zip4j.crypto;

import net.lingala.zip4j.exception.*;

public interface IEncrypter
{
    int encryptData(final byte[] p0) throws ZipException;
    
    int encryptData(final byte[] p0, final int p1, final int p2) throws ZipException;
}
