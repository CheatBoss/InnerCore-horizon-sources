package com.android.dx.rop.code;

import com.android.dx.rop.type.*;
import com.android.dx.rop.cst.*;

public final class Rops
{
    public static final Rop ADD_CONST_DOUBLE;
    public static final Rop ADD_CONST_FLOAT;
    public static final Rop ADD_CONST_INT;
    public static final Rop ADD_CONST_LONG;
    public static final Rop ADD_DOUBLE;
    public static final Rop ADD_FLOAT;
    public static final Rop ADD_INT;
    public static final Rop ADD_LONG;
    public static final Rop AGET_BOOLEAN;
    public static final Rop AGET_BYTE;
    public static final Rop AGET_CHAR;
    public static final Rop AGET_DOUBLE;
    public static final Rop AGET_FLOAT;
    public static final Rop AGET_INT;
    public static final Rop AGET_LONG;
    public static final Rop AGET_OBJECT;
    public static final Rop AGET_SHORT;
    public static final Rop AND_CONST_INT;
    public static final Rop AND_CONST_LONG;
    public static final Rop AND_INT;
    public static final Rop AND_LONG;
    public static final Rop APUT_BOOLEAN;
    public static final Rop APUT_BYTE;
    public static final Rop APUT_CHAR;
    public static final Rop APUT_DOUBLE;
    public static final Rop APUT_FLOAT;
    public static final Rop APUT_INT;
    public static final Rop APUT_LONG;
    public static final Rop APUT_OBJECT;
    public static final Rop APUT_SHORT;
    public static final Rop ARRAY_LENGTH;
    public static final Rop CHECK_CAST;
    public static final Rop CMPG_DOUBLE;
    public static final Rop CMPG_FLOAT;
    public static final Rop CMPL_DOUBLE;
    public static final Rop CMPL_FLOAT;
    public static final Rop CMPL_LONG;
    public static final Rop CONST_DOUBLE;
    public static final Rop CONST_FLOAT;
    public static final Rop CONST_INT;
    public static final Rop CONST_LONG;
    public static final Rop CONST_OBJECT;
    public static final Rop CONST_OBJECT_NOTHROW;
    public static final Rop CONV_D2F;
    public static final Rop CONV_D2I;
    public static final Rop CONV_D2L;
    public static final Rop CONV_F2D;
    public static final Rop CONV_F2I;
    public static final Rop CONV_F2L;
    public static final Rop CONV_I2D;
    public static final Rop CONV_I2F;
    public static final Rop CONV_I2L;
    public static final Rop CONV_L2D;
    public static final Rop CONV_L2F;
    public static final Rop CONV_L2I;
    public static final Rop DIV_CONST_DOUBLE;
    public static final Rop DIV_CONST_FLOAT;
    public static final Rop DIV_CONST_INT;
    public static final Rop DIV_CONST_LONG;
    public static final Rop DIV_DOUBLE;
    public static final Rop DIV_FLOAT;
    public static final Rop DIV_INT;
    public static final Rop DIV_LONG;
    public static final Rop FILL_ARRAY_DATA;
    public static final Rop GET_FIELD_BOOLEAN;
    public static final Rop GET_FIELD_BYTE;
    public static final Rop GET_FIELD_CHAR;
    public static final Rop GET_FIELD_DOUBLE;
    public static final Rop GET_FIELD_FLOAT;
    public static final Rop GET_FIELD_INT;
    public static final Rop GET_FIELD_LONG;
    public static final Rop GET_FIELD_OBJECT;
    public static final Rop GET_FIELD_SHORT;
    public static final Rop GET_STATIC_BOOLEAN;
    public static final Rop GET_STATIC_BYTE;
    public static final Rop GET_STATIC_CHAR;
    public static final Rop GET_STATIC_DOUBLE;
    public static final Rop GET_STATIC_FLOAT;
    public static final Rop GET_STATIC_INT;
    public static final Rop GET_STATIC_LONG;
    public static final Rop GET_STATIC_OBJECT;
    public static final Rop GET_STATIC_SHORT;
    public static final Rop GOTO;
    public static final Rop IF_EQZ_INT;
    public static final Rop IF_EQZ_OBJECT;
    public static final Rop IF_EQ_INT;
    public static final Rop IF_EQ_OBJECT;
    public static final Rop IF_GEZ_INT;
    public static final Rop IF_GE_INT;
    public static final Rop IF_GTZ_INT;
    public static final Rop IF_GT_INT;
    public static final Rop IF_LEZ_INT;
    public static final Rop IF_LE_INT;
    public static final Rop IF_LTZ_INT;
    public static final Rop IF_LT_INT;
    public static final Rop IF_NEZ_INT;
    public static final Rop IF_NEZ_OBJECT;
    public static final Rop IF_NE_INT;
    public static final Rop IF_NE_OBJECT;
    public static final Rop INSTANCE_OF;
    public static final Rop MARK_LOCAL_DOUBLE;
    public static final Rop MARK_LOCAL_FLOAT;
    public static final Rop MARK_LOCAL_INT;
    public static final Rop MARK_LOCAL_LONG;
    public static final Rop MARK_LOCAL_OBJECT;
    public static final Rop MONITOR_ENTER;
    public static final Rop MONITOR_EXIT;
    public static final Rop MOVE_DOUBLE;
    public static final Rop MOVE_FLOAT;
    public static final Rop MOVE_INT;
    public static final Rop MOVE_LONG;
    public static final Rop MOVE_OBJECT;
    public static final Rop MOVE_PARAM_DOUBLE;
    public static final Rop MOVE_PARAM_FLOAT;
    public static final Rop MOVE_PARAM_INT;
    public static final Rop MOVE_PARAM_LONG;
    public static final Rop MOVE_PARAM_OBJECT;
    public static final Rop MOVE_RETURN_ADDRESS;
    public static final Rop MUL_CONST_DOUBLE;
    public static final Rop MUL_CONST_FLOAT;
    public static final Rop MUL_CONST_INT;
    public static final Rop MUL_CONST_LONG;
    public static final Rop MUL_DOUBLE;
    public static final Rop MUL_FLOAT;
    public static final Rop MUL_INT;
    public static final Rop MUL_LONG;
    public static final Rop NEG_DOUBLE;
    public static final Rop NEG_FLOAT;
    public static final Rop NEG_INT;
    public static final Rop NEG_LONG;
    public static final Rop NEW_ARRAY_BOOLEAN;
    public static final Rop NEW_ARRAY_BYTE;
    public static final Rop NEW_ARRAY_CHAR;
    public static final Rop NEW_ARRAY_DOUBLE;
    public static final Rop NEW_ARRAY_FLOAT;
    public static final Rop NEW_ARRAY_INT;
    public static final Rop NEW_ARRAY_LONG;
    public static final Rop NEW_ARRAY_SHORT;
    public static final Rop NEW_INSTANCE;
    public static final Rop NOP;
    public static final Rop NOT_INT;
    public static final Rop NOT_LONG;
    public static final Rop OR_CONST_INT;
    public static final Rop OR_CONST_LONG;
    public static final Rop OR_INT;
    public static final Rop OR_LONG;
    public static final Rop PUT_FIELD_BOOLEAN;
    public static final Rop PUT_FIELD_BYTE;
    public static final Rop PUT_FIELD_CHAR;
    public static final Rop PUT_FIELD_DOUBLE;
    public static final Rop PUT_FIELD_FLOAT;
    public static final Rop PUT_FIELD_INT;
    public static final Rop PUT_FIELD_LONG;
    public static final Rop PUT_FIELD_OBJECT;
    public static final Rop PUT_FIELD_SHORT;
    public static final Rop PUT_STATIC_BOOLEAN;
    public static final Rop PUT_STATIC_BYTE;
    public static final Rop PUT_STATIC_CHAR;
    public static final Rop PUT_STATIC_DOUBLE;
    public static final Rop PUT_STATIC_FLOAT;
    public static final Rop PUT_STATIC_INT;
    public static final Rop PUT_STATIC_LONG;
    public static final Rop PUT_STATIC_OBJECT;
    public static final Rop PUT_STATIC_SHORT;
    public static final Rop REM_CONST_DOUBLE;
    public static final Rop REM_CONST_FLOAT;
    public static final Rop REM_CONST_INT;
    public static final Rop REM_CONST_LONG;
    public static final Rop REM_DOUBLE;
    public static final Rop REM_FLOAT;
    public static final Rop REM_INT;
    public static final Rop REM_LONG;
    public static final Rop RETURN_DOUBLE;
    public static final Rop RETURN_FLOAT;
    public static final Rop RETURN_INT;
    public static final Rop RETURN_LONG;
    public static final Rop RETURN_OBJECT;
    public static final Rop RETURN_VOID;
    public static final Rop SHL_CONST_INT;
    public static final Rop SHL_CONST_LONG;
    public static final Rop SHL_INT;
    public static final Rop SHL_LONG;
    public static final Rop SHR_CONST_INT;
    public static final Rop SHR_CONST_LONG;
    public static final Rop SHR_INT;
    public static final Rop SHR_LONG;
    public static final Rop SUB_CONST_DOUBLE;
    public static final Rop SUB_CONST_FLOAT;
    public static final Rop SUB_CONST_INT;
    public static final Rop SUB_CONST_LONG;
    public static final Rop SUB_DOUBLE;
    public static final Rop SUB_FLOAT;
    public static final Rop SUB_INT;
    public static final Rop SUB_LONG;
    public static final Rop SWITCH;
    public static final Rop THROW;
    public static final Rop TO_BYTE;
    public static final Rop TO_CHAR;
    public static final Rop TO_SHORT;
    public static final Rop USHR_CONST_INT;
    public static final Rop USHR_CONST_LONG;
    public static final Rop USHR_INT;
    public static final Rop USHR_LONG;
    public static final Rop XOR_CONST_INT;
    public static final Rop XOR_CONST_LONG;
    public static final Rop XOR_INT;
    public static final Rop XOR_LONG;
    
