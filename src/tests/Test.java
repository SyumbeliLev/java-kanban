package tests;

import model.Epic;
import model.Progress;
import model.Subtack;
import service.Manager;

public class Test {
    public static void allTest() {
        Manager manager = new Manager();



        Epic epic1 = new Epic("Уборка дома", "в команте", 0);
        manager.addEpic(epic1);

        Subtack subtack1 = new Subtack("Помыть пол", "шваброй",Progress.NEW,0, epic1.getId());
        manager.addSubtack(subtack1);

        Subtack subtack2 = new Subtack("Протереть пыль", "тряпкой", Progress.NEW,0,epic1.getId());
        manager.addSubtack(subtack2);

        Epic epic2 = new Epic("Сходить в магазин", "гастраном", 0);
        manager.addEpic(epic2);

        Subtack subtack3 = new Subtack("Купить соль", "наличные", Progress.NEW,0,epic2.getId());
        manager.addSubtack(subtack3);





        System.out.println(manager.getSubtackEpic(1));
        System.out.println("...........................");
        subtack1.setStatus(Progress.IN_PROGRESS);
        manager.removeSubtackById(3);

        manager.updateSubtack(subtack1);

        System.out.println(manager.getSubtackEpic(1));
        System.out.println(manager.getByIdEpic(1));







    }
}
