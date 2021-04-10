package com.zhekasmirnov.horizon.runtime.task;

import java.util.*;

public class TaskManager
{
    private final HashMap<Object, ThreadHolder> threads;
    private List<StateCallback> stateCallbacks;
    private int numRunningThreads;
    private List<Task> activeTasks;
    
    public TaskManager() {
        this.threads = new HashMap<Object, ThreadHolder>();
        this.stateCallbacks = new ArrayList<StateCallback>();
        this.numRunningThreads = 0;
        this.activeTasks = new ArrayList<Task>();
    }
    
    private ThreadHolder getHolder(final Object lock) {
        synchronized (this.threads) {
            ThreadHolder holder = this.threads.get(lock);
            if (holder == null) {
                holder = new ThreadHolder(lock);
                this.threads.put(lock, holder);
            }
            return holder;
        }
    }
    
    private synchronized void registerStateChange(final Object senderLock) {
        this.numRunningThreads = 0;
        this.activeTasks.clear();
        synchronized (this.threads) {
            for (final Map.Entry<Object, ThreadHolder> entry : this.threads.entrySet()) {
                final ThreadHolder holder = entry.getValue();
                if (holder.isRunning()) {
                    ++this.numRunningThreads;
                    final Task task = holder.getCurrentTask();
                    if (task == null) {
                        continue;
                    }
                    this.activeTasks.add(task);
                }
            }
        }
        for (final StateCallback callback : this.stateCallbacks) {
            callback.onStateUpdated(this, this.getHolder(senderLock));
        }
    }
    
    public List<Task> getActiveTasks() {
        return this.activeTasks;
    }
    
    public int getNumRunningThreads() {
        return this.numRunningThreads;
    }
    
    public String getFormattedTaskDescriptions(final int maxTasks) {
        final StringBuilder builder = new StringBuilder();
        if (this.activeTasks.size() > maxTasks) {
            builder.append(this.activeTasks.size()).append(" tasks are running...");
        }
        else {
            boolean isFirst = true;
            for (final Task task : this.activeTasks) {
                final String description = task.getDescription();
                if (description != null) {
                    if (isFirst) {
                        isFirst = false;
                    }
                    else {
                        builder.append(", ");
                    }
                    builder.append(description);
                }
            }
        }
        return builder.toString();
    }
    
    public void addStateCallback(final StateCallback callback) {
        this.stateCallbacks.add(callback);
    }
    
    public TaskWatcher addTask(final Task task, final TaskWatcher.Callback callback) {
        final ThreadHolder holder = this.getHolder(task.getLock());
        final TaskWatcher watcher = new TaskWatcher(task, callback);
        holder.runInQueue(task, watcher);
        return watcher;
    }
    
    public TaskWatcher addTask(final Task task) {
        return this.addTask(task, null);
    }
    
    public TaskWatcher addTaskSequence(final TaskSequence sequence, final TaskWatcher.Callback callback, final Runnable complete) {
        TaskWatcher watcher = null;
        for (final Task task : sequence.getAllTasks()) {
            watcher = this.addTask(task, callback);
        }
        if (complete != null) {
            watcher = this.addTask(new Task(sequence.getSequenceId()) {
                @Override
                public Object getLock() {
                    return sequence.getLock();
                }
                
                @Override
                public void run() {
                    complete.run();
                }
            });
        }
        return watcher;
    }
    
    public void interruptTaskSequence(final TaskSequence sequence) {
        for (final ThreadHolder holder : this.threads.values()) {
            synchronized (holder.queue) {
                for (int i = 0; i < holder.queue.size(); ++i) {
                    if (this.isSequenceTask(holder.queue.get(i).task.getSequenceId(), sequence)) {
                        holder.queue.set(i, null);
                    }
                }
            }
        }
    }
    
    private boolean isSequenceTask(final int sequenceId, final TaskSequence sequence) {
        if (sequenceId == sequence.getSequenceId()) {
            return true;
        }
        for (final TaskSequence subsequence : sequence.getSubSequencies()) {
            if (this.isSequenceTask(sequenceId, subsequence)) {
                return true;
            }
        }
        return false;
    }
    
    public void addTaskSequence(final TaskSequence sequence, final Runnable complete) {
        this.addTaskSequence(sequence, null, complete);
    }
    
    public void addTaskSequence(final TaskSequence sequence) {
        this.addTaskSequence(sequence, null, null);
    }
    
    public class ThreadHolder
    {
        private final Object lock;
        private Thread thread;
        private QueuedTask currentTask;
        private final List<QueuedTask> queue;
        
        private ThreadHolder(final Object lock) {
            this.thread = null;
            this.currentTask = null;
            this.queue = new ArrayList<QueuedTask>();
            this.lock = lock;
        }
        
        private void queueTask(final QueuedTask task) {
            final int priority = task.task.getQueuePriority();
            for (int i = 0; i < this.queue.size(); ++i) {
                final QueuedTask queued = this.queue.get(i);
                if (priority > queued.task.getQueuePriority()) {
                    this.queue.add(i, task);
                    return;
                }
            }
            this.queue.add(task);
        }
        
        void runInQueue(final Task task, final TaskWatcher watcher) {
            synchronized (this.queue) {
                this.queueTask(new QueuedTask(task, watcher));
                if (this.thread == null) {
                    this.thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (true) {
                                synchronized (ThreadHolder.this.queue) {
                                    ThreadHolder.this.currentTask = null;
                                    while (ThreadHolder.this.queue.size() > 0 && ThreadHolder.this.currentTask == null) {
                                        ThreadHolder.this.currentTask = ThreadHolder.this.queue.remove(0);
                                    }
                                    if (ThreadHolder.this.currentTask == null) {
                                        break;
                                    }
                                }
                                TaskManager.this.registerStateChange(ThreadHolder.this.lock);
                                ThreadHolder.this.currentTask.run();
                            }
                            ThreadHolder.this.thread = null;
                            TaskManager.this.registerStateChange(ThreadHolder.this.lock);
                        }
                    });
                    TaskManager.this.registerStateChange(this.lock);
                    this.thread.start();
                }
            }
        }
        
        public boolean isRunning() {
            return this.thread != null;
        }
        
        public Task getCurrentTask() {
            return (this.currentTask != null) ? this.currentTask.task : null;
        }
        
        public String getCurrentTaskDescription() {
            return (this.currentTask != null) ? this.currentTask.task.getDescription() : null;
        }
        
        private class QueuedTask
        {
            final Task task;
            final TaskWatcher watcher;
            
            private QueuedTask(final Task task, final TaskWatcher watcher) {
                this.task = task;
                this.watcher = watcher;
            }
            
            private void run() {
                try {
                    this.task.run();
                    this.watcher.onComplete();
                }
                catch (Throwable error) {
                    this.watcher.onError(error);
                }
            }
        }
    }
    
    public interface StateCallback
    {
        void onStateUpdated(final TaskManager p0, final ThreadHolder p1);
    }
}
