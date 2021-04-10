package org.apache.james.mime4j.field.datetime.parser;

import java.io.*;

public class DateTimeParserTokenManager implements DateTimeParserConstants
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
        jjstrLiteralImages = new String[] { "", "\r", "\n", ",", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", ":", null, "UT", "GMT", "EST", "EDT", "CST", "CDT", "MST", "MDT", "PST", "PDT", null, null, null, null, null, null, null, null, null, null, null, null, null, null };
        lexStateNames = new String[] { "DEFAULT", "INCOMMENT", "NESTED_COMMENT" };
        jjnewLexState = new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, 0, -1, 2, -1, -1, -1, -1, -1, -1, -1, -1 };
        jjtoToken = new long[] { 70437463654399L };
        jjtoSkip = new long[] { 343597383680L };
        jjtoSpecial = new long[] { 68719476736L };
        jjtoMore = new long[] { 69956427317248L };
    }
    
    public DateTimeParserTokenManager(final SimpleCharStream input_stream) {
        this.debugStream = System.out;
        this.jjrounds = new int[4];
        this.jjstateSet = new int[8];
        this.curLexState = 0;
        this.defaultLexState = 0;
        this.input_stream = input_stream;
    }
    
    public DateTimeParserTokenManager(final SimpleCharStream simpleCharStream, final int n) {
        this(simpleCharStream);
        this.SwitchTo(n);
    }
    
    private final void ReInitRounds() {
        this.jjround = -2147483647;
        int n = 4;
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
            this.jjstateSet[this.jjnewStateCnt++] = DateTimeParserTokenManager.jjnextStates[n];
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
        this.jjCheckNAdd(DateTimeParserTokenManager.jjnextStates[n]);
        this.jjCheckNAdd(DateTimeParserTokenManager.jjnextStates[n + 1]);
    }
    
    private final void jjCheckNAddStates(int n, final int n2) {
        while (true) {
            this.jjCheckNAdd(DateTimeParserTokenManager.jjnextStates[n]);
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
        this.jjnewStateCnt = 4;
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
                    jjnewStateCnt = 36;
                    final int n6 = 46;
                    if (n5 != 0) {
                        if (n5 != 2) {
                            if (n5 != 3) {
                                jjnewStateCnt = n;
                            }
                            else if ((n3 & 0x3FF000000000000L) == 0x0L) {
                                jjnewStateCnt = n;
                            }
                            else {
                                this.jjCheckNAdd(3);
                                jjnewStateCnt = 46;
                            }
                        }
                        else if ((n3 & 0x100000200L) == 0x0L) {
                            jjnewStateCnt = n;
                        }
                        else {
                            this.jjCheckNAdd(2);
                            jjnewStateCnt = 36;
                        }
                    }
                    else if ((n3 & 0x3FF000000000000L) != 0x0L) {
                        if (n > 46) {
                            jjnewStateCnt = n6;
                        }
                        else {
                            jjnewStateCnt = n;
                        }
                        this.jjCheckNAdd(3);
                    }
                    else if ((n3 & 0x100000200L) != 0x0L) {
                        if (n <= 36) {
                            jjnewStateCnt = n;
                        }
                        this.jjCheckNAdd(2);
                    }
                    else {
                        jjnewStateCnt = n;
                        if ((n3 & 0x280000000000L) != 0x0L && (jjnewStateCnt = n) > 24) {
                            jjnewStateCnt = 24;
                        }
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
                    int n8 = jjnewStateCnt;
                    int i;
                    do {
                        final int[] jjstateSet2 = this.jjstateSet;
                        i = n8 - 1;
                        if (jjstateSet2[i] != 0) {
                            jjnewStateCnt = n;
                        }
                        else {
                            jjnewStateCnt = n;
                            if ((1L << (curChar & '?') & 0x7FFFBFE07FFFBFEL) != 0x0L) {
                                jjnewStateCnt = 35;
                            }
                        }
                        n8 = i;
                        n = jjnewStateCnt;
                    } while (i != jjnewStateCnt2);
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
            jjnewStateCnt2 = 4 - jjnewStateCnt2;
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
                        else if ((jjnewStateCnt = n) > 39) {
                            jjnewStateCnt = 39;
                        }
                    }
                    else if ((jjnewStateCnt = n) > 41) {
                        jjnewStateCnt = 41;
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
                            else if ((jjnewStateCnt = n) > 41) {
                                jjnewStateCnt = 41;
                            }
                        }
                        else if ((jjnewStateCnt = n) > 39) {
                            jjnewStateCnt = 39;
                        }
                    }
                    else {
                        int n5;
                        if ((n5 = n) > 41) {
                            n5 = 41;
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
                            if ((DateTimeParserTokenManager.jjbitVec0[n6] & n7) != 0x0L && (jjnewStateCnt = n) > 39) {
                                jjnewStateCnt = 39;
                            }
                        }
                    }
                    else {
                        jjnewStateCnt = n;
                        if ((DateTimeParserTokenManager.jjbitVec0[n6] & n7) != 0x0L && (jjnewStateCnt = n) > 41) {
                            jjnewStateCnt = 41;
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
                        else if ((jjnewStateCnt = n) > 42) {
                            jjnewStateCnt = 42;
                        }
                    }
                    else if ((jjnewStateCnt = n) > 45) {
                        jjnewStateCnt = 45;
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
                            else if ((jjnewStateCnt = n) > 45) {
                                jjnewStateCnt = 45;
                            }
                        }
                        else if ((jjnewStateCnt = n) > 42) {
                            jjnewStateCnt = 42;
                        }
                    }
                    else {
                        int n5;
                        if ((n5 = n) > 45) {
                            n5 = 45;
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
                            if ((DateTimeParserTokenManager.jjbitVec0[n6] & n7) != 0x0L && (jjnewStateCnt = n) > 42) {
                                jjnewStateCnt = 42;
                            }
                        }
                    }
                    else {
                        jjnewStateCnt = n;
                        if ((DateTimeParserTokenManager.jjbitVec0[n6] & n7) != 0x0L && (jjnewStateCnt = n) > 45) {
                            jjnewStateCnt = 45;
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
    
    private final int jjMoveStringLiteralDfa0_0() {
        final char curChar = this.curChar;
        if (curChar == '\n') {
            return this.jjStopAtPos(0, 2);
        }
        if (curChar == '\r') {
            return this.jjStopAtPos(0, 1);
        }
        if (curChar == '(') {
            return this.jjStopAtPos(0, 37);
        }
        if (curChar == ',') {
            return this.jjStopAtPos(0, 3);
        }
        if (curChar == ':') {
            return this.jjStopAtPos(0, 23);
        }
        if (curChar == 'A') {
            return this.jjMoveStringLiteralDfa1_0(278528L);
        }
        if (curChar == 'J') {
            return this.jjMoveStringLiteralDfa1_0(198656L);
        }
        if (curChar == 'W') {
            return this.jjMoveStringLiteralDfa1_0(64L);
        }
        switch (curChar) {
            default: {
                switch (curChar) {
                    default: {
                        switch (curChar) {
                            default: {
                                return this.jjMoveNfa_0(0, 0);
                            }
                            case 85: {
                                return this.jjMoveStringLiteralDfa1_0(33554432L);
                            }
                            case 84: {
                                return this.jjMoveStringLiteralDfa1_0(160L);
                            }
                            case 83: {
                                return this.jjMoveStringLiteralDfa1_0(525824L);
                            }
                        }
                        break;
                    }
                    case 80: {
                        return this.jjMoveStringLiteralDfa1_0(25769803776L);
                    }
                    case 79: {
                        return this.jjMoveStringLiteralDfa1_0(1048576L);
                    }
                    case 78: {
                        return this.jjMoveStringLiteralDfa1_0(2097152L);
                    }
                    case 77: {
                        return this.jjMoveStringLiteralDfa1_0(6442491920L);
                    }
                }
                break;
            }
            case 71: {
                return this.jjMoveStringLiteralDfa1_0(67108864L);
            }
            case 70: {
                return this.jjMoveStringLiteralDfa1_0(4352L);
            }
            case 69: {
                return this.jjMoveStringLiteralDfa1_0(402653184L);
            }
            case 68: {
                return this.jjMoveStringLiteralDfa1_0(4194304L);
            }
            case 67: {
                return this.jjMoveStringLiteralDfa1_0(1610612736L);
            }
        }
    }
    
    private final int jjMoveStringLiteralDfa0_1() {
        final char curChar = this.curChar;
        if (curChar == '(') {
            return this.jjStopAtPos(0, 40);
        }
        if (curChar != ')') {
            return this.jjMoveNfa_1(0, 0);
        }
        return this.jjStopAtPos(0, 38);
    }
    
    private final int jjMoveStringLiteralDfa0_2() {
        final char curChar = this.curChar;
        int n;
        if (curChar != '(') {
            if (curChar != ')') {
                return this.jjMoveNfa_2(0, 0);
            }
            n = 44;
        }
        else {
            n = 43;
        }
        return this.jjStopAtPos(0, n);
    }
    
    private final int jjMoveStringLiteralDfa1_0(final long n) {
        try {
            final char char1 = this.input_stream.readChar();
            this.curChar = char1;
            if (char1 == 'D') {
                return this.jjMoveStringLiteralDfa2_0(n, 22817013760L);
            }
            if (char1 == 'M') {
                return this.jjMoveStringLiteralDfa2_0(n, 67108864L);
            }
            if (char1 == 'a') {
                return this.jjMoveStringLiteralDfa2_0(n, 43520L);
            }
            if (char1 == 'c') {
                return this.jjMoveStringLiteralDfa2_0(n, 1048576L);
            }
            if (char1 == 'e') {
                return this.jjMoveStringLiteralDfa2_0(n, 4722752L);
            }
            if (char1 == 'h') {
                return this.jjMoveStringLiteralDfa2_0(n, 128L);
            }
            if (char1 == 'r') {
                return this.jjMoveStringLiteralDfa2_0(n, 256L);
            }
            if (char1 == 'u') {
                return this.jjMoveStringLiteralDfa2_0(n, 459808L);
            }
            if (char1 != 'S') {
                if (char1 != 'T') {
                    if (char1 == 'o') {
                        return this.jjMoveStringLiteralDfa2_0(n, 2097168L);
                    }
                    if (char1 == 'p') {
                        return this.jjMoveStringLiteralDfa2_0(n, 16384L);
                    }
                }
                else if ((n & 0x2000000L) != 0x0L) {
                    return this.jjStopAtPos(1, 25);
                }
                return this.jjStartNfa_0(0, n);
            }
            return this.jjMoveStringLiteralDfa2_0(n, 11408506880L);
        }
        catch (IOException ex) {
            this.jjStopStringLiteralDfa_0(0, n);
            return 1;
        }
    }
    
    private final int jjMoveStringLiteralDfa2_0(final long n, long n2) {
        n2 &= n;
        if (n2 == 0L) {
            return this.jjStartNfa_0(0, n);
        }
        try {
            final char char1 = this.input_stream.readChar();
            Label_0653: {
                if ((this.curChar = char1) != 'T') {
                    if (char1 != 'g') {
                        if (char1 != 'i') {
                            if (char1 != 'l') {
                                if (char1 != 'n') {
                                    if (char1 != 'p') {
                                        if (char1 != 'r') {
                                            if (char1 != 'y') {
                                                switch (char1) {
                                                    default: {
                                                        switch (char1) {
                                                            default: {
                                                                break Label_0653;
                                                            }
                                                            case 'v': {
                                                                if ((n2 & 0x200000L) != 0x0L) {
                                                                    return this.jjStopAtPos(2, 21);
                                                                }
                                                                break Label_0653;
                                                            }
                                                            case 'u': {
                                                                if ((n2 & 0x80L) != 0x0L) {
                                                                    return this.jjStopAtPos(2, 7);
                                                                }
                                                                break Label_0653;
                                                            }
                                                            case 't': {
                                                                if ((n2 & 0x200L) != 0x0L) {
                                                                    return this.jjStopAtPos(2, 9);
                                                                }
                                                                if ((n2 & 0x100000L) != 0x0L) {
                                                                    return this.jjStopAtPos(2, 20);
                                                                }
                                                                break Label_0653;
                                                            }
                                                        }
                                                        break;
                                                    }
                                                    case 'e': {
                                                        if ((n2 & 0x20L) != 0x0L) {
                                                            return this.jjStopAtPos(2, 5);
                                                        }
                                                        break;
                                                    }
                                                    case 'd': {
                                                        if ((n2 & 0x40L) != 0x0L) {
                                                            return this.jjStopAtPos(2, 6);
                                                        }
                                                        break;
                                                    }
                                                    case 'c': {
                                                        if ((n2 & 0x400000L) != 0x0L) {
                                                            return this.jjStopAtPos(2, 22);
                                                        }
                                                        break;
                                                    }
                                                    case 'b': {
                                                        if ((n2 & 0x1000L) != 0x0L) {
                                                            return this.jjStopAtPos(2, 12);
                                                        }
                                                        break;
                                                    }
                                                }
                                            }
                                            else if ((n2 & 0x8000L) != 0x0L) {
                                                return this.jjStopAtPos(2, 15);
                                            }
                                        }
                                        else {
                                            if ((n2 & 0x2000L) != 0x0L) {
                                                return this.jjStopAtPos(2, 13);
                                            }
                                            if ((n2 & 0x4000L) != 0x0L) {
                                                return this.jjStopAtPos(2, 14);
                                            }
                                        }
                                    }
                                    else if ((n2 & 0x80000L) != 0x0L) {
                                        return this.jjStopAtPos(2, 19);
                                    }
                                }
                                else {
                                    if ((n2 & 0x10L) != 0x0L) {
                                        return this.jjStopAtPos(2, 4);
                                    }
                                    if ((n2 & 0x400L) != 0x0L) {
                                        return this.jjStopAtPos(2, 10);
                                    }
                                    if ((n2 & 0x800L) != 0x0L) {
                                        return this.jjStopAtPos(2, 11);
                                    }
                                    if ((n2 & 0x10000L) != 0x0L) {
                                        return this.jjStopAtPos(2, 16);
                                    }
                                }
                            }
                            else if ((n2 & 0x20000L) != 0x0L) {
                                return this.jjStopAtPos(2, 17);
                            }
                        }
                        else if ((n2 & 0x100L) != 0x0L) {
                            return this.jjStopAtPos(2, 8);
                        }
                    }
                    else if ((n2 & 0x40000L) != 0x0L) {
                        return this.jjStopAtPos(2, 18);
                    }
                }
                else {
                    if ((n2 & 0x4000000L) != 0x0L) {
                        return this.jjStopAtPos(2, 26);
                    }
                    if ((n2 & 0x8000000L) != 0x0L) {
                        return this.jjStopAtPos(2, 27);
                    }
                    if ((n2 & 0x10000000L) != 0x0L) {
                        return this.jjStopAtPos(2, 28);
                    }
                    if ((n2 & 0x20000000L) != 0x0L) {
                        return this.jjStopAtPos(2, 29);
                    }
                    if ((n2 & 0x40000000L) != 0x0L) {
                        return this.jjStopAtPos(2, 30);
                    }
                    if ((n2 & 0x80000000L) != 0x0L) {
                        return this.jjStopAtPos(2, 31);
                    }
                    if ((n2 & 0x100000000L) != 0x0L) {
                        return this.jjStopAtPos(2, 32);
                    }
                    if ((n2 & 0x200000000L) != 0x0L) {
                        return this.jjStopAtPos(2, 33);
                    }
                    if ((n2 & 0x400000000L) != 0x0L) {
                        return this.jjStopAtPos(2, 34);
                    }
                }
            }
            return this.jjStartNfa_0(1, n2);
        }
        catch (IOException ex) {
            this.jjStopStringLiteralDfa_0(1, n2);
            return 2;
        }
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
    
    private final int jjStartNfa_0(final int n, final long n2) {
        return this.jjMoveNfa_0(this.jjStopStringLiteralDfa_0(n, n2), n + 1);
    }
    
    private final int jjStartNfa_1(final int n, final long n2) {
        return this.jjMoveNfa_1(this.jjStopStringLiteralDfa_1(n, n2), n + 1);
    }
    
    private final int jjStartNfa_2(final int n, final long n2) {
        return this.jjMoveNfa_2(this.jjStopStringLiteralDfa_2(n, n2), n + 1);
    }
    
    private final int jjStopAtPos(final int jjmatchedPos, final int jjmatchedKind) {
        this.jjmatchedKind = jjmatchedKind;
        return (this.jjmatchedPos = jjmatchedPos) + 1;
    }
    
    private final int jjStopStringLiteralDfa_0(final int n, final long n2) {
        if (n == 0) {
            if ((n2 & 0x7FE7CF7F0L) != 0x0L) {
                this.jjmatchedKind = 35;
            }
            return -1;
        }
        if (n != 1) {
            return -1;
        }
        if ((n2 & 0x7FE7CF7F0L) != 0x0L && this.jjmatchedPos == 0) {
            this.jjmatchedKind = 35;
            this.jjmatchedPos = 0;
        }
        return -1;
    }
    
    private final int jjStopStringLiteralDfa_1(final int n, final long n2) {
        return -1;
    }
    
    private final int jjStopStringLiteralDfa_2(final int n, final long n2) {
        return -1;
    }
    
    void MoreLexicalActions() {
        final int jjimageLen = this.jjimageLen;
        final int lengthOfMatch = this.jjmatchedPos + 1;
        this.lengthOfMatch = lengthOfMatch;
        this.jjimageLen = jjimageLen + lengthOfMatch;
        switch (this.jjmatchedKind) {
            default: {}
            case 44: {
                if (this.image == null) {
                    this.image = new StringBuffer();
                }
                this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
                this.jjimageLen = 0;
                if (--DateTimeParserTokenManager.commentNest == 0) {
                    this.SwitchTo(1);
                    return;
                }
                break;
            }
            case 43: {
                if (this.image == null) {
                    this.image = new StringBuffer();
                }
                this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
                this.jjimageLen = 0;
                ++DateTimeParserTokenManager.commentNest;
            }
            case 42: {
                if (this.image == null) {
                    this.image = new StringBuffer();
                }
                this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
                this.jjimageLen = 0;
                final StringBuffer image = this.image;
                image.deleteCharAt(image.length() - 2);
            }
            case 40: {
                if (this.image == null) {
                    this.image = new StringBuffer();
                }
                this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
                this.jjimageLen = 0;
                DateTimeParserTokenManager.commentNest = 1;
            }
            case 39: {
                if (this.image == null) {
                    this.image = new StringBuffer();
                }
                this.image.append(this.input_stream.GetSuffix(this.jjimageLen));
                this.jjimageLen = 0;
                final StringBuffer image2 = this.image;
                image2.deleteCharAt(image2.length() - 2);
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
        if (curLexState < 3 && curLexState >= 0) {
            this.curLexState = curLexState;
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Error: Ignoring invalid lexical state : ");
        sb.append(curLexState);
        sb.append(". State unchanged.");
        throw new TokenMgrError(sb.toString(), 2);
    }
    
    public Token getNextToken() {
        final String s = "";
        final Token token = null;
        Object specialToken = null;
        int n = 0;
        try {
            int n3 = 0;
        Label_0382:
            while (true) {
                this.curChar = this.input_stream.BeginToken();
                this.image = null;
                this.jjimageLen = 0;
                int n2 = n;
                while (true) {
                    final int curLexState = this.curLexState;
                    if (curLexState != 0) {
                        if (curLexState != 1) {
                            if (curLexState == 2) {
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
                        break Label_0382;
                    }
                    final int jjmatchedPos = this.jjmatchedPos;
                    if (jjmatchedPos + 1 < n2) {
                        this.input_stream.backup(n2 - jjmatchedPos - 1);
                    }
                    final long[] jjtoToken = DateTimeParserTokenManager.jjtoToken;
                    final int jjmatchedKind = this.jjmatchedKind;
                    final int n4 = jjmatchedKind >> 6;
                    final long n5 = jjtoToken[n4];
                    final long n6 = 1L << (jjmatchedKind & 0x3F);
                    if ((n5 & n6) != 0x0L) {
                        final Token jjFillToken = this.jjFillToken();
                        jjFillToken.specialToken = (Token)specialToken;
                        specialToken = DateTimeParserTokenManager.jjnewLexState;
                        final int jjmatchedKind2 = this.jjmatchedKind;
                        if (specialToken[jjmatchedKind2] != -1) {
                            this.curLexState = specialToken[jjmatchedKind2];
                        }
                        return jjFillToken;
                    }
                    if ((DateTimeParserTokenManager.jjtoSkip[n4] & n6) == 0x0L) {
                        this.MoreLexicalActions();
                        final int[] jjnewLexState = DateTimeParserTokenManager.jjnewLexState;
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
                        break Label_0382;
                    }
                    Token jjFillToken2 = (Token)specialToken;
                    if ((n6 & DateTimeParserTokenManager.jjtoSpecial[n4]) != 0x0L) {
                        jjFillToken2 = this.jjFillToken();
                        if (specialToken != null) {
                            jjFillToken2.specialToken = (Token)specialToken;
                            ((Token)specialToken).next = jjFillToken2;
                        }
                    }
                    final int[] jjnewLexState2 = DateTimeParserTokenManager.jjnewLexState;
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
        if ((getImage = DateTimeParserTokenManager.jjstrLiteralImages[this.jjmatchedKind]) == null) {
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
