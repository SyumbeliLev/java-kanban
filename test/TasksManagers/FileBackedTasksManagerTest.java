package TasksManagers;

import manager.taskManagers.FileBackedTasksManager;
import manager.taskManagers.InMemoryTaskManager;
import model.Epic;
import model.Progress;
import model.Task;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTasksManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    Task task1;
    Epic epic1;
    Epic epic2;
    FileBackedTasksManager manager;
    File file;

    @BeforeEach
    public void loadFileBackedTasksManager() {
        file = new File("saveTest.csv");
        taskManager = new FileBackedTasksManager(file);
        manager = new FileBackedTasksManager(file);

        task1 = new Task("title", "description", Progress.NEW, 14, LocalDateTime.of(2003, 1, 1, 1, 1));
        epic1 = new Epic("title", "description");
        epic2 = new Epic("title", "description");
    }

    @AfterEach
    public void clean() throws IOException {
        new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8));
        manager = null;
    }


    @Test
    public void loadManagerEmptyTest() {
        FileBackedTasksManager managerLoad = FileBackedTasksManager.loadFromFile(file);
        assertEquals(manager.getTaskList(), managerLoad.getTaskList(), "Таски после загрузки не сходятся");
        assertEquals(manager.getEpicList(), managerLoad.getEpicList(), "Эпики после загрузки не сходятся");
        assertEquals(manager.getSubtackList(), managerLoad.getSubtackList(), "Сабтаски после загрузки не сходятся");
        assertEquals(manager.getHistory(), managerLoad.getHistory(), "История после загрузки не сходится");
    }

    @Test
    public void loadManagerEmptyHistoryTest() {
        manager.addEpic(epic1);
        manager.addTask(task1);
        manager.addEpic(epic2);
        assertNotNull(manager.getEpicList(), "список эпиков не расширяется");
        FileBackedTasksManager managerLoad = FileBackedTasksManager.loadFromFile(file);

        assertEquals(manager.getTaskList(), managerLoad.getTaskList(), "Таски после загрузки не сходятся");
        assertEquals(manager.getEpicList(), managerLoad.getEpicList(), "Эпики после загрузки не сходятся");
        assertEquals(manager.getSubtackList(), managerLoad.getSubtackList(), "Сабтаски после загрузки не сходятся");
        assertEquals(manager.getHistory(), managerLoad.getHistory(), "История после загрузки не сходится");
        assertEquals(List.of(), managerLoad.getHistory(), "В историю добавились таски");
    }

    @Test
    public void loadManagerHistoryTest() {
        manager.addEpic(epic1);
        manager.addTask(task1);
        manager.addEpic(epic2);

        manager.getTaskById(2);
        manager.getEpicById(1);
        manager.getEpicById(3);
        FileBackedTasksManager managerLoad = FileBackedTasksManager.loadFromFile(file);
        assertEquals(manager.getHistory(), managerLoad.getHistory(), "История сохраняется неверно");
    }

}
