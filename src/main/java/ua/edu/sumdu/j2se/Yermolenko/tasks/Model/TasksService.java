package ua.edu.sumdu.j2se.Yermolenko.tasks.Model;

import org.apache.log4j.Logger;
import ua.edu.sumdu.j2se.Yermolenko.tasks.Controller.Controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Class TasksService relates to Model according to MVC pattern.
 * Describes the logic of the program.
 */
public class TasksService {
    private final static Logger logger = Logger.getLogger(TasksService.class);
    private TaskList taskList = loadTaskList();
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private SimpleDateFormat patternDate = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    private boolean daemonWork = true;

    /**
     * Load list of tasks from a file.
     * @return ArrayTaskList
     * Catches ParseException if can't parse the file.
     */
    public TaskList loadTaskList() {
        TaskList taskList = new ArrayTaskList();
        try {
            TaskIO.readText(taskList, new File("data.txt"));
            logger.debug("Opened file 'data.txt' from root directory");
        }
        catch (ParseException e) {
            logger.error("Error. Can't parse the file " + e);
        }
        return taskList;
    }

    public TaskList getTaskList() {
        return taskList;
    }

    /**
     * Adds new task to a list of tasks.
     * After adding a task to task list set the field daemonWork to false
     * @see #notifyService
     * @throws IOException if can't read the string from a console and
     * @see #handleDateParseException
     */
    public void addNewTask() throws IOException {
        String title = "";
        String input = "";
        Date time = null;
        Date start = null;
        Date end = null;
        int interval = 0;
        Task task = null;

        logger.debug("Starting adding a new task");
        System.out.println("Enter a title of the task");
        title = reader.readLine();
        if (!title.isEmpty()) {
            System.out.println("Is the task repeatable?");
            if (confirmation()) {
                System.out.println("Enter start time");
                dateFormatForUser();
                start = handleDateParseException();
                System.out.println("Enter end time");
                dateFormatForUser();
                end = handleDateParseException();
                System.out.println("Enter interval (in seconds).");
                input = reader.readLine();
                interval = Integer.parseInt(input);
                task = new Task(title, start, end, interval);
            } else {
                System.out.println("Enter time.");
                dateFormatForUser();
                time = handleDateParseException();
                task = new Task(title, time);
            }
            System.out.println("Set task to active?");
            if (confirmation()) {
                task.setActive(true);
            }
            getTaskList().add(task);
            logger.debug("New task added to a task list");
            daemonWork = false;
            logger.debug("daemonWork set to false");
        } else addNewTask();
    }

    /**
     * Returns task with index = digit - 1 from array list
     * @param digit id of a task in a view.
     * @return Task with digit id.
     */
    public Task getTaskModel(int digit) {
        return taskList.getTask(digit - 1);
    }

    /**
     * Logic for task editing. Works with user's input.
     * If user chooses
     * 6 - he can set a new task title
     * 7 - he can set a new task time
     * 8 - he can set activity of the task
     * If user entered another digit he would start again from this menu.
     * @param task the specific task from a list of tasks.
     * @throws IOException if can't read the string from a console
     */

    public void editTaskModel(Task task) throws IOException {

        Date time = null;
        Date start = null;
        Date end = null;
        int interval = 0;

        while (true) {
            int input = Integer.parseInt(reader.readLine());
            if (input == Controller.SETTITLE) {
                logger.debug("Setting new task title");
                System.out.println("Write new title: ");
                String newTitle = reader.readLine();
                if (!newTitle.isEmpty()) {
                    if (confirmation()) {
                        task.setTitle(newTitle);
                        System.out.println("\n***************************");
                        logger.info("title was changed");
                        System.out.println("****************************\n");
                        daemonWork = false;
                        break;
                    }
                } break;
            }
            else if (input == Controller.SETTIME) {
                if (!task.isRepeated()) {
                    System.out.println("Enter time.");
                    logger.debug("Setting new task time of a not repeatable task");
                    dateFormatForUser();
                    time = handleDateParseException();
                    if (confirmation()) {
                        task.setTime(time);
                        System.out.println("\n*****************");
                        logger.info("time was changed");
                        System.out.println("*****************\n");
                        daemonWork = false;
                        logger.debug("daemonWork set to false after changing the time of unrepeated task");
                    }
                } else {
                    System.out.println("Enter start time.");
                    logger.debug("Setting new task time of a repeatable task");
                    dateFormatForUser();
                    start = handleDateParseException();
                    System.out.println("Enter end time.");
                    dateFormatForUser();
                    end = handleDateParseException();
                    System.out.println("Write interval in seconds");
                    interval = Integer.parseInt(reader.readLine());
                    if (confirmation()) {
                        task.setTime(start, end, interval);
                        System.out.println("\n*****************");
                        logger.info("time was changed");
                        System.out.println("*****************\n");
                        daemonWork = false;
                        logger.debug("daemonWork set to false after changing the time of repeated task");
                    }
                }
                break;
            }
            else if (input == Controller.SETACTIVE) {
                System.out.println("'Y' makes the task active. 'N' - inactive.");
                logger.debug("Setting activity of a task");
                if (confirmation()) {
                    task.setActive(true);
                } else task.setActive(false);
            break;
            }
            else {
                logger.warn("Please, type only " + Controller.SETTITLE + ", "
                        + Controller.SETTIME + " or " + Controller.SETACTIVE);
            }
        }
    }

