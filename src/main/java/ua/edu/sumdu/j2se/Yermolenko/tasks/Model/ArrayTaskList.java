package ua.edu.sumdu.j2se.Yermolenko.tasks.Model;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayTaskList extends TaskList {
    private int initLength = 10;
    private Task[] tasks = new Task[initLength];
    private int size;

    public int getSize() {
        return size;
    }

    public Task[] getTasks() {
        return tasks;
    }
/**
 * Adding the task to the array.
 * If the array is full, the longer array will be created.
 */
    @Override
    public void add(Task task) {
        if (task == null) {
            throw new NullPointerException("Task must not be empty");
        }
        else {
            if (tasks.length == size + 1) {
                initLength *= 3 / 2 + 1;
                Task[] newTasks = new Task[initLength];
                System.arraycopy(tasks, 0, newTasks, 0, tasks.length);
                tasks = newTasks;
            }
            tasks[size] = task;
            size++;
        }
    }
/**
 * Removing the one task from the array and returning true if such
 * task/tasks was/were present.
 */
    @Override
    public boolean remove(Task task) {
        if (task == null) {
            throw new NullPointerException("Task must not be empty");
        }
        else {
            for (int i = 0; i < tasks.length; i++) {
                if (task.equals(tasks[i])) {
                    int taskMoved = initLength - i - 1;
                    System.arraycopy(tasks, i + 1, tasks, i, taskMoved);
                    tasks[--initLength] = null;
                    size--;
                    return true;
                }
            }
            return false;
        }
    }
/**
 * Returns amount of tasks.
 */
    @Override
    public int size() {
        return size;
    }
/**
 * Returns the task from the current position in array (begins from 0).
 */
    @Override
    public Task getTask(int index) {
        if (index >= tasks.length) {
            throw new ArrayIndexOutOfBoundsException("Array index out of bounds");
        }
        return tasks[index];
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        ArrayTaskList arrCompare = (ArrayTaskList) obj;
        if (size == arrCompare.size && hashCode() == arrCompare.hashCode()) {
            for (int i = 0; i < size; i++) {
                if (!tasks[i].equals(arrCompare.getTask(i))) {
                    return false;
                }
            }
        } else return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = 17;
        for (int i = 0; i < size; i++) {
            result = 31 * result + getTask(i).hashCode();
        }
        return result;
    }

    @Override
    public String toString() {
        int taskID = 1;
        String result = size + " tasks in ArrayTaskList:\n\n";
        if (tasks.length > 0) {
            for (Task t : tasks) {
                if (t != null) {
                    result = result + taskID + " " + t + "\n";
                    taskID++;
                }
            }
        }
        return result;
    }

    @Override
    public ArrayTaskList clone() throws CloneNotSupportedException{
        ArrayTaskList clone = new ArrayTaskList();
        for (Task entry : this) {
            clone.add(entry.clone());
        }
        return clone;
    }

    @Override
    public Iterator<Task> iterator() {
        return new ArrayTaskListIterator();
    }

    private class ArrayTaskListIterator implements Iterator<Task> {
        int index = 0;           // index of next element to return
        int lastReturned = -1;   // index of last element returned; -1 if no such

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public Task next() {
            if (!hasNext()) {
                throw new NoSuchElementException("There are no more tasks in the array list.");
            }
            int i = index++;
            return tasks[lastReturned = i];
        }

        @Override
        public void remove() {
            if (lastReturned < 0) {
                throw new IllegalStateException();
            }
            ArrayTaskList.this.remove(tasks[lastReturned]);
            index = lastReturned;
            lastReturned = -1;
        }
    }
}
