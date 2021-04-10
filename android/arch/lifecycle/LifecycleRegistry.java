package android.arch.lifecycle;

import java.util.*;
import android.arch.core.internal.*;

public class LifecycleRegistry extends Lifecycle
{
    private int mAddingObserverCounter;
    private boolean mHandlingEvent;
    private final LifecycleOwner mLifecycleOwner;
    private boolean mNewEventOccurred;
    private FastSafeIterableMap<LifecycleObserver, ObserverWithState> mObserverMap;
    private ArrayList<Lifecycle$State> mParentStates;
    private Lifecycle$State mState;
    
    public LifecycleRegistry(final LifecycleOwner mLifecycleOwner) {
        this.mObserverMap = new FastSafeIterableMap<LifecycleObserver, ObserverWithState>();
        this.mAddingObserverCounter = 0;
        this.mHandlingEvent = false;
        this.mNewEventOccurred = false;
        this.mParentStates = new ArrayList<Lifecycle$State>();
        this.mLifecycleOwner = mLifecycleOwner;
        this.mState = Lifecycle$State.INITIALIZED;
    }
    
    private void backwardPass() {
        final Iterator<Map.Entry<LifecycleObserver, ObserverWithState>> descendingIterator = (Iterator<Map.Entry<LifecycleObserver, ObserverWithState>>)this.mObserverMap.descendingIterator();
        while (descendingIterator.hasNext() && !this.mNewEventOccurred) {
            final Map.Entry<LifecycleObserver, ObserverWithState> entry = descendingIterator.next();
            final ObserverWithState observerWithState = entry.getValue();
            while (observerWithState.mState.compareTo((Enum)this.mState) > 0 && !this.mNewEventOccurred && this.mObserverMap.contains(entry.getKey())) {
                final Lifecycle$Event downEvent = downEvent(observerWithState.mState);
                this.pushParentState(getStateAfter(downEvent));
                observerWithState.dispatchEvent(this.mLifecycleOwner, downEvent);
                this.popParentState();
            }
        }
    }
    
