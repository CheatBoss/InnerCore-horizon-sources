package org.apache.james.mime4j.field.address.parser;

import java.io.*;

public class AddressListParserTokenManager implements AddressListParserConstants
{
    static int commentNest;
    static final long[] jjbitVec0;
    public static final int[] jjnewLexState;
    static final int[] jjnextStates;
    public static final String[] jjstrLiteralImages;
    static final long[] jjtoMore;
    static final long[] jjtoSkip;
    static final long[] jjtoSpecial;
    static final long[] jjtoToken;
    public static final String[] lexStateNames;
    protected char curChar;
    int curLexState;
    public PrintStream debugStream;
    int defaultLexState;
    StringBuffer image;
    protected SimpleCharStream input_stream;
    int jjimageLen;
    int jjmatchedKind;
    int jjmatchedPos;
    int jjnewStateCnt;
    int jjround;
    private final int[] jjrounds;
    private final int[] jjstateSet;
    int lengthOfMatch;
    
    static {
        jjbitVec0 = new long[] { 0L, 0L, -1L, -1L };
        jjnextStates = new int[0];
        jjstrLiteralImages = new String[] { "", "\r", "\n", ",", ":", ";", "<", ">", "@", ".", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null };
        lexStateNames = new String[] { "DEFAULT", "INDOMAINLITERAL", "INCOMMENT", "NESTED_COMMENT", "INQUOTEDSTRING" };
        jjnewLexState = new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, -1, -1, 0, 2, 0, -1, 3, -1, -1, -1, -1, -1, 4, -1, -1, 0, -1, -1 };
        jjtoToken = new long[] { 2147763199L };
        jjtoSkip = new long[] { 1049600L };
        jjtoSpecial = new long[] { 1024L };
        jjtoMore = new long[] { 2146140160L };
    }
    
    public AddressListParserTokenManager(final SimpleCharStream input_stream) {
        this.debugStream = System.out;
        this.jjrounds = new int[3];
        this.jjstateSet = new int[6];
        this.curLexState = 0;
        this.defaultLexState = 0;
        this.input_stream = input_stream;
    }
    
    public AddressListParserTokenManager(final SimpleCharStream simpleCharStream, final int n) {
        this(simpleCharStream);
        this.SwitchTo(n);
    }
    
    private final void ReInitRounds() {
        this.jjround = -2147483647;
        int n = 3;
        while (true) {
            final int n2 = n - 1;
            if (n <= 0) {
                break;
            }
            this.jjrounds[n2] = Integer.MIN_VALUE;
            n = n2;
        }
    }
    
    private final void jjAddStates(int n, final int n2) {
        while (true) {
            this.jjstateSet[this.jjnewStateCnt++] = AddressListParserTokenManager.jjnextStates[n];
            if (n == n2) {
                break;
            }
            ++n;
        }
    }
    
    private final void jjCheckNAdd(final int n) {
        final int[] jjrounds = this.jjrounds;
        final int n2 = jjrounds[n];
        final int jjround = this.jjround;
        if (n2 != jjround) {
            jjrounds[this.jjstateSet[this.jjnewStateCnt++] = n] = jjround;
        }
    }
    
    private final void jjCheckNAddStates(final int n) {
        this.jjCheckNAdd(AddressListParserTokenManager.jjnextStates[n]);
        this.jjCheckNAdd(AddressListParserTokenManager.jjnextStates[n + 1]);
    }
    
    private final void jjCheckNAddStates(int n, final int n2) {
        while (true) {
            this.jjCheckNAdd(AddressListParserTokenManager.jjnextStates[n]);
            if (n == n2) {
                break;
            }
            ++n;
        }
    }
    
    private final void jjCheckNAddTwoStates(final int n, final int n2) {
        this.jjCheckNAdd(n);
        this.jjCheckNAdd(n2);
    }
    
    private final int jjMoveNfa_0(int jjnewStateCnt, int n) {
        this.jjnewStateCnt = 3;
        this.jjstateSet[0] = jjnewStateCnt;
        jjnewStateCnt = 1;
        int jjnewStateCnt2 = 0;
        final int n2 = Integer.MAX_VALUE;
        int jjmatchedPos = n;
        n = n2;
        while (true) {
            final int jjround = this.jjround + 1;
            this.jjround = jjround;
            if (jjround == Integer.MAX_VALUE) {
                this.ReInitRounds();
            }
            final char curChar = this.curChar;
            if (curChar < '@') {
                final long n3 = 1L << curChar;
                int n4 = jjnewStateCnt;
                int i;
                do {
                    final int[] jjstateSet = this.jjstateSet;
                    i = n4 - 1;
                    jjnewStateCnt = jjstateSet[i];
                    final int n5 = 10;
                    Label_0230: {
                        if (jjnewStateCnt != 0) {
                            Label_0163: {
                                if (jjnewStateCnt != 1) {
                                    if (jjnewStateCnt != 2) {
                                        jjnewStateCnt = n;
                                        break Label_0230;
                                    }
                                    if ((n3 & 0xA3FFECFA00000000L) == 0x0L) {
                                        jjnewStateCnt = n;
                                        break Label_0230;
                                    }
                                    if ((jjnewStateCnt = n) <= 14) {
                                        break Label_0163;
                                    }
                                }
                                else if ((n3 & 0xA3FFACFA00000000L) != 0x0L) {
                                    if ((jjnewStateCnt = n) <= 14) {
                                        break Label_0163;
                                    }
                                }
                                else {
                                    jjnewStateCnt = n;
                                    if ((n3 & 0x100000200L) != 0x0L) {
                                        if (n > 10) {
                                            jjnewStateCnt = n5;
                                        }
                                        else {
                                            jjnewStateCnt = n;
                                        }
                                        this.jjCheckNAdd(0);
                                    }
                                    break Label_0230;
                                }
                                jjnewStateCnt = 14;
                            }
                            this.jjCheckNAdd(2);
                        }
                        else if ((n3 & 0x100000200L) == 0x0L) {
                            jjnewStateCnt = n;
                        }
                        else {
                            this.jjCheckNAdd(0);
                            jjnewStateCnt = 10;
                        }
                    }
                    n4 = i;
                    n = jjnewStateCnt;
                } while (i != jjnewStateCnt2);
            }
            else {
                int n6 = jjnewStateCnt;
                if (curChar < '\u0080') {
                    int n7 = jjnewStateCnt;
                    int j;
                    do {
                        final int[] jjstateSet2 = this.jjstateSet;
                        j = n7 - 1;
                        jjnewStateCnt = jjstateSet2[j];
                        if (jjnewStateCnt != 1 && jjnewStateCnt != 2) {
                            jjnewStateCnt = n;
                        }
                        else if ((1L << (curChar & '?') & 0x7FFFFFFFC7FFFFFEL) == 0x0L) {
                            jjnewStateCnt = n;
                        }
                        else {
                            if ((jjnewStateCnt = n) > 14) {
                                jjnewStateCnt = 14;
                            }
                            this.jjCheckNAdd(2);
                        }
                        n7 = j;
                        n = jjnewStateCnt;
                    } while (j != jjnewStateCnt2);
                }
                else {
                    do {
                        final int[] jjstateSet3 = this.jjstateSet;
                        jjnewStateCnt = n6 - 1;
                        final int n8 = jjstateSet3[jjnewStateCnt];
                    } while ((n6 = jjnewStateCnt) != jjnewStateCnt2);
                    jjnewStateCnt = n;
                }
            }
            n = jjnewStateCnt;
            if (jjnewStateCnt != Integer.MAX_VALUE) {
                this.jjmatchedKind = jjnewStateCnt;
                this.jjmatchedPos = jjmatchedPos;
                n = Integer.MAX_VALUE;
            }
            ++jjmatchedPos;
            jjnewStateCnt = this.jjnewStateCnt;
            this.jjnewStateCnt = jjnewStateCnt2;
            jjnewStateCnt2 = 3 - jjnewStateCnt2;
            if (jjnewStateCnt == jjnewStateCnt2) {
                break;
            }
            try {
                this.curChar = this.input_stream.readChar();
            }
            catch (IOException ex) {
                return jjmatchedPos;
            }
        }
        return jjmatchedPos;
    }
    
    private final int jjMoveNfa_1(int jjnewStateCnt, int n) {
        this.jjnewStateCnt = 3;
        this.jjstateSet[0] = jjnewStateCnt;
        jjnewStateCnt = 1;
        final int n2 = Integer.MAX_VALUE;
        int jjnewStateCnt2 = 0;
        int jjmatchedPos = n;
        n = n2;
        while (true) {
            final int jjround = this.jjround + 1;
            this.jjround = jjround;
            if (jjround == Integer.MAX_VALUE) {
                this.ReInitRounds();
            }
            final char curChar = this.curChar;
            if (curChar < '@') {
                int n3 = jjnewStateCnt;
                int i;
                do {
                    final int[] jjstateSet = this.jjstateSet;
                    i = n3 - 1;
                    jjnewStateCnt = jjstateSet[i];
                    if (jjnewStateCnt != 0) {
                        if (jjnewStateCnt != 1) {
                            jjnewStateCnt = n;
                        }
                        else if ((jjnewStateCnt = n) > 16) {
                            jjnewStateCnt = 16;
                        }
                    }
                    else if ((jjnewStateCnt = n) > 17) {
                        jjnewStateCnt = 17;
                    }
                    n3 = i;
                    n = jjnewStateCnt;
                } while (i != jjnewStateCnt2);
            }
            else if (curChar < '\u0080') {
                final long n4 = 1L << (curChar & '?');
                int n5 = jjnewStateCnt;
                int j;
                do {
                    final int[] jjstateSet2 = this.jjstateSet;
                    j = n5 - 1;
                    jjnewStateCnt = jjstateSet2[j];
                    Label_0289: {
                        if (jjnewStateCnt != 0) {
                            if (jjnewStateCnt != 1) {
                                if (jjnewStateCnt != 2) {
                                    jjnewStateCnt = n;
                                    break Label_0289;
                                }
                                jjnewStateCnt = n;
                                if ((n4 & 0xFFFFFFFFC7FFFFFFL) == 0x0L || (jjnewStateCnt = n) <= 17) {
                                    break Label_0289;
                                }
                            }
                            else {
                                if ((jjnewStateCnt = n) > 16) {
                                    jjnewStateCnt = 16;
                                }
                                break Label_0289;
                            }
                        }
                        else if ((n4 & 0xFFFFFFFFC7FFFFFFL) != 0x0L) {
                            if ((jjnewStateCnt = n) <= 17) {
                                break Label_0289;
                            }
                        }
                        else {
                            jjnewStateCnt = n;
                            if (this.curChar == '\\') {
                                jjnewStateCnt = this.jjnewStateCnt++;
                                jjstateSet2[jjnewStateCnt] = 1;
                                jjnewStateCnt = n;
                            }
                            break Label_0289;
                        }
                        jjnewStateCnt = 17;
                    }
                    n5 = j;
                    n = jjnewStateCnt;
                } while (j != jjnewStateCnt2);
            }
            else {
                final int n6 = (curChar & '\u00ff') >> 6;
                final long n7 = 1L << (curChar & '?');
                int n8 = jjnewStateCnt;
                int k;
                do {
                    final int[] jjstateSet3 = this.jjstateSet;
                    k = n8 - 1;
                    jjnewStateCnt = jjstateSet3[k];
                    if (jjnewStateCnt != 0) {
                        if (jjnewStateCnt != 1) {
                            jjnewStateCnt = n;
                        }
                        else {
                            jjnewStateCnt = n;
                            if ((AddressListParserTokenManager.jjbitVec0[n6] & n7) != 0x0L && (jjnewStateCnt = n) > 16) {
                                jjnewStateCnt = 16;
                            }
                        }
                    }
                    else {
                        jjnewStateCnt = n;
                        if ((AddressListParserTokenManager.jjbitVec0[n6] & n7) != 0x0L && (jjnewStateCnt = n) > 17) {
                            jjnewStateCnt = 17;
                        }
                    }
                    n8 = k;
                    n = jjnewStateCnt;
                } while (k != jjnewStateCnt2);
            }
            if ((n = jjnewStateCnt) != Integer.MAX_VALUE) {
                this.jjmatchedKind = jjnewStateCnt;
                this.jjmatchedPos = jjmatchedPos;
                n = Integer.MAX_VALUE;
            }
            ++jjmatchedPos;
            jjnewStateCnt = this.jjnewStateCnt;
            this.jjnewStateCnt = jjnewStateCnt2;
            jjnewStateCnt2 = 3 - jjnewStateCnt2;
            if (jjnewStateCnt == jjnewStateCnt2) {
                break;
            }
            try {
                this.curChar = this.input_stream.readChar();
            }
            catch (IOException ex) {
                return jjmatchedPos;
            }
        }
        return jjmatchedPos;
    }
    
    private final int jjMoveNfa_2(int jjnewStateCnt, int n) {
        this.jjnewStateCnt = 3;
        this.jjstateSet[0] = jjnewStateCnt;
        jjnewStateCnt = 1;
        final int n2 = Integer.MAX_VALUE;
        int jjnewStateCnt2 = 0;
        int jjmatchedPos = n;
        n = n2;
        while (true) {
            final int jjround = this.jjround + 1;
            this.jjround = jjround;
            if (jjround == Integer.MAX_VALUE) {
                this.ReInitRounds();
            }
            final char curChar = this.curChar;
            if (curChar < '@') {
                int n3 = jjnewStateCnt;
                int i;
                do {
                    final int[] jjstateSet = this.jjstateSet;
                    i = n3 - 1;
                    jjnewStateCnt = jjstateSet[i];
                    if (jjnewStateCnt != 0) {
                        if (jjnewStateCnt != 1) {
                            jjnewStateCnt = n;
                        }
                        else if ((jjnewStateCnt = n) > 21) {
                            jjnewStateCnt = 21;
                        }
                    }
                    else if ((jjnewStateCnt = n) > 23) {
                        jjnewStateCnt = 23;
                    }
                    n3 = i;
                    n = jjnewStateCnt;
                } while (i != jjnewStateCnt2);
            }
            else if (curChar < '\u0080') {
                int n4 = jjnewStateCnt;
                int j;
                do {
                    final int[] jjstateSet2 = this.jjstateSet;
                    j = n4 - 1;
                    jjnewStateCnt = jjstateSet2[j];
                    if (jjnewStateCnt != 0) {
                        if (jjnewStateCnt != 1) {
                            if (jjnewStateCnt != 2) {
                                jjnewStateCnt = n;
                            }
                            else if ((jjnewStateCnt = n) > 23) {
                                jjnewStateCnt = 23;
                            }
                        }
                        else if ((jjnewStateCnt = n) > 21) {
                            jjnewStateCnt = 21;
                        }
                    }
                    else {
                        int n5;
                        if ((n5 = n) > 23) {
                            n5 = 23;
                        }
                        jjnewStateCnt = n5;
                        if (this.curChar == '\\') {
                            final int[] jjstateSet3 = this.jjstateSet;
                            jjnewStateCnt = this.jjnewStateCnt++;
                            jjstateSet3[jjnewStateCnt] = 1;
                            jjnewStateCnt = n5;
                        }
                    }
                    n4 = j;
                    n = jjnewStateCnt;
                } while (j != jjnewStateCnt2);
            }
            else {
                final int n6 = (curChar & '\u00ff') >> 6;
                final long n7 = 1L << (curChar & '?');
                int n8 = jjnewStateCnt;
                int k;
                do {
                    final int[] jjstateSet4 = this.jjstateSet;
                    k = n8 - 1;
                    jjnewStateCnt = jjstateSet4[k];
                    if (jjnewStateCnt != 0) {
                        if (jjnewStateCnt != 1) {
                            jjnewStateCnt = n;
                        }
                        else {
                            jjnewStateCnt = n;
                            if ((AddressListParserTokenManager.jjbitVec0[n6] & n7) != 0x0L && (jjnewStateCnt = n) > 21) {
                                jjnewStateCnt = 21;
                            }
                        }
                    }
                    else {
                        jjnewStateCnt = n;
                        if ((AddressListParserTokenManager.jjbitVec0[n6] & n7) != 0x0L && (jjnewStateCnt = n) > 23) {
                            jjnewStateCnt = 23;
                        }
                    }
                    n8 = k;
                    n = jjnewStateCnt;
                } while (k != jjnewStateCnt2);
            }
            if ((n = jjnewStateCnt) != Integer.MAX_VALUE) {
                this.jjmatchedKind = jjnewStateCnt;
                this.jjmatchedPos = jjmatchedPos;
                n = Integer.MAX_VALUE;
            }
            ++jjmatchedPos;
            jjnewStateCnt = this.jjnewStateCnt;
            this.jjnewStateCnt = jjnewStateCnt2;
            jjnewStateCnt2 = 3 - jjnewStateCnt2;
            if (jjnewStateCnt == jjnewStateCnt2) {
                break;
            }
            try {
                this.curChar = this.input_stream.readChar();
            }
            catch (IOException ex) {
                return jjmatchedPos;
            }
        }
        return jjmatchedPos;
    }
    
    private final int jjMoveNfa_3(int jjnewStateCnt, int n) {
        this.jjnewStateCnt = 3;
        this.jjstateSet[0] = jjnewStateCnt;
        jjnewStateCnt = 1;
        final int n2 = Integer.MAX_VALUE;
        int jjnewStateCnt2 = 0;
        int jjmatchedPos = n;
        n = n2;
        while (true) {
            final int jjround = this.jjround + 1;
            this.jjround = jjround;
            if (jjround == Integer.MAX_VALUE) {
                this.ReInitRounds();
            }
            final char curChar = this.curChar;
            if (curChar < '@') {
                int n3 = jjnewStateCnt;
                int i;
                do {
                    final int[] jjstateSet = this.jjstateSet;
                    i = n3 - 1;
                    jjnewStateCnt = jjstateSet[i];
                    if (jjnewStateCnt != 0) {
                        if (jjnewStateCnt != 1) {
                            jjnewStateCnt = n;
                        }
                        else if ((jjnewStateCnt = n) > 24) {
                            jjnewStateCnt = 24;
                        }
                    }
                    else if ((jjnewStateCnt = n) > 27) {
                        jjnewStateCnt = 27;
                    }
                    n3 = i;
                    n = jjnewStateCnt;
                } while (i != jjnewStateCnt2);
            }
            else if (curChar < '\u0080') {
                int n4 = jjnewStateCnt;
                int j;
                do {
                    final int[] jjstateSet2 = this.jjstateSet;
                    j = n4 - 1;
                    jjnewStateCnt = jjstateSet2[j];
                    if (jjnewStateCnt != 0) {
                        if (jjnewStateCnt != 1) {
                            if (jjnewStateCnt != 2) {
                                jjnewStateCnt = n;
                            }
                            else if ((jjnewStateCnt = n) > 27) {
                                jjnewStateCnt = 27;
                            }
                        }
                        else if ((jjnewStateCnt = n) > 24) {
                            jjnewStateCnt = 24;
                        }
                    }
                    else {
                        int n5;
                        if ((n5 = n) > 27) {
                            n5 = 27;
                        }
                        jjnewStateCnt = n5;
                        if (this.curChar == '\\') {
                            final int[] jjstateSet3 = this.jjstateSet;
                            jjnewStateCnt = this.jjnewStateCnt++;
                            jjstateSet3[jjnewStateCnt] = 1;
                            jjnewStateCnt = n5;
                        }
                    }
                    n4 = j;
                    n = jjnewStateCnt;
                } while (j != jjnewStateCnt2);
            }
            else {
                final int n6 = (curChar & '\u00ff') >> 6;
                final long n7 = 1L << (curChar & '?');
                int n8 = jjnewStateCnt;
                int k;
                do {
                    final int[] jjstateSet4 = this.jjstateSet;
                    k = n8 - 1;
                    jjnewStateCnt = jjstateSet4[k];
                    if (jjnewStateCnt != 0) {
                        if (jjnewStateCnt != 1) {
                            jjnewStateCnt = n;
                        }
                        else {
                            jjnewStateCnt = n;
                            if ((AddressListParserTokenManager.jjbitVec0[n6] & n7) != 0x0L && (jjnewStateCnt = n) > 24) {
                                jjnewStateCnt = 24;
                            }
                        }
                    }
                    else {
                        jjnewStateCnt = n;
                        if ((AddressListParserTokenManager.jjbitVec0[n6] & n7) != 0x0L && (jjnewStateCnt = n) > 27) {
                            jjnewStateCnt = 27;
                        }
                    }
                    n8 = k;
                    n = jjnewStateCnt;
                } while (k != jjnewStateCnt2);
            }
            if ((n = jjnewStateCnt) != Integer.MAX_VALUE) {
                this.jjmatchedKind = jjnewStateCnt;
                this.jjmatchedPos = jjmatchedPos;
                n = Integer.MAX_VALUE;
            }
            ++jjmatchedPos;
            jjnewStateCnt = this.jjnewStateCnt;
            this.jjnewStateCnt = jjnewStateCnt2;
            jjnewStateCnt2 = 3 - jjnewStateCnt2;
            if (jjnewStateCnt == jjnewStateCnt2) {
                break;
            }
            try {
                this.curChar = this.input_stream.readChar();
            }
            catch (IOException ex) {
                return jjmatchedPos;
            }
        }
        return jjmatchedPos;
    }
    
    private final int jjMoveNfa_4(int jjnewStateCnt, int n) {
        this.jjnewStateCnt = 3;
        this.jjstateSet[0] = jjnewStateCnt;
        jjnewStateCnt = 1;
        final int n2 = Integer.MAX_VALUE;
        int jjnewStateCnt2 = 0;
        int jjmatchedPos = n;
        n = n2;
    Label_0363_Outer:
        while (true) {
            final int jjround = this.jjround + 1;
            this.jjround = jjround;
            if (jjround == Integer.MAX_VALUE) {
                this.ReInitRounds();
            }
            final char curChar = this.curChar;
        Label_0164_Outer:
            while (true) {
                if (curChar < '@') {
                    int n3 = jjnewStateCnt;
                    int i;
                    do {
                        final int[] jjstateSet = this.jjstateSet;
                        i = n3 - 1;
                        jjnewStateCnt = jjstateSet[i];
                        Label_0152: {
                            if (jjnewStateCnt != 0) {
                                if (jjnewStateCnt != 1) {
                                    if (jjnewStateCnt != 2) {
                                        jjnewStateCnt = n;
                                        break Label_0152;
                                    }
                                }
                                else {
                                    if ((jjnewStateCnt = n) > 29) {
                                        jjnewStateCnt = 29;
                                    }
                                    break Label_0152;
                                }
                            }
                            if ((1L << curChar & 0xFFFFFFFBFFFFFFFFL) == 0x0L) {
                                jjnewStateCnt = n;
                            }
                            else {
                                if ((jjnewStateCnt = n) > 30) {
                                    jjnewStateCnt = 30;
                                }
                                this.jjCheckNAdd(2);
                            }
                        }
                        n3 = i;
                        n = jjnewStateCnt;
                    } while (i != jjnewStateCnt2);
                }
                else {
                    if (curChar >= '\u0080') {
                        final int n4 = (curChar & '\u00ff') >> 6;
                        final long n5 = 1L << (curChar & '?');
                        final int n6 = jjnewStateCnt;
                        break Label_0363;
                    }
                    final long n7 = 1L << (curChar & '?');
                    int n8 = jjnewStateCnt;
                    int j;
                    do {
                        final int[] jjstateSet2 = this.jjstateSet;
                        j = n8 - 1;
                        jjnewStateCnt = jjstateSet2[j];
                        Label_0325: {
                            Label_0287: {
                                if (jjnewStateCnt != 0) {
                                    if (jjnewStateCnt != 1) {
                                        if (jjnewStateCnt != 2) {
                                            jjnewStateCnt = n;
                                            break Label_0325;
                                        }
                                        if ((n7 & 0xFFFFFFFFEFFFFFFFL) == 0x0L) {
                                            jjnewStateCnt = n;
                                            break Label_0325;
                                        }
                                        if ((jjnewStateCnt = n) <= 30) {
                                            break Label_0287;
                                        }
                                    }
                                    else {
                                        if ((jjnewStateCnt = n) > 29) {
                                            jjnewStateCnt = 29;
                                        }
                                        break Label_0325;
                                    }
                                }
                                else if ((n7 & 0xFFFFFFFFEFFFFFFFL) != 0x0L) {
                                    if ((jjnewStateCnt = n) <= 30) {
                                        break Label_0287;
                                    }
                                }
                                else {
                                    jjnewStateCnt = n;
                                    if (this.curChar == '\\') {
                                        jjnewStateCnt = this.jjnewStateCnt++;
                                        jjstateSet2[jjnewStateCnt] = 1;
                                        jjnewStateCnt = n;
                                    }
                                    break Label_0325;
                                }
                                jjnewStateCnt = 30;
                            }
                            this.jjCheckNAdd(2);
                        }
                        n8 = j;
                        n = jjnewStateCnt;
                    } while (j != jjnewStateCnt2);
                }
            Label_0543:
                while (true) {
                    if ((n = jjnewStateCnt) != Integer.MAX_VALUE) {
                        this.jjmatchedKind = jjnewStateCnt;
                        this.jjmatchedPos = jjmatchedPos;
                        n = Integer.MAX_VALUE;
                    }
                    ++jjmatchedPos;
                    jjnewStateCnt = this.jjnewStateCnt;
                    this.jjnewStateCnt = jjnewStateCnt2;
                    jjnewStateCnt2 = 3 - jjnewStateCnt2;
                    if (jjnewStateCnt == jjnewStateCnt2) {
                        break;
                    }
                    try {
                        this.curChar = this.input_stream.readChar();
                        continue Label_0363_Outer;
                    }
                    catch (IOException ex) {
                        return jjmatchedPos;
                    }
                    break Label_0543;
                    final int[] jjstateSet3 = this.jjstateSet;
                    final int n6 = n6 - 1;
                    jjnewStateCnt = jjstateSet3[n6];
                    Label_0465: {
                        final int n4;
                        final long n5;
                        if (jjnewStateCnt != 0) {
                            if (jjnewStateCnt != 1) {
                                if (jjnewStateCnt != 2) {
                                    jjnewStateCnt = n;
                                    break Label_0465;
                                }
                            }
                            else {
                                jjnewStateCnt = n;
                                if ((AddressListParserTokenManager.jjbitVec0[n4] & n5) != 0x0L && (jjnewStateCnt = n) > 29) {
                                    jjnewStateCnt = 29;
                                }
                                break Label_0465;
                            }
                        }
                        if ((AddressListParserTokenManager.jjbitVec0[n4] & n5) == 0x0L) {
                            jjnewStateCnt = n;
                        }
                        else {
                            if ((jjnewStateCnt = n) > 30) {
                                jjnewStateCnt = 30;
                            }
                            this.jjCheckNAdd(2);
                        }
                    }
                    if (n6 == jjnewStateCnt2) {
                        continue;
                    }
                    break;
                }
                n = jjnewStateCnt;
                continue Label_0164_Outer;
            }
        }
        return jjmatchedPos;
    }
    
    private final int jjMoveStringLiteralDfa0_0() {
        final char curChar = this.curChar;
        int n;
        if (curChar != '\n') {
            if (curChar == '\r') {
                return this.jjStopAtPos(0, 1);
            }
            if (curChar != '\"') {
                if (curChar != '(') {
                    if (curChar != ',') {
                        if (curChar != '.') {
                            if (curChar != '>') {
                                if (curChar != '@') {
                                    if (curChar != '[') {
                                        switch (curChar) {
                                            default: {
                                                return this.jjMoveNfa_0(1, 0);
                                            }
                                            case 60: {
                                                n = 6;
                                                break;
                                            }
                                            case 59: {
                                                n = 5;
                                                break;
                                            }
                                            case 58: {
                                                n = 4;
                                                break;
                                            }
                                        }
                                    }
                                    else {
                                        n = 15;
                                    }
                                }
                                else {
                                    n = 8;
                                }
                            }
                            else {
                                n = 7;
                            }
                        }
                        else {
                            n = 9;
                        }
                    }
                    else {
                        n = 3;
                    }
                }
                else {
                    n = 19;
                }
            }
            else {
                n = 28;
            }
        }
        else {
            n = 2;
        }
        return this.jjStopAtPos(0, n);
    }
    
    private final int jjMoveStringLiteralDfa0_1() {
        if (this.curChar != ']') {
            return this.jjMoveNfa_1(0, 0);
        }
        return this.jjStopAtPos(0, 18);
    }
    
    private final int jjMoveStringLiteralDfa0_2() {
        final char curChar = this.curChar;
        int n;
        if (curChar != '(') {
            if (curChar != ')') {
                return this.jjMoveNfa_2(0, 0);
            }
            n = 20;
        }
        else {
            n = 22;
        }
        return this.jjStopAtPos(0, n);
    }
    
    private final int jjMoveStringLiteralDfa0_3() {
        final char curChar = this.curChar;
        int n;
        if (curChar != '(') {
            if (curChar != ')') {
                return this.jjMoveNfa_3(0, 0);
            }
            n = 26;
        }
        else {
            n = 25;
        }
        return this.jjStopAtPos(0, n);
    }
    
    private final int jjMoveStringLiteralDfa0_4() {
        if (this.curChar != '\"') {
            return this.jjMoveNfa_4(0, 0);
        }
        return this.jjStopAtPos(0, 31);
    }
    
    private final int jjStartNfaWithStates_0(final int jjmatchedPos, final int jjmatchedKind, final int n) {
        this.jjmatchedKind = jjmatchedKind;
        this.jjmatchedPos = jjmatchedPos;
        try {
            this.curChar = this.input_stream.readChar();
            return this.jjMoveNfa_0(n, jjmatchedPos + 1);
        }
        catch (IOException ex) {
            return jjmatchedPos + 1;
        }
    }
    
    private final int jjStartNfaWithStates_1(final int jjmatchedPos, final int jjmatchedKind, final int n) {
        this.jjmatchedKind = jjmatchedKind;
        this.jjmatchedPos = jjmatchedPos;
        try {
            this.curChar = this.input_stream.readChar();
            return this.jjMoveNfa_1(n, jjmatchedPos + 1);
        }
        catch (IOException ex) {
            return jjmatchedPos + 1;
        }
    }
    
    private final int jjStartNfaWithStates_2(final int jjmatchedPos, final int jjmatchedKind, final int n) {
        this.jjmatchedKind = jjmatchedKind;
        this.jjmatchedPos = jjmatchedPos;
        try {
            this.curChar = this.input_stream.readChar();
            return this.jjMoveNfa_2(n, jjmatchedPos + 1);
        }
        catch (IOException ex) {
            return jjmatchedPos + 1;
        }
    }
    
    private final int jjStartNfaWithStates_3(final int jjmatchedPos, final int jjmatchedKind, final int n) {
        this.jjmatchedKind = jjmatchedKind;
        this.jjmatchedPos = jjmatchedPos;
        try {
            this.curChar = this.input_stream.readChar();
            return this.jjMoveNfa_3(n, jjmatchedPos + 1);
        }
        catch (IOException ex) {
            return jjmatchedPos + 1;
        }
    }
    
    private final int jjStartNfaWithStates_4(final int jjmatchedPos, final int jjmatchedKind, final int n) {
        this.jjmatchedKind = jjmatchedKind;
        this.jjmatchedPos = jjmatchedPos;
        try {
            this.curChar = this.input_stream.readChar();
            return this.jjMoveNfa_4(n, jjmatchedPos + 1);
        }
        catch (IOException ex) {
            return jjmatchedPos + 1;
        }
    }
    
    private final int jjStartNfa_0(final int n, final long n2) {
        return this.jjMoveNfa_0(this.jjStopStringLiteralDfa_0(n, n2), n + 1);
    }
    
    private final int jjStartNfa_1(final int n, final long n2) {
        return this.jjMoveNfa_1(this.jjStopStringLiteralDfa_1(n, n2), n + 1);
    }
    
    private final int jjStartNfa_2(final int n, final long n2) {
        return this.jjMoveNfa_2(this.jjStopStringLiteralDfa_2(n, n2), n + 1);
    }
    
    private final int jjStartNfa_3(final int n, final long n2) {
        return this.jjMoveNfa_3(this.jjStopStringLiteralDfa_3(n, n2), n + 1);
    }
    
    private final int jjStartNfa_4(final int n, final long n2) {
        return this.jjMoveNfa_4(this.jjStopStringLiteralDfa_4(n, n2), n + 1);
    }
    
    private final int jjStopAtPos(final int jjmatchedPos, final int jjmatchedKind) {
        this.jjmatchedKind = jjmatchedKind;
        return (this.jjmatchedPos = jjmatchedPos) + 1;
    }
    
    private final int jjStopStringLiteralDfa_0(final int n, final long n2) {
        return -1;
    }
    
    private final int jjStopStringLiteralDfa_1(final int n, final long n2) {
        return -1;
    }
    
    private final int jjStopStringLiteralDfa_2(final int n, final long n2) {
        return -1;
    }
    
    private final int jjStopStringLiteralDfa_3(final int n, final long n2) {
        return -1;
    }
    
    private final int jjStopStringLiteralDfa_4(final int n, final long n2) {
        return -1;
    }
    
    void MoreLexicalActions() {
        final int jjimageLen = this.jjimageLen;
        final int lengthOfMatch = this.jjmatchedPos + 1;
        this.lengthOfMatch = lengthOfMatch;
        this.jjimageLen = jjimageLen + lengthOfMatch;
        final int jjmatchedKind = this.jjmatchedKind;
        if (jjmatchedKind != 16) {
            if (jjmatchedKind == 21) {
                if (this.image == null) {
                    this.image = new StringBuffer();
                }
                this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
                this.jjimageLen = 0;
                final StringBuffer image = this.image;
                image.deleteCharAt(image.length() - 2);
                return;
            }
            if (jjmatchedKind == 22) {
                if (this.image == null) {
                    this.image = new StringBuffer();
                }
                this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
                this.jjimageLen = 0;
                AddressListParserTokenManager.commentNest = 1;
                return;
            }
            if (jjmatchedKind == 28) {
                if (this.image == null) {
                    this.image = new StringBuffer();
                }
                this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
                this.jjimageLen = 0;
                final StringBuffer image2 = this.image;
                image2.deleteCharAt(image2.length() - 1);
                return;
            }
            if (jjmatchedKind == 29) {
                if (this.image == null) {
                    this.image = new StringBuffer();
                }
                this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
                this.jjimageLen = 0;
                final StringBuffer image3 = this.image;
                image3.deleteCharAt(image3.length() - 2);
                return;
            }
            switch (jjmatchedKind) {
                default: {}
                case 26: {
                    if (this.image == null) {
                        this.image = new StringBuffer();
                    }
                    this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
                    this.jjimageLen = 0;
                    if (--AddressListParserTokenManager.commentNest == 0) {
                        this.SwitchTo(2);
                        return;
                    }
                    break;
                }
                case 25: {
                    if (this.image == null) {
                        this.image = new StringBuffer();
                    }
                    this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
                    this.jjimageLen = 0;
                    ++AddressListParserTokenManager.commentNest;
                }
                case 24: {
                    if (this.image == null) {
                        this.image = new StringBuffer();
                    }
                    this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
                    this.jjimageLen = 0;
                    final StringBuffer image4 = this.image;
                    image4.deleteCharAt(image4.length() - 2);
                }
            }
        }
        else {
            if (this.image == null) {
                this.image = new StringBuffer();
            }
            this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
            this.jjimageLen = 0;
            final StringBuffer image5 = this.image;
            image5.deleteCharAt(image5.length() - 2);
        }
    }
    
    public void ReInit(final SimpleCharStream input_stream) {
        this.jjnewStateCnt = 0;
        this.jjmatchedPos = 0;
        this.curLexState = this.defaultLexState;
        this.input_stream = input_stream;
        this.ReInitRounds();
    }
    
    public void ReInit(final SimpleCharStream simpleCharStream, final int n) {
        this.ReInit(simpleCharStream);
        this.SwitchTo(n);
    }
    
    public void SwitchTo(final int curLexState) {
        if (curLexState < 5 && curLexState >= 0) {
            this.curLexState = curLexState;
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Error: Ignoring invalid lexical state : ");
        sb.append(curLexState);
        sb.append(". State unchanged.");
        throw new TokenMgrError(sb.toString(), 2);
    }
    
    void TokenLexicalActions(final Token token) {
        final int jjmatchedKind = this.jjmatchedKind;
        String image3;
        if (jjmatchedKind != 18) {
            if (jjmatchedKind != 31) {
                return;
            }
            if (this.image == null) {
                this.image = new StringBuffer();
            }
            final StringBuffer image = this.image;
            final SimpleCharStream input_stream = this.input_stream;
            final int jjimageLen = this.jjimageLen;
            final int lengthOfMatch = this.jjmatchedPos + 1;
            this.lengthOfMatch = lengthOfMatch;
            image.append(input_stream.GetSuffix(jjimageLen + lengthOfMatch));
            final StringBuffer image2 = this.image;
            image3 = image2.substring(0, image2.length() - 1);
        }
        else {
            if (this.image == null) {
                this.image = new StringBuffer();
            }
            final StringBuffer image4 = this.image;
            final SimpleCharStream input_stream2 = this.input_stream;
            final int jjimageLen2 = this.jjimageLen;
            final int lengthOfMatch2 = this.jjmatchedPos + 1;
            this.lengthOfMatch = lengthOfMatch2;
            image4.append(input_stream2.GetSuffix(jjimageLen2 + lengthOfMatch2));
            image3 = this.image.toString();
        }
        token.image = image3;
    }
    
    public Token getNextToken() {
        final String s = "";
        final Token token = null;
        Object specialToken = null;
        int n = 0;
        try {
            int n3 = 0;
        Label_0436:
            while (true) {
                this.curChar = this.input_stream.BeginToken();
                this.image = null;
                this.jjimageLen = 0;
                int n2 = n;
                while (true) {
                    final int curLexState = this.curLexState;
                    if (curLexState != 0) {
                        if (curLexState != 1) {
                            if (curLexState != 2) {
                                if (curLexState != 3) {
                                    if (curLexState == 4) {
                                        this.jjmatchedKind = Integer.MAX_VALUE;
                                        this.jjmatchedPos = 0;
                                        n2 = this.jjMoveStringLiteralDfa0_4();
                                    }
                                }
                                else {
                                    this.jjmatchedKind = Integer.MAX_VALUE;
                                    this.jjmatchedPos = 0;
                                    n2 = this.jjMoveStringLiteralDfa0_3();
                                }
                            }
                            else {
                                this.jjmatchedKind = Integer.MAX_VALUE;
                                this.jjmatchedPos = 0;
                                n2 = this.jjMoveStringLiteralDfa0_2();
                            }
                        }
                        else {
                            this.jjmatchedKind = Integer.MAX_VALUE;
                            this.jjmatchedPos = 0;
                            n2 = this.jjMoveStringLiteralDfa0_1();
                        }
                    }
                    else {
                        this.jjmatchedKind = Integer.MAX_VALUE;
                        this.jjmatchedPos = 0;
                        n2 = this.jjMoveStringLiteralDfa0_0();
                    }
                    n3 = n2;
                    if (this.jjmatchedKind == Integer.MAX_VALUE) {
                        break Label_0436;
                    }
                    final int jjmatchedPos = this.jjmatchedPos;
                    if (jjmatchedPos + 1 < n2) {
                        this.input_stream.backup(n2 - jjmatchedPos - 1);
                    }
                    final long[] jjtoToken = AddressListParserTokenManager.jjtoToken;
                    final int jjmatchedKind = this.jjmatchedKind;
                    final int n4 = jjmatchedKind >> 6;
                    final long n5 = jjtoToken[n4];
                    final long n6 = 1L << (jjmatchedKind & 0x3F);
                    if ((n5 & n6) != 0x0L) {
                        final Token jjFillToken = this.jjFillToken();
                        jjFillToken.specialToken = (Token)specialToken;
                        this.TokenLexicalActions(jjFillToken);
                        specialToken = AddressListParserTokenManager.jjnewLexState;
                        final int jjmatchedKind2 = this.jjmatchedKind;
                        if (specialToken[jjmatchedKind2] != -1) {
                            this.curLexState = specialToken[jjmatchedKind2];
                        }
                        return jjFillToken;
                    }
                    if ((AddressListParserTokenManager.jjtoSkip[n4] & n6) == 0x0L) {
                        this.MoreLexicalActions();
                        final int[] jjnewLexState = AddressListParserTokenManager.jjnewLexState;
                        final int jjmatchedKind3 = this.jjmatchedKind;
                        if (jjnewLexState[jjmatchedKind3] != -1) {
                            this.curLexState = jjnewLexState[jjmatchedKind3];
                        }
                        this.jjmatchedKind = Integer.MAX_VALUE;
                        try {
                            this.curChar = this.input_stream.readChar();
                            n2 = 0;
                            continue;
                        }
                        catch (IOException specialToken) {
                            n3 = 0;
                        }
                        break Label_0436;
                    }
                    Token jjFillToken2 = (Token)specialToken;
                    if ((n6 & AddressListParserTokenManager.jjtoSpecial[n4]) != 0x0L) {
                        jjFillToken2 = this.jjFillToken();
                        if (specialToken != null) {
                            jjFillToken2.specialToken = (Token)specialToken;
                            ((Token)specialToken).next = jjFillToken2;
                        }
                    }
                    final int[] jjnewLexState2 = AddressListParserTokenManager.jjnewLexState;
                    final int jjmatchedKind4 = this.jjmatchedKind;
                    specialToken = jjFillToken2;
                    n = n2;
                    if (jjnewLexState2[jjmatchedKind4] != -1) {
                        this.curLexState = jjnewLexState2[jjmatchedKind4];
                        specialToken = jjFillToken2;
                        n = n2;
                        break;
                    }
                    break;
                }
            }
            int endLine = this.input_stream.getEndLine();
            int endColumn = this.input_stream.getEndColumn();
            boolean b;
            try {
                this.input_stream.readChar();
                this.input_stream.backup(1);
                b = false;
                specialToken = token;
            }
            catch (IOException specialToken) {
                if (n3 <= 1) {
                    specialToken = "";
                }
                else {
                    specialToken = this.input_stream.GetImage();
                }
                final char curChar = this.curChar;
                if (curChar != '\n' && curChar != '\r') {
                    ++endColumn;
                    b = true;
                }
                else {
                    ++endLine;
                    b = true;
                    endColumn = 0;
                }
            }
            if (!b) {
                this.input_stream.backup(1);
                if (n3 <= 1) {
                    specialToken = s;
                }
                else {
                    specialToken = this.input_stream.GetImage();
                }
            }
            throw new TokenMgrError(b, this.curLexState, endLine, endColumn, (String)specialToken, this.curChar, 0);
        }
        catch (IOException ex) {
            this.jjmatchedKind = 0;
            final Token jjFillToken3 = this.jjFillToken();
            jjFillToken3.specialToken = (Token)specialToken;
            return jjFillToken3;
        }
    }
    
    protected Token jjFillToken() {
        final Token token = Token.newToken(this.jjmatchedKind);
        token.kind = this.jjmatchedKind;
        String getImage;
        if ((getImage = AddressListParserTokenManager.jjstrLiteralImages[this.jjmatchedKind]) == null) {
            getImage = this.input_stream.GetImage();
        }
        token.image = getImage;
        token.beginLine = this.input_stream.getBeginLine();
        token.beginColumn = this.input_stream.getBeginColumn();
        token.endLine = this.input_stream.getEndLine();
        token.endColumn = this.input_stream.getEndColumn();
        return token;
    }
    
    public void setDebugStream(final PrintStream debugStream) {
        this.debugStream = debugStream;
    }
}
