package tests;

import model.Epic;
import model.Progress;
import model.Subtack;
import service.Manager;

public class Test {
    public static void allTest() {
        Manager manager = new Manager();



        Epic epic1 = new Epic("Уборка дома", "в команте",Progress.NEW, 0);
        manager.addEpic(epic1);

        Subtack subtack1 = new Subtack("Помыть пол", "шваброй",Progress.NEW,2, epic1.getId());
        manager.addSubtack(subtack1);

        Subtack subtack2 = new Subtack("Протереть пыль", "тряпкой", Progress.NEW,0,epic1.getId());
        manager.addSubtack(subtack2);

        Epic epic2 = new Epic("Сходить в магазин", "гастраном", Progress.NEW,0);
        manager.addEpic(epic2);

        Subtack subtack3 = new Subtack("Купить соль", "наличные", Progress.NEW,0,epic2.getId());
        manager.addSubtack(subtack3);





        System.out.println(manager.getSubtackByEpic(epic1.getId()));
        System.out.println("...........................");
        System.out.println(manager.getByIdEpic(1));

        subtack1.setStatus(Progress.DONE);

        manager.updateSubtack(subtack1);

        System.out.println(manager.getSubtackByEpic(epic1.getId()));
        System.out.println(manager.getSubtackByEpic(epic2.getId()));






    }
}
