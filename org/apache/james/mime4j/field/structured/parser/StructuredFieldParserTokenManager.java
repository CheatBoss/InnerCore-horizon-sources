package org.apache.james.mime4j.field.structured.parser;

import java.io.*;

public class StructuredFieldParserTokenManager implements StructuredFieldParserConstants
{
    static final long[] jjbitVec0;
    public static final int[] jjnewLexState;
    static final int[] jjnextStates;
    public static final String[] jjstrLiteralImages;
    static final long[] jjtoMore;
    static final long[] jjtoSkip;
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
        jjstrLiteralImages = new String[] { "", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null };
        lexStateNames = new String[] { "DEFAULT", "INCOMMENT", "NESTED_COMMENT", "INQUOTEDSTRING" };
        jjnewLexState = new int[] { -1, 1, 0, 2, -1, -1, -1, -1, -1, 3, -1, -1, -1, 0, -1, -1, -1, -1 };
        jjtoToken = new long[] { 63489L };
        jjtoSkip = new long[] { 1022L };
        jjtoMore = new long[] { 1024L };
    }
    
    public StructuredFieldParserTokenManager(final SimpleCharStream input_stream) {
        this.debugStream = System.out;
        this.jjrounds = new int[6];
        this.jjstateSet = new int[12];
        this.curLexState = 0;
        this.defaultLexState = 0;
        this.input_stream = input_stream;
    }
    
    public StructuredFieldParserTokenManager(final SimpleCharStream simpleCharStream, final int n) {
        this(simpleCharStream);
        this.SwitchTo(n);
    }
    
    private final void ReInitRounds() {
        this.jjround = -2147483647;
        int n = 6;
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
            this.jjstateSet[this.jjnewStateCnt++] = StructuredFieldParserTokenManager.jjnextStates[n];
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
        this.jjCheckNAdd(StructuredFieldParserTokenManager.jjnextStates[n]);
        this.jjCheckNAdd(StructuredFieldParserTokenManager.jjnextStates[n + 1]);
    }
    
    private final void jjCheckNAddStates(int n, final int n2) {
        while (true) {
            this.jjCheckNAdd(StructuredFieldParserTokenManager.jjnextStates[n]);
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
        final int n2 = Integer.MAX_VALUE;
        int jjnewStateCnt2 = 0;
        int jjmatchedPos = n;
        n = n2;
    Label_0307_Outer:
        while (true) {
            final int jjround = this.jjround + 1;
            this.jjround = jjround;
            if (jjround == Integer.MAX_VALUE) {
                this.ReInitRounds();
            }
            final char curChar = this.curChar;
        Label_0234_Outer:
            while (true) {
                if (curChar < '@') {
                    final long n3 = 1L << curChar;
                    int n4 = jjnewStateCnt;
                    int i;
                    do {
                        final int[] jjstateSet = this.jjstateSet;
                        i = n4 - 1;
                        jjnewStateCnt = jjstateSet[i];
                        final int n5 = 14;
                        if (jjnewStateCnt != 0) {
                            if (jjnewStateCnt != 1) {
                                if (jjnewStateCnt != 2) {
                                    jjnewStateCnt = n;
                                }
                                else if ((n3 & 0xFFFFFEFAFFFFD9FFL) != 0x0L) {
                                    if ((jjnewStateCnt = n) > 15) {
                                        jjnewStateCnt = 15;
                                    }
                                    this.jjCheckNAdd(1);
                                }
                                else {
                                    jjnewStateCnt = n;
                                    if ((n3 & 0x100002600L) != 0x0L) {
                                        if (n > 14) {
                                            jjnewStateCnt = n5;
                                        }
                                        else {
                                            jjnewStateCnt = n;
                                        }
                                        this.jjCheckNAdd(0);
                                    }
                                }
                            }
                            else if ((n3 & 0xFFFFFEFAFFFFD9FFL) == 0x0L) {
                                jjnewStateCnt = n;
                            }
                            else {
                                this.jjCheckNAdd(1);
                                jjnewStateCnt = 15;
                            }
                        }
                        else if ((n3 & 0x100002600L) == 0x0L) {
                            jjnewStateCnt = n;
                        }
                        else {
                            this.jjCheckNAdd(0);
                            jjnewStateCnt = 14;
                        }
                        n4 = i;
                        n = jjnewStateCnt;
                    } while (i != jjnewStateCnt2);
                }
                else {
                    final int n6 = jjnewStateCnt;
                    final int n7 = n;
                    if (curChar >= '\u0080') {
                        break Label_0307;
                    }
                    int n8 = jjnewStateCnt;
                    int j;
                    do {
                        final int[] jjstateSet2 = this.jjstateSet;
                        j = n8 - 1;
                        jjnewStateCnt = jjstateSet2[j];
                        if (jjnewStateCnt != 1 && jjnewStateCnt != 2) {
                            jjnewStateCnt = n;
                        }
                        else {
                            this.jjCheckNAdd(1);
                            jjnewStateCnt = 15;
                        }
                        n8 = j;
                        n = jjnewStateCnt;
                    } while (j != jjnewStateCnt2);
                }
            Label_0470:
                while (true) {
                    if ((n = jjnewStateCnt) != Integer.MAX_VALUE) {
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
                        continue Label_0307_Outer;
                    }
                    catch (IOException ex) {
                        return jjmatchedPos;
                    }
                    break Label_0470;
                    final int[] jjstateSet3 = this.jjstateSet;
                    final int n6 = n6 - 1;
                    jjnewStateCnt = jjstateSet3[n6];
                    if (jjnewStateCnt != 1 && jjnewStateCnt != 2) {
                        final int n7;
                        jjnewStateCnt = n7;
                    }
                    else if ((StructuredFieldParserTokenManager.jjbitVec0[(curChar & '\u00ff') >> 6] & 1L << (curChar & '?')) == 0x0L) {
                        final int n7;
                        jjnewStateCnt = n7;
                    }
                    else {
                        final int n7;
                        if ((jjnewStateCnt = n7) > 15) {
                            jjnewStateCnt = 15;
                        }
                        this.jjCheckNAdd(1);
                    }
                    if (n6 == jjnewStateCnt2) {
                        continue;
                    }
                    break;
                }
                final int n7 = jjnewStateCnt;
                continue Label_0234_Outer;
            }
        }
        return jjmatchedPos;
    }
    
    private final int jjMoveNfa_1(int jjnewStateCnt, int i) {
        this.jjnewStateCnt = 1;
        this.jjstateSet[0] = jjnewStateCnt;
        jjnewStateCnt = 1;
        final int n = Integer.MAX_VALUE;
        int jjnewStateCnt2 = 0;
        int jjmatchedPos = i;
        i = n;
        while (true) {
            final int jjround = this.jjround + 1;
            this.jjround = jjround;
            if (jjround == Integer.MAX_VALUE) {
                this.ReInitRounds();
            }
            final char curChar = this.curChar;
            if (curChar < '@') {
                int n2 = jjnewStateCnt;
                int j;
                do {
                    final int[] jjstateSet = this.jjstateSet;
                    j = n2 - 1;
                    if (jjstateSet[j] != 0) {
                        jjnewStateCnt = i;
                    }
                    else {
                        jjnewStateCnt = i;
                        if ((1L << curChar & 0xFFFFFCFFFFFFFFFFL) != 0x0L) {
                            jjnewStateCnt = 4;
                        }
                    }
                    n2 = j;
                    i = jjnewStateCnt;
                } while (j != jjnewStateCnt2);
            }
            else {
                int n3 = jjnewStateCnt;
                int n4 = i;
                if (curChar < '\u0080') {
                    int n5 = jjnewStateCnt;
                    int k;
                    do {
                        final int[] jjstateSet2 = this.jjstateSet;
                        k = n5 - 1;
                        if (jjstateSet2[k] != 0) {
                            jjnewStateCnt = i;
                        }
                        else {
                            jjnewStateCnt = 4;
                        }
                        n5 = k;
                        i = jjnewStateCnt;
                    } while (k != jjnewStateCnt2);
                }
                else {
                    do {
                        final int[] jjstateSet3 = this.jjstateSet;
                        i = n3 - 1;
                        if (jjstateSet3[i] != 0) {
                            jjnewStateCnt = n4;
                        }
                        else {
                            jjnewStateCnt = n4;
                            if ((StructuredFieldParserTokenManager.jjbitVec0[(curChar & '\u00ff') >> 6] & 1L << (curChar & '?')) != 0x0L && (jjnewStateCnt = n4) > 4) {
                                jjnewStateCnt = 4;
                            }
                        }
                        n3 = i;
                        n4 = jjnewStateCnt;
                    } while (i != jjnewStateCnt2);
                }
            }
            if ((i = jjnewStateCnt) != Integer.MAX_VALUE) {
                this.jjmatchedKind = jjnewStateCnt;
                this.jjmatchedPos = jjmatchedPos;
                i = Integer.MAX_VALUE;
            }
            ++jjmatchedPos;
            jjnewStateCnt = this.jjnewStateCnt;
            this.jjnewStateCnt = jjnewStateCnt2;
            jjnewStateCnt2 = 1 - jjnewStateCnt2;
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
    
    private final int jjMoveNfa_2(int n, int jjmatchedKind) {
        this.jjnewStateCnt = 3;
        this.jjstateSet[0] = n;
        n = 1;
        final int n2 = Integer.MAX_VALUE;
        int jjnewStateCnt = 0;
        int jjmatchedPos = jjmatchedKind;
        jjmatchedKind = n2;
        while (true) {
            final int jjround = this.jjround + 1;
            this.jjround = jjround;
            if (jjround == Integer.MAX_VALUE) {
                this.ReInitRounds();
            }
            final char curChar = this.curChar;
            if (curChar < '@') {
                int n3 = n;
                while (true) {
                    final int[] jjstateSet = this.jjstateSet;
                    --n3;
                    n = jjstateSet[n3];
                    if (n != 0) {
                        if (n != 1) {
                            n = jjmatchedKind;
                        }
                        else {
                            if ((n = jjmatchedKind) > 7) {
                                n = 7;
                            }
                            final int[] jjstateSet2 = this.jjstateSet;
                            jjmatchedKind = this.jjnewStateCnt++;
                            jjstateSet2[jjmatchedKind] = 1;
                        }
                    }
                    else {
                        n = jjmatchedKind;
                        if ((1L << curChar & 0xFFFFFCFFFFFFFFFFL) != 0x0L && (n = jjmatchedKind) > 8) {
                            n = 8;
                        }
                    }
                    if (n3 == jjnewStateCnt) {
                        break;
                    }
                    jjmatchedKind = n;
                }
                jjmatchedKind = n;
            }
            else {
                if (curChar < '\u0080') {
                    int n4 = n;
                    int i;
                    do {
                        final int[] jjstateSet3 = this.jjstateSet;
                        i = n4 - 1;
                        n = jjstateSet3[i];
                        Label_0276: {
                            if (n != 0) {
                                if (n != 1) {
                                    if (n != 2) {
                                        n = jjmatchedKind;
                                        break Label_0276;
                                    }
                                    if ((n = jjmatchedKind) > 8) {
                                        n = 8;
                                    }
                                    break Label_0276;
                                }
                                else if ((n = jjmatchedKind) > 7) {
                                    n = 7;
                                }
                            }
                            else {
                                int n5;
                                if ((n5 = jjmatchedKind) > 8) {
                                    n5 = 8;
                                }
                                n = n5;
                                if (this.curChar != '\\') {
                                    break Label_0276;
                                }
                                n = n5;
                            }
                            this.jjCheckNAdd(1);
                        }
                        n4 = i;
                        jjmatchedKind = n;
                    } while (i != jjnewStateCnt);
                }
                else {
                    final int n6 = (curChar & '\u00ff') >> 6;
                    final long n7 = 1L << (curChar & '?');
                    int n8 = n;
                    int j;
                    do {
                        final int[] jjstateSet4 = this.jjstateSet;
                        j = n8 - 1;
                        n = jjstateSet4[j];
                        if (n != 0) {
                            if (n != 1) {
                                n = jjmatchedKind;
                            }
                            else if ((StructuredFieldParserTokenManager.jjbitVec0[n6] & n7) == 0x0L) {
                                n = jjmatchedKind;
                            }
                            else {
                                if ((n = jjmatchedKind) > 7) {
                                    n = 7;
                                }
                                final int[] jjstateSet5 = this.jjstateSet;
                                jjmatchedKind = this.jjnewStateCnt++;
                                jjstateSet5[jjmatchedKind] = 1;
                            }
                        }
                        else {
                            n = jjmatchedKind;
                            if ((StructuredFieldParserTokenManager.jjbitVec0[n6] & n7) != 0x0L && (n = jjmatchedKind) > 8) {
                                n = 8;
                            }
                        }
                        n8 = j;
                        jjmatchedKind = n;
                    } while (j != jjnewStateCnt);
                }
                jjmatchedKind = n;
            }
            n = jjmatchedKind;
            if (jjmatchedKind != Integer.MAX_VALUE) {
                this.jjmatchedKind = jjmatchedKind;
                this.jjmatchedPos = jjmatchedPos;
                n = Integer.MAX_VALUE;
            }
            ++jjmatchedPos;
            final int jjnewStateCnt2 = this.jjnewStateCnt;
            this.jjnewStateCnt = jjnewStateCnt;
            jjnewStateCnt = 3 - jjnewStateCnt;
            if (jjnewStateCnt2 == jjnewStateCnt) {
                break;
            }
            try {
                this.curChar = this.input_stream.readChar();
                jjmatchedKind = n;
                n = jjnewStateCnt2;
            }
            catch (IOException ex) {
                return jjmatchedPos;
            }
        }
        return jjmatchedPos;
    }
    
    private final int jjMoveNfa_3(int jjnewStateCnt, int n) {
        this.jjnewStateCnt = 6;
        this.jjstateSet[0] = jjnewStateCnt;
        jjnewStateCnt = 1;
        final int n2 = Integer.MAX_VALUE;
        int jjnewStateCnt2 = 0;
        int jjmatchedPos = n;
        n = n2;
    Label_0547_Outer:
        while (true) {
            final int jjround = this.jjround + 1;
            this.jjround = jjround;
            if (jjround == Integer.MAX_VALUE) {
                this.ReInitRounds();
            }
            final char curChar = this.curChar;
            while (true) {
                if (curChar < '@') {
                    final long n3 = 1L << curChar;
                    int n4 = jjnewStateCnt;
                    while (true) {
                        final int[] jjstateSet = this.jjstateSet;
                        --n4;
                        final int n5 = jjstateSet[n4];
                        Label_0351: {
                            Label_0313: {
                                if (n5 != 0) {
                                    if (n5 == 1) {
                                        if ((jjnewStateCnt = n) > 10) {
                                            jjnewStateCnt = 10;
                                        }
                                        final int[] jjstateSet2 = this.jjstateSet;
                                        n = this.jjnewStateCnt++;
                                        jjstateSet2[n] = 1;
                                        break Label_0351;
                                    }
                                    if (n5 != 2) {
                                        jjnewStateCnt = 12;
                                        Label_0219: {
                                            if (n5 != 3) {
                                                if (n5 != 4) {
                                                    if (n5 != 5) {
                                                        jjnewStateCnt = n;
                                                        break Label_0351;
                                                    }
                                                    jjnewStateCnt = n;
                                                    if (this.curChar == '\r') {
                                                        jjnewStateCnt = this.jjnewStateCnt++;
                                                        jjstateSet[jjnewStateCnt] = 3;
                                                        jjnewStateCnt = n;
                                                    }
                                                    break Label_0351;
                                                }
                                                else {
                                                    if ((n3 & 0x100000200L) == 0x0L) {
                                                        jjnewStateCnt = n;
                                                        break Label_0351;
                                                    }
                                                    if (n > 12) {
                                                        break Label_0219;
                                                    }
                                                }
                                            }
                                            else {
                                                if (this.curChar != '\n') {
                                                    jjnewStateCnt = n;
                                                    break Label_0351;
                                                }
                                                if (n > 12) {
                                                    break Label_0219;
                                                }
                                            }
                                            jjnewStateCnt = n;
                                        }
                                        this.jjCheckNAdd(4);
                                        break Label_0351;
                                    }
                                    if ((n3 & 0xFFFFFFFBFFFFDFFFL) == 0x0L) {
                                        jjnewStateCnt = n;
                                        break Label_0351;
                                    }
                                    if ((jjnewStateCnt = n) <= 11) {
                                        break Label_0313;
                                    }
                                }
                                else if ((n3 & 0xFFFFFFFBFFFFDFFFL) != 0x0L) {
                                    if ((jjnewStateCnt = n) <= 11) {
                                        break Label_0313;
                                    }
                                }
                                else {
                                    jjnewStateCnt = n;
                                    if (this.curChar == '\r') {
                                        jjnewStateCnt = this.jjnewStateCnt++;
                                        jjstateSet[jjnewStateCnt] = 3;
                                        jjnewStateCnt = n;
                                    }
                                    break Label_0351;
                                }
                                jjnewStateCnt = 11;
                            }
                            this.jjCheckNAdd(2);
                        }
                        if (n4 == jjnewStateCnt2) {
                            break;
                        }
                        n = jjnewStateCnt;
                    }
                }
                else {
                    if (curChar >= '\u0080') {
                        final int n6 = (curChar & '\u00ff') >> 6;
                        final long n7 = 1L << (curChar & '?');
                        break Label_0547;
                    }
                    final long n8 = 1L << (curChar & '?');
                    int n9 = jjnewStateCnt;
                    int i;
                    do {
                        final int[] jjstateSet3 = this.jjstateSet;
                        i = n9 - 1;
                        jjnewStateCnt = jjstateSet3[i];
                        Label_0512: {
                            while (true) {
                                Label_0507: {
                                    Label_0488: {
                                        Label_0485: {
                                            if (jjnewStateCnt != 0) {
                                                if (jjnewStateCnt != 1) {
                                                    if (jjnewStateCnt != 2) {
                                                        jjnewStateCnt = n;
                                                        break Label_0512;
                                                    }
                                                    if ((n8 & 0xFFFFFFFFEFFFFFFFL) == 0x0L) {
                                                        jjnewStateCnt = n;
                                                        break Label_0512;
                                                    }
                                                    if ((jjnewStateCnt = n) > 11) {
                                                        break Label_0485;
                                                    }
                                                    break Label_0488;
                                                }
                                                else {
                                                    if (n <= 10) {
                                                        break Label_0507;
                                                    }
                                                    jjnewStateCnt = 10;
                                                }
                                            }
                                            else if ((n8 & 0xFFFFFFFFEFFFFFFFL) != 0x0L) {
                                                if ((jjnewStateCnt = n) > 11) {
                                                    break Label_0485;
                                                }
                                                break Label_0488;
                                            }
                                            else {
                                                jjnewStateCnt = n;
                                                if (this.curChar == '\\') {
                                                    break Label_0507;
                                                }
                                                break Label_0512;
                                            }
                                            this.jjCheckNAdd(1);
                                            break Label_0512;
                                        }
                                        jjnewStateCnt = 11;
                                    }
                                    this.jjCheckNAdd(2);
                                    break Label_0512;
                                }
                                jjnewStateCnt = n;
                                continue;
                            }
                        }
                        n9 = i;
                        n = jjnewStateCnt;
                    } while (i != jjnewStateCnt2);
                }
                final int n10;
                Label_0754: {
                    Label_0684: {
                        break Label_0684;
                        final int[] jjstateSet4 = this.jjstateSet;
                        n10 = jjnewStateCnt - 1;
                        jjnewStateCnt = jjstateSet4[n10];
                        Label_0676: {
                            Label_0653: {
                                final int n6;
                                final long n7;
                                Label_0637: {
                                    if (jjnewStateCnt != 0) {
                                        if (jjnewStateCnt != 1) {
                                            if (jjnewStateCnt == 2) {
                                                break Label_0637;
                                            }
                                        }
                                        else if ((StructuredFieldParserTokenManager.jjbitVec0[n6] & n7) != 0x0L) {
                                            if ((jjnewStateCnt = n) > 10) {
                                                jjnewStateCnt = 10;
                                            }
                                            final int[] jjstateSet5 = this.jjstateSet;
                                            n = this.jjnewStateCnt++;
                                            jjstateSet5[n] = 1;
                                            break Label_0653;
                                        }
                                        jjnewStateCnt = n;
                                        break Label_0653;
                                    }
                                }
                                if ((StructuredFieldParserTokenManager.jjbitVec0[n6] & n7) != 0x0L) {
                                    if ((jjnewStateCnt = n) > 11) {
                                        jjnewStateCnt = 11;
                                    }
                                    this.jjCheckNAdd(2);
                                    n = jjnewStateCnt;
                                    break Label_0676;
                                }
                                jjnewStateCnt = n;
                            }
                            n = jjnewStateCnt;
                        }
                        if (n10 != jjnewStateCnt2) {
                            break Label_0754;
                        }
                        jjnewStateCnt = n;
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
                    jjnewStateCnt2 = 6 - jjnewStateCnt2;
                    if (jjnewStateCnt == jjnewStateCnt2) {
                        break;
                    }
                    try {
                        this.curChar = this.input_stream.readChar();
                        continue Label_0547_Outer;
                    }
                    catch (IOException ex) {
                        return jjmatchedPos;
                    }
                }
                jjnewStateCnt = n10;
                continue;
            }
        }
        return jjmatchedPos;
    }
    
    private final int jjMoveStringLiteralDfa0_0() {
        final char curChar = this.curChar;
        if (curChar == '\"') {
            return this.jjStopAtPos(0, 9);
        }
        if (curChar != '(') {
            return this.jjMoveNfa_0(2, 0);
        }
        return this.jjStopAtPos(0, 1);
    }
    
    private final int jjMoveStringLiteralDfa0_1() {
        final char curChar = this.curChar;
        int n;
        if (curChar != '(') {
            if (curChar != ')') {
                return this.jjMoveNfa_1(0, 0);
            }
            n = 2;
        }
        else {
            n = 3;
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
            n = 6;
        }
        else {
            n = 5;
        }
        return this.jjStopAtPos(0, n);
    }
    
    private final int jjMoveStringLiteralDfa0_3() {
        if (this.curChar != '\"') {
            return this.jjMoveNfa_3(0, 0);
        }
        return this.jjStopAtPos(0, 13);
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
        if (this.jjmatchedKind != 10) {
            return;
        }
        if (this.image == null) {
            this.image = new StringBuffer();
        }
        this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
        this.jjimageLen = 0;
        final StringBuffer image = this.image;
        image.deleteCharAt(image.length() - 2);
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
    
    void SkipLexicalActions(final Token token) {
        final int jjmatchedKind = this.jjmatchedKind;
        if (jjmatchedKind != 3) {
            if (jjmatchedKind == 5) {
                if (this.image == null) {
                    this.image = new StringBuffer();
                }
                final StringBuffer image = this.image;
                final SimpleCharStream input_stream = this.input_stream;
                final int jjimageLen = this.jjimageLen;
                final int lengthOfMatch = this.jjmatchedPos + 1;
                this.lengthOfMatch = lengthOfMatch;
                image.append(input_stream.GetSuffix(jjimageLen + lengthOfMatch));
                ++this.commentNest;
                final PrintStream out = System.out;
                final StringBuilder sb = new StringBuilder();
                sb.append("+++ COMMENT NEST=");
                sb.append(this.commentNest);
                out.println(sb.toString());
                return;
            }
            if (jjmatchedKind != 6) {
                if (jjmatchedKind != 7) {
                    return;
                }
                if (this.image == null) {
                    this.image = new StringBuffer();
                }
                final StringBuffer image2 = this.image;
                final SimpleCharStream input_stream2 = this.input_stream;
                final int jjimageLen2 = this.jjimageLen;
                final int lengthOfMatch2 = this.jjmatchedPos + 1;
                this.lengthOfMatch = lengthOfMatch2;
                image2.append(input_stream2.GetSuffix(jjimageLen2 + lengthOfMatch2));
                final StringBuffer image3 = this.image;
                image3.deleteCharAt(image3.length() - 2);
            }
            else {
                if (this.image == null) {
                    this.image = new StringBuffer();
                }
                final StringBuffer image4 = this.image;
                final SimpleCharStream input_stream3 = this.input_stream;
                final int jjimageLen3 = this.jjimageLen;
                final int lengthOfMatch3 = this.jjmatchedPos + 1;
                this.lengthOfMatch = lengthOfMatch3;
                image4.append(input_stream3.GetSuffix(jjimageLen3 + lengthOfMatch3));
                --this.commentNest;
                final PrintStream out2 = System.out;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("+++ COMMENT NEST=");
                sb2.append(this.commentNest);
                out2.println(sb2.toString());
                if (this.commentNest == 0) {
                    this.SwitchTo(1);
                }
            }
        }
        else {
            if (this.image == null) {
                this.image = new StringBuffer();
            }
            final StringBuffer image5 = this.image;
            final SimpleCharStream input_stream4 = this.input_stream;
            final int jjimageLen4 = this.jjimageLen;
            final int lengthOfMatch4 = this.jjmatchedPos + 1;
            this.lengthOfMatch = lengthOfMatch4;
            image5.append(input_stream4.GetSuffix(jjimageLen4 + lengthOfMatch4));
            this.commentNest = 1;
        }
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
        if (this.jjmatchedKind != 13) {
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
        int n = 0;
        try {
            String s2 = null;
            int n3 = 0;
        Label_0354:
            while (true) {
                this.curChar = this.input_stream.BeginToken();
                s2 = null;
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
                        break Label_0354;
                    }
                    final int jjmatchedPos = this.jjmatchedPos;
                    if (jjmatchedPos + 1 < n2) {
                        this.input_stream.backup(n2 - jjmatchedPos - 1);
                    }
                    final long[] jjtoToken = StructuredFieldParserTokenManager.jjtoToken;
                    final int jjmatchedKind = this.jjmatchedKind;
                    final int n4 = jjmatchedKind >> 6;
                    final long n5 = jjtoToken[n4];
                    final long n6 = 1L << (jjmatchedKind & 0x3F);
                    if ((n5 & n6) != 0x0L) {
                        final Token jjFillToken = this.jjFillToken();
                        this.TokenLexicalActions(jjFillToken);
                        final int[] jjnewLexState = StructuredFieldParserTokenManager.jjnewLexState;
                        final int jjmatchedKind2 = this.jjmatchedKind;
                        if (jjnewLexState[jjmatchedKind2] != -1) {
                            this.curLexState = jjnewLexState[jjmatchedKind2];
                        }
                        return jjFillToken;
                    }
                    if ((n6 & StructuredFieldParserTokenManager.jjtoSkip[n4]) == 0x0L) {
                        this.MoreLexicalActions();
                        final int[] jjnewLexState2 = StructuredFieldParserTokenManager.jjnewLexState;
                        final int jjmatchedKind3 = this.jjmatchedKind;
                        if (jjnewLexState2[jjmatchedKind3] != -1) {
                            this.curLexState = jjnewLexState2[jjmatchedKind3];
                        }
                        this.jjmatchedKind = Integer.MAX_VALUE;
                        try {
                            this.curChar = this.input_stream.readChar();
                            n2 = 0;
                            continue;
                        }
                        catch (IOException ex) {
                            n3 = 0;
                        }
                        break Label_0354;
                    }
                    this.SkipLexicalActions(null);
                    final int[] jjnewLexState3 = StructuredFieldParserTokenManager.jjnewLexState;
                    final int jjmatchedKind4 = this.jjmatchedKind;
                    n = n2;
                    if (jjnewLexState3[jjmatchedKind4] != -1) {
                        this.curLexState = jjnewLexState3[jjmatchedKind4];
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
            }
            catch (IOException ex2) {
                if (n3 <= 1) {
                    s2 = "";
                }
                else {
                    s2 = this.input_stream.GetImage();
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
                    s2 = s;
                }
                else {
                    s2 = this.input_stream.GetImage();
                }
            }
            throw new TokenMgrError(b, this.curLexState, endLine, endColumn, s2, this.curChar, 0);
        }
        catch (IOException ex3) {
            this.jjmatchedKind = 0;
            return this.jjFillToken();
        }
    }
    
    protected Token jjFillToken() {
        final Token token = Token.newToken(this.jjmatchedKind);
        token.kind = this.jjmatchedKind;
        String getImage;
        if ((getImage = StructuredFieldParserTokenManager.jjstrLiteralImages[this.jjmatchedKind]) == null) {
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
