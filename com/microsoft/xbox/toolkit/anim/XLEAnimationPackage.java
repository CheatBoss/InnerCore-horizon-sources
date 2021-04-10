package com.microsoft.xbox.toolkit.anim;

import java.util.*;
import com.microsoft.xbox.toolkit.*;

public class XLEAnimationPackage
{
    private LinkedList<XLEAnimationEntry> animations;
    private Runnable onAnimationEndRunnable;
    private boolean running;
    
    public XLEAnimationPackage() {
        this.running = false;
        this.animations = new LinkedList<XLEAnimationEntry>();
    }
    
    private int getRemainingAnimations() {
        final Iterator<XLEAnimationEntry> iterator = this.animations.iterator();
        int n = 0;
        while (iterator.hasNext()) {
            if (!iterator.next().done) {
                ++n;
            }
        }
        return n;
    }
    
    private void tryFinishAll() {
        if (this.getRemainingAnimations() == 0) {
            XLEAssert.assertTrue(this.running);
            this.running = false;
            this.onAnimationEndRunnable.run();
        }
    }
    
    public XLEAnimationPackage add(final XLEAnimationPackage xleAnimationPackage) {
        if (xleAnimationPackage != null) {
            final Iterator<XLEAnimationEntry> iterator = xleAnimationPackage.animations.iterator();
            while (iterator.hasNext()) {
                this.add(iterator.next().animation);
            }
        }
        return this;
    }
    
    public void add(final XLEAnimation xleAnimation) {
        this.animations.add(new XLEAnimationEntry(xleAnimation));
    }
    
    public void clearAnimation() {
        final Iterator<XLEAnimationEntry> iterator = this.animations.iterator();
        while (iterator.hasNext()) {
            iterator.next().clearAnimation();
        }
    }
    
    public void setOnAnimationEndRunnable(final Runnable onAnimationEndRunnable) {
        this.onAnimationEndRunnable = onAnimationEndRunnable;
    }
    
    public void startAnimation() {
        XLEAssert.assertTrue(this.running ^ true);
        this.running = true;
        final Iterator<XLEAnimationEntry> iterator = this.animations.iterator();
        while (iterator.hasNext()) {
            iterator.next().startAnimation();
        }
    }
    
    private class XLEAnimationEntry
    {
        public XLEAnimation animation;
        public boolean done;
        public int iterationID;
        
        public XLEAnimationEntry(final XLEAnimation animation) {
            this.animation = animation;
            this.iterationID = 0;
            this.done = false;
            animation.setOnAnimationEnd(new Runnable() {
                @Override
                public void run() {
                    XLEAnimationEntry.this.onAnimationEnded();
                }
            });
        }
        
        private void finish() {
            this.done = true;
            XLEAnimationPackage.this.tryFinishAll();
        }
        
        private void onAnimationEnded() {
            final Thread currentThread = Thread.currentThread();
            final Thread uiThread = ThreadManager.UIThread;
            final boolean b = false;
            XLEAssert.assertTrue(currentThread == uiThread);
            boolean b2 = b;
            if (XLEAnimationPackage.this.onAnimationEndRunnable != null) {
                b2 = true;
            }
            XLEAssert.assertTrue(b2);
            ThreadManager.UIThreadPost(new Runnable() {
                final /* synthetic */ int val$finishIterationID = XLEAnimationEntry.this.iterationID;
                
                @Override
                public void run() {
                    if (this.val$finishIterationID == XLEAnimationEntry.this.iterationID) {
                        XLEAnimationEntry.this.finish();
                    }
                }
            });
        }
        
        public void clearAnimation() {
            ++this.iterationID;
            this.animation.clear();
        }
        
        public void startAnimation() {
            this.animation.start();
        }
    }
}
