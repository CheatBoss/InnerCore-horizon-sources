package com.google.android.gms.internal.measurement;

final class zzxx
{
    static String zzd(final zzud zzud) {
        final zzxy zzxy = new zzxy(zzud);
        final StringBuilder sb = new StringBuilder(zzxy.size());
        for (int i = 0; i < zzxy.size(); ++i) {
            int zzal = zzxy.zzal(i);
            String s;
            if (zzal != 34) {
                if (zzal != 39) {
                    if (zzal != 92) {
                        switch (zzal) {
                            default: {
                                if (zzal < 32 || zzal > 126) {
                                    sb.append('\\');
                                    sb.append((char)((zzal >>> 6 & 0x3) + 48));
                                    sb.append((char)((zzal >>> 3 & 0x7) + 48));
                                    zzal = (zzal & 0x7) + 48;
                                }
                                sb.append((char)zzal);
                                continue;
                            }
                            case 13: {
                                s = "\\r";
                                break;
                            }
                            case 12: {
                                s = "\\f";
                                break;
                            }
                            case 11: {
                                s = "\\v";
                                break;
                            }
                            case 10: {
                                s = "\\n";
                                break;
                            }
                            case 9: {
                                s = "\\t";
                                break;
                            }
                            case 8: {
                                s = "\\b";
                                break;
                            }
                            case 7: {
                                s = "\\a";
                                break;
                            }
                        }
                    }
                    else {
                        s = "\\\\";
                    }
                }
                else {
                    s = "\\'";
                }
            }
            else {
                s = "\\\"";
            }
            sb.append(s);
        }
        return sb.toString();
    }
}
