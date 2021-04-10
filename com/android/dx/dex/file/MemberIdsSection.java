package com.android.dx.dex.file;

import java.util.concurrent.atomic.*;
import com.android.dx.command.dexer.*;
import java.util.*;
import com.android.dex.*;

public abstract class MemberIdsSection extends UniformItemSection
{
    public MemberIdsSection(final String s, final DexFile dexFile) {
        super(s, dexFile, 4);
    }
    
    private String getTooManyMembersMessage() {
        Object o = new TreeMap<Object, AtomicInteger>();
        final Iterator<? extends Item> iterator = this.items().iterator();
        while (iterator.hasNext()) {
            final String packageName = ((MemberIdItem)iterator.next()).getDefiningClass().getPackageName();
            AtomicInteger atomicInteger;
            if ((atomicInteger = ((Map<Object, AtomicInteger>)o).get(packageName)) == null) {
                atomicInteger = new AtomicInteger();
                ((Map<String, AtomicInteger>)o).put(packageName, atomicInteger);
            }
            atomicInteger.incrementAndGet();
        }
        while (true) {
            final Formatter formatter = new Formatter();
            while (true) {
                try {
                    if (this instanceof MethodIdsSection) {
                        final String s = "method";
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Too many %s references: %d; max is %d.%n");
                        sb.append(Main.getTooManyIdsErrorMessage());
                        sb.append("%n");
                        sb.append("References by package:");
                        formatter.format(sb.toString(), s, this.items().size(), 65536);
                        final Iterator<Map.Entry<String, AtomicInteger>> iterator2 = ((Map<String, AtomicInteger>)o).entrySet().iterator();
                        while (iterator2.hasNext()) {
                            o = iterator2.next();
                            formatter.format("%n%6d %s", ((Map.Entry<String, AtomicInteger>)o).getValue().get(), ((Map.Entry<Object, AtomicInteger>)o).getKey());
                        }
                        return formatter.toString();
                    }
                }
                finally {
                    formatter.close();
                }
                final String s = "field";
                continue;
            }
        }
    }
    
    @Override
    protected void orderItems() {
        int index = 0;
        if (this.items().size() > 65536) {
            throw new DexIndexOverflowException(this.getTooManyMembersMessage());
        }
        final Iterator<? extends Item> iterator = this.items().iterator();
        while (iterator.hasNext()) {
            ((MemberIdItem)iterator.next()).setIndex(index);
            ++index;
        }
    }
}
