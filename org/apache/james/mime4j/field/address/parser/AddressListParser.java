package org.apache.james.mime4j.field.address.parser;

import java.io.*;
import java.util.*;

public class AddressListParser implements AddressListParserTreeConstants, AddressListParserConstants
{
    private static int[] jj_la1_0;
    private static int[] jj_la1_1;
    private final JJCalls[] jj_2_rtns;
    private int jj_endpos;
    private Vector<int[]> jj_expentries;
    private int[] jj_expentry;
    private int jj_gc;
    private int jj_gen;
    SimpleCharStream jj_input_stream;
    private int jj_kind;
    private int jj_la;
    private final int[] jj_la1;
    private Token jj_lastpos;
    private int[] jj_lasttokens;
    private final LookaheadSuccess jj_ls;
    public Token jj_nt;
    private int jj_ntk;
    private boolean jj_rescan;
    private Token jj_scanpos;
    private boolean jj_semLA;
    protected JJTAddressListParserState jjtree;
    public boolean lookingAhead;
    public Token token;
    public AddressListParserTokenManager token_source;
    
    static {
        jj_la1_0();
        jj_la1_1();
    }
    
    public AddressListParser(final InputStream inputStream) {
        this(inputStream, null);
    }
    
    public AddressListParser(final InputStream inputStream, final String s) {
        this.jjtree = new JJTAddressListParserState();
        final int n = 0;
        this.lookingAhead = false;
        this.jj_la1 = new int[22];
        this.jj_2_rtns = new JJCalls[2];
        this.jj_rescan = false;
        this.jj_gc = 0;
        this.jj_ls = new LookaheadSuccess();
        this.jj_expentries = new Vector<int[]>();
        this.jj_kind = -1;
        this.jj_lasttokens = new int[100];
        try {
            this.jj_input_stream = new SimpleCharStream(inputStream, s, 1, 1);
            this.token_source = new AddressListParserTokenManager(this.jj_input_stream);
            this.token = new Token();
            this.jj_ntk = -1;
            this.jj_gen = 0;
            int n2 = 0;
            int n3;
            while (true) {
                n3 = n;
                if (n2 >= 22) {
                    break;
                }
                this.jj_la1[n2] = -1;
                ++n2;
            }
            while (true) {
                final JJCalls[] jj_2_rtns = this.jj_2_rtns;
                if (n3 >= jj_2_rtns.length) {
                    break;
                }
                jj_2_rtns[n3] = new JJCalls();
                ++n3;
            }
        }
        catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public AddressListParser(final Reader reader) {
        this.jjtree = new JJTAddressListParserState();
        final int n = 0;
        this.lookingAhead = false;
        this.jj_la1 = new int[22];
        this.jj_2_rtns = new JJCalls[2];
        this.jj_rescan = false;
        this.jj_gc = 0;
        this.jj_ls = new LookaheadSuccess();
        this.jj_expentries = new Vector<int[]>();
        this.jj_kind = -1;
        this.jj_lasttokens = new int[100];
        this.jj_input_stream = new SimpleCharStream(reader, 1, 1);
        this.token_source = new AddressListParserTokenManager(this.jj_input_stream);
        this.token = new Token();
        this.jj_ntk = -1;
        this.jj_gen = 0;
        int n2 = 0;
        int n3;
        while (true) {
            n3 = n;
            if (n2 >= 22) {
                break;
            }
            this.jj_la1[n2] = -1;
            ++n2;
        }
        while (true) {
            final JJCalls[] jj_2_rtns = this.jj_2_rtns;
            if (n3 >= jj_2_rtns.length) {
                break;
            }
            jj_2_rtns[n3] = new JJCalls();
            ++n3;
        }
    }
    
    public AddressListParser(final AddressListParserTokenManager token_source) {
        this.jjtree = new JJTAddressListParserState();
        final int n = 0;
        this.lookingAhead = false;
        this.jj_la1 = new int[22];
        this.jj_2_rtns = new JJCalls[2];
        this.jj_rescan = false;
        this.jj_gc = 0;
        this.jj_ls = new LookaheadSuccess();
        this.jj_expentries = new Vector<int[]>();
        this.jj_kind = -1;
        this.jj_lasttokens = new int[100];
        this.token_source = token_source;
        this.token = new Token();
        this.jj_ntk = -1;
        this.jj_gen = 0;
        int n2 = 0;
        int n3;
        while (true) {
            n3 = n;
            if (n2 >= 22) {
                break;
            }
            this.jj_la1[n2] = -1;
            ++n2;
        }
        while (true) {
            final JJCalls[] jj_2_rtns = this.jj_2_rtns;
            if (n3 >= jj_2_rtns.length) {
                break;
            }
            jj_2_rtns[n3] = new JJCalls();
            ++n3;
        }
    }
    
    private final boolean jj_2_1(final int jj_la) {
        this.jj_la = jj_la;
        final Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            final boolean jj_3_1 = this.jj_3_1();
            this.jj_save(0, jj_la);
            return jj_3_1 ^ true;
        }
        catch (LookaheadSuccess lookaheadSuccess) {
            return true;
        }
        finally {
            this.jj_save(0, jj_la);
        }
    }
    
