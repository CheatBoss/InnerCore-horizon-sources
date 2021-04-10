package org.apache.james.mime4j.field.datetime.parser;

public class TokenMgrError extends Error
{
    static final int INVALID_LEXICAL_STATE = 2;
    static final int LEXICAL_ERROR = 0;
    static final int LOOP_DETECTED = 3;
    static final int STATIC_LEXER_ERROR = 1;
    int errorCode;
    
    public TokenMgrError() {
    }
    
    public TokenMgrError(final String s, final int errorCode) {
        super(s);
        this.errorCode = errorCode;
    }
    
    public TokenMgrError(final boolean b, final int n, final int n2, final int n3, final String s, final char c, final int n4) {
        this(LexicalError(b, n, n2, n3, s, c), n4);
    }
    
    protected static String LexicalError(final boolean b, final int n, final int n2, final int n3, final String s, final char c) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Lexical error at line ");
        sb.append(n2);
        sb.append(", column ");
        sb.append(n3);
        sb.append(".  Encountered: ");
        String string;
        if (b) {
            string = "<EOF> ";
        }
        else {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("\"");
            sb2.append(addEscapes(String.valueOf(c)));
            sb2.append("\"");
            sb2.append(" (");
            sb2.append((int)c);
            sb2.append("), ");
            string = sb2.toString();
        }
        sb.append(string);
        sb.append("after : \"");
        sb.append(addEscapes(s));
        sb.append("\"");
        return sb.toString();
    }
    
    protected static final String addEscapes(final String s) {
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); ++i) {
            final char char1 = s.charAt(i);
            if (char1 != '\0') {
                String string2;
                if (char1 != '\"') {
                    if (char1 != '\'') {
                        if (char1 != '\\') {
                            if (char1 != '\f') {
                                if (char1 != '\r') {
                                    switch (char1) {
                                        default: {
                                            final char char2 = s.charAt(i);
                                            if (char2 >= ' ' && char2 <= '~') {
                                                sb.append(char2);
                                                continue;
                                            }
                                            final StringBuilder sb2 = new StringBuilder();
                                            sb2.append("0000");
                                            sb2.append(Integer.toString(char2, 16));
                                            final String string = sb2.toString();
                                            final StringBuilder sb3 = new StringBuilder();
                                            sb3.append("\\u");
                                            sb3.append(string.substring(string.length() - 4, string.length()));
                                            string2 = sb3.toString();
                                            break;
                                        }
                                        case 10: {
                                            string2 = "\\n";
                                            break;
                                        }
                                        case 9: {
                                            string2 = "\\t";
                                            break;
                                        }
                                        case 8: {
                                            string2 = "\\b";
                                            break;
                                        }
                                    }
                                }
                                else {
                                    string2 = "\\r";
                                }
                            }
                            else {
                                string2 = "\\f";
                            }
                        }
                        else {
                            string2 = "\\\\";
                        }
                    }
                    else {
                        string2 = "\\'";
                    }
                }
                else {
                    string2 = "\\\"";
                }
                sb.append(string2);
            }
        }
        return sb.toString();
    }
    
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
