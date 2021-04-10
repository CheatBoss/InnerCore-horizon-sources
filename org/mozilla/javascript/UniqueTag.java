package org.mozilla.javascript;

import java.io.*;

public final class UniqueTag implements Serializable
{
    public static final UniqueTag DOUBLE_MARK;
    private static final int ID_DOUBLE_MARK = 3;
    private static final int ID_NOT_FOUND = 1;
    private static final int ID_NULL_VALUE = 2;
    public static final UniqueTag NOT_FOUND;
    public static final UniqueTag NULL_VALUE;
    static final long serialVersionUID = -4320556826714577259L;
    private final int tagId;
    
    static {
        NOT_FOUND = new UniqueTag(1);
        NULL_VALUE = new UniqueTag(2);
        DOUBLE_MARK = new UniqueTag(3);
    }
    
    private UniqueTag(final int tagId) {
        this.tagId = tagId;
    }
    
    public Object readResolve() {
        switch (this.tagId) {
            default: {
                throw new IllegalStateException(String.valueOf(this.tagId));
            }
            case 3: {
                return UniqueTag.DOUBLE_MARK;
            }
            case 2: {
                return UniqueTag.NULL_VALUE;
            }
            case 1: {
                return UniqueTag.NOT_FOUND;
            }
        }
    }
    
    @Override
    public String toString() {
        String s = null;
        switch (this.tagId) {
            default: {
                throw Kit.codeBug();
            }
            case 3: {
                s = "DOUBLE_MARK";
                break;
            }
            case 2: {
                s = "NULL_VALUE";
                break;
            }
            case 1: {
                s = "NOT_FOUND";
                break;
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(": ");
        sb.append(s);
        return sb.toString();
    }
}