    static {
        NOP = new Rop(1, Type.VOID, StdTypeList.EMPTY, "nop");
        MOVE_INT = new Rop(2, Type.INT, StdTypeList.INT, "move-int");
        MOVE_LONG = new Rop(2, Type.LONG, StdTypeList.LONG, "move-long");
        MOVE_FLOAT = new Rop(2, Type.FLOAT, StdTypeList.FLOAT, "move-float");
        MOVE_DOUBLE = new Rop(2, Type.DOUBLE, StdTypeList.DOUBLE, "move-double");
        MOVE_OBJECT = new Rop(2, Type.OBJECT, StdTypeList.OBJECT, "move-object");
        MOVE_RETURN_ADDRESS = new Rop(2, Type.RETURN_ADDRESS, StdTypeList.RETURN_ADDRESS, "move-return-address");
        MOVE_PARAM_INT = new Rop(3, Type.INT, StdTypeList.EMPTY, "move-param-int");
        MOVE_PARAM_LONG = new Rop(3, Type.LONG, StdTypeList.EMPTY, "move-param-long");
        MOVE_PARAM_FLOAT = new Rop(3, Type.FLOAT, StdTypeList.EMPTY, "move-param-float");
        MOVE_PARAM_DOUBLE = new Rop(3, Type.DOUBLE, StdTypeList.EMPTY, "move-param-double");
        MOVE_PARAM_OBJECT = new Rop(3, Type.OBJECT, StdTypeList.EMPTY, "move-param-object");
        CONST_INT = new Rop(5, Type.INT, StdTypeList.EMPTY, "const-int");
        CONST_LONG = new Rop(5, Type.LONG, StdTypeList.EMPTY, "const-long");
        CONST_FLOAT = new Rop(5, Type.FLOAT, StdTypeList.EMPTY, "const-float");
        CONST_DOUBLE = new Rop(5, Type.DOUBLE, StdTypeList.EMPTY, "const-double");
        CONST_OBJECT = new Rop(5, Type.OBJECT, StdTypeList.EMPTY, Exceptions.LIST_Error, "const-object");
        CONST_OBJECT_NOTHROW = new Rop(5, Type.OBJECT, StdTypeList.EMPTY, "const-object-nothrow");
        GOTO = new Rop(6, Type.VOID, StdTypeList.EMPTY, 3, "goto");
        IF_EQZ_INT = new Rop(7, Type.VOID, StdTypeList.INT, 4, "if-eqz-int");
        IF_NEZ_INT = new Rop(8, Type.VOID, StdTypeList.INT, 4, "if-nez-int");
        IF_LTZ_INT = new Rop(9, Type.VOID, StdTypeList.INT, 4, "if-ltz-int");
        IF_GEZ_INT = new Rop(10, Type.VOID, StdTypeList.INT, 4, "if-gez-int");
        IF_LEZ_INT = new Rop(11, Type.VOID, StdTypeList.INT, 4, "if-lez-int");
        IF_GTZ_INT = new Rop(12, Type.VOID, StdTypeList.INT, 4, "if-gtz-int");
        IF_EQZ_OBJECT = new Rop(7, Type.VOID, StdTypeList.OBJECT, 4, "if-eqz-object");
        IF_NEZ_OBJECT = new Rop(8, Type.VOID, StdTypeList.OBJECT, 4, "if-nez-object");
        IF_EQ_INT = new Rop(7, Type.VOID, StdTypeList.INT_INT, 4, "if-eq-int");
        IF_NE_INT = new Rop(8, Type.VOID, StdTypeList.INT_INT, 4, "if-ne-int");
        IF_LT_INT = new Rop(9, Type.VOID, StdTypeList.INT_INT, 4, "if-lt-int");
        IF_GE_INT = new Rop(10, Type.VOID, StdTypeList.INT_INT, 4, "if-ge-int");
        IF_LE_INT = new Rop(11, Type.VOID, StdTypeList.INT_INT, 4, "if-le-int");
        IF_GT_INT = new Rop(12, Type.VOID, StdTypeList.INT_INT, 4, "if-gt-int");
        IF_EQ_OBJECT = new Rop(7, Type.VOID, StdTypeList.OBJECT_OBJECT, 4, "if-eq-object");
        IF_NE_OBJECT = new Rop(8, Type.VOID, StdTypeList.OBJECT_OBJECT, 4, "if-ne-object");
        SWITCH = new Rop(13, Type.VOID, StdTypeList.INT, 5, "switch");
        ADD_INT = new Rop(14, Type.INT, StdTypeList.INT_INT, "add-int");
        ADD_LONG = new Rop(14, Type.LONG, StdTypeList.LONG_LONG, "add-long");
        ADD_FLOAT = new Rop(14, Type.FLOAT, StdTypeList.FLOAT_FLOAT, "add-float");
        ADD_DOUBLE = new Rop(14, Type.DOUBLE, StdTypeList.DOUBLE_DOUBLE, 1, "add-double");
        SUB_INT = new Rop(15, Type.INT, StdTypeList.INT_INT, "sub-int");
        SUB_LONG = new Rop(15, Type.LONG, StdTypeList.LONG_LONG, "sub-long");
        SUB_FLOAT = new Rop(15, Type.FLOAT, StdTypeList.FLOAT_FLOAT, "sub-float");
        SUB_DOUBLE = new Rop(15, Type.DOUBLE, StdTypeList.DOUBLE_DOUBLE, 1, "sub-double");
        MUL_INT = new Rop(16, Type.INT, StdTypeList.INT_INT, "mul-int");
        MUL_LONG = new Rop(16, Type.LONG, StdTypeList.LONG_LONG, "mul-long");
        MUL_FLOAT = new Rop(16, Type.FLOAT, StdTypeList.FLOAT_FLOAT, "mul-float");
        MUL_DOUBLE = new Rop(16, Type.DOUBLE, StdTypeList.DOUBLE_DOUBLE, 1, "mul-double");
        DIV_INT = new Rop(17, Type.INT, StdTypeList.INT_INT, Exceptions.LIST_Error_ArithmeticException, "div-int");
        DIV_LONG = new Rop(17, Type.LONG, StdTypeList.LONG_LONG, Exceptions.LIST_Error_ArithmeticException, "div-long");
        DIV_FLOAT = new Rop(17, Type.FLOAT, StdTypeList.FLOAT_FLOAT, "div-float");
        DIV_DOUBLE = new Rop(17, Type.DOUBLE, StdTypeList.DOUBLE_DOUBLE, "div-double");
        REM_INT = new Rop(18, Type.INT, StdTypeList.INT_INT, Exceptions.LIST_Error_ArithmeticException, "rem-int");
        REM_LONG = new Rop(18, Type.LONG, StdTypeList.LONG_LONG, Exceptions.LIST_Error_ArithmeticException, "rem-long");
        REM_FLOAT = new Rop(18, Type.FLOAT, StdTypeList.FLOAT_FLOAT, "rem-float");
        REM_DOUBLE = new Rop(18, Type.DOUBLE, StdTypeList.DOUBLE_DOUBLE, "rem-double");
        NEG_INT = new Rop(19, Type.INT, StdTypeList.INT, "neg-int");
        NEG_LONG = new Rop(19, Type.LONG, StdTypeList.LONG, "neg-long");
        NEG_FLOAT = new Rop(19, Type.FLOAT, StdTypeList.FLOAT, "neg-float");
        NEG_DOUBLE = new Rop(19, Type.DOUBLE, StdTypeList.DOUBLE, "neg-double");
        AND_INT = new Rop(20, Type.INT, StdTypeList.INT_INT, "and-int");
        AND_LONG = new Rop(20, Type.LONG, StdTypeList.LONG_LONG, "and-long");
        OR_INT = new Rop(21, Type.INT, StdTypeList.INT_INT, "or-int");
        OR_LONG = new Rop(21, Type.LONG, StdTypeList.LONG_LONG, "or-long");
        XOR_INT = new Rop(22, Type.INT, StdTypeList.INT_INT, "xor-int");
        XOR_LONG = new Rop(22, Type.LONG, StdTypeList.LONG_LONG, "xor-long");
        SHL_INT = new Rop(23, Type.INT, StdTypeList.INT_INT, "shl-int");
        SHL_LONG = new Rop(23, Type.LONG, StdTypeList.LONG_INT, "shl-long");
        SHR_INT = new Rop(24, Type.INT, StdTypeList.INT_INT, "shr-int");
        SHR_LONG = new Rop(24, Type.LONG, StdTypeList.LONG_INT, "shr-long");
        USHR_INT = new Rop(25, Type.INT, StdTypeList.INT_INT, "ushr-int");
        USHR_LONG = new Rop(25, Type.LONG, StdTypeList.LONG_INT, "ushr-long");
        NOT_INT = new Rop(26, Type.INT, StdTypeList.INT, "not-int");
        NOT_LONG = new Rop(26, Type.LONG, StdTypeList.LONG, "not-long");
        ADD_CONST_INT = new Rop(14, Type.INT, StdTypeList.INT, "add-const-int");
        ADD_CONST_LONG = new Rop(14, Type.LONG, StdTypeList.LONG, "add-const-long");
        ADD_CONST_FLOAT = new Rop(14, Type.FLOAT, StdTypeList.FLOAT, "add-const-float");
        ADD_CONST_DOUBLE = new Rop(14, Type.DOUBLE, StdTypeList.DOUBLE, "add-const-double");
        SUB_CONST_INT = new Rop(15, Type.INT, StdTypeList.INT, "sub-const-int");
        SUB_CONST_LONG = new Rop(15, Type.LONG, StdTypeList.LONG, "sub-const-long");
        SUB_CONST_FLOAT = new Rop(15, Type.FLOAT, StdTypeList.FLOAT, "sub-const-float");
        SUB_CONST_DOUBLE = new Rop(15, Type.DOUBLE, StdTypeList.DOUBLE, "sub-const-double");
        MUL_CONST_INT = new Rop(16, Type.INT, StdTypeList.INT, "mul-const-int");
        MUL_CONST_LONG = new Rop(16, Type.LONG, StdTypeList.LONG, "mul-const-long");
        MUL_CONST_FLOAT = new Rop(16, Type.FLOAT, StdTypeList.FLOAT, "mul-const-float");
        MUL_CONST_DOUBLE = new Rop(16, Type.DOUBLE, StdTypeList.DOUBLE, "mul-const-double");
        DIV_CONST_INT = new Rop(17, Type.INT, StdTypeList.INT, Exceptions.LIST_Error_ArithmeticException, "div-const-int");
        DIV_CONST_LONG = new Rop(17, Type.LONG, StdTypeList.LONG, Exceptions.LIST_Error_ArithmeticException, "div-const-long");
        DIV_CONST_FLOAT = new Rop(17, Type.FLOAT, StdTypeList.FLOAT, "div-const-float");
        DIV_CONST_DOUBLE = new Rop(17, Type.DOUBLE, StdTypeList.DOUBLE, "div-const-double");
        REM_CONST_INT = new Rop(18, Type.INT, StdTypeList.INT, Exceptions.LIST_Error_ArithmeticException, "rem-const-int");
        REM_CONST_LONG = new Rop(18, Type.LONG, StdTypeList.LONG, Exceptions.LIST_Error_ArithmeticException, "rem-const-long");
        REM_CONST_FLOAT = new Rop(18, Type.FLOAT, StdTypeList.FLOAT, "rem-const-float");
        REM_CONST_DOUBLE = new Rop(18, Type.DOUBLE, StdTypeList.DOUBLE, "rem-const-double");
        AND_CONST_INT = new Rop(20, Type.INT, StdTypeList.INT, "and-const-int");
        AND_CONST_LONG = new Rop(20, Type.LONG, StdTypeList.LONG, "and-const-long");
        OR_CONST_INT = new Rop(21, Type.INT, StdTypeList.INT, "or-const-int");
        OR_CONST_LONG = new Rop(21, Type.LONG, StdTypeList.LONG, "or-const-long");
        XOR_CONST_INT = new Rop(22, Type.INT, StdTypeList.INT, "xor-const-int");
        XOR_CONST_LONG = new Rop(22, Type.LONG, StdTypeList.LONG, "xor-const-long");
        SHL_CONST_INT = new Rop(23, Type.INT, StdTypeList.INT, "shl-const-int");
        SHL_CONST_LONG = new Rop(23, Type.LONG, StdTypeList.INT, "shl-const-long");
        SHR_CONST_INT = new Rop(24, Type.INT, StdTypeList.INT, "shr-const-int");
        SHR_CONST_LONG = new Rop(24, Type.LONG, StdTypeList.INT, "shr-const-long");
        USHR_CONST_INT = new Rop(25, Type.INT, StdTypeList.INT, "ushr-const-int");
        USHR_CONST_LONG = new Rop(25, Type.LONG, StdTypeList.INT, "ushr-const-long");
        CMPL_LONG = new Rop(27, Type.INT, StdTypeList.LONG_LONG, "cmpl-long");
        CMPL_FLOAT = new Rop(27, Type.INT, StdTypeList.FLOAT_FLOAT, "cmpl-float");
        CMPL_DOUBLE = new Rop(27, Type.INT, StdTypeList.DOUBLE_DOUBLE, "cmpl-double");
        CMPG_FLOAT = new Rop(28, Type.INT, StdTypeList.FLOAT_FLOAT, "cmpg-float");
        CMPG_DOUBLE = new Rop(28, Type.INT, StdTypeList.DOUBLE_DOUBLE, "cmpg-double");
        CONV_L2I = new Rop(29, Type.INT, StdTypeList.LONG, "conv-l2i");
        CONV_F2I = new Rop(29, Type.INT, StdTypeList.FLOAT, "conv-f2i");
        CONV_D2I = new Rop(29, Type.INT, StdTypeList.DOUBLE, "conv-d2i");
        CONV_I2L = new Rop(29, Type.LONG, StdTypeList.INT, "conv-i2l");
        CONV_F2L = new Rop(29, Type.LONG, StdTypeList.FLOAT, "conv-f2l");
        CONV_D2L = new Rop(29, Type.LONG, StdTypeList.DOUBLE, "conv-d2l");
        CONV_I2F = new Rop(29, Type.FLOAT, StdTypeList.INT, "conv-i2f");
        CONV_L2F = new Rop(29, Type.FLOAT, StdTypeList.LONG, "conv-l2f");
        CONV_D2F = new Rop(29, Type.FLOAT, StdTypeList.DOUBLE, "conv-d2f");
        CONV_I2D = new Rop(29, Type.DOUBLE, StdTypeList.INT, "conv-i2d");
        CONV_L2D = new Rop(29, Type.DOUBLE, StdTypeList.LONG, "conv-l2d");
        CONV_F2D = new Rop(29, Type.DOUBLE, StdTypeList.FLOAT, "conv-f2d");
        TO_BYTE = new Rop(30, Type.INT, StdTypeList.INT, "to-byte");
        TO_CHAR = new Rop(31, Type.INT, StdTypeList.INT, "to-char");
        TO_SHORT = new Rop(32, Type.INT, StdTypeList.INT, "to-short");
        RETURN_VOID = new Rop(33, Type.VOID, StdTypeList.EMPTY, 2, "return-void");
        RETURN_INT = new Rop(33, Type.VOID, StdTypeList.INT, 2, "return-int");
        RETURN_LONG = new Rop(33, Type.VOID, StdTypeList.LONG, 2, "return-long");
        RETURN_FLOAT = new Rop(33, Type.VOID, StdTypeList.FLOAT, 2, "return-float");
        RETURN_DOUBLE = new Rop(33, Type.VOID, StdTypeList.DOUBLE, 2, "return-double");
        RETURN_OBJECT = new Rop(33, Type.VOID, StdTypeList.OBJECT, 2, "return-object");
        ARRAY_LENGTH = new Rop(34, Type.INT, StdTypeList.OBJECT, Exceptions.LIST_Error_NullPointerException, "array-length");
        THROW = new Rop(35, Type.VOID, StdTypeList.THROWABLE, StdTypeList.THROWABLE, "throw");
        MONITOR_ENTER = new Rop(36, Type.VOID, StdTypeList.OBJECT, Exceptions.LIST_Error_NullPointerException, "monitor-enter");
        MONITOR_EXIT = new Rop(37, Type.VOID, StdTypeList.OBJECT, Exceptions.LIST_Error_Null_IllegalMonitorStateException, "monitor-exit");
        AGET_INT = new Rop(38, Type.INT, StdTypeList.INTARR_INT, Exceptions.LIST_Error_Null_ArrayIndexOutOfBounds, "aget-int");
        AGET_LONG = new Rop(38, Type.LONG, StdTypeList.LONGARR_INT, Exceptions.LIST_Error_Null_ArrayIndexOutOfBounds, "aget-long");
        AGET_FLOAT = new Rop(38, Type.FLOAT, StdTypeList.FLOATARR_INT, Exceptions.LIST_Error_Null_ArrayIndexOutOfBounds, "aget-float");
        AGET_DOUBLE = new Rop(38, Type.DOUBLE, StdTypeList.DOUBLEARR_INT, Exceptions.LIST_Error_Null_ArrayIndexOutOfBounds, "aget-double");
        AGET_OBJECT = new Rop(38, Type.OBJECT, StdTypeList.OBJECTARR_INT, Exceptions.LIST_Error_Null_ArrayIndexOutOfBounds, "aget-object");
        AGET_BOOLEAN = new Rop(38, Type.INT, StdTypeList.BOOLEANARR_INT, Exceptions.LIST_Error_Null_ArrayIndexOutOfBounds, "aget-boolean");
        AGET_BYTE = new Rop(38, Type.INT, StdTypeList.BYTEARR_INT, Exceptions.LIST_Error_Null_ArrayIndexOutOfBounds, "aget-byte");
        AGET_CHAR = new Rop(38, Type.INT, StdTypeList.CHARARR_INT, Exceptions.LIST_Error_Null_ArrayIndexOutOfBounds, "aget-char");
        AGET_SHORT = new Rop(38, Type.INT, StdTypeList.SHORTARR_INT, Exceptions.LIST_Error_Null_ArrayIndexOutOfBounds, "aget-short");
        APUT_INT = new Rop(39, Type.VOID, StdTypeList.INT_INTARR_INT, Exceptions.LIST_Error_Null_ArrayIndexOutOfBounds, "aput-int");
        APUT_LONG = new Rop(39, Type.VOID, StdTypeList.LONG_LONGARR_INT, Exceptions.LIST_Error_Null_ArrayIndexOutOfBounds, "aput-long");
        APUT_FLOAT = new Rop(39, Type.VOID, StdTypeList.FLOAT_FLOATARR_INT, Exceptions.LIST_Error_Null_ArrayIndexOutOfBounds, "aput-float");
        APUT_DOUBLE = new Rop(39, Type.VOID, StdTypeList.DOUBLE_DOUBLEARR_INT, Exceptions.LIST_Error_Null_ArrayIndexOutOfBounds, "aput-double");
        APUT_OBJECT = new Rop(39, Type.VOID, StdTypeList.OBJECT_OBJECTARR_INT, Exceptions.LIST_Error_Null_ArrayIndex_ArrayStore, "aput-object");
        APUT_BOOLEAN = new Rop(39, Type.VOID, StdTypeList.INT_BOOLEANARR_INT, Exceptions.LIST_Error_Null_ArrayIndex_ArrayStore, "aput-boolean");
        APUT_BYTE = new Rop(39, Type.VOID, StdTypeList.INT_BYTEARR_INT, Exceptions.LIST_Error_Null_ArrayIndex_ArrayStore, "aput-byte");
        APUT_CHAR = new Rop(39, Type.VOID, StdTypeList.INT_CHARARR_INT, Exceptions.LIST_Error_Null_ArrayIndex_ArrayStore, "aput-char");
        APUT_SHORT = new Rop(39, Type.VOID, StdTypeList.INT_SHORTARR_INT, Exceptions.LIST_Error_Null_ArrayIndex_ArrayStore, "aput-short");
        NEW_INSTANCE = new Rop(40, Type.OBJECT, StdTypeList.EMPTY, Exceptions.LIST_Error, "new-instance");
        NEW_ARRAY_INT = new Rop(41, Type.INT_ARRAY, StdTypeList.INT, Exceptions.LIST_Error_NegativeArraySizeException, "new-array-int");
        NEW_ARRAY_LONG = new Rop(41, Type.LONG_ARRAY, StdTypeList.INT, Exceptions.LIST_Error_NegativeArraySizeException, "new-array-long");
        NEW_ARRAY_FLOAT = new Rop(41, Type.FLOAT_ARRAY, StdTypeList.INT, Exceptions.LIST_Error_NegativeArraySizeException, "new-array-float");
        NEW_ARRAY_DOUBLE = new Rop(41, Type.DOUBLE_ARRAY, StdTypeList.INT, Exceptions.LIST_Error_NegativeArraySizeException, "new-array-double");
        NEW_ARRAY_BOOLEAN = new Rop(41, Type.BOOLEAN_ARRAY, StdTypeList.INT, Exceptions.LIST_Error_NegativeArraySizeException, "new-array-boolean");
        NEW_ARRAY_BYTE = new Rop(41, Type.BYTE_ARRAY, StdTypeList.INT, Exceptions.LIST_Error_NegativeArraySizeException, "new-array-byte");
        NEW_ARRAY_CHAR = new Rop(41, Type.CHAR_ARRAY, StdTypeList.INT, Exceptions.LIST_Error_NegativeArraySizeException, "new-array-char");
        NEW_ARRAY_SHORT = new Rop(41, Type.SHORT_ARRAY, StdTypeList.INT, Exceptions.LIST_Error_NegativeArraySizeException, "new-array-short");
        CHECK_CAST = new Rop(43, Type.VOID, StdTypeList.OBJECT, Exceptions.LIST_Error_ClassCastException, "check-cast");
        INSTANCE_OF = new Rop(44, Type.INT, StdTypeList.OBJECT, Exceptions.LIST_Error, "instance-of");
        GET_FIELD_INT = new Rop(45, Type.INT, StdTypeList.OBJECT, Exceptions.LIST_Error_NullPointerException, "get-field-int");
        GET_FIELD_LONG = new Rop(45, Type.LONG, StdTypeList.OBJECT, Exceptions.LIST_Error_NullPointerException, "get-field-long");
        GET_FIELD_FLOAT = new Rop(45, Type.FLOAT, StdTypeList.OBJECT, Exceptions.LIST_Error_NullPointerException, "get-field-float");
        GET_FIELD_DOUBLE = new Rop(45, Type.DOUBLE, StdTypeList.OBJECT, Exceptions.LIST_Error_NullPointerException, "get-field-double");
        GET_FIELD_OBJECT = new Rop(45, Type.OBJECT, StdTypeList.OBJECT, Exceptions.LIST_Error_NullPointerException, "get-field-object");
        GET_FIELD_BOOLEAN = new Rop(45, Type.INT, StdTypeList.OBJECT, Exceptions.LIST_Error_NullPointerException, "get-field-boolean");
        GET_FIELD_BYTE = new Rop(45, Type.INT, StdTypeList.OBJECT, Exceptions.LIST_Error_NullPointerException, "get-field-byte");
        GET_FIELD_CHAR = new Rop(45, Type.INT, StdTypeList.OBJECT, Exceptions.LIST_Error_NullPointerException, "get-field-char");
        GET_FIELD_SHORT = new Rop(45, Type.INT, StdTypeList.OBJECT, Exceptions.LIST_Error_NullPointerException, "get-field-short");
        GET_STATIC_INT = new Rop(46, Type.INT, StdTypeList.EMPTY, Exceptions.LIST_Error, "get-static-int");
        GET_STATIC_LONG = new Rop(46, Type.LONG, StdTypeList.EMPTY, Exceptions.LIST_Error, "get-static-long");
        GET_STATIC_FLOAT = new Rop(46, Type.FLOAT, StdTypeList.EMPTY, Exceptions.LIST_Error, "get-static-float");
        GET_STATIC_DOUBLE = new Rop(46, Type.DOUBLE, StdTypeList.EMPTY, Exceptions.LIST_Error, "get-static-double");
        GET_STATIC_OBJECT = new Rop(46, Type.OBJECT, StdTypeList.EMPTY, Exceptions.LIST_Error, "get-static-object");
        GET_STATIC_BOOLEAN = new Rop(46, Type.INT, StdTypeList.EMPTY, Exceptions.LIST_Error, "get-field-boolean");
        GET_STATIC_BYTE = new Rop(46, Type.INT, StdTypeList.EMPTY, Exceptions.LIST_Error, "get-field-byte");
        GET_STATIC_CHAR = new Rop(46, Type.INT, StdTypeList.EMPTY, Exceptions.LIST_Error, "get-field-char");
        GET_STATIC_SHORT = new Rop(46, Type.INT, StdTypeList.EMPTY, Exceptions.LIST_Error, "get-field-short");
        PUT_FIELD_INT = new Rop(47, Type.VOID, StdTypeList.INT_OBJECT, Exceptions.LIST_Error_NullPointerException, "put-field-int");
        PUT_FIELD_LONG = new Rop(47, Type.VOID, StdTypeList.LONG_OBJECT, Exceptions.LIST_Error_NullPointerException, "put-field-long");
        PUT_FIELD_FLOAT = new Rop(47, Type.VOID, StdTypeList.FLOAT_OBJECT, Exceptions.LIST_Error_NullPointerException, "put-field-float");
        PUT_FIELD_DOUBLE = new Rop(47, Type.VOID, StdTypeList.DOUBLE_OBJECT, Exceptions.LIST_Error_NullPointerException, "put-field-double");
        PUT_FIELD_OBJECT = new Rop(47, Type.VOID, StdTypeList.OBJECT_OBJECT, Exceptions.LIST_Error_NullPointerException, "put-field-object");
        PUT_FIELD_BOOLEAN = new Rop(47, Type.VOID, StdTypeList.INT_OBJECT, Exceptions.LIST_Error_NullPointerException, "put-field-boolean");
        PUT_FIELD_BYTE = new Rop(47, Type.VOID, StdTypeList.INT_OBJECT, Exceptions.LIST_Error_NullPointerException, "put-field-byte");
        PUT_FIELD_CHAR = new Rop(47, Type.VOID, StdTypeList.INT_OBJECT, Exceptions.LIST_Error_NullPointerException, "put-field-char");
        PUT_FIELD_SHORT = new Rop(47, Type.VOID, StdTypeList.INT_OBJECT, Exceptions.LIST_Error_NullPointerException, "put-field-short");
        PUT_STATIC_INT = new Rop(48, Type.VOID, StdTypeList.INT, Exceptions.LIST_Error, "put-static-int");
        PUT_STATIC_LONG = new Rop(48, Type.VOID, StdTypeList.LONG, Exceptions.LIST_Error, "put-static-long");
        PUT_STATIC_FLOAT = new Rop(48, Type.VOID, StdTypeList.FLOAT, Exceptions.LIST_Error, "put-static-float");
        PUT_STATIC_DOUBLE = new Rop(48, Type.VOID, StdTypeList.DOUBLE, Exceptions.LIST_Error, "put-static-double");
        PUT_STATIC_OBJECT = new Rop(48, Type.VOID, StdTypeList.OBJECT, Exceptions.LIST_Error, "put-static-object");
        PUT_STATIC_BOOLEAN = new Rop(48, Type.VOID, StdTypeList.INT, Exceptions.LIST_Error, "put-static-boolean");
        PUT_STATIC_BYTE = new Rop(48, Type.VOID, StdTypeList.INT, Exceptions.LIST_Error, "put-static-byte");
        PUT_STATIC_CHAR = new Rop(48, Type.VOID, StdTypeList.INT, Exceptions.LIST_Error, "put-static-char");
        PUT_STATIC_SHORT = new Rop(48, Type.VOID, StdTypeList.INT, Exceptions.LIST_Error, "put-static-short");
        MARK_LOCAL_INT = new Rop(54, Type.VOID, StdTypeList.INT, "mark-local-int");
        MARK_LOCAL_LONG = new Rop(54, Type.VOID, StdTypeList.LONG, "mark-local-long");
        MARK_LOCAL_FLOAT = new Rop(54, Type.VOID, StdTypeList.FLOAT, "mark-local-float");
        MARK_LOCAL_DOUBLE = new Rop(54, Type.VOID, StdTypeList.DOUBLE, "mark-local-double");
        MARK_LOCAL_OBJECT = new Rop(54, Type.VOID, StdTypeList.OBJECT, "mark-local-object");
        FILL_ARRAY_DATA = new Rop(57, Type.VOID, StdTypeList.EMPTY, "fill-array-data");
    }
    
