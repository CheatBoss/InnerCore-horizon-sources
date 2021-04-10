package org.mozilla.javascript.tools.idswitch;

import org.mozilla.javascript.tools.*;
import org.mozilla.javascript.*;
import java.text.*;
import java.util.*;
import java.io.*;

public class Main
{
    private static final int GENERATED_TAG = 2;
    private static final String GENERATED_TAG_STR = "generated";
    private static final int NORMAL_LINE = 0;
    private static final int STRING_TAG = 3;
    private static final String STRING_TAG_STR = "string";
    private static final int SWITCH_TAG = 1;
    private static final String SWITCH_TAG_STR = "string_id_map";
    private CodePrinter P;
    private ToolErrorReporter R;
    private final List<IdValuePair> all_pairs;
    private FileBody body;
    private String source_file;
    private int tag_definition_end;
    private int tag_value_end;
    private int tag_value_start;
    
    public Main() {
        this.all_pairs = new ArrayList<IdValuePair>();
    }
    
    private void add_id(final char[] array, final int n, final int n2, final int n3, final int n4) {
        final IdValuePair idValuePair = new IdValuePair(new String(array, n3, n4 - n3), new String(array, n, n2 - n));
        idValuePair.setLineNumber(this.body.getLineNumber());
        this.all_pairs.add(idValuePair);
    }
    
