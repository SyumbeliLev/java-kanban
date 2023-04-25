package tests;

import model.Epic;
import model.Progress;
import model.Subtack;

import static manager.Managers.getDefault;
import static manager.Managers.getDefaultHistory;

public class Test {
    public static void allTest() {


        Epic epic1 = new Epic("Уборка дома", "в команте");
        getDefault().addEpic(epic1);

        Subtack subtack1 = new Subtack("Помыть пол", "шваброй",Progress.NEW, epic1.getId());
        getDefault().addSubtack(subtack1);

        Subtack subtack2 = new Subtack("Протереть пыль", "тряпкой", Progress.NEW,epic1.getId());
        getDefault().addSubtack(subtack2);

        Epic epic2 = new Epic("Сходить в магазин", "гастраном");
        getDefault().addEpic(epic2);

        Subtack subtack3 = new Subtack("Купить соль", "наличные", Progress.NEW,epic2.getId());
        getDefault().addSubtack(subtack3);




        getDefault().getEpicById(1);
        getDefault().getSubtackById(2);
        getDefault().getSubtackById(3);
        System.out.println(getDefaultHistory().getHistory());






    }
}