    private Rops() {
    }
    
    public static Rop opAdd(final TypeList list) {
        return pickBinaryOp(list, Rops.ADD_CONST_INT, Rops.ADD_CONST_LONG, Rops.ADD_CONST_FLOAT, Rops.ADD_CONST_DOUBLE, Rops.ADD_INT, Rops.ADD_LONG, Rops.ADD_FLOAT, Rops.ADD_DOUBLE);
    }
    
    public static Rop opAget(final TypeBearer typeBearer) {
        switch (typeBearer.getBasicType()) {
            default: {
                return throwBadType(typeBearer);
            }
            case 9: {
                return Rops.AGET_OBJECT;
            }
            case 8: {
                return Rops.AGET_SHORT;
            }
            case 7: {
                return Rops.AGET_LONG;
            }
            case 6: {
                return Rops.AGET_INT;
            }
            case 5: {
                return Rops.AGET_FLOAT;
            }
            case 4: {
                return Rops.AGET_DOUBLE;
            }
            case 3: {
                return Rops.AGET_CHAR;
            }
            case 2: {
                return Rops.AGET_BYTE;
            }
            case 1: {
                return Rops.AGET_BOOLEAN;
            }
        }
    }
    
    public static Rop opAnd(final TypeList list) {
        return pickBinaryOp(list, Rops.AND_CONST_INT, Rops.AND_CONST_LONG, null, null, Rops.AND_INT, Rops.AND_LONG, null, null);
    }
    
