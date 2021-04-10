package org.mozilla.javascript.regexp;

import org.mozilla.javascript.*;

class NativeRegExpCtor extends BaseFunction
{
    private static final int DOLLAR_ID_BASE = 12;
    private static final int Id_AMPERSAND = 6;
    private static final int Id_BACK_QUOTE = 10;
    private static final int Id_DOLLAR_1 = 13;
    private static final int Id_DOLLAR_2 = 14;
    private static final int Id_DOLLAR_3 = 15;
    private static final int Id_DOLLAR_4 = 16;
    private static final int Id_DOLLAR_5 = 17;
    private static final int Id_DOLLAR_6 = 18;
    private static final int Id_DOLLAR_7 = 19;
    private static final int Id_DOLLAR_8 = 20;
    private static final int Id_DOLLAR_9 = 21;
    private static final int Id_PLUS = 8;
    private static final int Id_QUOTE = 12;
    private static final int Id_STAR = 2;
    private static final int Id_UNDERSCORE = 4;
    private static final int Id_input = 3;
    private static final int Id_lastMatch = 5;
    private static final int Id_lastParen = 7;
    private static final int Id_leftContext = 9;
    private static final int Id_multiline = 1;
    private static final int Id_rightContext = 11;
    private static final int MAX_INSTANCE_ID = 21;
    static final long serialVersionUID = -5733330028285400526L;
    private int inputAttr;
    private int multilineAttr;
    private int starAttr;
    private int underscoreAttr;
    
    NativeRegExpCtor() {
        this.multilineAttr = 4;
        this.starAttr = 4;
        this.inputAttr = 4;
        this.underscoreAttr = 4;
    }
    
    private static RegExpImpl getImpl() {
        return (RegExpImpl)ScriptRuntime.getRegExpProxy(Context.getCurrentContext());
    }
    
    @Override
    public Object call(final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        if (array.length > 0 && array[0] instanceof NativeRegExp && (array.length == 1 || array[1] == Undefined.instance)) {
            return array[0];
        }
        return this.construct(context, scriptable, array);
    }
    
    @Override
    public Scriptable construct(final Context context, final Scriptable scriptable, final Object[] array) {
        final NativeRegExp nativeRegExp = new NativeRegExp();
        nativeRegExp.compile(context, scriptable, array);
        ScriptRuntime.setBuiltinProtoAndParent(nativeRegExp, scriptable, TopLevel.Builtins.RegExp);
        return nativeRegExp;
    }
    
    @Override
    protected int findInstanceIdInfo(final String s) {
        int n = 0;
        String s2 = null;
        final int length = s.length();
        int n2 = 0;
        Label_0529: {
            Label_0501: {
                if (length != 2) {
                    if (length != 5) {
                        if (length != 9) {
                            switch (length) {
                                case 12: {
                                    s2 = "rightContext";
                                    n = 11;
                                    break;
                                }
                                case 11: {
                                    s2 = "leftContext";
                                    n = 9;
                                    break;
                                }
                            }
                        }
                        else {
                            final char char1 = s.charAt(4);
                            if (char1 == 'M') {
                                s2 = "lastMatch";
                                n = 5;
                            }
                            else if (char1 == 'P') {
                                s2 = "lastParen";
                                n = 7;
                            }
                            else if (char1 == 'i') {
                                s2 = "multiline";
                                n = 1;
                            }
                        }
                    }
                    else {
                        s2 = "input";
                        n = 3;
                    }
                }
                else {
                    final char char2 = s.charAt(1);
                    switch (char2) {
                        default: {
                            switch (char2) {
                                default: {
                                    switch (char2) {
                                        default: {
                                            switch (char2) {
                                                default: {
                                                    break Label_0501;
                                                }
                                                case 96: {
                                                    if (s.charAt(0) == '$') {
                                                        n2 = 10;
                                                        break Label_0529;
                                                    }
                                                    break Label_0501;
                                                }
                                                case 95: {
                                                    if (s.charAt(0) == '$') {
                                                        n2 = 4;
                                                        break Label_0529;
                                                    }
                                                    break Label_0501;
                                                }
                                            }
                                            break;
                                        }
                                        case 57: {
                                            if (s.charAt(0) == '$') {
                                                n2 = 21;
                                                break Label_0529;
                                            }
                                            break Label_0501;
                                        }
                                        case 56: {
                                            if (s.charAt(0) == '$') {
                                                n2 = 20;
                                                break Label_0529;
                                            }
                                            break Label_0501;
                                        }
                                        case 55: {
                                            if (s.charAt(0) == '$') {
                                                n2 = 19;
                                                break Label_0529;
                                            }
                                            break Label_0501;
                                        }
                                        case 54: {
                                            if (s.charAt(0) == '$') {
                                                n2 = 18;
                                                break Label_0529;
                                            }
                                            break Label_0501;
                                        }
                                        case 53: {
                                            if (s.charAt(0) == '$') {
                                                n2 = 17;
                                                break Label_0529;
                                            }
                                            break Label_0501;
                                        }
                                        case 52: {
                                            if (s.charAt(0) == '$') {
                                                n2 = 16;
                                                break Label_0529;
                                            }
                                            break Label_0501;
                                        }
                                        case 51: {
                                            if (s.charAt(0) == '$') {
                                                n2 = 15;
                                                break Label_0529;
                                            }
                                            break Label_0501;
                                        }
                                        case 50: {
                                            if (s.charAt(0) == '$') {
                                                n2 = 14;
                                                break Label_0529;
                                            }
                                            break Label_0501;
                                        }
                                        case 49: {
                                            if (s.charAt(0) == '$') {
                                                n2 = 13;
                                                break Label_0529;
                                            }
                                            break Label_0501;
                                        }
                                    }
                                    break;
                                }
                                case 43: {
                                    if (s.charAt(0) == '$') {
                                        n2 = 8;
                                        break Label_0529;
                                    }
                                    break Label_0501;
                                }
                                case 42: {
                                    if (s.charAt(0) == '$') {
                                        n2 = 2;
                                        break Label_0529;
                                    }
                                    break Label_0501;
                                }
                            }
                            break;
                        }
                        case 39: {
                            if (s.charAt(0) == '$') {
                                n2 = 12;
                                break Label_0529;
                            }
                            break;
                        }
                        case 38: {
                            if (s.charAt(0) == '$') {
                                n2 = 6;
                                break Label_0529;
                            }
                            break;
                        }
                    }
                }
            }
            n2 = n;
            if (s2 != null) {
                n2 = n;
                if (s2 != s) {
                    n2 = n;
                    if (!s2.equals(s)) {
                        n2 = 0;
                    }
                }
            }
        }
        if (n2 == 0) {
            return super.findInstanceIdInfo(s);
        }
        int n3 = 0;
        switch (n2) {
            default: {
                n3 = 5;
                break;
            }
            case 4: {
                n3 = this.underscoreAttr;
                break;
            }
            case 3: {
                n3 = this.inputAttr;
                break;
            }
            case 2: {
                n3 = this.starAttr;
                break;
            }
            case 1: {
                n3 = this.multilineAttr;
                break;
            }
        }
        return IdScriptableObject.instanceIdInfo(n3, super.getMaxInstanceId() + n2);
    }
    
