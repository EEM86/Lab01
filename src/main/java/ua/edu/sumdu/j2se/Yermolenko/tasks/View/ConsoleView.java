package ua.edu.sumdu.j2se.Yermolenko.tasks.View;

import org.apache.log4j.Logger;
import ua.edu.sumdu.j2se.Yermolenko.tasks.Controller.Controller;
import ua.edu.sumdu.j2se.Yermolenko.tasks.Model.Task;
import ua.edu.sumdu.j2se.Yermolenko.tasks.Model.TaskList;

public class ConsoleView implements View {
    private final static Logger logger = Logger.getLogger(ConsoleView.class);
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";

    @Override
    public void printMainMenu(TaskList taskList) {
        System.out.println("==============================================================");
        System.out.println("You have " + taskList);
        System.out.println("==============================================================");
        System.out.println(ANSI_BLUE + "\nYou can choose such options:" + ANSI_RESET);
        System.out.println(Controller.ADDTASK + " - Add new Task");
        System.out.println(Controller.VIEWTASK + " - View specific Task");
        System.out.println(Controller.SELECTDATE + " - Select a date");
        System.out.println(Controller.QUIT + " - Exit the program");
        System.out.println("\n==============================================================\n");
    }

    @Override
    public void addTaskMenu() {
        System.out.println("*****************************************************************");
        System.out.println("Adding new task");
    }

    @Override
    public void successTaskAdding() {
        System.out.println(ANSI_PURPLE + "Task was successfully added" + ANSI_RESET);
        System.out.println("*****************************************************************\n");
    }

    @Override
    public void taskView(Task task) {
        System.out.println("Task title is: " + task.getTitle());
        System.out.println(task.isActive() ? "Task is active" : "Task isn't active");
        if (task.isRepeated()) {
            System.out.println("Start time is: " + task.getStartTime() + "\nEnd time is: "
                    + task.getEndTime() + "\ninterval is " + task.getRepeatInterval());
        } else {
            System.out.println("Time is " + task.getTime());
        }
        System.out.println("\n" + Controller.EDITTASK + " - edit Task\n" + Controller.REMOVETASK + " - remove Task\n" +
                Controller.BACK + " - back to previous menu");
    }

    @Override
    public void editTaskView(Task task) {
        System.out.println(Controller.SETTITLE + " - Set title\n" + Controller.SETTIME + " - Set time\n" +
                Controller.SETACTIVE + " - Set activity");
    }

    @Override
    public void removeTaskView(Task task) {
        System.out.println("--------------------------------------------------------------------");
        System.out.println(ANSI_RED + "You are removing task:\n" + ANSI_RESET + ANSI_BLUE + task + ANSI_RESET);
        System.out.println("Are you sure?");
    }

    @Override
    public void successTaskRemoving() {
        System.out.println(ANSI_PURPLE + "Task was successfully removed" + ANSI_RESET);
        System.out.println("--------------------------------------------------------------------\n");
    }

}
