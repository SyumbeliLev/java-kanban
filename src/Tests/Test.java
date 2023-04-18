package Tests;

import Model.Epic;
import Model.Subtack;
import Model.Task;
import Service.Manager;

public class Test {
    public static void allTest() {
        Manager manager = new Manager();

        Subtack subtack1 = new Subtack("Помыть пол", "шваброй", 0, Task.progress.NEW);
        Subtack subtack2 = new Subtack("Протереть пыль", "тряпкой", 0, Task.progress.NEW);
        Epic epic1 = new Epic("Уборка дома", "в команте", 0, Task.progress.NEW);
        Subtack subtack3 = new Subtack("Купить соль", "наличные", 0, Task.progress.NEW);
        Epic epic2 = new Epic("Сходить в магазин", "гастраном", 0, Task.progress.NEW);

        manager.addSubtack(subtack1);
        manager.addSubtack(subtack2);
        manager.addEpic(epic1);

        manager.addSubtack(subtack3);
        manager.addEpic(epic2);

        System.out.println(manager.getEpicHashMap());
        System.out.println(manager.getSubtackHashMap());
        System.out.println("...........................");

        manager.updateSubtack(new Subtack("Помыть пол", "шваброй", 0, Task.progress.DONE), 1); /*Можно конечно по другом реализовать обновление подзадачи*/
        manager.updateEpic(epic1);

        System.out.println(manager.getEpicHashMap());
        System.out.println(manager.getSubtackHashMap());
        System.out.println("...........................");

        manager.removeSubtackById(1);
        System.out.println(manager.getSubtackHashMap());
        System.out.println("...........................");

        manager.removeEpicById(3); /*сделал удалние всех подзадач эпика, при его удаление*/
        System.out.println(manager.getEpicHashMap());
        System.out.println(manager.getSubtackHashMap());

    }
}