    @Override
    public int getArity() {
        return 2;
    }
    
    @Override
    public String getFunctionName() {
        return "RegExp";
    }
    
    @Override
    protected String getInstanceIdName(final int n) {
        final int n2 = n - super.getMaxInstanceId();
        if (1 > n2 || n2 > 21) {
            return super.getInstanceIdName(n);
        }
        switch (n2) {
            default: {
                return new String(new char[] { '$', (char)(n2 - 12 - 1 + 49) });
            }
            case 12: {
                return "$'";
            }
            case 11: {
                return "rightContext";
            }
            case 10: {
                return "$`";
            }
            case 9: {
                return "leftContext";
            }
            case 8: {
                return "$+";
            }
            case 7: {
                return "lastParen";
            }
            case 6: {
                return "$&";
            }
            case 5: {
                return "lastMatch";
            }
            case 4: {
                return "$_";
            }
            case 3: {
                return "input";
            }
            case 2: {
                return "$*";
            }
            case 1: {
                return "multiline";
            }
        }
    }
    
    @Override
    protected Object getInstanceIdValue(final int n) {
        final int n2 = n - super.getMaxInstanceId();
        if (1 > n2 || n2 > 21) {
            return super.getInstanceIdValue(n);
        }
        final RegExpImpl impl = getImpl();
        Object o = null;
        switch (n2) {
            default: {
                o = impl.getParenSubString(n2 - 12 - 1);
                break;
            }
            case 11:
            case 12: {
                o = impl.rightContext;
                break;
            }
            case 9:
            case 10: {
                o = impl.leftContext;
                break;
            }
            case 7:
            case 8: {
                o = impl.lastParen;
                break;
            }
            case 5:
            case 6: {
                o = impl.lastMatch;
                break;
            }
            case 3:
            case 4: {
                o = impl.input;
                break;
            }
            case 1:
            case 2: {
                return ScriptRuntime.wrapBoolean(impl.multiline);
            }
        }
        if (o == null) {
            return "";
        }
        return o.toString();
    }
    
    @Override
    public int getLength() {
        return 2;
    }
    
    @Override
    protected int getMaxInstanceId() {
        return super.getMaxInstanceId() + 21;
    }
    
    @Override
    protected void setInstanceIdAttributes(final int n, final int n2) {
        final int n3 = n - super.getMaxInstanceId();
        switch (n3) {
            default: {
                final int n4 = n3 - 12 - 1;
                if (n4 >= 0 && n4 <= 8) {
                    return;
                }
                super.setInstanceIdAttributes(n, n2);
            }
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12: {}
            case 4: {
                this.underscoreAttr = n2;
            }
            case 3: {
                this.inputAttr = n2;
            }
            case 2: {
                this.starAttr = n2;
            }
            case 1: {
                this.multilineAttr = n2;
            }
        }
    }
    
    @Override
    protected void setInstanceIdValue(final int n, final Object o) {
        final int n2 = n - super.getMaxInstanceId();
        switch (n2) {
            default: {
                final int n3 = n2 - 12 - 1;
                if (n3 >= 0 && n3 <= 8) {
                    return;
                }
                super.setInstanceIdValue(n, o);
            }
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12: {}
            case 3:
            case 4: {
                getImpl().input = ScriptRuntime.toString(o);
            }
            case 1:
            case 2: {
                getImpl().multiline = ScriptRuntime.toBoolean(o);
            }
        }
    }
}
