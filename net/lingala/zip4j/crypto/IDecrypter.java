package net.lingala.zip4j.crypto;

import net.lingala.zip4j.exception.*;

public interface IDecrypter
{
    int decryptData(final byte[] p0) throws ZipException;
    
    int decryptData(final byte[] p0, final int p1, final int p2) throws ZipException;
}