    private final boolean jj_2_2(final int jj_la) {
        this.jj_la = jj_la;
        final Token token = this.token;
        this.jj_scanpos = token;
        this.jj_lastpos = token;
        try {
            final boolean jj_3_2 = this.jj_3_2();
            this.jj_save(1, jj_la);
            return jj_3_2 ^ true;
        }
        catch (LookaheadSuccess lookaheadSuccess) {
            return true;
        }
        finally {
            this.jj_save(1, jj_la);
        }
    }
    
    private final boolean jj_3R_10() {
        final Token jj_scanpos = this.jj_scanpos;
        if (this.jj_3R_12()) {
            this.jj_scanpos = jj_scanpos;
            if (this.jj_scan_token(18)) {
                return true;
            }
        }
        return false;
    }
    
    private final boolean jj_3R_11() {
        final Token jj_scanpos = this.jj_scanpos;
        if (this.jj_scan_token(9)) {
            this.jj_scanpos = jj_scanpos;
        }
        final Token jj_scanpos2 = this.jj_scanpos;
        if (this.jj_scan_token(14)) {
            this.jj_scanpos = jj_scanpos2;
            if (this.jj_scan_token(31)) {
                return true;
            }
        }
        return false;
    }
    
    private final boolean jj_3R_12() {
        if (this.jj_scan_token(14)) {
            return true;
        }
        Token jj_scanpos;
        do {
            jj_scanpos = this.jj_scanpos;
        } while (!this.jj_3R_13());
        this.jj_scanpos = jj_scanpos;
        return false;
    }
    
    private final boolean jj_3R_13() {
        final Token jj_scanpos = this.jj_scanpos;
        if (this.jj_scan_token(9)) {
            this.jj_scanpos = jj_scanpos;
        }
        return this.jj_scan_token(14);
    }
    
    private final boolean jj_3R_8() {
        return this.jj_3R_9() || this.jj_scan_token(8) || this.jj_3R_10();
    }
    
    private final boolean jj_3R_9() {
        final Token jj_scanpos = this.jj_scanpos;
        if (this.jj_scan_token(14)) {
            this.jj_scanpos = jj_scanpos;
            if (this.jj_scan_token(31)) {
                return true;
            }
        }
        Token jj_scanpos2;
        do {
            jj_scanpos2 = this.jj_scanpos;
        } while (!this.jj_3R_11());
        this.jj_scanpos = jj_scanpos2;
        return false;
    }
    
    private final boolean jj_3_1() {
        return this.jj_3R_8();
    }
    
    private final boolean jj_3_2() {
        return this.jj_3R_8();
    }
    
    private void jj_add_error_token(final int n, final int jj_endpos) {
        if (jj_endpos >= 100) {
            return;
        }
        final int jj_endpos2 = this.jj_endpos;
        final int jj_endpos3 = jj_endpos2 + 1;
        if (jj_endpos == jj_endpos3) {
            final int[] jj_lasttokens = this.jj_lasttokens;
            this.jj_endpos = jj_endpos3;
            jj_lasttokens[jj_endpos2] = n;
            return;
        }
        if (jj_endpos2 != 0) {
            this.jj_expentry = new int[jj_endpos2];
            for (int i = 0; i < this.jj_endpos; ++i) {
                this.jj_expentry[i] = this.jj_lasttokens[i];
            }
            final Enumeration<int[]> elements = this.jj_expentries.elements();
            int n2 = 0;
            int n3;
            while (true) {
                n3 = n2;
                if (!elements.hasMoreElements()) {
                    break;
                }
                final int[] array = elements.nextElement();
                if (array.length != this.jj_expentry.length) {
                    continue;
                }
                int n4 = 0;
                while (true) {
                    final int[] jj_expentry = this.jj_expentry;
                    if (n4 >= jj_expentry.length) {
                        n3 = 1;
                        break;
                    }
                    if (array[n4] != jj_expentry[n4]) {
                        n3 = 0;
                        break;
                    }
                    ++n4;
                }
                n2 = n3;
                if (n3 != 0) {
                    break;
                }
            }
            if (n3 == 0) {
                this.jj_expentries.addElement(this.jj_expentry);
            }
            if (jj_endpos != 0) {
                this.jj_lasttokens[(this.jj_endpos = jj_endpos) - 1] = n;
            }
        }
    }
    
    private final Token jj_consume_token(int n) throws ParseException {
        final Token token = this.token;
        if (token.next != null) {
            this.token = this.token.next;
        }
        else {
            final Token token2 = this.token;
            final Token nextToken = this.token_source.getNextToken();
            token2.next = nextToken;
            this.token = nextToken;
        }
        this.jj_ntk = -1;
        if (this.token.kind == n) {
            ++this.jj_gen;
            n = this.jj_gc + 1;
            if ((this.jj_gc = n) > 100) {
                n = 0;
                this.jj_gc = 0;
                while (true) {
                    final JJCalls[] jj_2_rtns = this.jj_2_rtns;
                    if (n >= jj_2_rtns.length) {
                        break;
                    }
                    for (JJCalls next = jj_2_rtns[n]; next != null; next = next.next) {
                        if (next.gen < this.jj_gen) {
                            next.first = null;
                        }
                    }
                    ++n;
                }
            }
            return this.token;
        }
        this.token = token;
        this.jj_kind = n;
        throw this.generateParseException();
    }
    
