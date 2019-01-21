package ua.edu.sumdu.j2se.Yermolenko.tasks.Model;

import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedTaskList extends TaskList implements Cloneable {
    private final static Logger logger = Logger.getLogger(LinkedTaskList.class);
    private int size;
    private Node head;
    private Node tail;

    public int getSize() { return size; }

    public Node getHead() {
        return head;
    }

    public Node getTail() {
        return tail;
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task must not be empty");
        } else {
            Node newNode = new Node(task);
            if (tail == null) {
                tail = newNode;
                head = newNode;
            } else {
                tail.next = newNode;
                tail = newNode;
            }
            size++;
        }
    }

    @Override
    public boolean remove(Task task) {
        Node prev = null;
        if (task == null || head == null)
            throw new IllegalArgumentException("Task or linkedList must not be empty");
        else {
            for (Node i = head;  i != null; i = i.next) {
                if (task.equals(i.task)) {
                   if (i == tail && tail != head) {
                       tail = prev;
                       prev.next = null;
                   }
                   else {
                       if (i == head) {
                           head = head.next;
                       }
                       else {
                           prev.next = i.next;
                       }
                   i.next = null;
                   }
                   size--;
                   return true;
                }
                prev = i;
            }
            return false;
        }
    }

    @Override
    public int size() {return size;}

    @Override
    public Task getTask(int index) {
        int count = 0;
        Task result = null;
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index is out of size");
        for (Node i = head; i != null; i = i.next) {
            if (count == index) {
                result = i.task;
                break;
            }
            else count++;
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkedTaskList listCompare = (LinkedTaskList) o;
        if (this.size() != listCompare.size()) return false;
        for (int i = 0; i < size(); i ++) {
            if (((this.getTask(i) == null
                    && listCompare.getTask(i) == null))
                    || (this.getTask(i) != null
                    && (this.getTask(i).equals(listCompare.getTask(i))))) return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        for (Node i = head; i != null; i = i.next) {
            result = 31 * result + i.hashCode();
        }
        return result;
    }

    @Override
    public String toString() {
        String info = "LinkedTaskList: " + "size=" + size + ", tasks: ";
        for (Node i = head; i != null ; i = i.next) {
            if (i.next == null) {
                info = info + i.getTask() + ".";
            }
            else info = info + i.getTask() + ", ";
        }
        return info;
    }

    @Override
    public LinkedTaskList clone() throws CloneNotSupportedException {
        LinkedTaskList clone = new LinkedTaskList();
        for (Task entry: this) {
            clone.add(entry.clone());
        }
        return clone;
    }

    @Override
    public Iterator<Task> iterator() {
        return new LinkedTaskListIterator();
    }

    private class LinkedTaskListIterator implements Iterator<Task> {
        private int index = 0;
        private Node nextLink;
        private Node lastReturned;

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public Task next() {
            if (!hasNext()) {
                throw new NoSuchElementException("There are no more tasks in the linked list.");
            }
            if (index == 0) {
                lastReturned = head;
            }
            else {
                lastReturned = nextLink;
            }
            nextLink = lastReturned.next;
            index++;
            return lastReturned.task;
        }

        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
            LinkedTaskList.this.remove(lastReturned.getTask());
            index--;
            lastReturned = null;
        }
    }

    private class Node {
        private Task task;
        private Node next;

        Node(Task task) {
            this.task = task;
        }

        public Task getTask() {
            return task;
        }

        public Node getNext() {
            return next;
        }
    }
}