    public static Rop opAput(final TypeBearer typeBearer) {
        switch (typeBearer.getBasicType()) {
            default: {
                return throwBadType(typeBearer);
            }
            case 9: {
                return Rops.APUT_OBJECT;
            }
            case 8: {
                return Rops.APUT_SHORT;
            }
            case 7: {
                return Rops.APUT_LONG;
            }
            case 6: {
                return Rops.APUT_INT;
            }
            case 5: {
                return Rops.APUT_FLOAT;
            }
            case 4: {
                return Rops.APUT_DOUBLE;
            }
            case 3: {
                return Rops.APUT_CHAR;
            }
            case 2: {
                return Rops.APUT_BYTE;
            }
            case 1: {
                return Rops.APUT_BOOLEAN;
            }
        }
    }
    
    public static Rop opCmpg(final TypeBearer typeBearer) {
        switch (typeBearer.getBasicType()) {
            default: {
                return throwBadType(typeBearer);
            }
            case 5: {
                return Rops.CMPG_FLOAT;
            }
            case 4: {
                return Rops.CMPG_DOUBLE;
            }
        }
    }
    
    public static Rop opCmpl(final TypeBearer typeBearer) {
        final int basicType = typeBearer.getBasicType();
        if (basicType == 7) {
            return Rops.CMPL_LONG;
        }
        switch (basicType) {
            default: {
                return throwBadType(typeBearer);
            }
            case 5: {
                return Rops.CMPL_FLOAT;
            }
            case 4: {
                return Rops.CMPL_DOUBLE;
            }
        }
    }
    
