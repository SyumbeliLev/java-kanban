package Epic;

import manager.Managers;
import manager.taskManagers.TaskManager;
import model.Epic;
import model.Progress;
import model.Subtack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    TaskManager manager;
    Epic epic;

    @BeforeEach
    public void createManagerAndEpic() {
        manager = Managers.getDefault();
        epic = new Epic("title", "description");
        manager.addEpic(epic);
    }

    @Test
    public void statusNewEpicForEmptySubtaskList() {
        assertEquals(Progress.NEW, epic.getStatus());
    }

    @Test
    public void statusEpicWithNewSubtasks() {
        Subtack subtackNew = new Subtack("title", "description", Progress.NEW, 20, LocalDateTime.of(2022, 2, 1, 1, 1),epic.getId());
        manager.addSubtack(subtackNew);
        assertEquals(Progress.NEW, epic.getStatus());
    }

    @Test
    public void statusEpicWithDoneSubtasks() {
        Subtack subtackDone = new Subtack("title", "description", Progress.DONE, 20,LocalDateTime.of(2022, 2, 1, 1, 1),epic.getId());
        manager.addSubtack(subtackDone);
        assertEquals(Progress.DONE, epic.getStatus());
    }

    @Test
    public void statusEpicWithNewAndDoneSubtasks() {
        Subtack subtackNew = new Subtack("title", "description", Progress.NEW,20,LocalDateTime.of(2022, 2, 1, 1, 1),epic.getId());
        Subtack subtackDone = new Subtack("title", "description", Progress.DONE,20,LocalDateTime.of(2022, 2, 1, 1, 1),epic.getId());
        manager.addSubtack(subtackNew);
        manager.addSubtack(subtackDone);
        assertEquals(Progress.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void statusEpicWithInProgressSubtasks() {
        Subtack subtackInProgress = new Subtack("title", "description", Progress.IN_PROGRESS,20,LocalDateTime.of(2022, 2, 1, 1, 1),epic.getId());
        manager.addSubtack(subtackInProgress);
        assertEquals(Progress.IN_PROGRESS, epic.getStatus());
    }


}