    private static boolean equals(final String s, final char[] array, int n, final int n2) {
        if (s.length() == n2 - n) {
            final int n3 = 0;
            int i;
            for (i = n, n = n3; i != n2; ++i, ++n) {
                if (array[i] != s.charAt(n)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    private int exec(final String[] array) {
        this.R = new ToolErrorReporter(true, System.err);
        final int process_options = this.process_options(array);
        if (process_options == 0) {
            this.option_error(ToolErrorReporter.getMessage("msg.idswitch.no_file_argument"));
            return -1;
        }
        if (process_options > 1) {
            this.option_error(ToolErrorReporter.getMessage("msg.idswitch.too_many_arguments"));
            return -1;
        }
        (this.P = new CodePrinter()).setIndentStep(4);
        this.P.setIndentTabSize(0);
        try {
            this.process_file(array[0]);
            return 0;
        }
        catch (EvaluatorException ex2) {
            return -1;
        }
        catch (IOException ex) {
            this.print_error(ToolErrorReporter.getMessage("msg.idswitch.io_error", ex.toString()));
            return -1;
        }
    }
    
    private int extract_line_tag_id(final char[] array, int n, int n2) {
        final boolean b = false;
        final int skip_white_space = skip_white_space(array, n, n2);
        final int n3 = n = this.look_for_slash_slash(array, skip_white_space, n2);
        if (n3 != n2) {
            final boolean b2 = skip_white_space + 2 == n3;
            final int skip_white_space2 = skip_white_space(array, n3, n2);
            if ((n = skip_white_space2) != n2) {
                n = skip_white_space2;
                if (array[skip_white_space2] == '#') {
                    final int n4 = skip_white_space2 + 1;
                    final boolean b3 = false;
                    n = n4;
                    boolean b4 = b3;
                    if (n4 != n2) {
                        n = n4;
                        b4 = b3;
                        if (array[n4] == '/') {
                            n = n4 + 1;
                            b4 = true;
                        }
                    }
                    int i;
                    for (i = n; i != n2; ++i) {
                        final char c = array[i];
                        if (c == '#' || c == '=') {
                            break;
                        }
                        if (is_white_space(c)) {
                            break;
                        }
                    }
                    int n5 = b ? 1 : 0;
                    if (i == n2) {
                        return n5;
                    }
                    final int skip_white_space3 = skip_white_space(array, i, n2);
                    n5 = (b ? 1 : 0);
                    if (skip_white_space3 == n2) {
                        return n5;
                    }
                    final char c2 = array[skip_white_space3];
                    if (c2 != '=') {
                        n5 = (b ? 1 : 0);
                        if (c2 != '#') {
                            return n5;
                        }
                    }
                    final int get_tag_id = this.get_tag_id(array, n, i, b2);
                    if ((n5 = get_tag_id) == 0) {
                        return n5;
                    }
                    final String s = null;
                    final String s2 = null;
                    String s3;
                    if (c2 == '#') {
                        n = get_tag_id;
                        s3 = s2;
                        if (b4) {
                            n2 = (n = -get_tag_id);
                            s3 = s2;
                            if (is_value_type(n2)) {
                                s3 = "msg.idswitch.no_end_usage";
                                n = n2;
                            }
                        }
                        this.tag_definition_end = skip_white_space3 + 1;
                    }
                    else {
                        String s4;
                        if (b4) {
                            s4 = "msg.idswitch.no_end_with_value";
                        }
                        else {
                            s4 = s;
                            if (!is_value_type(get_tag_id)) {
                                s4 = "msg.idswitch.no_value_allowed";
                            }
                        }
                        n = this.extract_tag_value(array, skip_white_space3 + 1, n2, get_tag_id);
                        s3 = s4;
                    }
                    n5 = n;
                    if (s3 != null) {
                        throw this.R.runtimeError(ToolErrorReporter.getMessage(s3, tag_name(n)), this.source_file, this.body.getLineNumber(), null, 0);
                    }
                    return n5;
                }
            }
        }
        return b ? 1 : 0;
    }
    
    private int extract_tag_value(final char[] array, int tag_value_end, final int n, final int n2) {
        final boolean b = false;
        final int skip_white_space = skip_white_space(array, tag_value_end, n);
        boolean b2 = b;
        if (skip_white_space != n) {
            tag_value_end = skip_white_space;
            int skip_white_space2 = 0;
            Label_0111: {
                int n3;
                while (true) {
                    skip_white_space2 = tag_value_end;
                    n3 = skip_white_space;
                    if (tag_value_end == n) {
                        break;
                    }
                    final char c = array[tag_value_end];
                    if (is_white_space(c)) {
                        skip_white_space2 = skip_white_space(array, tag_value_end + 1, n);
                        if (skip_white_space2 != n && array[skip_white_space2] == '#') {
                            n3 = tag_value_end;
                            break;
                        }
                        tag_value_end = skip_white_space2 + 1;
                    }
                    else {
                        if (c == '#') {
                            skip_white_space2 = tag_value_end;
                            break Label_0111;
                        }
                        ++tag_value_end;
                    }
                }
                tag_value_end = n3;
            }
            b2 = b;
            if (skip_white_space2 != n) {
                b2 = true;
                this.tag_value_start = skip_white_space;
                this.tag_value_end = tag_value_end;
                this.tag_definition_end = skip_white_space2 + 1;
            }
        }
        if (b2) {
            return n2;
        }
        return 0;
    }
    
    private void generate_java_code() {
        this.P.clear();
        final IdValuePair[] array = new IdValuePair[this.all_pairs.size()];
        this.all_pairs.toArray(array);
        final SwitchGenerator switchGenerator = new SwitchGenerator();
        switchGenerator.char_tail_test_threshold = 2;
        switchGenerator.setReporter(this.R);
        switchGenerator.setCodePrinter(this.P);
        switchGenerator.generateSwitch(array, "0");
    }
    
    private int get_tag_id(final char[] array, final int n, final int n2, final boolean b) {
        if (b) {
            if (equals("string_id_map", array, n, n2)) {
                return 1;
            }
            if (equals("generated", array, n, n2)) {
                return 2;
            }
        }
        if (equals("string", array, n, n2)) {
            return 3;
        }
        return 0;
    }
    
    private String get_time_stamp() {
        return new SimpleDateFormat(" 'Last update:' yyyy-MM-dd HH:mm:ss z").format(new Date());
    }
    
    private static boolean is_value_type(final int n) {
        return n == 3;
    }
    
    private static boolean is_white_space(final int n) {
        return n == 32 || n == 9;
    }
    
    private void look_for_id_definitions(final char[] array, int n, int tag_value_start, final boolean b) {
        final int skip_white_space = skip_white_space(array, n, tag_value_start);
        n = skip_matched_prefix("Id_", array, skip_white_space, tag_value_start);
        if (n >= 0) {
            final int skip_name_char;
            final int n2 = skip_name_char = skip_name_char(array, n, tag_value_start);
            if (n != skip_name_char) {
                final int skip_white_space2 = skip_white_space(array, n2, tag_value_start);
                if (skip_white_space2 != tag_value_start && array[skip_white_space2] == '=') {
                    tag_value_start = n;
                    n = skip_name_char;
                    if (b) {
                        tag_value_start = this.tag_value_start;
                        n = this.tag_value_end;
                    }
                    this.add_id(array, skip_white_space, skip_name_char, tag_value_start, n);
                }
            }
        }
    }
    
    private int look_for_slash_slash(final char[] array, int n, final int n2) {
        while (n + 2 <= n2) {
            final int n3 = n + 1;
            if (array[n] == '/') {
                n = n3 + 1;
                if (array[n3] == '/') {
                    return n;
                }
                continue;
            }
            else {
                n = n3;
            }
        }
        return n2;
    }
    
    public static void main(final String[] array) {
        System.exit(new Main().exec(array));
    }
    
    private void option_error(final String s) {
        this.print_error(ToolErrorReporter.getMessage("msg.idswitch.bad_invocation", s));
    }
    
    private void print_error(final String s) {
        System.err.println(s);
    }
    
    private void process_file() {
        int n = 0;
        final char[] buffer = this.body.getBuffer();
        int n2 = -1;
        int n3 = -1;
        int n4 = -1;
        int n5 = -1;
        this.body.startLineLoop();
        while (this.body.nextLine()) {
            final int lineBegin = this.body.getLineBegin();
            int lineEnd = this.body.getLineEnd();
            final int extract_line_tag_id = this.extract_line_tag_id(buffer, lineBegin, lineEnd);
            final boolean b = false;
            int n6 = 0;
            int n7 = 0;
            int n8 = 0;
            int tag_definition_end = 0;
            boolean b2 = false;
            switch (n) {
                default: {
                    n6 = n;
                    n7 = n2;
                    n8 = n3;
                    tag_definition_end = n4;
                    lineEnd = n5;
                    b2 = b;
                    break;
                }
                case 2: {
                    if (extract_line_tag_id == 0) {
                        n6 = n;
                        n7 = n2;
                        n8 = n3;
                        tag_definition_end = n4;
                        lineEnd = n5;
                        b2 = b;
                        if (n2 < 0) {
                            n7 = lineBegin;
                            n6 = n;
                            n8 = n3;
                            tag_definition_end = n4;
                            lineEnd = n5;
                            b2 = b;
                            break;
                        }
                        break;
                    }
                    else {
                        if (extract_line_tag_id == -2) {
                            if ((n7 = n2) < 0) {
                                n7 = lineBegin;
                            }
                            n6 = 1;
                            n8 = lineBegin;
                            tag_definition_end = n4;
                            lineEnd = n5;
                            b2 = b;
                            break;
                        }
                        b2 = true;
                        n6 = n;
                        n7 = n2;
                        n8 = n3;
                        tag_definition_end = n4;
                        lineEnd = n5;
                        break;
                    }
                    break;
                }
                case 1: {
                    if (extract_line_tag_id == 0) {
                        this.look_for_id_definitions(buffer, lineBegin, lineEnd, false);
                        n6 = n;
                        n7 = n2;
                        n8 = n3;
                        tag_definition_end = n4;
                        lineEnd = n5;
                        b2 = b;
                        break;
                    }
                    if (extract_line_tag_id == 3) {
                        this.look_for_id_definitions(buffer, lineBegin, lineEnd, true);
                        n6 = n;
                        n7 = n2;
                        n8 = n3;
                        tag_definition_end = n4;
                        lineEnd = n5;
                        b2 = b;
                        break;
                    }
                    if (extract_line_tag_id == 2) {
                        if (n2 >= 0) {
                            b2 = true;
                            n6 = n;
                            n7 = n2;
                            n8 = n3;
                            tag_definition_end = n4;
                            lineEnd = n5;
                            break;
                        }
                        n6 = 2;
                        tag_definition_end = this.tag_definition_end;
                        n7 = n2;
                        n8 = n3;
                        b2 = b;
                        break;
                    }
                    else {
                        if (extract_line_tag_id != -1) {
                            b2 = true;
                            n6 = n;
                            n7 = n2;
                            n8 = n3;
                            tag_definition_end = n4;
                            lineEnd = n5;
                            break;
                        }
                        final int n9 = n6 = 0;
                        n7 = n2;
                        n8 = n3;
                        tag_definition_end = n4;
                        lineEnd = n5;
                        b2 = b;
                        if (n2 < 0) {
                            break;
                        }
                        n6 = n9;
                        n7 = n2;
                        n8 = n3;
                        tag_definition_end = n4;
                        lineEnd = n5;
                        b2 = b;
                        if (!this.all_pairs.isEmpty()) {
                            this.generate_java_code();
                            if (this.body.setReplacement(n2, n3, this.P.toString())) {
                                this.body.setReplacement(n4, n5, this.get_time_stamp());
                            }
                            n6 = n9;
                            n7 = n2;
                            n8 = n3;
                            tag_definition_end = n4;
                            lineEnd = n5;
                            b2 = b;
                            break;
                        }
                        break;
                    }
                    break;
                }
                case 0: {
                    if (extract_line_tag_id == 1) {
                        n6 = 1;
                        this.all_pairs.clear();
                        n7 = -1;
                        n8 = n3;
                        tag_definition_end = n4;
                        lineEnd = n5;
                        b2 = b;
                        break;
                    }
                    n6 = n;
                    n7 = n2;
                    n8 = n3;
                    tag_definition_end = n4;
                    lineEnd = n5;
                    b2 = b;
                    if (extract_line_tag_id == -1) {
                        b2 = true;
                        lineEnd = n5;
                        tag_definition_end = n4;
                        n8 = n3;
                        n7 = n2;
                        n6 = n;
                        break;
                    }
                    break;
                }
            }
            if (b2) {
                throw this.R.runtimeError(ToolErrorReporter.getMessage("msg.idswitch.bad_tag_order", tag_name(extract_line_tag_id)), this.source_file, this.body.getLineNumber(), null, 0);
            }
            n = n6;
            n2 = n7;
            n3 = n8;
            n4 = tag_definition_end;
            n5 = lineEnd;
        }
        if (n != 0) {
            throw this.R.runtimeError(ToolErrorReporter.getMessage("msg.idswitch.file_end_in_switch", tag_name(n)), this.source_file, this.body.getLineNumber(), null, 0);
        }
    }
    
    private int process_options(final String[] array) {
        final boolean b = true;
        final int length = array.length;
        int n = 0;
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
    Label_0245:
        while (true) {
            n4 = (b ? 1 : 0);
            n5 = n2;
            if (n3 == length) {
                break;
            }
            final String s = array[n3];
            final int length2 = s.length();
            int n6 = n2;
            int n7 = n;
            if (length2 >= 2) {
                n6 = n2;
                n7 = n;
                if (s.charAt(0) == '-') {
                    if (s.charAt(1) == '-') {
                        if (length2 == 2) {
                            array[n3] = null;
                            n4 = (b ? 1 : 0);
                            n5 = n2;
                            break;
                        }
                        if (s.equals("--help")) {
                            n2 = 1;
                        }
                        else {
                            if (!s.equals("--version")) {
                                this.option_error(ToolErrorReporter.getMessage("msg.idswitch.bad_option", s));
                                n4 = -1;
                                n5 = n2;
                                break;
                            }
                            n = 1;
                        }
                    }
                    else {
                        for (int i = 1; i != length2; ++i) {
                            final char char1 = s.charAt(i);
                            if (char1 != 'h') {
                                this.option_error(ToolErrorReporter.getMessage("msg.idswitch.bad_option_char", String.valueOf(char1)));
                                n4 = -1;
                                n5 = n2;
                                break Label_0245;
                            }
                            n2 = 1;
                        }
                    }
                    array[n3] = null;
                    n7 = n;
                    n6 = n2;
                }
            }
            ++n3;
            n2 = n6;
            n = n7;
        }
        int n8;
        if ((n8 = n4) == 1) {
            n8 = n4;
            if (n5 != 0) {
                this.show_usage();
                n8 = 0;
            }
            if (n != 0) {
                this.show_version();
                n8 = 0;
            }
        }
        if (n8 != 1) {
            System.exit(n8);
        }
        return this.remove_nulls(array);
    }
    
    private int remove_nulls(final String[] array) {
        int length;
        int n;
        for (length = array.length, n = 0; n != length && array[n] != null; ++n) {}
        int n3;
        int n2 = n3 = n;
        if (n != length) {
            int n4 = n + 1;
            while (true) {
                n3 = n2;
                if (n4 == length) {
                    break;
                }
                final String s = array[n4];
                int n5 = n2;
                if (s != null) {
                    array[n2] = s;
                    n5 = n2 + 1;
                }
                ++n4;
                n2 = n5;
            }
        }
        return n3;
    }
    
    private void show_usage() {
        System.out.println(ToolErrorReporter.getMessage("msg.idswitch.usage"));
        System.out.println();
    }
    
    private void show_version() {
        System.out.println(ToolErrorReporter.getMessage("msg.idswitch.version"));
    }
    
    private static int skip_matched_prefix(final String s, final char[] array, int n, int n2) {
        int n3 = -1;
        final int length = s.length();
        if (length <= n2 - n) {
            n2 = 0;
            while (true) {
                n3 = n;
                if (n2 == length) {
                    break;
                }
                if (s.charAt(n2) != array[n]) {
                    return -1;
                }
                ++n2;
                ++n;
            }
        }
        return n3;
    }
    
    private static int skip_name_char(final char[] array, int i, final int n) {
        while (i != n) {
            final char c = array[i];
            if (('a' > c || c > 'z') && ('A' > c || c > 'Z') && ('0' > c || c > '9') && c != '_') {
                return i;
            }
            ++i;
        }
        return i;
    }
    
    private static int skip_white_space(final char[] array, int i, final int n) {
        while (i != n) {
            if (!is_white_space(array[i])) {
                return i;
            }
            ++i;
        }
        return i;
    }
    
    private static String tag_name(final int n) {
        switch (n) {
            default: {
                return "";
            }
            case 2: {
                return "generated";
            }
            case 1: {
                return "string_id_map";
            }
            case -1: {
                return "/string_id_map";
            }
            case -2: {
                return "/generated";
            }
        }
    }
    
    void process_file(String out) throws IOException {
        this.source_file = (String)out;
        this.body = new FileBody();
        InputStream in;
        if (((String)out).equals("-")) {
            in = System.in;
        }
        else {
            in = new FileInputStream((String)out);
        }
        try {
            this.body.readData(new InputStreamReader(in, "ASCII"));
            in.close();
            this.process_file();
            if (this.body.wasModified()) {
                if (((String)out).equals("-")) {
                    out = System.out;
                }
                else {
                    out = new FileOutputStream((String)out);
                }
                try {
                    final OutputStreamWriter outputStreamWriter = new OutputStreamWriter((OutputStream)out);
                    this.body.writeData(outputStreamWriter);
                    outputStreamWriter.flush();
                }
                finally {
                    ((OutputStream)out).close();
                }
            }
        }
        finally {
            in.close();
        }
    }
}