    private static void jj_la1_0() {
        AddressListParser.jj_la1_0 = new int[] { 2, -2147467200, 8, -2147467200, 80, -2147467200, -2147467200, -2147467200, 8, -2147467200, 256, 264, 8, -2147467264, -2147467264, -2147467264, -2147466752, 512, -2147467264, 16896, 512, 278528 };
    }
    
    private static void jj_la1_1() {
        AddressListParser.jj_la1_1 = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    }
    
    private final int jj_ntk() {
        final Token next = this.token.next;
        this.jj_nt = next;
        int jj_ntk;
        if (next == null) {
            final Token token = this.token;
            final Token nextToken = this.token_source.getNextToken();
            token.next = nextToken;
            jj_ntk = nextToken.kind;
        }
        else {
            jj_ntk = next.kind;
        }
        return this.jj_ntk = jj_ntk;
    }
    
    private final void jj_rescan_token() {
        this.jj_rescan = true;
        for (int i = 0; i < 2; ++i) {
            try {
                JJCalls next = this.jj_2_rtns[i];
                do {
                    if (next.gen > this.jj_gen) {
                        this.jj_la = next.arg;
                        final Token first = next.first;
                        this.jj_scanpos = first;
                        this.jj_lastpos = first;
                        if (i != 0) {
                            if (i != 1) {
                                continue;
                            }
                            this.jj_3_2();
                        }
                        else {
                            this.jj_3_1();
                        }
                    }
                } while ((next = next.next) != null);
            }
            catch (LookaheadSuccess lookaheadSuccess) {}
        }
        this.jj_rescan = false;
    }
    
    private final void jj_save(final int n, final int arg) {
        JJCalls next = this.jj_2_rtns[n];
        JJCalls next2;
        while (true) {
            next2 = next;
            if (next.gen <= this.jj_gen) {
                break;
            }
            if (next.next == null) {
                next2 = new JJCalls();
                next.next = next2;
                break;
            }
            next = next.next;
        }
        next2.gen = this.jj_gen + arg - this.jj_la;
        next2.first = this.token;
        next2.arg = arg;
    }
    
    private final boolean jj_scan_token(final int n) {
        final Token jj_scanpos = this.jj_scanpos;
        if (jj_scanpos == this.jj_lastpos) {
            --this.jj_la;
            if (jj_scanpos.next == null) {
                final Token jj_scanpos2 = this.jj_scanpos;
                final Token nextToken = this.token_source.getNextToken();
                jj_scanpos2.next = nextToken;
                this.jj_scanpos = nextToken;
                this.jj_lastpos = nextToken;
            }
            else {
                final Token next = this.jj_scanpos.next;
                this.jj_scanpos = next;
                this.jj_lastpos = next;
            }
        }
        else {
            this.jj_scanpos = jj_scanpos.next;
        }
        if (this.jj_rescan) {
            Token token = this.token;
            int n2 = 0;
            while (token != null && token != this.jj_scanpos) {
                ++n2;
                token = token.next;
            }
            if (token != null) {
                this.jj_add_error_token(n, n2);
            }
        }
        if (this.jj_scanpos.kind != n) {
            return true;
        }
        if (this.jj_la != 0) {
            return false;
        }
        if (this.jj_scanpos != this.jj_lastpos) {
            return false;
        }
        throw this.jj_ls;
    }
    
