package org.apache.james.mime4j.field.structured.parser;

import java.util.*;
import java.io.*;

public class StructuredFieldParser implements StructuredFieldParserConstants
{
    private static int[] jj_la1_0;
    private Vector<int[]> jj_expentries;
    private int[] jj_expentry;
    private int jj_gen;
    SimpleCharStream jj_input_stream;
    private int jj_kind;
    private final int[] jj_la1;
    public Token jj_nt;
    private int jj_ntk;
    private boolean preserveFolding;
    public Token token;
    public StructuredFieldParserTokenManager token_source;
    
    static {
        jj_la1_0();
    }
    
    public StructuredFieldParser(final InputStream inputStream) {
        this(inputStream, null);
    }
    
    public StructuredFieldParser(final InputStream inputStream, final String s) {
        int i = 0;
        this.preserveFolding = false;
        this.jj_la1 = new int[2];
        this.jj_expentries = new Vector<int[]>();
        this.jj_kind = -1;
        try {
            this.jj_input_stream = new SimpleCharStream(inputStream, s, 1, 1);
            this.token_source = new StructuredFieldParserTokenManager(this.jj_input_stream);
            this.token = new Token();
            this.jj_ntk = -1;
            this.jj_gen = 0;
            while (i < 2) {
                this.jj_la1[i] = -1;
                ++i;
            }
        }
        catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public StructuredFieldParser(final Reader reader) {
        int i = 0;
        this.preserveFolding = false;
        this.jj_la1 = new int[2];
        this.jj_expentries = new Vector<int[]>();
        this.jj_kind = -1;
        this.jj_input_stream = new SimpleCharStream(reader, 1, 1);
        this.token_source = new StructuredFieldParserTokenManager(this.jj_input_stream);
        this.token = new Token();
        this.jj_ntk = -1;
        this.jj_gen = 0;
        while (i < 2) {
            this.jj_la1[i] = -1;
            ++i;
        }
    }
    
    public StructuredFieldParser(final StructuredFieldParserTokenManager token_source) {
        int i = 0;
        this.preserveFolding = false;
        this.jj_la1 = new int[2];
        this.jj_expentries = new Vector<int[]>();
        this.jj_kind = -1;
        this.token_source = token_source;
        this.token = new Token();
        this.jj_ntk = -1;
        this.jj_gen = 0;
        while (i < 2) {
            this.jj_la1[i] = -1;
            ++i;
        }
    }
    
    private final String doParse() throws ParseException {
        final StringBuffer sb = new StringBuffer(50);
        int n = 1;
        int n2 = 0;
        while (true) {
            int n3;
            if ((n3 = this.jj_ntk) == -1) {
                n3 = this.jj_ntk();
            }
            switch (n3) {
                default: {
                    this.jj_la1[0] = this.jj_gen;
                    return sb.toString();
                }
                case 11:
                case 12:
                case 13:
                case 14:
                case 15: {
                    int n4;
                    if ((n4 = this.jj_ntk) == -1) {
                        n4 = this.jj_ntk();
                    }
                    String image = null;
                    Label_0284: {
                        Token jj_consume_token2 = null;
                        int n5 = 0;
                        int n6 = 0;
                        Label_0272: {
                            Label_0205: {
                                switch (n4) {
                                    default: {
                                        this.jj_la1[1] = this.jj_gen;
                                        this.jj_consume_token(-1);
                                        throw new ParseException();
                                    }
                                    case 15: {
                                        final Token jj_consume_token = this.jj_consume_token(15);
                                        if (n != 0) {
                                            jj_consume_token2 = jj_consume_token;
                                            break;
                                        }
                                        n5 = n;
                                        n6 = n2;
                                        jj_consume_token2 = jj_consume_token;
                                        if (n2 != 0) {
                                            jj_consume_token2 = jj_consume_token;
                                            break Label_0205;
                                        }
                                        break Label_0272;
                                    }
                                    case 14: {
                                        this.jj_consume_token(14);
                                        n2 = 1;
                                        continue;
                                    }
                                    case 13: {
                                        final Token jj_consume_token3 = this.jj_consume_token(13);
                                        if (n != 0) {
                                            jj_consume_token2 = jj_consume_token3;
                                            break;
                                        }
                                        n5 = n;
                                        n6 = n2;
                                        jj_consume_token2 = jj_consume_token3;
                                        if (n2 != 0) {
                                            jj_consume_token2 = jj_consume_token3;
                                            break Label_0205;
                                        }
                                        break Label_0272;
                                    }
                                    case 12: {
                                        this.jj_consume_token(12);
                                        if (this.preserveFolding) {
                                            image = "\r\n";
                                            break Label_0284;
                                        }
                                        continue;
                                    }
                                    case 11: {
                                        jj_consume_token2 = this.jj_consume_token(11);
                                        n5 = n;
                                        n6 = n2;
                                        break Label_0272;
                                    }
                                }
                                n5 = 0;
                                n6 = n2;
                                break Label_0272;
                            }
                            sb.append(" ");
                            n6 = 0;
                            n5 = n;
                        }
                        image = jj_consume_token2.image;
                        n2 = n6;
                        n = n5;
                    }
                    sb.append(image);
                    continue;
                }
            }
        }
    }
    
    private final Token jj_consume_token(final int jj_kind) throws ParseException {
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
        if (this.token.kind == jj_kind) {
            ++this.jj_gen;
            return this.token;
        }
        this.token = token;
        this.jj_kind = jj_kind;
        throw this.generateParseException();
    }
    
    private static void jj_la1_0() {
        StructuredFieldParser.jj_la1_0 = new int[] { 63488, 63488 };
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
    
    public void ReInit(final InputStream inputStream) {
        this.ReInit(inputStream, null);
    }
    
    public void ReInit(final InputStream inputStream, final String s) {
        try {
            this.jj_input_stream.ReInit(inputStream, s, 1, 1);
            this.token_source.ReInit(this.jj_input_stream);
            this.token = new Token();
            this.jj_ntk = -1;
            int i = 0;
            this.jj_gen = 0;
            while (i < 2) {
                this.jj_la1[i] = -1;
                ++i;
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
        int i = 0;
        this.jj_gen = 0;
        while (i < 2) {
            this.jj_la1[i] = -1;
            ++i;
        }
    }
    
    public void ReInit(final StructuredFieldParserTokenManager token_source) {
        this.token_source = token_source;
        this.token = new Token();
        this.jj_ntk = -1;
        int i = 0;
        this.jj_gen = 0;
        while (i < 2) {
            this.jj_la1[i] = -1;
            ++i;
        }
    }
    
    public final void disable_tracing() {
    }
    
    public final void enable_tracing() {
    }
    
    public ParseException generateParseException() {
        this.jj_expentries.removeAllElements();
        final boolean[] array = new boolean[18];
        final int n = 0;
        for (int i = 0; i < 18; ++i) {
            array[i] = false;
        }
        final int jj_kind = this.jj_kind;
        if (jj_kind >= 0) {
            array[jj_kind] = true;
            this.jj_kind = -1;
        }
        for (int j = 0; j < 2; ++j) {
            if (this.jj_la1[j] == this.jj_gen) {
                for (int k = 0; k < 32; ++k) {
                    if ((StructuredFieldParser.jj_la1_0[j] & 1 << k) != 0x0) {
                        array[k] = true;
                    }
                }
            }
        }
        for (int l = 0; l < 18; ++l) {
            if (array[l]) {
                final int[] jj_expentry = { 0 };
                (this.jj_expentry = jj_expentry)[0] = l;
                this.jj_expentries.addElement(jj_expentry);
            }
        }
        final int[][] array2 = new int[this.jj_expentries.size()][];
        for (int n2 = n; n2 < this.jj_expentries.size(); ++n2) {
            array2[n2] = this.jj_expentries.elementAt(n2);
        }
        return new ParseException(this.token, array2, StructuredFieldParser.tokenImage);
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
        Token token = this.token;
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
    
    public boolean isFoldingPreserved() {
        return this.preserveFolding;
    }
    
    public String parse() throws ParseException {
        try {
            return this.doParse();
        }
        catch (TokenMgrError tokenMgrError) {
            throw new ParseException(tokenMgrError);
        }
    }
    
    public void setFoldingPreserved(final boolean preserveFolding) {
        this.preserveFolding = preserveFolding;
    }
}