    private static Lifecycle$Event downEvent(final Lifecycle$State lifecycle$State) {
        final int n = LifecycleRegistry$1.$SwitchMap$android$arch$lifecycle$Lifecycle$State[lifecycle$State.ordinal()];
        if (n == 1) {
            throw new IllegalArgumentException();
        }
        if (n == 2) {
            return Lifecycle$Event.ON_DESTROY;
        }
        if (n == 3) {
            return Lifecycle$Event.ON_STOP;
        }
        if (n == 4) {
            return Lifecycle$Event.ON_PAUSE;
        }
        if (n != 5) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unexpected state value ");
            sb.append(lifecycle$State);
            throw new IllegalArgumentException(sb.toString());
        }
        throw new IllegalArgumentException();
    }
    
    private void forwardPass() {
        final SafeIterableMap.IteratorWithAdditions iteratorWithAdditions = this.mObserverMap.iteratorWithAdditions();
        while (iteratorWithAdditions.hasNext() && !this.mNewEventOccurred) {
            final Map.Entry<K, ObserverWithState> entry = ((Iterator<Map.Entry<K, ObserverWithState>>)iteratorWithAdditions).next();
            final ObserverWithState observerWithState = entry.getValue();
            while (observerWithState.mState.compareTo((Enum)this.mState) < 0 && !this.mNewEventOccurred && this.mObserverMap.contains((LifecycleObserver)entry.getKey())) {
                this.pushParentState(observerWithState.mState);
                observerWithState.dispatchEvent(this.mLifecycleOwner, upEvent(observerWithState.mState));
                this.popParentState();
            }
        }
    }
    
    static Lifecycle$State getStateAfter(final Lifecycle$Event lifecycle$Event) {
        switch (LifecycleRegistry$1.$SwitchMap$android$arch$lifecycle$Lifecycle$Event[lifecycle$Event.ordinal()]) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unexpected event value ");
                sb.append(lifecycle$Event);
                throw new IllegalArgumentException(sb.toString());
            }
            case 6: {
                return Lifecycle$State.DESTROYED;
            }
            case 5: {
                return Lifecycle$State.RESUMED;
            }
            case 3:
            case 4: {
                return Lifecycle$State.STARTED;
            }
            case 1:
            case 2: {
                return Lifecycle$State.CREATED;
            }
        }
    }
    
    private boolean isSynced() {
        if (this.mObserverMap.size() == 0) {
            return true;
        }
        final Lifecycle$State mState = this.mObserverMap.eldest().getValue().mState;
        final Lifecycle$State mState2 = this.mObserverMap.newest().getValue().mState;
        return mState == mState2 && this.mState == mState2;
    }
    
    static Lifecycle$State min(final Lifecycle$State lifecycle$State, final Lifecycle$State lifecycle$State2) {
        Lifecycle$State lifecycle$State3 = lifecycle$State;
        if (lifecycle$State2 != null) {
            lifecycle$State3 = lifecycle$State;
            if (lifecycle$State2.compareTo((Enum)lifecycle$State) < 0) {
                lifecycle$State3 = lifecycle$State2;
            }
        }
        return lifecycle$State3;
    }
    
    private void popParentState() {
        final ArrayList<Lifecycle$State> mParentStates = this.mParentStates;
        mParentStates.remove(mParentStates.size() - 1);
    }
    
    private void pushParentState(final Lifecycle$State lifecycle$State) {
        this.mParentStates.add(lifecycle$State);
    }
    
    private void sync() {
        while (!this.isSynced()) {
            this.mNewEventOccurred = false;
            if (this.mState.compareTo((Enum)this.mObserverMap.eldest().getValue().mState) < 0) {
                this.backwardPass();
            }
            final Map.Entry<LifecycleObserver, ObserverWithState> newest = this.mObserverMap.newest();
            if (!this.mNewEventOccurred && newest != null && this.mState.compareTo((Enum)newest.getValue().mState) > 0) {
                this.forwardPass();
            }
        }
        this.mNewEventOccurred = false;
    }
    
    private static Lifecycle$Event upEvent(final Lifecycle$State lifecycle$State) {
        final int n = LifecycleRegistry$1.$SwitchMap$android$arch$lifecycle$Lifecycle$State[lifecycle$State.ordinal()];
        if (n != 1) {
            if (n == 2) {
                return Lifecycle$Event.ON_START;
            }
            if (n == 3) {
                return Lifecycle$Event.ON_RESUME;
            }
            if (n == 4) {
                throw new IllegalArgumentException();
            }
            if (n != 5) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unexpected state value ");
                sb.append(lifecycle$State);
                throw new IllegalArgumentException(sb.toString());
            }
        }
        return Lifecycle$Event.ON_CREATE;
    }
    
    public void handleLifecycleEvent(final Lifecycle$Event lifecycle$Event) {
        this.mState = getStateAfter(lifecycle$Event);
        if (!this.mHandlingEvent && this.mAddingObserverCounter == 0) {
            this.mHandlingEvent = true;
            this.sync();
            this.mHandlingEvent = false;
            return;
        }
        this.mNewEventOccurred = true;
    }
    
    public void markState(final Lifecycle$State mState) {
        this.mState = mState;
    }
    
    static class ObserverWithState
    {
        GenericLifecycleObserver mLifecycleObserver;
        Lifecycle$State mState;
        
        void dispatchEvent(final LifecycleOwner lifecycleOwner, final Lifecycle$Event lifecycle$Event) {
            final Lifecycle$State stateAfter = LifecycleRegistry.getStateAfter(lifecycle$Event);
            this.mState = LifecycleRegistry.min(this.mState, stateAfter);
            this.mLifecycleObserver.onStateChanged(lifecycleOwner, lifecycle$Event);
            this.mState = stateAfter;
        }
    }
}
