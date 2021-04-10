package com.googleplay.licensing;

public interface Obfuscator
{
    String obfuscate(final String p0, final String p1);
    
    String unobfuscate(final String p0, final String p1) throws ValidationException;
}
