package ua.edu.sumdu.j2se.Yermolenko.tasks.Controller;

import org.apache.log4j.Logger;
import ua.edu.sumdu.j2se.Yermolenko.tasks.Model.TasksService;
import ua.edu.sumdu.j2se.Yermolenko.tasks.View.ConsoleView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Class Controller according to MVC pattern.
 * Combine Model logic and View data.
 */
public class Controller {
    private final static Logger logger = Logger.getLogger(Controller.class);
    private ConsoleView consoleView;
    private TasksService tasksService;

    public static final int ADDTASK = 1;
    public static int VIEWTASK = 2;
    public static int SELECTDATE = 3;
    public static int EDITTASK = 4;
    public static int REMOVETASK = 5;
    public static int SETTITLE = 6;
    public static int SETTIME = 7;
    public static int SETACTIVE = 8;
    public static int BACK = 9;
    public static int QUIT = 0;

    public Controller(TasksService tasksService, ConsoleView consoleView) {
        this.tasksService = tasksService;
        this.consoleView = consoleView;
    }

    /**
     * Main Menu of Task Manager.
     */
    public void mainMenu() {
        consoleView.printMainMenu(tasksService.getTaskList());
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        tasksService.notifyService();
        logger.debug("Daemon thread for task notifying has been started");


        while (true) {
            try {
                String input = reader.readLine();
                if (ADDTASK == Integer.parseInt(input)) {
                    consoleView.addTaskMenu();
                    tasksService.addNewTask();
                    consoleView.successTaskAdding();
                }
                if (VIEWTASK == Integer.parseInt(input)) {
                    if (tasksService.getTaskList().size() == 0) {
                        System.out.println("Please, add a task at first. Returning to main menu...\n");
                        Thread.sleep(1000);
                    } else {
                        System.out.println("Write task id for viewing. Only digit.");
                        int digit = 0;
                        digit = Integer.parseInt(reader.readLine());
                        while (digit > tasksService.getTaskList().size()) {
                            System.out.println("Incorrect task ID. Try again.");
                            digit = Integer.parseInt(reader.readLine());
                        }
                            while (true) {
                                consoleView.taskView(tasksService.getTaskModel(digit));
                                input = reader.readLine();
                                if (EDITTASK == Integer.parseInt(input)) {
                                    consoleView.editTaskView(tasksService.getTaskModel(digit));
                                    tasksService.editTaskModel(tasksService.getTaskModel(digit));
                                } else if (REMOVETASK == Integer.parseInt(input)) {
                                    consoleView.removeTaskView(tasksService.getTaskModel(digit));
                                    if (tasksService.confirmation()) {
                                        tasksService.removeTaskModel(tasksService.getTaskModel(digit));
                                        consoleView.successTaskRemoving();
                                        consoleView.printMainMenu(tasksService.getTaskList());
                                    }
                                } else if (BACK == Integer.parseInt(input)) {
                                    break;
                                } else {
                                    System.out.println("==============================================================");
                                    System.out.println("Please, type only " + EDITTASK + ", " + REMOVETASK + " or " + BACK);
                                    System.out.println("==============================================================");
                                    Thread.sleep(500);
                                }
                            }
                    }
                }
                if (SELECTDATE == Integer.parseInt(input)) {
                    if (tasksService.getTaskList().size() == 0) {
                        System.out.println("Please, add a task at first. Returning to main menu...\n");
                        Thread.sleep(1000);
                    } else {
                        System.out.println("Select date");
                        tasksService.showCalendar();
                        System.out.println("\nType " + Controller.BACK + " to return to previous menu\n");
                        input = reader.readLine();
                        while (BACK != Integer.parseInt(input)) {
                            System.out.println("\nType " + Controller.BACK + " to return to previous menu");
                            input = reader.readLine();
                        }
                        consoleView.printMainMenu(tasksService.getTaskList());
                    }
                } else if (QUIT == Integer.parseInt(input)) {
                    System.out.println("Quit the program?");
                    if (!tasksService.confirmation()) {
                        consoleView.printMainMenu(tasksService.getTaskList());
                    } else {
                        tasksService.saveBeforeQuit();
                        System.out.println("Bye bye");
                        System.exit(0);
                    }
                }
                consoleView.printMainMenu(tasksService.getTaskList());
            } catch (NumberFormatException e) {
                logger.error("Incorrect option. Try again.");
                consoleView.printMainMenu(tasksService.getTaskList());
            } catch (IOException e) {
                logger.error("IOException" + e);
                consoleView.printMainMenu(tasksService.getTaskList());
            } catch (InterruptedException e) {
                logger.error("Thread can't sleep" + e);
                consoleView.printMainMenu(tasksService.getTaskList());
            }
        }
    }
}
