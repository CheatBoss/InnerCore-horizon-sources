package com.android.dx.rop.code;

import com.android.dx.util.*;

public final class RegOps
{
    public static final int ADD = 14;
    public static final int AGET = 38;
    public static final int AND = 20;
    public static final int APUT = 39;
    public static final int ARRAY_LENGTH = 34;
    public static final int CHECK_CAST = 43;
    public static final int CMPG = 28;
    public static final int CMPL = 27;
    public static final int CONST = 5;
    public static final int CONV = 29;
    public static final int DIV = 17;
    public static final int FILLED_NEW_ARRAY = 42;
    public static final int FILL_ARRAY_DATA = 57;
    public static final int GET_FIELD = 45;
    public static final int GET_STATIC = 46;
    public static final int GOTO = 6;
    public static final int IF_EQ = 7;
    public static final int IF_GE = 10;
    public static final int IF_GT = 12;
    public static final int IF_LE = 11;
    public static final int IF_LT = 9;
    public static final int IF_NE = 8;
    public static final int INSTANCE_OF = 44;
    public static final int INVOKE_DIRECT = 52;
    public static final int INVOKE_INTERFACE = 53;
    public static final int INVOKE_STATIC = 49;
    public static final int INVOKE_SUPER = 51;
    public static final int INVOKE_VIRTUAL = 50;
    public static final int MARK_LOCAL = 54;
    public static final int MONITOR_ENTER = 36;
    public static final int MONITOR_EXIT = 37;
    public static final int MOVE = 2;
    public static final int MOVE_EXCEPTION = 4;
    public static final int MOVE_PARAM = 3;
    public static final int MOVE_RESULT = 55;
    public static final int MOVE_RESULT_PSEUDO = 56;
    public static final int MUL = 16;
    public static final int NEG = 19;
    public static final int NEW_ARRAY = 41;
    public static final int NEW_INSTANCE = 40;
    public static final int NOP = 1;
    public static final int NOT = 26;
    public static final int OR = 21;
    public static final int PUT_FIELD = 47;
    public static final int PUT_STATIC = 48;
    public static final int REM = 18;
    public static final int RETURN = 33;
    public static final int SHL = 23;
    public static final int SHR = 24;
    public static final int SUB = 15;
    public static final int SWITCH = 13;
    public static final int THROW = 35;
    public static final int TO_BYTE = 30;
    public static final int TO_CHAR = 31;
    public static final int TO_SHORT = 32;
    public static final int USHR = 25;
    public static final int XOR = 22;
    
    private RegOps() {
    }
    
    public static int flippedIfOpcode(final int n) {
        switch (n) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unrecognized IF regop: ");
                sb.append(n);
                throw new RuntimeException(sb.toString());
            }
            case 12: {
                return 9;
            }
            case 11: {
                return 10;
            }
            case 10: {
                return 11;
            }
            case 9: {
                return 12;
            }
            case 7:
            case 8: {
                return n;
            }
        }
    }
    
    public static String opName(final int n) {
        switch (n) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("unknown-");
                sb.append(Hex.u1(n));
                return sb.toString();
            }
            case 57: {
                return "fill-array-data";
            }
            case 56: {
                return "move-result-pseudo";
            }
            case 55: {
                return "move-result";
            }
            case 53: {
                return "invoke-interface";
            }
            case 52: {
                return "invoke-direct";
            }
            case 51: {
                return "invoke-super";
            }
            case 50: {
                return "invoke-virtual";
            }
            case 49: {
                return "invoke-static";
            }
            case 48: {
                return "put-static";
            }
            case 47: {
                return "put-field";
            }
            case 46: {
                return "get-static";
            }
            case 45: {
                return "get-field";
            }
            case 44: {
                return "instance-of";
            }
            case 43: {
                return "check-cast";
            }
            case 42: {
                return "filled-new-array";
            }
            case 41: {
                return "new-array";
            }
            case 40: {
                return "new-instance";
            }
            case 39: {
                return "aput";
            }
            case 38: {
                return "aget";
            }
            case 37: {
                return "monitor-exit";
            }
            case 36: {
                return "monitor-enter";
            }
            case 35: {
                return "throw";
            }
            case 34: {
                return "array-length";
            }
            case 33: {
                return "return";
            }
            case 32: {
                return "to-short";
            }
            case 31: {
                return "to-char";
            }
            case 30: {
                return "to-byte";
            }
            case 29: {
                return "conv";
            }
            case 28: {
                return "cmpg";
            }
            case 27: {
                return "cmpl";
            }
            case 26: {
                return "not";
            }
            case 25: {
                return "ushr";
            }
            case 24: {
                return "shr";
            }
            case 23: {
                return "shl";
            }
            case 22: {
                return "xor";
            }
            case 21: {
                return "or";
            }
            case 20: {
                return "and";
            }
            case 19: {
                return "neg";
            }
            case 18: {
                return "rem";
            }
            case 17: {
                return "div";
            }
            case 16: {
                return "mul";
            }
            case 15: {
                return "sub";
            }
            case 14: {
                return "add";
            }
            case 13: {
                return "switch";
            }
            case 12: {
                return "if-gt";
            }
            case 11: {
                return "if-le";
            }
            case 10: {
                return "if-ge";
            }
            case 9: {
                return "if-lt";
            }
            case 8: {
                return "if-ne";
            }
            case 7: {
                return "if-eq";
            }
            case 6: {
                return "goto";
            }
            case 5: {
                return "const";
            }
            case 4: {
                return "move-exception";
            }
            case 3: {
                return "move-param";
            }
            case 2: {
                return "move";
            }
            case 1: {
                return "nop";
            }
        }
    }
}
