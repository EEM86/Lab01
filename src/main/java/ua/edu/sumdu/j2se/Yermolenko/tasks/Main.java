package ua.edu.sumdu.j2se.Yermolenko.tasks;


import ua.edu.sumdu.j2se.Yermolenko.tasks.Controller.Controller;
import ua.edu.sumdu.j2se.Yermolenko.tasks.Model.TasksService;
import ua.edu.sumdu.j2se.Yermolenko.tasks.View.ConsoleView;

import java.io.IOException;

/**
 * Main class of the Task Manager.
 */
public class Main {
    /**
     * Start point of the program.
     * @param args comand line strings.
     */
    public static void main(String[] args) {

        ConsoleView view = new ConsoleView();
        TasksService tasksService = new TasksService();

        Controller controller = new Controller(tasksService, view);
        controller.mainMenu();
    }
}
