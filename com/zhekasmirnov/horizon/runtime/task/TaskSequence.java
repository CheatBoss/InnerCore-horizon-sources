package com.zhekasmirnov.horizon.runtime.task;

import java.util.*;

public class TaskSequence
{
    private static int nextId;
    private int sequenceId;
    private final Object lock;
    private List<Task> allTasks;
    private List<TaskSequence> subSequences;
    
    public TaskSequence(final Object lock, final Object... tasks) {
        this.allTasks = new ArrayList<Task>();
        this.subSequences = new ArrayList<TaskSequence>();
        this.sequenceId = TaskSequence.nextId++;
        this.lock = lock;
        for (final Object task : tasks) {
            if (task instanceof Runnable) {
                this.addTask((Runnable)task);
            }
            else if (task instanceof TaskSequence) {
                this.subSequences.add((TaskSequence)task);
                for (final Runnable subTask : ((TaskSequence)task).allTasks) {
                    this.addTask(subTask);
                }
            }
        }
    }
    
    public List<Task> getAllTasks() {
        return this.allTasks;
    }
    
    public void addTask(final Runnable task) {
        this.allTasks.add(new Task(this.sequenceId) {
            @Override
            public Object getLock() {
                return TaskSequence.this.lock;
            }
            
            @Override
            public String getDescription() {
                if (task instanceof Task) {
                    return ((Task)task).getDescription();
                }
                return null;
            }
            
            @Override
            public void run() {
                task.run();
            }
        });
    }
    
    public Object getLock() {
        return this.lock;
    }
    
    public int getSequenceId() {
        return this.sequenceId;
    }
    
    public List<TaskSequence> getSubSequencies() {
        return this.subSequences;
    }
    
    static {
        TaskSequence.nextId = 0;
    }
    
    public abstract static class AnonymousTask extends Task
    {
        @Override
        public Object getLock() {
            return null;
        }
    }
}
