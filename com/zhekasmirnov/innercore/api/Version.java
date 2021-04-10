package com.zhekasmirnov.innercore.api;

public class Version
{
    public static final Version INNER_CORE_VERSION;
    public int build;
    public boolean isBeta;
    public int level;
    public String name;
    
    static {
        INNER_CORE_VERSION = new Version("2.0.0.0", 10, true);
    }
    
    public Version(final String s) {
        this(s, 0, false);
    }
    
    public Version(final String s, final int n) {
        this(s, n, false);
    }
    
    public Version(final String name, final int level, final boolean isBeta) {
        this.isBeta = false;
        this.level = 0;
        this.name = "";
        this.build = -1;
        this.name = name;
        this.level = level;
        this.isBeta = isBeta;
    }
    
    public void setBuild(final int build) {
        this.build = build;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("v");
        sb.append(this.name);
        String s;
        if (this.isBeta) {
            s = " beta";
        }
        else {
            s = "";
        }
        sb.append(s);
        String string;
        if (this.build > 0) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(" build ");
            sb2.append(this.build);
            string = sb2.toString();
        }
        else {
            string = "";
        }
        sb.append(string);
        return sb.toString();
    }
}
