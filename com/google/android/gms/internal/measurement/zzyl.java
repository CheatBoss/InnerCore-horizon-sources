package com.google.android.gms.internal.measurement;

import java.nio.*;

abstract class zzyl
{
    static void zzc(CharSequence charSequence, final ByteBuffer byteBuffer) {
        final int length = charSequence.length();
        final int position = byteBuffer.position();
        int n = 0;
        while (true) {
            Label_0080: {
                if (n >= length) {
                    break Label_0080;
                }
                int n2;
                int position2;
                char char1;
                int n3 = 0;
                char char2;
                byte b;
                int n4;
                int max;
                char char3;
                int n5;
                byte b2;
                byte b3;
                char char4;
                int codePoint;
                byte b4;
                int n6;
                byte b5;
                byte b6;
                Label_0571_Outer:Label_0106_Outer:
                while (true) {
                    n2 = position;
                    position2 = n;
                    while (true) {
                        Label_0677: {
                            Label_0571:Label_0438_Outer:
                            while (true) {
                                try {
                                    char1 = charSequence.charAt(n);
                                    if (char1 < '\u0080') {
                                        n2 = position;
                                        position2 = n;
                                        byteBuffer.put(position + n, (byte)char1);
                                        ++n;
                                        break;
                                    }
                                    if (n == length) {
                                        n2 = position;
                                        position2 = n;
                                        byteBuffer.position(position + n);
                                        return;
                                    }
                                    break Label_0571;
                                    // iftrue(Label_0228:, char2 >= '\u0800')
                                    // iftrue(Label_0157:, char2 >= '\u0080')
                                    // iftrue(Label_0555:, n >= length)
                                    // iftrue(Label_0465:, char2 < '\ud800' || '\udfff' < char2)
                                    // iftrue(Label_0445:, position2 == length)
                                Label_0445:
                                    while (true) {
                                        Label_0243: {
                                            while (true) {
                                                Label_0228: {
                                                    while (true) {
                                                        n2 = n3 + 1;
                                                        b = (byte)(char2 >>> 6 | 0xC0);
                                                        position2 = n2;
                                                        try {
                                                            byteBuffer.put(n3, b);
                                                            position2 = n2;
                                                            byteBuffer.put(n2, (byte)((char2 & '?') | 0x80));
                                                            n3 = n2;
                                                            break Label_0677;
                                                        }
                                                        catch (IndexOutOfBoundsException ex) {
                                                            n4 = position2;
                                                            break Label_0571;
                                                        }
                                                        break Label_0228;
                                                        position2 = byteBuffer.position();
                                                        max = Math.max(n, n4 - byteBuffer.position() + 1);
                                                        char3 = charSequence.charAt(n);
                                                        charSequence = new StringBuilder(37);
                                                        ((StringBuilder)charSequence).append("Failed writing ");
                                                        ((StringBuilder)charSequence).append(char3);
                                                        ((StringBuilder)charSequence).append(" at index ");
                                                        ((StringBuilder)charSequence).append(position2 + max);
                                                        throw new ArrayIndexOutOfBoundsException(((StringBuilder)charSequence).toString());
                                                        Label_0465: {
                                                            n5 = n3 + 1;
                                                        }
                                                        b2 = (byte)(char2 >>> 12 | 0xE0);
                                                        position2 = n5;
                                                        byteBuffer.put(n3, b2);
                                                        n3 = n5 + 1;
                                                        b3 = (byte)((char2 >>> 6 & 0x3F) | 0x80);
                                                        n2 = n3;
                                                        position2 = n;
                                                        byteBuffer.put(n5, b3);
                                                        n2 = n3;
                                                        position2 = n;
                                                        byteBuffer.put(n3, (byte)((char2 & '?') | 0x80));
                                                        break Label_0677;
                                                        Label_0157:
                                                        continue Label_0571_Outer;
                                                    }
                                                    while (true) {
                                                        n2 = n3;
                                                        position2 = n;
                                                        char2 = charSequence.charAt(n);
                                                        n2 = n3;
                                                        position2 = n;
                                                        byteBuffer.put(n3, (byte)char2);
                                                        break Label_0677;
                                                        n = position2;
                                                        continue Label_0571;
                                                        continue Label_0438_Outer;
                                                    }
                                                }
                                                break Label_0243;
                                                n = n3;
                                                try {
                                                    char4 = charSequence.charAt(position2);
                                                    n = n3;
                                                    if (Character.isSurrogatePair(char2, char4)) {
                                                        n = n3;
                                                        codePoint = Character.toCodePoint(char2, char4);
                                                        n2 = n3 + 1;
                                                        b4 = (byte)(codePoint >>> 18 | 0xF0);
                                                        n = n2;
                                                        try {
                                                            byteBuffer.put(n3, b4);
                                                            n6 = n2 + 1;
                                                            b5 = (byte)((codePoint >>> 12 & 0x3F) | 0x80);
                                                            n = n6;
                                                            byteBuffer.put(n2, b5);
                                                            n3 = n6 + 1;
                                                            b6 = (byte)((codePoint >>> 6 & 0x3F) | 0x80);
                                                            n = n3;
                                                            byteBuffer.put(n6, b6);
                                                            n = n3;
                                                            byteBuffer.put(n3, (byte)((codePoint & 0x3F) | 0x80));
                                                            n = position2;
                                                            break Label_0677;
                                                        }
                                                        catch (IndexOutOfBoundsException ex2) {
                                                            n4 = n;
                                                            continue Label_0106_Outer;
                                                        }
                                                    }
                                                    n = position2;
                                                    break Label_0445;
                                                }
                                                catch (IndexOutOfBoundsException ex3) {
                                                    n4 = n;
                                                }
                                                continue Label_0106_Outer;
                                            }
                                            Label_0555: {
                                                n2 = n3;
                                            }
                                            position2 = n;
                                            byteBuffer.position(n3);
                                            return;
                                        }
                                        position2 = n + 1;
                                        continue;
                                    }
                                    n2 = n3;
                                    position2 = n;
                                    throw new zzyn(n, length);
                                }
                                catch (IndexOutOfBoundsException ex4) {
                                    n4 = n2;
                                    n = position2;
                                    continue Label_0571;
                                }
                                break;
                            }
                            n3 = position + n;
                            continue;
                        }
                        ++n;
                        ++n3;
                        continue;
                    }
                }
            }
        }
    }
    
    abstract int zzb(final int p0, final byte[] p1, final int p2, final int p3);
    
    abstract int zzb(final CharSequence p0, final byte[] p1, final int p2, final int p3);
    
    abstract void zzb(final CharSequence p0, final ByteBuffer p1);
    
    final boolean zzf(final byte[] array, final int n, final int n2) {
        return this.zzb(0, array, n, n2) == 0;
    }
    
    abstract String zzh(final byte[] p0, final int p1, final int p2) throws zzvt;
}
