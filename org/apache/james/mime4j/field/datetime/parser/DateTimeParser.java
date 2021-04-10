package org.apache.james.mime4j.field.datetime.parser;

import java.util.*;
import java.io.*;
import org.apache.james.mime4j.field.datetime.*;

public class DateTimeParser implements DateTimeParserConstants
{
    private static final boolean ignoreMilitaryZoneOffset = true;
    private static int[] jj_la1_0;
    private static int[] jj_la1_1;
    private Vector<int[]> jj_expentries;
    private int[] jj_expentry;
    private int jj_gen;
    SimpleCharStream jj_input_stream;
    private int jj_kind;
    private final int[] jj_la1;
    public Token jj_nt;
    private int jj_ntk;
    public Token token;
    public DateTimeParserTokenManager token_source;
    
    static {
        jj_la1_0();
        jj_la1_1();
    }
    
    public DateTimeParser(final InputStream inputStream) {
        this(inputStream, null);
    }
    
    public DateTimeParser(final InputStream inputStream, final String s) {
        this.jj_la1 = new int[7];
        this.jj_expentries = new Vector<int[]>();
        this.jj_kind = -1;
        try {
            this.jj_input_stream = new SimpleCharStream(inputStream, s, 1, 1);
            this.token_source = new DateTimeParserTokenManager(this.jj_input_stream);
            this.token = new Token();
            this.jj_ntk = -1;
            int i = 0;
            this.jj_gen = 0;
            while (i < 7) {
                this.jj_la1[i] = -1;
                ++i;
            }
        }
        catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public DateTimeParser(final Reader reader) {
        this.jj_la1 = new int[7];
        this.jj_expentries = new Vector<int[]>();
        this.jj_kind = -1;
        this.jj_input_stream = new SimpleCharStream(reader, 1, 1);
        this.token_source = new DateTimeParserTokenManager(this.jj_input_stream);
        this.token = new Token();
        this.jj_ntk = -1;
        int i = 0;
        this.jj_gen = 0;
        while (i < 7) {
            this.jj_la1[i] = -1;
            ++i;
        }
    }
    
    public DateTimeParser(final DateTimeParserTokenManager token_source) {
        this.jj_la1 = new int[7];
        this.jj_expentries = new Vector<int[]>();
        this.jj_kind = -1;
        this.token_source = token_source;
        this.token = new Token();
        this.jj_ntk = -1;
        int i = 0;
        this.jj_gen = 0;
        while (i < 7) {
            this.jj_la1[i] = -1;
            ++i;
        }
    }
    
    private static int getMilitaryZoneOffset(final char c) {
        return 0;
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
        DateTimeParser.jj_la1_0 = new int[] { 2, 2032, 2032, 8386560, 8388608, -16777216, -33554432 };
    }
    
    private static void jj_la1_1() {
        DateTimeParser.jj_la1_1 = new int[] { 0, 0, 0, 0, 0, 15, 15 };
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
    
    public static void main(final String[] array) throws ParseException {
        try {
            while (true) {
                new DateTimeParser(System.in).parseLine();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private static int parseDigits(final Token token) {
        return Integer.parseInt(token.image, 10);
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
            while (i < 7) {
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
        while (i < 7) {
            this.jj_la1[i] = -1;
            ++i;
        }
    }
    
    public void ReInit(final DateTimeParserTokenManager token_source) {
        this.token_source = token_source;
        this.token = new Token();
        this.jj_ntk = -1;
        int i = 0;
        this.jj_gen = 0;
        while (i < 7) {
            this.jj_la1[i] = -1;
            ++i;
        }
    }
    
    public final Date date() throws ParseException {
        return new Date(this.year(), this.month(), this.day());
    }
    
    public final DateTime date_time() throws ParseException {
        int n;
        if ((n = this.jj_ntk) == -1) {
            n = this.jj_ntk();
        }
        switch (n) {
            default: {
                this.jj_la1[1] = this.jj_gen;
                break;
            }
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10: {
                this.day_of_week();
                this.jj_consume_token(3);
                break;
            }
        }
        final Date date = this.date();
        final Time time = this.time();
        return new DateTime(date.getYear(), date.getMonth(), date.getDay(), time.getHour(), time.getMinute(), time.getSecond(), time.getZone());
    }
    
    public final int day() throws ParseException {
        return parseDigits(this.jj_consume_token(46));
    }
    
    public final String day_of_week() throws ParseException {
        int n;
        if ((n = this.jj_ntk) == -1) {
            n = this.jj_ntk();
        }
        int n2 = 0;
        switch (n) {
            default: {
                this.jj_la1[2] = this.jj_gen;
                this.jj_consume_token(-1);
                throw new ParseException();
            }
            case 10: {
                n2 = 10;
                break;
            }
            case 9: {
                n2 = 9;
                break;
            }
            case 8: {
                n2 = 8;
                break;
            }
            case 7: {
                n2 = 7;
                break;
            }
            case 6: {
                n2 = 6;
                break;
            }
            case 5: {
                n2 = 5;
                break;
            }
            case 4: {
                n2 = 4;
                break;
            }
        }
        this.jj_consume_token(n2);
        return this.token.image;
    }
    
    public final void disable_tracing() {
    }
    
    public final void enable_tracing() {
    }
    
    public ParseException generateParseException() {
        this.jj_expentries.removeAllElements();
        final boolean[] array = new boolean[49];
        final int n = 0;
        for (int i = 0; i < 49; ++i) {
            array[i] = false;
        }
        final int jj_kind = this.jj_kind;
        if (jj_kind >= 0) {
            array[jj_kind] = true;
            this.jj_kind = -1;
        }
        for (int j = 0; j < 7; ++j) {
            if (this.jj_la1[j] == this.jj_gen) {
                for (int k = 0; k < 32; ++k) {
                    final int n2 = DateTimeParser.jj_la1_0[j];
                    final int n3 = 1 << k;
                    if ((n2 & n3) != 0x0) {
                        array[k] = true;
                    }
                    if ((DateTimeParser.jj_la1_1[j] & n3) != 0x0) {
                        array[k + 32] = true;
                    }
                }
            }
        }
        for (int l = 0; l < 49; ++l) {
            if (array[l]) {
                final int[] jj_expentry = { 0 };
                (this.jj_expentry = jj_expentry)[0] = l;
                this.jj_expentries.addElement(jj_expentry);
            }
        }
        final int[][] array2 = new int[this.jj_expentries.size()][];
        for (int n4 = n; n4 < this.jj_expentries.size(); ++n4) {
            array2[n4] = this.jj_expentries.elementAt(n4);
        }
        return new ParseException(this.token, array2, DateTimeParser.tokenImage);
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
    
    public final int hour() throws ParseException {
        return parseDigits(this.jj_consume_token(46));
    }
    
    public final int minute() throws ParseException {
        return parseDigits(this.jj_consume_token(46));
    }
    
    public final int month() throws ParseException {
        int n;
        if ((n = this.jj_ntk) == -1) {
            n = this.jj_ntk();
        }
        switch (n) {
            default: {
                this.jj_la1[3] = this.jj_gen;
                this.jj_consume_token(-1);
                throw new ParseException();
            }
            case 22: {
                this.jj_consume_token(22);
                return 12;
            }
            case 21: {
                this.jj_consume_token(21);
                return 11;
            }
            case 20: {
                this.jj_consume_token(20);
                return 10;
            }
            case 19: {
                this.jj_consume_token(19);
                return 9;
            }
            case 18: {
                this.jj_consume_token(18);
                return 8;
            }
            case 17: {
                this.jj_consume_token(17);
                return 7;
            }
            case 16: {
                this.jj_consume_token(16);
                return 6;
            }
            case 15: {
                this.jj_consume_token(15);
                return 5;
            }
            case 14: {
                this.jj_consume_token(14);
                return 4;
            }
            case 13: {
                this.jj_consume_token(13);
                return 3;
            }
            case 12: {
                this.jj_consume_token(12);
                return 2;
            }
            case 11: {
                this.jj_consume_token(11);
                return 1;
            }
        }
    }
    
    public final int obs_zone() throws ParseException {
        int n;
        if ((n = this.jj_ntk) == -1) {
            n = this.jj_ntk();
        }
        final int n2 = -7;
        int n6 = 0;
        Label_0222: {
            int n5 = 0;
            Label_0201: {
                int n4 = 0;
                Label_0173: {
                    int n3 = 0;
                    switch (n) {
                        default: {
                            this.jj_la1[6] = this.jj_gen;
                            this.jj_consume_token(-1);
                            throw new ParseException();
                        }
                        case 35: {
                            final int militaryZoneOffset = getMilitaryZoneOffset(this.jj_consume_token(35).image.charAt(0));
                            return militaryZoneOffset * 100;
                        }
                        case 34: {
                            n3 = 34;
                            break;
                        }
                        case 33: {
                            this.jj_consume_token(33);
                            final int militaryZoneOffset = -8;
                            return militaryZoneOffset * 100;
                        }
                        case 32: {
                            n4 = 32;
                            break Label_0173;
                        }
                        case 31: {
                            n3 = 31;
                            break;
                        }
                        case 30: {
                            n5 = 30;
                            break Label_0201;
                        }
                        case 29: {
                            n4 = 29;
                            break Label_0173;
                        }
                        case 28: {
                            this.jj_consume_token(28);
                            final int militaryZoneOffset = -4;
                            return militaryZoneOffset * 100;
                        }
                        case 27: {
                            n5 = 27;
                            break Label_0201;
                        }
                        case 26: {
                            n6 = 26;
                            break Label_0222;
                        }
                        case 25: {
                            n6 = 25;
                            break Label_0222;
                        }
                    }
                    this.jj_consume_token(n3);
                    final int militaryZoneOffset = n2;
                    return militaryZoneOffset * 100;
                }
                this.jj_consume_token(n4);
                final int militaryZoneOffset = -6;
                return militaryZoneOffset * 100;
            }
            this.jj_consume_token(n5);
            final int militaryZoneOffset = -5;
            return militaryZoneOffset * 100;
        }
        this.jj_consume_token(n6);
        final int militaryZoneOffset = 0;
        return militaryZoneOffset * 100;
    }
    
    public final DateTime parseAll() throws ParseException {
        final DateTime date_time = this.date_time();
        this.jj_consume_token(0);
        return date_time;
    }
    
    public final DateTime parseLine() throws ParseException {
        final DateTime date_time = this.date_time();
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
        return date_time;
    }
    
    public final int second() throws ParseException {
        return parseDigits(this.jj_consume_token(46));
    }
    
    public final Time time() throws ParseException {
        final int hour = this.hour();
        this.jj_consume_token(23);
        final int minute = this.minute();
        int n;
        if ((n = this.jj_ntk) == -1) {
            n = this.jj_ntk();
        }
        int second;
        if (n != 23) {
            this.jj_la1[4] = this.jj_gen;
            second = 0;
        }
        else {
            this.jj_consume_token(23);
            second = this.second();
        }
        return new Time(hour, minute, second, this.zone());
    }
    
    public final String year() throws ParseException {
        return this.jj_consume_token(46).image;
    }
    
    public final int zone() throws ParseException {
        final int jj_ntk = this.jj_ntk;
        final int n = -1;
        int jj_ntk2 = jj_ntk;
        if (jj_ntk == -1) {
            jj_ntk2 = this.jj_ntk();
        }
        switch (jj_ntk2) {
            default: {
                this.jj_la1[5] = this.jj_gen;
                this.jj_consume_token(-1);
                throw new ParseException();
            }
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35: {
                return this.obs_zone();
            }
            case 24: {
                final Token jj_consume_token = this.jj_consume_token(24);
                final int digits = parseDigits(this.jj_consume_token(46));
                int n2;
                if (jj_consume_token.image.equals("-")) {
                    n2 = n;
                }
                else {
                    n2 = 1;
                }
                return digits * n2;
            }
        }
    }
    
    private static class Date
    {
        private int day;
        private int month;
        private String year;
        
        public Date(final String year, final int month, final int day) {
            this.year = year;
            this.month = month;
            this.day = day;
        }
        
        public int getDay() {
            return this.day;
        }
        
        public int getMonth() {
            return this.month;
        }
        
        public String getYear() {
            return this.year;
        }
    }
    
    private static class Time
    {
        private int hour;
        private int minute;
        private int second;
        private int zone;
        
        public Time(final int hour, final int minute, final int second, final int zone) {
            this.hour = hour;
            this.minute = minute;
            this.second = second;
            this.zone = zone;
        }
        
        public int getHour() {
            return this.hour;
        }
        
        public int getMinute() {
            return this.minute;
        }
        
        public int getSecond() {
            return this.second;
        }
        
        public int getZone() {
            return this.zone;
        }
    }
}
