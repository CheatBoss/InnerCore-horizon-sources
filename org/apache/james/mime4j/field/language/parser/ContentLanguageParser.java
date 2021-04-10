package org.apache.james.mime4j.field.language.parser;

import java.util.*;
import java.io.*;

public class ContentLanguageParser implements ContentLanguageParserConstants
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
    private List<String> languages;
    public Token token;
    public ContentLanguageParserTokenManager token_source;
    
    static {
        jj_la1_0();
    }
    
    public ContentLanguageParser(final InputStream inputStream) {
        this(inputStream, null);
    }
    
    public ContentLanguageParser(final InputStream inputStream, final String s) {
        this.languages = new ArrayList<String>();
        this.jj_la1 = new int[3];
        this.jj_expentries = new Vector<int[]>();
        this.jj_kind = -1;
        try {
            this.jj_input_stream = new SimpleCharStream(inputStream, s, 1, 1);
            this.token_source = new ContentLanguageParserTokenManager(this.jj_input_stream);
            this.token = new Token();
            this.jj_ntk = -1;
            int i = 0;
            this.jj_gen = 0;
            while (i < 3) {
                this.jj_la1[i] = -1;
                ++i;
            }
        }
        catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public ContentLanguageParser(final Reader reader) {
        this.languages = new ArrayList<String>();
        this.jj_la1 = new int[3];
        this.jj_expentries = new Vector<int[]>();
        this.jj_kind = -1;
        this.jj_input_stream = new SimpleCharStream(reader, 1, 1);
        this.token_source = new ContentLanguageParserTokenManager(this.jj_input_stream);
        this.token = new Token();
        this.jj_ntk = -1;
        int i = 0;
        this.jj_gen = 0;
        while (i < 3) {
            this.jj_la1[i] = -1;
            ++i;
        }
    }
    
    public ContentLanguageParser(final ContentLanguageParserTokenManager token_source) {
        this.languages = new ArrayList<String>();
        this.jj_la1 = new int[3];
        this.jj_expentries = new Vector<int[]>();
        this.jj_kind = -1;
        this.token_source = token_source;
        this.token = new Token();
        this.jj_ntk = -1;
        int i = 0;
        this.jj_gen = 0;
        while (i < 3) {
            this.jj_la1[i] = -1;
            ++i;
        }
    }
    
    private final List<String> doParse() throws ParseException {
        while (true) {
            this.language();
            int n;
            if ((n = this.jj_ntk) == -1) {
                n = this.jj_ntk();
            }
            if (n != 1) {
                break;
            }
            this.jj_consume_token(1);
        }
        this.jj_la1[0] = this.jj_gen;
        return this.languages;
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
        ContentLanguageParser.jj_la1_0 = new int[] { 2, 524292, 524292 };
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
            while (i < 3) {
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
        while (i < 3) {
            this.jj_la1[i] = -1;
            ++i;
        }
    }
    
    public void ReInit(final ContentLanguageParserTokenManager token_source) {
        this.token_source = token_source;
        this.token = new Token();
        this.jj_ntk = -1;
        int i = 0;
        this.jj_gen = 0;
        while (i < 3) {
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
        final boolean[] array = new boolean[23];
        final int n = 0;
        for (int i = 0; i < 23; ++i) {
            array[i] = false;
        }
        final int jj_kind = this.jj_kind;
        if (jj_kind >= 0) {
            array[jj_kind] = true;
            this.jj_kind = -1;
        }
        for (int j = 0; j < 3; ++j) {
            if (this.jj_la1[j] == this.jj_gen) {
                for (int k = 0; k < 32; ++k) {
                    if ((ContentLanguageParser.jj_la1_0[j] & 1 << k) != 0x0) {
                        array[k] = true;
                    }
                }
            }
        }
        for (int l = 0; l < 23; ++l) {
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
        return new ParseException(this.token, array2, ContentLanguageParser.tokenImage);
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
    
    public final String language() throws ParseException {
        final StringBuffer sb = new StringBuffer();
        Token token = this.jj_consume_token(18);
        while (true) {
            sb.append(token.image);
            int n;
            if ((n = this.jj_ntk) == -1) {
                n = this.jj_ntk();
            }
            if (n != 2 && n != 19) {
                this.jj_la1[1] = this.jj_gen;
                final String string = sb.toString();
                this.languages.add(string);
                return string;
            }
            int n2;
            if ((n2 = this.jj_ntk) == -1) {
                n2 = this.jj_ntk();
            }
            if (n2 != 2) {
                if (n2 != 19) {
                    this.jj_la1[2] = this.jj_gen;
                    this.jj_consume_token(-1);
                    throw new ParseException();
                }
                token = this.jj_consume_token(19);
            }
            else {
                this.jj_consume_token(2);
                token = this.jj_consume_token(18);
            }
            sb.append('-');
        }
    }
    
    public List<String> parse() throws ParseException {
        try {
            return this.doParse();
        }
        catch (TokenMgrError tokenMgrError) {
            throw new ParseException(tokenMgrError);
        }
    }
}
