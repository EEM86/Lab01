package ua.edu.sumdu.j2se.Yermolenko.tasks;


import ua.edu.sumdu.j2se.Yermolenko.tasks.Controller.Controller;
import ua.edu.sumdu.j2se.Yermolenko.tasks.Model.TasksService;
import ua.edu.sumdu.j2se.Yermolenko.tasks.View.ConsoleView;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        ConsoleView view = new ConsoleView();
        TasksService tasksService = new TasksService();

        Controller controller = new Controller(tasksService, view);
        controller.mainMenu();
    }
}
