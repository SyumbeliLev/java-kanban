package tests;

import manager.Managers;
import manager.taskManagers.FileBackedTasksManager;
import manager.taskManagers.TaskManager;
import model.Epic;
import model.Progress;
import model.Subtack;
import model.Task;

import java.io.File;
import java.nio.file.Path;

public class Test {
    public static void allTest() {

        Path path = Path.of("resources/save_file.csv");
        File file = new File(String.valueOf(path));
        FileBackedTasksManager fileBackedTasksManager= new FileBackedTasksManager(file);




        Epic epic1 = new Epic("Уборка дома", "в команте");

        fileBackedTasksManager.addEpic(epic1);
        Subtack subtack1 = new Subtack("Помыть пол", "шваброй", Progress.NEW, epic1.getId());
        fileBackedTasksManager.addSubtack(subtack1);

        Subtack subtack2 = new Subtack("Протереть пыль", "тряпкой", Progress.NEW, epic1.getId());
        fileBackedTasksManager.addSubtack(subtack2);

        Subtack subtack3 = new Subtack("Купить соль", "наличные", Progress.NEW, epic1.getId());
        fileBackedTasksManager.addSubtack(subtack3);

        Epic epic2 = new Epic("Сходить в магазин", "гастраном");
        fileBackedTasksManager.addEpic(epic2);




       /* taskManager.getSubtackById(2);
        taskManager.getSubtackById(3);
        taskManager.getSubtackById(4);

        taskManager.removeSubtackById(3);

        taskManager.getSubtackById(2);
        taskManager.getEpicById(5);*/



    }
}
