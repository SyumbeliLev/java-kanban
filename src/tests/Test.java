package tests;

import static manager.Managers.getDefault;

import manager.taskManagers.TaskManager;
import model.Epic;
import model.Progress;
import model.Subtack;

public class Test {
    public static void allTest() {
        TaskManager taskManager = getDefault();
        Epic epic1 = new Epic("Уборка дома", "в команте");
        taskManager.addEpic(epic1);

        Subtack subtack1 = new Subtack("Помыть пол", "шваброй", Progress.NEW, epic1.getId());
        taskManager.addSubtack(subtack1);

        Subtack subtack2 = new Subtack("Протереть пыль", "тряпкой", Progress.NEW, epic1.getId());
        taskManager.addSubtack(subtack2);

        Epic epic2 = new Epic("Сходить в магазин", "гастраном");
        taskManager.addEpic(epic2);

        Subtack subtack3 = new Subtack("Купить соль", "наличные", Progress.NEW, epic2.getId());
        taskManager.addSubtack(subtack3);


        taskManager.getEpicById(1);
        taskManager.getSubtackById(2);
        taskManager.getSubtackById(3);
        taskManager.getEpicById(4);
        taskManager.getSubtackById(5);
        taskManager.getEpicById(1);
        taskManager.getSubtackById(2);
        taskManager.getSubtackById(3);
        taskManager.getEpicById(4);
        taskManager.getSubtackById(5);
        taskManager.getEpicById(1);
        System.out.println(taskManager.getHistory());

    }
}