    public static Rop opConst(final TypeBearer typeBearer) {
        if (typeBearer.getType() == Type.KNOWN_NULL) {
            return Rops.CONST_OBJECT_NOTHROW;
        }
        switch (typeBearer.getBasicFrameType()) {
            default: {
                return throwBadType(typeBearer);
            }
            case 9: {
                return Rops.CONST_OBJECT;
            }
            case 7: {
                return Rops.CONST_LONG;
            }
            case 6: {
                return Rops.CONST_INT;
            }
            case 5: {
                return Rops.CONST_FLOAT;
            }
            case 4: {
                return Rops.CONST_DOUBLE;
            }
        }
    }
    
    public static Rop opConv(final TypeBearer typeBearer, final TypeBearer typeBearer2) {
        final int basicFrameType = typeBearer.getBasicFrameType();
        Label_0175: {
            switch (typeBearer2.getBasicFrameType()) {
                case 6: {
                    if (basicFrameType == 7) {
                        return Rops.CONV_I2L;
                    }
                    switch (basicFrameType) {
                        default: {
                            break Label_0175;
                        }
                        case 5: {
                            return Rops.CONV_I2F;
                        }
                        case 4: {
                            return Rops.CONV_I2D;
                        }
                    }
                    break;
                }
                case 7: {
                    switch (basicFrameType) {
                        default: {
                            break Label_0175;
                        }
                        case 6: {
                            return Rops.CONV_L2I;
                        }
                        case 5: {
                            return Rops.CONV_L2F;
                        }
                        case 4: {
                            return Rops.CONV_L2D;
                        }
                    }
                    break;
                }
                case 5: {
                    if (basicFrameType == 4) {
                        return Rops.CONV_F2D;
                    }
                    switch (basicFrameType) {
                        default: {
                            break Label_0175;
                        }
                        case 7: {
                            return Rops.CONV_F2L;
                        }
                        case 6: {
                            return Rops.CONV_F2I;
                        }
                    }
                    break;
                }
                case 4: {
                    switch (basicFrameType) {
                        default: {
                            break Label_0175;
                        }
                        case 7: {
                            return Rops.CONV_D2L;
                        }
                        case 6: {
                            return Rops.CONV_D2I;
                        }
                        case 5: {
                            return Rops.CONV_D2F;
                        }
                    }
                    break;
                }
            }
        }
        return throwBadTypes(StdTypeList.make(typeBearer.getType(), typeBearer2.getType()));
    }
    
