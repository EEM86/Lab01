package ua.edu.sumdu.j2se.Yermolenko.tasks.View;

import org.apache.log4j.Logger;
import ua.edu.sumdu.j2se.Yermolenko.tasks.Controller.Controller;
import ua.edu.sumdu.j2se.Yermolenko.tasks.Model.Task;
import ua.edu.sumdu.j2se.Yermolenko.tasks.Model.TaskList;

/**
 * Class ConsoleView according to MVC pattern, realise View interface.
 * Prints menus and action options for user.
 */
public class ConsoleView implements View {
    private final static Logger logger = Logger.getLogger(ConsoleView.class);


    /**
     * Prints Main Menu View
     * Prints all tasks and options for user to work with.
     * @param taskList list of tasks
     */
    @Override
    public void printMainMenu(TaskList taskList) {
        System.out.println("==============================================================");
        System.out.println("You have " + taskList);
        System.out.println("==============================================================");
        System.out.println("\nYou can choose such options:");
        System.out.println(Controller.ADDTASK + " - Add new Task");
        System.out.println(Controller.VIEWTASK + " - View specific Task");
        System.out.println(Controller.SELECTDATE + " - Select a date");
        System.out.println(Controller.QUIT + " - Exit the program");
        System.out.println("\n==============================================================\n");
    }

    /**
     * Prints info about starting adding a new task.
     */
    @Override
    public void addTaskMenu() {
        System.out.println("*****************************************************************");
        System.out.println("Adding new task");
    }

    /**
     * Prints if a task was added to the list of tasks.
     */
    @Override
    public void successTaskAdding() {
        System.out.println("Task was successfully added");
        System.out.println("*****************************************************************\n");
    }

    /**
     * Prints actual info about specific task and options to work with it.
     * @param task the specific task from a list of tasks.
     */
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

    /**
     * Prints available options to edit the task.
     * @param task the specific task from a list of tasks.
     */
    @Override
    public void editTaskView(Task task) {
        System.out.println(Controller.SETTITLE + " - Set title\n" + Controller.SETTIME + " - Set time\n" +
                Controller.SETACTIVE + " - Set activity");
    }

    /**
     * Prints confirmation menu to prevent from deleting the task by mistake.
     * @param task the specific task from a list of tasks.
     */
    @Override
    public void removeTaskView(Task task) {
        System.out.println("--------------------------------------------------------------------");
        System.out.println("You are removing task:\n"+ task);
        System.out.println("Are you sure?");
    }

    /**
     * Prints if a task was removed from the list of tasks.
     */
    @Override
    public void successTaskRemoving() {
        System.out.println("Task was successfully removed");
        System.out.println("--------------------------------------------------------------------\n");
    }

}
