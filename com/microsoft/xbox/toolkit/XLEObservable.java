package com.microsoft.xbox.toolkit;

import java.util.*;

public abstract class XLEObservable<T>
{
    private HashSet<XLEObserver<T>> data;
    
    public XLEObservable() {
        this.data = new HashSet<XLEObserver<T>>();
    }
    
    public void addObserver(final XLEObserver<T> xleObserver) {
        while (true) {
            while (true) {
                synchronized (this) {
                    if (Thread.currentThread() == ThreadManager.UIThread) {
                        final boolean b = true;
                        XLEAssert.assertTrue(b);
                        this.data.add(xleObserver);
                        return;
                    }
                }
                final boolean b = false;
                continue;
            }
        }
    }
    
    public void addUniqueObserver(final XLEObserver<T> xleObserver) {
        synchronized (this) {
            if (!this.data.contains(xleObserver)) {
                this.addObserver(xleObserver);
            }
        }
    }
    
    protected void clearObserver() {
        while (true) {
            while (true) {
                synchronized (this) {
                    if (Thread.currentThread() == ThreadManager.UIThread) {
                        final boolean b = true;
                        XLEAssert.assertTrue(b);
                        this.data.clear();
                        return;
                    }
                }
                final boolean b = false;
                continue;
            }
        }
    }
    
    protected ArrayList<XLEObserver<T>> getObservers() {
        synchronized (this) {
            return new ArrayList<XLEObserver<T>>(this.data);
        }
    }
    
    public void notifyObservers(final AsyncResult<T> asyncResult) {
        while (true) {
            while (true) {
                synchronized (this) {
                    if (Thread.currentThread() == ThreadManager.UIThread) {
                        final boolean b = true;
                        XLEAssert.assertTrue(b);
                        final Iterator<XLEObserver<T>> iterator = (Iterator<XLEObserver<T>>)new ArrayList<Object>(this.data).iterator();
                        while (iterator.hasNext()) {
                            iterator.next().update(asyncResult);
                        }
                        return;
                    }
                }
                final boolean b = false;
                continue;
            }
        }
    }
    
    public void removeObserver(final XLEObserver<T> xleObserver) {
        while (true) {
            while (true) {
                synchronized (this) {
                    if (Thread.currentThread() == ThreadManager.UIThread) {
                        final boolean b = true;
                        XLEAssert.assertTrue(b);
                        this.data.remove(xleObserver);
                        return;
                    }
                }
                final boolean b = false;
                continue;
            }
        }
    }
}
