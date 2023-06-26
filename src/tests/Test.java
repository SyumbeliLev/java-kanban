package tests;


import manager.Managers;
import manager.taskManagers.FileBackedTasksManager;

import manager.taskManagers.TaskManager;
import model.Epic;
import model.Progress;
import model.Subtack;
import model.Task;

import java.io.File;
import java.time.LocalDateTime;


public class Test {
    public static void allTest() {

        File file = new File("save.csv");
        //FileBackedTasksManager manager = new FileBackedTasksManager(file);
        TaskManager manager = Managers.getDefault();

        Epic epic1 = new Epic("Уборка дома", "в комнате");
        Task task = new Task("Помыть пол", "шваброй", Progress.NEW, 20, LocalDateTime.of(2022, 2, 1, 1, 1));
        manager.addEpic(epic1);
        Subtack subtack1 = new Subtack("Помыть пол", "шваброй", Progress.NEW, 10, LocalDateTime.of(2020, 5, 5, 5, 5), epic1.getId());
        Subtack subtack2 = new Subtack("Помыть пол", "шваброй", Progress.NEW, 50, LocalDateTime.of(2023, 3, 3, 3, 3), epic1.getId());
        manager.addSubtack(subtack1);
        manager.addSubtack(subtack2);
        manager.addTask(task);

        System.out.println(epic1);
        System.out.println(subtack2);
        System.out.println(subtack1);

    }
}