    public static void main(final String[] array) throws ParseException {
        try {
            while (true) {
                final AddressListParser addressListParser = new AddressListParser(System.in);
                addressListParser.parseLine();
                ((SimpleNode)addressListParser.jjtree.rootNode()).dump("> ");
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void ReInit(final InputStream inputStream) {
        this.ReInit(inputStream, null);
    }
    
    public void ReInit(final InputStream inputStream, final String s) {
        try {
            this.jj_input_stream.ReInit(inputStream, s, 1, 1);
            this.token_source.ReInit(this.jj_input_stream);
            this.token = new Token();
            this.jj_ntk = -1;
            this.jjtree.reset();
            final int n = 0;
            this.jj_gen = 0;
            int n2 = 0;
            int n3;
            while (true) {
                n3 = n;
                if (n2 >= 22) {
                    break;
                }
                this.jj_la1[n2] = -1;
                ++n2;
            }
            while (true) {
                final JJCalls[] jj_2_rtns = this.jj_2_rtns;
                if (n3 >= jj_2_rtns.length) {
                    break;
                }
                jj_2_rtns[n3] = new JJCalls();
                ++n3;
            }
        }
        catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public void ReInit(final Reader reader) {
        this.jj_input_stream.ReInit(reader, 1, 1);
        this.token_source.ReInit(this.jj_input_stream);
        this.token = new Token();
        this.jj_ntk = -1;
        this.jjtree.reset();
        final int n = 0;
        this.jj_gen = 0;
        int n2 = 0;
        int n3;
        while (true) {
            n3 = n;
            if (n2 >= 22) {
                break;
            }
            this.jj_la1[n2] = -1;
            ++n2;
        }
        while (true) {
            final JJCalls[] jj_2_rtns = this.jj_2_rtns;
            if (n3 >= jj_2_rtns.length) {
                break;
            }
            jj_2_rtns[n3] = new JJCalls();
            ++n3;
        }
    }
    
    public void ReInit(final AddressListParserTokenManager token_source) {
        this.token_source = token_source;
        this.token = new Token();
        this.jj_ntk = -1;
        this.jjtree.reset();
        final int n = 0;
        this.jj_gen = 0;
        int n2 = 0;
        int n3;
        while (true) {
            n3 = n;
            if (n2 >= 22) {
                break;
            }
            this.jj_la1[n2] = -1;
            ++n2;
        }
        while (true) {
            final JJCalls[] jj_2_rtns = this.jj_2_rtns;
            if (n3 >= jj_2_rtns.length) {
                break;
            }
            jj_2_rtns[n3] = new JJCalls();
            ++n3;
        }
    }
    
    public final void addr_spec() throws ParseException {
        final ASTaddr_spec asTaddr_spec = new ASTaddr_spec(9);
        this.jjtree.openNodeScope(asTaddr_spec);
        this.jjtreeOpenNodeScope(asTaddr_spec);
        try {
            this.local_part();
            this.jj_consume_token(8);
            this.domain();
            this.jjtree.closeNodeScope(asTaddr_spec, true);
            this.jjtreeCloseNodeScope(asTaddr_spec);
        }
        finally {
            boolean b;
            try {
                this.jjtree.clearNodeScope(asTaddr_spec);
                try {
                    final Throwable t;
                    if (t instanceof RuntimeException) {
                        throw (RuntimeException)t;
                    }
                    if (t instanceof ParseException) {
                        throw (ParseException)t;
                    }
                    throw (Error)t;
                }
                finally {}
            }
            finally {
                b = true;
            }
            if (b) {
                this.jjtree.closeNodeScope(asTaddr_spec, true);
                this.jjtreeCloseNodeScope(asTaddr_spec);
            }
        }
    }
    
    public final void address() throws ParseException {
        ASTaddress asTaddress;
        int jj_ntk;
        int jj_ntk2;
        final Throwable t;
        boolean b = false;
        Label_0087_Outer:Label_0122_Outer:
        while (true) {
            asTaddress = new ASTaddress(2);
            this.jjtree.openNodeScope(asTaddress);
            this.jjtreeOpenNodeScope(asTaddress);
            Label_0146_Outer:Label_0115_Outer:
            while (true) {
            Label_0115:
                while (true) {
                    while (true) {
                        Label_0253: {
                            while (true) {
                                while (true) {
                                    Label_0232: {
                                        try {
                                            // iftrue(Label_0107:, this.jj_ntk != -1)
                                            while (true) {
                                                if (this.jj_2_1(Integer.MAX_VALUE)) {
                                                    this.addr_spec();
                                                    this.jjtree.closeNodeScope(asTaddress, true);
                                                    this.jjtreeCloseNodeScope(asTaddress);
                                                    return;
                                                }
                                                if (this.jj_ntk == -1) {
                                                    this.jj_ntk();
                                                    break Label_0232;
                                                }
                                                jj_ntk = this.jj_ntk;
                                                break Label_0232;
                                                Label_0107: {
                                                    jj_ntk2 = this.jj_ntk;
                                                }
                                                break Label_0253;
                                                this.phrase();
                                                Block_10: {
                                                    break Block_10;
                                                    this.jj_la1[4] = this.jj_gen;
                                                    this.jj_consume_token(-1);
                                                    throw new ParseException();
                                                    this.group_body();
                                                    continue Label_0087_Outer;
                                                    this.jj_la1[5] = this.jj_gen;
                                                    this.jj_consume_token(-1);
                                                    throw new ParseException();
                                                }
                                                this.jj_ntk();
                                                break Label_0253;
                                                this.angle_addr();
                                                continue Label_0087_Outer;
                                            }
                                        }
                                        finally {
                                            try {
                                                this.jjtree.clearNodeScope(asTaddress);
                                                try {
                                                    if (t instanceof RuntimeException) {
                                                        throw (RuntimeException)t;
                                                    }
                                                    if (t instanceof ParseException) {
                                                        throw (ParseException)t;
                                                    }
                                                    throw (Error)t;
                                                }
                                                finally {}
                                            }
                                            finally {
                                                b = true;
                                            }
                                            if (b) {
                                                this.jjtree.closeNodeScope(asTaddress, true);
                                                this.jjtreeCloseNodeScope(asTaddress);
                                            }
                                        }
                                    }
                                    if ((b ? 1 : 0) == 6) {
                                        continue Label_0115;
                                    }
                                    if ((b ? 1 : 0) != 14 && (b ? 1 : 0) != 31) {
                                        continue Label_0115_Outer;
                                    }
                                    break;
                                }
                                continue Label_0122_Outer;
                            }
                        }
                        if ((b ? 1 : 0) == 4) {
                            continue Label_0115_Outer;
                        }
                        break;
                    }
                    if ((b ? 1 : 0) == 6) {
                        continue Label_0115;
                    }
                    break;
                }
                continue Label_0146_Outer;
            }
        }
    }
    
    public final void address_list() throws ParseException {
        final ASTaddress_list asTaddress_list = new ASTaddress_list(1);
        this.jjtree.openNodeScope(asTaddress_list);
        this.jjtreeOpenNodeScope(asTaddress_list);
        try {
            int n;
            if (this.jj_ntk == -1) {
                n = this.jj_ntk();
            }
            else {
                n = this.jj_ntk;
            }
            while (true) {
                Label_0074: {
                    if (n == 6 || n == 14 || n == 31) {
                        break Label_0074;
                    }
                    this.jj_la1[1] = this.jj_gen;
                    while (true) {
                        int n2;
                        if (this.jj_ntk == -1) {
                            n2 = this.jj_ntk();
                        }
                        else {
                            n2 = this.jj_ntk;
                        }
                        if (n2 != 3) {
                            break;
                        }
                        this.jj_consume_token(3);
                        int n3;
                        if (this.jj_ntk == -1) {
                            n3 = this.jj_ntk();
                        }
                        else {
                            n3 = this.jj_ntk;
                        }
                        if (n3 == 6 || n3 == 14 || n3 == 31) {
                            break Label_0074;
                        }
                        this.jj_la1[3] = this.jj_gen;
                    }
                    this.jj_la1[2] = this.jj_gen;
                    this.jjtree.closeNodeScope(asTaddress_list, true);
                    this.jjtreeCloseNodeScope(asTaddress_list);
                    return;
                }
                this.address();
                continue;
            }
        }
        finally {
            boolean b;
            try {
                this.jjtree.clearNodeScope(asTaddress_list);
                try {
                    final Throwable t;
                    if (t instanceof RuntimeException) {
                        throw (RuntimeException)t;
                    }
                    if (t instanceof ParseException) {
                        throw (ParseException)t;
                    }
                    throw (Error)t;
                }
                finally {}
            }
            finally {
                b = true;
            }
            if (b) {
                this.jjtree.closeNodeScope(asTaddress_list, true);
                this.jjtreeCloseNodeScope(asTaddress_list);
            }
        }
    }
    
    public final void angle_addr() throws ParseException {
        final ASTangle_addr asTangle_addr = new ASTangle_addr(6);
        this.jjtree.openNodeScope(asTangle_addr);
        this.jjtreeOpenNodeScope(asTangle_addr);
        try {
            this.jj_consume_token(6);
            int n;
            if (this.jj_ntk == -1) {
                n = this.jj_ntk();
            }
            else {
                n = this.jj_ntk;
            }
            if (n != 8) {
                this.jj_la1[10] = this.jj_gen;
            }
            else {
                this.route();
            }
            this.addr_spec();
            this.jj_consume_token(7);
            this.jjtree.closeNodeScope(asTangle_addr, true);
            this.jjtreeCloseNodeScope(asTangle_addr);
        }
        finally {
            boolean b;
            try {
                this.jjtree.clearNodeScope(asTangle_addr);
                try {
                    final Throwable t;
                    if (t instanceof RuntimeException) {
                        throw (RuntimeException)t;
                    }
                    if (t instanceof ParseException) {
                        throw (ParseException)t;
                    }
                    throw (Error)t;
                }
                finally {}
            }
            finally {
                b = true;
            }
            if (b) {
                this.jjtree.closeNodeScope(asTangle_addr, true);
                this.jjtreeCloseNodeScope(asTangle_addr);
            }
        }
    }
    
    public final void disable_tracing() {
    }
    
    public final void domain() throws ParseException {
        final ASTdomain asTdomain = new ASTdomain(11);
        this.jjtree.openNodeScope(asTdomain);
        this.jjtreeOpenNodeScope(asTdomain);
        try {
            int n;
            if (this.jj_ntk == -1) {
                n = this.jj_ntk();
            }
            else {
                n = this.jj_ntk;
            }
            if (n != 14) {
                if (n != 18) {
                    this.jj_la1[21] = this.jj_gen;
                    this.jj_consume_token(-1);
                    throw new ParseException();
                }
                this.jj_consume_token(18);
            }
            else {
                while (true) {
                    Token token = this.jj_consume_token(14);
                    int n2;
                    if (this.jj_ntk == -1) {
                        n2 = this.jj_ntk();
                    }
                    else {
                        n2 = this.jj_ntk;
                    }
                    if (n2 != 9 && n2 != 14) {
                        this.jj_la1[19] = this.jj_gen;
                        break;
                    }
                    int n3;
                    if (this.jj_ntk == -1) {
                        n3 = this.jj_ntk();
                    }
                    else {
                        n3 = this.jj_ntk;
                    }
                    if (n3 != 9) {
                        this.jj_la1[20] = this.jj_gen;
                    }
                    else {
                        token = this.jj_consume_token(9);
                    }
                    if (token.image.charAt(token.image.length() - 1) == '.') {
                        continue;
                    }
                    throw new ParseException("Atoms in domain names must be separated by '.'");
                }
            }
        }
        finally {
            this.jjtree.closeNodeScope(asTdomain, true);
            this.jjtreeCloseNodeScope(asTdomain);
        }
    }
    
    public final void enable_tracing() {
    }
    
    public ParseException generateParseException() {
        this.jj_expentries.removeAllElements();
        final boolean[] array = new boolean[34];
        final int n = 0;
        for (int i = 0; i < 34; ++i) {
            array[i] = false;
        }
        final int jj_kind = this.jj_kind;
        if (jj_kind >= 0) {
            array[jj_kind] = true;
            this.jj_kind = -1;
        }
        for (int j = 0; j < 22; ++j) {
            if (this.jj_la1[j] == this.jj_gen) {
                for (int k = 0; k < 32; ++k) {
                    final int n2 = AddressListParser.jj_la1_0[j];
                    final int n3 = 1 << k;
                    if ((n2 & n3) != 0x0) {
                        array[k] = true;
                    }
                    if ((AddressListParser.jj_la1_1[j] & n3) != 0x0) {
                        array[k + 32] = true;
                    }
                }
            }
        }
        for (int l = 0; l < 34; ++l) {
            if (array[l]) {
                final int[] jj_expentry = { 0 };
                (this.jj_expentry = jj_expentry)[0] = l;
                this.jj_expentries.addElement(jj_expentry);
            }
        }
        this.jj_endpos = 0;
        this.jj_rescan_token();
        this.jj_add_error_token(0, 0);
        final int[][] array2 = new int[this.jj_expentries.size()][];
        for (int n4 = n; n4 < this.jj_expentries.size(); ++n4) {
            array2[n4] = this.jj_expentries.elementAt(n4);
        }
        return new ParseException(this.token, array2, AddressListParser.tokenImage);
    }
    
    public final Token getNextToken() {
        if (this.token.next != null) {
            this.token = this.token.next;
        }
        else {
            final Token token = this.token;
            final Token nextToken = this.token_source.getNextToken();
            token.next = nextToken;
            this.token = nextToken;
        }
        this.jj_ntk = -1;
        ++this.jj_gen;
        return this.token;
    }
    
    public final Token getToken(final int n) {
        Token token;
        if (this.lookingAhead) {
            token = this.jj_scanpos;
        }
        else {
            token = this.token;
        }
        for (int i = 0; i < n; ++i) {
            if (token.next != null) {
                token = token.next;
            }
            else {
                final Token nextToken = this.token_source.getNextToken();
                token.next = nextToken;
                token = nextToken;
            }
        }
        return token;
    }
    
    public final void group_body() throws ParseException {
        final ASTgroup_body asTgroup_body = new ASTgroup_body(5);
        this.jjtree.openNodeScope(asTgroup_body);
        this.jjtreeOpenNodeScope(asTgroup_body);
        try {
            this.jj_consume_token(4);
            int n;
            if (this.jj_ntk == -1) {
                n = this.jj_ntk();
            }
            else {
                n = this.jj_ntk;
            }
            while (true) {
                Label_0081: {
                    if (n == 6 || n == 14 || n == 31) {
                        break Label_0081;
                    }
                    this.jj_la1[7] = this.jj_gen;
                    while (true) {
                        int n2;
                        if (this.jj_ntk == -1) {
                            n2 = this.jj_ntk();
                        }
                        else {
                            n2 = this.jj_ntk;
                        }
                        if (n2 != 3) {
                            break;
                        }
                        this.jj_consume_token(3);
                        int n3;
                        if (this.jj_ntk == -1) {
                            n3 = this.jj_ntk();
                        }
                        else {
                            n3 = this.jj_ntk;
                        }
                        if (n3 == 6 || n3 == 14 || n3 == 31) {
                            break Label_0081;
                        }
                        this.jj_la1[9] = this.jj_gen;
                    }
                    this.jj_la1[8] = this.jj_gen;
                    this.jj_consume_token(5);
                    this.jjtree.closeNodeScope(asTgroup_body, true);
                    this.jjtreeCloseNodeScope(asTgroup_body);
                    return;
                }
                this.mailbox();
                continue;
            }
        }
        finally {
            boolean b;
            try {
                this.jjtree.clearNodeScope(asTgroup_body);
                try {
                    final Throwable t;
                    if (t instanceof RuntimeException) {
                        throw (RuntimeException)t;
                    }
                    if (t instanceof ParseException) {
                        throw (ParseException)t;
                    }
                    throw (Error)t;
                }
                finally {}
            }
            finally {
                b = true;
            }
            if (b) {
                this.jjtree.closeNodeScope(asTgroup_body, true);
                this.jjtreeCloseNodeScope(asTgroup_body);
            }
        }
    }
    
    void jjtreeCloseNodeScope(final Node node) {
        ((SimpleNode)node).lastToken = this.getToken(0);
    }
    
    void jjtreeOpenNodeScope(final Node node) {
        ((SimpleNode)node).firstToken = this.getToken(1);
    }
    
    public final void local_part() throws ParseException {
        ASTlocal_part asTlocal_part;
        int n = 0;
        Token token;
        Block_8_Outer:Label_0047_Outer:
        while (true) {
            asTlocal_part = new ASTlocal_part(10);
            this.jjtree.openNodeScope(asTlocal_part);
            this.jjtreeOpenNodeScope(asTlocal_part);
            while (true) {
                Label_0057_Outer:Label_0274_Outer:
                while (true) {
                Label_0274:
                    while (true) {
                    Label_0327:
                        while (true) {
                            Label_0312: {
                                try {
                                    if (this.jj_ntk == -1) {
                                        n = this.jj_ntk();
                                        break Label_0312;
                                    }
                                    n = this.jj_ntk;
                                    break Label_0312;
                                    // iftrue(Label_0147:, n == 9 || n == 14 || n == 31)
                                    // iftrue(Label_0163:, this.jj_ntk != -1)
                                    // iftrue(Label_0284:, token.kind == 31 || token.image.charAt(token.image.length() - 1) != '.')
                                    // iftrue(Label_0188:, n == 9)
                                    // iftrue(Label_0241:, this.jj_ntk != -1)
                                    // iftrue(Label_0098:, this.jj_ntk != -1)
                                Label_0082:
                                    while (true) {
                                    Block_16:
                                        while (true) {
                                            Block_13_Outer:Label_0195_Outer:
                                            while (true) {
                                            Block_15:
                                                while (true) {
                                                    while (true) {
                                                        Label_0168: {
                                                            Block_11: {
                                                                break Block_11;
                                                                Label_0147: {
                                                                    n = this.jj_ntk();
                                                                }
                                                                break Label_0168;
                                                            }
                                                            this.jj_la1[16] = this.jj_gen;
                                                            return;
                                                            Label_0284: {
                                                                throw new ParseException("Words in local part must be separated by '.'");
                                                            }
                                                            this.jj_la1[17] = this.jj_gen;
                                                            break Block_15;
                                                            n = this.jj_ntk();
                                                            continue Block_13_Outer;
                                                            Label_0163:
                                                            n = this.jj_ntk;
                                                        }
                                                        continue Label_0195_Outer;
                                                    }
                                                    Label_0188: {
                                                        token = this.jj_consume_token(9);
                                                    }
                                                    continue Block_8_Outer;
                                                }
                                                break Block_16;
                                                token = this.jj_consume_token(31);
                                                break Label_0082;
                                                this.jj_la1[15] = this.jj_gen;
                                                this.jj_consume_token(-1);
                                                throw new ParseException();
                                                Label_0098: {
                                                    n = this.jj_ntk;
                                                }
                                                continue Block_13_Outer;
                                            }
                                            continue Label_0047_Outer;
                                        }
                                        n = this.jj_ntk();
                                        break Label_0327;
                                        Label_0241: {
                                            n = this.jj_ntk;
                                        }
                                        break Label_0327;
                                        token = this.jj_consume_token(14);
                                        continue Label_0082;
                                    }
                                    this.jj_la1[18] = this.jj_gen;
                                    this.jj_consume_token(-1);
                                    throw new ParseException();
                                }
                                finally {
                                    this.jjtree.closeNodeScope(asTlocal_part, true);
                                    this.jjtreeCloseNodeScope(asTlocal_part);
                                }
                            }
                            if (n == 14) {
                                continue Label_0274;
                            }
                            if (n == 31) {
                                continue Label_0057_Outer;
                            }
                            continue Label_0274_Outer;
                        }
                        if (n == 14) {
                            continue Label_0274;
                        }
                        break;
                    }
                    if (n == 31) {
                        continue Label_0057_Outer;
                    }
                    break;
                }
                continue;
            }
        }
    }
    
    public final void mailbox() throws ParseException {
    Label_0088_Outer:
        while (true) {
            final ASTmailbox asTmailbox = new ASTmailbox(3);
            this.jjtree.openNodeScope(asTmailbox);
            this.jjtreeOpenNodeScope(asTmailbox);
            int jj_ntk;
            final Throwable t;
            boolean b = false;
            Label_0095_Outer:Label_0063_Outer:
            while (true) {
                while (true) {
                    while (true) {
                        Label_0181: {
                            try {
                                while (true) {
                                    if (this.jj_2_2(Integer.MAX_VALUE)) {
                                        this.addr_spec();
                                        this.jjtree.closeNodeScope(asTmailbox, true);
                                        this.jjtreeCloseNodeScope(asTmailbox);
                                        return;
                                    }
                                    if (this.jj_ntk == -1) {
                                        this.jj_ntk();
                                        break Label_0181;
                                    }
                                    jj_ntk = this.jj_ntk;
                                    break Label_0181;
                                    this.name_addr();
                                    continue Label_0088_Outer;
                                    this.angle_addr();
                                    continue Label_0088_Outer;
                                }
                                this.jj_la1[6] = this.jj_gen;
                                this.jj_consume_token(-1);
                                throw new ParseException();
                            }
                            finally {
                                try {
                                    this.jjtree.clearNodeScope(asTmailbox);
                                    try {
                                        if (t instanceof RuntimeException) {
                                            throw (RuntimeException)t;
                                        }
                                        if (t instanceof ParseException) {
                                            throw (ParseException)t;
                                        }
                                        throw (Error)t;
                                    }
                                    finally {}
                                }
                                finally {
                                    b = true;
                                }
                                if (b) {
                                    this.jjtree.closeNodeScope(asTmailbox, true);
                                    this.jjtreeCloseNodeScope(asTmailbox);
                                }
                            }
                        }
                        if ((b ? 1 : 0) == 6) {
                            continue Label_0063_Outer;
                        }
                        break;
                    }
                    if ((b ? 1 : 0) != 14 && (b ? 1 : 0) != 31) {
                        continue;
                    }
                    break;
                }
                continue Label_0095_Outer;
            }
        }
    }
    
    public final void name_addr() throws ParseException {
        final ASTname_addr asTname_addr = new ASTname_addr(4);
        this.jjtree.openNodeScope(asTname_addr);
        this.jjtreeOpenNodeScope(asTname_addr);
        try {
            this.phrase();
            this.angle_addr();
            this.jjtree.closeNodeScope(asTname_addr, true);
            this.jjtreeCloseNodeScope(asTname_addr);
        }
        finally {
            boolean b;
            try {
                this.jjtree.clearNodeScope(asTname_addr);
                try {
                    final Throwable t;
                    if (t instanceof RuntimeException) {
                        throw (RuntimeException)t;
                    }
                    if (t instanceof ParseException) {
                        throw (ParseException)t;
                    }
                    throw (Error)t;
                }
                finally {}
            }
            finally {
                b = true;
            }
            if (b) {
                this.jjtree.closeNodeScope(asTname_addr, true);
                this.jjtreeCloseNodeScope(asTname_addr);
            }
        }
    }
    
    public ASTaddress parseAddress() throws ParseException {
        try {
            this.parseAddress0();
            return (ASTaddress)this.jjtree.rootNode();
        }
        catch (TokenMgrError tokenMgrError) {
            throw new ParseException(tokenMgrError.getMessage());
        }
    }
    
    public final void parseAddress0() throws ParseException {
        this.address();
        this.jj_consume_token(0);
    }
    
    public ASTaddress_list parseAddressList() throws ParseException {
        try {
            this.parseAddressList0();
            return (ASTaddress_list)this.jjtree.rootNode();
        }
        catch (TokenMgrError tokenMgrError) {
            throw new ParseException(tokenMgrError.getMessage());
        }
    }
    
    public final void parseAddressList0() throws ParseException {
        this.address_list();
        this.jj_consume_token(0);
    }
    
    public final void parseLine() throws ParseException {
        this.address_list();
        int n;
        if ((n = this.jj_ntk) == -1) {
            n = this.jj_ntk();
        }
        if (n != 1) {
            this.jj_la1[0] = this.jj_gen;
        }
        else {
            this.jj_consume_token(1);
        }
        this.jj_consume_token(2);
    }
    
    public ASTmailbox parseMailbox() throws ParseException {
        try {
            this.parseMailbox0();
            return (ASTmailbox)this.jjtree.rootNode();
        }
        catch (TokenMgrError tokenMgrError) {
            throw new ParseException(tokenMgrError.getMessage());
        }
    }
    
    public final void parseMailbox0() throws ParseException {
        this.mailbox();
        this.jj_consume_token(0);
    }
    
    public final void phrase() throws ParseException {
        final ASTphrase asTphrase = new ASTphrase(8);
        this.jjtree.openNodeScope(asTphrase);
        this.jjtreeOpenNodeScope(asTphrase);
        try {
            int n;
            do {
                int n2;
                if (this.jj_ntk == -1) {
                    n2 = this.jj_ntk();
                }
                else {
                    n2 = this.jj_ntk;
                }
                if (n2 != 14) {
                    if (n2 != 31) {
                        this.jj_la1[13] = this.jj_gen;
                        this.jj_consume_token(-1);
                        throw new ParseException();
                    }
                    this.jj_consume_token(31);
                }
                else {
                    this.jj_consume_token(14);
                }
                if (this.jj_ntk == -1) {
                    n = this.jj_ntk();
                }
                else {
                    n = this.jj_ntk;
                }
            } while (n == 14 || n == 31);
            this.jj_la1[14] = this.jj_gen;
        }
        finally {
            this.jjtree.closeNodeScope(asTphrase, true);
            this.jjtreeCloseNodeScope(asTphrase);
        }
    }
    
    public final void route() throws ParseException {
        final ASTroute asTroute = new ASTroute(7);
        this.jjtree.openNodeScope(asTroute);
        this.jjtreeOpenNodeScope(asTroute);
        try {
            this.jj_consume_token(8);
            while (true) {
                this.domain();
                int n;
                if (this.jj_ntk == -1) {
                    n = this.jj_ntk();
                }
                else {
                    n = this.jj_ntk;
                }
                if (n != 3 && n != 8) {
                    break;
                }
                while (true) {
                    int n2;
                    if (this.jj_ntk == -1) {
                        n2 = this.jj_ntk();
                    }
                    else {
                        n2 = this.jj_ntk;
                    }
                    if (n2 != 3) {
                        break;
                    }
                    this.jj_consume_token(3);
                }
                this.jj_la1[12] = this.jj_gen;
                this.jj_consume_token(8);
            }
            this.jj_la1[11] = this.jj_gen;
            this.jj_consume_token(4);
            this.jjtree.closeNodeScope(asTroute, true);
            this.jjtreeCloseNodeScope(asTroute);
        }
        finally {
            boolean b;
            try {
                this.jjtree.clearNodeScope(asTroute);
                try {
                    final Throwable t;
                    if (t instanceof RuntimeException) {
                        throw (RuntimeException)t;
                    }
                    if (t instanceof ParseException) {
                        throw (ParseException)t;
                    }
                    throw (Error)t;
                }
                finally {}
            }
            finally {
                b = true;
            }
            if (b) {
                this.jjtree.closeNodeScope(asTroute, true);
                this.jjtreeCloseNodeScope(asTroute);
            }
        }
    }
    
    static final class JJCalls
    {
        int arg;
        Token first;
        int gen;
        JJCalls next;
    }
    
    private static final class LookaheadSuccess extends Error
    {
    }
}
