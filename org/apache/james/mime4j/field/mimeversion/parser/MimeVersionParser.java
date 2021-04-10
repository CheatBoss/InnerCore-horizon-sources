package org.apache.james.mime4j.field.mimeversion.parser;

import java.util.*;
import java.io.*;

public class MimeVersionParser implements MimeVersionParserConstants
{
    public static final int INITIAL_VERSION_VALUE = -1;
    private static int[] jj_la1_0;
    private Vector<int[]> jj_expentries;
    private int[] jj_expentry;
    private int jj_gen;
    SimpleCharStream jj_input_stream;
    private int jj_kind;
    private final int[] jj_la1;
    public Token jj_nt;
    private int jj_ntk;
    private int major;
    private int minor;
    public Token token;
    public MimeVersionParserTokenManager token_source;
    
    static {
        jj_la1_0();
    }
    
    public MimeVersionParser(final InputStream inputStream) {
        this(inputStream, null);
    }
    
    public MimeVersionParser(final InputStream inputStream, final String s) {
        this.major = -1;
        this.minor = -1;
        this.jj_la1 = new int[1];
        this.jj_expentries = new Vector<int[]>();
        this.jj_kind = -1;
        try {
            this.jj_input_stream = new SimpleCharStream(inputStream, s, 1, 1);
            this.token_source = new MimeVersionParserTokenManager(this.jj_input_stream);
            this.token = new Token();
            this.jj_ntk = -1;
            int i = 0;
            this.jj_gen = 0;
            while (i < 1) {
                this.jj_la1[i] = -1;
                ++i;
            }
        }
        catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public MimeVersionParser(final Reader reader) {
        this.major = -1;
        this.minor = -1;
        this.jj_la1 = new int[1];
        this.jj_expentries = new Vector<int[]>();
        this.jj_kind = -1;
        this.jj_input_stream = new SimpleCharStream(reader, 1, 1);
        this.token_source = new MimeVersionParserTokenManager(this.jj_input_stream);
        this.token = new Token();
        this.jj_ntk = -1;
        int i = 0;
        this.jj_gen = 0;
        while (i < 1) {
            this.jj_la1[i] = -1;
            ++i;
        }
    }
    
    public MimeVersionParser(final MimeVersionParserTokenManager token_source) {
        this.major = -1;
        this.minor = -1;
        this.jj_la1 = new int[1];
        this.jj_expentries = new Vector<int[]>();
        this.jj_kind = -1;
        this.token_source = token_source;
        this.token = new Token();
        this.jj_ntk = -1;
        int i = 0;
        this.jj_gen = 0;
        while (i < 1) {
            this.jj_la1[i] = -1;
            ++i;
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
        MimeVersionParser.jj_la1_0 = new int[] { 2 };
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
            while (i < 1) {
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
        while (i < 1) {
            this.jj_la1[i] = -1;
            ++i;
        }
    }
    
    public void ReInit(final MimeVersionParserTokenManager token_source) {
        this.token_source = token_source;
        this.token = new Token();
        this.jj_ntk = -1;
        int i = 0;
        this.jj_gen = 0;
        while (i < 1) {
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
        final boolean[] array = new boolean[21];
        final int n = 0;
        for (int i = 0; i < 21; ++i) {
            array[i] = false;
        }
        final int jj_kind = this.jj_kind;
        if (jj_kind >= 0) {
            array[jj_kind] = true;
            this.jj_kind = -1;
        }
        for (int j = 0; j < 1; ++j) {
            if (this.jj_la1[j] == this.jj_gen) {
                for (int k = 0; k < 32; ++k) {
                    if ((MimeVersionParser.jj_la1_0[j] & 1 << k) != 0x0) {
                        array[k] = true;
                    }
                }
            }
        }
        for (int l = 0; l < 21; ++l) {
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
        return new ParseException(this.token, array2, MimeVersionParser.tokenImage);
    }
    
    public int getMajorVersion() {
        return this.major;
    }
    
    public int getMinorVersion() {
        return this.minor;
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
    
    public final void parse() throws ParseException {
        final Token jj_consume_token = this.jj_consume_token(17);
        this.jj_consume_token(18);
        final Token jj_consume_token2 = this.jj_consume_token(17);
        try {
            this.major = Integer.parseInt(jj_consume_token.image);
            this.minor = Integer.parseInt(jj_consume_token2.image);
        }
        catch (NumberFormatException ex) {
            throw new ParseException(ex.getMessage());
        }
    }
    
    public final void parseAll() throws ParseException {
        this.parse();
        this.jj_consume_token(0);
    }
    
    public final void parseLine() throws ParseException {
        this.parse();
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
}
