package tests;

import manager.taskManagers.FileBackedTasksManager;
import model.Epic;
import model.Progress;
import model.Subtack;

import java.io.File;


public class Test {
    public static void allTest() {

        File file = new File("save.csv");   //не разобрался как создавать файл в папе resources
        FileBackedTasksManager manager = new FileBackedTasksManager(file);
        FileBackedTasksManager manager2 = new FileBackedTasksManager(file);


        Epic epic1 = new Epic("Уборка дома", "в команте");

        manager.addEpic(epic1);
        Subtack subtack1 = new Subtack("Помыть пол", "шваброй", Progress.NEW, epic1.getId());
        manager.addSubtack(subtack1);

        Subtack subtack2 = new Subtack("Протереть пыль", "тряпкой", Progress.NEW, epic1.getId());
        manager.addSubtack(subtack2);

        Subtack subtack3 = new Subtack("Купить соль", "наличные", Progress.NEW, epic1.getId());
        manager.addSubtack(subtack3);

        Epic epic2 = new Epic("Сходить в магазин", "гастраном");
        manager.addEpic(epic2);


       // manager.getSubtackList();
        manager.getEpicById(1);
        manager.getSubtackById(2);


        System.out.println(manager.getHistory());


    }
}
