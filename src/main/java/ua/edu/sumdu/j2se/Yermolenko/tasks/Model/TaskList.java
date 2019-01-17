package ua.edu.sumdu.j2se.Yermolenko.tasks.Model;

import java.io.Serializable;

public abstract class TaskList implements Iterable<Task>, Cloneable, Serializable {
    public abstract void add(Task task);
    public abstract boolean remove(Task task);
    public abstract int size();
    public abstract Task getTask(int index);

    @Override
    protected TaskList clone() throws CloneNotSupportedException {
        TaskList clone = null;
        try {
            clone = this.getClass().newInstance();
            for (Task entry : this) {
                clone.add(entry.clone());
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return clone;
    }
}

