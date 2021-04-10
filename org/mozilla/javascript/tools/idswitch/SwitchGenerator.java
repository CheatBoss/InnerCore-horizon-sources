package org.mozilla.javascript.tools.idswitch;

import org.mozilla.javascript.tools.*;
import org.mozilla.javascript.*;

public class SwitchGenerator
{
    private CodePrinter P;
    private ToolErrorReporter R;
    private boolean c_was_defined;
    int char_tail_test_threshold;
    private int[] columns;
    private String default_value;
    private IdValuePair[] pairs;
    private String source_file;
    int use_if_threshold;
    String v_c;
    String v_guess;
    String v_id;
    String v_label;
    String v_length_suffix;
    String v_s;
    String v_switch_label;
    
    public SwitchGenerator() {
        this.v_switch_label = "L0";
        this.v_label = "L";
        this.v_s = "s";
        this.v_c = "c";
        this.v_guess = "X";
        this.v_id = "id";
        this.v_length_suffix = "_length";
        this.use_if_threshold = 3;
        this.char_tail_test_threshold = 2;
    }
    
    private static boolean bigger(final IdValuePair idValuePair, final IdValuePair idValuePair2, int n) {
        final boolean b = false;
        final boolean b2 = false;
        boolean b3 = false;
        if (n >= 0) {
            boolean b4 = b2;
            if (idValuePair.id.charAt(n) > idValuePair2.id.charAt(n)) {
                b4 = true;
            }
            return b4;
        }
        n = idValuePair.idLength - idValuePair2.idLength;
        if (n != 0) {
            if (n > 0) {
                b3 = true;
            }
            return b3;
        }
        boolean b5 = b;
        if (idValuePair.id.compareTo(idValuePair2.id) > 0) {
            b5 = true;
        }
        return b5;
    }
    
    private void check_all_is_different(int n, final int n2) {
        if (n != n2) {
            IdValuePair idValuePair = this.pairs[n];
            while (true) {
                ++n;
                if (n == n2) {
                    break;
                }
                final IdValuePair idValuePair2 = this.pairs[n];
                if (idValuePair.id.equals(idValuePair2.id)) {
                    throw this.on_same_pair_fail(idValuePair, idValuePair2);
                }
                idValuePair = idValuePair2;
            }
        }
    }
    
    private int count_different_chars(int i, final int n, final int n2) {
        int n3 = 0;
        int n4 = -1;
        while (i != n) {
            final char char1 = this.pairs[i].id.charAt(n2);
            int n5 = n3;
            int n6;
            if (char1 != (n6 = n4)) {
                n5 = n3 + 1;
                n6 = char1;
            }
            ++i;
            n3 = n5;
            n4 = n6;
        }
        return n3;
    }
    
    private int count_different_lengths(int i, final int n) {
        int n2 = 0;
        int n3 = -1;
        while (i != n) {
            final int idLength = this.pairs[i].idLength;
            int n4 = n2;
            int n5;
            if ((n5 = n3) != idLength) {
                n4 = n2 + 1;
                n5 = idLength;
            }
            ++i;
            n2 = n4;
            n3 = n5;
        }
        return n2;
    }
    
    private int find_max_different_column(final int n, final int n2, final int n3) {
        int n4 = 0;
        int n5 = 0;
        int n7;
        for (int i = 0; i != n3; ++i, n4 = n7) {
            final int n6 = this.columns[i];
            this.sort_pairs(n, n2, n6);
            final int count_different_chars = this.count_different_chars(n, n2, n6);
            if (count_different_chars == n2 - n) {
                return i;
            }
            if ((n7 = n4) < count_different_chars) {
                n7 = count_different_chars;
                n5 = i;
            }
        }
        if (n5 != n3 - 1) {
            this.sort_pairs(n, n2, this.columns[n5]);
        }
        return n5;
    }
    
