package org.spongycastle.crypto;

public enum PasswordConverter implements CharToByteConverter
{
    ASCII {
        @Override
        public byte[] convert(final char[] array) {
            return PBEParametersGenerator.PKCS5PasswordToBytes(array);
        }
        
        @Override
        public String getType() {
            return "ASCII";
        }
    }, 
    PKCS12 {
        @Override
        public byte[] convert(final char[] array) {
            return PBEParametersGenerator.PKCS12PasswordToBytes(array);
        }
        
        @Override
        public String getType() {
            return "PKCS12";
        }
    }, 
    UTF8 {
        @Override
        public byte[] convert(final char[] array) {
            return PBEParametersGenerator.PKCS5PasswordToUTF8Bytes(array);
        }
        
        @Override
        public String getType() {
            return "UTF8";
        }
    };
}
