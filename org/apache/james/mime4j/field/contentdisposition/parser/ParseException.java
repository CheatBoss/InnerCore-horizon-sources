package org.apache.james.mime4j.field.contentdisposition.parser;

public class ParseException extends org.apache.james.mime4j.field.ParseException
{
    private static final long serialVersionUID = 1L;
    public Token currentToken;
    protected String eol;
    public int[][] expectedTokenSequences;
    protected boolean specialConstructor;
    public String[] tokenImage;
    
    public ParseException() {
        super("Cannot parse field");
        this.eol = System.getProperty("line.separator", "\n");
        this.specialConstructor = false;
    }
    
    public ParseException(final String s) {
        super(s);
        this.eol = System.getProperty("line.separator", "\n");
        this.specialConstructor = false;
    }
    
    public ParseException(final Throwable t) {
        super(t);
        this.eol = System.getProperty("line.separator", "\n");
        this.specialConstructor = false;
    }
    
    public ParseException(final Token currentToken, final int[][] expectedTokenSequences, final String[] tokenImage) {
        super("");
        this.eol = System.getProperty("line.separator", "\n");
        this.specialConstructor = true;
        this.currentToken = currentToken;
        this.expectedTokenSequences = expectedTokenSequences;
        this.tokenImage = tokenImage;
    }
    
    protected String add_escapes(final String s) {
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
        if (!this.specialConstructor) {
            return super.getMessage();
        }
        final StringBuffer sb = new StringBuffer();
        int n = 0;
        int n2 = 0;
        while (true) {
            final int[][] expectedTokenSequences = this.expectedTokenSequences;
            if (n >= expectedTokenSequences.length) {
                break;
            }
            int length;
            if ((length = n2) < expectedTokenSequences[n].length) {
                length = expectedTokenSequences[n].length;
            }
            int n3 = 0;
            int[][] expectedTokenSequences2;
            while (true) {
                expectedTokenSequences2 = this.expectedTokenSequences;
                if (n3 >= expectedTokenSequences2[n].length) {
                    break;
                }
                sb.append(this.tokenImage[expectedTokenSequences2[n][n3]]);
                sb.append(" ");
                ++n3;
            }
            if (expectedTokenSequences2[n][expectedTokenSequences2[n].length - 1] != 0) {
                sb.append("...");
            }
            sb.append(this.eol);
            sb.append("    ");
            ++n;
            n2 = length;
        }
        Token token = this.currentToken.next;
        String string = "Encountered \"";
        int n4 = 0;
        String string2;
        while (true) {
            string2 = string;
            if (n4 >= n2) {
                break;
            }
            String string3 = string;
            if (n4 != 0) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(string);
                sb2.append(" ");
                string3 = sb2.toString();
            }
            if (token.kind == 0) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append(string3);
                sb3.append(this.tokenImage[0]);
                string2 = sb3.toString();
                break;
            }
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(string3);
            sb4.append(this.add_escapes(token.image));
            string = sb4.toString();
            token = token.next;
            ++n4;
        }
        final StringBuilder sb5 = new StringBuilder();
        sb5.append(string2);
        sb5.append("\" at line ");
        sb5.append(this.currentToken.next.beginLine);
        sb5.append(", column ");
        sb5.append(this.currentToken.next.beginColumn);
        final String string4 = sb5.toString();
        final StringBuilder sb6 = new StringBuilder();
        sb6.append(string4);
        sb6.append(".");
        sb6.append(this.eol);
        final String string5 = sb6.toString();
        StringBuilder sb7;
        String s;
        if (this.expectedTokenSequences.length == 1) {
            sb7 = new StringBuilder();
            sb7.append(string5);
            s = "Was expecting:";
        }
        else {
            sb7 = new StringBuilder();
            sb7.append(string5);
            s = "Was expecting one of:";
        }
        sb7.append(s);
        sb7.append(this.eol);
        sb7.append("    ");
        final String string6 = sb7.toString();
        final StringBuilder sb8 = new StringBuilder();
        sb8.append(string6);
        sb8.append(sb.toString());
        return sb8.toString();
    }
}
