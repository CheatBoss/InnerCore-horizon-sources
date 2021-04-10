package com.microsoft.aad.adal;

import java.util.*;

final class AggregatedDispatcher extends DefaultDispatcher
{
    AggregatedDispatcher(final IDispatcher dispatcher) {
        super(dispatcher);
    }
    
    @Override
    void flush(final String s) {
    Label_0054_Outer:
        while (true) {
            while (true) {
                Label_0108: {
                    synchronized (this) {
                        final HashMap<String, String> hashMap = new HashMap<String, String>();
                        if (this.getDispatcher() == null) {
                            return;
                        }
                        final List<IEvents> list = this.getObjectsToBeDispatched().remove(s);
                        if (list != null && !list.isEmpty()) {
                            break Label_0108;
                        }
                        return;
                        // iftrue(Label_0087:, n >= list2.size())
                        while (true) {
                            final List<IEvents> list2;
                            final int n;
                            list2.get(n).processEvent(hashMap);
                            ++n;
                            continue Label_0054_Outer;
                        }
                        Label_0087: {
                            this.getDispatcher().dispatchEvent(hashMap);
                        }
                        return;
                    }
                }
                int n = 0;
                continue;
            }
        }
    }
    
    @Override
    void receive(final String s, final IEvents events) {
        List<IEvents> list;
        if ((list = this.getObjectsToBeDispatched().get(s)) == null) {
            list = new ArrayList<IEvents>();
        }
        list.add(events);
        this.getObjectsToBeDispatched().put(s, list);
    }
}
