package net.lingala.zip4j.crypto.PBKDF2;

class PBKDF2HexFormatter
{
    public boolean fromString(final PBKDF2Parameters pbkdf2Parameters, final String s) {
        if (pbkdf2Parameters == null) {
            return true;
        }
        if (s == null) {
            return true;
        }
        final String[] split = s.split(":");
        if (split == null) {
            return true;
        }
        if (split.length != 3) {
            return true;
        }
        final byte[] hex2bin = BinTools.hex2bin(split[0]);
        final int int1 = Integer.parseInt(split[1]);
        final byte[] hex2bin2 = BinTools.hex2bin(split[2]);
        pbkdf2Parameters.setSalt(hex2bin);
        pbkdf2Parameters.setIterationCount(int1);
        pbkdf2Parameters.setDerivedKey(hex2bin2);
        return false;
    }
    
    public String toString(final PBKDF2Parameters pbkdf2Parameters) {
        final StringBuilder sb = new StringBuilder();
        sb.append(BinTools.bin2hex(pbkdf2Parameters.getSalt()));
        sb.append(":");
        sb.append(String.valueOf(pbkdf2Parameters.getIterationCount()));
        sb.append(":");
        sb.append(BinTools.bin2hex(pbkdf2Parameters.getDerivedKey()));
        return sb.toString();
    }
}
