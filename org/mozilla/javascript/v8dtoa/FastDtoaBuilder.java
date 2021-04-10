package org.mozilla.javascript.v8dtoa;

import java.util.*;

public class FastDtoaBuilder
{
    static final char[] digits;
    final char[] chars;
    int end;
    boolean formatted;
    int point;
    
    static {
        digits = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
    }
    
    public FastDtoaBuilder() {
        this.chars = new char[25];
        this.end = 0;
        this.formatted = false;
    }
    
    private void toExponentialFormat(int end, int n) {
        if (this.end - end > 1) {
            ++end;
            System.arraycopy(this.chars, end, this.chars, end + 1, this.end - end);
            this.chars[end] = '.';
            ++this.end;
        }
        final char[] chars = this.chars;
        end = this.end++;
        chars[end] = 'e';
        char c = '+';
        end = n - 1;
        if ((n = end) < 0) {
            c = '-';
            n = -end;
        }
        final char[] chars2 = this.chars;
        end = this.end++;
        chars2[end] = c;
        if (n > 99) {
            end = this.end + 2;
        }
        else if (n > 9) {
            end = 1 + this.end;
        }
        else {
            end = this.end;
        }
        this.end = end + 1;
        while (true) {
            this.chars[end] = FastDtoaBuilder.digits[n % 10];
            n /= 10;
            if (n == 0) {
                break;
            }
            --end;
        }
    }
    
    private void toFixedFormat(final int n, final int n2) {
        if (this.point >= this.end) {
            if (this.point > this.end) {
                Arrays.fill(this.chars, this.end, this.point, '0');
                this.end += this.point - this.end;
            }
            return;
        }
        if (n2 > 0) {
            System.arraycopy(this.chars, this.point, this.chars, this.point + 1, this.end - this.point);
            this.chars[this.point] = '.';
            ++this.end;
            return;
        }
        final int n3 = n + 2 - n2;
        System.arraycopy(this.chars, n, this.chars, n3, this.end - n);
        this.chars[n] = '0';
        this.chars[n + 1] = '.';
        if (n2 < 0) {
            Arrays.fill(this.chars, n + 2, n3, '0');
        }
        this.end += 2 - n2;
    }
    
    void append(final char c) {
        this.chars[this.end++] = c;
    }
    
    void decreaseLast() {
        final char[] chars = this.chars;
        final int n = this.end - 1;
        --chars[n];
    }
    
    public String format() {
        if (!this.formatted) {
            int n;
            if (this.chars[0] == '-') {
                n = 1;
            }
            else {
                n = 0;
            }
            final int n2 = this.point - n;
            if (n2 >= -5 && n2 <= 21) {
                this.toFixedFormat(n, n2);
            }
            else {
                this.toExponentialFormat(n, n2);
            }
            this.formatted = true;
        }
        return new String(this.chars, 0, this.end);
    }
    
    public void reset() {
        this.end = 0;
        this.formatted = false;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[chars:");
        sb.append(new String(this.chars, 0, this.end));
        sb.append(", point:");
        sb.append(this.point);
        sb.append("]");
        return sb.toString();
    }
}
