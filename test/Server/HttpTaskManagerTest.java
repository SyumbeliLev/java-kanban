package Server;

import manager.Managers;
import manager.taskManagers.HttpTaskManager;
import model.Epic;
import model.Progress;
import model.Subtack;
import model.Task;
import org.junit.jupiter.api.Test;
import server.KVServer;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class HttpTaskManagerTest {
    @Test
    public void loadFromServerTest() throws IOException {
        new KVServer().start();
        HttpTaskManager httpManager = Managers.getDefault();

        Task task1 = new Task("Task1", "descriptionTask", Progress.NEW, 233, LocalDateTime.now());
        Task task2 = new Task("Task2", "descriptionTask", Progress.NEW, 233, LocalDateTime.of(2000, 1, 2, 2, 2));
        Epic epic1 = new Epic("Epic2", "descriptionEpic");
        Subtack subtack1 = new Subtack("Subtack1", "descriptionSubtack", Progress.NEW, 233, LocalDateTime.of(2002, 1, 2, 2, 2), 3);
        Subtack subtack2 = new Subtack("Subtack2", "descriptionSubtack", Progress.NEW, 233, LocalDateTime.of(2004, 1, 2, 2, 2), 3);

        httpManager.addTask(task1);
        httpManager.addTask(task2);
        httpManager.addEpic(epic1);
        httpManager.addSubtack(subtack1);
        httpManager.addSubtack(subtack2);

        httpManager.getTaskById(2);
        httpManager.getSubtackById(5);
        httpManager.getEpicById(3);


        HttpTaskManager managerLoadFromServer =new HttpTaskManager("http://localhost:8078/",true);

        assertIterableEquals(httpManager.getTaskList(), managerLoadFromServer.getTaskList());
        assertIterableEquals(httpManager.getEpicList(), managerLoadFromServer.getEpicList());
        assertIterableEquals(httpManager.getSubtackList(), managerLoadFromServer.getSubtackList());
        assertIterableEquals(httpManager.getHistory(), managerLoadFromServer.getHistory());
        assertIterableEquals(httpManager.getPrioritizedTasks(), managerLoadFromServer.getPrioritizedTasks());

    }
}
