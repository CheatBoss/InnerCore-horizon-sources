package com.bumptech.glide.load.engine.bitmap_recycle;

import java.util.*;

class PrettyPrintTreeMap<K, V> extends TreeMap<K, V>
{
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("( ");
        for (final Map.Entry<K, V> entry : this.entrySet()) {
            sb.append('{');
            sb.append(entry.getKey());
            sb.append(':');
            sb.append(entry.getValue());
            sb.append("}, ");
        }
        if (!this.isEmpty()) {
            sb.replace(sb.length() - 2, sb.length(), "");
        }
        sb.append(" )");
        return sb.toString();
    }
}