    /**
     * Handles if user enters wrong option.
     * @return true if user writes 'y' or 'Y' and false if user writes 'n' or 'N'
     * @throws IOException if can't read the string from a console
     */
    public boolean confirmation() throws IOException {
        System.out.println("Please, confirm. (Y/N)");
        String confirm = reader.readLine();
        while (true) {
            if ("y".equalsIgnoreCase(confirm)) {
                return true;
            }
            if ("n".equalsIgnoreCase(confirm)) {
                break;
            } else {
                System.out.println("Please write only Y or N");
                confirm = reader.readLine();
            }
        }
    return false;
    }

    public void removeTaskModel(Task task) {
        taskList.remove(task);
        daemonWork = false;
    }

    /**
     * Saves task list to a file before exit the program.
     */
    public void saveBeforeQuit() {
        TaskIO.writeText(taskList, new File("data.txt"));
    }

    /**
     * Prints message about correct date format to enter from a console.
     */
    public void dateFormatForUser() {
        System.out.println("Please, use such format: day-month-year hours:minutes. For example: 01-11-2019 15:25");
    }

    /**
     * Prints calendar of tasks from start date to end date.
     * @throws IOException if can't read the string from a console.
     */
    public void showCalendar() throws IOException {
        System.out.println("Enter start date: ");
        dateFormatForUser();
        Date start = handleDateParseException();
        System.out.println("Enter end date: ");
        dateFormatForUser();
        Date end = handleDateParseException();
        System.out.println("Date\t\t\t\t\t\t\t" +
                "Tasks\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t");
        for (Map.Entry<Date, Set<Task>> pair: (Tasks.calendar(taskList, start, end)).entrySet()) {
            for (Task t: pair.getValue()) {
                System.out.println(pair.getKey() + "\t" + t.getTitle());
            }
        }
    }

    /**
     * Handles exception when user types incorrect date format
     * @return correct date according to date format.
     * @throws IOException if can't read the string from a console
     */
    public Date handleDateParseException() throws IOException {
        Date tmp = null;
        String input = reader.readLine();
        while (true) {
            try {
                tmp = patternDate.parse(input);
                break;
            } catch (ParseException e) {
                dateFormatForUser();
                input = reader.readLine();
            }
        }
        return tmp;
    }

    /**
     * Returns a task with nearest execute time from the current moment.
     * @param tasks tasks list.
     * @return a task with nearest execute time.
     */
    public Task nearestTask(TaskList tasks) {
        Date current = new Date();
        Task result = null;
        Date resultDate = null;
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.getTask(i);
            Date nextDate = task.nextTimeAfter(current);
            if ((nextDate != null && resultDate == null) || (nextDate != null && nextDate.compareTo(resultDate) <= 0)){
                result = task;
                resultDate = nextDate;
            }
        }
        return result;
    }

    /**
     * Create a daemon thread for notification when it is the time of a task.
     *
     * Gets a task with nearest time execute comparing to current time. Then compares task execute time and current time.
     * As method ignores milliseconds, current time can be later than actual task execute time, so this method prints task time in console.
     *
     * If prints a notifying, sleeps for a 1 second for correct method working.
     * Without sleep method prints a notify several times because this program works too quickly.
     *
     * If a task has just been added or changed, field daemonWork set to false.
     * When the daemonWork is false, this method begins work with an updated task list.
     */
    public void notifyService() {
        Thread notifyThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        logger.error("Thread can't sleep" + e);
                    }
                    daemonWork = true;
                    Date current = new Date();
                    Task task = nearestTask(getTaskList());

                    if (task != null) {
                        Date tmp = task.nextTimeAfter(current);
                        while (daemonWork) {
                            if (tmp == null || tmp.compareTo(current) <= 0) {
                                System.out.println("\n" + "Current time is " + current.toString() + ". It's task time: " + task.getTitle());
                                try {
                                    Thread.sleep(1000);
                                    logger.debug("Daemon thread sleeps for a second");
                                } catch (InterruptedException e) {
                                    logger.error("Thread can't sleep" + e);
                                }
                                break;
                            }
                        current = new Date();
                        }
                    }
                }
            }
        });
        notifyThread.setDaemon(true);
        notifyThread.start();
        logger.debug("A daemon thread just has been started");
    }
}
