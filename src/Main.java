import manager.taskManagers.FileBackedTasksManager;
import model.Epic;
import model.Progress;
import model.Task;

import java.io.File;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        File file = new File("save.csv");

        FileBackedTasksManager manager = new FileBackedTasksManager(file);
        Task task1 = new Task("title", "description", Progress.NEW, 14, LocalDateTime.of(2003, 1, 1, 1, 1));
        Epic epic1 = new Epic("title", "description");

        manager.addTask(task1);
        manager.addEpic(epic1);

        FileBackedTasksManager manager1=FileBackedTasksManager.loadFromFile(file);
        System.out.println(manager1.getTaskList());

    }
}
