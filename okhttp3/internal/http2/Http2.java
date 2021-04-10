package okhttp3.internal.http2;

import okio.*;
import okhttp3.internal.*;
import java.io.*;

public final class Http2
{
    static final String[] BINARY;
    static final ByteString CONNECTION_PREFACE;
    static final String[] FLAGS;
    private static final String[] FRAME_NAMES;
    
    static {
        CONNECTION_PREFACE = ByteString.encodeUtf8("PRI * HTTP/2.0\r\n\r\nSM\r\n\r\n");
        final int n = 0;
        FRAME_NAMES = new String[] { "DATA", "HEADERS", "PRIORITY", "RST_STREAM", "SETTINGS", "PUSH_PROMISE", "PING", "GOAWAY", "WINDOW_UPDATE", "CONTINUATION" };
        FLAGS = new String[64];
        BINARY = new String[256];
        int n2 = 0;
        while (true) {
            final String[] binary = Http2.BINARY;
            if (n2 >= binary.length) {
                break;
            }
            binary[n2] = Util.format("%8s", Integer.toBinaryString(n2)).replace(' ', '0');
            ++n2;
        }
        final String[] flags = Http2.FLAGS;
        flags[0] = "";
        flags[1] = "END_STREAM";
        final int[] array = { 1 };
        flags[8] = "PADDED";
        for (int i = 0; i < 1; ++i) {
            final int n3 = array[i];
            final String[] flags2 = Http2.FLAGS;
            final StringBuilder sb = new StringBuilder();
            sb.append(Http2.FLAGS[n3]);
            sb.append("|PADDED");
            flags2[n3 | 0x8] = sb.toString();
        }
        final String[] flags3 = Http2.FLAGS;
        flags3[4] = "END_HEADERS";
        flags3[32] = "PRIORITY";
        flags3[36] = "END_HEADERS|PRIORITY";
        int n4 = 0;
        int n5;
        while (true) {
            n5 = n;
            if (n4 >= 3) {
                break;
            }
            final int n6 = (new int[] { 4, 32, 36 })[n4];
            for (int j = 0; j < 1; ++j) {
                final int n7 = array[j];
                final String[] flags4 = Http2.FLAGS;
                final int n8 = n7 | n6;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(Http2.FLAGS[n7]);
                sb2.append('|');
                sb2.append(Http2.FLAGS[n6]);
                flags4[n8] = sb2.toString();
                final String[] flags5 = Http2.FLAGS;
                final StringBuilder sb3 = new StringBuilder();
                sb3.append(Http2.FLAGS[n7]);
                sb3.append('|');
                sb3.append(Http2.FLAGS[n6]);
                sb3.append("|PADDED");
                flags5[n8 | 0x8] = sb3.toString();
            }
            ++n4;
        }
        while (true) {
            final String[] flags6 = Http2.FLAGS;
            if (n5 >= flags6.length) {
                break;
            }
            if (flags6[n5] == null) {
                flags6[n5] = Http2.BINARY[n5];
            }
            ++n5;
        }
    }
    
    private Http2() {
    }
    
    static String formatFlags(final byte b, final byte b2) {
        if (b2 == 0) {
            return "";
        }
        if (b != 2 && b != 3) {
            if (b != 4 && b != 6) {
                if (b != 7 && b != 8) {
                    final String[] flags = Http2.FLAGS;
                    String s;
                    if (b2 < flags.length) {
                        s = flags[b2];
                    }
                    else {
                        s = Http2.BINARY[b2];
                    }
                    if (b == 5 && (b2 & 0x4) != 0x0) {
                        return s.replace("HEADERS", "PUSH_PROMISE");
                    }
                    if (b == 0 && (b2 & 0x20) != 0x0) {
                        return s.replace("PRIORITY", "COMPRESSED");
                    }
                    return s;
                }
            }
            else {
                if (b2 == 1) {
                    return "ACK";
                }
                return Http2.BINARY[b2];
            }
        }
        return Http2.BINARY[b2];
    }
    
    static String frameLog(final boolean b, final int n, final int n2, final byte b2, final byte b3) {
        final String[] frame_NAMES = Http2.FRAME_NAMES;
        String format;
        if (b2 < frame_NAMES.length) {
            format = frame_NAMES[b2];
        }
        else {
            format = Util.format("0x%02x", b2);
        }
        final String formatFlags = formatFlags(b2, b3);
        String s;
        if (b) {
            s = "<<";
        }
        else {
            s = ">>";
        }
        return Util.format("%s 0x%08x %5d %-13s %s", s, n, n2, format, formatFlags);
    }
    
    static IllegalArgumentException illegalArgument(final String s, final Object... array) {
        throw new IllegalArgumentException(Util.format(s, array));
    }
    
    static IOException ioException(final String s, final Object... array) throws IOException {
        throw new IOException(Util.format(s, array));
    }
}
