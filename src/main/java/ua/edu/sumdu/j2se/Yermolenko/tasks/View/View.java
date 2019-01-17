package ua.edu.sumdu.j2se.Yermolenko.tasks.View;

import ua.edu.sumdu.j2se.Yermolenko.tasks.Model.Task;
import ua.edu.sumdu.j2se.Yermolenko.tasks.Model.TaskList;

public interface View {
    public void printMainMenu(TaskList taskList);
    public void addTaskMenu();
    public void successTaskAdding();
    public void taskView(Task task);
    public void editTaskView(Task task);
    public void removeTaskView(Task task);
    public void successTaskRemoving();
}
