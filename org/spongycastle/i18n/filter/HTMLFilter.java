package org.spongycastle.i18n.filter;

public class HTMLFilter implements Filter
{
    @Override
    public String doFilter(String s) {
        final StringBuffer sb = new StringBuffer(s);
        for (int i = 0; i < sb.length(); i += 4) {
            final char char1 = sb.charAt(i);
            int n;
            if (char1 != '\"') {
                if (char1 != '#') {
                    if (char1 != '+') {
                        if (char1 != '-') {
                            if (char1 != '>') {
                                if (char1 != ';') {
                                    if (char1 != '<') {
                                        switch (char1) {
                                            default: {
                                                i -= 3;
                                                continue;
                                            }
                                            case 41: {
                                                n = i + 1;
                                                s = "&#41";
                                                break;
                                            }
                                            case 40: {
                                                n = i + 1;
                                                s = "&#40";
                                                break;
                                            }
                                            case 39: {
                                                n = i + 1;
                                                s = "&#39";
                                                break;
                                            }
                                            case 38: {
                                                n = i + 1;
                                                s = "&#38";
                                                break;
                                            }
                                            case 37: {
                                                n = i + 1;
                                                s = "&#37";
                                                break;
                                            }
                                        }
                                    }
                                    else {
                                        n = i + 1;
                                        s = "&#60";
                                    }
                                }
                                else {
                                    n = i + 1;
                                    s = "&#59";
                                }
                            }
                            else {
                                n = i + 1;
                                s = "&#62";
                            }
                        }
                        else {
                            n = i + 1;
                            s = "&#45";
                        }
                    }
                    else {
                        n = i + 1;
                        s = "&#43";
                    }
                }
                else {
                    n = i + 1;
                    s = "&#35";
                }
            }
            else {
                n = i + 1;
                s = "&#34";
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
