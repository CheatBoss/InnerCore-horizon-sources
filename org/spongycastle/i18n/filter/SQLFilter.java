package org.spongycastle.i18n.filter;

public class SQLFilter implements Filter
{
    @Override
    public String doFilter(String s) {
        final StringBuffer sb = new StringBuffer(s);
        int n;
        for (int i = 0; i < sb.length(); i = n + 1) {
            final char char1 = sb.charAt(i);
            if (char1 != '\n') {
                if (char1 != '\r') {
                    if (char1 != '\"') {
                        if (char1 != '\'') {
                            if (char1 != '-') {
                                if (char1 != '/') {
                                    if (char1 != ';') {
                                        if (char1 != '=') {
                                            if (char1 != '\\') {
                                                n = i;
                                                continue;
                                            }
                                            n = i + 1;
                                            s = "\\\\";
                                        }
                                        else {
                                            n = i + 1;
                                            s = "\\=";
                                        }
                                    }
                                    else {
                                        n = i + 1;
                                        s = "\\;";
                                    }
                                }
                                else {
                                    n = i + 1;
                                    s = "\\/";
                                }
                            }
                            else {
                                n = i + 1;
                                s = "\\-";
                            }
                        }
                        else {
                            n = i + 1;
                            s = "\\'";
                        }
                    }
                    else {
                        n = i + 1;
                        s = "\\\"";
                    }
                }
                else {
                    n = i + 1;
                    s = "\\r";
                }
            }
            else {
                n = i + 1;
                s = "\\n";
            }
            sb.replace(i, n, s);
        }
        return sb.toString();
    }
    
    @Override
    public String doFilterUrl(final String s) {
        return this.doFilter(s);
    }
}
