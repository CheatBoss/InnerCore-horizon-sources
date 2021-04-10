package com.android.dex;

public final class DexFormat
{
    public static final int API_CURRENT = 14;
    public static final int API_NO_EXTENDED_OPCODES = 13;
    public static final String DEX_IN_JAR_NAME = "classes.dex";
    public static final int ENDIAN_TAG = 305419896;
    public static final String MAGIC_PREFIX = "dex\n";
    public static final String MAGIC_SUFFIX = "\u0000";
    public static final int MAX_MEMBER_IDX = 65535;
    public static final int MAX_TYPE_IDX = 65535;
    public static final String VERSION_CURRENT = "036";
    public static final String VERSION_FOR_API_13 = "035";
    
    private DexFormat() {
    }
    
    public static String apiToMagic(final int n) {
        String s;
        if (n >= 14) {
            s = "036";
        }
        else {
            s = "035";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("dex\n");
        sb.append(s);
        sb.append("\u0000");
        return sb.toString();
    }
    
    public static int magicToApi(final byte[] array) {
        if (array.length != 8) {
            return -1;
        }
        if (array[0] != 100 || array[1] != 101 || array[2] != 120 || array[3] != 10) {
            return -1;
        }
        if (array[7] != 0) {
            return -1;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append((char)array[4]);
        sb.append((char)array[5]);
        sb.append((char)array[6]);
        final String string = sb.toString();
        if (string.equals("036")) {
            return 14;
        }
        if (string.equals("035")) {
            return 13;
        }
        return -1;
    }
}
