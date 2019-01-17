package ua.edu.sumdu.j2se.Yermolenko.tasks.Model;

import org.apache.log4j.Logger;
import ua.edu.sumdu.j2se.Yermolenko.tasks.Controller.Controller;
import ua.edu.sumdu.j2se.Yermolenko.tasks.View.ConsoleView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class TasksService {
    private final static Logger logger = Logger.getLogger(TasksService.class);
    private TaskList taskList = loadTaskList();
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private SimpleDateFormat patternDate = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    private boolean daemonWork = true;

    public TaskList loadTaskList() {
        TaskList taskList = new ArrayTaskList();
        File dir = new File("D://Text//JAVA//Netcracker//java.course.dev//ide//src//main//resources");
        try {
            TaskIO.readText(taskList, new File(dir,"data.txt"));
        } catch (ParseException e) {
            logger.error("Problem with file 'data.txt', make sure this file is ok" + e);
        }
        return taskList;
    }

    public TaskList getTaskList() {
        return taskList;
    }

    public void addNewTask() throws IOException {
        String title = "";
        String input = "";
        Date time = null;
        Date start = null;
        Date end = null;
        int interval = 0;
        Task task = null;

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
            taskList.add(task);
            daemonWork = false;
        } else addNewTask();
    }

    public Task getTaskModel(int digit) {
        return taskList.getTask(digit - 1);
    }

    public void editTaskModel(Task task) throws IOException {

        Date time = null;
        Date start = null;
        Date end = null;
        int interval = 0;

        while (true) {
            int input = Integer.parseInt(reader.readLine());
            if (input == Controller.SETTITLE) {
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
                    dateFormatForUser();
                    time = handleDateParseException();
                    if (confirmation()) {
                        task.setTime(time);
                        System.out.println("\n*****************");
                        logger.info("time was changed");
                        System.out.println("*****************\n");
                        daemonWork = false;
                    }
                } else {
                    System.out.println("Enter start time.");
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
                    }
                }
                break;
            }
            else if (input == Controller.SETACTIVE) {
                System.out.println("'Y' makes the task active. 'N' - inactive.");
                if (confirmation()) {
                    task.setActive(true);
                } else task.setActive(false);
            break;
            }
            else {
                logger.warn(ConsoleView.ANSI_BLUE + "Please, type only " + Controller.SETTITLE + ", "
                        + Controller.SETTIME + " or " + Controller.SETACTIVE + ConsoleView.ANSI_RESET);
            }
        }
    }

    public boolean confirmation() throws IOException {
        System.out.println(ConsoleView.ANSI_BLUE + "Please, confirm. (Y/N)" + ConsoleView.ANSI_RESET);
        String confirm = reader.readLine();
        while (true) {
            if ("y".equalsIgnoreCase(confirm)) {
                return true;
            }
            if ("n".equalsIgnoreCase(confirm)) {
                break;
            } else {
                System.out.println(ConsoleView.ANSI_RED + "Please write only Y or N" + ConsoleView.ANSI_RESET);
                confirm = reader.readLine();
            }
        }
    return false;
    }

    public void removeTaskModel(Task task) {
        taskList.remove(task);
        daemonWork = false;
    }

    public void saveBeforeQuit() {
        File dir = new File("D://Text//JAVA//Netcracker//java.course.dev//ide//src//main//resources");
        TaskIO.writeText(taskList, new File(dir,"data.txt"));
    }

    public void dateFormatForUser() {
        System.out.println("Please, use such format: day-month-year hours:minutes. For example: 01-11-2019 15:25");
    }

    public void showCalendar() throws IOException {
        System.out.println("Enter start date: ");
        Date start = handleDateParseException();
        System.out.println("Enter end date: ");
        Date end = handleDateParseException();
        System.out.println(ConsoleView.ANSI_BLACK_BACKGROUND + ConsoleView.ANSI_YELLOW + "Date\t\t\t\t\t\t\t" +
                "Tasks\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + ConsoleView.ANSI_RESET);
        for (Map.Entry<Date, Set<Task>> pair: (Tasks.calendar(taskList, start, end)).entrySet()) {
            Date gdfg = pair.getKey();
            Set s1 = pair.getValue();
            System.out.println(pair.getKey() + "\t" + pair.getValue());
        }
    }

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

    public void notifyService() {
        Thread notifyThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    daemonWork = true;
                    Date current = new Date();
                    Task task = nearestTask(taskList);

                    if (task != null) {
                        Date tmp = task.nextTimeAfter(current);
                        while (daemonWork) {
                            if (tmp == null || tmp.compareTo(current) <= 0) {
                                System.out.println("\n" + ConsoleView.ANSI_PURPLE + "Current time is " + current.toString() + ". It's task time: " + task.getTitle() + ConsoleView.ANSI_RESET);
                                try {
                                    Thread.sleep(1000);
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
    }
}