    public static Rop opDiv(final TypeList list) {
        return pickBinaryOp(list, Rops.DIV_CONST_INT, Rops.DIV_CONST_LONG, Rops.DIV_CONST_FLOAT, Rops.DIV_CONST_DOUBLE, Rops.DIV_INT, Rops.DIV_LONG, Rops.DIV_FLOAT, Rops.DIV_DOUBLE);
    }
    
    public static Rop opFilledNewArray(final TypeBearer typeBearer, final int n) {
        final Type componentType = typeBearer.getType().getComponentType();
        if (componentType.isCategory2()) {
            return throwBadType(typeBearer);
        }
        if (n < 0) {
            throw new IllegalArgumentException("count < 0");
        }
        final StdTypeList list = new StdTypeList(n);
        for (int i = 0; i < n; ++i) {
            list.set(i, componentType);
        }
        return new Rop(42, list, Exceptions.LIST_Error);
    }
    
    public static Rop opGetField(final TypeBearer typeBearer) {
        switch (typeBearer.getBasicType()) {
            default: {
                return throwBadType(typeBearer);
            }
            case 9: {
                return Rops.GET_FIELD_OBJECT;
            }
            case 8: {
                return Rops.GET_FIELD_SHORT;
            }
            case 7: {
                return Rops.GET_FIELD_LONG;
            }
            case 6: {
                return Rops.GET_FIELD_INT;
            }
            case 5: {
                return Rops.GET_FIELD_FLOAT;
            }
            case 4: {
                return Rops.GET_FIELD_DOUBLE;
            }
            case 3: {
                return Rops.GET_FIELD_CHAR;
            }
            case 2: {
                return Rops.GET_FIELD_BYTE;
            }
            case 1: {
                return Rops.GET_FIELD_BOOLEAN;
            }
        }
    }
    
    public static Rop opGetStatic(final TypeBearer typeBearer) {
        switch (typeBearer.getBasicType()) {
            default: {
                return throwBadType(typeBearer);
            }
            case 9: {
                return Rops.GET_STATIC_OBJECT;
            }
            case 8: {
                return Rops.GET_STATIC_SHORT;
            }
            case 7: {
                return Rops.GET_STATIC_LONG;
            }
            case 6: {
                return Rops.GET_STATIC_INT;
            }
            case 5: {
                return Rops.GET_STATIC_FLOAT;
            }
            case 4: {
                return Rops.GET_STATIC_DOUBLE;
            }
            case 3: {
                return Rops.GET_STATIC_CHAR;
            }
            case 2: {
                return Rops.GET_STATIC_BYTE;
            }
            case 1: {
                return Rops.GET_STATIC_BOOLEAN;
            }
        }
    }
    
    public static Rop opIfEq(final TypeList list) {
        return pickIf(list, Rops.IF_EQZ_INT, Rops.IF_EQZ_OBJECT, Rops.IF_EQ_INT, Rops.IF_EQ_OBJECT);
    }
    
    public static Rop opIfGe(final TypeList list) {
        return pickIf(list, Rops.IF_GEZ_INT, null, Rops.IF_GE_INT, null);
    }
    
    public static Rop opIfGt(final TypeList list) {
        return pickIf(list, Rops.IF_GTZ_INT, null, Rops.IF_GT_INT, null);
    }
    
    public static Rop opIfLe(final TypeList list) {
        return pickIf(list, Rops.IF_LEZ_INT, null, Rops.IF_LE_INT, null);
    }
    
    public static Rop opIfLt(final TypeList list) {
        return pickIf(list, Rops.IF_LTZ_INT, null, Rops.IF_LT_INT, null);
    }
    
    public static Rop opIfNe(final TypeList list) {
        return pickIf(list, Rops.IF_NEZ_INT, Rops.IF_NEZ_OBJECT, Rops.IF_NE_INT, Rops.IF_NE_OBJECT);
    }
    
    public static Rop opInvokeDirect(final Prototype prototype) {
        return new Rop(52, prototype.getParameterFrameTypes(), StdTypeList.THROWABLE);
    }
    
    public static Rop opInvokeInterface(final Prototype prototype) {
        return new Rop(53, prototype.getParameterFrameTypes(), StdTypeList.THROWABLE);
    }
    
    public static Rop opInvokeStatic(final Prototype prototype) {
        return new Rop(49, prototype.getParameterFrameTypes(), StdTypeList.THROWABLE);
    }
    
    public static Rop opInvokeSuper(final Prototype prototype) {
        return new Rop(51, prototype.getParameterFrameTypes(), StdTypeList.THROWABLE);
    }
    
