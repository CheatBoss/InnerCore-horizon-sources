package okhttp3.internal.http;

import okhttp3.*;
import java.net.*;
import java.io.*;

public final class StatusLine
{
    public final int code;
    public final String message;
    public final Protocol protocol;
    
    public StatusLine(final Protocol protocol, final int code, final String message) {
        this.protocol = protocol;
        this.code = code;
        this.message = message;
    }
    
    public static StatusLine parse(String substring) throws IOException {
        final boolean startsWith = substring.startsWith("HTTP/1.");
        int n = 9;
        Protocol protocol;
        if (startsWith) {
            if (substring.length() < 9 || substring.charAt(8) != ' ') {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unexpected status line: ");
                sb.append(substring);
                throw new ProtocolException(sb.toString());
            }
            final int n2 = substring.charAt(7) - '0';
            if (n2 == 0) {
                protocol = Protocol.HTTP_1_0;
            }
            else {
                if (n2 != 1) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Unexpected status line: ");
                    sb2.append(substring);
                    throw new ProtocolException(sb2.toString());
                }
                protocol = Protocol.HTTP_1_1;
            }
        }
        else {
            if (!substring.startsWith("ICY ")) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("Unexpected status line: ");
                sb3.append(substring);
                throw new ProtocolException(sb3.toString());
            }
            protocol = Protocol.HTTP_1_0;
            n = 4;
        }
        final int length = substring.length();
        final int n3 = n + 3;
        if (length >= n3) {
            try {
                final int int1 = Integer.parseInt(substring.substring(n, n3));
                if (substring.length() > n3) {
                    if (substring.charAt(n3) != ' ') {
                        final StringBuilder sb4 = new StringBuilder();
                        sb4.append("Unexpected status line: ");
                        sb4.append(substring);
                        throw new ProtocolException(sb4.toString());
                    }
                    substring = substring.substring(n + 4);
                }
                else {
                    substring = "";
                }
                return new StatusLine(protocol, int1, substring);
            }
            catch (NumberFormatException ex) {
                final StringBuilder sb5 = new StringBuilder();
                sb5.append("Unexpected status line: ");
                sb5.append(substring);
                throw new ProtocolException(sb5.toString());
            }
        }
        final StringBuilder sb6 = new StringBuilder();
        sb6.append("Unexpected status line: ");
        sb6.append(substring);
        throw new ProtocolException(sb6.toString());
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        String s;
        if (this.protocol == Protocol.HTTP_1_0) {
            s = "HTTP/1.0";
        }
        else {
            s = "HTTP/1.1";
        }
        sb.append(s);
        sb.append(' ');
        sb.append(this.code);
        if (this.message != null) {
            sb.append(' ');
            sb.append(this.message);
        }
        return sb.toString();
    }
}
