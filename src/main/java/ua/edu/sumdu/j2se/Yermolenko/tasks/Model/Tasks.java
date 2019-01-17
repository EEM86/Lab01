package ua.edu.sumdu.j2se.Yermolenko.tasks.Model;

import java.util.*;

public class Tasks {

    public static Iterable<Task> incoming(Iterable<Task> tasks, Date start, Date end) {
        ArrayTaskList arr = new ArrayTaskList();
        for (Task t : tasks) {
            if (t.isActive() && t.nextTimeAfter(start) != null
                    && t.nextTimeAfter(start).compareTo(end) <= 0) {
                arr.add(t);
            }
        }
        return arr;
    }

    public static SortedMap<Date, Set<Task>> calendar(Iterable<Task> tasks, Date start, Date end) {
        SortedMap<Date, Set<Task>> calend = new TreeMap<Date, Set<Task>>();
        for (Task t : tasks) {
            Date result = t.nextTimeAfter(start);
            while (result != null && result.compareTo(end) <= 0) {
                if (calend.containsKey(result)) {
                    calend.get(result).add(t);
                }
                else {
                    Set<Task> tasksSet = new HashSet<Task>();
                    tasksSet.add(t);
                    calend.put(result, tasksSet);
                }
                result = t.nextTimeAfter(result);
            }
        }
        return calend;
    }
}