    private void generate_body(final int n, final int n2, final int n3) {
        this.P.indent(n3);
        this.P.p(this.v_switch_label);
        this.P.p(": { ");
        this.P.p(this.v_id);
        this.P.p(" = ");
        this.P.p(this.default_value);
        this.P.p("; String ");
        this.P.p(this.v_guess);
        this.P.p(" = null;");
        this.c_was_defined = false;
        final int offset = this.P.getOffset();
        this.P.p(" int ");
        this.P.p(this.v_c);
        this.P.p(';');
        final int offset2 = this.P.getOffset();
        this.P.nl();
        this.generate_length_switch(n, n2, n3 + 1);
        if (!this.c_was_defined) {
            this.P.erase(offset, offset2);
        }
        this.P.indent(n3 + 1);
        this.P.p("if (");
        this.P.p(this.v_guess);
        this.P.p("!=null && ");
        this.P.p(this.v_guess);
        this.P.p("!=");
        this.P.p(this.v_s);
        this.P.p(" && !");
        this.P.p(this.v_guess);
        this.P.p(".equals(");
        this.P.p(this.v_s);
        this.P.p(")) ");
        this.P.p(this.v_id);
        this.P.p(" = ");
        this.P.p(this.default_value);
        this.P.p(";");
        this.P.nl();
        this.P.indent(n3 + 1);
        this.P.p("break ");
        this.P.p(this.v_switch_label);
        this.P.p(";");
        this.P.nl();
        this.P.line(n3, "}");
    }
    
    private void generate_length_switch(final int n, final int n2, final int n3) {
        this.sort_pairs(n, n2, -1);
        this.check_all_is_different(n, n2);
        final int count_different_lengths = this.count_different_lengths(n, n2);
        this.columns = new int[this.pairs[n2 - 1].idLength];
        boolean b2;
        if (count_different_lengths <= this.use_if_threshold) {
            final boolean b = b2 = true;
            if (count_different_lengths != 1) {
                this.P.indent(n3);
                this.P.p("int ");
                this.P.p(this.v_s);
                this.P.p(this.v_length_suffix);
                this.P.p(" = ");
                this.P.p(this.v_s);
                this.P.p(".length();");
                this.P.nl();
                b2 = b;
            }
        }
        else {
            b2 = false;
            this.P.indent(n3);
            this.P.p(this.v_label);
            this.P.p(": switch (");
            this.P.p(this.v_s);
            this.P.p(".length()) {");
            this.P.nl();
        }
        int n4 = n;
        int idLength = this.pairs[n].idLength;
        int idLength2 = 0;
        int n5 = n4;
        while (true) {
            ++n4;
            if (n4 != n2 && (idLength2 = this.pairs[n4].idLength) == idLength) {
                continue;
            }
            int n6;
            if (b2) {
                this.P.indent(n3);
                if (n5 != n) {
                    this.P.p("else ");
                }
                this.P.p("if (");
                if (count_different_lengths == 1) {
                    this.P.p(this.v_s);
                    this.P.p(".length()==");
                }
                else {
                    this.P.p(this.v_s);
                    this.P.p(this.v_length_suffix);
                    this.P.p("==");
                }
                this.P.p(idLength);
                this.P.p(") {");
                n6 = n3 + 1;
            }
            else {
                this.P.indent(n3);
                this.P.p("case ");
                this.P.p(idLength);
                this.P.p(":");
                n6 = n3 + 1;
            }
            this.generate_letter_switch(n5, n4, n6, !b2, b2);
            if (b2) {
                this.P.p("}");
                this.P.nl();
            }
            else {
                this.P.p("break ");
                this.P.p(this.v_label);
                this.P.p(";");
                this.P.nl();
            }
            if (n4 == n2) {
                break;
            }
            n5 = n4;
            idLength = idLength2;
        }
        if (!b2) {
            this.P.indent(n3);
            this.P.p("}");
            this.P.nl();
        }
    }
    
    private void generate_letter_switch(final int n, final int n2, final int n3, final boolean b, final boolean b2) {
        final int idLength = this.pairs[n].idLength;
        for (int i = 0; i != idLength; ++i) {
            this.columns[i] = i;
        }
        this.generate_letter_switch_r(n, n2, idLength, n3, b, b2);
    }
    
