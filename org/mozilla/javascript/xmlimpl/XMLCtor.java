package org.mozilla.javascript.xmlimpl;

import org.mozilla.javascript.*;

class XMLCtor extends IdFunctionObject
{
    private static final int Id_defaultSettings = 1;
    private static final int Id_ignoreComments = 1;
    private static final int Id_ignoreProcessingInstructions = 2;
    private static final int Id_ignoreWhitespace = 3;
    private static final int Id_prettyIndent = 4;
    private static final int Id_prettyPrinting = 5;
    private static final int Id_setSettings = 3;
    private static final int Id_settings = 2;
    private static final int MAX_FUNCTION_ID = 3;
    private static final int MAX_INSTANCE_ID = 5;
    private static final Object XMLCTOR_TAG;
    static final long serialVersionUID = -8708195078359817341L;
    private XmlProcessor options;
    
    static {
        XMLCTOR_TAG = "XMLCtor";
    }
    
    XMLCtor(final XML xml, final Object o, final int n, final int n2) {
        super(xml, o, n, n2);
        this.options = xml.getProcessor();
        this.activatePrototypeMap(3);
    }
    
    private void readSettings(final Scriptable scriptable) {
        for (int i = 1; i <= 5; ++i) {
            final int n = super.getMaxInstanceId() + i;
            final Object property = ScriptableObject.getProperty(scriptable, this.getInstanceIdName(n));
            if (property != Scriptable.NOT_FOUND) {
                switch (i) {
                    default: {
                        throw new IllegalStateException();
                    }
                    case 4: {
                        if (!(property instanceof Number)) {
                            continue;
                        }
                        break;
                    }
                    case 1:
                    case 2:
                    case 3:
                    case 5: {
                        if (!(property instanceof Boolean)) {
                            continue;
                        }
                        break;
                    }
                }
                this.setInstanceIdValue(n, property);
            }
        }
    }
    
    private void writeSetting(final Scriptable scriptable) {
        for (int i = 1; i <= 5; ++i) {
            final int n = super.getMaxInstanceId() + i;
            ScriptableObject.putProperty(scriptable, this.getInstanceIdName(n), this.getInstanceIdValue(n));
        }
    }
    
    @Override
    public Object execIdCall(final IdFunctionObject idFunctionObject, final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        if (!idFunctionObject.hasTag(XMLCtor.XMLCTOR_TAG)) {
            return super.execIdCall(idFunctionObject, context, scriptable, scriptable2, array);
        }
        final int methodId = idFunctionObject.methodId();
        switch (methodId) {
            default: {
                throw new IllegalArgumentException(String.valueOf(methodId));
            }
            case 3: {
                if (array.length != 0 && array[0] != null && array[0] != Undefined.instance) {
                    if (array[0] instanceof Scriptable) {
                        this.readSettings((Scriptable)array[0]);
                    }
                }
                else {
                    this.options.setDefault();
                }
                return Undefined.instance;
            }
            case 2: {
                final Scriptable object = context.newObject(scriptable);
                this.writeSetting(object);
                return object;
            }
            case 1: {
                this.options.setDefault();
                final Scriptable object2 = context.newObject(scriptable);
                this.writeSetting(object2);
                return object2;
            }
        }
    }
    
    @Override
    protected int findInstanceIdInfo(final String s) {
        int n = 0;
        String s2 = null;
        final int length = s.length();
        if (length != 12) {
            if (length != 14) {
                if (length != 16) {
                    if (length == 28) {
                        s2 = "ignoreProcessingInstructions";
                        n = 2;
                    }
                }
                else {
                    s2 = "ignoreWhitespace";
                    n = 3;
                }
            }
            else {
                final char char1 = s.charAt(0);
                if (char1 == 'i') {
                    s2 = "ignoreComments";
                    n = 1;
                }
                else if (char1 == 'p') {
                    s2 = "prettyPrinting";
                    n = 5;
                }
            }
        }
        else {
            s2 = "prettyIndent";
            n = 4;
        }
        int n2 = n;
        if (s2 != null) {
            n2 = n;
            if (s2 != s) {
                n2 = n;
                if (!s2.equals(s)) {
                    n2 = 0;
                }
            }
        }
        if (n2 == 0) {
            return super.findInstanceIdInfo(s);
        }
        switch (n2) {
            default: {
                throw new IllegalStateException();
            }
            case 1:
            case 2:
            case 3:
            case 4:
            case 5: {
                return IdScriptableObject.instanceIdInfo(6, super.getMaxInstanceId() + n2);
            }
        }
    }
    
