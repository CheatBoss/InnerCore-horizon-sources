package org.apache.james.mime4j.field.mimeversion.parser;

import java.io.*;

public class MimeVersionParserTokenManager implements MimeVersionParserConstants
{
    static final long[] jjbitVec0;
    public static final int[] jjnewLexState;
    static final int[] jjnextStates;
    public static final String[] jjstrLiteralImages;
    static final long[] jjtoMore;
    static final long[] jjtoSkip;
    static final long[] jjtoSpecial;
    static final long[] jjtoToken;
    public static final String[] lexStateNames;
    int commentNest;
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
        jjstrLiteralImages = new String[] { "", "\r", "\n", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, ".", null, null };
        lexStateNames = new String[] { "DEFAULT", "INCOMMENT", "NESTED_COMMENT", "INQUOTEDSTRING" };
        jjnewLexState = new int[] { -1, -1, -1, -1, 1, 0, -1, 2, -1, -1, -1, -1, -1, 3, -1, -1, 0, -1, -1, -1, -1 };
        jjtoToken = new long[] { 458759L };
        jjtoSkip = new long[] { 40L };
        jjtoSpecial = new long[] { 8L };
        jjtoMore = new long[] { 65488L };
    }
    
    public MimeVersionParserTokenManager(final SimpleCharStream input_stream) {
        this.debugStream = System.out;
        this.jjrounds = new int[3];
        this.jjstateSet = new int[6];
        this.curLexState = 0;
        this.defaultLexState = 0;
        this.input_stream = input_stream;
    }
    
    public MimeVersionParserTokenManager(final SimpleCharStream simpleCharStream, final int n) {
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
            this.jjstateSet[this.jjnewStateCnt++] = MimeVersionParserTokenManager.jjnextStates[n];
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
        this.jjCheckNAdd(MimeVersionParserTokenManager.jjnextStates[n]);
        this.jjCheckNAdd(MimeVersionParserTokenManager.jjnextStates[n + 1]);
    }
    
    private final void jjCheckNAddStates(int n, final int n2) {
        while (true) {
            this.jjCheckNAdd(MimeVersionParserTokenManager.jjnextStates[n]);
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
        this.jjnewStateCnt = 2;
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
                while (true) {
                    final int[] jjstateSet = this.jjstateSet;
                    --n4;
                    final int n5 = jjstateSet[n4];
                    final int n6 = 3;
                    jjnewStateCnt = 17;
                    if (n5 != 0) {
                        if (n5 != 1) {
                            if (n5 != 2) {
                                jjnewStateCnt = n;
                            }
                            else if ((n3 & 0x3FF000000000000L) != 0x0L) {
                                if (n <= 17) {
                                    jjnewStateCnt = n;
                                }
                                this.jjCheckNAdd(1);
                            }
                            else {
                                jjnewStateCnt = n;
                                if ((n3 & 0x100002600L) != 0x0L) {
                                    if (n > 3) {
                                        jjnewStateCnt = n6;
                                    }
                                    else {
                                        jjnewStateCnt = n;
                                    }
                                    this.jjCheckNAdd(0);
                                }
                            }
                        }
                        else if ((n3 & 0x3FF000000000000L) == 0x0L) {
                            jjnewStateCnt = n;
                        }
                        else {
                            this.jjCheckNAdd(1);
                            jjnewStateCnt = 17;
                        }
                    }
                    else if ((n3 & 0x100002600L) == 0x0L) {
                        jjnewStateCnt = n;
                    }
                    else {
                        this.jjCheckNAdd(0);
                        jjnewStateCnt = 3;
                    }
                    if (n4 == jjnewStateCnt2) {
                        break;
                    }
                    n = jjnewStateCnt;
                }
            }
            else {
                int n7 = jjnewStateCnt;
                if (curChar < '\u0080') {
                    int n8;
                    do {
                        final int[] jjstateSet2 = this.jjstateSet;
                        n8 = jjnewStateCnt - 1;
                        jjnewStateCnt = jjstateSet2[n8];
                    } while ((jjnewStateCnt = n8) != jjnewStateCnt2);
                    jjnewStateCnt = n;
                }
                else {
                    do {
                        final int[] jjstateSet3 = this.jjstateSet;
                        jjnewStateCnt = n7 - 1;
                        final int n9 = jjstateSet3[jjnewStateCnt];
                    } while ((n7 = jjnewStateCnt) != jjnewStateCnt2);
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
            jjnewStateCnt2 = 2 - jjnewStateCnt2;
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
                        else if ((jjnewStateCnt = n) > 6) {
                            jjnewStateCnt = 6;
                        }
                    }
                    else if ((jjnewStateCnt = n) > 8) {
                        jjnewStateCnt = 8;
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
                            else if ((jjnewStateCnt = n) > 8) {
                                jjnewStateCnt = 8;
                            }
                        }
                        else if ((jjnewStateCnt = n) > 6) {
                            jjnewStateCnt = 6;
                        }
                    }
                    else {
                        int n5;
                        if ((n5 = n) > 8) {
                            n5 = 8;
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
                            if ((MimeVersionParserTokenManager.jjbitVec0[n6] & n7) != 0x0L && (jjnewStateCnt = n) > 6) {
                                jjnewStateCnt = 6;
                            }
                        }
                    }
                    else {
                        jjnewStateCnt = n;
                        if ((MimeVersionParserTokenManager.jjbitVec0[n6] & n7) != 0x0L && (jjnewStateCnt = n) > 8) {
                            jjnewStateCnt = 8;
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
                        else if ((jjnewStateCnt = n) > 9) {
                            jjnewStateCnt = 9;
                        }
                    }
                    else if ((jjnewStateCnt = n) > 12) {
                        jjnewStateCnt = 12;
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
                            else if ((jjnewStateCnt = n) > 12) {
                                jjnewStateCnt = 12;
                            }
                        }
                        else if ((jjnewStateCnt = n) > 9) {
                            jjnewStateCnt = 9;
                        }
                    }
                    else {
                        int n5;
                        if ((n5 = n) > 12) {
                            n5 = 12;
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
                            if ((MimeVersionParserTokenManager.jjbitVec0[n6] & n7) != 0x0L && (jjnewStateCnt = n) > 9) {
                                jjnewStateCnt = 9;
                            }
                        }
                    }
                    else {
                        jjnewStateCnt = n;
                        if ((MimeVersionParserTokenManager.jjbitVec0[n6] & n7) != 0x0L && (jjnewStateCnt = n) > 12) {
                            jjnewStateCnt = 12;
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
                                    if ((jjnewStateCnt = n) > 14) {
                                        jjnewStateCnt = 14;
                                    }
                                    break Label_0152;
                                }
                            }
                            if ((1L << curChar & 0xFFFFFFFBFFFFFFFFL) == 0x0L) {
                                jjnewStateCnt = n;
                            }
                            else {
                                if ((jjnewStateCnt = n) > 15) {
                                    jjnewStateCnt = 15;
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
                                        if ((jjnewStateCnt = n) <= 15) {
                                            break Label_0287;
                                        }
                                    }
                                    else {
                                        if ((jjnewStateCnt = n) > 14) {
                                            jjnewStateCnt = 14;
                                        }
                                        break Label_0325;
                                    }
                                }
                                else if ((n7 & 0xFFFFFFFFEFFFFFFFL) != 0x0L) {
                                    if ((jjnewStateCnt = n) <= 15) {
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
                                jjnewStateCnt = 15;
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
                                if ((MimeVersionParserTokenManager.jjbitVec0[n4] & n5) != 0x0L && (jjnewStateCnt = n) > 14) {
                                    jjnewStateCnt = 14;
                                }
                                break Label_0465;
                            }
                        }
                        if ((MimeVersionParserTokenManager.jjbitVec0[n4] & n5) == 0x0L) {
                            jjnewStateCnt = n;
                        }
                        else {
                            if ((jjnewStateCnt = n) > 15) {
                                jjnewStateCnt = 15;
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
        if (curChar == '\n') {
            return this.jjStartNfaWithStates_0(0, 2, 0);
        }
        if (curChar == '\r') {
            return this.jjStartNfaWithStates_0(0, 1, 0);
        }
        if (curChar == '\"') {
            return this.jjStopAtPos(0, 13);
        }
        if (curChar == '(') {
            return this.jjStopAtPos(0, 4);
        }
        if (curChar != '.') {
            return this.jjMoveNfa_0(2, 0);
        }
        return this.jjStopAtPos(0, 18);
    }
    
    private final int jjMoveStringLiteralDfa0_1() {
        final char curChar = this.curChar;
        int n;
        if (curChar != '(') {
            if (curChar != ')') {
                return this.jjMoveNfa_1(0, 0);
            }
            n = 5;
        }
        else {
            n = 7;
        }
        return this.jjStopAtPos(0, n);
    }
    
    private final int jjMoveStringLiteralDfa0_2() {
        final char curChar = this.curChar;
        int n;
        if (curChar != '(') {
            if (curChar != ')') {
                return this.jjMoveNfa_2(0, 0);
            }
            n = 11;
        }
        else {
            n = 10;
        }
        return this.jjStopAtPos(0, n);
    }
    
    private final int jjMoveStringLiteralDfa0_3() {
        if (this.curChar != '\"') {
            return this.jjMoveNfa_3(0, 0);
        }
        return this.jjStopAtPos(0, 16);
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
    
    void MoreLexicalActions() {
        final int jjimageLen = this.jjimageLen;
        final int lengthOfMatch = this.jjmatchedPos + 1;
        this.lengthOfMatch = lengthOfMatch;
        this.jjimageLen = jjimageLen + lengthOfMatch;
        switch (this.jjmatchedKind) {
            default: {}
            case 14: {
                if (this.image == null) {
                    this.image = new StringBuffer();
                }
                this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
                this.jjimageLen = 0;
                final StringBuffer image = this.image;
                image.deleteCharAt(image.length() - 2);
            }
            case 13: {
                if (this.image == null) {
                    this.image = new StringBuffer();
                }
                this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
                this.jjimageLen = 0;
                final StringBuffer image2 = this.image;
                image2.deleteCharAt(image2.length() - 1);
            }
            case 11: {
                if (this.image == null) {
                    this.image = new StringBuffer();
                }
                this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
                this.jjimageLen = 0;
                if (--this.commentNest == 0) {
                    this.SwitchTo(1);
                    return;
                }
                break;
            }
            case 10: {
                if (this.image == null) {
                    this.image = new StringBuffer();
                }
                this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
                this.jjimageLen = 0;
                ++this.commentNest;
            }
            case 9: {
                if (this.image == null) {
                    this.image = new StringBuffer();
                }
                this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
                this.jjimageLen = 0;
                final StringBuffer image3 = this.image;
                image3.deleteCharAt(image3.length() - 2);
            }
            case 7: {
                if (this.image == null) {
                    this.image = new StringBuffer();
                }
                this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
                this.jjimageLen = 0;
                this.commentNest = 1;
            }
            case 6: {
                if (this.image == null) {
                    this.image = new StringBuffer();
                }
                this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
                this.jjimageLen = 0;
                final StringBuffer image4 = this.image;
                image4.deleteCharAt(image4.length() - 2);
                break;
            }
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
        if (curLexState < 4 && curLexState >= 0) {
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
        if (this.jjmatchedKind != 16) {
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
        token.image = image2.substring(0, image2.length() - 1);
    }
    
    public Token getNextToken() {
        final String s = "";
        final Token token = null;
        Object specialToken = null;
        int n = 0;
        try {
            int n3 = 0;
        Label_0412:
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
                                if (curLexState == 3) {
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
                        break Label_0412;
                    }
                    final int jjmatchedPos = this.jjmatchedPos;
                    if (jjmatchedPos + 1 < n2) {
                        this.input_stream.backup(n2 - jjmatchedPos - 1);
                    }
                    final long[] jjtoToken = MimeVersionParserTokenManager.jjtoToken;
                    final int jjmatchedKind = this.jjmatchedKind;
                    final int n4 = jjmatchedKind >> 6;
                    final long n5 = jjtoToken[n4];
                    final long n6 = 1L << (jjmatchedKind & 0x3F);
                    if ((n5 & n6) != 0x0L) {
                        final Token jjFillToken = this.jjFillToken();
                        jjFillToken.specialToken = (Token)specialToken;
                        this.TokenLexicalActions(jjFillToken);
                        specialToken = MimeVersionParserTokenManager.jjnewLexState;
                        final int jjmatchedKind2 = this.jjmatchedKind;
                        if (specialToken[jjmatchedKind2] != -1) {
                            this.curLexState = specialToken[jjmatchedKind2];
                        }
                        return jjFillToken;
                    }
                    if ((MimeVersionParserTokenManager.jjtoSkip[n4] & n6) == 0x0L) {
                        this.MoreLexicalActions();
                        final int[] jjnewLexState = MimeVersionParserTokenManager.jjnewLexState;
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
                        break Label_0412;
                    }
                    Token jjFillToken2 = (Token)specialToken;
                    if ((n6 & MimeVersionParserTokenManager.jjtoSpecial[n4]) != 0x0L) {
                        jjFillToken2 = this.jjFillToken();
                        if (specialToken != null) {
                            jjFillToken2.specialToken = (Token)specialToken;
                            ((Token)specialToken).next = jjFillToken2;
                        }
                    }
                    final int[] jjnewLexState2 = MimeVersionParserTokenManager.jjnewLexState;
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
        if ((getImage = MimeVersionParserTokenManager.jjstrLiteralImages[this.jjmatchedKind]) == null) {
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