    public static Rop opInvokeVirtual(final Prototype prototype) {
        return new Rop(50, prototype.getParameterFrameTypes(), StdTypeList.THROWABLE);
    }
    
    public static Rop opMarkLocal(final TypeBearer typeBearer) {
        switch (typeBearer.getBasicFrameType()) {
            default: {
                return throwBadType(typeBearer);
            }
            case 9: {
                return Rops.MARK_LOCAL_OBJECT;
            }
            case 7: {
                return Rops.MARK_LOCAL_LONG;
            }
            case 6: {
                return Rops.MARK_LOCAL_INT;
            }
            case 5: {
                return Rops.MARK_LOCAL_FLOAT;
            }
            case 4: {
                return Rops.MARK_LOCAL_DOUBLE;
            }
        }
    }
    
    public static Rop opMove(final TypeBearer typeBearer) {
        switch (typeBearer.getBasicFrameType()) {
            default: {
                return throwBadType(typeBearer);
            }
            case 10: {
                return Rops.MOVE_RETURN_ADDRESS;
            }
            case 9: {
                return Rops.MOVE_OBJECT;
            }
            case 7: {
                return Rops.MOVE_LONG;
            }
            case 6: {
                return Rops.MOVE_INT;
            }
            case 5: {
                return Rops.MOVE_FLOAT;
            }
            case 4: {
                return Rops.MOVE_DOUBLE;
            }
        }
    }
    
    public static Rop opMoveException(final TypeBearer typeBearer) {
        return new Rop(4, typeBearer.getType(), StdTypeList.EMPTY, null);
    }
    
    public static Rop opMoveParam(final TypeBearer typeBearer) {
        switch (typeBearer.getBasicFrameType()) {
            default: {
                return throwBadType(typeBearer);
            }
            case 9: {
                return Rops.MOVE_PARAM_OBJECT;
            }
            case 7: {
                return Rops.MOVE_PARAM_LONG;
            }
            case 6: {
                return Rops.MOVE_PARAM_INT;
            }
            case 5: {
                return Rops.MOVE_PARAM_FLOAT;
            }
            case 4: {
                return Rops.MOVE_PARAM_DOUBLE;
            }
        }
    }
    
    public static Rop opMoveResult(final TypeBearer typeBearer) {
        return new Rop(55, typeBearer.getType(), StdTypeList.EMPTY, null);
    }
    
    public static Rop opMoveResultPseudo(final TypeBearer typeBearer) {
        return new Rop(56, typeBearer.getType(), StdTypeList.EMPTY, null);
    }
    
    public static Rop opMul(final TypeList list) {
        return pickBinaryOp(list, Rops.MUL_CONST_INT, Rops.MUL_CONST_LONG, Rops.MUL_CONST_FLOAT, Rops.MUL_CONST_DOUBLE, Rops.MUL_INT, Rops.MUL_LONG, Rops.MUL_FLOAT, Rops.MUL_DOUBLE);
    }
    
    public static Rop opNeg(final TypeBearer typeBearer) {
        switch (typeBearer.getBasicFrameType()) {
            default: {
                return throwBadType(typeBearer);
            }
            case 7: {
                return Rops.NEG_LONG;
            }
            case 6: {
                return Rops.NEG_INT;
            }
            case 5: {
                return Rops.NEG_FLOAT;
            }
            case 4: {
                return Rops.NEG_DOUBLE;
            }
        }
    }
    
    public static Rop opNewArray(final TypeBearer typeBearer) {
        final Type type = typeBearer.getType();
        switch (type.getComponentType().getBasicType()) {
            default: {
                return throwBadType(type);
            }
            case 9: {
                return new Rop(41, type, StdTypeList.INT, Exceptions.LIST_Error_NegativeArraySizeException, "new-array-object");
            }
            case 8: {
                return Rops.NEW_ARRAY_SHORT;
            }
            case 7: {
                return Rops.NEW_ARRAY_LONG;
            }
            case 6: {
                return Rops.NEW_ARRAY_INT;
            }
            case 5: {
                return Rops.NEW_ARRAY_FLOAT;
            }
            case 4: {
                return Rops.NEW_ARRAY_DOUBLE;
            }
            case 3: {
                return Rops.NEW_ARRAY_CHAR;
            }
            case 2: {
                return Rops.NEW_ARRAY_BYTE;
            }
            case 1: {
                return Rops.NEW_ARRAY_BOOLEAN;
            }
        }
    }
    
    public static Rop opNot(final TypeBearer typeBearer) {
        switch (typeBearer.getBasicFrameType()) {
            default: {
                return throwBadType(typeBearer);
            }
            case 7: {
                return Rops.NOT_LONG;
            }
            case 6: {
                return Rops.NOT_INT;
            }
        }
    }
    
    public static Rop opOr(final TypeList list) {
        return pickBinaryOp(list, Rops.OR_CONST_INT, Rops.OR_CONST_LONG, null, null, Rops.OR_INT, Rops.OR_LONG, null, null);
    }
    
    public static Rop opPutField(final TypeBearer typeBearer) {
        switch (typeBearer.getBasicType()) {
            default: {
                return throwBadType(typeBearer);
            }
            case 9: {
                return Rops.PUT_FIELD_OBJECT;
            }
            case 8: {
                return Rops.PUT_FIELD_SHORT;
            }
            case 7: {
                return Rops.PUT_FIELD_LONG;
            }
            case 6: {
                return Rops.PUT_FIELD_INT;
            }
            case 5: {
                return Rops.PUT_FIELD_FLOAT;
            }
            case 4: {
                return Rops.PUT_FIELD_DOUBLE;
            }
            case 3: {
                return Rops.PUT_FIELD_CHAR;
            }
            case 2: {
                return Rops.PUT_FIELD_BYTE;
            }
            case 1: {
                return Rops.PUT_FIELD_BOOLEAN;
            }
        }
    }
    
    public static Rop opPutStatic(final TypeBearer typeBearer) {
        switch (typeBearer.getBasicType()) {
            default: {
                return throwBadType(typeBearer);
            }
            case 9: {
                return Rops.PUT_STATIC_OBJECT;
            }
            case 8: {
                return Rops.PUT_STATIC_SHORT;
            }
            case 7: {
                return Rops.PUT_STATIC_LONG;
            }
            case 6: {
                return Rops.PUT_STATIC_INT;
            }
            case 5: {
                return Rops.PUT_STATIC_FLOAT;
            }
            case 4: {
                return Rops.PUT_STATIC_DOUBLE;
            }
            case 3: {
                return Rops.PUT_STATIC_CHAR;
            }
            case 2: {
                return Rops.PUT_STATIC_BYTE;
            }
            case 1: {
                return Rops.PUT_STATIC_BOOLEAN;
            }
        }
    }
    
    public static Rop opRem(final TypeList list) {
        return pickBinaryOp(list, Rops.REM_CONST_INT, Rops.REM_CONST_LONG, Rops.REM_CONST_FLOAT, Rops.REM_CONST_DOUBLE, Rops.REM_INT, Rops.REM_LONG, Rops.REM_FLOAT, Rops.REM_DOUBLE);
    }
    
    public static Rop opReturn(final TypeBearer typeBearer) {
        final int basicFrameType = typeBearer.getBasicFrameType();
        if (basicFrameType == 0) {
            return Rops.RETURN_VOID;
        }
        if (basicFrameType == 9) {
            return Rops.RETURN_OBJECT;
        }
        switch (basicFrameType) {
            default: {
                return throwBadType(typeBearer);
            }
            case 7: {
                return Rops.RETURN_LONG;
            }
            case 6: {
                return Rops.RETURN_INT;
            }
            case 5: {
                return Rops.RETURN_FLOAT;
            }
            case 4: {
                return Rops.RETURN_DOUBLE;
            }
        }
    }
    
    public static Rop opShl(final TypeList list) {
        return pickBinaryOp(list, Rops.SHL_CONST_INT, Rops.SHL_CONST_LONG, null, null, Rops.SHL_INT, Rops.SHL_LONG, null, null);
    }
    
    public static Rop opShr(final TypeList list) {
        return pickBinaryOp(list, Rops.SHR_CONST_INT, Rops.SHR_CONST_LONG, null, null, Rops.SHR_INT, Rops.SHR_LONG, null, null);
    }
    