    @Override
    protected int findPrototypeId(final String s) {
        int n = 0;
        String s2 = null;
        final int length = s.length();
        if (length == 8) {
            s2 = "settings";
            n = 2;
        }
        else if (length == 11) {
            s2 = "setSettings";
            n = 3;
        }
        else if (length == 15) {
            s2 = "defaultSettings";
            n = 1;
        }
        int n2 = n;
        if (s2 != null) {
            n2 = n;
            if (s2 != s) {
                n2 = n;
                if (!s2.equals(s)) {
                    n2 = 0;
                }
            }
        }
        return n2;
    }
    
    @Override
    protected String getInstanceIdName(final int n) {
        switch (n - super.getMaxInstanceId()) {
            default: {
                return super.getInstanceIdName(n);
            }
            case 5: {
                return "prettyPrinting";
            }
            case 4: {
                return "prettyIndent";
            }
            case 3: {
                return "ignoreWhitespace";
            }
            case 2: {
                return "ignoreProcessingInstructions";
            }
            case 1: {
                return "ignoreComments";
            }
        }
    }
    
    @Override
    protected Object getInstanceIdValue(final int n) {
        switch (n - super.getMaxInstanceId()) {
            default: {
                return super.getInstanceIdValue(n);
            }
            case 5: {
                return ScriptRuntime.wrapBoolean(this.options.isPrettyPrinting());
            }
            case 4: {
                return ScriptRuntime.wrapInt(this.options.getPrettyIndent());
            }
            case 3: {
                return ScriptRuntime.wrapBoolean(this.options.isIgnoreWhitespace());
            }
            case 2: {
                return ScriptRuntime.wrapBoolean(this.options.isIgnoreProcessingInstructions());
            }
            case 1: {
                return ScriptRuntime.wrapBoolean(this.options.isIgnoreComments());
            }
        }
    }
    
    @Override
    protected int getMaxInstanceId() {
        return super.getMaxInstanceId() + 5;
    }
    
    @Override
    public boolean hasInstance(final Scriptable scriptable) {
        return scriptable instanceof XML || scriptable instanceof XMLList;
    }
    
    @Override
    protected void initPrototypeId(final int n) {
        int n2 = 0;
        String s = null;
        switch (n) {
            default: {
                throw new IllegalArgumentException(String.valueOf(n));
            }
            case 3: {
                n2 = 1;
                s = "setSettings";
                break;
            }
            case 2: {
                n2 = 0;
                s = "settings";
                break;
            }
            case 1: {
                n2 = 0;
                s = "defaultSettings";
                break;
            }
        }
        this.initPrototypeMethod(XMLCtor.XMLCTOR_TAG, n, s, n2);
    }
    
    @Override
    protected void setInstanceIdValue(final int n, final Object o) {
        switch (n - super.getMaxInstanceId()) {
            default: {
                super.setInstanceIdValue(n, o);
            }
            case 5: {
                this.options.setPrettyPrinting(ScriptRuntime.toBoolean(o));
            }
            case 4: {
                this.options.setPrettyIndent(ScriptRuntime.toInt32(o));
            }
            case 3: {
                this.options.setIgnoreWhitespace(ScriptRuntime.toBoolean(o));
            }
            case 2: {
                this.options.setIgnoreProcessingInstructions(ScriptRuntime.toBoolean(o));
            }
            case 1: {
                this.options.setIgnoreComments(ScriptRuntime.toBoolean(o));
            }
        }
    }
}