    private boolean generate_letter_switch_r(int i, int n, final int n2, final int n3, boolean b, final boolean b2) {
        final boolean b3 = false;
        final int n4 = 1;
        if (i + 1 == n) {
            this.P.p(' ');
            final IdValuePair idValuePair = this.pairs[i];
            boolean b4;
            if (n2 > this.char_tail_test_threshold) {
                this.P.p(this.v_guess);
                this.P.p("=");
                this.P.qstring(idValuePair.id);
                this.P.p(";");
                this.P.p(this.v_id);
                this.P.p("=");
                this.P.p(idValuePair.value);
                this.P.p(";");
                b4 = b3;
            }
            else if (n2 == 0) {
                b4 = true;
                this.P.p(this.v_id);
                this.P.p("=");
                this.P.p(idValuePair.value);
                this.P.p("; break ");
                this.P.p(this.v_switch_label);
                this.P.p(";");
            }
            else {
                this.P.p("if (");
                i = this.columns[0];
                this.P.p(this.v_s);
                this.P.p(".charAt(");
                this.P.p(i);
                this.P.p(")==");
                this.P.qchar(idValuePair.id.charAt(i));
                for (i = n4; i != n2; ++i) {
                    this.P.p(" && ");
                    n = this.columns[i];
                    this.P.p(this.v_s);
                    this.P.p(".charAt(");
                    this.P.p(n);
                    this.P.p(")==");
                    this.P.qchar(idValuePair.id.charAt(n));
                }
                this.P.p(") {");
                this.P.p(this.v_id);
                this.P.p("=");
                this.P.p(idValuePair.value);
                this.P.p("; break ");
                this.P.p(this.v_switch_label);
                this.P.p(";}");
                b4 = b3;
            }
            this.P.p(' ');
            return b4;
        }
        final int find_max_different_column = this.find_max_different_column(i, n, n2);
        final int n5 = this.columns[find_max_different_column];
        final int count_different_chars = this.count_different_chars(i, n, n5);
        this.columns[find_max_different_column] = this.columns[n2 - 1];
        if (b2) {
            this.P.nl();
            this.P.indent(n3);
        }
        else {
            this.P.p(' ');
        }
        boolean b5;
        if (count_different_chars <= this.use_if_threshold) {
            this.c_was_defined = true;
            this.P.p(this.v_c);
            this.P.p("=");
            this.P.p(this.v_s);
            this.P.p(".charAt(");
            this.P.p(n5);
            this.P.p(");");
            b5 = true;
        }
        else {
            if (!b) {
                b = true;
                this.P.p(this.v_label);
                this.P.p(": ");
            }
            this.P.p("switch (");
            this.P.p(this.v_s);
            this.P.p(".charAt(");
            this.P.p(n5);
            this.P.p(")) {");
            b5 = false;
        }
        int n6 = i;
        int char1 = this.pairs[i].id.charAt(n5);
        int n7 = '\0';
        int n8 = n6;
        while (true) {
            final int n9 = n6 + 1;
            if (n9 != n) {
                final char char2;
                final char c = char2 = this.pairs[n9].id.charAt(n5);
                n6 = n9;
                n7 = char2;
                if (c == char1) {
                    continue;
                }
                n7 = char2;
            }
            int n10;
            if (b5) {
                this.P.nl();
                this.P.indent(n3);
                if (n8 != i) {
                    this.P.p("else ");
                }
                this.P.p("if (");
                this.P.p(this.v_c);
                this.P.p("==");
                this.P.qchar(char1);
                this.P.p(") {");
                n10 = n3 + 1;
            }
            else {
                this.P.nl();
                this.P.indent(n3);
                this.P.p("case ");
                this.P.qchar(char1);
                this.P.p(":");
                n10 = n3 + 1;
            }
            final boolean generate_letter_switch_r = this.generate_letter_switch_r(n8, n9, n2 - 1, n10, b, b5);
            if (b5) {
                this.P.p("}");
            }
            else if (!generate_letter_switch_r) {
                this.P.p("break ");
                this.P.p(this.v_label);
                this.P.p(";");
            }
            final int n11 = n9;
            if (n11 == n) {
                break;
            }
            char1 = n7;
            n6 = n11;
            n8 = n11;
        }
        if (b5) {
            this.P.nl();
            if (b2) {
                this.P.indent(n3 - 1);
            }
            else {
                this.P.indent(n3);
            }
        }
        else {
            this.P.nl();
            this.P.indent(n3);
            this.P.p("}");
            if (b2) {
                this.P.nl();
                this.P.indent(n3 - 1);
            }
            else {
                this.P.p(' ');
            }
        }
        this.columns[find_max_different_column] = n5;
        return false;
    }
    
    private static void heap4Sort(final IdValuePair[] array, final int n, int i, final int n2) {
        if (i <= 1) {
            return;
        }
        makeHeap4(array, n, i, n2);
        while (i > 1) {
            --i;
            final IdValuePair idValuePair = array[n + i];
            array[n + i] = array[n + 0];
            array[n + 0] = idValuePair;
            heapify4(array, n, i, 0, n2);
        }
    }
    
