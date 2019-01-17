package ua.edu.sumdu.j2se.Yermolenko.tasks.Model;

import java.io.Serializable;
import java.util.Date;

/**
 This first practice Task was made by
 @author Yermolenko Yevhen
 */

public class Task implements Cloneable, Serializable {

    private String title;
    private Date time = new Date(234234);
    private Date start = new Date(433535);
    private Date end = new Date(235345345);
    private int interval;
    private boolean active = true;
    private boolean repeated = true;

    /**
     * This constructor creates inactive unrepeatable task.
     */

    public Task(String title, Date time) {
        this.title = title;
        this.time = time;
        active = false;
        repeated = false;
    }

    /**
     * This constructor creates inactive task, which works during the timeline.
     */

    public Task(String title, Date start, Date end, int interval) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.interval = interval;
        active = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    /**
     * If the task is repeatable, the method must return startTime
     * of a repeatable task
     */
    public Date getTime() {
        return (isRepeated() ? start : time);
    }
    /**
     * If the task was repeatable, it must change to unrepeatable
     */
    public void setTime(Date time) {
        if (time == null)
            throw new IllegalArgumentException("Time must not be null!");
        else {
            if (isRepeated()) {
                this.start = time;
                this.end = time;
                this.interval = interval = 0;
                this.repeated = false;
            }
            this.time = time;
        }
    }
    /**
     * If the task is unrepeatable, the method must return time
     * of the unrepeatable task
     */
    public Date getStartTime() {
        return (isRepeated() ? start : time);
    }
    /**
     * see getStartTime()
     */
    public Date getEndTime() {
        return (isRepeated() ? end : time);
    }
    /**
     * If the task is unrepeatable, method must return 0
     */
    public int getRepeatInterval() {
        return (isRepeated() ? interval : 0);
    }


    /**
     * If task is unrepeatable, it must be repeatable.
     */

    public void setTime(Date start, Date end, int interval) {
        if (start == null)
            throw new IllegalArgumentException("Start time must be positive!");
        if (end == null)
            throw new IllegalArgumentException("End time must be grater than start time!");
        if (interval == 0)
            throw new IllegalArgumentException("Interval must be grater than zero!");
        else {
            this.start = start;
            this.end = end;
            this.interval = interval;

            if (!isRepeated()) {
                time = start;
                repeated = repeated = true;
            }
        }
    }

    public boolean isRepeated() {
        return (interval == 0 ? false : true);
    }
    /**
     * If the task is not active, it will never run.
     * If the task is active and runs only one time, next run time
     * will be at this only moment or never (if it was run).
     * If the task is active and repeatable, we need to find
     * the next run time.
     */
    public Date nextTimeAfter(Date current) {
        Date copyCurrent = (Date) current.clone();
        if (!isActive())
            return null;
        else {
            if (!isRepeated()) {
                return ((copyCurrent.compareTo(time) == -1) ? time : null);
            }
            else {
                if (copyCurrent.compareTo(end) >= 0)
                    return null;
                if (copyCurrent.compareTo(start) <= 0)
                    return start;
                else {
                    Date nextDate = new Date(start.getTime() + interval * 1000);
                    while (nextDate.compareTo(end) <= 0) {
                        if (copyCurrent.compareTo(nextDate) == -1) {
                            copyCurrent = nextDate;
                            break;
                        }
                        else {
                            nextDate.setTime(nextDate.getTime() + interval * 1000);
                        }
                    }
                    if (nextDate.compareTo(end) > 0) copyCurrent = null;
                    return copyCurrent;
                }
            }
        }
    }


    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if ((o == null) || (o.getClass() != this.getClass()))  return false; // don't use instanceof
        Task compare = (Task) o;
        if (this.isRepeated()) {
            return ((this.getTitle() != null && this.getTitle().equals(compare.getTitle()))
                    && (this.getStartTime() != null && this.getStartTime().equals(compare.getStartTime()))
                    && (this.getEndTime() != null && this.getEndTime().equals(compare.getEndTime()))
                    && this.getRepeatInterval() == compare.getRepeatInterval())
                    && this.isActive() == compare.isActive();
        } else {
            return (this.getTitle() != null && this.getTitle().equals(compare.getTitle())
                    && this.getTime() != null && this.getTime().equals(compare.getTime()))
                    && this.isActive() == compare.isActive();
        }


        //return (title.equals(compare.title)) && (title != null && title.equals(compare.title));

    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + ((title == null) ? 0 : title.hashCode());
        return result;
    }

    @Override
    public String toString() {
        if (this.isRepeated()) {
            return "['" + title + "', " + "start: " + getStartTime()
                    + ", end: " + getEndTime() + " interval: " + getRepeatInterval() + "]";
        } else
        return "['" + title + "', " + getTime() + "]";
    }

    @Override
    public Task clone() throws CloneNotSupportedException {
        Task taskCloning = (Task) super.clone();
            taskCloning.time = (Date) time.clone();
            taskCloning.start = (Date) start.clone();
            taskCloning.end = (Date) end.clone();

        return taskCloning;
    }
}
