package com.android.dx.cf.direct;

import com.android.dx.cf.iface.*;
import com.android.dx.util.*;
import com.android.dx.rop.cst.*;
import com.android.dx.cf.attrib.*;

public class AttributeFactory
{
    public static final int CTX_CLASS = 0;
    public static final int CTX_CODE = 3;
    public static final int CTX_COUNT = 4;
    public static final int CTX_FIELD = 1;
    public static final int CTX_METHOD = 2;
    
    public final Attribute parse(final DirectClassFile directClassFile, final int n, final int n2, final ParseObserver parseObserver) {
        if (directClassFile == null) {
            throw new NullPointerException("cf == null");
        }
        if (n >= 0 && n < 4) {
            CstString cstString = null;
            Label_0210: {
                try {
                    final ByteArray bytes = directClassFile.getBytes();
                    final ConstantPool constantPool = directClassFile.getConstantPool();
                    final int unsignedShort = bytes.getUnsignedShort(n2);
                    final int int1 = bytes.getInt(n2 + 2);
                    cstString = (CstString)constantPool.get(unsignedShort);
                    if (parseObserver != null) {
                        try {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("name: ");
                            sb.append(cstString.toHuman());
                            parseObserver.parsed(bytes, n2, 2, sb.toString());
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("length: ");
                            sb2.append(Hex.u4(int1));
                            parseObserver.parsed(bytes, n2 + 2, 4, sb2.toString());
                        }
                        catch (ParseException ex) {
                            break Label_0210;
                        }
                    }
                    try {
                        final String string = cstString.getString();
                        try {
                            return this.parse0(directClassFile, n, string, n2 + 6, int1, parseObserver);
                        }
                        catch (ParseException ex) {}
                    }
                    catch (ParseException ex) {}
                }
                catch (ParseException ex) {
                    cstString = null;
                }
            }
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("...while parsing ");
            String string2;
            if (cstString != null) {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append(cstString.toHuman());
                sb4.append(" ");
                string2 = sb4.toString();
            }
            else {
                string2 = "";
            }
            sb3.append(string2);
            sb3.append("attribute at offset ");
            sb3.append(Hex.u4(n2));
            final ParseException ex;
            ex.addContext(sb3.toString());
            throw ex;
        }
        throw new IllegalArgumentException("bad context");
    }
    
    protected Attribute parse0(final DirectClassFile directClassFile, final int n, final String s, final int n2, final int n3, final ParseObserver parseObserver) {
        final ByteArray bytes = directClassFile.getBytes();
        final RawAttribute rawAttribute = new RawAttribute(s, bytes, n2, n3, directClassFile.getConstantPool());
        if (parseObserver != null) {
            parseObserver.parsed(bytes, n2, n3, "attribute data");
        }
        return rawAttribute;
    }
}
