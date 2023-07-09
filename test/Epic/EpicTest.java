package Epic;

import manager.Managers;
import manager.taskManagers.TaskManager;
import model.Epic;
import model.Progress;
import model.Subtack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    private TaskManager manager;
    private Epic epic;

    @BeforeEach
    public void createManagerAndEpic() throws IOException, InterruptedException {
        manager = Managers.getDefault();
        epic = new Epic("title", "description");
        manager.addEpic(epic);
    }

    @Test
    public void EpicStartTimeTest(){
        manager.addEpic(epic);
        assertNull(epic.getStartTime());

        Subtack subtackFirstTime = new Subtack("title", "description", Progress.NEW, 15,LocalDateTime.of(2000,1,1,1,1),epic.getId());
        Subtack subtackSecondTime = new Subtack("title", "description", Progress.NEW, 15,LocalDateTime.of(2002, 2, 2, 2, 1),epic.getId());
        manager.addSubtack(subtackFirstTime);
        manager.addSubtack(subtackSecondTime);

        assertEquals(epic.getStartTime(), subtackFirstTime.getStartTime(), "Время начала должно быть минимальным временем начала дочерней подзадачи");
    }

    @Test
    public void EpicDurationTest(){
        manager.addEpic(epic);
        assertEquals(0,epic.getDuration(),"Время продолжения эпика без задач должно быть равно нулю");

        Subtack subtack20Duration = new Subtack("title", "description", Progress.NEW, 20,LocalDateTime.of(2000,1,1,1,1),epic.getId());
        Subtack subtack32Duration = new Subtack("title", "description", Progress.NEW, 32,LocalDateTime.of(2002, 2, 2, 2, 1),epic.getId());
        manager.addSubtack(subtack20Duration);
        manager.addSubtack(subtack32Duration);

        int finalDurationEpic = subtack20Duration.getDuration() + subtack32Duration.getDuration();
        assertEquals(finalDurationEpic, epic.getDuration(),"продолжительность эпика должна быть - суммарной продолжительностью всех подзадач.");
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
        Subtack subtackNew = new Subtack("title", "description", Progress.NEW,20,LocalDateTime.of(2022, 1, 1, 1, 1),epic.getId());
        Subtack subtackDone = new Subtack("title", "description", Progress.DONE,20,LocalDateTime.of(2022, 3, 1, 1, 1),epic.getId());
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