    private static void heapify4(final IdValuePair[] array, final int n, final int n2, int n3, final int n4) {
        final IdValuePair idValuePair = array[n + n3];
        int n5 = n3;
        while (true) {
            final int n6 = n5 << 2;
            n3 = (n6 | 0x1);
            final int n7 = n6 | 0x2;
            final int n8 = n6 | 0x3;
            final int n9 = n6 + 4;
            if (n9 >= n2) {
                if (n3 < n2) {
                    IdValuePair idValuePair3;
                    final IdValuePair idValuePair2 = idValuePair3 = array[n + n3];
                    int n10 = n3;
                    if (n7 != n2) {
                        final IdValuePair idValuePair4 = array[n + n7];
                        IdValuePair idValuePair5 = idValuePair2;
                        if (bigger(idValuePair4, idValuePair2, n4)) {
                            idValuePair5 = idValuePair4;
                            n3 = n7;
                        }
                        idValuePair3 = idValuePair5;
                        n10 = n3;
                        if (n8 != n2) {
                            final IdValuePair idValuePair6 = array[n + n8];
                            idValuePair3 = idValuePair5;
                            n10 = n3;
                            if (bigger(idValuePair6, idValuePair5, n4)) {
                                idValuePair3 = idValuePair6;
                                n10 = n8;
                            }
                        }
                    }
                    if (bigger(idValuePair3, idValuePair, n4)) {
                        array[n + n5] = idValuePair3;
                        array[n + n10] = idValuePair;
                    }
                }
                return;
            }
            final IdValuePair idValuePair7 = array[n + n3];
            final IdValuePair idValuePair8 = array[n + n7];
            final IdValuePair idValuePair9 = array[n + n8];
            final IdValuePair idValuePair10 = array[n + n9];
            IdValuePair idValuePair11 = idValuePair7;
            if (bigger(idValuePair8, idValuePair7, n4)) {
                idValuePair11 = idValuePair8;
                n3 = n7;
            }
            int n11 = n8;
            IdValuePair idValuePair12 = idValuePair9;
            if (bigger(idValuePair10, idValuePair9, n4)) {
                idValuePair12 = idValuePair10;
                n11 = n9;
            }
            IdValuePair idValuePair13 = idValuePair11;
            if (bigger(idValuePair12, idValuePair11, n4)) {
                n3 = n11;
                idValuePair13 = idValuePair12;
            }
            if (bigger(idValuePair, idValuePair13, n4)) {
                return;
            }
            array[n + n5] = idValuePair13;
            array[n + n3] = idValuePair;
            n5 = n3;
        }
    }
    
    private static void makeHeap4(final IdValuePair[] array, final int n, final int n2, final int n3) {
        int i = n2 + 2 >> 2;
        while (i != 0) {
            --i;
            heapify4(array, n, n2, i, n3);
        }
    }
    
    private EvaluatorException on_same_pair_fail(final IdValuePair idValuePair, final IdValuePair idValuePair2) {
        final int lineNumber = idValuePair.getLineNumber();
        final int lineNumber2 = idValuePair2.getLineNumber();
        int n = lineNumber;
        int n2 = lineNumber2;
        if (lineNumber2 > lineNumber) {
            n2 = lineNumber;
            n = lineNumber2;
        }
        return this.R.runtimeError(ToolErrorReporter.getMessage("msg.idswitch.same_string", idValuePair.id, new Integer(n2)), this.source_file, n, null, 0);
    }
    
    private void sort_pairs(final int n, final int n2, final int n3) {
        heap4Sort(this.pairs, n, n2 - n, n3);
    }
    
    public void generateSwitch(final String[] array, final String s) {
        final int n = array.length / 2;
        final IdValuePair[] array2 = new IdValuePair[n];
        for (int i = 0; i != n; ++i) {
            array2[i] = new IdValuePair(array[i * 2], array[i * 2 + 1]);
        }
        this.generateSwitch(array2, s);
    }
    
    public void generateSwitch(final IdValuePair[] pairs, final String default_value) {
        final int length = pairs.length;
        if (length == 0) {
            return;
        }
        this.pairs = pairs;
        this.default_value = default_value;
        this.generate_body(0, length, 2);
    }
    
    public CodePrinter getCodePrinter() {
        return this.P;
    }
    
    public ToolErrorReporter getReporter() {
        return this.R;
    }
    
    public String getSourceFileName() {
        return this.source_file;
    }
    
    public void setCodePrinter(final CodePrinter p) {
        this.P = p;
    }
    
    public void setReporter(final ToolErrorReporter r) {
        this.R = r;
    }
    
    public void setSourceFileName(final String source_file) {
        this.source_file = source_file;
    }
}