    public static Rop opSub(final TypeList list) {
        return pickBinaryOp(list, Rops.SUB_CONST_INT, Rops.SUB_CONST_LONG, Rops.SUB_CONST_FLOAT, Rops.SUB_CONST_DOUBLE, Rops.SUB_INT, Rops.SUB_LONG, Rops.SUB_FLOAT, Rops.SUB_DOUBLE);
    }
    
    public static Rop opUshr(final TypeList list) {
        return pickBinaryOp(list, Rops.USHR_CONST_INT, Rops.USHR_CONST_LONG, null, null, Rops.USHR_INT, Rops.USHR_LONG, null, null);
    }
    
    public static Rop opXor(final TypeList list) {
        return pickBinaryOp(list, Rops.XOR_CONST_INT, Rops.XOR_CONST_LONG, null, null, Rops.XOR_INT, Rops.XOR_LONG, null, null);
    }
    
    private static Rop pickBinaryOp(final TypeList list, Rop rop, final Rop rop2, final Rop rop3, final Rop rop4, final Rop rop5, final Rop rop6, final Rop rop7, final Rop rop8) {
        final int basicFrameType = list.getType(0).getBasicFrameType();
        final Rop rop9 = null;
        final Rop rop10 = null;
        Label_0162: {
            switch (list.size()) {
                default: {
                    rop = rop9;
                    break;
                }
                case 2: {
                    switch (basicFrameType) {
                        default: {
                            rop = rop9;
                            break Label_0162;
                        }
                        case 7: {
                            return rop6;
                        }
                        case 6: {
                            return rop5;
                        }
                        case 5: {
                            rop = rop7;
                            break Label_0162;
                        }
                        case 4: {
                            rop = rop8;
                            break Label_0162;
                        }
                    }
                    break;
                }
                case 1: {
                    switch (basicFrameType) {
                        default: {
                            rop = rop10;
                            break Label_0162;
                        }
                        case 7: {
                            return rop2;
                        }
                        case 6: {
                            return rop;
                        }
                        case 5: {
                            rop = rop3;
                            break Label_0162;
                        }
                        case 4: {
                            rop = rop4;
                            break Label_0162;
                        }
                    }
                    break;
                }
            }
        }
        if (rop == null) {
            return throwBadTypes(list);
        }
        return rop;
    }
    
    private static Rop pickIf(final TypeList list, final Rop rop, final Rop rop2, final Rop rop3, final Rop rop4) {
        switch (list.size()) {
            case 2: {
                final int basicFrameType = list.getType(0).getBasicFrameType();
                if (basicFrameType != list.getType(1).getBasicFrameType()) {
                    break;
                }
                if (basicFrameType == 6) {
                    return rop3;
                }
                if (basicFrameType != 9) {
                    break;
                }
                if (rop4 != null) {
                    return rop4;
                }
                break;
            }
            case 1: {
                final int basicFrameType2 = list.getType(0).getBasicFrameType();
                if (basicFrameType2 != 6) {
                    if (basicFrameType2 == 9) {
                        if (rop2 != null) {
                            return rop2;
                        }
                    }
                    break;
                }
                return rop;
            }
        }
        return throwBadTypes(list);
    }
    
    public static Rop ropFor(final int n, final TypeBearer typeBearer, final TypeList list, final Constant constant) {
        switch (n) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("unknown opcode ");
                sb.append(RegOps.opName(n));
                throw new RuntimeException(sb.toString());
            }
            case 53: {
                final CstMethodRef cstMethodRef = (CstMethodRef)constant;
                return opInvokeInterface(cstMethodRef.getPrototype().withFirstParameter(cstMethodRef.getDefiningClass().getClassType()));
            }
            case 52: {
                final CstMethodRef cstMethodRef2 = (CstMethodRef)constant;
                return opInvokeDirect(cstMethodRef2.getPrototype().withFirstParameter(cstMethodRef2.getDefiningClass().getClassType()));
            }
            case 51: {
                final CstMethodRef cstMethodRef3 = (CstMethodRef)constant;
                return opInvokeSuper(cstMethodRef3.getPrototype().withFirstParameter(cstMethodRef3.getDefiningClass().getClassType()));
            }
            case 50: {
                final CstMethodRef cstMethodRef4 = (CstMethodRef)constant;
                return opInvokeVirtual(cstMethodRef4.getPrototype().withFirstParameter(cstMethodRef4.getDefiningClass().getClassType()));
            }
            case 49: {
                return opInvokeStatic(((CstMethodRef)constant).getPrototype());
            }
            case 48: {
                return opPutStatic(list.getType(0));
            }
            case 47: {
                return opPutField(list.getType(0));
            }
            case 46: {
                return opGetStatic(typeBearer);
            }
            case 45: {
                return opGetField(typeBearer);
            }
            case 44: {
                return Rops.INSTANCE_OF;
            }
            case 43: {
                return Rops.CHECK_CAST;
            }
            case 41: {
                return opNewArray(typeBearer.getType());
            }
            case 40: {
                return Rops.NEW_INSTANCE;
            }
            case 39: {
                final Type type = list.getType(1);
                Type type2;
                if (type == Type.KNOWN_NULL) {
                    type2 = list.getType(0);
                }
                else {
                    type2 = type.getComponentType();
                }
                return opAput(type2);
            }
            case 38: {
                final Type type3 = list.getType(0);
                Type type4;
                if (type3 == Type.KNOWN_NULL) {
                    type4 = typeBearer.getType();
                }
                else {
                    type4 = type3.getComponentType();
                }
                return opAget(type4);
            }
            case 37: {
                return Rops.MONITOR_EXIT;
            }
            case 36: {
                return Rops.MONITOR_ENTER;
            }
            case 35: {
                return Rops.THROW;
            }
            case 34: {
                return Rops.ARRAY_LENGTH;
            }
            case 33: {
                if (list.size() == 0) {
                    return Rops.RETURN_VOID;
                }
                return opReturn(list.getType(0));
            }
            case 32: {
                return Rops.TO_SHORT;
            }
            case 31: {
                return Rops.TO_CHAR;
            }
            case 30: {
                return Rops.TO_BYTE;
            }
            case 29: {
                return opConv(typeBearer, list.getType(0));
            }
            case 28: {
                return opCmpg(list.getType(0));
            }
            case 27: {
                return opCmpl(list.getType(0));
            }
            case 26: {
                return opNot(typeBearer);
            }
            case 25: {
                return opUshr(list);
            }
            case 24: {
                return opShr(list);
            }
            case 23: {
                return opShl(list);
            }
            case 22: {
                return opXor(list);
            }
            case 21: {
                return opOr(list);
            }
            case 20: {
                return opAnd(list);
            }
            case 19: {
                return opNeg(typeBearer);
            }
            case 18: {
                return opRem(list);
            }
            case 17: {
                return opDiv(list);
            }
            case 16: {
                return opMul(list);
            }
            case 15: {
                return opSub(list);
            }
            case 14: {
                return opAdd(list);
            }
            case 13: {
                return Rops.SWITCH;
            }
            case 12: {
                return opIfGt(list);
            }
            case 11: {
                return opIfLe(list);
            }
            case 10: {
                return opIfGe(list);
            }
            case 9: {
                return opIfLt(list);
            }
            case 8: {
                return opIfNe(list);
            }
            case 7: {
                return opIfEq(list);
            }
            case 6: {
                return Rops.GOTO;
            }
            case 5: {
                return opConst(typeBearer);
            }
            case 4: {
                return opMoveException(typeBearer);
            }
            case 3: {
                return opMoveParam(typeBearer);
            }
            case 2: {
                return opMove(typeBearer);
            }
            case 1: {
                return Rops.NOP;
            }
        }
    }
    
    private static Rop throwBadType(final TypeBearer typeBearer) {
        final StringBuilder sb = new StringBuilder();
        sb.append("bad type: ");
        sb.append(typeBearer);
        throw new IllegalArgumentException(sb.toString());
    }
    
    private static Rop throwBadTypes(final TypeList list) {
        final StringBuilder sb = new StringBuilder();
        sb.append("bad types: ");
        sb.append(list);
        throw new IllegalArgumentException(sb.toString());
    }
}
